package script.test;

import script.library.healing;
import script.library.xp;
import script.obj_id;

/**
 * Identity-bound ServerConsole fixture for Publish 14.1 tendDamage and
 * tendWound.
 *
 * The fixture owns healer-state snapshots and command telemetry only. The
 * established healDamage and healWound fixtures provide disposable patients,
 * while the connected client remains the sole queue owner.
 */
public class precu_tending_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String MEDIC_NOVICE = "science_medic_novice";
    private static final String TEND_DAMAGE = "tendDamage";
    private static final String TEND_WOUND = "tendWound";
    private static final String ROOT = "precu.tendingCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_NOVICE = ROOT + ".originalNovice";
    private static final String ORIGINAL_TEND_DAMAGE =
        ROOT + ".originalTendDamage";
    private static final String ORIGINAL_TEND_WOUND =
        ROOT + ".originalTendWound";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_FOCUS = ROOT + ".originalFocus";
    private static final String ORIGINAL_WILLPOWER =
        ROOT + ".originalWillpower";
    private static final String ORIGINAL_FOCUS_WOUND =
        ROOT + ".originalFocusWound";
    private static final String ORIGINAL_WILLPOWER_WOUND =
        ROOT + ".originalWillpowerWound";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String EXPECTED_DAMAGE_COST =
        ROOT + ".expectedDamageCost";
    private static final String EXPECTED_WOUND_COST =
        ROOT + ".expectedWoundCost";
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <lifecycle>";

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

        String action = args[0];
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
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
            if (ownership == null && isFixtureActive(player))
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent") &&
                isFixtureActive(player))
            {
                return ownership;
            }
            // Packed player objvars retain their leaf values after removal.
            // An inactive fixture owns no live state, so preparation safely
            // overwrites its lifecycle, snapshots, and telemetry.
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_NOVICE,
            hasSkill(player, MEDIC_NOVICE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_TEND_DAMAGE,
            hasCommand(player, TEND_DAMAGE) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_TEND_WOUND,
            hasCommand(player, TEND_WOUND) ? 1 : 0);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_FOCUS, getAttrib(player, FOCUS));
        setObjVar(
            player,
            ORIGINAL_WILLPOWER,
            getAttrib(player, WILLPOWER));
        setObjVar(
            player,
            ORIGINAL_FOCUS_WOUND,
            getAttribWound(player, FOCUS));
        setObjVar(
            player,
            ORIGINAL_WILLPOWER_WOUND,
            getAttribWound(player, WILLPOWER));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, xp.MEDICAL));
        resetTelemetry(player);

        boolean skillReady =
            hasSkill(player, MEDIC_NOVICE) ||
            grantSkill(player, MEDIC_NOVICE);
        if (!skillReady ||
            !hasCommand(player, TEND_DAMAGE) ||
            !hasCommand(player, TEND_WOUND) ||
            getSkillStatMod(player, "healing_injury_treatment") <= 0 ||
            getSkillStatMod(player, "healing_wound_treatment") <= 0)
        {
            boolean restored = restore(player);
            return "error=medicPreparationFailed restored=" + restored;
        }

        setWoundExact(player, FOCUS, 0);
        setWoundExact(player, WILLPOWER, 0);
        setAttrib(player, MIND, getMaxAttrib(player, MIND));
        setAttrib(player, FOCUS, getMaxAttrib(player, FOCUS));
        setAttrib(player, WILLPOWER, getMaxAttrib(player, WILLPOWER));
        if (getAttribWound(player, FOCUS) != 0 ||
            getAttribWound(player, WILLPOWER) != 0 ||
            getAttrib(player, MIND) != getMaxAttrib(player, MIND) ||
            getAttrib(player, FOCUS) != getMaxAttrib(player, FOCUS) ||
            getAttrib(player, WILLPOWER) !=
                getMaxAttrib(player, WILLPOWER))
        {
            String failed = buildStatus(player).replace(' ', '_');
            boolean restored = restore(player);
            return "error=hamPreparationFailed restored=" + restored +
                " failedStatus=" + failed;
        }

        setObjVar(
            player,
            EXPECTED_DAMAGE_COST,
            healing.getTendMindCost(player, false));
        setObjVar(
            player,
            EXPECTED_WOUND_COST,
            healing.getTendMindCost(player, true));
        return "action=prepare resumed=false " +
            buildStatus(player);
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
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        if (getCurrentCommand(player) != 0)
        {
            return "error=commandPending " + buildStatus(player);
        }
        return "action=cleanup alreadyClean=false restored=" +
            restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        boolean complete =
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_TEND_DAMAGE) &&
            hasObjVar(player, ORIGINAL_TEND_WOUND) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_FOCUS) &&
            hasObjVar(player, ORIGINAL_WILLPOWER) &&
            hasObjVar(player, ORIGINAL_FOCUS_WOUND) &&
            hasObjVar(player, ORIGINAL_WILLPOWER_WOUND) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP);
        if (!complete)
        {
            return false;
        }

        setWoundExact(
            player,
            FOCUS,
            getIntObjVar(player, ORIGINAL_FOCUS_WOUND));
        setWoundExact(
            player,
            WILLPOWER,
            getIntObjVar(player, ORIGINAL_WILLPOWER_WOUND));
        setAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MIND));
        setAttrib(player, FOCUS, getIntObjVar(player, ORIGINAL_FOCUS));
        setAttrib(
            player,
            WILLPOWER,
            getIntObjVar(player, ORIGINAL_WILLPOWER));

        int originalXp = getIntObjVar(player, ORIGINAL_MEDICAL_XP);
        int currentXp = getExperiencePoints(player, xp.MEDICAL);
        if (currentXp != originalXp)
        {
            grantExperiencePoints(
                player,
                xp.MEDICAL,
                originalXp - currentXp);
        }
        boolean restored =
            getAttribWound(player, FOCUS) ==
                getIntObjVar(player, ORIGINAL_FOCUS_WOUND) &&
            getAttribWound(player, WILLPOWER) ==
                getIntObjVar(player, ORIGINAL_WILLPOWER_WOUND) &&
            getAttrib(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MIND) &&
            getAttrib(player, FOCUS) ==
                getIntObjVar(player, ORIGINAL_FOCUS) &&
            getAttrib(player, WILLPOWER) ==
                getIntObjVar(player, ORIGINAL_WILLPOWER) &&
            getExperiencePoints(player, xp.MEDICAL) == originalXp;

        if (getIntObjVar(player, ORIGINAL_NOVICE) == 0 &&
            hasSkill(player, MEDIC_NOVICE))
        {
            revokeSkill(player, MEDIC_NOVICE);
        }
        restoreCommand(
            player,
            TEND_DAMAGE,
            getIntObjVar(player, ORIGINAL_TEND_DAMAGE) == 1);
        restoreCommand(
            player,
            TEND_WOUND,
            getIntObjVar(player, ORIGINAL_TEND_WOUND) == 1);

        clearFixtureVariables(player);
        return restored;
    }

    private void restoreCommand(
        obj_id player,
        String command,
        boolean originallyPresent)
        throws InterruptedException
    {
        if (originallyPresent && !hasCommand(player, command))
        {
            grantCommand(player, command);
        }
        else if (!originallyPresent && hasCommand(player, command))
        {
            revokeCommand(player, command);
        }
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        return null;
    }

    private boolean isFixtureActive(obj_id player)
        throws InterruptedException
    {
        return hasSkill(player, MEDIC_NOVICE) &&
            hasCommand(player, TEND_DAMAGE) &&
            hasCommand(player, TEND_WOUND) &&
            getSkillStatMod(player, "healing_injury_treatment") > 0 &&
            getSkillStatMod(player, "healing_wound_treatment") > 0 &&
            hasObjVar(player, EXPECTED_DAMAGE_COST) &&
            hasObjVar(player, EXPECTED_WOUND_COST) &&
            getIntObjVar(player, EXPECTED_DAMAGE_COST) > 0 &&
            getIntObjVar(player, EXPECTED_WOUND_COST) > 0;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        int damageEntered = readInt(player, ".tendDamageEntered");
        int woundEntered = readInt(player, ".tendWoundEntered");
        return "player=" + player +
            " currentCommand=" + getCurrentCommand(player) +
            " novice=" + hasSkill(player, MEDIC_NOVICE) +
            " tendDamageCommand=" + hasCommand(player, TEND_DAMAGE) +
            " tendWoundCommand=" + hasCommand(player, TEND_WOUND) +
            " injuryTreatment=" +
                getSkillStatMod(player, "healing_injury_treatment") +
            " woundTreatment=" +
                getSkillStatMod(player, "healing_wound_treatment") +
            " healerMind=" + getAttrib(player, MIND) +
            " healerFocus=" + getAttrib(player, FOCUS) +
            " healerWillpower=" + getAttrib(player, WILLPOWER) +
            " healerFocusWound=" + getAttribWound(player, FOCUS) +
            " healerWillpowerWound=" +
                getAttribWound(player, WILLPOWER) +
            " medicalXp=" + getExperiencePoints(player, xp.MEDICAL) +
            " medicalXpDelta=" +
                (hasObjVar(player, ORIGINAL_MEDICAL_XP)
                    ? getExperiencePoints(player, xp.MEDICAL) -
                        getIntObjVar(player, ORIGINAL_MEDICAL_XP)
                    : 0) +
            " expectedDamageCost=" +
                readKey(player, EXPECTED_DAMAGE_COST) +
            " expectedWoundCost=" +
                readKey(player, EXPECTED_WOUND_COST) +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " tendDamageEntered=" + damageEntered +
            " tendDamageOutcome=" +
                readString(player, ".tendDamageOutcome") +
            " tendDamageHealthHeal=" +
                readInt(player, ".tendDamageHealthHeal") +
            " tendDamageActionHeal=" +
                readInt(player, ".tendDamageActionHeal") +
            " tendDamageMindCost=" +
                readInt(player, ".tendDamageMindCost") +
            " tendDamageExpectedMindCost=" +
                readInt(player, ".tendDamageExpectedMindCost") +
            " tendDamageFocusWoundCost=" +
                readInt(player, ".tendDamageFocusWoundCost") +
            " tendDamageWillpowerWoundCost=" +
                readInt(player, ".tendDamageWillpowerWoundCost") +
            " tendDamageMedicalXp=" +
                readInt(player, ".tendDamageMedicalXp") +
            " tendWoundEntered=" + woundEntered +
            " queueDelta=" +
                (damageEntered > 0 && woundEntered > 0
                    ? woundEntered - damageEntered
                    : 0) +
            " tendWoundOutcome=" +
                readString(player, ".tendWoundOutcome") +
            " tendWoundAttribute=" +
                readInt(player, ".tendWoundAttribute") +
            " tendWoundHeal=" +
                readInt(player, ".tendWoundHeal") +
            " tendWoundMindCost=" +
                readInt(player, ".tendWoundMindCost") +
            " tendWoundExpectedMindCost=" +
                readInt(player, ".tendWoundExpectedMindCost") +
            " tendWoundFocusWoundCost=" +
                readInt(player, ".tendWoundFocusWoundCost") +
            " tendWoundWillpowerWoundCost=" +
                readInt(player, ".tendWoundWillpowerWoundCost") +
            " tendWoundMedicalXp=" +
                readInt(player, ".tendWoundMedicalXp");
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        return readKey(player, ROOT + suffix);
    }

    private int readKey(obj_id player, String key)
        throws InterruptedException
    {
        return hasObjVar(player, key) ? getIntObjVar(player, key) : 0;
    }

    private String readString(obj_id player, String suffix)
        throws InterruptedException
    {
        String key = ROOT + suffix;
        return hasObjVar(player, key)
            ? getStringObjVar(player, key)
            : "none";
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        String[] integerKeys =
        {
            EXPECTED_DAMAGE_COST,
            EXPECTED_WOUND_COST,
            ROOT + ".handlerCalls",
            ROOT + ".tendDamageEntered",
            ROOT + ".tendDamageHealthHeal",
            ROOT + ".tendDamageActionHeal",
            ROOT + ".tendDamageMindCost",
            ROOT + ".tendDamageExpectedMindCost",
            ROOT + ".tendDamageFocusWoundCost",
            ROOT + ".tendDamageWillpowerWoundCost",
            ROOT + ".tendDamageMedicalXp",
            ROOT + ".tendWoundEntered",
            ROOT + ".tendWoundAttribute",
            ROOT + ".tendWoundHeal",
            ROOT + ".tendWoundMindCost",
            ROOT + ".tendWoundExpectedMindCost",
            ROOT + ".tendWoundFocusWoundCost",
            ROOT + ".tendWoundWillpowerWoundCost",
            ROOT + ".tendWoundMedicalXp"
        };
        for (String key : integerKeys)
        {
            setObjVar(player, key, 0);
        }
        setObjVar(player, ROOT + ".tendDamageOutcome", "none");
        setObjVar(player, ROOT + ".tendWoundOutcome", "none");
    }

    private void clearFixtureVariables(obj_id player)
        throws InterruptedException
    {
        String[] keys =
        {
            LIFECYCLE,
            ORIGINAL_NOVICE,
            ORIGINAL_TEND_DAMAGE,
            ORIGINAL_TEND_WOUND,
            ORIGINAL_MIND,
            ORIGINAL_FOCUS,
            ORIGINAL_WILLPOWER,
            ORIGINAL_FOCUS_WOUND,
            ORIGINAL_WILLPOWER_WOUND,
            ORIGINAL_MEDICAL_XP,
            EXPECTED_DAMAGE_COST,
            EXPECTED_WOUND_COST,
            ROOT + ".handlerCalls",
            ROOT + ".tendDamageEntered",
            ROOT + ".tendDamageOutcome",
            ROOT + ".tendDamageHealthHeal",
            ROOT + ".tendDamageActionHeal",
            ROOT + ".tendDamageMindCost",
            ROOT + ".tendDamageExpectedMindCost",
            ROOT + ".tendDamageFocusWoundCost",
            ROOT + ".tendDamageWillpowerWoundCost",
            ROOT + ".tendDamageMedicalXp",
            ROOT + ".tendWoundEntered",
            ROOT + ".tendWoundOutcome",
            ROOT + ".tendWoundAttribute",
            ROOT + ".tendWoundHeal",
            ROOT + ".tendWoundMindCost",
            ROOT + ".tendWoundExpectedMindCost",
            ROOT + ".tendWoundFocusWoundCost",
            ROOT + ".tendWoundWillpowerWoundCost",
            ROOT + ".tendWoundMedicalXp"
        };
        for (String key : keys)
        {
            if (hasObjVar(player, key))
            {
                removeObjVar(player, key);
            }
        }
        if (hasObjVar(player, ROOT))
        {
            removeObjVar(player, ROOT);
        }
    }

    private boolean setWoundExact(
        obj_id target,
        int attribute,
        int requested)
        throws InterruptedException
    {
        int current = getAttribWound(target, attribute);
        if (current > requested)
        {
            healWound(target, attribute, current - requested);
        }
        else if (current < requested)
        {
            addWound(target, attribute, requested - current);
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
