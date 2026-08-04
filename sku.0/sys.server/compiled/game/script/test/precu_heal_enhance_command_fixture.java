package script.test;

import script.attrib_mod;
import script.dictionary;
import script.location;
import script.obj_id;
import script.library.consumable;
import script.library.create;
import script.library.healing;
import script.library.pet_lib;
import script.library.skill;
import script.library.utils;

/**
 * Identity-bound and reversible fixture for Publish 14.1 Heal Enhance.
 *
 * The fixture creates one disposable pet patient, one deterministic two-charge
 * Health enhancement pack, a temporary interior medical-facility marker or
 * exterior fixture-owned surgical-droid provider, and the exact
 * Medic-through-Doctor-Wound-II skill chain. The connected client remains
 * the sole command-admission owner.
 */
public class precu_heal_enhance_command_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String COMMAND = "healEnhance";
    private static final int BUFF_ATTRIBUTE = HEALTH;
    private static final int BUFF_POWER = 200;
    private static final float BUFF_DURATION = 1800.0f;
    private static final int BASE_MIND_COST = 150;
    private static final String MEDICINE_TEMPLATE =
        "object/tangible/medicine/crafted/crafted_medpack_enhance_health_a.iff";
    private static final String FACILITY_VAR =
        "healing.canhealwound";
    private static final String ROOT =
        "precu.healEnhanceCommandFixture";
    private static final String LIFECYCLE =
        ROOT + ".lifecycle";
    private static final String PREPARED =
        ROOT + ".prepared";
    private static final String ITEM =
        ROOT + ".item";
    private static final String PATIENT =
        ROOT + ".patient";
    private static final String BUILDING =
        ROOT + ".building";
    private static final String ORIGINAL_FACILITY =
        ROOT + ".originalFacility";
    private static final String ORIGINAL_MIND =
        ROOT + ".originalMind";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String ORIGINAL_COOLDOWN_PRESENT =
        ROOT + ".originalCooldownPresent";
    private static final String ORIGINAL_COOLDOWN =
        ROOT + ".originalCooldown";
    private static final String BEFORE_MIND =
        ROOT + ".beforeMind";
    private static final String BEFORE_CHARGES =
        ROOT + ".beforeCharges";
    private static final String BEFORE_BUFF =
        ROOT + ".beforeBuff";
    private static final String BEFORE_XP =
        ROOT + ".beforeXp";
    private static final String EXPECTED_COST =
        ROOT + ".expectedCost";
    private static final String EXPECTED_ROUND_TIME =
        ROOT + ".expectedRoundTime";
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
        "science_doctor_wound_02"
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args =
            params == null
                ? new String[0]
                : params.trim().split("[ ]+");
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
        if (playerValue != PLAYER_OID ||
            !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null ||
            player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() ||
            !isPlayer(player) ||
            getPlayerStationId(player) !=
                PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, args[2]);
        }
        return USAGE;
    }

    private String prepare(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership =
                validateOwnership(player, lifecycle);
            if (ownership == null &&
                getIntObjVar(player, PREPARED) == 1)
            {
                return
                    "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent"))
            {
                return ownership;
            }
        }
        if (hasAnyFixtureSkill(player) ||
            hasCommand(player, COMMAND))
        {
            return "error=fixtureVectorAlreadyOwned";
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
            if (!isIdValid(building) ||
                !building.isAuthoritative())
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
            isIdValid(building) &&
                hasObjVar(building, FACILITY_VAR)
                ? 1
                : 0);
        setObjVar(
            player,
            ORIGINAL_MIND,
            getAttrib(player, MIND));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, "medical"));
        setObjVar(
            player,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            ORIGINAL_COOLDOWN_PRESENT,
            hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND)
                ? 1
                : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN,
            hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND)
                ? getIntObjVar(
                    player,
                    healing.VAR_HEALING_CAN_HEALWOUND)
                : 0);
        resetTelemetry(player);

        if (isIdValid(building) &&
            !hasObjVar(building, FACILITY_VAR))
        {
            setObjVar(building, FACILITY_VAR, 1);
        }
        if (!grantSkills(player) ||
            !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return
                "error=skillPreparationFailed restored=" +
                restored;
        }
        healing.setCanHealWound(player, 0);
        if (!setAttrib(
                player,
                MIND,
                getMaxAttrib(player, MIND)))
        {
            boolean restored = restore(player);
            return
                "error=mindPreparationFailed restored=" +
                restored;
        }

        dictionary patientData =
            utils.dataTableGetRow(
                create.CREATURE_TABLE,
                "bantha");
        if (patientData == null)
        {
            boolean restored = restore(player);
            return
                "error=patientDataUnavailable restored=" +
                restored;
        }
        obj_id patient =
            createObject(
                create.TEMPLATE_PREFIX +
                    patientData.getString("template"),
                current);
        if (isIdValid(patient))
        {
            setCreatureName(
                patient,
                "bantha");
            setObjVar(
                patient,
                "creature_type",
                "bantha");
        }
        if (!isIdValid(patient) ||
            !patient.isAuthoritative())
        {
            if (isIdValid(patient))
            {
                destroyObject(patient);
            }
            boolean restored = restore(player);
            return
                "error=patientCreationFailed restored=" +
                restored;
        }
        setMaster(patient, player);
        setObjVar(patient, "ai.pet", 1);
        setShockWound(patient, 0);
        setObjVar(player, PATIENT, patient);
        if (!pet_lib.isCreaturePet(patient) &&
            !pet_lib.isNpcPet(patient))
        {
            boolean restored = restore(player);
            return
                "error=patientPetPreparationFailed restored=" +
                restored;
        }
        if (!isIdValid(building))
        {
            setObjVar(patient, "medpower", 1.0f);
        }

        obj_id inventory =
            utils.getInventoryContainer(player);
        obj_id medicine =
            isIdValid(inventory)
                ? createObject(
                    MEDICINE_TEMPLATE,
                    inventory,
                    "")
                : obj_id.NULL_ID;
        if (!isIdValid(medicine))
        {
            boolean restored = restore(player);
            return
                "error=medicineCreationFailed restored=" +
                restored;
        }
        setObjVar(player, ITEM, medicine);
        attrib_mod[] modifiers =
            new attrib_mod[]
            {
                new attrib_mod(
                    BUFF_ATTRIBUTE,
                    BUFF_POWER,
                    BUFF_DURATION,
                    healing.VAR_BUFF_MOD_ATTACK,
                    healing.VAR_BUFF_MOD_DECAY)
            };
        setObjVar(
            medicine,
            consumable.VAR_CONSUMABLE_MODS,
            modifiers);
        setObjVar(
            medicine,
            consumable.VAR_CONSUMABLE_MEDICINE,
            true);
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

        if (!healing.isBuffMedicine(medicine) ||
            healing.getHealEnhanceMedicineAttribute(
                medicine) != BUFF_ATTRIBUTE ||
            healing.hasEnhancement(
                patient,
                BUFF_ATTRIBUTE) ||
            getCount(medicine) != 2)
        {
            boolean restored = restore(player);
            return
                "error=enhancementPreparationFailed restored=" +
                restored;
        }
        setObjVar(
            player,
            EXPECTED_COST,
            healing.getMedicalMindCost(
                player,
                BASE_MIND_COST));
        setObjVar(
            player,
            EXPECTED_ROUND_TIME,
            healing.getHealEnhanceRoundTime(player));
        markBefore(player);
        setObjVar(player, PREPARED, 1);
        return
            "action=prepare resumed=false " +
            buildStatus(player);
    }

    private void markBefore(obj_id player)
        throws InterruptedException
    {
        obj_id medicine =
            getObjIdObjVar(player, ITEM);
        obj_id patient =
            getObjIdObjVar(player, PATIENT);
        setObjVar(
            player,
            BEFORE_MIND,
            getAttrib(player, MIND));
        setObjVar(
            player,
            BEFORE_CHARGES,
            getCount(medicine));
        setObjVar(
            player,
            BEFORE_BUFF,
            healing.getHealEnhanceValue(
                patient,
                BUFF_ATTRIBUTE));
        setObjVar(
            player,
            BEFORE_XP,
            getExperiencePoints(player, "medical"));
    }

    private String status(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(player, lifecycle);
        return ownership == null
            ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return
                "action=cleanup alreadyClean=true " +
                "restored=true";
        }
        String ownership =
            validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(player))
        {
            return
                "action=cleanup alreadyClean=false " +
                "restored=false incompleteSnapshot=true";
        }
        return
            "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        if (hasObjVar(player, ITEM))
        {
            obj_id medicine =
                getObjIdObjVar(player, ITEM);
            if (isIdValid(medicine) &&
                medicine.isLoaded())
            {
                destroyObject(medicine);
            }
        }
        if (hasObjVar(player, PATIENT))
        {
            obj_id patient =
                getObjIdObjVar(player, PATIENT);
            if (isIdValid(patient) &&
                patient.isLoaded())
            {
                destroyObject(patient);
            }
        }

        int originalMind =
            getIntObjVar(player, ORIGINAL_MIND);
        setAttrib(player, MIND, originalMind);
        int originalXp =
            getIntObjVar(
                player,
                ORIGINAL_MEDICAL_XP);
        int currentXp =
            getExperiencePoints(player, "medical");
        if (currentXp != originalXp)
        {
            grantExperiencePoints(
                player,
                "medical",
                originalXp - currentXp);
        }
        if (getIntObjVar(
                player,
                ORIGINAL_COOLDOWN_PRESENT) == 1)
        {
            setObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND,
                getIntObjVar(
                    player,
                    ORIGINAL_COOLDOWN));
        }
        else if (hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND))
        {
            removeObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND);
        }
        if (hasObjVar(player, BUILDING))
        {
            obj_id building =
                getObjIdObjVar(player, BUILDING);
            if (getIntObjVar(
                    player,
                    ORIGINAL_FACILITY) == 0 &&
                isIdValid(building) &&
                hasObjVar(building, FACILITY_VAR))
            {
                removeObjVar(building, FACILITY_VAR);
            }
        }

        revokeSkills(player);
        boolean restored =
            getAttrib(player, MIND) == originalMind &&
            getExperiencePoints(player, "medical") ==
                originalXp &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(
                    player,
                    ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, COMMAND);
        removeObjVar(player, ROOT);
        return restored;
    }

    private boolean grantSkills(obj_id player)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!grantSkill(player, skillName) ||
                !hasSkill(player, skillName))
            {
                return false;
            }
        }
        return true;
    }

    private void revokeSkills(obj_id player)
        throws InterruptedException
    {
        for (int index = SKILLS.length - 1;
            index >= 0;
            --index)
        {
            if (hasSkill(player, SKILLS[index]))
            {
                revokeSkill(player, SKILLS[index]);
            }
        }
    }

    private boolean hasAnyFixtureSkill(obj_id player)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (hasSkill(player, skillName))
            {
                return true;
            }
        }
        return false;
    }

    private String validateOwnership(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, ITEM) ||
            !hasObjVar(player, PATIENT))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(
                getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        obj_id medicine =
            getObjIdObjVar(player, ITEM);
        obj_id patient =
            getObjIdObjVar(player, PATIENT);
        if (!isIdValid(medicine) ||
            utils.getContainingPlayer(medicine) !=
                player)
        {
            return
                "error=fixtureMedicineUnavailable";
        }
        if (!isIdValid(patient) ||
            !patient.isLoaded() ||
            getMaster(patient) != player)
        {
            return
                "error=fixturePatientUnavailable";
        }
        return null;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        obj_id medicine =
            hasObjVar(player, ITEM)
                ? getObjIdObjVar(player, ITEM)
                : obj_id.NULL_ID;
        obj_id patient =
            hasObjVar(player, PATIENT)
                ? getObjIdObjVar(player, PATIENT)
                : obj_id.NULL_ID;
        int charges =
            isIdValid(medicine)
                ? getCount(medicine)
                : -1;
        int buff =
            isIdValid(patient)
                ? healing.getHealEnhanceValue(
                    patient,
                    BUFF_ATTRIBUTE)
                : -1;
        int mind = getAttrib(player, MIND);
        int medicalXp =
            getExperiencePoints(player, "medical");
        int cooldown =
            hasObjVar(
                player,
                healing.VAR_HEALING_CAN_HEALWOUND)
                ? getIntObjVar(
                    player,
                    healing.VAR_HEALING_CAN_HEALWOUND) -
                    getGameTime()
                : 0;
        return
            "player=" + player +
            " patient=" + patient +
            " currentCommand=" +
                getCurrentCommand(player) +
            " dead=" + isDead(player) +
            " incapacitated=" +
                isIncapacitated(player) +
            " skills=" + buildSkillBits(player) +
            " command=" +
                hasCommand(player, COMMAND) +
            " treatment=" +
                getSkillStatMod(
                    player,
                    "healing_wound_treatment") +
            " woundSpeed=" +
                getSkillStatMod(
                    player,
                    "healing_wound_speed") +
            " healingAbility=" +
                getSkillStatMod(
                    player,
                    "healing_ability") +
            " healthBuff=" + buff +
            " mind=" + mind +
            " focus=" + getAttrib(player, FOCUS) +
            " charges=" + charges +
            " medicalXp=" + medicalXp +
            " cooldownRemaining=" + cooldown +
            " handlerEntered=" +
                readInt(player, ".handlerEntered") +
            " handlerCalls=" +
                readInt(player, ".handlerCalls") +
            " handlerOutcome=" +
                readString(player, ".outcome") +
            " appliedTarget=" +
                readString(player, ".target") +
            " appliedAttribute=" +
                readInt(player, ".attribute") +
            " appliedProjectedPower=" +
                readInt(player, ".projectedPower") +
            " appliedBuffBefore=" +
                readInt(player, ".buffBefore") +
            " appliedBuffAfter=" +
                readInt(player, ".buffAfter") +
            " appliedAmountEnhanced=" +
                readInt(player, ".amountEnhanced") +
            " appliedMindCost=" +
                readInt(player, ".mindCost") +
            " appliedChargeCost=" +
                readInt(player, ".chargeCost") +
            " appliedMedicalXpDelta=" +
                readInt(player, ".medicalXpDelta") +
            " appliedRoundTime=" +
                readInt(player, ".roundTime") +
            " expectedCost=" +
                readInt(player, ".expectedCost") +
            " expectedRoundTime=" +
                readInt(
                    player,
                    ".expectedRoundTime") +
            " buffDelta=" +
                delta(
                    player,
                    BEFORE_BUFF,
                    buff) +
            " mindDelta=" +
                reverseDelta(
                    player,
                    BEFORE_MIND,
                    mind) +
            " chargeDelta=" +
                reverseDelta(
                    player,
                    BEFORE_CHARGES,
                    charges) +
            " medicalXpDelta=" +
                delta(
                    player,
                    BEFORE_XP,
                    medicalXp) +
            " expectedMedicalXp=" +
                Math.max(
                    0,
                    (int)(delta(
                        player,
                        BEFORE_BUFF,
                        buff) * 2.5f)) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player);
    }

    private String buildSkillBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(player, skillName)
                ? "1"
                : "0";
        }
        return bits;
    }

    private int readInt(
        obj_id player,
        String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
        return hasObjVar(player, path)
            ? getIntObjVar(player, path)
            : 0;
    }

    private String readString(
        obj_id player,
        String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
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

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, ORIGINAL_FACILITY) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(
                player,
                ORIGINAL_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_COOLDOWN);
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered",
            "handlerCalls",
            "outcome",
            "target",
            "medicine",
            "attribute",
            "projectedPower",
            "buffBefore",
            "buffAfter",
            "amountEnhanced",
            "mindCost",
            "chargeCost",
            "medicalXpDelta",
            "roundTime"
        };
        for (String leaf : leaves)
        {
            String path = ROOT + "." + leaf;
            if (hasObjVar(player, path))
            {
                removeObjVar(player, path);
            }
        }
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        if (lifecycle == null ||
            lifecycle.length() != 32)
        {
            return false;
        }
        for (int index = 0;
            index < lifecycle.length();
            ++index)
        {
            char value =
                lifecycle.charAt(index);
            boolean digit =
                value >= '0' && value <= '9';
            boolean lowerHex =
                value >= 'a' && value <= 'f';
            if (!digit && !lowerHex)
            {
                return false;
            }
        }
        return true;
    }
}
