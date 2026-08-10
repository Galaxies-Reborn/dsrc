package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.create;

/**
 * Identity-bound, reversible live fixture for the native Core3 AI attack
 * interval. Two disposable creatures fight each other so the production
 * command queue is exercised without changing player skills or inventory.
 */
public class precu_ai_attack_interval_runtime extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String ROOT = "precu.aiAttackIntervalRuntime";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String ATTACKER = ROOT + ".attacker";
    private static final String DEFENDER = ROOT + ".defender";
    private static final String QUEUED = ROOT + ".queued";
    private static final String CREATURE_TYPE = "worrt";
    private static final String ATTACK_COMMAND = "meleeHit";
    private static final int ATTACK_COUNT = 6;
    private static final String USAGE =
        "usage: prepare|queue|status|cleanup <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 3 || !isValidLifecycle(args[2]))
            return USAGE;

        long playerOid;
        try
        {
            playerOid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (playerOid != PLAYER_OID)
            return "error=playerIdentityRejected";

        obj_id player = obj_id.getObjId(playerOid);
        if (!isAuthoritativePlayer(player))
            return "error=playerUnavailable";
        if (args[0].equalsIgnoreCase("prepare"))
            return prepare(player, args[2]);
        if (args[0].equalsIgnoreCase("queue"))
            return queueAttack(player, args[2]);
        if (args[0].equalsIgnoreCase("status"))
            return status(player, args[2]);
        if (args[0].equalsIgnoreCase("cleanup"))
            return cleanup(player, args[2]);
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null ? "action=prepare resumed=true " +
                buildStatus(player) : ownership;
        }

        location origin = getWorldLocation(player);
        if (origin == null)
            return "error=playerLocationUnavailable";
        origin = new location(origin);
        origin.cell = obj_id.NULL_ID;

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);

        location attackerLocation = new location(origin);
        attackerLocation.x += 20.0f;
        location defenderLocation = new location(attackerLocation);
        defenderLocation.x += 2.0f;

        obj_id attacker = createFixtureCreature(attackerLocation);
        obj_id defender = createFixtureCreature(defenderLocation);
        if (!isIdValid(attacker) || !isIdValid(defender))
        {
            if (isIdValid(attacker)) destroyObject(attacker);
            if (isIdValid(defender)) destroyObject(defender);
            removeObjVar(player, ROOT);
            return "error=creaturePreparationFailed restored=true";
        }

        setName(attacker, "PRECU AI Cadence Attacker");
        setName(defender, "PRECU AI Cadence Defender");
        fortify(attacker);
        fortify(defender);
        setObjVar(player, ATTACKER, attacker);
        setObjVar(player, DEFENDER, defender);

        startCombat(attacker, defender);
        addToMentalStateToward(
            attacker,
            defender,
            ANGER,
            100,
            BEHAVIOR_ATTACK);
        int queued = 0;
        int commandCrc = getStringCrc(ATTACK_COMMAND.toLowerCase());
        for (int attack = 0; attack < ATTACK_COUNT; ++attack)
        {
            if (queueCommand(
                attacker,
                commandCrc,
                defender,
                "",
                COMMAND_PRIORITY_DEFAULT))
                ++queued;
        }
        setObjVar(player, QUEUED, queued);

        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String queueAttack(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
            return ownership;
        obj_id attacker = readObject(player, ATTACKER);
        obj_id defender = readObject(player, DEFENDER);
        if (!isIdValid(attacker) || !attacker.isLoaded() ||
            !isIdValid(defender) || !defender.isLoaded())
            return "error=fixtureCreatureUnavailable";
        boolean queued = queueCommand(
            attacker,
            getStringCrc(ATTACK_COMMAND.toLowerCase()),
            defender,
            "",
            COMMAND_PRIORITY_DEFAULT);
        if (queued)
            setObjVar(player, QUEUED, getIntObjVar(player, QUEUED) + 1);
        return "action=queue queued=" + queued + " " + buildStatus(player);
    }

    private obj_id createFixtureCreature(location loc)
        throws InterruptedException
    {
        dictionary data = dataTableGetRow(create.CREATURE_TABLE, CREATURE_TYPE);
        if (data == null)
            return obj_id.NULL_ID;
        String template = data.getString("template");
        if (template == null || template.length() == 0)
            return obj_id.NULL_ID;
        data.put("lootTable", "");
        obj_id creature = createObject(create.TEMPLATE_PREFIX + template, loc);
        if (!isIdValid(creature))
            return obj_id.NULL_ID;
        create.randomlyNameCreature(creature, CREATURE_TYPE);
        create.initializeCreature(creature, CREATURE_TYPE, data, -1);
        create.attachCreatureScripts(
            creature,
            data.getString("scripts"),
            true);
        return creature;
    }

    private void fortify(obj_id creature) throws InterruptedException
    {
        for (int attribute = HEALTH; attribute <= MIND; ++attribute)
        {
            setMaxAttrib(creature, attribute, 100000);
            setAttrib(creature, attribute, 100000);
        }
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
            return "action=cleanup alreadyClean=true restored=true";
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
            return ownership;

        destroyTracked(player, ATTACKER);
        destroyTracked(player, DEFENDER);
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true";
    }

    private void destroyTracked(obj_id player, String name)
        throws InterruptedException
    {
        obj_id object = readObject(player, name);
        if (isIdValid(object) && object.isLoaded())
            destroyObject(object);
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id attacker = readObject(player, ATTACKER);
        obj_id defender = readObject(player, DEFENDER);
        return "authoritative=true player=" + player +
            " stationId=" + getPlayerStationId(player) +
            " lifecycle=" + getStringObjVar(player, LIFECYCLE) +
            " attacker=" + attacker +
            " attackerLoaded=" +
                (isIdValid(attacker) && attacker.isLoaded()) +
            " defender=" + defender +
            " defenderLoaded=" +
                (isIdValid(defender) && defender.isLoaded()) +
            " command=" + ATTACK_COMMAND +
            " requested=" + ATTACK_COUNT +
            " queued=" +
                (hasObjVar(player, QUEUED) ?
                    getIntObjVar(player, QUEUED) : 0) +
            " playerStateMutated=false disposableCreatures=true";
    }

    private obj_id readObject(obj_id player, String name)
        throws InterruptedException
    {
        if (!hasObjVar(player, name))
            return obj_id.NULL_ID;
        return getObjIdObjVar(player, name);
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String error = validateLifecycle(player, lifecycle);
        if (error != null)
            return error;
        if (getIntObjVar(player, PROTOCOL) != PROTOCOL_VERSION)
            return "error=protocolMismatch";
        return null;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) ||
            !getStringObjVar(player, LIFECYCLE).equals(lifecycle))
            return "error=lifecycleRejected";
        return null;
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return player != null && player != obj_id.NULL_ID &&
            player.isLoaded() && player.isAuthoritative() &&
            isPlayer(player) &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private boolean isValidLifecycle(String value)
    {
        return value != null && value.matches("[a-f0-9]{32}");
    }
}
