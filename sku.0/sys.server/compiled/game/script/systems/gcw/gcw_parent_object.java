package script.systems.gcw;

import script.dictionary;
import script.library.gcw;
import script.obj_id;

public class gcw_parent_object extends script.base_script
{
    public gcw_parent_object()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "GCW Parent Object! DO NOT DELETE");
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "GCW Parent Object! DO NOT DELETE");
        return SCRIPT_CONTINUE;
    }
    public int updateGCWData(obj_id self, dictionary params) throws InterruptedException
    {
        // Retained compatibility only: the always-present planet object owns
        // PRE-CU base control, never this legacy hard-coded object.
        return SCRIPT_CONTINUE;
    }
    public int updateGCWScore(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || !params.containsKey("intScoreChange") || !params.containsKey("strFaction"))
        {
            return SCRIPT_CONTINUE;
        }
        gcw.changeGCWScore(getLocation(self), params.getInt("intScoreChange"), params.getString("strFaction"));
        return SCRIPT_CONTINUE;
    }
    public int synchronizeGCWScore(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || !params.containsKey("imperialScore") || !params.containsKey("rebelScore"))
        {
            return SCRIPT_CONTINUE;
        }
        gcw.synchronizePlanetaryBaseControlScore(getLocation(self), params.getInt("imperialScore"), params.getInt("rebelScore"));
        return SCRIPT_CONTINUE;
    }
}
