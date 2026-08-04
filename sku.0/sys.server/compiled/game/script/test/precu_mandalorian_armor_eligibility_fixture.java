package script.test;

import script.obj_id;
import script.library.utils;

/**
 * Identity-bound, reversible proof of the four Publish 14.1 Mandalorian armor
 * master-profession alternatives.
 */
public class precu_mandalorian_armor_eligibility_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String[] MASTER_SKILLS =
    {
        "combat_bountyhunter_master",
        "combat_commando_master",
        "outdoors_squadleader_master",
        "outdoors_ranger_master"
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

        boolean[] originallyOwned = new boolean[MASTER_SKILLS.length];
        for (int index = 0; index < MASTER_SKILLS.length; ++index)
        {
            originallyOwned[index] = hasSkill(player, MASTER_SKILLS[index]);
        }
        String result = "error=probeIncomplete";
        try
        {
            for (int index = 0; index < MASTER_SKILLS.length; ++index)
            {
                if (originallyOwned[index])
                {
                    revokeSkill(player, MASTER_SKILLS[index]);
                }
            }
            boolean baselineRejected = !utils.hasSpecialSkills(player);
            boolean alternativesPassed = true;
            String vector = "";
            for (int index = 0; index < MASTER_SKILLS.length; ++index)
            {
                boolean granted =
                    grantSkill(player, MASTER_SKILLS[index]) &&
                    hasSkill(player, MASTER_SKILLS[index]);
                boolean admitted = granted && utils.hasSpecialSkills(player);
                revokeSkill(player, MASTER_SKILLS[index]);
                boolean rejected =
                    !hasSkill(player, MASTER_SKILLS[index]) &&
                    !utils.hasSpecialSkills(player);
                alternativesPassed =
                    alternativesPassed && granted && admitted && rejected;
                vector +=
                    (index == 0 ? "" : ",") +
                    MASTER_SKILLS[index] + ":" +
                    granted + "/" + admitted + "/" + rejected;
            }
            boolean passed = baselineRejected && alternativesPassed;
            result =
                "action=probe authoritative=true" +
                " baselineRejected=" + baselineRejected +
                " alternatives=" + vector +
                " alternativesPassed=" + alternativesPassed +
                " passed=" + passed;
        }
        finally
        {
            boolean restored = true;
            for (int index = 0; index < MASTER_SKILLS.length; ++index)
            {
                boolean owned = hasSkill(player, MASTER_SKILLS[index]);
                if (originallyOwned[index] && !owned)
                {
                    grantSkill(player, MASTER_SKILLS[index]);
                }
                else if (!originallyOwned[index] && owned)
                {
                    revokeSkill(player, MASTER_SKILLS[index]);
                }
                restored =
                    restored &&
                    hasSkill(player, MASTER_SKILLS[index]) ==
                        originallyOwned[index];
            }
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=mandalorianRestoreFailed " + result;
            }
        }
        return result;
    }
}
