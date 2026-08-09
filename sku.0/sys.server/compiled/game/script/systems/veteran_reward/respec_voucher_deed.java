package script.systems.veteran_reward;

import script.*;
import script.library.sui;
import script.library.utils;

public class respec_voucher_deed extends script.base_script
{
    public respec_voucher_deed()
    {
    }
    public static final String STF_FILE = "veteran";
    public static final string_id RESPEC_VOUCHER = new string_id(STF_FILE, "respec_voucher");
    public static final string_id SID_RESPEC_VOUCHER_TITLE = new string_id(STF_FILE, "sui_respec_title");
    public static final string_id SID_RESPEC_VOUCHER_PROMPT = new string_id(STF_FILE, "sui_respec_prompt");
    public static final boolean POST_P14_VETERAN_RESPEC_VOUCHER_RETIRED = true;
    public static boolean isPostP14VeteranRespecVoucherRetired()
    {
        return POST_P14_VETERAN_RESPEC_VOUCHER_RETIRED;
    }
    public static void retirePostP14VeteranRespecVoucherScript(obj_id voucher) throws InterruptedException
    {
        if (isIdValid(voucher) && exists(voucher) && hasScript(voucher, "systems.veteran_reward.respec_voucher_deed"))
        {
            detachScript(voucher, "systems.veteran_reward.respec_voucher_deed");
        }
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        retirePostP14VeteranRespecVoucherScript(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retirePostP14VeteranRespecVoucherScript(self);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        retirePostP14VeteranRespecVoucherScript(self);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        retirePostP14VeteranRespecVoucherScript(self);
        return SCRIPT_CONTINUE;
    }
    public int handleRespecChoice(obj_id self, dictionary params) throws InterruptedException
    {
        retirePostP14VeteranRespecVoucherScript(self);
        return SCRIPT_CONTINUE;
    }
}
