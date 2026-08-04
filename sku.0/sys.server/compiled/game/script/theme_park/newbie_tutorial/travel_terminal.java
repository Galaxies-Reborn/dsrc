package script.theme_park.newbie_tutorial;

import script.*;
import script.library.utils;
import script.library.weapons;

public class travel_terminal extends script.theme_park.newbie_tutorial.tutorial_base
{
    public travel_terminal()
    {
    }
    public static final string_id SID_USE_MENU = new string_id("sui", "use");
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        mi.addRootMenu(menu_info_types.ITEM_USE, SID_USE_MENU);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            if (!hasScript(player, NEWBIE_SCRIPT_SKIPPED))
            {
                if (!hasObjVar(player, "newbie.talkedtojedi"))
                {
                    string_id TALK_TO_JEDI = new string_id(NEWBIE_STRING_FILE, "talk_to_jedi");
                    sendSystemMessage(player, TALK_TO_JEDI);
                    return SCRIPT_CONTINUE;
                }
                else 
                {
                    removeObjVar(player, "newbie.talkedtojedi");
                }
            }
            leaveTutorial(self, player);
        }
        return SCRIPT_CONTINUE;
    }
    public void leaveTutorial(obj_id self, obj_id player) throws InterruptedException
    {
        removeObjVar(player, "banking_bankid");
        removeStaticWaypoint(self);
        if (hasObjVar(self, "newbie.skipped"))
        {
            if (!hasScript(player, NEWBIE_SCRIPT_SKIPPED))
            {
                attachScript(player, NEWBIE_SCRIPT_SKIPPED);
            }
        }
        setLookAtTarget(player, null);
        transferBankToInventory(player);
        obj_id currentWeapon = getCurrentWeapon(player);
        if (hasObjVar(currentWeapon, "newbie.item"))
        {
            destroyObject(currentWeapon);
        }
        obj_id playerInv = utils.getInventoryContainer(player);
        obj_id[] contents = getContents(playerInv);
        for (obj_id content : contents) {
            if (isWeapon(content)) {
                String templateName = getTemplateName(content);
                obj_id weapon = weapons.createWeapon(templateName, playerInv, 0.75f);
                equip(weapon, player);
                destroyObject(content);
                break;
            }
        }
        if (hasObjVar(self, "newbie.skipped"))
        {
            if (!hasObjVar(player, newbie_skipped.STARTING_LOCATION_TRANSFER_PENDING))
            {
                utils.setScriptVar(player, newbie_skipped.STARTING_LOCATION_SELECTION_OPEN, true);
                sendThoseStartLocs(player);
            }
        }
        else
        {
            sendThoseStartLocs(player);
        }
    }
}
