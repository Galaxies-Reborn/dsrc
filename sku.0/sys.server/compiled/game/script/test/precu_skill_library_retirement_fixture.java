package script.test;

import script.obj_id;
import script.library.expertise;
import script.library.skill;

/**
 * Identity-bound proof that residual level-stat and NGE expertise helpers
 * cannot reintroduce later-era progression into the Publish 14.1 runtime.
 */
public class precu_skill_library_retirement_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;

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

        String[] beforeExpertise = expertise.getExpertiseAllocation(player);
        int beforeCount =
            beforeExpertise == null ? 0 : beforeExpertise.length;
        boolean validateResult = skill.validateExpertise(player);
        String[] afterExpertise = expertise.getExpertiseAllocation(player);
        int afterCount = afterExpertise == null ? 0 : afterExpertise.length;
        int healthAtOne =
            skill.getPlayerStatForLevel(player, 1, "health");
        int healthAtNinety =
            skill.getPlayerStatForLevel(player, 90, "health");
        int luckAtNinety =
            skill.getPlayerStatForLevel(player, 90, "luck");

        skill.sendlevelUpStatChangeSystemMessages(player, 89, 90);

        boolean passed =
            !validateResult &&
            afterCount == 0 &&
            healthAtOne == 0 &&
            healthAtNinety == 0 &&
            luckAtNinety == 0;
        return
            "action=probe authoritative=true" +
            " expertiseBefore=" + beforeCount +
            " expertiseAfter=" + afterCount +
            " validateResult=" + validateResult +
            " healthLevel1=" + healthAtOne +
            " healthLevel90=" + healthAtNinety +
            " luckLevel90=" + luckAtNinety +
            " passed=" + passed;
    }
}
