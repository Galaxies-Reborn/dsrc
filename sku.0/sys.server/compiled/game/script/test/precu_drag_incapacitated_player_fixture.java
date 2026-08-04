package script.test;

import script.location;
import script.obj_id;
import script.library.group;
import script.library.pclib;

/**
 * ServerConsole-only, identity-bound fixture for Publish 14.1 player drag.
 *
 * The fixture owns reversible skill, command, modifier, posture, HAM, and
 * location preparation. The two real clients remain responsible for forming
 * their group and for admitting dragIncapacitatedPlayer through the normal
 * toolbar command path.
 */
public class precu_drag_incapacitated_player_fixture extends script.base_script
{
    private static final long MEDIC_OID = 39008597L;
    private static final int MEDIC_STATION_ID = 1001;
    private static final long PATIENT_OID = 44003778L;
    private static final int PATIENT_STATION_ID = 91001;
    private static final String MEDIC_NOVICE =
        "science_medic_novice";
    private static final String MEDIC_INJURY_SPEED_ONE =
        "science_medic_injury_speed_01";
    private static final String MEDIC_INJURY_SPEED_TWO =
        "science_medic_injury_speed_02";
    private static final String COMMAND =
        "dragIncapacitatedPlayer";
    private static final String RANGE_MOD = "healing_ability";
    private static final String INCAP_RESISTANCE_MOD =
        "resistance_incapacitation";
    private static final int HEALING_ABILITY = 10;
    private static final String ROOT =
        "precu.dragIncapacitatedPlayerFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PEER = ROOT + ".peer";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_LOCATION =
        ROOT + ".originalLocation";
    private static final String ORIGINAL_POSTURE =
        ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String ORIGINAL_HEALTH =
        ROOT + ".originalHealth";
    private static final String ORIGINAL_HEALTH_REGEN =
        ROOT + ".originalHealthRegen";
    private static final String ORIGINAL_ACTION =
        ROOT + ".originalAction";
    private static final String ORIGINAL_MIND =
        ROOT + ".originalMind";
    private static final String ORIGINAL_INCAP_RESISTANCE =
        ROOT + ".originalIncapResistance";
    private static final String APPLIED_INCAP_RESISTANCE_DELTA =
        ROOT + ".appliedIncapResistanceDelta";
    private static final String ORIGINAL_NOVICE =
        ROOT + ".originalNovice";
    private static final String ORIGINAL_SPEED_ONE =
        ROOT + ".originalSpeedOne";
    private static final String ORIGINAL_SPEED_TWO =
        ROOT + ".originalSpeedTwo";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalCommand";
    private static final String ORIGINAL_RANGE_MOD =
        ROOT + ".originalRangeMod";
    private static final String APPLIED_RANGE_MOD_DELTA =
        ROOT + ".appliedRangeModDelta";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String USAGE =
        "usage: inspect|recover|prepare|status|cleanup " +
        "39008597 44003778 <32-hex-lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args =
            params == null
                ? new String[0]
                : params.trim().split("[ ]+");
        if (args.length != 4 ||
            !args[3].matches("[a-f0-9]{32}"))
        {
            return USAGE;
        }

        long medicValue;
        long patientValue;
        try
        {
            medicValue = Long.parseLong(args[1]);
            patientValue = Long.parseLong(args[2]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (medicValue != MEDIC_OID ||
            patientValue != PATIENT_OID ||
            medicValue == patientValue)
        {
            return "error=identityNotAllowed";
        }

        obj_id medic = obj_id.getObjId(medicValue);
        obj_id patient = obj_id.getObjId(patientValue);
        String validation =
            validatePlayer(
                medic,
                MEDIC_STATION_ID,
                "medic");
        if (validation != null)
        {
            return validation;
        }
        validation =
            validatePlayer(
                patient,
                PATIENT_STATION_ID,
                "patient");
        if (validation != null)
        {
            return validation;
        }

        if (args[0].equalsIgnoreCase("inspect"))
        {
            return "action=inspect " +
                buildStatus(medic, patient, args[3]);
        }
        if (args[0].equalsIgnoreCase("recover"))
        {
            return recoverPartial(
                medic,
                patient,
                args[3]);
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(
                medic,
                patient,
                args[3]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            validation =
                validateOwnership(
                    medic,
                    patient,
                    args[3],
                    false);
            return validation == null
                ? "action=status " +
                    buildStatus(
                        medic,
                        patient,
                        args[3])
                : validation;
        }
        if (args[0].equalsIgnoreCase("cleanup"))
        {
            return cleanup(
                medic,
                patient,
                args[3]);
        }
        return USAGE;
    }

    private String prepare(
        obj_id medic,
        obj_id patient,
        String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(
                medic,
                patient,
                lifecycle,
                true);
        if (ownership == null)
        {
            if (getIntObjVar(medic, PREPARED) != 1 ||
                getIntObjVar(patient, PREPARED) != 1)
            {
                return "error=fixturePartial";
            }
            if (!reassertPreparedState(medic, patient))
            {
                return "error=fixtureReassertionFailed " +
                    buildStatus(
                        medic,
                        patient,
                        lifecycle);
            }
            return "action=prepare resumed=true " +
                buildStatus(
                    medic,
                    patient,
                    lifecycle);
        }
        if (!"fixtureAbsent".equals(ownership))
        {
            return ownership;
        }

        location medicOriginal = getLocation(medic);
        location patientOriginal = getLocation(patient);
        if (medicOriginal == null ||
            patientOriginal == null)
        {
            return "error=locationUnavailable";
        }
        snapshotPlayer(
            medic,
            patient,
            lifecycle,
            medicOriginal);
        snapshotPlayer(
            patient,
            medic,
            lifecycle,
            patientOriginal);
        snapshotMedic(medic);
        resetTelemetry(medic);

        boolean noviceReady =
            hasSkill(medic, MEDIC_NOVICE) ||
            grantSkill(medic, MEDIC_NOVICE);
        boolean speedOneReady =
            noviceReady &&
            (hasSkill(medic, MEDIC_INJURY_SPEED_ONE) ||
                grantSkill(
                    medic,
                    MEDIC_INJURY_SPEED_ONE));
        boolean speedTwoReady =
            speedOneReady &&
            (hasSkill(medic, MEDIC_INJURY_SPEED_TWO) ||
                grantSkill(
                    medic,
                    MEDIC_INJURY_SPEED_TWO));
        boolean commandReady =
            hasCommand(medic, COMMAND) ||
            grantCommand(medic, COMMAND);
        if (!noviceReady || !speedOneReady ||
            !speedTwoReady || !commandReady ||
            !hasSkill(medic, MEDIC_INJURY_SPEED_TWO) ||
            !hasCommand(medic, COMMAND))
        {
            boolean restored = restore(medic, patient);
            return "error=skillPreparationFailed" +
                " novice=" + noviceReady +
                " speedOne=" + speedOneReady +
                " speedTwo=" + speedTwoReady +
                " command=" + commandReady +
                " restored=" + restored;
        }

        int modifierDelta =
            HEALING_ABILITY -
                getSkillStatMod(medic, RANGE_MOD);
        setObjVar(
            medic,
            APPLIED_RANGE_MOD_DELTA,
            modifierDelta);
        if (modifierDelta != 0 &&
            !applySkillStatisticModifier(
                medic,
                RANGE_MOD,
                modifierDelta))
        {
            boolean restored = restore(medic, patient);
            return "error=rangeModifierPreparationFailed" +
                " restored=" + restored;
        }

        int incapResistanceDelta =
            -getSkillStatMod(
                patient,
                INCAP_RESISTANCE_MOD);
        setObjVar(
            patient,
            APPLIED_INCAP_RESISTANCE_DELTA,
            incapResistanceDelta);
        if (incapResistanceDelta != 0 &&
            !applySkillStatisticModifier(
                patient,
                INCAP_RESISTANCE_MOD,
                incapResistanceDelta))
        {
            boolean restored = restore(medic, patient);
            return "error=incapResistancePreparationFailed" +
                " restored=" + restored;
        }

        if (!reassertPreparedState(medic, patient))
        {
            String failedStatus =
                buildStatus(
                    medic,
                    patient,
                    lifecycle).replace(' ', '_');
            boolean restored = restore(medic, patient);
            return "error=dragPreparationFailed" +
                " restored=" + restored +
                " failedStatus=" + failedStatus;
        }
        setObjVar(medic, PREPARED, 1);
        setObjVar(patient, PREPARED, 1);
        return "action=prepare resumed=false " +
            buildStatus(medic, patient, lifecycle);
    }

    private String cleanup(
        obj_id medic,
        obj_id patient,
        String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(
                medic,
                patient,
                lifecycle,
                true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanup alreadyClean=true" +
                " restored=true lifecycle=" + lifecycle;
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(medic, true) ||
            !hasCompleteSnapshot(patient, false))
        {
            return "error=incompleteSnapshot";
        }

        boolean restored = restore(medic, patient);
        return restored
            ? "action=cleanup alreadyClean=false" +
                " restored=true lifecycle=" + lifecycle
            : "error=fixtureRestoreFailed " +
                buildStatus(
                    medic,
                    patient,
                    lifecycle);
    }

    private String recoverPartial(
        obj_id medic,
        obj_id patient,
        String lifecycle)
        throws InterruptedException
    {
        boolean medicRoot = hasObjVar(medic, ROOT);
        boolean patientRoot = hasObjVar(patient, ROOT);
        if (!medicRoot && !patientRoot)
        {
            return "action=recover alreadyClean=true" +
                " lifecycle=" + lifecycle;
        }
        if ((medicRoot &&
                hasObjVar(medic, LIFECYCLE) &&
                !lifecycle.equals(
                    getStringObjVar(medic, LIFECYCLE))) ||
            (patientRoot &&
                hasObjVar(patient, LIFECYCLE) &&
                !lifecycle.equals(
                    getStringObjVar(patient, LIFECYCLE))))
        {
            return "error=fixtureOwnershipMismatch";
        }

        boolean medicComplete =
            medicRoot && hasCompleteSnapshot(medic, true);
        boolean patientComplete =
            patientRoot &&
                hasCompleteSnapshot(patient, false);
        boolean restored =
            medicComplete && patientComplete &&
                restore(medic, patient);
        if (!restored)
        {
            if (medicRoot)
            {
                removeObjVar(medic, ROOT);
            }
            if (patientRoot)
            {
                removeObjVar(patient, ROOT);
            }
        }
        return "action=recover alreadyClean=false" +
            " medicSnapshotComplete=" + medicComplete +
            " patientSnapshotComplete=" + patientComplete +
            " restored=" + restored +
            " cleared=" +
                (!hasObjVar(medic, ROOT) &&
                    !hasObjVar(patient, ROOT)) +
            " lifecycle=" + lifecycle;
    }

    private void snapshotPlayer(
        obj_id player,
        obj_id peer,
        String lifecycle,
        location original)
        throws InterruptedException
    {
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PEER, peer);
        setObjVar(
            player,
            ORIGINAL_LOCATION,
            original);
        setObjVar(
            player,
            ORIGINAL_POSTURE,
            getPosture(player));
        setObjVar(
            player,
            ORIGINAL_LOCOMOTION,
            getLocomotion(player));
        setObjVar(
            player,
            ORIGINAL_HEALTH,
            getAttrib(player, HEALTH));
        setObjVar(
            player,
            ORIGINAL_HEALTH_REGEN,
            getHealthRegenRate(player));
        setObjVar(
            player,
            ORIGINAL_ACTION,
            getAttrib(player, ACTION));
        setObjVar(
            player,
            ORIGINAL_MIND,
            getAttrib(player, MIND));
        setObjVar(
            player,
            ORIGINAL_INCAP_RESISTANCE,
            getSkillStatMod(
                player,
                INCAP_RESISTANCE_MOD));
        setObjVar(
            player,
            APPLIED_INCAP_RESISTANCE_DELTA,
            0);
        setObjVar(player, PREPARED, 0);
    }

    private void snapshotMedic(obj_id medic)
        throws InterruptedException
    {
        setObjVar(
            medic,
            ORIGINAL_NOVICE,
            hasSkill(medic, MEDIC_NOVICE) ? 1 : 0);
        setObjVar(
            medic,
            ORIGINAL_SPEED_ONE,
            hasSkill(
                medic,
                MEDIC_INJURY_SPEED_ONE) ? 1 : 0);
        setObjVar(
            medic,
            ORIGINAL_SPEED_TWO,
            hasSkill(
                medic,
                MEDIC_INJURY_SPEED_TWO) ? 1 : 0);
        setObjVar(
            medic,
            ORIGINAL_COMMAND,
            hasCommand(medic, COMMAND) ? 1 : 0);
        setObjVar(
            medic,
            ORIGINAL_RANGE_MOD,
            getSkillStatMod(medic, RANGE_MOD));
        setObjVar(
            medic,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(medic, "medical"));
        setObjVar(
            medic,
            APPLIED_RANGE_MOD_DELTA,
            0);
    }

    private boolean reassertPreparedState(
        obj_id medic,
        obj_id patient)
        throws InterruptedException
    {
        stopCombat(medic);
        stopCombat(patient);
        setCombatTarget(medic, obj_id.NULL_ID);
        setCombatTarget(patient, obj_id.NULL_ID);
        setRegenRate(medic, HEALTH, 0.0f);
        setRegenRate(patient, HEALTH, 0.0f);

        float medicX = 3500.0f;
        float medicZ = -4800.0f;
        // Core3 drag authority uses raw world-position distance rather than
        // SWGSource's collision-radius-adjusted object distance.
        float patientX = 3509.0f;
        float patientZ = -4800.0f;
        location medicDestination =
            new location(
                medicX,
                getHeightAtLocation(medicX, medicZ),
                medicZ,
                "tatooine",
                null);
        location patientDestination =
            new location(
                patientX,
                getHeightAtLocation(patientX, patientZ),
                patientZ,
                "tatooine",
                null);
        boolean medicMoved =
            setLocation(medic, medicDestination);
        boolean patientMoved =
            setLocation(patient, patientDestination);
        boolean medicLocomotion =
            setLocomotion(
                medic,
                LOCOMOTION_STANDING);
        boolean medicPosture =
            setPostureClientImmediate(
                medic,
                POSTURE_UPRIGHT);
        boolean patientPosture =
            setPostureClientImmediate(
                patient,
                POSTURE_INCAPACITATED);
        location observedMedic = getLocation(medic);
        location observedPatient = getLocation(patient);
        return medicMoved && patientMoved &&
            medicLocomotion && medicPosture &&
            patientPosture &&
            getHealthRegenRate(medic) == 0.0f &&
            getHealthRegenRate(patient) == 0.0f &&
            getSkillStatMod(
                patient,
                INCAP_RESISTANCE_MOD) == 0 &&
            hasSkill(
                medic,
                MEDIC_INJURY_SPEED_TWO) &&
            hasCommand(medic, COMMAND) &&
            getSkillStatMod(medic, RANGE_MOD) ==
                HEALING_ABILITY &&
            getPosture(medic) == POSTURE_UPRIGHT &&
            getLocomotion(medic) ==
                LOCOMOTION_STANDING &&
            isIncapacitated(patient) &&
            getPosture(patient) ==
                POSTURE_INCAPACITATED &&
            observedMedic != null &&
            observedPatient != null &&
            !isIdValid(observedMedic.cell) &&
            !isIdValid(observedPatient.cell) &&
            "tatooine".equals(observedMedic.area) &&
            "tatooine".equals(observedPatient.area) &&
            getWorldDistance(
                observedMedic,
                observedPatient) > 8.9f &&
            getWorldDistance(
                observedMedic,
                observedPatient) < 9.1f;
    }

    private boolean restore(
        obj_id medic,
        obj_id patient)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(medic, true) ||
            !hasCompleteSnapshot(patient, false))
        {
            return false;
        }
        stopCombat(medic);
        stopCombat(patient);
        setCombatTarget(medic, obj_id.NULL_ID);
        setCombatTarget(patient, obj_id.NULL_ID);

        int modifierDelta =
            getIntObjVar(
                medic,
                APPLIED_RANGE_MOD_DELTA);
        boolean modifierRestored =
            modifierDelta == 0 ||
                applySkillStatisticModifier(
                    medic,
                    RANGE_MOD,
                    -modifierDelta);

        if (getIntObjVar(medic, ORIGINAL_SPEED_TWO) == 0 &&
            hasSkill(medic, MEDIC_INJURY_SPEED_TWO))
        {
            revokeSkill(
                medic,
                MEDIC_INJURY_SPEED_TWO);
        }
        if (getIntObjVar(medic, ORIGINAL_SPEED_ONE) == 0 &&
            hasSkill(medic, MEDIC_INJURY_SPEED_ONE))
        {
            revokeSkill(
                medic,
                MEDIC_INJURY_SPEED_ONE);
        }
        if (getIntObjVar(medic, ORIGINAL_NOVICE) == 0 &&
            hasSkill(medic, MEDIC_NOVICE))
        {
            revokeSkill(medic, MEDIC_NOVICE);
        }

        if (getIntObjVar(medic, ORIGINAL_COMMAND) == 1 &&
            !hasCommand(medic, COMMAND))
        {
            grantCommand(medic, COMMAND);
        }
        else if (
            getIntObjVar(medic, ORIGINAL_COMMAND) == 0 &&
            hasCommand(medic, COMMAND))
        {
            revokeCommand(medic, COMMAND);
        }

        boolean medicRestored =
            restorePlayer(medic);
        boolean patientRestored =
            restorePlayer(patient);
        boolean skillStateRestored =
            hasSkill(medic, MEDIC_NOVICE) ==
                (getIntObjVar(
                    medic,
                    ORIGINAL_NOVICE) == 1) &&
            hasSkill(medic, MEDIC_INJURY_SPEED_ONE) ==
                (getIntObjVar(
                    medic,
                    ORIGINAL_SPEED_ONE) == 1) &&
            hasSkill(medic, MEDIC_INJURY_SPEED_TWO) ==
                (getIntObjVar(
                    medic,
                    ORIGINAL_SPEED_TWO) == 1) &&
            hasCommand(medic, COMMAND) ==
                (getIntObjVar(
                    medic,
                    ORIGINAL_COMMAND) == 1) &&
            getSkillStatMod(medic, RANGE_MOD) ==
                getIntObjVar(
                    medic,
                    ORIGINAL_RANGE_MOD) &&
            getExperiencePoints(medic, "medical") ==
                getIntObjVar(
                    medic,
                    ORIGINAL_MEDICAL_XP);
        boolean restored =
            modifierRestored &&
            medicRestored &&
            patientRestored &&
            skillStateRestored;
        if (!restored)
        {
            return false;
        }

        removeObjVar(medic, ROOT);
        removeObjVar(patient, ROOT);
        return !hasObjVar(medic, ROOT) &&
            !hasObjVar(patient, ROOT);
    }

    private boolean restorePlayer(obj_id player)
        throws InterruptedException
    {
        int incapResistanceDelta =
            getIntObjVar(
                player,
                APPLIED_INCAP_RESISTANCE_DELTA);
        boolean incapResistanceRestored =
            incapResistanceDelta == 0 ||
                applySkillStatisticModifier(
                    player,
                    INCAP_RESISTANCE_MOD,
                    -incapResistanceDelta);
        boolean moved =
            setLocation(
                player,
                getLocationObjVar(
                    player,
                    ORIGINAL_LOCATION));
        boolean locomotionRestored =
            setLocomotion(
                player,
                getIntObjVar(
                    player,
                    ORIGINAL_LOCOMOTION));
        boolean postureRestored =
            setPostureClientImmediate(
                player,
                getIntObjVar(
                    player,
                    ORIGINAL_POSTURE));
        boolean healthRestored =
            setAttribAndVerify(
                player,
                HEALTH,
                getIntObjVar(
                    player,
                    ORIGINAL_HEALTH));
        float originalHealthRegen =
            getFloatObjVar(
                player,
                ORIGINAL_HEALTH_REGEN);
        setRegenRate(
            player,
            HEALTH,
            originalHealthRegen);
        boolean healthRegenRestored =
            getHealthRegenRate(player) ==
                originalHealthRegen;
        boolean actionRestored =
            setAttribAndVerify(
                player,
                ACTION,
                getIntObjVar(
                    player,
                    ORIGINAL_ACTION));
        boolean mindRestored =
            setAttribAndVerify(
                player,
                MIND,
                getIntObjVar(
                    player,
                    ORIGINAL_MIND));
        return incapResistanceRestored &&
            getSkillStatMod(
                player,
                INCAP_RESISTANCE_MOD) ==
                getIntObjVar(
                    player,
                    ORIGINAL_INCAP_RESISTANCE) &&
            moved && locomotionRestored &&
            postureRestored && healthRestored &&
            healthRegenRestored &&
            actionRestored && mindRestored;
    }

    private boolean setAttribAndVerify(
        obj_id player,
        int attribute,
        int value)
        throws InterruptedException
    {
        setAttrib(player, attribute, value);
        return getAttrib(player, attribute) == value;
    }

    private boolean hasCompleteSnapshot(
        obj_id player,
        boolean medic)
        throws InterruptedException
    {
        boolean common =
            hasObjVar(player, LIFECYCLE) &&
            hasObjVar(player, PEER) &&
            hasObjVar(player, ORIGINAL_LOCATION) &&
            hasObjVar(player, ORIGINAL_POSTURE) &&
            hasObjVar(player, ORIGINAL_LOCOMOTION) &&
            hasObjVar(player, ORIGINAL_HEALTH) &&
            hasObjVar(player, ORIGINAL_HEALTH_REGEN) &&
            hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(
                player,
                ORIGINAL_INCAP_RESISTANCE) &&
            hasObjVar(
                player,
                APPLIED_INCAP_RESISTANCE_DELTA);
        if (!medic)
        {
            return common;
        }
        return common &&
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_SPEED_TWO) &&
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_RANGE_MOD) &&
            hasObjVar(
                player,
                APPLIED_RANGE_MOD_DELTA) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP);
    }

    private String validatePlayer(
        obj_id player,
        int stationId,
        String role)
        throws InterruptedException
    {
        if (player == null ||
            player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=" + role + "NotLoaded";
        }
        if (!player.isAuthoritative() ||
            !isPlayer(player))
        {
            return "error=" + role +
                "NotAuthoritative oid=" + player;
        }
        if (getPlayerStationId(player) != stationId)
        {
            return "error=" + role +
                "StationNotAllowed oid=" + player;
        }
        return null;
    }

    private String validateOwnership(
        obj_id medic,
        obj_id patient,
        String lifecycle,
        boolean allowAbsent)
        throws InterruptedException
    {
        boolean medicRoot = hasObjVar(medic, ROOT);
        boolean patientRoot = hasObjVar(patient, ROOT);
        if (!medicRoot && !patientRoot)
        {
            return allowAbsent
                ? "fixtureAbsent"
                : "error=fixtureAbsent";
        }
        if (!medicRoot || !patientRoot ||
            !hasObjVar(medic, LIFECYCLE) ||
            !hasObjVar(patient, LIFECYCLE) ||
            !hasObjVar(medic, PEER) ||
            !hasObjVar(patient, PEER))
        {
            return "error=fixturePartial";
        }
        if (!lifecycle.equals(
                getStringObjVar(medic, LIFECYCLE)) ||
            !lifecycle.equals(
                getStringObjVar(patient, LIFECYCLE)) ||
            getObjIdObjVar(medic, PEER) != patient ||
            getObjIdObjVar(patient, PEER) != medic)
        {
            return "error=fixtureOwnershipMismatch";
        }
        return null;
    }

    private String buildStatus(
        obj_id medic,
        obj_id patient,
        String lifecycle)
        throws InterruptedException
    {
        location medicLocation = getLocation(medic);
        location patientLocation = getLocation(patient);
        return "lifecycle=" + lifecycle +
            " medicFixtureLifecycle=" +
                getStringOrNone(medic, LIFECYCLE) +
            " patientFixtureLifecycle=" +
                getStringOrNone(patient, LIFECYCLE) +
            " prepared=" +
                (getIntObjVar(medic, PREPARED) == 1 &&
                    getIntObjVar(patient, PREPARED) == 1) +
            " distanceCentimeters=" +
                (int)(getWorldDistance(
                    medicLocation,
                    patientLocation) * 100.0f + 0.5f) +
            " medicScene=" +
                sceneOrNone(medicLocation) +
            " patientScene=" +
                sceneOrNone(patientLocation) +
            " medicOutdoors=" +
                (medicLocation != null &&
                    !isIdValid(medicLocation.cell)) +
            " patientOutdoors=" +
                (patientLocation != null &&
                    !isIdValid(patientLocation.cell)) +
            " medicPosture=" + getPosture(medic) +
            " medicLocomotion=" +
                getLocomotion(medic) +
            " patientPosture=" +
                getPosture(patient) +
            " patientLocomotion=" +
                getLocomotion(patient) +
            " patientIncapacitated=" +
                isIncapacitated(patient) +
            " medicNovice=" +
                hasSkill(medic, MEDIC_NOVICE) +
            " medicSpeedOne=" +
                hasSkill(
                    medic,
                    MEDIC_INJURY_SPEED_ONE) +
            " medicSpeedTwo=" +
                hasSkill(
                    medic,
                    MEDIC_INJURY_SPEED_TWO) +
            " command=" +
                hasCommand(medic, COMMAND) +
            " healingAbility=" +
                getSkillStatMod(medic, RANGE_MOD) +
            " patientIncapResistance=" +
                getSkillStatMod(
                    patient,
                    INCAP_RESISTANCE_MOD) +
            " sameGroup=" +
                group.inSameGroup(medic, patient) +
            " hasConsent=" +
                pclib.hasConsent(medic, patient) +
            " medicHealth=" +
                getAttrib(medic, HEALTH) +
            " medicAction=" +
                getAttrib(medic, ACTION) +
            " medicMind=" +
                getAttrib(medic, MIND) +
            " medicalXp=" +
                getExperiencePoints(medic, "medical") +
            " patientHealth=" +
                getAttrib(patient, HEALTH) +
            " patientAction=" +
                getAttrib(patient, ACTION) +
            " patientMind=" +
                getAttrib(patient, MIND) +
            " patientXCentimeters=" +
                coordinateCentimeters(
                    patientLocation,
                    "x") +
            " patientYCentimeters=" +
                coordinateCentimeters(
                    patientLocation,
                    "y") +
            " patientZCentimeters=" +
                coordinateCentimeters(
                    patientLocation,
                    "z") +
            " handlerCalls=" +
                getIntOrZero(
                    medic,
                    ROOT + ".handlerCalls") +
            " handlerEntered=" +
                getIntOrZero(
                    medic,
                    ROOT + ".handlerEntered") +
            " handlerTarget=" +
                getStringOrNone(
                    medic,
                    ROOT + ".target") +
            " handlerHealingAbility=" +
                getIntOrZero(
                    medic,
                    ROOT + ".healingAbility") +
            " maximumRangeCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".maximumRangeCentimeters") +
            " preDistanceCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".preDistanceCentimeters") +
            " postDistanceCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".postDistanceCentimeters") +
            " movedCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT + ".movedCentimeters") +
            " handlerGrouped=" +
                getIntOrZero(
                    medic,
                    ROOT + ".grouped") +
            " handlerConsented=" +
                getIntOrZero(
                    medic,
                    ROOT + ".consented") +
            " handlerTargetXCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".targetXCentimeters") +
            " handlerTargetYCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".targetYCentimeters") +
            " handlerTargetZCentimeters=" +
                getIntOrZero(
                    medic,
                    ROOT +
                        ".targetZCentimeters") +
            " outcome=" +
                getStringOrNone(
                    medic,
                    ROOT + ".outcome");
    }

    private String sceneOrNone(location value)
    {
        return value == null || value.area == null
            ? "none"
            : value.area;
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

    private int coordinateCentimeters(
        location value,
        String axis)
    {
        if (value == null)
        {
            return 0;
        }
        if ("x".equals(axis))
        {
            return (int)(value.x * 100.0f);
        }
        if ("y".equals(axis))
        {
            return (int)(value.y * 100.0f);
        }
        return (int)(value.z * 100.0f);
    }

    private int getIntOrZero(
        obj_id player,
        String name)
        throws InterruptedException
    {
        return hasObjVar(player, name)
            ? getIntObjVar(player, name)
            : 0;
    }

    private String getStringOrNone(
        obj_id player,
        String name)
        throws InterruptedException
    {
        return hasObjVar(player, name)
            ? getStringObjVar(player, name)
            : "none";
    }

    private void resetTelemetry(obj_id medic)
        throws InterruptedException
    {
        String[] leaves =
        {
            ROOT + ".handlerCalls",
            ROOT + ".handlerEntered",
            ROOT + ".target",
            ROOT + ".healingAbility",
            ROOT + ".maximumRangeCentimeters",
            ROOT + ".preDistanceCentimeters",
            ROOT + ".postDistanceCentimeters",
            ROOT + ".movedCentimeters",
            ROOT + ".grouped",
            ROOT + ".consented",
            ROOT + ".targetXCentimeters",
            ROOT + ".targetYCentimeters",
            ROOT + ".targetZCentimeters",
            ROOT + ".outcome"
        };
        for (String leaf : leaves)
        {
            if (hasObjVar(medic, leaf))
            {
                removeObjVar(medic, leaf);
            }
        }
    }
}
