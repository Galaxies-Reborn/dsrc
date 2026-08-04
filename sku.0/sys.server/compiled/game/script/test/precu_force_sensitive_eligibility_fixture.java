package script.test;

import script.obj_id;
import script.library.jedi;

/**
 * Identity-bound, reversible ServerConsole fixture for the Publish 14.1
 * Force-sensitive eligibility boundary. It transitions only the native Jedi
 * state and restores the exact original value before returning.
 */
public class precu_force_sensitive_eligibility_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String CRYSTAL_TUNING_SKILL =
        "force_title_jedi_rank_01";

    public String executeProbe(String params) throws InterruptedException
    {
        long playerValue;
        try
        {
            playerValue = Long.parseLong(params == null ? "" : params.trim());
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID)
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player))
        {
            return "error=playerNotAuthoritative";
        }
        if (getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=stationNotAllowed";
        }

        int originalState = getJediState(player);
        boolean forceSensitiveBefore = jedi.isForceSensitive(player);
        boolean tuningSkillBefore = hasSkill(player, CRYSTAL_TUNING_SKILL);
        boolean canTuneBefore = jedi.canTuneLightsaberCrystal(player);
        boolean temporaryTuningSkill = false;
        String result = "error=probeIncomplete";

        try
        {
            if (!setJediState(player, JEDI_STATE_FORCE_SENSITIVE))
            {
                result = "error=forceSensitiveStateRejected";
            }
            else
            {
                int forceSensitiveState = getJediState(player);
                boolean forceSensitiveDuring = jedi.isForceSensitive(player);
                boolean levelGateDuring =
                    jedi.isForceSensitiveLevelRequired(player, 1);

                if (!setJediState(player, JEDI_STATE_JEDI))
                {
                    result = "error=jediStateRejected";
                }
                else
                {
                    int jediState = getJediState(player);
                    boolean jediCountsAsForceSensitive =
                        jedi.isForceSensitive(player);

                    if (!tuningSkillBefore)
                    {
                        temporaryTuningSkill =
                            grantSkill(player, CRYSTAL_TUNING_SKILL) &&
                            hasSkill(player, CRYSTAL_TUNING_SKILL);
                    }
                    boolean tuningSkillDuring =
                        hasSkill(player, CRYSTAL_TUNING_SKILL);
                    boolean canTuneDuring =
                        jedi.canTuneLightsaberCrystal(player);

                    if (!tuningSkillBefore && !temporaryTuningSkill)
                    {
                        result = "error=tuningSkillGrantFailed";
                    }
                    else
                    {
                        result =
                            "action=probe" +
                            " authoritative=true" +
                            " originalState=" + originalState +
                            " forceSensitiveBefore=" + forceSensitiveBefore +
                            " forceSensitiveState=" + forceSensitiveState +
                            " forceSensitiveDuring=" + forceSensitiveDuring +
                            " levelGateDuring=" + levelGateDuring +
                            " jediState=" + jediState +
                            " jediCountsAsForceSensitive=" +
                                jediCountsAsForceSensitive +
                            " tuningSkillBefore=" + tuningSkillBefore +
                            " canTuneBefore=" + canTuneBefore +
                            " tuningSkillDuring=" + tuningSkillDuring +
                            " canTuneDuring=" + canTuneDuring;
                    }
                }
            }
        }
        finally
        {
            if (temporaryTuningSkill)
            {
                revokeSkill(player, CRYSTAL_TUNING_SKILL);
            }
            boolean restoreAccepted = setJediState(player, originalState);
            int restoredState = getJediState(player);
            boolean restoredEligibility = jedi.isForceSensitive(player);
            boolean restoredTuningSkill =
                hasSkill(player, CRYSTAL_TUNING_SKILL);
            boolean restored =
                restoreAccepted &&
                restoredState == originalState &&
                restoredEligibility == forceSensitiveBefore &&
                restoredTuningSkill == tuningSkillBefore;
            result +=
                " restoredState=" + restoredState +
                " restoredEligibility=" + restoredEligibility +
                " restoredTuningSkill=" + restoredTuningSkill +
                " restored=" + restored;
            if (!restored)
            {
                result = "error=jediStateRestoreFailed " + result;
            }
        }
        return result;
    }
}
