package script.library;

import script.*;

/**
 * Server-authoritative helpers for bartender-hired private entertainers.
 *
 * The global post-NGE inspiration system stays retired. Paid private buffs
 * terminate at performance.applyPrecuEntertainerAttributeBuff(), the existing
 * Publish 14.1 attribute-buff boundary.
 */
public class private_entertainer extends script.base_script
{
    public private_entertainer()
    {
    }

    public static final String STF = "precu_private_entertainer";
    public static final String SCRIPT_PERFORMER =
        "npc.private_entertainer.performer";
    public static final String SCRIPT_PAYMENT =
        "player.private_entertainer_payment";
    public static final String SCRIPT_AUDIENCE =
        "player.private_entertainer_audience";

    public static final String TYPE_DANCER = "dancer";
    public static final String TYPE_MUSICIAN = "musician";

    public static final String VAR_ROOT = "private_entertainer";
    public static final String VAR_OWNER = VAR_ROOT + ".owner";
    public static final String VAR_TYPE = VAR_ROOT + ".type";
    public static final String VAR_BUILDING = VAR_ROOT + ".building";
    public static final String VAR_BARTENDER = VAR_ROOT + ".bartender";
    public static final String VAR_EXPIRES = VAR_ROOT + ".expires";
    public static final String VAR_LAST_OWNER_SEEN =
        VAR_ROOT + ".last_owner_seen";
    public static final String VAR_ACTIVE_DANCER =
        VAR_ROOT + ".active.dancer";
    public static final String VAR_ACTIVE_MUSICIAN =
        VAR_ROOT + ".active.musician";
    public static final String VAR_HIRE_LOCK = VAR_ROOT + ".hire_lock";

    public static final String PAYMENT_ROOT = VAR_ROOT + ".payment";
    public static final String PAYMENT_STATE = PAYMENT_ROOT + ".state";
    public static final String PAYMENT_PERFORMER =
        PAYMENT_ROOT + ".performer";
    public static final String PAYMENT_TYPE = PAYMENT_ROOT + ".type";
    public static final String PAYMENT_NONCE = PAYMENT_ROOT + ".nonce";
    public static final String PAYMENT_STARTED = PAYMENT_ROOT + ".started";
    public static final String PAYMENT_REFUND_ATTEMPTS =
        PAYMENT_ROOT + ".refund_attempts";
    public static final String PAYMENT_LATE_REFUND =
        PAYMENT_ROOT + ".late_refund";
    public static final String PAYMENT_PENDING = "pending";
    public static final String PAYMENT_TIMED_OUT = "timed_out";
    public static final String PAYMENT_SETTLING = "settling";
    public static final String PAYMENT_REFUNDING = "refunding";
    public static final String PAYMENT_REFUND_FAILED = "refund_failed";

    public static final int BUFF_PRICE = 10000;
    public static final float BUFF_STRENGTH_PERCENT = 25.0f;
    public static final float BUFF_DURATION_SECONDS =
        performance.PRECU_BUFF_MAX_DURATION_MINUTES * 60.0f;
    public static final int HIRE_LIFETIME_SECONDS = 1800;
    public static final int OWNER_AWAY_GRACE_SECONDS = 120;
    public static final int LIFECYCLE_TICK_SECONDS = 15;
    public static final int PAYMENT_TIMEOUT_SECONDS = 120;
    public static final float BARTENDER_USE_RANGE = 16.0f;
    public static final float PERFORMER_USE_RANGE = 32.0f;

    private static final String[] SIDE_ROOM_NAMES =
    {
        "alcove1",
        "alcove2",
        "alcove3",
        "alcove4"
    };
    private static final float[] SIDE_ROOM_X =
    {
        20.0f,
        17.5f,
        2.5f,
        3.1f
    };
    private static final float[] SIDE_ROOM_Y =
    {
        -0.9f,
        -0.9f,
        -0.9f,
        -0.9f
    };
    private static final float[] SIDE_ROOM_Z =
    {
        -17.2f,
        17.5f,
        -16.5f,
        21.5f
    };
    private static final float[] SIDE_ROOM_YAW =
    {
        0.0f,
        180.0f,
        0.0f,
        180.0f
    };

    public static boolean isSupportedType(String type)
    {
        return TYPE_DANCER.equals(type) || TYPE_MUSICIAN.equals(type);
    }

    public static String getPlayerActiveVar(String type)
    {
        if (TYPE_DANCER.equals(type))
        {
            return VAR_ACTIVE_DANCER;
        }
        if (TYPE_MUSICIAN.equals(type))
        {
            return VAR_ACTIVE_MUSICIAN;
        }
        return "";
    }

    public static obj_id getCantinaBuilding(obj_id object)
        throws InterruptedException
    {
        if (!isIdValid(object) || !exists(object))
        {
            return obj_id.NULL_ID;
        }
        obj_id building = getTopMostContainer(object);
        if (!isIdValid(building) || building == object ||
            !isIdValid(getCellId(building, "cantina")))
        {
            return obj_id.NULL_ID;
        }
        return building;
    }

    public static boolean canUseBartender(obj_id player, obj_id bartender)
        throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(bartender) ||
            !player.isLoaded() || !player.isAuthoritative() ||
            !isPlayer(player) || isDead(player) ||
            isIncapacitated(player) || !exists(bartender) ||
            !hasScript(bartender, "npc.bartender.base") ||
            getDistance(player, bartender) > BARTENDER_USE_RANGE)
        {
            return false;
        }
        obj_id building = getCantinaBuilding(bartender);
        return isIdValid(building) &&
            building == getCantinaBuilding(player) &&
            hasSideRoom(building);
    }

    public static boolean canPlayerUse(
        obj_id player,
        obj_id performer,
        String expectedType) throws InterruptedException
    {
        if (!isIdValid(player) || !isIdValid(performer) ||
            !player.isLoaded() || !player.isAuthoritative() ||
            !isPlayer(player) || isDead(player) ||
            isIncapacitated(player) || !exists(performer) ||
            !hasScript(performer, SCRIPT_PERFORMER) ||
            !hasObjVar(performer, VAR_OWNER) ||
            !hasObjVar(performer, VAR_TYPE) ||
            !hasObjVar(performer, VAR_BUILDING) ||
            !hasObjVar(performer, VAR_EXPIRES) ||
            getObjIdObjVar(performer, VAR_OWNER) != player ||
            getCalendarTime() >= getIntObjVar(performer, VAR_EXPIRES) ||
            getDistance(player, performer) > PERFORMER_USE_RANGE)
        {
            return false;
        }
        String type = getStringObjVar(performer, VAR_TYPE);
        if (!isSupportedType(type) ||
            (expectedType != null && expectedType.length() > 0 &&
                !expectedType.equals(type)))
        {
            return false;
        }
        obj_id building = getObjIdObjVar(performer, VAR_BUILDING);
        return isIdValid(building) &&
            building == getCantinaBuilding(player) &&
            building == getCantinaBuilding(performer);
    }

    public static obj_id getActive(obj_id player, String type)
        throws InterruptedException
    {
        String activeVar = getPlayerActiveVar(type);
        if (!isIdValid(player) || activeVar.length() == 0 ||
            !hasObjVar(player, activeVar))
        {
            return obj_id.NULL_ID;
        }
        obj_id performer = getObjIdObjVar(player, activeVar);
        if (!performerMatches(player, performer, type))
        {
            removeObjVar(player, activeVar);
            return obj_id.NULL_ID;
        }
        return performer;
    }

    public static obj_id hire(
        obj_id player,
        obj_id bartender,
        String type) throws InterruptedException
    {
        if (!isSupportedType(type) ||
            !canUseBartender(player, bartender))
        {
            if (isIdValid(player))
            {
                sendSystemMessage(player, new string_id(STF, "hire_failed"));
            }
            return obj_id.NULL_ID;
        }

        obj_id active = getActive(player, type);
        if (isIdValid(active))
        {
            sendSystemMessage(
                player,
                new string_id(STF, "already_hired_" + type));
            return active;
        }
        if (utils.hasScriptVar(player, VAR_HIRE_LOCK))
        {
            sendSystemMessage(player, new string_id(STF, "hire_busy"));
            return obj_id.NULL_ID;
        }

        utils.setScriptVar(player, VAR_HIRE_LOCK, getCalendarTime());
        try
        {
            obj_id building = getCantinaBuilding(bartender);
            location spawnLocation = getSideRoomLocation(
                building, player, type);
            if (spawnLocation == null)
            {
                sendSystemMessage(
                    player,
                    new string_id(STF, "no_side_room"));
                return obj_id.NULL_ID;
            }

            obj_id performer = create.object("entertainer", spawnLocation);
            if (!isIdValid(performer) || !exists(performer))
            {
                sendSystemMessage(
                    player,
                    new string_id(STF, "hire_failed"));
                return obj_id.NULL_ID;
            }

            int now = getCalendarTime();
            setObjVar(performer, VAR_OWNER, player);
            setObjVar(performer, VAR_TYPE, type);
            setObjVar(performer, VAR_BUILDING, building);
            setObjVar(performer, VAR_BARTENDER, bartender);
            setObjVar(performer, VAR_EXPIRES,
                now + HIRE_LIFETIME_SECONDS);
            setObjVar(performer, VAR_LAST_OWNER_SEEN, now);
            setObjVar(player, getPlayerActiveVar(type), performer);
            setName(
                performer,
                new string_id(STF, "private_" + type + "_name"));
            setInvulnerable(performer, true);
            setCreatureStatic(performer, true);
            factions.setFaction(performer, "Unattackable");
            ai_lib.setDefaultCalmBehavior(
                performer, ai_lib.BEHAVIOR_SENTINEL);
            int attachResult = attachScript(performer, SCRIPT_PERFORMER);
            if (attachResult != SCRIPT_CONTINUE ||
                !hasScript(performer, SCRIPT_PERFORMER))
            {
                clearPlayerPointer(player, performer, type);
                destroyObject(performer);
                sendSystemMessage(
                    player,
                    new string_id(STF, "hire_failed"));
                return obj_id.NULL_ID;
            }
            setYaw(performer, getSideRoomYaw(building, player, type));

            sendSystemMessage(
                player,
                new string_id(STF, "hired_" + type));
            return performer;
        }
        finally
        {
            utils.removeScriptVar(player, VAR_HIRE_LOCK);
        }
    }

    public static void dismissAll(obj_id player) throws InterruptedException
    {
        dismiss(player, TYPE_DANCER);
        dismiss(player, TYPE_MUSICIAN);
        if (isIdValid(player))
        {
            sendSystemMessage(player, new string_id(STF, "dismissed_all"));
        }
    }

    public static boolean dismiss(obj_id player, String type)
        throws InterruptedException
    {
        obj_id performer = getActive(player, type);
        if (!isIdValid(performer))
        {
            return false;
        }
        cleanupAudience(performer);
        clearPlayerPointer(player, performer, type);
        destroyObject(performer);
        return true;
    }

    public static void beginWatching(obj_id player, obj_id performer)
        throws InterruptedException
    {
        if (!canPlayerUse(player, performer, TYPE_DANCER))
        {
            sendSystemMessage(player, new string_id(STF, "not_your_performer"));
            return;
        }
        if (!hasScript(player, SCRIPT_AUDIENCE))
        {
            int attachResult = attachScript(player, SCRIPT_AUDIENCE);
            if (attachResult != SCRIPT_CONTINUE ||
                !hasScript(player, SCRIPT_AUDIENCE))
            {
                sendSystemMessage(
                    player,
                    new string_id(STF, "buff_not_available"));
                return;
            }
        }
        dictionary params = new dictionary();
        params.put("performer", performer);
        messageTo(
            player,
            "handlePrivateEntertainerWatch",
            params,
            0,
            false);
    }

    public static void beginListening(obj_id player, obj_id performer)
        throws InterruptedException
    {
        if (!canPlayerUse(player, performer, TYPE_MUSICIAN))
        {
            sendSystemMessage(player, new string_id(STF, "not_your_performer"));
            return;
        }
        if (!hasScript(player, SCRIPT_AUDIENCE))
        {
            int attachResult = attachScript(player, SCRIPT_AUDIENCE);
            if (attachResult != SCRIPT_CONTINUE ||
                !hasScript(player, SCRIPT_AUDIENCE))
            {
                sendSystemMessage(
                    player,
                    new string_id(STF, "buff_not_available"));
                return;
            }
        }
        dictionary params = new dictionary();
        params.put("performer", performer);
        messageTo(
            player,
            "handlePrivateEntertainerListen",
            params,
            0,
            false);
    }

    /**
     * Must run from a player-owned script handler. Trigger-volume helpers use
     * getSelf(), so this context check prevents a performer radial handler from
     * accidentally putting native audience state on the NPC.
     */
    public static boolean activateWatching(
        obj_id player,
        obj_id performer) throws InterruptedException
    {
        if (getSelf() != player ||
            !canPlayerUse(player, performer, TYPE_DANCER))
        {
            return false;
        }
        obj_id current = getPerformanceWatchTarget(player);
        if (isIdValid(current))
        {
            performance.stopWatch(player);
        }
        session.logActivity(player, session.ACTIVITY_BEEN_ENTERTAINED);
        setPerformanceWatchTarget(player, performer);
        listenToMessage(performer, "handlePerformerStopPerforming");
        createTriggerVolume(
            "performance_watch_volume",
            performance.PERFORMANCE_HEAL_RANGE,
            true);
        addTriggerVolumeEventSource(
            "performance_watch_volume", performer);
        performance.startEntertainingPlayer(player);
        utils.setScriptVar(
            player, "performance.dancerBuffTimer", getGameTime());
        if (!hasScript(player, performance.PERFORMANCE_ENTERTAINED_SCRIPT))
        {
            attachScript(
                player, performance.PERFORMANCE_ENTERTAINED_SCRIPT);
        }
        sendSystemMessage(player, new string_id(STF, "watch_started"));
        return true;
    }

    /** See activateWatching(). */
    public static boolean activateListening(
        obj_id player,
        obj_id performer) throws InterruptedException
    {
        if (getSelf() != player ||
            !canPlayerUse(player, performer, TYPE_MUSICIAN))
        {
            return false;
        }
        obj_id current = getPerformanceListenTarget(player);
        if (isIdValid(current))
        {
            performance.stopListen(player);
        }
        session.logActivity(player, session.ACTIVITY_BEEN_ENTERTAINED);
        setPerformanceListenTarget(player, performer);
        listenToMessage(performer, "handlePerformerStopPerforming");
        createTriggerVolume(
            "performance_listen_volume",
            performance.PERFORMANCE_HEAL_RANGE,
            true);
        addTriggerVolumeEventSource(
            "performance_listen_volume", performer);
        performance.startEntertainingPlayer(player);
        utils.setScriptVar(
            player, "performance.musicBuffTimer", getGameTime());
        if (!hasScript(player, performance.PERFORMANCE_ENTERTAINED_SCRIPT))
        {
            attachScript(
                player, performance.PERFORMANCE_ENTERTAINED_SCRIPT);
        }
        sendSystemMessage(player, new string_id(STF, "listen_started"));
        return true;
    }

    public static void cleanupAudience(obj_id performer)
        throws InterruptedException
    {
        if (!isIdValid(performer) || !hasObjVar(performer, VAR_OWNER))
        {
            return;
        }
        obj_id owner = getObjIdObjVar(performer, VAR_OWNER);
        if (!isIdValid(owner) || !owner.isLoaded())
        {
            return;
        }
        if (!hasScript(owner, SCRIPT_AUDIENCE))
        {
            attachScript(owner, SCRIPT_AUDIENCE);
        }
        dictionary params = new dictionary();
        params.put("performer", performer);
        messageTo(
            owner,
            "handlePrivateEntertainerAudienceCleanup",
            params,
            0,
            false);
    }

    public static void cleanupAudienceNow(
        obj_id player,
        obj_id performer) throws InterruptedException
    {
        if (getSelf() != player || !isIdValid(player) ||
            !isIdValid(performer))
        {
            return;
        }
        if (getPerformanceWatchTarget(player) == performer)
        {
            performance.stopWatch(player);
            utils.removeScriptVar(
                player, "performance.dancerBuffTimer");
        }
        if (getPerformanceListenTarget(player) == performer)
        {
            performance.stopListen(player);
            utils.removeScriptVar(
                player, "performance.musicBuffTimer");
        }
    }

    public static boolean hasPrivateAudienceState(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return false;
        }
        obj_id watchTarget = getPerformanceWatchTarget(player);
        obj_id listenTarget = getPerformanceListenTarget(player);
        return isPrivatePerformerForPlayer(player, watchTarget) ||
            isPrivatePerformerForPlayer(player, listenTarget) ||
            hasObjVar(player, VAR_ACTIVE_DANCER) ||
            hasObjVar(player, VAR_ACTIVE_MUSICIAN);
    }

    public static boolean isPrivatePerformerForPlayer(
        obj_id player,
        obj_id performer) throws InterruptedException
    {
        return isIdValid(player) && isIdValid(performer) &&
            exists(performer) && hasScript(performer, SCRIPT_PERFORMER) &&
            hasObjVar(performer, VAR_OWNER) &&
            getObjIdObjVar(performer, VAR_OWNER) == player;
    }

    public static boolean canApplyConfiguredBuff(
        obj_id player,
        String type) throws InterruptedException
    {
        if (!isIdValid(player) || !player.isLoaded() ||
            !player.isAuthoritative() || !isPlayer(player) ||
            isDead(player) || isIncapacitated(player) ||
            !isSupportedType(type) || BUFF_STRENGTH_PERCENT <= 0.0f ||
            BUFF_DURATION_SECONDS <= 0.0f)
        {
            return false;
        }
        if (TYPE_DANCER.equals(type))
        {
            int expectedMind = Math.round(
                getUnmodifiedMaxAttrib(player, MIND) *
                (BUFF_STRENGTH_PERCENT / 100.0f));
            return expectedMind > 0 && getNamedModifierValue(
                player,
                MIND,
                performance.PRECU_DANCE_MIND_BUFF) <= expectedMind;
        }

        int expectedFocus = Math.round(
            getUnmodifiedMaxAttrib(player, FOCUS) *
            (BUFF_STRENGTH_PERCENT / 100.0f));
        int expectedWillpower = Math.round(
            getUnmodifiedMaxAttrib(player, WILLPOWER) *
            (BUFF_STRENGTH_PERCENT / 100.0f));
        return expectedFocus > 0 && expectedWillpower > 0 &&
            getNamedModifierValue(
                player,
                FOCUS,
                performance.PRECU_MUSIC_FOCUS_BUFF) <= expectedFocus &&
            getNamedModifierValue(
                player,
                WILLPOWER,
                performance.PRECU_MUSIC_WILLPOWER_BUFF) <=
                expectedWillpower;
    }

    public static boolean applyConfiguredBuff(
        obj_id player,
        String type) throws InterruptedException
    {
        if (!canApplyConfiguredBuff(player, type))
        {
            return false;
        }
        String performanceType = TYPE_DANCER.equals(type) ?
            performance.PERFORMANCE_TYPE_DANCE :
            performance.PERFORMANCE_TYPE_MUSIC;
        return performance.applyPrecuEntertainerAttributeBuff(
            player,
            performanceType,
            BUFF_STRENGTH_PERCENT,
            BUFF_DURATION_SECONDS);
    }

    public static boolean beginPaidBuff(
        obj_id player,
        obj_id performer,
        String selectionNonce) throws InterruptedException
    {
        String type = isIdValid(performer) && hasObjVar(performer, VAR_TYPE) ?
            getStringObjVar(performer, VAR_TYPE) : "";
        if (!canPlayerUse(player, performer, type) ||
            !canApplyConfiguredBuff(player, type) ||
            selectionNonce == null || selectionNonce.length() < 8)
        {
            sendSystemMessage(
                player,
                new string_id(STF, "buff_not_available"));
            return false;
        }
        if (hasObjVar(player, PAYMENT_STATE))
        {
            sendSystemMessage(player, new string_id(STF, "buff_busy"));
            return false;
        }
        if (!money.hasFunds(player, money.MT_TOTAL, BUFF_PRICE))
        {
            sendSystemMessage(
                player,
                new string_id(STF, "insufficient_funds"));
            return false;
        }

        String nonce = selectionNonce + ":" + player + ":" + performer +
            ":" + getCalendarTime() + ":" + rand(1, 2000000000);
        setObjVar(player, PAYMENT_STATE, PAYMENT_PENDING);
        setObjVar(player, PAYMENT_PERFORMER, performer);
        setObjVar(player, PAYMENT_TYPE, type);
        setObjVar(player, PAYMENT_NONCE, nonce);
        setObjVar(player, PAYMENT_STARTED, getCalendarTime());
        setObjVar(player, PAYMENT_REFUND_ATTEMPTS, 0);
        // A fresh attach guarantees OnAttach schedules this operation's
        // timeout even if an orphaned coordinator script survived cleanup.
        if (hasScript(player, SCRIPT_PAYMENT))
        {
            detachScript(player, SCRIPT_PAYMENT);
        }
        int attachResult = attachScript(player, SCRIPT_PAYMENT);
        if (attachResult != SCRIPT_CONTINUE ||
            !hasScript(player, SCRIPT_PAYMENT))
        {
            cleanupPaymentSession(player);
            sendSystemMessage(
                player,
                new string_id(STF, "payment_unavailable"));
            return false;
        }

        dictionary payment = new dictionary();
        payment.put("private_performer", performer);
        payment.put("private_type", type);
        payment.put("private_nonce", nonce);
        payment.put("private_price", BUFF_PRICE);
        payment.put("npc", player);
        boolean requested = money.requestPayment(
            player,
            money.ACCT_PERFORM_ESCROW,
            BUFF_PRICE,
            "handlePrivateEntertainerPayment",
            payment,
            false);
        if (!requested)
        {
            cleanupPaymentSession(player);
            sendSystemMessage(
                player,
                new string_id(STF, "payment_unavailable"));
            return false;
        }
        return true;
    }

    public static boolean completePaidBuff(
        obj_id player,
        dictionary params) throws InterruptedException
    {
        if (getSelf() != player ||
            !paymentEnvelopeMatches(player, params))
        {
            return false;
        }

        if (params.getInt(money.DICT_CODE) != money.RET_SUCCESS)
        {
            sendSystemMessage(
                player,
                new string_id(STF, "insufficient_funds"));
            cleanupPaymentSession(player);
            return false;
        }

        String state = getStringObjVar(player, PAYMENT_STATE);
        if (PAYMENT_TIMED_OUT.equals(state))
        {
            return beginPaymentRefund(player, true);
        }
        setObjVar(player, PAYMENT_STATE, PAYMENT_SETTLING);
        obj_id performer = getObjIdObjVar(
            player, PAYMENT_PERFORMER);
        String type = getStringObjVar(player, PAYMENT_TYPE);
        boolean applied = canPlayerUse(player, performer, type) &&
            applyConfiguredBuff(player, type);
        if (applied)
        {
            sendSystemMessage(
                player,
                new string_id(STF, "buff_applied_" + type));
            cleanupPaymentSession(player);
            return true;
        }

        return beginPaymentRefund(player, false);
    }

    public static boolean paymentEnvelopeMatches(
        obj_id player,
        dictionary params) throws InterruptedException
    {
        if (!isIdValid(player) || params == null || params.isEmpty() ||
            !hasObjVar(player, PAYMENT_STATE) ||
            !hasObjVar(player, PAYMENT_PERFORMER) ||
            !hasObjVar(player, PAYMENT_TYPE) ||
            !hasObjVar(player, PAYMENT_NONCE) ||
            !params.containsKey(money.DICT_CODE) ||
            !params.containsKey(money.DICT_PLAYER_ID) ||
            !params.containsKey(money.DICT_TARGET_ID) ||
            !params.containsKey(money.DICT_ACCT_NAME) ||
            !params.containsKey(money.DICT_AMOUNT) ||
            !params.containsKey(money.DICT_TOTAL) ||
            !params.containsKey(money.DICT_HANDLER) ||
            !params.containsKey(money.DICT_PAY_HANDLER) ||
            !params.containsKey(money.DICT_NOTIFY) ||
            !params.containsKey("private_performer") ||
            !params.containsKey("private_type") ||
            !params.containsKey("private_nonce") ||
            !params.containsKey("private_price") ||
            !params.containsKey("npc"))
        {
            return false;
        }
        String state = getStringObjVar(player, PAYMENT_STATE);
        if (!PAYMENT_PENDING.equals(state) &&
            !PAYMENT_TIMED_OUT.equals(state))
        {
            return false;
        }
        String expectedType = getStringObjVar(player, PAYMENT_TYPE);
        String expectedNonce = getStringObjVar(player, PAYMENT_NONCE);
        String envelopeType = params.getString("private_type");
        String envelopeNonce = params.getString("private_nonce");
        String transportHandler = params.getString(money.DICT_HANDLER);
        // Mixed cash/bank payments rewrite DICT_AMOUNT to the cash deposit
        // and DICT_HANDLER to handlePayDeposit; DICT_TOTAL, PAY_HANDLER, and
        // the private price retain the exact requested payment contract.
        int transferAmount = params.getInt(money.DICT_AMOUNT);
        return isSupportedType(expectedType) &&
            expectedType.equals(envelopeType) &&
            expectedNonce != null && expectedNonce.equals(envelopeNonce) &&
            params.getObjId(money.DICT_PLAYER_ID) == player &&
            params.getObjId(money.DICT_TARGET_ID) == obj_id.NULL_ID &&
            money.ACCT_PERFORM_ESCROW.equals(
                params.getString(money.DICT_ACCT_NAME)) &&
            ("handlePrivateEntertainerPayment".equals(
                transportHandler) ||
                money.HANDLER_PAY_DEPOSIT.equals(transportHandler)) &&
            "handlePrivateEntertainerPayment".equals(
                params.getString(money.DICT_PAY_HANDLER)) &&
            !params.getBoolean(money.DICT_NOTIFY) &&
            params.getObjId("npc") == player &&
            (params.getInt(money.DICT_CODE) == money.RET_SUCCESS ||
                params.getInt(money.DICT_CODE) == money.RET_FAIL) &&
            transferAmount > 0 && transferAmount <= BUFF_PRICE &&
            params.getInt(money.DICT_TOTAL) == BUFF_PRICE &&
            params.getInt("private_price") == BUFF_PRICE &&
            params.getObjId("private_performer") ==
                getObjIdObjVar(player, PAYMENT_PERFORMER);
    }

    public static boolean markPaymentTimedOut(
        obj_id player,
        String nonce) throws InterruptedException
    {
        if (getSelf() != player || !isIdValid(player) ||
            !hasObjVar(player, PAYMENT_STATE) ||
            !PAYMENT_PENDING.equals(
                getStringObjVar(player, PAYMENT_STATE)) ||
            !hasObjVar(player, PAYMENT_NONCE) || nonce == null ||
            !nonce.equals(getStringObjVar(player, PAYMENT_NONCE)))
        {
            return false;
        }
        setObjVar(player, PAYMENT_STATE, PAYMENT_TIMED_OUT);
        sendSystemMessage(
            player,
            new string_id(STF, "payment_timeout"));
        return true;
    }

    public static boolean beginPaymentRefund(
        obj_id player,
        boolean latePayment) throws InterruptedException
    {
        if (getSelf() != player || !isIdValid(player) ||
            !hasObjVar(player, PAYMENT_STATE) ||
            !hasObjVar(player, PAYMENT_NONCE))
        {
            return false;
        }
        String state = getStringObjVar(player, PAYMENT_STATE);
        if (!PAYMENT_SETTLING.equals(state) &&
            !PAYMENT_TIMED_OUT.equals(state) &&
            !PAYMENT_REFUND_FAILED.equals(state))
        {
            return false;
        }

        int attempts = hasObjVar(player, PAYMENT_REFUND_ATTEMPTS) ?
            getIntObjVar(player, PAYMENT_REFUND_ATTEMPTS) : 0;
        String nonce = getStringObjVar(player, PAYMENT_NONCE);
        setObjVar(player, PAYMENT_REFUND_ATTEMPTS, attempts + 1);
        setObjVar(player, PAYMENT_LATE_REFUND, latePayment);
        setObjVar(player, PAYMENT_STATE, PAYMENT_REFUNDING);
        dictionary refund = new dictionary();
        refund.put(money.DICT_PLAYER_ID, player);
        refund.put(money.DICT_TARGET_ID, player);
        refund.put(money.DICT_ACCT_NAME, money.ACCT_PERFORM_ESCROW);
        refund.put(money.DICT_AMOUNT, BUFF_PRICE);
        refund.put(money.DICT_TOTAL, BUFF_PRICE);
        refund.put("private_refund_nonce", nonce);
        refund.put("private_refund_price", BUFF_PRICE);
        refund.put("private_refund_late", latePayment);
        boolean dispatched = transferBankCreditsFromNamedAccount(
            money.ACCT_PERFORM_ESCROW,
            player,
            BUFF_PRICE,
            "handlePrivateEntertainerRefundSuccess",
            "handlePrivateEntertainerRefundFailure",
            refund);
        if (!dispatched)
        {
            setObjVar(player, PAYMENT_STATE, PAYMENT_REFUND_FAILED);
            CustomerServiceLog(
                "PrivateEntertainer",
                "Refund dispatch failed for player " + player +
                    ", nonce " + nonce + ".");
            sendSystemMessage(
                player,
                new string_id(STF, "refund_failed"));
        }
        return false;
    }

    public static boolean completePaymentRefund(
        obj_id player,
        dictionary params) throws InterruptedException
    {
        if (getSelf() != player || !isIdValid(player) ||
            params == null || params.isEmpty() ||
            !hasObjVar(player, PAYMENT_STATE) ||
            !PAYMENT_REFUNDING.equals(
                getStringObjVar(player, PAYMENT_STATE)) ||
            !hasObjVar(player, PAYMENT_NONCE) ||
            !params.containsKey(money.DICT_CODE) ||
            !params.containsKey(money.DICT_PLAYER_ID) ||
            !params.containsKey(money.DICT_TARGET_ID) ||
            !params.containsKey(money.DICT_ACCT_NAME) ||
            !params.containsKey(money.DICT_AMOUNT) ||
            !params.containsKey(money.DICT_TOTAL) ||
            !params.containsKey("private_refund_nonce") ||
            !params.containsKey("private_refund_price") ||
            !params.containsKey("private_refund_late") ||
            params.getObjId(money.DICT_PLAYER_ID) != player ||
            params.getObjId(money.DICT_TARGET_ID) != player ||
            !money.ACCT_PERFORM_ESCROW.equals(
                params.getString(money.DICT_ACCT_NAME)) ||
            params.getInt(money.DICT_AMOUNT) != BUFF_PRICE ||
            params.getInt(money.DICT_TOTAL) != BUFF_PRICE ||
            params.getInt("private_refund_price") != BUFF_PRICE ||
            (params.getInt(money.DICT_CODE) != money.RET_SUCCESS &&
                params.getInt(money.DICT_CODE) != money.RET_FAIL))
        {
            return false;
        }
        String nonce = params.getString("private_refund_nonce");
        if (nonce == null ||
            !nonce.equals(getStringObjVar(player, PAYMENT_NONCE)))
        {
            return false;
        }
        if (params.getInt(money.DICT_CODE) == money.RET_SUCCESS)
        {
            boolean late = params.getBoolean("private_refund_late");
            sendSystemMessage(
                player,
                new string_id(
                    STF,
                    late ? "late_payment_refunded" : "buff_refunded"));
            cleanupPaymentSession(player);
            return true;
        }
        setObjVar(player, PAYMENT_STATE, PAYMENT_REFUND_FAILED);
        CustomerServiceLog(
            "PrivateEntertainer",
            "Refund transfer failed for player " + player +
                ", nonce " + nonce + ".");
        sendSystemMessage(
            player,
            new string_id(STF, "refund_failed"));
        return false;
    }

    public static void cleanupPaymentSession(obj_id player)
        throws InterruptedException
    {
        if (!isIdValid(player))
        {
            return;
        }
        removeObjVar(player, PAYMENT_ROOT);
        if (hasScript(player, SCRIPT_PAYMENT))
        {
            detachScript(player, SCRIPT_PAYMENT);
        }
    }

    public static void clearPlayerPointer(
        obj_id player,
        obj_id performer,
        String type) throws InterruptedException
    {
        String activeVar = getPlayerActiveVar(type);
        if (!isIdValid(player) || !player.isLoaded() ||
            activeVar.length() == 0 || !hasObjVar(player, activeVar))
        {
            return;
        }
        if (getObjIdObjVar(player, activeVar) == performer)
        {
            removeObjVar(player, activeVar);
        }
    }

    private static boolean performerMatches(
        obj_id player,
        obj_id performer,
        String type) throws InterruptedException
    {
        return isIdValid(performer) && exists(performer) &&
            hasScript(performer, SCRIPT_PERFORMER) &&
            hasObjVar(performer, VAR_OWNER) &&
            hasObjVar(performer, VAR_TYPE) &&
            hasObjVar(performer, VAR_BUILDING) &&
            hasObjVar(performer, VAR_EXPIRES) &&
            getObjIdObjVar(performer, VAR_OWNER) == player &&
            type.equals(getStringObjVar(performer, VAR_TYPE)) &&
            getCalendarTime() < getIntObjVar(performer, VAR_EXPIRES) &&
            getObjIdObjVar(performer, VAR_BUILDING) ==
                getCantinaBuilding(performer);
    }

    private static boolean hasSideRoom(obj_id building)
        throws InterruptedException
    {
        if (!isIdValid(building))
        {
            return false;
        }
        for (String roomName : SIDE_ROOM_NAMES)
        {
            if (isIdValid(getCellId(building, roomName)))
            {
                return true;
            }
        }
        return false;
    }

    private static int getSideRoomIndex(
        obj_id building,
        obj_id player,
        String type) throws InterruptedException
    {
        int hash = (player.toString() + ":" + type).hashCode();
        int start = (hash & 0x7fffffff) % SIDE_ROOM_NAMES.length;
        for (int offset = 0; offset < SIDE_ROOM_NAMES.length; ++offset)
        {
            int index = (start + offset) % SIDE_ROOM_NAMES.length;
            if (isIdValid(getCellId(building, SIDE_ROOM_NAMES[index])))
            {
                return index;
            }
        }
        return -1;
    }

    private static location getSideRoomLocation(
        obj_id building,
        obj_id player,
        String type) throws InterruptedException
    {
        int index = getSideRoomIndex(building, player, type);
        if (index < 0)
        {
            return null;
        }
        obj_id cell = getCellId(building, SIDE_ROOM_NAMES[index]);
        return new location(
            SIDE_ROOM_X[index],
            SIDE_ROOM_Y[index],
            SIDE_ROOM_Z[index],
            getLocation(building).area,
            cell);
    }

    private static float getSideRoomYaw(
        obj_id building,
        obj_id player,
        String type) throws InterruptedException
    {
        int index = getSideRoomIndex(building, player, type);
        return index < 0 ? 0.0f : SIDE_ROOM_YAW[index];
    }

    private static int getNamedModifierValue(
        obj_id target,
        int attribute,
        String name) throws InterruptedException
    {
        attrib_mod[] modifiers = getAttribModifiers(target, attribute);
        if (modifiers != null)
        {
            for (attrib_mod modifier : modifiers)
            {
                if (modifier != null && name.equals(modifier.getName()))
                {
                    return modifier.getValue();
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}
