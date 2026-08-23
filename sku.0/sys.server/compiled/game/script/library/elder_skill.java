package script.library;

import script.*;

/**
 * Shared apprenticeship and temporary Elder-skill policy.
 *
 * Elder boxes intentionally have no commands, schematics, or skill modifiers.
 * Their only durable effect is ownership of a hidden zero-point skill row until
 * its absolute expiry timestamp.  Bonuses can be designed later without
 * changing the lifecycle or purchase protocol wired here.
 */
public class elder_skill extends script.base_script
{
    public elder_skill()
    {
    }

    public static final String STRING_TABLE = "precu_elder";
    public static final String PLAYER_LIFECYCLE_SCRIPT =
        "player.skill.elder_skills";
    public static final String OBJVAR_ROOT = "precu.elderSkills";
    public static final String OBJVAR_EXPIRY_ROOT = OBJVAR_ROOT + ".expiresAt";
    public static final String OBJVAR_MENTOR_NEXT_AWARD =
        OBJVAR_ROOT + ".mentoring.nextAwardAt";
    public static final String PARAM_SKILL_NAME = "elderSkillName";
    public static final String PARAM_EXPIRES_AT = "elderExpiresAt";

    public static final int ELDER_DURATION_SECONDS = 30 * 24 * 60 * 60;
    public static final int ELDER_APPRENTICESHIP_XP_COST_PLACEHOLDER = 100;
    public static final int PLAYER_TRAINING_APPRENTICESHIP_XP_AWARD = 10;

    // Deliberately conservative placeholders.  Designers can tune rate and
    // level-gap scaling independently without altering the qualifying hook.
    public static final int GROUP_MENTOR_BASE_XP_PLACEHOLDER = 1;
    public static final int GROUP_MENTOR_LEVELS_PER_SCALE_PLACEHOLDER = 10;
    public static final int GROUP_MENTOR_LEVEL_GAP_BONUS_PLACEHOLDER = 0;
    public static final int GROUP_MENTOR_AWARD_COOLDOWN_SECONDS = 5 * 60;
    public static final float GROUP_MENTOR_MAX_RANGE = 128.0f;

    public static final String[] TRAINER_TYPES =
    {
        "trainer_rifleman",
        "trainer_pistol",
        "trainer_carbine",
        "trainer_unarmed",
        "trainer_1hsword",
        "trainer_2hsword",
        "trainer_polearm",
        "trainer_bountyhunter",
        "trainer_commando",
        "trainer_smuggler",
        "trainer_squadleader",
        "trainer_doctor",
        "trainer_combatmedic",
        "trainer_ranger",
        "trainer_creaturehandler",
        "trainer_bioengineer",
        "trainer_architect",
        "trainer_armorsmith",
        "trainer_weaponsmith",
        "trainer_chef",
        "trainer_tailor",
        "trainer_droidengineer",
        "trainer_merchant",
        "trainer_shipwright",
        "trainer_dancer",
        "trainer_musician",
        "trainer_imagedesigner",
        "trainer_politician"
    };

    public static final String[] ELDER_SKILLS =
    {
        "elder_combat_rifleman",
        "elder_combat_pistol",
        "elder_combat_carbine",
        "elder_combat_unarmed",
        "elder_combat_1hsword",
        "elder_combat_2hsword",
        "elder_combat_polearm",
        "elder_combat_bountyhunter",
        "elder_combat_commando",
        "elder_combat_smuggler",
        "elder_outdoors_squadleader",
        "elder_science_doctor",
        "elder_science_combatmedic",
        "elder_outdoors_ranger",
        "elder_outdoors_creaturehandler",
        "elder_outdoors_bio_engineer",
        "elder_crafting_architect",
        "elder_crafting_armorsmith",
        "elder_crafting_weaponsmith",
        "elder_crafting_chef",
        "elder_crafting_tailor",
        "elder_crafting_droidengineer",
        "elder_crafting_merchant",
        "elder_crafting_shipwright",
        "elder_social_dancer",
        "elder_social_musician",
        "elder_social_imagedesigner",
        "elder_social_politician"
    };

    public static final String[] MASTER_SKILLS =
    {
        "combat_rifleman_master",
        "combat_pistol_master",
        "combat_carbine_master",
        "combat_unarmed_master",
        "combat_1hsword_master",
        "combat_2hsword_master",
        "combat_polearm_master",
        "combat_bountyhunter_master",
        "combat_commando_master",
        "combat_smuggler_master",
        "outdoors_squadleader_master",
        "science_doctor_master",
        "science_combatmedic_master",
        "outdoors_ranger_master",
        "outdoors_creaturehandler_master",
        "outdoors_bio_engineer_master",
        "crafting_architect_master",
        "crafting_armorsmith_master",
        "crafting_weaponsmith_master",
        "crafting_chef_master",
        "crafting_tailor_master",
        "crafting_droidengineer_master",
        "crafting_merchant_master",
        "crafting_shipwright_master",
        "social_dancer_master",
        "social_musician_master",
        "social_imagedesigner_master",
        "social_politician_master"
    };

    public static final String[] DISPLAY_KEYS =
    {
        "rifleman_n",
        "pistoleer_n",
        "carbineer_n",
        "teras_kasi_artist_n",
        "fencer_n",
        "swordsman_n",
        "pikeman_n",
        "bounty_hunter_n",
        "commando_n",
        "smuggler_n",
        "squad_leader_n",
        "doctor_n",
        "combat_medic_n",
        "ranger_n",
        "creature_handler_n",
        "bio_engineer_n",
        "architect_n",
        "armorsmith_n",
        "weaponsmith_n",
        "chef_n",
        "tailor_n",
        "droid_engineer_n",
        "merchant_n",
        "shipwright_n",
        "dancer_n",
        "musician_n",
        "image_designer_n",
        "politician_n"
    };

    public static boolean isAdvancedProfessionTrainer(obj_id trainer)
        throws InterruptedException
    {
        return getTrainerIndex(trainer) >= 0;
    }

    public static int getTrainerIndex(obj_id trainer) throws InterruptedException
    {
        if (!isIdValid(trainer) || !exists(trainer) ||
            !hasObjVar(trainer, "trainer"))
        {
            return -1;
        }
        return getIndex(TRAINER_TYPES, getStringObjVar(trainer, "trainer"));
    }

    public static String getElderSkillForTrainer(obj_id trainer)
        throws InterruptedException
    {
        int index = getTrainerIndex(trainer);
        return index < 0 ? null : ELDER_SKILLS[index];
    }

    public static String getMasterSkillForElder(String elderSkill)
        throws InterruptedException
    {
        int index = getIndex(ELDER_SKILLS, elderSkill);
        return index < 0 ? null : MASTER_SKILLS[index];
    }

    public static String getDisplayKeyForElder(String elderSkill)
        throws InterruptedException
    {
        int index = getIndex(ELDER_SKILLS, elderSkill);
        return index < 0 ? null : DISPLAY_KEYS[index];
    }

    public static boolean isElderSkill(String skillName)
        throws InterruptedException
    {
        return getIndex(ELDER_SKILLS, skillName) >= 0;
    }

    public static String getExpiryObjVar(String skillName)
        throws InterruptedException
    {
        return OBJVAR_EXPIRY_ROOT + "." + skillName;
    }

    public static int getElderExpiry(obj_id player, String skillName)
        throws InterruptedException
    {
        String path = getExpiryObjVar(skillName);
        return isIdValid(player) && isElderSkill(skillName) &&
            hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }

    public static boolean ensureLifecycle(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return false;
        }
        if (!hasLifecycleState(player))
        {
            return true;
        }
        if (!hasScript(player, PLAYER_LIFECYCLE_SCRIPT))
        {
            attachScript(player, PLAYER_LIFECYCLE_SCRIPT);
            return hasScript(player, PLAYER_LIFECYCLE_SCRIPT);
        }
        reconcile(player);
        return true;
    }

    public static boolean hasLifecycleState(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return false;
        }
        for (String elderSkill : ELDER_SKILLS)
        {
            if (hasSkill(player, elderSkill) ||
                hasObjVar(player, getExpiryObjVar(elderSkill)))
            {
                return true;
            }
        }
        return false;
    }

    public static void reconcile(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int now = getCalendarTime();
        for (String elderSkill : ELDER_SKILLS)
        {
            reconcileSkill(player, elderSkill, now);
        }
    }

    public static void reconcileSkill(obj_id player, String elderSkill, int now)
        throws InterruptedException
    {
        if (!isIdValid(player) || !isElderSkill(elderSkill))
        {
            return;
        }
        String expiryPath = getExpiryObjVar(elderSkill);
        boolean ownsSkill = hasSkill(player, elderSkill);
        boolean ownsMaster = hasSkill(player, getMasterSkillForElder(elderSkill));
        int expiresAt = hasObjVar(player, expiryPath) ?
            getIntObjVar(player, expiryPath) : 0;

        if (!ownsSkill)
        {
            if (hasObjVar(player, expiryPath))
            {
                removeObjVar(player, expiryPath);
            }
            return;
        }
        if (!ownsMaster || expiresAt <= 0 || expiresAt <= now)
        {
            expireSkill(player, elderSkill, expiresAt, now);
            return;
        }
        scheduleExpiry(player, elderSkill, expiresAt, now);
    }

    public static boolean isCurrentExpiry(
        obj_id player,
        String elderSkill,
        int expectedExpiry) throws InterruptedException
    {
        return isIdValid(player) && isElderSkill(elderSkill) &&
            expectedExpiry > 0 &&
            getElderExpiry(player, elderSkill) == expectedExpiry;
    }

    public static void handleExpiry(
        obj_id player,
        String elderSkill,
        int expectedExpiry) throws InterruptedException
    {
        if (!isCurrentExpiry(player, elderSkill, expectedExpiry))
        {
            return;
        }
        int now = getCalendarTime();
        if (expectedExpiry > now)
        {
            scheduleExpiry(player, elderSkill, expectedExpiry, now);
            return;
        }
        expireSkill(player, elderSkill, expectedExpiry, now);
    }

    private static void expireSkill(
        obj_id player,
        String elderSkill,
        int expectedExpiry,
        int now) throws InterruptedException
    {
        String expiryPath = getExpiryObjVar(elderSkill);
        if (hasSkill(player, elderSkill))
        {
            revokeSkill(player, elderSkill);
        }
        if (hasSkill(player, elderSkill))
        {
            // Fail closed but preserve the durable marker for a retry if the
            // engine temporarily refuses the revoke during a load boundary.
            int retryAt = Math.max(now + 5, expectedExpiry);
            setObjVar(player, expiryPath, retryAt);
            scheduleExpiry(player, elderSkill, retryAt, now);
            return;
        }
        if (hasObjVar(player, expiryPath))
        {
            removeObjVar(player, expiryPath);
        }
        sendSystemMessage(player, new string_id(STRING_TABLE, "elder_expired"));
    }

    public static boolean scheduleExpiry(
        obj_id player,
        String elderSkill,
        int expiresAt,
        int now) throws InterruptedException
    {
        if (!isIdValid(player) || !hasScript(player, PLAYER_LIFECYCLE_SCRIPT) ||
            !isElderSkill(elderSkill) || expiresAt <= 0)
        {
            return false;
        }
        dictionary params = new dictionary();
        params.put(PARAM_SKILL_NAME, elderSkill);
        params.put(PARAM_EXPIRES_AT, expiresAt);
        float delay = Math.max(0.1f, expiresAt - now);
        return messageTo(
            player, "handleElderSkillExpiry", params, delay, false);
    }

    public static int trainOrRenew(obj_id player, String elderSkill)
        throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player) ||
            !isElderSkill(elderSkill))
        {
            return -1;
        }
        String masterSkill = getMasterSkillForElder(elderSkill);
        if (!hasSkill(player, masterSkill))
        {
            return -2;
        }
        dictionary costs = getSkillPrerequisiteExperience(elderSkill);
        if (costs == null || costs.size() != 1 ||
            costs.getInt(xp.APPRENTICESHIP) !=
                ELDER_APPRENTICESHIP_XP_COST_PLACEHOLDER)
        {
            return -3;
        }
        if (getExperiencePoints(player, xp.APPRENTICESHIP) <
            ELDER_APPRENTICESHIP_XP_COST_PLACEHOLDER)
        {
            return -4;
        }

        if (!hasScript(player, PLAYER_LIFECYCLE_SCRIPT))
        {
            attachScript(player, PLAYER_LIFECYCLE_SCRIPT);
        }
        if (!hasScript(player, PLAYER_LIFECYCLE_SCRIPT))
        {
            return -6;
        }

        int now = getCalendarTime();
        reconcileSkill(player, elderSkill, now);
        boolean renewing = hasSkill(player, elderSkill);
        int priorExpiry = renewing ? getElderExpiry(player, elderSkill) : 0;
        boolean paidAndGranted;
        if (renewing)
        {
            paidAndGranted = skill.deductXpCostForSkillPurchase(
                player, elderSkill);
        }
        else
        {
            paidAndGranted = skill.purchaseSkill(player, elderSkill);
        }
        if (!paidAndGranted)
        {
            return -5;
        }

        int expiresAt = now + ELDER_DURATION_SECONDS;
        setObjVar(player, getExpiryObjVar(elderSkill), expiresAt);
        if (!scheduleExpiry(player, elderSkill, expiresAt, now))
        {
            if (renewing)
            {
                setObjVar(player, getExpiryObjVar(elderSkill), priorExpiry);
                scheduleExpiry(player, elderSkill, priorExpiry, now);
            }
            else
            {
                revokeSkill(player, elderSkill);
                removeObjVar(player, getExpiryObjVar(elderSkill));
            }
            grantExperiencePoints(
                player,
                xp.APPRENTICESHIP,
                ELDER_APPRENTICESHIP_XP_COST_PLACEHOLDER);
            return -6;
        }
        return renewing ? 2 : 1;
    }

    public static int awardPlayerTrainingExperience(
        obj_id teacher,
        obj_id student) throws InterruptedException
    {
        if (!isIdValid(teacher) || !isIdValid(student) || teacher == student ||
            !isPlayer(teacher) || !isPlayer(student) ||
            !teacher.isLoaded() || !student.isLoaded())
        {
            return 0;
        }
        int teacherStationId = getPlayerStationId(teacher);
        int studentStationId = getPlayerStationId(student);
        if (teacherStationId <= 0 || studentStationId <= 0 ||
            teacherStationId == studentStationId)
        {
            return 0;
        }
        return xp.grant(
            teacher,
            xp.APPRENTICESHIP,
            PLAYER_TRAINING_APPRENTICESHIP_XP_AWARD,
            true);
    }

    public static int awardGroupMentorPresenceExperience(obj_id mentor)
        throws InterruptedException
    {
        if (!isEligibleMentoringMember(mentor))
        {
            return 0;
        }
        int now = getCalendarTime();
        if (hasObjVar(mentor, OBJVAR_MENTOR_NEXT_AWARD) &&
            getIntObjVar(mentor, OBJVAR_MENTOR_NEXT_AWARD) > now)
        {
            return 0;
        }
        obj_id groupId = getGroupObject(mentor);
        if (!group.isGroupObject(groupId))
        {
            return 0;
        }
        obj_id[] members = getGroupMemberIds(groupId);
        if (members == null || members.length < 2)
        {
            return 0;
        }
        int mentorStationId = getPlayerStationId(mentor);
        if (mentorStationId <= 0)
        {
            return 0;
        }
        int mentorLevel = skill.getPrecuEncounterDifficulty(mentor);
        int bestGap = 0;
        for (obj_id learner : members)
        {
            if (learner == mentor || !isEligibleMentoringMember(learner))
            {
                continue;
            }
            int learnerStationId = getPlayerStationId(learner);
            float distance = getDistance(mentor, learner);
            if (learnerStationId <= 0 ||
                learnerStationId == mentorStationId || distance < 0.0f ||
                distance > GROUP_MENTOR_MAX_RANGE)
            {
                continue;
            }
            int learnerLevel = skill.getPrecuEncounterDifficulty(learner);
            if (learnerLevel > 0 && mentorLevel > learnerLevel)
            {
                bestGap = Math.max(bestGap, mentorLevel - learnerLevel);
            }
        }
        if (bestGap <= 0)
        {
            return 0;
        }
        int scaledSteps = bestGap /
            GROUP_MENTOR_LEVELS_PER_SCALE_PLACEHOLDER;
        int award = GROUP_MENTOR_BASE_XP_PLACEHOLDER +
            scaledSteps * GROUP_MENTOR_LEVEL_GAP_BONUS_PLACEHOLDER;
        int granted = xp.grant(
            mentor, xp.APPRENTICESHIP, Math.max(1, award), false);
        if (granted > 0)
        {
            setObjVar(
                mentor,
                OBJVAR_MENTOR_NEXT_AWARD,
                now + GROUP_MENTOR_AWARD_COOLDOWN_SECONDS);
        }
        return granted;
    }

    private static boolean isEligibleMentoringMember(obj_id player)
        throws InterruptedException
    {
        return isIdValid(player) && exists(player) && player.isLoaded() &&
            isPlayer(player) && !player.isBeingDestroyed() && !isDead(player) &&
            !isIncapacitated(player);
    }

    private static int getIndex(String[] values, String value)
    {
        if (values == null || value == null)
        {
            return -1;
        }
        for (int i = 0; i < values.length; ++i)
        {
            if (value.equals(values[i]))
            {
                return i;
            }
        }
        return -1;
    }
}
