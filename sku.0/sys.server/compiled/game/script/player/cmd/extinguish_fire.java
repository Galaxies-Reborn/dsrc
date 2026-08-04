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

/**
 * Publish 14.1 Extinguish Fire compatibility adapter.
 *
 * The retained healing library owns fire-strength reduction, blanket power,
 * area packs, charge consumption, effects, and PvP-help notification. This
 * adapter restores the native cure-pack admission, target, shared treatment
 * recovery, Focus-adjusted Mind cost, and fixed XP rules.
 */
public class extinguish_fire extends script.base_script
{
    public static final String COOLDOWN_VAR =
        "healing.can_heal_state";
    private static final float RANGE = 7.0f;
    private static final int BASE_MIND_COST = 100;
    private static final int MIN_ROUND_TIME = 4;
    private static final string_id SID_NO_MEDICINE =
        new string_id(
            "healing_response",
            "healing_response_60");
    private static final string_id SID_MUST_WAIT =
        new string_id(
            "healing_response",
            "healing_must_wait");
    private static final string_id SID_NOT_ENOUGH_MIND =
        new string_id(
            "healing_response",
            "not_enough_mind");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final String FIXTURE_ROOT =
        "precu.extinguishFireCommandFixture";

    public int extinguishFire(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) && hasObjVar(self, FIXTURE_ROOT);
        markEntry(self, fixture);
        if (!isIdValid(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self))
        {
            recordOutcome(self, fixture, "medicRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isEligiblePatient(self, target))
        {
            target = self;
        }
        if (!isEligiblePatient(self, target))
        {
            recordOutcome(self, fixture, "targetRejected");
            return SCRIPT_CONTINUE;
        }
        if (self != target)
        {
            if (getDistance(self, target) > RANGE)
            {
                healing.sendMedicalSpam(
                    self,
                    consumable.SID_TARGET_OUT_OF_RANGE,
                    COMBAT_RESULT_OUT_OF_RANGE);
                recordOutcome(self, fixture, "outOfRange");
                return SCRIPT_CONTINUE;
            }
            if (!canSee(self, target))
            {
                healing.sendMedicalSpam(
                    self,
                    SID_NO_LINE_OF_SIGHT,
                    COMBAT_RESULT_MEDICAL);
                recordOutcome(
                    self,
                    fixture,
                    "lineOfSightRejected");
                return SCRIPT_CONTINUE;
            }
            if (!pvpCanHelp(self, target) ||
                !factions.pvpDoAllowedHelpCheck(self, target))
            {
                healing.sendMedicalSpam(
                    self,
                    healing.SID_PVP_NO_HELP,
                    COMBAT_RESULT_MEDICAL);
                recordOutcome(self, fixture, "pvpRejected");
                return SCRIPT_CONTINUE;
            }
        }
        if (!dot.isOnFire(target))
        {
            recordOutcome(self, fixture, "notOnFire");
            return SCRIPT_CONTINUE;
        }
        if (!canTreatCondition(self))
        {
            sendSystemMessage(self, SID_MUST_WAIT);
            recordOutcome(self, fixture, "cooldownRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id medicine = parseMedicine(self, params);
        if (!isIdValid(medicine))
        {
            medicine =
                healing.findCureDotMedicine(
                    self,
                    dot.DOT_FIRE);
        }
        if (!isValidMedicine(self, medicine))
        {
            sendSystemMessage(self, SID_NO_MEDICINE);
            recordOutcome(self, fixture, "noMedicine");
            return SCRIPT_CONTINUE;
        }

        int expectedMindCost =
            healing.getMedicalMindCost(
                self,
                BASE_MIND_COST);
        if (expectedMindCost < 0 ||
            getAttrib(self, MIND) < expectedMindCost)
        {
            sendSystemMessage(self, SID_NOT_ENOUGH_MIND);
            recordOutcome(self, fixture, "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int mindBefore = getAttrib(self, MIND);
        int chargesBefore = getCount(medicine);
        int xpBefore =
            getExperiencePoints(self, "medical");
        int fireBefore =
            getFireStrength(target);
        int power =
            healing.getDotPower(medicine);
        boolean performed =
            healing.performCureFire(
                self,
                target,
                medicine);
        if (!performed)
        {
            recordOutcome(self, fixture, "cureRejected");
            return SCRIPT_CONTINUE;
        }

        int roundTime = getRoundTime(self);
        setObjVar(
            self,
            COOLDOWN_VAR,
            getGameTime() + roundTime);
        doAnimationAction(
            self,
            self == target ? "heal_self" : "heal_other");
        healing.playHealDamageEffect(getLocation(target));

        if (fixture)
        {
            int chargesAfter =
                isIdValid(medicine) && exists(medicine)
                    ? getCount(medicine)
                    : 0;
            int fireAfter =
                getFireStrength(target);
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".medicine",
                medicine.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".power",
                power);
            setObjVar(
                self,
                FIXTURE_ROOT + ".fireBefore",
                fireBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".fireAfter",
                fireAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".fireReduction",
                fireBefore - Math.max(0, fireAfter));
            setObjVar(
                self,
                FIXTURE_ROOT + ".mindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".chargeCost",
                chargesBefore - chargesAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".medicalXpDelta",
                getExperiencePoints(self, "medical") -
                    xpBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".roundTime",
                roundTime);
        }
        recordOutcome(self, fixture, "performed");
        return SCRIPT_CONTINUE;
    }

    private boolean isEligiblePatient(
        obj_id medic,
        obj_id target)
        throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) ||
            !isMob(target) || isDead(target) ||
            pet_lib.isMounted(target) ||
            pvpCanAttack(medic, target))
        {
            return false;
        }
        if (isPlayer(target))
        {
            return true;
        }
        return
            (pet_lib.isCreaturePet(target) ||
                pet_lib.isNpcPet(target)) &&
            !ai_lib.isDroid(target) &&
            !ai_lib.isAndroid(target) &&
            !pet_lib.isVehiclePet(target);
    }

    private boolean isValidMedicine(
        obj_id medic,
        obj_id medicine)
        throws InterruptedException
    {
        return
            isIdValid(medicine) &&
            exists(medicine) &&
            healing.isCureFireMedicine(medicine) &&
            utils.getContainingPlayer(medicine) == medic &&
            getCount(medicine) > 0;
    }

    private boolean canTreatCondition(obj_id medic)
        throws InterruptedException
    {
        if (!hasObjVar(medic, COOLDOWN_VAR))
        {
            return true;
        }
        int readyAt =
            getIntObjVar(medic, COOLDOWN_VAR);
        if (readyAt > getGameTime())
        {
            return false;
        }
        removeObjVar(medic, COOLDOWN_VAR);
        return true;
    }

    private int getRoundTime(obj_id medic)
        throws InterruptedException
    {
        int injurySpeed =
            getSkillStatMod(
                medic,
                "healing_injury_speed");
        int roundTime =
            Math.round(20.0f - injurySpeed / 5.0f);
        int recovery =
            getSkillStatMod(medic, "heal_recovery");
        if (recovery > 0)
        {
            roundTime =
                Math.round(
                    roundTime *
                        (100.0f - recovery) /
                        100.0f);
        }
        return Math.max(MIN_ROUND_TIME, roundTime);
    }

    private int getFireStrength(obj_id target)
        throws InterruptedException
    {
        String[] fire =
            dot.getAllDotsType(
                target,
                dot.DOT_FIRE);
        if (fire == null)
        {
            return 0;
        }
        int total = 0;
        for (String dotId : fire)
        {
            int strength =
                dot.getDotStrength(target, dotId);
            if (strength > 0)
            {
                total += strength;
            }
        }
        return total;
    }

    private obj_id parseMedicine(
        obj_id medic,
        String params)
        throws InterruptedException
    {
        if (params == null ||
            params.trim().length() == 0)
        {
            return obj_id.NULL_ID;
        }
        String[] tokens =
            params.trim().split("[| ]+");
        for (String token : tokens)
        {
            try
            {
                obj_id medicine =
                    obj_id.getObjId(
                        Long.parseLong(token));
                if (isIdValid(medicine) &&
                    utils.getContainingPlayer(medicine) ==
                        medic)
                {
                    return medicine;
                }
            }
            catch (NumberFormatException exception)
            {
            }
        }
        return obj_id.NULL_ID;
    }

    private void markEntry(
        obj_id medic,
        boolean fixture)
        throws InterruptedException
    {
        if (!fixture)
        {
            return;
        }
        setObjVar(
            medic,
            FIXTURE_ROOT + ".handlerEntered",
            getGameTime());
        int calls =
            hasObjVar(
                medic,
                FIXTURE_ROOT + ".handlerCalls")
                ? getIntObjVar(
                    medic,
                    FIXTURE_ROOT + ".handlerCalls")
                : 0;
        setObjVar(
            medic,
            FIXTURE_ROOT + ".handlerCalls",
            calls + 1);
        recordOutcome(medic, true, "entered");
    }

    private void recordOutcome(
        obj_id medic,
        boolean fixture,
        String outcome)
        throws InterruptedException
    {
        if (fixture && isIdValid(medic))
        {
            setObjVar(
                medic,
                FIXTURE_ROOT + ".outcome",
                outcome);
        }
    }
}
