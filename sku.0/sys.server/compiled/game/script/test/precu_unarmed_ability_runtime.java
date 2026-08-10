package script.test;

import script.combat_engine;
import script.combat_engine.combat_data;
import script.combat_engine.weapon_data;
import script.dictionary;
import script.location;
import script.obj_id;
import script.library.buff;
import script.library.combat;
import script.library.create;
import script.library.factions;
import script.library.skill;
import script.library.utils;
import script.library.weapons;

/**
 * Identity-bound, reversible live proof for the three restored Core3 Teras
 * Kasi ability commands. The authenticated player exercises the production
 * command queue with the default unarmed weapon against one disposable,
 * fully initialized creature. All player state touched by the fixture is
 * restored. The fixture temporarily grants the authentic Brawler-to-Unarmed
 * ability prerequisite chain, then restores the exact skill and command bits
 * and verifies that available skill points return to their original value.
 */
public class precu_unarmed_ability_runtime extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 3;
    private static final int PREPARED_HAM = 100000;
    private static final int COMMAND_REPETITIONS = 2;
    private static final String TARGET_CREATURE = "worrt";
    private static final String COMBAT_ACTIONS_SCRIPT =
        "systems.combat.combat_actions";
    private static final String FIXTURE_SCRIPT =
        "test.precu_unarmed_ability_runtime";
    private static final String ROOT = "precu.unarmedAbilityRuntime";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String EXPECTED_COMMAND = ROOT + ".expectedCommand";
    private static final String QUEUED_COUNT = ROOT + ".queuedCount";
    private static final String ARMED_AT = ROOT + ".armedAt";
    private static final String ORIGINAL_LOCATION = ROOT + ".originalLocation";
    private static final String ORIGINAL_POSTURE = ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String ORIGINAL_ATTRIBUTES =
        ROOT + ".originalAttributes";
    private static final String ORIGINAL_MAXIMUM = ROOT + ".originalMaximum";
    private static final String ORIGINAL_WOUNDS = ROOT + ".originalWounds";
    private static final String ORIGINAL_REGEN = ROOT + ".originalRegen";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_COMMANDS = ROOT + ".originalCommands";
    private static final String ORIGINAL_SKILLS = ROOT + ".originalSkills";
    private static final String ORIGINAL_SKILL_POINTS =
        ROOT + ".originalSkillPoints";
    private static final String ORIGINAL_WEAPON_PRESENT =
        ROOT + ".originalWeaponPresent";
    private static final String ORIGINAL_WEAPON = ROOT + ".originalWeapon";
    private static final String ORIGINAL_COMBAT_SCRIPT =
        ROOT + ".originalCombatScript";
    private static final String ORIGINAL_FIXTURE_SCRIPT =
        ROOT + ".originalFixtureScript";
    private static final String DISPATCH_ROOT = ROOT + ".dispatch";
    private static final String DISPATCH_FAILURE =
        DISPATCH_ROOT + ".failureHook";
    private static final String DIAGNOSTIC_ROOT =
        "precu.p14.marksmanTier1Fixture.liveDiagnostic";
    private static final String DIAGNOSTIC_ENABLED =
        DIAGNOSTIC_ROOT + ".enabled";
    private static final String[] COMMANDS =
    {
        "unarmedDizzy1", "unarmedCombo1", "unarmedCombo2"
    };
    private static final String[] SKILLS =
    {
        "combat_brawler", "combat_brawler_novice",
        "combat_brawler_unarmed_01", "combat_brawler_unarmed_02",
        "combat_brawler_unarmed_03", "combat_brawler_unarmed_04",
        "combat_unarmed", "combat_unarmed_novice",
        "combat_unarmed_ability_01", "combat_unarmed_ability_02",
        "combat_unarmed_ability_03", "combat_unarmed_ability_04"
    };
    private static final String[] ANIMATIONS =
    {
        "attack_special_wookiee_slap", "combo_4b", "combo_4a"
    };
    private static final float[] COST_MULTIPLIERS =
    {
        1.5f, 1.5f, 2.0f
    };
    private static final int[] ATTRIBUTES =
    {
        HEALTH, STRENGTH, CONSTITUTION,
        ACTION, QUICKNESS, STAMINA,
        MIND, FOCUS, WILLPOWER
    };
    private static final int[] PRIMARY = {HEALTH, ACTION, MIND};
    private static final String USAGE =
        "usage: prepare|diagnostics|status|cleanup <playerOid> " +
        "<32-hex-lifecycle> | arm <playerOid> <32-hex-lifecycle> " +
        "<unarmedDizzy1|unarmedCombo1|unarmedCombo2>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0]
            : params.trim().split("[ ]+");
        boolean arm = args.length > 0 && args[0].equalsIgnoreCase("arm");
        if ((!arm && args.length != 3) || (arm && args.length != 4) ||
            !isValidLifecycle(args[2]))
        {
            return USAGE;
        }

        long playerOid;
        try
        {
            playerOid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (playerOid != PLAYER_OID)
        {
            return "error=playerIdentityRejected";
        }

        obj_id player = obj_id.getObjId(playerOid);
        if (!isAuthoritativePlayer(player))
        {
            return "error=playerUnavailable";
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (arm)
        {
            return arm(player, args[2], args[3]);
        }
        if (args[0].equalsIgnoreCase("diagnostics"))
        {
            return diagnostics(player, args[2]);
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

    public int failSpecialAttack(
        obj_id self, obj_id target, String params, float defaultTime)
        throws InterruptedException
    {
        if (isAuthoritativePlayer(self) && hasObjVar(self, ROOT))
        {
            setObjVar(self, DISPATCH_FAILURE,
                hasObjVar(self, EXPECTED_COMMAND) ?
                    getStringObjVar(self, EXPECTED_COMMAND) : "unknown");
        }
        return SCRIPT_CONTINUE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null ? "action=prepare resumed=true " +
                buildStatus(player) : ownership;
        }
        if (isDead(player) || isIncapacitated(player) ||
            getState(player, STATE_COMBAT) != 0)
        {
            return "error=playerNotAtSafeBoundary";
        }
        if (hasObjVar(player, DIAGNOSTIC_ROOT))
        {
            return "error=foreignLiveDiagnosticPresent";
        }

        snapshot(player, lifecycle);
        boolean combatScriptReady = hasScript(player, COMBAT_ACTIONS_SCRIPT);
        if (!combatScriptReady)
        {
            attachScript(player, COMBAT_ACTIONS_SCRIPT);
            combatScriptReady = hasScript(player, COMBAT_ACTIONS_SCRIPT);
        }
        boolean fixtureScriptReady = hasScript(player, FIXTURE_SCRIPT);
        if (!fixtureScriptReady)
        {
            attachScript(player, FIXTURE_SCRIPT);
            fixtureScriptReady = hasScript(player, FIXTURE_SCRIPT);
        }
        if (!combatScriptReady || !fixtureScriptReady ||
            !grantFixtureSkills(player) ||
            !grantFixtureCommands(player) ||
            !prepareUnarmedWeapon(player))
        {
            boolean restored = restorePlayer(player);
            removeObjVar(player, ROOT);
            return "error=playerPreparationFailed restored=" + restored;
        }

        location destination = new location(
            1000.0f, getHeightAtLocation(1000.0f, 1000.0f),
            1000.0f, "tatooine", null);
        stopCombat(player);
        clearHateList(player);
        setCombatTarget(player, obj_id.NULL_ID);
        boolean moved = setLocation(player, destination);
        boolean postureReady =
            setPostureClientImmediate(player, POSTURE_UPRIGHT);
        boolean locomotionReady =
            setLocomotion(player, LOCOMOTION_STANDING);
        boolean hamReady = preparePlayerHam(player);

        location targetLocation = new location(getLocation(player));
        targetLocation.x += 5.0f;
        targetLocation.y =
            getHeightAtLocation(targetLocation.x, targetLocation.z);
        obj_id target = createFixtureTarget(targetLocation);
        if (!moved || !postureReady || !locomotionReady || !hamReady ||
            !isIdValid(target))
        {
            forceDestroy(target);
            boolean restored = restorePlayer(player);
            removeObjVar(player, ROOT);
            return "error=fixturePreparationFailed restored=" + restored;
        }

        setName(target, "PRECU Unarmed Ability Target");
        setObjVar(player, TARGET, target);
        pvpSetAttackableOverride(target, true);
        pvpSetPermanentPersonalEnemyFlag(player, target);
        pvpSetPermanentPersonalEnemyFlag(target, player);
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String arm(obj_id player, String lifecycle, String command)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        int commandIndex = commandIndex(command);
        if (commandIndex < 0)
        {
            return "error=unsupportedCommand";
        }
        obj_id target = readTarget(player);
        if (!isFixtureTargetAvailable(target))
        {
            return "error=fixtureTargetUnavailable";
        }

        stopCombat(player);
        stopCombat(target);
        clearHateList(player);
        clearHateList(target);
        setCombatTarget(player, obj_id.NULL_ID);
        setCombatTarget(target, obj_id.NULL_ID);
        setPostureClientImmediate(player, POSTURE_UPRIGHT);
        setLocomotion(player, LOCOMOTION_STANDING);
        location targetLocation = new location(getLocation(player));
        targetLocation.x += 5.0f;
        targetLocation.y =
            getHeightAtLocation(targetLocation.x, targetLocation.z);
        setLocation(target, targetLocation);
        clearTargetState(target);
        utils.removeScriptVar(target, factions.IGNORE_PLAYER);
        setInvulnerable(target, false);
        pvpSetAttackableOverride(target, true);
        pvpSetPermanentPersonalEnemyFlag(player, target);
        pvpSetPermanentPersonalEnemyFlag(target, player);
        fortify(target);
        preparePlayerHam(player);
        resetLiveDiagnostic(player);
        if (hasObjVar(player, DISPATCH_ROOT))
        {
            removeObjVar(player, DISPATCH_ROOT);
        }
        setObjVar(player, EXPECTED_COMMAND, COMMANDS[commandIndex]);
        setObjVar(player, ARMED_AT, getGameTime());

        obj_id held = getObjectInSlot(player, "hold_r");
        obj_id weapon = getCurrentWeapon(player);
        boolean boundary = !isIdValid(held) && isIdValid(weapon) &&
            getWeaponType(weapon) == WEAPON_TYPE_UNARMED &&
            hasCommand(player, COMMANDS[commandIndex]) &&
            combat.canPerformAction(COMMANDS[commandIndex], player) == 0 &&
            combat.cachedCanSee(player, target) &&
            pvpCanAttack(player, target);
        if (!boundary)
        {
            return "error=unarmedAdmissionBoundaryFailed " +
                buildStatus(player);
        }

        int queued = 0;
        int commandCrc = getStringCrc(COMMANDS[commandIndex].toLowerCase());
        for (int repetition = 0; repetition < COMMAND_REPETITIONS;
            ++repetition)
        {
            if (queueCommand(
                player,
                commandCrc,
                target,
                "",
                COMMAND_PRIORITY_DEFAULT))
            {
                ++queued;
            }
        }
        setObjVar(player, QUEUED_COUNT, queued);
        return "action=arm command=" + COMMANDS[commandIndex] +
            " queued=" + queued + " requested=" + COMMAND_REPETITIONS +
            " unarmed=true " + buildStatus(player);
    }

    private String diagnostics(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(player, EXPECTED_COMMAND))
        {
            return "error=fixtureNotArmed";
        }
        String command = getStringObjVar(player, EXPECTED_COMMAND);
        int commandIndex = commandIndex(command);
        obj_id target = readTarget(player);
        if (commandIndex < 0 || !isFixtureTargetAvailable(target))
        {
            return "error=armedFixtureInvalid";
        }

        obj_id weapon = getCurrentWeapon(player);
        int[] expectedCosts = getExpectedCosts(
            player, weapon, COST_MULTIPLIERS[commandIndex]);
        int observedHealthCost = readDiagnosticInt("cost.health", -2, player);
        int observedActionCost = readDiagnosticInt("cost.action", -2, player);
        int observedMindCost = readDiagnosticInt("cost.mind", -2, player);
        String observedAction =
            readDiagnosticString("action", "none", player);
        String primaryResult =
            readDiagnosticString("primary.resultName", "none", player);
        String animation =
            readDiagnosticString("animation.generated", "none", player);
        String spamKey =
            readDiagnosticString("spam.key", "none", player);
        String damagePipeline =
            readDiagnosticString("damage.pipeline", "none", player);
        int configuredPool =
            readDiagnosticInt("targetPool.configured", -1, player);
        int healthApplied =
            readDiagnosticInt("poolDamage.applied0", -1, player);
        int actionApplied =
            readDiagnosticInt("poolDamage.applied1", -1, player);
        int mindApplied =
            readDiagnosticInt("poolDamage.applied2", -1, player);
        float healthMultiplier =
            readDiagnosticFloat("poolDamage.multiplier0", -1.0f, player);
        float actionMultiplier =
            readDiagnosticFloat("poolDamage.multiplier1", -1.0f, player);
        float mindMultiplier =
            readDiagnosticFloat("poolDamage.multiplier2", -1.0f, player);
        float multiplierTotal =
            healthMultiplier + actionMultiplier + mindMultiplier;
        boolean costsMatch = expectedCosts[0] == observedHealthCost &&
            expectedCosts[1] == observedActionCost &&
            expectedCosts[2] == observedMindCost;
        boolean commonPassed =
            getIntObjVar(player, QUEUED_COUNT) == COMMAND_REPETITIONS &&
            readFixtureString(DISPATCH_FAILURE, "none", player).equals("none") &&
            observedAction.equals(command) &&
            primaryResult.equals("HIT") && costsMatch &&
            animation.startsWith(ANIMATIONS[commandIndex]) &&
            spamKey.equals("cmd_n:" + command) &&
            damagePipeline.equals("PRECU_CORE3");
        boolean branchPassed;
        if (commandIndex == 0)
        {
            branchPassed = configuredPool == combat.PRECU_TARGET_POOL_RANDOM &&
                readDiagnosticInt("stateEffect.1.type", -1, player) == 1 &&
                readDiagnosticInt(
                    "stateEffect.1.durationBase", -1, player) == 30 &&
                readDiagnosticInt(
                    "stateEffect.1.resolvedDuration", -1, player) == 30 &&
                readDiagnosticString(
                    "stateEffect.1.result", "none", player).equals("APPLIED") &&
                getState(target, STATE_DIZZY) != 0 &&
                buff.hasBuff(target, "dizzy");
        }
        else
        {
            branchPassed = configuredPool == 4 &&
                readDiagnosticInt("poolDamage.activeMask", -1, player) == 7 &&
                healthApplied > 0 && actionApplied > 0 && mindApplied > 0 &&
                Math.abs(multiplierTotal - 1.0f) < 0.0001f &&
                (commandIndex != 1 ||
                    Math.abs(actionMultiplier - 0.1f) < 0.0001f);
        }
        boolean passed = commonPassed && branchPassed;

        stopCombat(player);
        stopCombat(target);
        clearHateList(player);
        clearHateList(target);
        setCombatTarget(player, obj_id.NULL_ID);
        setCombatTarget(target, obj_id.NULL_ID);

        return "action=diagnostics result=" +
            (passed ? "passed" : "failed") +
            " command=" + command +
            " failureHook=" +
                readFixtureString(DISPATCH_FAILURE, "none", player) +
            " traceStage=" +
                readDiagnosticString("trace.stage", "none", player) +
            " diagnosticAction=" + observedAction +
            " weaponTemplate=" +
                (isIdValid(weapon) ? getTemplateName(weapon) : "none") +
            " weaponType=" +
                (isIdValid(weapon) ? getWeaponType(weapon) : -1) +
            " queued=" + getIntObjVar(player, QUEUED_COUNT) +
            " armedAt=" + getIntObjVar(player, ARMED_AT) +
            " primaryResult=" + primaryResult +
            " expectedCosts=" + expectedCosts[0] + "," +
                expectedCosts[1] + "," + expectedCosts[2] +
            " observedCosts=" + observedHealthCost + "," +
                observedActionCost + "," + observedMindCost +
            " costsMatch=" + costsMatch +
            " targetPoolConfigured=" + configuredPool +
            " targetPoolResolved=" +
                readDiagnosticInt("targetPool.resolved", -1, player) +
            " healthMultiplier=" + healthMultiplier +
            " actionMultiplier=" + actionMultiplier +
            " mindMultiplier=" + mindMultiplier +
            " multiplierTotal=" + multiplierTotal +
            " healthApplied=" + healthApplied +
            " actionApplied=" + actionApplied +
            " mindApplied=" + mindApplied +
            " stateType=" +
                readDiagnosticInt("stateEffect.1.type", -1, player) +
            " stateDurationBase=" +
                readDiagnosticInt(
                    "stateEffect.1.durationBase", -1, player) +
            " stateResolvedDuration=" +
                readDiagnosticInt(
                    "stateEffect.1.resolvedDuration", -1, player) +
            " stateResult=" +
                readDiagnosticString(
                    "stateEffect.1.result", "none", player) +
            " targetDizzyState=" + getState(target, STATE_DIZZY) +
            " targetDizzyBuff=" + buff.hasBuff(target, "dizzy") +
            " animation=" + animation +
            " animationType=" +
                readDiagnosticInt("animation.type", -1, player) +
            " spamKey=" + spamKey +
            " damagePipeline=" + damagePipeline +
            " playerStateMutated=reversible disposableTarget=true";
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null ? "action=status " + buildStatus(player)
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
            return "error=incompleteSnapshot restored=false";
        }

        obj_id target = readTarget(player);
        stopCombat(player);
        clearHateList(player);
        setCombatTarget(player, obj_id.NULL_ID);
        if (isIdValid(target) && target.isLoaded())
        {
            stopCombat(target);
            clearHateList(target);
            setCombatTarget(target, obj_id.NULL_ID);
            pvpRemovePersonalEnemyFlags(player, target);
            pvpRemovePersonalEnemyFlags(target, player);
            pvpSetAttackableOverride(target, false);
        }
        forceDestroy(target);

        boolean restored = restorePlayer(player);
        if (!restored)
        {
            return "error=playerRestorationFailed " + buildStatus(player);
        }
        if (hasObjVar(player, DIAGNOSTIC_ROOT))
        {
            removeObjVar(player, DIAGNOSTIC_ROOT);
        }
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true";
    }

    private void snapshot(obj_id player, String lifecycle)
        throws InterruptedException
    {
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, PREPARED, 0);
        setObjVar(player, ORIGINAL_LOCATION, getLocation(player));
        setObjVar(player, ORIGINAL_POSTURE, getPosture(player));
        setObjVar(player, ORIGINAL_LOCOMOTION, getLocomotion(player));
        int[] attributes = new int[ATTRIBUTES.length];
        int[] maximum = new int[ATTRIBUTES.length];
        int[] wounds = new int[ATTRIBUTES.length];
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            attributes[index] = getAttrib(player, ATTRIBUTES[index]);
            maximum[index] = getMaxAttrib(player, ATTRIBUTES[index]);
            wounds[index] = getAttribWound(player, ATTRIBUTES[index]);
        }
        setObjVar(player, ORIGINAL_ATTRIBUTES, attributes);
        setObjVar(player, ORIGINAL_MAXIMUM, maximum);
        setObjVar(player, ORIGINAL_WOUNDS, wounds);
        float[] regen = new float[PRIMARY.length];
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            regen[index] = getRegenRate(player, PRIMARY[index]);
        }
        setObjVar(player, ORIGINAL_REGEN, regen);
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        int[] commands = new int[COMMANDS.length];
        for (int index = 0; index < COMMANDS.length; ++index)
        {
            commands[index] = hasCommand(player, COMMANDS[index]) ? 1 : 0;
        }
        setObjVar(player, ORIGINAL_COMMANDS, commands);
        int[] skills = new int[SKILLS.length];
        for (int index = 0; index < SKILLS.length; ++index)
        {
            skills[index] = hasSkill(player, SKILLS[index]) ? 1 : 0;
        }
        setObjVar(player, ORIGINAL_SKILLS, skills);
        setObjVar(player, ORIGINAL_SKILL_POINTS,
            skill.getAvailableSkillPoints(player));
        obj_id held = getObjectInSlot(player, "hold_r");
        setObjVar(player, ORIGINAL_WEAPON_PRESENT,
            isIdValid(held) ? 1 : 0);
        if (isIdValid(held))
        {
            setObjVar(player, ORIGINAL_WEAPON, held);
        }
        setObjVar(player, ORIGINAL_COMBAT_SCRIPT,
            hasScript(player, COMBAT_ACTIONS_SCRIPT) ? 1 : 0);
        setObjVar(player, ORIGINAL_FIXTURE_SCRIPT,
            hasScript(player, FIXTURE_SCRIPT) ? 1 : 0);
    }

    private boolean restorePlayer(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        boolean weaponRestored = restoreWeapon(player);
        boolean skillsRestored = restoreSkills(player);
        boolean commandsRestored = restoreCommands(player);
        int[] attributes = getIntArrayObjVar(player, ORIGINAL_ATTRIBUTES);
        int[] maximum = getIntArrayObjVar(player, ORIGINAL_MAXIMUM);
        int[] wounds = getIntArrayObjVar(player, ORIGINAL_WOUNDS);
        boolean hamRestored = attributes != null && maximum != null &&
            wounds != null && attributes.length == ATTRIBUTES.length &&
            maximum.length == ATTRIBUTES.length &&
            wounds.length == ATTRIBUTES.length;
        for (int index = 0; hamRestored && index < ATTRIBUTES.length; ++index)
        {
            hamRestored = setWoundExact(player, ATTRIBUTES[index], 0) &&
                setMaxAttrib(player, ATTRIBUTES[index], maximum[index]) &&
                setWoundExact(player, ATTRIBUTES[index], wounds[index]);
            setAttrib(player, ATTRIBUTES[index], attributes[index]);
            hamRestored = hamRestored &&
                getAttrib(player, ATTRIBUTES[index]) == attributes[index];
        }
        float[] regen = getFloatArrayObjVar(player, ORIGINAL_REGEN);
        boolean regenRestored = regen != null &&
            regen.length == PRIMARY.length;
        for (int index = 0; regenRestored && index < PRIMARY.length; ++index)
        {
            setRegenRate(player, PRIMARY[index], regen[index]);
            regenRestored = getRegenRate(player, PRIMARY[index]) ==
                regen[index];
        }
        boolean shockRestored =
            setShockWound(player, getIntObjVar(player, ORIGINAL_SHOCK)) &&
            getShockWound(player) == getIntObjVar(player, ORIGINAL_SHOCK);
        boolean moved = setLocation(
            player, getLocationObjVar(player, ORIGINAL_LOCATION));
        boolean postureRestored = setPostureClientImmediate(
            player, getIntObjVar(player, ORIGINAL_POSTURE));
        boolean locomotionRestored = setLocomotion(
            player, getIntObjVar(player, ORIGINAL_LOCOMOTION));
        boolean scriptRestored = true;
        if (getIntObjVar(player, ORIGINAL_COMBAT_SCRIPT) == 0 &&
            hasScript(player, COMBAT_ACTIONS_SCRIPT))
        {
            detachScript(player, COMBAT_ACTIONS_SCRIPT);
            scriptRestored = !hasScript(player, COMBAT_ACTIONS_SCRIPT);
        }
        boolean fixtureScriptRestored = true;
        if (getIntObjVar(player, ORIGINAL_FIXTURE_SCRIPT) == 0 &&
            hasScript(player, FIXTURE_SCRIPT))
        {
            detachScript(player, FIXTURE_SCRIPT);
            fixtureScriptRestored = !hasScript(player, FIXTURE_SCRIPT);
        }
        return weaponRestored && skillsRestored && commandsRestored &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_SKILL_POINTS) &&
            hamRestored &&
            regenRestored && shockRestored && moved && postureRestored &&
            locomotionRestored && scriptRestored && fixtureScriptRestored;
    }

    private boolean restoreWeapon(obj_id player) throws InterruptedException
    {
        obj_id inventory = getObjectInSlot(player, "inventory");
        if (!isIdValid(inventory))
        {
            return false;
        }
        obj_id current = getObjectInSlot(player, "hold_r");
        if (isIdValid(current) && !putInOverloaded(current, inventory))
        {
            return false;
        }
        if (getIntObjVar(player, ORIGINAL_WEAPON_PRESENT) == 0)
        {
            return !isIdValid(getObjectInSlot(player, "hold_r"));
        }
        obj_id original = getObjIdObjVar(player, ORIGINAL_WEAPON);
        if (!isIdValid(original) || !exists(original))
        {
            return false;
        }
        boolean attached = hasScript(original, "systems.combat.combat_weapon");
        if (attached)
        {
            detachScript(original, "systems.combat.combat_weapon");
        }
        boolean equipped = equipOverride(original, player) &&
            getObjectInSlot(player, "hold_r") == original;
        if (attached)
        {
            attachScript(original, "systems.combat.combat_weapon");
        }
        return equipped && (!attached ||
            hasScript(original, "systems.combat.combat_weapon"));
    }

    private boolean restoreCommands(obj_id player) throws InterruptedException
    {
        int[] original = getIntArrayObjVar(player, ORIGINAL_COMMANDS);
        if (original == null || original.length != COMMANDS.length)
        {
            return false;
        }
        boolean restored = true;
        for (int index = 0; index < COMMANDS.length; ++index)
        {
            if (original[index] == 0 && hasCommand(player, COMMANDS[index]))
            {
                revokeCommand(player, COMMANDS[index]);
            }
            else if (original[index] == 1 &&
                !hasCommand(player, COMMANDS[index]))
            {
                restored = grantCommand(player, COMMANDS[index]) && restored;
            }
            restored = restored &&
                hasCommand(player, COMMANDS[index]) == (original[index] == 1);
        }
        return restored;
    }

    private boolean restoreSkills(obj_id player) throws InterruptedException
    {
        int[] original = getIntArrayObjVar(player, ORIGINAL_SKILLS);
        if (original == null || original.length != SKILLS.length)
        {
            return false;
        }
        for (int index = SKILLS.length - 1; index >= 0; --index)
        {
            if (original[index] == 0 && hasSkill(player, SKILLS[index]))
            {
                revokeSkillSilent(player, SKILLS[index]);
            }
        }
        boolean restored = true;
        for (int index = 0; index < SKILLS.length; ++index)
        {
            boolean expected = original[index] == 1;
            if (expected && !hasSkill(player, SKILLS[index]))
            {
                restored = grantSkill(player, SKILLS[index]) && restored;
            }
            restored = restored && hasSkill(player, SKILLS[index]) == expected;
        }
        return restored && skill.getAvailableSkillPoints(player) ==
            getIntObjVar(player, ORIGINAL_SKILL_POINTS);
    }

    private boolean grantFixtureSkills(obj_id player)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!hasSkill(player, skillName) &&
                !grantSkill(player, skillName))
            {
                return false;
            }
        }
        return "111111111111".equals(buildSkillBits(player));
    }

    private boolean grantFixtureCommands(obj_id player)
        throws InterruptedException
    {
        for (String command : COMMANDS)
        {
            if (!hasCommand(player, command) && !grantCommand(player, command))
            {
                return false;
            }
        }
        return true;
    }

    private boolean prepareUnarmedWeapon(obj_id player)
        throws InterruptedException
    {
        obj_id held = getObjectInSlot(player, "hold_r");
        if (isIdValid(held))
        {
            obj_id inventory = getObjectInSlot(player, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(held, inventory))
            {
                return false;
            }
        }
        obj_id weapon = getCurrentWeapon(player);
        return isIdValid(weapon) &&
            getWeaponType(weapon) == WEAPON_TYPE_UNARMED &&
            getTemplateName(weapon).equals(
                "object/weapon/melee/unarmed/unarmed_default_player.iff");
    }

    private boolean preparePlayerHam(obj_id player)
        throws InterruptedException
    {
        boolean ready = true;
        for (int attribute : PRIMARY)
        {
            setRegenRate(player, attribute, 0.0f);
            ready = setAttribAndVerify(
                player, attribute, getMaxAttrib(player, attribute)) && ready;
        }
        return ready;
    }

    private obj_id createFixtureTarget(location loc)
        throws InterruptedException
    {
        dictionary data = dataTableGetRow(create.CREATURE_TABLE, TARGET_CREATURE);
        if (data == null)
        {
            return obj_id.NULL_ID;
        }
        String template = data.getString("template");
        if (template == null || template.length() == 0)
        {
            return obj_id.NULL_ID;
        }
        data.put("lootTable", "");
        obj_id target = createObject(create.TEMPLATE_PREFIX + template, loc);
        if (!isIdValid(target))
        {
            return obj_id.NULL_ID;
        }
        create.initializeCreature(target, TARGET_CREATURE, data, -1);
        create.attachCreatureScripts(
            target, data.getString("scripts"), true);
        utils.removeScriptVar(target, factions.IGNORE_PLAYER);
        setInvulnerable(target, false);
        fortify(target);
        obj_id weapon = getCurrentWeapon(target);
        if (isIdValid(weapon))
        {
            setWeaponMinDamage(weapon, 0);
            setWeaponMaxDamage(weapon, 0);
            weapons.setWeaponData(weapon);
        }
        return target;
    }

    private void fortify(obj_id target) throws InterruptedException
    {
        for (int attribute : ATTRIBUTES)
        {
            setMaxAttrib(target, attribute, PREPARED_HAM);
            setAttrib(target, attribute, PREPARED_HAM);
        }
        for (int attribute : PRIMARY)
        {
            setRegenRate(target, attribute, 0.0f);
        }
    }

    private void clearTargetState(obj_id target) throws InterruptedException
    {
        if (buff.hasBuff(target, "dizzy"))
        {
            buff.removeBuff(target, "dizzy");
        }
        setState(target, STATE_DIZZY, false);
    }

    private int[] getExpectedCosts(
        obj_id player, obj_id weapon, float multiplier)
        throws InterruptedException
    {
        int[] invalid = {-1, -1, -1};
        if (!isIdValid(weapon))
        {
            return invalid;
        }
        dictionary row = dataTableGetRow(
            combat.PRECU_WEAPON_HAM_COST_TABLE, getTemplateName(weapon));
        if (row == null)
        {
            return invalid;
        }
        return new int[]
        {
            calculateExpectedCost(
                getAttrib(player, STRENGTH), row.getInt("healthCost"),
                multiplier),
            calculateExpectedCost(
                getAttrib(player, QUICKNESS), row.getInt("actionCost"),
                multiplier),
            calculateExpectedCost(
                getAttrib(player, FOCUS), row.getInt("mindCost"),
                multiplier)
        };
    }

    private int calculateExpectedCost(
        int governingValue, int baseCost, float multiplier)
    {
        float cost = (float)baseCost * multiplier;
        cost -= ((float)governingValue - 300.0f) / 1200.0f * cost;
        return Math.max(0, (int)cost);
    }

    private void resetLiveDiagnostic(obj_id player)
        throws InterruptedException
    {
        if (hasObjVar(player, DIAGNOSTIC_ROOT))
        {
            removeObjVar(player, DIAGNOSTIC_ROOT);
        }
        setObjVar(player, DIAGNOSTIC_ENABLED, 1);
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id target = readTarget(player);
        obj_id held = getObjectInSlot(player, "hold_r");
        obj_id weapon = getCurrentWeapon(player);
        String expected = hasObjVar(player, EXPECTED_COMMAND) ?
            getStringObjVar(player, EXPECTED_COMMAND) : "none";
        dictionary commandRow = expected.equals("none") ? null :
            dataTableGetRow("datatables/command/command_table.iff", expected);
        combat_data actionData = expected.equals("none") ? null :
            combat_engine.getCombatData(expected);
        return "authoritative=true player=" + player +
            " stationId=" + getPlayerStationId(player) +
            " lifecycle=" + getStringObjVar(player, LIFECYCLE) +
            " target=" + target +
            " targetLoaded=" +
                (isIdValid(target) && target.isLoaded()) +
            " targetIsMob=" + (isIdValid(target) && isMob(target)) +
            " targetIgnorePlayer=" +
                (isIdValid(target) && factions.ignorePlayer(player, target)) +
            " targetInvulnerable=" +
                (isIdValid(target) && isInvulnerable(target)) +
            " targetVisible=" +
                (isIdValid(target) && combat.cachedCanSee(player, target)) +
            " commandBits=" + buildCommandBits(player) +
            " skillBits=" + buildSkillBits(player) +
            " availableSkillPoints=" + skill.getAvailableSkillPoints(player) +
            " originalSkillPoints=" +
                (hasObjVar(player, ORIGINAL_SKILL_POINTS) ?
                    getIntObjVar(player, ORIGINAL_SKILL_POINTS) : -1) +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " currentWeapon=" + (isIdValid(weapon) ? weapon : "none") +
            " currentWeaponTemplate=" +
                (isIdValid(weapon) ? getTemplateName(weapon) : "none") +
            " currentWeaponType=" +
                (isIdValid(weapon) ? getWeaponType(weapon) : -1) +
            " expectedCommand=" + expected +
            " combatScript=" + hasScript(player, COMBAT_ACTIONS_SCRIPT) +
            " fixtureScript=" + hasScript(player, FIXTURE_SCRIPT) +
            " posture=" + getPosture(player) +
            " locomotion=" + getLocomotion(player) +
            " activeStates=" + buildActiveStates(player) +
            " combatIsStunned=" + combat.isStunned(player) +
            " nextAttackDelayUntil=" +
                (utils.hasScriptVar(
                    player, "combat.precuNextAttackDelayUntil") ?
                    utils.getIntScriptVar(
                        player, "combat.precuNextAttackDelayUntil") : -1) +
            " gameTime=" + getGameTime() +
            " targetDistance=" +
                (isIdValid(target) ? getDistance(player, target) : -1.0f) +
            " pvpCanAttack=" +
                (isIdValid(target) && pvpCanAttack(player, target)) +
            " liveScriptHook=" +
                (commandRow == null ? "none" :
                    commandRow.getString("scriptHook")) +
            " liveCharacterAbility=" +
                (commandRow == null ? "none" :
                    commandRow.getString("characterAbility")) +
            " liveTargetType=" +
                (commandRow == null ? "none" :
                    commandRow.getString("targetType")) +
            " liveMaxRange=" +
                (commandRow == null ? -1.0f :
                    commandRow.getFloat("maxRangeToTarget")) +
            " liveCombatData=" + (actionData != null) +
            " livePrecuHamCostModel=" +
                (actionData == null ? -1 : actionData.precuHamCostModel) +
            " livePrecuTargetPool=" +
                (actionData == null ? -1 : actionData.precuTargetPool) +
            " queued=" +
                (hasObjVar(player, QUEUED_COUNT) ?
                    getIntObjVar(player, QUEUED_COUNT) : 0) +
            " failureHook=" +
                readFixtureString(DISPATCH_FAILURE, "none", player) +
            " playerStateMutated=reversible disposableTarget=true";
    }

    private String buildCommandBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (String command : COMMANDS)
        {
            bits += hasCommand(player, command) ? "1" : "0";
        }
        return bits;
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

    private String buildActiveStates(obj_id player)
        throws InterruptedException
    {
        String states = "none";
        for (int state = 0; state < STATE_NUMBER_OF_STATES; ++state)
        {
            if (getState(player, state) != 0)
            {
                states = states.equals("none") ? String.valueOf(state) :
                    states + "," + state;
            }
        }
        return states;
    }

    private int commandIndex(String command)
    {
        for (int index = 0; index < COMMANDS.length; ++index)
        {
            if (COMMANDS[index].equals(command))
            {
                return index;
            }
        }
        return -1;
    }

    private int readDiagnosticInt(
        String leaf, int fallback, obj_id player) throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : fallback;
    }

    private float readDiagnosticFloat(
        String leaf, float fallback, obj_id player) throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(player, path) ?
            getFloatObjVar(player, path) : fallback;
    }

    private String readDiagnosticString(
        String leaf, String fallback, obj_id player)
        throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(player, path) ?
            getStringObjVar(player, path) : fallback;
    }

    private String readFixtureString(
        String path, String fallback, obj_id player)
        throws InterruptedException
    {
        return hasObjVar(player, path) ?
            getStringObjVar(player, path) : fallback;
    }

    private boolean setWoundExact(
        obj_id player, int attribute, int requested)
        throws InterruptedException
    {
        int current = getAttribWound(player, attribute);
        if (current > requested)
        {
            healWound(player, attribute, current - requested);
        }
        else if (current < requested)
        {
            addWound(player, attribute, requested - current);
        }
        return getAttribWound(player, attribute) == requested;
    }

    private boolean setAttribAndVerify(
        obj_id player, int attribute, int value) throws InterruptedException
    {
        setAttrib(player, attribute, value);
        return getAttrib(player, attribute) == value;
    }

    private obj_id readTarget(obj_id player) throws InterruptedException
    {
        return hasObjVar(player, TARGET) ?
            getObjIdObjVar(player, TARGET) : obj_id.NULL_ID;
    }

    private boolean isFixtureTargetAvailable(obj_id target)
        throws InterruptedException
    {
        return isIdValid(target) && target.isLoaded() &&
            getName(target).equals("PRECU Unarmed Ability Target");
    }

    private void forceDestroy(obj_id object) throws InterruptedException
    {
        if (!isIdValid(object) || !object.isLoaded())
        {
            return;
        }
        setInvulnerable(object, true);
        stopCombat(object);
        clearHateList(object);
        setCombatTarget(object, obj_id.NULL_ID);
        destroyObject(object);
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String error = validateLifecycle(player, lifecycle);
        if (error != null)
        {
            return error;
        }
        if (!hasCompleteSnapshot(player) ||
            getIntObjVar(player, PROTOCOL) != PROTOCOL_VERSION ||
            getIntObjVar(player, PREPARED) != 1)
        {
            return "error=fixtureNotPrepared";
        }
        return null;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) ||
            !getStringObjVar(player, LIFECYCLE).equals(lifecycle))
        {
            return "error=lifecycleRejected";
        }
        return null;
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, PROTOCOL) ||
            !hasObjVar(player, PREPARED) ||
            !hasObjVar(player, ORIGINAL_LOCATION) ||
            !hasObjVar(player, ORIGINAL_POSTURE) ||
            !hasObjVar(player, ORIGINAL_LOCOMOTION) ||
            !hasObjVar(player, ORIGINAL_ATTRIBUTES) ||
            !hasObjVar(player, ORIGINAL_MAXIMUM) ||
            !hasObjVar(player, ORIGINAL_WOUNDS) ||
            !hasObjVar(player, ORIGINAL_REGEN) ||
            !hasObjVar(player, ORIGINAL_SHOCK) ||
            !hasObjVar(player, ORIGINAL_COMMANDS) ||
            !hasObjVar(player, ORIGINAL_SKILLS) ||
            !hasObjVar(player, ORIGINAL_SKILL_POINTS) ||
            !hasObjVar(player, ORIGINAL_WEAPON_PRESENT) ||
            !hasObjVar(player, ORIGINAL_COMBAT_SCRIPT) ||
            !hasObjVar(player, ORIGINAL_FIXTURE_SCRIPT))
        {
            return false;
        }
        return getIntObjVar(player, ORIGINAL_WEAPON_PRESENT) == 0 ||
            hasObjVar(player, ORIGINAL_WEAPON);
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return player != null && player != obj_id.NULL_ID &&
            player.isLoaded() && player.isAuthoritative() &&
            isPlayer(player) && player.getValue() == PLAYER_OID &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private boolean isValidLifecycle(String value)
    {
        return value != null && value.matches("[a-f0-9]{32}");
    }
}
