package script.systems.combat;

import script.library.*;
import script.obj_id;
import script.string_id;

public class combat_player extends script.systems.combat.combat_base
{
    public combat_player()
    {
    }
    public int peace(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        queueClear(self);
        stopCombat(self);
        return SCRIPT_CONTINUE;
    }
    public int OnExitedCombat(obj_id self) throws InterruptedException
    {
        combat.clearCombatDebuffs(self);
        setState(self, STATE_PEACE, false);
        obj_id objMount = getMountId(self);
        if (isIdValid(objMount))
        {
            obj_id objControlDevice = callable.getCallableCD(objMount);
            if (!pet_lib.isGalloping(objMount))
            {
                pet_lib.setMountedMovementRate(self, objMount);
            }
            else 
            {
                pet_lib.setUnmountedMovementRate(self, objMount);
            }
        }
        utils.setScriptVar(self, pet_lib.COMBAT_ENDED, getGameTime());
        return SCRIPT_CONTINUE;
    }
    public int OnEnteredCombat(obj_id self) throws InterruptedException
    {
        if (utils.hasScriptVar(self, "holoMessageTime"))
        {
            performance.holographicCleanup(self);
        }
        if (hasScript(self, performance.DANCE_HEARTBEAT_SCRIPT) || (hasScript(self, performance.MUSIC_HEARTBEAT_SCRIPT)))
        {
            performance.stopDance(self);
            performance.stopPlaying(self);
            performance.stopEntertainingPlayer(self);
        }
        if (getState(self, STATE_GLOWING_JEDI) > 0)
        {
            setState(self, STATE_GLOWING_JEDI, false);
        }
        combat.doCombatDebuffs(self);
        if (vehicle.isRidingVehicle(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id objL = getObjectInSlot(self, "hold_l");
        obj_id objR = getObjectInSlot(self, "hold_r");
        if (isIdValid(objL) && (getGameObjectType(objL) == GOT_misc_instrument))
        {
            obj_id inv = utils.getInventoryContainer(self);
            putInOverloaded(objL, inv);
        }
        if (isIdValid(objR) && (getGameObjectType(objR) == GOT_misc_instrument))
        {
            obj_id inv = utils.getInventoryContainer(self);
            putInOverloaded(objR, inv);
        }
        obj_id objMount = getMountId(self);
        if (isIdValid(objMount))
        {
            if (!pet_lib.isGalloping(objMount))
            {
                pet_lib.setMountCombatMoveSpeed(self, objMount);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int combatModeCheck(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int OnDefenderCombatAction(obj_id self, obj_id objAttacker, obj_id objWeapon, int intCombatResult) throws InterruptedException
    {
        if (vehicle.isRidingVehicle(self))
        {
            return SCRIPT_OVERRIDE;
        }
        if (getPosture(self) == POSTURE_SITTING)
        {
            queueCommand(self, (-1465754503), null, "", COMMAND_PRIORITY_FRONT);
            return SCRIPT_CONTINUE;
        }
        return SCRIPT_CONTINUE;
    }
    public int OnAboutToReceiveItem(obj_id self, obj_id objContainer, obj_id objTransferer, obj_id objItem) throws InterruptedException
    {
        if (isWeapon(objItem))
        {
            if (jedi.isLightsaber(getWeaponType(objItem)))
            {
                if (!utils.isProfession(self, utils.FORCE_SENSITIVE))
                {
                    string_id strSpam = new string_id("jedi_spam", "no_equip_lightsaber");
                    sendSystemMessage(self, strSpam);
                    return SCRIPT_OVERRIDE;
                }
                else 
                {
                    if (!jedi.hasColorCrystal(objItem))
                    {
                        string_id strSpam = new string_id("jedi_spam", "lightsaber_no_color");
                        sendSystemMessage(self, strSpam);
                        return SCRIPT_OVERRIDE;
                    }
                }
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnReceivedItem(obj_id self, obj_id objSrcContainer, obj_id objTransferer, obj_id objItem) throws InterruptedException
    {
        if (isWeapon(objItem))
        {
            if (!combat.hasCertification(self, objItem))
            {
                string_id strSpam = new string_id("combat_effects", "no_proficiency");
                sendSystemMessage(objTransferer, strSpam);
            }
            if (utils.isEquipped(objItem) && jedi.isLightsaber(objItem))
            {
                jedi.jediActionPerformed(self, jedi.SABER_EQUIP_VISIBILITY, jedi.VISIBILITY_WITNESS_RANGE);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int OnDeath(obj_id self, obj_id killer, obj_id corpseId) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int target(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        if (isIdValid(target))
        {
            setLookAtTarget(self, target);
        }
        return SCRIPT_CONTINUE;
    }
}
