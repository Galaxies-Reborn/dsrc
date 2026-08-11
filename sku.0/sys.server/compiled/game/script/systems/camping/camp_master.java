package script.systems.camping;

import script.*;
import script.library.*;

public class camp_master extends script.base_script
{
    public camp_master()
    {
    }
    public static final String VAR_CAMP_CAMPFIRE = "campfire";
    public static final String TEMPLATE_LOGS_FRESH = "object/static/structure/general/campfire_fresh.iff";
    public static final String TEMPLATE_LOGS_BURNT = "object/static/structure/general/campfire_burnt.iff";
    public static final String TEMPLATE_LOGS_SMOLDERING = "object/static/structure/general/campfire_smoldering.iff";
    public static final String TEMPLATE_LOGS_ASH = "object/static/structure/general/campfire_ash.iff";
    public static final string_id SID_SYS_COMBAT_ABANDONED = new string_id("camp", "sys_combat_abandoned");
    public static final string_id SID_SYS_ABANDONED_CAMP = new string_id("camp", "sys_abandoned_camp");
    public static final string_id SID_SYS_CAMP_HEAL = new string_id("camp", "sys_camp_heal");
    public int OnAttach(obj_id self) throws InterruptedException
    {
        setObjVar(self, camping.VAR_BEEN_INITIALIZED, true);
        createTriggerVolume("camp_" + self, camping.getCampSize(self), true);
        camping.setCampMasterName(self);
        obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
        setObjVar(self, camping.VAR_OWNER_NEAR, true);
        camping.registerCampVisitor(self, owner);
        messageTo(
            self,
            camping.HANDLER_CAMP_NATURAL_EXPIRY,
            null,
            camping.CAMP_NATURAL_EXPIRY,
            false);
        return SCRIPT_CONTINUE;
    }
    public int OnDestroy(obj_id self) throws InterruptedException
    {
        obj_id item = getObjIdObjVar(self, VAR_CAMP_CAMPFIRE);
        if ((item == null) || (item == obj_id.NULL_ID))
        {
        }
        else 
        {
            destroyObject(item);
        }
        return SCRIPT_CONTINUE;
    }
    public int theaterFinished(obj_id self, dictionary params) throws InterruptedException
    {
        notifyChildren(self, "handleCampPrep");
        return SCRIPT_CONTINUE;
    }
    public void notifyChildren(obj_id self, String msg) throws InterruptedException
    {
        dictionary outparams = new dictionary();
        outparams.put("master", self);
        obj_id[] children = getObjIdArrayObjVar(self, theater.VAR_CHILDREN);
        if ((children == null) || (children.length == 0))
        {
            return;
        }
        else 
        {
            int j = 0;
            for (obj_id child : children) {
                if ((child == null) || (child == obj_id.NULL_ID)) {
                } else {
                    messageTo(child, msg, outparams, 0, false);
                }
            }
        }
    }
    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (hasObjVar(self, camping.VAR_BEEN_INITIALIZED))
        {
            camping.nukeCamp(self);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnUnloadedFromMemory(obj_id self) throws InterruptedException
    {
        camping.nukeCamp(self);
        return SCRIPT_CONTINUE;
    }
    public int OnTriggerVolumeEntered(obj_id self, String volName, obj_id who) throws InterruptedException
    {
        if (who == self || !isPlayer(who))
        {
            return SCRIPT_CONTINUE;
        }
        if (volName.equals("camp_" + self))
        {
            obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
            if ((owner == null) || (owner == obj_id.NULL_ID))
            {
                sendSystemMessage(who, camping.SID_CAMP_ENTER);
                sendSystemMessage(who, SID_SYS_CAMP_HEAL);
            }
            else 
            {
                addCampMember(self, owner, who);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public void addCampMember(obj_id self, obj_id owner, obj_id who) throws InterruptedException
    {
        if (who == owner)
        {
            setObjVar(self, camping.VAR_OWNER_NEAR, true);
            camping.cancelCampOwnerAbsence(self);
        }
        camping.setCurrentCamp(who, self);
        prose_package pp = prose.getPackage(camping.PROSE_CAMP_ENTER, getName(self));
        sendSystemMessageProse(who, pp);
        sendSystemMessage(who, SID_SYS_CAMP_HEAL);
        camping.registerCampVisitor(self, who);
        int occ = getIntObjVar(self, "occ_count");
        occ++;
        setObjVar(self, "occ_count", occ);
    }
    public int OnTriggerVolumeExited(obj_id self, String volName, obj_id who) throws InterruptedException
    {
        if (who == self || !isPlayer(who))
        {
            return SCRIPT_CONTINUE;
        }
        if (volName.equals("camp_" + self))
        {
            obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
            if ((owner == null) || (owner == obj_id.NULL_ID))
            {
                sendSystemMessage(who, camping.SID_CAMP_EXIT);
            }
            else 
            {
                if (who == owner)
                {
                    setObjVar(self, camping.VAR_OWNER_NEAR, false);
                    camping.beginCampOwnerAbsence(self);
                }
                camping.clearCurrentCamp(who);
                prose_package pp = prose.getPackage(camping.PROSE_CAMP_EXIT, getName(self));
                sendSystemMessageProse(who, pp);
            }
            int occ = getIntObjVar(self, "occ_count");
            occ = Math.max(0, occ - 1);
            setObjVar(self, "occ_count", occ);
        }
        return SCRIPT_CONTINUE;
    }
    public int handleCampHealingReceived(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || !params.containsKey("player") ||
            camping.getStatus(self) != camping.STATUS_MAINTAIN ||
            !isIdValid(camping.getCampOwner(self)))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        if (!isIdValid(player) || !isPlayer(player) ||
            camping.getCurrentCamp(player) != self ||
            !isInTriggerVolume(self, "camp_" + self, player))
        {
            return SCRIPT_CONTINUE;
        }
        camping.recordCampHealingEvent(self);
        return SCRIPT_CONTINUE;
    }
    public int handleCampNaturalExpiry(obj_id self, dictionary params) throws InterruptedException
    {
        if (camping.getStatus(self) == camping.STATUS_MAINTAIN)
        {
            obj_id owner = camping.getCampOwner(self);
            if (!isIdValid(owner) || !owner.isLoaded() ||
                !exists(owner) || !isInWorld(owner))
            {
                camping.nukeCamp(self);
            }
            else if (!camping.awardCampExperienceAndNuke(self))
            {
                messageTo(
                    self,
                    camping.HANDLER_CAMP_NATURAL_EXPIRY,
                    null,
                    5.0f,
                    false);
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int handleCampOwnerEnteredCombat(obj_id self, dictionary params) throws InterruptedException
    {
        int status = camping.getStatus(self);
        if (params == null || !params.containsKey("owner") ||
            status < camping.STATUS_NEW ||
            status >= camping.STATUS_ABANDONED)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id owner = params.getObjId("owner");
        if (!isIdValid(owner) || !isPlayer(owner) ||
            camping.getCampOwner(self) != owner ||
            camping.getCurrentCamp(owner) != self ||
            !isInTriggerVolume(self, "camp_" + self, owner))
        {
            return SCRIPT_CONTINUE;
        }
        sendSystemMessage(owner, SID_SYS_COMBAT_ABANDONED);
        camping.campAbandoned(self);
        return SCRIPT_CONTINUE;
    }
    public int handleSetStatus(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        int status = params.getInt(camping.DICT_NEW_STATUS);
        switch (status)
        {
            case camping.STATUS_NEW:
            setCampfire(self, TEMPLATE_LOGS_FRESH);
            break;
            case camping.STATUS_CREATION:
            break;
            case camping.STATUS_MAINTAIN:
            setCampfire(self, TEMPLATE_LOGS_FRESH);
            break;
            case camping.STATUS_ABANDONED:
            default:
            setCampfire(self, TEMPLATE_LOGS_SMOLDERING);
            break;
        }
        return SCRIPT_CONTINUE;
    }
    public void setCampfire(obj_id self, String tpf) throws InterruptedException
    {
        obj_id item = getObjIdObjVar(self, VAR_CAMP_CAMPFIRE);
        if ((item == null) || (item == obj_id.NULL_ID))
        {
        }
        else 
        {
            String itemTemplate = getTemplateName(item);
            if (itemTemplate.equals(tpf))
            {
                return;
            }
            destroyObject(item);
        }
        location here = getLocation(self);
        item = createObject(tpf, here);
        setObjVar(self, VAR_CAMP_CAMPFIRE, item);
    }
    public int handleCampCreationHeartbeat(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, camping.VAR_OWNER))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
        if ((owner == null) || (owner == obj_id.NULL_ID) ||
            (!exists(owner)) || (!isInWorld(owner)) || (!owner.isLoaded()))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        if (camping.getStatus(self) == camping.STATUS_NEW)
        {
            camping.setStatus(self, camping.STATUS_CREATION);
        }
        camping.sendCampCreationComplete(self);
        return SCRIPT_CONTINUE;
    }
    public int handleCampComplete(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, camping.VAR_OWNER))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
        if ((owner == null) || (owner == obj_id.NULL_ID))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        if ((!exists(owner)) || (!isInWorld(owner)) || (!owner.isLoaded()))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        if (!camping.isOwnerInVicinity(self))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        camping.setStatus(self, camping.STATUS_MAINTAIN);
        camping.sendCampMaintenanceHeartbeat(self);
        return SCRIPT_CONTINUE;
    }
    public int handleCampMaintenanceHeartbeat(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, camping.VAR_OWNER))
        {
            setObjVar(self, camping.VAR_OWNER, obj_id.NULL_ID);
            camping.campAbandoned(self);
            return SCRIPT_CONTINUE;
        }
        obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
        if ((owner == null) || (owner == obj_id.NULL_ID))
        {
            camping.campAbandoned(self);
            return SCRIPT_CONTINUE;
        }
        if ((!exists(owner)) || (!isInWorld(owner)) || (!owner.isLoaded()))
        {
            camping.beginCampOwnerAbsence(self);
            camping.sendCampMaintenanceHeartbeat(self);
            return SCRIPT_CONTINUE;
        }
        if (ai_lib.isInCombat(owner))
        {
            sendSystemMessage(owner, SID_SYS_COMBAT_ABANDONED);
            camping.campAbandoned(self);
            return SCRIPT_CONTINUE;
        }
        if (!camping.isOwnerInVicinity(self))
        {
            camping.beginCampOwnerAbsence(self);
            camping.sendCampMaintenanceHeartbeat(self);
            return SCRIPT_CONTINUE;
        }
        camping.sendCampMaintenanceHeartbeat(self);
        return SCRIPT_CONTINUE;
    }
    public int handleCampRestoreHeartbeat(obj_id self, dictionary params) throws InterruptedException
    {
        if (!hasObjVar(self, camping.VAR_OWNER))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        if (params == null ||
            !params.containsKey(camping.DICT_ABANDON_SEQUENCE))
        {
            return SCRIPT_CONTINUE;
        }
        int sequence = params.getInt(camping.DICT_ABANDON_SEQUENCE);
        if (sequence !=
                getIntObjVar(self, camping.VAR_CAMP_ABANDON_SEQUENCE) ||
            camping.getStatus(self) != camping.STATUS_MAINTAIN ||
            !hasObjVar(self, camping.VAR_CAMP_ABANDON_PENDING))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id owner = getObjIdObjVar(self, camping.VAR_OWNER);
        if ((owner == null) || (owner == obj_id.NULL_ID))
        {
            camping.nukeCamp(self);
            return SCRIPT_CONTINUE;
        }
        else 
        {
            if (owner.isLoaded() && exists(owner) && isInWorld(owner) &&
                camping.isOwnerInVicinity(self) &&
                isInTriggerVolume(self, "camp_" + self, owner))
            {
                camping.cancelCampOwnerAbsence(self);
                return SCRIPT_CONTINUE;
            }
            else 
            {
                if (owner.isLoaded() && isInWorld(owner))
                {
                    sendSystemMessage(owner, SID_SYS_ABANDONED_CAMP);
                }
                camping.campAbandoned(self);
                return SCRIPT_CONTINUE;
            }
        }
    }
    public int handleNuke(obj_id self, dictionary params) throws InterruptedException
    {
        camping.nukeCamp(self);
        return SCRIPT_CONTINUE;
    }
}
