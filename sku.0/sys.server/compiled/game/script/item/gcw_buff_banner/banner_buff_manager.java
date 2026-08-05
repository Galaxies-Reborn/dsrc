package script.item.gcw_buff_banner;

import script.dictionary;
import script.library.trial;
import script.obj_id;

public class banner_buff_manager extends script.base_script
{
    public banner_buff_manager()
    {
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        trial.cleanupObject(self);
        return SCRIPT_CONTINUE;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        // The banner object and faction reward remain retained expansion
        // content. Its Roadmap class buffs are post-NGE gameplay authority,
        // so this manager now owns only the temporary visual lifetime.
        messageTo(self, "handleDeleteSelf", null, 180.0f, false);
        return SCRIPT_CONTINUE;
    }
    public int handleDeleteSelf(obj_id self, dictionary params) throws InterruptedException
    {
        trial.cleanupObject(self);
        return SCRIPT_CONTINUE;
    }
}
