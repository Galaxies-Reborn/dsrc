package script.npc.celebrity;

import script.library.ai_lib;
import script.library.create;
import script.dictionary;
import script.obj_id;

public class lifeday_spawner extends script.base_script
{
    private static final String CHILD_VAR = "event.lifeday.spawnedNpc";
    private static final String OWNER_VAR = "precuLifeDay.celebrity";
    public lifeday_spawner()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }
    public int reconcilePrecuLifeDayCelebrity(obj_id self, dictionary params) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }
    public int OnDestroy(obj_id self) throws InterruptedException
    {
        cleanup(self);
        return SCRIPT_CONTINUE;
    }
    private void reconcile(obj_id self) throws InterruptedException
    {
        if (!isLifeDayEnabled())
        {
            cleanup(self);
            return;
        }
        if (hasObjVar(self, CHILD_VAR))
        {
            obj_id existing = getObjIdObjVar(self, CHILD_VAR);
            if (isIdValid(existing) && exists(existing))
            {
                return;
            }
            removeObjVar(self, CHILD_VAR);
        }
        String spawn = getStringObjVar(self, "spawns");
        obj_id celeb = create.object(spawn, getLocation(self));
        if (!isIdValid(celeb))
        {
            return;
        }
        setObjVar(celeb, "objParent", self);
        setObjVar(celeb, OWNER_VAR, 1);
        setObjVar(self, CHILD_VAR, celeb);
        setInvulnerable(celeb, true);
        ai_lib.setDefaultCalmBehavior(celeb, ai_lib.BEHAVIOR_SENTINEL);
        if (hasObjVar(self, "quest_script"))
        {
            String script = getStringObjVar(self, "quest_script");
            attachScript(celeb, script);
        }
        if (hasObjVar(self, "quest_table"))
        {
            String table = getStringObjVar(self, "quest_table");
            setObjVar(celeb, "quest_table", table);
        }
        if (hasObjVar(self, "npc_name"))
        {
            String name = getStringObjVar(self, "npc_name");
            if (name != null && !name.equals(""))
            {
                setName(celeb, name);
            }
        }
    }
    private boolean isLifeDayEnabled() throws InterruptedException
    {
        String gameSetting = getConfigSetting("GameServer", "lifeday");
        String eventSetting = getConfigSetting("EventTeam", "lifeday");
        return (gameSetting != null && (gameSetting.equals("true") || gameSetting.equals("1"))) ||
            (eventSetting != null && (eventSetting.equals("true") || eventSetting.equals("1")));
    }
    private void cleanup(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, CHILD_VAR))
        {
            obj_id child = getObjIdObjVar(self, CHILD_VAR);
            if (isIdValid(child) && exists(child))
            {
                destroyObject(child);
            }
            removeObjVar(self, CHILD_VAR);
        }
        obj_id[] owned = getAllObjectsWithObjVar(getLocation(self), 128.0f, OWNER_VAR);
        if (owned != null)
        {
            for (obj_id child : owned)
            {
                if (isIdValid(child) && exists(child) && hasObjVar(child, "objParent") &&
                    getObjIdObjVar(child, "objParent") == self)
                {
                    destroyObject(child);
                }
            }
        }
    }
}
