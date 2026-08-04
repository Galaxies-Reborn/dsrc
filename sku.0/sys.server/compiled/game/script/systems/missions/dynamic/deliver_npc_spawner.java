package script.systems.missions.dynamic;

import script.library.create;
import script.location;
import script.obj_id;

public class deliver_npc_spawner extends script.base_script
{
    public deliver_npc_spawner()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        location here = getLocation(self);
        obj_id npc = create.object("commoner", here);
        attachScript(npc, "systems.missions.dynamic.mission_deliver_npc");
        return SCRIPT_CONTINUE;
    }
}
