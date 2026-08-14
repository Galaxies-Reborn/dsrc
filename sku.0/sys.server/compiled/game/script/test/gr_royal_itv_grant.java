package script.test;

import script.obj_id;

/** Reversible, identity-bound live grant for the Royal ITV control test. */
public class gr_royal_itv_grant extends script.base_script
{
    private static final long PLAYER_OID = 1433054682L;
    private static final String COMMAND = "callforroyalpickup";

    public String executeFixture(String params) throws InterruptedException
    {
        obj_id player = obj_id.getObjId(PLAYER_OID);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player))
        {
            return "error=playerNotAuthoritative";
        }
        String action = params == null ? "status" : params.trim();
        if (action.equalsIgnoreCase("grant"))
        {
            boolean granted = hasCommand(player, COMMAND) || grantCommand(player, COMMAND);
            return "action=grant granted=" + granted + " hasCommand=" + hasCommand(player, COMMAND);
        }
        if (action.equalsIgnoreCase("revoke"))
        {
            while (hasCommand(player, COMMAND))
            {
                revokeCommand(player, COMMAND);
            }
            return "action=revoke hasCommand=" + hasCommand(player, COMMAND);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return "action=status hasCommand=" + hasCommand(player, COMMAND);
        }
        return "usage: grant|status|revoke";
    }
}
