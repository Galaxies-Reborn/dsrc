package script.test;

import script.obj_id;
import script.library.utils;

public class precu_stateful_surrender_fixture extends script.base_script
{
    private static final String MARKER = "precu.statefulSurrender.skill";

    public String executeProbe(String params) throws InterruptedException
    {
        if (params == null)
        {
            return usage();
        }
        String[] args = params.trim().split("\\s+");
        if (args.length != 3)
        {
            return usage();
        }

        String action = args[0];
        obj_id player;
        try
        {
            player = obj_id.getObjId(Long.parseLong(args[1]));
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (!isIdValid(player) || !player.isLoaded() ||
            !player.isAuthoritative() || !isPlayer(player))
        {
            return "error=playerNotAuthoritative";
        }

        String skillName = args[2];
        if (!isSupported(skillName))
        {
            return "error=unsupportedSkill";
        }

        if (action.equalsIgnoreCase("prepare"))
        {
            if (hasObjVar(player, MARKER))
            {
                return "error=fixtureAlreadyArmed";
            }
            if (hasSkill(player, skillName))
            {
                return "error=skillAlreadyOwned";
            }
            grantSkill(player, skillName);
            if (!hasSkill(player, skillName))
            {
                return "error=grantFailed";
            }
            setObjVar(player, MARKER, skillName);
            return status("prepare", player, skillName, true);
        }

        if (!hasObjVar(player, MARKER) ||
            !skillName.equals(getStringObjVar(player, MARKER)))
        {
            return "error=fixtureNotArmed";
        }

        if (action.equalsIgnoreCase("queue"))
        {
            boolean queued = queueCommand(
                player,
                getStringCrc("surrenderskill"),
                obj_id.NULL_ID,
                skillName,
                COMMAND_PRIORITY_IMMEDIATE);
            return status("queue", player, skillName, queued);
        }
        if (action.equalsIgnoreCase("verifyRemoved"))
        {
            boolean removed = !hasSkill(player, skillName);
            if (removed)
            {
                removeObjVar(player, MARKER);
            }
            return status("verifyRemoved", player, skillName, removed);
        }
        if (action.equalsIgnoreCase("verifyPilotBlocked"))
        {
            return status("verifyPilotBlocked", player, skillName,
                skillName.startsWith("pilot_") && hasSkill(player, skillName));
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            if (hasSkill(player, skillName))
            {
                if (skillName.startsWith("pilot_"))
                {
                    utils.setScriptVar(player, "revokePilotSkill", 1);
                }
                revokeSkill(player, skillName);
                utils.removeScriptVar(player, "revokePilotSkill");
            }
            removeObjVar(player, MARKER);
            return status("cleanup", player, skillName, !hasSkill(player, skillName));
        }
        return usage();
    }

    private boolean isSupported(String skillName)
    {
        return skillName.equals("combat_bountyhunter_investigation_03") ||
            skillName.equals("outdoors_squadleader_offense_03") ||
            skillName.equals("pilot_neutral_novice") ||
            skillName.equals("force_sensitive_combat_prowess_ranged_accuracy_01");
    }

    private String status(String action, obj_id player, String skillName, boolean passed)
        throws InterruptedException
    {
        return "action=" + action + " skill=" + skillName +
            " owned=" + hasSkill(player, skillName) +
            " marker=" + hasObjVar(player, MARKER) +
            " passed=" + passed;
    }

    private String usage()
    {
        return "usage: prepare|queue|verifyRemoved|verifyPilotBlocked|cleanup <playerOid> <skill>";
    }
}
