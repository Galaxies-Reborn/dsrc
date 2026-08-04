package script.event.ewok_festival;

import script.dictionary;
import script.library.ai_lib;
import script.library.create;
import script.library.utils;
import script.location;
import script.obj_id;

public class loveday_disillusion_blaire_spawner extends script.base_script
{
    public loveday_disillusion_blaire_spawner()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        retireDisillusionSpawner(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        retireDisillusionSpawner(self);
        return SCRIPT_CONTINUE;
    }
    public int spawnDisillusionedCupid(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasScript(self, "event.ewok_festival.loveday_disillusion_blaire_spawner"))
        {
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(self, "spawnedDisillusionedCupid"))
        {
            location spawnerLoc = getLocation(self);
            float spawnerYaw = getYaw(self);
            boolean spawnBlaire = false;
            String lovedayRunning = getConfigSetting("GameServer", "loveday");
            if (lovedayRunning != null && lovedayRunning.length() > 0)
            {
                if (lovedayRunning.equals("true") || lovedayRunning.equals("1"))
                {
                    spawnBlaire = true;
                }
            }
            if (spawnBlaire)
            {
                obj_id blaire = create.object("loveday_ewok_disillusion_blaire", spawnerLoc);
                if (isIdValid(blaire))
                {
                    ai_lib.setDefaultCalmBehavior(blaire, ai_lib.BEHAVIOR_SENTINEL);
                    setYaw(blaire, spawnerYaw);
                    setName(self, "Disillusion Spawner (Benjamin)");
                }
            }
            else 
            {
                obj_id crossbow = createObject("object/tangible/quest/content/holiday_loveday_disillusion_crossbow.iff", spawnerLoc);
                if (isIdValid(crossbow))
                {
                    setYaw(crossbow, spawnerYaw);
                    setName(self, "Disillusion Spawner: (Crossbow)");
                }
            }
            utils.setScriptVar(self, "spawnedDisillusionedCupid", true);
        }
        return SCRIPT_CONTINUE;
    }
    public void areaDebugMessaging(obj_id self, String message) throws InterruptedException
    {
        obj_id[] players = getAllPlayers(getLocation(getTopMostContainer(self)), 35.0f);
        if (players != null && players.length > 0)
        {
            for (obj_id player : players) {
                sendSystemMessage(player, message, "");
            }
        }
    }
    public int OnHearSpeech(obj_id self, obj_id speaker, String text) throws InterruptedException
    {
        if (!isGod(speaker) || !hasObjVar(speaker, "cupidTestingAuthorized"))
        {
            return SCRIPT_CONTINUE;
        }
        if (text.equals("cupid_testing_spawner"))
        {
            utils.removeScriptVar(self, "spawnedDisillusionedCupid");
            messageTo(self, "spawnDisillusionedCupid", null, 1, false);
        }
        return SCRIPT_CONTINUE;
    }
    private void retireDisillusionSpawner(obj_id self) throws InterruptedException
    {
        destroyNearbyTemplate(self, "object/mobile/loveday_ewok_mister_disillusion.iff");
        destroyNearbyTemplate(self, "object/tangible/quest/content/holiday_loveday_disillusion_crossbow.iff");
        utils.removeScriptVar(self, "spawnedDisillusionedCupid");
        detachScript(self, "event.ewok_festival.loveday_disillusion_blaire_spawner");
    }
    private void destroyNearbyTemplate(obj_id self, String templateName) throws InterruptedException
    {
        obj_id[] objects = getAllObjectsWithTemplate(getLocation(self), 5.0f, templateName);
        if (objects != null)
        {
            for (obj_id object : objects)
            {
                if (isIdValid(object) && exists(object))
                {
                    destroyObject(object);
                }
            }
        }
    }
}
