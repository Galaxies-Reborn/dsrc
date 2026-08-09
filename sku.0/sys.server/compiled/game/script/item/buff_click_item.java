package script.item;

import script.*;
import script.library.buff;
import script.library.collection;
import script.library.prose;
import script.library.static_item;
import script.library.utils;

public class buff_click_item extends script.base_script
{
    public buff_click_item()
    {
    }
    public static final string_id SID_NOT_YET = new string_id("base_player", "not_yet");
    public static final string_id SID_NOT_LINKED = new string_id("base_player", "not_linked");
    public static final string_id SID_NOT_LINKED_TO_HOLDER = new string_id("base_player", "not_linked_to_holder");
    public static final string_id CANT_APPLY_BUFF = new string_id("base_player", "cant_apply_buff");
    public static final string_id BUFF_APPLIED = new string_id("base_player", "buff_applied");
    public static final string_id SID_ITEM_NOT_IN_INVENTORY = new string_id("base_player", "not_in_your_inventory");
    public static final string_id SID_MUST_BIO_LINK_FROM_INVENTORY = new string_id("base_player", "must_biolink_to_use_from_inventory");
    public static final string_id SID_BIOLINK_OTHER_PLAYER = new string_id("base_player", "wrong_player_per_biolink");
    public static final string_id SID_NO_USE_WHILE_DEAD = new string_id("player_structure", "while_dead");
    public static final string_id SID_BUFF_NOT_OWNER = new string_id("base_player", "food_buff_not_owner");
    public static final String OWNER_OID = "owner";
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true))
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
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (utils.getContainingPlayer(self) != player)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id biolink = getBioLink(self);
        if (isValidId(biolink) && biolink == utils.OBJ_ID_BIO_LINK_PENDING)
        {
            sendSystemMessage(player, SID_MUST_BIO_LINK_FROM_INVENTORY);
            return SCRIPT_CONTINUE;
        }
        if (isValidId(biolink) && biolink != player)
        {
            sendSystemMessage(player, SID_BIOLINK_OTHER_PLAYER);
            return SCRIPT_CONTINUE;
        }
        else if (hasObjVar(self, OWNER_OID))
        {
            if (player != getObjIdObjVar(self, OWNER_OID))
            {
                sendSystemMessage(player, SID_BUFF_NOT_OWNER);
                return SCRIPT_CONTINUE;
            }
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (isIncapacitated(player) || isDead(player))
            {
                sendSystemMessage(player, SID_NO_USE_WHILE_DEAD);
                return SCRIPT_CONTINUE;
            }
            if (hasScript(self, "item.armor.biolink_item_non_faction"))
            {
                obj_id bioLinked = getBioLink(self);
                if (bioLinked == null || bioLinked == utils.OBJ_ID_BIO_LINK_PENDING)
                {
                    sendSystemMessage(player, SID_NOT_LINKED);
                    return SCRIPT_CONTINUE;
                }
                if (bioLinked != player)
                {
                    sendSystemMessage(player, SID_NOT_LINKED_TO_HOLDER);
                    return SCRIPT_CONTINUE;
                }
            }
            String itemName = getStaticItemName(self);
            if (itemName == null || itemName.equals(""))
            {
                CustomerServiceLog("buff", "buff_click_item object self: " + self + " Name: " + getName(self) + " had an invalid static item name. Buff object is bailing out early as a result.");
                return SCRIPT_CONTINUE;
            }
            dictionary itemData = new dictionary();
            itemData = dataTableGetRow(static_item.ITEM_STAT_BALANCE_TABLE, itemName);
            if (itemData == null)
            {
                CustomerServiceLog("buff", "buff_click_item object self: " + self + " Name: " + getName(self) + " had invalid item data and as a result the buff object is bailing out early.");
                return SCRIPT_CONTINUE;
            }
            String buffName = itemData.getString("buff_name");
            String coolDownGroup = itemData.getString("cool_down_group");
            String clientEffect = itemData.getString("client_effect");
            String clientAnimation = itemData.getString("client_animation");
            int reuseTime = itemData.getInt("reuse_time");
            String varName = "clickItem." + coolDownGroup;
            int buffTime = getIntObjVar(player, varName);
            if (getGameTime() > buffTime || getGameTime() < buffTime && isGod(player))
            {
                if (buff.isRetiredPostNgePlayerInstantXpGrantBuffName(buffName))
                {
                    grantPrecuTcgInstantXpReplacement(self, player, itemName, clientEffect, clientAnimation);
                    return SCRIPT_CONTINUE;
                }
                if (buff.canApplyBuff(player, buffName))
                {
                    if (getGameTime() < buffTime && isGod(player))
                    {
                        sendSystemMessage(player, "The Buff was applied because you were in god mode.", null);
                    }
                    CustomerServiceLog("buff", "buff_click_item object self: " + self + " Static Item Name: " + itemName + " providing buff: " + buffName + " being used by player: " + player + " Name: " + getName(player));
                    buff.applyBuff(player, player, buffName);
                    setObjVar(player, varName, (getGameTime() + (reuseTime)));
                    sendCooldownGroupTimingOnly(player, getStringCrc(coolDownGroup.toLowerCase()), reuseTime);
                    sendSystemMessage(player, BUFF_APPLIED);
                    doAnimationAction(player, clientAnimation);
                    playClientEffectObj(player, clientEffect, player, "");
                    if (getCount(self) > 0)
                    {
                        CustomerServiceLog("buff", "buff_click_item object self: " + self + " Static Item Name: " + itemName + " providing buff: " + buffName + " being used by player: " + player + " Name: " + getName(player) + ". Object is being decremented by ONE.");
                        static_item.decrementStaticItem(self);
                    }
                }
                else 
                {
                    sendSystemMessage(player, CANT_APPLY_BUFF);
                    return SCRIPT_CONTINUE;
                }
            }
            else 
            {
                int timeDiff = buffTime - getGameTime();
                prose_package pp = prose.getPackage(SID_NOT_YET, timeDiff);
                sendSystemMessageProse(player, pp);
                return SCRIPT_CONTINUE;
            }
        }
        return SCRIPT_CONTINUE;
    }
    public void grantPrecuTcgInstantXpReplacement(obj_id self, obj_id player, String itemName, String clientEffect, String clientAnimation) throws InterruptedException
    {
        obj_id collectionItem = collection.grantRandomCollectionItem(player,
            "datatables/loot/loot_items/collectible/magseal_loot.iff", "collections");
        if (!isValidId(collectionItem) || !exists(collectionItem))
        {
            CustomerServiceLog("buff", "buff_click_item object self: " + self +
                " Static Item Name: " + itemName + " used by player: " + player +
                " Name: " + getName(player) +
                ". A PRE-CU collection replacement could not be delivered, so the item was not consumed.");
            sendSystemMessage(player, CANT_APPLY_BUFF);
            return;
        }
        if (clientAnimation != null && !clientAnimation.equals(""))
        {
            doAnimationAction(player, clientAnimation);
        }
        if (clientEffect != null && !clientEffect.equals(""))
        {
            playClientEffectObj(player, clientEffect, player, "");
        }
        CustomerServiceLog("buff", "buff_click_item object self: " + self +
            " Static Item Name: " + itemName + " used by player: " + player +
            " Name: " + getName(player) + ". PRE-CU collection item " + collectionItem +
            " was received by the player; the consumable is being decremented by one.");
        if (getCount(self) > 0)
        {
            static_item.decrementStaticItem(self);
        }
    }
}
