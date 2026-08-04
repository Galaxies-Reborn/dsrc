package script.quest.task.pgc;

import script.library.money;
import script.library.prose;
import script.library.utils;
import script.*;

public class credit_item extends script.base_script
{
    public credit_item()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "quest.task.pgc.credit_item");
        return SCRIPT_CONTINUE;
    }
    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int credits = getIntObjVar(self, "loot.cashAmount");
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        names[idx] = "amount";
        attribs[idx] = Integer.toString(credits);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuRequest(obj_id self, obj_id objPlayer, menu_info mi) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
}
