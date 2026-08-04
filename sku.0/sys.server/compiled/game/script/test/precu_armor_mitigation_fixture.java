package script.test;

import script.combat_engine.hit_result;
import script.combat_engine.weapon_data;
import script.library.armor;
import script.library.combat;
import script.library.utils;
import script.obj_id;

/**
 * Identity-bound, reversible fixture for Publish 14 armor/food mitigation.
 *
 * The fixture equips a real LIGHT helmet with exactly 20 percent energy
 * protection, arms the retained food.mitigate_damage effect at 25 percent,
 * and exposes both a deterministic production-helper probe and live combat
 * diagnostics. It never fabricates player damage.
 */
public class precu_armor_mitigation_fixture extends script.base_script
{
    private static final long ATTACKER_OID = 44003778L;
    private static final int ATTACKER_STATION_ID = 91001;
    private static final long DEFENDER_OID = 39008597L;
    private static final int DEFENDER_STATION_ID = 1001;
    private static final String ROOT = "precu.p14.armorMitigationFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PEER = ROOT + ".peer";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String FIXTURE_ARMOR = ROOT + ".fixtureArmor";
    private static final String ORIGINAL_HAT_PRESENT = ROOT + ".originalHatPresent";
    private static final String ORIGINAL_HAT = ROOT + ".originalHat";
    private static final String ORIGINAL_FOOD_EFF_PRESENT =
        ROOT + ".originalFoodEffPresent";
    private static final String ORIGINAL_FOOD_EFF = ROOT + ".originalFoodEff";
    private static final String ORIGINAL_FOOD_DUR_PRESENT =
        ROOT + ".originalFoodDurPresent";
    private static final String ORIGINAL_FOOD_DUR = ROOT + ".originalFoodDur";
    private static final String DIAGNOSTIC_ROOT =
        "precu.p14.marksmanTier1Fixture.liveDiagnostic";
    private static final String DIAGNOSTIC_ENABLED = DIAGNOSTIC_ROOT + ".enabled";
    private static final String FOOD_ROOT = "food.mitigate_damage";
    private static final String FOOD_EFF = FOOD_ROOT + ".eff";
    private static final String FOOD_DUR = FOOD_ROOT + ".dur";
    private static final String ARMOR_TEMPLATE =
        "object/tangible/wearables/armor/bone/armor_bone_s01_helmet.iff";
    private static final String ARMOR_TRANSFER_SCRIPT =
        "item.armor.new_armor";
    private static final String USAGE =
        "usage: inspect|recover|prepare|status|probe1000|armLive|cleanup " +
        "44003778 39008597 <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args =
            params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 4 || !isValidLifecycle(args[3]))
        {
            return USAGE;
        }

        long attackerValue;
        long defenderValue;
        try
        {
            attackerValue = Long.parseLong(args[1]);
            defenderValue = Long.parseLong(args[2]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (attackerValue != ATTACKER_OID || defenderValue != DEFENDER_OID ||
            attackerValue == defenderValue)
        {
            return "error=identityNotAllowed";
        }

        obj_id attacker = obj_id.getObjId(attackerValue);
        obj_id defender = obj_id.getObjId(defenderValue);
        String validation =
            validatePlayer(attacker, ATTACKER_STATION_ID, "attacker");
        if (validation != null)
        {
            return validation;
        }
        validation = validatePlayer(defender, DEFENDER_STATION_ID, "defender");
        if (validation != null)
        {
            return validation;
        }

        String action = args[0];
        String lifecycle = args[3];
        if (action.equalsIgnoreCase("inspect"))
        {
            return "action=inspect " + buildStatus(attacker, defender, lifecycle);
        }
        if (action.equalsIgnoreCase("recover"))
        {
            return recover(attacker, defender, lifecycle);
        }
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(attacker, defender, lifecycle);
        }
        if (action.equalsIgnoreCase("status"))
        {
            validation = validateOwnership(attacker, defender, lifecycle);
            return validation == null ?
                "action=status " + buildStatus(attacker, defender, lifecycle) :
                validation;
        }
        if (action.equalsIgnoreCase("probe1000"))
        {
            return probe1000(attacker, defender, lifecycle);
        }
        if (action.equalsIgnoreCase("armLive"))
        {
            return armLive(attacker, defender, lifecycle);
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(attacker, defender, lifecycle);
        }
        return USAGE;
    }

    private String prepare(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle);
        if (ownership == null)
        {
            if (getIntObjVar(attacker, PREPARED) == 1 &&
                getIntObjVar(defender, PREPARED) == 1 &&
                reassertPreparedState(attacker, defender))
            {
                return "action=prepare resumed=true " +
                    buildStatus(attacker, defender, lifecycle);
            }
            return "error=fixturePartial";
        }
        if (!ownership.equals("fixtureAbsent"))
        {
            return ownership;
        }

        obj_id inventory = getObjectInSlot(defender, "inventory");
        if (!isIdValid(inventory))
        {
            return "error=inventoryUnavailable";
        }
        obj_id originalHat = getObjectInSlot(defender, "hat");
        snapshot(attacker, defender, lifecycle);
        snapshot(defender, attacker, lifecycle);
        setObjVar(defender, ORIGINAL_HAT_PRESENT, isIdValid(originalHat) ? 1 : 0);
        if (isIdValid(originalHat))
        {
            setObjVar(defender, ORIGINAL_HAT, originalHat);
        }
        setObjVar(
            defender,
            ORIGINAL_FOOD_EFF_PRESENT,
            utils.hasScriptVar(defender, FOOD_EFF) ? 1 : 0);
        if (utils.hasScriptVar(defender, FOOD_EFF))
        {
            setObjVar(
                defender,
                ORIGINAL_FOOD_EFF,
                utils.getIntScriptVar(defender, FOOD_EFF));
        }
        setObjVar(
            defender,
            ORIGINAL_FOOD_DUR_PRESENT,
            utils.hasScriptVar(defender, FOOD_DUR) ? 1 : 0);
        if (utils.hasScriptVar(defender, FOOD_DUR))
        {
            setObjVar(
                defender,
                ORIGINAL_FOOD_DUR,
                utils.getIntScriptVar(defender, FOOD_DUR));
        }

        if (isIdValid(originalHat) && !putInOverloaded(originalHat, inventory))
        {
            recover(attacker, defender, lifecycle);
            return "error=originalHatMoveFailed";
        }
        obj_id fixtureArmor = createObject(ARMOR_TEMPLATE, inventory, "");
        if (!isIdValid(fixtureArmor) ||
            !armor.setAbsoluteArmorData(
                fixtureArmor, AL_basic, AC_battle, 2000, 1000))
        {
            if (isIdValid(fixtureArmor))
            {
                destroyObject(fixtureArmor);
            }
            recover(attacker, defender, lifecycle);
            return "error=fixtureArmorCreationFailed";
        }
        setObjVar(defender, FIXTURE_ARMOR, fixtureArmor);
        if (!equipFixtureArmor(fixtureArmor, defender))
        {
            recover(attacker, defender, lifecycle);
            return "error=fixtureArmorEquipFailed";
        }

        setObjVar(attacker, PREPARED, 1);
        setObjVar(defender, PREPARED, 1);
        if (!reassertPreparedState(attacker, defender))
        {
            recover(attacker, defender, lifecycle);
            return "error=fixturePreparationFailed";
        }
        return "action=prepare resumed=false " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String probe1000(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!reassertPreparedState(attacker, defender))
        {
            return "error=fixtureReassertionFailed";
        }

        obj_id fixtureArmor = getObjIdObjVar(defender, FIXTURE_ARMOR);
        setHitpoints(fixtureArmor, 1000);
        int conditionBefore = getHitpoints(fixtureArmor);
        utils.setScriptVar(defender, FOOD_EFF, 25);
        utils.setScriptVar(defender, FOOD_DUR, 10);

        weapon_data weaponData = new weapon_data();
        weaponData.damageType = DAMAGE_ENERGY;
        weaponData.elementalType = DAMAGE_ENERGY;
        weaponData.elementalValue = 0;
        hit_result hitData = new hit_result();
        hitData.success = true;
        hitData.hitLocation =
            combat.selectPrecuHitLocationForPool(2);
        hitData.damage = 1000;
        int armorMitigated =
            combat.applyPrecuArmorProtection(defender, weaponData, hitData, 0);
        int postArmorDamage = hitData.damage + hitData.elementalDamage;
        int foodMitigated =
            combat.applyPrecuFoodMitigation(defender, hitData);
        int finalDamage = hitData.damage + hitData.elementalDamage;
        int conditionAfter = getHitpoints(fixtureArmor);
        int foodDurationAfter = utils.hasScriptVar(defender, FOOD_DUR) ?
            utils.getIntScriptVar(defender, FOOD_DUR) : 0;

        return "action=probe1000 rawDamage=1000 hitLocation=" +
            hitData.hitLocation +
            " armorPiercing=0 armorRating=" +
            combat.getPrecuArmorRating(fixtureArmor) +
            " protection=" +
            combat.getPrecuArmorObjectProtection(
                fixtureArmor, DAMAGE_ENERGY, false) +
            " armorMitigated=" + armorMitigated +
            " postArmorDamage=" + postArmorDamage +
            " foodEffectiveness=25 foodMitigated=" + foodMitigated +
            " finalDamage=" + finalDamage +
            " conditionBefore=" + conditionBefore +
            " conditionAfter=" + conditionAfter +
            " conditionDelta=" + (conditionBefore - conditionAfter) +
            " foodDurationBefore=10 foodDurationAfter=" + foodDurationAfter +
            " expectedPostArmorDamage=400 expectedFinalDamage=300 " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String armLive(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!reassertPreparedState(attacker, defender))
        {
            return "error=fixtureReassertionFailed";
        }
        obj_id fixtureArmor = getObjIdObjVar(defender, FIXTURE_ARMOR);
        setHitpoints(fixtureArmor, 1000);
        utils.setScriptVar(defender, FOOD_EFF, 25);
        utils.setScriptVar(defender, FOOD_DUR, 20);
        if (hasObjVar(attacker, DIAGNOSTIC_ROOT))
        {
            removeObjVar(attacker, DIAGNOSTIC_ROOT);
        }
        setObjVar(attacker, DIAGNOSTIC_ENABLED, 1);
        setAttrib(defender, MIND, getMaxAttrib(defender, MIND));
        return "action=armLive " + buildStatus(attacker, defender, lifecycle);
    }

    private String cleanup(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        if (!hasObjVar(attacker, ROOT) && !hasObjVar(defender, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        String ownership = validateOwnership(attacker, defender, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        boolean restored = restore(attacker, defender);
        return restored ?
            "action=cleanup alreadyClean=false restored=true" :
            "error=cleanupRestoreFailed";
    }

    private String recover(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        if (!hasObjVar(attacker, ROOT) && !hasObjVar(defender, ROOT))
        {
            return "action=recover alreadyClean=true restored=true";
        }
        if ((hasObjVar(attacker, LIFECYCLE) &&
                !lifecycle.equals(getStringObjVar(attacker, LIFECYCLE))) ||
            (hasObjVar(defender, LIFECYCLE) &&
                !lifecycle.equals(getStringObjVar(defender, LIFECYCLE))))
        {
            return "error=lifecycleMismatch";
        }
        boolean restored = restore(attacker, defender);
        return restored ?
            "action=recover alreadyClean=false restored=true" :
            "error=recoveryFailed";
    }

    private boolean restore(obj_id attacker, obj_id defender)
        throws InterruptedException
    {
        boolean restored = true;
        obj_id inventory = getObjectInSlot(defender, "inventory");
        obj_id fixtureArmor = hasObjVar(defender, FIXTURE_ARMOR) ?
            getObjIdObjVar(defender, FIXTURE_ARMOR) : obj_id.NULL_ID;
        if (isIdValid(fixtureArmor))
        {
            if (getObjectInSlot(defender, "hat") == fixtureArmor &&
                isIdValid(inventory))
            {
                restored &= putInOverloaded(fixtureArmor, inventory);
            }
            destroyObject(fixtureArmor);
        }

        if (getIntObjVar(defender, ORIGINAL_HAT_PRESENT) == 1 &&
            hasObjVar(defender, ORIGINAL_HAT))
        {
            obj_id originalHat = getObjIdObjVar(defender, ORIGINAL_HAT);
            if (isIdValid(originalHat) && originalHat.isLoaded())
            {
                restored &= equipOverride(originalHat, defender) &&
                    getObjectInSlot(defender, "hat") == originalHat;
            }
        }
        if (getIntObjVar(defender, ORIGINAL_FOOD_EFF_PRESENT) == 1 &&
            hasObjVar(defender, ORIGINAL_FOOD_EFF))
        {
            utils.setScriptVar(
                defender, FOOD_EFF, getIntObjVar(defender, ORIGINAL_FOOD_EFF));
        }
        else
        {
            utils.removeScriptVar(defender, FOOD_EFF);
        }
        if (getIntObjVar(defender, ORIGINAL_FOOD_DUR_PRESENT) == 1 &&
            hasObjVar(defender, ORIGINAL_FOOD_DUR))
        {
            utils.setScriptVar(
                defender, FOOD_DUR, getIntObjVar(defender, ORIGINAL_FOOD_DUR));
        }
        else
        {
            utils.removeScriptVar(defender, FOOD_DUR);
        }
        removeObjVar(attacker, ROOT);
        removeObjVar(defender, ROOT);
        return restored;
    }

    private void snapshot(obj_id owner, obj_id peer, String lifecycle)
        throws InterruptedException
    {
        setObjVar(owner, LIFECYCLE, lifecycle);
        setObjVar(owner, PEER, peer);
    }

    private boolean reassertPreparedState(obj_id attacker, obj_id defender)
        throws InterruptedException
    {
        if (!hasObjVar(defender, FIXTURE_ARMOR))
        {
            return false;
        }
        obj_id fixtureArmor = getObjIdObjVar(defender, FIXTURE_ARMOR);
        if (!isIdValid(fixtureArmor) || !fixtureArmor.isLoaded())
        {
            return false;
        }
        if (getObjectInSlot(defender, "hat") != fixtureArmor &&
            !equipFixtureArmor(fixtureArmor, defender))
        {
            return false;
        }
        utils.setScriptVar(defender, FOOD_EFF, 25);
        if (!utils.hasScriptVar(defender, FOOD_DUR))
        {
            utils.setScriptVar(defender, FOOD_DUR, 20);
        }
        setObjVar(attacker, DIAGNOSTIC_ENABLED, 1);
        return armor.getArmorLevel(fixtureArmor) == AL_basic &&
            combat.getPrecuArmorRating(fixtureArmor) == 1;
    }

    /**
     * NGE's armor script rejects transfers to an uncertified player in
     * OnAboutToBeTransferred, including test-owned equipOverride transfers.
     * The fixture bypasses only that transfer callback, then restores the
     * production armor script before it accepts the equipped object.
     */
    private boolean equipFixtureArmor(obj_id fixtureArmor, obj_id defender)
        throws InterruptedException
    {
        boolean armorScriptAttached =
            hasScript(fixtureArmor, ARMOR_TRANSFER_SCRIPT);
        if (armorScriptAttached)
        {
            detachScript(fixtureArmor, ARMOR_TRANSFER_SCRIPT);
        }
        boolean transferReady =
            !armorScriptAttached ||
            !hasScript(fixtureArmor, ARMOR_TRANSFER_SCRIPT);
        boolean equipped =
            transferReady &&
            equipOverride(fixtureArmor, defender) &&
            getObjectInSlot(defender, "hat") == fixtureArmor;
        if (armorScriptAttached)
        {
            attachScript(fixtureArmor, ARMOR_TRANSFER_SCRIPT);
        }
        boolean scriptRestored =
            !armorScriptAttached ||
            hasScript(fixtureArmor, ARMOR_TRANSFER_SCRIPT);
        return equipped && scriptRestored;
    }

    private String validateOwnership(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        boolean attackerRoot = hasObjVar(attacker, ROOT);
        boolean defenderRoot = hasObjVar(defender, ROOT);
        if (!attackerRoot && !defenderRoot)
        {
            return "fixtureAbsent";
        }
        if (!attackerRoot || !defenderRoot ||
            !hasObjVar(attacker, LIFECYCLE) ||
            !hasObjVar(defender, LIFECYCLE) ||
            !hasObjVar(attacker, PEER) ||
            !hasObjVar(defender, PEER))
        {
            return "error=fixturePartial";
        }
        if (!lifecycle.equals(getStringObjVar(attacker, LIFECYCLE)) ||
            !lifecycle.equals(getStringObjVar(defender, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (getObjIdObjVar(attacker, PEER) != defender ||
            getObjIdObjVar(defender, PEER) != attacker)
        {
            return "error=peerMismatch";
        }
        return null;
    }

    private String validatePlayer(obj_id player, int stationId, String role)
        throws InterruptedException
    {
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=" + role + "NotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != stationId)
        {
            return "error=" + role + "NotAuthoritative";
        }
        return null;
    }

    private String buildStatus(
        obj_id attacker,
        obj_id defender,
        String lifecycle) throws InterruptedException
    {
        obj_id fixtureArmor = hasObjVar(defender, FIXTURE_ARMOR) ?
            getObjIdObjVar(defender, FIXTURE_ARMOR) : obj_id.NULL_ID;
        boolean fixtureArmorValid =
            isIdValid(fixtureArmor) && fixtureArmor.isLoaded();
        return "lifecycle=" + lifecycle +
            " prepared=" +
                (getIntObjVar(attacker, PREPARED) == 1 &&
                    getIntObjVar(defender, PREPARED) == 1) +
            " fixtureArmor=" +
                (fixtureArmorValid ? fixtureArmor.toString() : "none") +
            " fixtureArmorEquipped=" +
                (fixtureArmorValid &&
                    getObjectInSlot(defender, "hat") == fixtureArmor) +
            " armorLevel=" +
                (fixtureArmorValid ? armor.getArmorLevel(fixtureArmor) : -1) +
            " armorRating=" +
                (fixtureArmorValid ?
                    combat.getPrecuArmorRating(fixtureArmor) : -1) +
            " energyProtection=" +
                (fixtureArmorValid ?
                    combat.getPrecuArmorObjectProtection(
                        fixtureArmor, DAMAGE_ENERGY, false) : -1.0f) +
            " armorCondition=" +
                (fixtureArmorValid ? getHitpoints(fixtureArmor) : -1) +
            " foodEffectiveness=" +
                (utils.hasScriptVar(defender, FOOD_EFF) ?
                    utils.getIntScriptVar(defender, FOOD_EFF) : -1) +
            " foodDuration=" +
                (utils.hasScriptVar(defender, FOOD_DUR) ?
                    utils.getIntScriptVar(defender, FOOD_DUR) : -1) +
            " defenderMind=" + getAttrib(defender, MIND) +
            " defenderMaxMind=" + getMaxAttrib(defender, MIND) +
            " diagnosticEnabled=" +
                readDiagnosticInt(attacker, "enabled", 0) +
            " diagnosticRawDamage=" +
                readDiagnosticInt(attacker, "armor.rawDamage", -1) +
            " diagnosticHitLocation=" +
                readDiagnosticInt(attacker, "armor.hitLocation", -1) +
            " diagnosticArmorPiercing=" +
                readDiagnosticInt(attacker, "armor.piercing", -1) +
            " diagnosticArmorRating=" +
                readDiagnosticInt(attacker, "armor.rating", -1) +
            " diagnosticArmorProtection=" +
                readDiagnosticFloat(attacker, "armor.protection", -1.0f) +
            " diagnosticPostArmorDamage=" +
                readDiagnosticInt(attacker, "armor.postArmorDamage", -1) +
            " diagnosticFoodMitigated=" +
                readDiagnosticInt(attacker, "armor.foodMitigated", -1) +
            " diagnosticFinalDamage=" +
                readDiagnosticInt(attacker, "armor.finalDamage", -1);
    }

    private int readDiagnosticInt(
        obj_id attacker, String leaf, int fallback) throws InterruptedException
    {
        String objvar = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(attacker, objvar) ?
            getIntObjVar(attacker, objvar) : fallback;
    }

    private float readDiagnosticFloat(
        obj_id attacker, String leaf, float fallback) throws InterruptedException
    {
        String objvar = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(attacker, objvar) ?
            getFloatObjVar(attacker, objvar) : fallback;
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        return lifecycle != null && lifecycle.matches("[a-f0-9]{32}");
    }
}
