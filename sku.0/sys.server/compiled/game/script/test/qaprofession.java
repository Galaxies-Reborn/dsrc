package script.test;

import script.obj_id;

public class qaprofession extends script.base_script
{
    public qaprofession()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "test.qaprofession");
        sendSystemMessageTestingOnly(self, "The NGE profession and roadmap assistant is retired. Use /qatool spec <PRE-CU skill box>.");
        return SCRIPT_CONTINUE;
    }
}
