package script.item.medicine;

import script.library.buff;
import script.library.healing;
import script.library.static_item;
import script.library.utils;
import script.*;

public class stimpack_crafted extends script.base_script
{
    public stimpack_crafted()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        static_item.removeLegacyNgeItemCombatLevelRequirement(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        static_item.removeLegacyNgeItemCombatLevelRequirement(self);
        return SCRIPT_CONTINUE;
    }
    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        static_item.removeLegacyNgeItemCombatLevelRequirement(self);
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, "healing.power"))
        {
            names[idx] = "healing_power";
            int value = getIntObjVar(self, "healing.power");
            attribs[idx] = Integer.toString(value);
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }
        names[idx] = "count";
        int value = getCount(self);
        attribs[idx] = Integer.toString(value);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (canManipulate(player, self, true, true, 15, true))
        {
            menu_info_data mid = mi.getMenuItemByType(menu_info_types.ITEM_USE);
            if (mid != null)
            {
                mid.setServerNotify(true);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        static_item.removeLegacyNgeItemCombatLevelRequirement(self);
        if (isDead(player) || isIncapacitated(player))
        {
            return SCRIPT_CONTINUE;
        }
        if (buff.hasBuff(player, "feign_death"))
        {
            return SCRIPT_CONTINUE;
        }
        if (item == menu_info_types.ITEM_USE)
        {
            if (hasObjVar(self, "healing.pool"))
            {
                int attrib = getIntObjVar(self, "healing.pool");
                boolean worked = healing.useHealDamageItem(player, self, attrib);
            }
            else
            {
                boolean worked = healing.useHealDamageItem(player, self);
            }
        }
        return SCRIPT_CONTINUE;
    }
}
