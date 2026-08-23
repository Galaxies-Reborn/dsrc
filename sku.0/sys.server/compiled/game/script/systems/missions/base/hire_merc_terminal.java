package script.systems.missions.base;

import script.*;
import script.library.mercenary;
import script.library.sui;
import script.library.utils;

/** Adds the server-supported Hire a Merc surface to destroy/combat terminals. */
public class hire_merc_terminal extends script.base_script
{
    public hire_merc_terminal()
    {
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
        throws InterruptedException
    {
        if (mercenary.isCombatMissionTerminal(self) && isIdValid(player) &&
            isPlayer(player) && !isDead(player) && !isIncapacitated(player))
        {
            // SERVER_MENU2 is reserved by mission_terminal for slicing.
            mi.addRootMenu(menu_info_types.SERVER_MENU1, mercenary.SID_HIRE);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item)
        throws InterruptedException
    {
        if (item != menu_info_types.SERVER_MENU1)
        {
            return SCRIPT_CONTINUE;
        }
        if (!mercenary.canUseTerminal(self, player))
        {
            sendSystemMessage(player, mercenary.SID_OUT_OF_RANGE);
            return SCRIPT_CONTINUE;
        }

        // The stock mission browser's tabs are client-authored and expose no
        // server extension hook.  This exact localized radial opens the closest
        // supported tab-like roster list without replacing the native browser.
        String title = utils.packStringId(mercenary.SID_TITLE);
        String prompt = mercenary.getRosterPrompt(player);
        String[] entries = mercenary.getRosterEntries(player);
        sui.listbox(self, player, prompt, sui.OK_CANCEL, title, entries,
            "handleHireMercSelection");
        return SCRIPT_CONTINUE;
    }

    public int handleHireMercSelection(obj_id self, dictionary params)
        throws InterruptedException
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
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL)
        {
            return SCRIPT_CONTINUE;
        }
        if (!mercenary.canUseTerminal(self, player))
        {
            sendSystemMessage(player, mercenary.SID_OUT_OF_RANGE);
            return SCRIPT_CONTINUE;
        }
        int selected = sui.getListboxSelectedRow(params);
        if (!mercenary.isValidArchetype(selected))
        {
            sendSystemMessage(player, mercenary.SID_INVALID_SELECTION);
            return SCRIPT_CONTINUE;
        }
        mercenary.beginHire(self, player, selected);
        return SCRIPT_CONTINUE;
    }
}
