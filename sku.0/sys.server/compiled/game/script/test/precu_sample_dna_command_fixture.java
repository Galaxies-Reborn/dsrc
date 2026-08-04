package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.create;
import script.library.skill;
import script.library.utils;
import script.library.xp;

/**
 * Identity-bound and reversible fixture for the Publish 14.1 sampleDNA
 * command. The connected client remains the sole command-admission owner.
 */
public class precu_sample_dna_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "sampleDNA";
    private static final String XP_TYPE = xp.BIO_ENGINEER_DNA_HARVESTING;
    private static final String CREATURE_TYPE = "worrt";
    private static final String QUICK_SAMPLE = "quick_dna_sample";
    private static final String HARVEST_RUNTIME = "bio_engineer.harvest_dna";
    private static final String ROOT = "precu.sampleDnaCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String DNA = ROOT + ".dna";
    private static final String ORIGINAL_ACTION = ROOT + ".originalAction";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_MAX_ACTION =
        ROOT + ".originalMaxAction";
    private static final String ORIGINAL_MAX_MIND =
        ROOT + ".originalMaxMind";
    private static final String ORIGINAL_XP = ROOT + ".originalXp";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_QUICK_PRESENT =
        ROOT + ".originalQuickPresent";
    private static final String BEFORE_ACTION = ROOT + ".beforeAction";
    private static final String BEFORE_MIND = ROOT + ".beforeMind";
    private static final String BEFORE_XP = ROOT + ".beforeXp";
    private static final String[] SKILLS =
    {
        "outdoors_bio_engineer",
        "outdoors_bio_engineer_novice",
        "outdoors_bio_engineer_dna_harvesting_01"
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null
            ? new String[0]
            : params.trim().split("[ ]+");
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
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("cleanup"))
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
            if (ownership == null && getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " + buildStatus(player);
            }
            return ownership == null ? "error=fixtureNotPrepared" : ownership;
        }
        if (hasAnyFixtureSkill(player) || hasCommand(player, COMMAND) ||
            utils.hasScriptVar(player, HARVEST_RUNTIME))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_ACTION, getAttrib(player, ACTION));
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_MAX_ACTION, getMaxAttrib(player, ACTION));
        setObjVar(player, ORIGINAL_MAX_MIND, getMaxAttrib(player, MIND));
        setObjVar(player, ORIGINAL_XP, getExperiencePoints(player, XP_TYPE));
        setObjVar(player, ORIGINAL_POINTS, skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_QUICK_PRESENT,
            hasObjVar(player, QUICK_SAMPLE) ? 1 : 0);
        resetTelemetry(player);

        if (!grantSkills(player) || !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        if (!setExperience(player, 0))
        {
            boolean restored = restore(player);
            return "error=xpPreparationFailed restored=" + restored;
        }
        setMaxAttrib(player, ACTION, Math.max(500, getMaxAttrib(player, ACTION)));
        setMaxAttrib(player, MIND, Math.max(500, getMaxAttrib(player, MIND)));
        setAttrib(player, ACTION, getMaxAttrib(player, ACTION));
        setAttrib(player, MIND, getMaxAttrib(player, MIND));
        if (getAttrib(player, ACTION) < 100 || getAttrib(player, MIND) < 250)
        {
            boolean restored = restore(player);
            return "error=attributePreparationFailed restored=" + restored;
        }
        if (!hasObjVar(player, QUICK_SAMPLE))
        {
            setObjVar(player, QUICK_SAMPLE, 1);
        }

        location targetLocation = new location(getLocation(player));
        targetLocation.x += 2.0f;
        obj_id target = createFixtureTarget(targetLocation);
        if (!isIdValid(target) || !target.isLoaded())
        {
            boolean restored = restore(player);
            return "error=targetCreationFailed restored=" + restored;
        }
        setObjVar(player, TARGET, target);
        setObjVar(player, BEFORE_ACTION, getAttrib(player, ACTION));
        setObjVar(player, BEFORE_MIND, getAttrib(player, MIND));
        setObjVar(player, BEFORE_XP, getExperiencePoints(player, XP_TYPE));
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
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
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(player))
        {
            boolean legacyClearable =
                !hasObjVar(player, PREPARED) &&
                !hasObjVar(player, TARGET) &&
                !hasObjVar(player, DNA) &&
                !hasAnyFixtureSkill(player) &&
                !hasCommand(player, COMMAND) &&
                !utils.hasScriptVar(player, HARVEST_RUNTIME);
            if (legacyClearable)
            {
                removeObjVar(player, ROOT);
                return "action=cleanup alreadyClean=false restored=true" +
                    " legacyMarkerCleared=true";
            }
            return "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true";
        }
        return "action=cleanup alreadyClean=false restored=" + restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        utils.removeScriptVar(player, HARVEST_RUNTIME);
        boolean restored = true;
        if (hasObjVar(player, DNA))
        {
            obj_id dna = getObjIdObjVar(player, DNA);
            if (isIdValid(dna) && dna.isLoaded())
            {
                restored = destroyObject(dna) && restored;
            }
        }
        if (hasObjVar(player, TARGET))
        {
            obj_id target = getObjIdObjVar(player, TARGET);
            if (isIdValid(target) && target.isLoaded())
            {
                if (getMaster(target) == player)
                {
                    setMaster(target, null);
                }
                restored = destroyObject(target) && restored;
            }
        }

        int originalAction = getIntObjVar(player, ORIGINAL_ACTION);
        int originalMind = getIntObjVar(player, ORIGINAL_MIND);
        setMaxAttrib(player, ACTION, getIntObjVar(player, ORIGINAL_MAX_ACTION));
        setMaxAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MAX_MIND));
        setAttrib(player, ACTION, originalAction);
        setAttrib(player, MIND, originalMind);
        restored = setExperience(player, getIntObjVar(player, ORIGINAL_XP)) &&
            restored;
        if (getIntObjVar(player, ORIGINAL_QUICK_PRESENT) == 0 &&
            hasObjVar(player, QUICK_SAMPLE))
        {
            removeObjVar(player, QUICK_SAMPLE);
        }
        revokeSkills(player);
        restored =
            getAttrib(player, ACTION) == originalAction &&
            getAttrib(player, MIND) == originalMind &&
            getMaxAttrib(player, ACTION) ==
                getIntObjVar(player, ORIGINAL_MAX_ACTION) &&
            getMaxAttrib(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MAX_MIND) &&
            getExperiencePoints(player, XP_TYPE) ==
                getIntObjVar(player, ORIGINAL_XP) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, COMMAND) &&
            getIntObjVar(player, ORIGINAL_QUICK_PRESENT) ==
                (hasObjVar(player, QUICK_SAMPLE) ? 1 : 0) &&
            restored;
        removeObjVar(player, ROOT);
        return restored;
    }

    private boolean grantSkills(obj_id player) throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!grantSkill(player, skillName) || !hasSkill(player, skillName))
            {
                return false;
            }
        }
        return true;
    }

    private obj_id createFixtureTarget(location targetLocation)
        throws InterruptedException
    {
        dictionary creatureData =
            dataTableGetRow(create.CREATURE_TABLE, CREATURE_TYPE);
        if (creatureData == null)
        {
            return obj_id.NULL_ID;
        }
        String templateName = creatureData.getString("template");
        if (templateName == null || templateName.length() == 0)
        {
            return obj_id.NULL_ID;
        }
        // ServerConsole has no script-owner context.  Loot initialization logs
        // through getSelf(), so disable loot only for this disposable fixture
        // creature; DNA harvesting does not consume or inspect creature loot.
        creatureData.put("lootTable", "");
        obj_id target = createObject(
            create.TEMPLATE_PREFIX + templateName,
            targetLocation);
        if (!isIdValid(target))
        {
            return obj_id.NULL_ID;
        }
        create.randomlyNameCreature(target, CREATURE_TYPE);
        create.initializeCreature(target, CREATURE_TYPE, creatureData, -1);
        create.attachCreatureScripts(
            target,
            creatureData.getString("scripts"),
            false);
        return target;
    }

    private void revokeSkills(obj_id player) throws InterruptedException
    {
        for (int index = SKILLS.length - 1; index >= 0; --index)
        {
            if (hasSkill(player, SKILLS[index]))
            {
                revokeSkill(player, SKILLS[index]);
            }
        }
    }

    private boolean hasAnyFixtureSkill(obj_id player)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (hasSkill(player, skillName))
            {
                return true;
            }
        }
        return false;
    }

    private boolean setExperience(obj_id player, int target)
        throws InterruptedException
    {
        int current = getExperiencePoints(player, XP_TYPE);
        if (current != target)
        {
            grantExperiencePoints(player, XP_TYPE, target - current);
        }
        return getExperiencePoints(player, XP_TYPE) == target;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, PROTOCOL) ||
            getIntObjVar(player, PROTOCOL) != PROTOCOL_VERSION)
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        if (!hasObjVar(player, PREPARED) || !hasObjVar(player, TARGET))
        {
            return "error=fixtureNotPrepared";
        }
        obj_id target = getObjIdObjVar(player, TARGET);
        if (!isIdValid(target) || !target.isLoaded())
        {
            return "error=fixtureTargetUnavailable";
        }
        return null;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id target = hasObjVar(player, TARGET)
            ? getObjIdObjVar(player, TARGET)
            : obj_id.NULL_ID;
        obj_id dna = hasObjVar(player, DNA)
            ? getObjIdObjVar(player, DNA)
            : obj_id.NULL_ID;
        int action = getAttrib(player, ACTION);
        int mind = getAttrib(player, MIND);
        int currentXp = getExperiencePoints(player, XP_TYPE);
        boolean targetAvailable = isIdValid(target) && target.isLoaded();
        boolean dnaAvailable = isIdValid(dna) && dna.isLoaded() &&
            utils.getContainingPlayer(dna) == player;
        return
            "player=" + player +
            " target=" + target +
            " targetAvailable=" + targetAvailable +
            " targetLevel=" + (targetAvailable ? getLevel(target) : -1) +
            " targetDead=" + (targetAvailable && isDead(target)) +
            " dna=" + dna +
            " dnaAvailable=" + dnaAvailable +
            " dnaTemplate=" + readString(player, ".dnaTemplate") +
            " skillBits=" + buildSkillBits(player) +
            " command=" + hasCommand(player, COMMAND) +
            " dnaHarvesting=" + getSkillStatMod(player, "dna_harvesting") +
            " action=" + action +
            " mind=" + mind +
            " xp=" + currentXp +
            " actionCost=" + readInt(player, ".actionCost") +
            " mindCost=" + readInt(player, ".mindCost") +
            " xpDelta=" + delta(player, BEFORE_XP, currentXp) +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " outcome=" + readString(player, ".outcome") +
            " skillRoll=" + readInt(player, ".skillRoll") +
            " survivalRoll=" + readInt(player, ".survivalRoll") +
            " behaviorRoll=" + readInt(player, ".behaviorRoll") +
            " xpGranted=" + readInt(player, ".xpGranted") +
            " creatureSurvived=" + readInt(player, ".creatureSurvived") +
            " harvestCount=" + readInt(player, ".harvestCount") +
            " harvesting=" +
                utils.hasScriptVar(player, HARVEST_RUNTIME) +
            " quickSample=" + hasObjVar(player, QUICK_SAMPLE) +
            " availablePoints=" + skill.getAvailableSkillPoints(player);
    }

    private String buildSkillBits(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(player, skillName) ? "1" : "0";
        }
        return bits;
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }

    private String readString(obj_id player, String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
        return hasObjVar(player, path)
            ? getStringObjVar(player, path)
            : "none";
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

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MAX_ACTION) &&
            hasObjVar(player, ORIGINAL_MAX_MIND) &&
            hasObjVar(player, ORIGINAL_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_QUICK_PRESENT);
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered", "handlerCalls", "startedAt", "completedAt",
            "outcome", "dna", "dnaTemplate", "skillRoll", "survivalRoll",
            "behaviorRoll", "xpGranted", "creatureSurvived", "harvestCount"
        };
        for (String leaf : leaves)
        {
            String path = ROOT + "." + leaf;
            if (hasObjVar(player, path))
            {
                removeObjVar(player, path);
            }
        }
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
