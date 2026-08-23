package script.ai;

import script.*;
import script.combat_engine.combat_data;
import script.library.*;

/**
 * Bounded party-companion controller for Hire a Merc NPCs.
 *
 * Ambient AI reactions are disabled.  This script is the sole authority for
 * assist targets: the party leader must be in combat, and nearby follow-on
 * targets must share both the creature row and the mission/lair anchor of the
 * leader's direct target.
 */
public class mercenary_party_member extends script.base_script
{
    private static final String SV_ROOT = "precu.hireMerc.ai";
    private static final String SV_RUNNING = SV_ROOT + ".running";
    private static final String SV_OWNER_MISSING = SV_ROOT + ".ownerMissing";
    private static final String SV_GROUP_MISSING = SV_ROOT + ".groupMissing";
    private static final String SV_FOCUS = SV_ROOT + ".focus";
    private static final String SV_FOCUS_NAME = SV_ROOT + ".focusName";
    private static final String SV_FOCUS_ANCHOR = SV_ROOT + ".focusAnchor";
    private static final String SV_LAST_HEAL = SV_ROOT + ".lastHeal";
    private static final String SV_LAST_ABILITY = SV_ROOT + ".lastAbility";
    private static final String SV_ABILITY_SEQUENCE = SV_ROOT + ".abilitySequence";

    private static final float HEARTBEAT_SECONDS = 1.0f;
    private static final float FOLLOW_MIN = 2.0f;
    private static final float FOLLOW_MAX = 5.0f;
    private static final float ASSIST_RADIUS = 48.0f;
    private static final float HEAL_RADIUS = 32.0f;
    private static final int HEAL_COOLDOWN_SECONDS = 6;
    private static final int ABILITY_COOLDOWN_SECONDS = 4;
    private static final int OWNER_MISSING_GRACE_SECONDS = 60;
    private static final int GROUP_REPAIR_ATTEMPTS = 5;
    private static final String OWN_VEHICLE_TEMPLATE =
        "object/mobile/vehicle/speederbike.iff";
    private static final String VEHICLE_PING_SCRIPT =
        "systems.vehicle_system.vehicle_ping";

    public mercenary_party_member()
    {
    }

    public int OnAttach(obj_id self) throws InterruptedException
    {
        startController(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        startController(self);
        return SCRIPT_CONTINUE;
    }

    private void startController(obj_id self) throws InterruptedException
    {
        setWantSawAttackTriggers(self, false);
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_SENTINEL);
        if (!utils.hasScriptVar(self, SV_RUNNING))
        {
            utils.setScriptVar(self, SV_RUNNING, 1);
            messageTo(self, "handleMercenaryHeartbeat", null,
                HEARTBEAT_SECONDS, false);
        }
    }

    /** Native pet grouping is invite/accept; setMaster alone is insufficient. */
    public int handleGroupInvite(obj_id self, dictionary params)
        throws InterruptedException
    {
        queueCommand(self, (-1449236473), null, "", COMMAND_PRIORITY_IMMEDIATE);
        return SCRIPT_CONTINUE;
    }

    public int handleMercenaryHeartbeat(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isIdValid(self) || !exists(self) || isDead(self))
        {
            return SCRIPT_CONTINUE;
        }

        setWantSawAttackTriggers(self, false);
        obj_id owner = getOwnerFromMercenary(self);
        if (!isIdValid(owner) || !exists(owner) || !owner.isLoaded())
        {
            if (!utils.hasScriptVar(self, SV_OWNER_MISSING))
            {
                utils.setScriptVar(self, SV_OWNER_MISSING, getGameTime());
            }
            else if (getGameTime() -
                utils.getIntScriptVar(self, SV_OWNER_MISSING) >=
                OWNER_MISSING_GRACE_SECONDS)
            {
                cleanupMercenary(self, null);
                return SCRIPT_CONTINUE;
            }
            suppressCombat(self);
            scheduleHeartbeat(self);
            return SCRIPT_CONTINUE;
        }
        utils.removeScriptVar(self, SV_OWNER_MISSING);

        if (getCalendarTime() >= getIntObjVar(self, mercenary.VAR_EXPIRES))
        {
            cleanupMercenary(self, mercenary.SID_EXPIRED);
            return SCRIPT_CONTINUE;
        }

        // During the debit/enrollment handshake the NPC is intentionally inert.
        if (!hasObjVar(owner, mercenary.VAR_ACTIVE) ||
            getObjIdObjVar(owner, mercenary.VAR_ACTIVE) != self)
        {
            suppressCombat(self);
            scheduleHeartbeat(self);
            return SCRIPT_CONTINUE;
        }

        if (!ensurePartyMembership(self, owner))
        {
            scheduleHeartbeat(self);
            return SCRIPT_CONTINUE;
        }

        obj_id leader = group.getLeader(owner);
        if (!isIdValid(leader) || !exists(leader) || !leader.isLoaded())
        {
            leader = owner;
        }

        manageVehicle(self, leader);
        boolean healed = tryHealParty(self);

        if (!ai_lib.isInCombat(leader))
        {
            suppressCombat(self);
            clearFocus(self);
            followLeader(self, leader);
            scheduleHeartbeat(self);
            return SCRIPT_CONTINUE;
        }

        obj_id target = selectAssistTarget(self, leader);
        if (!isValidCombatTarget(self, leader, target))
        {
            suppressCombat(self);
            followLeader(self, leader);
            scheduleHeartbeat(self);
            return SCRIPT_CONTINUE;
        }

        startCombat(self, target);
        if (!healed)
        {
            useScaledAbility(self, target);
        }
        scheduleHeartbeat(self);
        return SCRIPT_CONTINUE;
    }

    public int OnEnteredCombat(obj_id self) throws InterruptedException
    {
        obj_id leader = getPartyLeader(self);
        if (!isIdValid(leader) || !ai_lib.isInCombat(leader))
        {
            suppressCombat(self);
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnDefenderCombatAction(obj_id self, obj_id attacker,
        obj_id weapon, int combatResult) throws InterruptedException
    {
        obj_id leader = getPartyLeader(self);
        obj_id direct = getLeaderCombatTarget(leader);
        if (!isIdValid(leader) || !ai_lib.isInCombat(leader) ||
            (attacker != direct && !matchesFocusedFamily(self, attacker)))
        {
            suppressCombat(self);
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }

    public int OnSawAttack(obj_id self, obj_id defender, obj_id[] attackers)
        throws InterruptedException
    {
        // The heartbeat admits only the leader-directed target family.
        return SCRIPT_OVERRIDE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi)
        throws InterruptedException
    {
        if (player == getOwnerFromMercenary(self))
        {
            mi.addRootMenu(menu_info_types.SERVER_MENU1, mercenary.SID_DISMISS);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item)
        throws InterruptedException
    {
        if (item == menu_info_types.SERVER_MENU1 &&
            player == getOwnerFromMercenary(self))
        {
            cleanupMercenary(self, mercenary.SID_DISMISSED);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnIncapacitated(obj_id self, obj_id attacker)
        throws InterruptedException
    {
        // Hired party members are not loot sources.  Their scaled weapon and any
        // incidental source-template inventory are implementation equipment.
        obj_id weapon = getCurrentWeapon(self);
        if (isIdValid(weapon) && !isDefaultWeapon(weapon))
        {
            destroyObject(weapon);
        }
        obj_id inventory = utils.getInventoryContainer(self);
        if (isIdValid(inventory))
        {
            utils.emptyContainer(inventory);
        }
        releaseOwnerAndVehicle(self);
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self) throws InterruptedException
    {
        releaseOwnerAndVehicle(self);
        return SCRIPT_CONTINUE;
    }

    private boolean ensurePartyMembership(obj_id self, obj_id owner)
        throws InterruptedException
    {
        obj_id ownerGroup = getGroupObject(owner);
        obj_id myGroup = getGroupObject(self);
        if (isIdValid(ownerGroup) && ownerGroup == myGroup)
        {
            utils.removeScriptVar(self, SV_GROUP_MISSING);
            return true;
        }

        suppressCombat(self);
        int attempts = utils.hasScriptVar(self, SV_GROUP_MISSING) ?
            utils.getIntScriptVar(self, SV_GROUP_MISSING) + 1 : 1;
        utils.setScriptVar(self, SV_GROUP_MISSING, attempts);
        if (attempts > GROUP_REPAIR_ATTEMPTS)
        {
            cleanupMercenary(self, mercenary.SID_PARTY_LOST);
            return false;
        }
        mercenary.repeatGroupInvite(owner, self);
        return false;
    }

    private obj_id getOwnerFromMercenary(obj_id self)
        throws InterruptedException
    {
        if (hasObjVar(self, mercenary.VAR_OWNER))
        {
            return getObjIdObjVar(self, mercenary.VAR_OWNER);
        }
        return getMaster(self);
    }

    private obj_id getPartyLeader(obj_id self) throws InterruptedException
    {
        obj_id owner = getOwnerFromMercenary(self);
        if (!isIdValid(owner) || !exists(owner))
        {
            return null;
        }
        obj_id leader = group.getLeader(owner);
        return isIdValid(leader) ? leader : owner;
    }

    private void followLeader(obj_id self, obj_id leader)
        throws InterruptedException
    {
        if (getDistance(self, leader) > FOLLOW_MAX)
        {
            // Native mount locomotion is rider-driven; applying the normal AI
            // follow path to the mounted mercenary is what steers its fallback
            // vehicle. A passenger already shares the leader mount and has
            // effectively zero separation, so this does not fight the driver.
            ai_lib.aiFollow(self, leader, FOLLOW_MIN, FOLLOW_MAX);
        }
    }

    private void suppressCombat(obj_id self) throws InterruptedException
    {
        stopCombat(self);
        clearHateList(self);
    }

    private void scheduleHeartbeat(obj_id self) throws InterruptedException
    {
        if (isIdValid(self) && exists(self))
        {
            messageTo(self, "handleMercenaryHeartbeat", null,
                HEARTBEAT_SECONDS, false);
        }
    }

    private obj_id getLeaderCombatTarget(obj_id leader)
        throws InterruptedException
    {
        if (!isIdValid(leader) || !exists(leader))
        {
            return null;
        }
        obj_id target = getHateTarget(leader);
        if (!isIdValid(target))
        {
            target = getCombatTarget(leader);
        }
        if (!isIdValid(target))
        {
            target = getIntendedTarget(leader);
        }
        return target;
    }

    private obj_id selectAssistTarget(obj_id self, obj_id leader)
        throws InterruptedException
    {
        obj_id direct = getLeaderCombatTarget(leader);
        if (isValidCombatTarget(self, leader, direct))
        {
            rememberFocus(self, direct);
            return direct;
        }

        obj_id current = getHateTarget(self);
        if (isValidCombatTarget(self, leader, current) &&
            matchesFocusedFamily(self, current))
        {
            return current;
        }

        obj_id[] nearby = getCreaturesInRange(leader, ASSIST_RADIUS);
        if (nearby == null)
        {
            return null;
        }
        for (obj_id candidate : nearby)
        {
            if (isValidCombatTarget(self, leader, candidate) &&
                !isPlayer(candidate) && matchesFocusedFamily(self, candidate))
            {
                return candidate;
            }
        }
        return null;
    }

    private boolean isValidCombatTarget(obj_id self, obj_id leader, obj_id target)
        throws InterruptedException
    {
        return isIdValid(target) && exists(target) && target.isLoaded() &&
            target != self && target != leader && !isDead(target) &&
            pvpCanAttack(leader, target) && pvpCanAttack(self, target);
    }

    private void rememberFocus(obj_id self, obj_id target)
        throws InterruptedException
    {
        utils.setScriptVar(self, SV_FOCUS, target);
        String creatureName = getCreatureName(target);
        if (creatureName != null && !creatureName.equals(""))
        {
            utils.setScriptVar(self, SV_FOCUS_NAME, creatureName);
        }
        else
        {
            utils.removeScriptVar(self, SV_FOCUS_NAME);
        }
        obj_id anchor = getMissionOrLairAnchor(target);
        if (isIdValid(anchor))
        {
            utils.setScriptVar(self, SV_FOCUS_ANCHOR, anchor);
        }
        else
        {
            utils.removeScriptVar(self, SV_FOCUS_ANCHOR);
        }
    }

    private boolean matchesFocusedFamily(obj_id self, obj_id candidate)
        throws InterruptedException
    {
        if (!isIdValid(candidate) || !exists(candidate) ||
            !utils.hasScriptVar(self, SV_FOCUS_NAME))
        {
            return false;
        }
        String expectedName = utils.getStringScriptVar(self, SV_FOCUS_NAME);
        String candidateName = getCreatureName(candidate);
        if (candidateName == null || !candidateName.equals(expectedName))
        {
            return false;
        }

        obj_id candidateAnchor = getMissionOrLairAnchor(candidate);
        if (utils.hasScriptVar(self, SV_FOCUS_ANCHOR))
        {
            return candidateAnchor ==
                utils.getObjIdScriptVar(self, SV_FOCUS_ANCHOR);
        }
        // Unanchored wilderness targets never bleed into mission/lair spawns.
        return !isIdValid(candidateAnchor);
    }

    private obj_id getMissionOrLairAnchor(obj_id target)
        throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target))
        {
            return null;
        }
        if (hasObjVar(target, "objMission"))
        {
            return getObjIdObjVar(target, "objMission");
        }

        obj_id anchor = null;
        if (hasObjVar(target, "npc_lair.target"))
        {
            anchor = getObjIdObjVar(target, "npc_lair.target");
        }
        else if (hasObjVar(target, poi.POI_BASE_OBJECT))
        {
            anchor = getObjIdObjVar(target, poi.POI_BASE_OBJECT);
        }
        if (!isIdValid(anchor) || !exists(anchor))
        {
            return null;
        }
        if (hasObjVar(anchor, "objMission"))
        {
            return getObjIdObjVar(anchor, "objMission");
        }
        if (hasObjVar(anchor, poi.POI_BASE_OBJECT))
        {
            obj_id base = getObjIdObjVar(anchor, poi.POI_BASE_OBJECT);
            if (isIdValid(base))
            {
                return base;
            }
        }
        return anchor;
    }

    private void clearFocus(obj_id self) throws InterruptedException
    {
        utils.removeScriptVar(self, SV_FOCUS);
        utils.removeScriptVar(self, SV_FOCUS_NAME);
        utils.removeScriptVar(self, SV_FOCUS_ANCHOR);
    }

    private void useScaledAbility(obj_id self, obj_id target)
        throws InterruptedException
    {
        int now = getGameTime();
        if (utils.hasScriptVar(self, SV_LAST_ABILITY) &&
            now - utils.getIntScriptVar(self, SV_LAST_ABILITY) <
                ABILITY_COOLDOWN_SECONDS)
        {
            return;
        }
        int archetype = getIntObjVar(self, mercenary.VAR_ARCHETYPE);
        int level = getIntObjVar(self, mercenary.VAR_LEVEL);
        int sequence = utils.hasScriptVar(self, SV_ABILITY_SEQUENCE) ?
            utils.getIntScriptVar(self, SV_ABILITY_SEQUENCE) : 0;
        int unlocked = mercenary.getAbilityTierForLevel(level);
        for (int offset = 0; offset < unlocked; ++offset)
        {
            String ability = mercenary.getCombatAbility(
                archetype, level, sequence + offset);
            String bestAbility = combat.getBestAction(self, ability);
            if (combat.canPerformAction(bestAbility, self) !=
                combat.ACTION_SUCCESS)
            {
                continue;
            }

            combat_data actionData = combat_engine.getCombatData(bestAbility);
            range_info weaponRange = aiGetWeaponRangeInfo(getCurrentWeapon(self));
            if (actionData == null || weaponRange == null)
            {
                continue;
            }
            float distance = getDistance(self, target);
            if ((distance >= weaponRange.maxRange &&
                 distance >= actionData.maxRange) || !canSee(self, target))
            {
                continue;
            }
            if (actionData.cooldownGroup != null &&
                !actionData.cooldownGroup.equals("") &&
                getCooldownTimeLeft(
                    self, getStringCrc(actionData.cooldownGroup)) > 0.0f)
            {
                continue;
            }
            if (queueCommand(self, getStringCrc(bestAbility.toLowerCase()),
                target, "", COMMAND_PRIORITY_DEFAULT))
            {
                utils.setScriptVar(
                    self, SV_ABILITY_SEQUENCE, sequence + offset + 1);
                utils.setScriptVar(self, SV_LAST_ABILITY, now);
                return;
            }
        }
    }

    private boolean tryHealParty(obj_id self) throws InterruptedException
    {
        int archetype = getIntObjVar(self, mercenary.VAR_ARCHETYPE);
        int grade = mercenary.getHealGrade(archetype);
        int now = getGameTime();
        if (grade <= 0 ||
            (utils.hasScriptVar(self, SV_LAST_HEAL) &&
             now - utils.getIntScriptVar(self, SV_LAST_HEAL) <
                HEAL_COOLDOWN_SECONDS))
        {
            return false;
        }

        java.util.Vector members = group.getPCMembersInRange(self, HEAL_RADIUS);
        if (members == null)
        {
            return false;
        }
        obj_id patient = null;
        int attribute = HEALTH;
        float worstRatio = 1.0f;
        int[] primaryPools = { HEALTH, ACTION, MIND };
        for (Object entry : members)
        {
            obj_id member = (obj_id)entry;
            if (!isIdValid(member) || isDead(member) || isIncapacitated(member))
            {
                continue;
            }
            for (int pool : primaryPools)
            {
                int maximum = getMaxAttrib(member, pool);
                int current = getAttrib(member, pool);
                if (maximum > 0)
                {
                    float ratio = (float)current / (float)maximum;
                    if (ratio < worstRatio)
                    {
                        worstRatio = ratio;
                        patient = member;
                        attribute = pool;
                    }
                }
            }
        }
        if (!isIdValid(patient) || worstRatio >= 0.85f)
        {
            return false;
        }

        int level = getIntObjVar(self, mercenary.VAR_LEVEL);
        int amount = (grade == 2 ? 300 : 150) +
            (level * (grade == 2 ? 10 : 5));
        applyDamageHealing(patient, self, attribute, amount, true);
        utils.setScriptVar(self, SV_LAST_HEAL, now);
        return true;
    }

    private void manageVehicle(obj_id self, obj_id leader)
        throws InterruptedException
    {
        obj_id leaderMount = getMountId(leader);
        obj_id myMount = getMountId(self);
        obj_id ownVehicle = hasObjVar(self, mercenary.VAR_OWN_VEHICLE) ?
            getObjIdObjVar(self, mercenary.VAR_OWN_VEHICLE) : null;

        if (!isIdValid(leaderMount))
        {
            if (isIdValid(myMount))
            {
                dismountCreature(self);
            }
            destroyOwnVehicle(self, ownVehicle);
            return;
        }
        if (myMount == leaderMount)
        {
            return;
        }
        if (isIdValid(myMount))
        {
            dismountCreature(self);
            if (myMount == ownVehicle)
            {
                destroyOwnVehicle(self, ownVehicle);
                ownVehicle = null;
            }
        }

        // Always attempt an available passenger seat before fallback creation.
        if (doesMountHaveRoom(leaderMount) &&
            vehicle.mountPermissionCheck(leaderMount, self, false) &&
            mountCreature(self, leaderMount))
        {
            destroyOwnVehicle(self, ownVehicle);
            return;
        }

        if (!isIdValid(ownVehicle) || !exists(ownVehicle))
        {
            ownVehicle = createOwnVehicle(self);
        }
        if (isIdValid(ownVehicle) && !isIdValid(getMountId(self)))
        {
            mountCreature(self, ownVehicle);
        }
    }

    private obj_id createOwnVehicle(obj_id self) throws InterruptedException
    {
        location spawn = new location(getLocation(self));
        spawn.x += 2.0f;
        obj_id ownVehicle = create.object(OWN_VEHICLE_TEMPLATE, spawn, false);
        if (!isIdValid(ownVehicle) || !exists(ownVehicle))
        {
            return null;
        }
        setOwner(ownVehicle, self);
        setObjVar(ownVehicle, mercenary.VAR_OWNER, self);
        if (hasScript(ownVehicle, VEHICLE_PING_SCRIPT))
        {
            // This bounded, nonpersistent fallback intentionally has no VCD.
            detachScript(ownVehicle, VEHICLE_PING_SCRIPT);
        }
        if (couldPetBeMadeMountable(ownVehicle) != 0 ||
            !makePetMountable(ownVehicle))
        {
            destroyObject(ownVehicle);
            return null;
        }
        vehicle.initializeVehicle(ownVehicle, self);
        setObjVar(self, mercenary.VAR_OWN_VEHICLE, ownVehicle);
        return ownVehicle;
    }

    private void destroyOwnVehicle(obj_id self, obj_id ownVehicle)
        throws InterruptedException
    {
        if (isIdValid(ownVehicle) && exists(ownVehicle))
        {
            if (getMountId(self) == ownVehicle)
            {
                dismountCreature(self);
            }
            destroyObject(ownVehicle);
        }
        if (hasObjVar(self, mercenary.VAR_OWN_VEHICLE))
        {
            removeObjVar(self, mercenary.VAR_OWN_VEHICLE);
        }
    }

    private void cleanupMercenary(obj_id self, string_id ownerMessage)
        throws InterruptedException
    {
        obj_id owner = getOwnerFromMercenary(self);
        if (isIdValid(owner) && owner.isLoaded() && ownerMessage != null)
        {
            sendSystemMessage(owner, ownerMessage);
        }
        releaseOwnerAndVehicle(self);
        if (isIdValid(self) && exists(self))
        {
            removeObjVar(self, mercenary.VAR_ROOT);
            destroyObject(self);
        }
    }

    private void releaseOwnerAndVehicle(obj_id self)
        throws InterruptedException
    {
        obj_id owner = getOwnerFromMercenary(self);
        obj_id ownVehicle = hasObjVar(self, mercenary.VAR_OWN_VEHICLE) ?
            getObjIdObjVar(self, mercenary.VAR_OWN_VEHICLE) : null;
        if (isIdValid(getMountId(self)))
        {
            dismountCreature(self);
        }
        destroyOwnVehicle(self, ownVehicle);
        if (isIdValid(owner) && exists(owner) &&
            hasObjVar(owner, mercenary.VAR_ACTIVE) &&
            getObjIdObjVar(owner, mercenary.VAR_ACTIVE) == self)
        {
            removeObjVar(owner, mercenary.VAR_ACTIVE);
        }
        obj_id groupId = getGroupObject(self);
        if (isIdValid(groupId))
        {
            queueCommand(self, (1348589140), groupId, "",
                COMMAND_PRIORITY_IMMEDIATE);
        }
        setMaster(self, null);
    }
}
