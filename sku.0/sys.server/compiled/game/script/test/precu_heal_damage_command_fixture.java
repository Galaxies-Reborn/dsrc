package script.test;

import script.attrib_mod;
import script.dictionary;
import script.library.consumable;
import script.library.create;
import script.library.healing;
import script.library.utils;
import script.library.xp;
import script.location;
import script.obj_id;

/**
 * Identity-bound ServerConsole fixture for the Publish 14.1 healDamage
 * player command.
 *
 * The fixture creates only disposable patient and medicine objects. It never
 * queues a command; the connected client remains the sole queue owner.
 */
public class precu_heal_damage_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final int TEST_MAX = 1000;
    private static final int START_HEALTH = 500;
    private static final int START_ACTION = 550;
    private static final int START_MIND = 600;
    private static final int MEDICINE_POWER = 200;
    private static final int QUEUE_SAFETY_SECONDS = 6;
    private static final String MEDIC_NOVICE = "science_medic_novice";
    private static final String MEDIC_INJURY_ONE =
        "science_medic_injury_01";
    private static final String COMMAND = "healDamage";
    private static final String MEDICINE_TEMPLATE =
        "object/tangible/medicine/crafted/crafted_stimpack_sm_s1_a.iff";
    private static final String ROOT = "precu.healDamageCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ITEM = ROOT + ".item";
    private static final String PATIENT = ROOT + ".patient";
    private static final String ORIGINAL_NOVICE = ROOT + ".originalNovice";
    private static final String ORIGINAL_INJURY_ONE =
        ROOT + ".originalInjuryOne";
    private static final String ORIGINAL_COMMAND = ROOT + ".originalCommand";
    private static final String ORIGINAL_COOLDOWN_PRESENT =
        ROOT + ".originalCooldownPresent";
    private static final String ORIGINAL_COOLDOWN = ROOT + ".originalCooldown";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String EXPECTED_COST = ROOT + ".expectedCost";
    private static final String EXPECTED_ROUND_TIME =
        ROOT + ".expectedRoundTime";
    private static final String BEFORE_HEALTH = ROOT + ".beforeHealth";
    private static final String BEFORE_ACTION = ROOT + ".beforeAction";
    private static final String BEFORE_MIND_POOL = ROOT + ".beforeMindPool";
    private static final String BEFORE_HEALER_MIND =
        ROOT + ".beforeHealerMind";
    private static final String BEFORE_CHARGES = ROOT + ".beforeCharges";
    private static final String SECOND_HEALTH = ROOT + ".secondHealth";
    private static final String SECOND_ACTION = ROOT + ".secondAction";
    private static final String SECOND_MIND_POOL = ROOT + ".secondMindPool";
    private static final String SECOND_HEALER_MIND =
        ROOT + ".secondHealerMind";
    private static final String SECOND_CHARGES = ROOT + ".secondCharges";
    private static final String LAST_QUEUE_TIME = ROOT + ".lastQueueTime";
    private static final String USAGE =
        "usage: prepare|markQueue|status|markCooldown|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args =
            params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 3)
        {
            return USAGE;
        }
        long playerValue;
        try
        {
            playerValue = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID || !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        String action = args[0];
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (action.equalsIgnoreCase("markQueue"))
        {
            return markQueue(player, args[2], false);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (action.equalsIgnoreCase("markCooldown"))
        {
            return markQueue(player, args[2], true);
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, args[2]);
        }
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }

        location current = getLocation(player);
        if (current == null)
        {
            return "error=locationUnavailable";
        }
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_NOVICE,
            hasSkill(player, MEDIC_NOVICE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_INJURY_ONE,
            hasSkill(player, MEDIC_INJURY_ONE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COMMAND,
            hasCommand(player, COMMAND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN_PRESENT,
            hasObjVar(player, healing.VAR_HEALING_CAN_HEALDAMAGE)
                ? 1
                : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN,
            hasObjVar(player, healing.VAR_HEALING_CAN_HEALDAMAGE)
                ? getIntObjVar(
                    player,
                    healing.VAR_HEALING_CAN_HEALDAMAGE)
                : 0);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, xp.MEDICAL));

        dictionary patientData =
            utils.dataTableGetRow(create.CREATURE_TABLE, "bantha");
        obj_id patient = patientData == null
            ? obj_id.NULL_ID
            : createObject(
                create.TEMPLATE_PREFIX +
                    patientData.getString("template"),
                current);
        if (!isIdValid(patient) || !patient.isAuthoritative())
        {
            if (isIdValid(patient))
            {
                destroyObject(patient);
            }
            removeObjVar(player, ROOT);
            return "error=patientCreationFailed";
        }
        setCreatureName(patient, "bantha");
        setObjVar(patient, "creature_type", "bantha");
        setMaster(patient, player);
        setObjVar(patient, "ai.pet", 1);
        setObjVar(player, PATIENT, patient);

        boolean skillReady =
            hasSkill(player, MEDIC_NOVICE) ||
            grantSkill(player, MEDIC_NOVICE);
        skillReady =
            skillReady &&
            (hasSkill(player, MEDIC_INJURY_ONE) ||
                grantSkill(player, MEDIC_INJURY_ONE));
        if (!skillReady || !hasCommand(player, COMMAND) ||
            getSkillStatMod(player, "healing_injury_treatment") <= 0)
        {
            boolean restored = restore(player);
            return "error=medicPreparationFailed restored=" + restored;
        }

        healing.setCanHealDamage(player, 0);
        if (!setPatientPool(patient, HEALTH, START_HEALTH) ||
            !setPatientPool(patient, ACTION, START_ACTION) ||
            !setPatientPool(patient, MIND, START_MIND) ||
            !setShockWound(patient, 0) ||
            !setShockWound(player, 0) ||
            !setAttrib(player, MIND, getMaxAttrib(player, MIND)))
        {
            boolean restored = restore(player);
            return "error=hamPreparationFailed restored=" + restored;
        }

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id medicine = isIdValid(inventory)
            ? createObject(MEDICINE_TEMPLATE, inventory, "")
            : obj_id.NULL_ID;
        if (!isIdValid(medicine))
        {
            boolean restored = restore(player);
            return "error=medicineCreationFailed restored=" + restored;
        }
        setObjVar(player, ITEM, medicine);
        attrib_mod[] modifiers = new attrib_mod[3];
        modifiers[0] =
            utils.createHealDamageAttribMod(HEALTH, MEDICINE_POWER);
        modifiers[1] =
            utils.createHealDamageAttribMod(ACTION, MEDICINE_POWER);
        modifiers[2] =
            utils.createHealDamageAttribMod(MIND, MEDICINE_POWER);
        setObjVar(medicine, consumable.VAR_CONSUMABLE_MODS, modifiers);
        setObjVar(medicine, consumable.VAR_CONSUMABLE_MEDICINE, true);
        setObjVar(
            medicine,
            consumable.VAR_CONSUMABLE_STOMACH_VALUES,
            new int[] { 0, 0, 0 });
        setObjVar(
            medicine,
            consumable.VAR_SKILL_MOD_REQUIRED,
            new String[] { "healing_ability" });
        setObjVar(
            medicine,
            consumable.VAR_SKILL_MOD_MIN,
            new int[] { 0 });
        setCount(medicine, 2);

        setObjVar(
            player,
            EXPECTED_COST,
            healing.getMedicalMindCost(
                player,
                healing.VAR_HEALDAMAGE_COST));
        setObjVar(
            player,
            EXPECTED_ROUND_TIME,
            healing.getHealDamageRoundTime(player));
        markBefore(player, false);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String markQueue(
        obj_id player,
        String lifecycle,
        boolean cooldownProbe)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() - getIntObjVar(player, LAST_QUEUE_TIME) <
                QUEUE_SAFETY_SECONDS &&
            !(cooldownProbe && getCurrentCommand(player) != 0))
        {
            return "error=priorQueuePending " + buildStatus(player);
        }
        markBefore(player, cooldownProbe);
        setObjVar(player, LAST_QUEUE_TIME, getGameTime());
        return "action=" +
            (cooldownProbe ? "markCooldown" : "markQueue") +
            " executeTime=5 pendingClientQueue=true " +
            buildStatus(player);
    }

    private void markBefore(obj_id player, boolean cooldownProbe)
        throws InterruptedException
    {
        obj_id medicine = getObjIdObjVar(player, ITEM);
        obj_id patient = getObjIdObjVar(player, PATIENT);
        String healthKey =
            cooldownProbe ? SECOND_HEALTH : BEFORE_HEALTH;
        String actionKey =
            cooldownProbe ? SECOND_ACTION : BEFORE_ACTION;
        String mindPoolKey =
            cooldownProbe ? SECOND_MIND_POOL : BEFORE_MIND_POOL;
        String healerMindKey =
            cooldownProbe ? SECOND_HEALER_MIND : BEFORE_HEALER_MIND;
        String chargesKey =
            cooldownProbe ? SECOND_CHARGES : BEFORE_CHARGES;
        setObjVar(player, healthKey, getAttrib(patient, HEALTH));
        setObjVar(player, actionKey, getAttrib(patient, ACTION));
        setObjVar(player, mindPoolKey, getAttrib(patient, MIND));
        setObjVar(player, healerMindKey, getAttrib(player, MIND));
        setObjVar(player, chargesKey, getCount(medicine));
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null
            ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() - getIntObjVar(player, LAST_QUEUE_TIME) <
                QUEUE_SAFETY_SECONDS)
        {
            return "error=queuePendingPotential " + buildStatus(player);
        }
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        boolean complete =
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_SHOCK) &&
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_INJURY_ONE) &&
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_COOLDOWN) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP);
        if (!complete)
        {
            return false;
        }
        if (hasObjVar(player, ITEM))
        {
            obj_id medicine = getObjIdObjVar(player, ITEM);
            if (isIdValid(medicine) && medicine.isLoaded())
            {
                destroyObject(medicine);
            }
        }
        boolean restored =
            setAttrib(
                player,
                MIND,
                getIntObjVar(player, ORIGINAL_MIND));
        restored =
            setShockWound(
                player,
                getIntObjVar(player, ORIGINAL_SHOCK)) &&
            restored;
        int originalXp = getIntObjVar(player, ORIGINAL_MEDICAL_XP);
        int currentXp = getExperiencePoints(player, xp.MEDICAL);
        if (currentXp != originalXp)
        {
            grantExperiencePoints(
                player,
                xp.MEDICAL,
                originalXp - currentXp);
        }
        restored =
            getExperiencePoints(player, xp.MEDICAL) == originalXp &&
            restored;

        if (getIntObjVar(player, ORIGINAL_INJURY_ONE) == 0 &&
            hasSkill(player, MEDIC_INJURY_ONE))
        {
            revokeSkill(player, MEDIC_INJURY_ONE);
        }
        if (getIntObjVar(player, ORIGINAL_NOVICE) == 0 &&
            hasSkill(player, MEDIC_NOVICE))
        {
            revokeSkill(player, MEDIC_NOVICE);
        }
        if (getIntObjVar(player, ORIGINAL_COMMAND) == 1 &&
            !hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        else if (getIntObjVar(player, ORIGINAL_COMMAND) == 0 &&
            hasCommand(player, COMMAND))
        {
            revokeCommand(player, COMMAND);
        }
        if (getIntObjVar(player, ORIGINAL_COOLDOWN_PRESENT) == 1)
        {
            setObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALDAMAGE,
                getIntObjVar(player, ORIGINAL_COOLDOWN));
        }
        else if (hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALDAMAGE))
        {
            removeObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALDAMAGE);
        }
        if (hasObjVar(player, PATIENT))
        {
            obj_id patient = getObjIdObjVar(player, PATIENT);
            if (isIdValid(patient) && patient.isLoaded())
            {
                destroyObject(patient);
            }
        }
        removeObjVar(player, ROOT);
        return restored;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, ITEM) ||
            !hasObjVar(player, PATIENT))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        obj_id medicine = getObjIdObjVar(player, ITEM);
        obj_id patient = getObjIdObjVar(player, PATIENT);
        if (!isIdValid(medicine) ||
            utils.getContainingPlayer(medicine) != player)
        {
            return "error=fixtureMedicineUnavailable";
        }
        if (!isIdValid(patient) || !patient.isLoaded() ||
            getMaster(patient) != player)
        {
            return "error=fixturePatientUnavailable";
        }
        return null;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id medicine = hasObjVar(player, ITEM)
            ? getObjIdObjVar(player, ITEM)
            : obj_id.NULL_ID;
        obj_id patient = hasObjVar(player, PATIENT)
            ? getObjIdObjVar(player, PATIENT)
            : obj_id.NULL_ID;
        int health = isIdValid(patient)
            ? getAttrib(patient, HEALTH)
            : -1;
        int action = isIdValid(patient)
            ? getAttrib(patient, ACTION)
            : -1;
        int mindPool = isIdValid(patient)
            ? getAttrib(patient, MIND)
            : -1;
        int healerMind = getAttrib(player, MIND);
        int charges = isIdValid(medicine) ? getCount(medicine) : -1;
        int cooldown =
            hasObjVar(player, healing.VAR_HEALING_CAN_HEALDAMAGE)
                ? getIntObjVar(
                    player,
                    healing.VAR_HEALING_CAN_HEALDAMAGE) -
                    getGameTime()
                : 0;
        return "player=" + player +
            " patient=" + patient +
            " currentCommand=" + getCurrentCommand(player) +
            " novice=" + hasSkill(player, MEDIC_NOVICE) +
            " injuryOne=" + hasSkill(player, MEDIC_INJURY_ONE) +
            " command=" + hasCommand(player, COMMAND) +
            " treatment=" +
                getSkillStatMod(player, "healing_injury_treatment") +
            " health=" + health +
            " action=" + action +
            " mindPool=" + mindPool +
            " healerMind=" + healerMind +
            " focus=" + getAttrib(player, FOCUS) +
            " charges=" + charges +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " handlerOutcome=" +
                (hasObjVar(player, ROOT + ".handlerOutcome")
                    ? getStringObjVar(
                        player,
                        ROOT + ".handlerOutcome")
                    : "none") +
            " appliedHealthHeal=" +
                readInt(player, ".appliedHealthHeal") +
            " appliedActionHeal=" +
                readInt(player, ".appliedActionHeal") +
            " appliedMindHeal=" +
                readInt(player, ".appliedMindHeal") +
            " appliedMindCost=" +
                readInt(player, ".appliedMindCost") +
            " appliedChargeCost=" +
                readInt(player, ".appliedChargeCost") +
            " expectedMedicalXp=" +
                readInt(player, ".expectedMedicalXp") +
            " medicalXp=" + getExperiencePoints(player, xp.MEDICAL) +
            " medicalXpDelta=" +
                (hasObjVar(player, ORIGINAL_MEDICAL_XP)
                    ? getExperiencePoints(player, xp.MEDICAL) -
                        getIntObjVar(player, ORIGINAL_MEDICAL_XP)
                    : 0) +
            " expectedCost=" +
                (hasObjVar(player, EXPECTED_COST)
                    ? getIntObjVar(player, EXPECTED_COST)
                    : -1) +
            " cooldownRemaining=" + cooldown +
            " expectedRoundTime=" +
                (hasObjVar(player, EXPECTED_ROUND_TIME)
                    ? getIntObjVar(player, EXPECTED_ROUND_TIME)
                    : -1) +
            " healthDelta=" +
                delta(player, BEFORE_HEALTH, health) +
            " actionDelta=" +
                delta(player, BEFORE_ACTION, action) +
            " mindPoolDelta=" +
                delta(player, BEFORE_MIND_POOL, mindPool) +
            " healerMindDelta=" +
                reverseDelta(
                    player,
                    BEFORE_HEALER_MIND,
                    healerMind) +
            " chargeDelta=" +
                reverseDelta(player, BEFORE_CHARGES, charges) +
            " secondHealthDelta=" +
                delta(player, SECOND_HEALTH, health) +
            " secondActionDelta=" +
                delta(player, SECOND_ACTION, action) +
            " secondMindPoolDelta=" +
                delta(player, SECOND_MIND_POOL, mindPool) +
            " secondHealerMindDelta=" +
                reverseDelta(
                    player,
                    SECOND_HEALER_MIND,
                    healerMind) +
            " secondChargeDelta=" +
                reverseDelta(player, SECOND_CHARGES, charges);
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        return hasObjVar(player, ROOT + suffix)
            ? getIntObjVar(player, ROOT + suffix)
            : 0;
    }

    private int delta(obj_id player, String key, int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? current - getIntObjVar(player, key)
            : 0;
    }

    private int reverseDelta(obj_id player, String key, int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? getIntObjVar(player, key) - current
            : 0;
    }

    private boolean setPatientPool(
        obj_id patient,
        int attribute,
        int current)
        throws InterruptedException
    {
        int wound = getAttribWound(patient, attribute);
        if (wound > 0)
        {
            healWound(patient, attribute, wound);
        }
        return
            setMaxAttrib(patient, attribute, TEST_MAX) &&
            setAttrib(patient, attribute, current) &&
            getAttrib(patient, attribute) == current;
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        if (lifecycle == null || lifecycle.length() != 32)
        {
            return false;
        }
        for (int index = 0; index < lifecycle.length(); ++index)
        {
            char value = lifecycle.charAt(index);
            boolean digit = value >= '0' && value <= '9';
            boolean lowerHex = value >= 'a' && value <= 'f';
            if (!digit && !lowerHex)
            {
                return false;
            }
        }
        return true;
    }
}
