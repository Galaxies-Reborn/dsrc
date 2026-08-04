package script.test;

import script.obj_id;
import script.library.content;
import script.library.utils;

/**
 * Reversible live proof for exact-skill and compatibility-token profession
 * requirements.
 */
public class precu_profession_requirement_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ARTISAN = "crafting_artisan_novice";
    private static final String ENTERTAINER = "social_entertainer_novice";
    private static final String LIFEDAY_DRIVER =
        "test.precu_lifeday_2004_fixture";
    private static final String LIFEDAY_ACTION =
        "precu.fixture.lifeday.active";

    public String executeProbe(String params) throws InterruptedException
    {
        if (params != null && params.startsWith("lifeday-on:"))
        {
            return executeLifeDayLifecycle(params.substring(11), true);
        }
        if (params != null && params.startsWith("lifeday-off:"))
        {
            return executeLifeDayLifecycle(params.substring(12), false);
        }
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

        boolean artisanBefore = hasSkill(player, ARTISAN);
        boolean entertainerBefore = hasSkill(player, ENTERTAINER);
        int jediStateBefore = getJediState(player);
        String result = "error=probeIncomplete";
        try
        {
            if (artisanBefore)
            {
                revokeSkill(player, ARTISAN);
            }
            if (entertainerBefore)
            {
                revokeSkill(player, ENTERTAINER);
            }
            setJediState(player, JEDI_STATE_NONE);

            boolean emptyRejected =
                !utils.meetsProfessionRequirement(player, "");
            boolean unknownRejected =
                !utils.meetsProfessionRequirement(
                    player, "class_engineering_phase1_master");
            boolean spyRejected =
                !utils.meetsProfessionRequirement(player, "spy");
            boolean crafterRejected = !content.isCrafter(player);
            boolean entertainerRejected = !content.isEntertainer(player);
            boolean forceRejected =
                !utils.meetsProfessionRequirement(
                    player, "force_sensitive");

            boolean artisanGranted =
                grantSkill(player, ARTISAN) && hasSkill(player, ARTISAN);
            boolean exactArtisanAdmitted =
                utils.meetsProfessionRequirement(player, ARTISAN);
            boolean traderAdmitted =
                utils.meetsProfessionRequirement(player, "trader");
            boolean crafterAdmitted = content.isCrafter(player);

            boolean entertainerGranted =
                grantSkill(player, ENTERTAINER) &&
                hasSkill(player, ENTERTAINER);
            boolean exactEntertainerAdmitted =
                utils.meetsProfessionRequirement(player, ENTERTAINER);
            boolean entertainerTokenAdmitted =
                utils.meetsProfessionRequirement(player, "entertainer");
            boolean contentEntertainerAdmitted =
                content.isEntertainer(player);

            boolean forceStateAccepted =
                setJediState(player, JEDI_STATE_FORCE_SENSITIVE);
            boolean forceAdmitted =
                forceStateAccepted &&
                utils.meetsProfessionRequirement(
                    player, "force_sensitive");

            boolean passed =
                emptyRejected &&
                unknownRejected &&
                spyRejected &&
                crafterRejected &&
                entertainerRejected &&
                forceRejected &&
                artisanGranted &&
                exactArtisanAdmitted &&
                traderAdmitted &&
                crafterAdmitted &&
                entertainerGranted &&
                exactEntertainerAdmitted &&
                entertainerTokenAdmitted &&
                contentEntertainerAdmitted &&
                forceAdmitted;
            result =
                "action=probe authoritative=true" +
                " emptyRejected=" + emptyRejected +
                " unknownRejected=" + unknownRejected +
                " spyRejected=" + spyRejected +
                " crafterRejected=" + crafterRejected +
                " entertainerRejected=" + entertainerRejected +
                " forceRejected=" + forceRejected +
                " exactArtisanAdmitted=" + exactArtisanAdmitted +
                " traderAdmitted=" + traderAdmitted +
                " crafterAdmitted=" + crafterAdmitted +
                " exactEntertainerAdmitted=" +
                    exactEntertainerAdmitted +
                " entertainerTokenAdmitted=" +
                    entertainerTokenAdmitted +
                " contentEntertainerAdmitted=" +
                    contentEntertainerAdmitted +
                " forceAdmitted=" + forceAdmitted +
                " passed=" + passed;
        }
        finally
        {
            restoreSkill(player, ARTISAN, artisanBefore);
            restoreSkill(player, ENTERTAINER, entertainerBefore);
            boolean stateRestored =
                setJediState(player, jediStateBefore) &&
                getJediState(player) == jediStateBefore;
            boolean restored =
                stateRestored &&
                hasSkill(player, ARTISAN) == artisanBefore &&
                hasSkill(player, ENTERTAINER) == entertainerBefore;
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=requirementRestoreFailed " + result;
            }
        }
        return result;
    }

    private String executeLifeDayLifecycle(String params, boolean active)
        throws InterruptedException
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
        obj_id player = obj_id.getObjId(value);
        if (value != PLAYER_OID ||
            player == null || player == obj_id.NULL_ID ||
            !player.isLoaded() || !player.isAuthoritative() ||
            !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }
        setObjVar(player, LIFEDAY_ACTION, active ? 1 : 0);
        if (hasScript(player, LIFEDAY_DRIVER))
        {
            detachScript(player, LIFEDAY_DRIVER);
        }
        attachScript(player, LIFEDAY_DRIVER);
        return "action=" + (active ? "activate" : "deactivate") +
            " driverQueued=true passed=" + hasScript(player, LIFEDAY_DRIVER);
    }

    private void restoreSkill(
        obj_id player,
        String skillName,
        boolean originallyOwned) throws InterruptedException
    {
        boolean owned = hasSkill(player, skillName);
        if (originallyOwned && !owned)
        {
            grantSkill(player, skillName);
        }
        else if (!originallyOwned && owned)
        {
            revokeSkill(player, skillName);
        }
    }
}
