package script.test;

import script.obj_id;
import script.library.pgc_quests;

/**
 * Identity-bound proof that later Player Generated Chronicles quest
 * completion cannot mutate restored Publish 14.1 experience pools.
 */
public class precu_pgc_quest_xp_retirement_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String[] XP_TYPES =
    {
        "combat_general",
        "crafting_general",
        "entertainer"
    };

    public String executeProbe(String params) throws InterruptedException
    {
        long value;
        try
        {
            value = Long.parseLong(params == null ? "" : params.trim());
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (value != PLAYER_OID)
        {
            return "error=identityNotAllowed";
        }
        obj_id player = obj_id.getObjId(value);
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded() || !player.isAuthoritative() ||
            !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        int[] before = snapshot(player);
        String result = "error=probeIncomplete";
        try
        {
            pgc_quests.grantPgcNonChroniclesQuestXp(
                player,
                90,
                25.0f,
                obj_id.NULL_ID,
                -1);
            int[] after = snapshot(player);
            boolean unchanged = equal(before, after);
            result =
                "action=probe authoritative=true" +
                " before=" + join(before) +
                " after=" + join(after) +
                " unchanged=" + unchanged +
                " passed=" + unchanged;
        }
        finally
        {
            int[] current = snapshot(player);
            for (int index = 0; index < XP_TYPES.length; ++index)
            {
                int delta = before[index] - current[index];
                if (delta != 0)
                {
                    grantExperiencePoints(player, XP_TYPES[index], delta);
                }
            }
            boolean restored = equal(before, snapshot(player));
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=pgcXpRestoreFailed " + result;
            }
        }
        return result;
    }

    private int[] snapshot(obj_id player) throws InterruptedException
    {
        int[] values = new int[XP_TYPES.length];
        for (int index = 0; index < XP_TYPES.length; ++index)
        {
            values[index] = getExperiencePoints(player, XP_TYPES[index]);
        }
        return values;
    }

    private boolean equal(int[] left, int[] right)
    {
        if (left.length != right.length)
        {
            return false;
        }
        for (int index = 0; index < left.length; ++index)
        {
            if (left[index] != right[index])
            {
                return false;
            }
        }
        return true;
    }

    private String join(int[] values)
    {
        String result = "";
        for (int index = 0; index < values.length; ++index)
        {
            result += (index == 0 ? "" : ",") + values[index];
        }
        return result;
    }
}
