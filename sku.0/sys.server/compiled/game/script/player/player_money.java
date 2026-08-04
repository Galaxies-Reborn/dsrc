package script.player;

import script.dictionary;
import script.library.money;
import script.library.skill;
import script.library.prose;
import script.library.sui;
import script.library.utils;
import script.obj_id;
import script.prose_package;
import script.string_id;

public class player_money extends script.base_script
{
    private static final String PRECU_OP_ROOT = "precu.phaseA.operation";
    private static final String PRECU_OP_ATTEMPT_ID = PRECU_OP_ROOT + ".attemptId";
    private static final String PRECU_OP_ID = PRECU_OP_ROOT + ".id";
    private static final String PRECU_OP_KIND = PRECU_OP_ROOT + ".kind";
    private static final String PRECU_OP_STATE = PRECU_OP_ROOT + ".state";
    private static final String PRECU_OP_UPDATED = PRECU_OP_ROOT + ".updated";
    private static final String PRECU_OP_LIFECYCLE_ID = PRECU_OP_ROOT + ".lifecycleId";
    private static final String PRECU_OP_COST = PRECU_OP_ROOT + ".cost";
    private static final String PRECU_OP_TRAINER_OID = PRECU_OP_ROOT + ".trainerOid";
    private static final String PRECU_OP_SKILL_NAME = PRECU_OP_ROOT + ".skillName";
    private static final String PRECU_OP_PRE_CREDITS = PRECU_OP_ROOT + ".preCredits";
    private static final String PRECU_OP_PRE_CASH = PRECU_OP_ROOT + ".preCash";
    private static final String PRECU_OP_PRE_BANK = PRECU_OP_ROOT + ".preBank";
    private static final String PRECU_OP_PRE_XP = PRECU_OP_ROOT + ".preXp";
    private static final String PRECU_OP_PRE_POINTS = PRECU_OP_ROOT + ".prePoints";
    private static final String PRECU_OP_PRE_CAP = PRECU_OP_ROOT + ".preCap";
    private static final String PRECU_OP_PRE_NOVICE = PRECU_OP_ROOT + ".preNovice";
    private static final String PRECU_OP_PRE_SKILL = PRECU_OP_ROOT + ".preSkill";
    private static final String PRECU_OP_PROTOCOL_VERSION = PRECU_OP_ROOT + ".protocolVersion";
    private static final String PRECU_OP_REFUND_GENERATION =
        PRECU_OP_ROOT + ".refundGeneration";
    private static final String PRECU_OP_REFUND_ATTEMPT_KEY =
        PRECU_OP_ROOT + ".refundAttemptKey";
    private static final String PRECU_OP_REFUND_RETRY_CONSUMED =
        PRECU_OP_ROOT + ".refundRetryConsumed";
    private static final String PRECU_OP_ACCOUNTING_ATTEMPT_KEY =
        PRECU_OP_ROOT + ".accountingAttemptKey";
    private static final String PRECU_OP_ACCOUNTING_ACCOUNT =
        PRECU_OP_ROOT + ".accountingAccount";
    private static final String PRECU_OP_ACCOUNTING_OUTCOME =
        PRECU_OP_ROOT + ".accountingOutcome";
    private static final String PRECU_PARAM_ID = "precuPhaseAOperationId";
    private static final String PRECU_PARAM_KIND = "precuPhaseAOperationKind";
    private static final String PRECU_REFUND_PARAM_GENERATION =
        "precuPhaseARefundGeneration";
    private static final String PRECU_REFUND_PARAM_ATTEMPT_KEY =
        "precuPhaseARefundAttemptKey";
    private static final String PRECU_REFUND_PARAM_RETRY_CONSUMED =
        "precuPhaseARefundRetryConsumed";
    private static final String PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY =
        "precuPhaseAAccountingAttemptKey";
    private static final String PRECU_PROTOCOL_PARAM_VERSION =
        "precuPhaseAProtocolVersion";
    private static final String PRECU_LIFECYCLE_ATTEMPT_ID = "precu.phaseA.lifecycle.attemptId";
    private static final String PRECU_LIFECYCLE_ID = "precu.phaseA.lifecycle.id";
    private static final String PRECU_LIFECYCLE_STATE = "precu.phaseA.lifecycle.state";
    private static final String PRECU_LIFECYCLE_BASE_CASH = "precu.phaseA.lifecycle.baseCash";
    private static final String PRECU_LIFECYCLE_BASE_BANK = "precu.phaseA.lifecycle.baseBank";
    private static final String PRECU_LIFECYCLE_BASE_XP = "precu.phaseA.lifecycle.baseXp";
    private static final String PRECU_LIFECYCLE_BASE_POINTS = "precu.phaseA.lifecycle.basePoints";
    private static final String PRECU_LIFECYCLE_BASE_CAP = "precu.phaseA.lifecycle.baseCap";
    private static final String PRECU_LIFECYCLE_BASE_NOVICE = "precu.phaseA.lifecycle.baseNovice";
    private static final String PRECU_LIFECYCLE_BASE_SKILL = "precu.phaseA.lifecycle.baseSkill";
    private static final String PRECU_LIFECYCLE_PARAM_ID = "precuPhaseALifecycleId";
    private static final String PRECU_RELOG_NONCE = "precu.phaseA.relogNonce";
    private static final String PRECU_RESTART_NONCE = "precu.phaseA.restartNonce";
    private static final String PRECU_CRAFTING_XP_TYPE = "crafting_general";
    private static final String PRECU_CRAFTING_NOVICE_SKILL = "crafting_artisan_novice";
    private static final String PRECU_CRAFTING_SKILL = "crafting_artisan_engineering_01";
    private static final int PRECU_CRAFTING_TRAINER_COST = 1000;
    private static final int PRECU_CRAFTING_XP_COST = 500;
    private static final int PRECU_PREPURCHASE_XP_CAP = 1500;
    private static final int PRECU_TRAINED_XP_CAP = 2000;
    private static final int PRECU_CRAFTING_SCHEMATIC_COUNT = 35;
    private static final int PRECU_PROTOCOL_VERSION = 64;
    private static final String PRECU_ACCOUNTING_OUTCOME_NONE = "none";
    private static final String PRECU_ACCOUNTING_OUTCOME_SUCCESS = "SUCCESS";
    private static final String PRECU_ACCOUNTING_OUTCOME_QUEUE_FAILED = "QUEUE_FAILED";
    private static final String PRECU_ACCOUNTING_OUTCOME_FAILED = "FAILED";
    private static final String PRECU_VECTOR_PRE = "PRE";
    private static final String PRECU_VECTOR_DEBIT = "DEBIT";
    private static final String PRECU_VECTOR_HELD = "HELD";
    private static final String PRECU_SCHEMATIC_GROUP_TABLE =
        "datatables/crafting/schematic_group.iff";
    private static final String[] PRECU_CRAFTING_VECTOR_COMMANDS =
    {
        "private_artisan_novice",
        "private_artisan_engineering_1"
    };
    private static final String[] PRECU_CRAFTING_VECTOR_MODS =
    {
        "surveying",
        "general_assembly",
        "general_experimentation",
        "clothing_customization",
        "armor_customization",
        "slope_move"
    };
    private static final int[] PRECU_CRAFTING_VECTOR_MOD_VALUES =
    {
        20,
        30,
        30,
        20,
        40,
        25
    };
    private static final int[] PRECU_CRAFTING_PURCHASE_MOD_DELTAS =
    {
        0,
        10,
        10,
        0,
        20,
        0
    };
    private static final String[] PRECU_CRAFTING_VECTOR_SCHEMATIC_GROUPS =
    {
        "craftArtisanNewbieGroupA",
        "craftArtisanNewbieGroupB",
        "craftArtisanSurveyGroupA",
        "craftArtisanEngineeringGroupA",
        "craftArtisanToolGroupA"
    };
    private static final String[] PRECU_CRAFTING_PURCHASE_SCHEMATICS =
    {
        "object/draft_schematic/item/craftable_bug_habitat.iff",
        "object/draft_schematic/item/item_battery_droid.iff",
        "object/draft_schematic/item/item_clothing_tool.iff",
        "object/draft_schematic/item/item_firework_five.iff",
        "object/draft_schematic/item/item_firework_four.iff",
        "object/draft_schematic/item/item_hundred_sided_dice.iff",
        "object/draft_schematic/item/item_space_tool.iff",
        "object/draft_schematic/item/item_structure_tool.iff",
        "object/draft_schematic/item/item_twelve_sided_dice.iff",
        "object/draft_schematic/item/item_twenty_sided_dice.iff",
        "object/draft_schematic/item/item_weapon_tool.iff"
    };
    private static final String PRECU_BUILD_FINGERPRINT = "__PHASE_A_BUILD_FINGERPRINT__";
    public static String getPrecuPhaseABuildFingerprint()
    {
        return PRECU_BUILD_FINGERPRINT;
    }
    public player_money()
    {
    }
    public int handlePayDeposit(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        // The canary is funded entirely into bank. A tagged covert-deposit
        // callback is therefore impossible and must have no stock side effect.
        if (hasAnyPhaseATag(params))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id target = params.getObjId(money.DICT_TARGET_ID);
        int total = params.getInt(money.DICT_TOTAL);
        String returnHandler = params.getString(money.DICT_PAY_HANDLER);
        if (total < 1)
        {
            return SCRIPT_CONTINUE;
        }
        int retCode = money.getReturnCode(params);
        switch (retCode)
        {
            case money.RET_SUCCESS:
            if (!isIdValid(target))
            {
                String acct = params.getString(money.DICT_ACCT_NAME);
                if ((acct == null) || (acct.equals("")))
                {
                    return SCRIPT_CONTINUE;
                }
                else 
                {
                    transferBankCreditsToNamedAccount(self, acct, total, money.HANDLER_PAY_PASS, money.HANDLER_PAY_FAIL, params);
                    utils.moneyOutMetric(self, acct, total);
                }
            }
            else 
            {
                transferBankCreditsTo(self, target, total, money.HANDLER_PAY_PASS, money.HANDLER_PAY_FAIL, params);
            }
            return SCRIPT_CONTINUE;
            default:
            if (!isIdValid(target))
            {
                String acct = params.getString(money.DICT_ACCT_NAME);
                if ((acct == null) || (acct.equals("")))
                {
                    return SCRIPT_CONTINUE;
                }
                else 
                {
                    prose_package pp = prose.getPackage(money.PROSE_PAY_FAIL, acct, total);
                    sendSystemMessageProse(self, pp);
                    messageTo(self, returnHandler, params, 0, true);
                }
            }
            else 
            {
                String targetName = params.getString(money.DICT_TARGET_NAME);
                if (targetName != null && !targetName.equals(""))
                {
                    prose_package pp = prose.getPackage(money.PROSE_PAY_FAIL, self, null, null, target, targetName, null, null, null, null, total, 0.0f);
                    sendSystemMessageProse(self, pp);
                }
                else 
                {
                    prose_package pp = prose.getPackage(money.PROSE_PAY_FAIL, self, target, total);
                    sendSystemMessageProse(self, pp);
                }
                messageTo(target, returnHandler, params, 0, isObjectPersisted(target));
            }
            return SCRIPT_CONTINUE;
        }
    }
    public int handlePayPass(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        if (hasAnyPhaseATag(params))
        {
            // Native transfer success arrives without DICT_CODE.  Accept only
            // that exact pre-normalization shape; the stock code below then
            // publishes the explicit RET_SUCCESS required by attemptedPayment.
            if (!hasExactNativePaymentEnvelope(params) ||
                !transitionPhaseAPurchaseStage(
                    self,
                    params,
                    "paymentDispatching",
                    "",
                    "paymentSucceededCallback",
                    true))
            {
                return SCRIPT_CONTINUE;
            }
        }
        money.decrementPayTally(self, params);
        int retCode = money.getReturnCode(params);
        switch (retCode)
        {
            case -1:
            params.put(money.DICT_CODE, money.RET_SUCCESS);
            break;
            case money.RET_FAIL:
            messageTo(self, money.HANDLER_PAY_FAIL, params, 0, isObjectPersisted(self));
            return SCRIPT_CONTINUE;
        }
        obj_id target = params.getObjId(money.DICT_TARGET_ID);
        int total = params.getInt(money.DICT_TOTAL);
        boolean notify = params.getBoolean(money.DICT_NOTIFY);
        if (notify)
        {
            if (!isIdValid(target))
            {
                String acct = params.getString(money.DICT_ACCT_NAME);
                if ((acct == null) || (acct.equals("")))
                {
                }
                else 
                {
                    string_id sid_acct = new string_id(money.STF_ACCT_N, toLower(acct));
                    if (sid_acct != null)
                    {
                        prose_package ppSidAcct = prose.getPackage(money.PROSE_PAY_ACCT_SUCCESS, sid_acct, total);
                        sendSystemMessageProse(self, ppSidAcct);
                    }
                    else 
                    {
                        prose_package ppAcct = prose.getPackage(money.PROSE_PAY_ACCT_SUCCESS, acct, total);
                        sendSystemMessageProse(self, ppAcct);
                    }
                }
            }
            else 
            {
                String targetName = params.getString(money.DICT_TARGET_NAME);
                if (targetName != null && !targetName.equals(""))
                {
                    prose_package pp = prose.getPackage(money.PROSE_PAY_SUCCESS, self, null, null, target, targetName, null, null, null, null, total, 0.0f);
                    sendSystemMessageProse(self, pp);
                }
                else 
                {
                    if (exists(target))
                    {
                        prose_package pp = prose.getPackage(money.PROSE_PAY_SUCCESS, self, target, total);
                        sendSystemMessageProse(self, pp);
                    }
                    else 
                    {
                        prose_package pp = prose.getPackage(money.PROSE_PAY_SUCCESS_NO_TARGET, self, target, total);
                        sendSystemMessageProse(self, pp);
                    }
                }
            }
        }
        String returnHandler = params.getString(money.DICT_PAY_HANDLER);
        int msgPayer = params.getInt(money.DICT_MSG_PAYER);
        if ((target != null) && (target != obj_id.NULL_ID) && (msgPayer == 0))
        {
            messageTo(target, returnHandler, params, 0, isObjectPersisted(target));
        }
        else 
        {
            messageTo(self, returnHandler, params, 0, true);
        }
        return SCRIPT_CONTINUE;
    }
    public int handlePayFail(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        if (hasAnyPhaseATag(params) &&
            (!hasExactNativePaymentEnvelope(params) ||
             !transitionPhaseAPurchaseStage(
                 self,
                 params,
                 "paymentDispatching",
                 "",
                 "paymentFailedCallback",
                 false)))
        {
            return SCRIPT_CONTINUE;
        }
        money.decrementPayTally(self, params);
        params.put(money.DICT_CODE, money.RET_FAIL);
        String returnHandler = params.getString(money.DICT_PAY_HANDLER);
        obj_id target = params.getObjId(money.DICT_TARGET_ID);
        if (!isIdValid(target))
        {
            messageTo(self, returnHandler, params, 0, true);
        }
        else 
        {
            messageTo(target, returnHandler, params, 0, isObjectPersisted(target));
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseADispatchBankTransfer(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isExactPhaseABankDispatch(self, params))
        {
            return SCRIPT_CONTINUE;
        }
        String kind = params.getString(PRECU_PARAM_KIND);
        boolean queued;
        if ("fund".equals(kind))
        {
            queued = transferBankCreditsFromNamedAccount(
                money.ACCT_CUSTOMER_SERVICE,
                self,
                PRECU_CRAFTING_TRAINER_COST,
                money.HANDLER_BANK_SUCCESS,
                money.HANDLER_BANK_TRANSFER_ERROR,
                params);
        }
        else
        {
            queued = transferBankCreditsToNamedAccount(
                self,
                money.ACCT_CUSTOMER_SERVICE,
                PRECU_CRAFTING_TRAINER_COST,
                money.HANDLER_BANK_SUCCESS,
                money.HANDLER_BANK_TRANSFER_ERROR,
                params);
        }
        if (!queued)
        {
            transitionPhaseAOperation(self, params, false);
        }
        return SCRIPT_CONTINUE;
    }
    public int OnLogin(obj_id self) throws InterruptedException
    {
        if (utils.hasScriptVar(self, PRECU_RELOG_NONCE) &&
            hasObjVar(self, PRECU_OP_ATTEMPT_ID) && hasObjVar(self, PRECU_OP_ID) &&
            hasObjVar(self, PRECU_OP_KIND) && hasObjVar(self, PRECU_OP_STATE) &&
            hasObjVar(self, PRECU_OP_LIFECYCLE_ID) &&
            hasObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID) &&
            hasObjVar(self, PRECU_LIFECYCLE_ID) && hasObjVar(self, PRECU_LIFECYCLE_STATE) &&
            hasObjVar(self, PRECU_OP_PROTOCOL_VERSION) &&
            hasObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME))
        {
            String relogOperationId = utils.getStringScriptVar(self, PRECU_RELOG_NONCE);
            if (relogOperationId.equals(getStringObjVar(self, PRECU_OP_ATTEMPT_ID)) &&
                relogOperationId.equals(getStringObjVar(self, PRECU_OP_ID)) &&
                "purchase".equals(getStringObjVar(self, PRECU_OP_KIND)) &&
                "purchaseSucceeded".equals(getStringObjVar(self, PRECU_OP_STATE)) &&
                getStringObjVar(self, PRECU_OP_LIFECYCLE_ID).equals(
                    getStringObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID)) &&
                getStringObjVar(self, PRECU_OP_LIFECYCLE_ID).equals(
                    getStringObjVar(self, PRECU_LIFECYCLE_ID)) &&
                "established".equals(getStringObjVar(self, PRECU_LIFECYCLE_STATE)) &&
                getIntObjVar(self, PRECU_OP_PROTOCOL_VERSION) == PRECU_PROTOCOL_VERSION &&
                "SUCCESS".equals(getStringObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME)) &&
                hasExactHeldPhaseACraftingVector(self) &&
                !utils.hasScriptVar(self, PRECU_RESTART_NONCE))
            {
                utils.removeScriptVar(self, PRECU_RELOG_NONCE);
            }
        }
        if (!hasObjVar(self, PRECU_OP_STATE) ||
            !"queued".equals(getStringObjVar(self, PRECU_OP_STATE)) ||
            !hasObjVar(self, PRECU_OP_ID) || !hasObjVar(self, PRECU_OP_KIND) ||
            !hasObjVar(self, PRECU_OP_LIFECYCLE_ID))
        {
            return SCRIPT_CONTINUE;
        }
        dictionary params = new dictionary();
        params.put(PRECU_PARAM_ID, getStringObjVar(self, PRECU_OP_ID));
        params.put(PRECU_PARAM_KIND, getStringObjVar(self, PRECU_OP_KIND));
        params.put(PRECU_LIFECYCLE_PARAM_ID,
            getStringObjVar(self, PRECU_OP_LIFECYCLE_ID));
        params.put(PRECU_PROTOCOL_PARAM_VERSION, PRECU_PROTOCOL_VERSION);
        params.put(money.DICT_TOTAL, PRECU_CRAFTING_TRAINER_COST);
        return precuPhaseADispatchBankTransfer(self, params);
    }
    private boolean isExactPhaseABankDispatch(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isIdValid(self) || !isPlayer(self) || !self.isLoaded() ||
            !self.isAuthoritative() || params == null || params.isEmpty() ||
            !hasObjVar(self, PRECU_OP_ATTEMPT_ID) || !hasObjVar(self, PRECU_OP_ID) ||
            !hasObjVar(self, PRECU_OP_KIND) || !hasObjVar(self, PRECU_OP_STATE) ||
            !hasObjVar(self, PRECU_OP_LIFECYCLE_ID) ||
            !hasObjVar(self, PRECU_OP_COST) || !hasObjVar(self, PRECU_OP_PROTOCOL_VERSION) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ID) || !hasObjVar(self, PRECU_LIFECYCLE_STATE) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_XP))
        {
            return false;
        }
        String operationId = params.getString(PRECU_PARAM_ID);
        String kind = params.getString(PRECU_PARAM_KIND);
        String lifecycleId = params.getString(PRECU_LIFECYCLE_PARAM_ID);
        int currentXp = getExperiencePoints(self, PRECU_CRAFTING_XP_TYPE);
        boolean exactXp = currentXp == PRECU_CRAFTING_XP_COST ||
            ("drain".equals(kind) && currentXp == getIntObjVar(self, PRECU_LIFECYCLE_BASE_XP));
        if (operationId == null || lifecycleId == null ||
            (!"fund".equals(kind) && !"drain".equals(kind)) ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ATTEMPT_ID)) ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ID)) ||
            !kind.equals(getStringObjVar(self, PRECU_OP_KIND)) ||
            !"queued".equals(getStringObjVar(self, PRECU_OP_STATE)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_OP_LIFECYCLE_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ID)) ||
            !"established".equals(getStringObjVar(self, PRECU_LIFECYCLE_STATE)) ||
            params.getInt(PRECU_PROTOCOL_PARAM_VERSION) != PRECU_PROTOCOL_VERSION ||
            getIntObjVar(self, PRECU_OP_PROTOCOL_VERSION) != PRECU_PROTOCOL_VERSION ||
            params.getInt(money.DICT_TOTAL) != PRECU_CRAFTING_TRAINER_COST ||
            getIntObjVar(self, PRECU_OP_COST) != PRECU_CRAFTING_TRAINER_COST ||
            !hasSkill(self, PRECU_CRAFTING_NOVICE_SKILL) ||
            hasSkill(self, PRECU_CRAFTING_SKILL) ||
            !exactXp)
        {
            return false;
        }
        int credits = getTotalMoney(self);
        return ("fund".equals(kind) && credits == 0) ||
            ("drain".equals(kind) && credits == PRECU_CRAFTING_TRAINER_COST);
    }
    public int precuPhaseARequestAccounting(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!hasExactPhaseAAccountingAttempt(
                self,
                params,
                "accountingRequested",
                "",
                PRECU_ACCOUNTING_OUTCOME_NONE))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id trainer = params.getObjId(money.DICT_TARGET_ID);
        setObjVar(self, PRECU_OP_STATE, "accountingDispatching");
        setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
        if (!hasExactPhaseAAccountingAttempt(
                self,
                params,
                "accountingDispatching",
                "",
                PRECU_ACCOUNTING_OUTCOME_NONE))
        {
            return SCRIPT_CONTINUE;
        }
        boolean queued = transferBankCreditsToNamedAccount(
            trainer,
            money.ACCT_SKILL_TRAINING,
            params.getInt(money.DICT_TOTAL),
            "precuPhaseAAccountingSucceeded",
            "precuPhaseAAccountingFailed",
            params);
        if (!queued)
        {
            publishPhaseAAccountingOutcome(
                self,
                params,
                "accountingDispatching",
                "",
                PRECU_ACCOUNTING_OUTCOME_QUEUE_FAILED,
                "accountingQueueFailed");
        }
        else if (hasExactPhaseAAccountingAttempt(
                self,
                params,
                "accountingDispatching",
                "",
                PRECU_ACCOUNTING_OUTCOME_NONE))
        {
            setObjVar(self, PRECU_OP_STATE, "accountingPending");
            setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseAAccountingSucceeded(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!publishPhaseAAccountingOutcome(
                self,
                params,
                "accountingDispatching",
                "accountingPending",
                PRECU_ACCOUNTING_OUTCOME_SUCCESS,
                "accountingSucceededCallback"))
        {
            return SCRIPT_CONTINUE;
        }
        if (hasExactPhaseAAccountingAttempt(
                self,
                params,
                "accountingSucceededCallback",
                "",
                PRECU_ACCOUNTING_OUTCOME_SUCCESS))
        {
            setObjVar(self, PRECU_OP_STATE, "purchaseSucceeded");
            setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
            if ("purchaseSucceeded".equals(getStringObjVar(self, PRECU_OP_STATE)))
            {
                utils.moneyOutMetric(
                    params.getObjId(money.DICT_TARGET_ID),
                    money.ACCT_SKILL_TRAINING,
                    params.getInt(money.DICT_TOTAL));
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int precuPhaseAAccountingFailed(obj_id self, dictionary params)
        throws InterruptedException
    {
        publishPhaseAAccountingOutcome(
            self,
            params,
            "accountingDispatching",
            "accountingPending",
            PRECU_ACCOUNTING_OUTCOME_FAILED,
            "accountingFailed");
        return SCRIPT_CONTINUE;
    }
    public int acctToPass(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        int amt = params.getInt(money.DICT_AMOUNT);
        if (amt < 1)
        {
            return SCRIPT_CONTINUE;
        }
        obj_id target = params.getObjId(money.DICT_TARGET_ID);
        if (!isIdValid(target))
        {
            return SCRIPT_CONTINUE;
        }
        withdrawCashFromBank(target, amt, "withdrawPass", "xferFail", params);
        return SCRIPT_CONTINUE;
    }
    public int withdrawPass(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        params.put(money.DICT_CODE, money.RET_SUCCESS);
        String returnHandler = params.getString(money.DICT_PAY_HANDLER);
        messageTo(self, returnHandler, params, 0, true);
        return SCRIPT_CONTINUE;
    }
    public int xferFail(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        params.put(money.DICT_CODE, money.RET_FAIL);
        String returnHandler = params.getString(money.DICT_PAY_HANDLER);
        messageTo(self, returnHandler, params, 0, true);
        return SCRIPT_CONTINUE;
    }
    public int handleCovertDepositReturn(obj_id self, dictionary params) throws InterruptedException
    {
        int cash = getCashBalance(self);
        if (cash > 0)
        {
            money.covertDeposit(self, cash, "handleCovertDepositReturn", params);
            return SCRIPT_CONTINUE;
        }
        int bank = getBankBalance(self);
        if (bank < 1)
        {
            return SCRIPT_CONTINUE;
        }
        String acct = params.getString("acct");
        transferBankCreditsToNamedAccount(self, acct, bank, "finishClear", "finishClear", params);
        return SCRIPT_CONTINUE;
    }
    public int finishClear(obj_id self, dictionary params) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int handleDepositWithdraw(obj_id self, dictionary params) throws InterruptedException
    {
        utils.removeScriptVar(self, "bankMenu");
        if (params == null)
        {
            messageTo(self, money.HANDLER_BANK_UNKNOWN_ERROR, null, 0, true);
            return SCRIPT_CONTINUE;
        }
        int btnPressed = sui.getIntButtonPressed(params);
        switch (btnPressed)
        {
            case sui.BP_CANCEL:
            return SCRIPT_CONTINUE;
        }
        int cashIn = getCashBalance(self);
        int cashOut = sui.getTransferInputFrom(params);
        int bankIn = getBankBalance(self);
        int bankOut = sui.getTransferInputTo(params);
        if ((cashOut < 0) || (bankOut < 0))
        {
            return SCRIPT_OVERRIDE;
        }
        int totalIn = cashIn + bankIn;
        int totalOut = cashOut + bankOut;
        if (totalIn != totalOut)
        {
            return SCRIPT_OVERRIDE;
        }
        int amt = 0;
        dictionary d = new dictionary();
        if (cashIn > cashOut)
        {
            amt = cashIn - cashOut;
            if (!money.deposit(self, amt))
            {
            }
        }
        else if (cashIn < cashOut)
        {
            amt = cashOut - cashIn;
            if (!money.withdraw(self, amt))
            {
            }
        }
        return SCRIPT_CONTINUE;
    }
    public int handleBankSuccess(obj_id self, dictionary params) throws InterruptedException
    {
        if (hasAnyPhaseATag(params) && !transitionPhaseAOperation(self, params, true))
        {
            return SCRIPT_CONTINUE;
        }
        money.bankSuccess(self, params);
        return SCRIPT_CONTINUE;
    }
    public int handleBankUnknownError(obj_id self, dictionary params) throws InterruptedException
    {
        money.bankError(self);
        return SCRIPT_CONTINUE;
    }
    public int handleBankWithdrawError(obj_id self, dictionary params) throws InterruptedException
    {
        money.bankWithdrawError(self);
        return SCRIPT_CONTINUE;
    }
    public int handleBankDepositError(obj_id self, dictionary params) throws InterruptedException
    {
        money.bankDepositError(self);
        return SCRIPT_CONTINUE;
    }
    public int handleBankTransferError(obj_id self, dictionary params) throws InterruptedException
    {
        if (hasAnyPhaseATag(params) && !transitionPhaseAOperation(self, params, false))
        {
            return SCRIPT_CONTINUE;
        }
        money.bankTransferError(self);
        return SCRIPT_CONTINUE;
    }
    private boolean hasAnyPhaseATag(dictionary params)
    {
        return params != null && !params.isEmpty() &&
            (params.containsKey(PRECU_PARAM_ID) || params.containsKey(PRECU_PARAM_KIND) ||
                params.containsKey(PRECU_LIFECYCLE_PARAM_ID) ||
                params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) ||
                params.containsKey(PRECU_REFUND_PARAM_GENERATION) ||
                params.containsKey(PRECU_REFUND_PARAM_ATTEMPT_KEY) ||
                params.containsKey(PRECU_REFUND_PARAM_RETRY_CONSUMED) ||
                params.containsKey(PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY));
    }
    private boolean hasExactHandlerEnvelope(dictionary params)
    {
        return params != null && !params.isEmpty() &&
            params.containsKey(money.DICT_HANDLER) &&
            params.containsKey(money.DICT_PAY_HANDLER) &&
            params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) &&
            "attemptedPayment".equals(params.getString(money.DICT_HANDLER)) &&
            "attemptedPayment".equals(params.getString(money.DICT_PAY_HANDLER)) &&
            params.getInt(PRECU_PROTOCOL_PARAM_VERSION) == PRECU_PROTOCOL_VERSION;
    }
    private boolean hasExactNativePaymentEnvelope(dictionary params)
        throws InterruptedException
    {
        // The native bank callback has not normalized a return code yet.
        return hasExactHandlerEnvelope(params) &&
            !params.containsKey(money.DICT_CODE) && money.getReturnCode(params) == -1;
    }
    private boolean hasExactPaymentRequestEnvelope(dictionary params)
    {
        return hasExactHandlerEnvelope(params) && !params.containsKey(money.DICT_CODE);
    }
    private boolean hasExactSuccessfulPaymentProvenance(dictionary params)
    {
        return hasExactHandlerEnvelope(params) && params.containsKey(money.DICT_CODE) &&
            params.getInt(money.DICT_CODE) == money.RET_SUCCESS;
    }
    private String buildPhaseAAttemptKey(String operationId, String kind, int generation)
    {
        return operationId + "." + kind + "." + generation;
    }
    private boolean hasExactPhaseAAccountingAttempt(
        obj_id self,
        dictionary params,
        String expectedState,
        String alternateState,
        String expectedOutcome) throws InterruptedException
    {
        if (!hasExactSuccessfulPaymentProvenance(params) ||
            !params.containsKey(PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY) ||
            !params.containsKey(money.DICT_ACCT_NAME) ||
            !isExactActivePhaseAPurchaseStage(
                self,
                params,
                expectedState,
                alternateState,
                PRECU_VECTOR_HELD))
        {
            return false;
        }
        String expectedKey = buildPhaseAAttemptKey(
            params.getString(PRECU_PARAM_ID), "accounting", 1);
        return expectedKey.equals(
                params.getString(PRECU_ACCOUNTING_PARAM_ATTEMPT_KEY)) &&
            expectedKey.equals(getStringObjVar(self, PRECU_OP_ACCOUNTING_ATTEMPT_KEY)) &&
            money.ACCT_SKILL_TRAINING.equals(params.getString(money.DICT_ACCT_NAME)) &&
            money.ACCT_SKILL_TRAINING.equals(
                getStringObjVar(self, PRECU_OP_ACCOUNTING_ACCOUNT)) &&
            expectedOutcome.equals(getStringObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME)) &&
            getIntObjVar(self, PRECU_OP_REFUND_GENERATION) == 0 &&
            "none".equals(getStringObjVar(self, PRECU_OP_REFUND_ATTEMPT_KEY)) &&
            getIntObjVar(self, PRECU_OP_REFUND_RETRY_CONSUMED) == 0;
    }
    private boolean publishPhaseAAccountingOutcome(
        obj_id self,
        dictionary params,
        String expectedState,
        String alternateState,
        String outcome,
        String state) throws InterruptedException
    {
        if (!hasExactPhaseAAccountingAttempt(
                self,
                params,
                expectedState,
                alternateState,
                PRECU_ACCOUNTING_OUTCOME_NONE))
        {
            return false;
        }
        setObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME, outcome);
        if (!outcome.equals(getStringObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME)))
        {
            return false;
        }
        setObjVar(self, PRECU_OP_STATE, state);
        setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
        return state.equals(getStringObjVar(self, PRECU_OP_STATE));
    }
    private boolean transitionPhaseAOperation(obj_id self, dictionary params, boolean succeeded)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() ||
            !hasObjVar(self, PRECU_OP_ATTEMPT_ID) || !hasObjVar(self, PRECU_OP_ID) ||
            !hasObjVar(self, PRECU_OP_KIND) || !hasObjVar(self, PRECU_OP_STATE) ||
            !hasObjVar(self, PRECU_OP_LIFECYCLE_ID) ||
            !hasObjVar(self, PRECU_OP_COST) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ID) ||
            !hasObjVar(self, PRECU_LIFECYCLE_STATE) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_CASH) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_BANK) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_XP) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_POINTS) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_CAP) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_NOVICE) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_SKILL))
        {
            return false;
        }
        String operationId = params.getString(PRECU_PARAM_ID);
        String operationKind = params.getString(PRECU_PARAM_KIND);
        String lifecycleId = params.getString(PRECU_LIFECYCLE_PARAM_ID);
        if (operationId == null ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ATTEMPT_ID)) ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ID)) ||
            operationKind == null || !operationKind.equals(getStringObjVar(self, PRECU_OP_KIND)) ||
            lifecycleId == null || !lifecycleId.equals(getStringObjVar(self, PRECU_OP_LIFECYCLE_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ID)) ||
            !"established".equals(getStringObjVar(self, PRECU_LIFECYCLE_STATE)) ||
            params.getInt(money.DICT_TOTAL) != getIntObjVar(self, PRECU_OP_COST) ||
            (!"fund".equals(operationKind) && !"drain".equals(operationKind)))
        {
            return false;
        }
        String currentState = getStringObjVar(self, PRECU_OP_STATE);
        if (!"enqueueing".equals(currentState) && !"queued".equals(currentState))
        {
            return false;
        }
        String terminalState = operationKind + (succeeded ? "Succeeded" : "Failed");
        setObjVar(
            self,
            PRECU_OP_STATE,
            terminalState);
        setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
        return terminalState.equals(getStringObjVar(self, PRECU_OP_STATE));
    }
    private boolean isExactActivePhaseAPurchaseStage(
        obj_id self,
        dictionary params,
        String expectedState,
        String alternateState,
        String expectedVector) throws InterruptedException
    {
        if (!isIdValid(self) || !isPlayer(self) || !self.isLoaded() ||
            !self.isAuthoritative() || params == null || params.isEmpty() ||
            !hasObjVar(self, PRECU_OP_ATTEMPT_ID) || !hasObjVar(self, PRECU_OP_ID) ||
            !hasObjVar(self, PRECU_OP_KIND) || !hasObjVar(self, PRECU_OP_STATE) ||
            !hasObjVar(self, PRECU_OP_UPDATED) ||
            !hasObjVar(self, PRECU_OP_LIFECYCLE_ID) ||
            !hasObjVar(self, PRECU_OP_TRAINER_OID) ||
            !hasObjVar(self, PRECU_OP_SKILL_NAME) || !hasObjVar(self, PRECU_OP_COST) ||
            !hasObjVar(self, PRECU_OP_PRE_CREDITS) ||
            !hasObjVar(self, PRECU_OP_PRE_CASH) || !hasObjVar(self, PRECU_OP_PRE_BANK) ||
            !hasObjVar(self, PRECU_OP_PRE_XP) || !hasObjVar(self, PRECU_OP_PRE_POINTS) ||
            !hasObjVar(self, PRECU_OP_PRE_CAP) || !hasObjVar(self, PRECU_OP_PRE_NOVICE) ||
            !hasObjVar(self, PRECU_OP_PRE_SKILL) ||
            !hasObjVar(self, PRECU_OP_PROTOCOL_VERSION) ||
            !hasObjVar(self, PRECU_OP_REFUND_GENERATION) ||
            !hasObjVar(self, PRECU_OP_REFUND_ATTEMPT_KEY) ||
            !hasObjVar(self, PRECU_OP_REFUND_RETRY_CONSUMED) ||
            !hasObjVar(self, PRECU_OP_ACCOUNTING_ATTEMPT_KEY) ||
            !hasObjVar(self, PRECU_OP_ACCOUNTING_ACCOUNT) ||
            !hasObjVar(self, PRECU_OP_ACCOUNTING_OUTCOME) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID) ||
            !hasObjVar(self, PRECU_LIFECYCLE_ID) ||
            !hasObjVar(self, PRECU_LIFECYCLE_STATE) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_CASH) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_BANK) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_XP) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_POINTS) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_CAP) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_NOVICE) ||
            !hasObjVar(self, PRECU_LIFECYCLE_BASE_SKILL))
        {
            return false;
        }
        String operationId = params.getString(PRECU_PARAM_ID);
        String operationKind = params.getString(PRECU_PARAM_KIND);
        String lifecycleId = params.getString(PRECU_LIFECYCLE_PARAM_ID);
        String skillName = params.getString("skillName");
        obj_id taggedPlayer = params.getObjId(money.DICT_PLAYER_ID);
        obj_id taggedTrainer = params.getObjId(money.DICT_TARGET_ID);
        String currentState = getStringObjVar(self, PRECU_OP_STATE);
        int cost = getIntObjVar(self, PRECU_OP_COST);
        if (!params.containsKey(PRECU_PARAM_ID) ||
            !params.containsKey(PRECU_PARAM_KIND) ||
            !params.containsKey(PRECU_LIFECYCLE_PARAM_ID) ||
            !params.containsKey(PRECU_PROTOCOL_PARAM_VERSION) ||
            !params.containsKey("skillName") ||
            !params.containsKey(money.DICT_PLAYER_ID) ||
            !params.containsKey(money.DICT_TARGET_ID) ||
            !params.containsKey(money.DICT_AMOUNT) ||
            !params.containsKey(money.DICT_TOTAL) ||
            operationId == null || !operationId.matches("[a-f0-9]{32}") ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ATTEMPT_ID)) ||
            !operationId.equals(getStringObjVar(self, PRECU_OP_ID)) ||
            !"purchase".equals(operationKind) ||
            !operationKind.equals(getStringObjVar(self, PRECU_OP_KIND)) ||
            lifecycleId == null || !lifecycleId.matches("[a-f0-9]{32}") ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_OP_LIFECYCLE_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ATTEMPT_ID)) ||
            !lifecycleId.equals(getStringObjVar(self, PRECU_LIFECYCLE_ID)) ||
            !"established".equals(getStringObjVar(self, PRECU_LIFECYCLE_STATE)) ||
            taggedPlayer == null || !self.equals(taggedPlayer) ||
            !isIdValid(taggedTrainer) || !taggedTrainer.isLoaded() ||
            !taggedTrainer.isAuthoritative() ||
            !taggedTrainer.toString().equals(getStringObjVar(self, PRECU_OP_TRAINER_OID)) ||
            skillName == null || !PRECU_CRAFTING_SKILL.equals(skillName) ||
            !skillName.equals(getStringObjVar(self, PRECU_OP_SKILL_NAME)) ||
            params.getInt(PRECU_PROTOCOL_PARAM_VERSION) != PRECU_PROTOCOL_VERSION ||
            getIntObjVar(self, PRECU_OP_PROTOCOL_VERSION) != PRECU_PROTOCOL_VERSION ||
            cost != PRECU_CRAFTING_TRAINER_COST ||
            getIntObjVar(self, PRECU_OP_UPDATED) <= 0 ||
            params.getInt(money.DICT_AMOUNT) != cost ||
            params.getInt(money.DICT_TOTAL) != cost ||
            (!expectedState.equals(currentState) &&
                (alternateState.length() == 0 || !alternateState.equals(currentState))))
        {
            return false;
        }
        int preCredits = getIntObjVar(self, PRECU_OP_PRE_CREDITS);
        int preCash = getIntObjVar(self, PRECU_OP_PRE_CASH);
        int preBank = getIntObjVar(self, PRECU_OP_PRE_BANK);
        int preXp = getIntObjVar(self, PRECU_OP_PRE_XP);
        int prePoints = getIntObjVar(self, PRECU_OP_PRE_POINTS);
        int preCap = getIntObjVar(self, PRECU_OP_PRE_CAP);
        int preNovice = getIntObjVar(self, PRECU_OP_PRE_NOVICE);
        int preSkill = getIntObjVar(self, PRECU_OP_PRE_SKILL);
        int baseCash = getIntObjVar(self, PRECU_LIFECYCLE_BASE_CASH);
        int baseBank = getIntObjVar(self, PRECU_LIFECYCLE_BASE_BANK);
        int baseXp = getIntObjVar(self, PRECU_LIFECYCLE_BASE_XP);
        int basePoints = getIntObjVar(self, PRECU_LIFECYCLE_BASE_POINTS);
        int baseCap = getIntObjVar(self, PRECU_LIFECYCLE_BASE_CAP);
        int baseNovice = getIntObjVar(self, PRECU_LIFECYCLE_BASE_NOVICE);
        int baseSkill = getIntObjVar(self, PRECU_LIFECYCLE_BASE_SKILL);
        int novicePointCost = skill.getSkillPointCost(PRECU_CRAFTING_NOVICE_SKILL);
        int targetPointCost = skill.getSkillPointCost(PRECU_CRAFTING_SKILL);
        int expectedPrePoints = basePoints - (baseNovice == 0 ? novicePointCost : 0);
        if (baseCash < 0 || baseBank < 0 || preCredits < cost ||
            preCash < 0 || preBank < cost ||
            (long)preCredits != (long)preCash + (long)preBank ||
            preCash != baseCash ||
            (long)preBank != (long)baseBank + (long)cost ||
            (long)preXp != (long)baseXp + (long)PRECU_CRAFTING_XP_COST ||
            novicePointCost < 0 || targetPointCost < 0 ||
            prePoints != expectedPrePoints || prePoints < targetPointCost ||
            preCap != PRECU_PREPURCHASE_XP_CAP ||
            (baseNovice == 1 && baseCap != preCap) ||
            (baseNovice != 0 && baseNovice != 1) || baseSkill != 0 ||
            preNovice != 1 || preSkill != 0 ||
            !PRECU_VECTOR_PRE.equals(expectedVector) &&
                !PRECU_VECTOR_DEBIT.equals(expectedVector) &&
                !PRECU_VECTOR_HELD.equals(expectedVector))
        {
            return false;
        }
        int bankDebit = preBank < cost ? preBank : cost;
        int cashDebit = cost - bankDebit;
        if (PRECU_VECTOR_PRE.equals(expectedVector) ||
            PRECU_VECTOR_DEBIT.equals(expectedVector))
        {
            boolean debit = PRECU_VECTOR_DEBIT.equals(expectedVector);
            return getExperiencePoints(self, PRECU_CRAFTING_XP_TYPE) == preXp &&
                skill.getAvailableSkillPoints(self) == prePoints &&
                getExperienceCap(self, PRECU_CRAFTING_XP_TYPE) == preCap &&
                (hasSkill(self, PRECU_CRAFTING_NOVICE_SKILL) ? 1 : 0) == preNovice &&
                (hasSkill(self, PRECU_CRAFTING_SKILL) ? 1 : 0) == preSkill &&
                hasExactPreparedPhaseACraftingVector(self) &&
                !utils.hasScriptVar(self, PRECU_RELOG_NONCE) &&
                !utils.hasScriptVar(self, PRECU_RESTART_NONCE) &&
                getCashBalance(self) == (debit ? preCash - cashDebit : preCash) &&
                getBankBalance(self) == (debit ? preBank - bankDebit : preBank) &&
                getTotalMoney(self) == (debit ? preCredits - cost : preCredits);
        }
        return getCashBalance(self) == preCash - cashDebit &&
            getBankBalance(self) == preBank - bankDebit &&
            getTotalMoney(self) == preCredits - cost &&
            getExperiencePoints(self, PRECU_CRAFTING_XP_TYPE) ==
                preXp - PRECU_CRAFTING_XP_COST &&
            skill.getAvailableSkillPoints(self) == prePoints - targetPointCost &&
            getExperienceCap(self, PRECU_CRAFTING_XP_TYPE) == PRECU_TRAINED_XP_CAP &&
            hasExactHeldPhaseACraftingVector(self) &&
            !utils.hasScriptVar(self, PRECU_RESTART_NONCE) &&
            (!utils.hasScriptVar(self, PRECU_RELOG_NONCE) ||
                operationId.equals(utils.getStringScriptVar(self, PRECU_RELOG_NONCE)));
    }
    private boolean containsPhaseAValue(String[] values, String expected)
    {
        if (values == null || expected == null)
        {
            return false;
        }
        for (String value : values)
        {
            if (expected.equals(value))
            {
                return true;
            }
        }
        return false;
    }
    private boolean hasExactPhaseASchematicVector(obj_id player, boolean held)
        throws InterruptedException
    {
        String[] groupIds = dataTableGetStringColumnNoDefaults(
            PRECU_SCHEMATIC_GROUP_TABLE,
            "GroupId");
        String[] schematicNames = dataTableGetStringColumnNoDefaults(
            PRECU_SCHEMATIC_GROUP_TABLE,
            "SchematicName");
        if (groupIds == null || schematicNames == null ||
            groupIds.length != schematicNames.length)
        {
            return false;
        }
        java.util.Vector<String> seen = new java.util.Vector<String>();
        for (int i = 0; i < groupIds.length; ++i)
        {
            if (!containsPhaseAValue(PRECU_CRAFTING_VECTOR_SCHEMATIC_GROUPS, groupIds[i]))
            {
                continue;
            }
            String schematicName = schematicNames[i];
            if (schematicName == null || schematicName.length() == 0 ||
                seen.contains(schematicName))
            {
                return false;
            }
            seen.add(schematicName);
            boolean shouldOwn = held ||
                !containsPhaseAValue(PRECU_CRAFTING_PURCHASE_SCHEMATICS, schematicName);
            if (hasSchematic(player, schematicName) != shouldOwn)
            {
                return false;
            }
        }
        return seen.size() == PRECU_CRAFTING_SCHEMATIC_COUNT;
    }
    private boolean hasExactPreparedPhaseACraftingVector(obj_id player)
        throws InterruptedException
    {
        if (!hasSkill(player, PRECU_CRAFTING_NOVICE_SKILL) ||
            hasSkill(player, PRECU_CRAFTING_SKILL) ||
            !hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[0]) ||
            hasCommand(player, PRECU_CRAFTING_VECTOR_COMMANDS[1]) ||
            (hasObjVar(player, "newbie.hasSkill") && !hasObjVar(player, "newbie.trained")))
        {
            return false;
        }
        for (int i = 0; i < PRECU_CRAFTING_VECTOR_MODS.length; ++i)
        {
            if (getSkillStatisticModifier(player, PRECU_CRAFTING_VECTOR_MODS[i]) !=
                PRECU_CRAFTING_VECTOR_MOD_VALUES[i] -
                    PRECU_CRAFTING_PURCHASE_MOD_DELTAS[i])
            {
                return false;
            }
        }
        return hasExactPhaseASchematicVector(player, false);
    }
    private boolean hasExactHeldPhaseACraftingVector(obj_id player)
        throws InterruptedException
    {
        if (!hasSkill(player, PRECU_CRAFTING_NOVICE_SKILL) ||
            !hasSkill(player, PRECU_CRAFTING_SKILL))
        {
            return false;
        }
        for (String command : PRECU_CRAFTING_VECTOR_COMMANDS)
        {
            if (!hasCommand(player, command))
            {
                return false;
            }
        }
        for (int i = 0; i < PRECU_CRAFTING_VECTOR_MODS.length; ++i)
        {
            if (getSkillStatisticModifier(player, PRECU_CRAFTING_VECTOR_MODS[i]) !=
                PRECU_CRAFTING_VECTOR_MOD_VALUES[i])
            {
                return false;
            }
        }
        return hasExactPhaseASchematicVector(player, true);
    }
    private boolean transitionPhaseAPurchaseStage(
        obj_id self,
        dictionary params,
        String expectedState,
        String alternateState,
        String state,
        boolean expectDebit) throws InterruptedException
    {
        if (!isExactActivePhaseAPurchaseStage(
                self,
                params,
                expectedState,
                alternateState,
                expectDebit ? PRECU_VECTOR_DEBIT : PRECU_VECTOR_PRE))
        {
            return false;
        }
        setObjVar(self, PRECU_OP_STATE, state);
        setObjVar(self, PRECU_OP_UPDATED, getCalendarTime());
        return state.equals(getStringObjVar(self, PRECU_OP_STATE));
    }
    public int handleCashTransferError(obj_id self, dictionary params) throws InterruptedException
    {
        money.cashTransferError(self);
        return SCRIPT_CONTINUE;
    }
    public int handleCovertTransactionPass(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("money", self + " covert transaction succeeded");
        params.put(money.DICT_CODE, money.RET_SUCCESS);
        String returnHandler = params.getString(money.DICT_HANDLER);
        messageTo(self, returnHandler, params, 0, true);
        return SCRIPT_CONTINUE;
    }
    public int handleCovertTransactionFail(obj_id self, dictionary params) throws InterruptedException
    {
        LOG("money", self + " covert transaction FAILED");
        params.put(money.DICT_CODE, money.RET_FAIL);
        String returnHandler = params.getString(money.DICT_HANDLER);
        messageTo(self, returnHandler, params, 0, true);
        return SCRIPT_CONTINUE;
    }
    public int handlePaymentRequest(obj_id self, dictionary params) throws InterruptedException
    {
        if ((params == null) || (params.isEmpty()))
        {
            return SCRIPT_CONTINUE;
        }
        if (hasAnyPhaseATag(params) &&
            (!hasExactPaymentRequestEnvelope(params) ||
             !transitionPhaseAPurchaseStage(
                 self,
                 params,
                 "enqueueing",
                 "",
                 "paymentDispatching",
                 false)))
        {
            return SCRIPT_CONTINUE;
        }
        int amt = params.getInt(money.DICT_AMOUNT);
        String returnHandler = params.getString(money.DICT_HANDLER);
        boolean notify = params.getBoolean(money.DICT_NOTIFY);
        obj_id target = params.getObjId(money.DICT_TARGET_ID);
        if (target == null)
        {
            return SCRIPT_CONTINUE;
        }
        else if (target == obj_id.NULL_ID)
        {
            String acct = params.getString(money.DICT_ACCT_NAME);
            if ((acct == null) || (acct.equals("")))
            {
                return SCRIPT_CONTINUE;
            }
            money.pay(self, acct, amt, returnHandler, params, notify);
        }
        else 
        {
            money.pay(self, target, amt, returnHandler, params, notify);
        }
        return SCRIPT_CONTINUE;
    }
}
