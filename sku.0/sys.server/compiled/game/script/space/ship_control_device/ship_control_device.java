package script.space.ship_control_device;

import script.*;
import script.library.*;

public class ship_control_device extends script.base_script
{
    public ship_control_device()
    {
    }
    public static final string_id RENAME_SHIP = new string_id("sui", "rename_ship");
    public static final string_id PACK_SHIP = new string_id("sui", "pack_ship");
    public static final string_id PROMPT1 = new string_id("sui", "rename_ship_text");
    public static final String[] ignoreRules = new String[]
    {
        "name_declined_number"
    };
    public static final String SPACE_MINING = "space_mining";
    public static final String IN_USE_OBJVAR = "ship_redeed.inUse";
    public static final int MAX_RESOURCE = 1000000;
    public static final string_id SID_TERMINAL_REDEED_STORAGE = new string_id("player_structure", "redeed_storage");
    public static final string_id SID_STORAGE_INCREASE_REDEED_TITLE = new string_id("player_structure", "sui_storage_redeed_title");
    public static final string_id SID_STORAGE_INCREASE_REDEED_PROMPT = new string_id("player_structure", "sui_storage_redeed_prompt");
    public static final int MENU_HOUSING = menu_info_types.SERVER_MENU5;
    public static final int MENU_PARK = menu_info_types.SERVER_MENU6;
    public static final int MENU_STATUS = menu_info_types.SERVER_MENU7;
    public static final int MENU_PAY_MAINTENANCE = menu_info_types.SERVER_MENU8;
    public static final int MENU_RESIDENCE = menu_info_types.SERVER_MENU9;
    public static final int MENU_ACCESS = menu_info_types.SERVER_MENU10;
    public static final int MENU_ATMOSPHERIC_PILOT = menu_info_types.SERVER_MENU11;
    public static final int MENU_ATMOSPHERIC_LAUNCH_SPACE = menu_info_types.SERVER_MENU12;
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setObjVar(self, "noTrade", 1);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        pobShipLotRefunder(self);
        setObjVar(self, "noTrade", 1);
        removeObjVar(self, IN_USE_OBJVAR);
        atmospheric_ship.getShipForControlDevice(self);
        messageTo(self, "checkCollectionReactor", null, 2, false);
        return SCRIPT_CONTINUE;
    }
    public int OnGetAttributes(obj_id self, obj_id objPlayer, String[] strNames, String[] strAttribs) throws InterruptedException
    {
        int intIndex = utils.getValidAttributeIndex(strNames);
        if (intIndex == -1)
        {
            return SCRIPT_CONTINUE;
        }
        String strName = getAssignedName(self);
        if ((strName != null) && (!strName.equals("")))
        {
            strNames[intIndex] = "ship_name";
            strAttribs[intIndex] = strName;
            intIndex++;
        }
        return SCRIPT_CONTINUE;
    }
    public int OnTransferred(obj_id self, obj_id source, obj_id destination, obj_id transferer) throws InterruptedException
    {
        if (isIdValid(destination))
        {
            obj_id player = utils.getContainingPlayer(destination);
            if (isIdValid(player))
            {
                if (!isPlayer(player))
                {
                    return SCRIPT_CONTINUE;
                }
                obj_id ship = space_transition.getShipFromShipControlDevice(self);
                setOwner(ship, player);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(menu_info_types.SERVER_MENU1, RENAME_SHIP);
        obj_id objShip = atmospheric_ship.getShipForControlDevice(self);
        if (isIdValid(objShip))
        {
            int atmosphericState = atmospheric_ship.getControlState(self);
            if (objShip.isLoaded())
            {
                gunshipCheck(objShip);
            }
            if (atmosphericState == atmospheric_ship.STATE_STORED && !atmospheric_ship.isCallPending(self))
            {
                mi.addRootMenu(menu_info_types.VEHICLE_GENERATE, atmospheric_ship.SID_CALL);
                if (objShip.isLoaded() && hasObjVar(objShip, player_structure.OBJVAR_STRUCTURE_STORAGE_INCREASE))
                {
                    mi.addRootMenu(menu_info_types.DICE_ROLL, SID_TERMINAL_REDEED_STORAGE);
                }
                else
                {
                    mi.addRootMenu(menu_info_types.SERVER_MENU2, PACK_SHIP);
                }
            }
            else if (objShip.isLoaded() && getPilotId(objShip) == player)
            {
                mi.addRootMenu(menu_info_types.VEHICLE_STORE, atmospheric_ship.SID_LAND);
                if (atmosphericState == atmospheric_ship.STATE_ACTIVE && !atmospheric_ship.isParkedHousing(objShip))
                {
                    mi.addRootMenu(MENU_ATMOSPHERIC_LAUNCH_SPACE, atmospheric_ship.SID_LAUNCH_SPACE);
                }
            }
            else if (atmosphericState == atmospheric_ship.STATE_ACTIVE && objShip.isLoaded())
            {
                mi.addRootMenu(menu_info_types.VEHICLE_STORE, atmospheric_ship.SID_STORE);
                if (!atmospheric_ship.isParkedHousing(objShip) && atmospheric_ship.isOwner(player, objShip))
                {
                    obj_id atmosphericPilot = getPilotId(objShip);
                    if (!isIdValid(atmosphericPilot))
                    {
                        mi.addRootMenu(MENU_ATMOSPHERIC_PILOT, atmospheric_ship.SID_PILOT);
                    }
                    if (!isIdValid(atmosphericPilot) || atmosphericPilot == player)
                    {
                        mi.addRootMenu(MENU_ATMOSPHERIC_LAUNCH_SPACE, atmospheric_ship.SID_LAUNCH_SPACE);
                    }
                }
            }
            if ((atmosphericState == atmospheric_ship.STATE_PARKED || (objShip.isLoaded() && space_utils.isShipWithInterior(objShip))) && atmosphericState != atmospheric_ship.STATE_STORED)
            {
                int housingRoot = mi.addRootMenu(MENU_HOUSING, atmospheric_ship.SID_HOUSING);
                if (atmosphericState == atmospheric_ship.STATE_PARKED)
                {
                    mi.addSubMenu(housingRoot, MENU_PARK, atmospheric_ship.SID_UNPARK);
                    mi.addSubMenu(housingRoot, MENU_STATUS, atmospheric_ship.SID_STATUS);
                    mi.addSubMenu(housingRoot, MENU_PAY_MAINTENANCE, atmospheric_ship.SID_PAY_MAINTENANCE);
                    mi.addSubMenu(housingRoot, MENU_RESIDENCE, atmospheric_ship.SID_RESIDENCE);
                    mi.addSubMenu(housingRoot, MENU_ACCESS, atmospheric_ship.SID_ACCESS);
                }
                else
                {
                    mi.addSubMenu(housingRoot, MENU_PARK, atmospheric_ship.SID_PARK);
                }
            }
            if (objShip.isLoaded())
            {
                String strChassisType = getShipChassisType(objShip);
                if (strChassisType.equals("player_sorosuub_space_yacht"))
                {
                    menu_info_data data = mi.getMenuItemByType(menu_info_types.ITEM_USE);
                    string_id strSpam = new string_id("space/space_interaction", "repair");
                    mi.addRootMenu(menu_info_types.ITEM_USE, strSpam);
                }
                if (isShipSlotInstalled(objShip, ship_chassis_slot_type.SCST_cargo_hold))
                {
                    string_id strSpam = new string_id("space/space_interaction", "view");
                    mi.addRootMenu(menu_info_types.SERVER_MENU3, strSpam);
                    string_id strSpam2 = new string_id("space/space_interaction", "unload");
                    mi.addRootMenu(menu_info_types.SERVER_MENU4, strSpam2);
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int completeAtmosphericShipCall(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = params.getObjId("player");
        obj_id ship = params.getObjId("ship");
        location callLocation = params.getLocation("callLocation");
        atmospheric_ship.completeCallDown(self, player, ship, callLocation);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.VEHICLE_GENERATE)
        {
            atmospheric_ship.callDown(self, player);
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.VEHICLE_STORE)
        {
            obj_id groundShip = atmospheric_ship.getShipForControlDevice(self);
            if (isIdValid(groundShip) && getPilotId(groundShip) == player)
            {
                atmospheric_ship.land(self, player);
            }
            else
            {
                atmospheric_ship.store(self, player);
            }
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_ATMOSPHERIC_PILOT)
        {
            obj_id atmosphericShip = atmospheric_ship.getShipForControlDevice(self);
            atmospheric_ship.pilot(player, atmosphericShip);
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_ATMOSPHERIC_LAUNCH_SPACE)
        {
            atmospheric_ship.launchToSpace(self, player);
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_PARK)
        {
            obj_id housingShip = atmospheric_ship.getShipForControlDevice(self);
            if (atmospheric_ship.getControlState(self) == atmospheric_ship.STATE_PARKED)
            {
                atmospheric_ship.unparkHousing(self, player);
            }
            else
            {
                atmospheric_ship.parkAsHousing(self, player);
            }
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_STATUS)
        {
            showAtmosphericHousingStatus(self, player);
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_PAY_MAINTENANCE)
        {
            sui.inputbox(self, player, "Enter the number of credits to add to this ship's maintenance pool.", sui.OK_CANCEL, "POB Ship Maintenance", sui.INPUT_NORMAL, null, "payAtmosphericHousingMaintenance", null);
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_RESIDENCE)
        {
            atmospheric_ship.declareResidence(self, player);
            return SCRIPT_CONTINUE;
        }
        if (item == MENU_ACCESS)
        {
            showAtmosphericHousingAccess(self, player);
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            obj_id objShip = atmospheric_ship.getShipForControlDevice(self);
            if (isIdValid(objShip))
            {
                String strChassisType = getShipChassisType(objShip);
                if (strChassisType.equals("player_sorosuub_space_yacht"))
                {
                    string_id strSpam = new string_id("space/space_interaction", "complete_repair");
                    sendSystemMessage(player, strSpam);
                    space_crafting.repairDamage(player, objShip, 1.0f);
                }
            }
        }
        if (item == menu_info_types.SERVER_MENU1)
        {
            if (isSpaceScene())
            {
                string_id strSpam = new string_id("space/space_interaction", "no_rename_space");
                sendSystemMessage(player, strSpam);
                return SCRIPT_CONTINUE;
            }
            sui.inputbox(self, player, utils.packStringId(PROMPT1), sui.OK_CANCEL, utils.packStringId(RENAME_SHIP), sui.INPUT_NORMAL, null, "renameShip", null);
        }
        if (item == menu_info_types.SERVER_MENU2)
        {
            obj_id objShip = space_transition.getShipFromShipControlDevice(self);
            if (!isIdValid(objShip))
            {
                return SCRIPT_CONTINUE;
            }
            int[] installed = space_crafting.getShipInstalledSlots(objShip);
            if (getIntObjVar(self, IN_USE_OBJVAR) == 1)
            {
                return SCRIPT_CONTINUE;
            }
            setObjVar(self, IN_USE_OBJVAR, 1);
            if ((getShipChassisType(objShip)).equals("player_sorosuub_space_yacht"))
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "space_yacht"));
                removeObjVar(self, IN_USE_OBJVAR);
                return SCRIPT_CONTINUE;
            }
            if ((getShipChassisType(objShip)).equals("player_prototype_z95") || (getShipChassisType(objShip)).equals("player_prototype_tiefighter") || (getShipChassisType(objShip)).equals("player_prototype_hutt_light"))
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "newbie_ship"));
                removeObjVar(self, "ship_redeed.inUse");
                return SCRIPT_CONTINUE;
            }
            if ((getShipChassisType(objShip)).equals("player_basic_z95") || (getShipChassisType(objShip)).equals("player_basic_tiefighter") || (getShipChassisType(objShip)).equals("player_basic_hutt_light"))
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "newbie_ship"));
                removeObjVar(self, IN_USE_OBJVAR);
                return SCRIPT_CONTINUE;
            }
            if (space_utils.isShipWithInterior(objShip))
            {
                int intItemCount = player_structure.getStructureNumItems(objShip);
                if (intItemCount > 0)
                {
                    sendSystemMessage(player, new string_id("space/space_interaction", "items_in_ship"));
                    removeObjVar(self, IN_USE_OBJVAR);
                    return SCRIPT_CONTINUE;
                }
            }
            if (installed.length == 0)
            {
                if (hasObjVar(self, space_crafting.TCG_SHIP_TYPE))
                {
                    String staticItemName = getStringObjVar(self, space_crafting.TCG_SHIP_DEED_STATIC_ITEM_NAME);
                    if (staticItemName == null || staticItemName.equals(""))
                    {
                        CustomerServiceLog("ship_redeed", "PLAYER " + player + " FAILED TO REDEED SHIP " + objShip + " FROM SCD " + self + " BECAUSE OF DEED STATIC ITEM NAME BEING INVALID");
                        return SCRIPT_CONTINUE;
                    }
                    obj_id pInv = utils.getInventoryContainer(player);
                    if (!isValidId(pInv) || !exists(pInv))
                    {
                        CustomerServiceLog("ship_redeed", "PLAYER " + player + " FAILED TO REDEED SHIP " + objShip + " FROM SCD " + self + " BECAUSE OF PLAYER INVENTORY WAS INVALID");
                        return SCRIPT_CONTINUE;
                    }
                    obj_id deedOid = static_item.createNewItemFunction(staticItemName, pInv);
                    if (!isValidId(deedOid) || !exists(deedOid))
                    {
                        CustomerServiceLog("ship_redeed", "PLAYER " + player + " FAILED TO REDEEDED SHIP " + objShip + " FROM SCD " + self + " TO MAKE TCG STATIC ITEM DEED " + staticItemName + ". The static item could not be created in player inventory: " + pInv);
                        return SCRIPT_CONTINUE;
                    }
                    sendSystemMessage(player, new string_id("space/space_interaction", "packed"));
                    CustomerServiceLog("ship_redeed", "PLAYER " + player + " REDEEDED SHIP " + objShip + " FROM SCD " + self + " TO MAKE TCG STATIC ITEM DEED " + staticItemName);
                    destroyObject(self);
                    return SCRIPT_CONTINUE;
                }
                obj_id newShip = packShipDeed(self, objShip, player);
                if (isIdValid(newShip))
                {
                    sendSystemMessage(player, new string_id("space/space_interaction", "packed"));
                    CustomerServiceLog("ship_redeed", "PLAYER " + player + " REDEEDED SHIP " + objShip + " FROM SCD " + self + " TO MAKE DEED " + newShip);
                    destroyObject(self);
                    return SCRIPT_CONTINUE;
                }
                else
                {
                    removeObjVar(self, IN_USE_OBJVAR);
                    return SCRIPT_CONTINUE;
                }
            }
            else 
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "components_installed"));
                removeObjVar(self, IN_USE_OBJVAR);
                return SCRIPT_CONTINUE;
            }
        }
        if (item == menu_info_types.SERVER_MENU3)
        {
            obj_id objShip = atmospheric_ship.getShipForControlDevice(self);
            if (!isIdValid(objShip))
            {
                return SCRIPT_CONTINUE;
            }
            if (getShipCargoHoldContentsCurrent(objShip) <= 0)
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "empty_hold"));
                return SCRIPT_CONTINUE;
            }
            obj_id[] resourceTypes = getShipCargoHoldContentsResourceTypes(objShip);
            String[] entries = new String[resourceTypes.length];
            for (int i = 0; i < entries.length; i++)
            {
                string_id stIdResourceName = utils.unpackString(getResourceName(resourceTypes[i]));
                entries[i] = getShipCargoHoldContent(objShip, resourceTypes[i]) + " " + localize(stIdResourceName);
            }
            String prompt = utils.packStringId(new string_id(SPACE_MINING, "prompt"));
            String title = utils.packStringId(new string_id(SPACE_MINING, "title"));
            int pid = sui.listbox(self, player, prompt, sui.OK_ONLY, title, entries, "cargoList", false, false);
            showSUIPage(pid);
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.SERVER_MENU4)
        {
            obj_id objShip = atmospheric_ship.getShipForControlDevice(self);
            obj_id pInv = utils.getInventoryContainer(player);
            int maxInventorySpace = getVolumeFree(pInv);
            if (getShipCargoHoldContentsCurrent(objShip) <= 0)
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "empty_hold"));
                return SCRIPT_CONTINUE;
            }
            obj_id[] resourceTypes = getShipCargoHoldContentsResourceTypes(objShip);
            boolean resourcesGiven = false;
            for (obj_id resourceType : resourceTypes) {
                if (!giveResourceReward(resourceType, player, getShipCargoHoldContent(objShip, resourceType), objShip)) {
                    sendSystemMessage(player, new string_id("space/space_interaction", "full_inventory"));
                    break;
                } else {
                    resourcesGiven = true;
                }
            }
            if (resourcesGiven)
            {
                sendSystemMessage(player, new string_id("space/space_interaction", "resources_transferred"));
            }
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.DICE_ROLL)
        {
            obj_id objShip = space_transition.getShipFromShipControlDevice(self);
            if (!hasObjVar(objShip, player_structure.OBJVAR_STRUCTURE_STORAGE_INCREASE))
            {
                return SCRIPT_CONTINUE;
            }
            player_structure.displayAvailableNonGenericStorageTypes(player, self, objShip);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnAboutToBeTransferred(obj_id self, obj_id destContainer, obj_id transferer) throws InterruptedException
    {
        if (isIdValid(getContainedBy(self)))
        {
            obj_id[] contents = getContents(self);
            if (contents == null || contents.length == 0)
            {
                return SCRIPT_OVERRIDE;
            }
        }
        if (isIdValid(destContainer))
        {
            obj_id player = utils.getContainedBy(destContainer);
            if (isIdValid(player))
            {
                if (!isPlayer(player))
                {
                    sendSystemMessage(player, new string_id("space/space_interaction", "noadd"));
                    return SCRIPT_OVERRIDE;
                }
                if (utils.hasLocalVar(player, "ctsBeingUnpacked"))
                {
                    return SCRIPT_CONTINUE;
                }
                boolean belowLimit = space_transition.isPlayerBelowShipLimit(player, destContainer);
                if (belowLimit == true)
                {
                    return SCRIPT_CONTINUE;
                }
                else 
                {
                    sendSystemMessage(player, new string_id("space/space_interaction", "toomanyships"));
                    return SCRIPT_OVERRIDE;
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnDestroy(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, IN_USE_OBJVAR))
        {
            obj_id ship = space_transition.getShipFromShipControlDevice(self);
            if (isIdValid(ship))
            {
                String type = getShipChassisType(ship);
                obj_id player = utils.getContainingPlayer(self);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int forceCollectionReactorInit(obj_id self, dictionary params) throws InterruptedException
    {
        messageTo(self, "checkCollectionReactor", null, 2, false);
        return SCRIPT_CONTINUE;
    }
    public int checkCollectionReactor(obj_id self, dictionary params) throws InterruptedException
    {
        CustomerServiceLog("ShipComponents", "Initializing Collection reactor check for: (" + self + ") " + getName(self) + " a ship control device in player datapad.");
        blog("component_fix", "SCD - CHECKING SHIP " + self + " FOR COLLECTION REACTOR");
        obj_id ship = space_transition.getShipFromShipControlDevice(self);
        if (!isIdValid(ship))
        {
            return SCRIPT_CONTINUE;
        }
        boolean success = space_crafting.checkForCollectionReactor(self, ship);
        if (success)
        {
            CustomerServiceLog("ShipComponents", "Collection reactor found and handled for: (" + self + ") " + getName(self));
            blog("component_fix", "SCD - checkCollectionReactor: SUCCESS FROM space_crafting library");
            return SCRIPT_CONTINUE;
        }
        CustomerServiceLog("ShipComponents", "Collection reactor NOT found or failed to uninstall for: (" + self + ") " + getName(self));
        blog("component_fix", "SCD - checkCollectionReactor: FAIL FROM space_crafting library");
        return SCRIPT_CONTINUE;
    }
    public int handleStorageRedeedChoice(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        String accessFee = sui.getInputBoxText(params);
        int btn = sui.getIntButtonPressed(params);
        if (btn == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id objShip = space_transition.getShipFromShipControlDevice(self);
        if (!isIdValid(objShip) || getOwner(objShip) != player)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(objShip, player_structure.OBJVAR_STRUCTURE_STORAGE_INCREASE))
        {
            int storageRedeedSelected = 0;
            if (params.containsKey(sui.LISTBOX_LIST + "." + sui.PROP_SELECTEDROW))
            {
                storageRedeedSelected = sui.getListboxSelectedRow(params);
                if (storageRedeedSelected < 0)
                {
                    return SCRIPT_CONTINUE;
                }
            }
            if (player_structure.decrementStorageAmount(player, objShip, self, storageRedeedSelected))
            {
                sendSystemMessage(player, new string_id("player_structure", "storage_increase_redeeded"));
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int cargoList(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int renameShip(obj_id self, dictionary params) throws InterruptedException
    {
        int intButton = sui.getIntButtonPressed(params);
        if (intButton == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        String newShipName = sui.getInputBoxText(params);
        if (isNameReserved(newShipName, ignoreRules) != true && newShipName.length() < 21)
        {
            setName(self, newShipName);
            obj_id objShip = space_transition.getShipFromShipControlDevice(self);
            if (isIdValid(objShip))
            {
                space_transition.setShipName(objShip, player, self);
            }
            return SCRIPT_CONTINUE;
        }
        else
        {
            string_id msg2 = new string_id("sui", "rename_ship_reserved");
            sendSystemMessage(player, msg2);
            return SCRIPT_CONTINUE;
        }
    }
    public obj_id packShipDeed(obj_id pcd, obj_id ship, obj_id player) throws InterruptedException
    {
        String type = getShipChassisType(ship);
        String newType = type.substring(7, type.length());
        obj_id inventory = utils.getInventoryContainer(player);
        float mass = getChassisComponentMassMaximum(ship);
        float hp = getShipMaximumChassisHitPoints(ship);
        float currentHp = getShipCurrentChassisHitPoints(ship);
        obj_id newDeed = createObject("object/tangible/ship/crafted/chassis/" + newType + "_deed.iff", inventory, "");
        if (isIdValid(newDeed))
        {
            setObjVar(newDeed, "ship_chassis.mass", mass);
            setObjVar(newDeed, "ship_chassis.hp", hp);
            setObjVar(newDeed, "ship_chassis.currentHp", currentHp);
            setObjVar(newDeed, "ship_chassis.type", newType);
            return newDeed;
        }
        else
        {
            return null;
        }
    }
    public void pobShipLotRefunder(obj_id pcd) throws InterruptedException
    {
        obj_id player = utils.getContainingPlayer(pcd);
        if (!isIdValid(player))
        {
            return;
        }
        if (!hasObjVar(pcd, "lotReqRemoved"))
        {
            obj_id ship = space_transition.getShipFromShipControlDevice(pcd);
            if (!isIdValid(ship))
            {
                return;
            }
            String type = getShipChassisType(ship);
            if (space_utils.isPobType(type))
            {
                adjustLotCount(getPlayerObject(player), -1);
                setObjVar(pcd, "lotReqRemoved", true);
                string_id strSpam = new string_id("space/space_interaction", "pob_lot_returned");
                sendSystemMessage(player, strSpam);
            }
        }
        return;
    }
    public boolean giveResourceReward(obj_id objResourceId, obj_id player, int intAmount, obj_id objShip) throws InterruptedException
    {
        obj_id objContainer = utils.getInventoryContainer(player);
        obj_id objStack = getResourceStack(objContainer, objResourceId);
        if (isIdValid(objStack))
        {
            int intCount = getResourceContainerQuantity(objStack);
            intCount += intAmount;
            if (intCount > MAX_RESOURCE)
            {
                intCount = intCount - MAX_RESOURCE;
                sendSystemMessageTestingOnly(player, "Add the Diff!  " + intCount);
                intAmount = intAmount - intCount;
                addResourceToContainer(objStack, objResourceId, intAmount, null);
                objStack = null;
            }
            else
            {
                addResourceToContainer(objStack, objResourceId, intAmount, null);
                sendSystemMessageTestingOnly(player, "Incrementing count!");
            }
        }
        else
        {
            objStack = createResourceCrate(objResourceId, intAmount, objContainer);
            if (objStack == null)
            {
                return false;
            }
        }
        setShipCargoHoldContent(objShip, objResourceId, 0);
        return true;
    }
    public obj_id getResourceStack(obj_id objContainer, obj_id objResource) throws InterruptedException
    {
        if (!isIdValid(objContainer))
        {
            return null;
        }
        obj_id[] objContents = getContents(objContainer);
        if (objContents == null)
        {
            return null;
        }
        for (obj_id objContent : objContents) {
            obj_id objType = getResourceContainerResourceType(objContent);
            if (objType == objResource) {
                int intCount = getResourceContainerQuantity(objContent);
                if (intCount < MAX_RESOURCE) {
                    return objContent;
                }
            }
        }
        return null;
    }
    public void gunshipCheck(obj_id objShip) throws InterruptedException
    {
        if (!isIdValid(objShip))
        {
            return;
        }
        String type = getShipChassisType(objShip);
        if (type.startsWith("player_gunship") && (!hasObjVar(objShip, "structure.capacity_override")))
        {
            int newCapacity;
            if (hasObjVar(objShip, "structureChange.storageIncrease"))
            {
                newCapacity = getIntObjVar(objShip, "structureChange.storageIncrease");
                newCapacity = newCapacity - 50;
                if (newCapacity <= 0)
                {
                    removeObjVar(objShip, "structureChange.storageIncrease");
                }
                else if (newCapacity > 0)
                {
                    setObjVar(objShip, "structureChange.storageIncrease", newCapacity);
                }
                setObjVar(objShip, "structure.capacity_override", 150);
            }
        }
        return;
    }
    public void showAtmosphericHousingStatus(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        if (!atmospheric_ship.isOwner(player, ship) || !atmospheric_ship.isParkedHousing(ship))
        {
            return;
        }
        int pool = getBankBalance(ship);
        int rate = atmospheric_ship.SMALL_HOUSE_MAINTENANCE_RATE;
        int secondsRemaining = pool > 0 ? (pool / rate) * atmospheric_ship.MAINTENANCE_HEARTBEAT_SECONDS : 0;
        String[] status = new String[]
        {
            "Ship: " + getAssignedName(self),
            "Maintenance pool: " + pool + " credits",
            "Maintenance rate: " + rate + " credits per 30 minutes",
            "Time remaining: " + utils.assembleTimeRemainToUse(secondsRemaining, false),
            "Lots used: " + atmospheric_ship.SMALL_HOUSE_LOTS,
            "Residence: " + (hasObjVar(ship, player_structure.VAR_RESIDENCE_BUILDING) ? "Yes" : "No"),
            "Access: " + (permissionsIsPublic(ship) ? "Public" : "Private"),
            "Condition: " + (hasObjVar(ship, atmospheric_ship.VAR_CONDEMNED) ? "Maintenance required" : "Good standing")
        };
        int pid = sui.listbox(self, player, "POB ships use the same maintenance rate as a small house.", sui.OK_ONLY, "POB Ship Housing Status", status, "noHandler", false, false);
        showSUIPage(pid);
    }
    public int payAtmosphericHousingMaintenance(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        if (!atmospheric_ship.isOwner(player, ship) || !atmospheric_ship.isParkedHousing(ship))
        {
            return SCRIPT_CONTINUE;
        }
        String text = sui.getInputBoxText(params);
        if (text == null || text.length() == 0)
        {
            return SCRIPT_CONTINUE;
        }
        int amount = utils.stringToInt(text);
        if (amount < 1 || amount > 100000)
        {
            return SCRIPT_CONTINUE;
        }
        player_structure.payMaintenance(player, ship, amount);
        return SCRIPT_CONTINUE;
    }
    public void showAtmosphericHousingAccess(obj_id self, obj_id player) throws InterruptedException
    {
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        if (!atmospheric_ship.isOwner(player, ship) || !atmospheric_ship.isParkedHousing(ship))
        {
            return;
        }
        String toggle = permissionsIsPublic(ship) ? "Make Private" : "Make Public";
        String[] choices = new String[] { toggle, "Add Entry Name", "Remove Entry Name", "View Entry List" };
        int pid = sui.listbox(self, player, "Manage who may enter this parked POB ship.", sui.OK_CANCEL, "POB Ship Access", choices, "handleAtmosphericHousingAccess", false, false);
        showSUIPage(pid);
    }
    public int handleAtmosphericHousingAccess(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        if (!atmospheric_ship.isOwner(player, ship) || !atmospheric_ship.isParkedHousing(ship))
        {
            return SCRIPT_CONTINUE;
        }
        int selected = sui.getListboxSelectedRow(params);
        if (selected == 0)
        {
            if (permissionsIsPublic(ship))
            {
                permissionsMakePrivate(ship);
            }
            else
            {
                permissionsMakePublic(ship);
            }
        }
        else if (selected == 1)
        {
            sui.inputbox(self, player, "Enter the character name to allow aboard.", sui.OK_CANCEL, "Add POB Entry Permission", sui.INPUT_NORMAL, null, "addAtmosphericHousingEntry", null);
        }
        else if (selected == 2)
        {
            sui.inputbox(self, player, "Enter the character name to remove.", sui.OK_CANCEL, "Remove POB Entry Permission", sui.INPUT_NORMAL, null, "removeAtmosphericHousingEntry", null);
        }
        else if (selected == 3)
        {
            String[] allowed = permissionsGetAllowed(ship);
            if (allowed == null || allowed.length == 0)
            {
                allowed = new String[] { "No names on the entry list." };
            }
            int pid = sui.listbox(self, player, "Characters allowed to enter this parked POB ship.", sui.OK_ONLY, "POB Entry List", allowed, "noHandler", false, false);
            showSUIPage(pid);
        }
        return SCRIPT_CONTINUE;
    }
    public int addAtmosphericHousingEntry(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        String name = sui.getInputBoxText(params);
        if (atmospheric_ship.isOwner(player, ship) && atmospheric_ship.isParkedHousing(ship) && name != null && name.length() > 0)
        {
            permissionsAddAllowed(ship, name);
        }
        return SCRIPT_CONTINUE;
    }
    public int removeAtmosphericHousingEntry(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = sui.getPlayerId(params);
        obj_id ship = atmospheric_ship.getShipForControlDevice(self);
        String name = sui.getInputBoxText(params);
        if (atmospheric_ship.isOwner(player, ship) && atmospheric_ship.isParkedHousing(ship) && name != null && name.length() > 0 && !name.equalsIgnoreCase(getPlayerName(player)))
        {
            permissionsRemoveAllowed(ship, name);
        }
        return SCRIPT_CONTINUE;
    }
    public boolean blog(String category, String msg) throws InterruptedException
    {
        return true;
    }
}
