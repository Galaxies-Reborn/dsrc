package script.player.reborn;

import script.*;
import script.library.force_threads;

public class force_threads_player extends script.base_script
{
    public int OnLogin(obj_id self) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        force_threads.reconcile(self);
        return SCRIPT_CONTINUE;
    }

    public int OnHealingReceived(obj_id self, obj_id healer, int actualDelta) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        force_threads.observeAdvancedCampHealing(self, actualDelta);
        return SCRIPT_CONTINUE;
    }

    public int OnQuestCompleted(obj_id self, int questCrc) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        force_threads.observeOutcome(self, questCrc);
        return SCRIPT_CONTINUE;
    }

    public int OnClusterWideDataResponse(obj_id self, String manager, String name, int requestId, String[] elementNames, dictionary[] data, int lockKey) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            force_threads.releaseOwnedClusterResponse(self, manager, requestId, lockKey);
            return SCRIPT_CONTINUE;
        }
        force_threads.handleClusterResponse(self, manager, requestId, elementNames, data, lockKey);
        return SCRIPT_CONTINUE;
    }
}
