package script.test;

import script.*;
import script.library.xp;

public class precu_xp_routing_runtime extends script.base_script
{
    private static final int RUNTIME_STATION_ID = 1001;
    private static final String VAR_ROOT = "test.precuXpRoute";
    private static final String VAR_FAMILY = VAR_ROOT + ".family";
    private static final String VAR_REQUESTED_TYPE = VAR_ROOT + ".requestedType";
    private static final String VAR_EFFECTIVE_TYPE = VAR_ROOT + ".effectiveType";
    private static final String VAR_BASELINE_XP = VAR_ROOT + ".baselineXp";
    private static final String VAR_BASELINE_SKILLS = VAR_ROOT + ".baselineSkills";
    private static final String VAR_AMOUNT = VAR_ROOT + ".amount";
    private static final String USAGE =
        "usage: begin <playerOid> combat|social|crafting|craftingQuest <xpType> <amount>; " +
        "verify <playerOid>; cancel <playerOid>";

    public String executeProbe(String params) throws InterruptedException
    {
        if (params == null || params.trim().length() == 0)
        {
            return USAGE;
        }

        String[] args = params.trim().split("\\s+");
        if (args.length < 2 || !args[1].matches("[0-9]+"))
        {
            return USAGE;
        }

        obj_id player;
        try
        {
            player = obj_id.getObjId(Long.parseLong(args[1]));
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }

        String playerError = validatePlayer(player);
        if (playerError != null)
        {
            return playerError;
        }

        String action = args[0];
        if (action.equalsIgnoreCase("begin"))
        {
            if (args.length != 5 || hasObjVar(player, VAR_ROOT))
            {
                return hasObjVar(player, VAR_ROOT) ? "error=probeAlreadyActive" : USAGE;
            }

            int amount;
            try
            {
                amount = Integer.parseInt(args[4]);
            }
            catch (NumberFormatException exception)
            {
                return "error=invalidAmount";
            }
            if (amount < 1 || amount > 1000)
            {
                return "error=amountOutOfRange";
            }

            String family = args[2];
            String requestedType = args[3];
            String effectiveType = getEffectiveType(family, requestedType);
            if (effectiveType == null)
            {
                return "error=invalidRoute family=" + family + " xpType=" + requestedType;
            }

            int baselineXp = getExperiencePoints(player, effectiveType);
            int baselineSkills = getSkillCount(player);
            setObjVar(player, VAR_FAMILY, family);
            setObjVar(player, VAR_REQUESTED_TYPE, requestedType);
            setObjVar(player, VAR_EFFECTIVE_TYPE, effectiveType);
            setObjVar(player, VAR_BASELINE_XP, baselineXp);
            setObjVar(player, VAR_BASELINE_SKILLS, baselineSkills);
            setObjVar(player, VAR_AMOUNT, amount);

            int queued = grantRoute(player, family, requestedType, amount);
            return "action=begin queued=" + (queued > 0) +
                " oid=" + player + " family=" + family +
                " requestedType=" + requestedType + " effectiveType=" + effectiveType +
                " amount=" + amount + " baselineXp=" + baselineXp +
                " baselineSkills=" + baselineSkills;
        }

        if (action.equalsIgnoreCase("verify"))
        {
            if (args.length != 2 || !hasObjVar(player, VAR_ROOT))
            {
                return args.length == 2 ? "error=noActiveProbe" : USAGE;
            }

            String family = getStringObjVar(player, VAR_FAMILY);
            String requestedType = getStringObjVar(player, VAR_REQUESTED_TYPE);
            String effectiveType = getStringObjVar(player, VAR_EFFECTIVE_TYPE);
            int amount = getIntObjVar(player, VAR_AMOUNT);
            int baselineXp = getIntObjVar(player, VAR_BASELINE_XP);
            int baselineSkills = getIntObjVar(player, VAR_BASELINE_SKILLS);
            int observedXp = getExperiencePoints(player, effectiveType);
            int observedSkills = getSkillCount(player);
            int delta = observedXp - baselineXp;
            boolean skillsStable = observedSkills == baselineSkills;

            if (delta != 0)
            {
                grantExperiencePoints(player, effectiveType, -delta);
            }
            int restoredXp = getExperiencePoints(player, effectiveType);
            removeObjVar(player, VAR_ROOT);

            return "action=verify oid=" + player + " family=" + family +
                " requestedType=" + requestedType + " effectiveType=" + effectiveType +
                " amount=" + amount + " delta=" + delta +
                " baselineSkills=" + baselineSkills + " observedSkills=" + observedSkills +
                " skillsStable=" + skillsStable + " restored=" + (restoredXp == baselineXp) +
                " restoredXp=" + restoredXp;
        }

        if (action.equalsIgnoreCase("cancel"))
        {
            if (args.length != 2)
            {
                return USAGE;
            }
            if (!hasObjVar(player, VAR_ROOT))
            {
                return "action=cancel active=false";
            }
            String effectiveType = getStringObjVar(player, VAR_EFFECTIVE_TYPE);
            int baselineXp = getIntObjVar(player, VAR_BASELINE_XP);
            int currentXp = getExperiencePoints(player, effectiveType);
            if (currentXp != baselineXp)
            {
                grantExperiencePoints(player, effectiveType, baselineXp - currentXp);
            }
            removeObjVar(player, VAR_ROOT);
            return "action=cancel active=true restored=" +
                (getExperiencePoints(player, effectiveType) == baselineXp);
        }

        return USAGE;
    }

    private String validatePlayer(obj_id player) throws InterruptedException
    {
        if (player == null || player == obj_id.NULL_ID)
        {
            return "error=invalidPlayer";
        }
        if (!player.isLoaded())
        {
            return "error=playerNotLoaded oid=" + player;
        }
        if (!player.isAuthoritative())
        {
            return "error=playerNotAuthoritative oid=" + player +
                " process=" + player.getProcessId();
        }
        if (!isPlayer(player))
        {
            return "error=objectIsNotPlayer oid=" + player;
        }
        if (getPlayerStationId(player) != RUNTIME_STATION_ID)
        {
            return "error=stationNotAllowed oid=" + player;
        }
        return null;
    }

    private String getEffectiveType(String family, String requestedType)
        throws InterruptedException
    {
        if (family.equalsIgnoreCase("combat"))
        {
            return xp.getPrecuCombatXpType(requestedType);
        }
        if (family.equalsIgnoreCase("social"))
        {
            return xp.getPrecuSocialXpType(requestedType);
        }
        if (family.equalsIgnoreCase("crafting"))
        {
            return xp.getPrecuCraftingXpType(requestedType);
        }
        if (family.equalsIgnoreCase("craftingQuest"))
        {
            return xp.CRAFTING_GENERAL;
        }
        return null;
    }

    private int grantRoute(obj_id player, String family, String requestedType, int amount)
        throws InterruptedException
    {
        if (family.equalsIgnoreCase("combat"))
        {
            return xp.grantCombatStyleXp(player, requestedType, amount);
        }
        if (family.equalsIgnoreCase("social"))
        {
            return xp.grantSocialStyleXp(player, requestedType, amount);
        }
        if (family.equalsIgnoreCase("crafting"))
        {
            return xp.grantCraftingStyleXp(player, requestedType, amount);
        }
        if (family.equalsIgnoreCase("craftingQuest"))
        {
            return xp.grantCraftingQuestXp(player, amount);
        }
        return 0;
    }

    private int getSkillCount(obj_id player) throws InterruptedException
    {
        String[] skills = getSkillListingForPlayer(player);
        return skills == null ? 0 : skills.length;
    }
}
