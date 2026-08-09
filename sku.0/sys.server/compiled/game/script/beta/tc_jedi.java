package script.beta;

import script.obj_id;

public class tc_jedi extends script.base_script
{
    public tc_jedi()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "beta.tc_jedi");
        sendSystemMessageTestingOnly(self, "The NGE Jedi conversion is retired. PRE-CU Jedi progression is preserved.");
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        detachScript(self, "beta.tc_jedi");
        sendSystemMessageTestingOnly(self, "The NGE Jedi conversion is retired. PRE-CU Jedi progression is preserved.");
        return SCRIPT_CONTINUE;
    }
}
