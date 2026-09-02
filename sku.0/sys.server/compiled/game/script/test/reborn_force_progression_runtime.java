package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.force_progression;
import script.library.jedi_trials;
import script.library.quests;
import script.library.utils;

import java.util.Vector;

/**
 * ServerConsole acceptance probe for the replacement Force progression's
 * runtime-owned surfaces. The probe is read-only: it validates the compiled
 * quest network and the planet-owned mentor population without changing a
 * player's progression.
 */
public class reborn_force_progression_runtime extends script.base_script
{
    private static final String PLANET = "tatooine";
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String SPAWNER_SCRIPT =
        "systems.reborn.force_progression.world_spawner";
    private static final String MENTOR_SCRIPT = "npc.reborn.force_mentor";
    private static final String[] EXPECTED_TATOOINE_QUESTS =
    {
        "bestine_dry_well",
        "wayfar_power_cell",
        "mos_eisley_unfired_blaster",
        "mos_espa_three_hands"
    };
    private static final String SYNTHETIC_MIGRATION_SKILL =
        "force_sensitive_combat_prowess_ranged_accuracy_01";
    private static final String[] AWAKENING_QUESTS =
    {
        "coronet_relief_manifest",
        "dantooine_forgotten_beacon",
        "dearic_broken_harvester",
        "yavin_fragmented_archive",
        "moenia_quiet_ward",
        "endor_moonlit_tracks",
        "tyrena_signal_lens",
        "dathomir_song_shards",
        "mos_eisley_unfired_blaster",
        "endor_stranded_scout",
        "nyms_debt_without_blood",
        "mos_espa_three_hands"
    };
    private static final String[] POST_AWAKENING_QUESTS =
    {
        "kaadara_water_clock",
        "wayfar_power_cell",
        "dathomir_night_test",
        "bela_vistal_fevered_beast"
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
    private static final String USAGE =
        "usage: tatooine | player 39008597 <32-hex-lifecycle>";

    public String executeProbe(String params) throws InterruptedException
    {
        String requestedPlanet = params == null ? "" : params.trim();
        if (requestedPlanet.startsWith("player "))
        {
            return executePlayerLifecycle(requestedPlanet);
        }
        if (!PLANET.equals(requestedPlanet))
        {
            return USAGE;
        }
        String mode = getConfigSetting(
            force_progression.CONFIG_SECTION,
            force_progression.CONFIG_KEY);
        if (!force_progression.MODE_REPLACEMENT.equals(mode) ||
            !force_progression.isReplacementEnabled())
        {
            return "error=replacementModeDisabled";
        }

        obj_id planet = getPlanetByName(PLANET);
        if (!isIdValid(planet) || !planet.isLoaded() ||
            !planet.isAuthoritative())
        {
            return "error=planetNotAuthoritative";
        }
        if (!hasScript(planet, SPAWNER_SCRIPT))
        {
            return "error=spawnerScriptMissing";
        }

        int rows = dataTableGetNumRows(force_progression.QUEST_NETWORK_TABLE);
        int echoes = 0;
        int threads = 0;
        int convergences = 0;
        Vector routes = new Vector();
        Vector planets = new Vector();
        Vector branches = new Vector();
        int expectedTatooineRows = 0;
        for (int rowIndex = 0; rowIndex < rows; ++rowIndex)
        {
            dictionary row = dataTableGetRow(
                force_progression.QUEST_NETWORK_TABLE,
                rowIndex);
            if (row == null)
            {
                return "error=nullQuestRow";
            }
            String questId = row.getString("id");
            if (force_progression.getQuestRow(questId) == null)
            {
                return "error=invalidQuestRow";
            }
            String eventType = row.getString("event_type");
            if (force_progression.EVENT_ECHO.equals(eventType))
            {
                ++echoes;
            }
            else if (force_progression.EVENT_THREAD.equals(eventType))
            {
                ++threads;
            }
            else if (force_progression.EVENT_CONVERGENCE.equals(eventType))
            {
                ++convergences;
            }
            else
            {
                return "error=invalidEventType";
            }
            addUnique(routes, row.getString("route_family"));
            addUnique(planets, row.getString("planet"));
            addUnique(branches, row.getString("branch"));
            if (PLANET.equals(row.getString("planet")))
            {
                ++expectedTatooineRows;
                if (!contains(EXPECTED_TATOOINE_QUESTS, questId))
                {
                    return "error=unexpectedTatooineQuestRow";
                }
            }
        }
        if (rows != 24 || echoes != 16 || threads != 7 ||
            convergences != 1 || routes.size() != 6 ||
            planets.size() != 10 || branches.size() != 16 ||
            expectedTatooineRows != EXPECTED_TATOOINE_QUESTS.length)
        {
            return "error=questNetworkCardinality";
        }

        int[] seen = new int[EXPECTED_TATOOINE_QUESTS.length];
        int ownedSpawnCount = 0;
        obj_id[] candidates = getAllObjectsWithObjVar(
            new location(0.0f, 0.0f, 0.0f, PLANET),
            32000.0f,
            force_progression.VAR_NPC_QUEST_ID);
        if (candidates != null)
        {
            for (obj_id candidate : candidates)
            {
                if (!isIdValid(candidate) || !exists(candidate) ||
                    !hasObjVar(candidate, force_progression.VAR_NPC_OWNER) ||
                    getObjIdObjVar(candidate, force_progression.VAR_NPC_OWNER) != planet)
                {
                    continue;
                }
                ++ownedSpawnCount;
                String questId = getStringObjVar(
                    candidate,
                    force_progression.VAR_NPC_QUEST_ID);
                int expectedIndex = indexOf(EXPECTED_TATOOINE_QUESTS, questId);
                if (expectedIndex < 0)
                {
                    return "error=unexpectedOwnedMentor";
                }
                ++seen[expectedIndex];
                if (seen[expectedIndex] > 1)
                {
                    return "error=duplicateOwnedMentor";
                }
                if (!candidate.isAuthoritative() ||
                    !hasScript(candidate, MENTOR_SCRIPT) ||
                    !isInvulnerable(candidate) ||
                    !hasCondition(candidate, CONDITION_CONVERSABLE))
                {
                    return "error=mentorRuntimeState";
                }
            }
        }
        if (ownedSpawnCount != EXPECTED_TATOOINE_QUESTS.length)
        {
            return "error=ownedMentorCount";
        }
        for (int count : seen)
        {
            if (count != 1)
            {
                return "error=missingOwnedMentor";
            }
        }

        obj_id player = obj_id.getObjId(PLAYER_OID);
        boolean playerLoaded = player != null &&
            player != obj_id.NULL_ID && player.isLoaded();
        return
            "action=inspect" +
            " mode=" + mode +
            " planet=" + PLANET +
            " rows=" + rows +
            " echo=" + echoes +
            " thread=" + threads +
            " convergence=" + convergences +
            " routes=" + routes.size() +
            " planets=" + planets.size() +
            " branches=" + branches.size() +
            " expectedSpawns=" + EXPECTED_TATOOINE_QUESTS.length +
            " loadedSpawns=" + ownedSpawnCount +
            " exactSpawns=true" +
            " authoritative=true" +
            " mentorScript=true" +
            " invulnerable=true" +
            " conversable=true" +
            " owner=true" +
            " spawnerScript=true" +
            " playerLoaded=" + playerLoaded;
    }

    private String executePlayerLifecycle(String params)
        throws InterruptedException
    {
        String[] args = params.split("[ ]+");
        if (args.length != 3 || !"player".equals(args[0]))
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
        if (!force_progression.isReplacementEnabled())
        {
            return "error=replacementModeDisabled";
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
        String cleanError = getCleanPlayerError(player);
        if (cleanError != null)
        {
            return cleanError;
        }

        String result = "error=playerProbeIncomplete";
        boolean restored = false;
        try
        {
            result = runPlayerLifecycle(player);
        }
        finally
        {
            restored = clearControlledPlayerState(player);
        }
        if (!restored)
        {
            return "error=playerCleanupFailed priorResult=" +
                result.replace(' ', '_') + " restored=false";
        }
        return result + " restored=true";
    }

    private String runPlayerLifecycle(obj_id player)
        throws InterruptedException
    {
        String migrationError = exerciseMigration(player);
        if (migrationError != null)
        {
            return migrationError;
        }
        if (!clearControlledPlayerState(player))
        {
            return "error=migrationResetFailed";
        }

        force_progression.reconcilePlayer(player);
        if (!"silent".equals(force_progression.getSensitivityStatusKey(player)) ||
            force_progression.getAttunementEventCount(player) != 0)
        {
            return "error=initialSensitivityState";
        }
        int hintAt = getCalendarTime();
        if (!force_progression.canUseMonthlyHint(player, hintAt) ||
            !force_progression.claimMonthlyHint(player, hintAt) ||
            force_progression.canUseMonthlyHint(
                player,
                hintAt + force_progression.MONTHLY_HINT_COOLDOWN_SECONDS - 1) ||
            !force_progression.canUseMonthlyHint(
                player,
                hintAt + force_progression.MONTHLY_HINT_COOLDOWN_SECONDS) ||
            !force_progression.claimMonthlyHint(
                player,
                hintAt + force_progression.MONTHLY_HINT_COOLDOWN_SECONDS))
        {
            return "error=monthlyHintBoundary";
        }
        int bartenderBeforeAt = getCalendarTime();
        if (force_progression.tryBartenderHint(player, bartenderBeforeAt) != null ||
            hasObjVar(player, force_progression.VAR_BARTENDER_LAST_ROLL))
        {
            return "error=bartenderPreAwakening";
        }
        if (force_progression.canStartQuestChain(
            player,
            "mos_espa_three_hands"))
        {
            return "error=earlyConvergenceAdmission";
        }

        for (int index = 0; index < AWAKENING_QUESTS.length - 1; ++index)
        {
            String questError = completeQuestChain(
                player,
                AWAKENING_QUESTS[index],
                index == 0);
            if (questError != null)
            {
                return questError;
            }
            int completed = index + 1;
            String expectedStatus =
                completed == 11 ? "threshold" :
                completed >= 8 ? "gathering" :
                completed >= 4 ? "stirring" : "distant";
            if (!expectedStatus.equals(
                force_progression.getSensitivityStatusKey(player)))
            {
                return "error=statusAfter" + completed;
            }
        }
        if (!force_progression.isPreConvergenceEligible(player) ||
            force_progression.isForceSensitivityEligible(player) ||
            getJediState(player) != 0 ||
            !"threshold".equals(
                force_progression.getSensitivityStatusKey(player)) ||
            !force_progression.canStartQuestChain(
                player,
                AWAKENING_QUESTS[AWAKENING_QUESTS.length - 1]))
        {
            return "error=preConvergenceThreshold";
        }

        String convergenceError = completeQuestChain(
            player,
            AWAKENING_QUESTS[AWAKENING_QUESTS.length - 1],
            false);
        if (convergenceError != null)
        {
            return convergenceError;
        }
        if (getJediState(player) != JEDI_STATE_FORCE_SENSITIVE ||
            force_progression.getAttunementEventCount(player) != 12 ||
            !"strong".equals(
                force_progression.getSensitivityStatusKey(player)) ||
            !hasObjVar(player, force_progression.VAR_AWAKENING_NOTIFIED) ||
            getIntObjVar(player, force_progression.VAR_FS_POINTS_EARNED) != 48 ||
            getIntObjVar(player, force_progression.VAR_FS_POINTS_SPENT) != 0 ||
            force_progression.getAvailableFsQuestPoints(player) != 48)
        {
            return "error=awakeningAuthority";
        }

        int bartenderAt = getCalendarTime();
        force_progression.tryBartenderHint(player, bartenderAt);
        if (!hasObjVar(player, force_progression.VAR_BARTENDER_LAST_ROLL) ||
            getIntObjVar(player, force_progression.VAR_BARTENDER_LAST_ROLL) !=
                bartenderAt ||
            force_progression.tryBartenderHint(player, bartenderAt + 1) != null ||
            getIntObjVar(player, force_progression.VAR_BARTENDER_LAST_ROLL) !=
                bartenderAt)
        {
            return "error=bartenderThrottle";
        }

        for (String questId : POST_AWAKENING_QUESTS)
        {
            String questError = completeQuestChain(player, questId, false);
            if (questError != null)
            {
                return questError;
            }
        }
        if (force_progression.getAttunementEventCount(player) != 12 ||
            getIntObjVar(player, force_progression.VAR_FS_POINTS_EARNED) != 64 ||
            force_progression.getAvailableFsQuestPoints(player) != 64)
        {
            return "error=postAwakeningInsight";
        }

        for (String branch : force_progression.FS_BRANCHES)
        {
            for (int tier = 1; tier <= 4; ++tier)
            {
                if (!force_progression.purchaseNextSkillInBranch(
                    player,
                    branch))
                {
                    return "error=insightPurchase" + tier;
                }
            }
        }
        if (getIntObjVar(player, force_progression.VAR_FS_POINTS_SPENT) != 64 ||
            force_progression.getAvailableFsQuestPoints(player) != 0 ||
            force_progression.getLearnedFsTreeCount(player) != 4 ||
            !force_progression.hasLearnedAllFsTrees(player) ||
            !force_progression.isPadawanReady(player) ||
            !jedi_trials.isEligibleForJediPadawanTrials(player) ||
            !hasObjVar(player, force_progression.VAR_PADAWAN_INITIALIZED) ||
            !hasObjVar(player, jedi_trials.PADAWAN_TRIALS_ELIGIBLE_OBJVAR) ||
            !hasScript(player, jedi_trials.PADAWAN_TRIALS_SCRIPT) ||
            hasObjVar(player, force_progression.VAR_QUARANTINE))
        {
            return "error=padawanAuthority";
        }

        return
            "action=player" +
            " passed=true" +
            " migration=true" +
            " migrationCredit=12" +
            " hintBoundary=true" +
            " wrongAnswerCooldown=true" +
            " eventCount=12" +
            " status=strong" +
            " completedQuests=16" +
            " insightEarned=64" +
            " insightSpent=64" +
            " insightAvailable=0" +
            " trees=4" +
            " padawanReady=true" +
            " padawanInitialized=true" +
            " bartenderThrottle=true";
    }

    private String exerciseMigration(obj_id player)
        throws InterruptedException
    {
        if (!setJediState(player, JEDI_STATE_FORCE_SENSITIVE) ||
            !grantSkill(player, SYNTHETIC_MIGRATION_SKILL))
        {
            return "error=migrationSetup";
        }
        setObjVar(player, "fs_quest.branches_unlocked", 7);
        force_progression.reconcilePlayer(player);
        if (hasObjVar(player, force_progression.VAR_QUARANTINE) ||
            !hasObjVar(player, force_progression.VAR_MIGRATION_VERSION) ||
            getIntObjVar(player, force_progression.VAR_MIGRATION_VERSION) !=
                force_progression.MIGRATION_VERSION ||
            getIntObjVar(player, force_progression.VAR_FS_MIGRATION_CREDIT) != 12 ||
            getIntObjVar(player, force_progression.VAR_FS_POINTS_SPENT) != 1 ||
            force_progression.getAvailableFsQuestPoints(player) != 11 ||
            hasObjVar(player, "fs_quest") ||
            !hasSkill(player, SYNTHETIC_MIGRATION_SKILL))
        {
            return "error=migrationAuthority";
        }
        return null;
    }

    private String completeQuestChain(
        obj_id player,
        String questId,
        boolean testWrongAnswer) throws InterruptedException
    {
        dictionary row = force_progression.getQuestRow(questId);
        if (row == null || !force_progression.beginQuestChain(player, questId))
        {
            return "error=questBegin_" + questId;
        }
        String statePath = force_progression.VAR_QUEST_STATE + "." + questId;
        for (int stage = 1; stage <= 3; ++stage)
        {
            int correct = row.getInt("answer_" + stage);
            if (testWrongAnswer && stage == 1)
            {
                int wrong = (correct + 1) % 3;
                if (force_progression.answerQuestChain(player, questId, wrong) !=
                        force_progression.QUEST_RESULT_WRONG ||
                    force_progression.answerQuestChain(player, questId, correct) !=
                        force_progression.QUEST_RESULT_WAIT)
                {
                    return "error=wrongAnswerCooldown_" + questId;
                }
            }
            setObjVar(
                player,
                statePath + ".readyAt",
                getCalendarTime());
            int result = force_progression.answerQuestChain(
                player,
                questId,
                correct);
            int expected = stage < 3 ?
                force_progression.QUEST_RESULT_ADVANCED :
                force_progression.QUEST_RESULT_COMPLETED;
            if (result != expected)
            {
                return "error=questStage" + stage + "_" + questId;
            }
        }
        return force_progression.isQuestChainCompleted(player, questId) ?
            null : "error=questCompletion_" + questId;
    }

    private String getCleanPlayerError(obj_id player)
        throws InterruptedException
    {
        if (getJediState(player) != 0)
        {
            return "error=playerNotCleanJediState";
        }
        if (hasObjVar(player, force_progression.PERSISTENT_ROOT) ||
            hasObjVar(player, "fs_quest") ||
            hasObjVar(player, "padawan_trials") ||
            hasObjVar(player, jedi_trials.JEDI_TRIALS_BASE_OBJVAR) ||
            hasObjVar(player, "quest.fs_theater_camp.waypoint") ||
            hasObjVar(player, "quest.fs_theater_final.waypoint") ||
            hasObjVar(player, "fs_kickoff_stage") ||
            hasObjVar(player, "fs_delay") ||
            hasObjVar(player, "fs_delay_start") ||
            hasObjVar(player, "theaterRecoveryTarget") ||
            hasScript(player, "quest.force_sensitive.fs_kickoff") ||
            hasScript(player, "quest.force_sensitive.fs_xp_convert") ||
            hasScript(player, jedi_trials.PADAWAN_TRIALS_SCRIPT))
        {
            return "error=playerNotCleanProgression";
        }
        for (String questName : RETIRED_LEGACY_QUESTS)
        {
            if (quests.isActive(questName, player))
            {
                return "error=playerHasActiveLegacyQuest";
            }
        }
        return hasAnyControlledSkill(player) ?
            "error=playerNotCleanSkills" : null;
    }

    private boolean clearControlledPlayerState(obj_id player)
        throws InterruptedException
    {
        if (utils.hasScriptVar(player, "jedi_trials.openSui"))
        {
            forceCloseSUIPage(
                utils.getIntScriptVar(player, "jedi_trials.openSui"));
        }
        utils.removeScriptVarTree(player, "jedi_trials");
        if (hasScript(player, jedi_trials.PADAWAN_TRIALS_SCRIPT))
        {
            detachScript(player, jedi_trials.PADAWAN_TRIALS_SCRIPT);
        }
        for (String master : force_progression.FS_TREE_MASTERS)
        {
            revokeIfPresent(player, master);
        }
        for (String branch : force_progression.FS_BRANCHES)
        {
            for (int tier = 4; tier >= 1; --tier)
            {
                revokeIfPresent(player, branch + "_0" + tier);
            }
        }
        for (String novice : force_progression.FS_TREE_NOVICES)
        {
            revokeIfPresent(player, novice);
        }
        revokeIfPresent(player, jedi_trials.PADAWAN_INITIATE_SKBOX);
        revokeIfPresent(player, jedi_trials.JEDI_PADAWAN_SKBOX);
        revokeIfPresent(player, "force_title_jedi_novice");
        removeObjVar(player, force_progression.PERSISTENT_ROOT);
        removeObjVar(player, "fs_quest");
        removeObjVar(player, "padawan_trials");
        removeObjVar(player, jedi_trials.JEDI_TRIALS_BASE_OBJVAR);
        boolean stateRestored = setJediState(player, 0);
        return stateRestored && getJediState(player) == 0 &&
            !hasObjVar(player, force_progression.PERSISTENT_ROOT) &&
            !hasObjVar(player, "fs_quest") &&
            !hasObjVar(player, "padawan_trials") &&
            !hasObjVar(player, jedi_trials.JEDI_TRIALS_BASE_OBJVAR) &&
            !hasScript(player, jedi_trials.PADAWAN_TRIALS_SCRIPT) &&
            !hasAnyControlledSkill(player);
    }

    private boolean hasAnyControlledSkill(obj_id player)
        throws InterruptedException
    {
        if (hasSkill(player, "force_title_jedi_novice"))
        {
            return true;
        }
        if (hasSkill(player, jedi_trials.PADAWAN_INITIATE_SKBOX) ||
            hasSkill(player, jedi_trials.JEDI_PADAWAN_SKBOX))
        {
            return true;
        }
        for (String novice : force_progression.FS_TREE_NOVICES)
        {
            if (hasSkill(player, novice))
            {
                return true;
            }
        }
        for (String master : force_progression.FS_TREE_MASTERS)
        {
            if (hasSkill(player, master))
            {
                return true;
            }
        }
        for (String branch : force_progression.FS_BRANCHES)
        {
            for (int tier = 1; tier <= 4; ++tier)
            {
                if (hasSkill(player, branch + "_0" + tier))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void revokeIfPresent(obj_id player, String skillName)
        throws InterruptedException
    {
        if (hasSkill(player, skillName))
        {
            revokeSkill(player, skillName);
        }
    }

    private boolean isValidLifecycle(String value)
    {
        return value != null && value.matches("[0-9a-f]{32}");
    }

    private static void addUnique(Vector values, String value)
    {
        if (!values.contains(value))
        {
            values.add(value);
        }
    }

    private static boolean contains(String[] values, String value)
    {
        return indexOf(values, value) >= 0;
    }

    private static int indexOf(String[] values, String value)
    {
        for (int index = 0; index < values.length; ++index)
        {
            if (values[index].equals(value))
            {
                return index;
            }
        }
        return -1;
    }
}
