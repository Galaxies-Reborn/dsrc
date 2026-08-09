package script.systems.veteran_reward;

import script.menu_info;
import script.menu_info_types;
import script.obj_id;

public class character_respec_reset_device extends script.base_script {

    public character_respec_reset_device() {}

    public static final boolean POST_P14_VETERAN_RESPEC_RESET_RETIRED = true;

    public static boolean isPostP14VeteranRespecResetRetired()
    {
        return POST_P14_VETERAN_RESPEC_RESET_RETIRED;
    }

    public static void retirePostP14VeteranRespecResetScript(obj_id device) throws InterruptedException
    {
        if (isIdValid(device) && exists(device) && hasScript(device, "systems.veteran_reward.character_respec_reset_device"))
        {
            detachScript(device, "systems.veteran_reward.character_respec_reset_device");
        }
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        retirePostP14VeteranRespecResetScript(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retirePostP14VeteranRespecResetScript(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info item) throws InterruptedException {
        retirePostP14VeteranRespecResetScript(self);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        retirePostP14VeteranRespecResetScript(self);
        return SCRIPT_CONTINUE;
    }
}
