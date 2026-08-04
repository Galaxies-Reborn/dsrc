package script.test;

import script.attrib_mod;
import script.dictionary;
import script.library.consumable;
import script.library.create;
import script.library.healing;
import script.library.utils;
import script.library.xp;
import script.location;
import script.obj_id;

/**
 * Identity-bound ServerConsole fixture for the Publish 14.1 healWound
 * player command.
 *
 * The fixture snapshots the bound player, temporarily grants medic novice,
 * arms one Health wound, and creates a deterministic two-charge wound pack.
 * Queue actions enter the real command table and player.cmd.heal_wound
 * adapter. Cleanup restores every player/facility value touched here.
 */
public class precu_heal_wound_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final int TEST_MAX = 1000;
    private static final int TEST_WOUND = 300;
    private static final int MEDICINE_POWER = 200;
    private static final int QUEUE_SAFETY_SECONDS = 8;
    private static final String MEDIC_NOVICE = "science_medic_novice";
    private static final String MEDIC_INJURY_ONE =
        "science_medic_injury_01";
    private static final String COMMAND = "healWound";
    private static final String MEDICINE_TEMPLATE =
        "object/tangible/medicine/medpack_wound_health.iff";
    private static final String FACILITY_VAR = "healing.canhealwound";
    private static final String ROOT = "precu.healWoundCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ITEM = ROOT + ".item";
    private static final String BUILDING = ROOT + ".building";
    private static final String PATIENT = ROOT + ".patient";
    private static final String ORIGINAL_FACILITY = ROOT + ".originalFacility";
    private static final String ORIGINAL_NOVICE = ROOT + ".originalNovice";
    private static final String ORIGINAL_INJURY_ONE =
        ROOT + ".originalInjuryOne";
    private static final String ORIGINAL_COMMAND = ROOT + ".originalCommand";
    private static final String ORIGINAL_COOLDOWN_PRESENT =
        ROOT + ".originalCooldownPresent";
    private static final String ORIGINAL_COOLDOWN = ROOT + ".originalCooldown";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String EXPECTED_COST = ROOT + ".expectedCost";
    private static final String EXPECTED_ROUND_TIME =
        ROOT + ".expectedRoundTime";
    private static final String BEFORE_WOUND = ROOT + ".beforeWound";
    private static final String BEFORE_MIND = ROOT + ".beforeMind";
    private static final String BEFORE_CHARGES = ROOT + ".beforeCharges";
    private static final String SECOND_WOUND = ROOT + ".secondWound";
    private static final String SECOND_MIND = ROOT + ".secondMind";
    private static final String SECOND_CHARGES = ROOT + ".secondCharges";
    private static final String LAST_QUEUE_TIME = ROOT + ".lastQueueTime";
    private static final String USAGE =
        "usage: prepare|markQueue|status|markCooldown|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args =
            params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 3)
        {
            return USAGE;
        }

        long playerValue;
        try
        {
            playerValue = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID || !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        String action = args[0];
        String lifecycle = args[2];
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, lifecycle);
        }
        if (action.equalsIgnoreCase("markQueue"))
        {
            return markQueue(player, lifecycle, false);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return status(player, lifecycle);
        }
        if (action.equalsIgnoreCase("markCooldown"))
        {
            return markQueue(player, lifecycle, true);
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, lifecycle);
        }
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            if (ownership != null)
            {
                return ownership;
            }
            return "action=prepare resumed=true " + buildStatus(player);
        }

        location current = getLocation(player);
        if (current == null)
        {
            return "error=locationUnavailable";
        }
        obj_id building = obj_id.NULL_ID;
        if (isIdValid(current.cell))
        {
            building = getTopMostContainer(current.cell);
            if (!isIdValid(building) || !building.isAuthoritative())
            {
                return "error=medicalFacilityUnavailable";
            }
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        if (isIdValid(building))
        {
            setObjVar(player, BUILDING, building);
        }
        setObjVar(
            player,
            ORIGINAL_FACILITY,
            isIdValid(building) && hasObjVar(building, FACILITY_VAR) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_NOVICE,
            hasSkill(player, MEDIC_NOVICE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_INJURY_ONE,
            hasSkill(player, MEDIC_INJURY_ONE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COMMAND,
            hasCommand(player, COMMAND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN_PRESENT,
            hasObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN,
            hasObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND)
                ? getIntObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND)
                : 0);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, xp.MEDICAL));

        if (isIdValid(building) && !hasObjVar(building, FACILITY_VAR))
        {
            setObjVar(building, FACILITY_VAR, 1);
        }
        dictionary patientData =
            utils.dataTableGetRow(create.CREATURE_TABLE, "bantha");
        if (patientData == null)
        {
            removeObjVar(player, ROOT);
            return "error=patientDataUnavailable";
        }
        obj_id patient = createObject(
            create.TEMPLATE_PREFIX + patientData.getString("template"),
            current);
        if (isIdValid(patient))
        {
            setCreatureName(patient, "bantha");
            setObjVar(patient, "creature_type", "bantha");
        }
        if (!isIdValid(patient) || !patient.isAuthoritative())
        {
            if (isIdValid(patient))
            {
                destroyObject(patient);
            }
            removeObjVar(player, ROOT);
            return "error=patientCreationFailed";
        }
        setMaster(patient, player);
        setObjVar(patient, "ai.pet", 1);
        if (!isIdValid(building))
        {
            setObjVar(patient, "medpower", 1.0f);
        }
        setObjVar(player, PATIENT, patient);
        boolean skillReady =
            hasSkill(player, MEDIC_NOVICE) || grantSkill(player, MEDIC_NOVICE);
        skillReady =
            skillReady &&
            (hasSkill(player, MEDIC_INJURY_ONE) ||
                grantSkill(player, MEDIC_INJURY_ONE));
        if (!skillReady ||
            !hasSkill(player, MEDIC_INJURY_ONE) ||
            !hasCommand(player, COMMAND) ||
            getSkillStatMod(player, "healing_wound_treatment") <= 0)
        {
            String failed = buildStatus(player).replace(' ', '_');
            boolean restored = restore(player);
            return "error=medicNovicePreparationFailed restored=" + restored +
                " failedStatus=" + failed;
        }

        healing.setCanHealWound(player, 0);
        if (!setMaxAttrib(patient, HEALTH, TEST_MAX) ||
            !setAttrib(patient, HEALTH, TEST_MAX) ||
            !setWoundExact(patient, HEALTH, TEST_WOUND) ||
            !setShockWound(player, 0) ||
            !setAttrib(player, MIND, getMaxAttrib(player, MIND)))
        {
            String failed = buildStatus(player).replace(' ', '_');
            boolean restored = restore(player);
            return "error=hamPreparationFailed restored=" + restored +
                " failedStatus=" + failed;
        }

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id medicine = isIdValid(inventory)
            ? createObject(MEDICINE_TEMPLATE, inventory, "")
            : obj_id.NULL_ID;
        if (!isIdValid(medicine))
        {
            boolean restored = restore(player);
            return "error=medicineCreationFailed restored=" + restored;
        }
        setObjVar(player, ITEM, medicine);
        attrib_mod[] modifiers = new attrib_mod[1];
        modifiers[0] =
            utils.createHealWoundAttribMod(HEALTH, MEDICINE_POWER);
        setObjVar(medicine, consumable.VAR_CONSUMABLE_MODS, modifiers);
        setObjVar(medicine, consumable.VAR_CONSUMABLE_MEDICINE, true);
        setObjVar(
            medicine,
            consumable.VAR_CONSUMABLE_STOMACH_VALUES,
            new int[] { 0, 0, 0 });
        setObjVar(
            medicine,
            consumable.VAR_SKILL_MOD_REQUIRED,
            new String[] { "healing_ability" });
        setObjVar(
            medicine,
            consumable.VAR_SKILL_MOD_MIN,
            new int[] { 0 });
        setCount(medicine, 2);

        setObjVar(
            player,
            EXPECTED_COST,
            healing.getHealWoundMindCost(
                player,
                healing.VAR_HEALWOUND_COST));
        setObjVar(
            player,
            EXPECTED_ROUND_TIME,
            healing.getHealWoundRoundTime(player));
        setObjVar(player, BEFORE_WOUND, getAttribWound(patient, HEALTH));
        setObjVar(player, BEFORE_MIND, getAttrib(player, MIND));
        setObjVar(player, BEFORE_CHARGES, getCount(medicine));
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String markQueue(
        obj_id player,
        String lifecycle,
        boolean cooldownProbe)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        obj_id medicine = getObjIdObjVar(player, ITEM);
        obj_id patient = getObjIdObjVar(player, PATIENT);
        if (!isIdValid(medicine))
        {
            return "error=medicineUnavailable";
        }
        if (!isIdValid(patient))
        {
            return "error=patientUnavailable";
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() - getIntObjVar(player, LAST_QUEUE_TIME) <
                QUEUE_SAFETY_SECONDS &&
            !(cooldownProbe && getCurrentCommand(player) != 0))
        {
            return "error=priorQueuePending " + buildStatus(player);
        }

        if (cooldownProbe)
        {
            setObjVar(player, SECOND_WOUND, getAttribWound(patient, HEALTH));
            setObjVar(player, SECOND_MIND, getAttrib(player, MIND));
            setObjVar(player, SECOND_CHARGES, getCount(medicine));
        }
        else
        {
            setObjVar(player, BEFORE_WOUND, getAttribWound(patient, HEALTH));
            setObjVar(player, BEFORE_MIND, getAttrib(player, MIND));
            setObjVar(player, BEFORE_CHARGES, getCount(medicine));
        }
        setObjVar(player, LAST_QUEUE_TIME, getGameTime());
        return "action=" + (cooldownProbe ? "markCooldown" : "markQueue") +
            " executeTime=7 pendingClientQueue=true " +
            buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        return "action=status " + buildStatus(player);
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() - getIntObjVar(player, LAST_QUEUE_TIME) <
                QUEUE_SAFETY_SECONDS)
        {
            return "error=queuePendingPotential " + buildStatus(player);
        }
        boolean restored = restore(player);
        return "action=cleanup alreadyClean=false restored=" + restored;
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        boolean complete =
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_SHOCK) &&
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_INJURY_ONE) &&
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_COOLDOWN) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP) &&
            hasObjVar(player, ORIGINAL_FACILITY);
        if (!complete)
        {
            return false;
        }

        if (hasObjVar(player, ITEM))
        {
            obj_id medicine = getObjIdObjVar(player, ITEM);
            if (isIdValid(medicine) && medicine.isLoaded())
            {
                destroyObject(medicine);
            }
        }
        boolean restored =
            setAttrib(
                player,
                MIND,
                getIntObjVar(player, ORIGINAL_MIND));
        restored =
            setShockWound(
                player,
                getIntObjVar(player, ORIGINAL_SHOCK)) && restored;
        int currentMedicalXp =
            getExperiencePoints(player, xp.MEDICAL);
        int originalMedicalXp =
            getIntObjVar(player, ORIGINAL_MEDICAL_XP);
        if (currentMedicalXp != originalMedicalXp)
        {
            grantExperiencePoints(
                player,
                xp.MEDICAL,
                originalMedicalXp - currentMedicalXp);
        }
        restored =
            getExperiencePoints(player, xp.MEDICAL) ==
                originalMedicalXp && restored;

        if (getIntObjVar(player, ORIGINAL_INJURY_ONE) == 0 &&
            hasSkill(player, MEDIC_INJURY_ONE))
        {
            revokeSkill(player, MEDIC_INJURY_ONE);
        }
        if (getIntObjVar(player, ORIGINAL_NOVICE) == 0 &&
            hasSkill(player, MEDIC_NOVICE))
        {
            revokeSkill(player, MEDIC_NOVICE);
        }
        if (getIntObjVar(player, ORIGINAL_COMMAND) == 1 &&
            !hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        else if (getIntObjVar(player, ORIGINAL_COMMAND) == 0 &&
            hasCommand(player, COMMAND))
        {
            revokeCommand(player, COMMAND);
        }

        if (getIntObjVar(player, ORIGINAL_COOLDOWN_PRESENT) == 1)
        {
            setObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND,
                getIntObjVar(player, ORIGINAL_COOLDOWN));
        }
        else if (hasObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND))
        {
            removeObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND);
        }

        if (hasObjVar(player, BUILDING))
        {
            obj_id building = getObjIdObjVar(player, BUILDING);
            if (getIntObjVar(player, ORIGINAL_FACILITY) == 0 &&
                isIdValid(building) && hasObjVar(building, FACILITY_VAR))
            {
                removeObjVar(building, FACILITY_VAR);
            }
        }
        if (hasObjVar(player, PATIENT))
        {
            obj_id patient = getObjIdObjVar(player, PATIENT);
            if (isIdValid(patient) && patient.isLoaded())
            {
                destroyObject(patient);
            }
        }
        removeObjVar(player, ROOT);
        return restored;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, ITEM) ||
            !hasObjVar(player, PATIENT))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        obj_id medicine = getObjIdObjVar(player, ITEM);
        obj_id patient = getObjIdObjVar(player, PATIENT);
        if (!isIdValid(medicine) ||
            utils.getContainingPlayer(medicine) != player)
        {
            return "error=fixtureMedicineUnavailable";
        }
        if (!isIdValid(patient) || !patient.isLoaded() ||
            getMaster(patient) != player)
        {
            return "error=fixturePatientUnavailable";
        }
        return null;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id medicine = hasObjVar(player, ITEM)
            ? getObjIdObjVar(player, ITEM)
            : obj_id.NULL_ID;
        obj_id patient = hasObjVar(player, PATIENT)
            ? getObjIdObjVar(player, PATIENT)
            : obj_id.NULL_ID;
        int charges = isIdValid(medicine) ? getCount(medicine) : -1;
        int wound = isIdValid(patient)
            ? getAttribWound(patient, HEALTH)
            : -1;
        int mind = getAttrib(player, MIND);
        int cooldown = hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND)
            ? getIntObjVar(player, healing.VAR_HEALING_CAN_HEALWOUND) -
                getGameTime()
            : 0;
        int woundDelta = hasObjVar(player, BEFORE_WOUND)
            ? getIntObjVar(player, BEFORE_WOUND) - wound
            : 0;
        int mindDelta = hasObjVar(player, BEFORE_MIND)
            ? getIntObjVar(player, BEFORE_MIND) - mind
            : 0;
        int chargeDelta = hasObjVar(player, BEFORE_CHARGES)
            ? getIntObjVar(player, BEFORE_CHARGES) - charges
            : 0;
        int secondWoundDelta = hasObjVar(player, SECOND_WOUND)
            ? getIntObjVar(player, SECOND_WOUND) - wound
            : 0;
        int secondMindDelta = hasObjVar(player, SECOND_MIND)
            ? getIntObjVar(player, SECOND_MIND) - mind
            : 0;
        int secondChargeDelta = hasObjVar(player, SECOND_CHARGES)
            ? getIntObjVar(player, SECOND_CHARGES) - charges
            : 0;
        return "player=" + player +
            " patient=" + patient +
            " dead=" + isDead(player) +
            " posture=" + getPosture(player) +
            " locomotion=" + getLocomotion(player) +
            " currentCommand=" + getCurrentCommand(player) +
            " novice=" + hasSkill(player, MEDIC_NOVICE) +
            " injuryOne=" + hasSkill(player, MEDIC_INJURY_ONE) +
            " command=" + hasCommand(player, COMMAND) +
            " treatment=" +
                getSkillStatMod(player, "healing_wound_treatment") +
            " healthWound=" + wound +
            " mind=" + mind +
            " focus=" + getAttrib(player, FOCUS) +
            " charges=" + charges +
            " handlerEntered=" +
                (hasObjVar(
                    player,
                    ROOT + ".handlerEntered")
                    ? getIntObjVar(player, ROOT + ".handlerEntered")
                    : 0) +
            " handlerCalls=" +
                (hasObjVar(player, ROOT + ".handlerCalls")
                    ? getIntObjVar(player, ROOT + ".handlerCalls")
                    : 0) +
            " handlerOutcome=" +
                (hasObjVar(player, ROOT + ".handlerOutcome")
                    ? getStringObjVar(player, ROOT + ".handlerOutcome")
                    : "none") +
            " appliedWoundHeal=" +
                (hasObjVar(player, ROOT + ".appliedWoundHeal")
                    ? getIntObjVar(player, ROOT + ".appliedWoundHeal")
                    : 0) +
            " appliedMindCost=" +
                (hasObjVar(player, ROOT + ".appliedMindCost")
                    ? getIntObjVar(player, ROOT + ".appliedMindCost")
                    : 0) +
            " appliedChargeCost=" +
                (hasObjVar(player, ROOT + ".appliedChargeCost")
                    ? getIntObjVar(player, ROOT + ".appliedChargeCost")
                    : 0) +
            " expectedMedicalXp=" +
                (hasObjVar(player, ROOT + ".expectedMedicalXp")
                    ? getIntObjVar(player, ROOT + ".expectedMedicalXp")
                    : 0) +
            " medicalXp=" +
                getExperiencePoints(player, xp.MEDICAL) +
            " medicalXpDelta=" +
                (hasObjVar(player, ORIGINAL_MEDICAL_XP)
                    ? getExperiencePoints(player, xp.MEDICAL) -
                        getIntObjVar(player, ORIGINAL_MEDICAL_XP)
                    : 0) +
            " medicalXpCap=" +
                getExperienceCap(player, xp.MEDICAL) +
            " woundDelta=" + woundDelta +
            " mindDelta=" + mindDelta +
            " chargeDelta=" + chargeDelta +
            " expectedCost=" +
                (hasObjVar(player, EXPECTED_COST)
                    ? getIntObjVar(player, EXPECTED_COST)
                    : -1) +
            " cooldownRemaining=" + cooldown +
            " expectedRoundTime=" +
                (hasObjVar(player, EXPECTED_ROUND_TIME)
                    ? getIntObjVar(player, EXPECTED_ROUND_TIME)
                    : -1) +
            " secondWoundDelta=" + secondWoundDelta +
            " secondMindDelta=" + secondMindDelta +
            " secondChargeDelta=" + secondChargeDelta;
    }

    private boolean setWoundExact(
        obj_id target,
        int attribute,
        int requested)
        throws InterruptedException
    {
        int current = getAttribWound(target, attribute);
        if (current == ATTRIB_ERROR)
        {
            return false;
        }
        if (current < requested)
        {
            addWound(target, attribute, requested - current);
        }
        else if (current > requested)
        {
            healWound(target, attribute, current - requested);
        }
        return getAttribWound(target, attribute) == requested;
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        if (lifecycle == null || lifecycle.length() != 32)
        {
            return false;
        }
        for (int index = 0; index < lifecycle.length(); ++index)
        {
            char value = lifecycle.charAt(index);
            boolean digit = value >= '0' && value <= '9';
            boolean lowerHex = value >= 'a' && value <= 'f';
            if (!digit && !lowerHex)
            {
                return false;
            }
        }
        return true;
    }
}
