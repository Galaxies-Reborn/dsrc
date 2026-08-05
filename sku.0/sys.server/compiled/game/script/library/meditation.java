package script.library;

import script.dictionary;
import script.obj_id;
import script.prose_package;
import script.string_id;

public class meditation extends script.base_script
{
    public meditation()
    {
    }
    public static final float INITIAL_DELAY = 3.5f;
    public static final float TIME_TICK = 5.0f;
    public static final float POWERBOOST_RAMP = 60.0f;
    public static final int POWERBOOST_BASE_DURATION = 300;
    public static final int POWERBOOST_RAMP_TICKS = 20;
    public static final String MOD_MEDITATE = "meditate";
    public static final String VAR_MEDITATION_BASE = "meditation";
    public static final String VAR_POWERBOOST_ACTIVE = "meditation.powerBoost";
    public static final String VAR_POWERBOOST_BONUS =
        "meditation.powerBoostBonus";
    public static final String VAR_POWERBOOST_TICK =
        "meditation.powerBoostTick";
    public static final String VAR_POWERBOOST_DURATION =
        "meditation.powerBoostDuration";
    public static final String VAR_POWERBOOST_COUNTER =
        "meditation.powerBoostCounter";
    public static final String VAR_POWERBOOST_HEALTH_ACTION_APPLIED =
        "meditation.powerBoostHealthActionApplied";
    public static final String VAR_POWERBOOST_MIND_APPLIED =
        "meditation.powerBoostMindApplied";
    public static final String VAR_FORCE_OF_WILL_ACTIVE = "meditation.forceOfWill";
    public static final String MOD_POWERBOOST_DRAIN =
        "meditation.powerboost.drain";
    public static final String MOD_POWERBOOST_RESTORE =
        "meditation.powerboost.restore";
    public static final String MOD_POWERBOOST_MIND =
        "meditation.powerboost.mind";
    public static final String MOD_POWERBOOST_HEALTH =
        "meditation.powerboost.health";
    public static final String MOD_POWERBOOST_ACTION =
        "meditation.powerboost.action";
    public static final String MOD_FORCE_OF_WILL_PREFIX =
        "meditation.forceofwill.";
    public static final String SCRIPT_MEDITATE = "player.skill.meditate";
    public static final String HANDLER_MEDITATION_TICK = "handleMeditationTick";
    public static final String HANDLER_POWERBOOST_TICK = "handlePowerBoostTick";
    public static final String HANDLER_POWERBOOST_MIND_RISE =
        "handlePowerBoostMindRise";
    public static final String HANDLER_POWERBOOST_WANE = "handlePowerBoostWane";
    public static final String HANDLER_POWERBOOST_END = "handlePowerBoostEnd";
    public static final String STF_TERASKASI = "teraskasi";
    public static final string_id SID_MED_BEGIN = new string_id(STF_TERASKASI, "med_begin");
    public static final string_id SID_MED_END = new string_id(STF_TERASKASI, "med_end");
    public static final string_id SID_MED_FAIL = new string_id(STF_TERASKASI, "med_fail");
    public static final string_id SID_POWERBOOST_BEGIN = new string_id(STF_TERASKASI, "powerboost_begin");
    public static final string_id SID_POWERBOOST_WANE = new string_id(STF_TERASKASI, "powerboost_wane");
    public static final string_id SID_POWERBOOST_END = new string_id(STF_TERASKASI, "powerboost_end");
    public static final string_id SID_POWERBOOST_FAIL = new string_id(STF_TERASKASI, "powerboost_fail");
    public static final string_id SID_POWERBOOST_ACTIVE = new string_id(STF_TERASKASI, "powerboost_active");
    public static final string_id SID_POWERBOOST_MIND = new string_id(STF_TERASKASI, "powerboost_mind");
    public static final string_id SID_FORCEOFWILL = new string_id(STF_TERASKASI, "forceofwill");
    public static final string_id SID_FORCEOFWILL_UNSUCCESSFUL = new string_id(STF_TERASKASI, "forceofwill_unsuccessful");
    public static final string_id SID_FORCEOFWILL_FAIL = new string_id(STF_TERASKASI, "forceofwill_fail");
    public static final string_id SID_FORCEOFWILL_LOST = new string_id(STF_TERASKASI, "forceofwill_lost");
    public static final string_id SID_FORCEOFWILL_UNAVAILABLE = new string_id(STF_TERASKASI, "forceofwill_unavailable");
    public static final string_id SID_MUST_BE_MEDITATING = new string_id(STF_TERASKASI, "must_be_meditating");
    public static final string_id SID_STATE_PREVENTS_POWERBOOST = new string_id(STF_TERASKASI, "state_prevent_powerboost");
    public static final string_id SID_MIND_POOL_TOO_LOW = new string_id(STF_TERASKASI, "mind_pool_too_low");
    public static final string_id PROSE_CUREWOUND = new string_id(STF_TERASKASI, "prose_curewound");
    public static final String[] ATTRIBUTE_NAMES =
    {
        "health", "strength", "constitution",
        "action", "quickness", "stamina",
        "mind", "focus", "willpower"
    };
    public static int getMeditationSkillMod(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return -1;
        }
        int meditate = getEnhancedSkillStatisticModifierUncapped(player, MOD_MEDITATE);
        return meditate;
    }
    public static boolean startMeditation(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        buff.retirePostNgeMeditationBuffs(player);
        setState(player, STATE_MEDITATE, true);
        chat.setTempAnimationMood(player, "meditating");
        messageTo(player, HANDLER_MEDITATION_TICK, trial.getSessionDict(player, meditation.HANDLER_MEDITATION_TICK), INITIAL_DELAY, false);
        sendSystemMessage(player, SID_MED_BEGIN);
        return true;
    }
    public static void endMeditation(obj_id player, boolean verbose) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return;
        }
        setState(player, STATE_MEDITATE, false);
        chat.resetTempAnimationMood(player);
        utils.removeScriptVar(player, VAR_MEDITATION_BASE);
        trial.bumpSession(player, meditation.HANDLER_MEDITATION_TICK);
        if (verbose)
        {
            sendSystemMessage(player, SID_MED_END);
        }
    }
    public static void endMeditation(obj_id player) throws InterruptedException
    {
        endMeditation(player, true);
    }
    public static boolean isMeditating(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        if (getState(player, STATE_MEDITATE) == 1)
        {
            return true;
        }
        return false;
    }
    public static float trance(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return -1.0f;
        }
        int modval = getMeditationSkillMod(player);
        if ((modval < 1) || !isMeditating(player))
        {
            return -1.0f;
        }
        if (dot.isBleeding(player) || dot.isPoisoned(player) ||
            dot.isDiseased(player))
        {
            if (dot.isBleeding(player) && modval >= 15)
            {
                slowDOT(player, modval, dot.DOT_BLEEDING);
            }
            else if (dot.isPoisoned(player) && modval >= 30)
            {
                slowDOT(player, modval, dot.DOT_POISON);
            }
            else if (dot.isDiseased(player) && modval >= 45)
            {
                slowDOT(player, modval, dot.DOT_DISEASE);
            }
            return TIME_TICK;
        }
        if (modval >= 75)
        {
            return cureWounds(player, modval);
        }
        return TIME_TICK;
    }
    public static float slowDOT(obj_id player, int modval, String dotType) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return 0.0f;
        }
        int reduction = 15 + (modval / 3);
        dot.reduceDotTypeStrength(player, dotType, reduction);
        return TIME_TICK;
    }
    public static float cureWounds(obj_id player, int modval) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return 0.0f;
        }
        int[] woundedPools = new int[ATTRIBUTE_NAMES.length];
        int woundedCount = 0;
        for (int attribute = 0; attribute < ATTRIBUTE_NAMES.length;
            ++attribute)
        {
            if (getAttribWound(player, attribute) > 0)
            {
                woundedPools[woundedCount++] = attribute;
            }
        }
        if (woundedCount == 0)
        {
            return 0.0f;
        }
        int heal = 20 + rand(0, 10);
        if (modval >= 100)
        {
            heal = 30 + rand(0, 20);
        }
        int attribute = woundedPools[rand(0, woundedCount - 1)];
        heal = Math.min(heal, getAttribWound(player, attribute));
        healWound(player, attribute, heal);
        prose_package message = prose.getPackage(PROSE_CUREWOUND);
        message = prose.setTO(message, ATTRIBUTE_NAMES[attribute]);
        message = prose.setDI(message, heal);
        sendSystemMessageProse(player, message);
        return TIME_TICK;
    }
    public static boolean forceOfWill(obj_id player, int delta) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        if (delta < 0)
        {
            delta = 0;
        }
        int modval = getMeditationSkillMod(player);
        if ((modval < 1) || getPosture(player) != POSTURE_INCAPACITATED)
        {
            return false;
        }
        clearForceOfWillModifiers(player);
        if (delta < 10)
        {
            for (int attribute = HEALTH; attribute <= WILLPOWER; ++attribute)
            {
                addWound(player, attribute, 100);
            }
            addShockWound(player, 100);
            sendSystemMessage(player,
                new string_id(STF_TERASKASI, "forceofwill_crit_fail"));
        }
        else if (delta < 40)
        {
            for (int attribute = HEALTH; attribute <= WILLPOWER; ++attribute)
            {
                addAttribModifier(player,
                    MOD_FORCE_OF_WILL_PREFIX + attribute, attribute, -200,
                    300.0f, 0.0f, 0.0f, false, false, true);
            }
            addShockWound(player, 100);
            sendSystemMessage(player,
                new string_id(STF_TERASKASI, "forceofwill_marginal"));
        }
        else if (delta < 70)
        {
            for (int attribute = HEALTH; attribute <= WILLPOWER; ++attribute)
            {
                addAttribModifier(player,
                    MOD_FORCE_OF_WILL_PREFIX + attribute, attribute, -100,
                    120.0f, 0.0f, 0.0f, false, false, true);
            }
            sendSystemMessage(player,
                new string_id(STF_TERASKASI, "forceofwill_normal"));
        }
        else 
        {
            sendSystemMessage(player, new string_id(STF_TERASKASI, "forceofwill_exceptional"));
        }
        innate.equalizeEffect(player);
        utils.removeScriptVar(player, "incap.timeStamp");
        setCount(player, 0);
        setState(player, STATE_FEIGN_DEATH, false);
        setPostureClientImmediate(player, POSTURE_UPRIGHT);
        return true;
    }
    public static String getForceOfWillTier(int delta)
    {
        if (delta < 10)
        {
            return "critical";
        }
        if (delta < 40)
        {
            return "marginal";
        }
        if (delta < 70)
        {
            return "normal";
        }
        return "exceptional";
    }
    public static void clearForceOfWillModifiers(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return;
        }
        for (int attribute = HEALTH; attribute <= WILLPOWER; ++attribute)
        {
            String name = MOD_FORCE_OF_WILL_PREFIX + attribute;
            if (hasAttribModifier(player, name))
            {
                removeAttribOrSkillModModifier(player, name);
            }
        }
    }
    public static boolean forceOfWill(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        return forceOfWill(player, rand(1, getMeditationSkillMod(player)));
    }
    public static boolean powerBoost(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        int modval = getMeditationSkillMod(player);
        if ((modval < 1))
        {
            return false;
        }
        if (!isMeditating(player))
        {
            sendSystemMessage(player, SID_MUST_BE_MEDITATING);
            return false;
        }
        if (hasObjVar(player, VAR_POWERBOOST_ACTIVE) ||
            hasPowerBoostModifiers(player))
        {
            sendSystemMessage(player, SID_POWERBOOST_ACTIVE);
            return false;
        }
        int baseMind = utils.getUnbuffedWoundedMaxAttrib(player, MIND);
        if (baseMind < POWERBOOST_RAMP_TICKS)
        {
            sendSystemMessage(player, SID_STATE_PREVENTS_POWERBOOST);
            return false;
        }
        int boost = baseMind / 2;
        if (getAttrib(player, MIND) <= boost)
        {
            sendSystemMessage(player, SID_MIND_POOL_TOO_LOW);
            return false;
        }
        int tick = boost / POWERBOOST_RAMP_TICKS;
        int adjustedBoost = tick * POWERBOOST_RAMP_TICKS;
        int duration = POWERBOOST_BASE_DURATION +
            (modval / 100) * POWERBOOST_BASE_DURATION;
        if (tick < 1 || adjustedBoost < POWERBOOST_RAMP_TICKS)
        {
            sendSystemMessage(player, SID_STATE_PREVENTS_POWERBOOST);
            return false;
        }
        float plateau = Math.max(0.0f,
            duration - (POWERBOOST_RAMP * 2.0f));
        boolean applied =
            addAttribModifier(player, MOD_POWERBOOST_DRAIN, MIND,
                -adjustedBoost, POWERBOOST_RAMP, 0.0f, 0.0f,
                true, false, true) &&
            addAttribModifier(player, MOD_POWERBOOST_RESTORE, MIND,
                adjustedBoost, 0.0f, POWERBOOST_RAMP,
                0.0f, false, false, true) &&
            addAttribModifier(player, MOD_POWERBOOST_HEALTH, HEALTH,
                adjustedBoost, plateau, POWERBOOST_RAMP,
                POWERBOOST_RAMP, false, false, false) &&
            addAttribModifier(player, MOD_POWERBOOST_ACTION, ACTION,
                adjustedBoost, plateau, POWERBOOST_RAMP,
                POWERBOOST_RAMP, false, false, false);
        if (!applied)
        {
            return false;
        }
        int now = getGameTime();
        int expiration = now + duration;
        setObjVar(player, VAR_POWERBOOST_ACTIVE, expiration);
        setObjVar(player, VAR_POWERBOOST_BONUS, adjustedBoost);
        setObjVar(player, VAR_POWERBOOST_TICK, tick);
        setObjVar(player, VAR_POWERBOOST_DURATION, duration);
        dictionary d = new dictionary();
        d.put("expiration", expiration);
        messageTo(player, HANDLER_POWERBOOST_MIND_RISE, d,
            POWERBOOST_RAMP, false);
        messageTo(player, HANDLER_POWERBOOST_WANE, d,
            duration - POWERBOOST_RAMP, false);
        messageTo(player, HANDLER_POWERBOOST_END, d, duration, true);
        messageTo(player, "handlePowerBoostLog", null, POWERBOOST_RAMP * 2, false);
        sendSystemMessage(player, SID_POWERBOOST_BEGIN);
        return true;
    }
    public static boolean hasPowerBoostModifiers(obj_id player)
        throws InterruptedException
    {
        return isIdValid(player) &&
            (hasAttribModifier(player, MOD_POWERBOOST_DRAIN) ||
            hasAttribModifier(player, MOD_POWERBOOST_RESTORE) ||
            hasAttribModifier(player, MOD_POWERBOOST_MIND) ||
            hasAttribModifier(player, MOD_POWERBOOST_HEALTH) ||
            hasAttribModifier(player, MOD_POWERBOOST_ACTION));
    }
    public static void beginPowerBoostMindRise(obj_id player,
        dictionary params) throws InterruptedException
    {
        if (!isIdValid(player) || params == null ||
            !hasObjVar(player, VAR_POWERBOOST_ACTIVE))
        {
            return;
        }
        int expiration = getIntObjVar(player, VAR_POWERBOOST_ACTIVE);
        if (params.getInt("expiration") != expiration)
        {
            return;
        }
        int tick = getIntObjVar(player, VAR_POWERBOOST_TICK);
        int duration = getIntObjVar(player, VAR_POWERBOOST_DURATION);
        int adjustedBoost = tick * POWERBOOST_RAMP_TICKS;
        addAttribModifier(player, MOD_POWERBOOST_MIND, MIND,
            adjustedBoost, Math.max(0.0f, duration - 180.0f),
            POWERBOOST_RAMP, POWERBOOST_RAMP, false, false, true);
    }
    public static void endPowerBoost(obj_id player, boolean verbose)
        throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return;
        }
        if (hasObjVar(player, VAR_POWERBOOST_ACTIVE) &&
            getGameTime() < getIntObjVar(player, VAR_POWERBOOST_ACTIVE))
        {
            return;
        }
        String[] modifiers =
        {
            MOD_POWERBOOST_DRAIN,
            MOD_POWERBOOST_RESTORE,
            MOD_POWERBOOST_MIND,
            MOD_POWERBOOST_HEALTH,
            MOD_POWERBOOST_ACTION
        };
        for (String modifier : modifiers)
        {
            if (hasAttribModifier(player, modifier))
            {
                removeAttribOrSkillModModifier(player, modifier);
            }
        }
        removeObjVar(player, VAR_POWERBOOST_ACTIVE);
        removeObjVar(player, VAR_POWERBOOST_BONUS);
        removeObjVar(player, VAR_POWERBOOST_TICK);
        removeObjVar(player, VAR_POWERBOOST_DURATION);
        removeObjVar(player, VAR_POWERBOOST_COUNTER);
        removeObjVar(player, VAR_POWERBOOST_HEALTH_ACTION_APPLIED);
        removeObjVar(player, VAR_POWERBOOST_MIND_APPLIED);
        if (verbose)
        {
            sendSystemMessage(player, SID_POWERBOOST_END);
        }
    }
}
