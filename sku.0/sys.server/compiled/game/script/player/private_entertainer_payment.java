package script.player;

import script.*;
import script.library.money;
import script.library.private_entertainer;

/** Persistent exactly-once coordinator for the 10,000-credit paid buff. */
public class private_entertainer_payment extends script.base_script
{
    private static final int REFUND_RETRY_SECONDS = 5;
    private static final int MAX_REFUND_ATTEMPTS = 3;

    public int OnAttach(obj_id self) throws InterruptedException
    {
        schedulePendingTimeout(self);
        return SCRIPT_CONTINUE;
    }

    public int OnInitialize(obj_id self) throws InterruptedException
    {
        if (!hasObjVar(self, private_entertainer.PAYMENT_STATE))
        {
            detachScript(self, private_entertainer.SCRIPT_PAYMENT);
            return SCRIPT_CONTINUE;
        }
        String state = getStringObjVar(
            self, private_entertainer.PAYMENT_STATE);
        if (private_entertainer.PAYMENT_PENDING.equals(state))
        {
            schedulePendingTimeout(self);
        }
        else if (private_entertainer.PAYMENT_SETTLING.equals(state))
        {
            // A success callback was accepted before restart, but completion
            // was not durably recorded. Favor a refund over a double debit.
            beginRefundAndRetryIfNeeded(self, false);
        }
        else if (private_entertainer.PAYMENT_REFUND_FAILED.equals(state))
        {
            scheduleRefundRetry(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerPayment(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        private_entertainer.completePaidBuff(self, params);
        if (hasObjVar(self, private_entertainer.PAYMENT_STATE) &&
            private_entertainer.PAYMENT_REFUND_FAILED.equals(
                getStringObjVar(
                    self, private_entertainer.PAYMENT_STATE)))
        {
            scheduleRefundRetry(self);
        }
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerRefundSuccess(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (params != null)
        {
            params.put(money.DICT_CODE, money.RET_SUCCESS);
        }
        finishRefundCallback(self, params);
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerRefundFailure(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (params != null)
        {
            params.put(money.DICT_CODE, money.RET_FAIL);
        }
        finishRefundCallback(self, params);
        return SCRIPT_CONTINUE;
    }

    private void finishRefundCallback(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (!private_entertainer.completePaymentRefund(self, params) &&
            hasObjVar(self, private_entertainer.PAYMENT_STATE) &&
            private_entertainer.PAYMENT_REFUND_FAILED.equals(
                getStringObjVar(
                    self, private_entertainer.PAYMENT_STATE)))
        {
            scheduleRefundRetry(self);
        }
    }

    public int handlePrivateEntertainerPaymentTimeout(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (!hasExactNonce(self, params, "private_payment_nonce") ||
            !hasObjVar(self, private_entertainer.PAYMENT_STARTED))
        {
            return SCRIPT_CONTINUE;
        }
        int started = getIntObjVar(
            self, private_entertainer.PAYMENT_STARTED);
        if (getCalendarTime() - started <
            private_entertainer.PAYMENT_TIMEOUT_SECONDS)
        {
            schedulePendingTimeout(self);
            return SCRIPT_CONTINUE;
        }
        private_entertainer.markPaymentTimedOut(
            self,
            getStringObjVar(self, private_entertainer.PAYMENT_NONCE));
        return SCRIPT_CONTINUE;
    }

    public int handlePrivateEntertainerRefundRetry(
        obj_id self,
        dictionary params) throws InterruptedException
    {
        if (!hasExactNonce(self, params, "private_refund_nonce") ||
            !hasObjVar(self, private_entertainer.PAYMENT_STATE) ||
            !private_entertainer.PAYMENT_REFUND_FAILED.equals(
                getStringObjVar(
                    self, private_entertainer.PAYMENT_STATE)))
        {
            return SCRIPT_CONTINUE;
        }
        int attempts = hasObjVar(
            self, private_entertainer.PAYMENT_REFUND_ATTEMPTS) ?
            getIntObjVar(
                self, private_entertainer.PAYMENT_REFUND_ATTEMPTS) : 0;
        if (attempts >= MAX_REFUND_ATTEMPTS)
        {
            CustomerServiceLog(
                "PrivateEntertainer",
                "Refund retry limit reached for player " + self +
                    ", nonce " +
                    getStringObjVar(
                        self, private_entertainer.PAYMENT_NONCE) + ".");
            return SCRIPT_CONTINUE;
        }
        boolean late = hasObjVar(
            self, private_entertainer.PAYMENT_LATE_REFUND) &&
            getBooleanObjVar(
                self, private_entertainer.PAYMENT_LATE_REFUND);
        beginRefundAndRetryIfNeeded(self, late);
        return SCRIPT_CONTINUE;
    }

    private void beginRefundAndRetryIfNeeded(
        obj_id self,
        boolean late) throws InterruptedException
    {
        private_entertainer.beginPaymentRefund(self, late);
        if (hasObjVar(self, private_entertainer.PAYMENT_STATE) &&
            private_entertainer.PAYMENT_REFUND_FAILED.equals(
                getStringObjVar(
                    self, private_entertainer.PAYMENT_STATE)))
        {
            // Native no-dispatch failures do not produce a callback, so the
            // coordinator must explicitly arm the next bounded retry.
            scheduleRefundRetry(self);
        }
    }

    private void schedulePendingTimeout(obj_id self)
        throws InterruptedException
    {
        if (!hasObjVar(self, private_entertainer.PAYMENT_STATE) ||
            !private_entertainer.PAYMENT_PENDING.equals(
                getStringObjVar(
                    self, private_entertainer.PAYMENT_STATE)) ||
            !hasObjVar(self, private_entertainer.PAYMENT_NONCE) ||
            !hasObjVar(self, private_entertainer.PAYMENT_STARTED))
        {
            return;
        }
        int elapsed = Math.max(
            0,
            getCalendarTime() - getIntObjVar(
                self, private_entertainer.PAYMENT_STARTED));
        int delay = Math.max(
            1,
            private_entertainer.PAYMENT_TIMEOUT_SECONDS - elapsed);
        dictionary timeout = new dictionary();
        timeout.put(
            "private_payment_nonce",
            getStringObjVar(self, private_entertainer.PAYMENT_NONCE));
        messageTo(
            self,
            "handlePrivateEntertainerPaymentTimeout",
            timeout,
            delay,
            true);
    }

    private void scheduleRefundRetry(obj_id self)
        throws InterruptedException
    {
        if (!hasObjVar(self, private_entertainer.PAYMENT_NONCE))
        {
            return;
        }
        dictionary retry = new dictionary();
        retry.put(
            "private_refund_nonce",
            getStringObjVar(self, private_entertainer.PAYMENT_NONCE));
        messageTo(
            self,
            "handlePrivateEntertainerRefundRetry",
            retry,
            REFUND_RETRY_SECONDS,
            true);
    }

    private boolean hasExactNonce(
        obj_id self,
        dictionary params,
        String key) throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !params.containsKey(key) ||
            !hasObjVar(self, private_entertainer.PAYMENT_NONCE))
        {
            return false;
        }
        String actual = params.getString(key);
        String expected = getStringObjVar(
            self, private_entertainer.PAYMENT_NONCE);
        return expected != null && expected.equals(actual);
    }
}
