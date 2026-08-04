package script.test;

import script.location;
import script.obj_id;
import script.combat_engine;
import script.combat_engine.combat_data;
import script.library.combat;

/**
 * Layered, ServerConsole-only live fixture for the Publish 14.1 Marksman
 * tier-I pistol and carbine commands. The established headShot1 fixture owns
 * location, PvP, combat state, regeneration, and HAM restoration. This layer
 * owns only the additional skills and CDEF weapons, and never queues a command
 * or fabricates combat results.
 */
public class precu_marksman_tier1_fixture extends script.base_script
{
    private static final long ATTACKER_OID = 44003778L;
    private static final int ATTACKER_STATION_ID = 91001;
    private static final long DEFENDER_OID = 39008597L;
    private static final int DEFENDER_STATION_ID = 1001;
    private static final String PISTOL_ONE = "combat_marksman_pistol_01";
    private static final String MARKSMAN_PISTOL_TWO = "combat_marksman_pistol_02";
    private static final String MARKSMAN_PISTOL_THREE = "combat_marksman_pistol_03";
    private static final String MARKSMAN_PISTOL_FOUR = "combat_marksman_pistol_04";
    private static final String PISTOL_NOVICE = "combat_pistol_novice";
    private static final String PISTOL_ACCURACY_ONE = "combat_pistol_accuracy_01";
    private static final String PISTOL_ACCURACY_TWO = "combat_pistol_accuracy_02";
    private static final String PISTOL_ACCURACY_THREE = "combat_pistol_accuracy_03";
    private static final String PISTOL_ACCURACY_FOUR = "combat_pistol_accuracy_04";
    private static final String CARBINE_ONE = "combat_marksman_carbine_01";
    private static final String MARKSMAN_CARBINE_TWO = "combat_marksman_carbine_02";
    private static final String MARKSMAN_CARBINE_THREE = "combat_marksman_carbine_03";
    private static final String MARKSMAN_CARBINE_FOUR = "combat_marksman_carbine_04";
    private static final String CARBINE_NOVICE = "combat_carbine_novice";
    private static final String PISTOL_ABILITY_ONE = "combat_pistol_ability_01";
    private static final String PISTOL_ABILITY_TWO = "combat_pistol_ability_02";
    private static final String PISTOL_ABILITY_THREE = "combat_pistol_ability_03";
    private static final String CARBINE_ABILITY_ONE = "combat_carbine_ability_01";
    private static final String CARBINE_ABILITY_TWO = "combat_carbine_ability_02";
    private static final String CARBINE_ABILITY_THREE = "combat_carbine_ability_03";
    private static final String CARBINE_SPEED_ONE = "combat_carbine_speed_01";
    private static final String CARBINE_SPEED_TWO = "combat_carbine_speed_02";
    private static final String CARBINE_SPEED_THREE = "combat_carbine_speed_03";
    private static final String BODY_COMMAND = "bodyShot1";
    private static final String LEG_COMMAND = "legShot1";
    private static final String BURST_COMMAND = "burstShot1";
    private static final String BURST_TWO_COMMAND = "burstShot2";
    private static final String DISARM_COMMAND = "disarmingShot1";
    private static final String DOUBLE_TAP_COMMAND = "doubleTap";
    private static final String STOPPING_SHOT_COMMAND = "stoppingShot";
    private static final String CRIPPLING_SHOT_COMMAND = "cripplingShot";
    private static final String POINT_BLANK_SINGLE_TWO_COMMAND = "pointBlankSingle2";
    private static final String POINT_BLANK_AREA_TWO_COMMAND = "pointBlankArea2";
    private static final String CARBINE_CDEF_CERTIFICATION = "cert_carbine_cdef";
    private static final String PISTOL_TEMPLATE =
        "object/weapon/ranged/pistol/pistol_cdef.iff";
    private static final String CARBINE_TEMPLATE =
        "object/weapon/ranged/carbine/carbine_cdef.iff";
    private static final String RIFLE_TEMPLATE =
        "object/weapon/ranged/rifle/rifle_cdef.iff";
    private static final String LIGHTSABER_TEMPLATE =
        "object/weapon/ranged/pistol/pistol_precu_ricochet_fixture.iff";
    private static final String FALLBACK_WEAPON_TEMPLATE =
        "object/weapon/ranged/pistol/pistol_dl44_metal.iff";
    private static final String COMBAT_WEAPON_SCRIPT =
        "systems.combat.combat_weapon";

    private static final String HEADSHOT_ROOT = "precu.p14.headShot1Fixture";
    private static final String HEADSHOT_LIFECYCLE = HEADSHOT_ROOT + ".lifecycle";
    private static final String HEADSHOT_PREPARED = HEADSHOT_ROOT + ".prepared";
    private static final String ROOT = "precu.p14.marksmanTier1Fixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PEER = ROOT + ".peer";
    private static final String ORIGINAL_PISTOL_ONE = ROOT + ".originalPistolOne";
    private static final String ORIGINAL_MARKSMAN_PISTOL_TWO =
        ROOT + ".originalMarksmanPistolTwo";
    private static final String ORIGINAL_MARKSMAN_PISTOL_THREE =
        ROOT + ".originalMarksmanPistolThree";
    private static final String ORIGINAL_MARKSMAN_PISTOL_FOUR =
        ROOT + ".originalMarksmanPistolFour";
    private static final String ORIGINAL_PISTOL_NOVICE =
        ROOT + ".originalPistolNovice";
    private static final String ORIGINAL_PISTOL_ACCURACY_ONE =
        ROOT + ".originalPistolAccuracyOne";
    private static final String ORIGINAL_PISTOL_ACCURACY_TWO =
        ROOT + ".originalPistolAccuracyTwo";
    private static final String ORIGINAL_PISTOL_ACCURACY_THREE =
        ROOT + ".originalPistolAccuracyThree";
    private static final String ORIGINAL_PISTOL_ACCURACY_FOUR =
        ROOT + ".originalPistolAccuracyFour";
    private static final String ORIGINAL_CARBINE_ONE = ROOT + ".originalCarbineOne";
    private static final String ORIGINAL_MARKSMAN_CARBINE_TWO =
        ROOT + ".originalMarksmanCarbineTwo";
    private static final String ORIGINAL_MARKSMAN_CARBINE_THREE =
        ROOT + ".originalMarksmanCarbineThree";
    private static final String ORIGINAL_MARKSMAN_CARBINE_FOUR =
        ROOT + ".originalMarksmanCarbineFour";
    private static final String ORIGINAL_CARBINE_NOVICE =
        ROOT + ".originalCarbineNovice";
    private static final String ORIGINAL_PISTOL_ABILITY_ONE =
        ROOT + ".originalPistolAbilityOne";
    private static final String ORIGINAL_PISTOL_ABILITY_TWO =
        ROOT + ".originalPistolAbilityTwo";
    private static final String ORIGINAL_PISTOL_ABILITY_THREE =
        ROOT + ".originalPistolAbilityThree";
    private static final String ORIGINAL_CARBINE_ABILITY_ONE =
        ROOT + ".originalCarbineAbilityOne";
    private static final String ORIGINAL_CARBINE_ABILITY_TWO =
        ROOT + ".originalCarbineAbilityTwo";
    private static final String ORIGINAL_CARBINE_ABILITY_THREE =
        ROOT + ".originalCarbineAbilityThree";
    private static final String ORIGINAL_CARBINE_SPEED_ONE =
        ROOT + ".originalCarbineSpeedOne";
    private static final String ORIGINAL_CARBINE_SPEED_TWO =
        ROOT + ".originalCarbineSpeedTwo";
    private static final String ORIGINAL_CARBINE_SPEED_THREE =
        ROOT + ".originalCarbineSpeedThree";
    private static final String ORIGINAL_CARBINE_CDEF_CERTIFICATION =
        ROOT + ".originalCarbineCdefCertification";
    private static final String ORIGINAL_CENTER_OF_BEING =
        ROOT + ".originalCenterOfBeing";
    private static final String ORIGINAL_SABER_BLOCK = ROOT + ".originalSaberBlock";
    private static final String ORIGINAL_DEFENDER_SKILL_TEMPLATE =
        ROOT + ".originalDefenderSkillTemplate";
    private static final String ORIGINAL_DEFENDER_WOUNDS =
        ROOT + ".originalDefenderWounds";
    private static final String ORIGINAL_DEFENDER_SHOCK =
        ROOT + ".originalDefenderShock";
    private static final String PISTOL_WEAPON = ROOT + ".pistolWeapon";
    private static final String CARBINE_WEAPON = ROOT + ".carbineWeapon";
    private static final String DEFENDER_RIFLE_WEAPON = ROOT + ".defenderRifleWeapon";
    private static final String DEFENDER_PISTOL_WEAPON = ROOT + ".defenderPistolWeapon";
    private static final String DEFENDER_CARBINE_WEAPON = ROOT + ".defenderCarbineWeapon";
    private static final String DEFENDER_LIGHTSABER_WEAPON =
        ROOT + ".defenderLightsaberWeapon";
    private static final String DEFENDER_FALLBACK_WEAPON =
        ROOT + ".defenderFallbackWeapon";
    private static final String SECONDARY_CONTROL_MOD =
        ROOT + ".secondaryControl.mod";
    private static final String SECONDARY_CONTROL_DELTA =
        ROOT + ".secondaryControl.delta";
    private static final String DIAGNOSTIC_ROOT = ROOT + ".liveDiagnostic";
    private static final String DIAGNOSTIC_ENABLED = DIAGNOSTIC_ROOT + ".enabled";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String USAGE =
        "usage: inspect|recover|prepare|status|armSuccess|armNoPartialBody|" +
        "armNoPartialLeg|armPrimaryIdeal|armPrimaryNearMax|armPrimaryFallback|" +
        "armWounds|" +
        "armSecondaryBlock|armSecondaryDodge|armSecondaryCounter|" +
        "armSecondaryRicochet|armSecondaryFallback|" +
        "equipSecondaryRicochet|equipSecondaryFallback|" +
        "cleanup 44003778 39008597 <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null ? new String[0] : params.trim().split("[ ]+");
        if (args.length != 4 || !args[3].matches("[a-f0-9]{32}"))
        {
            return USAGE;
        }

        long attackerValue;
        long defenderValue;
        try
        {
            attackerValue = Long.parseLong(args[1]);
            defenderValue = Long.parseLong(args[2]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidOid";
        }
        if (attackerValue != ATTACKER_OID || defenderValue != DEFENDER_OID ||
            attackerValue == defenderValue)
        {
            return "error=identityNotAllowed";
        }

        obj_id attacker = obj_id.getObjId(attackerValue);
        obj_id defender = obj_id.getObjId(defenderValue);
        String validation = validatePlayer(attacker, ATTACKER_STATION_ID, "attacker");
        if (validation != null)
        {
            return validation;
        }
        validation = validatePlayer(defender, DEFENDER_STATION_ID, "defender");
        if (validation != null)
        {
            return validation;
        }

        if (args[0].equalsIgnoreCase("inspect"))
        {
            return "action=inspect " + buildStatus(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("recover"))
        {
            return recover(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            validation = validateOwnership(attacker, defender, args[3], false);
            return validation == null ? buildStatus(attacker, defender, args[3]) : validation;
        }
        if (args[0].equalsIgnoreCase("armSuccess"))
        {
            return arm(attacker, defender, args[3], "success");
        }
        if (args[0].equalsIgnoreCase("armNoPartialBody"))
        {
            return arm(attacker, defender, args[3], "body");
        }
        if (args[0].equalsIgnoreCase("armNoPartialLeg"))
        {
            return arm(attacker, defender, args[3], "leg");
        }
        if (args[0].equalsIgnoreCase("armPrimaryIdeal"))
        {
            return armPrimaryRange(attacker, defender, args[3], "ideal", 16.0f);
        }
        if (args[0].equalsIgnoreCase("armPrimaryNearMax"))
        {
            return armPrimaryRange(attacker, defender, args[3], "nearMax", 64.0f);
        }
        if (args[0].equalsIgnoreCase("armPrimaryFallback"))
        {
            return armPrimaryRange(attacker, defender, args[3], "fallback", 6.0f);
        }
        if (args[0].equalsIgnoreCase("armWounds"))
        {
            return armWounds(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armSecondaryBlock"))
        {
            return armSecondaryDefense(
                attacker, defender, args[3], "block",
                "private_center_of_being", 1000);
        }
        if (args[0].equalsIgnoreCase("armSecondaryDodge"))
        {
            return armSecondaryDefense(
                attacker, defender, args[3], "dodge",
                "private_center_of_being", 1000);
        }
        if (args[0].equalsIgnoreCase("armSecondaryCounter"))
        {
            return armSecondaryDefense(
                attacker, defender, args[3], "counter",
                "private_center_of_being", 1000);
        }
        if (args[0].equalsIgnoreCase("armSecondaryRicochet"))
        {
            return armSecondaryDefense(
                attacker, defender, args[3], "ricochet", "saber_block", 101);
        }
        if (args[0].equalsIgnoreCase("armSecondaryFallback"))
        {
            return armSecondaryDefense(
                attacker, defender, args[3], "fallback", "", 0);
        }
        if (args[0].equalsIgnoreCase("equipSecondaryRicochet"))
        {
            return equipSecondaryDefenseWeapon(
                attacker, defender, args[3], "ricochet");
        }
        if (args[0].equalsIgnoreCase("equipSecondaryFallback"))
        {
            return equipSecondaryDefenseWeapon(
                attacker, defender, args[3], "fallback");
        }
        if (args[0].equalsIgnoreCase("cleanup"))
        {
            return cleanup(attacker, defender, args[3]);
        }
        return USAGE;
    }

    private String prepare(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        String dependency = validateHeadShotLayer(attacker, defender, lifecycle);
        if (dependency != null)
        {
            return dependency;
        }
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if (ownership == null)
        {
            if (getIntObjVar(attacker, PREPARED) != 1 ||
                getIntObjVar(defender, PREPARED) != 1)
            {
                return "error=fixturePartial";
            }
            resetLiveDiagnostic(attacker);
            return fixtureReady(attacker, defender) ?
                "action=prepare resumed=true " + buildStatus(attacker, defender, lifecycle) :
                "error=fixtureReassertionFailed " + buildStatus(attacker, defender, lifecycle);
        }
        if (!"fixtureAbsent".equals(ownership))
        {
            return ownership;
        }

        setObjVar(attacker, LIFECYCLE, lifecycle);
        setObjVar(attacker, PEER, defender);
        setObjVar(defender, LIFECYCLE, lifecycle);
        setObjVar(defender, PEER, attacker);
        setObjVar(attacker, ORIGINAL_PISTOL_ONE, hasSkill(attacker, PISTOL_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_PISTOL_TWO,
            hasSkill(attacker, MARKSMAN_PISTOL_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_PISTOL_THREE,
            hasSkill(attacker, MARKSMAN_PISTOL_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_PISTOL_FOUR,
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_NOVICE,
            hasSkill(attacker, PISTOL_NOVICE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ACCURACY_ONE,
            hasSkill(attacker, PISTOL_ACCURACY_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ACCURACY_TWO,
            hasSkill(attacker, PISTOL_ACCURACY_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ACCURACY_THREE,
            hasSkill(attacker, PISTOL_ACCURACY_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ACCURACY_FOUR,
            hasSkill(attacker, PISTOL_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(attacker, ORIGINAL_CARBINE_ONE, hasSkill(attacker, CARBINE_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_CARBINE_TWO,
            hasSkill(attacker, MARKSMAN_CARBINE_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_CARBINE_THREE,
            hasSkill(attacker, MARKSMAN_CARBINE_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_MARKSMAN_CARBINE_FOUR,
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_NOVICE,
            hasSkill(attacker, CARBINE_NOVICE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ABILITY_ONE,
            hasSkill(attacker, PISTOL_ABILITY_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ABILITY_TWO,
            hasSkill(attacker, PISTOL_ABILITY_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_PISTOL_ABILITY_THREE,
            hasSkill(attacker, PISTOL_ABILITY_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_ABILITY_ONE,
            hasSkill(attacker, CARBINE_ABILITY_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_ABILITY_TWO,
            hasSkill(attacker, CARBINE_ABILITY_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_ABILITY_THREE,
            hasSkill(attacker, CARBINE_ABILITY_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_SPEED_ONE,
            hasSkill(attacker, CARBINE_SPEED_ONE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_SPEED_TWO,
            hasSkill(attacker, CARBINE_SPEED_TWO) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_SPEED_THREE,
            hasSkill(attacker, CARBINE_SPEED_THREE) ? 1 : 0);
        setObjVar(
            attacker,
            ORIGINAL_CARBINE_CDEF_CERTIFICATION,
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) ? 1 : 0);
        setObjVar(
            defender,
            ORIGINAL_CENTER_OF_BEING,
            getEnhancedSkillStatisticModifierUncapped(
                defender, "private_center_of_being"));
        setObjVar(
            defender,
            ORIGINAL_SABER_BLOCK,
            getEnhancedSkillStatisticModifierUncapped(defender, "saber_block"));
        String defenderSkillTemplate = getSkillTemplate(defender);
        int[] defenderWounds = new int[NUM_ATTRIBUTES];
        for (int attribute = 0; attribute < NUM_ATTRIBUTES; ++attribute)
        {
            defenderWounds[attribute] = getAttribWound(defender, attribute);
            if (defenderWounds[attribute] == ATTRIB_ERROR)
            {
                return "error=woundSnapshotFailed attribute=" + attribute;
            }
        }
        int defenderShock = getShockWound(defender);
        if (defenderShock == ATTRIB_ERROR)
        {
            return "error=shockSnapshotFailed";
        }
        setObjVar(
            defender,
            ORIGINAL_DEFENDER_SKILL_TEMPLATE,
            defenderSkillTemplate == null ? "" : defenderSkillTemplate);
        setObjVar(defender, ORIGINAL_DEFENDER_WOUNDS, defenderWounds);
        setObjVar(defender, ORIGINAL_DEFENDER_SHOCK, defenderShock);
        setObjVar(attacker, PREPARED, 0);
        setObjVar(defender, PREPARED, 0);

        boolean pistolGranted = hasSkill(attacker, PISTOL_ONE) ||
            grantSkill(attacker, PISTOL_ONE);
        boolean marksmanPistolTwoGranted =
            hasSkill(attacker, MARKSMAN_PISTOL_TWO) ||
            grantSkill(attacker, MARKSMAN_PISTOL_TWO);
        boolean marksmanPistolThreeGranted =
            hasSkill(attacker, MARKSMAN_PISTOL_THREE) ||
            grantSkill(attacker, MARKSMAN_PISTOL_THREE);
        boolean marksmanPistolFourGranted =
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR) ||
            grantSkill(attacker, MARKSMAN_PISTOL_FOUR);
        boolean pistolNoviceGranted = hasSkill(attacker, PISTOL_NOVICE) ||
            grantSkill(attacker, PISTOL_NOVICE);
        boolean pistolAccuracyOneGranted = hasSkill(attacker, PISTOL_ACCURACY_ONE) ||
            grantSkill(attacker, PISTOL_ACCURACY_ONE);
        boolean pistolAccuracyTwoGranted = hasSkill(attacker, PISTOL_ACCURACY_TWO) ||
            grantSkill(attacker, PISTOL_ACCURACY_TWO);
        boolean pistolAccuracyThreeGranted = hasSkill(attacker, PISTOL_ACCURACY_THREE) ||
            grantSkill(attacker, PISTOL_ACCURACY_THREE);
        boolean pistolAccuracyFourGranted = hasSkill(attacker, PISTOL_ACCURACY_FOUR) ||
            grantSkill(attacker, PISTOL_ACCURACY_FOUR);
        boolean carbineGranted = hasSkill(attacker, CARBINE_ONE) ||
            grantSkill(attacker, CARBINE_ONE);
        boolean marksmanCarbineTwoGranted =
            hasSkill(attacker, MARKSMAN_CARBINE_TWO) ||
            grantSkill(attacker, MARKSMAN_CARBINE_TWO);
        boolean marksmanCarbineThreeGranted =
            hasSkill(attacker, MARKSMAN_CARBINE_THREE) ||
            grantSkill(attacker, MARKSMAN_CARBINE_THREE);
        boolean marksmanCarbineFourGranted =
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR) ||
            grantSkill(attacker, MARKSMAN_CARBINE_FOUR);
        boolean carbineNoviceGranted = hasSkill(attacker, CARBINE_NOVICE) ||
            grantSkill(attacker, CARBINE_NOVICE);
        boolean pistolAbilityGranted = hasSkill(attacker, PISTOL_ABILITY_ONE) ||
            grantSkill(attacker, PISTOL_ABILITY_ONE);
        boolean pistolAbilityTwoGranted = hasSkill(attacker, PISTOL_ABILITY_TWO) ||
            grantSkill(attacker, PISTOL_ABILITY_TWO);
        boolean pistolAbilityThreeGranted = hasSkill(attacker, PISTOL_ABILITY_THREE) ||
            grantSkill(attacker, PISTOL_ABILITY_THREE);
        boolean carbineAbilityGranted = hasSkill(attacker, CARBINE_ABILITY_ONE) ||
            grantSkill(attacker, CARBINE_ABILITY_ONE);
        boolean carbineAbilityTwoGranted = hasSkill(attacker, CARBINE_ABILITY_TWO) ||
            grantSkill(attacker, CARBINE_ABILITY_TWO);
        boolean carbineAbilityThreeGranted = hasSkill(attacker, CARBINE_ABILITY_THREE) ||
            grantSkill(attacker, CARBINE_ABILITY_THREE);
        boolean carbineSpeedOneGranted = hasSkill(attacker, CARBINE_SPEED_ONE) ||
            grantSkill(attacker, CARBINE_SPEED_ONE);
        boolean carbineSpeedTwoGranted = hasSkill(attacker, CARBINE_SPEED_TWO) ||
            grantSkill(attacker, CARBINE_SPEED_TWO);
        boolean carbineSpeedThreeGranted = hasSkill(attacker, CARBINE_SPEED_THREE) ||
            grantSkill(attacker, CARBINE_SPEED_THREE);
        boolean carbineCertificationGranted =
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) ||
            grantCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        if (!pistolGranted ||
            !marksmanPistolTwoGranted || !marksmanPistolThreeGranted ||
            !marksmanPistolFourGranted || !pistolNoviceGranted ||
            !pistolAccuracyOneGranted || !pistolAccuracyTwoGranted ||
            !pistolAccuracyThreeGranted || !pistolAccuracyFourGranted ||
            !carbineGranted ||
            !marksmanCarbineTwoGranted || !marksmanCarbineThreeGranted ||
            !marksmanCarbineFourGranted || !carbineNoviceGranted ||
            !pistolAbilityGranted ||
            !pistolAbilityTwoGranted || !pistolAbilityThreeGranted ||
            !carbineAbilityGranted || !carbineAbilityTwoGranted ||
            !carbineAbilityThreeGranted || !carbineSpeedOneGranted ||
            !carbineSpeedTwoGranted || !carbineSpeedThreeGranted ||
            !carbineCertificationGranted ||
            !hasCommand(attacker, BODY_COMMAND) || !hasCommand(attacker, LEG_COMMAND) ||
            !hasCommand(attacker, BURST_COMMAND) ||
            !hasCommand(attacker, BURST_TWO_COMMAND) ||
            !hasCommand(attacker, DISARM_COMMAND) ||
            !hasCommand(attacker, DOUBLE_TAP_COMMAND) ||
            !hasCommand(attacker, STOPPING_SHOT_COMMAND) ||
            !hasCommand(attacker, CRIPPLING_SHOT_COMMAND) ||
            !hasCommand(attacker, POINT_BLANK_SINGLE_TWO_COMMAND) ||
            !hasCommand(attacker, POINT_BLANK_AREA_TWO_COMMAND))
        {
            String recovery = recover(attacker, defender, lifecycle).replace(' ', '_');
            return "error=skillPreparationFailed pistol=" + pistolGranted +
                " marksmanPistolTwo=" + marksmanPistolTwoGranted +
                " marksmanPistolThree=" + marksmanPistolThreeGranted +
                " marksmanPistolFour=" + marksmanPistolFourGranted +
                " pistolNovice=" + pistolNoviceGranted +
                " pistolAccuracyOne=" + pistolAccuracyOneGranted +
                " pistolAccuracyTwo=" + pistolAccuracyTwoGranted +
                " pistolAccuracyThree=" + pistolAccuracyThreeGranted +
                " pistolAccuracyFour=" + pistolAccuracyFourGranted +
                " carbine=" + carbineGranted +
                " marksmanCarbineTwo=" + marksmanCarbineTwoGranted +
                " marksmanCarbineThree=" + marksmanCarbineThreeGranted +
                " marksmanCarbineFour=" + marksmanCarbineFourGranted +
                " carbineNovice=" + carbineNoviceGranted +
                " pistolAbility=" + pistolAbilityGranted +
                " pistolAbilityTwo=" + pistolAbilityTwoGranted +
                " pistolAbilityThree=" + pistolAbilityThreeGranted +
                " carbineAbility=" + carbineAbilityGranted +
                " carbineSpeedOne=" + carbineSpeedOneGranted +
                " carbineSpeedTwo=" + carbineSpeedTwoGranted +
                " carbineSpeedThree=" + carbineSpeedThreeGranted +
                " carbineCertification=" + carbineCertificationGranted +
                " recovery=" + recovery;
        }

        obj_id pistol = createOwnedWeapon(attacker, PISTOL_WEAPON, PISTOL_TEMPLATE);
        if (!isIdValid(pistol))
        {
            String recovery = recover(attacker, defender, lifecycle).replace(' ', '_');
            return "error=pistolCreationFailed recovery=" + recovery;
        }

        obj_id carbine = createOwnedWeapon(attacker, CARBINE_WEAPON, CARBINE_TEMPLATE);
        if (!isIdValid(carbine))
        {
            String recovery = recover(attacker, defender, lifecycle).replace(' ', '_');
            return "error=carbineCreationFailed recovery=" + recovery;
        }
        obj_id defenderRifle =
            createOwnedWeapon(defender, DEFENDER_RIFLE_WEAPON, RIFLE_TEMPLATE);
        obj_id defenderPistol =
            createOwnedWeapon(defender, DEFENDER_PISTOL_WEAPON, PISTOL_TEMPLATE);
        obj_id defenderCarbine =
            createOwnedWeapon(defender, DEFENDER_CARBINE_WEAPON, CARBINE_TEMPLATE);
        obj_id defenderLightsaber =
            createOwnedWeapon(
                defender, DEFENDER_LIGHTSABER_WEAPON, LIGHTSABER_TEMPLATE);
        if (isIdValid(defenderLightsaber))
        {
            setObjVar(
                defenderLightsaber,
                script.library.weapons.OBJVAR_WP_LEVEL,
                1);
        }
        obj_id defenderFallback =
            createOwnedWeapon(
                defender, DEFENDER_FALLBACK_WEAPON, FALLBACK_WEAPON_TEMPLATE);
        if (isIdValid(defenderFallback))
        {
            setObjVar(
                defenderFallback,
                script.library.weapons.OBJVAR_WP_LEVEL,
                1);
        }
        if (!isIdValid(defenderRifle) || !isIdValid(defenderPistol) ||
            !isIdValid(defenderCarbine) || !isIdValid(defenderLightsaber) ||
            !isIdValid(defenderFallback))
        {
            String recovery = recover(attacker, defender, lifecycle).replace(' ', '_');
            return "error=defenderWeaponCreationFailed recovery=" + recovery;
        }

        setObjVar(attacker, PREPARED, 1);
        setObjVar(defender, PREPARED, 1);
        resetLiveDiagnostic(attacker);
        if (!fixtureReady(attacker, defender))
        {
            String status = buildStatus(attacker, defender, lifecycle).replace(' ', '_');
            String recovery = recover(attacker, defender, lifecycle).replace(' ', '_');
            return "error=fixturePreparationFailed status=" + status +
                " recovery=" + recovery;
        }
        return "action=prepare resumed=false " + buildStatus(attacker, defender, lifecycle);
    }

    private String arm(
        obj_id attacker, obj_id defender, String lifecycle, String mode)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        if (ownership != null)
        {
            return ownership;
        }
        String dependency = validateHeadShotLayer(attacker, defender, lifecycle);
        if (dependency != null)
        {
            return dependency;
        }
        if (!fixtureReady(attacker, defender))
        {
            return "error=fixtureNotPrepared";
        }

        resetLiveDiagnostic(attacker);
        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        combat.clearCombatDebuffs(attacker);
        combat.clearCombatDebuffs(defender);

        int health = getMaxAttrib(attacker, HEALTH);
        int action = getMaxAttrib(attacker, ACTION);
        int mind = getMaxAttrib(attacker, MIND);
        if (mode.equals("body"))
        {
            health = 3;
            action = 11;
            mind = 6;
        }
        else if (mode.equals("leg"))
        {
            health = 10;
            action = 18;
            mind = 6;
        }

        boolean armed =
            setAttribAndVerify(attacker, HEALTH, health) &
            setAttribAndVerify(attacker, ACTION, action) &
            setAttribAndVerify(attacker, MIND, mind) &
            setAttribAndVerify(defender, HEALTH, getMaxAttrib(defender, HEALTH)) &
            setAttribAndVerify(defender, ACTION, getMaxAttrib(defender, ACTION)) &
            setAttribAndVerify(defender, MIND, getMaxAttrib(defender, MIND));
        if (!armed)
        {
            return "error=hamPreparationFailed";
        }
        return "action=arm mode=" + mode +
            " expectedHealth=" + health +
            " expectedAction=" + action +
            " expectedMind=" + mind + " " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String armPrimaryRange(
        obj_id attacker, obj_id defender, String lifecycle, String mode,
        float centerSeparationMeters) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        if (ownership != null)
        {
            return ownership;
        }
        String dependency = validateHeadShotLayer(attacker, defender, lifecycle);
        if (dependency != null)
        {
            return dependency;
        }
        if (!fixtureReady(attacker, defender))
        {
            return "error=fixtureNotPrepared";
        }
        resetLiveDiagnostic(attacker);
        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        combat.clearCombatDebuffs(attacker);
        combat.clearCombatDebuffs(defender);

        float attackerX = 1000.0f;
        float attackerZ = 1000.0f;
        float defenderX = attackerX;
        float defenderZ = attackerZ + centerSeparationMeters;
        location attackerDestination =
            new location(
                attackerX,
                getHeightAtLocation(attackerX, attackerZ),
                attackerZ,
                "tatooine",
                null);
        location defenderDestination =
            new location(
                defenderX,
                getHeightAtLocation(defenderX, defenderZ),
                defenderZ,
                "tatooine",
                null);
        boolean moved =
            setLocation(attacker, attackerDestination) &
            setLocation(defender, defenderDestination);
        boolean stateReady =
            setLocomotion(attacker, LOCOMOTION_STANDING) &
            setLocomotion(defender, LOCOMOTION_STANDING) &
            setPostureClientImmediate(attacker, POSTURE_UPRIGHT) &
            setPostureClientImmediate(defender, POSTURE_UPRIGHT);
        pvpSetPermanentPersonalEnemyFlag(attacker, defender);
        pvpSetPermanentPersonalEnemyFlag(defender, attacker);
        boolean pvpReady =
            pvpCanAttack(attacker, defender) &
            pvpCanAttack(defender, attacker);
        boolean hamReady =
            setAttribAndVerify(attacker, HEALTH, getMaxAttrib(attacker, HEALTH)) &
            setAttribAndVerify(attacker, ACTION, getMaxAttrib(attacker, ACTION)) &
            setAttribAndVerify(attacker, MIND, getMaxAttrib(attacker, MIND)) &
            setAttribAndVerify(defender, HEALTH, getMaxAttrib(defender, HEALTH)) &
            setAttribAndVerify(defender, ACTION, getMaxAttrib(defender, ACTION)) &
            setAttribAndVerify(defender, MIND, getMaxAttrib(defender, MIND));
        if (!moved || !stateReady || !pvpReady || !hamReady ||
            getLocomotion(attacker) != LOCOMOTION_STANDING ||
            getLocomotion(defender) != LOCOMOTION_STANDING ||
            getPosture(attacker) != POSTURE_UPRIGHT ||
            getPosture(defender) != POSTURE_UPRIGHT)
        {
            return "error=primaryRangePreparationFailed mode=" + mode +
                " moved=" + moved +
                " stateReady=" + stateReady +
                " pvpReady=" + pvpReady +
                " hamReady=" + hamReady + " " +
                buildStatus(attacker, defender, lifecycle);
        }
        return "action=armPrimaryRange mode=" + mode +
            " centerSeparationMeters=" + centerSeparationMeters + " " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String armSecondaryDefense(
        obj_id attacker, obj_id defender, String lifecycle, String mode,
        String controlMod, int controlDelta) throws InterruptedException
    {
        if (!setFixtureDefenderSkillTemplate(defender, mode.equals("ricochet")))
        {
            return "error=secondaryDefenseCertificationStateFailed mode=" + mode +
                " " + buildStatus(attacker, defender, lifecycle);
        }
        String placement =
            armPrimaryRange(attacker, defender, lifecycle, "secondary-" + mode, 16.0f);
        if (placement.startsWith("error="))
        {
            return placement;
        }
        if (!applySecondaryDefenseControl(defender, controlMod, controlDelta))
        {
            return "error=secondaryDefenseControlFailed mode=" + mode + " " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (mode.equals("ricochet"))
        {
            obj_id lightsaber =
                getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON);
            if (!isIdValid(lightsaber))
            {
                return "error=fixtureLightsaberMissing " +
                    buildStatus(attacker, defender, lifecycle);
            }
            // Retain the weapon-identity cache as a second, narrow
            // certification signal. The snapshot-backed class-template seam
            // above is authoritative across the transfer process boundary.
            script.library.utils.setScriptVar(
                defender, "combat.weaponCertified", lightsaber);
        }
        return "action=armSecondaryDefense mode=" + mode +
            " expectedDefenderWeapon=" +
            getSecondaryDefenseWeaponTemplate(mode) + " " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String armWounds(
        obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        if (ownership != null)
        {
            return ownership;
        }
        if (!restoreFixtureWounds(defender))
        {
            return "error=woundBaselineRestoreFailed " +
                buildStatus(attacker, defender, lifecycle);
        }
        String placement =
            armPrimaryRange(attacker, defender, lifecycle, "wounds", 16.0f);
        if (placement.startsWith("error="))
        {
            return placement;
        }
        return "action=armWounds expectedRifleWoundsRatio=4 " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String equipSecondaryDefenseWeapon(
        obj_id attacker, obj_id defender, String lifecycle, String mode)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        if (ownership != null)
        {
            return ownership;
        }
        if (getIntObjVar(attacker, PREPARED) != 1 ||
            getIntObjVar(defender, PREPARED) != 1)
        {
            return "error=fixtureNotPrepared";
        }

        String weaponObjvar = mode.equals("ricochet") ?
            DEFENDER_LIGHTSABER_WEAPON : DEFENDER_FALLBACK_WEAPON;
        String weaponTemplate = getSecondaryDefenseWeaponTemplate(mode);
        if (!hasOwnedWeapon(defender, weaponObjvar, weaponTemplate))
        {
            return "error=secondaryDefenseWeaponMissing mode=" + mode;
        }
        obj_id weapon = getObjIdObjVar(defender, weaponObjvar);
        obj_id current = getObjectInSlot(defender, "hold_r");
        if (current == weapon)
        {
            return "action=equipSecondaryDefenseWeapon mode=" + mode +
                " resumed=true equipped=true " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (isIdValid(current))
        {
            boolean currentOwned =
                current == getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON) ||
                current == getObjIdObjVar(defender, DEFENDER_FALLBACK_WEAPON);
            obj_id inventory = getObjectInSlot(defender, "inventory");
            if (!currentOwned || !isIdValid(inventory) ||
                !putInOverloaded(current, inventory))
            {
                return "error=unexpectedDefenderWeapon mode=" + mode + " " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            mode.equals("ricochet") && hasScript(weapon, COMBAT_WEAPON_SCRIPT);
        if (combatWeaponAttached)
        {
            detachScript(weapon, COMBAT_WEAPON_SCRIPT);
        }
        boolean transferReady =
            !combatWeaponAttached || !hasScript(weapon, COMBAT_WEAPON_SCRIPT);
        boolean equipped = transferReady && equipOverride(weapon, defender) &&
            getObjectInSlot(defender, "hold_r") == weapon;
        if (combatWeaponAttached)
        {
            attachScript(weapon, COMBAT_WEAPON_SCRIPT);
        }
        boolean scriptRestored =
            !combatWeaponAttached || hasScript(weapon, COMBAT_WEAPON_SCRIPT);
        return equipped && scriptRestored ?
            "action=equipSecondaryDefenseWeapon mode=" + mode +
                " resumed=false equipped=true combatWeaponScriptRestored=" +
                scriptRestored + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=secondaryDefenseWeaponEquipFailed mode=" + mode +
                " combatWeaponAttached=" + combatWeaponAttached +
                " transferReady=" + transferReady +
                " equipped=" + equipped +
                " combatWeaponScriptRestored=" + scriptRestored + " " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String cleanup(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanup alreadyClean=true lifecycle=" + lifecycle;
        }
        if (ownership != null)
        {
            return ownership;
        }
        return recover(attacker, defender, lifecycle).replace(
            "action=recover", "action=cleanup");
    }

    private String recover(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        boolean attackerRoot = hasFixtureState(attacker);
        boolean defenderRoot = hasFixtureState(defender);
        if (!attackerRoot && !defenderRoot)
        {
            return "action=recover alreadyClean=true lifecycle=" + lifecycle;
        }
        if (!attackerRoot || !defenderRoot ||
            !hasObjVar(attacker, LIFECYCLE) || !hasObjVar(defender, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(attacker, LIFECYCLE)) ||
            !lifecycle.equals(getStringObjVar(defender, LIFECYCLE)))
        {
            return "error=fixtureOwnershipMismatch";
        }
        if (!hasObjVar(attacker, ORIGINAL_PISTOL_ONE) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_TWO) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_THREE) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_FOUR) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_NOVICE) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_ONE) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_TWO) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_THREE) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_FOUR) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_ONE) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_TWO) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_THREE) ||
            !hasObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_FOUR) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_NOVICE) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ABILITY_ONE) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ABILITY_TWO) ||
            !hasObjVar(attacker, ORIGINAL_PISTOL_ABILITY_THREE) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_ABILITY_ONE) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_ABILITY_TWO) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_ABILITY_THREE) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_SPEED_TWO) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_SPEED_THREE) ||
            !hasObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) ||
            !hasObjVar(defender, ORIGINAL_CENTER_OF_BEING) ||
            !hasObjVar(defender, ORIGINAL_SABER_BLOCK) ||
            !hasObjVar(defender, ORIGINAL_DEFENDER_SKILL_TEMPLATE) ||
            !hasObjVar(defender, ORIGINAL_DEFENDER_WOUNDS) ||
            !hasObjVar(defender, ORIGINAL_DEFENDER_SHOCK))
        {
            return "error=fixtureSnapshotIncomplete";
        }

        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        boolean secondaryControlRestored = clearSecondaryDefenseControl(defender);
        clearFixtureWeaponCertification(defender);
        boolean defenderSkillTemplateRestored =
            setFixtureDefenderSkillTemplate(defender, false);
        boolean defenderWoundsRestored = restoreFixtureWounds(defender);
        boolean pistolDestroyed =
            destroyOwnedWeapon(attacker, PISTOL_WEAPON, PISTOL_TEMPLATE);
        boolean carbineDestroyed =
            destroyOwnedWeapon(attacker, CARBINE_WEAPON, CARBINE_TEMPLATE);
        boolean defenderRifleDestroyed =
            destroyOwnedWeapon(defender, DEFENDER_RIFLE_WEAPON, RIFLE_TEMPLATE);
        boolean defenderPistolDestroyed =
            destroyOwnedWeapon(defender, DEFENDER_PISTOL_WEAPON, PISTOL_TEMPLATE);
        boolean defenderCarbineDestroyed =
            destroyOwnedWeapon(defender, DEFENDER_CARBINE_WEAPON, CARBINE_TEMPLATE);
        boolean defenderLightsaberDestroyed =
            destroyOwnedWeapon(
                defender, DEFENDER_LIGHTSABER_WEAPON, LIGHTSABER_TEMPLATE);
        boolean defenderFallbackDestroyed =
            destroyOwnedWeapon(
                defender, DEFENDER_FALLBACK_WEAPON, FALLBACK_WEAPON_TEMPLATE);
        if (!secondaryControlRestored || !defenderSkillTemplateRestored ||
            !defenderWoundsRestored ||
            !pistolDestroyed || !carbineDestroyed ||
            !defenderRifleDestroyed || !defenderPistolDestroyed ||
            !defenderCarbineDestroyed || !defenderLightsaberDestroyed ||
            !defenderFallbackDestroyed)
        {
            return "error=fixtureWeaponRestoreFailed control=" +
                secondaryControlRestored +
                " skillTemplate=" + defenderSkillTemplateRestored +
                " wounds=" + defenderWoundsRestored +
                " pistol=" + pistolDestroyed +
                " carbine=" + carbineDestroyed +
                " defenderRifle=" + defenderRifleDestroyed +
                " defenderPistol=" + defenderPistolDestroyed +
                " defenderCarbine=" + defenderCarbineDestroyed +
                " defenderLightsaber=" + defenderLightsaberDestroyed +
                " defenderFallback=" + defenderFallbackDestroyed;
        }

        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, PISTOL_ACCURACY_FOUR))
        {
            revokeSkill(attacker, PISTOL_ACCURACY_FOUR);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, PISTOL_ACCURACY_THREE))
        {
            revokeSkill(attacker, PISTOL_ACCURACY_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, PISTOL_ACCURACY_TWO))
        {
            revokeSkill(attacker, PISTOL_ACCURACY_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, PISTOL_ACCURACY_ONE))
        {
            revokeSkill(attacker, PISTOL_ACCURACY_ONE);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_THREE) == 0 &&
            hasSkill(attacker, PISTOL_ABILITY_THREE))
        {
            revokeSkill(attacker, PISTOL_ABILITY_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_TWO) == 0 &&
            hasSkill(attacker, PISTOL_ABILITY_TWO))
        {
            revokeSkill(attacker, PISTOL_ABILITY_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_ONE) == 0 &&
            hasSkill(attacker, PISTOL_ABILITY_ONE))
        {
            revokeSkill(attacker, PISTOL_ABILITY_ONE);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_NOVICE) == 0 &&
            hasSkill(attacker, PISTOL_NOVICE))
        {
            revokeSkill(attacker, PISTOL_NOVICE);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_FOUR);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_THREE) == 0 &&
            hasSkill(attacker, CARBINE_ABILITY_THREE))
        {
            revokeSkill(attacker, CARBINE_ABILITY_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_TWO) == 0 &&
            hasSkill(attacker, CARBINE_ABILITY_TWO))
        {
            revokeSkill(attacker, CARBINE_ABILITY_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_ONE) == 0 &&
            hasSkill(attacker, CARBINE_ABILITY_ONE))
        {
            revokeSkill(attacker, CARBINE_ABILITY_ONE);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_THREE) == 0 &&
            hasSkill(attacker, CARBINE_SPEED_THREE))
        {
            revokeSkill(attacker, CARBINE_SPEED_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_TWO) == 0 &&
            hasSkill(attacker, CARBINE_SPEED_TWO))
        {
            revokeSkill(attacker, CARBINE_SPEED_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) == 0 &&
            hasSkill(attacker, CARBINE_SPEED_ONE))
        {
            revokeSkill(attacker, CARBINE_SPEED_ONE);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_NOVICE) == 0 &&
            hasSkill(attacker, CARBINE_NOVICE))
        {
            revokeSkill(attacker, CARBINE_NOVICE);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_FOUR);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_THREE))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_THREE);
        }
        if (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_TWO))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_TWO);
        }
        if (getIntObjVar(attacker, ORIGINAL_CARBINE_ONE) == 0 &&
            hasSkill(attacker, CARBINE_ONE))
        {
            revokeSkill(attacker, CARBINE_ONE);
        }
        if (getIntObjVar(attacker, ORIGINAL_PISTOL_ONE) == 0 &&
            hasSkill(attacker, PISTOL_ONE))
        {
            revokeSkill(attacker, PISTOL_ONE);
        }
        if ((getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_FOUR) == 0 &&
                hasSkill(attacker, PISTOL_ACCURACY_FOUR)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_THREE) == 0 &&
                hasSkill(attacker, PISTOL_ACCURACY_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_TWO) == 0 &&
                hasSkill(attacker, PISTOL_ACCURACY_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ACCURACY_ONE) == 0 &&
                hasSkill(attacker, PISTOL_ACCURACY_ONE)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_THREE) == 0 &&
                hasSkill(attacker, PISTOL_ABILITY_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_TWO) == 0 &&
                hasSkill(attacker, PISTOL_ABILITY_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ABILITY_ONE) == 0 &&
                hasSkill(attacker, PISTOL_ABILITY_ONE)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_NOVICE) == 0 &&
                hasSkill(attacker, PISTOL_NOVICE)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_FOUR) == 0 &&
                hasSkill(attacker, MARKSMAN_PISTOL_FOUR)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_THREE) == 0 &&
                hasSkill(attacker, MARKSMAN_PISTOL_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_PISTOL_TWO) == 0 &&
                hasSkill(attacker, MARKSMAN_PISTOL_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) == 0 &&
                hasCommand(attacker, CARBINE_CDEF_CERTIFICATION)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_ONE) == 0 &&
                hasSkill(attacker, CARBINE_ABILITY_ONE)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_TWO) == 0 &&
                hasSkill(attacker, CARBINE_ABILITY_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_THREE) == 0 &&
                hasSkill(attacker, CARBINE_ABILITY_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_THREE) == 0 &&
                hasSkill(attacker, CARBINE_SPEED_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_TWO) == 0 &&
                hasSkill(attacker, CARBINE_SPEED_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) == 0 &&
                hasSkill(attacker, CARBINE_SPEED_ONE)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_NOVICE) == 0 &&
                hasSkill(attacker, CARBINE_NOVICE)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_FOUR) == 0 &&
                hasSkill(attacker, MARKSMAN_CARBINE_FOUR)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_THREE) == 0 &&
                hasSkill(attacker, MARKSMAN_CARBINE_THREE)) ||
            (getIntObjVar(attacker, ORIGINAL_MARKSMAN_CARBINE_TWO) == 0 &&
                hasSkill(attacker, MARKSMAN_CARBINE_TWO)) ||
            (getIntObjVar(attacker, ORIGINAL_CARBINE_ONE) == 0 &&
                hasSkill(attacker, CARBINE_ONE)) ||
            (getIntObjVar(attacker, ORIGINAL_PISTOL_ONE) == 0 &&
                hasSkill(attacker, PISTOL_ONE)))
        {
            return "error=fixtureSkillRestoreFailed";
        }

        removeObjVar(attacker, ROOT);
        removeObjVar(defender, ROOT);
        return "action=recover alreadyClean=false restored=true lifecycle=" + lifecycle;
    }

    private boolean restoreFixtureWounds(obj_id defender)
        throws InterruptedException
    {
        if (!hasObjVar(defender, ORIGINAL_DEFENDER_WOUNDS) ||
            !hasObjVar(defender, ORIGINAL_DEFENDER_SHOCK))
        {
            return false;
        }
        int[] originalWounds =
            getIntArrayObjVar(defender, ORIGINAL_DEFENDER_WOUNDS);
        if (originalWounds == null || originalWounds.length != NUM_ATTRIBUTES)
        {
            return false;
        }
        for (int attribute = 0; attribute < NUM_ATTRIBUTES; ++attribute)
        {
            int current = getAttribWound(defender, attribute);
            int delta = current - originalWounds[attribute];
            if (current == ATTRIB_ERROR || delta < 0 ||
                (delta > 0 && healWound(defender, attribute, delta) != delta) ||
                getAttribWound(defender, attribute) != originalWounds[attribute])
            {
                return false;
            }
        }
        int originalShock = getIntObjVar(defender, ORIGINAL_DEFENDER_SHOCK);
        return setShockWound(defender, originalShock) &&
            getShockWound(defender) == originalShock;
    }

    private boolean fixtureReady(obj_id attacker, obj_id defender)
        throws InterruptedException
    {
        return getIntObjVar(attacker, PREPARED) == 1 &&
            getIntObjVar(defender, PREPARED) == 1 &&
            hasSkill(attacker, PISTOL_ONE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO) &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR) &&
            hasSkill(attacker, PISTOL_NOVICE) &&
            hasSkill(attacker, PISTOL_ACCURACY_ONE) &&
            hasSkill(attacker, PISTOL_ACCURACY_TWO) &&
            hasSkill(attacker, PISTOL_ACCURACY_THREE) &&
            hasSkill(attacker, PISTOL_ACCURACY_FOUR) &&
            hasSkill(attacker, CARBINE_ONE) &&
            hasSkill(attacker, MARKSMAN_CARBINE_TWO) &&
            hasSkill(attacker, MARKSMAN_CARBINE_THREE) &&
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR) &&
            hasSkill(attacker, CARBINE_NOVICE) &&
            hasSkill(attacker, PISTOL_ABILITY_ONE) &&
            hasSkill(attacker, PISTOL_ABILITY_TWO) &&
            hasSkill(attacker, PISTOL_ABILITY_THREE) &&
            hasSkill(attacker, CARBINE_ABILITY_ONE) &&
            hasSkill(attacker, CARBINE_ABILITY_TWO) &&
            hasSkill(attacker, CARBINE_ABILITY_THREE) &&
            hasSkill(attacker, CARBINE_SPEED_ONE) &&
            hasSkill(attacker, CARBINE_SPEED_TWO) &&
            hasSkill(attacker, CARBINE_SPEED_THREE) &&
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) &&
            hasCommand(attacker, BODY_COMMAND) &&
            hasCommand(attacker, LEG_COMMAND) &&
            hasCommand(attacker, BURST_COMMAND) &&
            hasCommand(attacker, BURST_TWO_COMMAND) &&
            hasCommand(attacker, DISARM_COMMAND) &&
            hasCommand(attacker, DOUBLE_TAP_COMMAND) &&
            hasCommand(attacker, STOPPING_SHOT_COMMAND) &&
            hasCommand(attacker, CRIPPLING_SHOT_COMMAND) &&
            hasCommand(attacker, POINT_BLANK_SINGLE_TWO_COMMAND) &&
            hasCommand(attacker, POINT_BLANK_AREA_TWO_COMMAND) &&
            hasOwnedWeapon(attacker, PISTOL_WEAPON, PISTOL_TEMPLATE) &&
            hasOwnedWeapon(attacker, CARBINE_WEAPON, CARBINE_TEMPLATE) &&
            hasOwnedWeapon(defender, DEFENDER_RIFLE_WEAPON, RIFLE_TEMPLATE) &&
            hasOwnedWeapon(defender, DEFENDER_PISTOL_WEAPON, PISTOL_TEMPLATE) &&
            hasOwnedWeapon(defender, DEFENDER_CARBINE_WEAPON, CARBINE_TEMPLATE) &&
            hasOwnedWeapon(
                defender, DEFENDER_LIGHTSABER_WEAPON, LIGHTSABER_TEMPLATE) &&
            getIntObjVar(
                getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON),
                script.library.weapons.OBJVAR_WP_LEVEL) == 1 &&
            hasOwnedWeapon(
                defender, DEFENDER_FALLBACK_WEAPON, FALLBACK_WEAPON_TEMPLATE) &&
            getIntObjVar(
                getObjIdObjVar(defender, DEFENDER_FALLBACK_WEAPON),
                script.library.weapons.OBJVAR_WP_LEVEL) == 1;
    }

    private boolean hasOwnedWeapon(
        obj_id owner, String objvar, String template) throws InterruptedException
    {
        if (!hasObjVar(owner, objvar))
        {
            return false;
        }
        obj_id weapon = getObjIdObjVar(owner, objvar);
        return isIdValid(weapon) && template.equals(getTemplateName(weapon));
    }

    private obj_id createOwnedWeapon(
        obj_id owner, String objvar, String template) throws InterruptedException
    {
        obj_id weapon = createObjectInInventoryAllowOverload(template, owner);
        if (!isIdValid(weapon) || !template.equals(getTemplateName(weapon)))
        {
            if (isIdValid(weapon))
            {
                destroyObject(weapon);
            }
            return obj_id.NULL_ID;
        }
        setObjVar(owner, objvar, weapon);
        return weapon;
    }

    private boolean destroyOwnedWeapon(
        obj_id owner, String objvar, String template) throws InterruptedException
    {
        if (!hasObjVar(owner, objvar))
        {
            return true;
        }
        obj_id weapon = getObjIdObjVar(owner, objvar);
        if (!isIdValid(weapon))
        {
            removeObjVar(owner, objvar);
            return true;
        }
        if (!template.equals(getTemplateName(weapon)))
        {
            return false;
        }
        if (!destroyObject(weapon))
        {
            return false;
        }
        removeObjVar(owner, objvar);
        return true;
    }

    private String getSecondaryDefenseWeaponTemplate(String mode)
        throws InterruptedException
    {
        if (mode.equals("block"))
        {
            return RIFLE_TEMPLATE;
        }
        if (mode.equals("dodge"))
        {
            return PISTOL_TEMPLATE;
        }
        if (mode.equals("counter"))
        {
            return CARBINE_TEMPLATE;
        }
        if (mode.equals("ricochet"))
        {
            return LIGHTSABER_TEMPLATE;
        }
        return FALLBACK_WEAPON_TEMPLATE;
    }

    private boolean applySecondaryDefenseControl(
        obj_id defender, String controlMod, int controlDelta)
        throws InterruptedException
    {
        if (!clearSecondaryDefenseControl(defender))
        {
            return false;
        }
        if (controlDelta == 0)
        {
            return true;
        }
        int originalValue = getOriginalSecondaryDefenseValue(defender, controlMod);
        if (!applySkillStatisticModifier(defender, controlMod, controlDelta))
        {
            return false;
        }
        if (getEnhancedSkillStatisticModifierUncapped(defender, controlMod) !=
            originalValue + controlDelta)
        {
            applySkillStatisticModifier(defender, controlMod, -controlDelta);
            return false;
        }
        setObjVar(defender, SECONDARY_CONTROL_MOD, controlMod);
        setObjVar(defender, SECONDARY_CONTROL_DELTA, controlDelta);
        return true;
    }

    private boolean clearSecondaryDefenseControl(obj_id defender)
        throws InterruptedException
    {
        boolean hasMod = hasObjVar(defender, SECONDARY_CONTROL_MOD);
        boolean hasDelta = hasObjVar(defender, SECONDARY_CONTROL_DELTA);
        if (!hasMod && !hasDelta)
        {
            return true;
        }
        if (!hasMod || !hasDelta)
        {
            return false;
        }
        String controlMod = getStringObjVar(defender, SECONDARY_CONTROL_MOD);
        int controlDelta = getIntObjVar(defender, SECONDARY_CONTROL_DELTA);
        int originalValue = getOriginalSecondaryDefenseValue(defender, controlMod);
        boolean restored =
            applySkillStatisticModifier(defender, controlMod, -controlDelta) &&
            getEnhancedSkillStatisticModifierUncapped(defender, controlMod) ==
                originalValue;
        if (restored)
        {
            removeObjVar(defender, SECONDARY_CONTROL_MOD);
            removeObjVar(defender, SECONDARY_CONTROL_DELTA);
        }
        return restored;
    }

    private void clearFixtureWeaponCertification(obj_id defender)
        throws InterruptedException
    {
        if (!script.library.utils.hasScriptVar(
                defender, "combat.weaponCertified") ||
            !hasObjVar(defender, DEFENDER_LIGHTSABER_WEAPON))
        {
            return;
        }
        obj_id certified = script.library.utils.getObjIdScriptVar(
            defender, "combat.weaponCertified");
        if (certified == getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON))
        {
            script.library.utils.removeScriptVar(
                defender, "combat.weaponCertified");
        }
    }

    private boolean setFixtureDefenderSkillTemplate(
        obj_id defender, boolean forceSensitive) throws InterruptedException
    {
        if (!hasObjVar(defender, ORIGINAL_DEFENDER_SKILL_TEMPLATE))
        {
            return false;
        }
        String expected = forceSensitive ? "force_sensitive_1a" :
            getStringObjVar(defender, ORIGINAL_DEFENDER_SKILL_TEMPLATE);
        String current = getSkillTemplate(defender);
        if (current == null)
        {
            current = "";
        }
        if (!expected.equals(current))
        {
            setSkillTemplate(defender, expected);
        }
        String actual = getSkillTemplate(defender);
        return expected.equals(actual == null ? "" : actual);
    }

    private int getOriginalSecondaryDefenseValue(
        obj_id defender, String controlMod) throws InterruptedException
    {
        return controlMod.equals("saber_block") ?
            getIntObjVar(defender, ORIGINAL_SABER_BLOCK) :
            getIntObjVar(defender, ORIGINAL_CENTER_OF_BEING);
    }

    private void resetLiveDiagnostic(obj_id attacker) throws InterruptedException
    {
        if (hasObjVar(attacker, DIAGNOSTIC_ROOT))
        {
            removeObjVar(attacker, DIAGNOSTIC_ROOT);
        }
        setObjVar(attacker, DIAGNOSTIC_ENABLED, 1);
    }

    private boolean setAttribAndVerify(obj_id player, int attrib, int value)
        throws InterruptedException
    {
        setAttrib(player, attrib, value);
        return getAttrib(player, attrib) == value;
    }

    private String validateHeadShotLayer(
        obj_id attacker, obj_id defender, String lifecycle) throws InterruptedException
    {
        if (!hasObjVar(attacker, HEADSHOT_LIFECYCLE) ||
            !hasObjVar(defender, HEADSHOT_LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(attacker, HEADSHOT_LIFECYCLE)) ||
            !lifecycle.equals(getStringObjVar(defender, HEADSHOT_LIFECYCLE)) ||
            getIntObjVar(attacker, HEADSHOT_PREPARED) != 1 ||
            getIntObjVar(defender, HEADSHOT_PREPARED) != 1)
        {
            return "error=headShotFixtureDependencyNotPrepared";
        }
        return null;
    }

    private String validatePlayer(obj_id player, int stationId, String role)
        throws InterruptedException
    {
        if (player == null || player == obj_id.NULL_ID || !player.isLoaded())
        {
            return "error=" + role + "NotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player))
        {
            return "error=" + role + "NotAuthoritative oid=" + player;
        }
        return getPlayerStationId(player) == stationId ?
            null : "error=" + role + "StationNotAllowed oid=" + player;
    }

    private String validateOwnership(
        obj_id attacker, obj_id defender, String lifecycle, boolean allowAbsent)
        throws InterruptedException
    {
        boolean attackerRoot = hasFixtureState(attacker);
        boolean defenderRoot = hasFixtureState(defender);
        if (!attackerRoot && !defenderRoot)
        {
            return allowAbsent ? "fixtureAbsent" : "error=fixtureAbsent";
        }
        if (!attackerRoot || !defenderRoot ||
            !hasObjVar(attacker, LIFECYCLE) || !hasObjVar(defender, LIFECYCLE) ||
            !hasObjVar(attacker, PEER) || !hasObjVar(defender, PEER) ||
            !lifecycle.equals(getStringObjVar(attacker, LIFECYCLE)) ||
            !lifecycle.equals(getStringObjVar(defender, LIFECYCLE)) ||
            getObjIdObjVar(attacker, PEER) != defender ||
            getObjIdObjVar(defender, PEER) != attacker)
        {
            return "error=fixtureOwnershipMismatch";
        }
        return null;
    }

    private boolean hasFixtureState(obj_id player) throws InterruptedException
    {
        return hasObjVar(player, LIFECYCLE) ||
            hasObjVar(player, PEER) ||
            hasObjVar(player, PREPARED) ||
            hasObjVar(player, ORIGINAL_PISTOL_ONE) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_PISTOL_TWO) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_PISTOL_THREE) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_PISTOL_FOUR) ||
            hasObjVar(player, ORIGINAL_PISTOL_NOVICE) ||
            hasObjVar(player, ORIGINAL_PISTOL_ACCURACY_ONE) ||
            hasObjVar(player, ORIGINAL_PISTOL_ACCURACY_TWO) ||
            hasObjVar(player, ORIGINAL_PISTOL_ACCURACY_THREE) ||
            hasObjVar(player, ORIGINAL_PISTOL_ACCURACY_FOUR) ||
            hasObjVar(player, ORIGINAL_CARBINE_ONE) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_CARBINE_TWO) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_CARBINE_THREE) ||
            hasObjVar(player, ORIGINAL_MARKSMAN_CARBINE_FOUR) ||
            hasObjVar(player, ORIGINAL_CARBINE_NOVICE) ||
            hasObjVar(player, ORIGINAL_PISTOL_ABILITY_ONE) ||
            hasObjVar(player, ORIGINAL_PISTOL_ABILITY_TWO) ||
            hasObjVar(player, ORIGINAL_PISTOL_ABILITY_THREE) ||
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_ONE) ||
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_TWO) ||
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_THREE) ||
            hasObjVar(player, ORIGINAL_CARBINE_SPEED_ONE) ||
            hasObjVar(player, ORIGINAL_CARBINE_SPEED_TWO) ||
            hasObjVar(player, ORIGINAL_CARBINE_SPEED_THREE) ||
            hasObjVar(player, ORIGINAL_CARBINE_CDEF_CERTIFICATION) ||
            hasObjVar(player, ORIGINAL_CENTER_OF_BEING) ||
            hasObjVar(player, ORIGINAL_SABER_BLOCK) ||
            hasObjVar(player, ORIGINAL_DEFENDER_SKILL_TEMPLATE) ||
            hasObjVar(player, ORIGINAL_DEFENDER_WOUNDS) ||
            hasObjVar(player, ORIGINAL_DEFENDER_SHOCK) ||
            hasObjVar(player, PISTOL_WEAPON) ||
            hasObjVar(player, CARBINE_WEAPON) ||
            hasObjVar(player, DEFENDER_RIFLE_WEAPON) ||
            hasObjVar(player, DEFENDER_PISTOL_WEAPON) ||
            hasObjVar(player, DEFENDER_CARBINE_WEAPON) ||
            hasObjVar(player, DEFENDER_LIGHTSABER_WEAPON) ||
            hasObjVar(player, DEFENDER_FALLBACK_WEAPON) ||
            hasObjVar(player, SECONDARY_CONTROL_MOD) ||
            hasObjVar(player, SECONDARY_CONTROL_DELTA);
    }

    private String buildStatus(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        obj_id weapon = getObjectInSlot(attacker, "hold_r");
        String weaponTemplate = isIdValid(weapon) ? getTemplateName(weapon) : "none";
        obj_id defenderWeapon = getObjectInSlot(defender, "hold_r");
        String defenderWeaponTemplate = isIdValid(defenderWeapon) ?
            getTemplateName(defenderWeapon) : "none";
        combat_data headShotData = combat_engine.getCombatData("headShot1");
        float headShotMaxRange = headShotData == null ? -1.0f : headShotData.maxRange;
        String pistol = hasObjVar(attacker, PISTOL_WEAPON) ?
            getObjIdObjVar(attacker, PISTOL_WEAPON).toString() : "none";
        String carbine = hasObjVar(attacker, CARBINE_WEAPON) ?
            getObjIdObjVar(attacker, CARBINE_WEAPON).toString() : "none";
        String defenderRifle = hasObjVar(defender, DEFENDER_RIFLE_WEAPON) ?
            getObjIdObjVar(defender, DEFENDER_RIFLE_WEAPON).toString() : "none";
        String defenderPistol = hasObjVar(defender, DEFENDER_PISTOL_WEAPON) ?
            getObjIdObjVar(defender, DEFENDER_PISTOL_WEAPON).toString() : "none";
        String defenderCarbine = hasObjVar(defender, DEFENDER_CARBINE_WEAPON) ?
            getObjIdObjVar(defender, DEFENDER_CARBINE_WEAPON).toString() : "none";
        String defenderLightsaber =
            hasObjVar(defender, DEFENDER_LIGHTSABER_WEAPON) ?
            getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON).toString() : "none";
        String defenderFallback =
            hasObjVar(defender, DEFENDER_FALLBACK_WEAPON) ?
            getObjIdObjVar(defender, DEFENDER_FALLBACK_WEAPON).toString() : "none";
        return "lifecycle=" + lifecycle +
            " storedAttackerLifecycle=" +
                (hasObjVar(attacker, LIFECYCLE) ?
                    getStringObjVar(attacker, LIFECYCLE) : "none") +
            " storedDefenderLifecycle=" +
                (hasObjVar(defender, LIFECYCLE) ?
                    getStringObjVar(defender, LIFECYCLE) : "none") +
            " prepared=" + (getIntObjVar(attacker, PREPARED) == 1 &&
                getIntObjVar(defender, PREPARED) == 1) +
            " headShotPrepared=" + (getIntObjVar(attacker, HEADSHOT_PREPARED) == 1 &&
                getIntObjVar(defender, HEADSHOT_PREPARED) == 1) +
            " pistolOne=" + hasSkill(attacker, PISTOL_ONE) +
            " marksmanPistolTwo=" + hasSkill(attacker, MARKSMAN_PISTOL_TWO) +
            " marksmanPistolThree=" + hasSkill(attacker, MARKSMAN_PISTOL_THREE) +
            " marksmanPistolFour=" + hasSkill(attacker, MARKSMAN_PISTOL_FOUR) +
            " pistolNovice=" + hasSkill(attacker, PISTOL_NOVICE) +
            " pistolAccuracyOne=" + hasSkill(attacker, PISTOL_ACCURACY_ONE) +
            " pistolAccuracyTwo=" + hasSkill(attacker, PISTOL_ACCURACY_TWO) +
            " pistolAccuracyThree=" + hasSkill(attacker, PISTOL_ACCURACY_THREE) +
            " pistolAccuracyFour=" + hasSkill(attacker, PISTOL_ACCURACY_FOUR) +
            " carbineOne=" + hasSkill(attacker, CARBINE_ONE) +
            " marksmanCarbineTwo=" + hasSkill(attacker, MARKSMAN_CARBINE_TWO) +
            " marksmanCarbineThree=" + hasSkill(attacker, MARKSMAN_CARBINE_THREE) +
            " marksmanCarbineFour=" + hasSkill(attacker, MARKSMAN_CARBINE_FOUR) +
            " carbineProfession=" +
                hasSkill(attacker, "combat_carbine") +
            " carbineNovice=" + hasSkill(attacker, CARBINE_NOVICE) +
            " pistolAbilityOne=" + hasSkill(attacker, PISTOL_ABILITY_ONE) +
            " pistolAbilityTwo=" + hasSkill(attacker, PISTOL_ABILITY_TWO) +
            " pistolAbilityThree=" + hasSkill(attacker, PISTOL_ABILITY_THREE) +
            " carbineAbilityOne=" + hasSkill(attacker, CARBINE_ABILITY_ONE) +
            " carbineAbilityTwo=" + hasSkill(attacker, CARBINE_ABILITY_TWO) +
            " carbineAbilityThree=" + hasSkill(attacker, CARBINE_ABILITY_THREE) +
            " carbineSpeedOne=" + hasSkill(attacker, CARBINE_SPEED_ONE) +
            " carbineSpeedTwo=" + hasSkill(attacker, CARBINE_SPEED_TWO) +
            " carbineSpeedThree=" + hasSkill(attacker, CARBINE_SPEED_THREE) +
            " carbineCdefCertification=" +
                hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) +
            " hasBodyShot1=" + hasCommand(attacker, BODY_COMMAND) +
            " hasLegShot1=" + hasCommand(attacker, LEG_COMMAND) +
            " hasBurstShot1=" + hasCommand(attacker, BURST_COMMAND) +
            " hasBurstShot2=" + hasCommand(attacker, BURST_TWO_COMMAND) +
            " hasDisarmingShot1=" + hasCommand(attacker, DISARM_COMMAND) +
            " hasDoubleTap=" + hasCommand(attacker, DOUBLE_TAP_COMMAND) +
            " hasStoppingShot=" + hasCommand(attacker, STOPPING_SHOT_COMMAND) +
            " hasCripplingShot=" + hasCommand(attacker, CRIPPLING_SHOT_COMMAND) +
            " hasPointBlankSingle2=" +
                hasCommand(attacker, POINT_BLANK_SINGLE_TWO_COMMAND) +
            " hasPointBlankArea2=" +
                hasCommand(attacker, POINT_BLANK_AREA_TWO_COMMAND) +
            " weapon=" + weaponTemplate +
            " canBodyShot1=" + (isIdValid(weapon) ?
                combat.canPerformAction(BODY_COMMAND, attacker) : -1) +
            " canLegShot1=" + (isIdValid(weapon) ?
                combat.canPerformAction(LEG_COMMAND, attacker) : -1) +
            " canBurstShot1=" + (isIdValid(weapon) ?
                combat.canPerformAction(BURST_COMMAND, attacker) : -1) +
            " canBurstShot2=" + (isIdValid(weapon) ?
                combat.canPerformAction(BURST_TWO_COMMAND, attacker) : -1) +
            " canDisarmingShot1=" + (isIdValid(weapon) ?
                combat.canPerformAction(DISARM_COMMAND, attacker) : -1) +
            " canDoubleTap=" + (isIdValid(weapon) ?
                combat.canPerformAction(DOUBLE_TAP_COMMAND, attacker) : -1) +
            " canStoppingShot=" + (isIdValid(weapon) ?
                combat.canPerformAction(STOPPING_SHOT_COMMAND, attacker) : -1) +
            " canCripplingShot=" + (isIdValid(weapon) ?
                combat.canPerformAction(CRIPPLING_SHOT_COMMAND, attacker) : -1) +
            " canPointBlankSingle2=" + (isIdValid(weapon) ?
                combat.canPerformAction(POINT_BLANK_SINGLE_TWO_COMMAND, attacker) : -1) +
            " canPointBlankArea2=" + (isIdValid(weapon) ?
                combat.canPerformAction(POINT_BLANK_AREA_TWO_COMMAND, attacker) : -1) +
            " pistolWeapon=" + pistol +
            " carbineWeapon=" + carbine +
            " defenderWeapon=" + defenderWeaponTemplate +
            " defenderRifleWeapon=" + defenderRifle +
            " defenderPistolWeapon=" + defenderPistol +
            " defenderCarbineWeapon=" + defenderCarbine +
            " defenderLightsaberWeapon=" + defenderLightsaber +
            " defenderFallbackWeapon=" + defenderFallback +
            " secondaryControlMod=" +
                (hasObjVar(defender, SECONDARY_CONTROL_MOD) ?
                    getStringObjVar(defender, SECONDARY_CONTROL_MOD) : "none") +
            " secondaryControlDelta=" +
                (hasObjVar(defender, SECONDARY_CONTROL_DELTA) ?
                    getIntObjVar(defender, SECONDARY_CONTROL_DELTA) : 0) +
            " defenderCenterOfBeing=" +
                getEnhancedSkillStatisticModifierUncapped(
                    defender, "private_center_of_being") +
            " defenderSaberBlock=" +
                getEnhancedSkillStatisticModifierUncapped(defender, "saber_block") +
            " defenderSkillTemplate=" +
                (getSkillTemplate(defender) == null ?
                    "" : getSkillTemplate(defender)) +
            " defenderLevel=" + getLevel(defender) +
            " defenderLightsaberCertified=" +
                isFixtureLightsaberCertified(defender) +
            " globalMaxCombatRange=" + combat_engine.getMaxCombatRange() +
            " attackerWeaponMaxRange=" +
                (isIdValid(weapon) ? getMaxRange(weapon) : -1.0f) +
            " headShot1MaxRange=" + headShotMaxRange +
            " lineOfSight=" + canSee(attacker, defender) +
            " diagnosticEnabled=" +
                readDiagnosticInt(attacker, "enabled", 0) +
            " diagnosticAction=" +
                readDiagnosticString(attacker, "action", "none") +
            " diagnosticAttackerWeapon=" +
                readDiagnosticString(attacker, "attackerWeaponTemplate", "none") +
            " diagnosticPrimaryAccuracySkill=" +
                readDiagnosticInt(attacker, "primary.accuracySkill", -1) +
            " diagnosticPrimaryAccuracyWeapon=" +
                readDiagnosticFloat(attacker, "primary.accuracyWeapon", -1.0f) +
            " diagnosticPrimaryAccuracyPosture=" +
                readDiagnosticInt(attacker, "primary.accuracyPosture", -1) +
            " diagnosticPrimaryAccuracyBonus=" +
                readDiagnosticInt(attacker, "primary.accuracyBonus", -1) +
            " diagnosticPrimaryAccuracyPrivate=" +
                readDiagnosticInt(attacker, "primary.accuracyPrivate", -1) +
            " diagnosticPrimaryAccuracyTotal=" +
                readDiagnosticFloat(attacker, "primary.accuracyTotal", -1.0f) +
            " diagnosticPrimaryDefenseSkill=" +
                readDiagnosticInt(attacker, "primary.defenseSkill", -1) +
            " diagnosticPrimaryDefenseLocomotion=" +
                readDiagnosticInt(attacker, "primary.defenseLocomotion", -1) +
            " diagnosticPrimaryDefenseTotal=" +
                readDiagnosticFloat(attacker, "primary.defenseTotal", -1.0f) +
            " diagnosticPrimaryHitChance=" +
                readDiagnosticFloat(attacker, "primary.hitChance", -1.0f) +
            " diagnosticPrimaryRoll=" +
                readDiagnosticInt(attacker, "primary.roll", -1) +
            " diagnosticPrimaryResult=" +
                readDiagnosticInt(attacker, "primary.result", -1) +
            " diagnosticPrimaryResultName=" +
                readDiagnosticString(attacker, "primary.resultName", "none") +
            " diagnosticSecondaryProfile=" +
                readDiagnosticString(attacker, "secondary.profile", "none") +
            " diagnosticSecondaryDefenderWeapon=" +
                readDiagnosticString(
                    attacker, "secondary.defenderWeaponTemplate", "none") +
            " diagnosticSecondarySkillName=" +
                readDiagnosticString(attacker, "secondary.skillName", "none") +
            " diagnosticSecondaryAccuracyTotal=" +
                readDiagnosticFloat(attacker, "secondary.accuracyTotal", -1.0f) +
            " diagnosticSecondaryEvadeSkill=" +
                readDiagnosticInt(attacker, "secondary.evadeSkill", -1) +
            " diagnosticSecondaryEvadeCenter=" +
                readDiagnosticInt(attacker, "secondary.evadeCenter", -1) +
            " diagnosticSecondaryEvadePosture=" +
                readDiagnosticInt(attacker, "secondary.evadePosture", -1) +
            " diagnosticSecondaryEvadeTotal=" +
                readDiagnosticInt(attacker, "secondary.evadeTotal", -1) +
            " diagnosticSecondaryAttackRoll=" +
                readDiagnosticInt(attacker, "secondary.attackRoll", -1) +
            " diagnosticSecondaryDefendRoll=" +
                readDiagnosticInt(attacker, "secondary.defendRoll", -1) +
            " diagnosticSecondaryResult=" +
                readDiagnosticInt(attacker, "secondary.result", -1) +
            " diagnosticSecondaryResultName=" +
                readDiagnosticString(attacker, "secondary.resultName", "none") +
            " diagnosticSecondaryBlockBaseBefore=" +
                readDiagnosticInt(attacker, "secondary.blockBaseBefore", -1) +
            " diagnosticSecondaryBlockBaseAfter=" +
                readDiagnosticInt(attacker, "secondary.blockBaseAfter", -1) +
            " diagnosticSecondaryBlockElementalBefore=" +
                readDiagnosticInt(attacker, "secondary.blockElementalBefore", -1) +
            " diagnosticSecondaryBlockElementalAfter=" +
                readDiagnosticInt(attacker, "secondary.blockElementalAfter", -1) +
            " diagnosticSecondaryCounterQueued=" +
                readDiagnosticInt(attacker, "secondary.counterQueued", -1) +
            " diagnosticSecondaryCounterDispatched=" +
                readDiagnosticInt(attacker, "secondary.counterDispatched", -1) +
            " diagnosticSecondaryNgeParryBranch=" +
                readDiagnosticInt(attacker, "secondary.ngeParryBranch", -1) +
            " diagnosticSecondaryReflectQueued=" +
                readDiagnosticInt(attacker, "secondary.reflectQueued", -1) +
            " diagnosticWoundProfile=" +
                readDiagnosticString(attacker, "wound.profile", "none") +
            " diagnosticWoundsRatio=" +
                readDiagnosticInt(attacker, "wound.ratio", -1) +
            " diagnosticWoundRoll=" +
                readDiagnosticInt(attacker, "wound.roll", -1) +
            " diagnosticWoundPrimaryAttribute=" +
                readDiagnosticInt(attacker, "wound.primaryAttribute", -1) +
            " diagnosticWoundApplied=" +
                readDiagnosticInt(attacker, "wound.applied", -1) +
            " diagnosticShockAdded=" +
                readDiagnosticInt(attacker, "wound.shockAdded", -1) +
            " diagnosticSpamKey=" +
                readDiagnosticString(attacker, "spam.key", "none") +
            " diagnosticSpamResult=" +
                readDiagnosticInt(attacker, "spam.result", -1) +
            " diagnosticSpamDamage=" +
                readDiagnosticInt(attacker, "spam.damage", -1) +
            " distanceCentimeters=" + (int)(getDistance(attacker, defender) * 100.0f) +
            " pvpCanAttack=" + pvpCanAttack(attacker, defender) +
            " attackerHealth=" + getAttrib(attacker, HEALTH) +
            " attackerMaxHealth=" + getMaxAttrib(attacker, HEALTH) +
            " attackerAction=" + getAttrib(attacker, ACTION) +
            " attackerMaxAction=" + getMaxAttrib(attacker, ACTION) +
            " attackerMind=" + getAttrib(attacker, MIND) +
            " attackerMaxMind=" + getMaxAttrib(attacker, MIND) +
            " defenderHealth=" + getAttrib(defender, HEALTH) +
            " defenderMaxHealth=" + getMaxAttrib(defender, HEALTH) +
            " defenderAction=" + getAttrib(defender, ACTION) +
            " defenderMaxAction=" + getMaxAttrib(defender, ACTION) +
            " defenderMind=" + getAttrib(defender, MIND) +
            " defenderMaxMind=" + getMaxAttrib(defender, MIND) +
            " defenderHealthWound=" + getAttribWound(defender, HEALTH) +
            " defenderStrengthWound=" + getAttribWound(defender, STRENGTH) +
            " defenderConstitutionWound=" + getAttribWound(defender, CONSTITUTION) +
            " defenderActionWound=" + getAttribWound(defender, ACTION) +
            " defenderQuicknessWound=" + getAttribWound(defender, QUICKNESS) +
            " defenderStaminaWound=" + getAttribWound(defender, STAMINA) +
            " defenderMindWound=" + getAttribWound(defender, MIND) +
            " defenderFocusWound=" + getAttribWound(defender, FOCUS) +
            " defenderWillpowerWound=" + getAttribWound(defender, WILLPOWER) +
            " defenderShockWound=" + getShockWound(defender);
    }

    private boolean isFixtureLightsaberCertified(obj_id defender)
        throws InterruptedException
    {
        return script.library.utils.hasScriptVar(
                defender, "combat.weaponCertified") &&
            hasObjVar(defender, DEFENDER_LIGHTSABER_WEAPON) &&
            script.library.utils.getObjIdScriptVar(
                defender, "combat.weaponCertified") ==
                getObjIdObjVar(defender, DEFENDER_LIGHTSABER_WEAPON);
    }

    private String readDiagnosticString(
        obj_id attacker, String leaf, String fallback) throws InterruptedException
    {
        String objvar = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(attacker, objvar) ? getStringObjVar(attacker, objvar) : fallback;
    }

    private int readDiagnosticInt(
        obj_id attacker, String leaf, int fallback) throws InterruptedException
    {
        String objvar = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(attacker, objvar) ? getIntObjVar(attacker, objvar) : fallback;
    }

    private float readDiagnosticFloat(
        obj_id attacker, String leaf, float fallback) throws InterruptedException
    {
        String objvar = DIAGNOSTIC_ROOT + "." + leaf;
        return hasObjVar(attacker, objvar) ? getFloatObjVar(attacker, objvar) : fallback;
    }
}
