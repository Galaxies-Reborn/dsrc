package script.terminal;

import script.*;
import script.library.gcw;
import script.library.sui;
import script.library.utils;

public class terminal_gcw_publish_gift extends script.terminal.base.base_terminal
{
    public terminal_gcw_publish_gift()
    {
    }
    public static final String VAR_TERMINAL_STATUS = "gcw.static_base.terminal_status";
    public static final String VAR_BASE_STATUS = "gcw.static_base.base_status";
    public static final String BASE_BEING_CAPTURED = " TERMINAL STATUS: BEING CAPTURED\n";
    public static final String BASE_SECURED = " TERMINAL STATUS: BASE SECURED\n";
    public static final String PID_NAME = "gcw_terminal.pid";
    public static final int FACTION_NEUTRAL = 0;
    public static final int FACTION_IMPERIAL = 1;
    public static final int FACTION_REBEL = 2;
    public static final int MINIMUM_TIME_FOR_UPDATE = 30;
    public static final string_id SID_IN_HOUSE_ONLY = new string_id("gcw", "gcw_terminal_used_in_house_only");
    public static final string_id SID_IN_INVENTORY_ONLY = new string_id("gcw", "gcw_intelpad_used_in_inventory_only");
    public static final String WAR_INTELPAD_TEMPLATE = "object/tangible/droid/war_intel_datapad.iff";
    public static final String WAR_TERMINAL_TEMPLATE = "object/tangible/terminal/terminal_gcw_publish_gift.iff";
    public static final string_id SID_MENU_GCW = new string_id("gcw", "gcw_war_terminal_menu");
    public static final string_id SID_MENU_GCW_REPORT = new string_id("gcw", "gcw_report_war_terminal_menu");
    public static final string_id SID_MENU_GCW_FACTIONAL_PRESENCE = new string_id("gcw", "gcw_factional_presence_war_terminal_menu");
    public static final String[] STAIC_BASE_PLANETS = 
    {
        "corellia",
        "naboo",
        "talus"
    };
    public static final String[][] PLANETS_DATA = 
    {
        
        {
            "corellia",
            "dantooine",
            "lok",
            "naboo",
            "rori",
            "talus",
            "tatooine"
        },
        
        {
            "CORELLIA",
            "DANTOOINE",
            "LOK",
            "NABOO",
            "RORI",
            "TALUS",
            "TATOOINE"
        }
    };
    private void retirePostNgeWarTerminalState(obj_id self, obj_id player) throws InterruptedException
    {
        // The GU10 queue and the later 30-day GCW point-contribution ledger are
        // retired. Preserve the terminal/report asset and planetary presentation,
        // but scrub cached actions and contribution UI state from older sessions.
        if (isValidId(self) && hasObjVar(self, "gcwWarIntelPadMostRecentAction"))
        {
            int action = getIntObjVar(self, "gcwWarIntelPadMostRecentAction");
            if (action == menu_info_types.SERVER_MENU1 || action == menu_info_types.SERVER_MENU3)
            {
                removeObjVar(self, "gcwWarIntelPadMostRecentAction");
            }
        }
        if (!isValidId(player))
        {
            return;
        }
        if (utils.hasScriptVar(player, "gcw.gcwPersonalContributionTablePid"))
        {
            int existingPid = utils.getIntScriptVar(player, "gcw.gcwPersonalContributionTablePid");
            utils.removeScriptVar(player, "gcw.gcwPersonalContributionTablePid");
            forceCloseSUIPage(existingPid);
        }
        obj_id playerObject = getPlayerObject(player);
        if (isValidId(playerObject))
        {
            utils.removeLocalVar(playerObject, "gcwContributionTrackingLastUpdated");
            utils.removeLocalVar(playerObject, "gcwContributionTrackingColumnName");
            utils.removeLocalVar(playerObject, "gcwContributionTrackingColumnType");
            utils.removeLocalVar(playerObject, "gcwContributionTrackingColumn0");
            utils.removeLocalVar(playerObject, "gcwContributionTrackingColumn1");
        }
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        retirePostNgeWarTerminalState(self, player);
        menu_info_data data = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (!isGod(player))
        {
            if (utils.isNestedWithinAPlayer(self) && (getTemplateName(self)).equals(WAR_TERMINAL_TEMPLATE))
            {
                sendSystemMessage(player, SID_IN_HOUSE_ONLY);
                return super.OnObjectMenuRequest(self, player, mi);
            }
            else if (!utils.isNestedWithinAPlayer(self) && (getTemplateName(self)).equals(WAR_INTELPAD_TEMPLATE))
            {
                sendSystemMessage(player, SID_IN_INVENTORY_ONLY);
                return super.OnObjectMenuRequest(self, player, mi);
            }
        }
        int gcwMenu = mi.addRootMenu(menu_info_types.SERVER_MENU5, SID_MENU_GCW);
        mi.addSubMenu(gcwMenu, menu_info_types.SERVER_MENU6, SID_MENU_GCW_REPORT);
        mi.addSubMenu(gcwMenu, menu_info_types.SERVER_MENU2, SID_MENU_GCW_FACTIONAL_PRESENCE);
        // The post-NGE city/guild regional-defender report is not part of Publish 14.
        updateGCWInfo(self);
        return super.OnObjectMenuRequest(self, player, mi);
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        final boolean isIntelPad = (getTemplateName(self)).equals(WAR_INTELPAD_TEMPLATE);
        retirePostNgeWarTerminalState(self, player);
        if (!isGod(player))
        {
            if (utils.isNestedWithinAPlayer(self) && (getTemplateName(self)).equals(WAR_TERMINAL_TEMPLATE))
            {
                sendSystemMessage(player, SID_IN_HOUSE_ONLY);
                return SCRIPT_CONTINUE;
            }
            else if (!utils.isNestedWithinAPlayer(self) && isIntelPad)
            {
                sendSystemMessage(player, SID_IN_INVENTORY_ONLY);
                return SCRIPT_CONTINUE;
            }
        }
        if (isIntelPad)
        {
            if (item == menu_info_types.ITEM_USE)
            {
                if (hasObjVar(self, "gcwWarIntelPadMostRecentAction"))
                {
                    final int mostRecentAction = getIntObjVar(self, "gcwWarIntelPadMostRecentAction");
                    if (mostRecentAction > 0)
                    {
                        item = mostRecentAction;
                    }
                }
            }
        }
        if (item == menu_info_types.ITEM_USE)
        {
            openSui(player);
        }
        else if (item == menu_info_types.SERVER_MENU6)
        {
            if (isIntelPad)
            {
                setObjVar(self, "gcwWarIntelPadMostRecentAction", item);
            }
            openSui(player);
        }
        else if (item == menu_info_types.SERVER_MENU1 || item == menu_info_types.SERVER_MENU3)
        {
            // Fail closed for a stale radial selection or cached datapad action.
            retirePostNgeWarTerminalState(self, player);
            return SCRIPT_CONTINUE;
        }
        else if (item == menu_info_types.SERVER_MENU2)
        {
            if (isIntelPad)
            {
                setObjVar(self, "gcwWarIntelPadMostRecentAction", item);
            }
            if (utils.hasScriptVar(player, "gcw.factionalPresenceTablePid"))
            {
                int existingPid = utils.getIntScriptVar(player, "gcw.factionalPresenceTablePid");
                utils.removeScriptVar(player, "gcw.factionalPresenceTablePid");
                forceCloseSUIPage(existingPid);
            }
            int newPid = -1;
            dictionary dict = getGcwFactionalPresenceTableDictionary();
            if (dict != null)
            {
                String[] columnHeader = dict.getStringArray("column");
                if ((columnHeader != null) && (columnHeader.length > 0))
                {
                    String[] columnHeaderType = dict.getStringArray("columnType");
                    if ((columnHeaderType != null) && (columnHeaderType.length > 0) && (columnHeaderType.length == columnHeader.length))
                    {
                        boolean validColumnData = true;
                        String[][] columnData = new String[columnHeader.length][0];
                        for (int i = 0; i < columnHeader.length; ++i)
                        {
                            columnData[i] = dict.getStringArray("column" + i);
                            if ((columnData[i] == null) || (columnData[i].length <= 0))
                            {
                                validColumnData = false;
                                break;
                            }
                        }
                        if (validColumnData)
                        {
                            newPid = sui.tableColumnMajor(player, player, sui.OK_ONLY, "@gcw:gcw_factional_presence_sui_table_title", "onGcwFactionalPresenceTableDictionaryResponse", "@gcw:gcw_factional_presence_sui_table_header", columnHeader, columnHeaderType, columnData);
                        }
                    }
                }
            }
            if (newPid > 0)
            {
                utils.setScriptVar(player, "gcw.factionalPresenceTablePid", newPid);
            }
            else 
            {
                sendSystemMessage(player, "There is currently no factional presence activity in any GCW region.", "");
            }
        }
        else if (item == menu_info_types.SERVER_MENU4)
        {
            if (utils.hasScriptVar(player, "gcw.gcwRegionDefenderTablePid"))
            {
                final int existingPid = utils.getIntScriptVar(player, "gcw.gcwRegionDefenderTablePid");
                utils.removeScriptVar(player, "gcw.gcwRegionDefenderTablePid");
                forceCloseSUIPage(existingPid);
            }
            if (utils.hasScriptVar(player, "gcw.gcwRegionDefenderDetailsTablePid"))
            {
                final int existingPid = utils.getIntScriptVar(player, "gcw.gcwRegionDefenderDetailsTablePid");
                utils.removeScriptVar(player, "gcw.gcwRegionDefenderDetailsTablePid");
                forceCloseSUIPage(existingPid);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnClusterWideDataResponse(obj_id self, String strCategory, String strSubCategory, int intRequestId, String[] strElementNames, dictionary[] dctData, int intLockKey) throws InterruptedException
    {
        blog("OnClusterWideDataResponse: " + strSubCategory);
        final int imperialPlanetaryScore = getGcwGroupImperialScorePercentile(strSubCategory);
        utils.setScriptVar(self, "Imperial." + strSubCategory + ".controlScore", imperialPlanetaryScore);
        utils.setScriptVar(self, "Rebel." + strSubCategory + ".controlScore", (100 - imperialPlanetaryScore));
        return SCRIPT_CONTINUE;
    }
    public int refreshSuiGcwData(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!isValidId(player))
        {
            return SCRIPT_CONTINUE;
        }
        blog("player id: " + player);
        if (!sui.hasPid(player, PID_NAME))
        {
            return SCRIPT_CONTINUE;
        }
        blog("player has a pid scriptvar");
        int currentPid = sui.getPid(player, PID_NAME);
        blog("currentPid: " + currentPid);
        int callingPid = params.getInt("callingPid");
        blog("callingPid: " + callingPid);
        if (callingPid != currentPid)
        {
            return SCRIPT_CONTINUE;
        }
        String gcwData = getGcwData(self, player);
        if (gcwData == null || gcwData.equals(""))
        {
            return SCRIPT_CONTINUE;
        }
        setSUIProperty(currentPid, "Prompt.lblPrompt", "LocalText", gcwData);
        flushSUIPage(currentPid);
        messageTo(self, "refreshSuiGcwData", params, 10, false);
        return SCRIPT_CONTINUE;
    }
    public int closeSui(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        if (!isValidId(player))
        {
            return SCRIPT_CONTINUE;
        }
        sui.removePid(player, PID_NAME);
        return SCRIPT_CONTINUE;
    }
    public boolean updateGCWInfo(obj_id self) throws InterruptedException
    {
        blog("in updateGCWInfo");
        if (!utils.hasScriptVar(self, "lastUpdate"))
        {
            utils.setScriptVar(self, "lastUpdate", getGameTime());
        }
        else 
        {
            int lastUpdateTime = utils.getIntScriptVar(self, "lastUpdate");
            int timePassed = getGameTime() - lastUpdateTime;
            blog("timePassed: " + timePassed);
            if (timePassed < MINIMUM_TIME_FOR_UPDATE)
            {
                return false;
            }
        }
        for (int i = 0; i < PLANETS_DATA[0].length; i++)
        {
            blog("in loop planet: " + PLANETS_DATA[0][i]);
            String strPlanet = PLANETS_DATA[0][i];
            int requestId = getClusterWideData("gcw", strPlanet, false, self);
        }
        return true;
    }
    public void openSui(obj_id player) throws InterruptedException
    {
        obj_id self = getSelf();
        String gcwData = getGcwData(self, player);
        if ((gcwData != null) && (!gcwData.equals("")))
        {
            createCustomUI(self, player, gcwData);
        }
    }
    public String getGcwData(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(player))
        {
            return null;
        }
        blog("in getGcwData");
        String gcwData = "";
        if ((getTemplateName(self)).equals(WAR_INTELPAD_TEMPLATE))
        {
            int gcwPlanetIdx = checkScene(self, player);
            if (gcwPlanetIdx < 0)
            {
                return sui.newLine() + "GCW Data not available on this planet. " + sui.newLine() + "Currently monitoring: Corellia, Dantooine, Lok, Naboo, Rori, Talus or Tatooine.";
            }
            String planetName = PLANETS_DATA[1][gcwPlanetIdx];
            String planet = PLANETS_DATA[0][gcwPlanetIdx];
            obj_id planetId = getPlanetByName(planet);
            if (isGod(player))
            {
                planetName += " ( " + planetId + " )";
            }
            gcwData += getSpecificPlanetData(self, player, planetId, gcwData, planet, planetName);
            int calTime = getCalendarTime();
            String strCalTime = getCalendarTimeStringLocal(calTime);
            gcwData += "Last Updated: " + strCalTime;
            return gcwData;
        }
        gcwData += "Galactic Civil War Data by Planet:\n\n";
        for (int i = 0; i < PLANETS_DATA[0].length; i++)
        {
            String planetName = PLANETS_DATA[1][i];
            String planet = PLANETS_DATA[0][i];
            obj_id planetId = getPlanetByName(planet);
            if (isGod(player))
            {
                planetName += " ( " + planetId + " )";
            }
            gcwData = getSpecificPlanetData(self, player, planetId, gcwData, planet, planetName);
        }
        int calTime = getCalendarTime();
        String strCalTime = getCalendarTimeStringLocal(calTime);
        gcwData += "Last Updated: " + strCalTime;
        return gcwData;
    }
    public String getPlayerBaseData(obj_id player, obj_id planetId, int maxBasesOnPlanet) throws InterruptedException
    {
        if (!isValidId(planetId))
        {
            return null;
        }
        if (maxBasesOnPlanet <= 0)
        {
            return null;
        }
        String allBaseData = "";
        String treePrefix = ".hq_";
        dictionary baseDic = new dictionary();
        int faction = 0;
        String factionName = "";
        location loc = new location();
        for (int i = 1; i < maxBasesOnPlanet; i++)
        {
            baseDic = utils.getDictionaryScriptVar(planetId, gcw.VAR_BASE_HACK_DICTIONARY + treePrefix + i);
            if (baseDic == null)
            {
                continue;
            }
            else 
            {
                if (isGod(player))
                {
                    allBaseData += " BASE ID: " + baseDic.getObjId("id") + sui.newLine();
                }
                faction = baseDic.getInt("faction");
                switch (faction)
                {
                    case gcw.FACTION_REBEL:
                    factionName = "REBEL";
                    break;
                    case gcw.FACTION_IMPERIAL:
                    factionName = "IMPERIAL";
                    break;
                    default:
                    factionName = "ERROR";
                    break;
                }
                loc = baseDic.getLocation("location");
                int x = (int)loc.x;
                int y = (int)loc.y;
                int z = (int)loc.z;
                allBaseData += " " + factionName + " Player Base reports alarm at: " + x + " " + y + " " + z + sui.newLine();
            }
        }
        return allBaseData;
    }
    public String getTerminalData(int[] terminalArray) throws InterruptedException
    {
        if (terminalArray == null || terminalArray.length == 0)
        {
            return null;
        }
        int terminal_one = terminalArray[0];
        for (int i1 : terminalArray) {
            if (terminal_one != i1) {
                return BASE_BEING_CAPTURED;
            }
        }
        return BASE_SECURED;
    }
    public String getStaticBaseData(int status) throws InterruptedException
    {
        if (status < 0)
        {
            return null;
        }
        String prefix = " ";
        switch (status)
        {
            case FACTION_NEUTRAL:
            return prefix + "NEUTRAL\n";
            case FACTION_REBEL:
            return prefix + "REBEL\n";
            case FACTION_IMPERIAL:
            return prefix + "IMPERIAL\n";
            default:
            return null;
        }
    }
    public void createCustomUI(obj_id self, obj_id player, String combinedString) throws InterruptedException
    {
        if (!isValidId(self) || !isValidId(player))
        {
            return;
        }
        dictionary params = new dictionary();
        params.put("player", player);
        blog("in createCustomUI");
        closeOldWindow(player);
        String uiTitle = "GCW Report";
        int pid = createSUIPage("/Script.messageBox", self, player);
        setSUIAssociatedLocation(pid, self);
        setSUIMaxRangeToObject(pid, 8);
        params.put("callingPid", pid);
        sui.setPid(player, pid, PID_NAME);
        setSUIProperty(pid, "Prompt.lblPrompt", "LocalText", combinedString);
        setSUIProperty(pid, "bg.caption.lblTitle", "Text", uiTitle);
        setSUIProperty(pid, "Prompt.lblPrompt", "Editable", "false");
        setSUIProperty(pid, "Prompt.lblPrompt", "GetsInput", "true");
        setSUIProperty(pid, "btnCancel", "Visible", "false");
        setSUIProperty(pid, "btnRevert", "Visible", "false");
        setSUIProperty(pid, "btnOk", sui.PROP_TEXT, "Close");
        subscribeToSUIEvent(pid, sui_event_type.SET_onClosedOk, "%button0%", "closeSui");
        subscribeToSUIEvent(pid, sui_event_type.SET_onClosedCancel, "%button0%", "closeSui");
        showSUIPage(pid);
        flushSUIPage(pid);
        messageTo(self, "refreshSuiGcwData", params, 10, false);
    }
    public void closeOldWindow(obj_id player) throws InterruptedException
    {
        int pid = sui.getPid(player, PID_NAME);
        if (pid > -1)
        {
            forceCloseSUIPage(pid);
            sui.removePid(player, PID_NAME);
        }
    }
    public int checkScene(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(self) || !isValidId(player))
        {
            return -1;
        }
        String scene = getCurrentSceneName();
        int gcwPlanetIndex = -1;
        for (int i = 0; i < PLANETS_DATA[0].length; i++)
        {
            if (PLANETS_DATA[0][i].equals(scene))
            {
                gcwPlanetIndex = i;
            }
        }
        return gcwPlanetIndex;
    }
    public String getSpecificPlanetData(obj_id self, obj_id player, obj_id planetId, String gcwData, String planet, String planetName) throws InterruptedException
    {
        if (!isValidId(self) || !exists(self) || !isValidId(player) || !exists(player) || !isValidId(planetId) || !exists(planetId))
        {
            return null;
        }
        else if (planet == null || planet.equals("") || planetName == null || planetName.equals(""))
        {
            return null;
        }
        int maxPlanet = gcw.getCurrentPlanetaryFactionBaseMaxPlanet(planet);
        gcwData += sui.colorGreen() + planetName + sui.colorWhite() + sui.newLine();
        String planetScoreReb = "Unknown";
        String planetScoreImp = "Unknown";
        if (utils.hasScriptVar(self, "Rebel." + planet + ".controlScore"))
        {
            planetScoreReb = "" + utils.getIntScriptVar(self, "Rebel." + planet + ".controlScore");
        }
        if (utils.hasScriptVar(self, "Imperial." + planet + ".controlScore"))
        {
            planetScoreImp = "" + utils.getIntScriptVar(self, "Imperial." + planet + ".controlScore");
        }
        gcwData += sui.colorGreen() + " Planetary Score Rebel: " + sui.colorWhite() + planetScoreReb + sui.newLine();
        gcwData += sui.colorGreen() + " Planetary Score Imperial: " + sui.colorWhite() + planetScoreImp + sui.newLine();
        gcwData += sui.colorBlue() + " Total Player Bases: " + sui.colorWhite() + gcw.getCurrentPlanetaryFactionBaseCountPlanet(planet) + sui.newLine();
        gcwData += sui.colorBlue() + " Total Player Base Max: " + sui.colorWhite() + maxPlanet + sui.newLine();
        gcwData += sui.colorBlue() + " Total Imperial Bases: " + sui.colorWhite() + gcw.getImperialBaseCount(planetId) + sui.newLine();
        gcwData += sui.colorBlue() + " Total Alliance Bases: " + sui.colorWhite() + gcw.getRebelBaseCount(planetId) + sui.newLine();
        if (utils.hasScriptVarTree(planetId, gcw.VAR_BASE_HACK_DICTIONARY))
        {
            gcwData += sui.colorRed() + getPlayerBaseData(player, planetId, maxPlanet) + sui.colorWhite();
        }
        if (planet.equals("corellia") || planet.equals("naboo") || planet.equals("talus"))
        {
            gcwData += sui.colorOrange();
            if (utils.hasScriptVar(planetId, VAR_BASE_STATUS))
            {
                gcwData += "GCW BASE: " + getStaticBaseData(utils.getIntScriptVar(planetId, VAR_BASE_STATUS));
                if (utils.hasScriptVar(planetId, VAR_TERMINAL_STATUS))
                {
                    gcwData += getTerminalData(utils.getIntArrayScriptVar(planetId, VAR_TERMINAL_STATUS));
                }
            }
            gcwData += sui.colorWhite();
        }
        gcwData += sui.newLine();
        return gcwData;
    }
    public boolean blog(String msg) throws InterruptedException
    {
        return true;
    }
}
