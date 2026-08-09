package script.test;

import script.obj_id;

public class qa_character extends script.base_script
{
    public qa_character()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "test.qa_character");
        sendSystemMessageTestingOnly(self, "The NGE class/template setup tool is retired. Use /qatool spec <PRE-CU skill box>.");
        return SCRIPT_CONTINUE;
    }
}
