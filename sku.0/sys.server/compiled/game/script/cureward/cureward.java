package script.cureward;

import script.dictionary;
import script.obj_id;

public class cureward extends script.base_script
{
    public static final boolean COMBAT_UPGRADE_REWARD_RUNTIME_RETIRED = true;
    public cureward()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "cureward.cureward");
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        detachScript(self, "cureward.cureward");
        return SCRIPT_CONTINUE;
    }
    public boolean createRewards(obj_id self) throws InterruptedException
    {
        detachScript(self, "cureward.cureward");
        return true;
    }
    public int handleRetryRewardNextLogin(obj_id self, dictionary params) throws InterruptedException
    {
        detachScript(self, "cureward.cureward");
        return SCRIPT_CONTINUE;
    }
}
