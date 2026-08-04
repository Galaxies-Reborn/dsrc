package script.systems.veteran_reward;

import script.menu_info;
import script.menu_info_types;
import script.obj_id;
import script.string_id;

public class instant_travel_terminal_deed extends script.base_script
{
    public instant_travel_terminal_deed()
    {
    }
    private static final String ITV_COMMAND_TABLE = "datatables/item/itv_command_list.iff";
    public static final string_id LEARN_ABILITY = new string_id("item_n", "instant_travel_terminal_learn");
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE instant-travel deed menu request for " + player);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        LOG("LOG_CHANNEL", "Ignored retired NGE instant-travel deed selection for " + player);
        return SCRIPT_CONTINUE;
    }
}
