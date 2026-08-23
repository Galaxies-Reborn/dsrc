package script.structure;

import script.*;
import script.library.player_structure;
import script.library.utils;

public class worker_droid_target extends script.base_script
{
    public static final String GRANT_ROOT = "precu.workerDroid.grant";
    public static final String GRANT_PLAYER_SUFFIX = ".player";
    public static final String GRANT_DROID_SUFFIX = ".droid";
    public static final String GRANT_TOKEN_SUFFIX = ".token";
    public static final String GRANT_EXPIRES_SUFFIX = ".expires";
    public static final String GRANT_ACTION_SUFFIX = ".action";
    public static final String GRANT_STATE_SUFFIX = ".state";
    public static final String GRANT_SUCCESS_SUFFIX = ".success";
    public static final String GRANT_MESSAGE_SUFFIX = ".message";
    public static final String GRANT_COMPLETED_AT_SUFFIX = ".completedAt";
    public static final int GRANT_STATE_PENDING = 1;
    public static final int GRANT_STATE_COMPLETED = 2;
    public static final float GRANT_TIMEOUT_SECONDS = 25.0f * 60.0f;
    public static final float ACTION_ACK_DELAY_SECONDS = 2.0f;
    public static final String RESULT_ROOT = "precu.workerDroid.result";
    public static final String RESULT_PLAYER = RESULT_ROOT + ".player";
    public static final String RESULT_DROID = RESULT_ROOT + ".droid";
    public static final String RESULT_TOKEN = RESULT_ROOT + ".token";
    public static final String RESULT_ACTION = RESULT_ROOT + ".action";
    public static final String RESULT_SUCCESS = RESULT_ROOT + ".success";
    public static final String RESULT_TIME = RESULT_ROOT + ".time";

    public worker_droid_target()
    {
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        clearExpiredWorkerDroidGrants(self);
        return SCRIPT_CONTINUE;
    }

    public int workerDroidQuery(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        obj_id workerDroid = params.getObjId("workerDroid");
        int token = params.getInt("token");
        if (!isValidId(workerDroid) || token <= 0 || !isEligibleTarget(self, player))
        {
            return SCRIPT_CONTINUE;
        }

        clearExpiredWorkerDroidGrants(self);
        String grantRoot = getGrantRoot(token);
        if (!grantMatches(self, player, workerDroid, token))
        {
            utils.removeScriptVarTree(self, RESULT_ROOT);
            removeObjVar(self, grantRoot);
            boolean grantStored = setObjVar(self, grantRoot + GRANT_PLAYER_SUFFIX, player);
            grantStored = setObjVar(
                self, grantRoot + GRANT_DROID_SUFFIX, workerDroid) &&
                grantStored;
            grantStored = setObjVar(
                self, grantRoot + GRANT_TOKEN_SUFFIX, token) &&
                grantStored;
            grantStored = setObjVar(
                self,
                grantRoot + GRANT_EXPIRES_SUFFIX,
                getCalendarTime() + (int)GRANT_TIMEOUT_SECONDS) &&
                grantStored;
            if (!grantStored ||
                !grantMatches(self, player, workerDroid, token))
            {
                removeObjVar(self, grantRoot);
                return SCRIPT_CONTINUE;
            }
            dictionary grantTimeout = new dictionary();
            grantTimeout.put("player", player);
            grantTimeout.put("workerDroid", workerDroid);
            grantTimeout.put("token", token);
            messageTo(
                self,
                "clearWorkerDroidGrant",
                grantTimeout,
                GRANT_TIMEOUT_SECONDS,
                true);
        }

        dictionary response = new dictionary();
        response.put("player", player);
        response.put("target", self);
        response.put("token", params.getInt("token"));
        response.put("label", buildTargetLabel(self));
        messageTo(workerDroid, "handleWorkerDroidQueryResponse", response, 0.0f, true);
        return SCRIPT_CONTINUE;
    }

    public int workerDroidAction(obj_id self, dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        obj_id workerDroid = params.getObjId("workerDroid");
        int token = params.getInt("token");
        String action = params.getString("action");
        if (action == null)
        {
            action = "";
        }
        if (!isValidId(player) || isIdNull(workerDroid) || token <= 0)
        {
            sendTerminalActionResult(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The Worker Droid sent an invalid remote-action request.",
                false);
            return SCRIPT_CONTINUE;
        }

        if (!grantMatches(self, player, workerDroid, token))
        {
            sendTerminalActionResult(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The Worker Droid authorization expired or did not match this installation.",
                false);
            return SCRIPT_CONTINUE;
        }

        String grantRoot = getGrantRoot(token);
        if (hasCompletedGrantAction(self, grantRoot))
        {
            String storedAction = getStringObjVar(
                self, grantRoot + GRANT_ACTION_SUFFIX);
            if (storedAction.equals(action))
            {
                replayCompletedGrantAction(
                    self, player, workerDroid, token, storedAction);
            }
            else
            {
                sendTerminalActionResult(
                    self,
                    player,
                    workerDroid,
                    token,
                    action,
                    false,
                    "The Worker Droid nonce was already completed with a different action.",
                    false);
            }
            return SCRIPT_CONTINUE;
        }
        if (hasObjVar(self, grantRoot + GRANT_ACTION_SUFFIX) &&
            !action.equals(getStringObjVar(
                self, grantRoot + GRANT_ACTION_SUFFIX)))
        {
            sendTerminalActionResult(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The Worker Droid nonce is already processing a different action.",
                false);
            return SCRIPT_CONTINUE;
        }
        if (!persistPendingGrantAction(self, grantRoot, action))
        {
            sendTerminalActionResult(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The installation could not preserve the Worker Droid action state.",
                false);
            return SCRIPT_CONTINUE;
        }

        if (!isEligibleTarget(self, player))
        {
            completeGrantAction(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "Worker Droid access was denied because you no longer own an eligible installation.");
            return SCRIPT_CONTINUE;
        }
        if (!"activate".equals(action) && !"deactivate".equals(action))
        {
            completeGrantAction(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The Worker Droid rejected an invalid remote action.");
            return SCRIPT_CONTINUE;
        }

        String targetName = getTargetName(self);
        boolean wasActive = isHarvesterActive(self);
        boolean success;
        String result;
        if ("activate".equals(action))
        {
            if (wasActive)
            {
                success = true;
                result = targetName + " is already active.";
            }
            else
            {
                success = activate(self) && isHarvesterActive(self);
                result = success
                    ? targetName + " was activated by the Worker Droid."
                    : targetName + " could not be activated. Check its power, maintenance, resource, and factory schematic requirements.";
            }
        }
        else
        {
            if (!wasActive)
            {
                success = true;
                result = targetName + " is already inactive.";
            }
            else
            {
                success = deactivate(self) && !isHarvesterActive(self);
                result = success
                    ? targetName + " was deactivated by the Worker Droid."
                    : targetName + " could not be deactivated.";
            }
        }

        CustomerServiceLog(
            "WorkerDroid",
            "Worker Droid action for owner " + player + " on installation " + self +
                ": action=" + action + ", success=" + success + ".");
        completeGrantAction(
            self,
            player,
            workerDroid,
            token,
            action,
            success,
            result);
        return SCRIPT_CONTINUE;
    }

    public boolean persistPendingGrantAction(obj_id self, String grantRoot,
        String action) throws InterruptedException
    {
        if (hasObjVar(self, grantRoot + GRANT_ACTION_SUFFIX) &&
            !action.equals(getStringObjVar(
                self, grantRoot + GRANT_ACTION_SUFFIX)))
        {
            return false;
        }
        if (!setObjVar(self, grantRoot + GRANT_ACTION_SUFFIX, action))
        {
            return false;
        }
        // The state is written last. A crash before this write leaves a grant
        // that a retry can safely stage again.
        if (!setObjVar(
                self,
                grantRoot + GRANT_STATE_SUFFIX,
                GRANT_STATE_PENDING))
        {
            return false;
        }
        return hasObjVar(self, grantRoot + GRANT_ACTION_SUFFIX) &&
            hasObjVar(self, grantRoot + GRANT_STATE_SUFFIX) &&
            action.equals(getStringObjVar(
                self, grantRoot + GRANT_ACTION_SUFFIX)) &&
            getIntObjVar(self, grantRoot + GRANT_STATE_SUFFIX) ==
                GRANT_STATE_PENDING;
    }

    public boolean hasCompletedGrantAction(obj_id self, String grantRoot)
        throws InterruptedException
    {
        return hasObjVar(self, grantRoot + GRANT_ACTION_SUFFIX) &&
            hasObjVar(self, grantRoot + GRANT_STATE_SUFFIX) &&
            hasObjVar(self, grantRoot + GRANT_SUCCESS_SUFFIX) &&
            hasObjVar(self, grantRoot + GRANT_MESSAGE_SUFFIX) &&
            getIntObjVar(self, grantRoot + GRANT_STATE_SUFFIX) ==
                GRANT_STATE_COMPLETED;
    }

    public void completeGrantAction(obj_id self, obj_id player,
        obj_id workerDroid, int token, String action, boolean success,
        String message) throws InterruptedException
    {
        String grantRoot = getGrantRoot(token);
        boolean stored = true;
        stored &= setObjVar(
            self, grantRoot + GRANT_SUCCESS_SUFFIX, success ? 1 : 0);
        stored &= setObjVar(
            self, grantRoot + GRANT_MESSAGE_SUFFIX, message);
        stored &= setObjVar(
            self,
            grantRoot + GRANT_COMPLETED_AT_SUFFIX,
            getCalendarTime());
        // Completion is committed last. If this write is lost, a duplicate
        // request repeats only the desired-state activate/deactivate action.
        stored &= setObjVar(
            self,
            grantRoot + GRANT_STATE_SUFFIX,
            GRANT_STATE_COMPLETED);
        if (!stored || !hasCompletedGrantAction(self, grantRoot))
        {
            CustomerServiceLog(
                "WorkerDroid",
                "Installation " + self +
                    " could not commit Worker Droid result token=" + token +
                    ". The droid will retry.");
            return;
        }
        recordActionResult(
            self, player, workerDroid, token, action, success);
        sendActionResult(player, self, success, message);
        queueStoredActionAck(
            self, player, workerDroid, token, action);
    }

    public void replayCompletedGrantAction(obj_id self, obj_id player,
        obj_id workerDroid, int token, String action)
        throws InterruptedException
    {
        String grantRoot = getGrantRoot(token);
        boolean success = getIntObjVar(
            self, grantRoot + GRANT_SUCCESS_SUFFIX) == 1;
        String message = getStringObjVar(
            self, grantRoot + GRANT_MESSAGE_SUFFIX);
        recordActionResult(
            self, player, workerDroid, token, action, success);
        sendActionResult(player, self, success, message);
        queueStoredActionAck(
            self, player, workerDroid, token, action);
    }

    public void recordActionResult(obj_id self, obj_id player, obj_id workerDroid,
        int token, String action, boolean success) throws InterruptedException
    {
        utils.setScriptVar(self, RESULT_PLAYER, player);
        utils.setScriptVar(self, RESULT_DROID, workerDroid);
        utils.setScriptVar(self, RESULT_TOKEN, token);
        utils.setScriptVar(self, RESULT_ACTION, action == null ? "" : action);
        utils.setScriptVar(self, RESULT_SUCCESS, success ? 1 : 0);
        utils.setScriptVar(self, RESULT_TIME, getGameTime());
    }

    public void sendTerminalActionResult(obj_id self, obj_id player,
        obj_id workerDroid, int token, String action, boolean success,
        String message, boolean storedResult) throws InterruptedException
    {
        recordActionResult(
            self, player, workerDroid, token, action, success);
        sendActionResult(player, self, success, message);
        if (storedResult)
        {
            queueStoredActionAck(
                self, player, workerDroid, token, action);
        }
        else
        {
            sendActionAck(
                self,
                player,
                workerDroid,
                token,
                action,
                success,
                message);
        }
    }

    public void queueStoredActionAck(obj_id self, obj_id player,
        obj_id workerDroid, int token, String action)
        throws InterruptedException
    {
        dictionary pendingAck = new dictionary();
        pendingAck.put("player", player);
        pendingAck.put("workerDroid", workerDroid);
        pendingAck.put("token", token);
        pendingAck.put("action", action);
        messageTo(
            self,
            "sendStoredWorkerDroidActionAck",
            pendingAck,
            ACTION_ACK_DELAY_SECONDS,
            true);
    }

    public int sendStoredWorkerDroidActionAck(obj_id self,
        dictionary params) throws InterruptedException
    {
        if (params == null || params.isEmpty())
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        obj_id workerDroid = params.getObjId("workerDroid");
        int token = params.getInt("token");
        String action = params.getString("action");
        String grantRoot = getGrantRoot(token);
        if (!grantMatches(self, player, workerDroid, token) ||
            !hasCompletedGrantAction(self, grantRoot) || action == null ||
            !action.equals(getStringObjVar(
                self, grantRoot + GRANT_ACTION_SUFFIX)))
        {
            sendActionAck(
                self,
                player,
                workerDroid,
                token,
                action,
                false,
                "The installation could not recover the completed Worker Droid action result.");
            return SCRIPT_CONTINUE;
        }
        boolean success = getIntObjVar(
            self, grantRoot + GRANT_SUCCESS_SUFFIX) == 1;
        String message = getStringObjVar(
            self, grantRoot + GRANT_MESSAGE_SUFFIX);
        sendActionAck(
            self,
            player,
            workerDroid,
            token,
            action,
            success,
            message);
        return SCRIPT_CONTINUE;
    }

    public void sendActionAck(obj_id self, obj_id player,
        obj_id workerDroid, int token, String action, boolean success,
        String message) throws InterruptedException
    {
        if (isIdNull(workerDroid))
        {
            return;
        }
        dictionary ack = new dictionary();
        ack.put("player", player);
        ack.put("target", self);
        ack.put("workerDroid", workerDroid);
        ack.put("token", token);
        ack.put("action", action == null ? "" : action);
        ack.put("success", success);
        ack.put("message", message == null ? "" : message);
        messageTo(
            workerDroid,
            "handleWorkerDroidActionAck",
            ack,
            ACTION_ACK_DELAY_SECONDS,
            true);
    }

    public int clearWorkerDroidGrant(obj_id self, dictionary params) throws InterruptedException
    {
        if (params != null && !params.isEmpty() && grantMatches(
            self,
            params.getObjId("player"),
            params.getObjId("workerDroid"),
            params.getInt("token")))
        {
            removeObjVar(self, getGrantRoot(params.getInt("token")));
        }
        return SCRIPT_CONTINUE;
    }

    public boolean consumeGrant(obj_id self, obj_id player, obj_id workerDroid, int token) throws InterruptedException
    {
        // Kept as a compatibility alias for callers compiled against the
        // original API. The nonce grant now remains until its timeout so a
        // desired-state action can be replayed after a crash or lost ACK.
        return grantMatches(self, player, workerDroid, token);
    }

    public boolean grantMatches(obj_id self, obj_id player, obj_id workerDroid, int token) throws InterruptedException
    {
        String grantRoot = getGrantRoot(token);
        if (token <= 0 || !isValidId(player) || isIdNull(workerDroid) ||
            !hasObjVar(self, grantRoot + GRANT_PLAYER_SUFFIX) ||
            !hasObjVar(self, grantRoot + GRANT_DROID_SUFFIX) ||
            !hasObjVar(self, grantRoot + GRANT_TOKEN_SUFFIX) ||
            !hasObjVar(self, grantRoot + GRANT_EXPIRES_SUFFIX))
        {
            return false;
        }
        if (getIntObjVar(self, grantRoot + GRANT_EXPIRES_SUFFIX) <
            getCalendarTime())
        {
            removeObjVar(self, grantRoot);
            return false;
        }
        return getObjIdObjVar(self, grantRoot + GRANT_PLAYER_SUFFIX) == player &&
            getObjIdObjVar(self, grantRoot + GRANT_DROID_SUFFIX) == workerDroid &&
            getIntObjVar(self, grantRoot + GRANT_TOKEN_SUFFIX) == token;
    }

    public static String getGrantRoot(int token) throws InterruptedException
    {
        return GRANT_ROOT + "." + token;
    }

    public void clearExpiredWorkerDroidGrants(obj_id self) throws InterruptedException
    {
        obj_var_list grants = getObjVarList(self, GRANT_ROOT);
        if (grants == null || grants.getNumItems() == 0)
        {
            return;
        }
        int now = getCalendarTime();
        for (int i = 0; i < grants.getNumItems(); ++i)
        {
            obj_var grant = grants.getObjVar(i);
            if (!(grant instanceof obj_var_list))
            {
                continue;
            }
            String grantRoot = GRANT_ROOT + "." + grant.getName();
            if (!hasObjVar(self, grantRoot + GRANT_EXPIRES_SUFFIX) ||
                getIntObjVar(self, grantRoot + GRANT_EXPIRES_SUFFIX) < now)
            {
                removeObjVar(self, grantRoot);
            }
        }
    }

    public boolean isEligibleTarget(obj_id self, obj_id player) throws InterruptedException
    {
        if (!isValidId(self) || !isValidId(player) ||
            !isSupportedInstallation(self))
        {
            return false;
        }
        if (player_structure.getStructureOwnerObjId(self) != player)
        {
            return false;
        }
        return !player_structure.isStructureCondemned(self) &&
            !player_structure.isPreAbandoned(self) &&
            !player_structure.isAbandoned(self);
    }

    public String buildTargetLabel(obj_id self) throws InterruptedException
    {
        String type = getTargetType(self);
        String state = isHarvesterActive(self) ? "ACTIVE" : "INACTIVE";
        location targetLocation = getLocation(self);
        if (targetLocation == null)
        {
            return type + ": " + getTargetName(self) + " - " + state;
        }
        return type + ": " + getTargetName(self) + " - " + targetLocation.area + " (" +
            (int)targetLocation.x + ", " + (int)targetLocation.z + ") - " + state;
    }

    public String getTargetName(obj_id self) throws InterruptedException
    {
        String targetName = player_structure.getStructureName(self);
        if (targetName == null || targetName.length() == 0)
        {
            targetName = getTargetType(self);
        }
        return targetName;
    }

    public boolean isSupportedInstallation(obj_id self) throws InterruptedException
    {
        return player_structure.isFactory(self) ||
            player_structure.isHarvester(self) ||
            player_structure.isGenerator(self);
    }

    public String getTargetType(obj_id self) throws InterruptedException
    {
        if (player_structure.isFactory(self))
        {
            return "Factory";
        }
        if (player_structure.isGenerator(self))
        {
            return "Generator";
        }
        return "Harvester";
    }

    public void sendActionResult(obj_id player, obj_id target, boolean success, String message) throws InterruptedException
    {
        if (!isValidId(player))
        {
            return;
        }
        dictionary response = new dictionary();
        response.put("player", player);
        response.put("target", target);
        response.put("success", success);
        response.put("message", message);
        messageTo(player, "handleWorkerDroidActionResponse", response, 0.0f, true);
    }
}
