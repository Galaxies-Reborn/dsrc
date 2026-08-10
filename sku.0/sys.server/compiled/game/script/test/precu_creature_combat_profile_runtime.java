package script.test;

import script.combat_engine.hit_result;
import script.combat_engine.weapon_data;
import script.dictionary;
import script.location;
import script.obj_id;
import script.library.combat;
import script.library.corpse;
import script.library.create;

/**
 * Identity-bound, reversible live proof for the Core3 creature profile route.
 * Authentic kreetle and rancor initialization is inspected without modifying
 * the player's skills, attributes, equipment, inventory, or progression.
 */
public class precu_creature_combat_profile_runtime extends script.base_script
{
    private static final long PLAYER_OID = 44003778L;
    private static final int PLAYER_STATION_ID = 91001;
    private static final int PROTOCOL_VERSION = 1;
    private static final String ROOT = "precu.creatureCombatProfileRuntime";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PROTOCOL = ROOT + ".protocol";
    private static final String KREETLE = ROOT + ".kreetle";
    private static final String RANCOR = ROOT + ".rancor";
    private static final String DIAGNOSTIC_ROOT =
        "precu.p14.marksmanTier1Fixture.liveDiagnostic";
    private static final String DIAGNOSTIC_ENABLED =
        DIAGNOSTIC_ROOT + ".enabled";
    private static final String HARVEST_COMMAND = "harvestCorpse";
    private static final String ATTACK_COMMAND = "creatureMeleeAttack";
    private static final int DIAGNOSTIC_ATTACK_COUNT = 8;
    private static final String USAGE =
        "usage: prepare|probe|harvestAdmission|armCombatDiagnostics|" +
        "diagnostics|status|cleanup " +
        "<playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0]
            : params.trim().split("[ ]+");
        if (args.length != 3 || !isValidLifecycle(args[2]))
            return USAGE;

        long playerOid;
        try
        {
            playerOid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (playerOid != PLAYER_OID)
            return "error=playerIdentityRejected";

        obj_id player = obj_id.getObjId(playerOid);
        if (!isAuthoritativePlayer(player))
            return "error=playerUnavailable";
        if (args[0].equalsIgnoreCase("prepare"))
            return prepare(player, args[2]);
        if (args[0].equalsIgnoreCase("probe"))
            return probe(player, args[2]);
        if (args[0].equalsIgnoreCase("harvestAdmission"))
            return harvestAdmission(player, args[2]);
        if (args[0].equalsIgnoreCase("armCombatDiagnostics"))
            return armCombatDiagnostics(player, args[2]);
        if (args[0].equalsIgnoreCase("diagnostics"))
            return diagnostics(player, args[2]);
        if (args[0].equalsIgnoreCase("status"))
            return status(player, args[2]);
        if (args[0].equalsIgnoreCase("cleanup"))
            return cleanup(player, args[2]);
        return USAGE;
    }

    private String prepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateOwnership(player, lifecycle);
            return ownership == null ? "action=prepare resumed=true " +
                buildStatus(player) : ownership;
        }

        location origin = getWorldLocation(player);
        if (origin == null)
            return "error=playerLocationUnavailable";
        origin = new location(origin);
        origin.cell = obj_id.NULL_ID;

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PROTOCOL, PROTOCOL_VERSION);

        location kreetleLocation = new location(origin);
        kreetleLocation.x += 20.0f;
        location rancorLocation = new location(origin);
        rancorLocation.x += 22.0f;

        obj_id kreetle = createFixtureCreature("kreetle", kreetleLocation);
        obj_id rancor = createFixtureCreature("rancor", rancorLocation);
        if (!isIdValid(kreetle) || !isIdValid(rancor))
        {
            if (isIdValid(kreetle))
                destroyObject(kreetle);
            if (isIdValid(rancor))
                destroyObject(rancor);
            removeObjVar(player, ROOT);
            return "error=creaturePreparationFailed restored=true";
        }

        setName(kreetle, "PRECU Core3 Profile Kreetle");
        setName(rancor, "PRECU Core3 Profile Rancor");
        setInvulnerable(kreetle, true);
        setInvulnerable(rancor, true);
        setObjVar(player, KREETLE, kreetle);
        setObjVar(player, RANCOR, rancor);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String probe(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
            return ownership;
        obj_id kreetle = readObject(player, KREETLE);
        obj_id rancor = readObject(player, RANCOR);
        if (!isFixtureCreatureAvailable(kreetle) ||
            !isFixtureCreatureAvailable(rancor))
            return "error=fixtureCreatureUnavailable";

        int kreetleKinetic = resolveDamage(kreetle, DAMAGE_KINETIC, 1000);
        int kreetleStun = resolveDamage(kreetle, DAMAGE_STUN, 1000);
        int rancorKinetic = resolveDamage(rancor, DAMAGE_KINETIC, 1000);
        int rancorBlast = resolveDamage(rancor, DAMAGE_BLAST, 1000);
        boolean passed =
            profileMatches(kreetle, "kreetle", 3, 35, 45, 45, 90, 110, 23) &&
            getIntObjVar(kreetle, "precu.armor.rating") == 0 &&
            getIntObjVar(kreetle, "precu.armor.kinetic") == 0 &&
            getIntObjVar(kreetle, "precu.armor.stun") == -1 &&
            profileMatches(rancor, "rancor", 50, 420, 550, 4916,
                10000, 12000, 50) &&
            getIntObjVar(rancor, "precu.armor.rating") == 1 &&
            getIntObjVar(rancor, "precu.armor.kinetic") == 130 &&
            getIntObjVar(rancor, "precu.armor.blast") == -1 &&
            kreetleKinetic == 1000 && kreetleStun == 1000 &&
            rancorKinetic == 350 && rancorBlast == 1000;

        return "action=probe result=" + (passed ? "passed" : "failed") +
            " rawDamage=1000 armorPiercing=0" +
            " kreetleKineticFinal=" + kreetleKinetic +
            " kreetleStunFinal=" + kreetleStun +
            " rancorKineticFinal=" + rancorKinetic +
            " rancorBlastFinal=" + rancorBlast + " " +
            buildStatus(player);
    }

    private String harvestAdmission(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
            return ownership;
        obj_id kreetle = readObject(player, KREETLE);
        if (!isFixtureCreatureAvailable(kreetle))
            return "error=fixtureCreatureUnavailable";

        boolean noviceScoutOwned = hasSkill(player, corpse.SKILL_NOVICE_SCOUT);
        boolean commandOwned = hasCommand(player, HARVEST_COMMAND);
        dictionary resources =
            corpse.getHarvestCorpseResources(player, kreetle, "meat");
        boolean familyResolved = resources != null &&
            resources.getInt("meat_insect") > 0;
        boolean libraryAdmitted = corpse.canPlayerHarvestCreature(player, false);
        boolean queued = false;
        if (noviceScoutOwned && commandOwned && familyResolved && libraryAdmitted)
        {
            queued = queueCommand(
                player,
                getStringCrc(HARVEST_COMMAND.toLowerCase()),
                kreetle,
                "meat",
                COMMAND_PRIORITY_DEFAULT);
        }
        boolean passed = noviceScoutOwned && commandOwned && familyResolved &&
            libraryAdmitted && queued;
        return "action=harvestAdmission result=" +
            (passed ? "passed" : "failed") +
            " noviceScoutOwned=" + noviceScoutOwned +
            " commandOwned=" + commandOwned +
            " family=kreetle resourceClass=meat_insect" +
            " familyResolved=" + familyResolved +
            " libraryAdmitted=" + libraryAdmitted +
            " queueCommandReturned=" + queued +
            " inventoryMutated=false skillStateMutated=false";
    }

    private int resolveDamage(obj_id defender, int damageType, int damage)
        throws InterruptedException
    {
        weapon_data weaponData = new weapon_data();
        weaponData.damageType = damageType;
        weaponData.elementalType = DAMAGE_NONE;
        weaponData.elementalValue = 0;
        hit_result hitData = new hit_result();
        hitData.success = true;
        hitData.damage = damage;
        combat.applyPrecuCreatureArmorProtection(
            defender, weaponData, hitData, 0);
        return hitData.damage + hitData.elementalDamage;
    }

    private String armCombatDiagnostics(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
            return ownership;
        obj_id kreetle = readObject(player, KREETLE);
        obj_id rancor = readObject(player, RANCOR);
        if (!isFixtureCreatureAvailable(kreetle) ||
            !isFixtureCreatureAvailable(rancor))
            return "error=fixtureCreatureUnavailable";

        if (hasObjVar(rancor, DIAGNOSTIC_ROOT))
            removeObjVar(rancor, DIAGNOSTIC_ROOT);
        setObjVar(rancor, DIAGNOSTIC_ENABLED, 1);
        setInvulnerable(kreetle, false);
        setInvulnerable(rancor, false);
        fortify(kreetle);
        fortify(rancor);
        pvpSetAttackableOverride(kreetle, true);
        pvpSetAttackableOverride(rancor, true);
        pvpSetPermanentPersonalEnemyFlag(rancor, kreetle);
        pvpSetPermanentPersonalEnemyFlag(kreetle, rancor);
        setCombatTarget(rancor, kreetle);
        setCombatTarget(kreetle, rancor);
        startCombat(rancor, kreetle);
        startCombat(kreetle, rancor);
        aiEquipPrimaryWeapon(rancor);
        aiEquipPrimaryWeapon(kreetle);
        addToMentalStateToward(rancor, kreetle, ANGER, 100, BEHAVIOR_ATTACK);

        int queued = 0;
        int commandCrc = getStringCrc(ATTACK_COMMAND.toLowerCase());
        for (int attack = 0; attack < DIAGNOSTIC_ATTACK_COUNT; ++attack)
        {
            if (queueCommand(
                rancor,
                commandCrc,
                kreetle,
                "",
                COMMAND_PRIORITY_DEFAULT))
                ++queued;
        }
        obj_id attackerWeapon = getCurrentWeapon(rancor);
        return "action=armCombatDiagnostics queued=" + queued +
            " requested=" + DIAGNOSTIC_ATTACK_COUNT +
            " attacker=" + rancor + " defender=" + kreetle +
            " command=" + ATTACK_COMMAND +
            " combatScript=" +
                hasScript(rancor, "systems.combat.combat_actions") +
            " pvpCanAttack=" + pvpCanAttack(rancor, kreetle) +
            " weapon=" + attackerWeapon +
            " weaponTemplate=" +
                (isIdValid(attackerWeapon) ?
                    getTemplateName(attackerWeapon) : "none") +
            " weaponType=" +
                (isIdValid(attackerWeapon) ?
                    getWeaponType(attackerWeapon) : -1) +
            " playerStateMutated=false";
    }

    private String diagnostics(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        if (ownership != null)
            return ownership;
        obj_id kreetle = readObject(player, KREETLE);
        obj_id rancor = readObject(player, RANCOR);
        if (!isFixtureCreatureAvailable(kreetle) ||
            !isFixtureCreatureAvailable(rancor))
            return "error=fixtureCreatureUnavailable";

        int ngeDelay = readDiagnosticInt(rancor,
            "preparation.ngeDelayApplied", -1);
        int ngeRange = readDiagnosticInt(rancor,
            "preparation.ngeRangeApplied", -1);
        int ngeElemental = readDiagnosticInt(rancor,
            "preparation.ngeElementalMultiplierApplied", -1);
        int ngeOverload = readDiagnosticInt(rancor,
            "preparation.ngeWeaponOverloadApplied", -1);
        int ngeExpertise = readDiagnosticInt(rancor,
            "damage.ngeExpertiseApplied", -1);
        int ngeKillMeter = readDiagnosticInt(rancor,
            "damage.ngeKillMeterApplied", -1);
        int ngeNiche = readDiagnosticInt(rancor,
            "damage.ngeNicheApplied", -1);
        String damagePipeline = readDiagnosticString(rancor,
            "damage.pipeline", "none");
        String primaryResult = readDiagnosticString(rancor,
            "primary.resultName", "none");
        String secondaryProfile = readDiagnosticString(rancor,
            "secondary.profile", "none");
        String secondaryResult = readDiagnosticString(rancor,
            "secondary.resultName", "none");
        int targetPool = readDiagnosticInt(rancor,
            "targetPool.resolved", -1);
        boolean passed = ngeDelay == 0 && ngeRange == 0 &&
            ngeElemental == 0 && ngeOverload == 0 &&
            ngeExpertise == 0 && ngeKillMeter == 0 && ngeNiche == 0 &&
            damagePipeline.equals("PRECU_CORE3") &&
            !primaryResult.equals("none") &&
            !primaryResult.equals("FALLBACK") &&
            !secondaryProfile.equals("none") &&
            !secondaryProfile.startsWith("FALLBACK") &&
            !secondaryResult.equals("none") &&
            !secondaryResult.equals("FALLBACK") &&
            targetPool >= 0 && targetPool <= 2;

        stopCombat(rancor);
        stopCombat(kreetle);
        clearHateList(rancor);
        clearHateList(kreetle);

        return "action=diagnostics result=" +
            (passed ? "passed" : "failed") +
            " attackerProfile=" +
                getStringObjVar(rancor, "precu.combatProfile") +
            " command=" + ATTACK_COMMAND +
            " traceStage=" +
                readDiagnosticString(rancor, "trace.stage", "none") +
            " costHealth=" +
                readDiagnosticInt(rancor, "cost.health", -3) +
            " costAction=" +
                readDiagnosticInt(rancor, "cost.action", -3) +
            " costMind=" +
                readDiagnosticInt(rancor, "cost.mind", -3) +
            " preparationDelay=" +
                readDiagnosticInt(rancor, "preparation.delay", -1) +
            " preparationMaxRange=" +
                readDiagnosticFloat(rancor, "preparation.maxRange", -1.0f) +
            " preparationElementalValue=" +
                readDiagnosticInt(rancor, "preparation.elementalValue", -1) +
            " ngeDelayApplied=" + ngeDelay +
            " ngeRangeApplied=" + ngeRange +
            " ngeElementalMultiplierApplied=" + ngeElemental +
            " ngeWeaponOverloadApplied=" + ngeOverload +
            " primaryResult=" + primaryResult +
            " primaryAccuracyBonus=" +
                readDiagnosticFloat(rancor, "primary.accuracyBonus", -1.0f) +
            " primaryHitChance=" +
                readDiagnosticFloat(rancor, "primary.hitChance", -1.0f) +
            " secondaryProfile=" + secondaryProfile +
            " secondaryResult=" + secondaryResult +
            " targetPoolResolved=" + targetPool +
            " damagePipeline=" + damagePipeline +
            " damageMinimum=" +
                readDiagnosticFloat(rancor, "damage.minimum", -1.0f) +
            " damageMaximum=" +
                readDiagnosticFloat(rancor, "damage.maximum", -1.0f) +
            " ngeExpertiseApplied=" + ngeExpertise +
            " ngeKillMeterApplied=" + ngeKillMeter +
            " ngeNicheApplied=" + ngeNiche +
            " playerStateMutated=false";
    }

    private void fortify(obj_id creature) throws InterruptedException
    {
        for (int attribute = HEALTH; attribute <= MIND; ++attribute)
        {
            setMaxAttrib(creature, attribute, 100000);
            setAttrib(creature, attribute, 100000);
        }
    }

    private int readDiagnosticInt(obj_id creature, String leaf, int fallback)
        throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(creature, path) ? getIntObjVar(creature, path)
            : fallback;
    }

    private float readDiagnosticFloat(
        obj_id creature, String leaf, float fallback)
        throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(creature, path) ? getFloatObjVar(creature, path)
            : fallback;
    }

    private String readDiagnosticString(
        obj_id creature, String leaf, String fallback)
        throws InterruptedException
    {
        String path = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(creature, path) ? getStringObjVar(creature, path)
            : fallback;
    }

    private boolean profileMatches(
        obj_id creature,
        String profile,
        int level,
        int damageMin,
        int damageMax,
        int xp,
        int hamMin,
        int hamMax,
        int accuracy) throws InterruptedException
    {
        obj_id weapon = getCurrentWeapon(creature);
        int health = getMaxAttrib(creature, HEALTH);
        int action = getMaxAttrib(creature, ACTION);
        int mind = getMaxAttrib(creature, MIND);
        return getStringObjVar(creature, "precu.combatProfile").equals(profile) &&
            getLevel(creature) == level &&
            getIntObjVar(creature, "intCombatDifficulty") == level &&
            isIdValid(weapon) &&
            getWeaponMinDamage(weapon) == damageMin &&
            getWeaponMaxDamage(weapon) == damageMax &&
            getIntObjVar(creature, "combat.intCombatXP") == xp &&
            health >= hamMin && health <= hamMax &&
            action >= hamMin && action <= hamMax &&
            mind >= hamMin && mind <= hamMax &&
            getSkillStatMod(creature, "toHitChance") == accuracy;
    }

    private obj_id createFixtureCreature(String creatureType, location loc)
        throws InterruptedException
    {
        dictionary data = dataTableGetRow(create.CREATURE_TABLE, creatureType);
        if (data == null)
            return obj_id.NULL_ID;
        String template = data.getString("template");
        if (template == null || template.length() == 0)
            return obj_id.NULL_ID;
        data.put("lootTable", "");
        obj_id creature = createObject(create.TEMPLATE_PREFIX + template, loc);
        if (!isIdValid(creature))
            return obj_id.NULL_ID;
        create.initializeCreature(creature, creatureType, data, -1);
        create.attachCreatureScripts(
            creature,
            data.getString("scripts"),
            true);
        return creature;
    }

    private String status(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(player, lifecycle);
        return ownership == null ? "action=status " + buildStatus(player)
            : ownership;
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
            return "action=cleanup alreadyClean=true restored=true";
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
            return ownership;
        destroyTracked(player, KREETLE);
        destroyTracked(player, RANCOR);
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true";
    }

    private String buildStatus(obj_id player) throws InterruptedException
    {
        obj_id kreetle = readObject(player, KREETLE);
        obj_id rancor = readObject(player, RANCOR);
        return "authoritative=true player=" + player +
            " stationId=" + getPlayerStationId(player) +
            " lifecycle=" + getStringObjVar(player, LIFECYCLE) +
            " kreetle=" + describeCreature(kreetle) +
            " rancor=" + describeCreature(rancor) +
            " playerStateMutated=false disposableCreatures=true";
    }

    private String describeCreature(obj_id creature) throws InterruptedException
    {
        if (!isFixtureCreatureAvailable(creature))
            return creature + ":loaded=false";
        obj_id weapon = getCurrentWeapon(creature);
        return creature + ":loaded=true" +
            ",profile=" + getStringObjVar(creature, "precu.combatProfile") +
            ",level=" + getLevel(creature) +
            ",difficulty=" + getIntObjVar(creature, "intCombatDifficulty") +
            ",damageMin=" +
                (isIdValid(weapon) ? getWeaponMinDamage(weapon) : -1) +
            ",damageMax=" +
                (isIdValid(weapon) ? getWeaponMaxDamage(weapon) : -1) +
            ",speed=" +
                (isIdValid(weapon) ? getWeaponAttackSpeed(weapon) : -1.0f) +
            ",xp=" + getIntObjVar(creature, "combat.intCombatXP") +
            ",accuracy=" + getSkillStatMod(creature, "toHitChance") +
            ",health=" + getMaxAttrib(creature, HEALTH) +
            ",action=" + getMaxAttrib(creature, ACTION) +
            ",mind=" + getMaxAttrib(creature, MIND) +
            ",armor=" + getIntObjVar(creature, "precu.armor.rating") +
            ",kinetic=" + getIntObjVar(creature, "precu.armor.kinetic") +
            ",blast=" + getIntObjVar(creature, "precu.armor.blast") +
            ",stun=" + getIntObjVar(creature, "precu.armor.stun");
    }

    private void destroyTracked(obj_id player, String name)
        throws InterruptedException
    {
        obj_id object = readObject(player, name);
        forceDestroy(object);
    }

    private void forceDestroy(obj_id object) throws InterruptedException
    {
        if (!isIdValid(object) || !object.isLoaded())
            return;
        setInvulnerable(object, true);
        stopCombat(object);
        clearHateList(object);
        setCombatTarget(object, obj_id.NULL_ID);
        destroyObject(object);
    }

    private obj_id readObject(obj_id player, String name)
        throws InterruptedException
    {
        return hasObjVar(player, name) ? getObjIdObjVar(player, name)
            : obj_id.NULL_ID;
    }

    private boolean isFixtureCreatureAvailable(obj_id creature)
        throws InterruptedException
    {
        return isIdValid(creature) && creature.isLoaded() &&
            hasObjVar(creature, "precu.combatProfile") &&
            hasObjVar(creature, "precu.armor.rating");
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String error = validateLifecycle(player, lifecycle);
        if (error != null)
            return error;
        if (getIntObjVar(player, PROTOCOL) != PROTOCOL_VERSION)
            return "error=protocolMismatch";
        return null;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE) ||
            !getStringObjVar(player, LIFECYCLE).equals(lifecycle))
            return "error=lifecycleRejected";
        return null;
    }

    private boolean isAuthoritativePlayer(obj_id player)
        throws InterruptedException
    {
        return player != null && player != obj_id.NULL_ID &&
            player.isLoaded() && player.isAuthoritative() &&
            isPlayer(player) &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private boolean isValidLifecycle(String value)
    {
        return value != null && value.matches("[a-f0-9]{32}");
    }
}
