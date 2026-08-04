package script.player.skill;

import script.library.*;
import script.obj_id;
import script.prose_package;

public class teraskasi extends script.systems.combat.combat_base
{
    private static final long PRECU_ACCURACY_PLAYER_OID = 44003778L;
    private static final int PRECU_ACCURACY_STATION_ID = 91001;
    private static final int PRECU_ACCURACY_PROTOCOL_VERSION = 1;
    private static final String PRECU_ACCURACY_ROOT =
        "precu.unarmedAccuracyFixture";
    public teraskasi()
    {
    }
    public int cmdForceOfWill(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        boolean fixture = isPrecuAccuracyFixture(self);
        recordFixture(self, fixture, "forceHandlerEntered", 1);
        int modval = meditation.getMeditationSkillMod(self);
        if (modval < 1)
        {
            recordFixtureOutcome(self, fixture, "forceNoSkill");
            return SCRIPT_CONTINUE;
        }
        if (getPosture(self) != POSTURE_INCAPACITATED)
        {
            sendSystemMessage(self, meditation.SID_FORCEOFWILL_FAIL);
            recordFixtureOutcome(self, fixture, "forceNotIncapacitated");
            return SCRIPT_CONTINUE;
        }
        int now = getGameTime();
        if (hasObjVar(self, meditation.VAR_FORCE_OF_WILL_ACTIVE))
        {
            int stamp = getIntObjVar(self,
                meditation.VAR_FORCE_OF_WILL_ACTIVE);
            if (stamp < 0)
            {
                sendSystemMessage(self, meditation.SID_FORCEOFWILL_LOST);
                recordFixtureOutcome(self, fixture, "forceLost");
                return SCRIPT_CONTINUE;
            }
            int remaining = stamp + 3600 - now;
            if (remaining > 0)
            {
                String timeString = player_structure.assembleTimeRemaining(
                    player_structure.convertSecondsTime(remaining));
                prose_package unavailable = prose.getPackage(
                    meditation.SID_FORCEOFWILL_UNAVAILABLE, timeString);
                sendSystemMessageProse(self, unavailable);
                recordFixtureOutcome(self, fixture, "forceCooldown");
                return SCRIPT_CONTINUE;
            }
        }
        int roll = fixture && hasObjVar(self,
            PRECU_ACCURACY_ROOT + ".forcedRoll")
                ? getIntObjVar(self,
                    PRECU_ACCURACY_ROOT + ".forcedRoll")
                : rand(0, 100);
        if (fixture && hasObjVar(self,
            PRECU_ACCURACY_ROOT + ".forcedRoll"))
        {
            removeObjVar(self, PRECU_ACCURACY_ROOT + ".forcedRoll");
        }
        recordFixture(self, fixture, "forceModifier", modval);
        recordFixture(self, fixture, "forceRoll", roll);
        if (roll < 5 || modval < roll)
        {
            setObjVar(self, meditation.VAR_FORCE_OF_WILL_ACTIVE, -1);
            sendSystemMessage(self, meditation.SID_FORCEOFWILL_UNSUCCESSFUL);
            recordFixtureOutcome(self, fixture, "forceUnsuccessful");
            return SCRIPT_CONTINUE;
        }
        int delta = modval - roll;
        String tier = meditation.getForceOfWillTier(delta);
        if (meditation.forceOfWill(self, delta))
        {
            setObjVar(self, meditation.VAR_FORCE_OF_WILL_ACTIVE, now);
            recordFixture(self, fixture, "forceDelta", delta);
            if (fixture)
            {
                setObjVar(self, PRECU_ACCURACY_ROOT + ".forceTier", tier);
            }
            recordFixtureOutcome(self, fixture, "forcePassed");
        }
        return SCRIPT_CONTINUE;
    }
    public int cmdForceOfWillFail(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        return SCRIPT_CONTINUE;
    }
    public int cmdPowerBoost(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        boolean fixture = isPrecuAccuracyFixture(self);
        recordFixture(self, fixture, "powerHandlerEntered", 1);
        boolean applied = meditation.powerBoost(self);
        if (fixture)
        {
            recordFixture(self, true, "powerBonus",
                hasObjVar(self, meditation.VAR_POWERBOOST_BONUS)
                    ? getIntObjVar(self, meditation.VAR_POWERBOOST_BONUS)
                    : 0);
            recordFixture(self, true, "powerTick",
                hasObjVar(self, meditation.VAR_POWERBOOST_TICK)
                    ? getIntObjVar(self, meditation.VAR_POWERBOOST_TICK)
                    : 0);
            recordFixture(self, true, "powerDuration",
                hasObjVar(self, meditation.VAR_POWERBOOST_DURATION)
                    ? getIntObjVar(self, meditation.VAR_POWERBOOST_DURATION)
                    : 0);
        }
        recordFixtureOutcome(self, fixture,
            applied ? "powerPassed" : "powerRejected");
        return applied ? SCRIPT_CONTINUE : SCRIPT_OVERRIDE;
    }
    public int cmdPowerBoostFail(obj_id self, obj_id target, String params, float defaultTime) throws InterruptedException
    {
        boolean fixture = isPrecuAccuracyFixture(self);
        recordFixture(self, fixture, "powerFailHandlerEntered", 1);
        recordFixtureOutcome(self, fixture, "powerCommandFailed");
        combat.sendCombatSpamMessage(self, meditation.SID_POWERBOOST_FAIL);
        return SCRIPT_CONTINUE;
    }
    private boolean isPrecuAccuracyFixture(obj_id player)
        throws InterruptedException
    {
        return isIdValid(player) && player.isLoaded() &&
            player.isAuthoritative() && isPlayer(player) &&
            player.getValue() == PRECU_ACCURACY_PLAYER_OID &&
            getPlayerStationId(player) == PRECU_ACCURACY_STATION_ID &&
            hasObjVar(player, PRECU_ACCURACY_ROOT + ".protocol") &&
            getIntObjVar(player, PRECU_ACCURACY_ROOT + ".protocol") ==
                PRECU_ACCURACY_PROTOCOL_VERSION &&
            hasObjVar(player, PRECU_ACCURACY_ROOT + ".prepared") &&
            getIntObjVar(player, PRECU_ACCURACY_ROOT + ".prepared") == 1;
    }
    private void recordFixture(obj_id player, boolean fixture, String leaf,
        int value) throws InterruptedException
    {
        if (fixture)
        {
            setObjVar(player, PRECU_ACCURACY_ROOT + "." + leaf, value);
        }
    }
    private void recordFixtureOutcome(obj_id player, boolean fixture,
        String outcome) throws InterruptedException
    {
        if (fixture)
        {
            setObjVar(player, PRECU_ACCURACY_ROOT + ".outcome", outcome);
        }
    }
}
