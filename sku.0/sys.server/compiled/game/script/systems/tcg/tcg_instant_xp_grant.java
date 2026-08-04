package script.systems.tcg;

import script.library.collection;
import script.library.utils;
import script.*;

public class tcg_instant_xp_grant extends script.base_script
{
    public tcg_instant_xp_grant()
    {
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (utils.isNestedWithinAPlayer(self))
        {
            menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
            if (mid != null)
            {
                mid.setServerNotify(true);
            }
            else
            {
                mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("ui_radial", "item_use"));
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (utils.getContainingPlayer(self) != player)
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (hasObjVar(self, "grant_xp_percent"))
            {
                // Publish 14.1 has no class-template level or percentage-to-next-level XP.
                // Preserve the retained TCG item through its authored level-cap fallback.
                obj_id collectionItem = collection.grantRandomCollectionItem(player, "datatables/loot/loot_items/collectible/magseal_loot.iff", "collections");
                if (!isValidId(collectionItem) || !exists(collectionItem))
                {
                    CustomerServiceLog("tcg", "TCG Item (" + self + ") used by player: (" + player + ")" + getFirstName(player) + ". A PRE-CU collection replacement could not be delivered, so the item was not consumed.");
                    return SCRIPT_CONTINUE;
                }
                playClientEffectObj(player, "clienteffect/tcg_t16_skyhopper_toy_flyby.cef", player, "root");
                CustomerServiceLog("tcg", "TCG Item (" + self + ") used by player: (" + player + ")" + getFirstName(player) + ". PRE-CU collection item (" + collectionItem + ") was received by the player.");
                decrementCount(self);
            }
            else
            {
                CustomerServiceLog("tcg", "Player " + getFirstName(player) + "(" + player + ") attempted to use TCG Item(" + self + ") but can not - item is missing 'grant_xp_percent' objvar)");
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
}
