package script.player.skill;

import script.dictionary;
import script.library.*;
import script.location;
import script.obj_id;
import script.string_id;

public class taming extends script.base_script
{
    public taming()
    {
    }
    public static final string_id SHAPECHANGE = new string_id("spam", "not_while_shapechanged");
    public static final string_id TAME_SUCCESS = new string_id("hireling/hireling", "taming_success");
    public static final string_id TAME_FAILED = new string_id("hireling/hireling", "taming_fail");
    public static final string_id TAME_TOO_FAR = new string_id("hireling/hireling", "taming_toofar");
    public static final float TAME_RANGE = 8.0f;
    public static final float TAME_PHASE_DELAY = 10.0f;
    public static final float TAME_HOLD_DELAY = 1.0f;
    public static final String TAME_ROOT = "pet.precuTame";
    public static final String TAME_TARGET_LOCK = "pet.precuTamePlayer";
    public static final String TAME_SCRIPT = "player.skill.taming_task";
    private static final long PRECU_TAME_PLAYER_OID = 44003778L;
    private static final int PRECU_TAME_STATION_ID = 91001;
    private static final int PRECU_TAME_PROTOCOL_VERSION = 1;
    private static final String PRECU_TAME_FIXTURE_ROOT =
        "precu.tameCommandFixture";
    public int cmdTellPet(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!callable.hasAnyCallable(self) || params == null || params.length() > 30)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id[] callableList = callable.getCallables(self);
        for (obj_id obj_id : callableList) {
            if (getDistance(obj_id, self) < 200.0f && !ai_lib.aiIsDead(obj_id) && !beast_lib.isBeast(obj_id)) {
                dictionary parms = new dictionary();
                parms.put("text", params);
                parms.put("master", self);
                messageTo(obj_id, "handleTellPet", parms, 0, false);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int cmdTame(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (isPrecuTameFixture(self, target))
        {
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".handlerEntered", 1);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".handlerCalls",
                readPrecuTameInt(self, ".handlerCalls") + 1);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".startedAt", getGameTime());
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome", "entered");
        }
        if (!canBeginTame(self, target, true))
        {
            if (isPrecuTameFixture(self, target))
            {
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome",
                    "admissionFailed");
            }
            return SCRIPT_CONTINUE;
        }
        int originalBehavior = getBehavior(target);
        if (originalBehavior > ai_lib.BEHAVIOR_CALM)
        {
            originalBehavior = ai_lib.BEHAVIOR_LOITER;
        }
        utils.setScriptVar(self, TAME_ROOT + ".target", target);
        utils.setScriptVar(self, TAME_ROOT + ".phase", 1);
        utils.setScriptVar(self, TAME_ROOT + ".originalBehavior", originalBehavior);
        utils.setScriptVar(target, TAME_TARGET_LOCK, self);
        if (!hasScript(self, TAME_SCRIPT))
        {
            attachScript(self, TAME_SCRIPT);
            if (!hasScript(self, TAME_SCRIPT))
            {
                clearTameState(self, target, true);
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
                if (isPrecuTameFixture(self, target))
                {
                    setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome",
                        "handlerAttachFailed");
                    setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                        getGameTime());
                }
                return SCRIPT_CONTINUE;
            }
            utils.setScriptVar(self, TAME_ROOT + ".detachHandler", 1);
        }
        stop(target);
        messageTo(self, "handlePrecuTameHold", null, TAME_HOLD_DELAY, false);
        chat.chat(self, new string_id("hireling/hireling", "taming_" + rand(1, 4)));
        if (isPrecuTameFixture(self, target))
        {
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome", "started");
        }
        messageTo(self, "handlePrecuTamePhase", null, TAME_PHASE_DELAY, false);
        return SCRIPT_CONTINUE;
    }
    public int handlePrecuTameHold(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!utils.hasScriptVar(self, TAME_ROOT + ".target"))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id target = utils.getObjIdScriptVar(self, TAME_ROOT + ".target");
        if (!isIdValid(target) || !exists(target) ||
            !utils.hasScriptVar(target, TAME_TARGET_LOCK) ||
            !self.equals(utils.getObjIdScriptVar(target, TAME_TARGET_LOCK)))
        {
            return SCRIPT_CONTINUE;
        }
        stop(target);
        messageTo(self, "handlePrecuTameHold", null, TAME_HOLD_DELAY, false);
        return SCRIPT_CONTINUE;
    }
    public int handlePrecuTamePhase(obj_id self, dictionary params) throws InterruptedException
    {
        if (!utils.hasScriptVar(self, TAME_ROOT + ".target"))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id target = utils.getObjIdScriptVar(self, TAME_ROOT + ".target");
        if (isPrecuTameFixture(self, target))
        {
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".phaseCallbacks",
                readPrecuTameInt(self, ".phaseCallbacks") + 1);
        }
        if (!canContinueTame(self, target))
        {
            if (isIdValid(target) && exists(target) && getDistance(self, target) > TAME_RANGE)
            {
                sendSystemMessage(self, TAME_TOO_FAR);
                showFlyText(target, new string_id("npc_reaction/flytext", "toofar"), 1.0f, 204, 0, 0);
            }
            else
            {
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
            }
            clearTameState(self, target, true);
            if (isPrecuTameFixture(self, target))
            {
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome",
                    "interrupted");
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                    getGameTime());
            }
            return SCRIPT_CONTINUE;
        }
        int phase = utils.getIntScriptVar(self, TAME_ROOT + ".phase");
        if (phase < 3)
        {
            chat.chat(self, new string_id("hireling/hireling", "taming_" + rand(1, 4)));
            utils.setScriptVar(self, TAME_ROOT + ".phase", phase + 1);
            messageTo(self, "handlePrecuTamePhase", null, TAME_PHASE_DELAY, false);
            return SCRIPT_CONTINUE;
        }
        if (!canCommitTame(self, target))
        {
            clearTameState(self, target, true);
            if (isPrecuTameFixture(self, target))
            {
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome",
                    "commitRejected");
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                    getGameTime());
            }
            return SCRIPT_CONTINUE;
        }
        int chance = pet_lib.getChanceToTame(target, self);
        int roll = getTameRoll(self, target);
        if (isPrecuTameFixture(self, target))
        {
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".chance", chance);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".roll", roll);
        }
        if (roll >= chance)
        {
            sendSystemMessage(self, TAME_FAILED);
            showFlyText(target, new string_id("npc_reaction/flytext", "fail"), 1.0f, 204, 0, 0);
            int petType = pet_lib.getPetType(target);
            clearTameState(self, target, true);
            if (petType == pet_lib.PET_TYPE_AGGRO && rand(1, 20) == 1)
            {
                startCombat(target, self);
            }
            if (isPrecuTameFixture(self, target))
            {
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome", "failed");
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                    getGameTime());
            }
            return SCRIPT_CONTINUE;
        }
        int sourceLevel = getLevel(target);
        int originalBehavior = utils.getIntScriptVar(
            self, TAME_ROOT + ".originalBehavior");
        clearTameState(self, target, false);
        obj_id petControlDevice = pet_lib.makeTamedCreature(self, target, 1);
        if (!isIdValid(petControlDevice) || !exists(petControlDevice))
        {
            sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
            restoreWildCreature(target, originalBehavior);
            if (isPrecuTameFixture(self, target))
            {
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome",
                    "materializationFailed");
                setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                    getGameTime());
            }
            return SCRIPT_CONTINUE;
        }
        int xpAmount = sourceLevel * 20;
        if (xpAmount > 0)
        {
            xp.grant(self, xp.CREATUREHANDLER, xpAmount);
        }
        if (isPrecuTameFixture(self, target))
        {
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".pcd", petControlDevice);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".pet", target);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".sourceLevel", sourceLevel);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".xpGranted", xpAmount);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".petPersisted",
                isObjectPersisted(target) ? 1 : 0);
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".outcome", "passed");
            setObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".completedAt",
                getGameTime());
        }
        sendSystemMessage(self, TAME_SUCCESS);
        showFlyText(target, new string_id("npc_reaction/flytext", "success"), 1.0f, 0, 204, 0);
        return SCRIPT_CONTINUE;
    }
    private boolean canBeginTame(obj_id self, obj_id target, boolean sendFailure) throws InterruptedException
    {
        if (!isIdValid(self) || !exists(self) || !isPlayer(self) ||
            !isIdValid(target) || !exists(target) || !isMob(target) || isPlayer(target) ||
            isDead(self) || isIncapacitated(self) || ai_lib.isAiDead(target) ||
            ai_lib.isInCombat(self) || ai_lib.isInCombat(target) ||
            getDistance(self, target) > TAME_RANGE || pet_lib.hasMaster(target) ||
            !ai_lib.isMonster(target) || !hasScript(target, "ai.pet_advance") ||
            !pet_lib.isTameable(target) ||
            utils.hasScriptVar(self, TAME_ROOT) ||
            utils.hasScriptVar(target, TAME_TARGET_LOCK))
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
            }
            return false;
        }
        if (!hasSkill(self, "outdoors_creaturehandler_novice") ||
            pet_lib.getChanceToTame(target, self) <= 0)
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_LACK_SKILL);
            }
            return false;
        }
        int petType = pet_lib.getPetType(target);
        if (petType != pet_lib.PET_TYPE_NON_AGGRO && petType != pet_lib.PET_TYPE_AGGRO)
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
            }
            return false;
        }
        if (pet_lib.hasMaxPets(self, petType))
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_TOO_MANY_PETS);
            }
            return false;
        }
        if (pet_lib.hasMaxStoredPetsOfType(self, petType))
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_TOO_MANY_STORED_PETS);
            }
            return false;
        }
        if (!pet_lib.canControlPetsOfLevel(
            self, petType, getLevel(target), pet_lib.getCreatureName(target)))
        {
            return false;
        }
        obj_id datapad = utils.getPlayerDatapad(self);
        if (!isIdValid(datapad) || !exists(datapad))
        {
            if (sendFailure)
            {
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_TAME);
            }
            return false;
        }
        return true;
    }
    private boolean canContinueTame(obj_id self, obj_id target) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) ||
            !utils.hasScriptVar(target, TAME_TARGET_LOCK) ||
            !self.equals(utils.getObjIdScriptVar(target, TAME_TARGET_LOCK)))
        {
            return false;
        }
        return isIdValid(self) && exists(self) && !isDead(self) && !isIncapacitated(self) &&
            !ai_lib.isAiDead(target) && !ai_lib.isInCombat(self) && !ai_lib.isInCombat(target) &&
            getDistance(self, target) <= TAME_RANGE && !pet_lib.hasMaster(target) &&
            ai_lib.isMonster(target) && hasScript(target, "ai.pet_advance") &&
            pet_lib.isTameable(target);
    }
    private boolean canCommitTame(obj_id self, obj_id target) throws InterruptedException
    {
        if (!hasSkill(self, "outdoors_creaturehandler_novice") ||
            pet_lib.getChanceToTame(target, self) <= 0)
        {
            sendSystemMessage(self, pet_lib.SID_SYS_LACK_SKILL);
            return false;
        }
        int petType = pet_lib.getPetType(target);
        if ((petType != pet_lib.PET_TYPE_NON_AGGRO &&
            petType != pet_lib.PET_TYPE_AGGRO) ||
            pet_lib.hasMaxPets(self, petType) ||
            pet_lib.hasMaxStoredPetsOfType(self, petType) ||
            !pet_lib.canControlPetsOfLevel(
                self, petType, getLevel(target), pet_lib.getCreatureName(target)))
        {
            return false;
        }
        obj_id datapad = utils.getPlayerDatapad(self);
        return isIdValid(datapad) && exists(datapad);
    }
    private int getTameRoll(obj_id self, obj_id target) throws InterruptedException
    {
        return isPrecuTameFixture(self, target) ? 0 : rand(0, 100);
    }
    private boolean isPrecuTameFixture(obj_id self, obj_id target)
        throws InterruptedException
    {
        if (!isIdValid(self) || !exists(self) || !isPlayer(self) ||
            self.getValue() != PRECU_TAME_PLAYER_OID ||
            getPlayerStationId(self) != PRECU_TAME_STATION_ID ||
            !hasObjVar(self, PRECU_TAME_FIXTURE_ROOT) ||
            !hasObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".protocol") ||
            getIntObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".protocol") !=
                PRECU_TAME_PROTOCOL_VERSION ||
            !hasObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".prepared") ||
            getIntObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".prepared") != 1 ||
            !hasObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".target"))
        {
            return false;
        }
        return isIdValid(target) && target.equals(
            getObjIdObjVar(self, PRECU_TAME_FIXTURE_ROOT + ".target"));
    }
    private int readPrecuTameInt(obj_id self, String suffix)
        throws InterruptedException
    {
        String path = PRECU_TAME_FIXTURE_ROOT + suffix;
        return hasObjVar(self, path) ? getIntObjVar(self, path) : 0;
    }
    private void clearTameState(obj_id self, obj_id target, boolean restoreCreature) throws InterruptedException
    {
        int originalBehavior = ai_lib.BEHAVIOR_LOITER;
        boolean detachHandler = false;
        if (utils.hasScriptVar(self, TAME_ROOT + ".originalBehavior"))
        {
            originalBehavior = utils.getIntScriptVar(self, TAME_ROOT + ".originalBehavior");
        }
        if (utils.hasScriptVar(self, TAME_ROOT + ".detachHandler"))
        {
            detachHandler = utils.getIntScriptVar(
                self, TAME_ROOT + ".detachHandler") == 1;
        }
        utils.removeScriptVar(self, TAME_ROOT);
        if (isIdValid(target) && exists(target) &&
            utils.hasScriptVar(target, TAME_TARGET_LOCK) &&
            self.equals(utils.getObjIdScriptVar(target, TAME_TARGET_LOCK)))
        {
            utils.removeScriptVar(target, TAME_TARGET_LOCK);
        }
        if (restoreCreature)
        {
            restoreWildCreature(target, originalBehavior);
        }
        if (detachHandler && hasScript(self, TAME_SCRIPT))
        {
            detachScript(self, TAME_SCRIPT);
        }
    }
    private void restoreWildCreature(obj_id target, int originalBehavior) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) || pet_lib.hasMaster(target))
        {
            return;
        }
        if (originalBehavior > ai_lib.BEHAVIOR_CALM)
        {
            originalBehavior = ai_lib.BEHAVIOR_LOITER;
        }
        ai_lib.setDefaultCalmBehavior(target, originalBehavior);
    }
    public int tellPetAttack(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (callable.hasAnyCallable(self))
        {
            obj_id[] callableList = callable.getCallables(self);
            if (callableList.length > 0 && callableList != null)
            {
                for (obj_id obj_id : callableList) {
                    if (getDistance(obj_id, self) < 200.0f && !ai_lib.aiIsDead(obj_id) && !beast_lib.isBeast(obj_id)) {
                        pet_lib.doCommandNum(obj_id, pet_lib.COMMAND_ATTACK, self);
                    }
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int tellPetFollow(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (callable.hasAnyCallable(self))
        {
            obj_id[] callableList = callable.getCallables(self);
            if (callableList.length > 0 && callableList != null)
            {
                for (obj_id obj_id : callableList) {
                    if (getDistance(obj_id, self) < 200.0f && !ai_lib.aiIsDead(obj_id) && !beast_lib.isBeast(obj_id)) {
                        pet_lib.doCommandNum(obj_id, pet_lib.COMMAND_FOLLOW, self);
                    }
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int mount(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        LOG("mount", "mount(): enter self: " + self + " target: " + target);
        debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ just entered commandHandler mount()");
        location here = getLocation(self);
        location term = getLocation(target);
        float dist = getDistance(here, term);
        if (!isIdValid(self) || !isIdValid(target))
        {
            debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ either isIdValid(self) or isIdValid(target) failed");
            LOG("mount", "mount(): player or mount id is not valid");
            return SCRIPT_CONTINUE;
        }
        if (buff.hasBuff(self, "instance_exiting"))
        {
            return SCRIPT_CONTINUE;
        }
        if (!pet_lib.isPet(target))
        {
            return SCRIPT_CONTINUE;
        }
        if (isDead(self) || isIncapacitated(self) || dist > 5.0)
        {
            return SCRIPT_CONTINUE;
        }
        int shapechange = buff.getBuffOnTargetFromGroup(self, "shapechange");
        obj_id playerCurrentMount = getMountId(self);
        if (isIdValid(playerCurrentMount) && exists(playerCurrentMount))
        {
            String mountName = getTemplateName(playerCurrentMount);
            if (vehicle.isJetPackVehicle(playerCurrentMount))
            {
                if (shapechange != 0)
                {
                    utils.dismountRiderJetpackCheck(self);
                    return SCRIPT_CONTINUE;
                }
            }
        }
        if (shapechange != 0)
        {
            sendSystemMessage(self, SHAPECHANGE);
            return SCRIPT_CONTINUE;
        }
        if (isIdValid(playerCurrentMount))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isMob(target))
        {
            return SCRIPT_CONTINUE;
        }
        if (self != getMaster(target))
        {
            if (!doesMountHaveRoom(target))
            {
                return SCRIPT_CONTINUE;
            }
            else if (!vehicle.mountPermissionCheck(target, self, true))
            {
                return SCRIPT_CONTINUE;
            }
        }
        if (pet_lib.isInRestrictedScene(self))
        {
            sendSystemMessage(self, pet_lib.SID_SYS_MOUNT_RESTRICTED_SCENE);
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(target, "vehicularTestBed"))
        {
            if (!hasScript(target, "ai.pet"))
            {
                debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ ai.pet isn't present on the target pet");
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_MOUNT);
                return SCRIPT_CONTINUE;
            }
        }
        if (getMountsEnabled() && pet_lib.canMount(target, self))
        {
            debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ pet_lib.canMount(target, self) returned TRUE");
            if (vehicle.isVehicle(target) && isDisabled(target))
            {
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_MOUNT_VEHICLE_DISABLED);
                debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ disabled vehicle");
                return SCRIPT_CONTINUE;
            }
            queueClear(self);
            if (!mountCreature(self, target))
            {
                debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ mountCreature (self,target) returned FALSE");
                sendSystemMessage(self, pet_lib.SID_SYS_CANT_MOUNT);
            }
            else 
            {
                pet_lib.setMountedMovementRate(self, target);
                debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ mountCreature (self,target) returned TRUE");
                messageTo(self, "applyMountBuff", null, 1, false);
                if (!hasObjVar(target, "vehicularTestBed"))
                {
                    if (!hasScript(target, "ai.mount_combat"))
                    {
                        debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ mounted pet doesn't have ai.mount_combat script. adding it now");
                        attachScript(target, "ai.mount_combat");
                    }
                }
            }
        }
        else 
        {
            debugServerConsoleMsg(self, "+++ PLAYER.skill.taming +++ commandHandler mount() +++ pet_lib.canMount(target, self) returned FALSE");
        }
        return SCRIPT_CONTINUE;
    }
    public int battlefieldMount(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        obj_id mount = target;
        if (!isIdValid(self) || !isIdValid(mount))
        {
            return SCRIPT_CONTINUE;
        }
        if (buff.hasBuff(self, "instance_exiting"))
        {
            return SCRIPT_CONTINUE;
        }
        int shapechange = buff.getBuffOnTargetFromGroup(self, "shapechange");
        if (shapechange != 0)
        {
            sendSystemMessage(self, SHAPECHANGE);
            return SCRIPT_CONTINUE;
        }
        obj_id playerCurrentMount = getMountId(self);
        if (isIdValid(playerCurrentMount))
        {
            return SCRIPT_CONTINUE;
        }
        if (!doesMountHaveRoom(mount))
        {
            return SCRIPT_CONTINUE;
        }
        queueClear(self);
        if (!mountCreature(self, mount))
        {
            debugSpeakMsg(self, "Epic fail");
        }
        else 
        {
            obj_id pilot = getRiderId(mount);
            if (!isIdValid(getMaster(mount)) || (pilot != getMaster(mount) && pilot == getObjectInSlot(mount, "rider")))
            {
                setMaster(mount, pilot);
            }
            pet_lib.setMountedMovementRate(self, mount);
            setState(self, STATE_RIDING_MOUNT, true);
            messageTo(self, "applyMountBuff", null, 1, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int dismount(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        debugServerConsoleMsg(null, "+++ player.skill.taming.commandHandler dismount +++ just entered the dismount command handler");
        obj_id playerCurrentMount = getMountId(self);
        if (!isIdValid(playerCurrentMount))
        {
            return SCRIPT_CONTINUE;
        }
        if (vehicle.isBattlefieldVehicle(playerCurrentMount))
        {
            return SCRIPT_CONTINUE;
        }
        boolean dismountSuccess = pet_lib.doDismountNow(self);
        if (!dismountSuccess)
        {
            LOG("mounts-bug", "taming.script.commandHandler dismount(): pet_lib.doDismountNow() failed for rider [" + self + "]");
        }
        int vehicleBuff = buff.getBuffOnTargetFromGroup(self, "vehicle");
        if (vehicleBuff != 0)
        {
            buff.removeBuff(self, vehicleBuff);
        }
        String mountName = getTemplateName(playerCurrentMount);
        if (vehicle.isJetPackVehicle(playerCurrentMount))
        {
            string_id jetDismount = new string_id("pet/pet_menu", "jetpack_dismount");
            sendSystemMessage(self, jetDismount);
            obj_id petControlDevice = callable.getCallableCD(playerCurrentMount);
            vehicle.storeVehicle(petControlDevice, self);
        }
        return SCRIPT_CONTINUE;
    }
    public int battlefieldDismount(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        obj_id mount = getMountId(self);
        vehicle.setHoverHeight(mount, 0.5f);
        dismountCreature(self);
        setState(self, STATE_RIDING_MOUNT, false);
        obj_id master = getMaster(mount);
        if (isIdValid(master) && master == self)
        {
            obj_id[] passengerList = utils.getAllRidersInVehicle(self, mount);
            if (passengerList != null && passengerList.length > 0)
            {
                for (obj_id obj_id : passengerList) {
                    if (isIdValid(obj_id)) {
                        dismountCreature(obj_id);
                    }
                }
            }
            setMaster(mount, null);
        }
        else 
        {
            if (isIdValid(master))
            {
                buff.removeBuff(mount, "vehicle_passenger");
            }
        }
        int vehicleBuff = buff.getBuffOnTargetFromGroup(self, "vehicle");
        if (vehicleBuff != 0)
        {
            buff.removeBuff(self, vehicleBuff);
        }
        int vehicleHasBuff = buff.getBuffOnTargetFromGroup(mount, "vehicle");
        if (vehicleHasBuff != 0)
        {
            buff.removeBuff(mount, vehicleHasBuff);
        }
        return SCRIPT_CONTINUE;
    }
    public int dismountandstore(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (!isIdValid(self) || !getMountsEnabled())
        {
            return SCRIPT_CONTINUE;
        }
        boolean dismountResult = pet_lib.doDismountNow(self);
        if (!dismountResult)
        {
            LOG("mounts-bug", "dismountandstore called on rider [" + self + "] but pet_lib.doDismountNow() returned false; aborting store but essential state is already destroyed.");
            sendSystemMessage(self, pet_lib.SID_SYS_CANT_DISMOUNT);
            return SCRIPT_CONTINUE;
        }
        obj_id playerCurrentMount = getMountId(self);
        if (isIdValid(playerCurrentMount))
        {
            obj_id petControlDevice = callable.getCallableCD(playerCurrentMount);
            setObjVar(petControlDevice, "pet.timeStored", getGameTime());
            destroyObject(playerCurrentMount);
        }
        else 
        {
            sendSystemMessage(self, pet_lib.SID_SYS_CANT_DISMOUNT);
        }
        return SCRIPT_CONTINUE;
    }
}
