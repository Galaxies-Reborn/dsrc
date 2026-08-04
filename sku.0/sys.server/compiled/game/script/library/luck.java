package script.library;

import script.obj_id;
import script.prose_package;
import script.string_id;

public class luck extends script.base_script
{
    public luck()
    {
    }
    public static boolean isLucky(obj_id player) throws InterruptedException
    {
        return isLucky(player, 0.10f, true);
    }
    public static boolean isLucky(obj_id player, float mod) throws InterruptedException
    {
        return isLucky(player, mod, true);
    }
    public static boolean isLucky(obj_id player, float mod, boolean showFlyText) throws InterruptedException
    {
        // Retained NGE consumers use this generic level-capped primary-stat
        // proc. Publish 14 Luck is system-specific, so fail this ABI closed.
        return false;
    }
    public static int getPrecuCraftingLuckRoll(obj_id player) throws InterruptedException
    {
        if (!isPlayer(player))
        {
            return 0;
        }
        int luckSkill = Math.max(0, getSkillStatisticModifier(player, "luck"));
        int forceLuckSkill = Math.max(0, getSkillStatisticModifier(player, "force_luck"));
        int totalLuck = luckSkill + forceLuckSkill;
        if (totalLuck <= 0)
        {
            return 0;
        }
        return rand(0, totalLuck);
    }
    public static void showLuckyFlyText(obj_id player) throws InterruptedException
    {
        prose_package pp = new prose_package();
        pp = prose.setStringId(pp, new string_id("system_msg", "lucky_fly_text"));
        showFlyTextPrivateProseWithFlags(player, player, pp, 1.5f, colors.GOLD, FLY_TEXT_FLAG_IS_LUCKY);
    }
}
