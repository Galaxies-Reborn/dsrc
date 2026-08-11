package script.player.cmd;

import script.obj_id;
import script.prose_package;
import script.string_id;
import script.library.ai_lib;
import script.library.consumable;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.prose;

/**
 * Publish 14.1 Quick Heal command.
 *
 * The command consumes no medicine and grants no XP. It heals the same random
 * amount of Health and Action damage, charges a Focus-adjusted 1,000-point
 * Mind cost, and applies ten Focus and Willpower wounds.
 */
public class quick_heal extends script.base_script
{
    private static final float RANGE = 6.0f;
    private static final int BASE_MIND_COST = 1000;
    private static final int MIND_WOUND_COST = 10;
    private static final int MIN_HEAL = 150;
    private static final int MAX_HEAL = 750;
    private static final string_id SID_NO_DAMAGE_SELF =
        new string_id("healing_response", "healing_response_61");
    private static final string_id SID_NO_DAMAGE_TARGET =
        new string_id("healing_response", "healing_response_63");
    private static final string_id SID_INVALID_TARGET =
        new string_id("healing_response", "healing_response_99");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final String FIXTURE_ROOT =
        "precu.quickHealCommandFixture";

    public int quickHeal(
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

        int healthBefore = getAttrib(target, HEALTH);
        int actionBefore = getAttrib(target, ACTION);
        if (healthBefore >= getWoundedMaxAttrib(target, HEALTH) &&
            actionBefore >= getWoundedMaxAttrib(target, ACTION))
        {
            if (self == target)
            {
                sendSystemMessage(self, SID_NO_DAMAGE_SELF);
            }
            else
            {
                prose_package message =
                    prose.getPackage(SID_NO_DAMAGE_TARGET, target);
                sendSystemMessageProse(self, message);
            }
            recordOutcome(self, fixture, "noDamage");
            return SCRIPT_CONTINUE;
        }

        int focus = getAttrib(self, FOCUS);
        int mindCost = calculateMindCost(focus);
        int mindBefore = getAttrib(self, MIND);
        if (mindBefore < mindCost)
        {
            healing.sendMedicalSpam(
                self,
                healing.SID_NOT_ENOUGH_MIND,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(self, fixture, "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int focusWoundBefore = getAttribWound(self, FOCUS);
        int willpowerWoundBefore =
            getAttribWound(self, WILLPOWER);
        int medicalXpBefore =
            getExperiencePoints(self, "medical");
        int healPower = rand(MIN_HEAL, MAX_HEAL);
        int healthHealed =
            healing.healDamage(
                self,
                target,
                HEALTH,
                healPower,
                true);
        int actionHealed =
            healing.healDamage(
                self,
                target,
                ACTION,
                healPower,
                true);
        setAttrib(self, MIND, mindBefore - mindCost);
        addWound(self, FOCUS, MIND_WOUND_COST);
        addWound(self, WILLPOWER, MIND_WOUND_COST);

        doAnimationAction(
            self,
            self == target ? "heal_self" : "heal_other");
        healing.playHealDamageEffect(getLocation(target));
        if (self != target)
        {
            pvpHelpPerformed(self, target);
        }

        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".focus",
                focus);
            setObjVar(
                self,
                FIXTURE_ROOT + ".mindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".healPower",
                healPower);
            setObjVar(
                self,
                FIXTURE_ROOT + ".healthHealed",
                healthHealed);
            setObjVar(
                self,
                FIXTURE_ROOT + ".actionHealed",
                actionHealed);
            setObjVar(
                self,
                FIXTURE_ROOT + ".focusWounds",
                getAttribWound(self, FOCUS) -
                    focusWoundBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".willpowerWounds",
                getAttribWound(self, WILLPOWER) -
                    willpowerWoundBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".medicalXpDelta",
                getExperiencePoints(self, "medical") -
                    medicalXpBefore);
        }
        recordOutcome(self, fixture, "performed");
        return SCRIPT_CONTINUE;
    }

    private int calculateMindCost(int focus)
    {
        float cost =
            BASE_MIND_COST -
            (((focus - 300.0f) / 1200.0f) *
                BASE_MIND_COST);
        return Math.max(0, (int)cost);
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
