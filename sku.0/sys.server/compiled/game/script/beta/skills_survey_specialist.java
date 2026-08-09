package script.beta;

import script.library.skill;
import script.obj_id;

public class skills_survey_specialist extends script.base_script
{
    public skills_survey_specialist()
    {
    }
    public static final String SKILL_NAME = "crafting_artisan_master";
    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!isGod(self) || getGodLevel(self) < 10 || !isPlayer(self))
        {
            detachScript(self, "beta.skills_survey_specialist");
            return SCRIPT_CONTINUE;
        }
        if (!skill.grantPrecuSkillWithPrerequisites(self, SKILL_NAME))
        {
            debugSpeakMsg(self, "Unable to grant the PRE-CU Artisan tree within the 250-point skill cap.");
        }
        return SCRIPT_CONTINUE;
    }
    public int OnDetach(obj_id self) throws InterruptedException
    {
        debugSpeakMsg(self, "PRE-CU trained Artisan skill boxes remain learned when the survey test script is detached.");
        return SCRIPT_CONTINUE;
    }
}
