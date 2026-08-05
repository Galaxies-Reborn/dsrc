package script.systems.gcw;

import script.dictionary;
import script.obj_id;

public class gcw_parent_object extends script.base_script
{
    public gcw_parent_object()
    {
    }
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, "GCW Parent Object! DO NOT DELETE");
        ensurePrecuControlScoreState(self);
        return SCRIPT_CONTINUE;
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, "GCW Parent Object! DO NOT DELETE");
        ensurePrecuControlScoreState(self);
        return SCRIPT_CONTINUE;
    }
    public int updateGCWData(obj_id self, dictionary params) throws InterruptedException
    {
        // Compatibility handler for messages queued by the retired NGE regional
        // percentile updater.  PRE-CU planet control is owned by player bases.
        ensurePrecuControlScoreState(self);
        return SCRIPT_CONTINUE;
    }
    public int updateGCWScore(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || !params.containsKey("intScoreChange") || !params.containsKey("strFaction"))
        {
            return SCRIPT_CONTINUE;
        }
        String faction = params.getString("strFaction");
        if (!"Imperial".equals(faction) && !"Rebel".equals(faction))
        {
            return SCRIPT_CONTINUE;
        }
        String scoreObjVar = faction + ".controlScore";
        int currentScore = hasObjVar(self, scoreObjVar) ? getIntObjVar(self, scoreObjVar) : 0;
        long adjustedScore = (long)Math.max(0, currentScore) + params.getInt("intScoreChange");
        int newScore = adjustedScore > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)Math.max(0L, adjustedScore);
        setObjVar(self, scoreObjVar, newScore);
        return SCRIPT_CONTINUE;
    }
    public int synchronizeGCWScore(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || !params.containsKey("imperialScore") || !params.containsKey("rebelScore"))
        {
            return SCRIPT_CONTINUE;
        }
        setObjVar(self, "Imperial.controlScore", Math.max(0, params.getInt("imperialScore")));
        setObjVar(self, "Rebel.controlScore", Math.max(0, params.getInt("rebelScore")));
        return SCRIPT_CONTINUE;
    }
    private void ensurePrecuControlScoreState(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, "Imperial.controlScore"))
        {
            setObjVar(self, "Imperial.controlScore", 0);
        }
        if (!hasObjVar(self, "Rebel.controlScore"))
        {
            setObjVar(self, "Rebel.controlScore", 0);
        }
    }
}
