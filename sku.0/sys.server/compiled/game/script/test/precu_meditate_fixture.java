package script.test;

import script.obj_id;
import script.library.dot;
import script.library.meditation;
import script.library.skill;
import script.library.utils;

/** Identity-bound reversible fixture for the Publish 14.1 meditate lifecycle. */
public class precu_meditate_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "meditate";
    private static final String DOT_ID = "precu_meditate_fixture_bleed";
    private static final int BLEEDING_STRENGTH = 100;
    private static final String ROOT = "precu.meditateFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_POSTURE = ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String[] SKILLS =
    {
        "combat_brawler",
        "combat_brawler_novice",
        "combat_brawler_unarmed_01",
        "combat_brawler_unarmed_02",
        "combat_brawler_unarmed_03",
        "combat_brawler_unarmed_04",
        "combat_unarmed",
        "combat_unarmed_novice"
    };
    private static final String USAGE =
        "usage: prepare|arm|status|cleanup <playerOid> <32-hex-lifecycle>";

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
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("arm"))
        {
            return arm(player, args[2]);
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
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }
        if (hasAnyFixtureSkill(player) || hasCommand(player, COMMAND) ||
            dot.isBleeding(player) || meditation.isMeditating(player))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_POSTURE, getPosture(player));
        setObjVar(player, ORIGINAL_LOCOMOTION, getLocomotion(player));
        if (!grantSkills(player) || !hasCommand(player, COMMAND) ||
            meditation.getMeditationSkillMod(player) != 15)
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        setObjVar(player, PREPARED, 1);
        String armed = arm(player, lifecycle);
        if (!armed.startsWith("action=arm"))
        {
            boolean restored = restore(player);
            return "error=armFailed restored=" + restored +
                " observed=" + armed;
        }
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String arm(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        meditation.endMeditation(player, false);
        if (dot.getDotStrength(player, DOT_ID) >= 0)
        {
            dot.removeDotEffect(player, DOT_ID, false);
        }
        if (dot.isBleeding(player))
        {
            return "error=foreignBleedingPresent";
        }
        setPostureClientImmediate(player, POSTURE_SITTING);
        String dotRoot = dot.getDotScriptVarName(DOT_ID);
        utils.setScriptVar(player, dotRoot + dot.VAR_TYPE, dot.DOT_BLEEDING);
        utils.setScriptVar(player, dotRoot + dot.VAR_ATTRIBUTE, HEALTH);
        utils.setScriptVar(player, dotRoot + dot.VAR_STRENGTH,
            BLEEDING_STRENGTH);
        utils.setScriptVar(player, dotRoot + dot.VAR_DURATION, 600);
        utils.setScriptVar(player, dotRoot + dot.VAR_TIME_START,
            getGameTime());
        boolean armed = getPosture(player) == POSTURE_SITTING &&
            dot.isBleeding(player) &&
            dot.getDotStrength(player, DOT_ID) == BLEEDING_STRENGTH;
        return armed
            ? "action=arm " + buildStatus(player)
            : "error=armBoundaryFailed " + buildStatus(player);
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
        meditation.endMeditation(player, false);
        if (dot.getDotStrength(player, DOT_ID) >= 0)
        {
            dot.removeDotEffect(player, DOT_ID, false);
        }
        revokeSkills(player);
        int originalPosture = getIntObjVar(player, ORIGINAL_POSTURE);
        int originalLocomotion = getIntObjVar(player, ORIGINAL_LOCOMOTION);
        if (getPosture(player) != originalPosture)
        {
            setPostureClientImmediate(player, originalPosture);
        }
        if (getLocomotion(player) != originalLocomotion)
        {
            setLocomotion(player, originalLocomotion);
        }
        boolean restored = !meditation.isMeditating(player) &&
            dot.getDotStrength(player, DOT_ID) < 0 &&
            !hasAnyFixtureSkill(player) && !hasCommand(player, COMMAND) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            getPosture(player) == originalPosture &&
            getLocomotion(player) == originalLocomotion;
        if (restored)
        {
            removeObjVar(player, ROOT);
        }
        return restored;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        return
            "player=" + player +
            " command=" + hasCommand(player, COMMAND) +
            " skillBits=" + buildSkillBits(player) +
            " meditateMod=" + meditation.getMeditationSkillMod(player) +
            " posture=" + getPosture(player) +
            " locomotion=" + getLocomotion(player) +
            " meditating=" + meditation.isMeditating(player) +
            " bleeding=" + dot.isBleeding(player) +
            " bleedingStrength=" + dot.getDotStrength(player, DOT_ID) +
            " availablePoints=" + skill.getAvailableSkillPoints(player) +
            " originalPoints=" + readSnapshot(player, ORIGINAL_POINTS) +
            " originalPosture=" + readSnapshot(player, ORIGINAL_POSTURE) +
            " originalLocomotion=" +
                readSnapshot(player, ORIGINAL_LOCOMOTION) +
            " snapshotComplete=" + hasCompleteSnapshot(player);
    }

    private int readSnapshot(obj_id player, String path)
        throws InterruptedException
    {
        return hasObjVar(player, path) ? getIntObjVar(player, path) : -1;
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

    private String buildSkillBits(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(player, skillName) ? "1" : "0";
        }
        return bits;
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
        return hasObjVar(player, LIFECYCLE) && hasObjVar(player, PROTOCOL) &&
            hasObjVar(player, PREPARED) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_POSTURE) &&
            hasObjVar(player, ORIGINAL_LOCOMOTION);
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
