package script.item.conversion;

import script.*;
import script.library.armor;
import script.library.sui;
import script.library.utils;

public class armor_base_conversion extends script.base_script
{
    public armor_base_conversion()
    {
    }
    public static final String ARMOR_SET_PREFIX = "object/tangible/wearables/armor/";
    public static final String[] ARMOR_SET_ASSAULT =
    {
    };
    public static final String[] ARMOR_SET_BATTLE =
    {
    };
    public static final String[] ARMOR_SET_RECON =
    {
    };
    public static final String[] ARMOR_TYPE =
    {
        "Assault",
        "Battle",
        "Reconnaissance"
    };
    public static final String[] ASSAULT_TYPE =
    {
    };
    public static final String[] BATTLE_TYPE =
    {
    };
    public static final String[] RECON_TYPE =
    {
    };
    public static final boolean POST_P14_ARMOR_REFIT_RETIRED = true;
    private static final String[] RETIRED_POST_P14_ARMOR_REFIT_SCRIPTS =
    {
        "item.conversion.armor_base_conversion",
        "item.conversion.armor_bicep_l_conversion",
        "item.conversion.armor_bicep_l_ith_conversion",
        "item.conversion.armor_bicep_r_conversion",
        "item.conversion.armor_bicep_r_ith_conversion",
        "item.conversion.armor_boots_conversion",
        "item.conversion.armor_boots_ith_conversion",
        "item.conversion.armor_bracer_l_conversion",
        "item.conversion.armor_bracer_l_ith_conversion",
        "item.conversion.armor_bracer_l_wookie_conversion",
        "item.conversion.armor_bracer_r_conversion",
        "item.conversion.armor_bracer_r_ith_conversion",
        "item.conversion.armor_bracer_r_wookie_conversion",
        "item.conversion.armor_chest_conversion",
        "item.conversion.armor_chest_ith_conversion",
        "item.conversion.armor_chest_wookie_conversion",
        "item.conversion.armor_gloves_conversion",
        "item.conversion.armor_gloves_ith_conversion",
        "item.conversion.armor_helmet_conversion",
        "item.conversion.armor_helmet_ith_conversion",
        "item.conversion.armor_leggings_conversion",
        "item.conversion.armor_leggings_ith_conversion",
        "item.conversion.armor_leggings_wookie_conversion"
    };
    public static boolean isPostP14ArmorRefitRetired()
    {
        return POST_P14_ARMOR_REFIT_RETIRED;
    }
    public static void retirePostP14ArmorRefitScriptState(obj_id item) throws InterruptedException
    {
        if (!isIdValid(item) || !exists(item))
        {
            return;
        }
        for (String retiredScript : RETIRED_POST_P14_ARMOR_REFIT_SCRIPTS)
        {
            if (hasScript(item, retiredScript))
            {
                detachScript(item, retiredScript);
            }
        }
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        retirePostP14ArmorRefitScriptState(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retirePostP14ArmorRefitScriptState(self);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        retirePostP14ArmorRefitScriptState(self);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        retirePostP14ArmorRefitScriptState(self);
        return SCRIPT_CONTINUE;
    }
    public void showConfirmationWindow(obj_id player) throws InterruptedException
    {
        return;
    }
    public void refitArmor(obj_id player, String newItemTemplate, obj_id oldObject, int armorCategory) throws InterruptedException
    {
        return;
    }
    public void closeOldWindow(obj_id player) throws InterruptedException
    {
        if (utils.hasScriptVar(player, "refit_armor.pid"))
        {
            int oldpid = utils.getIntScriptVar(player, "refit_armor.pid");
            forceCloseSUIPage(oldpid);
            utils.removeScriptVar(player, "refit_armor.pid");
        }
    }
    public void setWindowPid(obj_id player, int pid) throws InterruptedException
    {
        return;
    }
    public int handleConfirmationSelect(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handleArmorType(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handleAssaultSelect(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handleBattleSelect(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handleReconSelect(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public String[] getAssaultTemplates() throws InterruptedException
    {
        return ARMOR_SET_ASSAULT;
    }
    public String[] getBattleTemplates() throws InterruptedException
    {
        return ARMOR_SET_BATTLE;
    }
    public String[] getReconTemplates() throws InterruptedException
    {
        return ARMOR_SET_RECON;
    }
    public String[] getAssaultTypes() throws InterruptedException
    {
        return ASSAULT_TYPE;
    }
    public String[] getBattleTypes() throws InterruptedException
    {
        return BATTLE_TYPE;
    }
    public String[] getReconTypes() throws InterruptedException
    {
        return RECON_TYPE;
    }
}
