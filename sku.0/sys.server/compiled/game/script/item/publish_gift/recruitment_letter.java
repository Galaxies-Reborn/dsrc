package script.item.publish_gift;

import script.library.factions;
import script.menu_info;
import script.obj_id;
import script.string_id;

public class recruitment_letter extends script.base_script
{
    public recruitment_letter()
    {
    }
    public static final string_id USE_LETTER = new string_id("gcw", "use_pub_gift_recruitment_letter");
    public static final string_id SID_NOT_ALLIGNED = new string_id("gcw", "must_be_factionally_alligned");
    public static final int LETTER_POINT_VALUE = 10000;
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        // This later publish gift awarded the retired NGE GCW-point currency.
        // Preserve the authored item, but do not offer a destructive action
        // that cannot provide a Publish 14 faction-standing/rank reward.
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        // Fail closed for a radial selection queued before PRE-CU retirement.
        // The gift remains intact and no obsolete point-success log is emitted.
        return SCRIPT_CONTINUE;
    }
    public boolean isOwner(obj_id self, obj_id player) throws InterruptedException
    {
        return getOwner(self) == player;
    }
    public boolean isAlligned(obj_id player) throws InterruptedException
    {
        String faction = factions.getFaction(player);
        if (faction.equals("Rebel") || faction.equals("Imperial"))
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
}
