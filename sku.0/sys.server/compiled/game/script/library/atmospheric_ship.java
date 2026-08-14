package script.library;

import script.*;

import java.util.Vector;

/**
 * Galaxies Reborn ground-ship lifecycle.
 *
 * The real ship contained by the player's ship control device is moved into
 * the ground scene.  No surrogate object is created, so chassis appearance,
 * components, customization, cargo and POB contents remain authoritative.
 */
public class atmospheric_ship extends script.base_script
{
    public static final String SCRIPT = "space.ship.atmospheric_ship";
    public static final String ROOT = "galaxiesReborn.atmosphericShip";
    public static final String VAR_ACTIVE = ROOT + ".active";
    public static final String VAR_WORLD_VISIBLE = ROOT + ".worldVisible";
    public static final String VAR_CONTROL_DEVICE = ROOT + ".controlDevice";
    public static final String VAR_CONTROLLED_SHIP = ROOT + ".ship";
    public static final String VAR_CONTROL_STATE = ROOT + ".state";
    public static final String VAR_GROUND_LOCATION = ROOT + ".groundLocation";
    public static final String VAR_TRANSITION_PENDING = ROOT + ".transitionPending";
    public static final String VAR_GROUND_PROXY = ROOT + ".groundProxy";
    public static final String SCRIPTVAR_PROXY_NETWORK_ADMITTED = ROOT + ".proxy.networkAdmitted";
    public static final String VAR_PROXY_SHIP = ROOT + ".proxy.ship";
    public static final String VAR_PROXY_CONTROL_DEVICE = ROOT + ".proxy.controlDevice";
    public static final String SCRIPTVAR_CALL_PENDING = ROOT + ".call.pending";
    public static final String SCRIPTVAR_CALL_PLAYER = ROOT + ".call.player";
    public static final String SCRIPTVAR_CALL_SHIP = ROOT + ".call.ship";
    public static final String VAR_PARKED = ROOT + ".housing.parked";
    public static final String VAR_HOUSING_OWNER = ROOT + ".housing.owner";
    public static final String VAR_MAINTENANCE_RATE = ROOT + ".housing.maintenanceRate";
    public static final String VAR_CONDEMNED = ROOT + ".housing.condemned";
    public static final String VAR_LAST_MAINTENANCE = ROOT + ".housing.lastMaintenance";

    public static final int SMALL_HOUSE_MAINTENANCE_RATE = 4;
    public static final int SMALL_HOUSE_LOTS = 1;
    public static final int MAINTENANCE_HEARTBEAT_SECONDS = 1800;
    public static final float CALL_DISTANCE = 20.0f;
    public static final float LANDING_CLEARANCE = 4.0f;
    public static final float MINIMUM_CALL_FOOTPRINT = 4.0f;
    public static final float MAXIMUM_FIGHTER_CALL_FOOTPRINT = 16.0f;
    public static final float MAXIMUM_POB_CALL_FOOTPRINT = 48.0f;
    public static final float CALL_SEARCH_PADDING = 8.0f;
    public static final float ITV_CALL_DELAY_SECONDS = 2.0f;
    public static final float MAX_LANDING_AGL = 20.0f;
    public static final float ATMOSPHERE_EXIT_AGL = 1000.0f;
    public static final float HOUSING_CLEARANCE_RADIUS = 48.0f;
    public static final int STATE_STORED = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_PARKED = 2;
    public static final String LAUNCH_TABLE = "datatables/space_zones/launch_locations.iff";
    public static final String HANGAR_MINI_SHIPS_TABLE = "datatables/tcg/hangar_mini_ships.iff";
    public static final String HUTT_LIGHT_HANGAR_TEMPLATE = "object/tangible/tcg/series5/hangar_ships/hutt_fighter_light_01.iff";
    public static final String HUTT_LIGHT_GROUND_TERMINAL_TEMPLATE = "object/tangible/terminal/terminal_atmospheric_ship_hutt_light.iff";

    public static final string_id SID_CALL = new string_id("ui_radial", "control_call");
    public static final string_id SID_STORE = new string_id("ui_radial", "atmospheric_flight_store");
    public static final string_id SID_LAND = new string_id("ui_radial", "atmospheric_flight_land");
    public static final string_id SID_PILOT = new string_id("ui_radial", "atmospheric_flight_fly");
    public static final string_id SID_LAUNCH_SPACE = new string_id("ui_radial", "atmospheric_flight_launch_space");
    public static final string_id SID_ENTER = new string_id("ui_radial", "atmospheric_ship_enter");
    public static final string_id SID_HOUSING = new string_id("ui_radial", "atmospheric_ship_housing");
    public static final string_id SID_PARK = new string_id("ui_radial", "atmospheric_ship_park");
    public static final string_id SID_UNPARK = new string_id("ui_radial", "atmospheric_ship_unpark");
    public static final string_id SID_STATUS = new string_id("ui_radial", "atmospheric_ship_status");
    public static final string_id SID_PAY_MAINTENANCE = new string_id("ui_radial", "atmospheric_ship_pay_maintenance");
    public static final string_id SID_RESIDENCE = new string_id("ui_radial", "atmospheric_ship_residence");
    public static final string_id SID_ACCESS = new string_id("ui_radial", "atmospheric_ship_access");
    public static final string_id SID_CALL_FAILED = new string_id("pet/pet_menu", "failed_to_call_vehicle");
    public static final string_id SID_CALLING_FOR_PICKUP = new string_id("travel", "calling_for_pickup");

    public static boolean isCallPending(obj_id controlDevice) throws InterruptedException
    {
        return isIdValid(controlDevice) && utils.hasScriptVar(controlDevice, SCRIPTVAR_CALL_PENDING);
    }

    public static void clearCallPending(obj_id controlDevice) throws InterruptedException
    {
        if (!isIdValid(controlDevice))
        {
            return;
        }
        utils.removeScriptVar(controlDevice, SCRIPTVAR_CALL_PENDING);
        utils.removeScriptVar(controlDevice, SCRIPTVAR_CALL_PLAYER);
        utils.removeScriptVar(controlDevice, SCRIPTVAR_CALL_SHIP);
        sendDirtyObjectMenuNotification(controlDevice);
    }

    public static boolean isGroundProxy(obj_id object) throws InterruptedException
    {
        return isIdValid(object) && hasObjVar(object, VAR_PROXY_SHIP);
    }

    public static obj_id getProxyShip(obj_id proxy) throws InterruptedException
    {
        if (!isGroundProxy(proxy))
        {
            return null;
        }
        return getObjIdObjVar(proxy, VAR_PROXY_SHIP);
    }

    public static obj_id getGroundProxy(obj_id ship) throws InterruptedException
    {
        if (!isIdValid(ship) || !hasObjVar(ship, VAR_GROUND_PROXY))
        {
            return null;
        }
        obj_id proxy = getObjIdObjVar(ship, VAR_GROUND_PROXY);
        if (!isIdValid(proxy) || !exists(proxy))
        {
            removeObjVar(ship, VAR_GROUND_PROXY);
            return null;
        }
        return proxy;
    }

    public static void destroyGroundProxy(obj_id ship) throws InterruptedException
    {
        if (!isIdValid(ship))
        {
            return;
        }
        obj_id proxy = getGroundProxy(ship);
        removeObjVar(ship, VAR_GROUND_PROXY);
        if (isIdValid(proxy) && exists(proxy))
        {
            destroyObject(proxy);
        }
    }

    public static String getGroundProxyTemplate(obj_id ship) throws InterruptedException
    {
        if (!isIdValid(ship))
        {
            return null;
        }
        dictionary row = dataTableGetRow(HANGAR_MINI_SHIPS_TABLE, getShipChassisType(ship));
        if (row == null)
        {
            return null;
        }
        String template = row.getString("template");
        if (template == null || template.length() <= 0)
        {
            return null;
        }
        // Hangar miniatures are inventory/house decorations and do not acquire
        // a client observer when created as freestanding world objects.  The
        // terminal wrapper uses the same exact Scyk shared appearance while
        // retaining the proven ITV terminal world-observer behavior.
        if (template.equals(HUTT_LIGHT_HANGAR_TEMPLATE))
        {
            return HUTT_LIGHT_GROUND_TERMINAL_TEMPLATE;
        }
        return template;
    }

    public static void copyGroundProxyCustomization(obj_id ship, obj_id proxy) throws InterruptedException
    {
        if (!isIdValid(ship) || !isIdValid(proxy))
        {
            return;
        }
        String textureIndex = getShipChassisType(ship).equals("player_yt2400") ? "/private/index_texture_1" : "/shared_owner/index_texture_1";
        int color0 = hue.getVarColorIndex(ship, "/shared_owner/index_color_1");
        int color1 = hue.getVarColorIndex(ship, "/shared_owner/index_color_2");
        int texture = getRangedIntCustomVarValue(ship, textureIndex);
        if (color0 > -1)
        {
            hue.setColor(proxy, "/shared_owner/index_color_1", color0);
        }
        if (color1 > -1)
        {
            hue.setColor(proxy, "/shared_owner/index_color_2", color1);
        }
        if (texture > -1)
        {
            hue.setRangedIntCustomVar(proxy, textureIndex, texture);
        }
    }

    public static obj_id ensureGroundProxy(obj_id controlDevice, obj_id ship) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !isIdValid(ship) || !ship.isLoaded() || !isActive(ship) || isSpaceScene())
        {
            return null;
        }
        obj_id existing = getGroundProxy(ship);
        if (isIdValid(existing))
        {
            setTransform_o2p(existing, getTransform_o2w(ship));
            return existing;
        }
        String template = getGroundProxyTemplate(ship);
        if (template == null)
        {
            CustomerServiceLog("atmospheric_ship", "No Hangar ground-display template for chassis " + getShipChassisType(ship) + " ship " + ship);
            return null;
        }
        // Create beyond the ground observer range, then use a real setLocation
        // transition on the proxy's first monitor tick.  setTransform alone
        // changes coordinates without producing the spatial enter edge that
        // sends SceneCreate to an already-stationary caller.
        location stagingLocation = getLocation(ship);
        stagingLocation.x += 4096.0f;
        obj_id proxy = createObject(template, stagingLocation);
        if (!isIdValid(proxy))
        {
            CustomerServiceLog("atmospheric_ship", "Could not create ground-display template " + template + " for ship " + ship);
            return null;
        }
        setObjVar(proxy, VAR_PROXY_SHIP, ship);
        setObjVar(proxy, VAR_PROXY_CONTROL_DEVICE, controlDevice);
        setObjVar(ship, VAR_GROUND_PROXY, proxy);
        setInvulnerable(proxy, true);
        float proxyRadius = getObjectCollisionRadius(proxy);
        float shipRadius = getCallFootprint(ship);
        float proxyScale = template.equals(HUTT_LIGHT_GROUND_TERMINAL_TEMPLATE) ? 1.0f : (proxyRadius > 0.0f ? shipRadius / proxyRadius : 1.0f);
        proxyScale = Math.max(1.0f, Math.min(8.0f, proxyScale));
        setScale(proxy, proxyScale);
        copyGroundProxyCustomization(ship, proxy);
        String proxyName = getAssignedName(ship);
        if (proxyName == null || proxyName.length() <= 0)
        {
            proxyName = template.equals(HUTT_LIGHT_GROUND_TERMINAL_TEMPLATE) ? "Scyk Light Fighter" : getString(getNameStringId(ship));
        }
        if (proxyName == null || proxyName.length() <= 0 || proxyName.indexOf(":[") >= 0)
        {
            proxyName = "Player Starship";
        }
        setName(proxy, proxyName);
        attachScript(proxy, SCRIPT);
        persistObject(proxy);
        messageTo(proxy, "monitorAtmosphericGroundProxy", null, 0.25f, false);
        CustomerServiceLog("atmospheric_ship", "Created visible Hangar ground-display proxy " + proxy + " template=" + template + " scale=" + proxyScale + " proxyRadius=" + proxyRadius + " shipRadius=" + shipRadius + " for ship " + ship);
        return proxy;
    }

    public static obj_id getShipForControlDevice(obj_id controlDevice) throws InterruptedException
    {
        if (!isIdValid(controlDevice))
        {
            return null;
        }
        if (hasObjVar(controlDevice, VAR_CONTROLLED_SHIP))
        {
            obj_id linkedShip = getObjIdObjVar(controlDevice, VAR_CONTROLLED_SHIP);
            if (isIdValid(linkedShip))
            {
                normalizeStoredShipState(controlDevice, linkedShip);
                return linkedShip;
            }
            removeObjVar(controlDevice, VAR_CONTROLLED_SHIP);
        }
        obj_id ship = space_transition.getShipFromShipControlDevice(controlDevice);
        if (isIdValid(ship))
        {
            setObjVar(controlDevice, VAR_CONTROLLED_SHIP, ship);
            setObjVar(ship, VAR_CONTROL_DEVICE, controlDevice);
            normalizeStoredShipState(controlDevice, ship);
        }
        return ship;
    }

    public static void normalizeStoredShipState(obj_id controlDevice, obj_id ship) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !isIdValid(ship) || !ship.isLoaded())
        {
            return;
        }

        // Physical containment is authoritative after a failed/incomplete call
        // or a server restart.  Never normalize an intentionally parked POB.
        if (getContainedBy(ship) != controlDevice || isParkedHousing(ship))
        {
            return;
        }

        destroyGroundProxy(ship);
        removeObjVar(ship, VAR_ACTIVE);
        removeObjVar(ship, VAR_WORLD_VISIBLE);
        removeObjVar(ship, VAR_GROUND_LOCATION);
        removeObjVar(ship, VAR_TRANSITION_PENDING);
        if (!hasObjVar(controlDevice, VAR_CONTROL_STATE) || getControlState(controlDevice) != STATE_STORED)
        {
            setControlState(controlDevice, STATE_STORED);
        }
    }

    public static int getControlState(obj_id controlDevice) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !hasObjVar(controlDevice, VAR_CONTROL_STATE))
        {
            return STATE_STORED;
        }
        return getIntObjVar(controlDevice, VAR_CONTROL_STATE);
    }

    public static void setControlState(obj_id controlDevice, int state) throws InterruptedException
    {
        if (isIdValid(controlDevice))
        {
            setObjVar(controlDevice, VAR_CONTROL_STATE, state);
            sendDirtyObjectMenuNotification(controlDevice);
        }
    }

    public static boolean rollbackFailedCall(obj_id controlDevice, obj_id ship) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !isIdValid(ship))
        {
            return false;
        }

        obj_id currentContainer = getContainedBy(ship);
        if (currentContainer != controlDevice)
        {
            obj_id[] existingContents = getContents(controlDevice);
            if ((existingContents != null && existingContents.length > 0) || !putIn(ship, controlDevice) || getContainedBy(ship) != controlDevice)
            {
                setControlState(controlDevice, STATE_ACTIVE);
                CustomerServiceLog("atmospheric_ship", "Could not roll ship " + ship + " back into control device " + controlDevice + "; preserving active world state");
                return false;
            }
        }
        destroyGroundProxy(ship);
        removeObjVar(ship, VAR_ACTIVE);
        removeObjVar(ship, VAR_WORLD_VISIBLE);
        removeObjVar(ship, VAR_GROUND_LOCATION);
        removeObjVar(ship, VAR_TRANSITION_PENDING);
        setControlState(controlDevice, STATE_STORED);
        return true;
    }

    public static obj_id getControlDevice(obj_id ship) throws InterruptedException
    {
        if (isIdValid(ship) && hasObjVar(ship, VAR_CONTROL_DEVICE))
        {
            return getObjIdObjVar(ship, VAR_CONTROL_DEVICE);
        }
        if (isIdValid(ship) && hasObjVar(ship, "shipControlDevice"))
        {
            return getObjIdObjVar(ship, "shipControlDevice");
        }
        return null;
    }

    public static boolean isActive(obj_id ship) throws InterruptedException
    {
        return isIdValid(ship) && hasObjVar(ship, VAR_ACTIVE) && getIntObjVar(ship, VAR_ACTIVE) != 0;
    }

    public static boolean isParkedHousing(obj_id ship) throws InterruptedException
    {
        return isIdValid(ship) && hasObjVar(ship, VAR_PARKED) && getIntObjVar(ship, VAR_PARKED) != 0;
    }

    public static boolean isOwner(obj_id player, obj_id ship) throws InterruptedException
    {
        return isIdValid(player) && isIdValid(ship) && (getOwner(ship) == player || (hasObjVar(ship, VAR_HOUSING_OWNER) && getObjIdObjVar(ship, VAR_HOUSING_OWNER) == player));
    }

    public static boolean isNoBuild(location where) throws InterruptedException
    {
        return where == null || isIdValid(where.cell) || getRegionsWithBuildableAtPoint(where, regions.BUILD_FALSE) != null;
    }

    public static boolean isItvCallRestricted(location where) throws InterruptedException
    {
        if (where == null || isIdValid(where.cell))
        {
            return true;
        }
        if (locations.isInCity(where))
        {
            return true;
        }
        region[] geoCities = getRegionsWithGeographicalAtPoint(where, regions.GEO_CITY);
        if (geoCities != null && geoCities.length > 0)
        {
            return true;
        }
        region[] pvpRegions = getRegionsWithPvPAtPoint(where, regions.PVP_REGION_TYPE_ADVANCED);
        return pvpRegions != null && pvpRegions.length > 0;
    }

    public static boolean isCallPathAllowed(obj_id player, location start, location end) throws InterruptedException
    {
        if (start == null || end == null || !start.area.equals(end.area))
        {
            return false;
        }
        for (int sample = 0; sample <= 4; ++sample)
        {
            float fraction = sample / 4.0f;
            location point = new location(start.x + (end.x - start.x) * fraction, start.y + (end.y - start.y) * fraction, start.z + (end.z - start.z) * fraction, start.area);
            if (!isGroundLocationAllowed(player, point, false))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isHousingFootprintAllowed(obj_id player, location center) throws InterruptedException
    {
        if (!isGroundLocationAllowed(player, center, true))
        {
            return false;
        }
        for (int sample = 0; sample < 8; ++sample)
        {
            double angle = (Math.PI * 2.0 * sample) / 8.0;
            location edge = new location(center.x + (float)Math.cos(angle) * HOUSING_CLEARANCE_RADIUS, center.y, center.z + (float)Math.sin(angle) * HOUSING_CLEARANCE_RADIUS, center.area);
            if (!isGroundLocationAllowed(player, edge, true))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isCallFootprintAllowed(obj_id player, location center, float radius) throws InterruptedException
    {
        if (!isGroundLocationAllowed(player, center, false))
        {
            return false;
        }
        for (int sample = 0; sample < 12; ++sample)
        {
            double angle = (Math.PI * 2.0 * sample) / 12.0;
            location edge = new location(center.x + (float)Math.cos(angle) * radius, center.y, center.z + (float)Math.sin(angle) * radius, center.area);
            if (!isGroundLocationAllowed(player, edge, false))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isGroundLocationAllowed(obj_id player, location where, boolean permanent) throws InterruptedException
    {
        if (!isIdValid(player) || where == null || isSpaceScene() || isIdValid(where.cell))
        {
            return false;
        }
        if (vehicle.isInRestrictedScene(player) ||
            (permanent ? isNoBuild(where) : isItvCallRestricted(where)))
        {
            return false;
        }
        if (permanent)
        {
            int cityId = getCityAtLocation(where, 0);
            if (cityExists(cityId) && city.isCityZoned(cityId) && !city.hasZoningRights(player, cityId))
            {
                return false;
            }
        }
        return true;
    }

    public static float getCallFootprint(obj_id ship) throws InterruptedException
    {
        if (!isIdValid(ship))
        {
            return MINIMUM_CALL_FOOTPRINT;
        }
        float maximumFootprint = space_utils.isShipWithInterior(ship) ? MAXIMUM_POB_CALL_FOOTPRINT : MAXIMUM_FIGHTER_CALL_FOOTPRINT;
        return Math.max(MINIMUM_CALL_FOOTPRINT, Math.min(maximumFootprint, getObjectCollisionRadius(ship)));
    }

    public static location getCallLocation(obj_id player, obj_id ship) throws InterruptedException
    {
        float footprint = getCallFootprint(ship);
        float playerRadius = Math.max(0.0f, getObjectCollisionRadius(player));
        float callDistance = Math.max(CALL_DISTANCE, footprint + playerRadius + CALL_SEARCH_PADDING);
        transform playerTransform = getTransform_o2w(player);
        transform spawnTransform = playerTransform.move_l(new vector(0.0f, 0.0f, callDistance));
        vector spawnPosition = spawnTransform.getPosition_p();
        location intended = new location(spawnPosition.x, getHeightAtLocation(spawnPosition.x, spawnPosition.z), spawnPosition.z, getCurrentSceneName());
        float diameter = footprint * 2.0f;
        location clearLocation = locations.getGoodLocationAroundLocationAvoidCollidables(intended, diameter, diameter, footprint, footprint, false, false, footprint);
        if (clearLocation == null)
        {
            CustomerServiceLog("atmospheric_ship", "Rejected call placement: no clear terrain rectangle for ship " + ship + " footprint=" + footprint + " intended=" + intended);
            return null;
        }
        location playerLocation = getLocation(player);
        double deltaX = clearLocation.x - playerLocation.x;
        double deltaZ = clearLocation.z - playerLocation.z;
        float minimumSeparation = footprint + playerRadius + CALL_SEARCH_PADDING;
        if ((deltaX * deltaX + deltaZ * deltaZ) < (minimumSeparation * minimumSeparation))
        {
            CustomerServiceLog("atmospheric_ship", "Rejected call placement: insufficient player separation for ship " + ship + " footprint=" + footprint + " clearLocation=" + clearLocation);
            return null;
        }
        if (!isCallFootprintAllowed(player, clearLocation, footprint))
        {
            CustomerServiceLog("atmospheric_ship", "Rejected call placement: center or hull perimeter intersects a no-build region for ship " + ship + " footprint=" + footprint + " clearLocation=" + clearLocation);
            return null;
        }
        clearLocation.y = getHeightAtLocation(clearLocation.x, clearLocation.z) + Math.max(LANDING_CLEARANCE, footprint * 0.25f);
        return clearLocation;
    }

    public static transform getCallTransform(obj_id player, location where) throws InterruptedException
    {
        transform source = getTransform_o2w(player);
        return new transform(source.getLocalFrameI_p(), source.getLocalFrameJ_p(), source.getLocalFrameK_p(), new vector(where.x, where.y, where.z));
    }

    public static boolean callDown(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !isIdValid(player) || !utils.isNestedWithin(controlDevice, player) || isCallPending(controlDevice))
        {
            return false;
        }
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isIdValid(ship) || !ship.isLoaded() || !isOwner(player, ship) || getControlState(controlDevice) != STATE_STORED || isActive(ship) || getContainedBy(ship) != controlDevice)
        {
            return false;
        }
        if (ai_lib.aiIsDead(player) || ai_lib.isInCombat(player))
        {
            return false;
        }
        location callLocation = getCallLocation(player, ship);
        if (callLocation == null || !isCallPathAllowed(player, getLocation(player), callLocation))
        {
            sendSystemMessage(player, new string_id("player_structure", "not_permitted"));
            return false;
        }

        // Match the authentic ITV command's two-second summon cadence while
        // keeping the selected persistent player ship as the spawned object.
        // The exact validated landing point is retained and rechecked when the
        // timer completes, matching ITV cadence without a second random search.
        utils.setScriptVar(controlDevice, SCRIPTVAR_CALL_PENDING, 1);
        utils.setScriptVar(controlDevice, SCRIPTVAR_CALL_PLAYER, player);
        utils.setScriptVar(controlDevice, SCRIPTVAR_CALL_SHIP, ship);
        doAnimationAction(player, "manipulate_low");
        playClientEffectObj(player, "clienteffect/space_command/sys_manipulation.cef", player, "");
        sendSystemMessage(player, SID_CALLING_FOR_PICKUP);
        dictionary params = new dictionary();
        params.put("player", player);
        params.put("ship", ship);
        params.put("callLocation", callLocation);
        messageTo(controlDevice, "completeAtmosphericShipCall", params, ITV_CALL_DELAY_SECONDS, false);
        sendDirtyObjectMenuNotification(controlDevice);
        return true;
    }

    public static boolean completeCallDown(obj_id controlDevice, obj_id player, obj_id requestedShip, location requestedCallLocation) throws InterruptedException
    {
        if (!isCallPending(controlDevice))
        {
            return false;
        }
        obj_id pendingPlayer = utils.getObjIdScriptVar(controlDevice, SCRIPTVAR_CALL_PLAYER);
        obj_id pendingShip = utils.getObjIdScriptVar(controlDevice, SCRIPTVAR_CALL_SHIP);
        clearCallPending(controlDevice);
        if (!isIdValid(player) || player != pendingPlayer || !isIdValid(requestedShip) || requestedShip != pendingShip || !utils.isNestedWithin(controlDevice, player))
        {
            return false;
        }
        obj_id ship = getShipForControlDevice(controlDevice);
        if (ship != requestedShip || !ship.isLoaded() || !isOwner(player, ship) || getControlState(controlDevice) != STATE_STORED || isActive(ship) || getContainedBy(ship) != controlDevice)
        {
            return false;
        }
        if (ai_lib.aiIsDead(player) || ai_lib.isInCombat(player))
        {
            return false;
        }
        float footprint = getCallFootprint(ship);
        location callLocation = requestedCallLocation;
        if (callLocation == null ||
            !isCallPathAllowed(player, getLocation(player), callLocation) ||
            !isCallFootprintAllowed(player, callLocation, footprint))
        {
            sendSystemMessage(player, new string_id("player_structure", "not_permitted"));
            return false;
        }

        setObjVar(controlDevice, VAR_CONTROLLED_SHIP, ship);
        setObjVar(ship, VAR_CONTROL_DEVICE, controlDevice);
        setObjVar(ship, "shipControlDevice", controlDevice);
        setObjVar(ship, VAR_ACTIVE, 1);
        setObjVar(ship, VAR_WORLD_VISIBLE, 1);
        setObjVar(ship, VAR_GROUND_LOCATION, callLocation);
        removeObjVar(ship, VAR_TRANSITION_PENDING);
        if (!hasScript(ship, SCRIPT))
        {
            attachScript(ship, SCRIPT);
        }
        boolean movedToWorld = setLocation(ship, callLocation);
        obj_id postCallContainer = getContainedBy(ship);
        if (!movedToWorld || isIdValid(postCallContainer))
        {
            rollbackFailedCall(controlDevice, ship);
            sendSystemMessage(player, SID_CALL_FAILED);
            CustomerServiceLog("atmospheric_ship", "Failed to call ship " + ship + " from control device " + controlDevice + " for player " + player);
            return false;
        }
        setTransform_o2p(ship, getCallTransform(player, callLocation));
        setControlState(controlDevice, STATE_ACTIVE);
        // The persistent ShipObject is the ground representation.  It carries
        // the authoritative appearanceData and component baselines, so paint,
        // wings, engines and weapons remain identical to the player's ship.
        // A decorative hangar proxy can only reproduce the base hull and can
        // obscure the real ship at the same transform.
        destroyGroundProxy(ship);
        sendDirtyObjectMenuNotification(controlDevice);
        dictionary arrivalParams = new dictionary();
        arrivalParams.put("player", player);
        messageTo(ship, "playAtmosphericArrivalEffect", arrivalParams, 0.25f, false);
        messageTo(ship, "monitorAtmosphericFlight", null, 1.0f, false);
        CustomerServiceLog("atmospheric_ship", "Player " + player + " called ship " + ship + " from control device " + controlDevice + " at " + callLocation);
        return true;
    }

    public static boolean store(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isOwner(player, ship) || !ship.isLoaded() || !isActive(ship) || isParkedHousing(ship))
        {
            return false;
        }
        Vector occupants = space_transition.getContainedPlayers(ship, null);
        if (occupants != null && occupants.size() > 0)
        {
            return false;
        }
        return storeIntoControlDevice(controlDevice, ship, "Player " + player + " stored ground ship");
    }

    public static boolean forceStoreForLogout(obj_id ship) throws InterruptedException
    {
        if (!isIdValid(ship) || !ship.isLoaded() || !isActive(ship) || isParkedHousing(ship))
        {
            return false;
        }
        obj_id controlDevice = getControlDevice(ship);
        Vector occupants = space_transition.getContainedPlayers(ship, null);
        if (occupants != null)
        {
            location shipLocation = getLocation(ship);
            obj_id pilot = getPilotId(ship);
            for (Object occupantEntry : occupants)
            {
                obj_id occupant = (obj_id)occupantEntry;
                if (occupant == pilot)
                {
                    unpilotShip(occupant);
                }
                setState(occupant, STATE_SITTING_ON_CHAIR, false);
                int posture = getPosture(occupant);
                if (posture == POSTURE_SITTING || posture == POSTURE_PRONE)
                {
                    setPostureClientImmediate(occupant, POSTURE_UPRIGHT);
                }
                setLocation(occupant, shipLocation);
            }
        }
        return storeIntoControlDevice(controlDevice, ship, "Stored atmospheric ship because its owner logged out");
    }

    public static boolean storeIntoControlDevice(obj_id controlDevice, obj_id ship, String reason) throws InterruptedException
    {
        if (!isIdValid(controlDevice) || !isIdValid(ship) || !ship.isLoaded() || getContainedBy(ship) == controlDevice)
        {
            return false;
        }

        obj_id[] existingContents = getContents(controlDevice);
        if (existingContents != null && existingContents.length > 0)
        {
            CustomerServiceLog("atmospheric_ship", "Refused to store ship " + ship + " into non-empty control device " + controlDevice);
            return false;
        }

        if (!putIn(ship, controlDevice) || getContainedBy(ship) != controlDevice)
        {
            CustomerServiceLog("atmospheric_ship", "Failed to store ship " + ship + " into control device " + controlDevice + "; ship remains active in world");
            return false;
        }

        destroyGroundProxy(ship);
        removeObjVar(ship, VAR_ACTIVE);
        removeObjVar(ship, VAR_WORLD_VISIBLE);
        removeObjVar(ship, VAR_GROUND_LOCATION);
        removeObjVar(ship, VAR_TRANSITION_PENDING);
        setControlState(controlDevice, STATE_STORED);
        CustomerServiceLog("atmospheric_ship", reason + " " + ship + " into control device " + controlDevice);
        return true;
    }

    public static boolean pilot(obj_id player, obj_id ship) throws InterruptedException
    {
        if (!isOwner(player, ship) || !ship.isLoaded() || !isActive(ship) || isParkedHousing(ship) || isNoBuild(getLocation(ship)) || getDistance(player, ship) > 64.0f)
        {
            return false;
        }
        if (!hasCertificationsForItem(player, ship) && !isGod(player))
        {
            sendSystemMessage(player, new string_id("space/space_interaction", "no_ship_certification"));
            return false;
        }
        obj_id pilotSlot = space_transition.findPilotSlotObjectForShip(player, ship);
        if (!isIdValid(pilotSlot) || !pilotShip(player, pilotSlot))
        {
            CustomerServiceLog("atmospheric_ship", "Pilot handoff failed for player " + player + " ship " + ship + " slot " + pilotSlot);
            return false;
        }
        obj_id containingShip = space_transition.getContainingShip(player);
        if (getPilotId(ship) != player || containingShip != ship)
        {
            CustomerServiceLog("atmospheric_ship", "Pilot handoff postcondition failed for player " + player + " ship " + ship + " pilot=" + getPilotId(ship) + " containingShip=" + containingShip);
            if (getPilotId(ship) == player)
            {
                unpilotShip(player);
            }
            return false;
        }
        CustomerServiceLog("atmospheric_ship", "Pilot handoff succeeded for player " + player + " ship " + ship);
        return true;
    }

    public static boolean launchToSpace(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isIdValid(ship) || !ship.isLoaded() || !isOwner(player, ship) || !isActive(ship) || isParkedHousing(ship) || isSpaceScene() || hasObjVar(ship, VAR_TRANSITION_PENDING))
        {
            return false;
        }

        obj_id currentPilot = getPilotId(ship);
        if (isIdValid(currentPilot) && currentPilot != player)
        {
            return false;
        }
        if (!isIdValid(currentPilot) && !pilot(player, ship))
        {
            return false;
        }

        return transitionToSpace(ship);
    }

    public static boolean enter(obj_id player, obj_id ship) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(ship) || !isActive(ship) || !space_utils.isShipWithInterior(ship))
        {
            return false;
        }
        if (!isOwner(player, ship) && !permissionsIsPublic(ship) && !permissionsIsAllowed(ship, player))
        {
            sendSystemMessage(player, permissions.SID_INSUFFICIENT_PERMISSIONS);
            return false;
        }
        location destination = space_transition.getShipBoardingDestination(ship);
        if (destination == null || !isIdValid(destination.cell))
        {
            return false;
        }
        setLocation(player, destination);
        return true;
    }

    public static boolean land(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isOwner(player, ship) || !ship.isLoaded() || getPilotId(ship) != player || isParkedHousing(ship))
        {
            return false;
        }
        location shipLocation = getLocation(ship);
        float terrainHeight = getHeightAtLocation(shipLocation.x, shipLocation.z);
        if ((shipLocation.y - terrainHeight) > MAX_LANDING_AGL || isNoBuild(shipLocation))
        {
            return false;
        }
        boolean result = unpilotShip(player);
        if (result && !space_utils.isShipWithInterior(ship))
        {
            transform exitTransform = getTransform_o2w(ship).move_l(new vector(0.0f, 0.0f, -12.0f));
            vector exitPosition = exitTransform.getPosition_p();
            location exitLocation = new location(exitPosition.x, getHeightAtLocation(exitPosition.x, exitPosition.z), exitPosition.z, getCurrentSceneName());
            setLocation(player, exitLocation);
        }
        return result;
    }

    public static boolean hasHousingClearance(obj_id ship) throws InterruptedException
    {
        obj_id[] nearby = getObjectsInRange(getLocation(ship), HOUSING_CLEARANCE_RADIUS);
        if (nearby == null)
        {
            return true;
        }
        for (obj_id object : nearby)
        {
            if (!isIdValid(object) || object == ship || isGroundProxy(object) || getContainedBy(object) == ship)
            {
                continue;
            }
            int type = getGameObjectType(object);
            if (isGameObjectTypeOf(type, GOT_building) || isGameObjectTypeOf(type, GOT_installation) || (isGameObjectTypeOf(type, GOT_ship) && isParkedHousing(object)))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean parkAsHousing(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isOwner(player, ship) || !ship.isLoaded() || !isActive(ship) || isParkedHousing(ship) || !space_utils.isShipWithInterior(ship) || isIdValid(getPilotId(ship)))
        {
            return false;
        }
        location where = getLocation(ship);
        if (!isHousingFootprintAllowed(player, where) || !hasHousingClearance(ship))
        {
            sendSystemMessage(player, new string_id("player_structure", "not_permitted"));
            return false;
        }
        obj_id playerObject = getPlayerObject(player);
        if (!isIdValid(playerObject) || getAccountNumLots(playerObject) + SMALL_HOUSE_LOTS > player_structure.MAX_LOTS)
        {
            sendSystemMessage(player, new string_id("player_structure", "not_enough_lots"));
            return false;
        }

        adjustLotCount(playerObject, SMALL_HOUSE_LOTS);
        setObjVar(ship, VAR_PARKED, 1);
        setObjVar(ship, VAR_HOUSING_OWNER, player);
        setObjVar(ship, VAR_MAINTENANCE_RATE, SMALL_HOUSE_MAINTENANCE_RATE);
        setObjVar(ship, VAR_LAST_MAINTENANCE, getGameTime());
        setControlState(controlDevice, STATE_PARKED);
        removeObjVar(ship, VAR_CONDEMNED);
        permissionsMakePrivate(ship);
        permissionsAddAllowed(ship, getPlayerName(player));
        messageTo(ship, "housingMaintenanceHeartbeat", null, MAINTENANCE_HEARTBEAT_SECONDS, false);
        sendDirtyObjectMenuNotification(controlDevice);
        CustomerServiceLog("atmospheric_ship", "Player " + player + " permanently parked POB ship " + ship + " using one lot at " + where);
        return true;
    }

    public static boolean unparkHousing(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isOwner(player, ship) || !ship.isLoaded() || !isParkedHousing(ship))
        {
            return false;
        }
        if (hasObjVar(ship, player_structure.VAR_RESIDENCE_BUILDING))
        {
            obj_id resident = getObjIdObjVar(ship, player_structure.VAR_RESIDENCE_BUILDING);
            if (isIdValid(resident))
            {
                setHouseId(resident, obj_id.NULL_ID);
                city.removeCitizen(resident, ship);
            }
            removeObjVar(ship, player_structure.VAR_RESIDENCE_BUILDING);
        }
        obj_id playerObject = getPlayerObject(player);
        if (isIdValid(playerObject))
        {
            adjustLotCount(playerObject, -SMALL_HOUSE_LOTS);
        }
        removeObjVar(ship, VAR_PARKED);
        removeObjVar(ship, VAR_HOUSING_OWNER);
        removeObjVar(ship, VAR_MAINTENANCE_RATE);
        removeObjVar(ship, VAR_CONDEMNED);
        removeObjVar(ship, VAR_LAST_MAINTENANCE);
        setControlState(controlDevice, STATE_ACTIVE);
        CustomerServiceLog("atmospheric_ship", "Player " + player + " released parked POB ship " + ship + " and reclaimed one lot");
        return true;
    }

    public static boolean declareResidence(obj_id controlDevice, obj_id player) throws InterruptedException
    {
        obj_id ship = getShipForControlDevice(controlDevice);
        if (!isOwner(player, ship) || !ship.isLoaded() || !isParkedHousing(ship))
        {
            return false;
        }
        int now = getGameTime();
        if (hasObjVar(player, player_structure.VAR_RESIDENCE_CAN_DECLARE) && now - getIntObjVar(player, player_structure.VAR_RESIDENCE_CAN_DECLARE) < player_structure.MIN_RESIDENCE_DURATION)
        {
            return false;
        }
        int currentCity = getCitizenOfCityId(player);
        if (player == cityGetLeader(currentCity))
        {
            sendSystemMessage(player, city.SID_MAYOR_RESIDENCE_CHANGE);
            return false;
        }
        obj_id oldResidence = player_structure.getResidence(player);
        setHouseId(player, ship);
        setObjVar(player, player_structure.VAR_RESIDENCE_CAN_DECLARE, now);
        setObjVar(ship, player_structure.VAR_RESIDENCE_BUILDING, player);
        if (isIdValid(oldResidence) && oldResidence != ship)
        {
            messageTo(oldResidence, "removeResidentVar", null, 0.0f, true);
        }
        city.setCityResidence(player, ship);
        skill.grantAllPoliticianSkills(player);
        return true;
    }

    public static location getAtmosphericSpaceDestination(String groundScene) throws InterruptedException
    {
        int row = dataTableSearchColumnForString(groundScene, "groundScene", LAUNCH_TABLE);
        if (row < 0)
        {
            return null;
        }
        location center = new location(dataTableGetFloat(LAUNCH_TABLE, row, "spaceX"), dataTableGetFloat(LAUNCH_TABLE, row, "spaceY"), dataTableGetFloat(LAUNCH_TABLE, row, "spaceZ"), dataTableGetString(LAUNCH_TABLE, row, "spaceScene"));
        return space_utils.getRandomLocationInSphere(center, 150.0f, 300.0f);
    }

    public static boolean transitionToSpace(obj_id ship) throws InterruptedException
    {
        if (!isActive(ship) || isParkedHousing(ship) || isSpaceScene() || hasObjVar(ship, VAR_TRANSITION_PENDING))
        {
            return false;
        }
        obj_id pilot = getPilotId(ship);
        if (!isIdValid(pilot) || getOwner(ship) != pilot)
        {
            return false;
        }
        location groundLocation = getLocation(ship);
        location spaceDestination = getAtmosphericSpaceDestination(groundLocation.area);
        if (spaceDestination == null)
        {
            return false;
        }
        Vector containedPlayers = space_transition.getContainedPlayers(ship, null);
        obj_id[] passengers = null;
        if (containedPlayers != null && containedPlayers.size() > 0)
        {
            passengers = new obj_id[containedPlayers.size()];
            for (int i = 0; i < containedPlayers.size(); ++i)
            {
                passengers[i] = (obj_id)containedPlayers.get(i);
            }
        }
        setObjVar(ship, VAR_TRANSITION_PENDING, 1);
        obj_id controlDevice = getControlDevice(ship);
        Vector occupants = space_transition.getContainedPlayers(ship, null);
        if (occupants != null)
        {
            for (Object occupantEntry : occupants)
            {
                obj_id occupant = (obj_id)occupantEntry;
                if (occupant == pilot)
                {
                    unpilotShip(occupant);
                }
                setState(occupant, STATE_SITTING_ON_CHAIR, false);
                setLocation(occupant, groundLocation);
            }
        }
        if (!storeIntoControlDevice(controlDevice, ship, "Packed atmospheric ship for space transition"))
        {
            removeObjVar(ship, VAR_TRANSITION_PENDING);
            return false;
        }
        space_transition.launch(pilot, ship, passengers, spaceDestination, groundLocation);
        CustomerServiceLog("atmospheric_ship", "Player " + pilot + " exited atmosphere in ship " + ship + " from " + groundLocation.area + " to " + spaceDestination.area);
        return true;
    }
}
