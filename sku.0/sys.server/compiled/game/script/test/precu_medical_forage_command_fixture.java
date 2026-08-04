package script.test;

import script.location;
import script.obj_id;
import script.library.utils;

/**
 * Identity-bound and reversible live fixture for Publish 14.1 medical forage.
 *
 * The connected client remains the command owner. The fixture preserves the
 * character's Action, world location, novice skill, and command grant, then
 * forces the real delayed handler to produce one ordinary medicine component.
 */
public class precu_medical_forage_command_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String MEDIC_NOVICE = "science_medic_novice";
    private static final String COMMAND = "medicalForage";
    private static final String ROOT =
        "precu.medicalForageCommandFixture";
    private static final String RUNTIME_ROOT = "precu.medicalForage";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_NOVICE =
        ROOT + ".originalNovice";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalCommand";
    private static final String ORIGINAL_ACTION =
        ROOT + ".originalAction";
    private static final String ORIGINAL_LOCATION =
        ROOT + ".originalLocation";
    private static final String REWARD_OID = ROOT + ".rewardOid";
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args =
            params == null
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
        if (playerValue != PLAYER_OID ||
            !isValidLifecycle(args[2]))
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
            if (ownership == null &&
                getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent"))
            {
                return ownership;
            }
        }
        if (utils.hasScriptVarTree(player, RUNTIME_ROOT))
        {
            return "error=existingForageRuntimeState";
        }

        location originalLocation = getLocation(player);
        location world = getWorldLocation(player);
        if (originalLocation == null || world == null)
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
            ORIGINAL_COMMAND,
            hasCommand(player, COMMAND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_ACTION,
            getAttrib(player, ACTION));
        setObjVar(player, ORIGINAL_LOCATION, originalLocation);
        resetTelemetry(player);

        boolean skillReady =
            hasSkill(player, MEDIC_NOVICE) ||
            grantSkill(player, MEDIC_NOVICE);
        if (!hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        if (!skillReady || !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=medicPreparationFailed restored=" +
                restored;
        }

        if (isIdValid(originalLocation.cell))
        {
            location outside = new location(
                world.x + 25.0f,
                world.y,
                world.z + 25.0f,
                world.area,
                obj_id.NULL_ID);
            if (!setLocation(player, outside))
            {
                boolean restored = restore(player);
                return "error=relocationFailed restored=" + restored;
            }
        }
        setObjVar(player, ROOT + ".forceSuccessRoll", 0);
        setObjVar(player, ROOT + ".forceRewardRoll", 120);
        setObjVar(player, ROOT + ".forceComponentIndex", 0);
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " +
            buildStatus(player);
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
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (utils.hasScriptVar(
                player,
                RUNTIME_ROOT + ".pending"))
        {
            return "error=foragePending " + buildStatus(player);
        }
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        boolean restored = true;
        if (hasObjVar(player, REWARD_OID) &&
            !getStringObjVar(player, REWARD_OID).equals("none"))
        {
            obj_id reward = obj_id.getObjId(
                Long.parseLong(getStringObjVar(player, REWARD_OID)));
            if (isIdValid(reward) && exists(reward))
            {
                obj_id inventory = utils.getInventoryContainer(player);
                if (getContainedBy(reward) != inventory)
                {
                    return false;
                }
                restored = destroyObject(reward) && restored;
            }
        }
        utils.removeScriptVarTree(player, RUNTIME_ROOT);

        location original =
            getLocationObjVar(player, ORIGINAL_LOCATION);
        restored = setLocation(player, original) && restored;
        int originalAction = getIntObjVar(player, ORIGINAL_ACTION);
        setAttrib(player, ACTION, originalAction);
        restored =
            getAttrib(player, ACTION) == originalAction &&
            restored;

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
        clearFixtureVariables(player);
        return restored;
    }

    private String validateOwnership(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(
            getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_LOCATION);
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        location current = getLocation(player);
        return
            "player=" + player +
            " currentCommand=" + getCurrentCommand(player) +
            " novice=" + hasSkill(player, MEDIC_NOVICE) +
            " medicalForageCommand=" +
                hasCommand(player, COMMAND) +
            " prepared=" + readInt(player, "prepared") +
            " cell=" +
                (current == null ? "none" : current.cell) +
            " pending=" +
                utils.hasScriptVar(
                    player,
                    RUNTIME_ROOT + ".pending") +
            " handlerCalls=" + readInt(player, "handlerCalls") +
            " handlerEntered=" +
                readInt(player, "handlerEntered") +
            " actionBefore=" + readInt(player, "actionBefore") +
            " actionCost=" + readInt(player, "actionCost") +
            " actionAfter=" + readInt(player, "actionAfter") +
            " finishedAt=" + readInt(player, "finishedAt") +
            " elapsed=" + readInt(player, "elapsed") +
            " skillMod=" + readInt(player, "skillMod") +
            " chance=" + readInt(player, "chance") +
            " successRoll=" + readInt(player, "successRoll") +
            " rewardRoll=" + readInt(player, "rewardRoll") +
            " rewardType=" + readString(player, "rewardType") +
            " rewardOid=" + readString(player, "rewardOid") +
            " rewardTemplate=" +
                readString(player, "rewardTemplate") +
            " outcome=" + readString(player, "outcome");
    }

    private int readInt(obj_id player, String field)
        throws InterruptedException
    {
        String key = ROOT + "." + field;
        return hasObjVar(player, key)
            ? getIntObjVar(player, key)
            : 0;
    }

    private String readString(obj_id player, String field)
        throws InterruptedException
    {
        String key = ROOT + "." + field;
        return hasObjVar(player, key)
            ? getStringObjVar(player, key)
            : "none";
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        String[] stringFields =
        {
            "outcome",
            "rewardType",
            "rewardOid",
            "rewardTemplate"
        };
        String[] intFields =
        {
            "prepared",
            "handlerCalls",
            "handlerEntered",
            "actionBefore",
            "actionCost",
            "actionAfter",
            "finishedAt",
            "elapsed",
            "skillMod",
            "chance",
            "successRoll",
            "rewardRoll"
        };
        for (String field : stringFields)
        {
            setObjVar(player, ROOT + "." + field, "none");
        }
        for (String field : intFields)
        {
            setObjVar(player, ROOT + "." + field, 0);
        }
    }

    private void clearFixtureVariables(obj_id player)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            removeObjVar(player, ROOT);
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
