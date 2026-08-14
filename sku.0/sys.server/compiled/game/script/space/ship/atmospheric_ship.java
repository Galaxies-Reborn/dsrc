package script.space.ship;

import script.*;
import script.library.*;

public class atmospheric_ship extends script.base_script
{
    public static final int MENU_ENTER = menu_info_types.SERVER_MENU42;
    public static final int MENU_PILOT = menu_info_types.SERVER_MENU43;
    public static final int MENU_LAUNCH_SPACE = menu_info_types.SERVER_MENU44;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (script.library.atmospheric_ship.isGroundProxy(self))
        {
            messageTo(self, "monitorAtmosphericGroundProxy", null, 0.25f, false);
            return SCRIPT_CONTINUE;
        }
        messageTo(self, "monitorAtmosphericFlight", null, 1.0f, false);
        if (script.library.atmospheric_ship.isParkedHousing(self))
        {
            messageTo(self, "housingMaintenanceHeartbeat", null, script.library.atmospheric_ship.MAINTENANCE_HEARTBEAT_SECONDS, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (script.library.atmospheric_ship.isGroundProxy(self))
        {
            messageTo(self, "monitorAtmosphericGroundProxy", null, 0.25f, false);
            return SCRIPT_CONTINUE;
        }
        if (script.library.atmospheric_ship.isActive(self))
        {
            setObjVar(self, script.library.atmospheric_ship.VAR_WORLD_VISIBLE, 1);
            script.library.atmospheric_ship.destroyGroundProxy(self);
            messageTo(self, "monitorAtmosphericFlight", null, 1.0f, false);
        }
        if (script.library.atmospheric_ship.isParkedHousing(self))
        {
            messageTo(self, "housingMaintenanceHeartbeat", null, script.library.atmospheric_ship.MAINTENANCE_HEARTBEAT_SECONDS, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        obj_id interactionShip = script.library.atmospheric_ship.isGroundProxy(self) ? script.library.atmospheric_ship.getProxyShip(self) : self;
        if (!isIdValid(interactionShip) || !interactionShip.isLoaded() || !script.library.atmospheric_ship.isActive(interactionShip) || isSpaceScene())
        {
            return SCRIPT_CONTINUE;
        }
        if (space_utils.isShipWithInterior(interactionShip))
        {
            if (script.library.atmospheric_ship.isOwner(player, interactionShip) || permissionsIsPublic(interactionShip) || permissionsIsAllowed(interactionShip, player))
            {
                mi.addRootMenu(MENU_ENTER, script.library.atmospheric_ship.SID_ENTER);
            }
        }
        if (script.library.atmospheric_ship.isOwner(player, interactionShip) && !script.library.atmospheric_ship.isParkedHousing(interactionShip))
        {
            obj_id pilot = getPilotId(interactionShip);
            if (!isIdValid(pilot))
            {
                mi.addRootMenu(MENU_PILOT, script.library.atmospheric_ship.SID_PILOT);
            }
            if (!isIdValid(pilot) || pilot == player)
            {
                mi.addRootMenu(MENU_LAUNCH_SPACE, script.library.atmospheric_ship.SID_LAUNCH_SPACE);
            }
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        obj_id interactionShip = script.library.atmospheric_ship.isGroundProxy(self) ? script.library.atmospheric_ship.getProxyShip(self) : self;
        if (!isIdValid(interactionShip) || !interactionShip.isLoaded())
        {
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_ENTER)
        {
            script.library.atmospheric_ship.enter(player, interactionShip);
        }
        if (item == MENU_PILOT)
        {
            script.library.atmospheric_ship.pilot(player, interactionShip);
        }
        if (item == MENU_LAUNCH_SPACE)
        {
            obj_id controlDevice = script.library.atmospheric_ship.getControlDevice(interactionShip);
            script.library.atmospheric_ship.launchToSpace(controlDevice, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int monitorAtmosphericGroundProxy(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id ship = script.library.atmospheric_ship.getProxyShip(self);
        if (!isIdValid(ship) || !ship.isLoaded() || !script.library.atmospheric_ship.isActive(ship) || isSpaceScene() || getTopMostContainer(ship) != ship)
        {
            if (isIdValid(ship) && hasObjVar(ship, script.library.atmospheric_ship.VAR_GROUND_PROXY) && getObjIdObjVar(ship, script.library.atmospheric_ship.VAR_GROUND_PROXY) == self)
            {
                removeObjVar(ship, script.library.atmospheric_ship.VAR_GROUND_PROXY);
            }
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(self, script.library.atmospheric_ship.SCRIPTVAR_PROXY_NETWORK_ADMITTED))
        {
            if (!setLocation(self, getLocation(ship)))
            {
                destroyObject(self);
                return SCRIPT_CONTINUE;
            }
            utils.setScriptVar(self, script.library.atmospheric_ship.SCRIPTVAR_PROXY_NETWORK_ADMITTED, 1);
        }
        setTransform_o2p(self, getTransform_o2w(ship));
        messageTo(self, "monitorAtmosphericGroundProxy", null, 0.25f, false);
        return SCRIPT_CONTINUE;
    }

    public int monitorAtmosphericFlight(obj_id self, dictionary params) throws InterruptedException
    {
        if (!script.library.atmospheric_ship.isActive(self) || isSpaceScene() || getTopMostContainer(self) != self)
        {
            return SCRIPT_CONTINUE;
        }
        script.library.atmospheric_ship.destroyGroundProxy(self);
        if (!script.library.atmospheric_ship.isParkedHousing(self))
        {
            obj_id pilot = getPilotId(self);
            if (isIdValid(pilot))
            {
                location where = getLocation(self);
                float terrainHeight = getHeightAtLocation(where.x, where.z);
                if (where.y - terrainHeight >= script.library.atmospheric_ship.ATMOSPHERE_EXIT_AGL)
                {
                    script.library.atmospheric_ship.transitionToSpace(self);
                    return SCRIPT_CONTINUE;
                }
            }
        }
        messageTo(self, "monitorAtmosphericFlight", null, 1.0f, false);
        return SCRIPT_CONTINUE;
    }

    public int playAtmosphericArrivalEffect(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = params == null ? null : params.getObjId("player");
        obj_id effectShip = script.library.atmospheric_ship.isGroundProxy(self) ? script.library.atmospheric_ship.getProxyShip(self) : self;
        if (isIdValid(player) && isIdValid(effectShip) && script.library.atmospheric_ship.isActive(effectShip) && getTopMostContainer(effectShip) == effectShip)
        {
            playClientEffectObj(player, "clienteffect/space_command/shp_dock_release.cef", self, "");
        }
        return SCRIPT_CONTINUE;
    }

    public int housingMaintenanceHeartbeat(obj_id self, dictionary params) throws InterruptedException
    {
        if (!script.library.atmospheric_ship.isParkedHousing(self))
        {
            return SCRIPT_CONTINUE;
        }
        int elapsed = getGameTime() - getIntObjVar(self, script.library.atmospheric_ship.VAR_LAST_MAINTENANCE);
        int heartbeats = elapsed / script.library.atmospheric_ship.MAINTENANCE_HEARTBEAT_SECONDS;
        if (heartbeats > 0)
        {
            int amount = heartbeats * script.library.atmospheric_ship.SMALL_HOUSE_MAINTENANCE_RATE;
            int remaining = player_structure.decrementMaintenancePool(self, amount);
            if (remaining < 0)
            {
                setObjVar(self, script.library.atmospheric_ship.VAR_CONDEMNED, 1);
            }
            else
            {
                removeObjVar(self, script.library.atmospheric_ship.VAR_CONDEMNED);
            }
            setObjVar(self, script.library.atmospheric_ship.VAR_LAST_MAINTENANCE, getGameTime());
        }
        messageTo(self, "housingMaintenanceHeartbeat", null, script.library.atmospheric_ship.MAINTENANCE_HEARTBEAT_SECONDS, false);
        return SCRIPT_CONTINUE;
    }

    public int handlePayment(obj_id self, dictionary params) throws InterruptedException
    {
        removeObjVar(self, script.library.atmospheric_ship.VAR_CONDEMNED);
        obj_id controlDevice = script.library.atmospheric_ship.getControlDevice(self);
        if (isIdValid(controlDevice))
        {
            sendDirtyObjectMenuNotification(controlDevice);
        }
        return SCRIPT_CONTINUE;
    }

    public int removeResidentVar(obj_id self, dictionary params) throws InterruptedException
    {
        if (hasObjVar(self, player_structure.VAR_RESIDENCE_BUILDING))
        {
            removeObjVar(self, player_structure.VAR_RESIDENCE_BUILDING);
        }
        return SCRIPT_CONTINUE;
    }
}
