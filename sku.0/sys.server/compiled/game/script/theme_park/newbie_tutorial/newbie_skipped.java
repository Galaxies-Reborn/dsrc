package script.theme_park.newbie_tutorial;

import script.dictionary;
import script.library.features;
import script.library.money;
import script.library.utils;
import script.location;
import script.obj_id;

public class newbie_skipped extends script.theme_park.newbie_tutorial.tutorial_base
{
    public static final String STARTING_LOCATION_SELECTION_OPEN = "newbie.startingLocationSelectionOpen";
    public static final String STARTING_LOCATION_TRANSFER_PENDING = "newbie.startingLocationTransferPending";
    public static final String STARTING_LOCATION_TRANSFER_NAME = "newbie.startingLocationTransferName";
    public static final String STARTING_LOCATION_TRANSFER_POLLS = "newbie.startingLocationTransferPolls";
    public static final int STARTING_LOCATION_TRANSFER_MAX_POLLS = 30;
    public newbie_skipped()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (features.isSpaceEdition(self))
        {
            setObjVar(self, "jtlNewbie", 4);
        }
        deleteInventory(self);
        fillbank(self);
        grantNewbieStartingMoney(self);
        setObjVar(self, "newbie.oathCompleted", true);
        String skillName = getStringObjVar(self, "newbie.hasSkill");
        if (skillName != null)
        {
            if (!hasSkill(self, skillName))
            {
                grantSkill(self, skillName);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnDetach(obj_id self) throws InterruptedException
    {
        sendStartingMessage(self);
        newbieTutorialEnableHudElement(self, "all", true, 0.0f);
        obj_id playerInv = utils.getInventoryContainer(self);
        obj_id[] contents = getContents(playerInv);
        for (obj_id content : contents) {
            if (hasObjVar(content, "newbie.item")) {
                if (hasScript(content, BOX_ITEM_SCRIPT)) {
                    detachScript(content, BOX_ITEM_SCRIPT);
                }
                destroyObject(content);
            }
        }
        transferBankToInventory(self);
        for (String boxContent : BOX_CONTENTS) {
            createObject(boxContent, playerInv, "");
        }
        obj_id pInv = utils.getInventoryContainer(self);
        contents = getContents(pInv);
        if (contents != null)
        {
            for (int i = 0; i < contents.length; i++)
            {
            }
        }
        contents = getContents(self);
        if (contents != null)
        {
            for (int i = 0; i < contents.length; i++)
            {
            }
        }
        removeObjVar(self, "newbie");
        removeObjVar(self, "skipTutorial");
        utils.removeScriptVar(self, STARTING_LOCATION_SELECTION_OPEN);
        removeObjVar(self, STARTING_LOCATION_TRANSFER_PENDING);
        removeObjVar(self, STARTING_LOCATION_TRANSFER_NAME);
        removeObjVar(self, STARTING_LOCATION_TRANSFER_POLLS);
        removeObjVar(self, "banking_bankid");
        return SCRIPT_CONTINUE;
    }
    public int xferFailed(obj_id self, dictionary params) throws InterruptedException
    {
        grantNewbieStartingMoney(self);
        return SCRIPT_CONTINUE;
    }
    public int timeToWithdraw(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            messageTo(self, "xferFailed", null, 0, false);
            return SCRIPT_CONTINUE;
        }
        int amt = params.getInt(money.DICT_AMOUNT);
        withdrawCashFromBank(self, amt, "cashReceived", "xferFailed", params);
        return SCRIPT_CONTINUE;
    }
    public int cashReceived(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int newbieRequestStartingLocations(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if ("tutorial".equals(getLocation(self).area) && utils.hasScriptVar(self, STARTING_LOCATION_SELECTION_OPEN) && !hasObjVar(self, STARTING_LOCATION_TRANSFER_PENDING))
        {
            // The travel terminal is the sole authority that opens this gate.
            sendThoseStartLocs(self);
        }
        return SCRIPT_CONTINUE;
    }
    public int newbieSelectStartingLocation(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (hasObjVar(self, STARTING_LOCATION_TRANSFER_PENDING) || !utils.hasScriptVar(self, STARTING_LOCATION_SELECTION_OPEN))
        {
            newbieTutorialSendStartingLocationSelectionResult(self, params, false);
            return SCRIPT_CONTINUE;
        }
        utils.removeScriptVar(self, STARTING_LOCATION_SELECTION_OPEN);
        if (!sendToStartLocation(self, params))
        {
            utils.setScriptVar(self, STARTING_LOCATION_SELECTION_OPEN, true);
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, STARTING_LOCATION_TRANSFER_PENDING, true);
        setObjVar(self, STARTING_LOCATION_TRANSFER_NAME, params);
        setObjVar(self, STARTING_LOCATION_TRANSFER_POLLS, 0);
        messageTo(self, "handleEndTutorial", null, 1.0f, false);
        return SCRIPT_CONTINUE;
    }
    public int handleEndTutorial(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, STARTING_LOCATION_TRANSFER_PENDING))
        {
            return SCRIPT_CONTINUE;
        }
        location loc = getLocation(self);
        if (!loc.area.equals("tutorial"))
        {
            removeObjVar(self, "newbie.startSkippedTutorial");
            newbieTutorialEnableHudElement(self, "all", true, 0.0f);
            detachScript(self, "theme_park.newbie_tutorial.newbie_skipped");
            return SCRIPT_CONTINUE;
        }
        int polls = getIntObjVar(self, STARTING_LOCATION_TRANSFER_POLLS);
        if (polls >= STARTING_LOCATION_TRANSFER_MAX_POLLS)
        {
            String selection = getStringObjVar(self, STARTING_LOCATION_TRANSFER_NAME);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_PENDING);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_NAME);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_POLLS);
            utils.setScriptVar(self, STARTING_LOCATION_SELECTION_OPEN, true);
            newbieTutorialSendStartingLocationSelectionResult(self, selection, false);
            sendThoseStartLocs(self);
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, STARTING_LOCATION_TRANSFER_POLLS, polls + 1);
        messageTo(self, "handleEndTutorial", null, 1.0f, false);
        return SCRIPT_CONTINUE;
    }
    public int OnLogin(obj_id self) throws InterruptedException
    {
        location loc = getLocation(self);
        String area = loc.area;
        if (!area.equals("tutorial"))
        {
            removeObjVar(self, "newbie.startSkippedTutorial");
            newbieTutorialEnableHudElement(self, "all", true, 0.0f);
            detachScript(self, "theme_park.newbie_tutorial.newbie_skipped");
        }
        else if (hasObjVar(self, STARTING_LOCATION_TRANSFER_PENDING))
        {
            utils.removeScriptVar(self, STARTING_LOCATION_SELECTION_OPEN);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_PENDING);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_NAME);
            removeObjVar(self, STARTING_LOCATION_TRANSFER_POLLS);
        }
        return SCRIPT_CONTINUE;
    }
}
