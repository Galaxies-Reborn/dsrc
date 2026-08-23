package script.test;

import script.attrib_mod;
import script.obj_id;
import script.library.performance;
import script.library.private_entertainer;

/**
 * Identity-bound, reversible ServerConsole probe for the configured private
 * dancer/musician PRE-CU attribute packages.
 */
public class precu_private_entertainer_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;

    public String executeProbe(String params) throws InterruptedException
    {
        long playerValue;
        try
        {
            playerValue = Long.parseLong(
                params == null ? "" : params.trim());
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID)
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }
        if (getNamedModifier(
                player, MIND, performance.PRECU_DANCE_MIND_BUFF) != null ||
            getNamedModifier(
                player, FOCUS, performance.PRECU_MUSIC_FOCUS_BUFF) != null ||
            getNamedModifier(
                player,
                WILLPOWER,
                performance.PRECU_MUSIC_WILLPOWER_BUFF) != null)
        {
            return "error=preexistingEntertainerBuff";
        }

        int mindBefore = getAttrib(player, MIND);
        int focusBefore = getAttrib(player, FOCUS);
        int willpowerBefore = getAttrib(player, WILLPOWER);
        int mindMaxBefore = getMaxAttrib(player, MIND);
        int focusMaxBefore = getMaxAttrib(player, FOCUS);
        int willpowerMaxBefore = getMaxAttrib(player, WILLPOWER);
        String result = "error=probeIncomplete";
        try
        {
            int expectedMind = Math.round(
                getUnmodifiedMaxAttrib(player, MIND) * 0.25f);
            int expectedFocus = Math.round(
                getUnmodifiedMaxAttrib(player, FOCUS) * 0.25f);
            int expectedWillpower = Math.round(
                getUnmodifiedMaxAttrib(player, WILLPOWER) * 0.25f);

            boolean dancerReady =
                private_entertainer.canApplyConfiguredBuff(
                    player, private_entertainer.TYPE_DANCER);
            boolean dancerApplied =
                private_entertainer.applyConfiguredBuff(
                    player, private_entertainer.TYPE_DANCER);
            attrib_mod mindMod = getNamedModifier(
                player, MIND, performance.PRECU_DANCE_MIND_BUFF);
            boolean dancerExact = mindMod != null &&
                mindMod.getValue() == expectedMind &&
                durationMatches(mindMod);

            removeAttribOrSkillModModifier(
                player, performance.PRECU_DANCE_MIND_BUFF);

            boolean musicianReady =
                private_entertainer.canApplyConfiguredBuff(
                    player, private_entertainer.TYPE_MUSICIAN);
            boolean musicianApplied =
                private_entertainer.applyConfiguredBuff(
                    player, private_entertainer.TYPE_MUSICIAN);
            attrib_mod focusMod = getNamedModifier(
                player, FOCUS, performance.PRECU_MUSIC_FOCUS_BUFF);
            attrib_mod willpowerMod = getNamedModifier(
                player,
                WILLPOWER,
                performance.PRECU_MUSIC_WILLPOWER_BUFF);
            boolean musicianExact = focusMod != null &&
                willpowerMod != null &&
                focusMod.getValue() == expectedFocus &&
                willpowerMod.getValue() == expectedWillpower &&
                durationMatches(focusMod) &&
                durationMatches(willpowerMod);

            boolean passed =
                private_entertainer.BUFF_PRICE == 10000 &&
                private_entertainer.BUFF_STRENGTH_PERCENT == 25.0f &&
                dancerReady && dancerApplied && dancerExact &&
                musicianReady && musicianApplied && musicianExact;
            result =
                "action=probe" +
                " price=" + private_entertainer.BUFF_PRICE +
                " strength=" +
                    private_entertainer.BUFF_STRENGTH_PERCENT +
                " duration=" +
                    private_entertainer.BUFF_DURATION_SECONDS +
                " dancerReady=" + dancerReady +
                " dancerApplied=" + dancerApplied +
                " dancerExact=" + dancerExact +
                " musicianReady=" + musicianReady +
                " musicianApplied=" + musicianApplied +
                " musicianExact=" + musicianExact +
                " passed=" + passed;
        }
        finally
        {
            removeAttribOrSkillModModifier(
                player, performance.PRECU_DANCE_MIND_BUFF);
            removeAttribOrSkillModModifier(
                player, performance.PRECU_MUSIC_FOCUS_BUFF);
            removeAttribOrSkillModModifier(
                player, performance.PRECU_MUSIC_WILLPOWER_BUFF);
            setAttrib(player, MIND, mindBefore);
            setAttrib(player, FOCUS, focusBefore);
            setAttrib(player, WILLPOWER, willpowerBefore);
            boolean restored =
                getNamedModifier(
                    player,
                    MIND,
                    performance.PRECU_DANCE_MIND_BUFF) == null &&
                getNamedModifier(
                    player,
                    FOCUS,
                    performance.PRECU_MUSIC_FOCUS_BUFF) == null &&
                getNamedModifier(
                    player,
                    WILLPOWER,
                    performance.PRECU_MUSIC_WILLPOWER_BUFF) == null &&
                getAttrib(player, MIND) == mindBefore &&
                getAttrib(player, FOCUS) == focusBefore &&
                getAttrib(player, WILLPOWER) == willpowerBefore &&
                getMaxAttrib(player, MIND) == mindMaxBefore &&
                getMaxAttrib(player, FOCUS) == focusMaxBefore &&
                getMaxAttrib(player, WILLPOWER) == willpowerMaxBefore;
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=privateBuffRestoreFailed " + result;
            }
        }
        return result;
    }

    private static boolean durationMatches(attrib_mod modifier)
    {
        return modifier != null &&
            Math.abs(
                modifier.getDuration() -
                    private_entertainer.BUFF_DURATION_SECONDS) <= 2.0f;
    }

    private static attrib_mod getNamedModifier(
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
                    return modifier;
                }
            }
        }
        return null;
    }
}
