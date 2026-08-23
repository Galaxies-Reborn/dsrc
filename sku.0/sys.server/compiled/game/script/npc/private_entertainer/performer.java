package script.npc.private_entertainer;

import script.*;
import script.library.*;
import java.util.Vector;

/** A short-lived, owner-gated cantina performer hired from a bartender. */
public class performer extends script.base_script
{
    private static final String BUFF_UI_ROOT =
        private_entertainer.VAR_ROOT + ".buff_ui";
    private static final String BUFF_UI_PID = BUFF_UI_ROOT + ".pid";
    private static final String BUFF_UI_PLAYER = BUFF_UI_ROOT + ".player";
    private static final String BUFF_UI_TYPE = BUFF_UI_ROOT + ".type";
    private static final String BUFF_UI_NONCE = BUFF_UI_ROOT + ".nonce";
    private static final String CLEANUP_SENT =
        private_entertainer.VAR_ROOT + ".cleanup_sent";

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (!configure(self))
        {
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }
        messageTo(
            self,
            "handlePrivateEntertainerLifecycle",
            null,
            private_entertainer.LIFECYCLE_TICK_SECONDS,
            false);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (!configure(self))
        {
            cleanup(self);
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }
        messageTo(
            self,
            "handlePrivateEntertainerLifecycle",
            null,
            private_entertainer.LIFECYCLE_TICK_SECONDS,
            false);
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(
        obj_id self,
        obj_id player,
        menu_info mi) throws InterruptedException
    {
        String type = getPerformerType(self);
        if (!private_entertainer.canPlayerUse(player, self, type))
        {
            return SCRIPT_CONTINUE;
        }
        mi.addRootMenu(
            menu_info_types.SERVER_MENU1,
            new string_id(
                private_entertainer.STF,
                private_entertainer.TYPE_DANCER.equals(type) ?
                    "watch_performance" : "listen_performance"));
        mi.addRootMenu(
            menu_info_types.SERVER_MENU2,
            new string_id(private_entertainer.STF, "buff_yourself"));
        mi.addRootMenu(
            menu_info_types.SERVER_MENU3,
            new string_id(private_entertainer.STF, "dismiss_performer"));
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(
        obj_id self,
        obj_id player,
        int item) throws InterruptedException
    {
        String type = getPerformerType(self);
        if (!private_entertainer.canPlayerUse(player, self, type))
        {
            if (isIdValid(player))
            {
                sendSystemMessage(
                    player,
                    new string_id(
                        private_entertainer.STF,
                        "not_your_performer"));
            }
            return SCRIPT_CONTINUE;
        }

        if (item == menu_info_types.SERVER_MENU1)
        {
            if (private_entertainer.TYPE_DANCER.equals(type))
            {
                private_entertainer.beginWatching(player, self);
            }
            else
            {
                private_entertainer.beginListening(player, self);
            }
        }
        else if (item == menu_info_types.SERVER_MENU2)
        {
            showBuffSelection(self, player, type);
        }
        else if (item == menu_info_types.SERVER_MENU3)
        {
            private_entertainer.dismiss(player, type);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerBuffSelection(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = sui.getPlayerId(params);
        int pageId = params.getInt("pageId");
        if (!isIdValid(player) ||
            !utils.hasScriptVar(self, BUFF_UI_PID) ||
            !utils.hasScriptVar(self, BUFF_UI_PLAYER) ||
            !utils.hasScriptVar(self, BUFF_UI_TYPE) ||
            !utils.hasScriptVar(self, BUFF_UI_NONCE) ||
            utils.getIntScriptVar(self, BUFF_UI_PID) != pageId ||
            utils.getObjIdScriptVar(self, BUFF_UI_PLAYER) != player)
        {
            return SCRIPT_CONTINUE;
        }

        String type = utils.getStringScriptVar(self, BUFF_UI_TYPE);
        String selectionNonce = utils.getStringScriptVar(
            self, BUFF_UI_NONCE);
        utils.removeScriptVarTree(self, BUFF_UI_ROOT);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL ||
            sui.getListboxSelectedRow(params) != 0 ||
            selectionNonce == null || selectionNonce.length() < 16 ||
            !private_entertainer.isSupportedType(type) ||
            !private_entertainer.canPlayerUse(player, self, type))
        {
            return SCRIPT_CONTINUE;
        }

        private_entertainer.beginPaidBuff(
            player, self, selectionNonce);
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerLifecycle(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (!hasRequiredTags(self))
        {
            cleanup(self);
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }

        int now = getCalendarTime();
        obj_id owner = getObjIdObjVar(
            self, private_entertainer.VAR_OWNER);
        obj_id building = getObjIdObjVar(
            self, private_entertainer.VAR_BUILDING);
        boolean expired = now >= getIntObjVar(
            self, private_entertainer.VAR_EXPIRES);
        boolean displaced = building !=
            private_entertainer.getCantinaBuilding(self);
        boolean orphaned = false;

        if (isIdValid(owner) && owner.isLoaded())
        {
            String type = getPerformerType(self);
            String activeVar = private_entertainer.getPlayerActiveVar(type);
            orphaned = activeVar.length() == 0 ||
                !hasObjVar(owner, activeVar) ||
                getObjIdObjVar(owner, activeVar) != self;
            if (!orphaned &&
                private_entertainer.getCantinaBuilding(owner) == building)
            {
                setObjVar(
                    self,
                    private_entertainer.VAR_LAST_OWNER_SEEN,
                    now);
            }
        }

        int lastSeen = getIntObjVar(
            self, private_entertainer.VAR_LAST_OWNER_SEEN);
        boolean ownerAway = !isIdValid(owner) || !owner.isLoaded() ||
            private_entertainer.getCantinaBuilding(owner) != building;
        if (expired || displaced || orphaned ||
            (ownerAway &&
                now - lastSeen >=
                    private_entertainer.OWNER_AWAY_GRACE_SECONDS))
        {
            if (expired && isIdValid(owner) && owner.isLoaded())
            {
                sendSystemMessage(
                    owner,
                    new string_id(
                        private_entertainer.STF,
                        "performer_expired"));
            }
            cleanup(self);
            destroyObject(self);
            return SCRIPT_CONTINUE;
        }

        ensurePerformanceState(self);
        messageTo(
            self,
            "handlePrivateEntertainerLifecycle",
            null,
            private_entertainer.LIFECYCLE_TICK_SECONDS,
            false);
        return SCRIPT_CONTINUE;
    }

    public int OnDestroy(obj_id self) throws InterruptedException
    {
        cleanup(self);
        return SCRIPT_CONTINUE;
    }

    public int OnDetach(obj_id self) throws InterruptedException
    {
        cleanup(self);
        return SCRIPT_CONTINUE;
    }

    private void showBuffSelection(
        obj_id self,
        obj_id player,
        String type) throws InterruptedException
    {
        if (!private_entertainer.canPlayerUse(player, self, type) ||
            !private_entertainer.canApplyConfiguredBuff(player, type))
        {
            sendSystemMessage(
                player,
                new string_id(
                    private_entertainer.STF,
                    "buff_not_available"));
            return;
        }
        if (utils.hasScriptVar(self, BUFF_UI_PID))
        {
            obj_id oldPlayer = utils.getObjIdScriptVar(
                self, BUFF_UI_PLAYER);
            if (isIdValid(oldPlayer))
            {
                sui.closeSUI(
                    oldPlayer,
                    utils.getIntScriptVar(self, BUFF_UI_PID));
            }
            utils.removeScriptVarTree(self, BUFF_UI_ROOT);
        }

        Vector choices = new Vector();
        choices.add(
            "@" + private_entertainer.STF + ":buff_" + type + "_option");
        String prompt = "@performance:inspire_menu_prompt " +
            getName(player) + "\n\n" +
            "@performance:inspire_menu_prompt2\n\n" +
            "@" + private_entertainer.STF + ":buff_prompt";
        int pid = sui.listbox(
            self,
            player,
            prompt,
            sui.OK_CANCEL,
            "@performance:inspire_menu_title",
            choices,
            "handlePrivateEntertainerBuffSelection");
        if (pid < 0)
        {
            return;
        }

        String nonce = player + ":" + self + ":" +
            getCalendarTime() + ":" + getGameTime() + ":" +
            rand(1, 2000000000);
        utils.setScriptVar(self, BUFF_UI_PID, pid);
        utils.setScriptVar(self, BUFF_UI_PLAYER, player);
        utils.setScriptVar(self, BUFF_UI_TYPE, type);
        utils.setScriptVar(self, BUFF_UI_NONCE, nonce);
        setSUIMaxRangeToObject(pid, private_entertainer.PERFORMER_USE_RANGE);
    }

    private boolean configure(obj_id self) throws InterruptedException
    {
        if (!hasRequiredTags(self) ||
            getCalendarTime() >= getIntObjVar(
                self, private_entertainer.VAR_EXPIRES) ||
            getObjIdObjVar(self, private_entertainer.VAR_BUILDING) !=
                private_entertainer.getCantinaBuilding(self))
        {
            return false;
        }
        setInvulnerable(self, true);
        setCreatureStatic(self, true);
        factions.setFaction(self, "Unattackable");
        ai_lib.setDefaultCalmBehavior(self, ai_lib.BEHAVIOR_SENTINEL);
        ensurePerformanceState(self);
        return getPerformanceType(self) > 0;
    }

    private void ensurePerformanceState(obj_id self)
        throws InterruptedException
    {
        String type = getPerformerType(self);
        int performanceIndex;
        if (private_entertainer.TYPE_DANCER.equals(type))
        {
            performanceIndex = performance.lookupPerformanceIndex(
                -1788534963, "basic", 0);
            ai_lib.setDefaultCalmMood(self, "npc_dance_basic");
            ai_lib.setMood(self, "npc_dance_basic");
            setPerformanceWatchTarget(self, self);
        }
        else if (private_entertainer.TYPE_MUSICIAN.equals(type))
        {
            performanceIndex = performance.lookupPerformanceIndex(
                866729052, "starwars1", 2);
            ai_lib.setDefaultCalmMood(self, "themepark_music_3");
            ai_lib.setMood(self, "themepark_music_3");
            setPerformanceListenTarget(self, self);
        }
        else
        {
            setPerformanceType(self, 0);
            return;
        }
        if (performanceIndex > 0)
        {
            setPerformanceType(self, performanceIndex);
            setPerformanceStartTime(self, getGameTime());
        }
    }

    private boolean hasRequiredTags(obj_id self)
        throws InterruptedException
    {
        return isIdValid(self) && exists(self) &&
            hasObjVar(self, private_entertainer.VAR_OWNER) &&
            hasObjVar(self, private_entertainer.VAR_TYPE) &&
            hasObjVar(self, private_entertainer.VAR_BUILDING) &&
            hasObjVar(self, private_entertainer.VAR_EXPIRES) &&
            hasObjVar(self, private_entertainer.VAR_LAST_OWNER_SEEN) &&
            private_entertainer.isSupportedType(getPerformerType(self));
    }

    private String getPerformerType(obj_id self)
        throws InterruptedException
    {
        return hasObjVar(self, private_entertainer.VAR_TYPE) ?
            getStringObjVar(self, private_entertainer.VAR_TYPE) : "";
    }

    private void cleanup(obj_id self) throws InterruptedException
    {
        if (!isIdValid(self) || utils.hasScriptVar(self, CLEANUP_SENT))
        {
            return;
        }
        utils.setScriptVar(self, CLEANUP_SENT, true);
        if (utils.hasScriptVar(self, BUFF_UI_PID))
        {
            obj_id player = utils.getObjIdScriptVar(
                self, BUFF_UI_PLAYER);
            if (isIdValid(player) && player.isLoaded())
            {
                sui.closeSUI(
                    player,
                    utils.getIntScriptVar(self, BUFF_UI_PID));
            }
            utils.removeScriptVarTree(self, BUFF_UI_ROOT);
        }
        private_entertainer.cleanupAudience(self);
        if (hasObjVar(self, private_entertainer.VAR_OWNER) &&
            hasObjVar(self, private_entertainer.VAR_TYPE))
        {
            private_entertainer.clearPlayerPointer(
                getObjIdObjVar(self, private_entertainer.VAR_OWNER),
                self,
                getStringObjVar(self, private_entertainer.VAR_TYPE));
        }
        setPerformanceType(self, 0);
    }
}
