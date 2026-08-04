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
 * Publish 14.1 player command adapter for medical wound packs.
 *
 * The retail client and medicine radial both queue healWound. This adapter
 * resolves the selected pack (or searches for one), revalidates the original
 * target/range/facility/combat/PvP gates, then enters the retained production
 * medicine-consumption path.
 */
public class heal_wound extends script.base_script
{
    private static final string_id SID_NO_MEDICINE =
        new string_id("healing_response", "healing_response_60");
    private static final string_id SID_INVALID_MEDICINE =
        new string_id("healing_response", "healing_response_66");
    private static final string_id SID_NO_WOUNDS =
        new string_id("healing_response", "healing_response_67");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");
    private static final int[] MEDICAL_WOUND_ATTRIBUTES = {
        HEALTH,
        STRENGTH,
        CONSTITUTION,
        ACTION,
        QUICKNESS,
        STAMINA
    };
    private static final String FIXTURE_ROOT =
        "precu.healWoundCommandFixture";

    public int cmdHealWound(
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
        if (getState(self, STATE_COMBAT) == 1)
        {
            sendSystemMessage(
                self,
                "You cannot heal your own wounds while still in Combat.",
                "");
            return SCRIPT_CONTINUE;
        }
        if (getState(target, STATE_COMBAT) == 1)
        {
            sendSystemMessage(
                self,
                "You cannot heal your target's wounds while they are in Combat.",
                "");
            return SCRIPT_CONTINUE;
        }
        if (self != target)
        {
            if (getDistance(self, target) > consumable.MAX_AFFECT_DISTANCE)
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
        if (!healing.canHealWound(self))
        {
            recordFixtureOutcome(
                self,
                fixture,
                "canHealWoundRejected");
            return SCRIPT_CONTINUE;
        }

        int requestedAttribute = parseAttribute(params);
        obj_id medicine = parseMedicine(self, params);
        if (isIdValid(medicine))
        {
            if (healing.getHealWoundMedicineAttribute(medicine) < 0)
            {
                healing.sendMedicalSpam(
                    self,
                    SID_INVALID_MEDICINE,
                    COMBAT_RESULT_MEDICAL);
                return SCRIPT_CONTINUE;
            }
        }
        else
        {
            medicine =
                findMedicine(self, target, requestedAttribute);
            if (!isIdValid(medicine))
            {
                healing.sendMedicalSpam(
                    self,
                    SID_NO_MEDICINE,
                    COMBAT_RESULT_MEDICAL);
                return SCRIPT_CONTINUE;
            }
        }

        int attribute =
            healing.getHealWoundMedicineAttribute(medicine);
        if (getAttribWound(target, attribute) <= 0)
        {
            if (self == target)
            {
                healing.sendMedicalSpam(
                    self,
                    SID_NO_WOUNDS,
                    COMBAT_RESULT_MEDICAL);
            }
            else
            {
                healing.sendMedicalSpam(
                    self,
                    healing.SID_NO_WOUNDS_OF_TYPE_TARGET,
                    COMBAT_RESULT_MEDICAL);
            }
            return SCRIPT_CONTINUE;
        }
        int mindBefore = fixture ? getAttrib(self, MIND) : 0;
        int woundBefore =
            fixture ? getAttribWound(target, attribute) : 0;
        int chargesBefore = fixture ? getCount(medicine) : 0;
        boolean performed =
            healing.performMedicalHealWound(
                self,
                target,
                medicine,
                true);
        if (fixture)
        {
            int chargesAfter =
                isIdValid(medicine) && exists(medicine)
                    ? getCount(medicine)
                    : 0;
            int appliedWoundHeal =
                woundBefore - getAttribWound(target, attribute);
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedMindCost",
                mindBefore - getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedWoundHeal",
                appliedWoundHeal);
            setObjVar(
                self,
                FIXTURE_ROOT + ".appliedChargeCost",
                chargesBefore - chargesAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".expectedMedicalXp",
                (int)(appliedWoundHeal * 2.5f));
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

    private int parseAttribute(String params)
        throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return -1;
        }
        String name = params.trim().split("[| ]+")[0];
        for (int attribute : MEDICAL_WOUND_ATTRIBUTES)
        {
            if (name.equalsIgnoreCase(
                    healing.attributeToString(attribute)))
            {
                return attribute;
            }
        }
        return -1;
    }

    private obj_id findMedicine(
        obj_id healer,
        obj_id target,
        int requestedAttribute)
        throws InterruptedException
    {
        if (requestedAttribute >= HEALTH &&
            getAttribWound(target, requestedAttribute) > 0)
        {
            return
                healing.findHealWoundMedicine(
                    healer,
                    requestedAttribute);
        }
        for (int attribute : MEDICAL_WOUND_ATTRIBUTES)
        {
            if (getAttribWound(target, attribute) <= 0)
            {
                continue;
            }
            obj_id medicine =
                healing.findHealWoundMedicine(healer, attribute);
            if (isIdValid(medicine))
            {
                return medicine;
            }
        }
        return obj_id.NULL_ID;
    }
}
