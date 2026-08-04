package script.test;

import script.obj_id;

/** Reversible Master Pistoleer layer over the marksman combat fixture. */
public class precu_pistoleer_master_fixture extends script.base_script
{
    private static final long ATTACKER_OID = 44003778L;
    private static final int ATTACKER_STATION_ID = 91001;
    private static final String MARKSMAN_ROOT = "precu.p14.marksmanTier1Fixture";
    private static final String MARKSMAN_LIFECYCLE = MARKSMAN_ROOT + ".lifecycle";
    private static final String MARKSMAN_PREPARED = MARKSMAN_ROOT + ".prepared";
    private static final String ROOT = "precu.p14.pistoleerMasterFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_BITS = ROOT + ".originalBits";
    private static final String MULTI_TARGET_COMMAND = "multiTargetPistolShot";
    private static final String DISARM_COMMAND = "disarmingShot2";
    private static final String FAN_COMMAND = "fanShot";
    private static final String[] SKILLS =
    {
        "combat_pistol_ability_04",
        "combat_pistol_speed_01",
        "combat_pistol_speed_02",
        "combat_pistol_speed_03",
        "combat_pistol_speed_04",
        "combat_pistol_support_01",
        "combat_pistol_support_02",
        "combat_pistol_support_03",
        "combat_pistol_support_04",
        "combat_pistol_master"
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup <attackerOid> <lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 3)
        {
            return USAGE;
        }
        long value;
        try
        {
            value = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (value != ATTACKER_OID || !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }
        obj_id player = obj_id.getObjId(value);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != ATTACKER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        String ownership = validateOwnership(player, args[2]);
        if (ownership != null)
        {
            if (args[0].equalsIgnoreCase("cleanup") &&
                ownership.equals("error=fixtureAbsent"))
            {
                return "action=cleanup alreadyClean=true restored=true";
            }
            return ownership;
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return "action=status " + buildStatus(player);
        }
        if (args[0].equalsIgnoreCase("cleanup"))
        {
            return cleanup(player);
        }
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            if (ownership == null && getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " + buildStatus(player);
            }
            return ownership == null ? "error=fixtureIncomplete" : ownership;
        }
        if (!hasObjVar(player, MARKSMAN_PREPARED) ||
            getIntObjVar(player, MARKSMAN_PREPARED) != 1 ||
            !hasObjVar(player, MARKSMAN_LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, MARKSMAN_LIFECYCLE)))
        {
            return "error=marksmanFixtureNotPrepared";
        }

        int[] originalBits = new int[SKILLS.length];
        for (int index = 0; index < SKILLS.length; ++index)
        {
            originalBits[index] = hasSkill(player, SKILLS[index]) ? 1 : 0;
        }
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, ORIGINAL_BITS, originalBits);

        for (int index = 0; index < SKILLS.length; ++index)
        {
            if (originalBits[index] == 0 &&
                (!grantSkill(player, SKILLS[index]) || !hasSkill(player, SKILLS[index])))
            {
                restore(player);
                removeObjVar(player, ROOT);
                return "error=skillGrantFailed index=" + index;
            }
        }
        if (!hasSkill(player, "combat_pistol_master") ||
            !hasCommand(player, MULTI_TARGET_COMMAND) ||
            !hasCommand(player, DISARM_COMMAND) ||
            !hasCommand(player, FAN_COMMAND))
        {
            restore(player);
            removeObjVar(player, ROOT);
            return "error=commandGrantFailed";
        }
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String cleanup(obj_id player) throws InterruptedException
    {
        boolean restored = restore(player);
        if (restored)
        {
            removeObjVar(player, ROOT);
        }
        return "action=cleanup alreadyClean=false restored=" + restored;
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_BITS))
        {
            return false;
        }
        int[] originalBits = getIntArrayObjVar(player, ORIGINAL_BITS);
        if (originalBits == null || originalBits.length != SKILLS.length)
        {
            return false;
        }
        for (int index = SKILLS.length - 1; index >= 0; --index)
        {
            if (originalBits[index] == 0 && hasSkill(player, SKILLS[index]))
            {
                revokeSkill(player, SKILLS[index]);
            }
        }
        for (int index = 0; index < SKILLS.length; ++index)
        {
            if (hasSkill(player, SKILLS[index]) != (originalBits[index] == 1))
            {
                return false;
            }
        }
        return originalBits[SKILLS.length - 1] == 1 ||
            (!hasCommand(player, MULTI_TARGET_COMMAND) &&
                !hasCommand(player, DISARM_COMMAND) &&
                !hasCommand(player, FAN_COMMAND));
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < SKILLS.length; ++index)
        {
            bits += hasSkill(player, SKILLS[index]) ? "1" : "0";
        }
        return "player=" + player +
            " skillBits=" + bits +
            " master=" + (hasSkill(player, "combat_pistol_master") ? "1" : "0") +
            " hasMultiTargetPistolShot=" +
                (hasCommand(player, MULTI_TARGET_COMMAND) ? "1" : "0") +
            " canMultiTargetPistolShot=" +
                (hasSkill(player, "combat_pistol_master") &&
                    hasCommand(player, MULTI_TARGET_COMMAND) ? "1" : "0") +
            " hasDisarmingShot2=" +
                (hasCommand(player, DISARM_COMMAND) ? "1" : "0") +
            " canDisarmingShot2=" +
                (hasSkill(player, "combat_pistol_master") &&
                    hasCommand(player, DISARM_COMMAND) ? "1" : "0") +
            " hasFanShot=" +
                (hasCommand(player, FAN_COMMAND) ? "1" : "0") +
            " canFanShot=" +
                (hasSkill(player, "combat_pistol_ability_04") &&
                    hasCommand(player, FAN_COMMAND) ? "1" : "0");
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        if (lifecycle == null || lifecycle.length() != 32)
        {
            return false;
        }
        for (int index = 0; index < lifecycle.length(); ++index)
        {
            char value = lifecycle.charAt(index);
            boolean digit = value >= '0' && value <= '9';
            boolean lowerHex = value >= 'a' && value <= 'f';
            if (!digit && !lowerHex)
            {
                return false;
            }
        }
        return true;
    }
}
