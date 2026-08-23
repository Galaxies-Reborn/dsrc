package script.item.survey_droid;

import script.dictionary;
import script.location;
import script.menu_info;
import script.menu_info_data;
import script.menu_info_types;
import script.obj_id;
import script.prose_package;
import script.string_id;
import script.library.consumable;
import script.library.craftinglib;
import script.library.player_structure;
import script.library.prose;
import script.library.sui;
import script.library.utils;

public class survey_droid_device extends script.base_script
{
    public survey_droid_device()
    {
    }

    public static final String STF_FILE = "pet/droid_modules";
    public static final string_id SID_PLANET_TITLE = new string_id(STF_FILE, "survey_planet_title");
    public static final string_id SID_PLANET_PROMPT = new string_id(STF_FILE, "survey_planet_prompt");
    public static final string_id SID_YOU_MUST_BE_OUTDOORS = new string_id(STF_FILE, "you_must_be_outdoors");
    public static final string_id SID_SURVEY_DROID_LAUNCHED = new string_id(STF_FILE, "survey_droid_launched");
    public static final string_id DISPLAY_NAME =
        new string_id("precu_container_droid", "survey_droid_n");
    public static final String SEEKER_VISUAL = "object/creature/npc/droid/bounty_seeker.iff";
    public static final int MIN_SURVEY_TIME = 15 * 60;
    public static final int MAX_SURVEY_TIME = 60 * 60;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, DISPLAY_NAME);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, DISPLAY_NAME);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        menu_info_data useMenu = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (useMenu == null)
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("", ""));
        }
        else
        {
            useMenu.setServerNotify(true);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item != menu_info_types.ITEM_USE)
        {
            return SCRIPT_CONTINUE;
        }
        if (!canLaunch(self, player))
        {
            return SCRIPT_CONTINUE;
        }

        String title = utils.packStringId(SID_PLANET_TITLE);
        String prompt = utils.packStringId(SID_PLANET_PROMPT);
        String[] planetNames = new String[script.library.survey_droid.PLANET_INTERNAL.length];
        for (int i = 0; i < script.library.survey_droid.PLANET_INTERNAL.length; i++)
        {
            planetNames[i] = utils.packStringId(new string_id(
                "planet_n",
                script.library.survey_droid.PLANET_INTERNAL[i]));
        }
        sui.listbox(self, player, prompt, sui.OK_CANCEL, title, planetNames, "handlePlanetSelection");
        return SCRIPT_CONTINUE;
    }

    public int handlePlanetSelection(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        if (!canLaunch(self, player) || sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }

        int selectedRow = sui.getListboxSelectedRow(params);
        if (selectedRow < 0 || selectedRow >= script.library.survey_droid.PLANET_INTERNAL.length)
        {
            return SCRIPT_CONTINUE;
        }

        String planet = script.library.survey_droid.PLANET_INTERNAL[selectedRow];
        int surveyTime = getSurveyTime(self, player);
        dictionary reportRequest = new dictionary();
        reportRequest.put("planetName", planet);
        boolean queued = messageTo(
            player,
            "handleSurveyDroidWaypointReport",
            reportRequest,
            surveyTime,
            true);
        if (!queued)
        {
            sendSystemMessage(
                player,
                "The Survey Droid could not establish a return route. It was not consumed.",
                null);
            return SCRIPT_CONTINUE;
        }

        launchSeekerVisual(player);
        consumable.decrementCharges(self, player);

        String runTime = player_structure.assembleTimeRemaining(
            player_structure.convertSecondsTime(surveyTime));
        prose_package launched = prose.getPackage(SID_SURVEY_DROID_LAUNCHED);
        prose.setTT(launched, runTime);
        sendSystemMessageProse(player, launched);
        return SCRIPT_CONTINUE;
    }

    public boolean canLaunch(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(self) || !exists(self) || !isValidId(player) || !isPlayer(player) ||
            !utils.isNestedWithin(self, player))
        {
            return false;
        }
        location playerLocation = getLocation(player);
        if (playerLocation == null || isValidId(playerLocation.cell))
        {
            sendSystemMessage(player, SID_YOU_MUST_BE_OUTDOORS);
            return false;
        }
        if (isSpaceScene())
        {
            sendSystemMessage(player, new string_id("mission/mission_generic", "in_space"));
            return false;
        }
        return true;
    }

    public int getSurveyTime(obj_id self, obj_id player) throws InterruptedException
    {
        float quality = getFloatObjVar(
            self,
            craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + ".mechanism_quality");
        if (quality < 0.0f)
        {
            quality = 0.0f;
        }
        else if (quality > 100.0f)
        {
            quality = 100.0f;
        }
        int surveyTime = MIN_SURVEY_TIME +
            (int)((MAX_SURVEY_TIME - MIN_SURVEY_TIME) * ((100.0f - quality) / 100.0f));
        if (isGod(player))
        {
            String configuredDelay = getConfigSetting("GameServer", "InterplanetarySurveyDelay");
            if (configuredDelay != null && !configuredDelay.equals(""))
            {
                surveyTime = Integer.parseInt(configuredDelay);
            }
        }
        return surveyTime;
    }

    public void launchSeekerVisual(obj_id player) throws InterruptedException
    {
        location spawnLocation = getLocation(player);
        location heading = getHeading(player);
        if (spawnLocation == null || heading == null)
        {
            return;
        }
        spawnLocation.x += heading.x;
        spawnLocation.z += heading.z;
        obj_id seeker = createObject(SEEKER_VISUAL, spawnLocation);
        if (isValidId(seeker))
        {
            messageTo(seeker, "takeOff", null, 5.0f, true);
        }
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }

        int count = getCount(self);
        if (count > 0)
        {
            names[idx] = "quantity";
            attribs[idx] = Integer.toString(count);
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }

        if (hasObjVar(self, craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + ".mechanism_quality"))
        {
            names[idx] = "mechanism_quality";
            int value = (int)getFloatObjVar(
                self,
                craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + ".mechanism_quality");
            attribs[idx] = Integer.toString(value);
        }
        return SCRIPT_CONTINUE;
    }
}
