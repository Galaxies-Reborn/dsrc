package script.library;

import script.dictionary;
import script.obj_id;

public class missions extends script.base_script
{
    public missions()
    {
    }
    public static final int STATE_DYNAMIC_PICKUP = 1;
    public static final int STATE_DYNAMIC_DROPOFF = 2;
    public static final int STATE_DYNAMIC_START = 3;
    public static final int STATE_DELIVER_PICKUP = 4;
    public static final int STATE_DELIVER_DROPOFF = 5;
    public static final int STATE_DEAD = 6;
    public static final int STATE_MISSION_COMPLETE = 7;
    public static final int BH_STAT_MIN = -1;
    public static final int WINS = 0;
    public static final int LOSSES = 1;
    public static final int ABORTS = 2;
    public static final int TIMEOUTS = 3;
    public static final int BH_STAT_MAX = 4;
    public static final int BOUNTY_FLAG_NONE = 0;
    public static final int BOUNTY_FLAG_SMUGGLER = 1;
    public static final String DAILY_MISSION_OBJVAR = "missions.daily";
    public static final String DAILY_MISSION_CLOCK_OBJVAR = "missions.dailyClock";
    public static final String PRECU_SKILL_TABLE = skill.TBL_SKILL;
    public static final int PRECU_MISSION_COMBAT_SCORE_MAX = skill.PRECU_COMBAT_SKILL_SCORE_MAX;
    public static final int PRECU_ADVANCED_COMBAT_SKILL_WEIGHT = skill.PRECU_ADVANCED_COMBAT_SKILL_WEIGHT;
    public static final float PRECU_MISSION_MEMBER_REWARD_BONUS = 0.10f;
    public static final String PRECU_MISSION_GROUP_SIZE = "precuMission.groupSize";
    public static final String PRECU_MISSION_GROUP_COMBAT_SCORE = "precuMission.groupCombatScore";
    public static final String PRECU_MISSION_GROUP_AVERAGE_SCORE = "precuMission.groupAverageCombatScore";
    public static final String PRECU_MISSION_CREDIT_MULTIPLIER = "precuMission.creditMultiplier";
    public static final String PRECU_MISSION_BASE_REWARD = "precuMission.baseReward";
    public static boolean isPrecuCombatSkillBox(String skillName) throws InterruptedException
    {
        return skill.isPrecuCombatSkillBox(skillName);
    }
    public static boolean isPrecuBaseCombatSkillBox(String skillName) throws InterruptedException
    {
        return skill.isPrecuBaseCombatSkillBox(skillName);
    }
    public static int getPrecuCombatSkillScore(obj_id player) throws InterruptedException
    {
        return skill.getPrecuCombatSkillScore(player);
    }
    public static dictionary getPrecuMissionGroupRating(obj_id player) throws InterruptedException
    {
        dictionary rating = new dictionary();
        obj_id groupId = getGroupObject(player);
        obj_id[] members = null;
        if (isIdValid(groupId))
        {
            members = getGroupMemberIds(groupId);
        }
        if (members == null || members.length == 0)
        {
            members = new obj_id[] { player };
        }
        int memberCount = 0;
        int totalScore = 0;
        for (obj_id member : members)
        {
            if (!isIdValid(member) || !exists(member) || !member.isLoaded() || !isPlayer(member))
            {
                continue;
            }
            memberCount++;
            totalScore += getPrecuCombatSkillScore(member);
        }
        if (memberCount < 1)
        {
            memberCount = 1;
            totalScore = getPrecuCombatSkillScore(player);
        }
        int averageScore = Math.round(totalScore / (float)memberCount);
        averageScore = Math.max(1, Math.min(averageScore, PRECU_MISSION_COMBAT_SCORE_MAX));
        float multiplier = 1.0f + ((memberCount - 1) * PRECU_MISSION_MEMBER_REWARD_BONUS) + (averageScore / 100.0f);
        rating.put(PRECU_MISSION_GROUP_SIZE, memberCount);
        rating.put(PRECU_MISSION_GROUP_COMBAT_SCORE, totalScore);
        rating.put(PRECU_MISSION_GROUP_AVERAGE_SCORE, averageScore);
        rating.put(PRECU_MISSION_CREDIT_MULTIPLIER, multiplier);
        return rating;
    }
    public static int getPrecuMissionGroupCombatScore(obj_id player) throws InterruptedException
    {
        return getPrecuMissionGroupRating(player).getInt(PRECU_MISSION_GROUP_AVERAGE_SCORE);
    }
    public static void applyPrecuMissionGroupReward(obj_id missionData, obj_id player) throws InterruptedException
    {
        if (!isIdValid(missionData))
        {
            return;
        }
        dictionary rating = getPrecuMissionGroupRating(player);
        int baseReward = getMissionReward(missionData);
        float multiplier = rating.getFloat(PRECU_MISSION_CREDIT_MULTIPLIER);
        int scaledReward = Math.max(baseReward, Math.round(baseReward * multiplier));
        setObjVar(missionData, PRECU_MISSION_BASE_REWARD, baseReward);
        setObjVar(missionData, PRECU_MISSION_GROUP_SIZE, rating.getInt(PRECU_MISSION_GROUP_SIZE));
        setObjVar(missionData, PRECU_MISSION_GROUP_COMBAT_SCORE, rating.getInt(PRECU_MISSION_GROUP_COMBAT_SCORE));
        setObjVar(missionData, PRECU_MISSION_GROUP_AVERAGE_SCORE, rating.getInt(PRECU_MISSION_GROUP_AVERAGE_SCORE));
        setObjVar(missionData, PRECU_MISSION_CREDIT_MULTIPLIER, multiplier);
        setMissionReward(missionData, scaledReward);
        LOG("PreCuMission", "reward mission=" + missionData + " base=" + baseReward + " members=" + rating.getInt(PRECU_MISSION_GROUP_SIZE) + " averageCombatScore=" + rating.getInt(PRECU_MISSION_GROUP_AVERAGE_SCORE) + " multiplier=" + multiplier + " scaled=" + scaledReward);
    }
    public static int getDailyMissionXpLimit() throws InterruptedException
    {
        return 0;
    }
    public static void sendBountyFail(obj_id hunter, obj_id target) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put("target", target);
        setObjVar(hunter, "intState", STATE_MISSION_COMPLETE);
        messageTo(hunter, "bountyFailure", params, 0, true);
    }
    public static void sendBountySuccess(obj_id hunter, obj_id target) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put("target", target);
        setObjVar(hunter, "intState", STATE_MISSION_COMPLETE);
        messageTo(hunter, "bountySuccess", params, 0, true);
    }
    public static void sendBountyIncomplete(obj_id hunter, obj_id target) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put("target", target);
        setObjVar(hunter, "intState", STATE_MISSION_COMPLETE);
        messageTo(hunter, "bountyIncomplete", params, 0, true);
    }
    public static void increaseBountyJediKillTracking(obj_id objPlayer, int stat) throws InterruptedException
    {
        if (stat <= missions.BH_STAT_MIN || stat >= missions.BH_STAT_MAX)
        {
            return;
        }
        int[] killData = new int[missions.BH_STAT_MAX];
        if (!hasObjVar(objPlayer, "bounty_hunter.jedi_kill_tracker"))
        {
            setObjVar(objPlayer, "bounty_hunter.jedi_kill_tracker", killData);
        }
        killData = getIntArrayObjVar(objPlayer, "bounty_hunter.jedi_kill_tracker");
        if (killData.length != missions.BH_STAT_MAX)
        {
            return;
        }
        killData[stat]++;
        setObjVar(objPlayer, "bounty_hunter.jedi_kill_tracker", killData);
    }
    public static int getPlayerDailyCount(obj_id player) throws InterruptedException
    {
        return 0;
    }
    public static void incrementDaily(obj_id player) throws InterruptedException
    {
    }
    public static boolean canEarnDailyMissionXp(obj_id player) throws InterruptedException
    {
        return false;
    }
    public static float alterMissionPayoutDivisor(obj_id player, float divisor, int missionLevel) throws InterruptedException
    {
        return divisor;
    }
    public static float alterMissionPayoutDivisorDaily(obj_id player, float divisor) throws InterruptedException
    {
        return divisor;
    }
    public static float alterMissionPayoutDivisorDaily(obj_id player) throws InterruptedException
    {
        return alterMissionPayoutDivisorDaily(player, 1.0f);
    }
    public static void initializeDailyOnLogin(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, DAILY_MISSION_OBJVAR) || hasObjVar(player, DAILY_MISSION_CLOCK_OBJVAR))
        {
            clearDailyObjVars(player);
        }
    }
    public static void clearDailyObjVars(obj_id player) throws InterruptedException
    {
        removeObjVar(player, DAILY_MISSION_OBJVAR);
        removeObjVar(player, DAILY_MISSION_CLOCK_OBJVAR);
    }
    public static boolean isDestroyMission(obj_id objMissionData) throws InterruptedException
    {
        if (!isIdValid(objMissionData))
        {
            return false;
        }
        String strTest = getMissionType(objMissionData);
        if (strTest.equals("destroy"))
        {
            return true;
        }
        return false;
    }
}
