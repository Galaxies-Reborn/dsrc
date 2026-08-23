package script.test;

import script.*;
import script.library.elder_skill;
import script.library.group;
import script.library.skill;
import script.library.xp;

/** Read-only live fixture for the Elder/apprenticeship wiring. */
public class precu_elder_apprenticeship_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String USAGE = "usage: status <playerOid>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null || params.trim().length() == 0 ?
            new String[0] : params.trim().split("[ ]+");
        if (args.length != 2 || !"status".equalsIgnoreCase(args[0]))
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
        if (!isIdValid(player) || !exists(player) || !player.isLoaded() ||
            !isPlayer(player) || getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerUnavailableOrStationMismatch";
        }

        boolean rosterValid = elder_skill.TRAINER_TYPES.length == 28 &&
            elder_skill.ELDER_SKILLS.length == 28 &&
            elder_skill.MASTER_SKILLS.length == 28 &&
            elder_skill.DISPLAY_KEYS.length == 28;
        int validRows = 0;
        int activeSkills = 0;
        int scheduledSkills = 0;
        int now = getCalendarTime();
        for (int i = 0; i < elder_skill.ELDER_SKILLS.length; ++i)
        {
            String elderSkill = elder_skill.ELDER_SKILLS[i];
            int row = dataTableSearchColumnForString(
                elderSkill, "NAME", skill.TBL_SKILL);
            if (row >= 0 &&
                dataTableGetInt(skill.TBL_SKILL, row, "POINTS_REQUIRED") == 0 &&
                dataTableGetInt(skill.TBL_SKILL, row, "IS_HIDDEN") == 1 &&
                dataTableGetInt(skill.TBL_SKILL, row, "SEARCHABLE") == 0 &&
                elder_skill.MASTER_SKILLS[i].equals(
                    dataTableGetString(
                        skill.TBL_SKILL, row, "SKILLS_REQUIRED")) &&
                xp.APPRENTICESHIP.equals(
                    dataTableGetString(skill.TBL_SKILL, row, "XP_TYPE")) &&
                dataTableGetInt(skill.TBL_SKILL, row, "XP_COST") ==
                    elder_skill.ELDER_APPRENTICESHIP_XP_COST_PLACEHOLDER &&
                isEmpty(dataTableGetString(skill.TBL_SKILL, row, "COMMANDS")) &&
                isEmpty(dataTableGetString(skill.TBL_SKILL, row, "SKILL_MODS")) &&
                isEmpty(dataTableGetString(
                    skill.TBL_SKILL, row, "SCHEMATICS_GRANTED")) &&
                isEmpty(dataTableGetString(
                    skill.TBL_SKILL, row, "SCHEMATICS_REVOKED")))
            {
                ++validRows;
            }
            if (hasSkill(player, elderSkill))
            {
                ++activeSkills;
            }
            if (elder_skill.getElderExpiry(player, elderSkill) > now)
            {
                ++scheduledSkills;
            }
        }

        obj_id groupId = getGroupObject(player);
        int lowerMembers = 0;
        int playerLevel = skill.getPrecuEncounterDifficulty(player);
        if (group.isGroupObject(groupId))
        {
            obj_id[] members = getGroupMemberIds(groupId);
            if (members != null)
            {
                for (obj_id member : members)
                {
                    if (member != player && isIdValid(member) &&
                        exists(member) && member.isLoaded() && isPlayer(member) &&
                        getPlayerStationId(member) != PLAYER_STATION_ID &&
                        skill.getPrecuEncounterDifficulty(member) < playerLevel)
                    {
                        ++lowerMembers;
                    }
                }
            }
        }

        boolean lifecycleValid = activeSkills == scheduledSkills &&
            (activeSkills == 0 ||
                hasScript(player, elder_skill.PLAYER_LIFECYCLE_SCRIPT));
        boolean valid = rosterValid && validRows == 28 && lifecycleValid;
        return "action=status valid=" + valid +
            " rosterCount=" + elder_skill.ELDER_SKILLS.length +
            " validRows=" + validRows +
            " apprenticeshipXp=" +
                getExperiencePoints(player, xp.APPRENTICESHIP) +
            " activeElderSkills=" + activeSkills +
            " futureExpiries=" + scheduledSkills +
            " lifecycleScript=" +
                hasScript(player, elder_skill.PLAYER_LIFECYCLE_SCRIPT) +
            " combatScore=" + playerLevel +
            " lowerDifferentAccountGroupMembers=" + lowerMembers +
            " mentorCooldownUntil=" +
                (hasObjVar(player, elder_skill.OBJVAR_MENTOR_NEXT_AWARD) ?
                    getIntObjVar(
                        player, elder_skill.OBJVAR_MENTOR_NEXT_AWARD) : 0);
    }

    private boolean isEmpty(String value)
    {
        return value == null || value.length() == 0;
    }
}
