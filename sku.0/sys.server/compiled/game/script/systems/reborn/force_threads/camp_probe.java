package script.systems.reborn.force_threads;

import script.*;
import script.library.force_threads;

public class camp_probe extends script.base_script
{
    public int OnTriggerVolumeEntered(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        if (force_threads.CAMP_VOLUME.equals(volumeName))
        {
            force_threads.beginAdvancedCampVisit(self, breacher);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnTriggerVolumeExited(obj_id self, String volumeName, obj_id breacher) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        if (force_threads.CAMP_VOLUME.equals(volumeName))
        {
            force_threads.endAdvancedCampVisit(self, breacher);
        }
        return SCRIPT_CONTINUE;
    }

    public int handleForceThreadsDwell(obj_id self, dictionary params) throws InterruptedException
    {
        if (!force_threads.isShadowEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        force_threads.completeAdvancedCampDwell(self, params);
        return SCRIPT_CONTINUE;
    }
}
