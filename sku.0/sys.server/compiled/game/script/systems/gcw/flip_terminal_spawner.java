package script.systems.gcw;

import script.dictionary;
import script.library.utils;
import script.obj_id;

public class flip_terminal_spawner extends script.base_script
{
    public static final String SCRIPT_NAME = "systems.gcw.flip_terminal_spawner";

    public flip_terminal_spawner()
    {
    }
    public static boolean isPostNgeRegionalMissionTerminalRetired()
    {
        return true;
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        retireSpawner(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retireSpawner(self);
        return SCRIPT_CONTINUE;
    }
    public void checkDestroy(obj_id self) throws InterruptedException
    {
        if (utils.hasScriptVar(self, "terminal"))
        {
            destroyRetiredTerminal(utils.getObjIdScriptVar(self, "terminal"));
            utils.removeScriptVar(self, "terminal");
        }
        if (hasObjVar(self, "terminal"))
        {
            destroyRetiredTerminal(getObjIdObjVar(self, "terminal"));
            removeObjVar(self, "terminal");
        }
    }
    public void destroyRetiredTerminal(obj_id terminal) throws InterruptedException
    {
        if (isIdValid(terminal) && exists(terminal))
        {
            destroyObject(terminal);
        }
    }
    public void retireSpawner(obj_id self) throws InterruptedException
    {
        checkDestroy(self);
        if (utils.hasScriptVar(self, "lastCheckTime"))
        {
            utils.removeScriptVar(self, "lastCheckTime");
        }
        detachScript(self, SCRIPT_NAME);
    }
    public int OnDestroy(obj_id self) throws InterruptedException
    {
        checkDestroy(self);
        return SCRIPT_CONTINUE;
    }
    public int OnUnloadedFromMemory(obj_id self) throws InterruptedException
    {
        checkDestroy(self);
        return SCRIPT_CONTINUE;
    }
    public int checkTerminal(obj_id self, dictionary params) throws InterruptedException
    {
        retireSpawner(self);
        return SCRIPT_CONTINUE;
    }
    public void spawnTerminal(obj_id self, String faction) throws InterruptedException
    {
        retireSpawner(self);
    }
}
