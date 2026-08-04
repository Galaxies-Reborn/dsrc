package script.player.cmd;

import script.obj_id;
import script.prose_package;
import script.string_id;
import script.library.ai_lib;
import script.library.dot;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.prose;

/**
 * Publish 14.1 organic first-aid command.
 *
 * First aid consumes no medicine and no Mind. It removes bleeding strength
 * equal to three times injury treatment from the medic or an eligible
 * six-meter organic patient.
 */
public class first_aid extends script.base_script
{
    private static final float FIRST_AID_RANGE = 6.0f;
    private static final string_id SID_SELF_NOT_BLEEDING =
        new string_id("healing_response", "healing_response_78");
    private static final string_id SID_TARGET_NOT_BLEEDING =
        new string_id("healing_response", "healing_response_80");
    private static final string_id SID_INVALID_TARGET =
        new string_id("healing_response", "healing_response_79");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final String FIXTURE_ROOT =
        "precu.firstAidCommandFixture";

    public int cmdFirstAid(
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
            sendSystemMessage(self, SID_INVALID_TARGET);
            recordOutcome(self, fixture, "targetRejected");
            return SCRIPT_CONTINUE;
        }
        if (self != target)
        {
            if (getDistance(self, target) > FIRST_AID_RANGE)
            {
                healing.sendMedicalSpam(
                    self,
                    script.library.consumable.SID_TARGET_OUT_OF_RANGE,
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
                recordOutcome(self, fixture, "lineOfSightRejected");
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
        if (!dot.isBleeding(target))
        {
            if (self == target)
            {
                sendSystemMessage(self, SID_SELF_NOT_BLEEDING);
            }
            else
            {
                prose_package message =
                    prose.getPackage(SID_TARGET_NOT_BLEEDING, target);
                sendSystemMessageProse(self, message);
            }
            recordOutcome(self, fixture, "notBleeding");
            return SCRIPT_CONTINUE;
        }

        int treatment =
            getSkillStatMod(self, "healing_injury_treatment");
        int requestedReduction = treatment * 3;
        int mindBefore = fixture ? getAttrib(self, MIND) : 0;
        int reduced =
            dot.reduceDotTypeStrength(
                target,
                dot.DOT_BLEEDING,
                requestedReduction);
        boolean performed = reduced >= 0;
        if (performed)
        {
            if (self != target)
            {
                prose_package toMedic =
                    prose.getPackage(
                        healing.SID_YOU_APPLY_FIRST_AID,
                        target);
                healing.sendMedicalSpam(
                    self,
                    toMedic,
                    COMBAT_RESULT_MEDICAL);
                prose_package toPatient =
                    prose.getPackage(
                        healing.SID_APPLIES_FIRST_AID,
                        self);
                healing.sendMedicalSpam(
                    target,
                    toPatient,
                    COMBAT_RESULT_MEDICAL);
                pvpHelpPerformed(self, target);
            }
            else
            {
                healing.sendMedicalSpam(
                    self,
                    healing.SID_APPLY_FIRST_AID_SELF,
                    COMBAT_RESULT_MEDICAL);
            }
            doAnimationAction(
                self,
                self == target ? "heal_self" : "heal_other");
            healing.playHealDamageEffect(getLocation(target));
        }
        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".treatment",
                treatment);
            setObjVar(
                self,
                FIXTURE_ROOT + ".requestedReduction",
                requestedReduction);
            setObjVar(
                self,
                FIXTURE_ROOT + ".reportedReduction",
                reduced);
            setObjVar(
                self,
                FIXTURE_ROOT + ".mindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".bleedingAfter",
                dot.isBleeding(target) ? 1 : 0);
        }
        recordOutcome(
            self,
            fixture,
            performed ? "performed" : "reductionRejected");
        return SCRIPT_CONTINUE;
    }

    private boolean isEligiblePatient(obj_id medic, obj_id target)
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
        int calls = hasObjVar(medic, FIXTURE_ROOT + ".handlerCalls")
            ? getIntObjVar(medic, FIXTURE_ROOT + ".handlerCalls")
            : 0;
        setObjVar(medic, FIXTURE_ROOT + ".handlerCalls", calls + 1);
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
