package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.callable;
import script.library.create;
import script.library.pet_lib;
import script.library.skill;
import script.library.utils;

/** Identity-bound reversible fixture for the Publish 14.1 healMind command. */
public class precu_heal_mind_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "healMind";
    private static final String CREATURE_TYPE = "worrt";
    private static final String ROOT = "precu.healMindCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String PET = ROOT + ".pet";
    private static final String PCD = ROOT + ".pcd";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_MIND_MAX = ROOT + ".originalMindMax";
    private static final String ORIGINAL_MIND_WOUND = ROOT + ".originalMindWound";
    private static final String ORIGINAL_FOCUS_WOUND = ROOT + ".originalFocusWound";
    private static final String ORIGINAL_WILLPOWER_WOUND =
        ROOT + ".originalWillpowerWound";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_PET_MASTER = ROOT + ".originalPetMaster";
    private static final String BEFORE_MIND = ROOT + ".beforeMind";
    private static final String BEFORE_MIND_WOUND = ROOT + ".beforeMindWound";
    private static final String BEFORE_FOCUS_WOUND = ROOT + ".beforeFocusWound";
    private static final String BEFORE_WILLPOWER_WOUND =
        ROOT + ".beforeWillpowerWound";
    private static final String BEFORE_SHOCK = ROOT + ".beforeShock";
    private static final String BEFORE_PET_MIND = ROOT + ".beforePetMind";
    private static final String FIXTURE_SCRIPT =
        "test.precu_heal_mind_command_fixture";
    private static final String[] SKILLS =
    {
        "science_combatmedic",
        "science_combatmedic_novice",
        "science_combatmedic_healing_range_speed_01",
        "science_combatmedic_healing_range_speed_02",
        "science_combatmedic_healing_range_speed_03",
        "science_combatmedic_healing_range_speed_04"
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

    public int OnAttach(obj_id self) throws InterruptedException
    {
        startPoll(self);
        return SCRIPT_CONTINUE;
    }

    public int OnLogin(obj_id self) throws InterruptedException
    {
        startPoll(self);
        return SCRIPT_CONTINUE;
    }

    public int handlePrecuHealMindFixturePoll(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isFixtureOwner(self))
        {
            utils.removeScriptVar(self, ROOT + ".pollActive");
            return SCRIPT_CONTINUE;
        }
        if (readInt(self, ".prepareRequested") == 1)
        {
            removeObjVar(self, ROOT + ".prepareRequested");
            preparePet(self);
        }
        messageTo(self, "handlePrecuHealMindFixturePoll", null, 1.0f, false);
        return SCRIPT_CONTINUE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            if (ownership == null)
            {
                return "action=prepare resumed=true " + buildStatus(player);
            }
            return ownership;
        }
        if (hasAnyFixtureSkill(player) || hasCommand(player, COMMAND) ||
            callable.hasCallable(player, callable.CALLABLE_TYPE_COMBAT_PET) ||
            callable.getNumStoredCDByType(
                player, callable.CALLABLE_TYPE_COMBAT_PET) != 0 ||
            hasScript(player, FIXTURE_SCRIPT))
        {
            return "error=fixtureVectorAlreadyOwned";
        }
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_MIND_MAX, getMaxAttrib(player, MIND));
        setObjVar(player, ORIGINAL_MIND_WOUND, getAttribWound(player, MIND));
        setObjVar(player, ORIGINAL_FOCUS_WOUND, getAttribWound(player, FOCUS));
        setObjVar(player, ORIGINAL_WILLPOWER_WOUND,
            getAttribWound(player, WILLPOWER));
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_PET_MASTER,
            hasScript(player, "ai.pet_master") ? 1 : 0);
        resetTelemetry(player);
        if (!grantSkills(player) || !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        setMaxAttrib(player, MIND, Math.max(1000, getMaxAttrib(player, MIND)));
        setAttrib(player, MIND, 1000);
        if (!setWoundExact(player, MIND, 0) ||
            !setWoundExact(player, FOCUS, 0) ||
            !setWoundExact(player, WILLPOWER, 0) ||
            !setShockWound(player, 0))
        {
            boolean restored = restore(player);
            return "error=attributePreparationFailed restored=" + restored;
        }
        setObjVar(player, PREPARED, 1);
        attachScript(player, FIXTURE_SCRIPT);
        if (!hasScript(player, FIXTURE_SCRIPT))
        {
            boolean restored = restore(player);
            return "error=fixtureScriptAttachFailed restored=" + restored;
        }
        setObjVar(player, ROOT + ".prepareRequested", 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private void preparePet(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, PET))
        {
            return;
        }
        location targetLocation = new location(getLocation(player));
        targetLocation.x += 2.0f;
        obj_id target = createFixtureTarget(targetLocation);
        if (!isIdValid(target) || !target.isLoaded())
        {
            setObjVar(player, ROOT + ".outcome", "targetPreparationFailed");
            return;
        }
        setObjVar(player, TARGET, target);
        obj_id pcd = pet_lib.makeTamedCreature(player, target, 1);
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            setObjVar(player, PCD, pcd);
        }
        obj_id pet = isIdValid(pcd) && pcd.isLoaded()
            ? callable.getCDCallable(pcd)
            : obj_id.NULL_ID;
        if (!isIdValid(pet) || !pet.isLoaded())
        {
            pet = callable.getCallable(
                player, callable.CALLABLE_TYPE_COMBAT_PET);
        }
        if (isIdValid(pet) && pet.isLoaded())
        {
            setObjVar(player, PET, pet);
        }
        if (!isIdValid(pcd) || !pcd.isLoaded() ||
            !isIdValid(pet) || !pet.isLoaded() || pet != target)
        {
            setObjVar(player, ROOT + ".outcome", "petPreparationFailed");
            return;
        }
        callable.setCallable(player, pet, callable.CALLABLE_TYPE_COMBAT_PET);
        int petMindMax = getWoundedMaxAttrib(pet, MIND);
        setAttrib(pet, MIND, Math.max(1, petMindMax - 200));
        setObjVar(player, BEFORE_MIND, getAttrib(player, MIND));
        setObjVar(player, BEFORE_MIND_WOUND, getAttribWound(player, MIND));
        setObjVar(player, BEFORE_FOCUS_WOUND, getAttribWound(player, FOCUS));
        setObjVar(player, BEFORE_WILLPOWER_WOUND,
            getAttribWound(player, WILLPOWER));
        setObjVar(player, BEFORE_SHOCK, getShockWound(player));
        setObjVar(player, BEFORE_PET_MIND, getAttrib(pet, MIND));
        setObjVar(player, ROOT + ".outcome", "ready");
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
        return "action=cleanup alreadyClean=false restored=" + restore(player);
    }

    private boolean restore(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        utils.removeScriptVar(player, ROOT + ".pollActive");
        obj_id pcd = readObjId(player, PCD);
        obj_id pet = readObjId(player, PET);
        callable.setCallable(player, null, callable.CALLABLE_TYPE_COMBAT_PET);
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            callable.setCDCallable(pcd, null);
        }
        if (isIdValid(pet) && pet.isLoaded())
        {
            if (getMaster(pet) == player)
            {
                setMaster(pet, null);
            }
            destroyObject(pet);
        }
        obj_id target = readObjId(player, TARGET);
        if (isIdValid(target) && target.isLoaded() && target != pet)
        {
            destroyObject(target);
        }
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            destroyObject(pcd);
        }
        setWoundExact(player, MIND, getIntObjVar(player, ORIGINAL_MIND_WOUND));
        setWoundExact(player, FOCUS, getIntObjVar(player, ORIGINAL_FOCUS_WOUND));
        setWoundExact(player, WILLPOWER,
            getIntObjVar(player, ORIGINAL_WILLPOWER_WOUND));
        setShockWound(player, getIntObjVar(player, ORIGINAL_SHOCK));
        setMaxAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MIND_MAX));
        setAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MIND));
        revokeSkills(player);
        if (getIntObjVar(player, ORIGINAL_PET_MASTER) == 0 &&
            hasScript(player, "ai.pet_master"))
        {
            detachScript(player, "ai.pet_master");
        }
        if (hasScript(player, FIXTURE_SCRIPT))
        {
            detachScript(player, FIXTURE_SCRIPT);
        }
        boolean restored =
            getAttrib(player, MIND) == getIntObjVar(player, ORIGINAL_MIND) &&
            getMaxAttrib(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MIND_MAX) &&
            getAttribWound(player, MIND) ==
                getIntObjVar(player, ORIGINAL_MIND_WOUND) &&
            getAttribWound(player, FOCUS) ==
                getIntObjVar(player, ORIGINAL_FOCUS_WOUND) &&
            getAttribWound(player, WILLPOWER) ==
                getIntObjVar(player, ORIGINAL_WILLPOWER_WOUND) &&
            getShockWound(player) == getIntObjVar(player, ORIGINAL_SHOCK) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) && !hasCommand(player, COMMAND) &&
            !callable.hasCallable(player, callable.CALLABLE_TYPE_COMBAT_PET) &&
            callable.getNumStoredCDByType(
                player, callable.CALLABLE_TYPE_COMBAT_PET) == 0 &&
            getIntObjVar(player, ORIGINAL_PET_MASTER) ==
                (hasScript(player, "ai.pet_master") ? 1 : 0);
        removeObjVar(player, ROOT);
        return restored;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id pet = readObjId(player, PET);
        obj_id pcd = readObjId(player, PCD);
        boolean petAvailable = isIdValid(pet) && pet.isLoaded();
        boolean pcdAvailable = isIdValid(pcd) && pcd.isLoaded();
        boolean pcdContained = false;
        if (pcdAvailable)
        {
            obj_id datapad = getContainedBy(pcd);
            pcdContained = isIdValid(datapad) && datapad.isLoaded() &&
                getContainedBy(datapad) == player;
        }
        int petMind = petAvailable ? getAttrib(pet, MIND) : 0;
        return
            "player=" + player +
            " pet=" + pet +
            " petAvailable=" + petAvailable +
            " pcd=" + pcd +
            " pcdAvailable=" + pcdAvailable +
            " pcdContained=" + pcdContained +
            " masterLinked=" + (petAvailable && getMaster(pet) == player) +
            " creaturePet=" + (petAvailable && pet_lib.isCreaturePet(pet)) +
            " command=" + hasCommand(player, COMMAND) +
            " skillBits=" + buildSkillBits(player) +
            " effectiveness=" +
                getSkillStatMod(player, "combat_medic_effectiveness") +
            " playerMind=" + getAttrib(player, MIND) +
            " playerMindDelta=" + delta(player, BEFORE_MIND,
                getAttrib(player, MIND)) +
            " petMindBefore=" + readIntPath(player, BEFORE_PET_MIND) +
            " petMind=" + petMind +
            " petMindDelta=" + delta(player, BEFORE_PET_MIND, petMind) +
            " mindWound=" + getAttribWound(player, MIND) +
            " mindWoundDelta=" + delta(player, BEFORE_MIND_WOUND,
                getAttribWound(player, MIND)) +
            " focusWound=" + getAttribWound(player, FOCUS) +
            " focusWoundDelta=" + delta(player, BEFORE_FOCUS_WOUND,
                getAttribWound(player, FOCUS)) +
            " willpowerWound=" + getAttribWound(player, WILLPOWER) +
            " willpowerWoundDelta=" + delta(player,
                BEFORE_WILLPOWER_WOUND, getAttribWound(player, WILLPOWER)) +
            " battleFatigue=" + getShockWound(player) +
            " battleFatigueDelta=" + delta(player, BEFORE_SHOCK,
                getShockWound(player)) +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " randomRoll=" + readInt(player, ".randomRoll") +
            " rawPower=" + readInt(player, ".rawPower") +
            " healPower=" + readInt(player, ".healPower") +
            " healedMind=" + readInt(player, ".healedMind") +
            " targetMindBefore=" + readInt(player, ".targetMindBefore") +
            " targetMindAfter=" + readInt(player, ".targetMindAfter") +
            " healerMindBefore=" + readInt(player, ".healerMindBefore") +
            " healerMindAfterWounds=" +
                readInt(player, ".healerMindAfterWounds") +
            " woundCost=" + readInt(player, ".woundCost") +
            " outcome=" + readString(player, ".outcome") +
            " availablePoints=" + skill.getAvailableSkillPoints(player);
    }

    private obj_id createFixtureTarget(location targetLocation)
        throws InterruptedException
    {
        dictionary creatureData =
            dataTableGetRow(create.CREATURE_TABLE, CREATURE_TYPE);
        if (creatureData == null)
        {
            return obj_id.NULL_ID;
        }
        String templateName = creatureData.getString("template");
        if (templateName == null || templateName.length() == 0)
        {
            return obj_id.NULL_ID;
        }
        creatureData.put("lootTable", "");
        obj_id target = createObject(
            create.TEMPLATE_PREFIX + templateName, targetLocation);
        if (!isIdValid(target))
        {
            return obj_id.NULL_ID;
        }
        create.randomlyNameCreature(target, CREATURE_TYPE);
        create.initializeCreature(target, CREATURE_TYPE, creatureData, -1);
        create.attachCreatureScripts(
            target, creatureData.getString("scripts"), true);
        if (!hasScript(target, "ai.pet_advance"))
        {
            attachScript(target, "ai.pet_advance");
        }
        return target;
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
        return hasObjVar(player, LIFECYCLE) && hasObjVar(player, PROTOCOL) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MIND_MAX) &&
            hasObjVar(player, ORIGINAL_MIND_WOUND) &&
            hasObjVar(player, ORIGINAL_FOCUS_WOUND) &&
            hasObjVar(player, ORIGINAL_WILLPOWER_WOUND) &&
            hasObjVar(player, ORIGINAL_SHOCK) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_PET_MASTER);
    }

    private void startPoll(obj_id player) throws InterruptedException
    {
        if (utils.hasScriptVar(player, ROOT + ".pollActive"))
        {
            return;
        }
        utils.setScriptVar(player, ROOT + ".pollActive", 1);
        messageTo(player, "handlePrecuHealMindFixturePoll", null, 1.0f, false);
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered", "handlerCalls", "randomRoll", "effectiveness",
            "rawPower", "healPower", "healedMind", "woundCost",
            "targetMindBefore", "targetMindAfter", "completedAt", "outcome",
            "healerMindBefore", "healerMindAfterWounds", "prepareRequested"
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

    private boolean setWoundExact(obj_id target, int attribute, int requested)
        throws InterruptedException
    {
        int current = getAttribWound(target, attribute);
        if (current == ATTRIB_ERROR)
        {
            return false;
        }
        if (current < requested)
        {
            addWound(target, attribute, requested - current);
        }
        else if (current > requested)
        {
            healWound(target, attribute, current - requested);
        }
        return getAttribWound(target, attribute) == requested;
    }

    private obj_id readObjId(obj_id player, String path)
        throws InterruptedException
    {
        return hasObjVar(player, path)
            ? getObjIdObjVar(player, path)
            : obj_id.NULL_ID;
    }

    private int readInt(obj_id player, String suffix)
        throws InterruptedException
    {
        return readIntPath(player, ROOT + suffix);
    }

    private int readIntPath(obj_id player, String path)
        throws InterruptedException
    {
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

    private int delta(obj_id player, String key, int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? current - getIntObjVar(player, key)
            : 0;
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
