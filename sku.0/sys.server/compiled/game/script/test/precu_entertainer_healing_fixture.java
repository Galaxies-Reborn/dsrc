package script.test;

import script.library.performance;
import script.library.skill;
import script.obj_id;

/**
 * Identity-bound ServerConsole acceptance fixture for the Publish 14.1
 * entertainer Mind, Focus, Willpower, and battle-fatigue healing path.
 */
public class precu_entertainer_healing_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT = "precu.entertainerHealingFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_WOUNDS = ROOT + ".originalWounds";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_XP = ROOT + ".originalXp";
    private static final String ORIGINAL_SKILLS = ROOT + ".originalSkills";
    private static final String[] SKILLS =
    {
        "social_entertainer_novice",
        "social_entertainer_healing_01",
        "social_entertainer_healing_02",
        "social_entertainer_healing_03",
        "social_entertainer_healing_04"
    };
    private static final int[] MIND_ATTRIBUTES =
        new int[] { MIND, FOCUS, WILLPOWER };
    private static final int CONTROLLED_WOUND = 100;
    private static final int CONTROLLED_SHOCK = 100;
    private static final int FLOURISH_COUNT = 2;
    private static final int EXPECTED_HEAL = 3;
    private static final int EXPECTED_XP = 6;
    private static final String USAGE =
        "usage: prepare|pulse|status|cleanup <playerOid> <lifecycle>";

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
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (action.equalsIgnoreCase("pulse"))
        {
            return pulse(player, args[2]);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (action.equalsIgnoreCase("cleanup"))
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
            String ownership = validateOwnership(player, lifecycle);
            if (ownership != null)
            {
                return ownership;
            }
            return "action=prepare resumed=true " + buildStatus(player);
        }

        int[] originalWounds = new int[MIND_ATTRIBUTES.length];
        for (int index = 0; index < MIND_ATTRIBUTES.length; ++index)
        {
            originalWounds[index] =
                getAttribWound(player, MIND_ATTRIBUTES[index]);
        }
        int[] originalSkills = new int[SKILLS.length];
        for (int index = 0; index < SKILLS.length; ++index)
        {
            originalSkills[index] = hasSkill(player, SKILLS[index]) ? 1 : 0;
        }

        setObjVar(player, ORIGINAL_WOUNDS, originalWounds);
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(
            player,
            ORIGINAL_XP,
            getExperiencePoints(player, "entertainer_healing"));
        setObjVar(player, ORIGINAL_SKILLS, originalSkills);
        setObjVar(player, LIFECYCLE, lifecycle);

        if (!grantFixtureSkills(player) || !resetControlledState(player))
        {
            cleanup(player, lifecycle);
            return "error=fixtureSetupFailed";
        }
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String pulse(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!resetControlledState(player))
        {
            return "error=fixtureResetFailed " + buildStatus(player);
        }

        int performanceIndex =
            performance.lookupPerformanceIndex(-1788534963, "basic", 0);
        if (performanceIndex <= 0)
        {
            return "error=basicDanceMissing " + buildStatus(player);
        }
        int amountHealed = performance.applyPrecuEntertainerHealing(
            player,
            player,
            performance.PERFORMANCE_TYPE_DANCE,
            performanceIndex,
            FLOURISH_COUNT,
            true,
            1.0f);
        boolean passed =
            amountHealed == EXPECTED_XP &&
            getAttribWound(player, MIND) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getAttribWound(player, FOCUS) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getAttribWound(player, WILLPOWER) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getShockWound(player) == CONTROLLED_SHOCK - EXPECTED_HEAL;
        return "action=pulse passed=" + passed +
            " amountHealed=" + amountHealed +
            " xpDelivery=asynchronous" +
            " " + buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        boolean passed =
            getAttribWound(player, MIND) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getAttribWound(player, FOCUS) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getAttribWound(player, WILLPOWER) ==
                CONTROLLED_WOUND - EXPECTED_HEAL &&
            getShockWound(player) == CONTROLLED_SHOCK - EXPECTED_HEAL &&
            getExperiencePoints(player, "entertainer_healing") ==
                EXPECTED_XP;
        return "action=status passed=" + passed +
            " " + buildStatus(player);
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }

        int[] originalWounds = getIntArrayObjVar(player, ORIGINAL_WOUNDS);
        boolean restored =
            originalWounds != null &&
            originalWounds.length == MIND_ATTRIBUTES.length;
        if (restored)
        {
            for (int index = 0; index < MIND_ATTRIBUTES.length; ++index)
            {
                restored =
                    setWound(
                        player,
                        MIND_ATTRIBUTES[index],
                        originalWounds[index]) &&
                    restored;
            }
        }
        restored =
            setShockWound(player, getIntObjVar(player, ORIGINAL_SHOCK)) &&
            restoreExperience(player) &&
            restoreSkills(player) &&
            restored;
        if (!restored)
        {
            return "error=cleanupRestoreFailed " + buildStatus(player);
        }

        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true " +
            "mindWound=" + getAttribWound(player, MIND) +
            " focusWound=" + getAttribWound(player, FOCUS) +
            " willpowerWound=" + getAttribWound(player, WILLPOWER) +
            " shock=" + getShockWound(player) +
            " healingXp=" +
                getExperiencePoints(player, "entertainer_healing");
    }

    private boolean grantFixtureSkills(obj_id player)
        throws InterruptedException
    {
        boolean granted = true;
        for (String skillName : SKILLS)
        {
            if (!hasSkill(player, skillName))
            {
                granted = skill.grantSkillToPlayer(player, skillName) && granted;
            }
        }
        return granted;
    }

    private boolean restoreSkills(obj_id player) throws InterruptedException
    {
        int[] originalSkills = getIntArrayObjVar(player, ORIGINAL_SKILLS);
        if (originalSkills == null || originalSkills.length != SKILLS.length)
        {
            return false;
        }
        boolean restored = true;
        for (int index = SKILLS.length - 1; index >= 0; --index)
        {
            if (originalSkills[index] == 0 && hasSkill(player, SKILLS[index]))
            {
                revokeSkill(player, SKILLS[index]);
                restored = !hasSkill(player, SKILLS[index]) && restored;
            }
        }
        return restored;
    }

    private boolean resetControlledState(obj_id player)
        throws InterruptedException
    {
        boolean reset = true;
        for (int attribute : MIND_ATTRIBUTES)
        {
            reset = setWound(player, attribute, CONTROLLED_WOUND) && reset;
        }
        reset =
            setShockWound(player, CONTROLLED_SHOCK) &&
            setExperience(player, 0) &&
            reset;
        return reset;
    }

    private boolean restoreExperience(obj_id player)
        throws InterruptedException
    {
        return setExperience(player, getIntObjVar(player, ORIGINAL_XP));
    }

    private boolean setExperience(obj_id player, int requested)
        throws InterruptedException
    {
        int current = getExperiencePoints(player, "entertainer_healing");
        if (current != requested)
        {
            grantExperiencePoints(
                player,
                "entertainer_healing",
                requested - current);
        }
        return
            getExperiencePoints(player, "entertainer_healing") == requested;
    }

    private boolean setWound(obj_id player, int attribute, int requested)
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
        return getAttribWound(player, attribute) == requested;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        int performanceIndex =
            performance.lookupPerformanceIndex(-1788534963, "basic", 0);
        return "player=" + player +
            " performanceIndex=" + performanceIndex +
            " healMindBase=" +
                performance.getPerformanceHealWoundMod(performanceIndex) +
            " healShockBase=" +
                performance.getPerformanceHealShockMod(performanceIndex) +
            " danceWoundSkill=" +
                getSkillStatMod(player, "healing_dance_wound") +
            " danceShockSkill=" +
                getSkillStatMod(player, "healing_dance_shock") +
            " mindWound=" + getAttribWound(player, MIND) +
            " focusWound=" + getAttribWound(player, FOCUS) +
            " willpowerWound=" + getAttribWound(player, WILLPOWER) +
            " shock=" + getShockWound(player) +
            " healingXp=" +
                getExperiencePoints(player, "entertainer_healing");
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, ORIGINAL_WOUNDS) ||
            !hasObjVar(player, ORIGINAL_SHOCK) ||
            !hasObjVar(player, ORIGINAL_XP) ||
            !hasObjVar(player, ORIGINAL_SKILLS))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
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
