package script.test;

import script.attrib_mod;
import script.obj_id;
import script.library.meditation;
import script.library.pclib;
import script.library.skill;
import script.library.utils;

/** Identity-bound reversible fixture for the Teras Kasi Accuracy lifecycle. */
public class precu_unarmed_accuracy_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String ROOT = "precu.unarmedAccuracyFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_POSTURE = ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_TIMES_PRESENT =
        ROOT + ".originalTimesPresent";
    private static final String ORIGINAL_TIMES = ROOT + ".originalTimes";
    private static final int FORCE_ROLL = 35;
    private static final int PREPARED_MAXIMUM = 1000;
    private static final int[] ACCEPTANCE_BASELINE =
    {
        69355, 1000, 300,
        500, 300, 1000,
        100, 100, 100
    };
    private static final float[] ACCEPTANCE_BASELINE_REGEN =
    {
        1.857143f, 6.190476f, 1.0f
    };
    private static final String[] COMMANDS =
    {
        "meditate", "powerBoost", "forceOfWill"
    };
    private static final String[] SKILLS =
    {
        "combat_brawler",
        "combat_brawler_novice",
        "combat_brawler_unarmed_01",
        "combat_brawler_unarmed_02",
        "combat_brawler_unarmed_03",
        "combat_brawler_unarmed_04",
        "combat_unarmed",
        "combat_unarmed_novice",
        "combat_unarmed_accuracy_01",
        "combat_unarmed_accuracy_02",
        "combat_unarmed_accuracy_03",
        "combat_unarmed_accuracy_04"
    };
    private static final int[] ATTRIBUTES =
    {
        HEALTH, STRENGTH, CONSTITUTION,
        ACTION, QUICKNESS, STAMINA,
        MIND, FOCUS, WILLPOWER
    };
    private static final int[] PRIMARY = {HEALTH, ACTION, MIND};
    private static final String USAGE =
        "usage: prepare|armPower|status|clearPower|armForce|stabilize|" +
        "cleanup|repairBaseline <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null
            ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 3 || !isValidLifecycle(args[2]))
        {
            return USAGE;
        }
        long oid;
        try
        {
            oid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (oid != PLAYER_OID)
        {
            return "error=playerIdentityRejected";
        }
        obj_id player = obj_id.getObjId(oid);
        if (!isAuthoritativePlayer(player))
        {
            return "error=playerUnavailable";
        }
        String action = args[0];
        String lifecycle = args[2];
        if (action.equalsIgnoreCase("repairBaseline"))
        {
            return repairBaseline(player);
        }
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, lifecycle);
        }
        if (action.equalsIgnoreCase("armPower"))
        {
            return armPower(player, lifecycle);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return status(player, lifecycle);
        }
        if (action.equalsIgnoreCase("clearPower"))
        {
            return clearPower(player, lifecycle);
        }
        if (action.equalsIgnoreCase("armForce"))
        {
            return armForce(player, lifecycle);
        }
        if (action.equalsIgnoreCase("stabilize"))
        {
            return stabilize(player, lifecycle);
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, lifecycle);
        }
        return USAGE;
    }

    private String repairBaseline(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, meditation.VAR_POWERBOOST_ACTIVE) ||
            meditation.hasPowerBoostModifiers(player) ||
            hasForceModifiers(player))
        {
            return "error=transientAbilityStillActive " + buildStatus(player);
        }
        clearTransientEffects(player, true);
        revokeSkills(player);
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            restoreWound(player, ATTRIBUTES[index], 0);
            restoreAttribute(player, ATTRIBUTES[index],
                ACCEPTANCE_BASELINE[index], ACCEPTANCE_BASELINE[index]);
        }
        restoreShock(player, 0);
        setLocomotion(player, 0);
        setPostureClientImmediate(player, 0);
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            setRegenRate(player, PRIMARY[index],
                ACCEPTANCE_BASELINE_REGEN[index]);
        }
        removeObjVar(player, ROOT);
        boolean restored = !hasAnyFixtureSkill(player) &&
            !hasAnyCommand(player) &&
            skill.getAvailableSkillPoints(player) == 100 &&
            getPosture(player) == 0 && getLocomotion(player) == 0;
        for (int index = 0; restored && index < ATTRIBUTES.length; ++index)
        {
            restored = getAttrib(player, ATTRIBUTES[index]) ==
                    ACCEPTANCE_BASELINE[index] &&
                getMaxAttrib(player, ATTRIBUTES[index]) ==
                    ACCEPTANCE_BASELINE[index] &&
                getAttribWound(player, ATTRIBUTES[index]) == 0;
        }
        return "action=repairBaseline restored=" + restored +
            " attributeCurrent=" + buildAttributeVector(player, false) +
            " attributeMaximum=" + buildAttributeVector(player, true) +
            " availablePoints=" + skill.getAvailableSkillPoints(player) +
            " posture=" + getPosture(player) +
            " locomotion=" + getLocomotion(player);
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }
        if (hasAnyFixtureSkill(player) || hasAnyCommand(player) ||
            meditation.isMeditating(player) ||
            hasObjVar(player, meditation.VAR_POWERBOOST_ACTIVE) ||
            meditation.hasPowerBoostModifiers(player) ||
            hasObjVar(player, meditation.VAR_FORCE_OF_WILL_ACTIVE) ||
            hasForceModifiers(player) || isIncapacitated(player) ||
            isDead(player) || getPosture(player) != POSTURE_UPRIGHT)
        {
            return "error=fixtureVectorAlreadyOwned";
        }
        attrib_mod[] modifiers = getAllAttribModifiers(player);
        if (modifiers != null && modifiers.length > 0)
        {
            return "error=foreignAttributeModifiersPresent";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_POSTURE, getPosture(player));
        setObjVar(player, ORIGINAL_LOCOMOTION, getLocomotion(player));
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            int attribute = ATTRIBUTES[index];
            setObjVar(player, currentPath(index), getAttrib(player, attribute));
            setObjVar(player, maximumPath(index),
                getMaxAttrib(player, attribute));
            setObjVar(player, woundPath(index),
                getAttribWound(player, attribute));
        }
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            setObjVar(player, regenPath(index),
                getRegenRate(player, PRIMARY[index]));
        }
        boolean timesPresent = hasObjVar(player,
            pclib.VAR_PRECU_INCAPACITATION_TIMES);
        setObjVar(player, ORIGINAL_TIMES_PRESENT, timesPresent ? 1 : 0);
        if (timesPresent)
        {
            int[] times = getIntArrayObjVar(player,
                pclib.VAR_PRECU_INCAPACITATION_TIMES);
            setObjVar(player, ORIGINAL_TIMES,
                times == null ? new int[0] : times);
        }
        if (!grantSkills(player) || !hasAllCommands(player) ||
            meditation.getMeditationSkillMod(player) != 75)
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            setRegenRate(player, PRIMARY[index], 0.0f);
        }
        setObjVar(player, PREPARED, 1);
        String armed = armPower(player, lifecycle);
        if (!armed.startsWith("action=armPower"))
        {
            boolean restored = restore(player);
            return "error=powerArmFailed restored=" + restored +
                " observed=" + armed;
        }
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String armPower(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        clearTransientEffects(player, true);
        prepareAttributes(player);
        setPostureClientImmediate(player, POSTURE_SITTING);
        boolean armed = getPosture(player) == POSTURE_SITTING &&
            hasAllCommands(player) &&
            meditation.getMeditationSkillMod(player) == 75 &&
            !meditation.isMeditating(player) &&
            !meditation.hasPowerBoostModifiers(player);
        return armed
            ? "action=armPower " + buildStatus(player)
            : "error=powerArmBoundaryFailed " + buildStatus(player);
    }

    private String clearPower(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        meditation.endMeditation(player, false);
        meditation.endPowerBoost(player, false);
        setPostureClientImmediate(player, POSTURE_UPRIGHT);
        setLocomotion(player, LOCOMOTION_STANDING);
        boolean cleared = !meditation.isMeditating(player) &&
            !hasObjVar(player, meditation.VAR_POWERBOOST_ACTIVE) &&
            !meditation.hasPowerBoostModifiers(player) &&
            getPosture(player) == POSTURE_UPRIGHT;
        return cleared
            ? "action=clearPower " + buildStatus(player)
            : "error=powerClearFailed " + buildStatus(player);
    }

    private String armForce(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        clearTransientEffects(player, true);
        prepareAttributes(player);
        pclib.clearPrecuIncapacitationTimes(player);
        setObjVar(player, ROOT + ".forcedRoll", FORCE_ROLL);
        setAttrib(player, ACTION, -25);
        if (getPosture(player) != POSTURE_INCAPACITATED)
        {
            return "error=forceIncapacitationFailed " + buildStatus(player);
        }
        int recoveryTime = getGameTime() + 60;
        utils.setScriptVar(player, "incap.timeStamp", recoveryTime);
        setCount(player, 60);
        boolean armed = isIncapacitated(player) &&
            getPosture(player) == POSTURE_INCAPACITATED &&
            utils.getIntScriptVar(player, "incap.timeStamp") == recoveryTime;
        return armed
            ? "action=armForce " + buildStatus(player)
            : "error=forceArmBoundaryFailed " + buildStatus(player);
    }

    private String stabilize(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        clearTransientEffects(player, false);
        prepareAttributes(player);
        setPostureClientImmediate(player, POSTURE_UPRIGHT);
        setLocomotion(player, LOCOMOTION_STANDING);
        boolean stable = !isIncapacitated(player) &&
            !meditation.isMeditating(player) &&
            !meditation.hasPowerBoostModifiers(player) &&
            !hasForceModifiers(player) && hasAllCommands(player) &&
            hasObjVar(player, meditation.VAR_FORCE_OF_WILL_ACTIVE) &&
            getIntObjVar(player,
                meditation.VAR_FORCE_OF_WILL_ACTIVE) >= 0;
        return stable
            ? "action=stabilize " + buildStatus(player)
            : "error=stabilizeFailed " + buildStatus(player);
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
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        if (!hasCompleteSnapshot(player))
        {
            return "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true";
        }
        boolean restored = restore(player);
        return "action=cleanup alreadyClean=false restored=" + restored +
            (restored ? "" : " observed=" + buildStatus(player));
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        clearTransientEffects(player, true);
        revokeSkills(player);
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            int attribute = ATTRIBUTES[index];
            restoreWound(player, attribute,
                getIntObjVar(player, woundPath(index)));
            restoreAttribute(player, attribute,
                getIntObjVar(player, maximumPath(index)),
                getIntObjVar(player, currentPath(index)));
        }
        restoreShock(player, getIntObjVar(player, ORIGINAL_SHOCK));
        if (getIntObjVar(player, ORIGINAL_TIMES_PRESENT) == 1)
        {
            setObjVar(player, pclib.VAR_PRECU_INCAPACITATION_TIMES,
                getIntArrayObjVar(player, ORIGINAL_TIMES));
        }
        else
        {
            pclib.clearPrecuIncapacitationTimes(player);
        }
        setLocomotion(player, getIntObjVar(player, ORIGINAL_LOCOMOTION));
        setPostureClientImmediate(player,
            getIntObjVar(player, ORIGINAL_POSTURE));
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            setRegenRate(player, PRIMARY[index],
                getFloatObjVar(player, regenPath(index)));
        }
        boolean restored = !hasAnyFixtureSkill(player) &&
            !hasAnyCommand(player) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            getPosture(player) == getIntObjVar(player, ORIGINAL_POSTURE) &&
            getLocomotion(player) ==
                getIntObjVar(player, ORIGINAL_LOCOMOTION) &&
            !meditation.isMeditating(player) &&
            !meditation.hasPowerBoostModifiers(player) &&
            !hasForceModifiers(player) &&
            !hasObjVar(player, meditation.VAR_FORCE_OF_WILL_ACTIVE) &&
            attributesRestored(player) &&
            getShockWound(player) == getIntObjVar(player, ORIGINAL_SHOCK);
        if (restored)
        {
            removeObjVar(player, ROOT);
        }
        return restored;
    }

    private void clearTransientEffects(obj_id player, boolean clearCooldown)
        throws InterruptedException
    {
        meditation.endMeditation(player, false);
        meditation.endPowerBoost(player, false);
        meditation.clearForceOfWillModifiers(player);
        utils.removeScriptVar(player, "incap.timeStamp");
        setCount(player, 0);
        setState(player, STATE_FEIGN_DEATH, false);
        if (clearCooldown &&
            hasObjVar(player, meditation.VAR_FORCE_OF_WILL_ACTIVE))
        {
            removeObjVar(player, meditation.VAR_FORCE_OF_WILL_ACTIVE);
        }
        if (hasObjVar(player, ROOT + ".forcedRoll"))
        {
            removeObjVar(player, ROOT + ".forcedRoll");
        }
    }

    private void prepareAttributes(obj_id player) throws InterruptedException
    {
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            int attribute = ATTRIBUTES[index];
            restoreWound(player, attribute, 0);
            int maximum = Math.max(PREPARED_MAXIMUM,
                getIntObjVar(player, maximumPath(index)));
            restoreAttribute(player, attribute, maximum, maximum);
        }
        restoreShock(player, 0);
    }

    private void restoreAttribute(obj_id player, int attribute, int maximum,
        int current) throws InterruptedException
    {
        setMaxAttrib(player, attribute, maximum);
        int maximumDelta = maximum - getMaxAttrib(player, attribute);
        if (maximumDelta != 0)
        {
            setMaxAttrib(player, attribute, maximum + maximumDelta);
        }
        setAttrib(player, attribute, current);
        int currentDelta = current - getAttrib(player, attribute);
        if (currentDelta != 0)
        {
            setAttrib(player, attribute, current + currentDelta);
        }
    }

    private void restoreWound(obj_id player, int attribute, int requested)
        throws InterruptedException
    {
        int current = getAttribWound(player, attribute);
        if (current < requested)
        {
            addWound(player, attribute, requested - current);
        }
        else if (current > requested)
        {
            healWound(player, attribute, current - requested);
        }
    }

    private void restoreShock(obj_id player, int requested)
        throws InterruptedException
    {
        int current = getShockWound(player);
        if (current < requested)
        {
            addShockWound(player, requested - current);
        }
        else if (current > requested)
        {
            healShockWound(player, current - requested);
        }
    }

    private boolean attributesRestored(obj_id player)
        throws InterruptedException
    {
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            int attribute = ATTRIBUTES[index];
            if (getAttrib(player, attribute) !=
                    getIntObjVar(player, currentPath(index)) ||
                getMaxAttrib(player, attribute) !=
                    getIntObjVar(player, maximumPath(index)) ||
                getAttribWound(player, attribute) !=
                    getIntObjVar(player, woundPath(index)))
            {
                return false;
            }
        }
        return true;
    }

    private boolean grantSkills(obj_id player) throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!skill.grantSkillToPlayer(player, skillName) ||
                !hasSkill(player, skillName))
            {
                return false;
            }
        }
        return true;
    }

    private void revokeSkills(obj_id player) throws InterruptedException
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
        for (String skillName : SKILLS)
        {
            if (hasSkill(player, skillName))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyCommand(obj_id player) throws InterruptedException
    {
        for (String command : COMMANDS)
        {
            if (hasCommand(player, command))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasAllCommands(obj_id player) throws InterruptedException
    {
        for (String command : COMMANDS)
        {
            if (!hasCommand(player, command))
            {
                return false;
            }
        }
        return true;
    }

    private boolean hasForceModifiers(obj_id player)
        throws InterruptedException
    {
        for (int attribute : ATTRIBUTES)
        {
            if (hasAttribModifier(player,
                meditation.MOD_FORCE_OF_WILL_PREFIX + attribute))
            {
                return true;
            }
        }
        return false;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        int expiration = hasObjVar(player, meditation.VAR_POWERBOOST_ACTIVE)
            ? getIntObjVar(player, meditation.VAR_POWERBOOST_ACTIVE)
            : 0;
        int forceStamp = hasObjVar(player,
            meditation.VAR_FORCE_OF_WILL_ACTIVE)
                ? getIntObjVar(player,
                    meditation.VAR_FORCE_OF_WILL_ACTIVE)
                : -2;
        int recoveryTime = utils.hasScriptVar(player, "incap.timeStamp")
            ? utils.getIntScriptVar(player, "incap.timeStamp")
            : 0;
        return
            "player=" + player +
            " commandBits=" + buildCommandBits(player) +
            " skillBits=" + buildSkillBits(player) +
            " meditateMod=" + meditation.getMeditationSkillMod(player) +
            " posture=" + getPosture(player) +
            " locomotion=" + getLocomotion(player) +
            " meditating=" + meditation.isMeditating(player) +
            " powerActive=" + hasObjVar(player,
                meditation.VAR_POWERBOOST_ACTIVE) +
            " powerChannelBits=" + buildPowerChannelBits(player) +
            " powerBonus=" + readInt(player, "powerBonus") +
            " powerTick=" + readInt(player, "powerTick") +
            " powerDuration=" + readInt(player, "powerDuration") +
            " powerCounter=" + powerValue(player,
                meditation.VAR_POWERBOOST_COUNTER) +
            " powerHealthActionApplied=" + powerValue(player,
                meditation.VAR_POWERBOOST_HEALTH_ACTION_APPLIED) +
            " powerMindApplied=" + powerValue(player,
                meditation.VAR_POWERBOOST_MIND_APPLIED) +
            " powerRemaining=" + Math.max(0, expiration - getGameTime()) +
            " powerHandlerEntered=" +
                readInt(player, "powerHandlerEntered") +
            " powerFailHandlerEntered=" +
                readInt(player, "powerFailHandlerEntered") +
            " powerCommandCooldownRemaining=" +
                (int)Math.ceil(getCooldownTimeLeft(player, "powerBoost")) +
            " forceHandlerEntered=" +
                readInt(player, "forceHandlerEntered") +
            " forceModifier=" + readInt(player, "forceModifier") +
            " forceRoll=" + readInt(player, "forceRoll") +
            " forceDelta=" + readInt(player, "forceDelta") +
            " forceTier=" + readString(player, "forceTier") +
            " forceModifierBits=" + buildForceModifierBits(player) +
            " forceStamp=" + forceStamp +
            " forceCooldownRemaining=" + (forceStamp >= 0
                ? Math.max(0, forceStamp + 3600 - getGameTime())
                : 0) +
            " recoveryTime=" + recoveryTime +
            " recoveryRemaining=" + Math.max(0,
                recoveryTime - getGameTime()) +
            " attributeCurrent=" + buildAttributeVector(player, false) +
            " attributeMaximum=" + buildAttributeVector(player, true) +
            " woundVector=" + buildWoundVector(player) +
            " shock=" + getShockWound(player) +
            " outcome=" + readString(player, "outcome") +
            " availablePoints=" + skill.getAvailableSkillPoints(player) +
            " originalPoints=" + readSnapshot(player, ORIGINAL_POINTS) +
            " snapshotComplete=" + hasCompleteSnapshot(player);
    }

    private String buildCommandBits(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (String command : COMMANDS)
        {
            bits += hasCommand(player, command) ? "1" : "0";
        }
        return bits;
    }

    private String buildSkillBits(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(player, skillName) ? "1" : "0";
        }
        return bits;
    }

    private String buildPowerChannelBits(obj_id player)
        throws InterruptedException
    {
        return meditation.hasPowerBoostModifiers(player) ? "1111" : "0000";
    }

    private int powerValue(obj_id player, String path)
        throws InterruptedException
    {
        return hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }

    private String buildForceModifierBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (int attribute : ATTRIBUTES)
        {
            bits += hasAttribModifier(player,
                meditation.MOD_FORCE_OF_WILL_PREFIX + attribute)
                    ? "1"
                    : "0";
        }
        return bits;
    }

    private String buildAttributeVector(obj_id player, boolean maximum)
        throws InterruptedException
    {
        String values = "";
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (index > 0)
            {
                values += ",";
            }
            values += maximum
                ? getMaxAttrib(player, ATTRIBUTES[index])
                : getAttrib(player, ATTRIBUTES[index]);
        }
        return values;
    }

    private String buildWoundVector(obj_id player)
        throws InterruptedException
    {
        String values = "";
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (index > 0)
            {
                values += ",";
            }
            values += getAttribWound(player, ATTRIBUTES[index]);
        }
        return values;
    }

    private int readInt(obj_id player, String leaf)
        throws InterruptedException
    {
        String path = ROOT + "." + leaf;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }

    private String readString(obj_id player, String leaf)
        throws InterruptedException
    {
        String path = ROOT + "." + leaf;
        return hasObjVar(player, path)
            ? getStringObjVar(player, path)
            : "none";
    }

    private int readSnapshot(obj_id player, String path)
        throws InterruptedException
    {
        return hasObjVar(player, path) ? getIntObjVar(player, path) : -1;
    }

    private String currentPath(int index)
    {
        return ROOT + ".current" + index;
    }

    private String maximumPath(int index)
    {
        return ROOT + ".maximum" + index;
    }

    private String woundPath(int index)
    {
        return ROOT + ".wound" + index;
    }

    private String regenPath(int index)
    {
        return ROOT + ".regen" + index;
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return isIdValid(player) && player.isLoaded() &&
            player.isAuthoritative() && isPlayer(player) &&
            player.getValue() == PLAYER_OID &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        return hasObjVar(player, PROTOCOL) &&
            getIntObjVar(player, PROTOCOL) == PROTOCOL_VERSION &&
            hasObjVar(player, PREPARED) &&
            getIntObjVar(player, PREPARED) == 1
                ? null
                : "error=fixtureNotPrepared";
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) || !hasObjVar(player, PROTOCOL) ||
            !hasObjVar(player, PREPARED) ||
            !hasObjVar(player, ORIGINAL_POINTS) ||
            !hasObjVar(player, ORIGINAL_POSTURE) ||
            !hasObjVar(player, ORIGINAL_LOCOMOTION) ||
            !hasObjVar(player, ORIGINAL_SHOCK) ||
            !hasObjVar(player, ORIGINAL_TIMES_PRESENT))
        {
            return false;
        }
        if (getIntObjVar(player, ORIGINAL_TIMES_PRESENT) == 1 &&
            !hasObjVar(player, ORIGINAL_TIMES))
        {
            return false;
        }
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (!hasObjVar(player, currentPath(index)) ||
                !hasObjVar(player, maximumPath(index)) ||
                !hasObjVar(player, woundPath(index)))
            {
                return false;
            }
        }
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            if (!hasObjVar(player, regenPath(index)))
            {
                return false;
            }
        }
        return true;
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
            if (!((value >= '0' && value <= '9') ||
                (value >= 'a' && value <= 'f')))
            {
                return false;
            }
        }
        return true;
    }
}
