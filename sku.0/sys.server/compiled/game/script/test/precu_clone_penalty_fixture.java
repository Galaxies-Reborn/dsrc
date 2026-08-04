package script.test;

import script.library.cloninglib;
import script.library.utils;
import script.obj_id;

/**
 * Identity-bound, reversible ServerConsole fixture for Publish 14.1 clone
 * wounds, battle fatigue, insured/uninsured decay, and death-type exclusion.
 *
 * Every pre-existing decay-eligible item is snapshotted before the production
 * helper is invoked and restored during each reset and cleanup.
 */
public class precu_clone_penalty_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT = "precu.clonePenaltyFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_WOUNDS = ROOT + ".originalWounds";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_ITEMS = ROOT + ".originalItems";
    private static final String ORIGINAL_ITEM_COUNT =
        ROOT + ".originalItemCount";
    private static final String ORIGINAL_ITEM_HP =
        ROOT + ".originalItemHitpoints";
    private static final String ORIGINAL_ITEM_INSURED =
        ROOT + ".originalItemInsured";
    private static final String INSURED_ITEM = ROOT + ".insuredItem";
    private static final String UNINSURED_ITEM = ROOT + ".uninsuredItem";
    private static final String AUTO_INSURED_ITEM =
        ROOT + ".autoInsuredItem";
    private static final String WEAPON_TEMPLATE =
        "object/weapon/ranged/rifle/rifle_cdef.iff";
    private static final int FIXTURE_MAX_HITPOINTS = 1000;
    private static final int[] PRIMARY =
        new int[] { HEALTH, ACTION, MIND };
    private static final String USAGE =
        "usage: prepare|unboundPve|boundPve|unboundPvp|status|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args =
            params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 3)
        {
            return USAGE;
        }

        long playerValue;
        try
        {
            playerValue = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (playerValue != PLAYER_OID || !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        String action = args[0];
        String lifecycle = args[2];
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, lifecycle);
        }
        if (action.equalsIgnoreCase("unboundPve"))
        {
            return applyMode(player, lifecycle, false, true, action);
        }
        if (action.equalsIgnoreCase("boundPve"))
        {
            return applyMode(player, lifecycle, true, true, action);
        }
        if (action.equalsIgnoreCase("unboundPvp"))
        {
            return applyMode(player, lifecycle, false, false, action);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return status(player, lifecycle);
        }
        if (action.equalsIgnoreCase("cleanup"))
        {
            return cleanup(player, lifecycle);
        }
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            if (ownership != null)
            {
                return ownership;
            }
            return "action=prepare resumed=true " + buildStatus(player);
        }

        int[] originalWounds = new int[PRIMARY.length];
        for (int index = 0; index < PRIMARY.length; ++index)
        {
            originalWounds[index] =
                getAttribWound(player, PRIMARY[index]);
        }
        setObjVar(player, ORIGINAL_WOUNDS, originalWounds);
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(player, LIFECYCLE, lifecycle);
        snapshotEligibleItems(player);

        obj_id insured = createFixtureItem(player);
        obj_id uninsured = createFixtureItem(player);
        obj_id autoInsured = createFixtureItem(player);
        if (!isIdValid(insured) || !isIdValid(uninsured) ||
            !isIdValid(autoInsured))
        {
            destroyIfValid(insured);
            destroyIfValid(uninsured);
            destroyIfValid(autoInsured);
            restoreEligibleItems(player);
            removeObjVar(player, ROOT);
            return "error=fixtureItemCreateFailed";
        }

        setObjVar(player, INSURED_ITEM, insured);
        setObjVar(player, UNINSURED_ITEM, uninsured);
        setObjVar(player, AUTO_INSURED_ITEM, autoInsured);
        if (!configureFixtureItems(insured, uninsured, autoInsured) ||
            !resetControlledState(player))
        {
            cleanup(player, lifecycle);
            return "error=fixtureSetupFailed";
        }
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String applyMode(
        obj_id player,
        String lifecycle,
        boolean registeredFacility,
        boolean decayItems,
        String action) throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (!resetControlledState(player))
        {
            return "error=fixtureResetFailed " + buildStatus(player);
        }

        cloninglib.applyPrecuClonePenalties(
            player,
            registeredFacility,
            decayItems);

        int expectedWound =
            registeredFacility
                ? 0
                : cloninglib.PRECU_CLONE_WOUND_AMOUNT;
        int expectedInsuredHp =
            decayItems
                ? 990
                : FIXTURE_MAX_HITPOINTS;
        int expectedUninsuredHp =
            decayItems
                ? 950
                : FIXTURE_MAX_HITPOINTS;
        obj_id insured = getObjIdObjVar(player, INSURED_ITEM);
        obj_id uninsured = getObjIdObjVar(player, UNINSURED_ITEM);
        obj_id autoInsured = getObjIdObjVar(player, AUTO_INSURED_ITEM);
        boolean passed =
            getAttribWound(player, HEALTH) == expectedWound &&
            getAttribWound(player, ACTION) == expectedWound &&
            getAttribWound(player, MIND) == expectedWound &&
            getShockWound(player) == expectedWound &&
            getHitpoints(insured) == expectedInsuredHp &&
            getHitpoints(uninsured) == expectedUninsuredHp &&
            getHitpoints(autoInsured) == FIXTURE_MAX_HITPOINTS &&
            isInsured(insured) == !decayItems &&
            !isInsured(uninsured) &&
            isAutoInsured(autoInsured);
        return "action=" + action +
            " passed=" + passed +
            " registeredFacility=" + registeredFacility +
            " decayItems=" + decayItems + " " +
            buildStatus(player);
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        return "action=status " + buildStatus(player);
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (utils.hasScriptVar(
            player,
            cloninglib.PRECU_DECAY_REPORT_SUI))
        {
            forceCloseSUIPage(
                utils.getIntScriptVar(
                    player,
                    cloninglib.PRECU_DECAY_REPORT_SUI));
        }
        utils.removeScriptVarTree(
            player,
            cloninglib.PRECU_DECAY_REPORT_ROOT);

        int[] originalWounds =
            getIntArrayObjVar(player, ORIGINAL_WOUNDS);
        boolean restored =
            originalWounds != null &&
            originalWounds.length == PRIMARY.length;
        if (restored)
        {
            for (int index = 0; index < PRIMARY.length; ++index)
            {
                restored =
                    setWound(
                        player,
                        PRIMARY[index],
                        originalWounds[index]) &&
                    restored;
            }
        }
        int originalShock = getIntObjVar(player, ORIGINAL_SHOCK);
        restored =
            setShockWound(player, originalShock) &&
            getShockWound(player) == originalShock &&
            restoreEligibleItems(player) &&
            restored;
        if (!restored)
        {
            return "error=cleanupRestoreFailed " + buildStatus(player);
        }

        destroyIfValid(getObjIdObjVar(player, INSURED_ITEM));
        destroyIfValid(getObjIdObjVar(player, UNINSURED_ITEM));
        destroyIfValid(getObjIdObjVar(player, AUTO_INSURED_ITEM));
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true " +
            "healthWound=" + getAttribWound(player, HEALTH) +
            " actionWound=" + getAttribWound(player, ACTION) +
            " mindWound=" + getAttribWound(player, MIND) +
            " shock=" + getShockWound(player);
    }

    private void snapshotEligibleItems(obj_id player)
        throws InterruptedException
    {
        obj_id[] allItems = getInventoryAndEquipment(player);
        int count = 0;
        if (allItems != null)
        {
            for (obj_id item : allItems)
            {
                if (cloninglib.isDamagedOnClone(player, item))
                {
                    ++count;
                }
            }
        }

        obj_id[] items = new obj_id[count];
        int[] hitpoints = new int[count];
        int[] insured = new int[count];
        int index = 0;
        if (allItems != null)
        {
            for (obj_id item : allItems)
            {
                if (!cloninglib.isDamagedOnClone(player, item))
                {
                    continue;
                }
                items[index] = item;
                hitpoints[index] = getHitpoints(item);
                insured[index] = isInsured(item) ? 1 : 0;
                ++index;
            }
        }
        setObjVar(player, ORIGINAL_ITEMS, items);
        setObjVar(player, ORIGINAL_ITEM_HP, hitpoints);
        setObjVar(player, ORIGINAL_ITEM_INSURED, insured);
        setObjVar(player, ORIGINAL_ITEM_COUNT, count);
    }

    private boolean restoreEligibleItems(obj_id player)
        throws InterruptedException
    {
        int count = getIntObjVar(player, ORIGINAL_ITEM_COUNT);
        if (count == 0)
        {
            return true;
        }
        obj_id[] items = getObjIdArrayObjVar(player, ORIGINAL_ITEMS);
        int[] hitpoints = getIntArrayObjVar(player, ORIGINAL_ITEM_HP);
        int[] insured = getIntArrayObjVar(player, ORIGINAL_ITEM_INSURED);
        if (items == null || hitpoints == null || insured == null ||
            items.length != count ||
            items.length != hitpoints.length ||
            items.length != insured.length)
        {
            return false;
        }
        boolean restored = true;
        for (int index = 0; index < items.length; ++index)
        {
            obj_id item = items[index];
            if (!isIdValid(item) || !exists(item))
            {
                restored = false;
                continue;
            }
            restored =
                setHitpoints(item, hitpoints[index]) &&
                setInsured(item, insured[index] != 0) &&
                restored;
        }
        return restored;
    }

    private obj_id createFixtureItem(obj_id player)
        throws InterruptedException
    {
        return createObjectInInventoryAllowOverload(
            WEAPON_TEMPLATE,
            player);
    }

    private boolean configureFixtureItems(
        obj_id insured,
        obj_id uninsured,
        obj_id autoInsured) throws InterruptedException
    {
        boolean configured = true;
        obj_id[] items =
            new obj_id[] { insured, uninsured, autoInsured };
        for (obj_id item : items)
        {
            configured =
                setMaxHitpoints(item, FIXTURE_MAX_HITPOINTS) &&
                setHitpoints(item, FIXTURE_MAX_HITPOINTS) &&
                configured;
        }
        configured =
            setUninsurable(insured, false) &&
            setInsured(insured, true) &&
            setUninsurable(uninsured, false) &&
            setInsured(uninsured, false) &&
            setAutoInsured(autoInsured) &&
            configured;
        return configured;
    }

    private boolean resetControlledState(obj_id player)
        throws InterruptedException
    {
        boolean reset = restoreEligibleItems(player);
        for (int attribute : PRIMARY)
        {
            reset = setWound(player, attribute, 0) && reset;
        }
        reset =
            setShockWound(player, 0) &&
            configureFixtureItems(
                getObjIdObjVar(player, INSURED_ITEM),
                getObjIdObjVar(player, UNINSURED_ITEM),
                getObjIdObjVar(player, AUTO_INSURED_ITEM)) &&
            reset;
        return reset;
    }

    private boolean setWound(obj_id player, int attribute, int requested)
        throws InterruptedException
    {
        int current = getAttribWound(player, attribute);
        if (current < requested)
        {
            addWound(player, attribute, requested - current);
        }
        else if (current > requested)
        {
            healWound(player, attribute, current - requested);
        }
        return getAttribWound(player, attribute) == requested;
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, ORIGINAL_WOUNDS) ||
            !hasObjVar(player, ORIGINAL_SHOCK) ||
            !hasObjVar(player, ORIGINAL_ITEM_COUNT) ||
            !hasObjVar(player, INSURED_ITEM) ||
            !hasObjVar(player, UNINSURED_ITEM) ||
            !hasObjVar(player, AUTO_INSURED_ITEM))
        {
            return "error=fixtureAbsent";
        }
        int itemCount = getIntObjVar(player, ORIGINAL_ITEM_COUNT);
        if (itemCount > 0 &&
            (!hasObjVar(player, ORIGINAL_ITEMS) ||
            !hasObjVar(player, ORIGINAL_ITEM_HP) ||
            !hasObjVar(player, ORIGINAL_ITEM_INSURED)))
        {
            return "error=fixtureAbsent";
        }
        if (!lifecycle.equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=lifecycleMismatch";
        }
        return null;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id insured = getObjIdObjVar(player, INSURED_ITEM);
        obj_id uninsured = getObjIdObjVar(player, UNINSURED_ITEM);
        obj_id autoInsured = getObjIdObjVar(player, AUTO_INSURED_ITEM);
        return "healthWound=" + getAttribWound(player, HEALTH) +
            " actionWound=" + getAttribWound(player, ACTION) +
            " mindWound=" + getAttribWound(player, MIND) +
            " shock=" + getShockWound(player) +
            " insuredHp=" + getHitpoints(insured) +
            " insuredFlag=" + isInsured(insured) +
            " uninsuredHp=" + getHitpoints(uninsured) +
            " uninsuredFlag=" + isInsured(uninsured) +
            " autoInsuredHp=" + getHitpoints(autoInsured) +
            " autoInsuredFlag=" + isAutoInsured(autoInsured) +
            " decayReportActive=" +
            utils.hasScriptVar(
                player,
                cloninglib.PRECU_DECAY_REPORT_SUI);
    }

    private void destroyIfValid(obj_id item) throws InterruptedException
    {
        if (isIdValid(item) && exists(item))
        {
            destroyObject(item);
        }
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
