package script.event.lifeday;

import script.library.create;
import script.dictionary;
import script.location;
import script.obj_id;

public class lifeday_spawner extends script.base_script
{
    private static final String SPAWNED_VAR = "event.lifeday.spawned";
    private static final String OWNER_VAR = "precuLifeDay.anchor";
    private static final String[] CHILD_VARS =
    {
        "event.lifeday.elder",
        "event.lifeday.female1",
        "event.lifeday.female2",
        "event.lifeday.male2"
    };
    private static final String[] TEMPLATES =
    {
        "object/tangible/spawning/static_npc/wookiee_lifeday_elder.iff",
        "object/tangible/spawning/static_npc/wookiee_lifeday_female1.iff",
        "object/tangible/spawning/static_npc/wookiee_lifeday_female2.iff",
        "object/tangible/spawning/static_npc/wookiee_lifeday_male2.iff"
    };
    public lifeday_spawner()
    {
    }
    private static final int[][] LOCS =
    {
            {-2580,77,-5519},
            {-2576,77,-5519},
            {-2576,77,-5508},
            {-2580,77,-5508},
            {-1088,6,-998},
            {-1088,6,-994},
            {-1100,6,-994},
            {-1100,6,-998},
            {-12,163,-3918},
            {-12,163,-3922},
            {-3,163,-3922},
            {-3,163,-3918}
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
        detachScript(self, "event.lifeday.lifeday_spawner");
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
        boolean allPresent = true;
        for (int i = 0; i < CHILD_VARS.length; i++)
        {
            if (hasObjVar(self, CHILD_VARS[i]))
            {
                obj_id existing = getObjIdObjVar(self, CHILD_VARS[i]);
                if (isIdValid(existing) && exists(existing))
                {
                    messageTo(existing, "reconcilePrecuLifeDayCelebrity", null, 0.0f, false);
                    continue;
                }
                removeObjVar(self, CHILD_VARS[i]);
            }
            allPresent = false;
            location spawnLocation = new location(LOCS[locStart + i][0], LOCS[locStart + i][1], LOCS[locStart + i][2], myPlanet);
            obj_id anchor = create.object(TEMPLATES[i], spawnLocation);
            if (isIdValid(anchor))
            {
                setObjVar(anchor, "objParent", self);
                setObjVar(anchor, OWNER_VAR, 1);
                setObjVar(self, CHILD_VARS[i], anchor);
            }
        }
        if (allPresent || allChildrenValid(self))
        {
            setObjVar(self, SPAWNED_VAR, 1);
        }
    }
    private boolean allChildrenValid(obj_id self) throws InterruptedException
    {
        for (String childVar : CHILD_VARS)
        {
            if (!hasObjVar(self, childVar))
            {
                return false;
            }
            obj_id child = getObjIdObjVar(self, childVar);
            if (!isIdValid(child) || !exists(child))
            {
                return false;
            }
        }
        return true;
    }
    private int getLocationIndex(String planetName) throws InterruptedException
    {
        if (planetName.equals("dathomir")) return 0;
        if (planetName.equals("endor")) return 4;
        if (planetName.equals("yavin4")) return 8;
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
        for (String childVar : CHILD_VARS)
        {
            if (hasObjVar(self, childVar))
            {
                obj_id child = getObjIdObjVar(self, childVar);
                if (isIdValid(child) && exists(child))
                {
                    destroyObject(child);
                }
                removeObjVar(self, childVar);
            }
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
