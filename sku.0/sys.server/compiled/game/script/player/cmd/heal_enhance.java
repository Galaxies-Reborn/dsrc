package script.player.cmd;

import script.attrib_mod;
import script.obj_id;
import script.prose_package;
import script.string_id;
import script.library.ai_lib;
import script.library.consumable;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.prose;
import script.library.utils;

/**
 * Publish 14.1 Heal Enhance compatibility adapter.
 *
 * The retained consumable and healing libraries own medicine certification,
 * battle-fatigue scaling, buff replacement, charge consumption, medical XP,
 * PvP help, and the actual attribute modifier. This adapter restores the
 * native command's target, location, combat, seven-meter, line-of-sight,
 * medicine-selection, Focus-adjusted Mind, wound-treatment recovery, and
 * presentation rules.
 */
public class heal_enhance extends script.base_script
{
    private static final float RANGE = 7.0f;
    private static final int BASE_MIND_COST = 150;
    private static final string_id SID_NO_MEDICINE =
        new string_id(
            "healing_response",
            "healing_response_60");
    private static final string_id SID_INVALID_MEDICINE =
        new string_id(
            "healing_response",
            "healing_response_76");
    private static final string_id SID_INVALID_TARGET =
        new string_id(
            "healing_response",
            "healing_response_77");
    private static final string_id SID_NOT_ENOUGH_MIND =
        new string_id(
            "healing_response",
            "not_enough_mind");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final string_id SID_NO_ATTRIBUTE =
        new string_id("healing", "no_attrib_to_buff");
    private static final String FIXTURE_ROOT =
        "precu.healEnhanceCommandFixture";

    public int healEnhance(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) &&
            hasObjVar(self, FIXTURE_ROOT);
        markEntry(self, fixture);
        if (!isIdValid(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self))
        {
            recordOutcome(self, fixture, "medicRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id parameterTarget =
            parsePatient(self, params);
        if (isEligiblePatient(self, parameterTarget))
        {
            target = parameterTarget;
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
        if (getState(self, STATE_COMBAT) == 1)
        {
            sendSystemMessage(
                self,
                "You cannot HealEnhance yourself while in Combat.",
                "");
            recordOutcome(self, fixture, "medicInCombat");
            return SCRIPT_CONTINUE;
        }
        if (getState(target, STATE_COMBAT) == 1)
        {
            sendSystemMessage(
                self,
                "You cannot HealEnhance your target while they are still in Combat.",
                "");
            recordOutcome(self, fixture, "targetInCombat");
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
        if (!healing.canHealWound(self))
        {
            recordOutcome(
                self,
                fixture,
                "woundTreatmentRejected");
            return SCRIPT_CONTINUE;
        }

        int requestedAttribute =
            parseAttribute(params);
        obj_id medicine =
            parseMedicine(self, params);
        if (isIdValid(medicine))
        {
            if (!isValidMedicine(
                    self,
                    medicine,
                    -1))
            {
                sendSystemMessage(
                    self,
                    SID_INVALID_MEDICINE);
                recordOutcome(
                    self,
                    fixture,
                    "invalidMedicine");
                return SCRIPT_CONTINUE;
            }
            requestedAttribute =
                healing.getHealEnhanceMedicineAttribute(
                    medicine);
        }
        else
        {
            medicine =
                findMedicine(
                    self,
                    target,
                    requestedAttribute);
        }
        if (!isIdValid(medicine))
        {
            if (requestedAttribute >= HEALTH)
            {
                sendSystemMessage(self, SID_NO_MEDICINE);
            }
            else
            {
                prose_package message =
                    prose.getPackage(
                        SID_NO_ATTRIBUTE,
                        target);
                sendSystemMessageProse(self, message);
            }
            recordOutcome(self, fixture, "noMedicine");
            return SCRIPT_CONTINUE;
        }

        int attribute =
            healing.getHealEnhanceMedicineAttribute(
                medicine);
        int expectedMindCost =
            healing.getMedicalMindCost(
                self,
                BASE_MIND_COST);
        if (expectedMindCost < 0 ||
            getAttrib(self, MIND) < expectedMindCost)
        {
            sendSystemMessage(self, SID_NOT_ENOUGH_MIND);
            recordOutcome(
                self,
                fixture,
                "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int mindBefore = getAttrib(self, MIND);
        int chargesBefore = getCount(medicine);
        int xpBefore =
            getExperiencePoints(self, "medical");
        int buffBefore =
            healing.getHealEnhanceValue(
                target,
                attribute);
        int projectedPower =
            getProjectedPower(
                self,
                target,
                medicine,
                attribute);
        boolean performed =
            healing.performHealEnhance(
                self,
                target,
                medicine);
        if (!performed)
        {
            recordOutcome(
                self,
                fixture,
                "enhanceRejected");
            return SCRIPT_CONTINUE;
        }

        int roundTime =
            healing.getHealEnhanceRoundTime(self);
        healing.setCanHealWound(self, roundTime);
        doAnimationAction(
            self,
            self == target
                ? "heal_self"
                : "heal_other");
        healing.playHealEnhanceEffect(
            getLocation(target));

        if (fixture)
        {
            int chargesAfter =
                isIdValid(medicine) &&
                    exists(medicine)
                    ? getCount(medicine)
                    : 0;
            int buffAfter =
                healing.getHealEnhanceValue(
                    target,
                    attribute);
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
                FIXTURE_ROOT + ".attribute",
                attribute);
            setObjVar(
                self,
                FIXTURE_ROOT + ".projectedPower",
                projectedPower);
            setObjVar(
                self,
                FIXTURE_ROOT + ".buffBefore",
                buffBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".buffAfter",
                buffAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".amountEnhanced",
                buffAfter - buffBefore);
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
            isIncapacitated(target) ||
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

    private obj_id findMedicine(
        obj_id medic,
        obj_id target,
        int requestedAttribute)
        throws InterruptedException
    {
        if (requestedAttribute >= HEALTH &&
            requestedAttribute < MIND)
        {
            obj_id requested =
                healing.findBuffMedicine(
                    medic,
                    requestedAttribute);
            return isValidMedicine(
                    medic,
                    requested,
                    requestedAttribute)
                ? requested
                : obj_id.NULL_ID;
        }

        for (int attribute = HEALTH;
            attribute < MIND;
            ++attribute)
        {
            if (healing.hasEnhancement(
                    target,
                    attribute))
            {
                continue;
            }
            obj_id candidate =
                healing.findBuffMedicine(
                    medic,
                    attribute);
            if (isValidMedicine(
                    medic,
                    candidate,
                    attribute))
            {
                return candidate;
            }
        }
        for (int attribute = HEALTH;
            attribute < MIND;
            ++attribute)
        {
            obj_id candidate =
                healing.findBuffMedicine(
                    medic,
                    attribute);
            if (!isValidMedicine(
                    medic,
                    candidate,
                    attribute))
            {
                continue;
            }
            int current =
                healing.getHealEnhanceValue(
                    target,
                    attribute);
            if (getProjectedPower(
                    medic,
                    target,
                    candidate,
                    attribute) >= current)
            {
                return candidate;
            }
        }
        return obj_id.NULL_ID;
    }

    private boolean isValidMedicine(
        obj_id medic,
        obj_id medicine,
        int expectedAttribute)
        throws InterruptedException
    {
        if (!isIdValid(medicine) ||
            !exists(medicine) ||
            !healing.isBuffMedicine(medicine) ||
            utils.getContainingPlayer(medicine) !=
                medic ||
            getCount(medicine) <= 0)
        {
            return false;
        }
        int attribute =
            healing.getHealEnhanceMedicineAttribute(
                medicine);
        return
            attribute >= HEALTH &&
            attribute < MIND &&
            (expectedAttribute < HEALTH ||
                attribute == expectedAttribute);
    }

    private int getProjectedPower(
        obj_id medic,
        obj_id target,
        obj_id medicine,
        int attribute)
        throws InterruptedException
    {
        attrib_mod[] modifiers =
            getAttribModArrayObjVar(
                medicine,
                consumable.VAR_CONSUMABLE_MODS);
        if (modifiers == null)
        {
            return 0;
        }
        for (attrib_mod modifier : modifiers)
        {
            if (modifier.getAttribute() != attribute)
            {
                continue;
            }
            float multiplier =
                healing.getHealingMultiplier(
                    medic,
                    medicine,
                    healing.HEAL_TYPE_MEDICAL_BUFF);
            multiplier =
                healing.applyShockWoundModifier(
                    multiplier,
                    target);
            return Math.max(
                0,
                (int)(modifier.getValue() *
                    multiplier));
        }
        return 0;
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

    private obj_id parsePatient(
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
                obj_id patient =
                    obj_id.getObjId(
                        Long.parseLong(token));
                if (isEligiblePatient(
                        medic,
                        patient))
                {
                    return patient;
                }
            }
            catch (NumberFormatException exception)
            {
            }
        }
        return obj_id.NULL_ID;
    }

    private int parseAttribute(String params)
        throws InterruptedException
    {
        if (params == null ||
            params.trim().length() == 0)
        {
            return -1;
        }
        String first =
            params.trim().split("[| ]+")[0];
        for (int attribute = HEALTH;
            attribute < MIND;
            ++attribute)
        {
            if (first.equalsIgnoreCase(
                    healing.attributeToString(
                        attribute)))
            {
                return attribute;
            }
        }
        return -1;
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
