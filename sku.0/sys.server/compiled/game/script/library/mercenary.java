package script.library;

import script.*;

/**
 * Server-authoritative configuration and hiring lifecycle for PRE-CU mercenaries.
 *
 * The constants below are intentionally conservative defaults because design did
 * not specify price, duration, or party limits.  They are centralized here so a
 * later balance pass does not need to change terminal or AI scripts.
 */
public class mercenary extends script.base_script
{
    public mercenary()
    {
    }

    public static final String STF = "precu_hire_merc";
    public static final String ACCOUNT = "precu_hire_merc";
    public static final String AI_SCRIPT = "ai.mercenary_party_member";
    public static final String PAYMENT_SCRIPT = "player.mercenary_hire_payment";
    public static final String TERMINAL_SCRIPT = "systems.missions.base.hire_merc_terminal";

    public static final String VAR_ROOT = "precu.hireMerc";
    public static final String VAR_ACTIVE = VAR_ROOT + ".active";
    public static final String VAR_OWNER = VAR_ROOT + ".owner";
    public static final String VAR_ARCHETYPE = VAR_ROOT + ".archetype";
    public static final String VAR_LEVEL = VAR_ROOT + ".level";
    public static final String VAR_ABILITY_TIER = VAR_ROOT + ".abilityTier";
    public static final String VAR_WEAPON_QUALITY = VAR_ROOT + ".weaponQuality";
    public static final String VAR_EXPIRES = VAR_ROOT + ".expires";
    public static final String VAR_NO_MISSION_CREDIT = VAR_ROOT + ".noMissionCredit";
    public static final String VAR_OWN_VEHICLE = VAR_ROOT + ".ownVehicle";

    // Persistent transaction ledger.  Named-bank callbacks and group invites
    // can cross a server restart, so these must never be transient scriptvars.
    public static final String VAR_TX_ROOT = VAR_ROOT + ".transaction";
    public static final String VAR_TX_NONCE = VAR_TX_ROOT + ".nonce";
    public static final String VAR_TX_PLAYER = VAR_TX_ROOT + ".player";
    public static final String VAR_TX_STARTED = VAR_TX_ROOT + ".started";
    public static final String VAR_TX_ARCHETYPE = VAR_TX_ROOT + ".archetype";
    public static final String VAR_TX_LEVEL = VAR_TX_ROOT + ".level";
    public static final String VAR_TX_COST = VAR_TX_ROOT + ".cost";
    public static final String VAR_TX_TERMINAL = VAR_TX_ROOT + ".terminal";
    public static final String VAR_TX_STATE = VAR_TX_ROOT + ".state";
    public static final String VAR_TX_HIRED = VAR_TX_ROOT + ".hired";
    public static final String VAR_TX_ACCOUNT = VAR_TX_ROOT + ".account";
    public static final String VAR_TX_REFUND_ATTEMPT =
        VAR_TX_ROOT + ".refundAttempt";
    public static final String LEGACY_SV_PENDING = VAR_ROOT + ".pending";
    public static final String STATE_QUEUED = "queued";
    public static final String STATE_DEBITED = "debited";
    public static final String STATE_ENROLLING = "enrolling";
    public static final String STATE_REFUNDING = "refunding";
    public static final String STATE_COMPLETED = "completed";

    // Provisional design constants.  Keep these together for the balance pass.
    public static final int HIRE_DURATION_SECONDS = 2 * 60 * 60;
    public static final int BASE_HIRE_COST = 5000;
    public static final int COST_PER_COMBAT_LEVEL = 100;
    public static final int MIN_HIRE_LEVEL = 1;
    public static final int MAX_HIRE_LEVEL = 100;
    public static final int MAX_PARTY_MEMBERS = squad_leader.MAX_GROUP_SIZE;
    // Payment operations deliberately have no automatic timeout.  A named-bank
    // callback can arrive late; retaining the nonce prevents a second debit or
    // a free duplicate spawn.  This age is diagnostic only.
    public static final int PAYMENT_DIAGNOSTIC_SECONDS = 30;
    public static final int GROUP_ENROLLMENT_RETRIES = 3;
    public static final float TERMINAL_USE_RANGE = 8.0f;

    public static final string_id SID_HIRE = new string_id(STF, "hire_merc");
    public static final string_id SID_TITLE = new string_id(STF, "hire_merc_title");
    public static final string_id SID_PROMPT = new string_id(STF, "hire_merc_prompt");
    public static final string_id SID_ROSTER_ENTRY = new string_id(STF, "roster_entry");
    public static final string_id SID_ALREADY_HIRED = new string_id(STF, "already_hired");
    public static final string_id SID_PAYMENT_PENDING = new string_id(STF, "payment_pending");
    public static final string_id SID_GROUP_FULL = new string_id(STF, "group_full");
    public static final string_id SID_INVALID_SELECTION = new string_id(STF, "invalid_selection");
    public static final string_id SID_OUT_OF_RANGE = new string_id(STF, "out_of_range");
    public static final string_id SID_PAYMENT_FAILED = new string_id(STF, "payment_failed");
    public static final string_id SID_SPAWN_FAILED = new string_id(STF, "spawn_failed_refund");
    public static final string_id SID_REFUND_FAILED = new string_id(STF, "refund_failed");
    public static final string_id SID_HIRED = new string_id(STF, "hired");
    public static final string_id SID_DISMISS = new string_id(STF, "dismiss_merc");
    public static final string_id SID_DISMISSED = new string_id(STF, "dismissed");
    public static final string_id SID_EXPIRED = new string_id(STF, "expired");
    public static final string_id SID_PARTY_LOST = new string_id(STF, "party_lost");

    // Every classic ground-combat family plus the Doctor and Ranger support
    // hybrids is represented.  There are deliberately no crafting,
    // entertainer, politician, or space-pilot entries.
    public static final String[] ARCHETYPE_KEYS =
    {
        "novice_brawler",
        "novice_marksman",
        "novice_medic",
        "bounty_hunter",
        "carbineer",
        "combat_medic",
        "commando",
        "creature_handler",
        "doctor",
        "fencer",
        "pikeman",
        "pistoleer",
        "rifleman",
        "ranger",
        "smuggler",
        "squad_leader",
        "swordsman",
        "teras_kasi_artist"
    };

    /** Creature rows provide stable appearances and weapon categories. */
    public static final String[] CREATURE_TYPES =
    {
        "borvo_bounty_hunter",                  // unarmed
        "bith_sniper",                          // rifle
        "medic",                                // pistol/support
        "bounty_hunter",                        // carbine
        "mercenary",                            // carbine
        "medic",                                // pistol/support
        "commando",                             // carbine
        "frontiersman",                         // carbine
        "medic",                                // pistol/medical support
        "mission_bounty_aakuan_spice_guard",    // one-handed sword
        "mission_bounty_aakuan_guardian",       // polearm
        "monumenter_smuggler",                  // pistol
        "bith_assassin",                        // rifle
        "frontiersman",                         // carbine/outdoors support
        "monumenter_smuggler",                  // pistol
        "sharpshooter",                         // rifle
        "precu_hire_merc_swordsman",            // neutral two-handed sword
        "borvo_bounty_hunter"                   // unarmed
    };

    // Ability slots unlock at levels 1, 25, 50, and 75.  Weapon damage and HAM
    // are independently scaled by create.createCreature's PRE-CU level profile.
    public static final String[][] COMBAT_ABILITIES =
    {
        { "unarmedHit1", "unarmedStun1", "unarmedBlind1", "unarmedSpinAttack1" },
        { "headShot1", "mindShot1", "strafeShot1", "sniperShot" },
        { "bodyShot1", "healthShot1", "disarmingShot1", "stoppingShot" },
        { "legShot1", "fullAutoSingle1", "actionShot1", "burstShot1" },
        { "legShot1", "fullAutoSingle1", "legShot2", "fullAutoArea1" },
        { "bodyShot1", "healthShot1", "disarmingShot1", "stoppingShot" },
        { "legShot1", "burstShot1", "fullAutoSingle1", "fullAutoArea1" },
        { "legShot1", "fullAutoSingle1", "actionShot1", "chargeShot1" },
        { "bodyShot1", "healthShot1", "disarmingShot1", "stoppingShot" },
        { "melee1hHit1", "melee1hBodyHit1", "melee1hDizzyHit1", "melee1hSpinAttack1" },
        { "polearmHit1", "polearmLegHit1", "polearmStun1", "polearmSpinAttack1" },
        { "bodyShot1", "healthShot1", "disarmingShot1", "stoppingShot" },
        { "headShot1", "mindShot1", "strafeShot1", "sniperShot" },
        { "legShot1", "fullAutoSingle1", "actionShot1", "chargeShot1" },
        { "bodyShot1", "healthShot1", "disarmingShot1", "stoppingShot" },
        { "headShot1", "warningShot", "strafeShot1", "sniperShot" },
        { "melee2hHit1", "melee2hHeadHit1", "melee2hSweep1", "melee2hSpinAttack1" },
        { "unarmedHit1", "unarmedStun1", "unarmedBlind1", "unarmedSpinAttack1" }
    };

    // 0 = no medical routine, 1 = first aid/support, 2 = combat medic.
    public static final int[] HEAL_GRADES =
    {
        0, 0, 1, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1, 0, 0
    };

    public static boolean isCombatMissionTerminal(obj_id terminal) throws InterruptedException
    {
        if (!isIdValid(terminal) || !exists(terminal))
        {
            return false;
        }

        // Buildout-authored generic templates can carry one of these role objvars,
        // so both the exact template allow-list and exclusions are required.
        if (hasObjVar(terminal, "intArtisan") ||
            hasObjVar(terminal, "intScout") ||
            hasObjVar(terminal, "intEntertainer") ||
            hasObjVar(terminal, "intBounty") ||
            hasObjVar(terminal, "intNewbie"))
        {
            return false;
        }

        String template = getTemplateName(terminal);
        return "object/tangible/terminal/terminal_mission.iff".equals(template) ||
            "object/tangible/terminal/terminal_mission_imperial.iff".equals(template) ||
            "object/tangible/terminal/terminal_mission_rebel.iff".equals(template);
    }

    public static boolean canUseTerminal(obj_id terminal, obj_id player)
        throws InterruptedException
    {
        return isCombatMissionTerminal(terminal) && isIdValid(player) &&
            exists(player) && player.isLoaded() && isPlayer(player) &&
            !isDead(player) && !isIncapacitated(player) &&
            getDistance(terminal, player) <= TERMINAL_USE_RANGE;
    }

    public static int getHireLevel(obj_id player) throws InterruptedException
    {
        int level = skill.getPrecuEncounterDifficulty(player);
        return Math.max(MIN_HIRE_LEVEL, Math.min(MAX_HIRE_LEVEL, level));
    }

    public static int getHireCostForLevel(int level)
    {
        int bounded = Math.max(MIN_HIRE_LEVEL, Math.min(MAX_HIRE_LEVEL, level));
        return BASE_HIRE_COST + (bounded * COST_PER_COMBAT_LEVEL);
    }

    public static int getAbilityTierForLevel(int level)
    {
        int bounded = Math.max(MIN_HIRE_LEVEL, Math.min(MAX_HIRE_LEVEL, level));
        return Math.min(4, 1 + ((bounded - 1) / 25));
    }

    public static string_id getArchetypeName(int index)
    {
        if (!isValidArchetype(index))
        {
            return new string_id(STF, "invalid_selection");
        }
        return new string_id(STF, "archetype_" + ARCHETYPE_KEYS[index]);
    }

    public static boolean isValidArchetype(int index)
    {
        return index >= 0 && index < ARCHETYPE_KEYS.length &&
            index < CREATURE_TYPES.length && index < COMBAT_ABILITIES.length &&
            index < HEAL_GRADES.length;
    }

    public static String[] getRosterEntries(obj_id player) throws InterruptedException
    {
        int level = getHireLevel(player);
        String[] entries = new String[ARCHETYPE_KEYS.length];
        for (int i = 0; i < entries.length; ++i)
        {
            prose_package pp = prose.getPackage(
                SID_ROSTER_ENTRY, null, null, getArchetypeName(i), level);
            entries[i] = " \0" + packOutOfBandProsePackage(null, pp);
        }
        return entries;
    }

    public static String getRosterPrompt(obj_id player) throws InterruptedException
    {
        int cost = getHireCostForLevel(getHireLevel(player));
        prose_package pp = prose.getPackage(SID_PROMPT, cost);
        return " \0" + packOutOfBandProsePackage(null, pp);
    }

    public static boolean hasActiveMercenary(obj_id player) throws InterruptedException
    {
        if (!hasObjVar(player, VAR_ACTIVE))
        {
            return false;
        }
        obj_id active = getObjIdObjVar(player, VAR_ACTIVE);
        if (!isIdValid(active) || !exists(active) ||
            !hasObjVar(active, VAR_OWNER) || getObjIdObjVar(active, VAR_OWNER) != player)
        {
            removeObjVar(player, VAR_ACTIVE);
            return false;
        }
        return true;
    }

    public static boolean isPartyFull(obj_id player) throws InterruptedException
    {
        obj_id groupId = getGroupObject(player);
        if (!isIdValid(groupId))
        {
            return false;
        }
        obj_id[] members = getGroupMemberIds(groupId);
        return members != null && members.length >= MAX_PARTY_MEMBERS;
    }

    public static void beginHire(obj_id terminal, obj_id player, int archetype)
        throws InterruptedException
    {
        if (!canUseTerminal(terminal, player))
        {
            sendSystemMessage(player, SID_OUT_OF_RANGE);
            return;
        }
        if (!isValidArchetype(archetype))
        {
            sendSystemMessage(player, SID_INVALID_SELECTION);
            return;
        }
        if (hasActiveMercenary(player))
        {
            sendSystemMessage(player, SID_ALREADY_HIRED);
            return;
        }
        if (isPartyFull(player))
        {
            sendSystemMessage(player, SID_GROUP_FULL);
            return;
        }

        if (hasObjVar(player, VAR_TX_NONCE))
        {
            sendSystemMessage(player, SID_PAYMENT_PENDING);
            return;
        }

        int level = getHireLevel(player);
        int cost = getHireCostForLevel(level);
        int nonce = getGameTime() ^ rand(100000, 999999) ^ (int)player.getValue();
        if (nonce == 0)
        {
            nonce = rand(100000, 999999);
        }

        if (!hasScript(player, PAYMENT_SCRIPT))
        {
            attachScript(player, PAYMENT_SCRIPT);
        }
        if (!hasScript(player, PAYMENT_SCRIPT))
        {
            sendSystemMessage(player, SID_PAYMENT_FAILED);
            return;
        }

        setObjVar(player, VAR_TX_NONCE, nonce);
        setObjVar(player, VAR_TX_PLAYER, player);
        setObjVar(player, VAR_TX_STARTED, getCalendarTime());
        setObjVar(player, VAR_TX_ARCHETYPE, archetype);
        setObjVar(player, VAR_TX_LEVEL, level);
        setObjVar(player, VAR_TX_COST, cost);
        setObjVar(player, VAR_TX_TERMINAL, terminal);
        setObjVar(player, VAR_TX_ACCOUNT, ACCOUNT);
        setObjVar(player, VAR_TX_STATE, STATE_QUEUED);

        dictionary params = new dictionary();
        params.put("npc", player); // routes the named-account NSF callback here too
        params.put("terminal", terminal);
        params.put("archetype", archetype);
        params.put("level", level);
        params.put("cost", cost);
        params.put("nonce", nonce);
        boolean dispatched = money.requestPayment(
            player, ACCOUNT, cost, "handleHireMercPayment", params, false);
        if (!dispatched)
        {
            // For this fully validated named-account request, false is the
            // authoritative no-debit path (including NSF, whose queued RET_FAIL
            // becomes an inert late callback after this nonce is cleared).
            clearPendingHire(player);
            detachScript(player, PAYMENT_SCRIPT);
            sendSystemMessage(player, SID_PAYMENT_FAILED);
        }
    }

    public static boolean validatePaymentCallback(obj_id player, dictionary params)
        throws InterruptedException
    {
        if (!validateDurableTransaction(player) || params == null ||
            params.isEmpty() || !hasObjVar(player, VAR_TX_NONCE) ||
            !hasObjVar(player, VAR_TX_PLAYER) ||
            getObjIdObjVar(player, VAR_TX_PLAYER) != player ||
            !hasObjVar(player, VAR_TX_ACCOUNT) ||
            !ACCOUNT.equals(getStringObjVar(player, VAR_TX_ACCOUNT)) ||
            !params.containsKey(money.DICT_CODE) ||
            !params.containsKey(money.DICT_PLAYER_ID) ||
            !params.containsKey(money.DICT_TARGET_ID) ||
            !params.containsKey(money.DICT_ACCT_NAME) ||
            !params.containsKey(money.DICT_AMOUNT) ||
            !params.containsKey(money.DICT_TOTAL) ||
            !params.containsKey("npc") ||
            !params.containsKey("terminal") ||
            !params.containsKey("archetype") ||
            !params.containsKey("level") ||
            !params.containsKey("cost") ||
            !params.containsKey("nonce") ||
            !STATE_QUEUED.equals(getStringObjVar(player, VAR_TX_STATE)))
        {
            return false;
        }
        int code = params.getInt(money.DICT_CODE);
        int nonce = params.getInt("nonce");
        int archetype = params.getInt("archetype");
        int level = params.getInt("level");
        int cost = params.getInt("cost");
        obj_id terminal = params.getObjId("terminal");
        return (code == money.RET_SUCCESS || code == money.RET_FAIL) &&
            params.getObjId(money.DICT_PLAYER_ID) == player &&
            params.getObjId(money.DICT_TARGET_ID) == obj_id.NULL_ID &&
            ACCOUNT.equals(params.getString(money.DICT_ACCT_NAME)) &&
            params.getInt(money.DICT_AMOUNT) == cost &&
            params.getInt(money.DICT_TOTAL) == cost &&
            params.getObjId("npc") == player &&
            nonce == getIntObjVar(player, VAR_TX_NONCE) &&
            archetype == getIntObjVar(player, VAR_TX_ARCHETYPE) &&
            level == getIntObjVar(player, VAR_TX_LEVEL) &&
            cost == getIntObjVar(player, VAR_TX_COST) &&
            terminal == getObjIdObjVar(player, VAR_TX_TERMINAL) &&
            isValidArchetype(archetype) &&
            cost == getHireCostForLevel(level);
    }

    public static boolean validateDurableTransaction(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player) ||
            !hasObjVar(player, VAR_TX_NONCE) ||
            !hasObjVar(player, VAR_TX_PLAYER) ||
            !hasObjVar(player, VAR_TX_STARTED) ||
            !hasObjVar(player, VAR_TX_ARCHETYPE) ||
            !hasObjVar(player, VAR_TX_LEVEL) ||
            !hasObjVar(player, VAR_TX_COST) ||
            !hasObjVar(player, VAR_TX_TERMINAL) ||
            !hasObjVar(player, VAR_TX_ACCOUNT) ||
            !hasObjVar(player, VAR_TX_STATE) ||
            getObjIdObjVar(player, VAR_TX_PLAYER) != player ||
            !ACCOUNT.equals(getStringObjVar(player, VAR_TX_ACCOUNT)))
        {
            return false;
        }
        int archetype = getIntObjVar(player, VAR_TX_ARCHETYPE);
        int level = getIntObjVar(player, VAR_TX_LEVEL);
        int cost = getIntObjVar(player, VAR_TX_COST);
        String state = getStringObjVar(player, VAR_TX_STATE);
        return getIntObjVar(player, VAR_TX_NONCE) != 0 &&
            getIntObjVar(player, VAR_TX_STARTED) > 0 &&
            isIdValid(getObjIdObjVar(player, VAR_TX_TERMINAL)) &&
            isValidArchetype(archetype) &&
            level >= MIN_HIRE_LEVEL && level <= MAX_HIRE_LEVEL &&
            cost == getHireCostForLevel(level) &&
            (STATE_QUEUED.equals(state) || STATE_DEBITED.equals(state) ||
             STATE_ENROLLING.equals(state) || STATE_REFUNDING.equals(state) ||
             STATE_COMPLETED.equals(state));
    }

    public static obj_id prepareHire(obj_id player, obj_id terminal,
        int archetype, int level) throws InterruptedException
    {
        if (!canUseTerminal(terminal, player) || hasActiveMercenary(player) ||
            isPartyFull(player) || !isValidArchetype(archetype))
        {
            return null;
        }

        location spawn = new location(getLocation(player));
        spawn.x += 2.0f;
        obj_id hired = create.createCreature(
            CREATURE_TYPES[archetype], spawn, level, true, false);
        if (!isIdValid(hired) || !exists(hired))
        {
            return null;
        }

        setObjVar(hired, "ai.pet", true);
        setObjVar(hired, VAR_OWNER, player);
        setObjVar(hired, VAR_ARCHETYPE, archetype);
        setObjVar(hired, VAR_LEVEL, level);
        setObjVar(hired, VAR_ABILITY_TIER, getAbilityTierForLevel(level));
        setObjVar(hired, VAR_WEAPON_QUALITY, level);
        setObjVar(hired, VAR_EXPIRES, getCalendarTime() + HIRE_DURATION_SECONDS);
        setObjVar(hired, VAR_NO_MISSION_CREDIT, 1);
        setObjVar(hired, "ai.pet.masterName", getEncodedName(player));
        setName(hired, getArchetypeName(archetype));

        // Mercenaries inherit the hirer's PvP relationship through their master;
        // authored source-creature factions must not make them independently aggro.
        removeObjVar(hired, "faction");
        pvpMakeNeutral(hired);
        clearCondition(hired, CONDITION_AGGRESSIVE);
        setWantSawAttackTriggers(hired, false);
        ai_lib.setDefaultCalmBehavior(hired, ai_lib.BEHAVIOR_SENTINEL);

        setMaster(hired, player);
        attachScript(hired, AI_SCRIPT);
        if (!hasScript(hired, AI_SCRIPT) || getMaster(hired) != player)
        {
            rollbackHire(player, hired);
            return null;
        }

        // Store the spawned oid before dispatching the asynchronous invite. If
        // the process stops at any later instruction, login reconciliation can
        // roll back this exact NPC and refund the accepted debit.
        setObjVar(player, VAR_TX_HIRED, hired);

        // Pet grouping is an explicit native invite/accept handshake; setMaster
        // alone does not enroll an NPC as a party member.
        obj_id groupLeader = group.getLeader(player);
        if (!isIdValid(groupLeader) || !exists(groupLeader))
        {
            groupLeader = player;
        }
        queueCommand(groupLeader, (-2007999144), hired, "",
            COMMAND_PRIORITY_IMMEDIATE);
        messageTo(hired, "handleGroupInvite", null, 1.0f, false);
        return hired;
    }

    public static boolean finalizeEnrollment(obj_id player, obj_id hired)
        throws InterruptedException
    {
        if (!isEnrollmentComplete(player, hired) || hasActiveMercenary(player))
        {
            return false;
        }

        setObjVar(player, VAR_ACTIVE, hired);
        int archetype = getIntObjVar(hired, VAR_ARCHETYPE);
        prose_package pp = prose.getPackage(SID_HIRED, getArchetypeName(archetype));
        sendSystemMessageProse(player, pp);
        return true;
    }

    public static boolean isEnrollmentComplete(obj_id player, obj_id hired)
        throws InterruptedException
    {
        if (!isIdValid(player) || !isPlayer(player) || !isIdValid(hired) ||
            !exists(hired) || getMaster(hired) != player ||
            !hasObjVar(hired, VAR_OWNER) ||
            getObjIdObjVar(hired, VAR_OWNER) != player ||
            !hasScript(hired, AI_SCRIPT))
        {
            return false;
        }
        obj_id playerGroup = getGroupObject(player);
        obj_id hiredGroup = getGroupObject(hired);
        if (!isIdValid(playerGroup) || playerGroup != hiredGroup)
        {
            return false;
        }
        if (!hasScript(hired, group.SCRIPT_GROUP_MEMBER))
        {
            attachScript(hired, group.SCRIPT_GROUP_MEMBER);
        }
        return hasScript(hired, group.SCRIPT_GROUP_MEMBER);
    }

    public static void repeatGroupInvite(obj_id player, obj_id hired)
        throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(hired) || !exists(hired) ||
            getMaster(hired) != player)
        {
            return;
        }
        obj_id groupLeader = group.getLeader(player);
        if (!isIdValid(groupLeader) || !exists(groupLeader))
        {
            groupLeader = player;
        }
        queueCommand(groupLeader, (-2007999144), hired, "",
            COMMAND_PRIORITY_IMMEDIATE);
        messageTo(hired, "handleGroupInvite", null, 1.0f, false);
    }

    public static void rollbackHire(obj_id player, obj_id hired)
        throws InterruptedException
    {
        if (isIdValid(player) && hasObjVar(player, VAR_ACTIVE) &&
            getObjIdObjVar(player, VAR_ACTIVE) == hired)
        {
            removeObjVar(player, VAR_ACTIVE);
        }
        if (!isIdValid(hired) || !exists(hired))
        {
            return;
        }
        obj_id groupId = getGroupObject(hired);
        if (isIdValid(groupId))
        {
            queueCommand(hired, (1348589140), groupId, "",
                COMMAND_PRIORITY_IMMEDIATE);
        }
        setMaster(hired, null);
        removeObjVar(hired, VAR_ROOT);
        destroyObject(hired);
    }

    public static void clearPendingHire(obj_id player) throws InterruptedException
    {
        removeObjVar(player, VAR_TX_ROOT);
        // Clean the transient pre-durability shape if a development shard was
        // running an earlier revision when this update was deployed.
        utils.removeScriptVarTree(player, LEGACY_SV_PENDING);
    }

    public static String getCombatAbility(int archetype, int level, int sequence)
    {
        if (!isValidArchetype(archetype))
        {
            return "defaultAttack";
        }
        int unlocked = getAbilityTierForLevel(level);
        String[] abilities = COMBAT_ABILITIES[archetype];
        unlocked = Math.min(unlocked, abilities.length);
        int slot = Math.abs(sequence) % Math.max(1, unlocked);
        return abilities[slot];
    }

    public static int getHealGrade(int archetype)
    {
        return isValidArchetype(archetype) ? HEAL_GRADES[archetype] : 0;
    }
}
