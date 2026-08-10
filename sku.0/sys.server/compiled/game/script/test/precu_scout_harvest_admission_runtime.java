package script.test;

import script.obj_id;

/**
 * Identity-bound, state-free live probe for the native Novice Scout harvest
 * admission gate. The production queueCommand path reaches CommandQueue before
 * corpse or resource validation, so an invalid target is intentional here.
 */
public class precu_scout_harvest_admission_runtime extends script.base_script
{
    private static final long PLAYER_OID = 1433054682L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String NOVICE_SCOUT = "outdoors_scout_novice";
    private static final String HARVEST_COMMAND = "harvestCorpse";
    private static final String USAGE = "usage: probe <playerOid>";

    public String executeProbe(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 2 || !args[0].equalsIgnoreCase("probe") ||
            !args[1].matches("[0-9]+"))
            return USAGE;

        long playerOid;
        try
        {
            playerOid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (playerOid != PLAYER_OID)
            return "error=playerIdentityRejected";

        obj_id player = obj_id.getObjId(playerOid);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
            return "error=playerUnavailable";
        if (!player.isAuthoritative())
            return "error=playerNotAuthoritative process=" +
                player.getProcessId();
        if (!isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
            return "error=playerIdentityRejected";

        boolean noviceScoutOwned = hasSkill(player, NOVICE_SCOUT);
        boolean commandOwned = hasCommand(player, HARVEST_COMMAND);
        if (noviceScoutOwned)
            return "error=noviceScoutUnexpectedlyOwned";

        boolean queued = queueCommand(
            player,
            getStringCrc(HARVEST_COMMAND.toLowerCase()),
            obj_id.NULL_ID,
            "meat",
            COMMAND_PRIORITY_DEFAULT);

        return "action=probe authoritative=true player=" + player +
            " stationId=" + getPlayerStationId(player) +
            " noviceScoutOwned=" + noviceScoutOwned +
            " commandOwned=" + commandOwned +
            " queued=" + queued +
            " command=" + HARVEST_COMMAND +
            " target=0 params=meat stateFree=true";
    }
}
