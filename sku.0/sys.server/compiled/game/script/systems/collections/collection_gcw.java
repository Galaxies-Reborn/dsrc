package script.systems.collections;

import script.*;
import script.library.sui;

public class collection_gcw extends script.base_script
{
    public collection_gcw()
    {
    }
    public static final String PID_NAME = "gcw_consume";
    public static final string_id SID_GCW_CONSUME_PROMPT = new string_id("collection", "consume_gcw_prompt");
    public static final string_id SID_GCW_CONSUME_TITLE = new string_id("collection", "consume_gcw_title");
    public static final string_id SID_GCW_CONSUME_ITEM = new string_id("collection", "consume_gcw_item");
    public static final string_id WRONG_FACTION = new string_id("collection", "wrong_faction");
    public static final string_id USED_ITEM = new string_id("collection", "gcw_point_item_used");
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        // These later collection items awarded the retired NGE GCW point and
        // weekly-rating currency. Preserve the authored items, but do not offer
        // a destructive consume action that cannot award a PRE-CU reward.
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handlerSuiGrantGcwPoints(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
        {
            return SCRIPT_CONTINUE;
        }
        // Fail closed for a callback queued before the PRE-CU retirement. The
        // item must remain intact and no obsolete point-success message is sent.
        sui.removePid(player, PID_NAME);
        return SCRIPT_CONTINUE;
    }
}
