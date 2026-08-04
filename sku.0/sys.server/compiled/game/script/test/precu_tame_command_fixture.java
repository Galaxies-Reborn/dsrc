package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.library.ai_lib;
import script.library.callable;
import script.library.create;
import script.library.pet_lib;
import script.library.skill;
import script.library.utils;
import script.library.xp;

/**
 * Identity-bound, persistent, and reversible fixture for the Publish 14.1
 * tame command and pet-control lifecycle.  The authenticated client remains
 * the sole owner of command admission and dispatch.
 */
public class precu_tame_command_fixture extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String COMMAND = "tame";
    private static final String XP_TYPE = xp.CREATUREHANDLER;
    private static final String CREATURE_TYPE = "worrt";
    private static final String ROOT = "precu.tameCommandFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String TARGET = ROOT + ".target";
    private static final String PCD = ROOT + ".pcd";
    private static final String PET = ROOT + ".pet";
    private static final String ORIGINAL_XP = ROOT + ".originalXp";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String ORIGINAL_PET_MASTER =
        ROOT + ".originalPetMaster";
    private static final String BEFORE_XP = ROOT + ".beforeXp";
    private static final String TAME_RUNTIME = "pet.precuTame";
    private static final String TAME_TARGET_LOCK = "pet.precuTamePlayer";
    private static final String TAME_SCRIPT = "player.skill.taming_task";
    private static final String FIXTURE_SCRIPT =
        "test.precu_tame_command_fixture";
    private static final String[] SKILLS =
    {
        "outdoors_creaturehandler",
        "outdoors_creaturehandler_novice"
    };
    private static final String USAGE =
        "usage: prepare|status|store|call|cleanup <playerOid> <32-hex-lifecycle>";

    public int OnAttach(obj_id self) throws InterruptedException
    {
        startFixturePoll(self);
        return SCRIPT_CONTINUE;
    }

    public int OnLogin(obj_id self) throws InterruptedException
    {
        startFixturePoll(self);
        return SCRIPT_CONTINUE;
    }

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null
            ? new String[0]
            : params.trim().split("[ ]+");
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
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return status(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("store"))
        {
            return store(player, args[2]);
        }
        if (args[0].equalsIgnoreCase("call"))
        {
            return call(player, args[2]);
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
            utils.hasScriptVar(player, TAME_RUNTIME) ||
            hasScript(player, FIXTURE_SCRIPT))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);
        setObjVar(player, ORIGINAL_XP, getExperiencePoints(player, XP_TYPE));
        setObjVar(player, ORIGINAL_POINTS, skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_PET_MASTER,
            hasScript(player, "ai.pet_master") ? 1 : 0);
        resetTelemetry(player);

        if (!grantSkills(player) || !hasCommand(player, COMMAND))
        {
            boolean restored = restore(player);
            return "error=skillPreparationFailed restored=" + restored;
        }
        if (!setExperience(player, 0))
        {
            boolean restored = restore(player);
            return "error=xpPreparationFailed restored=" + restored;
        }

        location targetLocation = new location(getLocation(player));
        targetLocation.x += 2.0f;
        obj_id target = createFixtureTarget(targetLocation);
        if (!isIdValid(target) || !target.isLoaded() ||
            !ai_lib.isMonster(target) || !pet_lib.isTameable(target) ||
            !hasScript(target, "ai.pet_advance"))
        {
            if (isIdValid(target) && target.isLoaded())
            {
                destroyObject(target);
            }
            boolean restored = restore(player);
            return "error=targetPreparationFailed restored=" + restored;
        }
        setObjVar(player, TARGET, target);
        setObjVar(player, BEFORE_XP, getExperiencePoints(player, XP_TYPE));
        attachScript(player, FIXTURE_SCRIPT);
        if (!hasScript(player, FIXTURE_SCRIPT))
        {
            boolean restored = restore(player);
            return "error=fixtureScriptAttachFailed restored=" + restored;
        }
        setObjVar(player, PREPARED, 1);
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

    private String store(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        obj_id pcd = readObjId(player, PCD);
        obj_id pet = getActivePet(player, pcd);
        if (!isIdValid(pcd) || !pcd.isLoaded())
        {
            return "error=pcdUnavailable";
        }
        if (!isIdValid(pet) || !pet.isLoaded())
        {
            return "error=petUnavailable";
        }
        setObjVar(player, ROOT + ".storeRequestedAt", getGameTime());
        setObjVar(player, ROOT + ".storeRequested", 1);
        return "action=store " + buildStatus(player);
    }

    private String call(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        obj_id pcd = readObjId(player, PCD);
        if (!isIdValid(pcd) || !pcd.isLoaded())
        {
            return "error=pcdUnavailable";
        }
        obj_id current = getActivePet(player, pcd);
        if (isIdValid(current) && current.isLoaded())
        {
            return "error=petAlreadyActive";
        }
        setObjVar(player, ROOT + ".callRequestedAt", getGameTime());
        setObjVar(player, ROOT + ".callRequested", 1);
        return "action=call " + buildStatus(player);
    }

    public int handlePrecuTameFixturePoll(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isFixtureOwner(self))
        {
            utils.removeScriptVar(self, ROOT + ".pollActive");
            return SCRIPT_CONTINUE;
        }
        if (readInt(self, ".storeRequested") == 1)
        {
            removeObjVar(self, ROOT + ".storeRequested");
            handlePrecuTameFixtureStore(self, null);
        }
        if (readInt(self, ".callRequested") == 1)
        {
            removeObjVar(self, ROOT + ".callRequested");
            handlePrecuTameFixtureCall(self, null);
        }
        messageTo(self, "handlePrecuTameFixturePoll", null, 1.0f, false);
        return SCRIPT_CONTINUE;
    }

    private void startFixturePoll(obj_id player) throws InterruptedException
    {
        if (utils.hasScriptVar(player, ROOT + ".pollActive"))
        {
            return;
        }
        utils.setScriptVar(player, ROOT + ".pollActive", 1);
        messageTo(player, "handlePrecuTameFixturePoll", null, 1.0f, false);
    }

    public int handlePrecuTameFixtureStore(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isFixtureOwner(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id pcd = readObjId(self, PCD);
        obj_id pet = getActivePet(self, pcd);
        if (!isIdValid(pcd) || !pcd.isLoaded() ||
            !isIdValid(pet) || !pet.isLoaded())
        {
            setObjVar(self, ROOT + ".storeError", 1);
            return SCRIPT_CONTINUE;
        }
        pet_lib.storePet(pet, self);
        callable.setCallable(self, null, callable.CALLABLE_TYPE_COMBAT_PET);
        setObjVar(self, ROOT + ".storeCompletedAt", getGameTime());
        return SCRIPT_CONTINUE;
    }

    public int handlePrecuTameFixtureCall(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (!isFixtureOwner(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id pcd = readObjId(self, PCD);
        obj_id current = getActivePet(self, pcd);
        if (!isIdValid(pcd) || !pcd.isLoaded() ||
            (isIdValid(current) && current.isLoaded()))
        {
            setObjVar(self, ROOT + ".callError", 1);
            return SCRIPT_CONTINUE;
        }
        pet_lib.createPetFromData(pcd);
        obj_id recalled = getActivePet(self, pcd);
        if (isIdValid(recalled) && recalled.isLoaded())
        {
            setObjVar(self, ROOT + ".recalledPet", recalled);
            setObjVar(self, ROOT + ".callCompletedAt", getGameTime());
        }
        else
        {
            setObjVar(self, ROOT + ".callError", 1);
        }
        return SCRIPT_CONTINUE;
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
        boolean detachTameScript =
            utils.hasScriptVar(player, TAME_RUNTIME + ".detachHandler") &&
            utils.getIntScriptVar(
                player, TAME_RUNTIME + ".detachHandler") == 1;
        utils.removeScriptVar(player, TAME_RUNTIME);
        utils.removeScriptVar(player, ROOT + ".pollActive");
        if (detachTameScript && hasScript(player, TAME_SCRIPT))
        {
            detachScript(player, TAME_SCRIPT);
        }
        obj_id pcd = readObjId(player, PCD);
        obj_id pet = getActivePet(player, pcd);
        if (!isIdValid(pet) || !pet.isLoaded())
        {
            pet = readObjId(player, PET);
        }
        callable.setCallable(player, null, callable.CALLABLE_TYPE_COMBAT_PET);
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            callable.setCDCallable(pcd, null);
        }
        boolean restored = true;
        if (isIdValid(pet) && pet.isLoaded())
        {
            utils.removeScriptVar(pet, TAME_TARGET_LOCK);
            if (getMaster(pet) == player)
            {
                setMaster(pet, null);
            }
            // A pet reconstructed from PCD data is intentionally transient;
            // destroyObject may report false for that object even though its
            // callable link is cleared and teardown is accepted.
            destroyObject(pet);
        }
        obj_id originalTarget = readObjId(player, TARGET);
        if (isIdValid(originalTarget) && originalTarget.isLoaded() &&
            originalTarget != pet)
        {
            utils.removeScriptVar(originalTarget, TAME_TARGET_LOCK);
            if (getMaster(originalTarget) == player)
            {
                setMaster(originalTarget, null);
            }
            restored = destroyObject(originalTarget) && restored;
        }
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            restored = destroyObject(pcd) && restored;
        }
        restored = setExperience(player, getIntObjVar(player, ORIGINAL_XP)) &&
            restored;
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
        restored =
            getExperiencePoints(player, XP_TYPE) ==
                getIntObjVar(player, ORIGINAL_XP) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            !hasAnyFixtureSkill(player) &&
            !hasCommand(player, COMMAND) &&
            !callable.hasCallable(player, callable.CALLABLE_TYPE_COMBAT_PET) &&
            callable.getNumStoredCDByType(
                player, callable.CALLABLE_TYPE_COMBAT_PET) == 0 &&
            getIntObjVar(player, ORIGINAL_PET_MASTER) ==
                (hasScript(player, "ai.pet_master") ? 1 : 0) &&
            restored;
        removeObjVar(player, ROOT);
        return restored;
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
            create.TEMPLATE_PREFIX + templateName,
            targetLocation);
        if (!isIdValid(target))
        {
            return obj_id.NULL_ID;
        }
        create.randomlyNameCreature(target, CREATURE_TYPE);
        create.initializeCreature(target, CREATURE_TYPE, creatureData, -1);
        create.attachCreatureScripts(
            target,
            creatureData.getString("scripts"),
            true);
        if (!hasScript(target, "ai.pet_advance"))
        {
            attachScript(target, "ai.pet_advance");
        }
        return target;
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id target = readObjId(player, TARGET);
        obj_id pcd = readObjId(player, PCD);
        obj_id pet = getActivePet(player, pcd);
        boolean targetAvailable = isIdValid(target) && target.isLoaded();
        boolean pcdAvailable = isIdValid(pcd) && pcd.isLoaded();
        boolean petAvailable = isIdValid(pet) && pet.isLoaded();
        boolean pcdContained = false;
        if (pcdAvailable)
        {
            obj_id datapad = getContainedBy(pcd);
            pcdContained = isIdValid(datapad) && datapad.isLoaded() &&
                getContainedBy(datapad) == player;
        }
        int currentXp = getExperiencePoints(player, XP_TYPE);
        return
            "player=" + player +
            " target=" + target +
            " targetAvailable=" + targetAvailable +
            " targetLevel=" + (targetAvailable ? getLevel(target) :
                readInt(player, ".sourceLevel")) +
            " pcd=" + pcd +
            " pcdAvailable=" + pcdAvailable +
            " pcdContained=" + pcdContained +
            " pcdStored=" + (pcdAvailable && !callable.hasCDCallable(pcd)) +
            " pcdCreatureName=" + (pcdAvailable &&
                hasObjVar(pcd, "pet.creatureName")
                    ? getStringObjVar(pcd, "pet.creatureName") : "none") +
            " pcdScript=" + (pcdAvailable &&
                hasScript(pcd, "ai.pet_control_device")) +
            " growthStage=" + (pcdAvailable &&
                hasObjVar(pcd, "ai.petAdvance.growthStage")
                    ? getIntObjVar(pcd, "ai.petAdvance.growthStage") : -1) +
            " activePet=" + pet +
            " petAvailable=" + petAvailable +
            " masterLinked=" + (petAvailable && getMaster(pet) == player) +
            " petScript=" + (petAvailable && hasScript(pet, "ai.pet")) +
            " babyScript=" + (petAvailable && hasScript(pet, "ai.pet_advance")) +
            " petPersisted=" + (petAvailable && isObjectPersisted(pet)) +
            " skillBits=" + buildSkillBits(player) +
            " command=" + hasCommand(player, COMMAND) +
            " tameLevel=" + getSkillStatMod(player, "tame_level") +
            " tameNonAggro=" + getSkillStatMod(player, "tame_non_aggro") +
            " storedCount=" + callable.getNumStoredCDByType(
                player, callable.CALLABLE_TYPE_COMBAT_PET) +
            " xp=" + currentXp +
            " xpDelta=" + delta(player, BEFORE_XP, currentXp) +
            " handlerEntered=" + readInt(player, ".handlerEntered") +
            " handlerCalls=" + readInt(player, ".handlerCalls") +
            " phaseCallbacks=" + readInt(player, ".phaseCallbacks") +
            " chance=" + readInt(player, ".chance") +
            " roll=" + readInt(player, ".roll") +
            " xpGranted=" + readInt(player, ".xpGranted") +
            " outcome=" + readString(player, ".outcome") +
            " storeError=" + readInt(player, ".storeError") +
            " callError=" + readInt(player, ".callError") +
            " availablePoints=" + skill.getAvailableSkillPoints(player);
    }

    private obj_id getActivePet(obj_id player, obj_id pcd)
        throws InterruptedException
    {
        if (isIdValid(pcd) && pcd.isLoaded())
        {
            obj_id pet = callable.getCDCallable(pcd);
            if (isIdValid(pet) && pet.isLoaded())
            {
                return pet;
            }
        }
        if (callable.hasCallable(player, callable.CALLABLE_TYPE_COMBAT_PET))
        {
            return callable.getCallable(
                player, callable.CALLABLE_TYPE_COMBAT_PET);
        }
        return obj_id.NULL_ID;
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

    private boolean setExperience(obj_id player, int target)
        throws InterruptedException
    {
        int current = getExperiencePoints(player, XP_TYPE);
        if (current != target)
        {
            grantExperiencePoints(player, XP_TYPE, target - current);
        }
        return getExperiencePoints(player, XP_TYPE) == target;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE) ||
            !hasObjVar(player, PROTOCOL) ||
            getIntObjVar(player, PROTOCOL) != PROTOCOL_VERSION)
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        return hasObjVar(player, PREPARED) && hasObjVar(player, TARGET)
            ? null
            : "error=fixtureNotPrepared";
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return hasObjVar(player, ORIGINAL_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_PET_MASTER);
    }

    private boolean isFixtureOwner(obj_id player) throws InterruptedException
    {
        return isIdValid(player) && player.isLoaded() && isPlayer(player) &&
            player.getValue() == PLAYER_OID &&
            getPlayerStationId(player) == PLAYER_STATION_ID &&
            hasObjVar(player, PROTOCOL) &&
            getIntObjVar(player, PROTOCOL) == PROTOCOL_VERSION &&
            hasObjVar(player, PREPARED) &&
            getIntObjVar(player, PREPARED) == 1;
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

    private int delta(obj_id player, String key, int current)
        throws InterruptedException
    {
        return hasObjVar(player, key)
            ? current - getIntObjVar(player, key)
            : 0;
    }

    private void resetTelemetry(obj_id player) throws InterruptedException
    {
        String[] leaves =
        {
            "handlerEntered", "handlerCalls", "phaseCallbacks", "startedAt",
            "completedAt", "outcome", "chance", "roll", "pcd", "pet",
            "sourceLevel", "xpGranted", "petPersisted", "storeRequestedAt",
            "storeRequested", "storeCompletedAt", "storeError",
            "callRequestedAt", "callRequested", "callCompletedAt",
            "callError", "recalledPet"
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
