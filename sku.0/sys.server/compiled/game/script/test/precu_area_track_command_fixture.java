package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.ai_lib;
import script.library.create;
import script.library.skill;
import script.library.sui;
import script.library.utils;

/** Identity-bound reversible fixture for the Publish 14.1 Area Track lifecycle. */
public class precu_area_track_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String ROOT = "precu.areaTrackCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String OPTIONS_PID = "precu.areaTrack.options";
    private static final String RESULTS_PID = "precu.areaTrack.results";
    private static final String PENDING = "precu.areaTrack.pending";
    private static final String COOLDOWN = "precu.areaTrack.cooldownUntil";
    private static final String CREATURE_TYPE = "worrt";
    private static final String FIXTURE_NAME = "Precu Area Track Worrt";
    private static final String[] SKILLS =
    {
        "outdoors_ranger_novice",
        "outdoors_ranger_harvest_01",
        "outdoors_ranger_harvest_02",
        "outdoors_ranger_harvest_03",
        "outdoors_ranger_harvest_04"
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
        if (hasAnyFixtureSkill(player) || hasCommand(player, "areatrack") ||
            sui.hasPid(player, OPTIONS_PID) ||
            sui.hasPid(player, RESULTS_PID) ||
            utils.hasScriptVar(player, PENDING) ||
            utils.hasScriptVar(player, COOLDOWN))
            return "error=fixtureVectorAlreadyOwned";
        location playerLocation = getLocation(player);
        if (playerLocation == null || isIdValid(playerLocation.cell))
            return "error=playerMustBeOutdoors";

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        resetTelemetry(player);
        if (!grantSkills(player) || !hasCommand(player, "areatrack"))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }

        location targetLocation = new location(getWorldLocation(player));
        targetLocation.x += 10.0f;
        targetLocation.cell = obj_id.NULL_ID;
        obj_id target = createFixtureTarget(targetLocation);
        if (!isIdValid(target) || !target.isLoaded())
        {
            boolean restored = restore(player);
            return "error=targetPreparationFailed restored=" + restored;
        }
        setObjVar(player, TARGET, target);
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private obj_id createFixtureTarget(location loc)
        throws InterruptedException
    {
        dictionary data = dataTableGetRow(create.CREATURE_TABLE, CREATURE_TYPE);
        if (data == null) return obj_id.NULL_ID;
        String template = data.getString("template");
        if (template == null || template.length() == 0)
            return obj_id.NULL_ID;
        data.put("lootTable", "");
        obj_id target = createObject(create.TEMPLATE_PREFIX + template, loc);
        if (!isIdValid(target)) return obj_id.NULL_ID;
        create.initializeCreature(target, CREATURE_TYPE, data, -1);
        setName(target, FIXTURE_NAME);
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
        if (ownership != null) return ownership;
        if (!hasCompleteSnapshot(player))
            return "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true";
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player)) return false;
        closeTrackedSui(player, OPTIONS_PID);
        closeTrackedSui(player, RESULTS_PID);
        removeScriptVar(player, PENDING);
        removeScriptVar(player, COOLDOWN);
        obj_id target = readTarget(player);
        if (isIdValid(target) && target.isLoaded()) destroyObject(target);
        revokeSkills(player);
        boolean restored =
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, "areatrack") &&
            !sui.hasPid(player, OPTIONS_PID) &&
            !sui.hasPid(player, RESULTS_PID) &&
            !utils.hasScriptVar(player, PENDING) &&
            !utils.hasScriptVar(player, COOLDOWN);
        removeObjVar(player, ROOT);
        return restored;
    }

    private void closeTrackedSui(obj_id player, String pidName)
        throws InterruptedException
    {
        if (!sui.hasPid(player, pidName)) return;
        int pid = sui.getPid(player, pidName);
        if (pid > 0) forceCloseSUIPage(pid);
        sui.removePid(player, pidName);
    }

    private void removeScriptVar(obj_id player, String name)
        throws InterruptedException
    {
        if (utils.hasScriptVar(player, name))
            utils.removeScriptVar(player, name);
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id target = readTarget(player);
        return "player=" + player +
            " command=" + hasCommand(player, "areatrack") +
            " rangerNovice=" + hasSkill(player,
                "outdoors_ranger_novice") +
            " directionTier=" + hasSkill(player,
                "outdoors_ranger_harvest_01") +
            " npcTier=" + hasSkill(player,
                "outdoors_ranger_harvest_02") +
            " distanceTier=" + hasSkill(player,
                "outdoors_ranger_harvest_03") +
            " playerTier=" + hasSkill(player,
                "outdoors_ranger_harvest_04") +
            " target=" + target +
            " targetAvailable=" +
                (isIdValid(target) && target.isLoaded()) +
            " targetNiche=" + (isIdValid(target) && target.isLoaded()
                ? ai_lib.aiGetNiche(target) : -1) +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " failHandlerCalls=" + readInt(player,
                ".failHandlerCalls") +
            " outcome=" + readString(player, ".outcome") +
            " optionPid=" + readInt(player, ".optionPid") +
            " optionCount=" + readInt(player, ".optionCount") +
            " selectedType=" + readInt(player, ".selectedType") +
            " scanStartedAt=" + readInt(player, ".scanStartedAt") +
            " cooldownUntil=" + readInt(player, ".cooldownUntil") +
            " scanCompletedAt=" + readInt(player,
                ".scanCompletedAt") +
            " resultCount=" + readInt(player, ".resultCount") +
            " resultsPid=" + readInt(player, ".resultsPid") +
            " fixtureTargetFound=" + readInt(player,
                ".fixtureTargetFound") +
            " fixtureDirection=" + readString(player,
                ".fixtureDirection") +
            " fixtureDistance=" + readInt(player,
                ".fixtureDistance") +
            " pending=" + utils.hasScriptVar(player, PENDING) +
            " cooldownActive=" + (utils.hasScriptVar(player, COOLDOWN) &&
                getGameTime() < utils.getIntScriptVar(player, COOLDOWN));
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] suffixes =
        {
            ".handlerCalls", ".failHandlerCalls", ".outcome",
            ".optionPid", ".optionCount", ".selectedType",
            ".scanStartedAt", ".cooldownUntil", ".scanCompletedAt",
            ".resultCount", ".resultsPid", ".fixtureTargetFound",
            ".fixtureDirection", ".fixtureDistance"
        };
        for (String suffix : suffixes)
            if (hasObjVar(player, ROOT + suffix))
                removeObjVar(player, ROOT + suffix);
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
        for (int index = SKILLS.length - 1; index >= 0; --index)
            if (hasSkill(player, SKILLS[index]))
                revokeSkill(player, SKILLS[index]);
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

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null) return lifecycleError;
        obj_id target = readTarget(player);
        return isIdValid(target) && target.isLoaded()
            ? null : "error=fixtureTargetUnavailable";
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
            hasObjVar(player, ORIGINAL_POINTS);
    }

    private obj_id readTarget(obj_id player) throws InterruptedException
    {
        return hasObjVar(player, TARGET) ? getObjIdObjVar(player, TARGET)
            : obj_id.NULL_ID;
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        String name = ROOT + suffix;
        return hasObjVar(player, name) ? getIntObjVar(player, name) : 0;
    }

    private String readString(obj_id player, String suffix)
        throws InterruptedException
    {
        String name = ROOT + suffix;
        return hasObjVar(player, name) ? getStringObjVar(player, name) : "none";
    }

    private boolean isValidLifecycle(String value)
    {
        return value != null && value.matches("[0-9a-fA-F]{32}");
    }
}
