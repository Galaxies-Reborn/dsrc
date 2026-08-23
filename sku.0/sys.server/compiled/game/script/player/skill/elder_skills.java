package script.player.skill;

import script.*;
import script.library.elder_skill;
import script.library.utils;

/** Player-owned restart/relog-safe lifecycle endpoint for temporary Elder boxes. */
public class elder_skills extends script.base_script
{
    private static final String PULSE_TOKEN =
        "precu.elderSkills.mentoring.pulseToken";
    private static final String PARAM_PULSE_TOKEN = "elderMentoringPulseToken";

    public elder_skills()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        elder_skill.reconcile(self);
        restartMentoringPulse(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        elder_skill.reconcile(self);
        restartMentoringPulse(self);
        return SCRIPT_CONTINUE;
    }

    public int OnLogin(obj_id self) throws InterruptedException
    {
        elder_skill.reconcile(self);
        restartMentoringPulse(self);
        return SCRIPT_CONTINUE;
    }

    public int handleElderMentoringPulse(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !params.containsKey(PARAM_PULSE_TOKEN) ||
            !utils.hasScriptVar(self, PULSE_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }
        int token = params.getInt(PARAM_PULSE_TOKEN);
        if (token != utils.getIntScriptVar(self, PULSE_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }
        elder_skill.reconcile(self);
        elder_skill.awardGroupMentorPresenceExperience(self);
        scheduleMentoringPulse(
            self,
            token,
            elder_skill.GROUP_MENTOR_AWARD_COOLDOWN_SECONDS);
        return SCRIPT_CONTINUE;
    }

    public int handleElderSkillExpiry(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !params.containsKey(elder_skill.PARAM_SKILL_NAME) ||
            !params.containsKey(elder_skill.PARAM_EXPIRES_AT))
        {
            return SCRIPT_CONTINUE;
        }
        elder_skill.handleExpiry(
            self,
            params.getString(elder_skill.PARAM_SKILL_NAME),
            params.getInt(elder_skill.PARAM_EXPIRES_AT));
        return SCRIPT_CONTINUE;
    }

    private void restartMentoringPulse(obj_id player)
        throws InterruptedException
    {
        int token = utils.hasScriptVar(player, PULSE_TOKEN) ?
            utils.getIntScriptVar(player, PULSE_TOKEN) + 1 : 1;
        if (token <= 0)
        {
            token = 1;
        }
        utils.setScriptVar(player, PULSE_TOKEN, token);
        scheduleMentoringPulse(player, token, 1.0f);
    }

    private void scheduleMentoringPulse(
        obj_id player,
        int token,
        float delay) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put(PARAM_PULSE_TOKEN, token);
        messageTo(
            player,
            "handleElderMentoringPulse",
            params,
            Math.max(1.0f, delay),
            false);
    }
}
