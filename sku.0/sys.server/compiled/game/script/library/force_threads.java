package script.library;

import script.*;

import java.util.Vector;

public class force_threads extends script.base_script
{
    public static final int SCHEMA_VERSION = 1;
    public static final int MINIMUM_SHELTER_SECONDS = 180;
    public static final int TOKEN_TTL_SECONDS = 259200;
    public static final int DWELL_SECONDS = 180;
    public static final int TOKEN_LIFETIME_SECONDS = 259200;
    public static final int MAX_DEPTH = 3;
    public static final int MAX_ACTIVE_TOKENS = 8;
    public static final int MAX_OUTBOX_RECORDS = 8;
    public static final int MAX_LEDGER_RECORDS = 32;
    public static final int MAX_DEDUPE_IDS = 64;
    public static final int MAX_MAILBOX_RECORDS = 16;
    public static final int MAX_PERSISTENT_BYTES = 16384;
    public static final int MAX_CAMP_VISITORS = MAX_MAILBOX_RECORDS;

    public static final String PLAYER_SCRIPT = "player.reborn.force_threads_player";
    public static final String CAMP_PROBE_SCRIPT = "systems.reborn.force_threads.camp_probe";
    public static final String CAMP_VOLUME = "campsite";
    public static final String PERSISTENT_ROOT = "reborn.forceThreads";
    public static final String VAR_SCHEMA = PERSISTENT_ROOT + ".schema";
    public static final String VAR_STATE = PERSISTENT_ROOT + ".state";
    public static final String VAR_TOKENS = PERSISTENT_ROOT + ".tokens";
    public static final String VAR_LEDGER = PERSISTENT_ROOT + ".ledger";
    public static final String VAR_OUTBOX = PERSISTENT_ROOT + ".outbox";
    public static final String VAR_DEDUPE = PERSISTENT_ROOT + ".dedupe";
    public static final String VAR_QUARANTINE = PERSISTENT_ROOT + ".quarantineReason";
    public static final String VAR_LAST_RECONCILE = PERSISTENT_ROOT + ".lastReconcile";

    public static final String DEED_ORIGIN = PERSISTENT_ROOT + ".origin";
    public static final String DEED_ORIGIN_STATION = PERSISTENT_ROOT + ".originStation";
    public static final String DEED_SCHEMA = VAR_SCHEMA;
    public static final String CAMP_SCHEMA = VAR_SCHEMA;
    public static final String CAMP_READY = PERSISTENT_ROOT + ".ready";
    public static final String CAMP_SESSION = PERSISTENT_ROOT + ".session";
    public static final String CAMP_ORIGIN = PERSISTENT_ROOT + ".origin";
    public static final String CAMP_ORIGIN_STATION = PERSISTENT_ROOT + ".originStation";
    public static final String CAMP_DEPLOYER = PERSISTENT_ROOT + ".deployer";
    public static final String CAMP_DEPLOYER_STATION = PERSISTENT_ROOT + ".deployerStation";
    public static final String CAMP_VISITORS = PERSISTENT_ROOT + ".visitors";

    private static final String CWD_MANAGER = "reborn_force_threads";
    private static final String DELIVERY_PREFIX = "delivery_";
    private static final String ACK_PREFIX = "ack_";
    private static final String REQUEST_PREFIX = PERSISTENT_ROOT + ".request.";
    private static final String REQUEST_DELIVERY = "delivery";
    private static final String REQUEST_ACK = "ack";

    private static final String[] ADVANCED_CAMP_DEEDS =
    {
        "object/tangible/deed/camp_deed/camp_advanced_deed.iff",
        "object/tangible/deed/camp_deed/camp_basic_deed.iff",
        "object/tangible/deed/camp_deed/camp_elite_deed.iff",
        "object/tangible/deed/camp_deed/camp_improved_deed.iff",
        "object/tangible/deed/camp_deed/camp_luxury_deed.iff",
        "object/tangible/deed/camp_deed/camp_quality_deed.iff"
    };

    private static final int[] OUTCOME_QUESTS =
    {
        getStringCrc("quest/purvis_recon_one"),
        getStringCrc("quest/purvis_kill_warriors"),
        getStringCrc("quest/purvis_kill_soldiers")
    };

    public static boolean isShadowEnabled() throws InterruptedException
    {
        String mode = getConfigSetting("GameServer", "rebornForceThreadsMode");
        return "shadow".equals(mode);
    }

    public static void attachPlayerReceiver(obj_id player) throws InterruptedException
    {
        if (!isShadowEnabled() || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }
        ensurePlayerState(player);
        if (!hasScript(player, PLAYER_SCRIPT))
        {
            attachScript(player, PLAYER_SCRIPT);
        }
        reconcile(player);
    }

    public static void stampCraftedCampDeed(obj_id newObject, obj_id player) throws InterruptedException
    {
        if (!isShadowEnabled() || !isIdValid(newObject) || !isAdvancedCampDeed(newObject))
        {
            return;
        }
        obj_id origin = getCrafter(newObject);
        if (!isIdValid(origin) && isIdValid(player) && isPlayer(player))
        {
            origin = player;
        }
        if (!isIdValid(origin))
        {
            return;
        }
        int stationId = getPlayerStationId(origin);
        if (stationId <= 0 && origin == player)
        {
            stationId = getPlayerStationId(player);
        }
        if (stationId <= 0)
        {
            return;
        }
        setObjVar(newObject, DEED_ORIGIN, origin);
        setObjVar(newObject, DEED_ORIGIN_STATION, stationId);
        setObjVar(newObject, DEED_SCHEMA, SCHEMA_VERSION);
    }

    public static void initializeAdvancedCamp(obj_id deed, obj_id camp, obj_id deployer) throws InterruptedException
    {
        if (!isShadowEnabled() || !isIdValid(deed) || !isIdValid(camp) || !isIdValid(deployer) || !isPlayer(deployer))
        {
            return;
        }
        if (!hasObjVar(deed, DEED_ORIGIN) || !hasObjVar(deed, DEED_ORIGIN_STATION) || getIntObjVar(deed, DEED_SCHEMA) != SCHEMA_VERSION)
        {
            return;
        }
        obj_id origin = getObjIdObjVar(deed, DEED_ORIGIN);
        int originStation = getIntObjVar(deed, DEED_ORIGIN_STATION);
        int deployerStation = getPlayerStationId(deployer);
        if (!isIdValid(origin) || originStation <= 0 || deployerStation <= 0 || originStation == deployerStation)
        {
            return;
        }
        String session = "c1-" + camp + "-" + getCalendarTime();
        setObjVar(camp, CAMP_SCHEMA, SCHEMA_VERSION);
        setObjVar(camp, CAMP_SESSION, session);
        setObjVar(camp, CAMP_ORIGIN, origin);
        setObjVar(camp, CAMP_ORIGIN_STATION, originStation);
        setObjVar(camp, CAMP_DEPLOYER, deployer);
        setObjVar(camp, CAMP_DEPLOYER_STATION, deployerStation);
        if (!hasScript(camp, CAMP_PROBE_SCRIPT))
        {
            attachScript(camp, CAMP_PROBE_SCRIPT);
        }
        setObjVar(camp, CAMP_READY, 1);
    }

    public static void beginAdvancedCampVisit(obj_id camp, obj_id visitor) throws InterruptedException
    {
        if (!isReadyCamp(camp) || !isIdValid(visitor) || !isPlayer(visitor))
        {
            return;
        }
        int stationId = getPlayerStationId(visitor);
        int originStation = getIntObjVar(camp, CAMP_ORIGIN_STATION);
        int deployerStation = getIntObjVar(camp, CAMP_DEPLOYER_STATION);
        if (stationId <= 0 || stationId == originStation || stationId == deployerStation)
        {
            return;
        }
        String[] records = getArray(camp, CAMP_VISITORS);
        records = removeVisitor(records, visitor);
        if (records.length >= MAX_CAMP_VISITORS)
        {
            return;
        }
        int enteredAt = getCalendarTime();
        records = append(records, visitor + "|" + stationId + "|" + enteredAt + "|0", MAX_CAMP_VISITORS);
        writeArray(camp, CAMP_VISITORS, records);
        dictionary params = new dictionary();
        params.put("visitor", visitor);
        params.put("enteredAt", enteredAt);
        params.put("session", getStringObjVar(camp, CAMP_SESSION));
        messageTo(camp, "handleForceThreadsDwell", params, DWELL_SECONDS, false);
    }

    public static void endAdvancedCampVisit(obj_id camp, obj_id visitor) throws InterruptedException
    {
        if (!isReadyCamp(camp) || !isIdValid(visitor))
        {
            return;
        }
        writeArray(camp, CAMP_VISITORS, removeVisitor(getArray(camp, CAMP_VISITORS), visitor));
    }

    public static void observeAdvancedCampHealing(obj_id player, int actualDelta) throws InterruptedException
    {
        if (!isShadowEnabled() || actualDelta <= 0 || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }
        obj_id camp = camping.getCurrentAdvancedCamp(player);
        if (!isReadyCamp(camp) || !isInTriggerVolume(camp, CAMP_VOLUME, player))
        {
            return;
        }
        String[] records = getArray(camp, CAMP_VISITORS);
        for (int i = 0; i < records.length; ++i)
        {
            String[] fields = split(records[i], '|');
            if (fields.length == 4 && player == utils.stringToObjId(fields[0]))
            {
                records[i] = fields[0] + "|" + fields[1] + "|" + fields[2] + "|1";
                writeArray(camp, CAMP_VISITORS, records);
                return;
            }
        }
    }

    public static void completeAdvancedCampDwell(obj_id camp, dictionary params) throws InterruptedException
    {
        if (!isReadyCamp(camp) || params == null)
        {
            return;
        }
        obj_id visitor = params.getObjId("visitor");
        int enteredAt = params.getInt("enteredAt");
        String session = params.getString("session");
        if (!isIdValid(visitor) || !isPlayer(visitor) || session == null || !session.equals(getStringObjVar(camp, CAMP_SESSION)))
        {
            return;
        }
        if (getCalendarTime() - enteredAt < DWELL_SECONDS || !isInTriggerVolume(camp, CAMP_VOLUME, visitor) || camping.getCurrentAdvancedCamp(visitor) != camp)
        {
            return;
        }
        String[] records = getArray(camp, CAMP_VISITORS);
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length != 4 || visitor != utils.stringToObjId(fields[0]) || utils.stringToInt(fields[2]) != enteredAt || !fields[3].equals("1"))
            {
                continue;
            }
            int holderStation = utils.stringToInt(fields[1]);
            int originStation = getIntObjVar(camp, CAMP_ORIGIN_STATION);
            int deployerStation = getIntObjVar(camp, CAMP_DEPLOYER_STATION);
            if (holderStation <= 0 || originStation <= 0 || deployerStation <= 0 || holderStation == originStation || holderStation == deployerStation || originStation == deployerStation)
            {
                return;
            }
            int createdAt = getCalendarTime();
            String tokenId = "t1-" + camp + "-" + visitor + "-" + enteredAt;
            String token = "1|" + tokenId + "|" + getObjIdObjVar(camp, CAMP_ORIGIN) + "|" + originStation + "|" + getObjIdObjVar(camp, CAMP_DEPLOYER) + "|" + deployerStation + "|" + visitor + "|" + holderStation + "|" + createdAt + "|" + (createdAt + TOKEN_LIFETIME_SECONDS) + "|PROVENANCE>SHELTER";
            addToken(visitor, token);
            writeArray(camp, CAMP_VISITORS, removeVisitor(records, visitor));
            return;
        }
    }

    public static void observeOutcome(obj_id player, int questCrc) throws InterruptedException
    {
        if (!isShadowEnabled() || !isAllowedOutcome(questCrc) || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }
        ensurePlayerState(player);
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return;
        }
        String[] storedTokens = getArray(player, VAR_TOKENS);
        if (!areValidTokens(storedTokens))
        {
            quarantine(player, "token-schema");
            return;
        }
        String[] tokens = pruneTokens(storedTokens);
        writeArray(player, VAR_TOKENS, tokens);
        if (tokens.length == 0)
        {
            return;
        }
        String[] fields = split(tokens[0], '|');
        if (fields.length != 11 || !fields[0].equals("1") || !fields[10].equals("PROVENANCE>SHELTER"))
        {
            quarantine(player, "token-schema");
            return;
        }
        int playerStation = getPlayerStationId(player);
        int originStation = utils.stringToInt(fields[3]);
        int deployerStation = utils.stringToInt(fields[5]);
        int holderStation = utils.stringToInt(fields[7]);
        if (player != utils.stringToObjId(fields[6]) || playerStation <= 0 || playerStation != holderStation || originStation <= 0 || deployerStation <= 0 || originStation == deployerStation || originStation == holderStation || deployerStation == holderStation)
        {
            quarantine(player, "token-identity");
            return;
        }
        String eventId = "e1-" + fields[1] + "-" + questCrc;
        String event = "1|" + eventId + "|" + fields[2] + "|" + originStation + "|" + fields[4] + "|" + deployerStation + "|" + player + "|" + holderStation + "|" + fields[1] + "|" + questCrc + "|" + getCalendarTime() + "|PROVENANCE>SHELTER>OUTCOME";
        String[] outbox = getArray(player, VAR_OUTBOX);
        if (!areValidOutboxRecords(outbox, player))
        {
            quarantine(player, "outbox-schema");
            return;
        }
        if (containsTokenReference(outbox, fields[1]))
        {
            writeArray(player, VAR_TOKENS, removeTokenById(tokens, fields[1]));
            return;
        }
        if (containsEvent(outbox, eventId) || outbox.length >= MAX_OUTBOX_RECORDS)
        {
            return;
        }
        outbox = append(outbox, event, MAX_OUTBOX_RECORDS);
        if (!writeArray(player, VAR_OUTBOX, outbox) || !containsEvent(getArray(player, VAR_OUTBOX), eventId))
        {
            return;
        }
        if (!writeArray(player, VAR_TOKENS, removeTokenById(tokens, fields[1])) || containsTokenId(getArray(player, VAR_TOKENS), fields[1]))
        {
            return;
        }
        publishDelivery(event);
    }

    public static void reconcile(obj_id player) throws InterruptedException
    {
        if (!isShadowEnabled() || !isIdValid(player) || !isPlayer(player))
        {
            return;
        }
        ensurePlayerState(player);
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return;
        }
        setObjVar(player, VAR_LAST_RECONCILE, getCalendarTime());
        String[] tokens = getArray(player, VAR_TOKENS);
        if (!areValidTokens(tokens))
        {
            quarantine(player, "token-schema");
            return;
        }
        String[] outbox = getArray(player, VAR_OUTBOX);
        if (!areValidOutboxRecords(outbox, player))
        {
            quarantine(player, "outbox-schema");
            return;
        }
        for (String event : outbox)
        {
            String[] fields = split(event, '|');
            tokens = removeTokenById(tokens, fields[8]);
            if (!writeArray(player, VAR_TOKENS, tokens) || containsTokenId(getArray(player, VAR_TOKENS), fields[8]))
            {
                return;
            }
            publishDelivery(event);
        }
        int deliveryRequest = getClusterWideData(CWD_MANAGER, DELIVERY_PREFIX + player + "_*", true, player);
        int ackRequest = getClusterWideData(CWD_MANAGER, ACK_PREFIX + player + "_*", true, player);
        utils.setScriptVar(player, REQUEST_PREFIX + deliveryRequest, REQUEST_DELIVERY);
        utils.setScriptVar(player, REQUEST_PREFIX + ackRequest, REQUEST_ACK);
    }

    public static void handleClusterResponse(obj_id player, String manager, int requestId, String[] names, dictionary[] data, int lockKey) throws InterruptedException
    {
        if (!isShadowEnabled())
        {
            releaseOwnedClusterResponse(player, manager, requestId, lockKey);
            return;
        }
        String requestPath = REQUEST_PREFIX + requestId;
        String requestKind = utils.getStringScriptVar(player, requestPath);
        if (!CWD_MANAGER.equals(manager) || (!REQUEST_DELIVERY.equals(requestKind) && !REQUEST_ACK.equals(requestKind)))
        {
            return;
        }
        utils.removeScriptVar(player, requestPath);
        try
        {
            if (names == null || data == null || names.length != data.length)
            {
                return;
            }
            if (names.length > MAX_MAILBOX_RECORDS)
            {
                quarantine(player, "mailbox-capacity");
                return;
            }
            for (int i = 0; i < names.length; ++i)
            {
                if (data[i] == null)
                {
                    continue;
                }
                if (REQUEST_DELIVERY.equals(requestKind))
                {
                    acceptDelivery(player, names[i], data[i], lockKey);
                }
                else if (REQUEST_ACK.equals(requestKind))
                {
                    acceptAck(player, names[i], data[i], lockKey);
                }
            }
        }
        finally
        {
            if (lockKey > 0)
            {
                releaseClusterWideDataLock(manager, lockKey);
            }
        }
    }

    public static void releaseOwnedClusterResponse(obj_id player, String manager, int requestId, int lockKey) throws InterruptedException
    {
        String requestPath = REQUEST_PREFIX + requestId;
        String requestKind = utils.getStringScriptVar(player, requestPath);
        if (!CWD_MANAGER.equals(manager) || (!REQUEST_DELIVERY.equals(requestKind) && !REQUEST_ACK.equals(requestKind)))
        {
            return;
        }
        utils.removeScriptVar(player, requestPath);
        if (lockKey > 0)
        {
            releaseClusterWideDataLock(manager, lockKey);
        }
    }

    private static void acceptDelivery(obj_id origin, String name, dictionary data, int lockKey) throws InterruptedException
    {
        ensurePlayerState(origin);
        if (hasObjVar(origin, VAR_QUARANTINE))
        {
            return;
        }
        String event = data.getString("event");
        if (event == null)
        {
            return;
        }
        String[] fields = split(event, '|');
        if (!isValidOutboxRecord(event) || !name.equals(deliveryKey(fields[2], fields[1])))
        {
            quarantine(origin, "delivery-schema");
            return;
        }
        int originStation = utils.stringToInt(fields[3]);
        int deployerStation = utils.stringToInt(fields[5]);
        int carrierStation = utils.stringToInt(fields[7]);
        if (origin != utils.stringToObjId(fields[2]) || getPlayerStationId(origin) != originStation || originStation == deployerStation || originStation == carrierStation || deployerStation == carrierStation)
        {
            quarantine(origin, "delivery-identity");
            return;
        }
        String eventId = fields[1];
        String ledgerRecord = eventId + "|" + fields[6] + "|" + fields[8] + "|" + fields[9] + "|" + fields[10];
        String[] dedupe = getArray(origin, VAR_DEDUPE);
        if (!contains(dedupe, eventId))
        {
            if (dedupe.length >= MAX_DEDUPE_IDS)
            {
                quarantine(origin, "dedupe-capacity");
                return;
            }
            String[] ledger = getArray(origin, VAR_LEDGER);
            if (!contains(ledger, ledgerRecord))
            {
                ledger = appendRolling(ledger, ledgerRecord, MAX_LEDGER_RECORDS);
            }
            dedupe = append(dedupe, eventId, MAX_DEDUPE_IDS);
            if (!writeArray(origin, VAR_LEDGER, ledger) || !writeArray(origin, VAR_DEDUPE, dedupe))
            {
                return;
            }
        }
        else if (!contains(getArray(origin, VAR_LEDGER), ledgerRecord))
        {
            if (!writeArray(origin, VAR_LEDGER, appendRolling(getArray(origin, VAR_LEDGER), ledgerRecord, MAX_LEDGER_RECORDS)))
            {
                return;
            }
        }
        if (!contains(getArray(origin, VAR_DEDUPE), eventId) || !contains(getArray(origin, VAR_LEDGER), ledgerRecord))
        {
            return;
        }
        force_progression.observeAttunement(
            origin,
            eventId,
            force_progression.EVENT_THREAD,
            force_progression.ROUTE_FELLOWSHIP,
            "galactic",
            getCalendarTime());
        dictionary ack = new dictionary();
        ack.put("eventId", eventId);
        replaceClusterWideData(CWD_MANAGER, ackKey(fields[6], eventId), ack, false, 0);
        removeClusterWideData(CWD_MANAGER, name, lockKey);
    }

    private static void acceptAck(obj_id carrier, String name, dictionary data, int lockKey) throws InterruptedException
    {
        String eventId = data.getString("eventId");
        if (eventId == null || !name.equals(ackKey("" + carrier, eventId)))
        {
            return;
        }
        String[] outbox = getArray(carrier, VAR_OUTBOX);
        String[] remaining = removeEvent(outbox, eventId);
        if (remaining.length == outbox.length)
        {
            removeClusterWideData(CWD_MANAGER, name, lockKey);
            return;
        }
        if (writeArray(carrier, VAR_OUTBOX, remaining))
        {
            removeClusterWideData(CWD_MANAGER, name, lockKey);
        }
    }

    private static void publishDelivery(String event) throws InterruptedException
    {
        String[] fields = split(event, '|');
        if (!isValidOutboxRecord(event))
        {
            return;
        }
        dictionary delivery = new dictionary();
        delivery.put("event", event);
        replaceClusterWideData(CWD_MANAGER, deliveryKey(fields[2], fields[1]), delivery, false, 0);
    }

    private static String deliveryKey(String origin, String eventId)
    {
        return DELIVERY_PREFIX + origin + "_" + getStringCrc(eventId);
    }

    private static String ackKey(String carrier, String eventId)
    {
        return ACK_PREFIX + carrier + "_" + getStringCrc(eventId);
    }

    private static boolean isReadyCamp(obj_id camp) throws InterruptedException
    {
        return isShadowEnabled() && isIdValid(camp) && hasObjVar(camp, CAMP_READY) && getIntObjVar(camp, CAMP_READY) == 1 && getIntObjVar(camp, CAMP_SCHEMA) == SCHEMA_VERSION;
    }

    private static boolean isAdvancedCampDeedTemplate(String template) throws InterruptedException
    {
        for (String allowed : ADVANCED_CAMP_DEEDS)
        {
            if (allowed.equals(template))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdvancedCampDeed(obj_id deed) throws InterruptedException
    {
        return isAdvancedCampDeedTemplate(getTemplateName(deed));
    }

    private static boolean isAllowedOutcome(int questCrc)
    {
        for (int allowed : OUTCOME_QUESTS)
        {
            if (allowed == questCrc)
            {
                return true;
            }
        }
        return false;
    }

    private static void ensurePlayerState(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return;
        }
        if (hasObjVar(player, VAR_SCHEMA) && getIntObjVar(player, VAR_SCHEMA) != SCHEMA_VERSION)
        {
            quarantine(player, "player-schema");
            return;
        }
        setObjVar(player, VAR_SCHEMA, SCHEMA_VERSION);
        setObjVar(player, VAR_STATE, "shadow");
    }

    private static void quarantine(obj_id player, String reason) throws InterruptedException
    {
        setObjVar(player, VAR_QUARANTINE, reason);
        setObjVar(player, VAR_STATE, "quarantined");
    }

    private static void addToken(obj_id player, String token) throws InterruptedException
    {
        ensurePlayerState(player);
        if (hasObjVar(player, VAR_QUARANTINE))
        {
            return;
        }
        String[] storedTokens = getArray(player, VAR_TOKENS);
        if (!areValidTokens(storedTokens))
        {
            quarantine(player, "token-schema");
            return;
        }
        String[] tokens = pruneTokens(storedTokens);
        if (tokens.length >= MAX_ACTIVE_TOKENS)
        {
            return;
        }
        writeArray(player, VAR_TOKENS, append(tokens, token, MAX_ACTIVE_TOKENS));
    }

    private static String[] pruneTokens(String[] tokens) throws InterruptedException
    {
        Vector kept = new Vector();
        int now = getCalendarTime();
        for (String token : tokens)
        {
            String[] fields = split(token, '|');
            if (fields.length == 11 && fields[0].equals("1") && fields[10].equals("PROVENANCE>SHELTER") && utils.stringToInt(fields[9]) >= now)
            {
                kept.add(token);
            }
        }
        return vectorToStrings(kept);
    }

    private static boolean areValidTokens(String[] tokens) throws InterruptedException
    {
        if (tokens.length > MAX_ACTIVE_TOKENS)
        {
            return false;
        }
        for (String token : tokens)
        {
            if (!isValidTokenRecord(token))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidTokenRecord(String token) throws InterruptedException
    {
        if (token == null)
        {
            return false;
        }
        String[] fields = split(token, '|');
        if (fields.length != 11 || !fields[0].equals("1") || fields[1].length() == 0 || !fields[10].equals("PROVENANCE>SHELTER"))
        {
            return false;
        }
        int originStation = utils.stringToInt(fields[3]);
        int deployerStation = utils.stringToInt(fields[5]);
        int holderStation = utils.stringToInt(fields[7]);
        int createdAt = utils.stringToInt(fields[8]);
        int expiresAt = utils.stringToInt(fields[9]);
        return isIdValid(utils.stringToObjId(fields[2])) && isIdValid(utils.stringToObjId(fields[4])) && isIdValid(utils.stringToObjId(fields[6])) && originStation > 0 && deployerStation > 0 && holderStation > 0 && originStation != deployerStation && originStation != holderStation && deployerStation != holderStation && createdAt > 0 && expiresAt == createdAt + TOKEN_LIFETIME_SECONDS;
    }

    private static boolean areValidOutboxRecords(String[] records, obj_id carrier) throws InterruptedException
    {
        if (records.length > MAX_OUTBOX_RECORDS)
        {
            return false;
        }
        for (String record : records)
        {
            if (!isValidOutboxRecord(record))
            {
                return false;
            }
            String[] fields = split(record, '|');
            if (carrier != utils.stringToObjId(fields[6]) || getPlayerStationId(carrier) != utils.stringToInt(fields[7]))
            {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidOutboxRecord(String record) throws InterruptedException
    {
        if (record == null)
        {
            return false;
        }
        String[] fields = split(record, '|');
        if (fields.length != 12 || !fields[0].equals("1") || fields[1].length() == 0 || fields[8].length() == 0 || !fields[11].equals("PROVENANCE>SHELTER>OUTCOME"))
        {
            return false;
        }
        int originStation = utils.stringToInt(fields[3]);
        int deployerStation = utils.stringToInt(fields[5]);
        int carrierStation = utils.stringToInt(fields[7]);
        return isIdValid(utils.stringToObjId(fields[2])) && isIdValid(utils.stringToObjId(fields[4])) && isIdValid(utils.stringToObjId(fields[6])) && originStation > 0 && deployerStation > 0 && carrierStation > 0 && originStation != deployerStation && originStation != carrierStation && deployerStation != carrierStation && isAllowedOutcome(utils.stringToInt(fields[9])) && fields[1].equals("e1-" + fields[8] + "-" + fields[9]) && utils.stringToInt(fields[10]) > 0;
    }

    private static String[] removeVisitor(String[] records, obj_id visitor) throws InterruptedException
    {
        Vector kept = new Vector();
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length != 4 || visitor != utils.stringToObjId(fields[0]))
            {
                kept.add(record);
            }
        }
        return vectorToStrings(kept);
    }

    private static boolean containsEvent(String[] records, String eventId) throws InterruptedException
    {
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length > 1 && fields[1].equals(eventId))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean containsTokenReference(String[] records, String tokenId) throws InterruptedException
    {
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length == 12 && fields[8].equals(tokenId))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean containsTokenId(String[] tokens, String tokenId) throws InterruptedException
    {
        for (String token : tokens)
        {
            String[] fields = split(token, '|');
            if (fields.length == 11 && fields[1].equals(tokenId))
            {
                return true;
            }
        }
        return false;
    }

    private static String[] removeTokenById(String[] tokens, String tokenId) throws InterruptedException
    {
        Vector kept = new Vector();
        for (String token : tokens)
        {
            String[] fields = split(token, '|');
            if (fields.length != 11 || !fields[1].equals(tokenId))
            {
                kept.add(token);
            }
        }
        return vectorToStrings(kept);
    }

    private static String[] removeEvent(String[] records, String eventId) throws InterruptedException
    {
        Vector kept = new Vector();
        for (String record : records)
        {
            String[] fields = split(record, '|');
            if (fields.length <= 1 || !fields[1].equals(eventId))
            {
                kept.add(record);
            }
        }
        return vectorToStrings(kept);
    }

    private static boolean contains(String[] values, String sought)
    {
        for (String value : values)
        {
            if (sought.equals(value))
            {
                return true;
            }
        }
        return false;
    }

    private static String[] getArray(obj_id object, String path) throws InterruptedException
    {
        String[] values = getStringArrayObjVar(object, path);
        return values == null ? new String[0] : values;
    }

    private static boolean writeArray(obj_id object, String path, String[] values) throws InterruptedException
    {
        if (values == null || values.length == 0)
        {
            if (hasObjVar(object, path))
            {
                removeObjVar(object, path);
            }
            return !hasObjVar(object, path);
        }
        return setObjVar(object, path, values);
    }

    private static String[] append(String[] values, String value, int maximum)
    {
        if (values.length >= maximum)
        {
            return values;
        }
        String[] result = new String[values.length + 1];
        System.arraycopy(values, 0, result, 0, values.length);
        result[values.length] = value;
        return result;
    }

    private static String[] appendRolling(String[] values, String value, int maximum)
    {
        if (values.length < maximum)
        {
            return append(values, value, maximum);
        }
        String[] result = new String[maximum];
        System.arraycopy(values, 1, result, 0, maximum - 1);
        result[maximum - 1] = value;
        return result;
    }

    private static String[] removeAt(String[] values, int index)
    {
        String[] result = new String[values.length - 1];
        if (index > 0)
        {
            System.arraycopy(values, 0, result, 0, index);
        }
        if (index < values.length - 1)
        {
            System.arraycopy(values, index + 1, result, index, values.length - index - 1);
        }
        return result;
    }

    private static String[] vectorToStrings(Vector values)
    {
        String[] result = new String[values.size()];
        values.copyInto(result);
        return result;
    }
}
