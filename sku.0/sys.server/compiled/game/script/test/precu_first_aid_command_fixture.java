package script.test;

import script.obj_id;
import script.library.dot;
import script.library.utils;

/**
 * Identity-bound and reversible fixture for Publish 14.1 first aid.
 *
 * The connected client remains the sole command owner. Preparation adds one
 * private bleeding effect, exact tier-II injury treatment, and a temporary
 * command grant. Cleanup removes only that effect and restores all preimages.
 */
public class precu_first_aid_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String COMMAND = "firstAid";
    private static final String MOD = "healing_injury_treatment";
    private static final int TREATMENT = 35;
    private static final String DOT_ID = "precu_first_aid_fixture";
    private static final int BLEEDING_STRENGTH = 90;
    private static final String ROOT = "precu.firstAidCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalCommand";
    private static final String ORIGINAL_MOD = ROOT + ".originalMod";
    private static final String APPLIED_MOD_DELTA =
        ROOT + ".appliedModDelta";
    private static final String ORIGINAL_HEALTH =
        ROOT + ".originalHealth";
    private static final String ORIGINAL_MIND =
        ROOT + ".originalMind";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <lifecycle>";

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
            String ownership = validateOwnership(player, lifecycle);
            if (ownership == null &&
                getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            if (ownership != null &&
                !ownership.equals("error=fixtureAbsent"))
            {
                return ownership;
            }
        }
        if (dot.isBleeding(player))
        {
            return "error=existingBleeding";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_COMMAND,
            hasCommand(player, COMMAND) ? 1 : 0);
        setObjVar(
            player,
            ORIGINAL_MOD,
            getSkillStatMod(player, MOD));
        setObjVar(
            player,
            ORIGINAL_HEALTH,
            getAttrib(player, HEALTH));
        setObjVar(
            player,
            ORIGINAL_MIND,
            getAttrib(player, MIND));
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, "medical"));
        resetTelemetry(player);

        if (!hasCommand(player, COMMAND))
        {
            grantCommand(player, COMMAND);
        }
        int modDelta =
            TREATMENT - getSkillStatMod(player, MOD);
        if (modDelta != 0 &&
            !applySkillStatisticModifier(player, MOD, modDelta))
        {
            boolean restored = restore(player);
            return "error=skillModPreparationFailed restored=" +
                restored;
        }
        setObjVar(player, APPLIED_MOD_DELTA, modDelta);

        String dotRoot = dot.getDotScriptVarName(DOT_ID);
        utils.setScriptVar(
            player,
            dotRoot + dot.VAR_TYPE,
            dot.DOT_BLEEDING);
        utils.setScriptVar(
            player,
            dotRoot + dot.VAR_ATTRIBUTE,
            HEALTH);
        utils.setScriptVar(
            player,
            dotRoot + dot.VAR_STRENGTH,
            BLEEDING_STRENGTH);
        utils.setScriptVar(
            player,
            dotRoot + dot.VAR_DURATION,
            600);
        utils.setScriptVar(
            player,
            dotRoot + dot.VAR_TIME_START,
            getGameTime());
        boolean applied =
            dot.isBleeding(player) &&
            dot.getDotStrength(player, DOT_ID) ==
                BLEEDING_STRENGTH;
        boolean commandReady =
            hasCommand(player, COMMAND);
        int observedTreatment =
            getSkillStatMod(player, MOD);
        int observedBleedingStrength =
            dot.getDotStrength(player, DOT_ID);
        if (!commandReady ||
            observedTreatment != TREATMENT ||
            !applied ||
            observedBleedingStrength != BLEEDING_STRENGTH)
        {
            boolean restored = restore(player);
            return
                "error=firstAidPreparationFailed" +
                " command=" + commandReady +
                " treatment=" + observedTreatment +
                " dotApplied=" + applied +
                " bleedingStrength=" +
                    observedBleedingStrength +
                " restored=" + restored;
        }
        setObjVar(player, PREPARED, 1);
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
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(player))
        {
            clearFixtureVariables(player);
            return
                "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true cleared=true";
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
        boolean restored = true;
        if (dot.getDotStrength(player, DOT_ID) >= 0)
        {
            restored =
                dot.removeDotEffect(player, DOT_ID, false) &&
                restored;
        }
        int modDelta = hasObjVar(player, APPLIED_MOD_DELTA)
            ? getIntObjVar(player, APPLIED_MOD_DELTA)
            : 0;
        if (modDelta != 0)
        {
            restored =
                applySkillStatisticModifier(
                    player,
                    MOD,
                    -modDelta) &&
                restored;
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
        int originalHealth =
            getIntObjVar(player, ORIGINAL_HEALTH);
        int originalMind =
            getIntObjVar(player, ORIGINAL_MIND);
        setAttrib(player, HEALTH, originalHealth);
        setAttrib(player, MIND, originalMind);
        restored =
            getAttrib(player, HEALTH) == originalHealth &&
            getAttrib(player, MIND) == originalMind &&
            getSkillStatMod(player, MOD) ==
                getIntObjVar(player, ORIGINAL_MOD) &&
            getExperiencePoints(player, "medical") ==
                getIntObjVar(player, ORIGINAL_MEDICAL_XP) &&
            !dot.isBleeding(player) &&
            restored;
        clearFixtureVariables(player);
        return restored;
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
        return
            hasObjVar(player, ORIGINAL_COMMAND) &&
            hasObjVar(player, ORIGINAL_MOD) &&
            hasObjVar(player, ORIGINAL_HEALTH) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MEDICAL_XP);
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        return
            "player=" + player +
            " command=" + hasCommand(player, COMMAND) +
            " treatment=" + getSkillStatMod(player, MOD) +
            " bleeding=" + dot.isBleeding(player) +
            " bleedingStrength=" +
                dot.getDotStrength(player, DOT_ID) +
            " health=" + getAttrib(player, HEALTH) +
            " mind=" + getAttrib(player, MIND) +
            " medicalXp=" +
                getExperiencePoints(player, "medical") +
            " handlerCalls=" +
                getIntOrZero(player, ROOT + ".handlerCalls") +
            " handlerEntered=" +
                getIntOrZero(player, ROOT + ".handlerEntered") +
            " target=" +
                getStringOrNone(player, ROOT + ".target") +
            " requestedReduction=" +
                getIntOrZero(
                    player,
                    ROOT + ".requestedReduction") +
            " reportedReduction=" +
                getIntOrZero(
                    player,
                    ROOT + ".reportedReduction") +
            " mindCost=" +
                getIntOrZero(player, ROOT + ".mindCost") +
            " bleedingAfter=" +
                getIntOrZero(player, ROOT + ".bleedingAfter") +
            " outcome=" +
                getStringOrNone(player, ROOT + ".outcome");
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
            "treatment",
            "requestedReduction",
            "reportedReduction",
            "mindCost",
            "bleedingAfter",
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

    private void clearFixtureVariables(obj_id player)
        throws InterruptedException
    {
        String[] leaves =
        {
            "lifecycle",
            "prepared",
            "originalCommand",
            "originalMod",
            "appliedModDelta",
            "originalHealth",
            "originalMind",
            "originalMedicalXp",
            "handlerCalls",
            "handlerEntered",
            "target",
            "treatment",
            "requestedReduction",
            "reportedReduction",
            "mindCost",
            "bleedingAfter",
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
        if (hasObjVar(player, ROOT))
        {
            removeObjVar(player, ROOT);
        }
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        return
            lifecycle != null &&
            lifecycle.matches("[A-Za-z0-9._-]{8,64}");
    }
}
