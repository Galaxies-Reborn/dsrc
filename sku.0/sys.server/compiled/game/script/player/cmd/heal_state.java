package script.player.cmd;

import script.obj_id;
import script.string_id;
import script.library.ai_lib;
import script.library.buff;
import script.library.consumable;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.utils;

/**
 * Publish 14.1 Heal State compatibility adapter.
 *
 * Retail owned this command in the native server. SWGSource dispatches it
 * through this narrow script while preserving the pinned Core3 behavior.
 */
public class heal_state extends script.base_script
{
    public static final String COOLDOWN_VAR =
        "healing.can_heal_state";
    private static final float RANGE = 6.0f;
    private static final int BASE_MIND_COST = 20;
    private static final int MIN_ROUND_TIME = 4;
    private static final int[] HEALABLE_STATES =
    {
        STATE_STUNNED,
        STATE_DIZZY,
        STATE_BLINDED,
        STATE_INTIMIDATED
    };
    private static final string_id SID_INVALID_TARGET =
        new string_id(
            "healing_response",
            "healing_response_73");
    private static final string_id SID_NO_MEDICINE =
        new string_id(
            "healing_response",
            "healing_response_60");
    private static final string_id SID_NO_STATE_SELF =
        new string_id(
            "healing_response",
            "healing_response_72");
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
        "precu.healStateCommandFixture";

    public int healState(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) && hasObjVar(self, FIXTURE_ROOT);
        markEntry(self, fixture);
        if (!isIdValid(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isPlayer(self))
        {
            recordOutcome(self, fixture, "notPlayer");
            return SCRIPT_CONTINUE;
        }
        if (isDead(self))
        {
            recordOutcome(self, fixture, "dead");
            return SCRIPT_CONTINUE;
        }
        if (isIncapacitated(self))
        {
            recordOutcome(self, fixture, "incapacitated");
            return SCRIPT_CONTINUE;
        }
        if (getSkillStatMod(
                self,
                "healing_injury_treatment") <= 0)
        {
            recordOutcome(self, fixture, "noTreatmentSkill");
            return SCRIPT_CONTINUE;
        }
        if (!isEligiblePatient(self, target))
        {
            target = self;
        }
        if (!isEligiblePatient(self, target))
        {
            sendSystemMessage(self, SID_INVALID_TARGET);
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
        if (!canTreatState(self))
        {
            sendSystemMessage(self, SID_MUST_WAIT);
            recordOutcome(self, fixture, "cooldownRejected");
            return SCRIPT_CONTINUE;
        }

        int state = parseState(params);
        obj_id medicine = parseMedicine(self, params);
        if (isIdValid(medicine) &&
            healing.isHealStateMedicine(medicine))
        {
            int medicineState = healing.stringToState(
                healing.getHealingState(medicine));
            if (medicineState >= 0)
            {
                state = medicineState;
            }
        }
        else
        {
            medicine = obj_id.NULL_ID;
        }

        boolean foundState = false;
        if (state >= 0)
        {
            foundState = getState(target, state) == 1;
            if (!isIdValid(medicine))
            {
                medicine =
                    healing.findHealStateMedicine(self, state);
            }
        }
        else
        {
            for (int candidate : HEALABLE_STATES)
            {
                if (getState(target, candidate) != 1)
                {
                    continue;
                }
                foundState = true;
                obj_id candidateMedicine =
                    healing.findHealStateMedicine(
                        self,
                        candidate);
                if (isIdValid(candidateMedicine))
                {
                    state = candidate;
                    medicine = candidateMedicine;
                    break;
                }
            }
        }
        if (!foundState || state < 0 ||
            getState(target, state) != 1)
        {
            if (self == target)
            {
                sendSystemMessage(self, SID_NO_STATE_SELF);
            }
            else
            {
                sendSystemMessage(
                    self,
                    new string_id(
                        "healing",
                        "no_state_to_heal"));
            }
            recordOutcome(self, fixture, "noState");
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(medicine) ||
            !healing.isHealStateMedicine(medicine) ||
            utils.getContainingPlayer(medicine) != self)
        {
            sendSystemMessage(self, SID_NO_MEDICINE);
            recordOutcome(self, fixture, "noMedicine");
            return SCRIPT_CONTINUE;
        }

        int mindCost =
            healing.getMedicalMindCost(self, BASE_MIND_COST);
        int mindBefore = getAttrib(self, MIND);
        if (mindCost < 0 || mindBefore < mindCost)
        {
            sendSystemMessage(self, SID_NOT_ENOUGH_MIND);
            recordOutcome(self, fixture, "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int chargesBefore = getCount(medicine);
        int xpBefore = getExperiencePoints(self, "medical");
        if (!consumable.consumeItem(
                self,
                target,
                medicine,
                true,
                RANGE))
        {
            recordOutcome(self, fixture, "consumeRejected");
            return SCRIPT_CONTINUE;
        }

        buff.removeAllBuffsOfStateType(target, state);
        setState(target, state, false);
        if (getState(target, state) != 0)
        {
            recordOutcome(self, fixture, "removeRejected");
            return SCRIPT_CONTINUE;
        }

        setAttrib(self, MIND, mindBefore - mindCost);
        int roundTime = getRoundTime(self);
        setObjVar(
            self,
            COOLDOWN_VAR,
            getGameTime() + roundTime);
        if (self != target && isPlayer(target))
        {
            grantExperiencePoints(self, "medical", 50);
        }

        doAnimationAction(
            self,
            self == target ? "heal_self" : "heal_other");
        healing.playHealStateEffect(getLocation(target));
        if (self != target)
        {
            pvpHelpPerformed(self, target);
        }
        sendStateMessage(self, target, state);

        if (fixture)
        {
            int chargesAfter =
                isIdValid(medicine) && exists(medicine)
                    ? getCount(medicine)
                    : 0;
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".state",
                state);
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
                FIXTURE_ROOT + ".stateRemoved",
                getState(target, state) == 0 ? 1 : 0);
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

    private boolean canTreatState(obj_id medic)
        throws InterruptedException
    {
        if (!hasObjVar(medic, COOLDOWN_VAR))
        {
            return true;
        }
        int readyAt = getIntObjVar(medic, COOLDOWN_VAR);
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
            roundTime = Math.round(
                roundTime *
                    (100.0f - recovery) /
                    100.0f);
        }
        return Math.max(MIN_ROUND_TIME, roundTime);
    }

    private int parseState(String params)
        throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return -1;
        }
        String[] tokens = params.trim().split("[| ]+");
        return tokens.length == 0
            ? -1
            : healing.stringToState(tokens[0]);
    }

    private obj_id parseMedicine(
        obj_id medic,
        String params)
        throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return obj_id.NULL_ID;
        }
        String[] tokens = params.trim().split("[| ]+");
        for (int index = 1; index < tokens.length; ++index)
        {
            try
            {
                obj_id medicine = obj_id.getObjId(
                    Long.parseLong(tokens[index]));
                if (isIdValid(medicine) &&
                    utils.getContainingPlayer(medicine) == medic)
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

    private void sendStateMessage(
        obj_id medic,
        obj_id target,
        int state)
        throws InterruptedException
    {
        String stateName =
            healing.stateTypeToString(state).toLowerCase();
        if (medic == target)
        {
            sendSystemMessageTestingOnly(
                medic,
                "You remove the " + stateName +
                    " state from yourself.");
        }
        else
        {
            sendSystemMessageTestingOnly(
                medic,
                "You remove the " + stateName +
                    " state from " + getFirstName(target) + ".");
            if (isPlayer(target))
            {
                sendSystemMessageTestingOnly(
                    target,
                    getFirstName(medic) + " removes the " +
                        stateName + " state from you.");
            }
        }
    }

    private void markEntry(obj_id medic, boolean fixture)
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
            hasObjVar(medic, FIXTURE_ROOT + ".handlerCalls")
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
        if (fixture)
        {
            setObjVar(
                medic,
                FIXTURE_ROOT + ".outcome",
                outcome);
        }
    }
}
