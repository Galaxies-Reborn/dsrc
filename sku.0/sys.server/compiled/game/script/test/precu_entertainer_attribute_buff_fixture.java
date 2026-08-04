package script.test;

import script.attrib_mod;
import script.obj_id;
import script.library.performance;

/**
 * Identity-bound, reversible ServerConsole fixture for the Publish 14.1
 * entertainer attribute-buff application seam.
 */
public class precu_entertainer_attribute_buff_fixture
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
        if (hasNamedModifier(
                player, MIND, performance.PRECU_DANCE_MIND_BUFF) ||
            hasNamedModifier(
                player, FOCUS, performance.PRECU_MUSIC_FOCUS_BUFF) ||
            hasNamedModifier(
                player,
                WILLPOWER,
                performance.PRECU_MUSIC_WILLPOWER_BUFF))
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
            boolean danceApplied =
                performance.applyPrecuEntertainerAttributeBuff(
                    player,
                    performance.PERFORMANCE_TYPE_DANCE,
                    12.0f,
                    300.0f);
            int expectedMind = Math.round(
                getUnmodifiedMaxAttrib(player, MIND) * 0.12f);
            int danceValue = getNamedModifierValue(
                player, MIND, performance.PRECU_DANCE_MIND_BUFF);
            boolean weakerDanceRejected =
                !performance.applyPrecuEntertainerAttributeBuff(
                    player,
                    performance.PERFORMANCE_TYPE_DANCE,
                    8.0f,
                    300.0f);
            int danceValueAfterWeaker = getNamedModifierValue(
                player, MIND, performance.PRECU_DANCE_MIND_BUFF);

            removeAttribOrSkillModModifier(
                player, performance.PRECU_DANCE_MIND_BUFF);

            boolean musicApplied =
                performance.applyPrecuEntertainerAttributeBuff(
                    player,
                    performance.PERFORMANCE_TYPE_MUSIC,
                    10.0f,
                    300.0f);
            int expectedFocus = Math.round(
                getUnmodifiedMaxAttrib(player, FOCUS) * 0.10f);
            int expectedWillpower = Math.round(
                getUnmodifiedMaxAttrib(player, WILLPOWER) * 0.10f);
            int focusValue = getNamedModifierValue(
                player, FOCUS, performance.PRECU_MUSIC_FOCUS_BUFF);
            int willpowerValue = getNamedModifierValue(
                player,
                WILLPOWER,
                performance.PRECU_MUSIC_WILLPOWER_BUFF);

            boolean passed =
                danceApplied &&
                danceValue == expectedMind &&
                weakerDanceRejected &&
                danceValueAfterWeaker == expectedMind &&
                musicApplied &&
                focusValue == expectedFocus &&
                willpowerValue == expectedWillpower;
            result =
                "action=probe" +
                " authoritative=true" +
                " danceApplied=" + danceApplied +
                " expectedMind=" + expectedMind +
                " danceValue=" + danceValue +
                " weakerDanceRejected=" + weakerDanceRejected +
                " danceValueAfterWeaker=" + danceValueAfterWeaker +
                " musicApplied=" + musicApplied +
                " expectedFocus=" + expectedFocus +
                " focusValue=" + focusValue +
                " expectedWillpower=" + expectedWillpower +
                " willpowerValue=" + willpowerValue +
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
                !hasNamedModifier(
                    player,
                    MIND,
                    performance.PRECU_DANCE_MIND_BUFF) &&
                !hasNamedModifier(
                    player,
                    FOCUS,
                    performance.PRECU_MUSIC_FOCUS_BUFF) &&
                !hasNamedModifier(
                    player,
                    WILLPOWER,
                    performance.PRECU_MUSIC_WILLPOWER_BUFF) &&
                getAttrib(player, MIND) == mindBefore &&
                getAttrib(player, FOCUS) == focusBefore &&
                getAttrib(player, WILLPOWER) == willpowerBefore &&
                getMaxAttrib(player, MIND) == mindMaxBefore &&
                getMaxAttrib(player, FOCUS) == focusMaxBefore &&
                getMaxAttrib(player, WILLPOWER) == willpowerMaxBefore;
            result += " restored=" + restored;
            if (!restored)
            {
                result = "error=attributeBuffRestoreFailed " + result;
            }
        }
        return result;
    }

    private static boolean hasNamedModifier(
        obj_id target,
        int attribute,
        String name) throws InterruptedException
    {
        return getNamedModifierValue(target, attribute, name) !=
            Integer.MIN_VALUE;
    }

    private static int getNamedModifierValue(
        obj_id target,
        int attribute,
        String name) throws InterruptedException
    {
        attrib_mod[] mods = getAttribModifiers(target, attribute);
        if (mods != null)
        {
            for (attrib_mod mod : mods)
            {
                if (mod != null && name.equals(mod.getName()))
                {
                    return mod.getValue();
                }
            }
        }
        return Integer.MIN_VALUE;
    }
}
