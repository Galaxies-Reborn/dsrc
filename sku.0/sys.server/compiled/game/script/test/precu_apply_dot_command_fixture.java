package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.create;
import script.library.dot;
import script.library.healing;
import script.library.skill;
import script.library.utils;
import script.library.consumable;

/** Identity-bound reversible fixture for both Publish 14.1 DOT-pack commands. */
public class precu_apply_dot_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String ROOT = "precu.applyDotCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String POISON_ITEM = ROOT + ".poisonItem";
    private static final String DISEASE_ITEM = ROOT + ".diseaseItem";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_MAX_MIND =
        ROOT + ".originalMaxMind";
    private static final String ORIGINAL_XP = ROOT + ".originalXp";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String POISON_COOLDOWN =
        "healing.can_apply_poison";
    private static final String DISEASE_COOLDOWN =
        "healing.can_apply_disease";
    private static final String ORIGINAL_POISON_COOLDOWN_PRESENT =
        ROOT + ".originalPoisonCooldownPresent";
    private static final String ORIGINAL_POISON_COOLDOWN =
        ROOT + ".originalPoisonCooldown";
    private static final String ORIGINAL_DISEASE_COOLDOWN_PRESENT =
        ROOT + ".originalDiseaseCooldownPresent";
    private static final String ORIGINAL_DISEASE_COOLDOWN =
        ROOT + ".originalDiseaseCooldown";
    private static final String POISON_ID = "precu_apply_poison_fixture";
    private static final String DISEASE_ID = "precu_apply_disease_fixture";
    private static final int DOT_POWER = 80;
    private static final int DOT_POTENCY = -1;
    private static final int DOT_DURATION = 600;
    private static final int MEDICINE_RANGE = 30;
    private static final String CREATURE_TYPE = "worrt";
    private static final String POISON_TEMPLATE =
        "object/tangible/medicine/crafted/medpack_poison_health_a.iff";
    private static final String DISEASE_TEMPLATE =
        "object/tangible/medicine/crafted/medpack_disease_health_a.iff";
    private static final String[] SKILLS =
    {
        "science_combatmedic",
        "science_combatmedic_novice",
        "science_combatmedic_healing_range_01",
        "science_combatmedic_healing_range_02"
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 3 || !isValidLifecycle(args[2]))
            return USAGE;
        long oid;
        try
        {
            oid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (oid != PLAYER_OID)
            return "error=playerIdentityRejected";
        obj_id player = obj_id.getObjId(oid);
        if (!isAuthoritativePlayer(player))
            return "error=playerUnavailable";
        if (args[0].equalsIgnoreCase("prepare"))
            return prepare(player, args[2]);
        if (args[0].equalsIgnoreCase("status"))
            return status(player, args[2]);
        if (args[0].equalsIgnoreCase("cleanup"))
            return cleanup(player, args[2]);
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            if (ownership == null && getIntObjVar(player, PREPARED) == 1)
                return "action=prepare resumed=true " + buildStatus(player);
            return ownership == null ? "error=fixtureNotPrepared" : ownership;
        }
        if (hasAnyFixtureSkill(player) || hasCommand(player, "applyPoison") ||
            hasCommand(player, "applyDisease"))
            return "error=fixtureVectorAlreadyOwned";

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_MAX_MIND, getMaxAttrib(player, MIND));
        setObjVar(player, ORIGINAL_XP,
            getExperiencePoints(player, "medical"));
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        snapshotCooldown(player, POISON_COOLDOWN,
            ORIGINAL_POISON_COOLDOWN_PRESENT, ORIGINAL_POISON_COOLDOWN);
        snapshotCooldown(player, DISEASE_COOLDOWN,
            ORIGINAL_DISEASE_COOLDOWN_PRESENT, ORIGINAL_DISEASE_COOLDOWN);
        resetTelemetry(player);

        if (!grantSkills(player) || !hasCommand(player, "applyPoison") ||
            !hasCommand(player, "applyDisease"))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        removeIfPresent(player, POISON_COOLDOWN);
        removeIfPresent(player, DISEASE_COOLDOWN);
        setMaxAttrib(player, MIND,
            Math.max(500, getMaxAttrib(player, MIND)));
        if (!setAttrib(player, MIND, getMaxAttrib(player, MIND)) ||
            getAttrib(player, MIND) <=
                healing.getCombatMedicMindCost(player, 150))
        {
            boolean restored = restore(player);
            return "error=mindPreparationFailed restored=" + restored;
        }

        location targetLocation = new location(getLocation(player));
        targetLocation.x += 3.0f;
        obj_id target = createFixtureTarget(targetLocation);
        if (!isIdValid(target) || !target.isLoaded())
        {
            boolean restored = restore(player);
            return "error=targetPreparationFailed restored=" + restored;
        }
        setObjVar(player, TARGET, target);
        setMaxAttrib(target, HEALTH, 10000);
        setAttrib(target, HEALTH, 10000);

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id poison = createMedicine(inventory, POISON_TEMPLATE,
            dot.DOT_POISON, POISON_ID);
        obj_id disease = createMedicine(inventory, DISEASE_TEMPLATE,
            dot.DOT_DISEASE, DISEASE_ID);
        if (!isIdValid(poison) || !isIdValid(disease))
        {
            if (isIdValid(poison)) destroyObject(poison);
            if (isIdValid(disease)) destroyObject(disease);
            boolean restored = restore(player);
            return "error=medicinePreparationFailed restored=" + restored;
        }
        setObjVar(player, POISON_ITEM, poison);
        setObjVar(player, DISEASE_ITEM, disease);
        setObjVar(player, ROOT + ".mindBefore", getAttrib(player, MIND));
        setObjVar(player, ROOT + ".xpBefore",
            getExperiencePoints(player, "medical"));
        setObjVar(player, ROOT + ".expectedMindCost",
            healing.getCombatMedicMindCost(player, 150));
        setObjVar(player, ROOT + ".expectedStrength",
            Math.round(DOT_POWER * (1.0f +
                getSkillStatMod(player, "combat_medic_effectiveness") /
                    100.0f)));
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private obj_id createMedicine(obj_id inventory, String template,
        String type, String id) throws InterruptedException
    {
        if (!isIdValid(inventory))
            return obj_id.NULL_ID;
        obj_id medicine = createObject(template, inventory, "");
        if (!isIdValid(medicine))
            return obj_id.NULL_ID;
        setObjVar(medicine, consumable.VAR_CONSUMABLE_MEDICINE, true);
        setObjVar(medicine, consumable.VAR_SKILL_MOD_REQUIRED,
            new String[] { "combat_healing_ability" });
        setObjVar(medicine, consumable.VAR_SKILL_MOD_MIN, new int[] { 0 });
        setObjVar(medicine, healing.VAR_HEALING_APPLY_DOT, type);
        setObjVar(medicine, healing.VAR_HEALING_DOT_POWER, DOT_POWER);
        setObjVar(medicine, healing.VAR_HEALING_DOT_POTENCY, DOT_POTENCY);
        setObjVar(medicine, healing.VAR_HEALING_DOT_ID, id);
        setObjVar(medicine, healing.VAR_HEALING_DOT_ATTRIBUTE, HEALTH);
        setObjVar(medicine, healing.VAR_HEALING_DOT_DURATION, DOT_DURATION);
        setObjVar(medicine, healing.VAR_HEALING_RANGE, MEDICINE_RANGE);
        setObjVar(medicine, healing.VAR_HEALING_AREA, 0);
        setCount(medicine, 2);
        return medicine;
    }

    private obj_id createFixtureTarget(location loc)
        throws InterruptedException
    {
        dictionary data = dataTableGetRow(create.CREATURE_TABLE, CREATURE_TYPE);
        if (data == null)
            return obj_id.NULL_ID;
        String template = data.getString("template");
        if (template == null || template.length() == 0)
            return obj_id.NULL_ID;
        data.put("lootTable", "");
        obj_id target = createObject(create.TEMPLATE_PREFIX + template, loc);
        if (!isIdValid(target))
            return obj_id.NULL_ID;
        create.randomlyNameCreature(target, CREATURE_TYPE);
        create.initializeCreature(target, CREATURE_TYPE, data, -1);
        create.attachCreatureScripts(target, data.getString("scripts"), true);
        return target;
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
            return "action=cleanup alreadyClean=true restored=true";
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
            return ownership;
        if (!hasCompleteSnapshot(player))
            return "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true";
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
            return false;
        obj_id target = readObjId(player, TARGET);
        if (isIdValid(target) && target.isLoaded())
        {
            if (dot.getDotStrength(target, POISON_ID) >= 0)
                dot.removeDotEffect(target, POISON_ID, false);
            if (dot.getDotStrength(target, DISEASE_ID) >= 0)
                dot.removeDotEffect(target, DISEASE_ID, false);
        }
        destroyTracked(player, POISON_ITEM);
        destroyTracked(player, DISEASE_ITEM);
        if (isIdValid(target) && target.isLoaded())
            destroyObject(target);
        setMaxAttrib(player, MIND,
            getIntObjVar(player, ORIGINAL_MAX_MIND));
        setAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MIND));
        int originalXp = getIntObjVar(player, ORIGINAL_XP);
        int currentXp = getExperiencePoints(player, "medical");
        if (currentXp != originalXp)
            grantExperiencePoints(player, "medical", originalXp - currentXp);
        restoreCooldown(player, POISON_COOLDOWN,
            ORIGINAL_POISON_COOLDOWN_PRESENT, ORIGINAL_POISON_COOLDOWN);
        restoreCooldown(player, DISEASE_COOLDOWN,
            ORIGINAL_DISEASE_COOLDOWN_PRESENT, ORIGINAL_DISEASE_COOLDOWN);
        revokeSkills(player);
        boolean restored = getAttrib(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MIND) &&
            getMaxAttrib(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MAX_MIND) &&
            getExperiencePoints(player, "medical") == originalXp &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, "applyPoison") &&
            !hasCommand(player, "applyDisease");
        removeObjVar(player, ROOT);
        return restored;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
            return lifecycleError;
        obj_id target = readObjId(player, TARGET);
        obj_id poison = readObjId(player, POISON_ITEM);
        obj_id disease = readObjId(player, DISEASE_ITEM);
        if (!isIdValid(target) || !target.isLoaded() ||
            !isIdValid(poison) || !poison.isLoaded() ||
            !isIdValid(disease) || !disease.isLoaded())
            return "error=fixtureObjectsUnavailable";
        return null;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE))
            return "error=fixtureAbsent";
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null : "error=lifecycleMismatch";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return hasObjVar(player, LIFECYCLE) && hasObjVar(player, PROTOCOL) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MAX_MIND) &&
            hasObjVar(player, ORIGINAL_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_POISON_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_POISON_COOLDOWN) &&
            hasObjVar(player, ORIGINAL_DISEASE_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_DISEASE_COOLDOWN);
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id target = readObjId(player, TARGET);
        obj_id poison = readObjId(player, POISON_ITEM);
        obj_id disease = readObjId(player, DISEASE_ITEM);
        return "player=" + player +
            " target=" + target +
            " targetAvailable=" + (isIdValid(target) && target.isLoaded()) +
            " poisonCommand=" + hasCommand(player, "applyPoison") +
            " diseaseCommand=" + hasCommand(player, "applyDisease") +
            " poisonCharges=" + count(poison) +
            " diseaseCharges=" + count(disease) +
            " poisonStrength=" + strength(target, POISON_ID) +
            " diseaseStrength=" + strength(target, DISEASE_ID) +
            " mind=" + getAttrib(player, MIND) +
            " mindDelta=" + delta(player, ROOT + ".mindBefore",
                getAttrib(player, MIND)) +
            " xp=" + getExperiencePoints(player, "medical") +
            " xpDelta=" + delta(player, ROOT + ".xpBefore",
                getExperiencePoints(player, "medical")) +
            " expectedMindCost=" + readInt(player, ".expectedMindCost") +
            " expectedStrength=" + readInt(player, ".expectedStrength") +
            buildDotStatus(player, dot.DOT_POISON) +
            buildDotStatus(player, dot.DOT_DISEASE);
    }

    private String buildDotStatus(obj_id player, String type)
        throws InterruptedException
    {
        String root = ROOT + "." + type;
        return " " + type + "HandlerCalls=" + readIntAt(player,
                root + ".handlerCalls") +
            " " + type + "Outcome=" + readStringAt(player,
                root + ".outcome") +
            " " + type + "MindCost=" + readIntAt(player,
                root + ".mindCost") +
            " " + type + "ChargeCost=" + readIntAt(player,
                root + ".chargeCost") +
            " " + type + "RoundTime=" + readIntAt(player,
                root + ".roundTime") +
            " " + type + "MedicalXpDelta=" + readIntAt(player,
                root + ".medicalXpDelta");
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        removeObjVar(player, ROOT + ".poison");
        removeObjVar(player, ROOT + ".disease");
    }

    private boolean grantSkills(obj_id player) throws InterruptedException
    {
        for (String name : SKILLS)
            if (!grantSkill(player, name) || !hasSkill(player, name))
                return false;
        return true;
    }

    private void revokeSkills(obj_id player) throws InterruptedException
    {
        for (int i = SKILLS.length - 1; i >= 0; --i)
            if (hasSkill(player, SKILLS[i])) revokeSkill(player, SKILLS[i]);
    }

    private boolean hasAnyFixtureSkill(obj_id player)
        throws InterruptedException
    {
        for (String name : SKILLS)
            if (hasSkill(player, name)) return true;
        return false;
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return player != null && player != obj_id.NULL_ID &&
            player.isLoaded() && player.isAuthoritative() && isPlayer(player) &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private void snapshotCooldown(obj_id player, String cooldown,
        String presentVar, String valueVar) throws InterruptedException
    {
        setObjVar(player, presentVar, hasObjVar(player, cooldown) ? 1 : 0);
        setObjVar(player, valueVar, hasObjVar(player, cooldown)
            ? getIntObjVar(player, cooldown) : 0);
    }

    private void restoreCooldown(obj_id player, String cooldown,
        String presentVar, String valueVar) throws InterruptedException
    {
        if (getIntObjVar(player, presentVar) == 1)
            setObjVar(player, cooldown, getIntObjVar(player, valueVar));
        else
            removeIfPresent(player, cooldown);
    }

    private void removeIfPresent(obj_id player, String name)
        throws InterruptedException
    {
        if (hasObjVar(player, name)) removeObjVar(player, name);
    }

    private void destroyTracked(obj_id player, String name)
        throws InterruptedException
    {
        obj_id object = readObjId(player, name);
        if (isIdValid(object) && object.isLoaded()) destroyObject(object);
    }

    private obj_id readObjId(obj_id player, String name)
        throws InterruptedException
    {
        return hasObjVar(player, name) ? getObjIdObjVar(player, name)
            : obj_id.NULL_ID;
    }

    private int count(obj_id object) throws InterruptedException
    {
        return isIdValid(object) && object.isLoaded() ? getCount(object) : 0;
    }

    private int strength(obj_id target, String id) throws InterruptedException
    {
        if (!isIdValid(target) || !target.isLoaded()) return 0;
        return Math.max(0, dot.getDotStrength(target, id));
    }

    private int delta(obj_id player, String beforeVar, int current)
        throws InterruptedException
    {
        return hasObjVar(player, beforeVar)
            ? current - getIntObjVar(player, beforeVar) : 0;
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        return readIntAt(player, ROOT + suffix);
    }

    private int readIntAt(obj_id player, String name)
        throws InterruptedException
    {
        return hasObjVar(player, name) ? getIntObjVar(player, name) : 0;
    }

    private String readStringAt(obj_id player, String name)
        throws InterruptedException
    {
        return hasObjVar(player, name) ? getStringObjVar(player, name) : "none";
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        if (lifecycle == null || lifecycle.length() != 32) return false;
        for (int i = 0; i < lifecycle.length(); ++i)
        {
            char c = lifecycle.charAt(i);
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')))
                return false;
        }
        return true;
    }
}
