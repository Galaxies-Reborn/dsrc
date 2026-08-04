package script.ai;

import script.dictionary;
import script.library.*;
import script.obj_id;
import script.string_id;

import java.util.Vector;

public class pet_master extends script.base_script
{
    public pet_master()
    {
    }
    public static final String CREATURE_TABLE = "datatables/mob/creatures.iff";
    public static final String STF_FILE = "pet/droid_modules";
    public static final string_id SID_SYS_EMBOLDEN = new string_id("pet/pet_menu", "sys_embolden");
    public static final string_id SID_SYS_FAIL_EMBOLDEN = new string_id("pet/pet_menu", "sys_fail_embolden");
    public static final string_id SID_SYS_ENRAGE = new string_id("pet/pet_menu", "sys_enrage");
    public static final string_id SID_SYS_FAIL_ENRAGE = new string_id("pet/pet_menu", "sys_fail_enrage");
    public static final string_id SID_SYS_CANT_BUFF = new string_id("pet/pet_menu", "sys_cant_buff");
    public static final String PRECU_EMBOLDEN_BUFF = "emboldenPet";
    public static final String PRECU_EMBOLDEN_COOLDOWN =
        "pet.precuEmboldenCooldownUntil";
    public static final float PRECU_EMBOLDEN_RANGE = 50.0f;
    public static final float PRECU_EMBOLDEN_DURATION = 60.0f;
    public static final int PRECU_EMBOLDEN_COOLDOWN_SECONDS = 300;
    public static final int PRECU_EMBOLDEN_BASE_MIND_COST = 100;
    private static final long PRECU_EMBOLDEN_PLAYER_OID = 44003778L;
    private static final int PRECU_EMBOLDEN_STATION_ID = 91001;
    private static final int PRECU_EMBOLDEN_PROTOCOL_VERSION = 1;
    private static final String PRECU_EMBOLDEN_FIXTURE_ROOT =
        "precu.emboldenPetsCommandFixture";
    public int OnRemovedFromGroup(obj_id self, obj_id group) throws InterruptedException
    {
        if (!callable.hasAnyCallable(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id[] memberList = getGroupMemberIds(group);
        if (memberList == null || memberList.length < 1)
        {
            return SCRIPT_CONTINUE;
        }
        for (obj_id obj_id : memberList) {
            if (isIdValid(obj_id) && pet_lib.isMyPet(obj_id, self)) {
                queueCommand(obj_id, (1348589140), group, "", COMMAND_PRIORITY_DEFAULT);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnDefenderCombatAction(obj_id self, obj_id attacker, obj_id weapon, int combatResult) throws InterruptedException
    {
        obj_id pet = callable.getCallable(self, callable.CALLABLE_TYPE_COMBAT_PET);
        if (isIdValid(pet) && exists(pet) && !ai_lib.isInCombat(pet) && !beast_lib.isBeast(pet) && utils.hasScriptVar(pet, "ai.pet.guarding"))
        {
            addHate(pet, attacker, 0.0f);
        }
        return SCRIPT_CONTINUE;
    }
    public int failPetBuff(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        sendSystemMessage(self, SID_SYS_CANT_BUFF);
        return SCRIPT_CONTINUE;
    }
    public int emboldenPets(obj_id self, obj_id target, String params,
        float defaultTime) throws InterruptedException
    {
        obj_id pet = callable.getCallable(
            self, callable.CALLABLE_TYPE_COMBAT_PET);
        boolean fixture = isPrecuEmboldenFixture(self, pet);
        if (fixture)
        {
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".handlerEntered", 1);
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".handlerCalls",
                readPrecuEmboldenInt(self, ".handlerCalls") + 1);
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".outcome", "entered");
        }
        if (!isIdValid(self) || !exists(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self) ||
            !hasSkill(self, "outdoors_creaturehandler_healing_02"))
        {
            recordPrecuEmboldenOutcome(self, fixture, "playerRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(pet) || !exists(pet) ||
            !pet_lib.isMyPet(pet, self) || !pet_lib.isCreaturePet(pet) ||
            ai_lib.isAiDead(pet) || isIncapacitated(pet) ||
            getDistance(self, pet) > PRECU_EMBOLDEN_RANGE)
        {
            sendSystemMessage(self, SID_SYS_FAIL_EMBOLDEN);
            recordPrecuEmboldenOutcome(self, fixture, "petRejected");
            return SCRIPT_CONTINUE;
        }
        int now = getGameTime();
        if (buff.hasBuff(pet, PRECU_EMBOLDEN_BUFF) ||
            (hasObjVar(pet, PRECU_EMBOLDEN_COOLDOWN) &&
                getIntObjVar(pet, PRECU_EMBOLDEN_COOLDOWN) > now))
        {
            showFlyText(pet,
                new string_id("combat_effects", "pet_embolden_no"),
                1.0f, 0, 153, 0);
            sendSystemMessage(self, SID_SYS_FAIL_EMBOLDEN);
            recordPrecuEmboldenOutcome(self, fixture, "cooldownRejected");
            return SCRIPT_CONTINUE;
        }
        int mindCost = getPrecuEmboldenMindCost(self);
        int mindBefore = getAttrib(self, MIND);
        if (mindBefore <= mindCost)
        {
            sendSystemMessage(self, SID_SYS_FAIL_EMBOLDEN);
            recordPrecuEmboldenOutcome(self, fixture, "notEnoughMind");
            return SCRIPT_CONTINUE;
        }
        if (!buff.applyBuff(
            pet, self, PRECU_EMBOLDEN_BUFF, PRECU_EMBOLDEN_DURATION))
        {
            sendSystemMessage(self, SID_SYS_CANT_BUFF);
            recordPrecuEmboldenOutcome(self, fixture, "buffRejected");
            return SCRIPT_CONTINUE;
        }
        setObjVar(pet, PRECU_EMBOLDEN_COOLDOWN,
            now + PRECU_EMBOLDEN_COOLDOWN_SECONDS);
        setAttrib(self, MIND, mindBefore - mindCost);
        showFlyText(pet,
            new string_id("combat_effects", "pet_embolden"),
            1.0f, 0, 153, 0);
        sendSystemMessage(self, SID_SYS_EMBOLDEN);
        if (fixture)
        {
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".mindCost", mindCost);
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".cooldownUntil",
                now + PRECU_EMBOLDEN_COOLDOWN_SECONDS);
            setObjVar(self, PRECU_EMBOLDEN_FIXTURE_ROOT + ".completedAt", now);
        }
        recordPrecuEmboldenOutcome(self, fixture, "passed");
        return SCRIPT_CONTINUE;
    }
    private int getPrecuEmboldenMindCost(obj_id player)
        throws InterruptedException
    {
        float cost = PRECU_EMBOLDEN_BASE_MIND_COST;
        cost -= ((getAttrib(player, FOCUS) - 300.0f) / 1200.0f) * cost;
        return Math.max(0, (int)cost);
    }
    private boolean isPrecuEmboldenFixture(obj_id player, obj_id pet)
        throws InterruptedException
    {
        return isIdValid(player) && exists(player) && isPlayer(player) &&
            player.getValue() == PRECU_EMBOLDEN_PLAYER_OID &&
            getPlayerStationId(player) == PRECU_EMBOLDEN_STATION_ID &&
            hasObjVar(player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".protocol") &&
            getIntObjVar(player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".protocol") ==
                PRECU_EMBOLDEN_PROTOCOL_VERSION &&
            hasObjVar(player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".prepared") &&
            getIntObjVar(player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".prepared") == 1 &&
            hasObjVar(player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".pet") &&
            isIdValid(pet) && pet.equals(getObjIdObjVar(
                player, PRECU_EMBOLDEN_FIXTURE_ROOT + ".pet"));
    }
    private int readPrecuEmboldenInt(obj_id player, String suffix)
        throws InterruptedException
    {
        String path = PRECU_EMBOLDEN_FIXTURE_ROOT + suffix;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }
    private void recordPrecuEmboldenOutcome(obj_id player, boolean fixture,
        String outcome) throws InterruptedException
    {
        if (fixture)
        {
            setObjVar(player,
                PRECU_EMBOLDEN_FIXTURE_ROOT + ".outcome", outcome);
        }
    }
    public int OnSkillGranted(obj_id self, String skillName) throws InterruptedException
    {
        if (hasObjVar(self, "familiar"))
        {
            obj_id pet = getObjIdObjVar(self, "familiar");
            if (exists(pet) && isInWorld(pet))
            {
                dictionary trickData = new dictionary();
                trickData.put("pet", pet);
                trickData.put("trickNum", 2);
                trickData.put("heartBeat", false);
                messageTo(pet, "doFamiliarTrick", trickData, 1, false);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnRemovingFromWorld(obj_id self) throws InterruptedException
    {
        if (!callable.hasAnyCallable(self))
        {
            return SCRIPT_CONTINUE;
        }
        callable.storeCallables(self);
        return SCRIPT_CONTINUE;
    }
    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        if (!callable.hasAnyCallable(self))
        {
            return SCRIPT_CONTINUE;
        }
        if (pet_lib.isPet(killer))
        {
            killer = getMaster(killer);
        }
        callable.killCallables(self, killer);
        return SCRIPT_CONTINUE;
    }
    public int cmdDetonateDroid(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        LOG("droid_module", "ai.pet_master.cmdDetonateDroid");
        if (!isIdValid(target))
        {
            target = getLookAtTarget(self);
            if (!isIdValid(target))
            {
                sendSystemMessage(self, new string_id(STF_FILE, "invalid_droid_bomb"));
                return SCRIPT_CONTINUE;
            }
        }
        if (!pet_lib.isDroidPet(target))
        {
            sendSystemMessage(self, new string_id(STF_FILE, "invalid_droid_bomb"));
            return SCRIPT_CONTINUE;
        }
        if (getMaster(target) != self)
        {
            sendSystemMessage(self, new string_id(STF_FILE, "must_be_owner_droid_bomb"));
            return SCRIPT_CONTINUE;
        }
        if (ai_lib.isAiDead(target))
        {
            sendSystemMessage(self, new string_id(STF_FILE, "droid_disabled_detonate"));
            return SCRIPT_CONTINUE;
        }
        int bomb_level = getIntObjVar(target, "module_data.bomb_level");
        if (bomb_level < 1)
        {
            sendSystemMessage(self, new string_id(STF_FILE, "no_bomb_module"));
            return SCRIPT_CONTINUE;
        }
        if (!hasSkill(self, "combat_smuggler_novice") && !hasSkill(self, "combat_bountyhunter_novice"))
        {
            sendSystemMessage(self, new string_id(STF_FILE, "insufficient_skill_detonate"));
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(target, "droid_module.countdown"))
        {
            sendSystemMessage(self, new string_id(STF_FILE, "countdown_already_started"));
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(self, "droid.bomb_droid_active") && utils.getIntScriptVar(self, "droid.bomb_droid_active") > getGameTime())
        {
            sendSystemMessage(self, new string_id(STF_FILE, "countdown_already_started"));
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(target, "module_data.detonation_warmup"))
        {
            sendSystemMessage(self, new string_id(STF_FILE, "detonation_warmup"));
            return SCRIPT_CONTINUE;
        }
        utils.setScriptVar(target, "droid_module.countdown", 1);
        dictionary d = new dictionary();
        d.put("master", self);
        d.put("count", 3);
        messageTo(target, "msgDetonationCountdown", d, 1.0f, false);
        sendSystemMessage(self, new string_id(STF_FILE, "countdown_started"));
        utils.setScriptVar(self, "droid.bomb_droid_active", (getGameTime() + 10));
        return SCRIPT_CONTINUE;
    }
    public int clearBombDroidTimer(obj_id self, dictionary params) throws InterruptedException
    {
        if (utils.hasScriptVar(self, "droid.bomb_droid_active"))
        {
            utils.removeScriptVar(self, "droid.bomb_droid_active");
        }
        return SCRIPT_CONTINUE;
    }
    public boolean hasPrereq(obj_id pet, int ability) throws InterruptedException
    {
        if (!isIdValid(pet) || !exists(pet))
        {
            return false;
        }
        obj_id pcd = callable.getCallableCD(pet);
        int[] abilityList = getIntArrayObjVar(pcd, "ai.petAbility.abilityList");
        if (abilityList == null || abilityList.length == 0)
        {
            return false;
        }
        return (hasPrereq(abilityList, ability));
    }
    public boolean hasPrereq(int[] abilityList, int ability) throws InterruptedException
    {
        int row = dataTableSearchColumnForInt(ability, "abilityCrc", pet_lib.PET_ABILITY_TABLE);
        if (row == -1)
        {
            return false;
        }
        dictionary abilityData = dataTableGetRow(pet_lib.PET_ABILITY_TABLE, row);
        String prereq = abilityData.getString("prereq");
        if (prereq == null || prereq.equals(""))
        {
            return true;
        }
        int prereqCrc = getStringCrc(prereq.toLowerCase());
        if (utils.getElementPositionInArray(abilityList, prereqCrc) != -1)
        {
            return true;
        }
        return false;
    }
    public int[] buildPrereqList(int ability) throws InterruptedException
    {
        Vector prereqList = new Vector();
        prereqList.setSize(0);
        int row = dataTableSearchColumnForInt(ability, "abilityCrc", pet_lib.PET_ABILITY_TABLE);
        if (row == -1)
        {
            return null;
        }
        dictionary abilityData = dataTableGetRow(pet_lib.PET_ABILITY_TABLE, row);
        String prereq = abilityData.getString("prereq");
        if (prereq == null || prereq.equals(""))
        {
            return null;
        }
        int prereqCrc = getStringCrc(prereq.toLowerCase());
        while (prereqCrc != 0)
        {
            row = dataTableSearchColumnForInt(prereqCrc, "abilityCrc", pet_lib.PET_ABILITY_TABLE);
            if (row == -1)
            {
                break;
            }
            prereqList = utils.addElement(prereqList, prereqCrc);
            abilityData = dataTableGetRow(pet_lib.PET_ABILITY_TABLE, row);
            prereq = abilityData.getString("prereq");
            if (prereq == null || prereq.equals(""))
            {
                break;
            }
            prereqCrc = getStringCrc(prereq.toLowerCase());
        }
        int[] _prereqList = new int[0];
        if (prereqList != null)
        {
            _prereqList = new int[prereqList.size()];
            for (int _i = 0; _i < prereqList.size(); ++_i)
            {
                _prereqList[_i] = (Integer) prereqList.get(_i);
            }
        }
        return _prereqList;
    }
    public int droid_follow(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_FOLLOW))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_follow_other(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_FOLLOW_OTHER))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_stay(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_STAY))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_guard(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_GUARD))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_friend(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_FRIEND))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_attack(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_ATTACK))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_patrol(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_PATROL))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_patrol_point(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_SET_PATROL_POINT))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_patrol_clear(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_CLEAR_PATROL_POINTS))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_store(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_RELEASE))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_transfer(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_TRANSFER))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_group(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_GROUP))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_trick_1(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_TRICK_1))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_trick_2(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_TRICK_2))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_trick_3(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_TRICK_3))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
    public int droid_trick_4(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!pet_lib.doCommandNum(self, pet_lib.COMMAND_TRICK_4))
        {
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }
}
