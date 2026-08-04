package script.test;

import script.location;
import script.obj_id;
import script.library.combat;
import script.library.pclib;
import script.library.utils;

/**
 * Layered, ServerConsole-only acceptance fixture for Publish 14.1 death blow.
 *
 * The headShot1 fixture owns the exact player preimages, permanent-enemy
 * flags, regeneration, and final restoration. This layer owns only reversible
 * incapacitated/dead state and six- versus four-meter placement. It never
 * calls the death-blow handler or fabricates its result.
 */
public class precu_death_blow_fixture extends script.base_script
{
    private static final long ATTACKER_OID = 44003778L;
    private static final int ATTACKER_STATION_ID = 91001;
    private static final long VICTIM_OID = 39008597L;
    private static final int VICTIM_STATION_ID = 1001;
    private static final String COMMAND = "deathBlow";
    private static final int FIXTURE_INCAP_HEALTH =
        -100;
    private static final String HEADSHOT_ROOT =
        "precu.p14.headShot1Fixture";
    private static final String HEADSHOT_LIFECYCLE =
        HEADSHOT_ROOT + ".lifecycle";
    private static final String HEADSHOT_PREPARED =
        HEADSHOT_ROOT + ".prepared";
    private static final String ROOT =
        "precu.p14.deathBlowFixture";
    private static final String LIFECYCLE =
        ROOT + ".lifecycle";
    private static final String PEER =
        ROOT + ".peer";
    private static final String PREPARED =
        ROOT + ".prepared";
    private static final String ORIGINAL_COMMAND =
        ROOT + ".originalDeathBlowCommand";
    private static final String USAGE =
        "usage: inspect|recover|prepareFar|armFeign|" +
        "armNear|stabilize|status|cleanup " +
        "44003778 39008597 " +
        "<32-hex-lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args =
            params == null
                ? new String[0]
                : params.trim().split("[ ]+");
        if (args.length != 4 ||
            !args[3].matches("[a-f0-9]{32}"))
        {
            return USAGE;
        }

        long attackerValue;
        long victimValue;
        try
        {
            attackerValue = Long.parseLong(args[1]);
            victimValue = Long.parseLong(args[2]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (attackerValue != ATTACKER_OID ||
            victimValue != VICTIM_OID ||
            attackerValue == victimValue)
        {
            return "error=identityNotAllowed";
        }

        obj_id attacker = obj_id.getObjId(attackerValue);
        obj_id victim = obj_id.getObjId(victimValue);
        String validation =
            validatePlayer(
                attacker,
                ATTACKER_STATION_ID,
                "attacker");
        if (validation != null)
        {
            return validation;
        }
        validation =
            validatePlayer(
                victim,
                VICTIM_STATION_ID,
                "victim");
        if (validation != null)
        {
            return validation;
        }

        String action = args[0];
        String lifecycle = args[3];
        if (action.equalsIgnoreCase("inspect"))
        {
            return "action=inspect " +
                buildStatus(
                    attacker,
                    victim,
                    lifecycle);
        }
        if (action.equalsIgnoreCase("recover"))
        {
            return recover(
                attacker,
                victim,
                lifecycle);
        }
        if (action.equalsIgnoreCase("prepareFar"))
        {
            return prepareFar(
                attacker,
                victim,
                lifecycle);
        }
        if (action.equalsIgnoreCase("armFeign"))
        {
            return arm(
                attacker,
                victim,
                lifecycle,
                4.0f,
                true,
                "feign");
        }
        if (action.equalsIgnoreCase("armNear"))
        {
            return arm(
                attacker,
                victim,
                lifecycle,
                4.0f,
                false,
                "near");
        }
        if (action.equalsIgnoreCase("stabilize"))
        {
            return stabilize(
                attacker,
                victim,
                lifecycle);
        }
        if (action.equalsIgnoreCase("status"))
        {
            validation =
                validateOwnership(
                    attacker,
                    victim,
                    lifecycle,
                    false);
            return validation == null
                ? "action=status " +
                    buildStatus(
                        attacker,
                        victim,
                        lifecycle)
                : validation;
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(
                attacker,
                victim,
                lifecycle);
        }
        return USAGE;
    }

    private String prepareFar(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        String dependency =
            validateHeadShotLayer(
                attacker,
                victim,
                lifecycle);
        if (dependency != null)
        {
            return dependency;
        }
        String ownership =
            validateOwnership(
                attacker,
                victim,
                lifecycle,
                true);
        if (ownership == null)
        {
            if (!hasCommand(attacker, COMMAND) &&
                !grantCommand(attacker, COMMAND))
            {
                return "error=deathBlowCommandGrantFailed";
            }
            return arm(
                attacker,
                victim,
                lifecycle,
                6.0f,
                false,
                "far").replace(
                    "action=arm",
                    "action=prepareFar resumed=true");
        }
        if (!ownership.equals("fixtureAbsent"))
        {
            return ownership;
        }

        setObjVar(attacker, LIFECYCLE, lifecycle);
        setObjVar(attacker, PEER, victim);
        setObjVar(
            attacker,
            ORIGINAL_COMMAND,
            hasCommand(attacker, COMMAND) ? 1 : 0);
        setObjVar(victim, LIFECYCLE, lifecycle);
        setObjVar(victim, PEER, attacker);
        setObjVar(attacker, PREPARED, 1);
        setObjVar(victim, PREPARED, 1);
        if (!hasCommand(attacker, COMMAND) &&
            !grantCommand(attacker, COMMAND))
        {
            String recovery =
                recover(
                    attacker,
                    victim,
                    lifecycle).replace(' ', '_');
            return "error=deathBlowCommandGrantFailed" +
                " recovery=" + recovery;
        }

        String result =
            arm(
                attacker,
                victim,
                lifecycle,
                6.0f,
                false,
                "far");
        return result.startsWith("action=arm")
            ? result.replace(
                "action=arm",
                "action=prepareFar resumed=false")
            : result;
    }

    private String arm(
        obj_id attacker,
        obj_id victim,
        String lifecycle,
        float separation,
        boolean feigning,
        String mode)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(
                attacker,
                victim,
                lifecycle,
                false);
        if (ownership != null)
        {
            return ownership;
        }
        String dependency =
            validateHeadShotLayer(
                attacker,
                victim,
                lifecycle);
        if (dependency != null)
        {
            return dependency;
        }

        stopCombat(attacker);
        stopCombat(victim);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(victim, obj_id.NULL_ID);
        combat.clearCombatDebuffs(attacker);
        combat.clearCombatDebuffs(victim);
        utils.removeScriptVar(
            victim,
            "death.beingCoupDeGraced");
        utils.removeScriptVar(
            victim,
            "incap.timeStamp");
        if (isDead(victim))
        {
            pclib.resurrectPlayer(victim);
        }
        removeObjVar(
            victim,
            pclib.VAR_BEEN_COUPDEGRACED);
        pclib.clearPrecuIncapacitationTimes(victim);

        float attackerX = 3500.0f;
        float attackerZ = -4800.0f;
        float victimX = attackerX + separation;
        location attackerDestination =
            new location(
                attackerX,
                5.0f,
                attackerZ,
                "tatooine",
                null);
        location victimDestination =
            new location(
                victimX,
                5.0f,
                attackerZ,
                "tatooine",
                null);
        boolean moved =
            setLocation(
                attacker,
                attackerDestination) &
            setLocation(
                victim,
                victimDestination);
        boolean attackerState =
            setState(
                attacker,
                STATE_FEIGN_DEATH,
                false) &
            setLocomotion(
                attacker,
                LOCOMOTION_STANDING) &
            setPostureClientImmediate(
                attacker,
                POSTURE_UPRIGHT);
        // Prepare HAM before entering incapacitated posture. The negative
        // Health value is an authentic incapacitation condition and naturally
        // drives the retained Publish 14 timer to its 60-second cap.
        boolean hamReady =
            setAttribAndVerify(
                attacker,
                HEALTH,
                getMaxAttrib(attacker, HEALTH)) &
            setAttribAndVerify(
                attacker,
                ACTION,
                getMaxAttrib(attacker, ACTION)) &
            setAttribAndVerify(
                attacker,
                MIND,
                getMaxAttrib(attacker, MIND)) &
            setAttribAndVerify(
                victim,
                HEALTH,
                FIXTURE_INCAP_HEALTH) &
            setAttribAndVerify(
                victim,
                ACTION,
                getMaxAttrib(victim, ACTION)) &
            setAttribAndVerify(
                victim,
                MIND,
                getMaxAttrib(victim, MIND));
        boolean victimState =
            setLocomotion(
                victim,
                LOCOMOTION_STANDING) &
            setPostureClientImmediate(
                victim,
                POSTURE_INCAPACITATED) &
            setState(
                victim,
                STATE_FEIGN_DEATH,
                feigning);
        // OnIncapacitated schedules a five-second recap message. Re-arming
        // several admission modes in one lifecycle can otherwise let an older
        // message match a newly reused timestamp and stand the victim early.
        // No recap message owns this fixture-only far-future value, and
        // cleanup always removes it.
        int fixtureRecoveryTime =
            getGameTime() + 60;
        utils.setScriptVar(
            victim,
            "incap.timeStamp",
            fixtureRecoveryTime);
        setCount(
            victim,
            60);
        pvpSetPermanentPersonalEnemyFlag(
            attacker,
            victim);
        pvpSetPermanentPersonalEnemyFlag(
            victim,
            attacker);

        boolean ready =
            moved &&
            attackerState &&
            victimState &&
            hamReady &&
            pvpCanAttack(attacker, victim) &&
            canSee(attacker, victim) &&
            !isDead(victim) &&
            getPosture(victim) ==
                POSTURE_INCAPACITATED &&
            (feigning ||
                isIncapacitated(victim)) &&
            getState(
                victim,
                STATE_FEIGN_DEATH) ==
                (feigning ? 1 : 0);
        return (ready
                ? "action=arm result=passed"
                : "error=armFailed") +
            " mode=" + mode +
            " expectedSeparationMeters=" +
                separation +
            " expectedFeign=" + feigning +
            " " +
            buildStatus(
                attacker,
                victim,
                lifecycle);
    }

    private String stabilize(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(
                attacker,
                victim,
                lifecycle,
                false);
        if (ownership != null)
        {
            return ownership;
        }
        String dependency =
            validateHeadShotLayer(
                attacker,
                victim,
                lifecycle);
        if (dependency != null)
        {
            return dependency;
        }
        if (isDead(victim) ||
            getPosture(victim) !=
                POSTURE_INCAPACITATED)
        {
            return "error=victimNotIncapacitated " +
                buildStatus(
                    attacker,
                    victim,
                    lifecycle);
        }

        // Invoke this action after setPostureClientImmediate has returned and
        // the authentic OnIncapacitated callback owns its normal recap
        // message. Re-key only the fixture timer so that stale recoveryTime
        // messages cannot stand the victim during queued command admission.
        utils.setScriptVar(
            victim,
            "incap.timeStamp",
            getGameTime() + 60);
        setCount(
            victim,
            60);
        return "action=stabilize result=passed " +
            buildStatus(
                attacker,
                victim,
                lifecycle);
    }

    private String cleanup(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        String ownership =
            validateOwnership(
                attacker,
                victim,
                lifecycle,
                true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanup alreadyClean=true" +
                " restored=true lifecycle=" +
                lifecycle;
        }
        if (ownership != null)
        {
            return ownership;
        }
        return recover(
            attacker,
            victim,
            lifecycle).replace(
                "action=recover",
                "action=cleanup");
    }

    private String recover(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        boolean attackerRoot =
            hasObjVar(attacker, ROOT);
        boolean victimRoot =
            hasObjVar(victim, ROOT);
        if (!attackerRoot && !victimRoot)
        {
            return "action=recover alreadyClean=true" +
                " restored=true lifecycle=" +
                lifecycle;
        }
        String ownership =
            validateOwnership(
                attacker,
                victim,
                lifecycle,
                false);
        if (ownership != null)
        {
            return ownership;
        }

        stopCombat(attacker);
        stopCombat(victim);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(victim, obj_id.NULL_ID);
        utils.removeScriptVar(
            victim,
            "death.beingCoupDeGraced");
        utils.removeScriptVar(
            victim,
            "incap.timeStamp");
        boolean resurrected =
            !isDead(victim) ||
            pclib.resurrectPlayer(victim);
        removeObjVar(
            victim,
            pclib.VAR_BEEN_COUPDEGRACED);
        pclib.clearPrecuIncapacitationTimes(victim);
        setState(
            attacker,
            STATE_FEIGN_DEATH,
            false);
        setState(
            victim,
            STATE_FEIGN_DEATH,
            false);
        boolean attackerUpright =
            setLocomotion(
                attacker,
                LOCOMOTION_STANDING) &
            setPostureClientImmediate(
                attacker,
                POSTURE_UPRIGHT);
        boolean victimUpright =
            setLocomotion(
                victim,
                LOCOMOTION_STANDING) &
            setPostureClientImmediate(
                victim,
                POSTURE_UPRIGHT);
        boolean hamReady =
            setAttribAndVerify(
                victim,
                HEALTH,
                getMaxAttrib(victim, HEALTH)) &
            setAttribAndVerify(
                victim,
                ACTION,
                getMaxAttrib(victim, ACTION)) &
            setAttribAndVerify(
                victim,
                MIND,
                getMaxAttrib(victim, MIND));
        boolean commandRestored = true;
        if (getIntObjVar(
                attacker,
                ORIGINAL_COMMAND) == 0 &&
            hasCommand(attacker, COMMAND))
        {
            revokeCommand(attacker, COMMAND);
            commandRestored =
                !hasCommand(attacker, COMMAND);
        }
        boolean restored =
            resurrected &&
            attackerUpright &&
            victimUpright &&
            hamReady &&
            commandRestored &&
            !isDead(victim) &&
            !isIncapacitated(victim);
        if (!restored)
        {
            return "error=fixtureRestoreFailed " +
                buildStatus(
                    attacker,
                    victim,
                    lifecycle);
        }
        removeObjVar(attacker, ROOT);
        removeObjVar(victim, ROOT);
        return "action=recover alreadyClean=false" +
            " restored=" +
                (!hasObjVar(attacker, ROOT) &&
                    !hasObjVar(victim, ROOT)) +
            " lifecycle=" + lifecycle;
    }

    private boolean setAttribAndVerify(
        obj_id player,
        int attribute,
        int value)
        throws InterruptedException
    {
        setAttrib(
            player,
            attribute,
            value);
        return getAttrib(
                player,
                attribute) ==
            value;
    }

    private String validateHeadShotLayer(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(attacker, HEADSHOT_ROOT) ||
            !hasObjVar(victim, HEADSHOT_ROOT) ||
            !hasObjVar(
                attacker,
                HEADSHOT_LIFECYCLE) ||
            !hasObjVar(
                victim,
                HEADSHOT_LIFECYCLE) ||
            !lifecycle.equals(
                getStringObjVar(
                    attacker,
                    HEADSHOT_LIFECYCLE)) ||
            !lifecycle.equals(
                getStringObjVar(
                    victim,
                    HEADSHOT_LIFECYCLE)) ||
            getIntObjVar(
                attacker,
                HEADSHOT_PREPARED) != 1 ||
            getIntObjVar(
                victim,
                HEADSHOT_PREPARED) != 1)
        {
            return "error=headShotFixtureNotPrepared";
        }
        return null;
    }

    private String validatePlayer(
        obj_id player,
        int stationId,
        String role)
        throws InterruptedException
    {
        if (player == null ||
            player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=" + role + "NotLoaded";
        }
        if (!player.isAuthoritative() ||
            !isPlayer(player))
        {
            return "error=" + role +
                "NotAuthoritative";
        }
        if (getPlayerStationId(player) !=
            stationId)
        {
            return "error=" + role +
                "StationNotAllowed";
        }
        return null;
    }

    private String validateOwnership(
        obj_id attacker,
        obj_id victim,
        String lifecycle,
        boolean allowAbsent)
        throws InterruptedException
    {
        boolean attackerRoot =
            hasObjVar(attacker, ROOT);
        boolean victimRoot =
            hasObjVar(victim, ROOT);
        if (!attackerRoot && !victimRoot)
        {
            return allowAbsent
                ? "fixtureAbsent"
                : "error=fixtureAbsent";
        }
        if (!attackerRoot ||
            !victimRoot ||
            !hasObjVar(attacker, LIFECYCLE) ||
            !hasObjVar(victim, LIFECYCLE) ||
            !hasObjVar(attacker, PEER) ||
            !hasObjVar(victim, PEER) ||
            !hasObjVar(
                attacker,
                ORIGINAL_COMMAND) ||
            getIntObjVar(attacker, PREPARED) != 1 ||
            getIntObjVar(victim, PREPARED) != 1)
        {
            return "error=fixturePartial";
        }
        if (!lifecycle.equals(
                getStringObjVar(
                    attacker,
                    LIFECYCLE)) ||
            !lifecycle.equals(
                getStringObjVar(
                    victim,
                    LIFECYCLE)) ||
            getObjIdObjVar(attacker, PEER) !=
                victim ||
            getObjIdObjVar(victim, PEER) !=
                attacker)
        {
            return "error=fixtureOwnershipMismatch";
        }
        return null;
    }

    private String buildStatus(
        obj_id attacker,
        obj_id victim,
        String lifecycle)
        throws InterruptedException
    {
        location attackerLocation =
            getLocation(attacker);
        location victimLocation =
            getLocation(victim);
        int distanceCentimeters =
            attackerLocation == null ||
                victimLocation == null
                ? -1
                : (int)(
                    attackerLocation.distance(
                        victimLocation) *
                    100.0f);
        return "lifecycle=" + lifecycle +
            " prepared=" +
                (getIntObjVar(
                        attacker,
                        PREPARED) == 1 &&
                    getIntObjVar(
                        victim,
                        PREPARED) == 1) +
            " distanceCentimeters=" +
                distanceCentimeters +
            " lineOfSight=" +
                canSee(attacker, victim) +
            " pvpCanAttack=" +
                pvpCanAttack(attacker, victim) +
            " hasDeathBlowCommand=" +
                hasCommand(attacker, COMMAND) +
            " attackerPosture=" +
                getPosture(attacker) +
            " attackerFeign=" +
                getState(
                    attacker,
                    STATE_FEIGN_DEATH) +
            " victimPosture=" +
                getPosture(victim) +
            " victimFeign=" +
                getState(
                    victim,
                    STATE_FEIGN_DEATH) +
            " victimIncapacitated=" +
                isIncapacitated(victim) +
            " victimDead=" +
                isDead(victim) +
            " victimBeenCoupDeGraced=" +
                hasObjVar(
                    victim,
                    pclib.VAR_BEEN_COUPDEGRACED) +
            " victimHealth=" +
                getAttrib(victim, HEALTH) +
            " victimAction=" +
                getAttrib(victim, ACTION) +
            " victimMind=" +
                getAttrib(victim, MIND);
    }
}
