package script.test;

import script.obj_id;
import script.library.buff;
import script.library.combat;
import script.library.skill;

/** Identity-bound reversible fixture for the Publish 14.1 berserk1 command. */
public class precu_berserk_one_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "berserk1";
    private static final String ROOT = "precu.b1Fixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String BERSERK_EXPIRY = "precu.berserk.expiresAt";
    private static final String BERSERK_STATUS_BUFF = "precu_berserk_status";
    private static final String[] SKILLS =
    {
        "combat_brawler",
        "combat_brawler_novice"
    };
    private static final int[] ATTRIBUTES =
    {
        HEALTH, ACTION, MIND, STRENGTH, QUICKNESS, FOCUS
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null
            ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 3 || !isValidLifecycle(args[2]))
        {
            return USAGE;
        }
        long oid;
        try
        {
            oid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (oid != PLAYER_OID)
        {
            return "error=playerIdentityRejected";
        }
        obj_id player = obj_id.getObjId(oid);
        if (!isAuthoritativePlayer(player))
        {
            return "error=playerUnavailable";
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, args[2]);
        }
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }
        obj_id weapon = getCurrentWeapon(player);
        if (hasAnyFixtureSkill(player) || hasCommand(player, COMMAND) ||
            getState(player, STATE_BERSERK) == 1 ||
            hasObjVar(player, BERSERK_EXPIRY) ||
            !isIdValid(weapon) ||
            (!combat.isMeleeWeapon(weapon) &&
                getWeaponType(weapon) != WEAPON_TYPE_UNARMED))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            snapshotAttribute(player, index, ATTRIBUTES[index]);
        }
        resetTelemetry(player);
        if (!grantSkills(player) || !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }

        setPreparedAttribute(player, HEALTH, 500);
        setPreparedAttribute(player, ACTION, 500);
        setPreparedAttribute(player, MIND, 500);
        if (getSkillStatMod(player, "berserk") != 0 ||
            getAttrib(player, HEALTH) < 500 ||
            getAttrib(player, ACTION) < 500 ||
            getAttrib(player, MIND) < 500)
        {
            String failedStatus = buildStatus(player);
            boolean restored = restore(player);
            return "error=attributePreparationFailed restored=" + restored +
                " observed=" + failedStatus;
        }
        setObjVar(player, PREPARED, 1);
        setObjVar(player, ROOT + ".outcome", "ready");
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null
            ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasCompleteSnapshot(player))
        {
            return "action=cleanup alreadyClean=false restored=false" +
                " incompleteSnapshot=true";
        }
        boolean restored = restore(player);
        return "action=cleanup alreadyClean=false restored=" + restored +
            (restored ? "" : " observed=" + buildStatus(player));
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        setState(player, STATE_BERSERK, false);
        if (buff.hasBuff(player, BERSERK_STATUS_BUFF))
        {
            buff.removeBuff(player, BERSERK_STATUS_BUFF);
        }
        if (hasObjVar(player, BERSERK_EXPIRY))
        {
            removeObjVar(player, BERSERK_EXPIRY);
        }
        revokeSkills(player);
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            restoreAttribute(player, index, ATTRIBUTES[index]);
        }
        boolean restored = getState(player, STATE_BERSERK) == 0 &&
            !buff.hasBuff(player, BERSERK_STATUS_BUFF) &&
            !hasObjVar(player, BERSERK_EXPIRY) &&
            !hasAnyFixtureSkill(player) && !hasCommand(player, COMMAND) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS);
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            restored = restored && attributeRestored(
                player, index, ATTRIBUTES[index]);
        }
        if (restored)
        {
            removeObjVar(player, ROOT);
        }
        return restored;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id weapon = getCurrentWeapon(player);
        int expiresAt = hasObjVar(player, BERSERK_EXPIRY)
            ? getIntObjVar(player, BERSERK_EXPIRY)
            : 0;
        return
            "player=" + player +
            " command=" + hasCommand(player, COMMAND) +
            " skillBits=" + buildSkillBits(player) +
            " weapon=" + weapon +
            " weaponType=" + (isIdValid(weapon) ? getWeaponType(weapon) : -1) +
            " meleeWeapon=" +
                (isIdValid(weapon) && combat.isMeleeWeapon(weapon)) +
            " berserkModifier=" + getSkillStatMod(player, "berserk") +
            " health=" + getAttrib(player, HEALTH) +
            " action=" + getAttrib(player, ACTION) +
            " mind=" + getAttrib(player, MIND) +
            " strength=" + getAttrib(player, STRENGTH) +
            " quickness=" + getAttrib(player, QUICKNESS) +
            " focus=" + getAttrib(player, FOCUS) +
            " berserkState=" + getState(player, STATE_BERSERK) +
            " expiresAt=" + expiresAt +
            " remaining=" + Math.max(0, expiresAt - getGameTime()) +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " randomRoll=" + readInt(player, ".randomRoll") +
            " chanceTotal=" + readInt(player, ".chanceTotal") +
            " healthCost=" + readInt(player, ".healthCost") +
            " actionCost=" + readInt(player, ".actionCost") +
            " mindCost=" + readInt(player, ".mindCost") +
            " healthBefore=" + readInt(player, ".healthBefore") +
            " healthAfter=" + readInt(player, ".healthAfter") +
            " actionBefore=" + readInt(player, ".actionBefore") +
            " actionAfter=" + readInt(player, ".actionAfter") +
            " mindBefore=" + readInt(player, ".mindBefore") +
            " mindAfter=" + readInt(player, ".mindAfter") +
            " activatedAt=" + readInt(player, ".activatedAt") +
            " expiredAt=" + readInt(player, ".expiredAt") +
            " outcome=" + readString(player, ".outcome") +
            " availablePoints=" + skill.getAvailableSkillPoints(player) +
            " snapshotComplete=" + hasCompleteSnapshot(player) +
            " restoreBits=" + buildRestoreBits(player) +
            " restoreDetail=" + buildRestoreDetail(player);
    }

    private void snapshotAttribute(obj_id player, int index, int attribute)
        throws InterruptedException
    {
        setObjVar(player, originalPath(index, false),
            getAttrib(player, attribute));
        setObjVar(player, originalPath(index, true),
            getMaxAttrib(player, attribute));
    }

    private void setPreparedAttribute(obj_id player, int attribute, int value)
        throws InterruptedException
    {
        setMaxAttrib(player, attribute,
            Math.max(value, getMaxAttrib(player, attribute)));
        setAttrib(player, attribute, value);
    }

    private void restoreAttribute(obj_id player, int index, int attribute)
        throws InterruptedException
    {
        setAttrib(player, attribute,
            getIntObjVar(player, originalPath(index, false)));
        setMaxAttrib(player, attribute,
            getIntObjVar(player, originalPath(index, true)));
    }

    private boolean attributeRestored(obj_id player, int index, int attribute)
        throws InterruptedException
    {
        int original = getIntObjVar(player, originalPath(index, false));
        int current = getAttrib(player, attribute);
        boolean currentRestored = index < 3
            ? current >= original
            : current == original;
        int originalMaximum = getIntObjVar(
            player, originalPath(index, true));
        boolean maximumRestored = index < 3
            ? getMaxAttrib(player, attribute) >= originalMaximum
            : getMaxAttrib(player, attribute) == originalMaximum;
        return currentRestored && maximumRestored;
    }

    private String buildRestoreBits(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return "none";
        }
        String bits = "";
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            bits += attributeRestored(player, index, ATTRIBUTES[index])
                ? "1"
                : "0";
        }
        return bits;
    }

    private String buildRestoreDetail(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return "none";
        }
        String detail = "";
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (index > 0)
            {
                detail += ",";
            }
            detail += index + ":" + getAttrib(player, ATTRIBUTES[index]) +
                "/" + getMaxAttrib(player, ATTRIBUTES[index]) +
                "/" + getIntObjVar(player, originalPath(index, false)) +
                "/" + getIntObjVar(player, originalPath(index, true));
        }
        return detail;
    }

    private String originalPath(int index, boolean maximum)
    {
        return ROOT + (maximum ? ".m" : ".c") + index;
    }

    private boolean grantSkills(obj_id player) throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (!grantSkill(player, skillName) || !hasSkill(player, skillName))
            {
                return false;
            }
        }
        return true;
    }

    private void revokeSkills(obj_id player) throws InterruptedException
    {
        for (int index = SKILLS.length - 1; index >= 0; --index)
        {
            if (hasSkill(player, SKILLS[index]))
            {
                revokeSkill(player, SKILLS[index]);
            }
        }
    }

    private boolean hasAnyFixtureSkill(obj_id player)
        throws InterruptedException
    {
        for (String skillName : SKILLS)
        {
            if (hasSkill(player, skillName))
            {
                return true;
            }
        }
        return false;
    }

    private String buildSkillBits(obj_id player) throws InterruptedException
    {
        String bits = "";
        for (String skillName : SKILLS)
        {
            bits += hasSkill(player, skillName) ? "1" : "0";
        }
        return bits;
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return isIdValid(player) && player.isLoaded() && isPlayer(player) &&
            player.getValue() == PLAYER_OID &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private boolean isFixtureOwner(obj_id player) throws InterruptedException
    {
        return isAuthoritativePlayer(player) &&
            hasObjVar(player, PROTOCOL) &&
            getIntObjVar(player, PROTOCOL) == PROTOCOL_VERSION &&
            hasObjVar(player, PREPARED) &&
            getIntObjVar(player, PREPARED) == 1;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        return isFixtureOwner(player) ? null : "error=fixtureNotPrepared";
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return getStringObjVar(player, LIFECYCLE).equals(lifecycle)
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) || !hasObjVar(player, PROTOCOL) ||
            !hasObjVar(player, ORIGINAL_POINTS))
        {
            return false;
        }
        for (int index = 0; index < ATTRIBUTES.length; ++index)
        {
            if (!hasObjVar(player, originalPath(index, false)) ||
                !hasObjVar(player, originalPath(index, true)))
            {
                return false;
            }
        }
        return true;
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered", "handlerCalls", "randomRoll",
            "berserkModifier", "chanceTotal", "healthCost", "actionCost",
            "mindCost", "healthBefore", "healthAfter", "actionBefore",
            "actionAfter", "mindBefore", "mindAfter", "expiresAt",
            "activatedAt", "expiredAt", "outcome"
        };
        for (String leaf : leaves)
        {
            String path = ROOT + "." + leaf;
            if (hasObjVar(player, path))
            {
                removeObjVar(player, path);
            }
        }
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : 0;
    }

    private String readString(obj_id player, String suffix)
        throws InterruptedException
    {
        String path = ROOT + suffix;
        return hasObjVar(player, path)
            ? getStringObjVar(player, path)
            : "none";
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
            if (!((value >= '0' && value <= '9') ||
                (value >= 'a' && value <= 'f')))
            {
                return false;
            }
        }
        return true;
    }
}
