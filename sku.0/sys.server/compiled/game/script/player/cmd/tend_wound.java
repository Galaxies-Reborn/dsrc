package script.player.cmd;

import script.obj_id;
import script.string_id;
import script.library.ai_lib;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.xp;

/**
 * Publish 14.1 organic tend-wound command.
 *
 * With no argument the command chooses the first wounded Health-through-
 * Stamina pool. Mind, Focus, and Willpower wounds are intentionally excluded.
 */
public class tend_wound extends script.base_script
{
    private static final float TEND_RANGE = 6.0f;
    private static final string_id SID_NO_WOUND =
        new string_id("healing_response", "healing_response_67");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final String FIXTURE_ROOT =
        "precu.tendingCommandFixture";

    public int cmdTendWound(
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
            return SCRIPT_CONTINUE;
        }
        if (!isValidPatient(self, target))
        {
            target = self;
        }
        if (!validateOtherPatient(self, target))
        {
            recordOutcome(self, fixture, "patientRejected");
            return SCRIPT_CONTINUE;
        }

        int attribute = parseAttribute(params);
        if (attribute < HEALTH || attribute >= MIND)
        {
            attribute = healing.findLargestTendWound(target);
        }
        if (attribute < HEALTH || attribute >= MIND ||
            getAttribWound(target, attribute) <= 0)
        {
            healing.sendMedicalSpam(
                self,
                SID_NO_WOUND,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(self, fixture, "noWound");
            return SCRIPT_CONTINUE;
        }

        int woundBefore = getAttribWound(target, attribute);
        int mindBefore = getAttrib(self, MIND);
        int focusWoundBefore = getAttribWound(self, FOCUS);
        int willpowerWoundBefore = getAttribWound(self, WILLPOWER);
        int xpBefore = getExperiencePoints(self, xp.MEDICAL);
        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundExpectedMindCost",
                healing.getTendMindCost(self, true));
        }
        boolean performed =
            healing.performTendWound(
                self,
                target,
                attribute,
                true);
        if (performed)
        {
            doAnimationAction(
                self,
                self == target ? "heal_self" : "heal_other");
            healing.playHealWoundEffect(getLocation(target));
        }
        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundAttribute",
                attribute);
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundHeal",
                woundBefore - getAttribWound(target, attribute));
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundMindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundFocusWoundCost",
                getAttribWound(self, FOCUS) - focusWoundBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundWillpowerWoundCost",
                getAttribWound(self, WILLPOWER) -
                    willpowerWoundBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".tendWoundMedicalXp",
                getExperiencePoints(self, xp.MEDICAL) - xpBefore);
        }
        recordOutcome(
            self,
            fixture,
            performed ? "performed" : "performRejected");
        return SCRIPT_CONTINUE;
    }

    private int parseAttribute(String params) throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return -1;
        }
        String[] tokens = params.trim().split("[| ]+");
        return tokens.length == 0
            ? -1
            : healing.stringToAttribute(tokens[0].toUpperCase());
    }

    private void markEntry(obj_id healer, boolean fixture)
        throws InterruptedException
    {
        if (!fixture)
        {
            return;
        }
        setObjVar(
            healer,
            FIXTURE_ROOT + ".tendWoundEntered",
            getGameTime());
        int calls = hasObjVar(healer, FIXTURE_ROOT + ".handlerCalls")
            ? getIntObjVar(healer, FIXTURE_ROOT + ".handlerCalls")
            : 0;
        setObjVar(healer, FIXTURE_ROOT + ".handlerCalls", calls + 1);
        recordOutcome(healer, true, "entered");
    }

    private void recordOutcome(
        obj_id healer,
        boolean fixture,
        String outcome)
        throws InterruptedException
    {
        if (fixture)
        {
            setObjVar(
                healer,
                FIXTURE_ROOT + ".tendWoundOutcome",
                outcome);
        }
    }

    private boolean validateOtherPatient(obj_id healer, obj_id target)
        throws InterruptedException
    {
        if (healer == target)
        {
            return true;
        }
        if (getDistance(healer, target) > TEND_RANGE)
        {
            healing.sendMedicalSpam(
                healer,
                script.library.consumable.SID_TARGET_OUT_OF_RANGE,
                COMBAT_RESULT_OUT_OF_RANGE);
            return false;
        }
        if (!canSee(healer, target))
        {
            healing.sendMedicalSpam(
                healer,
                SID_NO_LINE_OF_SIGHT,
                COMBAT_RESULT_MEDICAL);
            return false;
        }
        if (!pvpCanHelp(healer, target) ||
            !factions.pvpDoAllowedHelpCheck(healer, target))
        {
            healing.sendMedicalSpam(
                healer,
                healing.SID_PVP_NO_HELP,
                COMBAT_RESULT_MEDICAL);
            return false;
        }
        return true;
    }

    private boolean isValidPatient(obj_id healer, obj_id target)
        throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) ||
            isDead(target) || isIncapacitated(target))
        {
            return false;
        }
        if (isPlayer(target))
        {
            return true;
        }
        return
            (pet_lib.isCreaturePet(target) || pet_lib.isNpcPet(target)) &&
            !ai_lib.isAndroid(target) &&
            !pet_lib.isVehiclePet(target) &&
            !pvpCanAttack(healer, target);
    }
}
