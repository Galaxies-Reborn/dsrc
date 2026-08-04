package script.test;

import script.obj_id;
import script.library.xp;

/**
 * Identity-bound, reversible fixture for Publish 14.1 Quick Heal.
 *
 * The connected client remains the sole command owner. Preparation creates
 * enough deterministic damage and HAM capacity to observe the production
 * command's random heal, adjusted Mind cost, and two wound side effects.
 */
public class precu_quick_heal_command_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String COMMAND = "quickHeal";
    private static final String ROOT =
        "precu.quickHealCommandFixture";
    private static final String LIFECYCLE =
        ROOT + ".lifecycle";
    private static final String PREPARED =
        ROOT + ".prepared";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalCommand";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String FOCUS_CAPACITY_MOD =
        ROOT + ".focusCapacity";
    private static final int REQUIRED_FOCUS_CAPACITY = 1100;
    private static final float FOCUS_CAPACITY_DURATION = 3600.0f;
    private static final int[] ATTRIBUTES =
        { HEALTH, ACTION, MIND, FOCUS, WILLPOWER };
    private static final String[] ATTRIBUTE_NAMES =
        { "health", "action", "mind", "focus", "willpower" };
    private static final int PREPARED_HEALTH_CURRENT = 200;
    private static final int PREPARED_ACTION_CURRENT = 100;
    private static final String USAGE =
        "usage: prepare|status|cleanup|recover <playerOid> <lifecycle>";

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
        if (args[0].equalsIgnoreCase("recover"))
        {
            return recover(player);
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
            if (ownership == null &&
                getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent") &&
                getIntOrZero(player, PREPARED) == 1)
            {
                return ownership;
            }
            // Packed player objvars can retain inactive leaves after removal.
            // An unprepared fixture owns no live state and is safe to replace.
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_COMMAND,
            hasCommand(player, COMMAND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, "medical"));
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            setObjVar(
                player,
                originalPath(index, "max"),
                getMaxAttrib(player, ATTRIBUTES[index]));
            setObjVar(
                player,
                originalPath(index, "current"),
                getAttrib(player, ATTRIBUTES[index]));
            setObjVar(
                player,
                originalPath(index, "wound"),
                getAttribWound(player, ATTRIBUTES[index]));
        }
        resetTelemetry(player);
        if (!hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        if (hasAttribModifier(player, FOCUS_CAPACITY_MOD))
        {
            removeAttribOrSkillModModifier(
                player,
                FOCUS_CAPACITY_MOD);
        }
        int focusCapacityDelta =
            Math.max(
                0,
                REQUIRED_FOCUS_CAPACITY -
                    getMaxAttrib(player, FOCUS));
        if (focusCapacityDelta > 0)
        {
            addAttribModifier(
                player,
                FOCUS_CAPACITY_MOD,
                FOCUS,
                focusCapacityDelta,
                FOCUS_CAPACITY_DURATION,
                0.0f,
                0.0f,
                true,
                false,
                false);
        }

        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            setWoundExact(
                player,
                ATTRIBUTES[index],
                0);
            int preparedCurrent =
                index == 0
                    ? PREPARED_HEALTH_CURRENT
                    : index == 1
                        ? PREPARED_ACTION_CURRENT
                        : getMaxAttrib(
                            player,
                            ATTRIBUTES[index]);
            setAttrib(
                player,
                ATTRIBUTES[index],
                preparedCurrent);
        }
        boolean prepared =
            hasCommand(player, COMMAND) &&
            hasExactPreparedVector(player);
        if (!prepared)
        {
            String failed =
                buildStatus(player).replace(' ', '_');
            boolean restored = restore(player);
            return
                "error=quickHealPreparationFailed restored=" +
                restored +
                " failedStatus=" + failed;
        }
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " +
            buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(player, lifecycle);
        return ownership == null
            ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return
                "action=cleanup alreadyClean=true restored=true";
        }
        String ownership =
            validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(player))
        {
            removeObjVar(player, ROOT);
            return
                "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true cleared=true";
        }
        boolean restored = restore(player);
        return
            "action=cleanup alreadyClean=false restored=" +
            restored +
            (restored
                ? ""
                : " observed=" +
                    buildStatus(player).replace(' ', '_'));
    }

    private boolean restore(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        if (hasAttribModifier(player, FOCUS_CAPACITY_MOD))
        {
            removeAttribOrSkillModModifier(
                player,
                FOCUS_CAPACITY_MOD);
        }
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            int attribute = ATTRIBUTES[index];
            setWoundExact(player, attribute, 0);
            int originalMax =
                getIntObjVar(
                    player,
                    originalPath(index, "max"));
            if (getMaxAttrib(player, attribute) != originalMax)
            {
                setMaxAttrib(
                    player,
                    attribute,
                    originalMax);
            }
            setWoundExact(
                player,
                attribute,
                getIntObjVar(
                    player,
                    originalPath(index, "wound")));
            setAttrib(
                player,
                attribute,
                getIntObjVar(
                    player,
                    originalPath(index, "current")));
        }
        if (getIntObjVar(player, ORIGINAL_COMMAND) == 1 &&
            !hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        else if (
            getIntObjVar(player, ORIGINAL_COMMAND) == 0 &&
            hasCommand(player, COMMAND))
        {
            revokeCommand(player, COMMAND);
        }
        int xpDelta =
            getIntObjVar(player, ORIGINAL_MEDICAL_XP) -
            getExperiencePoints(player, "medical");
        if (xpDelta != 0)
        {
            grantExperiencePoints(
                player,
                xp.MEDICAL,
                xpDelta);
        }

        boolean restored = true;
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            int attribute = ATTRIBUTES[index];
            int observedMax =
                attribute == FOCUS
                    ? getUnmodifiedMaxAttrib(
                        player,
                        attribute)
                    : getMaxAttrib(player, attribute);
            int observedCurrent =
                attribute == FOCUS
                    ? getUnmodifiedAttrib(
                        player,
                        attribute)
                    : getAttrib(player, attribute);
            restored =
                observedMax ==
                    getIntObjVar(
                        player,
                        originalPath(index, "max")) &&
                observedCurrent >=
                    getIntObjVar(
                        player,
                        originalPath(index, "current")) &&
                getAttribWound(player, attribute) ==
                    getIntObjVar(
                        player,
                        originalPath(index, "wound")) &&
                restored;
        }
        restored =
            getExperiencePoints(player, "medical") ==
                getIntObjVar(player, ORIGINAL_MEDICAL_XP) &&
            restored;
        removeObjVar(player, ROOT);
        return restored;
    }

    private boolean hasExactPreparedVector(obj_id player)
        throws InterruptedException
    {
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            int attribute = ATTRIBUTES[index];
            int maximum = getMaxAttrib(player, attribute);
            int current = getAttrib(player, attribute);
            if (getAttribWound(player, attribute) != 0 ||
                (index < 2 &&
                    (current <
                        (index == 0
                            ? PREPARED_HEALTH_CURRENT
                            : PREPARED_ACTION_CURRENT) ||
                        maximum - current < 150)) ||
                (index >= 2 && current != maximum))
            {
                return false;
            }
        }
        return true;
    }

    private boolean setWoundExact(
        obj_id player,
        int attribute,
        int requested)
        throws InterruptedException
    {
        int current = getAttribWound(player, attribute);
        if (current > requested)
        {
            healWound(player, attribute, current - requested);
        }
        else if (current < requested)
        {
            addWound(player, attribute, requested - current);
        }
        return getAttribWound(player, attribute) == requested;
    }

    private String recover(obj_id player)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            return "error=fixtureActive";
        }
        resurrect(player);
        setAttrib(
            player,
            HEALTH,
            getWoundedMaxAttrib(player, HEALTH));
        setAttrib(
            player,
            ACTION,
            getWoundedMaxAttrib(player, ACTION));
        setAttrib(
            player,
            MIND,
            getWoundedMaxAttrib(player, MIND));
        setLocomotion(player, LOCOMOTION_STANDING);
        setPostureClientImmediate(player, POSTURE_UPRIGHT);
        return
            "action=recover restored=" +
            (!isIncapacitated(player) &&
                getAttrib(player, HEALTH) > 0 &&
                getAttrib(player, ACTION) > 0 &&
                getAttrib(player, MIND) > 0) +
            " " + buildStatus(player);
    }

    private String validateOwnership(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(
            getStringObjVar(player, LIFECYCLE))
                ? null
                : "error=lifecycleMismatch";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_COMMAND) ||
            !hasObjVar(player, ORIGINAL_MEDICAL_XP))
        {
            return false;
        }
        for (int index = 0;
            index < ATTRIBUTES.length;
            ++index)
        {
            if (!hasObjVar(
                    player,
                    originalPath(index, "max")) ||
                !hasObjVar(
                    player,
                    originalPath(index, "current")) ||
                !hasObjVar(
                    player,
                    originalPath(index, "wound")))
            {
                return false;
            }
        }
        return true;
    }

    private String originalPath(int index, String value)
    {
        return
            ROOT + ".original." +
            ATTRIBUTE_NAMES[index] + "." + value;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        return
            "player=" + player +
            " command=" + hasCommand(player, COMMAND) +
            " health=" + getAttrib(player, HEALTH) +
            " healthMax=" + getMaxAttrib(player, HEALTH) +
            " action=" + getAttrib(player, ACTION) +
            " actionMax=" + getMaxAttrib(player, ACTION) +
            " mind=" + getAttrib(player, MIND) +
            " mindMax=" + getMaxAttrib(player, MIND) +
            " fixtureFocusCapacity=" +
                hasAttribModifier(
                    player,
                    FOCUS_CAPACITY_MOD) +
            " focus=" + getAttrib(player, FOCUS) +
            " focusMax=" + getMaxAttrib(player, FOCUS) +
            " willpower=" + getAttrib(player, WILLPOWER) +
            " willpowerMax=" +
                getMaxAttrib(player, WILLPOWER) +
            " focusWound=" +
                getAttribWound(player, FOCUS) +
            " willpowerWound=" +
                getAttribWound(player, WILLPOWER) +
            " incapacitated=" +
                isIncapacitated(player) +
            " posture=" + getPosture(player) +
            " medicalXp=" +
                getExperiencePoints(player, "medical") +
            " handlerCalls=" +
                getIntOrZero(
                    player,
                    ROOT + ".handlerCalls") +
            " handlerEntered=" +
                getIntOrZero(
                    player,
                    ROOT + ".handlerEntered") +
            " target=" +
                getStringOrNone(
                    player,
                    ROOT + ".target") +
            " observedFocus=" +
                getIntOrZero(
                    player,
                    ROOT + ".focus") +
            " mindCost=" +
                getIntOrZero(
                    player,
                    ROOT + ".mindCost") +
            " healPower=" +
                getIntOrZero(
                    player,
                    ROOT + ".healPower") +
            " healthHealed=" +
                getIntOrZero(
                    player,
                    ROOT + ".healthHealed") +
            " actionHealed=" +
                getIntOrZero(
                    player,
                    ROOT + ".actionHealed") +
            " focusWounds=" +
                getIntOrZero(
                    player,
                    ROOT + ".focusWounds") +
            " willpowerWounds=" +
                getIntOrZero(
                    player,
                    ROOT + ".willpowerWounds") +
            " medicalXpDelta=" +
                getIntOrZero(
                    player,
                    ROOT + ".medicalXpDelta") +
            " outcome=" +
                getStringOrNone(
                    player,
                    ROOT + ".outcome");
    }

    private int getIntOrZero(obj_id player, String name)
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

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        String[] leaves =
        {
            "handlerCalls",
            "handlerEntered",
            "target",
            "focus",
            "mindCost",
            "healPower",
            "healthHealed",
            "actionHealed",
            "focusWounds",
            "willpowerWounds",
            "medicalXpDelta",
            "outcome"
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
        return
            lifecycle != null &&
            lifecycle.matches("[A-Za-z0-9._-]{8,64}");
    }
}
