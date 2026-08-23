package script.library;

import script.*;

public class account_containers extends script.base_script
{
    public account_containers()
    {
    }

    public static final String SCRIPT_ACCOUNT_BOUND = "item.container.account_bound";
    public static final String SCRIPT_NO_DESTROY = "item.special.nodestroy";

    public static final String VAR_ROOT = "accountContainer";
    public static final String VAR_KIND = VAR_ROOT + ".kind";
    public static final String VAR_STATION_ID = VAR_ROOT + ".stationId";
    public static final String VAR_NAME_INITIALIZED = VAR_ROOT + ".nameInitialized";

    public static final String KIND_RESOURCES = "resources";
    public static final String KIND_SHIP_PARTS = "shipParts";
    public static final String KIND_CRAFT_COMPONENTS = "craftComponents";
    public static final String KIND_VEHICLES = "vehicles";
    public static final String KIND_DROIDS = "droids";

    public static final String TEMPLATE_RESOURCES = "object/intangible/container/resource_container.iff";
    public static final String TEMPLATE_SHIP_PARTS = "object/intangible/container/ship_part_container_1000.iff";
    public static final String TEMPLATE_SHIP_PARTS_PREFIX = "object/intangible/container/ship_part_container_";
    public static final String TEMPLATE_CRAFT_COMPONENTS = "object/intangible/container/craft_component_container.iff";
    public static final String TEMPLATE_VEHICLES = "object/intangible/container/vehicle_container.iff";
    public static final String TEMPLATE_DROIDS = "object/intangible/container/droid_container.iff";
    // This live vehicle-table entry predates GOT_data_vehicle_control_device
    // and is uniquely typed as a pet PCD despite being a true vehicle device.
    public static final String TEMPLATE_WALKER_AT_RT_REG_PCD =
        "object/intangible/vehicle/walker_at_rt_reg_pcd.iff";

    public static final String[] KINDS =
    {
        KIND_RESOURCES,
        KIND_SHIP_PARTS,
        KIND_CRAFT_COMPONENTS,
        KIND_VEHICLES,
        KIND_DROIDS
    };

    public static final String[] TEMPLATES =
    {
        TEMPLATE_RESOURCES,
        TEMPLATE_SHIP_PARTS,
        TEMPLATE_CRAFT_COMPONENTS,
        TEMPLATE_VEHICLES,
        TEMPLATE_DROIDS
    };

    public static final String[] DEFAULT_NAMES =
    {
        "Resource Crates",
        "Spaceship Parts",
        "Craft Components",
        "Vehicles",
        "Droids"
    };

    public static final String NAME_TABLE = "precu_container_droid";
    public static final String[] DEFAULT_NAME_KEYS =
    {
        "resource_crates_n",
        "spaceship_parts_n",
        "craft_components_n",
        "vehicles_n",
        "droids_n"
    };

    public static void ensureContainers(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player))
        {
            return;
        }

        obj_id datapad = utils.getPlayerDatapad(player);
        if (!isIdValid(datapad))
        {
            return;
        }

        int stationId = getPlayerStationId(player);
        for (int i = 0; i < KINDS.length; ++i)
        {
            obj_id container = findContainer(datapad, KINDS[i], stationId);
            if (!isIdValid(container))
            {
                container = createObjectOverloaded(TEMPLATES[i], datapad);
                if (!isIdValid(container))
                {
                    CustomerServiceLog("account_container", "Unable to create " + KINDS[i] + " container for player " + player + " (station " + stationId + ").");
                    continue;
                }
            }

            initializeContainer(container);
            bindContainer(container, player);
        }

        migrateLegacyCraftComponents(player);
    }

    public static void initializeContainer(obj_id container) throws InterruptedException
    {
        if (!isIdValid(container))
        {
            return;
        }

        String templateKind = getKindForTemplate(getTemplateName(container));
        if (templateKind == null || templateKind.equals(""))
        {
            return;
        }

        String kind = getContainerKind(container);
        if (!hasObjVar(container, VAR_KIND) || !templateKind.equals(kind))
        {
            kind = templateKind;
            setObjVar(container, VAR_KIND, kind);
        }

        if (!hasObjVar(container, "noTrade"))
        {
            setObjVar(container, "noTrade", 1);
        }
        if (!hasObjVar(container, "noTradeShared"))
        {
            setObjVar(container, "noTradeShared", 1);
        }

        if (!hasScript(container, SCRIPT_ACCOUNT_BOUND))
        {
            attachScript(container, SCRIPT_ACCOUNT_BOUND);
        }
        if (!hasScript(container, SCRIPT_NO_DESTROY))
        {
            attachScript(container, SCRIPT_NO_DESTROY);
        }

        if (!hasObjVar(container, VAR_NAME_INITIALIZED))
        {
            String defaultNameKey = getDefaultNameKey(kind);
            if (defaultNameKey != null && !defaultNameKey.equals(""))
            {
                setName(
                    container,
                    new string_id(NAME_TABLE, defaultNameKey));
            }
            setObjVar(container, VAR_NAME_INITIALIZED, 1);
        }
    }

    public static void bindContainer(obj_id container, obj_id player) throws InterruptedException
    {
        if (!isIdValid(container) || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }

        int stationId = getPlayerStationId(player);
        if (stationId <= 0)
        {
            return;
        }

        if (!hasObjVar(container, VAR_STATION_ID))
        {
            setObjVar(container, VAR_STATION_ID, stationId);
            CustomerServiceLog("account_container", "Bound " + container + " (" + getContainerKind(container) + ") to station " + stationId + " for player " + player + ".");
        }
    }

    public static obj_id getContainer(obj_id player, String kind) throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player))
        {
            return obj_id.NULL_ID;
        }
        obj_id datapad = utils.getPlayerDatapad(player);
        if (!isIdValid(datapad))
        {
            return obj_id.NULL_ID;
        }
        return findContainer(datapad, kind, getPlayerStationId(player));
    }

    public static boolean isContainerForPlayer(obj_id container, String kind, obj_id player) throws InterruptedException
    {
        if (!isIdValid(container) || !isIdValid(player) || !isPlayer(player))
        {
            return false;
        }
        if (!isManagedContainer(container) || !kind.equals(getContainerKind(container)))
        {
            return false;
        }
        if (utils.getContainingPlayer(container) != player)
        {
            return false;
        }
        return isBoundToPlayer(container, player);
    }

    public static boolean isManagedContainer(obj_id container) throws InterruptedException
    {
        if (!isIdValid(container) || !hasScript(container, SCRIPT_ACCOUNT_BOUND))
        {
            return false;
        }
        String templateKind = getKindForTemplate(getTemplateName(container));
        String kind = getContainerKind(container);
        return templateKind != null && !templateKind.equals("") && templateKind.equals(kind);
    }

    public static boolean isBoundToContainingAccount(obj_id container) throws InterruptedException
    {
        if (!isIdValid(container))
        {
            return false;
        }
        obj_id player = utils.getContainingPlayer(container);
        return isIdValid(player) && isPlayer(player) && isBoundToPlayer(container, player);
    }

    public static boolean isBoundToPlayer(obj_id container, obj_id player) throws InterruptedException
    {
        if (!isIdValid(container) || !isIdValid(player) || !isPlayer(player))
        {
            return false;
        }

        int stationId = getPlayerStationId(player);
        if (!hasObjVar(container, VAR_STATION_ID))
        {
            return stationId <= 0;
        }
        int boundStationId = getIntObjVar(container, VAR_STATION_ID);
        return boundStationId > 0 && stationId == boundStationId;
    }

    public static boolean mayHoldItem(obj_id container, obj_id item) throws InterruptedException
    {
        if (!isIdValid(container) || !isIdValid(item))
        {
            return false;
        }

        String kind = getContainerKind(container);
        int got = getGameObjectType(item);
        if (KIND_RESOURCES.equals(kind))
        {
            return isGameObjectTypeOf(got, GOT_resource_container);
        }
        if (KIND_SHIP_PARTS.equals(kind))
        {
            return isGameObjectTypeOf(got, GOT_ship_component);
        }
        if (KIND_CRAFT_COMPONENTS.equals(kind))
        {
            return isGameObjectTypeOf(got, GOT_component) || armor.isArmorComponent(got);
        }
        if (KIND_VEHICLES.equals(kind))
        {
            return got == GOT_data_vehicle_control_device ||
                TEMPLATE_WALKER_AT_RT_REG_PCD.equals(getTemplateName(item));
        }
        if (KIND_DROIDS.equals(kind))
        {
            return got == GOT_data_droid_control_device;
        }
        return false;
    }

    public static String getContainerKind(obj_id container) throws InterruptedException
    {
        if (!isIdValid(container))
        {
            return "";
        }
        if (hasObjVar(container, VAR_KIND))
        {
            return getStringObjVar(container, VAR_KIND);
        }
        return getKindForTemplate(getTemplateName(container));
    }

    private static void migrateLegacyCraftComponents(obj_id player) throws InterruptedException
    {
        obj_id resourceContainer = getContainer(player, KIND_RESOURCES);
        obj_id componentContainer = getContainer(player, KIND_CRAFT_COMPONENTS);
        if (!isIdValid(resourceContainer) || !isIdValid(componentContainer))
        {
            return;
        }

        obj_id[] contents = getContents(resourceContainer);
        if (contents == null || contents.length == 0)
        {
            return;
        }

        for (obj_id content : contents)
        {
            if (isIdValid(content))
            {
                int got = getGameObjectType(content);
                if ((isGameObjectTypeOf(got, GOT_component) || armor.isArmorComponent(got)) &&
                    !putIn(content, componentContainer, player))
                {
                    CustomerServiceLog("account_container", "Unable to migrate craft component " + content + " from " + resourceContainer + " to " + componentContainer + " for player " + player + ".");
                }
            }
        }
    }

    private static obj_id findContainer(obj_id datapad, String kind, int stationId) throws InterruptedException
    {
        obj_id[] contents = getContents(datapad);
        if (contents == null || contents.length == 0)
        {
            return obj_id.NULL_ID;
        }

        obj_id legacyContainer = obj_id.NULL_ID;
        for (obj_id content : contents)
        {
            if (!isIdValid(content))
            {
                continue;
            }

            String contentKind = getContainerKind(content);
            if (!kind.equals(contentKind))
            {
                continue;
            }

            if (hasObjVar(content, VAR_STATION_ID))
            {
                int boundStationId = getIntObjVar(content, VAR_STATION_ID);
                if (stationId <= 0 || boundStationId == stationId)
                {
                    return content;
                }
                continue;
            }

            if (!isIdValid(legacyContainer))
            {
                legacyContainer = content;
            }
        }
        return legacyContainer;
    }

    private static String getKindForTemplate(String template) throws InterruptedException
    {
        if (template == null || template.equals(""))
        {
            return "";
        }
        if (TEMPLATE_RESOURCES.equals(template))
        {
            return KIND_RESOURCES;
        }
        if (template.startsWith(TEMPLATE_SHIP_PARTS_PREFIX) && template.endsWith(".iff"))
        {
            return KIND_SHIP_PARTS;
        }
        if (TEMPLATE_CRAFT_COMPONENTS.equals(template))
        {
            return KIND_CRAFT_COMPONENTS;
        }
        if (TEMPLATE_VEHICLES.equals(template))
        {
            return KIND_VEHICLES;
        }
        if (TEMPLATE_DROIDS.equals(template))
        {
            return KIND_DROIDS;
        }
        return "";
    }

    private static String getDefaultNameKey(String kind) throws InterruptedException
    {
        for (int i = 0; i < KINDS.length; ++i)
        {
            if (KINDS[i].equals(kind))
            {
                return DEFAULT_NAME_KEYS[i];
            }
        }
        return "";
    }
}
