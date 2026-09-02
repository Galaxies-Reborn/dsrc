package script.systems.reborn.force_progression;

import script.*;
import script.library.create;
import script.library.force_progression;

public class world_spawner extends script.base_script
{
    public static final String SCRIPT_NAME = "systems.reborn.force_progression.world_spawner";
    public static final String NPC_SCRIPT = "npc.reborn.force_mentor";
    public static final String VAR_CHILDREN = "reborn.forceProgression.questNpcChildren";
    public static final float RECONCILE_SECONDS = 1800.0f;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        reconcile(self);
        messageTo(self, "reconcileForceProgressionNpcNetwork", null, RECONCILE_SECONDS, false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        reconcile(self);
        messageTo(self, "reconcileForceProgressionNpcNetwork", null, RECONCILE_SECONDS, false);
        return SCRIPT_CONTINUE;
    }

    public int reconcileForceProgressionNpcNetwork(obj_id self, dictionary params) throws InterruptedException
    {
        reconcile(self);
        if (force_progression.isReplacementEnabled())
        {
            messageTo(self, "reconcileForceProgressionNpcNetwork", null, RECONCILE_SECONDS, false);
        }
        return SCRIPT_CONTINUE;
    }

    public int retireForceProgressionNpcNetwork(obj_id self, dictionary params) throws InterruptedException
    {
        cleanup(self);
        detachScript(self, SCRIPT_NAME);
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self) throws InterruptedException
    {
        cleanup(self);
        return SCRIPT_CONTINUE;
    }

    private void reconcile(obj_id self) throws InterruptedException
    {
        if (!force_progression.isReplacementEnabled())
        {
            cleanup(self);
            return;
        }
        String planet = getNameForPlanetObject(self);
        if (planet == null || planet.length() == 0)
        {
            return;
        }
        int rows = dataTableGetNumRows(force_progression.QUEST_NETWORK_TABLE);
        for (int rowIndex = 0; rowIndex < rows; ++rowIndex)
        {
            dictionary row = dataTableGetRow(force_progression.QUEST_NETWORK_TABLE, rowIndex);
            if (row == null || !planet.equals(row.getString("planet")))
            {
                continue;
            }
            String questId = row.getString("id");
            if (force_progression.getQuestRow(questId) == null)
            {
                CustomerServiceLog("reborn_force_progression", "Quest NPC row failed validation for " + questId + " on " + planet + ".");
                continue;
            }
            obj_id npc = getOwnedNpc(self, questId);
            if (!isIdValid(npc))
            {
                location spawnLocation = new location(row.getFloat("x"), 0.0f, row.getFloat("z"), planet);
                spawnLocation.y = getHeightAtLocation(spawnLocation.x, spawnLocation.z);
                npc = create.object(row.getString("npc_type"), spawnLocation);
                if (!isIdValid(npc))
                {
                    CustomerServiceLog("reborn_force_progression", "Failed to create quest NPC " + questId + " on " + planet + ".");
                    continue;
                }
                setYaw(npc, row.getFloat("yaw"));
                setObjVar(npc, force_progression.VAR_NPC_QUEST_ID, questId);
                setObjVar(npc, force_progression.VAR_NPC_OWNER, self);
                setObjVar(self, VAR_CHILDREN + "." + questId, npc);
            }
            if (!hasScript(npc, NPC_SCRIPT))
            {
                attachScript(npc, NPC_SCRIPT);
            }
            else
            {
                messageTo(npc, "reconcileForceMentor", null, 0.0f, false);
            }
        }
    }

    private obj_id getOwnedNpc(obj_id self, String questId) throws InterruptedException
    {
        String childPath = VAR_CHILDREN + "." + questId;
        if (hasObjVar(self, childPath))
        {
            obj_id child = getObjIdObjVar(self, childPath);
            if (isOwnedQuestNpc(self, child, questId))
            {
                return child;
            }
            removeObjVar(self, childPath);
        }
        obj_id[] candidates = getAllObjectsWithObjVar(getLocation(self), 32000.0f, force_progression.VAR_NPC_QUEST_ID);
        if (candidates != null)
        {
            for (obj_id candidate : candidates)
            {
                if (isOwnedQuestNpc(self, candidate, questId))
                {
                    setObjVar(self, childPath, candidate);
                    return candidate;
                }
            }
        }
        return obj_id.NULL_ID;
    }

    private boolean isOwnedQuestNpc(obj_id self, obj_id candidate, String questId) throws InterruptedException
    {
        return isIdValid(candidate) && exists(candidate) &&
            hasObjVar(candidate, force_progression.VAR_NPC_QUEST_ID) && questId.equals(getStringObjVar(candidate, force_progression.VAR_NPC_QUEST_ID)) &&
            hasObjVar(candidate, force_progression.VAR_NPC_OWNER) && getObjIdObjVar(candidate, force_progression.VAR_NPC_OWNER) == self;
    }

    private void cleanup(obj_id self) throws InterruptedException
    {
        obj_id[] candidates = getAllObjectsWithObjVar(getLocation(self), 32000.0f, force_progression.VAR_NPC_QUEST_ID);
        if (candidates != null)
        {
            for (obj_id candidate : candidates)
            {
                if (isIdValid(candidate) && exists(candidate) && hasObjVar(candidate, force_progression.VAR_NPC_OWNER) && getObjIdObjVar(candidate, force_progression.VAR_NPC_OWNER) == self)
                {
                    destroyObject(candidate);
                }
            }
        }
        removeObjVar(self, VAR_CHILDREN);
    }
}
