package script.player.cmd;

import script.obj_id;
import script.string_id;
import script.library.ai_lib;
import script.library.factions;
import script.library.healing;
import script.library.pet_lib;
import script.library.sui;
import script.library.utils;
import script.library.vehicle;

/**
 * Publish 14.1 medical diagnosis command.
 *
 * The command is a read-only six-meter inspection. It displays the patient's
 * nine organic wounds and Battle Fatigue in the original medical listbox.
 */
public class diagnose extends script.base_script
{
    private static final float DIAGNOSE_RANGE = 6.0f;
    private static final String SUI_PID = "precu.diagnose.pid";
    private static final String FIXTURE_ROOT =
        "precu.diagnoseCommandFixture";
    private static final string_id SID_CANNOT_DIAGNOSE =
        new string_id("healing_response", "healing_response_b6");
    private static final String[] ATTRIBUTE_NAMES =
    {
        "Health",
        "Strength",
        "Constitution",
        "Action",
        "Quickness",
        "Stamina",
        "Mind",
        "Focus",
        "Willpower"
    };
    private static final int[] ATTRIBUTES =
    {
        HEALTH,
        STRENGTH,
        CONSTITUTION,
        ACTION,
        QUICKNESS,
        STAMINA,
        MIND,
        FOCUS,
        WILLPOWER
    };

    public int cmdDiagnose(
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
        if (!isOrganicCreature(target))
        {
            sendSystemMessage(self, SID_CANNOT_DIAGNOSE);
            recordOutcome(self, fixture, "targetRejected");
            return SCRIPT_CONTINUE;
        }
        if (getDistance(self, target) > DIAGNOSE_RANGE)
        {
            healing.sendMedicalSpam(
                self,
                script.library.consumable.SID_TARGET_OUT_OF_RANGE,
                COMBAT_RESULT_OUT_OF_RANGE);
            recordOutcome(self, fixture, "outOfRange");
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

        if (utils.hasScriptVar(self, SUI_PID))
        {
            int oldPid = utils.getIntScriptVar(self, SUI_PID);
            if (oldPid >= 0)
            {
                sui.closeSUI(self, oldPid);
            }
            utils.removeScriptVar(self, SUI_PID);
        }

        int[] observedWounds = new int[ATTRIBUTES.length];
        String[] entries = new String[ATTRIBUTES.length + 1];
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            observedWounds[index] =
                getAttribWound(target, ATTRIBUTES[index]);
            entries[index] =
                ATTRIBUTE_NAMES[index] + " -- " +
                observedWounds[index];
        }
        int battleFatigue = getShockWound(target);
        entries[ATTRIBUTES.length] =
            "Battle Fatigue -- " + battleFatigue;

        String patientName = getName(target);
        String title = "Patient " + patientName;
        String prompt =
            "Below is a listing of the wound and Battle Fatigue levels of " +
            patientName + ". Wounds are healed through /tendwound or use " +
            "of wound Medpacks. High levels of Battle Fatigue can inhibit " +
            "the healing process, and Battle Fatigue can only be healed by " +
            "the patient choosing to watch performing entertainers.";
        int pid = sui.listbox(
            self,
            self,
            prompt,
            sui.OK_ONLY,
            title,
            entries,
            "noHandler");
        if (pid >= 0)
        {
            utils.setScriptVar(self, SUI_PID, pid);
        }

        if (fixture)
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".observedWounds",
                observedWounds);
            setObjVar(
                self,
                FIXTURE_ROOT + ".observedBattleFatigue",
                battleFatigue);
            setObjVar(
                self,
                FIXTURE_ROOT + ".entryCount",
                entries.length);
            setObjVar(self, FIXTURE_ROOT + ".suiPid", pid);
            setObjVar(self, FIXTURE_ROOT + ".title", title);
        }
        recordOutcome(
            self,
            fixture,
            pid >= 0 ? "displayed" : "suiRejected");
        return SCRIPT_CONTINUE;
    }

    public int cmdFailDiagnose(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) && hasObjVar(self, FIXTURE_ROOT);
        recordOutcome(self, fixture, "commandFailed");
        return SCRIPT_CONTINUE;
    }

    private boolean isOrganicCreature(obj_id target)
        throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) || !isMob(target))
        {
            return false;
        }
        return
            !ai_lib.isDroid(target) &&
            !ai_lib.isAndroid(target) &&
            !ai_lib.isVehicle(target) &&
            !pet_lib.isVehiclePet(target) &&
            !vehicle.isVehicle(target) &&
            !vehicle.isDriveableVehicle(target);
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
