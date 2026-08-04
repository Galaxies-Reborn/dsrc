package script.test;

import script.obj_id;
import script.library.pet_lib;

/**
 * Identity-bound, reversible proof that later Trader class and combat-level
 * state no longer raise droid combat-module display tiers or the NGE droid cap.
 */
public class precu_droid_class_bonus_retirement_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String DROID_ENGINEER_NOVICE =
        "crafting_droidengineer_novice";

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

        boolean originallyOwned = hasSkill(player, DROID_ENGINEER_NOVICE);
        String result = "error=probeIncomplete";
        try
        {
            if (originallyOwned)
            {
                revokeSkill(player, DROID_ENGINEER_NOVICE);
            }
            int[] baseline =
            {
                pet_lib.getDroidModuleCommandLevel(player, 0),
                pet_lib.getDroidModuleCommandLevel(player, 49),
                pet_lib.getDroidModuleCommandLevel(player, 50),
                pet_lib.getDroidModuleCommandLevel(player, 100),
                pet_lib.getDroidCapLevel(player, 90)
            };
            boolean granted =
                grantSkill(player, DROID_ENGINEER_NOVICE) &&
                hasSkill(player, DROID_ENGINEER_NOVICE);
            int[] owned =
            {
                pet_lib.getDroidModuleCommandLevel(player, 0),
                pet_lib.getDroidModuleCommandLevel(player, 49),
                pet_lib.getDroidModuleCommandLevel(player, 50),
                pet_lib.getDroidModuleCommandLevel(player, 100),
                pet_lib.getDroidCapLevel(player, 90)
            };
            boolean passed =
                granted &&
                baseline[0] == 0 &&
                baseline[1] == 1 &&
                baseline[2] == 1 &&
                baseline[3] == 1 &&
                baseline[4] == 60 &&
                owned[0] == baseline[0] &&
                owned[1] == baseline[1] &&
                owned[2] == baseline[2] &&
                owned[3] == baseline[3] &&
                owned[4] == baseline[4];
            result =
                "action=probe authoritative=true" +
                " baseline=" + join(baseline) +
                " droidEngineerOwned=" + join(owned) +
                " granted=" + granted +
                " passed=" + passed;
        }
        finally
        {
            boolean owned = hasSkill(player, DROID_ENGINEER_NOVICE);
            if (originallyOwned && !owned)
            {
                grantSkill(player, DROID_ENGINEER_NOVICE);
            }
            else if (!originallyOwned && owned)
            {
                revokeSkill(player, DROID_ENGINEER_NOVICE);
            }
            boolean restored =
                hasSkill(player, DROID_ENGINEER_NOVICE) == originallyOwned;
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=droidBonusRestoreFailed " + result;
            }
        }
        return result;
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
