package script.event;

import script.dictionary;
import script.library.holiday;
import script.library.utils;
import script.obj_id;

public class holiday_controller extends script.base_script
{
    public holiday_controller()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        CustomerServiceLog("holidayEvent", "holiday_controller.OnInitialize planet initialized, holiday controller called.");
        halloweenServerStart(self, null);
        messageTo(self, "lifedayServerStart", null, 610.0f, false);
        lovedayServerStart(self, null);
        empiredayServerStart(self, null);
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        halloweenServerStart(self, null);
        messageTo(self, "lifedayServerStart", null, 730.0f, false);
        lovedayServerStart(self, null);
        empiredayServerStart(self, null);
        return SCRIPT_CONTINUE;
    }
    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (!isGod(player))
        {
            return SCRIPT_CONTINUE;
        }
        String lifedayRunning = getConfigSetting("GameServer", "lifeday");
        names[idx] = "holiday_event";
        attribs[idx] = "Halloween Event Running = False (Publish 14.1)";
        idx++;
        if (lifedayRunning != null && (lifedayRunning.equals("true") || lifedayRunning.equals("1")))
        {
            names[idx] = "holiday_event";
            attribs[idx] = "Life Day Event Running = True";
            idx++;
        }
        else 
        {
            names[idx] = "holiday_event";
            attribs[idx] = "Life Day Event Running = False";
            idx++;
        }
        names[idx] = "holiday_event";
        attribs[idx] = "Love Day Event Running = False (Publish 14.1)";
        idx++;
        names[idx] = "holiday_event";
        attribs[idx] = "Empire Day Event Running = False (Publish 14.1)";
        return SCRIPT_CONTINUE;
    }
    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (!isGod(speaker))
        {
            return SCRIPT_CONTINUE;
        }

        String universeWideEvents = getCurrentUniverseWideEvents();
        int lifeday = universeWideEvents.indexOf("lifeday");
        String lifedayRunning = getConfigSetting("GameServer", "lifeday");
        switch (text) {
            case "halloweenStart":
                retireLaterHolidayEvent(speaker, "halloween", true);
                return SCRIPT_OVERRIDE;
            case "halloweenStop":
                retireLaterHolidayEvent(speaker, "halloween", false);
                return SCRIPT_OVERRIDE;
            case "halloweenStartForReals":
                retireLaterHolidayEvent(speaker, "halloween", true);
                return SCRIPT_OVERRIDE;
            case "halloweenStopForReals":
                retireLaterHolidayEvent(speaker, "halloween", false);
                return SCRIPT_OVERRIDE;
            case "lifedayStart":
                startHolidayEvent(speaker, "lifeday", lifedayRunning, lifeday);
                return SCRIPT_OVERRIDE;
            case "lifedayStop":
                stopHolidayEvent(speaker, "lifeday", lifedayRunning, lifeday);
                return SCRIPT_OVERRIDE;
            case "lifedayStartForReals":
                startHolidayEventForReals(speaker, "lifeday", lifedayRunning);
                return SCRIPT_OVERRIDE;
            case "lifedayStopForReals":
                stopHolidayEventForReals(speaker, "lifeday");
                return SCRIPT_OVERRIDE;
            case "lovedayStart":
                retireLaterHolidayEvent(speaker, "loveday", true);
                return SCRIPT_OVERRIDE;
            case "lovedayStop":
                retireLaterHolidayEvent(speaker, "loveday", false);
                return SCRIPT_OVERRIDE;
            case "lovedayStartForReals":
                retireLaterHolidayEvent(speaker, "loveday", true);
                return SCRIPT_OVERRIDE;
            case "lovedayStopForReals":
                retireLaterHolidayEvent(speaker, "loveday", false);
                return SCRIPT_OVERRIDE;
            case "empiredayStart":
                retireEmpireDayEvent();
                sendSystemMessageTestingOnly(speaker, "Empire Day is retired by the Publish 14.1 restoration.");
                return SCRIPT_OVERRIDE;
            case "empiredayStop":
                retireEmpireDayEvent();
                return SCRIPT_OVERRIDE;
            case "empiredayStartForReals":
                retireEmpireDayEvent();
                sendSystemMessageTestingOnly(speaker, "Empire Day is retired by the Publish 14.1 restoration.");
                return SCRIPT_OVERRIDE;
            case "empiredayStopForReals":
                retireEmpireDayEvent();
                return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    private void startHolidayEvent(obj_id speaker, String holidayName, String holidayRunning, int holidayStatus) throws InterruptedException
    {
        if (holidayRunning == null)
        {
            sendSystemMessageTestingOnly(speaker, "Server config is not marked as " + holidayName + " running");
            return;
        }
        if (holidayRunning.equals("true") || holidayRunning.equals("1"))
        {
            if (holidayStatus > -1)
            {
                sendSystemMessageTestingOnly(speaker, "Server says that " + holidayName + " is already running. If you are sure that it's not, say " + holidayName + "StartForReals");
            }
            if (holidayStatus < 0)
            {
                sendSystemMessageTestingOnly(speaker, holidayName + " started.");
                startUniverseWideEvent(holidayName);
            }
            refreshPrecuLifeDayPlanets(true);
        }
    }
    private void startHolidayEventForReals(obj_id speaker, String holidayName, String holidayRunning) throws InterruptedException
    {
        if (holidayRunning == null)
        {
            sendSystemMessageTestingOnly(speaker, "Server config is not marked as " + holidayName + " running");
            return;
        }
        if (holidayRunning.equals("true") || holidayRunning.equals("1"))
        {
            sendSystemMessageTestingOnly(speaker, holidayName + " started.");
            startUniverseWideEvent(holidayName);
            refreshPrecuLifeDayPlanets(true);
        }
    }
    private void stopHolidayEvent(obj_id speaker, String holidayName, String holidayRunning, int holidayStatus) throws InterruptedException
    {
        if (holidayRunning.equals("true") || holidayRunning.equals("1"))
        {
            sendSystemMessageTestingOnly(speaker, "Server config is marked as " + holidayName + " running. If you are sure that it should not be running anyway, say " + holidayName + "StopForReals");
        }
    }
    private void stopHolidayEventForReals(obj_id speaker, String holidayName) throws InterruptedException
    {
        sendSystemMessageTestingOnly(speaker, holidayName + " stopped.");
        stopUniverseWideEvent(holidayName);
        refreshPrecuLifeDayPlanets(false);
    }
    public int halloweenServerStart(obj_id self, dictionary params) throws InterruptedException
    {
        retireHolidayEvent("halloween");
        return SCRIPT_CONTINUE;
    }
    public int lifedayServerStart(obj_id self, dictionary params) throws InterruptedException
    {
        CustomerServiceLog("holidayEvent", "holiday_controller.lifedayServerStart Life Day event handler called.");
        String lifedayString = getCurrentUniverseWideEvents();
        int lifeday = lifedayString.indexOf("lifeday");
        String lifedayRunning = getConfigSetting("GameServer", "lifeday");
        CustomerServiceLog("holidayEvent", "holiday_controller.lifedayServerStart lifedayString: " + lifedayString + " lifeday: " + lifeday + " lifedayRunning: " + lifedayRunning);
        boolean enabled = lifedayRunning != null &&
            (lifedayRunning.equals("true") || lifedayRunning.equals("1"));
        if (!enabled)
        {
            CustomerServiceLog("holidayEvent", "holiday_controller.lifedayServerStart event is either not running or not in the server configs.");
            if (lifeday > -1)
            {
                stopUniverseWideEvent("lifeday");
            }
            refreshPrecuLifeDayPlanets(false);
        }
        else
        {
            if (lifeday < 0)
            {
                if (!startUniverseWideEvent("lifeday"))
                {
                    CustomerServiceLog("holidayEvent", "holiday_controller.lifedayServerStart startUniverseWideEvent reports FAILURE to start universe wide event.");
                }
                else 
                {
                    CustomerServiceLog("holidayEvent", "holiday_controller.lifedayServerStart startUniverseWideEvent reports SUCCESS starting universe wide event.");
                }
            }
            refreshPrecuLifeDayPlanets(true);
        }
        return SCRIPT_CONTINUE;
    }
    private void refreshPrecuLifeDayPlanets(boolean active) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put("active", active);
        String[] planets =
        {
            "tatooine",
            "corellia",
            "naboo",
            "dathomir",
            "endor",
            "yavin4"
        };
        for (String planetName : planets)
        {
            obj_id planet = getPlanetByName(planetName);
            if (isIdValid(planet) && exists(planet))
            {
                messageTo(planet, "refreshPrecuLifeDayAnchors", params, 2.0f, false);
            }
        }
    }
    public int lovedayServerStart(obj_id self, dictionary params) throws InterruptedException
    {
        retireHolidayEvent("loveday");
        return SCRIPT_CONTINUE;
    }
    private void retireLaterHolidayEvent(obj_id speaker, String holidayName, boolean startRequested) throws InterruptedException
    {
        retireHolidayEvent(holidayName);
        if (startRequested)
        {
            sendSystemMessageTestingOnly(speaker, holidayName + " is retired by the Publish 14.1 restoration.");
        }
    }
    private void retireHolidayEvent(String holidayName) throws InterruptedException
    {
        if (getCurrentUniverseWideEvents().indexOf(holidayName) > -1)
        {
            stopUniverseWideEvent(holidayName);
        }
    }
    public int empiredayServerStart(obj_id self, dictionary params) throws InterruptedException
    {
        retireEmpireDayEvent();
        return SCRIPT_CONTINUE;
    }
    private void retireEmpireDayEvent() throws InterruptedException
    {
        if (getCurrentUniverseWideEvents().indexOf("empireday_ceremony") > -1)
        {
            stopUniverseWideEvent("empireday_ceremony");
        }
    }
}
