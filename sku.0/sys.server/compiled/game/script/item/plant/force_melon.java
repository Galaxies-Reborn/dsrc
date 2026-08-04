package script.item.plant;

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
        if (hasObjVar(self, "healing.combat_level_required"))
        {
            removeObjVar(self, "healing.combat_level_required");
        }
        return SCRIPT_CONTINUE;
    }
}
