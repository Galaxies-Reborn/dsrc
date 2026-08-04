package script.event.lifeday;

import script.library.create;
import script.dictionary;
import script.location;
import script.obj_id;

public class city_spawner extends script.base_script
{
    private static final String CHILD_VAR = "event.lifeday.male1";
    private static final String SPAWNED_VAR = "event.lifeday.spawned";
    private static final String OWNER_VAR = "precuLifeDay.anchor";
    private static final String TEMPLATE = "object/tangible/spawning/static_npc/wookiee_lifeday_male1.iff";
    public city_spawner()
    {
    }
    private static final int[][] startLocations =
    {
        {131,52,-5384},
        {-5545,23,-6177},
        {-5557,-150,0}
    };
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
    public int reconcilePrecuLifeDayAnchors(obj_id self, dictionary params) throws InterruptedException
    {
        reconcile(self);
        return SCRIPT_CONTINUE;
    }
    public int retirePrecuLifeDayAnchors(obj_id self, dictionary params) throws InterruptedException
    {
        cleanup(self);
        detachScript(self, "event.lifeday.city_spawner");
        return SCRIPT_CONTINUE;
    }
    public int OnDetach(obj_id self) throws InterruptedException
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
                messageTo(existing, "reconcilePrecuLifeDayCelebrity", null, 0.0f, false);
                return;
            }
            removeObjVar(self, CHILD_VAR);
        }
        String myPlanet = getNameForPlanetObject(self);
        if (myPlanet == null || !myPlanet.equals(getCurrentSceneName()))
        {
            return;
        }
        int locStart = getLocationIndex(myPlanet);
        if (locStart < 0)
        {
            return;
        }
        location spawnLocation = new location(startLocations[locStart][0], startLocations[locStart][1], startLocations[locStart][2], myPlanet);
        obj_id anchor = create.object(TEMPLATE, spawnLocation);
        if (isIdValid(anchor))
        {
            setObjVar(anchor, "objParent", self);
            setObjVar(anchor, OWNER_VAR, 1);
            setObjVar(self, CHILD_VAR, anchor);
            setObjVar(self, SPAWNED_VAR, 1);
        }
    }
    private int getLocationIndex(String planetName) throws InterruptedException
    {
        if (planetName.equals("tatooine")) return 0;
        if (planetName.equals("corellia")) return 1;
        if (planetName.equals("naboo")) return 2;
        return -1;
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
        obj_id[] owned = getAllObjectsWithObjVar(getLocation(self), 32000.0f, OWNER_VAR);
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
        removeObjVar(self, SPAWNED_VAR);
    }
}
