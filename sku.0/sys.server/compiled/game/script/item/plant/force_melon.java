package script.item.plant;

import script.library.static_item;
import script.obj_id;

public class force_melon extends script.base_script
{
    public force_melon()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (hasScript(self, "item.comestible.crafted"))
        {
            detachScript(self, "item.comestible.crafted");
        }
        if (!hasScript(self, "item.medicine.stimpack"))
        {
            attachScript(self, "item.medicine.stimpack");
        }
        if (!hasObjVar(self, "healing.power"))
        {
            setObjVar(self, "healing.power", 1000);
        }
        static_item.removeLegacyNgeItemCombatLevelRequirement(self);
        return SCRIPT_CONTINUE;
    }
}
