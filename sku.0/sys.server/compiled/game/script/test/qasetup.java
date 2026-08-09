package script.test;

import script.obj_id;

public class qasetup extends script.base_script
{
    public qasetup()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "test.qasetup");
        sendSystemMessageTestingOnly(self, "The NGE level-90 and expertise setup tool is retired. Use /qatool spec <PRE-CU skill box>.");
        return SCRIPT_CONTINUE;
    }
}
