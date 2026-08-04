package script.item.levelup_orb;

import script.*;

public class levelup_orb extends script.base_script
{
    public levelup_orb()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        // Persisted post-era orbs remain loadable, but they must neither grant
        // combat levels nor remain permanently stuck in a PRE-CU inventory.
        if (hasScript(self, "item.special.nomove"))
        {
            detachScript(self, "item.special.nomove");
        }
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_OVERRIDE;
    }
}
