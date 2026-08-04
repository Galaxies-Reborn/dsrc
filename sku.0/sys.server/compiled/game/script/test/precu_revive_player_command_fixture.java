package script.test;

import script.attrib_mod;
import script.location;
import script.obj_id;
import script.library.consumable;
import script.library.group;
import script.library.healing;
import script.library.pclib;
import script.library.skill;
import script.library.utils;

/**
 * Identity-bound, two-client fixture for Publish 14.1 Revive Player.
 *
 * The fixture owns reversible skills, a deterministic two-charge revive pack,
 * primary-pool damage/wounds, death eligibility, location, and player state.
 * The clients own group formation and production command admission.
 */
public class precu_revive_player_command_fixture
    extends script.base_script
{
    private static final long MEDIC_OID = 39008597L;
    private static final int MEDIC_STATION_ID = 1001;
    private static final long PATIENT_OID = 44003778L;
    private static final int PATIENT_STATION_ID = 91001;
    private static final String COMMAND = "revivePlayer";
    private static final String ROOT =
        "precu.revivePlayerCommandFixture";
    private static final String LIFECYCLE =
        ROOT + ".lifecycle";
    private static final String PEER =
        ROOT + ".peer";
    private static final String PREPARED =
        ROOT + ".prepared";
    private static final String ITEM =
        ROOT + ".item";
    private static final String ORIGINAL_LOCATION =
        ROOT + ".originalLocation";
    private static final String ORIGINAL_POSTURE =
        ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String ORIGINAL_ATTRIBUTES =
        ROOT + ".originalAttributes";
    private static final String ORIGINAL_WOUNDS =
        ROOT + ".originalWounds";
    private static final String ORIGINAL_REGEN =
        ROOT + ".originalRegen";
    private static final String ORIGINAL_MODIFIER_COUNT =
        ROOT + ".originalModifierCount";
    private static final String ORIGINAL_MODIFIERS =
        ROOT + ".originalModifiers";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String BEFORE_MEDIC_MIND =
        ROOT + ".beforeMedicMind";
    private static final String BEFORE_MEDICAL_XP =
        ROOT + ".beforeMedicalXp";
    private static final String BEFORE_CHARGES =
        ROOT + ".beforeCharges";
    private static final String EXPECTED_MIND_COST =
        ROOT + ".fixtureExpectedMindCost";
    private static final int DAMAGE_POWER = 100;
    private static final int WOUND_POWER = 40;
    private static final int[] PRIMARY =
    {
        HEALTH,
        ACTION,
        MIND
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
    private static final String MEDICINE_TEMPLATE =
        "object/tangible/medicine/medpack_revive.iff";
    private static final String[] SKILLS =
    {
        "science_medic_novice",
        "science_medic_injury_01",
        "science_medic_injury_speed_01",
        "science_medic_ability_01",
        "science_medic_crafting_01",
        "science_medic_injury_02",
        "science_medic_injury_speed_02",
        "science_medic_ability_02",
        "science_medic_crafting_02",
        "science_medic_injury_03",
        "science_medic_injury_speed_03",
        "science_medic_ability_03",
        "science_medic_crafting_03",
        "science_medic_injury_04",
        "science_medic_injury_speed_04",
        "science_medic_ability_04",
        "science_medic_crafting_04",
        "science_medic_master",
        "science_doctor_novice",
        "science_doctor_wound_01",
        "science_doctor_wound_02",
        "science_doctor_wound_03",
        "science_doctor_wound_04"
    };
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
            return recover(
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
            return getIntObjVar(medic, PREPARED) == 1 &&
                getIntObjVar(patient, PREPARED) == 1
                    ? "action=prepare resumed=true " +
                        buildStatus(
                            medic,
                            patient,
                            lifecycle)
                    : "error=fixturePartial";
        }
        if (!"fixtureAbsent".equals(ownership))
        {
            return ownership;
        }
        if (isDead(medic) || isDead(patient) ||
            isIncapacitated(medic) ||
            isIncapacitated(patient))
        {
            return "error=unsafeInitialPosture";
        }
        if (!group.inSameGroup(medic, patient))
        {
            return "error=groupRequired";
        }
        if (hasAnyFixtureSkill(medic) ||
            hasCommand(medic, COMMAND))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        snapshotPlayer(
            medic,
            patient,
            lifecycle);
        snapshotPlayer(
            patient,
            medic,
            lifecycle);
        setObjVar(
            medic,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(medic, "medical"));
        setObjVar(
            medic,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(medic));
        resetTelemetry(medic);

        if (!grantSkills(medic) ||
            !hasCommand(medic, COMMAND))
        {
            boolean restored =
                restore(medic, patient);
            return "error=skillPreparationFailed" +
                " restored=" + restored;
        }

        stopCombat(medic);
        stopCombat(patient);
        setCombatTarget(medic, obj_id.NULL_ID);
        setCombatTarget(patient, obj_id.NULL_ID);
        setRegenRate(medic, MIND, 0.0f);
        for (int attribute : PRIMARY)
        {
            setRegenRate(patient, attribute, 0.0f);
        }
        if (!removeAllAttribModifiers(patient))
        {
            boolean restored =
                restore(medic, patient);
            return "error=modifierPreparationFailed" +
                " restored=" + restored;
        }

        float medicX = 3500.0f;
        float medicZ = -4800.0f;
        float patientX = 3505.0f;
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
        boolean moved =
            setLocation(medic, medicDestination) &&
                setLocation(patient, patientDestination);
        boolean postureReady =
            setLocomotion(
                medic,
                LOCOMOTION_STANDING) &&
            setPostureClientImmediate(
                medic,
                POSTURE_UPRIGHT) &&
            setLocomotion(
                patient,
                LOCOMOTION_STANDING) &&
            setPostureClientImmediate(
                patient,
                POSTURE_UPRIGHT);
        if (!moved || !postureReady)
        {
            boolean restored =
                restore(medic, patient);
            return "error=locationPreparationFailed" +
                " restored=" + restored;
        }

        for (int attribute : PRIMARY)
        {
            if (!setWoundExact(
                    patient,
                    attribute,
                    WOUND_POWER))
            {
                boolean restored =
                    restore(medic, patient);
                return "error=woundPreparationFailed" +
                    " restored=" + restored;
            }
        }
        setAttrib(medic, MIND, getMaxAttrib(medic, MIND));
        setAttrib(patient, HEALTH, 1);
        setAttrib(patient, ACTION, 100);
        setAttrib(patient, MIND, 50);

        obj_id inventory =
            utils.getInventoryContainer(medic);
        obj_id pack =
            isIdValid(inventory)
                ? createObject(
                    MEDICINE_TEMPLATE,
                    inventory,
                    "")
                : obj_id.NULL_ID;
        if (!isIdValid(pack))
        {
            boolean restored =
                restore(medic, patient);
            return "error=packCreationFailed" +
                " restored=" + restored;
        }
        setObjVar(medic, ITEM, pack);
        setObjVar(
            pack,
            consumable.VAR_CONSUMABLE_MEDICINE,
            true);
        setObjVar(
            pack,
            consumable.VAR_SKILL_MOD_REQUIRED,
            new String[] { "healing_ability" });
        setObjVar(
            pack,
            consumable.VAR_SKILL_MOD_MIN,
            new int[] { 0 });
        attrib_mod[] modifiers =
            new attrib_mod[6];
        for (int index = 0;
            index < PRIMARY.length;
            ++index)
        {
            modifiers[index * 2] =
                utils.createHealWoundAttribMod(
                    PRIMARY[index],
                    WOUND_POWER);
            modifiers[index * 2 + 1] =
                utils.createHealDamageAttribMod(
                    PRIMARY[index],
                    DAMAGE_POWER);
        }
        setObjVar(
            pack,
            consumable.VAR_CONSUMABLE_MODS,
            modifiers);
        setCount(pack, 2);

        setObjVar(
            patient,
            pclib.VAR_DEATHBLOW_KILLER,
            patient);
        setObjVar(
            patient,
            pclib.VAR_DEATHBLOW_STAMP,
            getGameTime());
        if (!setPostureClientImmediate(
                patient,
                POSTURE_DEAD))
        {
            boolean restored =
                restore(medic, patient);
            return "error=deathPreparationFailed" +
                " restored=" + restored;
        }
        setAttrib(patient, HEALTH, 1);
        setAttrib(patient, ACTION, 100);
        setAttrib(patient, MIND, 50);

        setObjVar(
            medic,
            EXPECTED_MIND_COST,
            healing.getMedicalMindCost(
                medic,
                healing.COST_MIND_REVIVE));
        setObjVar(
            medic,
            BEFORE_MEDIC_MIND,
            getAttrib(medic, MIND));
        setObjVar(
            medic,
            BEFORE_MEDICAL_XP,
            getExperiencePoints(medic, "medical"));
        setObjVar(
            medic,
            BEFORE_CHARGES,
            getCount(pack));
        setObjVar(medic, PREPARED, 1);
        setObjVar(patient, PREPARED, 1);

        if (!isDead(patient) ||
            !hasObjVar(
                patient,
                pclib.VAR_BEEN_COUPDEGRACED) ||
            getCount(pack) != 2 ||
            getDistance(medic, patient) > 7.0f)
        {
            String failed =
                buildStatus(
                    medic,
                    patient,
                    lifecycle).replace(' ', '_');
            boolean restored =
                restore(medic, patient);
            return "error=revivePreparationFailed" +
                " restored=" + restored +
                " failedStatus=" + failed;
        }
        return "action=prepare resumed=false " +
            buildStatus(
                medic,
                patient,
                lifecycle);
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
                " restored=true lifecycle=" +
                lifecycle;
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
        boolean restored =
            restore(medic, patient);
        return restored
            ? "action=cleanup alreadyClean=false" +
                " restored=true lifecycle=" +
                    lifecycle
            : "error=fixtureRestoreFailed " +
                buildStatus(
                    medic,
                    patient,
                    lifecycle);
    }

    private String recover(
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
                    getStringObjVar(
                        medic,
                        LIFECYCLE))) ||
            (patientRoot &&
                hasObjVar(patient, LIFECYCLE) &&
                !lifecycle.equals(
                    getStringObjVar(
                        patient,
                        LIFECYCLE))))
        {
            return "error=fixtureOwnershipMismatch";
        }
        boolean complete =
            medicRoot && patientRoot &&
            hasCompleteSnapshot(medic, true) &&
            hasCompleteSnapshot(patient, false);
        boolean restored =
            complete && restore(medic, patient);
        if (!restored)
        {
            emergencyClear(medic, patient);
        }
        return "action=recover alreadyClean=false" +
            " snapshotComplete=" + complete +
            " restored=" + restored +
            " cleared=" +
                (!hasObjVar(medic, ROOT) &&
                    !hasObjVar(patient, ROOT)) +
            " lifecycle=" + lifecycle;
    }

    private void snapshotPlayer(
        obj_id player,
        obj_id peer,
        String lifecycle)
        throws InterruptedException
    {
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PEER, peer);
        setObjVar(
            player,
            ORIGINAL_LOCATION,
            getLocation(player));
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
            ORIGINAL_ATTRIBUTES,
            readAttributes(player));
        setObjVar(
            player,
            ORIGINAL_WOUNDS,
            readWounds(player));
        setObjVar(
            player,
            ORIGINAL_REGEN,
            readRegen(player));
        attrib_mod[] modifiers =
            getAllAttribModifiers(player);
        int count =
            modifiers == null
                ? 0
                : modifiers.length;
        setObjVar(
            player,
            ORIGINAL_MODIFIER_COUNT,
            count);
        if (count > 0)
        {
            setObjVar(
                player,
                ORIGINAL_MODIFIERS,
                modifiers);
        }
        setObjVar(player, PREPARED, 0);
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

        if (hasObjVar(medic, ITEM))
        {
            obj_id pack =
                getObjIdObjVar(medic, ITEM);
            if (isIdValid(pack) &&
                pack.isLoaded())
            {
                destroyObject(pack);
            }
        }
        if (isDead(patient))
        {
            pclib.resurrectPlayer(patient);
        }
        removeObjVar(
            patient,
            pclib.VAR_BEEN_COUPDEGRACED);
        utils.removeScriptVar(
            patient,
            pclib.VAR_SUI_CLONE);
        utils.removeScriptVar(
            patient,
            pclib.VAR_REVIVE_OPTIONS);

        removeAllAttribModifiers(patient);
        int originalModifierCount =
            getIntObjVar(
                patient,
                ORIGINAL_MODIFIER_COUNT);
        boolean modifiersRestored =
            originalModifierCount == 0;
        if (originalModifierCount > 0 &&
            hasObjVar(patient, ORIGINAL_MODIFIERS))
        {
            attrib_mod[] originals =
                getAttribModArrayObjVar(
                    patient,
                    ORIGINAL_MODIFIERS);
            modifiersRestored =
                originals != null &&
                originals.length ==
                    originalModifierCount &&
                addAttribModifiers(
                    patient,
                    originals);
        }

        boolean medicRestored =
            restorePlayer(medic);
        boolean patientRestored =
            restorePlayer(patient);
        int originalXp =
            getIntObjVar(
                medic,
                ORIGINAL_MEDICAL_XP);
        int currentXp =
            getExperiencePoints(medic, "medical");
        if (currentXp != originalXp)
        {
            grantExperiencePoints(
                medic,
                "medical",
                originalXp - currentXp);
        }
        revokeSkills(medic);
        boolean skillStateRestored =
            !hasAnyFixtureSkill(medic) &&
            !hasCommand(medic, COMMAND) &&
            skill.getAvailableSkillPoints(medic) ==
                getIntObjVar(
                    medic,
                    ORIGINAL_POINTS);
        boolean restored =
            modifiersRestored &&
            medicRestored &&
            patientRestored &&
            skillStateRestored &&
            getExperiencePoints(medic, "medical") ==
                originalXp &&
            !isDead(patient) &&
            !hasObjVar(
                patient,
                pclib.VAR_BEEN_COUPDEGRACED);
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
        boolean moved =
            setLocation(
                player,
                getLocationObjVar(
                    player,
                    ORIGINAL_LOCATION));
        boolean locomotion =
            setLocomotion(
                player,
                getIntObjVar(
                    player,
                    ORIGINAL_LOCOMOTION));
        boolean posture =
            setPostureClientImmediate(
                player,
                getIntObjVar(
                    player,
                    ORIGINAL_POSTURE));
        int[] wounds =
            getIntArrayObjVar(
                player,
                ORIGINAL_WOUNDS);
        boolean woundsRestored =
            wounds != null &&
                wounds.length == ATTRIBUTES.length;
        if (woundsRestored)
        {
            for (int index = 0;
                index < ATTRIBUTES.length;
                ++index)
            {
                woundsRestored =
                    setWoundExact(
                        player,
                        ATTRIBUTES[index],
                        wounds[index]) &&
                    woundsRestored;
            }
        }
        int[] values =
            getIntArrayObjVar(
                player,
                ORIGINAL_ATTRIBUTES);
        boolean attributesRestored =
            values != null &&
                values.length == ATTRIBUTES.length;
        if (attributesRestored)
        {
            for (int index = 0;
                index < ATTRIBUTES.length;
                ++index)
            {
                setAttrib(
                    player,
                    ATTRIBUTES[index],
                    values[index]);
                attributesRestored =
                    getAttrib(
                        player,
                        ATTRIBUTES[index]) ==
                        values[index] &&
                    attributesRestored;
            }
        }
        float[] regen =
            getFloatArrayObjVar(
                player,
                ORIGINAL_REGEN);
        boolean regenRestored =
            regen != null &&
                regen.length == PRIMARY.length;
        if (regenRestored)
        {
            for (int index = 0;
                index < PRIMARY.length;
                ++index)
            {
                setRegenRate(
                    player,
                    PRIMARY[index],
                    regen[index]);
            }
        }
        return moved && locomotion && posture &&
            woundsRestored && attributesRestored &&
            regenRestored;
    }

    private void emergencyClear(
        obj_id medic,
        obj_id patient)
        throws InterruptedException
    {
        if (isDead(patient))
        {
            pclib.resurrectPlayer(patient);
            setPostureClientImmediate(
                patient,
                POSTURE_UPRIGHT);
        }
        removeObjVar(
            patient,
            pclib.VAR_BEEN_COUPDEGRACED);
        if (hasObjVar(medic, ROOT))
        {
            removeObjVar(medic, ROOT);
        }
        if (hasObjVar(patient, ROOT))
        {
            removeObjVar(patient, ROOT);
        }
    }

    private boolean grantSkills(obj_id medic)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!grantSkill(medic, skillName) ||
                !hasSkill(medic, skillName))
            {
                return false;
            }
        }
        return true;
    }

    private void revokeSkills(obj_id medic)
        throws InterruptedException
    {
        for (int index = SKILLS.length - 1;
            index >= 0;
            --index)
        {
            if (hasSkill(medic, SKILLS[index]))
            {
                revokeSkill(medic, SKILLS[index]);
            }
        }
    }

    private boolean hasAnyFixtureSkill(obj_id medic)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (hasSkill(medic, skillName))
            {
                return true;
            }
        }
        return false;
    }

    private boolean setWoundExact(
        obj_id target,
        int attribute,
        int requested)
        throws InterruptedException
    {
        int current =
            getAttribWound(target, attribute);
        if (current < requested)
        {
            addWound(
                target,
                attribute,
                requested - current);
        }
        else if (current > requested)
        {
            healWound(
                target,
                attribute,
                current - requested);
        }
        return getAttribWound(
                target,
                attribute) == requested;
    }

    private int[] readAttributes(obj_id player)
        throws InterruptedException
    {
        int[] values =
            new int[ATTRIBUTES.length];
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            values[index] =
                getAttrib(
                    player,
                    ATTRIBUTES[index]);
        }
        return values;
    }

    private int[] readWounds(obj_id player)
        throws InterruptedException
    {
        int[] values =
            new int[ATTRIBUTES.length];
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            values[index] =
                getAttribWound(
                    player,
                    ATTRIBUTES[index]);
        }
        return values;
    }

    private float[] readRegen(obj_id player)
        throws InterruptedException
    {
        float[] values =
            new float[PRIMARY.length];
        for (int index = 0;
            index < PRIMARY.length;
            ++index)
        {
            values[index] =
                getRegenRate(
                    player,
                    PRIMARY[index]);
        }
        return values;
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
            hasObjVar(player, ORIGINAL_ATTRIBUTES) &&
            hasObjVar(player, ORIGINAL_WOUNDS) &&
            hasObjVar(player, ORIGINAL_REGEN) &&
            hasObjVar(
                player,
                ORIGINAL_MODIFIER_COUNT);
        return !medic
            ? common
            : common &&
                hasObjVar(
                    player,
                    ORIGINAL_MEDICAL_XP) &&
                hasObjVar(
                    player,
                    ORIGINAL_POINTS);
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
            !hasObjVar(patient, LIFECYCLE))
        {
            return "error=fixturePartial";
        }
        if (!lifecycle.equals(
                getStringObjVar(
                    medic,
                    LIFECYCLE)) ||
            !lifecycle.equals(
                getStringObjVar(
                    patient,
                    LIFECYCLE)))
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
        obj_id pack =
            hasObjVar(medic, ITEM)
                ? getObjIdObjVar(medic, ITEM)
                : obj_id.NULL_ID;
        int charges =
            isIdValid(pack) && exists(pack)
                ? getCount(pack)
                : -1;
        int groggyCount =
            countGroggyModifiers(patient);
        return
            "lifecycle=" + lifecycle +
            " medicLifecycle=" +
                readString(medic, LIFECYCLE) +
            " patientLifecycle=" +
                readString(patient, LIFECYCLE) +
            " prepared=" +
                (readInt(medic, PREPARED) == 1 &&
                    readInt(patient, PREPARED) == 1) +
            " sameGroup=" +
                group.inSameGroup(medic, patient) +
            " hasConsent=" +
                pclib.hasConsent(medic, patient) +
            " distanceCentimeters=" +
                Math.round(
                    getDistance(medic, patient) *
                        100.0f) +
            " medicDead=" + isDead(medic) +
            " patientDead=" + isDead(patient) +
            " patientPosture=" +
                getPosture(patient) +
            " deathMarker=" +
                hasObjVar(
                    patient,
                    pclib.VAR_BEEN_COUPDEGRACED) +
            " skills=" + buildSkillBits(medic) +
            " command=" +
                hasCommand(medic, COMMAND) +
            " healingAbility=" +
                getSkillStatMod(
                    medic,
                    "healing_ability") +
            " medicMind=" +
                getAttrib(medic, MIND) +
            " medicFocus=" +
                getAttrib(medic, FOCUS) +
            " patientHealth=" +
                getAttrib(patient, HEALTH) +
            " patientAction=" +
                getAttrib(patient, ACTION) +
            " patientMind=" +
                getAttrib(patient, MIND) +
            " patientHealthWound=" +
                getAttribWound(patient, HEALTH) +
            " patientActionWound=" +
                getAttribWound(patient, ACTION) +
            " patientMindWound=" +
                getAttribWound(patient, MIND) +
            " charges=" + charges +
            " medicalXp=" +
                getExperiencePoints(medic, "medical") +
            " handlerEntered=" +
                readInt(
                    medic,
                    ROOT + ".handlerEntered") +
            " handlerCalls=" +
                readInt(
                    medic,
                    ROOT + ".handlerCalls") +
            " outcome=" +
                readString(
                    medic,
                    ROOT + ".outcome") +
            " appliedTarget=" +
                readString(
                    medic,
                    ROOT + ".target") +
            " damageHealing=" +
                readInt(
                    medic,
                    ROOT + ".damageHealing") +
            " woundHealing=" +
                readInt(
                    medic,
                    ROOT + ".woundHealing") +
            " actualHealing=" +
                readInt(
                    medic,
                    ROOT + ".actualHealing") +
            " appliedMindCost=" +
                readInt(
                    medic,
                    ROOT + ".mindCost") +
            " appliedChargeCost=" +
                readInt(
                    medic,
                    ROOT + ".chargeCost") +
            " appliedMedicalXpDelta=" +
                readInt(
                    medic,
                    ROOT + ".medicalXpDelta") +
            " expectedMindCost=" +
                readInt(
                    medic,
                    EXPECTED_MIND_COST) +
            " handlerExpectedMindCost=" +
                readInt(
                    medic,
                    ROOT + ".expectedMindCost") +
            " expectedMedicalXp=" +
                readInt(
                    medic,
                    ROOT + ".expectedMedicalXp") +
            " handlerGrouped=" +
                readInt(
                    medic,
                    ROOT + ".grouped") +
            " handlerConsented=" +
                readInt(
                    medic,
                    ROOT + ".consented") +
            " handlerGroggyCount=" +
                readInt(
                    medic,
                    ROOT + ".groggyCount") +
            " groggyCount=" + groggyCount +
            " mindDelta=" +
                reverseDelta(
                    medic,
                    BEFORE_MEDIC_MIND,
                    getAttrib(medic, MIND)) +
            " chargeDelta=" +
                reverseDelta(
                    medic,
                    BEFORE_CHARGES,
                    charges) +
            " medicalXpDelta=" +
                delta(
                    medic,
                    BEFORE_MEDICAL_XP,
                    getExperiencePoints(
                        medic,
                        "medical")) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(medic);
    }

    private String buildSkillBits(obj_id medic)
        throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(medic, skillName)
                ? "1"
                : "0";
        }
        return bits;
    }

    private int countGroggyModifiers(obj_id patient)
        throws InterruptedException
    {
        int count = 0;
        for (int attribute = HEALTH;
            attribute <= WILLPOWER;
            ++attribute)
        {
            if (hasAttribModifier(
                    patient,
                    "precu_private_groggy_" +
                        attribute))
            {
                ++count;
            }
        }
        return count;
    }

    private int readInt(obj_id player, String path)
        throws InterruptedException
    {
        return hasObjVar(player, path)
            ? getIntObjVar(player, path)
            : 0;
    }

    private String readString(
        obj_id player,
        String path)
        throws InterruptedException
    {
        return hasObjVar(player, path)
            ? getStringObjVar(player, path)
            : "none";
    }

    private int delta(
        obj_id player,
        String key,
        int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? current - getIntObjVar(player, key)
            : 0;
    }

    private int reverseDelta(
        obj_id player,
        String key,
        int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? getIntObjVar(player, key) - current
            : 0;
    }

    private void resetTelemetry(obj_id medic)
        throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered",
            "handlerCalls",
            "outcome",
            "target",
            "pack",
            "damageHealing",
            "woundHealing",
            "actualHealing",
            "mindCost",
            "chargeCost",
            "medicalXpDelta",
            "expectedMindCost",
            "expectedMedicalXp",
            "grouped",
            "consented",
            "groggyCount"
        };
        for (String leaf : leaves)
        {
            String path = ROOT + "." + leaf;
            if (hasObjVar(medic, path))
            {
                removeObjVar(medic, path);
            }
        }
    }
}
