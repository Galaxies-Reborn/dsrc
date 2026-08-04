package script.npe;

import script.library.groundquests;
import script.location;
import script.obj_id;

public class handoff_to_tatooine extends script.base_script
{
    public handoff_to_tatooine()
    {
    }
    public static final String questNewbieStart = "quest/speeder_quest";
    public static final String questNewbieStartBH = "quest/speeder_quest";
    public static final String questCrafterEntertainer = "quest/tatooine_eisley_noncombat";
    public int OnLogin(obj_id self) throws InterruptedException
    {
        // The post-CU Tatooine handoff grants NPE quests and rewrites the
        // toolbar from one class template. Publish 14.1 startup owns neither.
        detachScript(self, "npe.handoff_to_tatooine");
        return SCRIPT_CONTINUE;
    }
}
