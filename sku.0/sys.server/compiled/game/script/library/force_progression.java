package script.library;

import script.dictionary;
import script.obj_id;
import script.prose_package;
import script.string_id;

import java.util.Vector;

public class force_progression extends script.base_script
{
    public static final String CONFIG_SECTION = "GameServer";
    public static final String CONFIG_KEY = "rebornForceProgressionMode";
    public static final String MODE_SHADOW = "shadow";
    public static final String MODE_REPLACEMENT = "replacement";

    public static final int SCHEMA_VERSION = 1;
    public static final int REQUIRED_ECHO_EVENTS = 8;
    public static final int REQUIRED_THREAD_EVENTS = 3;
    public static final int REQUIRED_CONVERGENCE_EVENTS = 1;
    public static final int REQUIRED_TOTAL_EVENTS = 12;
    public static final int MAX_ATTUNEMENT_EVENTS = 32;
    public static final int REQUIRED_ROUTE_FAMILIES = 6;
    public static final int REQUIRED_PLANETS = 5;
    public static final int MONTHLY_HINT_COOLDOWN_SECONDS = 2592000;
    public static final int BARTENDER_HINT_CHANCE_PERCENT = 15;
    public static final int BARTENDER_ROLL_COOLDOWN_SECONDS = 86400;

    public static final int FS_TREE_COUNT = 4;
    public static final int FS_BRANCH_COUNT = 16;
    public static final int FS_TIER_BOX_COUNT = 64;
    public static final int FS_POINTS_PER_TIER_BOX = 1;
    public static final int FS_POINTS_PER_QUEST_CHAIN = 4;
    public static final int FS_POINTS_REQUIRED_FOR_ALL_TREES = 64;
    public static final int MAX_FS_QUEST_CHAINS = 24;
    public static final int MAX_FS_POINTS_EARNED = 96;
    public static final int QUEST_TRIAL_WRONG_COOLDOWN_SECONDS = 300;

    public static final int QUEST_RESULT_INVALID = -1;
    public static final int QUEST_RESULT_WAIT = 0;
    public static final int QUEST_RESULT_ADVANCED = 1;
    public static final int QUEST_RESULT_WRONG = 2;
    public static final int QUEST_RESULT_COMPLETED = 3;

    public static final String STF = "quest/force_sensitive/reborn_progression";
    public static final String QUEST_NETWORK_TABLE = "datatables/reborn/force_progression/quest_network.iff";

    public static final String EVENT_ECHO = "ECHO";
    public static final String EVENT_THREAD = "THREAD";
    public static final String EVENT_CONVERGENCE = "CONVERGENCE";

    public static final String ROUTE_SERVICE = "SERVICE";
    public static final String ROUTE_DISCOVERY = "DISCOVERY";
    public static final String ROUTE_CRAFT = "CRAFT";
    public static final String ROUTE_LORE = "LORE";
    public static final String ROUTE_RESTRAINT = "RESTRAINT";
    public static final String ROUTE_FELLOWSHIP = "FELLOWSHIP";

    public static final String PERSISTENT_ROOT = "reborn.forceProgression";
    public static final String VAR_SCHEMA = PERSISTENT_ROOT + ".schema";
    public static final String VAR_EVENTS = PERSISTENT_ROOT + ".events";
    public static final String VAR_QUARANTINE = PERSISTENT_ROOT + ".quarantineReason";
    public static final String VAR_HINT_LAST_USED = PERSISTENT_ROOT + ".hintLastUsed";
    public static final String VAR_BARTENDER_LAST_ROLL = PERSISTENT_ROOT + ".bartenderLastRoll";
    public static final String VAR_FS_POINTS_EARNED = PERSISTENT_ROOT + ".fsPointsEarned";
    public static final String VAR_FS_POINTS_SPENT = PERSISTENT_ROOT + ".fsPointsSpent";
    public static final String VAR_FS_AWARD_IDS = PERSISTENT_ROOT + ".fsAwardIds";
    public static final String VAR_FS_PURCHASE_IDS = PERSISTENT_ROOT + ".fsPurchaseIds";
    public static final String VAR_FS_MIGRATION_CREDIT = PERSISTENT_ROOT + ".fsMigrationCredit";
    public static final String VAR_QUEST_COMPLETIONS = PERSISTENT_ROOT + ".questCompletions";
    public static final String VAR_QUEST_STATE = PERSISTENT_ROOT + ".questState";
    public static final String VAR_MIGRATION_VERSION = PERSISTENT_ROOT + ".migrationVersion";
    public static final String VAR_AWAKENING_PENDING = PERSISTENT_ROOT + ".awakeningPending";
    public static final String VAR_AWAKENING_NOTIFIED = PERSISTENT_ROOT + ".awakeningNotified";
    public static final String VAR_PADAWAN_INITIALIZED = PERSISTENT_ROOT + ".padawanInitialized";
    public static final String VAR_NPC_QUEST_ID = "reborn.forceProgression.questNpcId";
    public static final String VAR_NPC_OWNER = "reborn.forceProgression.questNpcOwner";
    public static final int MIGRATION_VERSION = 1;

    private static final String[] ROUTE_FAMILIES =
    {
        ROUTE_SERVICE,
        ROUTE_DISCOVERY,
        ROUTE_CRAFT,
        ROUTE_LORE,
        ROUTE_RESTRAINT,
        ROUTE_FELLOWSHIP
    };

    public static final String[] FS_TREE_MASTERS =
    {
        "force_sensitive_combat_prowess_master",
        "force_sensitive_enhanced_reflexes_master",
        "force_sensitive_crafting_mastery_master",
        "force_sensitive_heightened_senses_master"
    };

    public static final String[] FS_TREE_NOVICES =
    {
        "force_sensitive_combat_prowess_novice",
        "force_sensitive_enhanced_reflexes_novice",
        "force_sensitive_crafting_mastery_novice",
        "force_sensitive_heightened_senses_novice"
    };

    public static final String[] FS_BRANCHES =
    {
        "force_sensitive_combat_prowess_ranged_accuracy",
        "force_sensitive_combat_prowess_ranged_speed",
        "force_sensitive_combat_prowess_melee_accuracy",
        "force_sensitive_combat_prowess_melee_speed",
        "force_sensitive_enhanced_reflexes_ranged_defense",
        "force_sensitive_enhanced_reflexes_melee_defense",
        "force_sensitive_enhanced_reflexes_vehicle_control",
        "force_sensitive_enhanced_reflexes_survival",
        "force_sensitive_crafting_mastery_experimentation",
        "force_sensitive_crafting_mastery_assembly",
        "force_sensitive_crafting_mastery_repair",
        "force_sensitive_crafting_mastery_technique",
        "force_sensitive_heightened_senses_healing",
        "force_sensitive_heightened_senses_surveying",
        "force_sensitive_heightened_senses_persuasion",
        "force_sensitive_heightened_senses_luck"
    };

    private static final String[] RETIRED_LEGACY_QUESTS =
    {
        "old_man_initial",
        "old_man_force_crystal",
        "two_military",
        "loot_datapad_1",
        "got_datapad",
        "loot_datapad_2",
        "got_datapad_2",
        "fs_theater_camp",
        "fs_theater_final",
        "old_man_final",
        "fs_village_elder",
        "fs_dath_woman_talk"
    };

    private static final String[] CREDITABLE_PLANETS =
    {
        "corellia",
        "dantooine",
        "dathomir",
        "endor",
        "lok",
        "naboo",
        "rori",
        "talus",
        "tatooine",
        "yavin4",
        "galactic"
    };

    private static final String[] LEGACY_EVENT_FRAGMENTS =
    {
        "old_man",
        "oldman",
        "sith",
        "mellichae",
        "fs_village"
    };

    public static boolean isShadowEnabled() throws InterruptedException
    {
        String mode = getConfigSetting(CONFIG_SECTION, CONFIG_KEY);
        return MODE_SHADOW.equals(mode);
    }

    public static boolean isReplacementEnabled() throws InterruptedException
    {
        String mode = getConfigSetting(CONFIG_SECTION, CONFIG_KEY);
        return MODE_REPLACEMENT.equals(mode);
    }

    public static boolean isObservationEnabled() throws InterruptedException
    {
        return isShadowEnabled() || isReplacementEnabled();
    }

    public static boolean isLegacyUnlockAllowed() throws InterruptedException
    {
        return !isReplacementEnabled();
    }

    public static boolean isRetiredLegacyQuest(String questName) throws InterruptedException
    {
        if (!isReplacementEnabled() || questName == null)
        {
            return false;
        }
        return contains(RETIRED_LEGACY_QUESTS, normalizeLower(questName));
    }

    public static void reconcilePlayer(obj_id player) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }
        ensureState(player);
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return;
        }
        boolean existingForceSensitive = getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE;
        if (!hasObjVar(player, VAR_MIGRATION_VERSION))
        {
            if (existingForceSensitive)
            {
                migrateLegacyForceSensitiveState(player);
            }
            setObjVar(player, VAR_MIGRATION_VERSION, MIGRATION_VERSION);
        }
        else if (getIntObjVar(player, VAR_MIGRATION_VERSION) != MIGRATION_VERSION)
        {
            quarantine(player, "migration-version");
            return;
        }
        retireLegacyPlayerProgression(player);
        reconcileCompletedQuestRewards(player);
        if (getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE)
        {
            if (hasObjVar(player, VAR_AWAKENING_PENDING))
            {
                sendAwakeningNotification(player);
                removeObjVar(player, VAR_AWAKENING_PENDING);
            }
            ensureForceSensitiveFoundationSkills(player);
            reconcilePurchasedSkills(player);
            grantEligibleTreeMasters(player);
            maybeInitializePadawanTrials(player);
        }
    }

    public static boolean awakenIfEligible(obj_id player) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || !isPlayer(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        if (getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE)
        {
            ensureForceSensitiveFoundationSkills(player);
            reconcileCompletedQuestRewards(player);
            return true;
        }
        if (!isForceSensitivityEligible(player))
        {
            return false;
        }
        setObjVar(player, VAR_AWAKENING_PENDING, getCalendarTime());
        setJediState(player, JEDI_STATE_FORCE_SENSITIVE);
        ensureForceSensitiveFoundationSkills(player);
        if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE)
        {
            quarantine(player, "awakening-state-write");
            return false;
        }
        sendAwakeningNotification(player);
        removeObjVar(player, VAR_AWAKENING_PENDING);
        reconcileCompletedQuestRewards(player);
        CustomerServiceLog("reborn_force_progression", "%TU became Force Sensitive through the replacement progression.", player, null);
        return true;
    }

    public static boolean isQuestChainCompleted(obj_id player, String questId) throws InterruptedException
    {
        return isIdValid(player) && isValidIdentifier(questId) && contains(getArray(player, VAR_QUEST_COMPLETIONS), questId);
    }

    public static int getQuestStage(obj_id player, String questId) throws InterruptedException
    {
        if (!isIdValid(player) || !isValidIdentifier(questId) || isQuestChainCompleted(player, questId))
        {
            return 0;
        }
        String path = getQuestStatePath(questId) + ".stage";
        return hasObjVar(player, path) ? Math.max(0, Math.min(3, getIntObjVar(player, path))) : 0;
    }

    public static boolean canStartQuestChain(obj_id player, String questId) throws InterruptedException
    {
        dictionary row = getQuestRow(questId);
        if (!isReplacementEnabled() || !isIdValid(player) || row == null || isQuestChainCompleted(player, questId))
        {
            return false;
        }
        if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE && EVENT_CONVERGENCE.equals(row.getString("event_type")))
        {
            return isPreConvergenceEligible(player);
        }
        return true;
    }

    public static boolean beginQuestChain(obj_id player, String questId) throws InterruptedException
    {
        if (!canStartQuestChain(player, questId) || getQuestStage(player, questId) != 0)
        {
            return false;
        }
        String base = getQuestStatePath(questId);
        setObjVar(player, base + ".stage", 1);
        setObjVar(player, base + ".readyAt", getCalendarTime());
        return getIntObjVar(player, base + ".stage") == 1;
    }

    public static int answerQuestChain(obj_id player, String questId, int choice) throws InterruptedException
    {
        dictionary row = getQuestRow(questId);
        int stage = getQuestStage(player, questId);
        if (!isReplacementEnabled() || row == null || stage < 1 || stage > 3 || choice < 0 || choice > 2)
        {
            return QUEST_RESULT_INVALID;
        }
        String base = getQuestStatePath(questId);
        int now = getCalendarTime();
        int readyAt = hasObjVar(player, base + ".readyAt") ? getIntObjVar(player, base + ".readyAt") : 0;
        if (now < readyAt)
        {
            return QUEST_RESULT_WAIT;
        }
        int correct = row.getInt("answer_" + stage);
        if (choice != correct)
        {
            setObjVar(player, base + ".readyAt", now + QUEST_TRIAL_WRONG_COOLDOWN_SECONDS);
            return QUEST_RESULT_WRONG;
        }
        if (stage < 3)
        {
            int delay = Math.max(0, row.getInt("delay_seconds"));
            setObjVar(player, base + ".stage", stage + 1);
            setObjVar(player, base + ".readyAt", now + delay);
            return QUEST_RESULT_ADVANCED;
        }
        return completeQuestChain(player, questId) ? QUEST_RESULT_COMPLETED : QUEST_RESULT_INVALID;
    }

    public static boolean completeQuestChain(obj_id player, String questId) throws InterruptedException
    {
        dictionary row = getQuestRow(questId);
        if (!isReplacementEnabled() || !isIdValid(player) || row == null || hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        if (isQuestChainCompleted(player, questId))
        {
            reconcileCompletedQuestRewards(player);
            return true;
        }
        if (getQuestStage(player, questId) != 3)
        {
            return false;
        }
        if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE && EVENT_CONVERGENCE.equals(row.getString("event_type")) && !isPreConvergenceEligible(player))
        {
            return false;
        }
        String[] completions = getArray(player, VAR_QUEST_COMPLETIONS);
        if (completions.length >= MAX_FS_QUEST_CHAINS || !areValidUniqueIds(completions, MAX_FS_QUEST_CHAINS))
        {
            quarantine(player, "quest-completion-schema");
            return false;
        }
        if (!writeArray(player, VAR_QUEST_COMPLETIONS, append(completions, questId, MAX_FS_QUEST_CHAINS)))
        {
            quarantine(player, "quest-completion-write");
            return false;
        }
        removeObjVar(player, getQuestStatePath(questId));
        reconcileCompletedQuestRewards(player);
        sendSystemMessage(player, new string_id(STF, "quest_completed"));
        return true;
    }

    public static dictionary getQuestRow(String questId) throws InterruptedException
    {
        if (!isValidIdentifier(questId))
        {
            return null;
        }
        int rowIndex = dataTableSearchColumnForString(questId, "id", QUEST_NETWORK_TABLE);
        if (rowIndex < 0)
        {
            return null;
        }
        dictionary row = dataTableGetRow(QUEST_NETWORK_TABLE, rowIndex);
        return isValidQuestRow(row, questId) ? row : null;
    }

    public static string_id getQuestText(String questId, String suffix) throws InterruptedException
    {
        if (!isValidIdentifier(questId) || !isValidIdentifier(suffix))
        {
            return new string_id(STF, "unavailable");
        }
        return new string_id(STF, "quest_" + questId + "_" + suffix);
    }

    public static String getQuestBranch(String questId) throws InterruptedException
    {
        dictionary row = getQuestRow(questId);
        return row == null ? null : row.getString("branch");
    }

    public static boolean observeAttunement(obj_id player, String eventId, String eventType, String routeFamily, String planet, int observedAt) throws InterruptedException
    {
        if (!isObservationEnabled() || !isIdValid(player) || !isPlayer(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        ensureState(player);
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        String[] records = getArray(player, VAR_EVENTS);
        if (!areValidEventRecords(records))
        {
            quarantine(player, "event-schema");
            return false;
        }
        String normalizedType = normalizeUpper(eventType);
        String normalizedRoute = normalizeUpper(routeFamily);
        String normalizedPlanet = normalizeLower(planet);
        if (!isValidIdentifier(eventId) || isLegacyEvent(eventId) || !isValidEventType(normalizedType) || !isValidRoute(normalizedRoute) || !isValidPlanet(normalizedPlanet) || observedAt <= 0 || containsEvent(records, eventId))
        {
            return false;
        }
        if (EVENT_CONVERGENCE.equals(normalizedType))
        {
            if (countType(records, EVENT_CONVERGENCE) >= REQUIRED_CONVERGENCE_EVENTS || !isPreConvergenceEligible(records))
            {
                return false;
            }
        }
        if (records.length >= MAX_ATTUNEMENT_EVENTS)
        {
            return false;
        }
        String record = SCHEMA_VERSION + "|" + eventId + "|" + normalizedType + "|" + normalizedRoute + "|" + normalizedPlanet + "|" + observedAt;
        return writeArray(player, VAR_EVENTS, append(records, record, MAX_ATTUNEMENT_EVENTS));
    }

    public static boolean isPreConvergenceEligible(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        String[] records = getArray(player, VAR_EVENTS);
        return areValidEventRecords(records) && isPreConvergenceEligible(records);
    }

    public static boolean isForceSensitivityEligible(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return false;
        }
        String[] records = getArray(player, VAR_EVENTS);
        return areValidEventRecords(records) &&
            countType(records, EVENT_ECHO) >= REQUIRED_ECHO_EVENTS &&
            countType(records, EVENT_THREAD) >= REQUIRED_THREAD_EVENTS &&
            countType(records, EVENT_CONVERGENCE) >= REQUIRED_CONVERGENCE_EVENTS &&
            countDistinctRoutes(records) >= REQUIRED_ROUTE_FAMILIES &&
            countDistinctPlanets(records) >= REQUIRED_PLANETS;
    }

    public static int getAttunementEventCount(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return 0;
        }
        String[] records = getArray(player, VAR_EVENTS);
        return areValidEventRecords(records) ? records.length : 0;
    }

    public static String getSensitivityStatusKey(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || hasObjVar(player, VAR_QUARANTINE))
        {
            return "silent";
        }
        if (getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE)
        {
            return "strong";
        }
        if (isForceSensitivityEligible(player))
        {
            return "ready";
        }
        String[] records = getArray(player, VAR_EVENTS);
        if (!areValidEventRecords(records))
        {
            return "silent";
        }
        if (isPreConvergenceEligible(records))
        {
            return "threshold";
        }
        if (records.length >= 8)
        {
            return "gathering";
        }
        if (records.length >= 4)
        {
            return "stirring";
        }
        if (records.length >= 1)
        {
            return "distant";
        }
        return "silent";
    }

    public static boolean canUseMonthlyHint(obj_id player, int now) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || now <= 0)
        {
            return false;
        }
        if (!hasObjVar(player, VAR_HINT_LAST_USED))
        {
            return true;
        }
        int lastUsed = getIntObjVar(player, VAR_HINT_LAST_USED);
        return now >= lastUsed && now - lastUsed >= MONTHLY_HINT_COOLDOWN_SECONDS;
    }

    public static boolean claimMonthlyHint(obj_id player, int now) throws InterruptedException
    {
        if (!canUseMonthlyHint(player, now))
        {
            return false;
        }
        setObjVar(player, VAR_HINT_LAST_USED, now);
        return getIntObjVar(player, VAR_HINT_LAST_USED) == now;
    }

    public static int getMonthlyHintAvailableAt(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !hasObjVar(player, VAR_HINT_LAST_USED))
        {
            return 0;
        }
        return getIntObjVar(player, VAR_HINT_LAST_USED) + MONTHLY_HINT_COOLDOWN_SECONDS;
    }

    public static String tryBartenderHint(obj_id player, int now) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || getJediState(player) < JEDI_STATE_FORCE_SENSITIVE || now <= 0)
        {
            return null;
        }
        if (hasObjVar(player, VAR_BARTENDER_LAST_ROLL))
        {
            int lastRoll = getIntObjVar(player, VAR_BARTENDER_LAST_ROLL);
            if (now < lastRoll || now - lastRoll < BARTENDER_ROLL_COOLDOWN_SECONDS)
            {
                return null;
            }
        }
        setObjVar(player, VAR_BARTENDER_LAST_ROLL, now);
        if (rand(1, 100) > BARTENDER_HINT_CHANCE_PERCENT)
        {
            return null;
        }
        return getPostUnlockHintKey(player);
    }

    public static boolean awardFsQuestPoints(obj_id player, String awardId, int points) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || getJediState(player) < JEDI_STATE_FORCE_SENSITIVE || points != FS_POINTS_PER_QUEST_CHAIN || !isQuestChainCompleted(player, awardId))
        {
            return false;
        }
        reconcileCompletedQuestRewards(player);
        return !hasObjVar(player, VAR_QUARANTINE) && contains(getArray(player, VAR_FS_AWARD_IDS), awardId);
    }

    public static boolean spendFsQuestPoint(obj_id player, String purchaseId) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || !isValidIdentifier(purchaseId) || getAvailableFsQuestPoints(player) < FS_POINTS_PER_TIER_BOX)
        {
            return false;
        }
        ensureState(player);
        if (hasObjVar(player, VAR_QUARANTINE) || !isValidFsCurrencyState(player))
        {
            quarantine(player, "fs-currency-schema");
            return false;
        }
        String[] purchases = getArray(player, VAR_FS_PURCHASE_IDS);
        if (purchases.length >= FS_TIER_BOX_COUNT || contains(purchases, purchaseId))
        {
            return false;
        }
        int spent = getFsPointsSpent(player);
        if (spent >= FS_POINTS_REQUIRED_FOR_ALL_TREES)
        {
            return false;
        }
        if (!writeArray(player, VAR_FS_PURCHASE_IDS, append(purchases, purchaseId, FS_TIER_BOX_COUNT)))
        {
            return false;
        }
        setObjVar(player, VAR_FS_POINTS_SPENT, spent + FS_POINTS_PER_TIER_BOX);
        return getIntObjVar(player, VAR_FS_POINTS_SPENT) == spent + FS_POINTS_PER_TIER_BOX;
    }

    public static boolean isFsTierSkillName(String skillName)
    {
        if (skillName == null || !skillName.startsWith("force_sensitive_") || skillName.length() < 4)
        {
            return false;
        }
        for (String branch : FS_BRANCHES)
        {
            for (int tier = 1; tier <= 4; ++tier)
            {
                if (skillName.equals(branch + "_0" + tier))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean purchaseFsSkill(obj_id player, String skillName) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || getJediState(player) < JEDI_STATE_FORCE_SENSITIVE || !isFsTierSkillName(skillName) || hasSkill(player, skillName) || !hasCompletedMentorForSkill(player, skillName))
        {
            return false;
        }
        String[] prerequisites = getSkillPrerequisiteSkills(skillName);
        if (prerequisites != null)
        {
            for (String prerequisite : prerequisites)
            {
                if (!hasSkill(player, prerequisite))
                {
                    return false;
                }
            }
        }
        if (!spendFsQuestPoint(player, skillName))
        {
            return false;
        }
        if (!grantSkill(player, skillName))
        {
            CustomerServiceLog("reborn_force_progression", "Deferred replay after the Force Insight purchase grant failed for " + skillName + " on %TU.", player, null);
            return false;
        }
        grantEligibleTreeMasters(player);
        maybeInitializePadawanTrials(player);
        prose_package learned = prose.getPackage(new string_id(STF, "skill_learned"), new string_id("skl_n", skillName));
        sendSystemMessageProse(player, learned);
        return true;
    }

    public static String getNextSkillInBranch(obj_id player, String branch) throws InterruptedException
    {
        if (!isIdValid(player) || !contains(FS_BRANCHES, branch))
        {
            return null;
        }
        for (int tier = 1; tier <= 4; ++tier)
        {
            String skillName = branch + "_0" + tier;
            if (!hasSkill(player, skillName))
            {
                return skillName;
            }
        }
        return null;
    }

    public static boolean purchaseNextSkillInBranch(obj_id player, String branch) throws InterruptedException
    {
        String skillName = getNextSkillInBranch(player, branch);
        return skillName != null && purchaseFsSkill(player, skillName);
    }

    public static boolean hasCompletedMentorForSkill(obj_id player, String skillName) throws InterruptedException
    {
        if (!isFsTierSkillName(skillName))
        {
            return false;
        }
        for (String branch : FS_BRANCHES)
        {
            if (skillName.startsWith(branch + "_"))
            {
                return hasCompletedMentorForBranch(player, branch);
            }
        }
        return false;
    }

    public static boolean hasCompletedMentorForBranch(obj_id player, String branch) throws InterruptedException
    {
        if (!isIdValid(player) || !contains(FS_BRANCHES, branch))
        {
            return false;
        }
        for (String questId : getArray(player, VAR_QUEST_COMPLETIONS))
        {
            dictionary row = getQuestRow(questId);
            if (row != null && branch.equals(row.getString("branch")))
            {
                return true;
            }
        }
        return false;
    }

    public static int getAvailableFsQuestPoints(obj_id player) throws InterruptedException
    {
        return Math.max(0, getFsPointsEarned(player) + getFsMigrationCredit(player) - getFsPointsSpent(player));
    }

    public static int getLearnedFsTreeCount(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return 0;
        }
        int learned = 0;
        for (String masterSkill : FS_TREE_MASTERS)
        {
            if (hasSkill(player, masterSkill))
            {
                learned++;
            }
        }
        return learned;
    }

    public static boolean hasLearnedAllFsTrees(obj_id player) throws InterruptedException
    {
        return getLearnedFsTreeCount(player) == FS_TREE_COUNT;
    }

    public static boolean isPadawanReady(obj_id player) throws InterruptedException
    {
        return isReplacementEnabled() &&
            isIdValid(player) &&
            getJediState(player) == JEDI_STATE_FORCE_SENSITIVE &&
            hasLearnedAllFsTrees(player);
    }

    public static void maybeInitializePadawanTrials(obj_id player) throws InterruptedException
    {
        if (!isPadawanReady(player) || hasObjVar(player, VAR_PADAWAN_INITIALIZED))
        {
            return;
        }
        setObjVar(player, VAR_PADAWAN_INITIALIZED, getCalendarTime());
        jedi_trials.initializePadawanTrials(player);
        sendSystemMessage(player, new string_id(STF, "padawan_ready"));
    }

    public static void handleCheckCommand(obj_id player, String params) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player))
        {
            return;
        }
        if (params != null && params.trim().equalsIgnoreCase("hint"))
        {
            int now = getCalendarTime();
            if (claimMonthlyHint(player, now))
            {
                sendSystemMessage(player, new string_id(STF, getDirectionalHintKey(player)));
            }
            else
            {
                int availableAt = getMonthlyHintAvailableAt(player);
                int days = Math.max(1, (availableAt - now + 86399) / 86400);
                sendSystemMessageProse(player, prose.getPackage(new string_id(STF, "hint_cooldown"), days));
            }
            return;
        }
        sendCheckSummary(player);
    }

    public static void sendAwakeningNotification(obj_id player) throws InterruptedException
    {
        if (!isReplacementEnabled() || !isIdValid(player) || getJediState(player) < JEDI_STATE_FORCE_SENSITIVE || hasObjVar(player, VAR_AWAKENING_NOTIFIED))
        {
            return;
        }
        sendSystemMessage(player, new string_id(STF, "awakening"));
        setObjVar(player, VAR_AWAKENING_NOTIFIED, getCalendarTime());
    }

    private static void sendCheckSummary(obj_id player) throws InterruptedException
    {
        String status = getSensitivityStatusKey(player);
        if (status.equals("strong"))
        {
            sendSystemMessage(player, new string_id(STF, "check_strong"));
            sendSystemMessageProse(player, prose.getPackage(new string_id(STF, "check_insight"), getAvailableFsQuestPoints(player)));
            sendSystemMessageProse(player, prose.getPackage(new string_id(STF, "check_trees"), getLearnedFsTreeCount(player)));
            return;
        }
        sendSystemMessage(player, new string_id(STF, "check_" + status));
    }

    private static String getDirectionalHintKey(obj_id player) throws InterruptedException
    {
        if (getJediState(player) >= JEDI_STATE_FORCE_SENSITIVE)
        {
            return getPostUnlockHintKey(player);
        }
        String[] records = getArray(player, VAR_EVENTS);
        for (String route : ROUTE_FAMILIES)
        {
            if (!containsRoute(records, route))
            {
                return "hint_route_" + route.toLowerCase();
            }
        }
        if (countDistinctPlanets(records) < REQUIRED_PLANETS)
        {
            return "hint_planet";
        }
        if (countType(records, EVENT_THREAD) < REQUIRED_THREAD_EVENTS)
        {
            return "hint_thread";
        }
        return "hint_convergence";
    }

    private static String getPostUnlockHintKey(obj_id player) throws InterruptedException
    {
        for (int i = 0; i < FS_TREE_MASTERS.length; ++i)
        {
            if (!hasSkill(player, FS_TREE_MASTERS[i]))
            {
                return "bartender_tree_" + i;
            }
        }
        return "bartender_trials";
    }

    private static void reconcileCompletedQuestRewards(obj_id player) throws InterruptedException
    {
        String[] completions = getArray(player, VAR_QUEST_COMPLETIONS);
        if (!areValidUniqueIds(completions, MAX_FS_QUEST_CHAINS))
        {
            quarantine(player, "quest-completion-schema");
            return;
        }
        for (String questId : completions)
        {
            dictionary row = getQuestRow(questId);
            if (row == null)
            {
                quarantine(player, "quest-catalog-reference");
                return;
            }
            if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE)
            {
                observeAttunement(player, questId, row.getString("event_type"), row.getString("route_family"), row.getString("planet"), getCalendarTime());
            }
        }
        if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE)
        {
            awakenIfEligible(player);
        }
        else
        {
            rebuildFsCurrencyLedgers(player, completions);
        }
    }

    private static void rebuildFsCurrencyLedgers(obj_id player, String[] completions) throws InterruptedException
    {
        String[] purchases = getArray(player, VAR_FS_PURCHASE_IDS);
        if (!areValidUniqueIds(purchases, FS_TIER_BOX_COUNT))
        {
            quarantine(player, "purchase-ledger-schema");
            return;
        }
        for (String purchaseId : purchases)
        {
            if (!isFsTierSkillName(purchaseId))
            {
                quarantine(player, "purchase-skill-name");
                return;
            }
        }
        if (!writeArray(player, VAR_FS_AWARD_IDS, completions))
        {
            quarantine(player, "award-ledger-write");
            return;
        }
        setObjVar(player, VAR_FS_POINTS_EARNED, completions.length * FS_POINTS_PER_QUEST_CHAIN);
        setObjVar(player, VAR_FS_POINTS_SPENT, purchases.length * FS_POINTS_PER_TIER_BOX);
        if (!isValidFsCurrencyState(player))
        {
            quarantine(player, "fs-currency-rebuild");
        }
    }

    private static void migrateLegacyForceSensitiveState(obj_id player) throws InterruptedException
    {
        Vector purchases = new Vector();
        for (String branch : FS_BRANCHES)
        {
            for (int tier = 1; tier <= 4; ++tier)
            {
                String skillName = branch + "_0" + tier;
                if (hasSkill(player, skillName))
                {
                    purchases.add(skillName);
                }
            }
        }
        String[] purchaseIds = new String[purchases.size()];
        purchases.toArray(purchaseIds);
        if (!writeArray(player, VAR_FS_PURCHASE_IDS, purchaseIds))
        {
            quarantine(player, "migration-purchase-write");
            return;
        }
        int unlockedBranches = 0;
        if (hasObjVar(player, "fs_quest.branches_unlocked"))
        {
            int legacyBits = getIntObjVar(player, "fs_quest.branches_unlocked");
            for (int branchId = 0; branchId < FS_BRANCH_COUNT; ++branchId)
            {
                if (utils.checkBit(legacyBits, branchId))
                {
                    unlockedBranches++;
                }
            }
        }
        int spent = purchaseIds.length;
        int migrationCredit = Math.min(FS_POINTS_REQUIRED_FOR_ALL_TREES, Math.max(spent, unlockedBranches * 4));
        setObjVar(player, VAR_FS_POINTS_SPENT, spent);
        setObjVar(player, VAR_FS_MIGRATION_CREDIT, migrationCredit);
        ensureForceSensitiveFoundationSkills(player);
        grantEligibleTreeMasters(player);
        sendSystemMessage(player, new string_id(STF, "migration_complete"));
        CustomerServiceLog("reborn_force_progression", "Migrated legacy Force Sensitive progression for %TU with " + spent + " tier boxes and " + migrationCredit + " preserved Insight.", player, null);
    }

    private static void retireLegacyPlayerProgression(obj_id player) throws InterruptedException
    {
        String[] legacyScripts =
        {
            "quest.force_sensitive.fs_kickoff",
            "quest.force_sensitive.fs_xp_convert"
        };
        for (String scriptName : legacyScripts)
        {
            if (hasScript(player, scriptName))
            {
                detachScript(player, scriptName);
            }
        }
        for (String questName : RETIRED_LEGACY_QUESTS)
        {
            if (quests.isActive(questName, player))
            {
                quests.deactivate(questName, player);
            }
        }
        destroyLegacyWaypoint(player, "quest.fs_theater_camp.waypoint");
        destroyLegacyWaypoint(player, "quest.fs_theater_final.waypoint");
        removeObjVar(player, "fs_kickoff_stage");
        removeObjVar(player, "fs_delay");
        removeObjVar(player, "fs_delay_start");
        removeObjVar(player, "theaterRecoveryTarget");
        removeObjVar(player, "fs_quest");
    }

    private static void destroyLegacyWaypoint(obj_id player, String path) throws InterruptedException
    {
        if (!hasObjVar(player, path))
        {
            return;
        }
        obj_id waypoint = getObjIdObjVar(player, path);
        if (isIdValid(waypoint))
        {
            destroyWaypointInDatapad(waypoint, player);
        }
        removeObjVar(player, path);
    }

    private static void ensureForceSensitiveFoundationSkills(obj_id player) throws InterruptedException
    {
        if (getJediState(player) < JEDI_STATE_FORCE_SENSITIVE)
        {
            return;
        }
        if (!hasSkill(player, "force_title_jedi_novice"))
        {
            grantSkill(player, "force_title_jedi_novice");
        }
        for (String noviceSkill : FS_TREE_NOVICES)
        {
            if (!hasSkill(player, noviceSkill))
            {
                grantSkill(player, noviceSkill);
            }
        }
    }

    private static void reconcilePurchasedSkills(obj_id player) throws InterruptedException
    {
        String[] purchases = getArray(player, VAR_FS_PURCHASE_IDS);
        for (String skillName : purchases)
        {
            if (!isFsTierSkillName(skillName))
            {
                quarantine(player, "purchase-skill-name");
                return;
            }
            if (!hasSkill(player, skillName) && !grantSkill(player, skillName))
            {
                quarantine(player, "purchase-replay-grant");
                return;
            }
        }
    }

    private static void grantEligibleTreeMasters(obj_id player) throws InterruptedException
    {
        for (String masterSkill : FS_TREE_MASTERS)
        {
            if (hasSkill(player, masterSkill))
            {
                continue;
            }
            String[] prerequisites = getSkillPrerequisiteSkills(masterSkill);
            boolean eligible = prerequisites != null && prerequisites.length == 4;
            if (eligible)
            {
                for (String prerequisite : prerequisites)
                {
                    if (!hasSkill(player, prerequisite))
                    {
                        eligible = false;
                        break;
                    }
                }
            }
            if (eligible)
            {
                grantSkill(player, masterSkill);
            }
        }
    }

    private static boolean isValidQuestRow(dictionary row, String questId) throws InterruptedException
    {
        if (row == null || row.isEmpty() || !questId.equals(row.getString("id")) || !isValidPlanet(row.getString("planet")) ||
            !isValidEventType(row.getString("event_type")) || !isValidRoute(row.getString("route_family")) ||
            !contains(FS_BRANCHES, row.getString("branch")) || !isValidIdentifier(row.getString("npc_type")))
        {
            return false;
        }
        for (int stage = 1; stage <= 3; ++stage)
        {
            int answer = row.getInt("answer_" + stage);
            if (answer < 0 || answer > 2)
            {
                return false;
            }
        }
        int delay = row.getInt("delay_seconds");
        return delay >= 0 && delay <= 3600;
    }

    private static String getQuestStatePath(String questId)
    {
        return VAR_QUEST_STATE + "." + questId;
    }

    private static boolean isPreConvergenceEligible(String[] records) throws InterruptedException
    {
        return countType(records, EVENT_ECHO) >= REQUIRED_ECHO_EVENTS &&
            countType(records, EVENT_THREAD) >= REQUIRED_THREAD_EVENTS &&
            countType(records, EVENT_CONVERGENCE) == 0 &&
            countDistinctRoutes(records) >= REQUIRED_ROUTE_FAMILIES &&
            countDistinctPlanets(records) >= REQUIRED_PLANETS;
    }

    private static int countType(String[] records, String eventType) throws InterruptedException
    {
        int count = 0;
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 6 && fields[2].equals(eventType))
            {
                count++;
            }
        }
        return count;
    }

    private static int countDistinctRoutes(String[] records) throws InterruptedException
    {
        Vector routes = new Vector();
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 6 && !routes.contains(fields[3]))
            {
                routes.add(fields[3]);
            }
        }
        return routes.size();
    }

    private static int countDistinctPlanets(String[] records) throws InterruptedException
    {
        Vector planets = new Vector();
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 6 && !fields[4].equals("galactic") && !planets.contains(fields[4]))
            {
                planets.add(fields[4]);
            }
        }
        return planets.size();
    }

    private static boolean containsRoute(String[] records, String route) throws InterruptedException
    {
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 6 && fields[3].equals(route))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean areValidEventRecords(String[] records) throws InterruptedException
    {
        if (records.length > MAX_ATTUNEMENT_EVENTS)
        {
            return false;
        }
        Vector ids = new Vector();
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length != 6 || !fields[0].equals("" + SCHEMA_VERSION) || !isValidIdentifier(fields[1]) || isLegacyEvent(fields[1]) || !isValidEventType(fields[2]) || !isValidRoute(fields[3]) || !isValidPlanet(fields[4]) || utils.stringToInt(fields[5]) <= 0 || ids.contains(fields[1]))
            {
                return false;
            }
            ids.add(fields[1]);
        }
        return true;
    }

    private static boolean containsEvent(String[] records, String eventId) throws InterruptedException
    {
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 6 && fields[1].equals(eventId))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isLegacyEvent(String eventId)
    {
        String normalized = normalizeLower(eventId);
        for (String fragment : LEGACY_EVENT_FRAGMENTS)
        {
            if (normalized.contains(fragment))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidEventType(String eventType)
    {
        return EVENT_ECHO.equals(eventType) || EVENT_THREAD.equals(eventType) || EVENT_CONVERGENCE.equals(eventType);
    }

    private static boolean isValidRoute(String route)
    {
        return contains(ROUTE_FAMILIES, route);
    }

    private static boolean isValidPlanet(String planet)
    {
        return contains(CREDITABLE_PLANETS, planet);
    }

    private static boolean isValidIdentifier(String value)
    {
        return value != null && value.length() > 0 && value.length() <= 64 && value.matches("[a-z0-9][a-z0-9_.:-]*");
    }

    private static String normalizeUpper(String value)
    {
        return value == null ? "" : value.trim().toUpperCase();
    }

    private static String normalizeLower(String value)
    {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private static int getFsPointsEarned(obj_id player) throws InterruptedException
    {
        return isIdValid(player) && hasObjVar(player, VAR_FS_POINTS_EARNED) ? Math.max(0, getIntObjVar(player, VAR_FS_POINTS_EARNED)) : 0;
    }

    private static int getFsPointsSpent(obj_id player) throws InterruptedException
    {
        return isIdValid(player) && hasObjVar(player, VAR_FS_POINTS_SPENT) ? Math.max(0, getIntObjVar(player, VAR_FS_POINTS_SPENT)) : 0;
    }

    private static int getFsMigrationCredit(obj_id player) throws InterruptedException
    {
        return isIdValid(player) && hasObjVar(player, VAR_FS_MIGRATION_CREDIT) ? Math.max(0, getIntObjVar(player, VAR_FS_MIGRATION_CREDIT)) : 0;
    }

    private static boolean isValidFsCurrencyState(obj_id player) throws InterruptedException
    {
        int earned = hasObjVar(player, VAR_FS_POINTS_EARNED) ? getIntObjVar(player, VAR_FS_POINTS_EARNED) : 0;
        int spent = hasObjVar(player, VAR_FS_POINTS_SPENT) ? getIntObjVar(player, VAR_FS_POINTS_SPENT) : 0;
        int migrationCredit = hasObjVar(player, VAR_FS_MIGRATION_CREDIT) ? getIntObjVar(player, VAR_FS_MIGRATION_CREDIT) : 0;
        String[] awards = getArray(player, VAR_FS_AWARD_IDS);
        String[] purchases = getArray(player, VAR_FS_PURCHASE_IDS);
        return earned >= 0 && earned <= MAX_FS_POINTS_EARNED &&
            spent >= 0 && spent <= FS_POINTS_REQUIRED_FOR_ALL_TREES &&
            migrationCredit >= 0 && migrationCredit <= FS_POINTS_REQUIRED_FOR_ALL_TREES &&
            spent <= earned + migrationCredit &&
            earned == awards.length * FS_POINTS_PER_QUEST_CHAIN &&
            spent == purchases.length * FS_POINTS_PER_TIER_BOX &&
            areValidUniqueIds(awards, MAX_FS_QUEST_CHAINS) &&
            areValidUniqueIds(purchases, FS_TIER_BOX_COUNT);
    }

    private static boolean areValidUniqueIds(String[] values, int maximum)
    {
        if (values.length > maximum)
        {
            return false;
        }
        Vector seen = new Vector();
        for (String value : values)
        {
            if (!isValidIdentifier(value) || seen.contains(value))
            {
                return false;
            }
            seen.add(value);
        }
        return true;
    }

    private static void ensureState(obj_id player) throws InterruptedException
    {
        if (!hasObjVar(player, VAR_SCHEMA))
        {
            setObjVar(player, VAR_SCHEMA, SCHEMA_VERSION);
        }
        else if (getIntObjVar(player, VAR_SCHEMA) != SCHEMA_VERSION)
        {
            quarantine(player, "root-schema");
        }
    }

    private static void quarantine(obj_id player, String reason) throws InterruptedException
    {
        if (!hasObjVar(player, VAR_QUARANTINE))
        {
            setObjVar(player, VAR_QUARANTINE, reason);
        }
    }

    private static String[] getArray(obj_id object, String path) throws InterruptedException
    {
        return hasObjVar(object, path) ? getStringArrayObjVar(object, path) : new String[0];
    }

    private static boolean writeArray(obj_id object, String path, String[] values) throws InterruptedException
    {
        setObjVar(object, path, values);
        String[] readback = getArray(object, path);
        if (readback.length != values.length)
        {
            return false;
        }
        for (int i = 0; i < values.length; ++i)
        {
            if (!values[i].equals(readback[i]))
            {
                return false;
            }
        }
        return true;
    }

    private static String[] append(String[] values, String value, int maximum)
    {
        if (values.length >= maximum)
        {
            return values;
        }
        String[] result = new String[values.length + 1];
        System.arraycopy(values, 0, result, 0, values.length);
        result[values.length] = value;
        return result;
    }

    private static boolean contains(String[] values, String sought)
    {
        for (String value : values)
        {
            if (value.equals(sought))
            {
                return true;
            }
        }
        return false;
    }
}
