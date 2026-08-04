package script.test;

import script.library.performance;
import script.obj_id;

/**
 * Identity-bound ServerConsole acceptance fixture for Publish 14.1
 * performance-loop and flourish Action drain.
 */
public class precu_performance_action_drain_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT =
        "precu.performanceActionDrainFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_ACTION =
        ROOT + ".originalAction";
    private static final String ORIGINAL_QUICKNESS =
        ROOT + ".originalQuickness";
    private static final String ORIGINAL_PERFORMANCE =
        ROOT + ".originalPerformance";
    private static final String PULSE_PASSED =
        ROOT + ".pulsePassed";
    private static final String LOOP_COST =
        ROOT + ".loopCost";
    private static final String LOOP_REMAINING =
        ROOT + ".loopRemaining";
    private static final String LOOP_BOUNDARY =
        ROOT + ".loopBoundaryRejected";
    private static final String FLOURISH_COST =
        ROOT + ".flourishCost";
    private static final String FLOURISH_REMAINING =
        ROOT + ".flourishRemaining";
    private static final String FLOURISH_BOUNDARY =
        ROOT + ".flourishBoundaryRejected";
    private static final String RESTORED =
        ROOT + ".restored";
    private static final String USAGE =
        "usage: prepare|pulse|status|cleanup <playerOid> <lifecycle>";
    private static final int REFERENCE_QUICKNESS = 400;

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
        if (args[0].equalsIgnoreCase("pulse"))
        {
            return pulse(player, args[2]);
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
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, ORIGINAL_ACTION, getAttrib(player, ACTION));
        setObjVar(
            player,
            ORIGINAL_QUICKNESS,
            getAttrib(player, QUICKNESS));
        setObjVar(
            player,
            ORIGINAL_PERFORMANCE,
            getPerformanceType(player));
        resetTelemetry(player);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String pulse(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }

        int performanceIndex =
            performance.lookupPerformanceIndex(-1788534963, "basic", 0);
        if (performanceIndex <= 0)
        {
            return "error=basicDanceMissing " + buildStatus(player);
        }

        boolean setup = true;
        boolean loopApplied = false;
        boolean loopBoundaryRejected = false;
        boolean flourishApplied = false;
        boolean flourishBoundaryRejected = false;
        int loopCost = -1;
        int loopRemaining = -1;
        int flourishCost = -1;
        int flourishRemaining = -1;
        boolean restored;
        try
        {
            setPerformanceType(player, performanceIndex);
            setup =
                getAttrib(player, QUICKNESS) == REFERENCE_QUICKNESS &&
                setExactAttribute(player, ACTION, 100);
            if (setup)
            {
                loopCost =
                    performance.calculatePerformanceLoopActionCost(player);
                loopApplied =
                    performance.applyPerformanceLoopActionCost(player);
                loopRemaining = getAttrib(player, ACTION);
            }

            setup =
                setup &&
                setExactAttribute(player, ACTION, 25);
            if (setup)
            {
                loopBoundaryRejected =
                    !performance.applyPerformanceLoopActionCost(player) &&
                    getAttrib(player, ACTION) == 25;
            }

            setup =
                setup &&
                setExactAttribute(player, ACTION, 100);
            if (setup)
            {
                flourishCost =
                    performance.calculatePerformanceFlourishActionCost(
                        player);
                flourishApplied =
                    performance.applyPerformanceFlourishActionCost(player);
                flourishRemaining = getAttrib(player, ACTION);
            }

            setup =
                setup &&
                setExactAttribute(player, ACTION, 9);
            if (setup)
            {
                flourishBoundaryRejected =
                    performance.calculatePerformanceFlourishActionCost(
                        player) == 9 &&
                    !performance.applyPerformanceFlourishActionCost(player) &&
                    getAttrib(player, ACTION) == 9;
            }
        }
        finally
        {
            restored = restoreSnapshot(player);
        }

        boolean passed =
            setup &&
            loopCost == 25 &&
            loopApplied &&
            loopRemaining == 75 &&
            loopBoundaryRejected &&
            flourishCost == 9 &&
            flourishApplied &&
            flourishRemaining == 91 &&
            flourishBoundaryRejected &&
            restored;
        setObjVar(player, PULSE_PASSED, passed ? 1 : 0);
        setObjVar(player, LOOP_COST, loopCost);
        setObjVar(player, LOOP_REMAINING, loopRemaining);
        setObjVar(
            player,
            LOOP_BOUNDARY,
            loopBoundaryRejected ? 1 : 0);
        setObjVar(player, FLOURISH_COST, flourishCost);
        setObjVar(player, FLOURISH_REMAINING, flourishRemaining);
        setObjVar(
            player,
            FLOURISH_BOUNDARY,
            flourishBoundaryRejected ? 1 : 0);
        setObjVar(player, RESTORED, restored ? 1 : 0);
        return "action=pulse passed=" + passed +
            " " + buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        boolean passed =
            readInt(player, PULSE_PASSED) == 1 &&
            restoreMatchesSnapshot(player);
        return "action=status passed=" + passed +
            " " + buildStatus(player);
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
        boolean restored = restoreSnapshot(player);
        if (!restored)
        {
            return "error=cleanupRestoreFailed " + buildStatus(player);
        }
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true";
    }

    private boolean restoreSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_ACTION) ||
            !hasObjVar(player, ORIGINAL_QUICKNESS) ||
            !hasObjVar(player, ORIGINAL_PERFORMANCE))
        {
            return false;
        }
        setPerformanceType(
            player,
            getIntObjVar(player, ORIGINAL_PERFORMANCE));
        boolean restored =
            setExactAttribute(
                player,
                ACTION,
                getIntObjVar(player, ORIGINAL_ACTION));
        return restored && restoreMatchesSnapshot(player);
    }

    private boolean setExactAttribute(
        obj_id player,
        int attribute,
        int value) throws InterruptedException
    {
        setAttrib(player, attribute, value);
        return getAttrib(player, attribute) == value;
    }

    private boolean restoreMatchesSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            getAttrib(player, ACTION) ==
                getIntObjVar(player, ORIGINAL_ACTION) &&
            getAttrib(player, QUICKNESS) ==
                getIntObjVar(player, ORIGINAL_QUICKNESS) &&
            getPerformanceType(player) ==
                getIntObjVar(player, ORIGINAL_PERFORMANCE);
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
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        return lifecycle != null &&
            lifecycle.matches("[A-Za-z0-9_-]{8,64}");
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        setObjVar(player, PULSE_PASSED, 0);
        setObjVar(player, LOOP_COST, -1);
        setObjVar(player, LOOP_REMAINING, -1);
        setObjVar(player, LOOP_BOUNDARY, 0);
        setObjVar(player, FLOURISH_COST, -1);
        setObjVar(player, FLOURISH_REMAINING, -1);
        setObjVar(player, FLOURISH_BOUNDARY, 0);
        setObjVar(player, RESTORED, 0);
    }

    private int readInt(obj_id player, String key)
        throws InterruptedException
    {
        return hasObjVar(player, key) ? getIntObjVar(player, key) : -1;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        return
            "player=" + player +
            " loopCost=" + readInt(player, LOOP_COST) +
            " loopRemaining=" + readInt(player, LOOP_REMAINING) +
            " loopBoundaryRejected=" +
                (readInt(player, LOOP_BOUNDARY) == 1) +
            " flourishCost=" + readInt(player, FLOURISH_COST) +
            " flourishRemaining=" +
                readInt(player, FLOURISH_REMAINING) +
            " flourishBoundaryRejected=" +
                (readInt(player, FLOURISH_BOUNDARY) == 1) +
            " restored=" + (readInt(player, RESTORED) == 1) +
            " action=" + getAttrib(player, ACTION) +
            " quickness=" + getAttrib(player, QUICKNESS) +
            " performance=" + getPerformanceType(player);
    }
}
