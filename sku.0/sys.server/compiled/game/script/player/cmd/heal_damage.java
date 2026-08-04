package script.player.cmd;

import script.obj_id;
import script.string_id;
import script.library.ai_lib;
import script.library.consumable;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.utils;

/**
 * Publish 14.1 player command adapter for medical stim packs.
 *
 * The authentic command is a five-second combat-queue entry. This adapter
 * resolves the patient and medicine, revalidates range, visibility, PvP,
 * treatment recovery, and damage, then enters the retained production
 * medicine-consumption path.
 */
public class heal_damage extends script.base_script
{
    private static final string_id SID_NO_MEDICINE =
        new string_id("healing_response", "healing_response_60");
    private static final string_id SID_NO_DAMAGE =
        new string_id("healing_response", "healing_response_61");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final float NORMAL_MEDICINE_RANGE = 7.0f;
    private static final String FIXTURE_ROOT =
        "precu.healDamageCommandFixture";

    public int cmdHealDamage(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) && hasObjVar(self, FIXTURE_ROOT);
        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".handlerEntered",
                getGameTime());
            int handlerCalls =
                hasObjVar(self, FIXTURE_ROOT + ".handlerCalls")
                    ? getIntObjVar(
                        self,
                        FIXTURE_ROOT + ".handlerCalls")
                    : 0;
            setObjVar(
                self,
                FIXTURE_ROOT + ".handlerCalls",
                handlerCalls + 1);
            setObjVar(
                self,
                FIXTURE_ROOT + ".handlerOutcome",
                "entered");
        }
        if (!isIdValid(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isValidPatient(self, target))
        {
            target = self;
        }
        if (self != target)
        {
            if (getDistance(self, target) > NORMAL_MEDICINE_RANGE)
            {
                healing.sendMedicalSpam(
                    self,
                    consumable.SID_TARGET_OUT_OF_RANGE,
                    COMBAT_RESULT_OUT_OF_RANGE);
                return SCRIPT_CONTINUE;
            }
            if (!canSee(self, target))
            {
                healing.sendMedicalSpam(
                    self,
                    SID_NO_LINE_OF_SIGHT,
                    COMBAT_RESULT_MEDICAL);
                return SCRIPT_CONTINUE;
            }
            if (!pvpCanHelp(self, target) ||
                !factions.pvpDoAllowedHelpCheck(self, target))
            {
                healing.sendMedicalSpam(
                    self,
                    healing.SID_PVP_NO_HELP,
                    COMBAT_RESULT_MEDICAL);
                return SCRIPT_CONTINUE;
            }
        }
        if (!healing.canHealDamage(self))
        {
            recordFixtureOutcome(
                self,
                fixture,
                "canHealDamageRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id medicine = parseMedicine(self, params);
        if (!isIdValid(medicine))
        {
            medicine = healing.findHealDamageMedicine(self, target);
        }
        if (!isIdValid(medicine) || !healing.isMedicine(medicine))
        {
            healing.sendMedicalSpam(
                self,
                SID_NO_MEDICINE,
                COMBAT_RESULT_MEDICAL);
            return SCRIPT_CONTINUE;
        }
        if (!hasPrimaryDamage(target))
        {
            healing.sendMedicalSpam(
                self,
                SID_NO_DAMAGE,
                COMBAT_RESULT_MEDICAL);
            return SCRIPT_CONTINUE;
        }

        int mindBefore = fixture ? getAttrib(self, MIND) : 0;
        int healthBefore = fixture ? getAttrib(target, HEALTH) : 0;
        int actionBefore = fixture ? getAttrib(target, ACTION) : 0;
        int mindPoolBefore = fixture ? getAttrib(target, MIND) : 0;
        int chargesBefore = fixture ? getCount(medicine) : 0;
        boolean performed =
            healing.performMedicalHealDamage(
                self,
                target,
                medicine,
                true,
                NORMAL_MEDICINE_RANGE);
        if (performed)
        {
            doAnimationAction(
                self,
                self == target ? "heal_self" : "heal_other");
            healing.playHealDamageEffect(getLocation(target));
        }
        if (fixture)
        {
            int chargesAfter =
                isIdValid(medicine) && exists(medicine)
                    ? getCount(medicine)
                    : 0;
            int appliedHealthHeal =
                getAttrib(target, HEALTH) - healthBefore;
            int appliedActionHeal =
                getAttrib(target, ACTION) - actionBefore;
            int appliedMindHeal =
                getAttrib(target, MIND) - mindPoolBefore;
            int expectedMedicalXp =
                self != target && isPlayer(target)
                    ? Math.round(
                        (appliedHealthHeal + appliedActionHeal) * 0.25f)
                    : 0;
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedMindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedHealthHeal",
                appliedHealthHeal);
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedActionHeal",
                appliedActionHeal);
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedMindHeal",
                appliedMindHeal);
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedChargeCost",
                chargesBefore - chargesAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".expectedMedicalXp",
                expectedMedicalXp);
            recordFixtureOutcome(
                self,
                true,
                performed ? "performed" : "performRejected");
        }
        return SCRIPT_CONTINUE;
    }

    private void recordFixtureOutcome(
        obj_id healer,
        boolean fixture,
        String outcome)
        throws InterruptedException
    {
        if (fixture)
        {
            setObjVar(
                healer,
                FIXTURE_ROOT + ".handlerOutcome",
                outcome);
        }
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

    private boolean hasPrimaryDamage(obj_id target)
        throws InterruptedException
    {
        int[] attributes = { HEALTH, ACTION, MIND };
        for (int attribute : attributes)
        {
            if (getAttrib(target, attribute) <
                getWoundedMaxAttrib(target, attribute))
            {
                return true;
            }
        }
        return false;
    }

    private obj_id parseMedicine(obj_id healer, String params)
        throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return obj_id.NULL_ID;
        }
        String[] tokens = params.trim().split("[| ]+");
        for (String token : tokens)
        {
            try
            {
                obj_id medicine =
                    obj_id.getObjId(Long.parseLong(token));
                if (isIdValid(medicine) &&
                    utils.getContainingPlayer(medicine) == healer)
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
}
