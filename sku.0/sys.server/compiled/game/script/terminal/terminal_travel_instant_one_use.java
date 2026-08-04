package script.terminal;

import script.library.create;
import script.library.locations;
import script.library.utils;
import script.*;

public class terminal_travel_instant_one_use extends script.base_script
{
    public terminal_travel_instant_one_use()
    {
    }
    public static final string_id SID_LOCATION_NOGOOD_FOR_PICKUP = new string_id("travel", "no_pickup_location");
    public static final string_id SID_TIMEOUT = new string_id("travel", "pickup_timeout");
    public static final string_id SID_LEFT_ME = new string_id("travel", "left_pickup_zone");
    public static final string_id SID_NOT_YOUR_SHIP = new string_id("travel", "not_your_ship");
    public static final String TRIGGER_VOLUME_PICKUP_SHIP = "travel_instant_pickup_interest_range";
    public static final float PICKUP_INTEREST_RADIUS = 64.0f;
    public static final string_id SID_CALLING_FOR_PICKUP = new string_id("travel", "calling_for_pickup");
    public static final boolean CONST_FLAG_DO_LOGGING = true;
    public static final int SHIP_TYPE_INSTANT_XWING_TIE = 1;
    public static final int SHIP_TYPE_INSTANT_PRIVATEER = 2;
    public static final int SHIP_TYPE_INSTANT_ROYAL_SHIP = 3;
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE one-use instant-travel menu request for " + player);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE one-use instant-travel selection for " + player);
        return SCRIPT_CONTINUE;
    }
    public obj_id spawnPickupCraft(obj_id player, int type) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Rejected retired NGE one-use instant-travel craft spawn for " + player);
        return null;
    }
    public void debugLogging(String section, String message) throws InterruptedException
    {
        if (CONST_FLAG_DO_LOGGING)
        {
            LOG("travel_terminal", message);
        }
    }
}
