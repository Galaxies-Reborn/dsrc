package script.player.cmd;

import script.attrib_mod;
import script.obj_id;
import script.string_id;
import script.library.consumable;
import script.library.factions;
import script.library.group;
import script.library.healing;
import script.library.pclib;
import script.library.utils;

/**
 * Publish 14.1 Revive Player compatibility adapter.
 *
 * The retained healing library owns medicine certification, modifier
 * application, charge consumption, death-effect cleanup, PvP help, XP, and
 * the player resuscitation message. This adapter restores Core3 command
 * admission, explicit target/pack resolution, seven-meter range, and the
 * presentation path.
 */
public class revive_player extends script.base_script
{
    private static final float RANGE = 7.0f;
    private static final String FIXTURE_ROOT =
        "precu.revivePlayerCommandFixture";
    private static final string_id SID_INVALID_TARGET =
        new string_id(
            "healing_response",
            "healing_response_a2");
    private static final string_id SID_NON_PLAYER =
        new string_id(
            "healing_response",
            "healing_response_a3");
    private static final string_id SID_NOT_DEAD =
        new string_id(
            "healing_response",
            "healing_response_a4");
    private static final string_id SID_SELF_DISALLOWED =
        new string_id(
            "error_message",
            "target_self_disallowed");
    private static final string_id SID_NO_LINE_OF_SIGHT =
        new string_id("healing", "no_line_of_sight");

    public int cmdRevivePlayer(
        obj_id self,
        obj_id target,
        String params,
        float defaultTime)
        throws InterruptedException
    {
        boolean fixture =
            isIdValid(self) &&
                hasObjVar(self, FIXTURE_ROOT);
        markEntry(self, fixture);
        if (!isIdValid(self) || !isPlayer(self) ||
            isDead(self) || isIncapacitated(self))
        {
            recordOutcome(self, fixture, "medicRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isIdValid(target))
        {
            sendSystemMessage(self, SID_INVALID_TARGET);
            recordOutcome(self, fixture, "targetMissing");
            return SCRIPT_CONTINUE;
        }
        if (!isPlayer(target))
        {
            sendSystemMessage(self, SID_NON_PLAYER);
            recordOutcome(self, fixture, "targetNotPlayer");
            return SCRIPT_CONTINUE;
        }
        if (target == self)
        {
            sendSystemMessage(self, SID_SELF_DISALLOWED);
            recordOutcome(self, fixture, "selfRejected");
            return SCRIPT_CONTINUE;
        }
        if (!isDead(target))
        {
            sendSystemMessage(self, SID_NOT_DEAD);
            recordOutcome(self, fixture, "targetNotDead");
            return SCRIPT_CONTINUE;
        }
        if (getDistance(self, target) > RANGE)
        {
            healing.sendMedicalSpam(
                self,
                consumable.SID_TARGET_OUT_OF_RANGE,
                COMBAT_RESULT_OUT_OF_RANGE);
            recordOutcome(self, fixture, "outOfRange");
            return SCRIPT_CONTINUE;
        }
        if (!canSee(self, target))
        {
            healing.sendMedicalSpam(
                self,
                SID_NO_LINE_OF_SIGHT,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(
                self,
                fixture,
                "lineOfSightRejected");
            return SCRIPT_CONTINUE;
        }
        if (!pvpCanHelp(self, target) ||
            !factions.pvpDoAllowedHelpCheck(self, target))
        {
            healing.sendMedicalSpam(
                self,
                healing.SID_PVP_NO_HELP,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(self, fixture, "pvpRejected");
            return SCRIPT_CONTINUE;
        }
        if (!group.inSameGroup(self, target) &&
            !pclib.hasConsent(self, target))
        {
            healing.sendMedicalSpam(
                self,
                healing.SID_GROUP_OR_CONSENT_FROM_TARGET,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(
                self,
                fixture,
                "groupOrConsentRejected");
            return SCRIPT_CONTINUE;
        }

        obj_id pack = parsePack(self, params);
        if (!isIdValid(pack))
        {
            pack = healing.getRevivePack(self);
        }
        if (!isValidPack(self, pack))
        {
            healing.sendMedicalSpam(
                self,
                healing.SID_CANNOT_RESUS_WITHOUT_KIT,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(self, fixture, "noPack");
            return SCRIPT_CONTINUE;
        }

        int expectedMindCost =
            healing.getMedicalMindCost(
                self,
                healing.COST_MIND_REVIVE);
        if (expectedMindCost < 0 ||
            getAttrib(self, MIND) < expectedMindCost)
        {
            healing.sendMedicalSpam(
                self,
                healing.SID_MIND_TOO_DRAINED,
                COMBAT_RESULT_MEDICAL);
            recordOutcome(
                self,
                fixture,
                "notEnoughMind");
            return SCRIPT_CONTINUE;
        }

        int[] primary =
        {
            HEALTH,
            ACTION,
            MIND
        };
        int[] damageBefore =
            readAttributes(target, primary);
        int[] woundBefore =
            readWounds(target, primary);
        int medicMindBefore = getAttrib(self, MIND);
        int chargesBefore = getCount(pack);
        int xpBefore =
            getExperiencePoints(self, "medical");
        boolean performed =
            healing.resuscitatePlayer(
                self,
                target,
                pack);
        if (!performed)
        {
            recordOutcome(
                self,
                fixture,
                "reviveRejected");
            return SCRIPT_CONTINUE;
        }

        doAnimationAction(self, "heal_other");
        playClientEffectObj(
            target,
            "clienteffect/healing_healwound.cef",
            target,
            "");
        if (fixture)
        {
            int[] damageAfter =
                readAttributes(target, primary);
            int[] woundAfter =
                readWounds(target, primary);
            int actualHealing = 0;
            int damageHealing = 0;
            int woundHealing = 0;
            for (int index = 0;
                index < primary.length;
                ++index)
            {
                int damageDelta =
                    Math.max(
                        0,
                        damageAfter[index] -
                            damageBefore[index]);
                int woundDelta =
                    Math.max(
                        0,
                        woundBefore[index] -
                            woundAfter[index]);
                damageHealing += damageDelta;
                woundHealing += woundDelta;
                actualHealing +=
                    damageDelta + woundDelta;
            }
            int chargesAfter =
                isIdValid(pack) && exists(pack)
                    ? getCount(pack)
                    : 0;
            setObjVar(
                self,
                FIXTURE_ROOT + ".target",
                target.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".pack",
                pack.toString());
            setObjVar(
                self,
                FIXTURE_ROOT + ".damageHealing",
                damageHealing);
            setObjVar(
                self,
                FIXTURE_ROOT + ".woundHealing",
                woundHealing);
            setObjVar(
                self,
                FIXTURE_ROOT + ".actualHealing",
                actualHealing);
            setObjVar(
                self,
                FIXTURE_ROOT + ".mindCost",
                medicMindBefore -
                    getAttrib(self, MIND));
            setObjVar(
                self,
                FIXTURE_ROOT + ".chargeCost",
                chargesBefore - chargesAfter);
            setObjVar(
                self,
                FIXTURE_ROOT + ".medicalXpDelta",
                getExperiencePoints(
                    self,
                    "medical") - xpBefore);
            setObjVar(
                self,
                FIXTURE_ROOT + ".expectedMindCost",
                expectedMindCost);
            setObjVar(
                self,
                FIXTURE_ROOT + ".expectedMedicalXp",
                Math.round(
                    (actualHealing + 250) * 0.5f));
            setObjVar(
                self,
                FIXTURE_ROOT + ".grouped",
                group.inSameGroup(self, target)
                    ? 1
                    : 0);
            setObjVar(
                self,
                FIXTURE_ROOT + ".consented",
                pclib.hasConsent(self, target)
                    ? 1
                    : 0);
            setObjVar(
                self,
                FIXTURE_ROOT + ".groggyCount",
                countGroggyModifiers(target));
        }
        recordOutcome(self, fixture, "Success");
        return SCRIPT_CONTINUE;
    }

    private obj_id parsePack(
        obj_id self,
        String params)
        throws InterruptedException
    {
        if (params == null ||
            params.trim().length() == 0)
        {
            return obj_id.NULL_ID;
        }
        try
        {
            obj_id candidate =
                obj_id.getObjId(
                    Long.parseLong(params.trim()));
            return isValidPack(self, candidate)
                ? candidate
                : obj_id.NULL_ID;
        }
        catch (NumberFormatException exception)
        {
            return obj_id.NULL_ID;
        }
    }

    private boolean isValidPack(
        obj_id self,
        obj_id pack)
        throws InterruptedException
    {
        if (!isIdValid(pack) ||
            !pack.isLoaded() ||
            utils.getContainingPlayer(pack) != self ||
            !healing.isRevivePack(pack))
        {
            return false;
        }
        attrib_mod[] modifiers =
            getAttribModArrayObjVar(
                pack,
                consumable.VAR_CONSUMABLE_MODS);
        return modifiers != null &&
            modifiers.length > 0 &&
            getCount(pack) > 0;
    }

    private int[] readAttributes(
        obj_id target,
        int[] attributes)
        throws InterruptedException
    {
        int[] values = new int[attributes.length];
        for (int index = 0;
            index < attributes.length;
            ++index)
        {
            values[index] =
                getAttrib(target, attributes[index]);
        }
        return values;
    }

    private int[] readWounds(
        obj_id target,
        int[] attributes)
        throws InterruptedException
    {
        int[] values = new int[attributes.length];
        for (int index = 0;
            index < attributes.length;
            ++index)
        {
            values[index] =
                getAttribWound(
                    target,
                    attributes[index]);
        }
        return values;
    }

    private int countGroggyModifiers(obj_id target)
        throws InterruptedException
    {
        int count = 0;
        for (int attribute = HEALTH;
            attribute <= WILLPOWER;
            ++attribute)
        {
            if (hasAttribModifier(
                    target,
                    "precu_private_groggy_" +
                        attribute))
            {
                ++count;
            }
        }
        return count;
    }

    private void markEntry(
        obj_id self,
        boolean fixture)
        throws InterruptedException
    {
        if (!fixture)
        {
            return;
        }
        int calls =
            hasObjVar(
                self,
                FIXTURE_ROOT + ".handlerCalls")
                ? getIntObjVar(
                    self,
                    FIXTURE_ROOT +
                        ".handlerCalls")
                : 0;
        setObjVar(
            self,
            FIXTURE_ROOT + ".handlerCalls",
            calls + 1);
        setObjVar(
            self,
            FIXTURE_ROOT + ".handlerEntered",
            1);
    }

    private void recordOutcome(
        obj_id self,
        boolean fixture,
        String outcome)
        throws InterruptedException
    {
        if (fixture && isIdValid(self))
        {
            setObjVar(
                self,
                FIXTURE_ROOT + ".outcome",
                outcome);
        }
    }
}
