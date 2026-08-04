package script.test;

import script.attrib_mod;
import script.library.consumable;
import script.library.healing;
import script.library.skill;
import script.library.utils;
import script.obj_id;

/**
 * Identity-bound fixture for the Publish 14.1 Heal State command.
 *
 * The fixture prepares a deterministic self-treatment boundary. The connected
 * client remains the sole command-queue owner.
 */
public class precu_heal_state_command_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final int TEST_STATE = STATE_DIZZY;
    private static final int QUEUE_SAFETY_SECONDS = 8;
    private static final int BASE_MIND_COST = 20;
    private static final String COMMAND = "healState";
    private static final String MEDICINE_TEMPLATE =
        "object/tangible/medicine/crafted/crafted_medpack_dizzy_a.iff";
    private static final String COOLDOWN_VAR =
        "healing.can_heal_state";
    private static final String ROOT =
        "precu.healStateCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ITEM = ROOT + ".item";
    private static final String ORIGINAL_MIND =
        ROOT + ".originalMind";
    private static final String ORIGINAL_SHOCK =
        ROOT + ".originalShock";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String ORIGINAL_STATES =
        ROOT + ".originalStates";
    private static final String ORIGINAL_COOLDOWN_PRESENT =
        ROOT + ".originalCooldownPresent";
    private static final String ORIGINAL_COOLDOWN =
        ROOT + ".originalCooldown";
    private static final String BEFORE_STATE =
        ROOT + ".beforeState";
    private static final String BEFORE_MIND =
        ROOT + ".beforeMind";
    private static final String BEFORE_CHARGES =
        ROOT + ".beforeCharges";
    private static final String BEFORE_XP =
        ROOT + ".beforeXp";
    private static final String EXPECTED_COST =
        ROOT + ".expectedCost";
    private static final String EXPECTED_ROUND_TIME =
        ROOT + ".expectedRoundTime";
    private static final String LAST_QUEUE_TIME =
        ROOT + ".lastQueueTime";
    private static final int[] HEALABLE_STATES =
    {
        STATE_STUNNED,
        STATE_DIZZY,
        STATE_BLINDED,
        STATE_INTIMIDATED
    };
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
        "science_doctor_novice"
    };
    private static final String USAGE =
        "usage: prepare|markQueue|status|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args = params == null
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
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("markQueue"))
        {
            return markQueue(player, args[2]);
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

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership =
                validateOwnership(player, lifecycle);
            return ownership == null
                ? "action=prepare resumed=true " +
                    buildStatus(player)
                : ownership;
        }
        if (hasAnyFixtureSkill(player) ||
            hasCommand(player, COMMAND))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, "medical"));
        setObjVar(
            player,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        int[] states = new int[HEALABLE_STATES.length];
        for (int index = 0; index < HEALABLE_STATES.length; ++index)
        {
            states[index] =
                getState(player, HEALABLE_STATES[index]);
        }
        setObjVar(player, ORIGINAL_STATES, states);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN_PRESENT,
            hasObjVar(player, COOLDOWN_VAR) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_COOLDOWN,
            hasObjVar(player, COOLDOWN_VAR)
                ? getIntObjVar(player, COOLDOWN_VAR)
                : 0);

        if (!grantSkills(player) ||
            !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" +
                restored;
        }
        if (hasObjVar(player, COOLDOWN_VAR))
        {
            removeObjVar(player, COOLDOWN_VAR);
        }
        if (!setShockWound(player, 0) ||
            !setAttrib(
                player,
                MIND,
                getMaxAttrib(player, MIND)) ||
            !setState(player, TEST_STATE, true) ||
            getState(player, TEST_STATE) != 1)
        {
            boolean restored = restore(player);
            return "error=statePreparationFailed restored=" +
                restored;
        }

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id medicine = isIdValid(inventory)
            ? createObject(MEDICINE_TEMPLATE, inventory, "")
            : obj_id.NULL_ID;
        if (!isIdValid(medicine))
        {
            boolean restored = restore(player);
            return "error=medicineCreationFailed restored=" +
                restored;
        }
        setObjVar(player, ITEM, medicine);
        attrib_mod[] modifiers =
        {
            utils.createHealDamageAttribMod(HEALTH, 1)
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
        setObjVar(
            medicine,
            healing.VAR_HEALING_STATE,
            "dizzy");
        setCount(medicine, 2);

        setObjVar(
            player,
            EXPECTED_COST,
            healing.getMedicalMindCost(
                player,
                BASE_MIND_COST));
        setObjVar(
            player,
            EXPECTED_ROUND_TIME,
            calculateRoundTime(player));
        markBefore(player);
        return "action=prepare resumed=false " +
            buildStatus(player);
    }

    private String markQueue(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() -
                getIntObjVar(player, LAST_QUEUE_TIME) <
                    QUEUE_SAFETY_SECONDS)
        {
            return "error=priorQueuePending " +
                buildStatus(player);
        }
        markBefore(player);
        setObjVar(player, LAST_QUEUE_TIME, getGameTime());
        return "action=markQueue executeTime=5 " +
            "pendingClientQueue=true " + buildStatus(player);
    }

    private void markBefore(obj_id player)
        throws InterruptedException
    {
        obj_id medicine = getObjIdObjVar(player, ITEM);
        setObjVar(
            player,
            BEFORE_STATE,
            getState(player, TEST_STATE));
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
            BEFORE_XP,
            getExperiencePoints(player, "medical"));
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null
            ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(
                getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (hasObjVar(player, LAST_QUEUE_TIME) &&
            getGameTime() -
                getIntObjVar(player, LAST_QUEUE_TIME) <
                    QUEUE_SAFETY_SECONDS)
        {
            return "error=queuePendingPotential " +
                buildStatus(player);
        }
        return "action=cleanup alreadyClean=false restored=" +
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
                getIntObjVar(player, ORIGINAL_SHOCK)) &&
            restored;
        int originalXp =
            getIntObjVar(player, ORIGINAL_MEDICAL_XP);
        int currentXp =
            getExperiencePoints(player, "medical");
        if (currentXp != originalXp)
        {
            grantExperiencePoints(
                player,
                "medical",
                originalXp - currentXp);
        }

        int[] states =
            getIntArrayObjVar(player, ORIGINAL_STATES);
        for (int index = 0; index < HEALABLE_STATES.length; ++index)
        {
            setState(
                player,
                HEALABLE_STATES[index],
                states[index] == 1);
        }
        if (getIntObjVar(
                player,
                ORIGINAL_COOLDOWN_PRESENT) == 1)
        {
            setObjVar(
                player,
                COOLDOWN_VAR,
                getIntObjVar(player, ORIGINAL_COOLDOWN));
        }
        else if (hasObjVar(player, COOLDOWN_VAR))
        {
            removeObjVar(player, COOLDOWN_VAR);
        }

        revokeSkills(player);
        restored =
            getExperiencePoints(player, "medical") ==
                originalXp &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, COMMAND) &&
            restored;
        removeObjVar(player, ROOT);
        return restored;
    }

    private boolean grantSkills(obj_id player)
        throws InterruptedException
    {
        for (int index = 0; index < SKILLS.length; ++index)
        {
            if (!grantSkill(player, SKILLS[index]) ||
                !hasSkill(player, SKILLS[index]))
            {
                return false;
            }
        }
        return true;
    }

    private void revokeSkills(obj_id player)
        throws InterruptedException
    {
        for (int index = SKILLS.length - 1; index >= 0; --index)
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
        for (int index = 0; index < SKILLS.length; ++index)
        {
            if (hasSkill(player, SKILLS[index]))
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
            !hasObjVar(player, ITEM))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(
                getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        obj_id medicine = getObjIdObjVar(player, ITEM);
        if (!isIdValid(medicine) ||
            utils.getContainingPlayer(medicine) != player)
        {
            return "error=fixtureMedicineUnavailable";
        }
        return null;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        obj_id medicine = hasObjVar(player, ITEM)
            ? getObjIdObjVar(player, ITEM)
            : obj_id.NULL_ID;
        int charges =
            isIdValid(medicine) ? getCount(medicine) : -1;
        int state = getState(player, TEST_STATE);
        int mind = getAttrib(player, MIND);
        int medicalXp =
            getExperiencePoints(player, "medical");
        int cooldown =
            hasObjVar(player, COOLDOWN_VAR)
                ? getIntObjVar(player, COOLDOWN_VAR) -
                    getGameTime()
                : 0;
        return "player=" + player +
            " currentCommand=" + getCurrentCommand(player) +
            " dead=" + isDead(player) +
            " incapacitated=" + isIncapacitated(player) +
            " skills=" + buildSkillBits(player) +
            " command=" + hasCommand(player, COMMAND) +
            " treatment=" +
                getSkillStatMod(
                    player,
                    "healing_injury_treatment") +
            " injurySpeed=" +
                getSkillStatMod(
                    player,
                    "healing_injury_speed") +
            " healingAbility=" +
                getSkillStatMod(player, "healing_ability") +
            " state=" + state +
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
                (hasObjVar(player, ROOT + ".outcome")
                    ? getStringObjVar(
                        player,
                        ROOT + ".outcome")
                    : "none") +
            " appliedState=" +
                readInt(player, ".state") +
            " appliedStateRemoved=" +
                readInt(player, ".stateRemoved") +
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
                readInt(player, ".expectedRoundTime") +
            " stateDelta=" +
                reverseDelta(player, BEFORE_STATE, state) +
            " mindDelta=" +
                reverseDelta(player, BEFORE_MIND, mind) +
            " chargeDelta=" +
                reverseDelta(player, BEFORE_CHARGES, charges) +
            " medicalXpDelta=" +
                delta(player, BEFORE_XP, medicalXp) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player);
    }

    private String buildSkillBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < SKILLS.length; ++index)
        {
            bits += hasSkill(player, SKILLS[index])
                ? "1"
                : "0";
        }
        return bits;
    }

    private int calculateRoundTime(obj_id player)
        throws InterruptedException
    {
        int injurySpeed =
            getSkillStatMod(
                player,
                "healing_injury_speed");
        int roundTime =
            Math.round(20.0f - injurySpeed / 5.0f);
        int recovery =
            getSkillStatMod(player, "heal_recovery");
        if (recovery > 0)
        {
            roundTime = Math.round(
                roundTime *
                    (100.0f - recovery) /
                    100.0f);
        }
        return Math.max(4, roundTime);
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        return hasObjVar(player, ROOT + suffix)
            ? getIntObjVar(player, ROOT + suffix)
            : 0;
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
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_SHOCK) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_STATES) &&
            hasObjVar(player, ORIGINAL_COOLDOWN_PRESENT) &&
            hasObjVar(player, ORIGINAL_COOLDOWN);
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
