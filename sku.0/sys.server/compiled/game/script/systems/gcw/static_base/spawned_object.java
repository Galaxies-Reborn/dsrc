package script.systems.gcw.static_base;

import script.dictionary;
import script.library.gcw;
import script.location;
import script.obj_id;

public class spawned_object extends script.base_script
{
    public spawned_object()
    {
    }
    public void cleanupRetiredFixedStaticBaseSpawn(obj_id self) throws InterruptedException
    {
        if (!gcw.isPostNgeFixedStaticBaseRetired())
        {
            return;
        }
        detachScript(self, "systems.gcw.static_base.spawned_object");
        destroyObject(self);
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        cleanupRetiredFixedStaticBaseSpawn(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        cleanupRetiredFixedStaticBaseSpawn(self);
        return SCRIPT_CONTINUE;
    }
    public int OnIncapacitated(obj_id self, obj_id killer) throws InterruptedException
    {
        if (gcw.isPostNgeFixedStaticBaseRetired())
        {
            cleanupRetiredFixedStaticBaseSpawn(self);
            return SCRIPT_CONTINUE;
        }
        location loc = getLocation(self);
        String datatable = "datatables/gcw/static_base/base_spawn_" + loc.area + ".iff";
        obj_id master = getObjIdObjVar(self, "master");
        if (!isIdValid(master))
        {
            return SCRIPT_OVERRIDE;
        }
        int spawnNum = getIntObjVar(self, "spawn_number");
        int status = getIntObjVar(self, "status");
        int respawnTime = dataTableGetInt(datatable, spawnNum, "respawn_time");
        if (respawnTime == 0)
        {
            respawnTime = 30;
        }
        dictionary info = new dictionary();
        info.put("spawnNumber", spawnNum);
        info.put("status", status);
        info.put("spawnObj", self);
        messageTo(master, "handleNpcDeath", info, respawnTime, false);
        return SCRIPT_CONTINUE;
    }
    public int OnObjectDisabled(obj_id self, obj_id killer) throws InterruptedException
    {
        if (gcw.isPostNgeFixedStaticBaseRetired())
        {
            cleanupRetiredFixedStaticBaseSpawn(self);
            return SCRIPT_CONTINUE;
        }
        location loc = getLocation(self);
        String datatable = "datatables/gcw/static_base/base_spawn_" + loc.area + ".iff";
        obj_id master = getObjIdObjVar(self, "master");
        if (!isIdValid(master))
        {
            return SCRIPT_OVERRIDE;
        }
        int spawnNum = getIntObjVar(self, "spawn_number");
        int status = getIntObjVar(self, "status");
        int respawnTime = dataTableGetInt(datatable, spawnNum, "respawn_time");
        if (respawnTime == 0)
        {
            respawnTime = 30;
        }
        dictionary info = new dictionary();
        info.put("spawnNumber", spawnNum);
        info.put("status", status);
        info.put("spawnObj", self);
        messageTo(master, "handleNpcDeath", info, respawnTime, false);
        return SCRIPT_CONTINUE;
    }
    public int handleDestroyRequest(obj_id self, dictionary params) throws InterruptedException
    {
        destroyObject(self);
        return SCRIPT_CONTINUE;
    }
}
