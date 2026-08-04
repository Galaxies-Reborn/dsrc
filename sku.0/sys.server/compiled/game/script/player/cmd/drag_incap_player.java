package script.player.cmd;

import script.location;
import script.obj_id;
import script.prose_package;
import script.string_id;
import script.library.arena;
import script.library.factions;
import script.library.group;
import script.library.meditation;
import script.library.pclib;
import script.library.pet_lib;
import script.library.prose;
import script.library.utils;

/**
 * Publish 14.1 incapacitated-player drag command.
 *
 * This is a narrow Java port of Core3's authoritative command. It deliberately
 * does not reuse the retained NGE corpse drag path: that helper operates on
 * dead corpses, has different permissions and ranges, and moves the corpse
 * directly onto the dragger.
 */
public class drag_incap_player extends script.base_script
{
    private static final String REQUIRED_SKILL =
        "science_medic_injury_speed_02";
    private static final String RANGE_MOD = "healing_ability";
    private static final float BASE_RANGE = 10.0f;
    private static final float RANGE_PER_MOD = 0.2f;
    private static final float MAX_MOVEMENT = 5.0f;
    private static final float MINIMUM_DISTANCE = 0.01f;
    private static final String DRAG_EFFECT =
        "clienteffect/medic_drag.cef";
    private static final String FIXTURE_ROOT =
        "precu.dragIncapacitatedPlayerFixture";

    private static final string_id SID_INVALID_TARGET =
        new string_id("healing_response", "healing_response_a5");
    private static final string_id SID_INVALID_PATIENT_STATE =
        new string_id("healing_response", "healing_response_a7");
    private static final string_id SID_MISSING_ABILITY =
        new string_id("healing_response", "healing_response_a9");
    private static final string_id SID_OUT_OF_RANGE =
        new string_id("healing_response", "healing_response_b1");
    private static final string_id SID_TARGET_BELOW =
        new string_id("healing_response", "healing_response_b2");
    private static final string_id SID_TARGET_ABOVE =
        new string_id("healing_response", "healing_response_b3");
    private static final string_id SID_GROUP_OR_CONSENT =
        new string_id("healing_response", "healing_response_b4");
    private static final string_id SID_DRAG_STARTED =
        new string_id("healing_response", "healing_response_b5");
    private static final string_id SID_NO_ARENA_HELP =
        new string_id("jedi_spam", "no_help_target");
    private static final string_id SID_PVP_NO_HELP =
        new string_id("healing", "pvp_no_help");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("container_error_message", "container18_prose");
    private static final string_id SID_DRAG_INTO_STRUCTURE =
        new string_id("error_message", "corpse_drag_into");
    private static final string_id SID_DRAG_INSIDE_STRUCTURE =
        new string_id("error_message", "corpse_drag_inside");
    private static final string_id SID_WRONG_STATE =
        new string_id("error_message", "wrong_state");
    private static final string_id SID_ON_MOUNT =
        new string_id("error_message", "survey_on_mount");
    private static final string_id SID_FLY_DRAG =
        new string_id("base_player", "fly_drag");

    public int cmdDragIncapPlayer(
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
        if (getPosture(self) == POSTURE_PRONE ||
            meditation.isMeditating(self) ||
            getState(self, STATE_SWIMMING) == 1)
        {
            sendSystemMessage(self, SID_WRONG_STATE);
            recordOutcome(self, fixture, "medicStateRejected");
            return SCRIPT_CONTINUE;
        }
        if (pet_lib.isMounted(self))
        {
            sendSystemMessage(self, SID_ON_MOUNT);
            recordOutcome(self, fixture, "mountedRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(target) || !exists(target) ||
            !isPlayer(target) || target == self)
        {
            sendSystemMessage(self, SID_INVALID_TARGET);
            recordOutcome(self, fixture, "targetRejected");
            return SCRIPT_CONTINUE;
        }
        if (!hasSkill(self, REQUIRED_SKILL))
        {
            sendSystemMessage(self, SID_MISSING_ABILITY);
            recordOutcome(self, fixture, "skillRejected");
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(target, arena.VAR_I_AM_DUELING))
        {
            sendSystemMessage(self, SID_NO_ARENA_HELP);
            recordOutcome(self, fixture, "arenaRejected");
            return SCRIPT_CONTINUE;
        }
        if (!pvpCanHelp(self, target) ||
            !factions.pvpDoAllowedHelpCheck(self, target))
        {
            sendSystemMessage(self, SID_PVP_NO_HELP);
            recordOutcome(self, fixture, "pvpRejected");
            return SCRIPT_CONTINUE;
        }
        if (!canSee(self, target))
        {
            prose_package noLineOfSight =
                prose.getPackage(SID_NO_LINE_OF_SIGHT, target);
            sendSystemMessageProse(self, noLineOfSight);
            recordOutcome(self, fixture, "lineOfSightRejected");
            return SCRIPT_CONTINUE;
        }

        location medicLocation = getLocation(self);
        location targetLocation = getLocation(target);
        if (medicLocation == null || targetLocation == null ||
            medicLocation.area == null || targetLocation.area == null ||
            !medicLocation.area.equals(targetLocation.area))
        {
            sendSystemMessage(self, SID_INVALID_TARGET);
            recordOutcome(self, fixture, "zoneRejected");
            return SCRIPT_CONTINUE;
        }

        boolean medicInside = isIdValid(medicLocation.cell);
        boolean targetInside = isIdValid(targetLocation.cell);
        if (medicInside || targetInside)
        {
            sendSystemMessage(
                self,
                medicInside && !targetInside
                    ? SID_DRAG_INTO_STRUCTURE
                    : SID_DRAG_INSIDE_STRUCTURE);
            recordOutcome(self, fixture, "interiorRejected");
            return SCRIPT_CONTINUE;
        }

        int healingAbility = getSkillStatMod(self, RANGE_MOD);
        float maximumRange =
            BASE_RANGE + healingAbility * RANGE_PER_MOD;
        float distance =
            getWorldDistance(medicLocation, targetLocation);
        if (distance < MINIMUM_DISTANCE)
        {
            recordOutcome(self, fixture, "minimumRangeRejected");
            return SCRIPT_CONTINUE;
        }
        if (distance > maximumRange)
        {
            prose_package outOfRange =
                prose.getPackage(SID_OUT_OF_RANGE, (int)maximumRange);
            sendSystemMessageProse(self, outOfRange);
            recordOutcome(self, fixture, "maximumRangeRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isDead(target) && !isIncapacitated(target))
        {
            sendSystemMessage(self, SID_INVALID_PATIENT_STATE);
            recordOutcome(self, fixture, "patientStateRejected");
            return SCRIPT_CONTINUE;
        }
        boolean grouped = group.inSameGroup(self, target);
        boolean consented = pclib.hasConsent(self, target);
        if (!grouped && !consented)
        {
            sendSystemMessage(self, SID_GROUP_OR_CONSENT);
            recordOutcome(self, fixture, "permissionRejected");
            return SCRIPT_CONTINUE;
        }

        float heightDifference =
            medicLocation.y - targetLocation.y;
        if (Math.abs(heightDifference) > maximumRange)
        {
            sendSystemMessage(
                self,
                heightDifference > 0.0f
                    ? SID_TARGET_BELOW
                    : SID_TARGET_ABOVE);
            recordOutcome(self, fixture, "heightRejected");
            return SCRIPT_CONTINUE;
        }

        location destination =
            getDragDestination(
                medicLocation,
                targetLocation,
                distance);
        faceTo(target, self);
        boolean moved = setLocation(target, destination);
        if (!moved)
        {
            recordOutcome(self, fixture, "movementRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id[] observers =
            getObjectsInRange(targetLocation, 20.0f);
        if (observers != null)
        {
            for (obj_id observer : observers)
            {
                if (isPlayer(observer))
                {
                    playClientEffectLoc(
                        observer,
                        DRAG_EFFECT,
                        targetLocation,
                        0.0f);
                }
            }
        }
        showFlyText(target, SID_FLY_DRAG, 1.0f, 255, 0, 0);
        prose_package dragStarted =
            prose.getPackage(SID_DRAG_STARTED, target);
        sendSystemMessageProse(self, dragStarted);
        pvpHelpPerformed(self, target);

        if (fixture)
        {
            location observed = getLocation(target);
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".healingAbility",
                healingAbility);
            setObjVar(
                self,
                FIXTURE_ROOT + ".maximumRangeCentimeters",
                (int)(maximumRange * 100.0f + 0.5f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".preDistanceCentimeters",
                (int)(distance * 100.0f + 0.5f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".postDistanceCentimeters",
                (int)(getWorldDistance(
                    medicLocation,
                    observed) * 100.0f + 0.5f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".movedCentimeters",
                (int)(getDistance(targetLocation, observed) *
                    100.0f + 0.5f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".grouped",
                grouped ? 1 : 0);
            setObjVar(
                self,
                FIXTURE_ROOT + ".consented",
                consented ? 1 : 0);
            setObjVar(
                self,
                FIXTURE_ROOT + ".targetXCentimeters",
                (int)(observed.x * 100.0f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".targetYCentimeters",
                (int)(observed.y * 100.0f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".targetZCentimeters",
                (int)(observed.z * 100.0f));
        }
        recordOutcome(self, fixture, "performed");
        return SCRIPT_CONTINUE;
    }

    private location getDragDestination(
        location medic,
        location patient,
        float distance)
        throws InterruptedException
    {
        if (distance <= MAX_MOVEMENT)
        {
            return new location(
                medic.x,
                getHeightAtLocation(medic.x, medic.z),
                medic.z,
                medic.area,
                null);
        }

        float ratio = MAX_MOVEMENT / distance;
        float x = patient.x + (medic.x - patient.x) * ratio;
        float z = patient.z + (medic.z - patient.z) * ratio;
        return new location(
            x,
            getHeightAtLocation(x, z),
            z,
            medic.area,
            null);
    }

    private float getWorldDistance(
        location first,
        location second)
    {
        float dx = first.x - second.x;
        float dy = first.y - second.y;
        float dz = first.z - second.z;
        return (float)Math.sqrt(
            dx * dx + dy * dy + dz * dz);
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
        int calls = hasObjVar(
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
        if (fixture)
        {
            setObjVar(
                medic,
                FIXTURE_ROOT + ".outcome",
                outcome);
        }
    }
}
