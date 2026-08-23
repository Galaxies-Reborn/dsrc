package script.player;

import script.*;
import script.library.mercenary;
import script.library.money;

/** Temporary callback receiver for the asynchronous named-account debit. */
public class mercenary_hire_payment extends script.base_script
{
    private static final int REFUND_RETRIES = 3;

    public mercenary_hire_payment()
    {
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        // Attached player scripts and objvars persist. Resume only from an
        // authenticated durable stage; never infer a second bank operation.
        messageTo(self, "reconcileHireMercTransaction", null, 1.0f, true);
        return SCRIPT_CONTINUE;
    }

    public int reconcileHireMercTransaction(obj_id self, dictionary ignored)
        throws InterruptedException
    {
        if (!hasObjVar(self, mercenary.VAR_TX_NONCE))
        {
            detachScript(self, mercenary.PAYMENT_SCRIPT);
            return SCRIPT_CONTINUE;
        }
        if (!mercenary.validateDurableTransaction(self))
        {
            CustomerServiceLog("hire_merc",
                "Invalid durable hire transaction for player " + self +
                "; operation left locked for reconciliation.");
            return SCRIPT_CONTINUE;
        }

        String state = getStringObjVar(self, mercenary.VAR_TX_STATE);
        if (mercenary.STATE_QUEUED.equals(state))
        {
            // The persistent money callback remains authoritative. Expiring or
            // redispatching this state could create a second debit.
            return SCRIPT_CONTINUE;
        }

        dictionary params = rebuildTransactionEnvelope(self);
        obj_id hired = hasObjVar(self, mercenary.VAR_TX_HIRED) ?
            getObjIdObjVar(self, mercenary.VAR_TX_HIRED) : null;
        if (mercenary.STATE_DEBITED.equals(state))
        {
            mercenary.rollbackHire(self, hired);
            sendSystemMessage(self, mercenary.SID_SPAWN_FAILED);
            queueRefund(self, params,
                getIntObjVar(self, mercenary.VAR_TX_COST), 0);
            return SCRIPT_CONTINUE;
        }

        if (mercenary.STATE_ENROLLING.equals(state))
        {
            if (hasObjVar(self, mercenary.VAR_ACTIVE) &&
                getObjIdObjVar(self, mercenary.VAR_ACTIVE) == hired &&
                mercenary.isEnrollmentComplete(self, hired))
            {
                // Crash after party commit but before ledger cleanup.
                setObjVar(self, mercenary.VAR_TX_STATE,
                    mercenary.STATE_COMPLETED);
                mercenary.clearPendingHire(self);
                detachScript(self, mercenary.PAYMENT_SCRIPT);
                return SCRIPT_CONTINUE;
            }
            params.put("hired", hired);
            params.put("enrollmentAttempt", 0);
            mercenary.repeatGroupInvite(self, hired);
            messageTo(self, "handleHireMercEnrollment", params, 2.5f, true);
            return SCRIPT_CONTINUE;
        }

        if (mercenary.STATE_COMPLETED.equals(state))
        {
            if (hasObjVar(self, mercenary.VAR_ACTIVE) &&
                getObjIdObjVar(self, mercenary.VAR_ACTIVE) == hired &&
                mercenary.isEnrollmentComplete(self, hired))
            {
                mercenary.clearPendingHire(self);
                detachScript(self, mercenary.PAYMENT_SCRIPT);
            }
            else
            {
                mercenary.rollbackHire(self, hired);
                queueRefund(self, params,
                    getIntObjVar(self, mercenary.VAR_TX_COST), 0);
            }
            return SCRIPT_CONTINUE;
        }

        // STATE_REFUNDING means a named-account transfer is already in flight.
        // Its persistent success/failure callback is the only safe authority;
        // redispatching here could double-credit the player.
        return SCRIPT_CONTINUE;
    }

    public int handleHireMercPayment(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!mercenary.validatePaymentCallback(self, params))
        {
            // Never clear a live nonce for an unauthenticated or duplicate
            // callback.  A late callback from an older operation must not
            // cancel a newer operation or create a free mercenary.
            CustomerServiceLog("hire_merc",
                "Rejected payment callback envelope for player " + self + ".");
            return SCRIPT_CONTINUE;
        }

        if (params.getInt(money.DICT_CODE) == money.RET_FAIL)
        {
            sendSystemMessage(self, mercenary.SID_PAYMENT_FAILED);
            mercenary.clearPendingHire(self);
            detachScript(self, mercenary.PAYMENT_SCRIPT);
            return SCRIPT_CONTINUE;
        }

        // Serial script dispatch makes this state transition the idempotency
        // barrier: any duplicate success callback is rejected before spawning.
        setObjVar(self, mercenary.VAR_TX_STATE, mercenary.STATE_DEBITED);
        obj_id terminal = params.getObjId("terminal");
        int archetype = params.getInt("archetype");
        int level = params.getInt("level");
        int cost = params.getInt("cost");
        obj_id hired = mercenary.prepareHire(self, terminal, archetype, level);
        if (!isIdValid(hired))
        {
            sendSystemMessage(self, mercenary.SID_SPAWN_FAILED);
            queueRefund(self, params, cost, 0);
            return SCRIPT_CONTINUE;
        }

        setObjVar(self, mercenary.VAR_TX_HIRED, hired);
        setObjVar(self, mercenary.VAR_TX_STATE, mercenary.STATE_ENROLLING);
        params.put("hired", hired);
        params.put("enrollmentAttempt", 0);
        messageTo(self, "handleHireMercEnrollment", params, 2.5f, true);
        return SCRIPT_CONTINUE;
    }

    public int handleHireMercEnrollment(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isEnrollmentCallback(self, params))
        {
            return SCRIPT_CONTINUE;
        }

        obj_id hired = params.getObjId("hired");
        if (mercenary.finalizeEnrollment(self, hired))
        {
            // Persist completion before clearing the ledger so restart
            // reconciliation can distinguish a delivered contract from one
            // that still requires a refund.
            setObjVar(self, mercenary.VAR_TX_STATE,
                mercenary.STATE_COMPLETED);
            mercenary.clearPendingHire(self);
            detachScript(self, mercenary.PAYMENT_SCRIPT);
            return SCRIPT_CONTINUE;
        }

        int attempt = params.getInt("enrollmentAttempt") + 1;
        if (attempt < mercenary.GROUP_ENROLLMENT_RETRIES &&
            isIdValid(hired) && exists(hired) && getMaster(hired) == self)
        {
            mercenary.repeatGroupInvite(self, hired);
            params.put("enrollmentAttempt", attempt);
            messageTo(self, "handleHireMercEnrollment", params, 2.0f, true);
            return SCRIPT_CONTINUE;
        }

        int cost = getIntObjVar(self, mercenary.VAR_TX_COST);
        mercenary.rollbackHire(self, hired);
        sendSystemMessage(self, mercenary.SID_SPAWN_FAILED);
        queueRefund(self, params, cost, 0);
        return SCRIPT_CONTINUE;
    }

    public int handleHireMercRefunded(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isRefundCallback(self, params))
        {
            return SCRIPT_CONTINUE;
        }
        mercenary.clearPendingHire(self);
        detachScript(self, mercenary.PAYMENT_SCRIPT);
        return SCRIPT_CONTINUE;
    }

    public int handleHireMercRefundFailed(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (isRefundCallback(self, params))
        {
            int attempt = params.getInt("refundAttempt") + 1;
            params.put("refundAttempt", attempt);
            setObjVar(self, mercenary.VAR_TX_REFUND_ATTEMPT, attempt);
            messageTo(self, "retryHireMercRefund", params, 2.0f, true);
        }
        return SCRIPT_CONTINUE;
    }

    public int retryHireMercRefund(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isRefundCallback(self, params))
        {
            return SCRIPT_CONTINUE;
        }
        int attempt = params.getInt("refundAttempt");
        if (attempt >= REFUND_RETRIES)
        {
            int amount = params.getInt("refundCost");
            CustomerServiceLog("hire_merc",
                "Refund retries exhausted for player " + self +
                ", nonce " + params.getInt("refundNonce") +
                ", amount " + amount +
                ". Operation remains locked for reconciliation.");
            sendSystemMessage(self, mercenary.SID_REFUND_FAILED);
            return SCRIPT_CONTINUE;
        }
        queueRefund(self, params, params.getInt("refundCost"), attempt);
        return SCRIPT_CONTINUE;
    }

    private boolean isEnrollmentCallback(obj_id self, dictionary params)
        throws InterruptedException
    {
        return params != null && !params.isEmpty() &&
            params.containsKey("nonce") && params.containsKey("hired") &&
            params.containsKey("enrollmentAttempt") &&
            mercenary.validateDurableTransaction(self) &&
            mercenary.STATE_ENROLLING.equals(
                getStringObjVar(self, mercenary.VAR_TX_STATE)) &&
            params.getInt("nonce") ==
                getIntObjVar(self, mercenary.VAR_TX_NONCE) &&
            params.getObjId("hired") ==
                getObjIdObjVar(self, mercenary.VAR_TX_HIRED);
    }

    private boolean isRefundCallback(obj_id self, dictionary params)
        throws InterruptedException
    {
        return params != null && !params.isEmpty() &&
            params.containsKey("refundPlayer") &&
            params.containsKey("refundNonce") &&
            params.containsKey("refundCost") &&
            params.containsKey("refundAccount") &&
            params.containsKey("refundAttempt") &&
            params.getObjId("refundPlayer") == self &&
            mercenary.ACCOUNT.equals(params.getString("refundAccount")) &&
            mercenary.validateDurableTransaction(self) &&
            mercenary.STATE_REFUNDING.equals(
                getStringObjVar(self, mercenary.VAR_TX_STATE)) &&
            params.getInt("refundNonce") ==
                getIntObjVar(self, mercenary.VAR_TX_NONCE) &&
            params.getInt("refundCost") ==
                getIntObjVar(self, mercenary.VAR_TX_COST) &&
            hasObjVar(self, mercenary.VAR_TX_REFUND_ATTEMPT) &&
            params.getInt("refundAttempt") ==
                getIntObjVar(self, mercenary.VAR_TX_REFUND_ATTEMPT);
    }

    private void queueRefund(obj_id self, dictionary params, int amount, int attempt)
        throws InterruptedException
    {
        setObjVar(self, mercenary.VAR_TX_STATE, mercenary.STATE_REFUNDING);
        setObjVar(self, mercenary.VAR_TX_REFUND_ATTEMPT, attempt);
        params.put("refundPlayer", self);
        params.put("refundNonce",
            getIntObjVar(self, mercenary.VAR_TX_NONCE));
        params.put("refundCost", amount);
        params.put("refundAccount", mercenary.ACCOUNT);
        params.put("refundAttempt", attempt);
        boolean queued = transferBankCreditsFromNamedAccount(
            mercenary.ACCOUNT, self, amount,
            "handleHireMercRefunded", "handleHireMercRefundFailed", params);
        if (!queued)
        {
            params.put("refundAttempt", attempt + 1);
            setObjVar(self, mercenary.VAR_TX_REFUND_ATTEMPT, attempt + 1);
            messageTo(self, "retryHireMercRefund", params, 2.0f, true);
        }
    }

    private dictionary rebuildTransactionEnvelope(obj_id self)
        throws InterruptedException
    {
        dictionary params = new dictionary();
        int cost = getIntObjVar(self, mercenary.VAR_TX_COST);
        params.put("npc", self);
        params.put("terminal",
            getObjIdObjVar(self, mercenary.VAR_TX_TERMINAL));
        params.put("archetype",
            getIntObjVar(self, mercenary.VAR_TX_ARCHETYPE));
        params.put("level", getIntObjVar(self, mercenary.VAR_TX_LEVEL));
        params.put("cost", cost);
        params.put("nonce", getIntObjVar(self, mercenary.VAR_TX_NONCE));
        params.put(money.DICT_PLAYER_ID, self);
        params.put(money.DICT_TARGET_ID, obj_id.NULL_ID);
        params.put(money.DICT_ACCT_NAME, mercenary.ACCOUNT);
        params.put(money.DICT_AMOUNT, cost);
        params.put(money.DICT_TOTAL, cost);
        return params;
    }
}
