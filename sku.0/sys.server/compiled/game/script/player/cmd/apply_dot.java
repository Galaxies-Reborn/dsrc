package script.player.cmd;

import script.obj_id;
import script.string_id;
import script.library.ai_lib;
import script.library.consumable;
import script.library.dot;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.utils;

/** Publish 14.1 shared Apply Poison / Apply Disease command adapter. */
public class apply_dot extends script.base_script
{
    private static final int BASE_MIND_COST = 150;
    private static final int MIN_ROUND_TIME = 5;
    private static final String POISON_COOLDOWN =
        "healing.can_apply_poison";
    private static final String DISEASE_COOLDOWN =
        "healing.can_apply_disease";
    private static final String FIXTURE_ROOT =
        "precu.applyDotCommandFixture";
    private static final string_id SID_MUST_WAIT =
        new string_id("healing_response", "healing_must_wait");
    private static final string_id SID_NO_MEDICINE =
        new string_id("healing_response", "healing_response_60");
    private static final string_id SID_NOT_ENOUGH_MIND =
        new string_id("healing_response", "not_enough_mind");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");

    public int applyPoison(obj_id self, obj_id target, String params,
        float defaultTime) throws InterruptedException
    {
        return applyDot(self, target, params, defaultTime, dot.DOT_POISON,
            healing.HEAL_TYPE_MEDICAL_APPLY_POISON,
            "science_combatmedic_novice", POISON_COOLDOWN);
    }

    public int applyDisease(obj_id self, obj_id target, String params,
        float defaultTime) throws InterruptedException
    {
        return applyDot(self, target, params, defaultTime, dot.DOT_DISEASE,
            healing.HEAL_TYPE_MEDICAL_APPLY_DISEASE,
            "science_combatmedic_healing_range_02", DISEASE_COOLDOWN);
    }

    private int applyDot(obj_id self, obj_id target, String params,
        float defaultTime, String dotType, String healType,
        String requiredSkill, String cooldown) throws InterruptedException
    {
        boolean fixture = isIdValid(self) && hasObjVar(self, FIXTURE_ROOT);
        markEntry(self, fixture, dotType);
        if (!isIdValid(self) || !exists(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self) ||
            !hasSkill(self, requiredSkill))
        {
            recordOutcome(self, fixture, dotType, "medicRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isEligibleTarget(self, target))
        {
            recordOutcome(self, fixture, dotType, "targetRejected");
            return SCRIPT_CONTINUE;
        }
        if (!canSee(self, target))
        {
            healing.sendMedicalSpam(self, SID_NO_LINE_OF_SIGHT,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(self, fixture, dotType, "lineOfSightRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isReady(self, cooldown))
        {
            sendSystemMessage(self, SID_MUST_WAIT);
            recordOutcome(self, fixture, dotType, "cooldownRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id medicine = parseMedicine(self, params);
        if (!isIdValid(medicine))
        {
            medicine = healing.findApplyDotMedicine(self, dotType);
        }
        if (!isValidMedicine(self, medicine, dotType))
        {
            sendSystemMessage(self, SID_NO_MEDICINE);
            recordOutcome(self, fixture, dotType, "noMedicine");
            return SCRIPT_CONTINUE;
        }
        float range = healing.getHealingRange(medicine) +
            getSkillStatMod(self, "healing_range") * 14.0f / 100.0f;
        if (getDistance(self, target) > range)
        {
            healing.sendMedicalSpam(self, consumable.SID_TARGET_OUT_OF_RANGE,
                COMBAT_RESULT_OUT_OF_RANGE);
            recordOutcome(self, fixture, dotType, "outOfRange");
            return SCRIPT_CONTINUE;
        }
        int expectedMindCost = healing.getCombatMedicMindCost(
            self, BASE_MIND_COST);
        if (expectedMindCost < 0 || getAttrib(self, MIND) < expectedMindCost)
        {
            sendSystemMessage(self, SID_NOT_ENOUGH_MIND);
            recordOutcome(self, fixture, dotType, "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int mindBefore = getAttrib(self, MIND);
        int chargesBefore = getCount(medicine);
        int xpBefore = getExperiencePoints(self, "medical");
        int strengthBefore = getDotStrength(target, dotType);
        boolean performed = dotType.equals(dot.DOT_POISON)
            ? healing.performApplyPosion(self, target, medicine)
            : healing.performApplyDisease(self, target, medicine);
        if (!performed)
        {
            recordOutcome(self, fixture, dotType, "applicationRejected");
            return SCRIPT_CONTINUE;
        }

        int roundTime = calculateRoundTime(self, defaultTime);
        setObjVar(self, cooldown, getGameTime() + roundTime);
        doAnimationAction(self, "throw_grenade");
        if (dotType.equals(dot.DOT_POISON))
        {
            if (healing.isAreaMedicine(medicine))
                healing.playApplyAreaPoisonEffect(getLocation(target));
            else
                healing.playApplyPoisonEffect(getLocation(target));
        }
        else
        {
            if (healing.isAreaMedicine(medicine))
                healing.playApplyAreaDiseaseEffect(getLocation(target));
            else
                healing.playApplyDiseaseEffect(getLocation(target));
        }

        if (fixture)
        {
            String root = FIXTURE_ROOT + "." + dotType;
            int chargesAfter = isIdValid(medicine) && exists(medicine)
                ? getCount(medicine) : 0;
            setObjVar(self, root + ".target", target.toString());
            setObjVar(self, root + ".medicine", medicine.toString());
            setObjVar(self, root + ".range", range);
            setObjVar(self, root + ".strengthBefore", strengthBefore);
            setObjVar(self, root + ".strengthAfter",
                getDotStrength(target, dotType));
            setObjVar(self, root + ".mindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(self, root + ".chargeCost",
                chargesBefore - chargesAfter);
            setObjVar(self, root + ".medicalXpDelta",
                getExperiencePoints(self, "medical") - xpBefore);
            setObjVar(self, root + ".roundTime", roundTime);
        }
        recordOutcome(self, fixture, dotType, "performed");
        return SCRIPT_CONTINUE;
    }

    private boolean isEligibleTarget(obj_id medic, obj_id target)
        throws InterruptedException
    {
        return isIdValid(target) && exists(target) && target != medic &&
            isMob(target) && !isDead(target) && !isIncapacitated(target) &&
            !pet_lib.isVehiclePet(target) && !ai_lib.isDroid(target) &&
            !ai_lib.isAndroid(target) &&
            factions.pvpDoAllowedAttackCheck(medic, target);
    }

    private boolean isValidMedicine(obj_id medic, obj_id medicine,
        String dotType) throws InterruptedException
    {
        if (!isIdValid(medicine) || !exists(medicine) ||
            utils.getContainingPlayer(medicine) != medic ||
            getCount(medicine) <= 0)
        {
            return false;
        }
        return dotType.equals(dot.DOT_POISON)
            ? healing.isApplyPoisonMedicine(medicine)
            : healing.isApplyDiseaseMedicine(medicine);
    }

    private obj_id parseMedicine(obj_id medic, String params)
        throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
            return obj_id.NULL_ID;
        String[] tokens = params.trim().split("[| ]+");
        for (String token : tokens)
        {
            try
            {
                obj_id medicine = obj_id.getObjId(Long.parseLong(token));
                if (isIdValid(medicine) &&
                    utils.getContainingPlayer(medicine) == medic)
                    return medicine;
            }
            catch (NumberFormatException exception)
            {
            }
        }
        return obj_id.NULL_ID;
    }

    private boolean isReady(obj_id medic, String cooldown)
        throws InterruptedException
    {
        if (!hasObjVar(medic, cooldown))
            return true;
        int readyAt = getIntObjVar(medic, cooldown);
        if (readyAt > getGameTime())
            return false;
        removeObjVar(medic, cooldown);
        return true;
    }

    private int calculateRoundTime(obj_id medic, float defaultTime)
        throws InterruptedException
    {
        int speed = getSkillStatMod(medic, "healing_range_speed");
        int roundTime = Math.round(12.0f - 6.0f * speed / 100.0f);
        int recovery = getSkillStatMod(medic, "heal_recovery");
        if (recovery > 0)
            roundTime = Math.round(roundTime * (100.0f - recovery) / 100.0f);
        return Math.max(Math.max(MIN_ROUND_TIME, Math.round(defaultTime)),
            roundTime);
    }

    private int getDotStrength(obj_id target, String dotType)
        throws InterruptedException
    {
        String[] ids = dot.getAllDotsType(target, dotType);
        if (ids == null)
            return 0;
        int total = 0;
        for (String id : ids)
        {
            int strength = dot.getDotStrength(target, id);
            if (strength > 0)
                total += strength;
        }
        return total;
    }

    private void markEntry(obj_id medic, boolean fixture, String dotType)
        throws InterruptedException
    {
        if (!fixture)
            return;
        String root = FIXTURE_ROOT + "." + dotType;
        int calls = hasObjVar(medic, root + ".handlerCalls")
            ? getIntObjVar(medic, root + ".handlerCalls") : 0;
        setObjVar(medic, root + ".handlerEntered", 1);
        setObjVar(medic, root + ".handlerCalls", calls + 1);
        setObjVar(medic, root + ".outcome", "entered");
    }

    private void recordOutcome(obj_id medic, boolean fixture,
        String dotType, String outcome) throws InterruptedException
    {
        if (fixture && isIdValid(medic))
            setObjVar(medic, FIXTURE_ROOT + "." + dotType + ".outcome",
                outcome);
    }
}
