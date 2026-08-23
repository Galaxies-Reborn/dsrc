package script.item.droid;

import script.*;
import script.library.consumable;
import script.library.craftinglib;
import script.library.sui;
import script.library.utils;

import java.util.Vector;

public class worker_droid extends script.base_script
{
    public worker_droid()
    {
    }

    public static final String PLAYER_REQUESTOR_OBJVAR = "precu.workerDroid.requestor";
    public static final String PLAYER_REQUEST_TOKEN_OBJVAR = "precu.workerDroid.requestToken";
    public static final String ITEM_REQUEST_TOKEN_OBJVAR = "precu.workerDroid.handoffToken";
    public static final String ITEM_PENDING_ACTION_ROOT =
        "precu.workerDroid.pendingAction";
    public static final String ITEM_PENDING_PLAYER =
        ITEM_PENDING_ACTION_ROOT + ".player";
    public static final String ITEM_PENDING_TARGET =
        ITEM_PENDING_ACTION_ROOT + ".target";
    public static final String ITEM_PENDING_TOKEN =
        ITEM_PENDING_ACTION_ROOT + ".token";
    public static final String ITEM_PENDING_ACTION =
        ITEM_PENDING_ACTION_ROOT + ".action";
    public static final String ITEM_PENDING_STATE =
        ITEM_PENDING_ACTION_ROOT + ".state";
    public static final String ITEM_PENDING_COUNT_BEFORE =
        ITEM_PENDING_ACTION_ROOT + ".countBefore";
    public static final String ITEM_PENDING_DEADLINE =
        ITEM_PENDING_ACTION_ROOT + ".deadline";
    public static final String ITEM_PENDING_TIMEOUT_NOTIFIED =
        ITEM_PENDING_ACTION_ROOT + ".timeoutNotified";
    public static final String PLAYER_ACTIVE_ITEM = "precu.workerDroid.activeItem";
    public static final String PLAYER_PENDING_ITEM =
        "precu.workerDroid.pendingItem";
    public static final String PLAYER_LOCATE_COOLDOWN =
        "precu.workerDroid.locateCooldownUntil";
    public static final String SCRIPT_VAR_BASE = "precu.workerDroid";
    public static final String SCRIPT_VAR_PHASE = SCRIPT_VAR_BASE + ".phase";
    public static final String SCRIPT_VAR_PLAYER = SCRIPT_VAR_BASE + ".player";
    public static final String SCRIPT_VAR_TOKEN = SCRIPT_VAR_BASE + ".token";
    public static final String SCRIPT_VAR_QUERIED_TARGETS = SCRIPT_VAR_BASE + ".queriedTargets";
    public static final String SCRIPT_VAR_TARGETS = SCRIPT_VAR_BASE + ".targets";
    public static final String SCRIPT_VAR_LABELS = SCRIPT_VAR_BASE + ".labels";
    public static final String SCRIPT_VAR_SELECTED_TARGET = SCRIPT_VAR_BASE + ".selectedTarget";
    public static final String PHASE_LOCATING = "locating";
    public static final String PHASE_QUERYING = "querying";
    public static final String PHASE_SELECTING_TARGET = "selectingTarget";
    public static final String PHASE_SELECTING_ACTION = "selectingAction";
    public static final String PHASE_AWAITING_ACTION = "awaitingAction";
    public static final String PID_NAME = "precuWorkerDroidManage";
    public static final String SEEKER_VISUAL = "object/creature/npc/droid/bounty_seeker.iff";
    public static final float LOCATE_TIMEOUT_SECONDS = 20.0f;
    public static final int LOCATE_COOLDOWN_SECONDS = 60;
    public static final float SELECTION_TIMEOUT_SECONDS = 10.0f * 60.0f;
    public static final float ACTION_DELIVERY_DELAY_SECONDS = 2.0f;
    public static final float ACTION_RETRY_SECONDS = 10.0f;
    public static final int ACTION_MAX_RETRIES = 12;
    public static final float ACTION_CONFIRMATION_TIMEOUT_SECONDS =
        5.0f * 60.0f;
    public static final int PENDING_STATE_DISPATCHED = 1;
    public static final int PENDING_STATE_ACKNOWLEDGED = 2;
    public static final int PENDING_STATE_TIMED_OUT = 3;
    public static final int LAUNCH_RESULT_QUEUED = 1;
    public static final int LAUNCH_RESULT_INVALID_STATE = -1;
    public static final int LAUNCH_RESULT_COOLDOWN = -2;
    public static final int LAUNCH_RESULT_ALREADY_RUNNING = -3;
    public static final int LAUNCH_RESULT_OTHER_ACTIVE_ITEM = -4;
    public static final int LAUNCH_RESULT_OTHER_REQUESTOR = -5;
    public static final int LAUNCH_RESULT_QUEUE_FAILED = -6;
    public static final int LAUNCH_RESULT_PENDING_ACTION = -7;
    public static final int LAUNCH_RESULT_OTHER_PENDING_ACTION = -8;
    public static final string_id DISPLAY_NAME =
        new string_id("precu_container_droid", "worker_droid_n");

    public int OnAttach(obj_id self) throws InterruptedException
    {
        setName(self, DISPLAY_NAME);
        if (hasPendingAction(self))
        {
            resumePendingAction(self);
        }
        else
        {
            clearStaleHandoff(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        setName(self, DISPLAY_NAME);
        if (hasPendingAction(self))
        {
            resumePendingAction(self);
        }
        else
        {
            clearStaleHandoff(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuRequest(obj_id self, obj_id player, menu_info mi) throws InterruptedException
    {
        if (!isValidId(player) || !utils.isNestedWithin(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        menu_info_data useMenu = mi.getMenuItemByType(menu_info_types.ITEM_USE);
        if (useMenu == null)
        {
            mi.addRootMenu(menu_info_types.ITEM_USE, new string_id("", ""));
        }
        else
        {
            useMenu.setServerNotify(true);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnObjectMenuSelect(obj_id self, obj_id player, int item) throws InterruptedException
    {
        if (item == menu_info_types.ITEM_USE)
        {
            beginWorkerDroidSearch(self, player);
        }
        return SCRIPT_CONTINUE;
    }

    public int OnAboutToBeTransferred(obj_id self, obj_id destination, obj_id transferer) throws InterruptedException
    {
        if (hasPendingAction(self) ||
            utils.hasScriptVar(self, SCRIPT_VAR_PHASE))
        {
            obj_id player = hasObjVar(self, ITEM_PENDING_PLAYER)
                ? getObjIdObjVar(self, ITEM_PENDING_PLAYER)
                : utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
            if (isValidId(player))
            {
                sendSystemMessage(player, "The Worker Droid cannot be moved while it is processing a request.", null);
            }
            return SCRIPT_OVERRIDE;
        }
        return SCRIPT_CONTINUE;
    }

    public int beginWorkerDroidSearch(obj_id self, obj_id player) throws InterruptedException
    {
        if (!canLaunch(self, player))
        {
            return LAUNCH_RESULT_INVALID_STATE;
        }
        if (hasPendingAction(self))
        {
            sendSystemMessage(
                player,
                "This Worker Droid is waiting for an installation to confirm its dispatched action.",
                null);
            return LAUNCH_RESULT_PENDING_ACTION;
        }
        if (hasObjVar(player, PLAYER_PENDING_ITEM))
        {
            obj_id pendingItem = getObjIdObjVar(
                player, PLAYER_PENDING_ITEM);
            if (isValidId(pendingItem) && exists(pendingItem) &&
                hasObjVar(pendingItem, ITEM_PENDING_ACTION_ROOT))
            {
                sendSystemMessage(
                    player,
                    "Another Worker Droid is waiting for an installation to confirm its dispatched action.",
                    null);
                return LAUNCH_RESULT_OTHER_PENDING_ACTION;
            }
            removeObjVar(player, PLAYER_PENDING_ITEM);
        }
        int now = getCalendarTime();
        if (hasObjVar(player, PLAYER_LOCATE_COOLDOWN))
        {
            int cooldownUntil = getIntObjVar(player, PLAYER_LOCATE_COOLDOWN);
            if (cooldownUntil > now)
            {
                sendSystemMessage(
                    player,
                    "The Worker Droid network is recharging. Try again in " +
                        (cooldownUntil - now) + " seconds.",
                    null);
                return LAUNCH_RESULT_COOLDOWN;
            }
            removeObjVar(player, PLAYER_LOCATE_COOLDOWN);
        }
        if (utils.hasScriptVar(self, SCRIPT_VAR_PHASE))
        {
            sendSystemMessage(player, "This Worker Droid is already processing a request.", null);
            return LAUNCH_RESULT_ALREADY_RUNNING;
        }
        if (utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM))
        {
            obj_id activeItem = utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM);
            if (isValidId(activeItem) && exists(activeItem))
            {
                sendSystemMessage(player, "Another Worker Droid request is already in progress.", null);
                return LAUNCH_RESULT_OTHER_ACTIVE_ITEM;
            }
            utils.removeScriptVar(player, PLAYER_ACTIVE_ITEM);
        }
        if (hasObjVar(player, PLAYER_REQUESTOR_OBJVAR))
        {
            obj_id requestor = getObjIdObjVar(player, PLAYER_REQUESTOR_OBJVAR);
            if (isValidId(requestor) && requestor != self && exists(requestor))
            {
                sendSystemMessage(player, "Another Worker Droid request is already in progress.", null);
                return LAUNCH_RESULT_OTHER_REQUESTOR;
            }
            removeObjVar(player, PLAYER_REQUESTOR_OBJVAR);
            removeObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR);
        }

        utils.removeScriptVarTree(self, SCRIPT_VAR_BASE);
        int requestToken = rand(1, 2000000000);
        utils.setScriptVar(self, SCRIPT_VAR_PHASE, PHASE_LOCATING);
        utils.setScriptVar(self, SCRIPT_VAR_PLAYER, player);
        utils.setScriptVar(self, SCRIPT_VAR_TOKEN, requestToken);
        utils.setScriptVar(player, PLAYER_ACTIVE_ITEM, self);
        setObjVar(player, PLAYER_REQUESTOR_OBJVAR, self);
        setObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR, requestToken);
        setObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR, requestToken);

        boolean queued = queueCommand(
            player,
            getStringCrc("locatestructure"),
            null,
            "",
            COMMAND_PRIORITY_DEFAULT);
        if (!queued)
        {
            cleanupFlow(self, player);
            sendSystemMessage(player, "The Worker Droid could not contact the structure network. It was not consumed.", null);
            return LAUNCH_RESULT_QUEUE_FAILED;
        }
        setObjVar(
            player,
            PLAYER_LOCATE_COOLDOWN,
            getCalendarTime() + LOCATE_COOLDOWN_SECONDS);

        dictionary timeout = new dictionary();
        timeout.put("token", requestToken);
        messageTo(self, "workerDroidLocateTimeout", timeout, LOCATE_TIMEOUT_SECONDS, false);
        sendSystemMessage(player, "The Worker Droid is locating your factories, harvesters, and generators...", null);
        return LAUNCH_RESULT_QUEUED;
    }

    public int handleWorkerDroidLocateResponse(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() || !isDiscoveryPhase(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
        if (!isFlowOwner(self, player))
        {
            cleanupFlow(self, player);
            return SCRIPT_CONTINUE;
        }

        String[] records = params.getStringArray("records");
        if (records == null || records.length == 0)
        {
            return SCRIPT_CONTINUE;
        }

        Vector queriedTargets = new Vector();
        if (utils.hasScriptVar(self, SCRIPT_VAR_QUERIED_TARGETS))
        {
            queriedTargets = utils.getResizeableObjIdArrayScriptVar(self, SCRIPT_VAR_QUERIED_TARGETS);
        }
        int requestToken = utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN);
        for (String record : records)
        {
            obj_id target = parseLocateRecord(record);
            if (!isValidId(target) || queriedTargets.contains(target))
            {
                continue;
            }
            queriedTargets.add(target);
            dictionary query = new dictionary();
            query.put("player", player);
            query.put("workerDroid", self);
            query.put("token", requestToken);
            messageTo(target, "workerDroidQuery", query, 0.0f, true);
        }
        utils.setScriptVar(
            self,
            SCRIPT_VAR_QUERIED_TARGETS,
            (obj_id[])queriedTargets.toArray(new obj_id[queriedTargets.size()]));
        utils.setScriptVar(self, SCRIPT_VAR_PHASE, PHASE_QUERYING);

        return SCRIPT_CONTINUE;
    }

    public int handleWorkerDroidQueryResponse(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !utils.hasScriptVar(self, SCRIPT_VAR_PHASE) ||
            !PHASE_QUERYING.equals(utils.getStringScriptVar(self, SCRIPT_VAR_PHASE)))
        {
            return SCRIPT_CONTINUE;
        }
        int requestToken = utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN);
        if (params.getInt("token") != requestToken)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
        if (params.getObjId("player") != player || !isFlowOwner(self, player))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id target = params.getObjId("target");
        if (!isValidId(target) || !utils.hasScriptVar(self, SCRIPT_VAR_QUERIED_TARGETS))
        {
            return SCRIPT_CONTINUE;
        }
        Vector queriedTargets = utils.getResizeableObjIdArrayScriptVar(self, SCRIPT_VAR_QUERIED_TARGETS);
        if (!queriedTargets.contains(target))
        {
            return SCRIPT_CONTINUE;
        }

        Vector targets = new Vector();
        Vector labels = new Vector();
        if (utils.hasScriptVar(self, SCRIPT_VAR_TARGETS))
        {
            targets = utils.getResizeableObjIdArrayScriptVar(self, SCRIPT_VAR_TARGETS);
            labels = utils.getResizeableStringArrayScriptVar(self, SCRIPT_VAR_LABELS);
        }
        if (targets.contains(target))
        {
            return SCRIPT_CONTINUE;
        }
        String label = params.getString("label");
        if (label == null || label.length() == 0)
        {
            label = "Installation " + target;
        }
        targets.add(target);
        labels.add(label);
        utils.setScriptVar(self, SCRIPT_VAR_TARGETS, (obj_id[])targets.toArray(new obj_id[targets.size()]));
        utils.setScriptVar(self, SCRIPT_VAR_LABELS, (String[])labels.toArray(new String[labels.size()]));
        return SCRIPT_CONTINUE;
    }

    public int finishWorkerDroidQuery(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !utils.hasScriptVar(self, SCRIPT_VAR_PHASE) ||
            !PHASE_QUERYING.equals(utils.getStringScriptVar(self, SCRIPT_VAR_PHASE)))
        {
            return SCRIPT_CONTINUE;
        }
        if (params.getInt("token") != utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
        if (!isFlowOwner(self, player))
        {
            cleanupFlow(self, player);
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(self, SCRIPT_VAR_TARGETS))
        {
            cleanupFlow(self, player);
            sendSystemMessage(player, "The Worker Droid found no eligible factories, harvesters, or generators owned by this character. It was not consumed.", null);
            return SCRIPT_CONTINUE;
        }

        obj_id[] targets = utils.getObjIdArrayScriptVar(self, SCRIPT_VAR_TARGETS);
        String[] labels = utils.getStringArrayScriptVar(self, SCRIPT_VAR_LABELS);
        if (targets == null || labels == null || targets.length == 0 || targets.length != labels.length)
        {
            cleanupFlow(self, player);
            sendSystemMessage(player, "The Worker Droid could not assemble a valid installation list. It was not consumed.", null);
            return SCRIPT_CONTINUE;
        }

        utils.setScriptVar(self, SCRIPT_VAR_PHASE, PHASE_SELECTING_TARGET);
        int pid = sui.listbox(
            self,
            player,
            "Select a factory, harvester, or generator for the Worker Droid to manage. Its current state is shown in the list.",
            sui.OK_CANCEL,
            "Worker Droid - Installations",
            labels,
            "handleWorkerDroidTargetSelection",
            true,
            false);
        if (pid < 0)
        {
            cleanupFlow(self, player);
            sendSystemMessage(player, "The Worker Droid could not open the installation list. It was not consumed.", null);
            return SCRIPT_CONTINUE;
        }
        sui.setPid(player, pid, PID_NAME);
        scheduleSelectionTimeout(
            self,
            utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN),
            PHASE_SELECTING_TARGET,
            pid);
        return SCRIPT_CONTINUE;
    }

    public int handleWorkerDroidTargetSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = getValidSuiPlayer(self, params, PHASE_SELECTING_TARGET);
        if (!isValidId(player))
        {
            return SCRIPT_CONTINUE;
        }
        int selectedRow = sui.getListboxSelectedRow(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL || selectedRow < 0)
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            return SCRIPT_CONTINUE;
        }
        obj_id[] targets = utils.getObjIdArrayScriptVar(self, SCRIPT_VAR_TARGETS);
        if (targets == null || selectedRow >= targets.length || !isValidId(targets[selectedRow]))
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            sendSystemMessage(player, "That Worker Droid target is no longer valid. The droid was not consumed.", null);
            return SCRIPT_CONTINUE;
        }

        utils.setScriptVar(self, SCRIPT_VAR_SELECTED_TARGET, targets[selectedRow]);
        utils.setScriptVar(self, SCRIPT_VAR_PHASE, PHASE_SELECTING_ACTION);
        String[] actions = { "Activate installation", "Deactivate installation" };
        int pid = sui.listbox(
            self,
            player,
            "Choose the single remote action this Worker Droid will perform. Dispatching consumes one droid.",
            sui.OK_CANCEL,
            "Worker Droid - Remote Action",
            actions,
            "handleWorkerDroidActionSelection",
            true,
            false);
        if (pid < 0)
        {
            cleanupFlow(self, player);
            sendSystemMessage(player, "The Worker Droid could not open the action list. It was not consumed.", null);
            return SCRIPT_CONTINUE;
        }
        sui.setPid(player, pid, PID_NAME);
        scheduleSelectionTimeout(
            self,
            utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN),
            PHASE_SELECTING_ACTION,
            pid);
        return SCRIPT_CONTINUE;
    }

    public int handleWorkerDroidActionSelection(obj_id self, dictionary params) throws InterruptedException
    {
        obj_id player = getValidSuiPlayer(self, params, PHASE_SELECTING_ACTION);
        if (!isValidId(player))
        {
            return SCRIPT_CONTINUE;
        }
        int selectedRow = sui.getListboxSelectedRow(params);
        if (sui.getIntButtonPressed(params) == sui.BP_CANCEL || selectedRow < 0)
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            return SCRIPT_CONTINUE;
        }
        if (selectedRow > 1 || !canLaunch(self, player))
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            return SCRIPT_CONTINUE;
        }

        obj_id target = utils.getObjIdScriptVar(self, SCRIPT_VAR_SELECTED_TARGET);
        if (!isValidId(target))
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            sendSystemMessage(player, "That Worker Droid target is no longer valid. The droid was not consumed.", null);
            return SCRIPT_CONTINUE;
        }
        String action = selectedRow == 0 ? "activate" : "deactivate";
        int requestToken = utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN);
        if (!storePendingAction(
                self,
                player,
                target,
                requestToken,
                action))
        {
            sui.removePid(player, PID_NAME);
            cleanupFlow(self, player);
            sendSystemMessage(
                player,
                "The Worker Droid could not preserve its dispatch state. It was not consumed.",
                null);
            return SCRIPT_CONTINUE;
        }

        sui.removePid(player, PID_NAME);
        if (!holdPendingActionFlow(self, player, requestToken))
        {
            removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
            clearPendingActionFlow(self, player);
            sendSystemMessage(
                player,
                "The Worker Droid could not lock its pending dispatch. It was not consumed.",
                null);
            return SCRIPT_CONTINUE;
        }
        CustomerServiceLog(
            "WorkerDroid",
            "Player %TU dispatched Worker Droid " + self + " to " +
                action + " installation " + target + ".",
            player);
        boolean deadlineQueued = armPendingActionDeadline(
            self,
            requestToken,
            getPendingActionDeadlineDelay(self));
        if (!deadlineQueued)
        {
            removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
            clearPendingActionFlow(self, player);
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " could not queue its durable deadline for token=" +
                    requestToken + "; the undispatched action was rolled back.");
            sendSystemMessage(
                player,
                "The Worker Droid could not queue its remote action. It was not consumed.",
                null);
            return SCRIPT_CONTINUE;
        }
        boolean dispatchQueued = armPendingActionRetry(
            self, requestToken, 0, ACTION_DELIVERY_DELAY_SECONDS);
        if (!dispatchQueued)
        {
            removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
            clearPendingActionFlow(self, player);
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " could not queue its initial delivery for token=" +
                    requestToken + "; the undispatched action was rolled back.");
            sendSystemMessage(
                player,
                "The Worker Droid could not queue its remote action. It was not consumed.",
                null);
            return SCRIPT_CONTINUE;
        }
        launchSeekerVisual(player);
        sendSystemMessage(
            player,
            "The Worker Droid has been dispatched. One droid will be consumed after the installation confirms the action.",
            null);
        return SCRIPT_CONTINUE;
    }

    public boolean storePendingAction(obj_id self, obj_id player,
        obj_id target, int token, String action)
        throws InterruptedException
    {
        if (hasPendingAction(self) || !isValidId(player) ||
            !isValidId(target) || token <= 0 ||
            (!"activate".equals(action) && !"deactivate".equals(action)))
        {
            return false;
        }
        int countBefore = getCount(self);
        if (countBefore < 0)
        {
            return false;
        }

        boolean stored = true;
        stored &= setObjVar(self, ITEM_PENDING_PLAYER, player);
        stored &= setObjVar(self, ITEM_PENDING_TARGET, target);
        stored &= setObjVar(self, ITEM_PENDING_TOKEN, token);
        stored &= setObjVar(self, ITEM_PENDING_ACTION, action);
        stored &= setObjVar(
            self, ITEM_PENDING_COUNT_BEFORE, countBefore);
        stored &= setObjVar(
            self,
            ITEM_PENDING_DEADLINE,
            getCalendarTime() +
                (int)ACTION_CONFIRMATION_TIMEOUT_SECONDS);
        // Commit the dispatchable state last. A partial tuple is never sent.
        stored &= setObjVar(
            self, ITEM_PENDING_STATE, PENDING_STATE_DISPATCHED);
        if (!stored || !hasValidPendingAction(self))
        {
            removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
            return false;
        }
        return true;
    }

    public boolean hasPendingAction(obj_id self)
        throws InterruptedException
    {
        return isValidId(self) && hasObjVar(self, ITEM_PENDING_ACTION_ROOT);
    }

    public boolean hasValidPendingAction(obj_id self)
        throws InterruptedException
    {
        if (!hasPendingAction(self) ||
            !hasObjVar(self, ITEM_PENDING_PLAYER) ||
            !hasObjVar(self, ITEM_PENDING_TARGET) ||
            !hasObjVar(self, ITEM_PENDING_TOKEN) ||
            !hasObjVar(self, ITEM_PENDING_ACTION) ||
            !hasObjVar(self, ITEM_PENDING_STATE) ||
            !hasObjVar(self, ITEM_PENDING_COUNT_BEFORE) ||
            !hasObjVar(self, ITEM_PENDING_DEADLINE))
        {
            return false;
        }
        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        String action = getStringObjVar(self, ITEM_PENDING_ACTION);
        return isValidId(getObjIdObjVar(self, ITEM_PENDING_PLAYER)) &&
            !isIdNull(getObjIdObjVar(self, ITEM_PENDING_TARGET)) &&
            getIntObjVar(self, ITEM_PENDING_TOKEN) > 0 &&
            getIntObjVar(self, ITEM_PENDING_COUNT_BEFORE) >= 0 &&
            getIntObjVar(self, ITEM_PENDING_DEADLINE) > 0 &&
            ("activate".equals(action) || "deactivate".equals(action)) &&
            (state == PENDING_STATE_DISPATCHED ||
                state == PENDING_STATE_ACKNOWLEDGED ||
                state == PENDING_STATE_TIMED_OUT);
    }

    public boolean holdPendingActionFlow(obj_id self, obj_id player,
        int token) throws InterruptedException
    {
        if (!isValidId(player) || token <= 0)
        {
            return false;
        }
        if (utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM) &&
            utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM) != self)
        {
            return false;
        }
        if (hasObjVar(player, PLAYER_PENDING_ITEM) &&
            getObjIdObjVar(player, PLAYER_PENDING_ITEM) != self)
        {
            obj_id pendingItem = getObjIdObjVar(
                player, PLAYER_PENDING_ITEM);
            if (isValidId(pendingItem) && exists(pendingItem) &&
                hasObjVar(pendingItem, ITEM_PENDING_ACTION_ROOT))
            {
                return false;
            }
            removeObjVar(player, PLAYER_PENDING_ITEM);
        }
        if (hasObjVar(player, PLAYER_REQUESTOR_OBJVAR) &&
            getObjIdObjVar(player, PLAYER_REQUESTOR_OBJVAR) != self)
        {
            return false;
        }

        utils.setScriptVar(player, PLAYER_ACTIVE_ITEM, self);
        if (!setObjVar(player, PLAYER_PENDING_ITEM, self))
        {
            return false;
        }
        if (hasObjVar(player, PLAYER_REQUESTOR_OBJVAR))
        {
            removeObjVar(player, PLAYER_REQUESTOR_OBJVAR);
            if (hasObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR))
            {
                removeObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR);
            }
        }
        if (hasObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR))
        {
            removeObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR);
        }

        utils.removeScriptVarTree(self, SCRIPT_VAR_BASE);
        utils.setScriptVar(self, SCRIPT_VAR_PHASE, PHASE_AWAITING_ACTION);
        utils.setScriptVar(self, SCRIPT_VAR_PLAYER, player);
        utils.setScriptVar(self, SCRIPT_VAR_TOKEN, token);
        return utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM) &&
            utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM) == self &&
            hasObjVar(player, PLAYER_PENDING_ITEM) &&
            getObjIdObjVar(player, PLAYER_PENDING_ITEM) == self;
    }

    public void clearPendingActionFlow(obj_id self, obj_id player)
        throws InterruptedException
    {
        boolean ownsPendingFlow = false;
        if (isValidId(player) &&
            utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM) &&
            utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM) == self)
        {
            utils.removeScriptVar(player, PLAYER_ACTIVE_ITEM);
            ownsPendingFlow = true;
        }
        if (isValidId(player) && hasObjVar(player, PLAYER_PENDING_ITEM) &&
            getObjIdObjVar(player, PLAYER_PENDING_ITEM) == self)
        {
            removeObjVar(player, PLAYER_PENDING_ITEM);
            ownsPendingFlow = true;
        }
        if (isValidId(player) &&
            hasObjVar(player, PLAYER_REQUESTOR_OBJVAR) &&
            getObjIdObjVar(player, PLAYER_REQUESTOR_OBJVAR) == self)
        {
            removeObjVar(player, PLAYER_REQUESTOR_OBJVAR);
            if (hasObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR))
            {
                removeObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR);
            }
            ownsPendingFlow = true;
        }
        if (isValidId(player) && ownsPendingFlow &&
            sui.hasPid(player, PID_NAME))
        {
            sui.removePid(player, PID_NAME);
        }
        if (exists(self) && hasObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR))
        {
            removeObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR);
        }
        if (exists(self))
        {
            utils.removeScriptVarTree(self, SCRIPT_VAR_BASE);
        }
    }

    public void resumePendingAction(obj_id self)
        throws InterruptedException
    {
        if (!hasPendingAction(self))
        {
            return;
        }
        if (hasObjVar(self, ITEM_PENDING_STATE) &&
            !hasObjVar(self, ITEM_PENDING_DEADLINE))
        {
            // Backfill durable actions created by an earlier script revision.
            // The existing state remains the commit marker.
            if (!setObjVar(
                    self,
                    ITEM_PENDING_DEADLINE,
                    getCalendarTime() +
                        (int)ACTION_CONFIRMATION_TIMEOUT_SECONDS))
            {
                obj_id legacyPlayer = hasObjVar(
                    self, ITEM_PENDING_PLAYER)
                    ? getObjIdObjVar(self, ITEM_PENDING_PLAYER)
                    : utils.getContainingPlayer(self);
                clearPendingActionFlow(self, legacyPlayer);
                CustomerServiceLog(
                    "WorkerDroid",
                    "CRITICAL: Worker Droid " + self +
                        " could not backfill its persistent deadline; its player lock was cleared and its item state was preserved for initialization or administrative recovery.");
                return;
            }
        }
        if (!hasValidPendingAction(self))
        {
            // PENDING_STATE is the commit marker and is written last. A root
            // without it was never dispatchable, so it is safe to discard.
            if (!hasObjVar(self, ITEM_PENDING_STATE))
            {
                obj_id partialPlayer = hasObjVar(self, ITEM_PENDING_PLAYER)
                    ? getObjIdObjVar(self, ITEM_PENDING_PLAYER)
                    : utils.getContainingPlayer(self);
                removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
                clearPendingActionFlow(self, partialPlayer);
                return;
            }
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " has malformed persistent pending-action state; preserving it for administrative recovery.");
            return;
        }

        obj_id player = getObjIdObjVar(self, ITEM_PENDING_PLAYER);
        int token = getIntObjVar(self, ITEM_PENDING_TOKEN);
        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        if (state == PENDING_STATE_ACKNOWLEDGED ||
            state == PENDING_STATE_TIMED_OUT)
        {
            // The exact target tuple is already terminal. Charge
            // reconciliation must not be blocked by a later unrelated droid.
            recoverTerminalPendingAction(
                self, token, "initialization recovery");
            return;
        }
        if (getIntObjVar(self, ITEM_PENDING_DEADLINE) <=
            getCalendarTime())
        {
            dictionary expired = new dictionary();
            expired.put("token", token);
            expirePendingWorkerDroidAction(self, expired);
            return;
        }
        if (!armPendingActionDeadline(
                self, token, getPendingActionDeadlineDelay(self)))
        {
            forcePendingActionTimeout(
                self, token, "initialization deadline enqueue failed");
            return;
        }
        if (!holdPendingActionFlow(self, player, token))
        {
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " could not restore its persistent pending-action lock; its durable deadline remains armed.");
            return;
        }

        if (!armPendingActionRetry(
                self, token, 0, ACTION_DELIVERY_DELAY_SECONDS))
        {
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " could not queue its initialization delivery retry for token=" +
                    token + "; its durable deadline remains armed.");
        }
    }

    public boolean armPendingActionRetry(obj_id self, int token,
        int attempt, float delay) throws InterruptedException
    {
        dictionary retry = new dictionary();
        retry.put("token", token);
        retry.put("attempt", attempt);
        return messageTo(
            self,
            "retryPendingWorkerDroidAction",
            retry,
            delay,
            true);
    }

    public boolean armPendingActionDeadline(obj_id self, int token,
        float delay) throws InterruptedException
    {
        dictionary deadline = new dictionary();
        deadline.put("token", token);
        return messageTo(
            self,
            "expirePendingWorkerDroidAction",
            deadline,
            Math.max(0.0f, delay),
            true);
    }

    public float getPendingActionDeadlineDelay(obj_id self)
        throws InterruptedException
    {
        if (!hasObjVar(self, ITEM_PENDING_DEADLINE))
        {
            return ACTION_CONFIRMATION_TIMEOUT_SECONDS;
        }
        return Math.max(
            0.0f,
            (float)(getIntObjVar(self, ITEM_PENDING_DEADLINE) -
                getCalendarTime()));
    }

    public int expirePendingWorkerDroidAction(obj_id self,
        dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !hasValidPendingAction(self) ||
            params.getInt("token") !=
                getIntObjVar(self, ITEM_PENDING_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }

        int token = getIntObjVar(self, ITEM_PENDING_TOKEN);
        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        if (state == PENDING_STATE_ACKNOWLEDGED ||
            state == PENDING_STATE_TIMED_OUT)
        {
            recoverTerminalPendingAction(
                self, token, "terminal deadline handler");
            return SCRIPT_CONTINUE;
        }

        int remaining = getIntObjVar(self, ITEM_PENDING_DEADLINE) -
            getCalendarTime();
        if (remaining > 0)
        {
            if (!armPendingActionDeadline(self, token, (float)remaining))
            {
                forcePendingActionTimeout(
                    self, token, "early deadline re-enqueue failed");
            }
            return SCRIPT_CONTINUE;
        }
        if (state != PENDING_STATE_DISPATCHED)
        {
            return SCRIPT_CONTINUE;
        }
        forcePendingActionTimeout(self, token, "confirmation deadline expired");
        return SCRIPT_CONTINUE;
    }

    public boolean dispatchPendingAction(obj_id self, int attempt)
        throws InterruptedException
    {
        if (!hasValidPendingAction(self) ||
            getIntObjVar(self, ITEM_PENDING_STATE) !=
                PENDING_STATE_DISPATCHED ||
            attempt < 0 || attempt > ACTION_MAX_RETRIES)
        {
            return false;
        }

        if (getIntObjVar(self, ITEM_PENDING_DEADLINE) <=
            getCalendarTime())
        {
            dictionary expired = new dictionary();
            expired.put("token", getIntObjVar(self, ITEM_PENDING_TOKEN));
            expirePendingWorkerDroidAction(self, expired);
            return false;
        }

        obj_id player = getObjIdObjVar(self, ITEM_PENDING_PLAYER);
        obj_id target = getObjIdObjVar(self, ITEM_PENDING_TARGET);
        int token = getIntObjVar(self, ITEM_PENDING_TOKEN);
        String action = getStringObjVar(self, ITEM_PENDING_ACTION);
        dictionary request = new dictionary();
        request.put("player", player);
        request.put("workerDroid", self);
        request.put("token", token);
        request.put("action", action);
        boolean queued = messageTo(
            target,
            "workerDroidAction",
            request,
            ACTION_DELIVERY_DELAY_SECONDS,
            true);

        if (attempt < ACTION_MAX_RETRIES)
        {
            if (!armPendingActionRetry(
                    self,
                    token,
                    attempt + 1,
                    ACTION_RETRY_SECONDS))
            {
                CustomerServiceLog(
                    "WorkerDroid",
                    "Worker Droid " + self +
                        " could not queue delivery retry=" +
                        (attempt + 1) + " token=" + token +
                        "; its durable deadline remains armed.");
            }
        }
        else
        {
            // Re-arm the overall deadline at the end of the bounded delivery
            // series so a failed initial deadline enqueue cannot wedge the
            // persistent player lock.
            if (!armPendingActionDeadline(
                    self, token, getPendingActionDeadlineDelay(self)))
            {
                forcePendingActionTimeout(
                    self, token, "final delivery deadline re-enqueue failed");
            }
        }
        return queued;
    }

    public int retryPendingWorkerDroidAction(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !hasValidPendingAction(self) ||
            getIntObjVar(self, ITEM_PENDING_STATE) !=
                PENDING_STATE_DISPATCHED ||
            params.getInt("token") !=
                getIntObjVar(self, ITEM_PENDING_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }
        dispatchPendingAction(self, params.getInt("attempt"));
        return SCRIPT_CONTINUE;
    }

    public int handleWorkerDroidActionAck(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !hasValidPendingAction(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = getObjIdObjVar(self, ITEM_PENDING_PLAYER);
        obj_id target = getObjIdObjVar(self, ITEM_PENDING_TARGET);
        int token = getIntObjVar(self, ITEM_PENDING_TOKEN);
        String action = getStringObjVar(self, ITEM_PENDING_ACTION);
        if (params.getObjId("player") != player ||
            params.getObjId("target") != target ||
            params.getObjId("workerDroid") != self ||
            params.getInt("token") != token ||
            !action.equals(params.getString("action")))
        {
            return SCRIPT_CONTINUE;
        }

        if (getIntObjVar(self, ITEM_PENDING_STATE) ==
            PENDING_STATE_DISPATCHED)
        {
            if (!setObjVar(
                    self,
                    ITEM_PENDING_STATE,
                    PENDING_STATE_ACKNOWLEDGED))
            {
                return SCRIPT_CONTINUE;
            }
        }
        CustomerServiceLog(
            "WorkerDroid",
            "Installation " + target + " acknowledged Worker Droid " +
                self + " action=" + action + " token=" + token +
                " success=" + params.getBoolean("success") + ".");
        recoverTerminalPendingAction(self, token, "target acknowledgement");
        return SCRIPT_CONTINUE;
    }

    public boolean forcePendingActionTimeout(obj_id self, int token,
        String context) throws InterruptedException
    {
        if (!hasValidPendingAction(self) ||
            getIntObjVar(self, ITEM_PENDING_TOKEN) != token)
        {
            return false;
        }

        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        if (state == PENDING_STATE_DISPATCHED)
        {
            if (!setObjVar(
                    self,
                    ITEM_PENDING_STATE,
                    PENDING_STATE_TIMED_OUT))
            {
                if (!armPendingActionDeadline(
                        self, token, ACTION_RETRY_SECONDS))
                {
                    obj_id player = getObjIdObjVar(
                        self, ITEM_PENDING_PLAYER);
                    clearPendingActionFlow(self, player);
                    CustomerServiceLog(
                        "WorkerDroid",
                        "CRITICAL: Worker Droid " + self +
                            " could neither persist nor requeue timeout recovery for token=" +
                            token + " context=" + context +
                            "; its player lock was cleared and its durable item state was preserved for initialization or administrative recovery.");
                }
                return false;
            }
        }
        else if (state != PENDING_STATE_ACKNOWLEDGED &&
            state != PENDING_STATE_TIMED_OUT)
        {
            return false;
        }

        return recoverTerminalPendingAction(self, token, context);
    }

    public boolean recoverTerminalPendingAction(obj_id self, int token,
        String context) throws InterruptedException
    {
        if (!hasValidPendingAction(self) ||
            getIntObjVar(self, ITEM_PENDING_TOKEN) != token)
        {
            return false;
        }
        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        if (state != PENDING_STATE_ACKNOWLEDGED &&
            state != PENDING_STATE_TIMED_OUT)
        {
            return false;
        }
        if (completeTerminalPendingAction(self))
        {
            return true;
        }
        if (exists(self) && !armPendingActionDeadline(
                self, token, ACTION_RETRY_SECONDS))
        {
            obj_id player = getObjIdObjVar(self, ITEM_PENDING_PLAYER);
            clearPendingActionFlow(self, player);
            CustomerServiceLog(
                "WorkerDroid",
                "CRITICAL: Worker Droid " + self +
                    " could not requeue terminal charge reconciliation for token=" +
                    token + " context=" + context +
                    "; its player lock was cleared and its terminal item state was preserved for initialization or administrative recovery.");
        }
        return false;
    }

    public boolean completeTerminalPendingAction(obj_id self)
        throws InterruptedException
    {
        if (!hasValidPendingAction(self))
        {
            return false;
        }

        int state = getIntObjVar(self, ITEM_PENDING_STATE);
        if (state != PENDING_STATE_ACKNOWLEDGED &&
            state != PENDING_STATE_TIMED_OUT)
        {
            return false;
        }

        obj_id player = getObjIdObjVar(self, ITEM_PENDING_PLAYER);
        if (state == PENDING_STATE_TIMED_OUT &&
            !hasObjVar(self, ITEM_PENDING_TIMEOUT_NOTIFIED))
        {
            obj_id target = getObjIdObjVar(self, ITEM_PENDING_TARGET);
            int token = getIntObjVar(self, ITEM_PENDING_TOKEN);
            String action = getStringObjVar(self, ITEM_PENDING_ACTION);
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self + " action=" + action +
                    " token=" + token + " target=" + target +
                    " exhausted its confirmation deadline; consuming the already-dispatched charge and clearing its player lock.");
            if (isValidId(player))
            {
                sendSystemMessage(
                    player,
                    "The installation did not confirm the Worker Droid action. The already-dispatched droid is being consumed and its remote-management lock has been cleared; verify the installation's current state.",
                    null);
            }
            setObjVar(self, ITEM_PENDING_TIMEOUT_NOTIFIED, 1);
        }
        int countBefore = getIntObjVar(
            self, ITEM_PENDING_COUNT_BEFORE);
        int currentCount = getCount(self);
        // The target has terminally acknowledged the exact tuple. Release the
        // cross-stack player lock before a final-charge destroy can remove the
        // only script capable of clearing it.
        clearPendingActionFlow(self, player);
        if (countBefore <= 1)
        {
            // Unstacked tangibles use count 0; an explicit final stack charge
            // uses count 1. Destruction is the decrement for both shapes.
            if (currentCount != countBefore || !destroyObject(self))
            {
                return false;
            }
            return true;
        }

        if (currentCount == countBefore)
        {
            if (!consumable.decrementCharges(self, player))
            {
                return false;
            }
            if (!exists(self))
            {
                return true;
            }
            currentCount = getCount(self);
        }
        if (currentCount != countBefore - 1)
        {
            CustomerServiceLog(
                "WorkerDroid",
                "Worker Droid " + self +
                    " could not finalize an acknowledged charge: before=" +
                    countBefore + " current=" + currentCount + ".");
            return false;
        }

        removeObjVar(self, ITEM_PENDING_ACTION_ROOT);
        return true;
    }

    public int workerDroidLocateTimeout(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !utils.hasScriptVar(self, SCRIPT_VAR_PHASE) ||
            params.getInt("token") != utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN))
        {
            return SCRIPT_CONTINUE;
        }
        String phase = utils.getStringScriptVar(self, SCRIPT_VAR_PHASE);
        if (PHASE_QUERYING.equals(phase))
        {
            return finishWorkerDroidQuery(self, params);
        }
        if (!PHASE_LOCATING.equals(phase))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
        cleanupFlow(self, player);
        if (isValidId(player))
        {
            sendSystemMessage(player, "The Worker Droid found no structures to query. It was not consumed.", null);
        }
        return SCRIPT_CONTINUE;
    }

    public int workerDroidSelectionTimeout(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !utils.hasScriptVar(self, SCRIPT_VAR_PHASE) ||
            params.getInt("token") != utils.getIntScriptVar(self, SCRIPT_VAR_TOKEN) ||
            !utils.getStringScriptVar(self, SCRIPT_VAR_PHASE).equals(params.getString("phase")))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = utils.getObjIdScriptVar(self, SCRIPT_VAR_PLAYER);
        int pageId = params.getInt("pageId");
        if (pageId > 0)
        {
            forceCloseSUIPage(pageId);
        }
        cleanupFlow(self, player);
        if (isValidId(player))
        {
            sendSystemMessage(player, "The Worker Droid request timed out. It was not consumed.", null);
        }
        return SCRIPT_CONTINUE;
    }

    public boolean canLaunch(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(self) || !exists(self) || !isValidId(player) || !isPlayer(player) ||
            isDead(player) || isIncapacitated(player) || utils.getContainingPlayer(self) != player)
        {
            return false;
        }
        location playerLocation = getLocation(player);
        if (playerLocation == null || isValidId(playerLocation.cell))
        {
            sendSystemMessage(player, "A Worker Droid must be launched outdoors.", null);
            return false;
        }
        if (isSpaceScene())
        {
            sendSystemMessage(player, new string_id("mission/mission_generic", "in_space"));
            return false;
        }
        return true;
    }

    public boolean isDiscoveryPhase(obj_id self) throws InterruptedException
    {
        if (!utils.hasScriptVar(self, SCRIPT_VAR_PHASE))
        {
            return false;
        }
        String phase = utils.getStringScriptVar(self, SCRIPT_VAR_PHASE);
        return PHASE_LOCATING.equals(phase) || PHASE_QUERYING.equals(phase);
    }

    public boolean isFlowOwner(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(player) || utils.getContainingPlayer(self) != player ||
            !utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM))
        {
            return false;
        }
        return utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM) == self;
    }

    public obj_id getValidSuiPlayer(obj_id self, dictionary params, String expectedPhase) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !utils.hasScriptVar(self, SCRIPT_VAR_PHASE) ||
            !expectedPhase.equals(utils.getStringScriptVar(self, SCRIPT_VAR_PHASE)))
        {
            return obj_id.NULL_ID;
        }
        obj_id player = sui.getPlayerId(params);
        if (!isFlowOwner(self, player) || !sui.hasPid(player, PID_NAME))
        {
            return obj_id.NULL_ID;
        }
        int pageId = params.getInt("pageId");
        int expectedPageId = sui.getPid(player, PID_NAME);
        if (pageId != expectedPageId)
        {
            if (pageId > 0)
            {
                forceCloseSUIPage(pageId);
            }
            if (expectedPageId > 0)
            {
                forceCloseSUIPage(expectedPageId);
            }
            cleanupFlow(self, player);
            return obj_id.NULL_ID;
        }
        return player;
    }

    public obj_id parseLocateRecord(String record) throws InterruptedException
    {
        if (record == null)
        {
            return obj_id.NULL_ID;
        }
        int comma = record.indexOf(',');
        if (comma <= 0)
        {
            return obj_id.NULL_ID;
        }
        return utils.stringToObjId(record.substring(0, comma).trim());
    }

    public void cleanupFlow(obj_id self, obj_id player) throws InterruptedException
    {
        if (isValidId(player))
        {
            boolean ownsHandoff = false;
            boolean ownsRequestor = false;
            if (utils.hasScriptVar(player, PLAYER_ACTIVE_ITEM) &&
                utils.getObjIdScriptVar(player, PLAYER_ACTIVE_ITEM) == self)
            {
                utils.removeScriptVar(player, PLAYER_ACTIVE_ITEM);
                ownsHandoff = true;
            }
            if (hasObjVar(player, PLAYER_REQUESTOR_OBJVAR) &&
                getObjIdObjVar(player, PLAYER_REQUESTOR_OBJVAR) == self)
            {
                removeObjVar(player, PLAYER_REQUESTOR_OBJVAR);
                ownsHandoff = true;
                ownsRequestor = true;
            }
            if (hasObjVar(player, PLAYER_PENDING_ITEM) &&
                getObjIdObjVar(player, PLAYER_PENDING_ITEM) == self)
            {
                removeObjVar(player, PLAYER_PENDING_ITEM);
                ownsHandoff = true;
            }
            if (ownsRequestor &&
                hasObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR))
            {
                removeObjVar(player, PLAYER_REQUEST_TOKEN_OBJVAR);
            }
            // The PID name is shared by all Worker Droid stacks carried by the
            // character. An unrelated stack can initialize while another
            // droid owns an active flow, so only its owner may clear the page.
            if (ownsHandoff && sui.hasPid(player, PID_NAME))
            {
                sui.removePid(player, PID_NAME);
            }
        }
        if (hasObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR))
        {
            removeObjVar(self, ITEM_REQUEST_TOKEN_OBJVAR);
        }
        utils.removeScriptVarTree(self, SCRIPT_VAR_BASE);
    }

    public void clearStaleHandoff(obj_id self) throws InterruptedException
    {
        obj_id player = utils.getContainingPlayer(self);
        cleanupFlow(self, player);
    }

    public void scheduleSelectionTimeout(obj_id self, int token, String phase, int pageId) throws InterruptedException
    {
        dictionary timeout = new dictionary();
        timeout.put("token", token);
        timeout.put("phase", phase);
        timeout.put("pageId", pageId);
        messageTo(self, "workerDroidSelectionTimeout", timeout, SELECTION_TIMEOUT_SECONDS, false);
    }

    public void launchSeekerVisual(obj_id player) throws InterruptedException
    {
        location spawnLocation = getLocation(player);
        location heading = getHeading(player);
        if (spawnLocation == null || heading == null)
        {
            return;
        }
        spawnLocation.x += heading.x;
        spawnLocation.z += heading.z;
        obj_id seeker = createObject(SEEKER_VISUAL, spawnLocation);
        if (isValidId(seeker))
        {
            messageTo(seeker, "takeOff", null, 5.0f, true);
        }
    }

    public int OnGetAttributes(obj_id self, obj_id player, String[] names, String[] attribs) throws InterruptedException
    {
        int idx = utils.getValidAttributeIndex(names);
        if (idx == -1)
        {
            return SCRIPT_CONTINUE;
        }
        int count = getCount(self);
        if (count > 0)
        {
            names[idx] = "quantity";
            attribs[idx] = Integer.toString(count);
            idx++;
            if (idx >= names.length)
            {
                return SCRIPT_CONTINUE;
            }
        }
        if (hasObjVar(self, craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + ".mechanism_quality"))
        {
            names[idx] = "mechanism_quality";
            int value = (int)getFloatObjVar(
                self,
                craftinglib.COMPONENT_ATTRIBUTE_OBJVAR_NAME + ".mechanism_quality");
            attribs[idx] = Integer.toString(value);
        }
        return SCRIPT_CONTINUE;
    }
}
