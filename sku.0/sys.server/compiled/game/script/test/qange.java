package script.test;

import script.obj_id;

public class qange extends script.base_script
{
    public qange()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "test.qange");
        sendSystemMessageTestingOnly(self, "The NGE combat-level respec tool is retired. Use /qatool spec <PRE-CU skill box>.");
        return SCRIPT_CONTINUE;
    }
}
