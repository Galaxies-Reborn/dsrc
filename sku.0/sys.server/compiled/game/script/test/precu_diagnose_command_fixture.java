package script.test;

import script.obj_id;
import script.library.utils;

/**
 * Identity-bound, reversible ServerConsole fixture for Publish 14.1 diagnose.
 *
 * The connected client remains the only command owner. This fixture supplies
 * deterministic patient data on the bound player and records the real command
 * handler's SUI telemetry.
 */
public class precu_diagnose_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String MEDIC_NOVICE = "science_medic_novice";
    private static final String DIAGNOSE = "diagnose";
    private static final String ROOT = "precu.diagnoseCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_NOVICE =
        ROOT + ".originalNovice";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalCommand";
    private static final String ORIGINAL_CURRENT =
        ROOT + ".originalCurrent";
    private static final String ORIGINAL_WOUNDS =
        ROOT + ".originalWounds";
    private static final String ORIGINAL_BATTLE_FATIGUE =
        ROOT + ".originalBattleFatigue";
    private static final String SUI_PID = "precu.diagnose.pid";
    private static final int[] ATTRIBUTES =
    {
        HEALTH,
        STRENGTH,
        CONSTITUTION,
        ACTION,
        QUICKNESS,
        STAMINA,
        MIND,
        FOCUS,
        WILLPOWER
    };
    private static final String[] ATTRIBUTE_NAMES =
    {
        "Health",
        "Strength",
        "Constitution",
        "Action",
        "Quickness",
        "Stamina",
        "Mind",
        "Focus",
        "Willpower"
    };
    private static final int[] TEST_WOUNDS =
    {
        11,
        22,
        33,
        44,
        55,
        66,
        77,
        88,
        99
    };
    private static final int TEST_BATTLE_FATIGUE = 321;
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <lifecycle>";

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
            if (ownership == null && isFixtureActive(player))
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent") &&
                isFixtureActive(player))
            {
                return ownership;
            }
            // Packed player objvars can retain inactive leaves. A restored
            // lifecycle owns no live state, so overwrite it deterministically.
        }

        int[] originalCurrent = new int[ATTRIBUTES.length];
        int[] originalWounds = new int[ATTRIBUTES.length];
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            originalCurrent[index] =
                getAttrib(player, ATTRIBUTES[index]);
            originalWounds[index] =
                getAttribWound(player, ATTRIBUTES[index]);
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_NOVICE,
            hasSkill(player, MEDIC_NOVICE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COMMAND,
            hasCommand(player, DIAGNOSE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CURRENT, originalCurrent);
        setObjVar(player, ORIGINAL_WOUNDS, originalWounds);
        setObjVar(
            player,
            ORIGINAL_BATTLE_FATIGUE,
            getShockWound(player));
        resetTelemetry(player);

        boolean skillReady =
            hasSkill(player, MEDIC_NOVICE) ||
            grantSkill(player, MEDIC_NOVICE);
        if (!hasCommand(player, DIAGNOSE))
        {
            grantCommand(player, DIAGNOSE);
        }
        if (!skillReady || !hasCommand(player, DIAGNOSE))
        {
            boolean restored = restore(player);
            return "error=medicPreparationFailed restored=" + restored;
        }

        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (!setWoundExact(
                    player,
                    ATTRIBUTES[index],
                    TEST_WOUNDS[index]))
            {
                boolean restored = restore(player);
                return "error=woundPreparationFailed index=" + index +
                    " restored=" + restored;
            }
        }
        if (!setShockWound(player, TEST_BATTLE_FATIGUE) ||
            getShockWound(player) != TEST_BATTLE_FATIGUE)
        {
            boolean restored = restore(player);
            return "error=battleFatiguePreparationFailed restored=" +
                restored;
        }
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
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (getCurrentCommand(player) != 0)
        {
            return "error=commandPending " + buildStatus(player);
        }
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        if (utils.hasScriptVar(player, SUI_PID))
        {
            // A ServerConsole handler has no script owner context and cannot
            // call sui.closeSUI(). The live runner dismisses the read-only
            // page through the client's background Escape path first.
            utils.removeScriptVar(player, SUI_PID);
        }

        int[] originalCurrent =
            getIntArrayObjVar(player, ORIGINAL_CURRENT);
        int[] originalWounds =
            getIntArrayObjVar(player, ORIGINAL_WOUNDS);
        if (originalCurrent == null ||
            originalCurrent.length != ATTRIBUTES.length ||
            originalWounds == null ||
            originalWounds.length != ATTRIBUTES.length)
        {
            return false;
        }

        boolean restored = true;
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            restored =
                setWoundExact(
                    player,
                    ATTRIBUTES[index],
                    originalWounds[index]) &&
                restored;
        }
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            setAttrib(
                player,
                ATTRIBUTES[index],
                originalCurrent[index]);
            restored =
                getAttrib(player, ATTRIBUTES[index]) ==
                    originalCurrent[index] &&
                getAttribWound(player, ATTRIBUTES[index]) ==
                    originalWounds[index] &&
                restored;
        }
        int originalBattleFatigue =
            getIntObjVar(player, ORIGINAL_BATTLE_FATIGUE);
        restored =
            setShockWound(player, originalBattleFatigue) &&
            getShockWound(player) == originalBattleFatigue &&
            restored;

        if (getIntObjVar(player, ORIGINAL_NOVICE) == 0 &&
            hasSkill(player, MEDIC_NOVICE))
        {
            revokeSkill(player, MEDIC_NOVICE);
        }
        restoreCommand(
            player,
            getIntObjVar(player, ORIGINAL_COMMAND) == 1);
        clearFixtureVariables(player);
        return restored;
    }

    private void restoreCommand(
        obj_id player,
        boolean originallyPresent)
        throws InterruptedException
    {
        if (originallyPresent && !hasCommand(player, DIAGNOSE))
        {
            grantCommand(player, DIAGNOSE);
        }
        else if (!originallyPresent && hasCommand(player, DIAGNOSE))
        {
            revokeCommand(player, DIAGNOSE);
        }
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        return null;
    }

    private boolean isFixtureActive(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, PREPARED) ||
            getIntObjVar(player, PREPARED) != 1 ||
            !hasCommand(player, DIAGNOSE))
        {
            return false;
        }
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (getAttribWound(player, ATTRIBUTES[index]) !=
                TEST_WOUNDS[index])
            {
                return false;
            }
        }
        return getShockWound(player) == TEST_BATTLE_FATIGUE;
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_CURRENT) &&
            hasObjVar(player, ORIGINAL_WOUNDS) &&
            hasObjVar(player, ORIGINAL_BATTLE_FATIGUE);
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        String result =
            "player=" + player +
            " currentCommand=" + getCurrentCommand(player) +
            " novice=" + hasSkill(player, MEDIC_NOVICE) +
            " diagnoseCommand=" + hasCommand(player, DIAGNOSE) +
            " prepared=" + readInt(player, ".prepared");
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            result +=
                " " + ATTRIBUTE_NAMES[index].toLowerCase() +
                "Wound=" +
                getAttribWound(player, ATTRIBUTES[index]);
        }
        result +=
            " battleFatigue=" + getShockWound(player) +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " outcome=" + readString(player, ".outcome") +
            " target=" + readString(player, ".target") +
            " entryCount=" + readInt(player, ".entryCount") +
            " suiPid=" + readInt(player, ".suiPid") +
            " observedBattleFatigue=" +
                readInt(player, ".observedBattleFatigue") +
            " title=" + readString(player, ".title").replace(' ', '_');
        if (hasObjVar(player, ROOT + ".observedWounds"))
        {
            int[] observed =
                getIntArrayObjVar(player, ROOT + ".observedWounds");
            if (observed != null)
            {
                for (int index = 0;
                    index < observed.length &&
                        index < ATTRIBUTE_NAMES.length;
                    ++index)
                {
                    result +=
                        " observed" + ATTRIBUTE_NAMES[index] +
                        "=" + observed[index];
                }
            }
        }
        return result;
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        String key = ROOT + suffix;
        return hasObjVar(player, key) ? getIntObjVar(player, key) : 0;
    }

    private String readString(obj_id player, String suffix)
        throws InterruptedException
    {
        String key = ROOT + suffix;
        return hasObjVar(player, key)
            ? getStringObjVar(player, key)
            : "none";
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        setObjVar(player, PREPARED, 0);
        setObjVar(player, ROOT + ".handlerCalls", 0);
        setObjVar(player, ROOT + ".handlerEntered", 0);
        setObjVar(player, ROOT + ".entryCount", 0);
        setObjVar(player, ROOT + ".suiPid", -1);
        setObjVar(player, ROOT + ".observedBattleFatigue", 0);
        setObjVar(
            player,
            ROOT + ".observedWounds",
            new int[ATTRIBUTES.length]);
        setObjVar(player, ROOT + ".outcome", "none");
        setObjVar(player, ROOT + ".target", "none");
        setObjVar(player, ROOT + ".title", "none");
    }

    private void clearFixtureVariables(obj_id player)
        throws InterruptedException
    {
        String[] keys =
        {
            LIFECYCLE,
            PREPARED,
            ORIGINAL_NOVICE,
            ORIGINAL_COMMAND,
            ORIGINAL_CURRENT,
            ORIGINAL_WOUNDS,
            ORIGINAL_BATTLE_FATIGUE,
            ROOT + ".handlerCalls",
            ROOT + ".handlerEntered",
            ROOT + ".outcome",
            ROOT + ".target",
            ROOT + ".observedWounds",
            ROOT + ".observedBattleFatigue",
            ROOT + ".entryCount",
            ROOT + ".suiPid",
            ROOT + ".title"
        };
        for (String key : keys)
        {
            if (hasObjVar(player, key))
            {
                removeObjVar(player, key);
            }
        }
        if (hasObjVar(player, ROOT))
        {
            removeObjVar(player, ROOT);
        }
    }

    private boolean setWoundExact(
        obj_id target,
        int attribute,
        int requested)
        throws InterruptedException
    {
        int current = getAttribWound(target, attribute);
        if (current > requested)
        {
            healWound(target, attribute, current - requested);
        }
        else if (current < requested)
        {
            addWound(target, attribute, requested - current);
        }
        return getAttribWound(target, attribute) == requested;
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
