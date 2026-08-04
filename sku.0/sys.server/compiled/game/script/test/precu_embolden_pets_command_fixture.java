package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.ai_lib;
import script.library.buff;
import script.library.callable;
import script.library.create;
import script.library.pet_lib;
import script.library.skill;
import script.library.utils;

/** Identity-bound reversible fixture for Publish 14.1 emboldenPets. */
public class precu_embolden_pets_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "emboldenpets";
    private static final String CREATURE_TYPE = "worrt";
    private static final String BUFF = "emboldenPet";
    private static final String COOLDOWN = "pet.precuEmboldenCooldownUntil";
    private static final String ROOT = "precu.emboldenPetsCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String PET = ROOT + ".pet";
    private static final String PCD = ROOT + ".pcd";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_MIND_MAX = ROOT + ".originalMindMax";
    private static final String ORIGINAL_FOCUS = ROOT + ".originalFocus";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_PET_MASTER =
        ROOT + ".originalPetMaster";
    private static final String BEFORE_MIND = ROOT + ".beforeMind";
    private static final String BEFORE_HEALTH_MAX = ROOT + ".beforeHealthMax";
    private static final String BEFORE_ACTION_MAX = ROOT + ".beforeActionMax";
    private static final String BEFORE_MIND_MAX = ROOT + ".beforeMindMax";
    private static final String FIXTURE_SCRIPT =
        "test.precu_embolden_pets_command_fixture";
    private static final String[] SKILLS =
    {
        "outdoors_creaturehandler",
        "outdoors_creaturehandler_novice",
        "outdoors_creaturehandler_healing_01",
        "outdoors_creaturehandler_healing_02"
    };
    private static final String USAGE =
        "usage: prepare|status|cleanup|recover <playerOid> <32-hex-lifecycle>";

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
        if (args[0].equalsIgnoreCase("recover"))
        {
            return recoverOrphan(player);
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

    public int handlePrecuEmboldenFixturePoll(obj_id self, dictionary params)
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
        messageTo(self, "handlePrecuEmboldenFixturePoll", null, 1.0f, false);
        return SCRIPT_CONTINUE;
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
            return ownership == null ? "error=fixtureNotPrepared" : ownership;
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
        setObjVar(player, ORIGINAL_FOCUS, getAttrib(player, FOCUS));
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
        setMaxAttrib(player, MIND, Math.max(500, getMaxAttrib(player, MIND)));
        setAttrib(player, MIND, 500);
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
        callable.setCallable(
            player, pet, callable.CALLABLE_TYPE_COMBAT_PET);
        setObjVar(player, BEFORE_MIND, getAttrib(player, MIND));
        setObjVar(player, BEFORE_HEALTH_MAX, getMaxAttrib(pet, HEALTH));
        setObjVar(player, BEFORE_ACTION_MAX, getMaxAttrib(pet, ACTION));
        setObjVar(player, BEFORE_MIND_MAX, getMaxAttrib(pet, MIND));
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

    private String recoverOrphan(obj_id player) throws InterruptedException
    {
        if (hasObjVar(player, ROOT) || hasAnyFixtureSkill(player) ||
            hasCommand(player, COMMAND) || hasScript(player, FIXTURE_SCRIPT))
        {
            return "error=recoveryRejected";
        }
        obj_id[] devices = callable.getDatapadCallablesByType(
            player, callable.CALLABLE_TYPE_COMBAT_PET);
        if (devices == null || devices.length != 1)
        {
            return "error=recoveryCardinality";
        }
        obj_id pcd = devices[0];
        if (!isIdValid(pcd) || !pcd.isLoaded() ||
            !hasObjVar(pcd, "pet.creatureName") ||
            !getStringObjVar(pcd, "pet.creatureName").equals(CREATURE_TYPE))
        {
            return "error=recoveryIdentityRejected";
        }
        obj_id pet = callable.getCDCallable(pcd);
        callable.setCallable(player, null, callable.CALLABLE_TYPE_COMBAT_PET);
        callable.setCDCallable(pcd, null);
        if (isIdValid(pet) && pet.isLoaded())
        {
            if (getMaster(pet) == player)
            {
                setMaster(pet, null);
            }
            destroyObject(pet);
        }
        destroyObject(pcd);
        if (hasScript(player, "ai.pet_master"))
        {
            detachScript(player, "ai.pet_master");
        }
        boolean restored =
            !callable.hasCallable(player, callable.CALLABLE_TYPE_COMBAT_PET) &&
            callable.getNumStoredCDByType(
                player, callable.CALLABLE_TYPE_COMBAT_PET) == 0 &&
            !hasScript(player, "ai.pet_master");
        return "action=recover count=1 restored=" + restored;
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
            buff.removeBuff(pet, BUFF);
            if (hasObjVar(pet, COOLDOWN))
            {
                removeObjVar(pet, COOLDOWN);
            }
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
            getAttrib(player, FOCUS) == getIntObjVar(player, ORIGINAL_FOCUS) &&
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
        int healthMax = petAvailable ? getMaxAttrib(pet, HEALTH) : 0;
        int actionMax = petAvailable ? getMaxAttrib(pet, ACTION) : 0;
        int mindMax = petAvailable ? getMaxAttrib(pet, MIND) : 0;
        int cooldownRemaining = 0;
        if (petAvailable && hasObjVar(pet, COOLDOWN))
        {
            cooldownRemaining = Math.max(
                0, getIntObjVar(pet, COOLDOWN) - getGameTime());
        }
        return
            "player=" + player +
            " pet=" + pet +
            " petAvailable=" + petAvailable +
            " pcd=" + pcd +
            " pcdAvailable=" + pcdAvailable +
            " pcdContained=" + pcdContained +
            " masterLinked=" +
                (petAvailable && getMaster(pet) == player) +
            " creaturePet=" +
                (petAvailable && pet_lib.isCreaturePet(pet)) +
            " petScript=" +
                (petAvailable && hasScript(pet, "ai.pet")) +
            " command=" + hasCommand(player, COMMAND) +
            " skillBits=" + buildSkillBits(player) +
            " buff=" + (petAvailable && buff.hasBuff(pet, BUFF)) +
            " buffRemaining=" +
                (petAvailable ? buff.getBuffTimeRemaining(pet, BUFF) : 0.0f) +
            " cooldownRemaining=" + cooldownRemaining +
            " mind=" + getAttrib(player, MIND) +
            " mindDelta=" + delta(player, BEFORE_MIND, getAttrib(player, MIND)) +
            " focus=" + getAttrib(player, FOCUS) +
            " healthMaxBefore=" + readIntPath(player, BEFORE_HEALTH_MAX) +
            " healthMax=" + healthMax +
            " healthMaxDelta=" + delta(player, BEFORE_HEALTH_MAX, healthMax) +
            " actionMaxBefore=" + readIntPath(player, BEFORE_ACTION_MAX) +
            " actionMax=" + actionMax +
            " actionMaxDelta=" + delta(player, BEFORE_ACTION_MAX, actionMax) +
            " mindMaxBefore=" + readIntPath(player, BEFORE_MIND_MAX) +
            " mindMax=" + mindMax +
            " mindMaxDelta=" + delta(player, BEFORE_MIND_MAX, mindMax) +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " mindCost=" + readInt(player, ".mindCost") +
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
            hasObjVar(player, ORIGINAL_FOCUS) &&
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
        messageTo(player, "handlePrecuEmboldenFixturePoll", null, 1.0f, false);
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered", "handlerCalls", "mindCost", "completedAt",
            "cooldownUntil", "outcome", "prepareRequested"
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
