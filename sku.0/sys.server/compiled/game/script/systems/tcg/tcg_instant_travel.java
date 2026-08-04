package script.systems.tcg;

import script.*;
import script.library.*;

public class tcg_instant_travel extends script.base_script
{
    public tcg_instant_travel()
    {
    }
    public static final String PID_VAR = "tcg_instant_travel";
    public static final string_id SID_WHILE_DEAD = new string_id("spam", "while_dead");

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE stationary instant-travel menu request for " + player);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE stationary instant-travel selection for " + player);
        return SCRIPT_CONTINUE;
    }
    public void markItvLocations(obj_id itv, obj_id player) throws InterruptedException
    {
        if (!isIdValid(itv) || !isIdValid(player))
        {
            return;
        }
        if (sui.hasPid(player, PID_VAR))
        {
            int pid = sui.getPid(player, PID_VAR);
            forceCloseSUIPage(pid);
        }
        String[] main_options = new String[2];
        if (hasObjVar(itv, "travel_tcg.stationary_itv.name.1"))
        {
            main_options[0] = "Reset " + getStringObjVar(itv, "travel_tcg.stationary_itv.name.1");
        }
        else 
        {
            main_options[0] = "Set Location 1";
        }
        if (hasObjVar(itv, "travel_tcg.stationary_itv.name.2"))
        {
            main_options[1] = "Reset " + getStringObjVar(itv, "travel_tcg.stationary_itv.name.2");
        }
        else 
        {
            main_options[1] = "Set Location 2";
        }
        int pid = sui.listbox(itv, player, "@tcg:stationary_set_location_d", sui.OK_CANCEL, "@tcg:stationary_set_location_t", main_options, "handlePlayerNameSetLocation", true, true);
        sui.setPid(player, pid, PID_VAR);
    }
    public int handlePlayerNameSetLocation(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (sui.hasPid(player, PID_VAR))
        {
            int pid = sui.getPid(player, PID_VAR);
            forceCloseSUIPage(pid);
        }
        if (!isIdValid(player))
        {
            if (sui.hasPid(player, PID_VAR))
            {
                int pid = sui.getPid(player, PID_VAR);
                forceCloseSUIPage(pid);
            }
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params);
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            if (sui.hasPid(player, PID_VAR))
            {
                int pid = sui.getPid(player, PID_VAR);
                forceCloseSUIPage(pid);
            }
            return SCRIPT_CONTINUE;
        }
        if (idx < 0)
        {
            if (sui.hasPid(player, PID_VAR))
            {
                int pid = sui.getPid(player, PID_VAR);
                forceCloseSUIPage(pid);
            }
            return SCRIPT_CONTINUE;
        }
        switch (idx)
        {
            case 0:
            if (canMarkAtLocation(player))
            {
                int pid = sui.filteredInputbox(self, player, "@tcg:prompt_stationary_itv_location_set", "@tcg:title_stationary_itv_location_set", "handleSetItvLocations", "");
                sui.setPid(player, pid, PID_VAR);
                utils.setScriptVar(player, "travel.itv.number", 1);
            }
            else 
            {
                sendSystemMessage(player, new string_id("tcg", "invalid_location_for_location_itv"));
            }
            break;
            case 1:
            if (canMarkAtLocation(player))
            {
                int pid = sui.filteredInputbox(self, player, "@tcg:prompt_stationary_itv_location_set", "@tcg:title_stationary_itv_location_set", "handleSetItvLocations", "");
                sui.setPid(player, pid, PID_VAR);
                utils.setScriptVar(player, "travel.itv.number", 2);
            }
            else 
            {
                sendSystemMessage(player, new string_id("tcg", "invalid_location_for_location_itv"));
            }
            break;
            default:
            if (sui.hasPid(player, PID_VAR))
            {
                int pid = sui.getPid(player, PID_VAR);
                forceCloseSUIPage(pid);
            }
            break;
        }
        return SCRIPT_CONTINUE;
    }
    public int handleSetItvLocations(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isIdValid(player))
        {
            utils.removeScriptVar(player, "travel.itv.number");
            return SCRIPT_CONTINUE;
        }
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            utils.removeScriptVar(player, "travel.itv.number");
            return SCRIPT_CONTINUE;
        }
        String markName = sui.getInputBoxText(params);
        if (markName == null || markName.length() == 0)
        {
            sendSystemMessage(self, new string_id("tcg", "not_valid_location_name"));
            utils.removeScriptVar(player, "travel.itv.number");
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(player, "travel.itv.number"))
        {
            return SCRIPT_CONTINUE;
        }
        int n = utils.getIntScriptVar(player, "travel.itv.number");
        utils.removeScriptVar(player, "travel.itv.number");
        if (canMarkAtLocation(player))
        {
            obj_id tcg_itv = getSelf();
            location markLocation = getLocation(player);
            String markScene = getCurrentSceneName();
            if (canMarkAtLocation(player) && markLocation != null && markScene != null)
            {
                setObjVar(tcg_itv, "travel_tcg.stationary_itv.location." + n, markLocation);
                setObjVar(tcg_itv, "travel_tcg.stationary_itv.scene." + n, markScene);
                setObjVar(tcg_itv, "travel_tcg.stationary_itv.name." + n, markName);
                sendSystemMessage(player, new string_id("tcg", "valid_location_set"));
            }
        }
        else 
        {
            sendSystemMessage(player, new string_id("tcg", "invalid_location_for_location_itv"));
        }
        return SCRIPT_CONTINUE;
    }
    public void retrieveLocationsList(obj_id itv, obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(itv))
        {
            return;
        }
        String[] main_options = new String[2];
        if (hasObjVar(itv, "travel_tcg.stationary_itv.name.1") && hasObjVar(itv, "travel_tcg.stationary_itv.location.1") && hasObjVar(itv, "travel_tcg.stationary_itv.scene.1"))
        {
            String name = getStringObjVar(itv, "travel_tcg.stationary_itv.name.1");
            String planet = getStringObjVar(itv, "travel_tcg.stationary_itv.scene.1");
            location loc = getLocationObjVar(itv, "travel_tcg.stationary_itv.location.1");
            String locString = getDisplayLocation(loc);
            main_options[0] = "Travel to: " + name + "  Planet: " + planet + " Location: " + locString;
        }
        else 
        {
            main_options[0] = "Unmarked Location";
        }
        if (hasObjVar(itv, "travel_tcg.stationary_itv.name.2") && hasObjVar(itv, "travel_tcg.stationary_itv.location.2") && hasObjVar(itv, "travel_tcg.stationary_itv.scene.2"))
        {
            String name = getStringObjVar(itv, "travel_tcg.stationary_itv.name.2");
            String planet = getStringObjVar(itv, "travel_tcg.stationary_itv.scene.2");
            location loc = getLocationObjVar(itv, "travel_tcg.stationary_itv.location.2");
            String locString = getDisplayLocation(loc);
            main_options[1] = "Travel to: " + name + "  Planet: " + planet + " Location: " + locString;
        }
        else 
        {
            main_options[1] = "Unmarked Location";
        }
        int pid = sui.listbox(itv, player, "@tcg:stationary_travel_d", sui.OK_CANCEL, "@tcg:stationary_travel_t", main_options, "handleSendPlayerToLocation", true, true);
        sui.setPid(player, pid, PID_VAR);
        return;
    }
    public int handleSendPlayerToLocation(obj_id self, dictionary params) throws InterruptedException
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
        int bp = sui.getIntButtonPressed(params);
        if (bp == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (!itvIsInRangeOfPlayer(self, player))
        {
            sendSystemMessage(player, new string_id("tcg", "itv_out_of_range"));
            return SCRIPT_CONTINUE;
        }
        int idx = sui.getListboxSelectedRow(params) + 1;
        if (idx > 0)
        {
            utils.setScriptVar(player, "travel.stationary_itv.index", idx);
        }
        sui.msgbox(self, player, "@tcg:stationary_travel_confirm_prompt", sui.OK_CANCEL, "@tcg:stationary_travel_confirm_title", sui.MSG_QUESTION, "handleConfirmStationaryTravel");
        return SCRIPT_CONTINUE;
    }
    public int handleConfirmStationaryTravel(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Rejected retired NGE stationary instant-travel warp");
        return SCRIPT_CONTINUE;
    }
    public void sendPlayerToLocation(obj_id player, int idx) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Rejected retired NGE stationary instant-travel location warp for " + player);
        return;
    }
    public boolean canMarkAtLocation(obj_id player) throws InterruptedException
    {
        obj_id playerCurrentMount = getMountId(player);
        if (isIdValid(playerCurrentMount))
        {
            return false;
        }
        if (isIdValid(structure.getContainingBuilding(player)))
        {
            return false;
        }
        if (isSpaceScene())
        {
            return false;
        }
        if (space_dungeon.verifyPlayerSession(player))
        {
            return false;
        }
        location here = getLocation(player);
        if (locations.isInCity(here))
        {
            return false;
        }
        region geoCities[] = getRegionsWithGeographicalAtPoint(here, regions.GEO_CITY);
        if (geoCities != null && geoCities.length > 0)
        {
            return false;
        }
        region[] regionList = getRegionsWithPvPAtPoint(here, regions.PVP_REGION_TYPE_ADVANCED);
        if (regionList != null && regionList.length > 0)
        {
            return false;
        }
        String planet = getCurrentSceneName();
        if (planet.startsWith("kashyyyk") || planet.equals("adventure1") || planet.equals("adventure2"))
        {
            return false;
        }
        if (combat.isInCombat(player))
        {
            return false;
        }
        return true;
    }
    public boolean isOwner(obj_id object, obj_id player) throws InterruptedException
    {
        return getOwner(object) == player;
    }
    public String getDisplayLocation(location loc) throws InterruptedException
    {
        String returnString = "" + Math.round(loc.x) + ", " + Math.round(loc.y) + ", " + Math.round(loc.z);
        return returnString;
    }
    public boolean itvIsInRangeOfPlayer(obj_id itv, obj_id player) throws InterruptedException
    {
        if (!isIdValid(itv) || !isIdValid(player))
        {
            return false;
        }
        if (!utils.isNestedWithin(itv, player))
        {
            if (utils.getDistance2D(itv, player) > 40)
            {
                return false;
            }
        }
        return true;
    }
}
