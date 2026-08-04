package script.systems.storyteller.events;

import script.dictionary;
import script.obj_id;

public class anniversary_event_nyms extends script.base_script
{
    public anniversary_event_nyms()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        String deleteEventProps = getConfigSetting("GameServer", "deleteEventProps");
        if (deleteEventProps != null && deleteEventProps.length() > 0)
        {
            if (deleteEventProps.equals("true") || deleteEventProps.equals("1"))
            {
                destroyObject(self);
                return SCRIPT_CONTINUE;
            }
        }
        detachScript(self, "systems.storyteller.events.anniversary_event_nyms");
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        detachScript(self, "systems.storyteller.events.anniversary_event_nyms");
        return SCRIPT_CONTINUE;
    }
    public int handlePersistEventProp(obj_id self, dictionary params) throws InterruptedException
    {
        persistObject(self);
        return SCRIPT_CONTINUE;
    }
    public int handleDeleteEventProps(obj_id self, dictionary params) throws InterruptedException
    {
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }
}
