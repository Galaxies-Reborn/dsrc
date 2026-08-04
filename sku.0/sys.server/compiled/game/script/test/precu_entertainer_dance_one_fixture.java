package script.test;

import script.library.performance;
import script.library.skill;
import script.library.utils;
import script.obj_id;

/**
 * Identity-bound reversible fixture for exact Publish 14.1 Entertainer
 * Dance I-IV, Dancer novice, Dancer Ability I-IV, Dancer Wound I-IV,
 * Dancer Shock I-IV, and Dancer Knowledge I-II
 * purchase,
 * real-client performance use, and production surrender.
 */
public class precu_entertainer_dance_one_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String NOVICE =
        "social_entertainer_novice";
    private static final String DANCE_ONE =
        "social_entertainer_dance_01";
    private static final String DANCE_TWO =
        "social_entertainer_dance_02";
    private static final String DANCE_THREE =
        "social_entertainer_dance_03";
    private static final String DANCE_FOUR =
        "social_entertainer_dance_04";
    private static final String HEALING_FOUR =
        "social_entertainer_healing_04";
    private static final String DANCER_NOVICE =
        "social_dancer_novice";
    private static final String DANCER_ABILITY_ONE =
        "social_dancer_ability_01";
    private static final String DANCER_ABILITY_TWO =
        "social_dancer_ability_02";
    private static final String DANCER_ABILITY_THREE =
        "social_dancer_ability_03";
    private static final String DANCER_ABILITY_FOUR =
        "social_dancer_ability_04";
    private static final String DANCER_WOUND_ONE =
        "social_dancer_wound_01";
    private static final String DANCER_WOUND_TWO =
        "social_dancer_wound_02";
    private static final String DANCER_WOUND_THREE =
        "social_dancer_wound_03";
    private static final String DANCER_WOUND_FOUR =
        "social_dancer_wound_04";
    private static final String DANCER_SHOCK_ONE =
        "social_dancer_shock_01";
    private static final String DANCER_SHOCK_TWO =
        "social_dancer_shock_02";
    private static final String DANCER_SHOCK_THREE =
        "social_dancer_shock_03";
    private static final String DANCER_SHOCK_FOUR =
        "social_dancer_shock_04";
    private static final String DANCER_KNOWLEDGE_ONE =
        "social_dancer_knowledge_01";
    private static final String DANCER_KNOWLEDGE_TWO =
        "social_dancer_knowledge_02";
    private static final String DANCER_KNOWLEDGE_THREE =
        "social_dancer_knowledge_03";
    private static final String DANCER_KNOWLEDGE_FOUR =
        "social_dancer_knowledge_04";
    private static final String DANCER_MASTER =
        "social_dancer_master";
    private static final String DANCE_XP = "dance";
    private static final String ENTERTAINER_HEALING_XP =
        "entertainer_healing";
    private static final String BASIC_TWO_ABILITY =
        "startDance+basic2";
    private static final String PRIVATE_ABILITY =
        "private_entertainer_dance_1";
    private static final String RHYTHMIC_TWO_ABILITY =
        "startDance+rhythmic2";
    private static final String PRIVATE_TWO_ABILITY =
        "private_entertainer_dance_2";
    private static final String FOOTLOOSE_ABILITY =
        "startDance+footloose";
    private static final String PRIVATE_THREE_ABILITY =
        "private_entertainer_dance_3";
    private static final String FORMAL_ABILITY =
        "startDance+formal";
    private static final String PRIVATE_FOUR_ABILITY =
        "private_entertainer_dance_4";
    private static final String POPULAR_ABILITY =
        "startDance+popular";
    private static final String POPULAR_TWO_ABILITY =
        "startDance+popular2";
    private static final String TUMBLE_ABILITY =
        "startDance+tumble";
    private static final String NGE_BUNDUKI_TWO_ABILITY =
        "startDance+bunduki2";
    private static final String POPLOCK_TWO_ABILITY =
        "startDance+poplock2";
    private static final String TUMBLE_TWO_ABILITY =
        "startDance+tumble2";
    private static final String LYRICAL_ABILITY =
        "startDance+lyrical";
    private static final String BREAKDANCE_ABILITY =
        "startDance+breakdance";
    private static final String BREAKDANCE_TWO_ABILITY =
        "startDance+breakdance2";
    private static final String EXOTIC_ABILITY =
        "startDance+exotic";
    private static final String EXOTIC_TWO_ABILITY =
        "startDance+exotic2";
    private static final String LYRICAL_TWO_ABILITY =
        "startDance+lyrical2";
    private static final String EXOTIC_THREE_ABILITY =
        "startDance+exotic3";
    private static final String EXOTIC_FOUR_ABILITY =
        "startDance+exotic4";
    private static final String PLACE_CANTINA_ABILITY =
        "place_cantina";
    private static final String PLACE_THEATER_ABILITY =
        "place_theater";
    private static final String POPLOCK_ABILITY =
        "startDance+poplock";
    private static final String REGISTER_ABILITY =
        "registerWithLocation";
    private static final String SPOTLIGHT_ABILITY = "spotlight";
    private static final String COLOR_LIGHTS_ABILITY = "colorlights";
    private static final String DAZZLE_ABILITY = "dazzle";
    private static final String DISTRACT_ABILITY = "distract";
    private static final String NGE_COLOR_SWIRL_ABILITY = "colorSwirl";
    private static final String SMOKE_BOMB_ABILITY = "smokebomb";
    private static final String NGE_CENTER_STAGE_ABILITY = "centerStage";
    private static final String NGE_FLOOR_LIGHTS_ABILITY = "floorLights";
    private static final String NGE_DOUBLE_RIBBON_ABILITY =
        "prop_double_ribbon";
    private static final String NGE_MAGIC_RIBBON_ABILITY =
        "prop_ribbon_magic";
    private static final String NGE_DOUBLE_MAGIC_RIBBON_ABILITY =
        "prop_double_ribbon_magic";
    private static final String NGE_SPARK_RIBBON_ABILITY =
        "prop_ribbon_spark";
    private static final String NGE_DOUBLE_SPARK_RIBBON_ABILITY =
        "prop_double_ribbon_spark";
    private static final String NGE_BUNDUKI_ABILITY =
        "startDance+bunduki";
    private static final String NGE_PROP_RIBBON_ABILITY =
        "prop_ribbon";
    private static final String NGE_PROP_DUAL_ABILITY =
        "prop_dual_wield";
    private static final String NGE_PROP_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_ribbon_r.iff";
    private static final String NGE_DOUBLE_RIBBON_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_double_ribbon_r.iff";
    private static final String NGE_MAGIC_RIBBON_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_ribbon_magic_r.iff";
    private static final String NGE_DOUBLE_MAGIC_RIBBON_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_double_ribbon_magic_r.iff";
    private static final String NGE_SPARK_RIBBON_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_ribbon_spark_r.iff";
    private static final String NGE_DOUBLE_SPARK_RIBBON_SCHEMATIC =
        "object/draft_schematic/dance_prop/prop_double_ribbon_spark_r.iff";
    private static final String DANCE_MOD =
        "healing_dance_ability";
    private static final String DANCE_WOUND_MOD =
        "healing_dance_wound";
    private static final String DANCE_SHOCK_MOD =
        "healing_dance_shock";
    private static final String DANCE_MIND_MOD =
        "healing_dance_mind";
    private static final String MELEE_DEFENSE_MOD = "melee_defense";
    private static final String RANGED_DEFENSE_MOD = "ranged_defense";
    private static final String PRIVATE_PLACE_CANTINA_MOD =
        "private_place_cantina";
    private static final String PRIVATE_PLACE_THEATER_MOD =
        "private_place_theater";
    private static final String PROP_ASSEMBLY_MOD = "prop_assembly";
    private static final int BASIC_TWO_INDEX = 282;
    private static final int RHYTHMIC_TWO_INDEX = 284;
    private static final int FOOTLOOSE_INDEX = 285;
    private static final int FORMAL_INDEX = 287;
    private static final int POPULAR_INDEX = 291;
    private static final int DANCE_ONE_XP_COST = 1000;
    private static final int DANCE_ONE_POINT_COST = 2;
    private static final int DANCE_TWO_XP_COST = 5000;
    private static final int DANCE_TWO_POINT_COST = 3;
    private static final int DANCE_THREE_XP_COST = 15000;
    private static final int DANCE_THREE_POINT_COST = 4;
    private static final int DANCE_FOUR_XP_COST = 45000;
    private static final int DANCE_FOUR_POINT_COST = 5;
    private static final int DANCER_NOVICE_XP_COST = 50000;
    private static final int DANCER_NOVICE_POINT_COST = 6;
    private static final int DANCER_ABILITY_ONE_XP_COST = 87500;
    private static final int DANCER_ABILITY_ONE_POINT_COST = 5;
    private static final int DANCER_ABILITY_TWO_XP_COST = 125000;
    private static final int DANCER_ABILITY_TWO_POINT_COST = 4;
    private static final int DANCER_ABILITY_THREE_XP_COST = 175000;
    private static final int DANCER_ABILITY_THREE_POINT_COST = 3;
    private static final int DANCER_ABILITY_FOUR_XP_COST = 225000;
    private static final int DANCER_ABILITY_FOUR_POINT_COST = 2;
    private static final int DANCER_WOUND_ONE_XP_COST = 25000;
    private static final int DANCER_WOUND_ONE_POINT_COST = 5;
    private static final int DANCER_WOUND_TWO_XP_COST = 50000;
    private static final int DANCER_WOUND_TWO_POINT_COST = 4;
    private static final int DANCER_WOUND_THREE_XP_COST = 100000;
    private static final int DANCER_WOUND_THREE_POINT_COST = 3;
    private static final int DANCER_WOUND_FOUR_XP_COST = 125000;
    private static final int DANCER_WOUND_FOUR_POINT_COST = 2;
    private static final int DANCER_SHOCK_ONE_XP_COST = 25000;
    private static final int DANCER_SHOCK_ONE_POINT_COST = 5;
    private static final int DANCER_SHOCK_TWO_XP_COST = 50000;
    private static final int DANCER_SHOCK_TWO_POINT_COST = 4;
    private static final int DANCER_SHOCK_THREE_XP_COST = 100000;
    private static final int DANCER_SHOCK_THREE_POINT_COST = 3;
    private static final int DANCER_SHOCK_FOUR_XP_COST = 125000;
    private static final int DANCER_SHOCK_FOUR_POINT_COST = 2;
    private static final int DANCER_KNOWLEDGE_ONE_XP_COST = 87500;
    private static final int DANCER_KNOWLEDGE_ONE_POINT_COST = 5;
    private static final int DANCER_KNOWLEDGE_TWO_XP_COST = 125000;
    private static final int DANCER_KNOWLEDGE_TWO_POINT_COST = 4;
    private static final int DANCER_KNOWLEDGE_THREE_XP_COST = 175000;
    private static final int DANCER_KNOWLEDGE_THREE_POINT_COST = 3;
    private static final int DANCER_KNOWLEDGE_FOUR_XP_COST = 225000;
    private static final int DANCER_KNOWLEDGE_FOUR_POINT_COST = 2;
    private static final int DANCER_MASTER_POINT_COST = 1;
    private static final int REFERENCE_QUICKNESS = 400;
    private static final int START_ACTION = 100;
    private static final int FIRST_LOOP_ACTION = 72;
    private static final int FOOTLOOSE_FIRST_LOOP_ACTION = 67;

    private static final String ROOT =
        "precu.entertainerDanceOneFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_DANCE_XP =
        ROOT + ".originalDanceXp";
    private static final String ORIGINAL_HEALING_XP =
        ROOT + ".originalHealingXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String ORIGINAL_ACTION =
        ROOT + ".originalAction";
    private static final String ORIGINAL_QUICKNESS =
        ROOT + ".originalQuickness";
    private static final String ORIGINAL_ACTION_REGEN =
        ROOT + ".originalActionRegen";
    private static final String ORIGINAL_POSTURE =
        ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION =
        ROOT + ".originalLocomotion";
    private static final String BASE_POINTS = ROOT + ".basePoints";
    private static final String BASE_DANCE_MOD =
        ROOT + ".baseDanceMod";
    private static final String BASE_DANCE_WOUND_MOD =
        ROOT + ".baseDanceWoundMod";
    private static final String BASE_DANCE_SHOCK_MOD =
        ROOT + ".baseDanceShockMod";
    private static final String BASE_DANCE_MIND_MOD =
        ROOT + ".baseDanceMindMod";
    private static final String BASE_PROP_ASSEMBLY_MOD =
        ROOT + ".basePropAssemblyMod";
    private static final String BASE_MELEE_DEFENSE_MOD =
        ROOT + ".baseMeleeDefenseMod";
    private static final String BASE_RANGED_DEFENSE_MOD =
        ROOT + ".baseRangedDefenseMod";
    private static final String BASE_PRIVATE_PLACE_CANTINA_MOD =
        ROOT + ".basePrivatePlaceCantinaMod";
    private static final String BASE_PRIVATE_PLACE_THEATER_MOD =
        ROOT + ".basePrivatePlaceTheaterMod";
    private static final String PURCHASED = ROOT + ".purchased";
    private static final String STARTED = ROOT + ".started";
    private static final String STOPPED = ROOT + ".stopped";
    private static final String SURRENDERED = ROOT + ".surrendered";
    private static final String USAGE =
        "usage: prepare|purchase|observeBasicTwoStart|" +
        "observeBasicTwoStop|observeSurrender|prepareTwo|" +
        "purchaseTwo|observeRhythmicTwoStart|" +
        "observeRhythmicTwoStop|observeSurrenderTwo|" +
        "prepareThree|purchaseThree|observeFootlooseStart|" +
        "observeFootlooseStop|observeSurrenderThree|" +
        "prepareFour|purchaseFour|observeFormalStart|" +
        "observeFormalStop|observeSurrenderFour|" +
        "prepareDancerNovice|purchaseDancerNovice|" +
        "observePopularStart|observePopularStop|" +
        "observeSurrenderDancerNovice|" +
        "prepareDancerAbilityOne|purchaseDancerAbilityOne|" +
        "observeSurrenderDancerAbilityOne|" +
        "prepareDancerAbilityTwo|purchaseDancerAbilityTwo|" +
        "observeSurrenderDancerAbilityTwo|" +
        "prepareDancerAbilityThree|purchaseDancerAbilityThree|" +
        "observeSurrenderDancerAbilityThree|" +
        "prepareDancerAbilityFour|purchaseDancerAbilityFour|" +
        "observeSurrenderDancerAbilityFour|" +
        "prepareDancerWoundOne|purchaseDancerWoundOne|" +
        "observeSurrenderDancerWoundOne|" +
        "prepareDancerWoundTwo|purchaseDancerWoundTwo|" +
        "observeSurrenderDancerWoundTwo|" +
        "prepareDancerWoundThree|purchaseDancerWoundThree|" +
        "observeSurrenderDancerWoundThree|" +
        "prepareDancerWoundFour|purchaseDancerWoundFour|" +
        "observeSurrenderDancerWoundFour|" +
        "prepareDancerShockOne|purchaseDancerShockOne|" +
        "observeSurrenderDancerShockOne|" +
        "prepareDancerShockTwo|purchaseDancerShockTwo|" +
        "observeSurrenderDancerShockTwo|" +
        "prepareDancerShockThree|purchaseDancerShockThree|" +
        "observeSurrenderDancerShockThree|" +
        "prepareDancerShockFour|purchaseDancerShockFour|" +
        "observeSurrenderDancerShockFour|" +
        "prepareDancerKnowledgeOne|purchaseDancerKnowledgeOne|" +
        "observeSurrenderDancerKnowledgeOne|" +
        "prepareDancerKnowledgeTwo|purchaseDancerKnowledgeTwo|" +
        "observeSurrenderDancerKnowledgeTwo|" +
        "prepareDancerKnowledgeThree|purchaseDancerKnowledgeThree|" +
        "observeSurrenderDancerKnowledgeThree|" +
        "prepareDancerKnowledgeFour|purchaseDancerKnowledgeFour|" +
        "observeSurrenderDancerKnowledgeFour|" +
        "prepareDancerMaster|purchaseDancerMaster|" +
        "observeSurrenderDancerMaster|" +
        "status|cleanup " +
        "<playerOid> <lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
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
        if (playerValue != PLAYER_OID ||
            !isValidLifecycle(args[2]))
        {
            return "error=identityNotAllowed";
        }

        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded())
        {
            return "error=playerNotLoaded";
        }
        if (!player.isAuthoritative() || !isPlayer(player) ||
            getPlayerStationId(player) != PLAYER_STATION_ID)
        {
            return "error=playerNotAuthoritative";
        }

        String action = args[0];
        if (action.equalsIgnoreCase("prepare"))
        {
            return prepare(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareTwo"))
        {
            return prepareTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareThree"))
        {
            return prepareThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareFour"))
        {
            return prepareFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerNovice"))
        {
            return prepareDancerNovice(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerAbilityOne"))
        {
            return prepareDancerAbilityOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerAbilityTwo"))
        {
            return prepareDancerAbilityTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerAbilityThree"))
        {
            return prepareDancerAbilityThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerAbilityFour"))
        {
            return prepareDancerAbilityFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerWoundOne"))
        {
            return prepareDancerWoundOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerWoundTwo"))
        {
            return prepareDancerWoundTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerWoundThree"))
        {
            return prepareDancerWoundThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerWoundFour"))
        {
            return prepareDancerWoundFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerShockOne"))
        {
            return prepareDancerShockOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerShockTwo"))
        {
            return prepareDancerShockTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerShockThree"))
        {
            return prepareDancerShockThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerShockFour"))
        {
            return prepareDancerShockFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerKnowledgeOne"))
        {
            return prepareDancerKnowledgeOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerKnowledgeTwo"))
        {
            return prepareDancerKnowledgeTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerKnowledgeThree"))
        {
            return prepareDancerKnowledgeThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerKnowledgeFour"))
        {
            return prepareDancerKnowledgeFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareDancerMaster"))
        {
            return prepareDancerMaster(player, args[2]);
        }
        if (action.equalsIgnoreCase("cleanup") &&
            !hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        String ownership = validateOwnership(player, args[2]);
        if (ownership != null)
        {
            return ownership;
        }
        if (action.equalsIgnoreCase("purchase"))
        {
            return purchase(player);
        }
        if (action.equalsIgnoreCase("purchaseTwo"))
        {
            return purchaseTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseThree"))
        {
            return purchaseThree(player);
        }
        if (action.equalsIgnoreCase("purchaseFour"))
        {
            return purchaseFour(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerNovice"))
        {
            return purchaseDancerNovice(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerAbilityOne"))
        {
            return purchaseDancerAbilityOne(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerAbilityTwo"))
        {
            return purchaseDancerAbilityTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerAbilityThree"))
        {
            return purchaseDancerAbilityThree(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerAbilityFour"))
        {
            return purchaseDancerAbilityFour(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerWoundOne"))
        {
            return purchaseDancerWoundOne(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerWoundTwo"))
        {
            return purchaseDancerWoundTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerWoundThree"))
        {
            return purchaseDancerWoundThree(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerWoundFour"))
        {
            return purchaseDancerWoundFour(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerShockOne"))
        {
            return purchaseDancerShockOne(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerShockTwo"))
        {
            return purchaseDancerShockTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerShockThree"))
        {
            return purchaseDancerShockThree(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerShockFour"))
        {
            return purchaseDancerShockFour(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerKnowledgeOne"))
        {
            return purchaseDancerKnowledgeOne(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerKnowledgeTwo"))
        {
            return purchaseDancerKnowledgeTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerKnowledgeThree"))
        {
            return purchaseDancerKnowledgeThree(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerKnowledgeFour"))
        {
            return purchaseDancerKnowledgeFour(player);
        }
        if (action.equalsIgnoreCase("purchaseDancerMaster"))
        {
            return purchaseDancerMaster(player);
        }
        if (action.equalsIgnoreCase("observeBasicTwoStart"))
        {
            return observeBasicTwoStart(player);
        }
        if (action.equalsIgnoreCase("observeBasicTwoStop"))
        {
            return observeBasicTwoStop(player);
        }
        if (action.equalsIgnoreCase("observeSurrender"))
        {
            return observeSurrender(player);
        }
        if (action.equalsIgnoreCase("observeRhythmicTwoStart"))
        {
            return observeRhythmicTwoStart(player);
        }
        if (action.equalsIgnoreCase("observeRhythmicTwoStop"))
        {
            return observeRhythmicTwoStop(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderTwo"))
        {
            return observeSurrenderTwo(player);
        }
        if (action.equalsIgnoreCase("observeFootlooseStart"))
        {
            return observeFootlooseStart(player);
        }
        if (action.equalsIgnoreCase("observeFootlooseStop"))
        {
            return observeFootlooseStop(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderThree"))
        {
            return observeSurrenderThree(player);
        }
        if (action.equalsIgnoreCase("observeFormalStart"))
        {
            return observeFormalStart(player);
        }
        if (action.equalsIgnoreCase("observeFormalStop"))
        {
            return observeFormalStop(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderFour"))
        {
            return observeSurrenderFour(player);
        }
        if (action.equalsIgnoreCase("observePopularStart"))
        {
            return observePopularStart(player);
        }
        if (action.equalsIgnoreCase("observePopularStop"))
        {
            return observePopularStop(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerNovice"))
        {
            return observeSurrenderDancerNovice(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerAbilityOne"))
        {
            return observeSurrenderDancerAbilityOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerAbilityTwo"))
        {
            return observeSurrenderDancerAbilityTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerAbilityThree"))
        {
            return observeSurrenderDancerAbilityThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerAbilityFour"))
        {
            return observeSurrenderDancerAbilityFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerWoundOne"))
        {
            return observeSurrenderDancerWoundOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerWoundTwo"))
        {
            return observeSurrenderDancerWoundTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerWoundThree"))
        {
            return observeSurrenderDancerWoundThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerWoundFour"))
        {
            return observeSurrenderDancerWoundFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerShockOne"))
        {
            return observeSurrenderDancerShockOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerShockTwo"))
        {
            return observeSurrenderDancerShockTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerShockThree"))
        {
            return observeSurrenderDancerShockThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerShockFour"))
        {
            return observeSurrenderDancerShockFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerKnowledgeOne"))
        {
            return observeSurrenderDancerKnowledgeOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerKnowledgeTwo"))
        {
            return observeSurrenderDancerKnowledgeTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerKnowledgeThree"))
        {
            return observeSurrenderDancerKnowledgeThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderDancerKnowledgeFour"))
        {
            return observeSurrenderDancerKnowledgeFour(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderDancerMaster"))
        {
            return observeSurrenderDancerMaster(player);
        }
        if (action.equalsIgnoreCase("status"))
        {
            return "action=status " + buildStatus(player);
        }
        if (action.equalsIgnoreCase("cleanup"))
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
            return ownership == null
                ? "action=prepare resumed=true " + buildStatus(player)
                : ownership;
        }
        if (hasSkill(player, NOVICE) ||
            hasSkill(player, DANCE_ONE) ||
            hasSkill(player, DANCE_TWO) ||
            hasSkill(player, DANCE_THREE) ||
            hasSkill(player, DANCE_FOUR) ||
            hasSkill(player, HEALING_FOUR) ||
            hasSkill(player, DANCER_NOVICE) ||
            hasSkill(player, DANCER_ABILITY_ONE) ||
            hasSkill(player, DANCER_ABILITY_TWO) ||
            hasSkill(player, DANCER_ABILITY_THREE) ||
            hasSkill(player, DANCER_ABILITY_FOUR) ||
            hasSkill(player, DANCER_WOUND_ONE) ||
            hasSkill(player, DANCER_WOUND_TWO) ||
            hasSkill(player, DANCER_WOUND_THREE) ||
            hasSkill(player, DANCER_WOUND_FOUR) ||
            hasSkill(player, DANCER_SHOCK_ONE) ||
            hasSkill(player, DANCER_SHOCK_TWO) ||
            hasSkill(player, DANCER_SHOCK_THREE) ||
            hasSkill(player, DANCER_SHOCK_FOUR) ||
            hasSkill(player, DANCER_KNOWLEDGE_ONE) ||
            hasSkill(player, DANCER_KNOWLEDGE_TWO) ||
            hasSkill(player, DANCER_KNOWLEDGE_THREE) ||
            hasSkill(player, DANCER_KNOWLEDGE_FOUR) ||
            hasSkill(player, DANCER_MASTER) ||
            hasCommand(player, BASIC_TWO_ABILITY) ||
            hasCommand(player, PRIVATE_ABILITY) ||
            hasCommand(player, RHYTHMIC_TWO_ABILITY) ||
            hasCommand(player, PRIVATE_TWO_ABILITY) ||
            hasCommand(player, FOOTLOOSE_ABILITY) ||
            hasCommand(player, PRIVATE_THREE_ABILITY) ||
            hasCommand(player, FORMAL_ABILITY) ||
            hasCommand(player, PRIVATE_FOUR_ABILITY) ||
            hasCommand(player, POPULAR_ABILITY) ||
            hasCommand(player, POPLOCK_ABILITY) ||
            hasCommand(player, REGISTER_ABILITY) ||
            hasCommand(player, SPOTLIGHT_ABILITY) ||
            hasCommand(player, COLOR_LIGHTS_ABILITY) ||
            hasCommand(player, DAZZLE_ABILITY) ||
            hasCommand(player, DISTRACT_ABILITY) ||
            hasCommand(player, NGE_COLOR_SWIRL_ABILITY) ||
            hasCommand(player, SMOKE_BOMB_ABILITY) ||
            hasCommand(player, NGE_CENTER_STAGE_ABILITY) ||
            hasCommand(player, NGE_FLOOR_LIGHTS_ABILITY))
        {
            return "error=fixtureRequiresUntrainedEntertainer";
        }
        if (getPerformanceType(player) != 0 ||
            hasScript(player, performance.DANCE_HEARTBEAT_SCRIPT) ||
            hasObjVar(player, performance.VAR_PERFORM) ||
            hasObjVar(
                player,
                performance.VAR_PERFORM_NO_GROUP_DANCE) ||
            isIdValid(getGroupObject(player)) ||
            getInstrumentAudioId(player) != 0)
        {
            return "error=fixtureRequiresIdleUngroupedPlayer";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_DANCE_XP,
            getExperiencePoints(player, DANCE_XP));
        setObjVar(
            player,
            ORIGINAL_HEALING_XP,
            getExperiencePoints(player, ENTERTAINER_HEALING_XP));
        setObjVar(
            player,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(player, ORIGINAL_ACTION, getAttrib(player, ACTION));
        setObjVar(
            player,
            ORIGINAL_QUICKNESS,
            getAttrib(player, QUICKNESS));
        setObjVar(
            player,
            ORIGINAL_ACTION_REGEN,
            getRegenRate(player, ACTION));
        setObjVar(player, ORIGINAL_POSTURE, getPosture(player));
        setObjVar(
            player,
            ORIGINAL_LOCOMOTION,
            getLocomotion(player));
        resetTelemetry(player);

        stopCombat(player);
        setCombatTarget(player, obj_id.NULL_ID);
        setRegenRate(player, ACTION, 0.0f);
        boolean standing =
            setLocomotion(player, LOCOMOTION_STANDING) &&
            setPostureClientImmediate(player, POSTURE_UPRIGHT);
        boolean noviceGranted =
            skill.grantSkillToPlayer(player, NOVICE) &&
            hasSkill(player, NOVICE);
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, DANCE_ONE_XP_COST);
        boolean quicknessReady =
            setExactAttribute(
                player,
                QUICKNESS,
                REFERENCE_QUICKNESS);
        boolean actionReady =
            setExactAttribute(player, ACTION, START_ACTION);
        setObjVar(
            player,
            performance.VAR_PERFORM_NO_GROUP_DANCE,
            true);

        boolean prepared =
            standing &&
            noviceGranted &&
            xpReady &&
            quicknessReady &&
            actionReady &&
            !hasSkill(player, DANCE_ONE) &&
            !hasCommand(player, BASIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) - 15;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepare resumed=false " +
            buildStatus(player);
    }

    private String prepareTwo(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String preparedOne = prepare(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, DANCE_TWO))
        {
            return "action=prepareTwo resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, DANCE_ONE) ||
            (skill.grantSkillToPlayer(player, DANCE_ONE) &&
                hasSkill(player, DANCE_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, DANCE_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, DANCE_ONE) &&
            !hasSkill(player, DANCE_TWO) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            !hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureTwoSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareTwo resumed=false " +
            buildStatus(player);
    }

    private String purchase(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchase resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCE_ONE,
                DANCE_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCE_ONE) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 5 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 10000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCE_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchase passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String prepareThree(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String preparedTwo = prepareTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, DANCE_THREE))
        {
            return "action=prepareThree resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, DANCE_TWO) ||
            (skill.grantSkillToPlayer(player, DANCE_TWO) &&
                hasSkill(player, DANCE_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, DANCE_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, DANCE_ONE) &&
            hasSkill(player, DANCE_TWO) &&
            !hasSkill(player, DANCE_THREE) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            !hasCommand(player, FOOTLOOSE_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureThreeSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareThree resumed=false " +
            buildStatus(player);
    }

    private String prepareFour(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String preparedThree = prepareThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, DANCE_FOUR))
        {
            return "action=prepareFour resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, DANCE_THREE) ||
            (skill.grantSkillToPlayer(player, DANCE_THREE) &&
                hasSkill(player, DANCE_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, DANCE_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, DANCE_ONE) &&
            hasSkill(player, DANCE_TWO) &&
            hasSkill(player, DANCE_THREE) &&
            !hasSkill(player, DANCE_FOUR) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasCommand(player, FOOTLOOSE_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            !hasCommand(player, FORMAL_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureFourSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareFour resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerNovice(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedFour = prepareFour(player, lifecycle);
        if (preparedFour.startsWith("error="))
        {
            return preparedFour;
        }
        if (hasSkill(player, DANCER_NOVICE))
        {
            return "action=prepareDancerNovice resumed=true " +
                buildStatus(player);
        }

        boolean danceReady =
            hasSkill(player, DANCE_FOUR) ||
            (skill.grantSkillToPlayer(player, DANCE_FOUR) &&
                hasSkill(player, DANCE_FOUR));
        boolean healingReady =
            hasSkill(player, HEALING_FOUR) ||
            (skill.grantSkillToPlayer(player, HEALING_FOUR) &&
                hasSkill(player, HEALING_FOUR));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, DANCER_NOVICE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            danceReady &&
            healingReady &&
            xpReady &&
            hasSkill(player, DANCE_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, FORMAL_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, POPULAR_ABILITY) &&
            !hasCommand(player, POPLOCK_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_ABILITY) &&
            !hasCommand(player, NGE_PROP_RIBBON_ABILITY) &&
            !hasCommand(player, NGE_PROP_DUAL_ABILITY) &&
            !hasSchematic(player, NGE_PROP_SCHEMATIC) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareDancerNovice resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerAbilityOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareDancerNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, DANCER_ABILITY_ONE))
        {
            return "action=prepareDancerAbilityOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, DANCER_NOVICE) ||
            (skill.grantSkillToPlayer(player, DANCER_NOVICE) &&
                hasSkill(player, DANCER_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_ABILITY_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasSkill(player, DANCER_ABILITY_ONE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerAbilityOneSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerAbilityOne resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerAbilityTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedAbilityOne =
            prepareDancerAbilityOne(player, lifecycle);
        if (preparedAbilityOne.startsWith("error="))
        {
            return preparedAbilityOne;
        }
        if (hasSkill(player, DANCER_ABILITY_TWO))
        {
            return "action=prepareDancerAbilityTwo resumed=true " +
                buildStatus(player);
        }

        boolean abilityOneReady =
            hasSkill(player, DANCER_ABILITY_ONE) ||
            (skill.grantSkillToPlayer(
                player,
                DANCER_ABILITY_ONE) &&
                hasSkill(player, DANCER_ABILITY_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_ABILITY_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            abilityOneReady &&
            xpReady &&
            hasSkill(player, DANCER_NOVICE) &&
            hasSkill(player, DANCER_ABILITY_ONE) &&
            !hasSkill(player, DANCER_ABILITY_TWO) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            !hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_ABILITY_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerAbilityTwoSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerAbilityTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerAbilityThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedAbilityTwo =
            prepareDancerAbilityTwo(player, lifecycle);
        if (preparedAbilityTwo.startsWith("error="))
        {
            return preparedAbilityTwo;
        }
        if (hasSkill(player, DANCER_ABILITY_THREE))
        {
            return "action=prepareDancerAbilityThree resumed=true " +
                buildStatus(player);
        }

        boolean abilityTwoReady =
            hasSkill(player, DANCER_ABILITY_TWO) ||
            (skill.grantSkillToPlayer(
                player,
                DANCER_ABILITY_TWO) &&
                hasSkill(player, DANCER_ABILITY_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_ABILITY_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            abilityTwoReady &&
            xpReady &&
            hasSkill(player, DANCER_ABILITY_ONE) &&
            hasSkill(player, DANCER_ABILITY_TWO) &&
            !hasSkill(player, DANCER_ABILITY_THREE) &&
            hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            !hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_ABILITY_ONE_POINT_COST -
                    DANCER_ABILITY_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerAbilityThreeSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerAbilityThree resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerAbilityFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedAbilityThree =
            prepareDancerAbilityThree(player, lifecycle);
        if (preparedAbilityThree.startsWith("error="))
        {
            return preparedAbilityThree;
        }
        if (hasSkill(player, DANCER_ABILITY_FOUR))
        {
            return "action=prepareDancerAbilityFour resumed=true " +
                buildStatus(player);
        }

        boolean abilityThreeReady =
            hasSkill(player, DANCER_ABILITY_THREE) ||
            (skill.grantSkillToPlayer(
                player,
                DANCER_ABILITY_THREE) &&
                hasSkill(player, DANCER_ABILITY_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_ABILITY_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            abilityThreeReady &&
            xpReady &&
            hasSkill(player, DANCER_ABILITY_TWO) &&
            hasSkill(player, DANCER_ABILITY_THREE) &&
            !hasSkill(player, DANCER_ABILITY_FOUR) &&
            hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            !hasCommand(player, NGE_FLOOR_LIGHTS_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_ABILITY_ONE_POINT_COST -
                    DANCER_ABILITY_TWO_POINT_COST -
                    DANCER_ABILITY_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerAbilityFourSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerAbilityFour resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerWoundOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareDancerNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, DANCER_WOUND_ONE))
        {
            return "action=prepareDancerWoundOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, DANCER_NOVICE) ||
            (skill.grantSkillToPlayer(player, DANCER_NOVICE) &&
                hasSkill(player, DANCER_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_WOUND_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasSkill(player, DANCER_WOUND_ONE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerWoundOneSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerWoundOne resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerWoundTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareDancerWoundOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, DANCER_WOUND_TWO))
        {
            return "action=prepareDancerWoundTwo resumed=true " +
                buildStatus(player);
        }

        boolean oneReady =
            hasSkill(player, DANCER_WOUND_ONE) ||
            (skill.grantSkillToPlayer(player, DANCER_WOUND_ONE) &&
                hasSkill(player, DANCER_WOUND_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_WOUND_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            oneReady &&
            xpReady &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            !hasSkill(player, DANCER_WOUND_TWO) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_WOUND_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerWoundTwoSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerWoundTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerWoundThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareDancerWoundTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, DANCER_WOUND_THREE))
        {
            return "action=prepareDancerWoundThree resumed=true " +
                buildStatus(player);
        }

        boolean twoReady =
            hasSkill(player, DANCER_WOUND_TWO) ||
            (skill.grantSkillToPlayer(player, DANCER_WOUND_TWO) &&
                hasSkill(player, DANCER_WOUND_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_WOUND_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            twoReady &&
            xpReady &&
            hasSkill(player, DANCER_WOUND_TWO) &&
            !hasSkill(player, DANCER_WOUND_THREE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_WOUND_ONE_POINT_COST -
                    DANCER_WOUND_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerWoundThreeSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerWoundThree resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerWoundFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareDancerWoundThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, DANCER_WOUND_FOUR))
        {
            return "action=prepareDancerWoundFour resumed=true " +
                buildStatus(player);
        }

        boolean threeReady =
            hasSkill(player, DANCER_WOUND_THREE) ||
            (skill.grantSkillToPlayer(player, DANCER_WOUND_THREE) &&
                hasSkill(player, DANCER_WOUND_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_WOUND_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            threeReady &&
            xpReady &&
            hasSkill(player, DANCER_WOUND_THREE) &&
            !hasSkill(player, DANCER_WOUND_FOUR) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_WOUND_ONE_POINT_COST -
                    DANCER_WOUND_TWO_POINT_COST -
                    DANCER_WOUND_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerWoundFourSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerWoundFour resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerShockOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareDancerNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, DANCER_SHOCK_ONE))
        {
            return "action=prepareDancerShockOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, DANCER_NOVICE) ||
            (skill.grantSkillToPlayer(player, DANCER_NOVICE) &&
                hasSkill(player, DANCER_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        setObjVar(
            player,
            BASE_PROP_ASSEMBLY_MOD,
            getSkillStatMod(player, PROP_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_SHOCK_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasSkill(player, DANCER_SHOCK_ONE) &&
            !hasCommand(player, NGE_DOUBLE_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_RIBBON_SCHEMATIC) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerShockOneSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerShockOne resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerShockTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareDancerShockOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, DANCER_SHOCK_TWO))
        {
            return "action=prepareDancerShockTwo resumed=true " +
                buildStatus(player);
        }

        boolean oneReady =
            hasSkill(player, DANCER_SHOCK_ONE) ||
            (skill.grantSkillToPlayer(player, DANCER_SHOCK_ONE) &&
                hasSkill(player, DANCER_SHOCK_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        setObjVar(
            player,
            BASE_PROP_ASSEMBLY_MOD,
            getSkillStatMod(player, PROP_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_SHOCK_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            oneReady &&
            xpReady &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            !hasSkill(player, DANCER_SHOCK_TWO) &&
            !hasCommand(player, NGE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_MAGIC_RIBBON_SCHEMATIC) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_SHOCK_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerShockTwoSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerShockTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerShockThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareDancerShockTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, DANCER_SHOCK_THREE))
        {
            return "action=prepareDancerShockThree resumed=true " +
                buildStatus(player);
        }

        boolean twoReady =
            hasSkill(player, DANCER_SHOCK_TWO) ||
            (skill.grantSkillToPlayer(player, DANCER_SHOCK_TWO) &&
                hasSkill(player, DANCER_SHOCK_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        setObjVar(
            player,
            BASE_PROP_ASSEMBLY_MOD,
            getSkillStatMod(player, PROP_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_SHOCK_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            twoReady &&
            xpReady &&
            hasSkill(player, DANCER_SHOCK_TWO) &&
            !hasSkill(player, DANCER_SHOCK_THREE) &&
            !hasCommand(player, NGE_DOUBLE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(
                player,
                NGE_DOUBLE_MAGIC_RIBBON_SCHEMATIC) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_SHOCK_ONE_POINT_COST -
                    DANCER_SHOCK_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerShockThreeSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerShockThree resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerShockFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareDancerShockThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, DANCER_SHOCK_FOUR))
        {
            return "action=prepareDancerShockFour resumed=true " +
                buildStatus(player);
        }

        boolean threeReady =
            hasSkill(player, DANCER_SHOCK_THREE) ||
            (skill.grantSkillToPlayer(player, DANCER_SHOCK_THREE) &&
                hasSkill(player, DANCER_SHOCK_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        setObjVar(
            player,
            BASE_PROP_ASSEMBLY_MOD,
            getSkillStatMod(player, PROP_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                DANCER_SHOCK_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            threeReady &&
            xpReady &&
            hasSkill(player, DANCER_SHOCK_THREE) &&
            !hasSkill(player, DANCER_SHOCK_FOUR) &&
            !hasCommand(player, NGE_SPARK_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_SPARK_RIBBON_SCHEMATIC) &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_SHOCK_ONE_POINT_COST -
                    DANCER_SHOCK_TWO_POINT_COST -
                    DANCER_SHOCK_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerShockFourSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerShockFour resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerKnowledgeOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareDancerNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_ONE))
        {
            return "action=prepareDancerKnowledgeOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, DANCER_NOVICE) ||
            (skill.grantSkillToPlayer(player, DANCER_NOVICE) &&
                hasSkill(player, DANCER_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_KNOWLEDGE_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            !hasCommand(player, POPULAR_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_TWO_ABILITY) &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerKnowledgeOneSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerKnowledgeOne resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerKnowledgeTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareDancerKnowledgeOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_TWO))
        {
            return "action=prepareDancerKnowledgeTwo resumed=true " +
                buildStatus(player);
        }

        boolean oneReady =
            hasSkill(player, DANCER_KNOWLEDGE_ONE) ||
            (skill.grantSkillToPlayer(player, DANCER_KNOWLEDGE_ONE) &&
                hasSkill(player, DANCER_KNOWLEDGE_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_KNOWLEDGE_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            oneReady &&
            xpReady &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            !hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasCommand(player, POPULAR_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_ABILITY) &&
            !hasCommand(player, POPLOCK_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_TWO_ABILITY) &&
            getExperienceCap(player, DANCE_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_KNOWLEDGE_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerKnowledgeTwoSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerKnowledgeTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerKnowledgeThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareDancerKnowledgeTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_THREE))
        {
            return "action=prepareDancerKnowledgeThree resumed=true " +
                buildStatus(player);
        }

        boolean twoReady =
            hasSkill(player, DANCER_KNOWLEDGE_TWO) ||
            (skill.grantSkillToPlayer(player, DANCER_KNOWLEDGE_TWO) &&
                hasSkill(player, DANCER_KNOWLEDGE_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_KNOWLEDGE_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            twoReady &&
            xpReady &&
            hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            !hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            hasCommand(player, POPLOCK_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_TWO_ABILITY) &&
            !hasCommand(player, LYRICAL_ABILITY) &&
            !hasCommand(player, BREAKDANCE_ABILITY) &&
            getExperienceCap(player, DANCE_XP) == 700000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_KNOWLEDGE_ONE_POINT_COST -
                    DANCER_KNOWLEDGE_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerKnowledgeThreeSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerKnowledgeThree resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerKnowledgeFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareDancerKnowledgeThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_FOUR))
        {
            return "action=prepareDancerKnowledgeFour resumed=true " +
                buildStatus(player);
        }

        boolean threeReady =
            hasSkill(player, DANCER_KNOWLEDGE_THREE) ||
            (skill.grantSkillToPlayer(player, DANCER_KNOWLEDGE_THREE) &&
                hasSkill(player, DANCER_KNOWLEDGE_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        boolean xpReady =
            setXpExact(
                player,
                DANCE_XP,
                DANCER_KNOWLEDGE_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            threeReady &&
            xpReady &&
            hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            !hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasCommand(player, LYRICAL_ABILITY) &&
            hasCommand(player, BREAKDANCE_ABILITY) &&
            !hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_ABILITY) &&
            !hasCommand(player, EXOTIC_TWO_ABILITY) &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_KNOWLEDGE_ONE_POINT_COST -
                    DANCER_KNOWLEDGE_TWO_POINT_COST -
                    DANCER_KNOWLEDGE_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerKnowledgeFourSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerKnowledgeFour resumed=false " +
            buildStatus(player);
    }

    private String prepareDancerMaster(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedKnowledge =
            prepareDancerKnowledgeFour(player, lifecycle);
        if (preparedKnowledge.startsWith("error="))
        {
            return preparedKnowledge;
        }
        if (hasSkill(player, DANCER_MASTER))
        {
            return "action=prepareDancerMaster resumed=true " +
                buildStatus(player);
        }

        boolean knowledgeReady =
            grantSkillIfMissing(player, DANCER_KNOWLEDGE_FOUR);
        boolean abilityReady =
            grantSkillIfMissing(player, DANCER_ABILITY_ONE) &&
            grantSkillIfMissing(player, DANCER_ABILITY_TWO) &&
            grantSkillIfMissing(player, DANCER_ABILITY_THREE) &&
            grantSkillIfMissing(player, DANCER_ABILITY_FOUR);
        boolean woundReady =
            grantSkillIfMissing(player, DANCER_WOUND_ONE) &&
            grantSkillIfMissing(player, DANCER_WOUND_TWO) &&
            grantSkillIfMissing(player, DANCER_WOUND_THREE) &&
            grantSkillIfMissing(player, DANCER_WOUND_FOUR);
        boolean shockReady =
            grantSkillIfMissing(player, DANCER_SHOCK_ONE) &&
            grantSkillIfMissing(player, DANCER_SHOCK_TWO) &&
            grantSkillIfMissing(player, DANCER_SHOCK_THREE) &&
            grantSkillIfMissing(player, DANCER_SHOCK_FOUR);

        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_SHOCK_MOD,
            getSkillStatMod(player, DANCE_SHOCK_MOD));
        setObjVar(
            player,
            BASE_DANCE_MIND_MOD,
            getSkillStatMod(player, DANCE_MIND_MOD));
        setObjVar(
            player,
            BASE_MELEE_DEFENSE_MOD,
            getSkillStatMod(player, MELEE_DEFENSE_MOD));
        setObjVar(
            player,
            BASE_RANGED_DEFENSE_MOD,
            getSkillStatMod(player, RANGED_DEFENSE_MOD));
        setObjVar(
            player,
            BASE_PRIVATE_PLACE_CANTINA_MOD,
            getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD));
        setObjVar(
            player,
            BASE_PRIVATE_PLACE_THEATER_MOD,
            getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD));
        setObjVar(
            player,
            BASE_PROP_ASSEMBLY_MOD,
            getSkillStatMod(player, PROP_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(player, DANCE_XP, 0) &&
            setXpExact(player, ENTERTAINER_HEALING_XP, 0);
        resetTelemetry(player);

        boolean prepared =
            knowledgeReady &&
            abilityReady &&
            woundReady &&
            shockReady &&
            xpReady &&
            hasSkill(player, DANCER_ABILITY_FOUR) &&
            hasSkill(player, DANCER_WOUND_FOUR) &&
            hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasSkill(player, DANCER_SHOCK_FOUR) &&
            !hasSkill(player, DANCER_MASTER) &&
            hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            hasCommand(player, EXOTIC_ABILITY) &&
            hasCommand(player, EXOTIC_TWO_ABILITY) &&
            !hasCommand(player, LYRICAL_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_THREE_ABILITY) &&
            !hasCommand(player, EXOTIC_FOUR_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_SPARK_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_SPARK_RIBBON_SCHEMATIC) &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - DANCE_ONE_POINT_COST -
                    DANCE_TWO_POINT_COST -
                    DANCE_THREE_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCE_FOUR_POINT_COST -
                    DANCER_NOVICE_POINT_COST -
                    DANCER_ABILITY_ONE_POINT_COST -
                    DANCER_ABILITY_TWO_POINT_COST -
                    DANCER_ABILITY_THREE_POINT_COST -
                    DANCER_ABILITY_FOUR_POINT_COST -
                    DANCER_WOUND_ONE_POINT_COST -
                    DANCER_WOUND_TWO_POINT_COST -
                    DANCER_WOUND_THREE_POINT_COST -
                    DANCER_WOUND_FOUR_POINT_COST -
                    DANCER_KNOWLEDGE_ONE_POINT_COST -
                    DANCER_KNOWLEDGE_TWO_POINT_COST -
                    DANCER_KNOWLEDGE_THREE_POINT_COST -
                    DANCER_KNOWLEDGE_FOUR_POINT_COST -
                    DANCER_SHOCK_ONE_POINT_COST -
                    DANCER_SHOCK_TWO_POINT_COST -
                    DANCER_SHOCK_THREE_POINT_COST -
                    DANCER_SHOCK_FOUR_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureDancerMasterSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareDancerMaster resumed=false " +
            buildStatus(player);
    }

    private String purchaseTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCE_TWO,
                DANCE_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCE_TWO) &&
            hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 5 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCE_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCE_THREE,
                DANCE_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCE_THREE) &&
            hasCommand(player, FOOTLOOSE_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 5 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 90000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCE_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCE_FOUR,
                DANCE_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCE_FOUR) &&
            hasCommand(player, FORMAL_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 150000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCE_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerNovice(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerNovice resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_NOVICE,
                DANCER_NOVICE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_ABILITY) &&
            !hasCommand(player, NGE_PROP_RIBBON_ABILITY) &&
            !hasCommand(player, NGE_PROP_DUAL_ABILITY) &&
            !hasSchematic(player, NGE_PROP_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 5 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 10 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_NOVICE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerNovice passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerAbilityOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerAbilityOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_ABILITY_ONE,
                DANCER_ABILITY_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_ABILITY_ONE) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 10 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_ABILITY_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerAbilityOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerAbilityTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerAbilityTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_ABILITY_TWO,
                DANCER_ABILITY_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_ABILITY_TWO) &&
            hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 10 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 700000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_ABILITY_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerAbilityTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerAbilityThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerAbilityThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_ABILITY_THREE,
                DANCER_ABILITY_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_ABILITY_THREE) &&
            hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 20 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_ABILITY_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerAbilityThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerAbilityFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerAbilityFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_ABILITY_FOUR,
                DANCER_ABILITY_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_ABILITY_FOUR) &&
            !hasCommand(player, NGE_FLOOR_LIGHTS_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 25 &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_ABILITY_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerAbilityFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerWoundOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerWoundOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_WOUND_ONE,
                DANCER_WOUND_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 5 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_WOUND_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerWoundOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerWoundTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerWoundTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_WOUND_TWO,
                DANCER_WOUND_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_WOUND_TWO) &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 10 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_WOUND_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerWoundTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerWoundThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerWoundThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_WOUND_THREE,
                DANCER_WOUND_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_WOUND_THREE) &&
            hasSkill(player, DANCER_WOUND_TWO) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 10 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_WOUND_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerWoundThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerWoundFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerWoundFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_WOUND_FOUR,
                DANCER_WOUND_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_WOUND_FOUR) &&
            hasSkill(player, DANCER_WOUND_THREE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 15 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_WOUND_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerWoundFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerShockOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerShockOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_SHOCK_ONE,
                DANCER_SHOCK_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            !hasCommand(player, NGE_DOUBLE_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_RIBBON_SCHEMATIC) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_SHOCK_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerShockOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerShockTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerShockTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_SHOCK_TWO,
                DANCER_SHOCK_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_SHOCK_TWO) &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            !hasCommand(player, NGE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_MAGIC_RIBBON_SCHEMATIC) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_SHOCK_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerShockTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerShockThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerShockThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_SHOCK_THREE,
                DANCER_SHOCK_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_SHOCK_THREE) &&
            hasSkill(player, DANCER_SHOCK_TWO) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 20 &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            !hasCommand(player, NGE_DOUBLE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(
                player,
                NGE_DOUBLE_MAGIC_RIBBON_SCHEMATIC) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_SHOCK_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerShockThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerShockFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerShockFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_SHOCK_FOUR,
                DANCER_SHOCK_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_SHOCK_FOUR) &&
            hasSkill(player, DANCER_SHOCK_THREE) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 25 &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            !hasCommand(player, NGE_SPARK_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_SPARK_RIBBON_SCHEMATIC) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_SHOCK_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerShockFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerKnowledgeOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerKnowledgeOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_KNOWLEDGE_ONE,
                DANCER_KNOWLEDGE_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_TWO_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_KNOWLEDGE_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerKnowledgeOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerKnowledgeTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerKnowledgeTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_KNOWLEDGE_TWO,
                DANCER_KNOWLEDGE_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasCommand(player, POPLOCK_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_TWO_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 700000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_KNOWLEDGE_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerKnowledgeTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerKnowledgeThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerKnowledgeThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_KNOWLEDGE_THREE,
                DANCER_KNOWLEDGE_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasCommand(player, LYRICAL_ABILITY) &&
            hasCommand(player, BREAKDANCE_ABILITY) &&
            hasCommand(player, POPLOCK_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_TWO_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_KNOWLEDGE_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerKnowledgeThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerKnowledgeFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerKnowledgeFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_KNOWLEDGE_FOUR,
                DANCER_KNOWLEDGE_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            hasCommand(player, EXOTIC_ABILITY) &&
            hasCommand(player, EXOTIC_TWO_ABILITY) &&
            hasCommand(player, LYRICAL_ABILITY) &&
            hasCommand(player, BREAKDANCE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_KNOWLEDGE_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerKnowledgeFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseDancerMaster(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseDancerMaster resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                DANCER_MASTER,
                DANCER_MASTER_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, DANCER_MASTER) &&
            hasSkill(player, DANCER_ABILITY_FOUR) &&
            hasSkill(player, DANCER_WOUND_FOUR) &&
            hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasSkill(player, DANCER_SHOCK_FOUR) &&
            hasCommand(player, LYRICAL_TWO_ABILITY) &&
            hasCommand(player, EXOTIC_THREE_ABILITY) &&
            hasCommand(player, EXOTIC_FOUR_ABILITY) &&
            hasCommand(player, PLACE_CANTINA_ABILITY) &&
            hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_SPARK_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_SPARK_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 15 &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) -
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) == 25 &&
            getSkillStatMod(player, DANCE_MIND_MOD) -
                getIntObjVar(player, BASE_DANCE_MIND_MOD) == 25 &&
            getSkillStatMod(player, MELEE_DEFENSE_MOD) -
                getIntObjVar(player, BASE_MELEE_DEFENSE_MOD) == 7 &&
            getSkillStatMod(player, RANGED_DEFENSE_MOD) -
                getIntObjVar(player, BASE_RANGED_DEFENSE_MOD) == 7 &&
            getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD) -
                getIntObjVar(player, BASE_PRIVATE_PLACE_CANTINA_MOD) ==
                    100 &&
            getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD) -
                getIntObjVar(player, BASE_PRIVATE_PLACE_THEATER_MOD) ==
                    100 &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    DANCER_MASTER_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseDancerMaster passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private boolean grantSkillIfMissing(
        obj_id player,
        String skillName)
        throws InterruptedException
    {
        return
            hasSkill(player, skillName) ||
            (skill.grantSkillToPlayer(player, skillName) &&
                hasSkill(player, skillName));
    }

    private boolean purchaseWithoutHolocron(
        obj_id player,
        String skillName,
        int expectedPointCost)
        throws InterruptedException
    {
        int pointsRequired = skill.getSkillPointCost(skillName);
        if (pointsRequired != expectedPointCost ||
            skill.getAvailableSkillPoints(player) < pointsRequired ||
            !skill.hasRequiredSkillsForSkillPurchase(
                player,
                skillName) ||
            !skill.hasRequiredXpForSkillPurchase(player, skillName) ||
            hasSkill(player, skillName))
        {
            return false;
        }
        if (!skill.grantSkillToPlayer(player, skillName))
        {
            return false;
        }
        if (skill.deductXpCostForSkillPurchase(player, skillName))
        {
            return true;
        }
        revokeSkill(player, skillName);
        return false;
    }

    private String observeBasicTwoStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                -1788534963,
                "basic2",
                0);
        int action = getAttrib(player, ACTION);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == BASIC_TWO_INDEX &&
            getPerformanceType(player) == BASIC_TWO_INDEX &&
            hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            (action == START_ACTION ||
                action == FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeBasicTwoStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeBasicTwoStop(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            !hasObjVar(player, performance.VAR_PERFORM);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=observeBasicTwoStop passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrender(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, DANCE_ONE) &&
            !hasCommand(player, BASIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 2000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrender passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeRhythmicTwoStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                -1788534963,
                "rhythmic2",
                0);
        int action = getAttrib(player, ACTION);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == RHYTHMIC_TWO_INDEX &&
            getPerformanceType(player) == RHYTHMIC_TWO_INDEX &&
            hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            (action == START_ACTION ||
                action == FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeRhythmicTwoStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeRhythmicTwoStop(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            !hasObjVar(player, performance.VAR_PERFORM);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=observeRhythmicTwoStop passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, DANCE_TWO) &&
            hasSkill(player, DANCE_ONE) &&
            !hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 10000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeFootlooseStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                -1788534963,
                "footloose",
                0);
        int action = getAttrib(player, ACTION);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == FOOTLOOSE_INDEX &&
            getPerformanceType(player) == FOOTLOOSE_INDEX &&
            hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            (action == START_ACTION ||
                action == FOOTLOOSE_FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeFootlooseStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeFootlooseStop(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            !hasObjVar(player, performance.VAR_PERFORM);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=observeFootlooseStop passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, DANCE_THREE) &&
            hasSkill(player, DANCE_TWO) &&
            hasSkill(player, DANCE_ONE) &&
            !hasCommand(player, FOOTLOOSE_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeFormalStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                -1788534963,
                "formal",
                0);
        int action = getAttrib(player, ACTION);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == FORMAL_INDEX &&
            getPerformanceType(player) == FORMAL_INDEX &&
            hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            (action == START_ACTION ||
                action == FOOTLOOSE_FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeFormalStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeFormalStop(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            !hasObjVar(player, performance.VAR_PERFORM);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=observeFormalStop passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, DANCE_FOUR) &&
            hasSkill(player, DANCE_THREE) &&
            hasSkill(player, DANCE_TWO) &&
            hasSkill(player, DANCE_ONE) &&
            !hasCommand(player, FORMAL_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            hasCommand(player, FOOTLOOSE_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasCommand(player, BASIC_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 90000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observePopularStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                -1788534963,
                "popular",
                0);
        int action = getAttrib(player, ACTION);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == POPULAR_INDEX &&
            getPerformanceType(player) == POPULAR_INDEX &&
            hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            (action == START_ACTION ||
                action == FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observePopularStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observePopularStop(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT) &&
            !hasObjVar(player, performance.VAR_PERFORM);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=observePopularStop passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerNovice(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, DANCER_NOVICE) &&
            hasSkill(player, HEALING_FOUR) &&
            hasSkill(player, DANCE_FOUR) &&
            !hasCommand(player, POPULAR_ABILITY) &&
            !hasCommand(player, POPLOCK_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, FORMAL_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_ABILITY) &&
            !hasCommand(player, NGE_PROP_RIBBON_ABILITY) &&
            !hasCommand(player, NGE_PROP_DUAL_ABILITY) &&
            !hasSchematic(player, NGE_PROP_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 150000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerNovice passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerAbilityOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_ABILITY_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerAbilityOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerAbilityTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_ABILITY_TWO) &&
            hasSkill(player, DANCER_ABILITY_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerAbilityTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerAbilityThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_ABILITY_THREE) &&
            hasSkill(player, DANCER_ABILITY_TWO) &&
            hasSkill(player, DANCER_ABILITY_ONE) &&
            !hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 700000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerAbilityThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerAbilityFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_ABILITY_FOUR) &&
            hasSkill(player, DANCER_ABILITY_THREE) &&
            hasSkill(player, DANCER_ABILITY_TWO) &&
            !hasCommand(player, NGE_FLOOR_LIGHTS_ABILITY) &&
            hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerAbilityFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerWoundOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_WOUND_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 75000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerWoundOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerWoundTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_WOUND_TWO) &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerWoundTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerWoundThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_WOUND_THREE) &&
            hasSkill(player, DANCER_WOUND_TWO) &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerWoundThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerWoundFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_WOUND_FOUR) &&
            hasSkill(player, DANCER_WOUND_THREE) &&
            hasSkill(player, DANCER_WOUND_TWO) &&
            hasSkill(player, DANCER_WOUND_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerWoundFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerShockOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_SHOCK_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 75000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerShockOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerShockTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_SHOCK_TWO) &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_MAGIC_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 200000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerShockTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerShockThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_SHOCK_THREE) &&
            hasSkill(player, DANCER_SHOCK_TWO) &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_MAGIC_RIBBON_ABILITY) &&
            !hasSchematic(
                player,
                NGE_DOUBLE_MAGIC_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 400000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerShockThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerShockFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_SHOCK_FOUR) &&
            hasSkill(player, DANCER_SHOCK_THREE) &&
            hasSkill(player, DANCER_SHOCK_TWO) &&
            hasSkill(player, DANCER_SHOCK_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, NGE_SPARK_RIBBON_ABILITY) &&
            !hasSchematic(player, NGE_SPARK_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerShockFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerKnowledgeOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, POPULAR_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_TWO_ABILITY) &&
            hasCommand(player, POPULAR_ABILITY) &&
            hasCommand(player, POPLOCK_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerKnowledgeOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerKnowledgeTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, POPLOCK_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_TWO_ABILITY) &&
            hasCommand(player, POPULAR_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerKnowledgeTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerKnowledgeThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, LYRICAL_ABILITY) &&
            !hasCommand(player, BREAKDANCE_ABILITY) &&
            hasCommand(player, POPLOCK_TWO_ABILITY) &&
            hasCommand(player, TUMBLE_TWO_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 700000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerKnowledgeThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerKnowledgeFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            hasSkill(player, DANCER_NOVICE) &&
            !hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_ABILITY) &&
            !hasCommand(player, EXOTIC_TWO_ABILITY) &&
            hasCommand(player, LYRICAL_ABILITY) &&
            hasCommand(player, BREAKDANCE_ABILITY) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerKnowledgeFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderDancerMaster(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, DANCER_MASTER) &&
            hasSkill(player, DANCER_ABILITY_FOUR) &&
            hasSkill(player, DANCER_WOUND_FOUR) &&
            hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            hasSkill(player, DANCER_SHOCK_FOUR) &&
            !hasCommand(player, LYRICAL_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_THREE_ABILITY) &&
            !hasCommand(player, EXOTIC_FOUR_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_SPARK_RIBBON_ABILITY) &&
            hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            hasCommand(player, EXOTIC_ABILITY) &&
            hasCommand(player, EXOTIC_TWO_ABILITY) &&
            !hasSchematic(player, NGE_DOUBLE_SPARK_RIBBON_SCHEMATIC) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_SHOCK_MOD) ==
                getIntObjVar(player, BASE_DANCE_SHOCK_MOD) &&
            getSkillStatMod(player, DANCE_MIND_MOD) ==
                getIntObjVar(player, BASE_DANCE_MIND_MOD) &&
            getSkillStatMod(player, MELEE_DEFENSE_MOD) ==
                getIntObjVar(player, BASE_MELEE_DEFENSE_MOD) &&
            getSkillStatMod(player, RANGED_DEFENSE_MOD) ==
                getIntObjVar(player, BASE_RANGED_DEFENSE_MOD) &&
            getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD) ==
                getIntObjVar(player, BASE_PRIVATE_PLACE_CANTINA_MOD) &&
            getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD) ==
                getIntObjVar(player, BASE_PRIVATE_PLACE_THEATER_MOD) &&
            getSkillStatMod(player, PROP_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD) &&
            getExperiencePoints(player, DANCE_XP) == 0 &&
            getExperiencePoints(player, ENTERTAINER_HEALING_XP) == 0 &&
            getExperienceCap(player, DANCE_XP) == 900000 &&
            getExperienceCap(player, ENTERTAINER_HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderDancerMaster passed=" +
            passed + " " + buildStatus(player);
    }

    private String cleanup(obj_id player)
        throws InterruptedException
    {
        boolean restored = restoreSnapshot(player);
        if (!restored)
        {
            return "error=cleanupRestoreFailed " +
                buildStatus(player);
        }
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false restored=true" +
            " danceXp=" + getExperiencePoints(player, DANCE_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player) +
            " performance=" + getPerformanceType(player);
    }

    private boolean restoreSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        clearPerformanceState(player);
        if (hasSkill(player, DANCER_MASTER))
        {
            revokeSkill(player, DANCER_MASTER);
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_FOUR))
        {
            revokeSkill(player, DANCER_KNOWLEDGE_FOUR);
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_THREE))
        {
            revokeSkill(player, DANCER_KNOWLEDGE_THREE);
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_TWO))
        {
            revokeSkill(player, DANCER_KNOWLEDGE_TWO);
        }
        if (hasSkill(player, DANCER_KNOWLEDGE_ONE))
        {
            revokeSkill(player, DANCER_KNOWLEDGE_ONE);
        }
        if (hasSkill(player, DANCER_SHOCK_FOUR))
        {
            revokeSkill(player, DANCER_SHOCK_FOUR);
        }
        if (hasSkill(player, DANCER_SHOCK_THREE))
        {
            revokeSkill(player, DANCER_SHOCK_THREE);
        }
        if (hasSkill(player, DANCER_SHOCK_TWO))
        {
            revokeSkill(player, DANCER_SHOCK_TWO);
        }
        if (hasSkill(player, DANCER_SHOCK_ONE))
        {
            revokeSkill(player, DANCER_SHOCK_ONE);
        }
        if (hasSkill(player, DANCER_WOUND_FOUR))
        {
            revokeSkill(player, DANCER_WOUND_FOUR);
        }
        if (hasSkill(player, DANCER_WOUND_THREE))
        {
            revokeSkill(player, DANCER_WOUND_THREE);
        }
        if (hasSkill(player, DANCER_WOUND_TWO))
        {
            revokeSkill(player, DANCER_WOUND_TWO);
        }
        if (hasSkill(player, DANCER_WOUND_ONE))
        {
            revokeSkill(player, DANCER_WOUND_ONE);
        }
        if (hasSkill(player, DANCER_ABILITY_FOUR))
        {
            revokeSkill(player, DANCER_ABILITY_FOUR);
        }
        if (hasSkill(player, DANCER_ABILITY_THREE))
        {
            revokeSkill(player, DANCER_ABILITY_THREE);
        }
        if (hasSkill(player, DANCER_ABILITY_TWO))
        {
            revokeSkill(player, DANCER_ABILITY_TWO);
        }
        if (hasSkill(player, DANCER_ABILITY_ONE))
        {
            revokeSkill(player, DANCER_ABILITY_ONE);
        }
        if (hasSkill(player, DANCER_NOVICE))
        {
            revokeSkill(player, DANCER_NOVICE);
        }
        if (hasSkill(player, HEALING_FOUR))
        {
            revokeSkill(player, HEALING_FOUR);
        }
        if (hasSkill(player, DANCE_FOUR))
        {
            revokeSkill(player, DANCE_FOUR);
        }
        if (hasSkill(player, DANCE_THREE))
        {
            revokeSkill(player, DANCE_THREE);
        }
        if (hasSkill(player, DANCE_TWO))
        {
            revokeSkill(player, DANCE_TWO);
        }
        if (hasSkill(player, DANCE_ONE))
        {
            revokeSkill(player, DANCE_ONE);
        }
        if (hasSkill(player, NOVICE))
        {
            revokeSkill(player, NOVICE);
        }
        boolean xpRestored =
            setXpExact(
                player,
                DANCE_XP,
                getIntObjVar(player, ORIGINAL_DANCE_XP));
        boolean healingXpRestored =
            setXpExact(
                player,
                ENTERTAINER_HEALING_XP,
                getIntObjVar(player, ORIGINAL_HEALING_XP));
        boolean quicknessRestored =
            setExactAttribute(
                player,
                QUICKNESS,
                getIntObjVar(player, ORIGINAL_QUICKNESS));
        setRegenRate(
            player,
            ACTION,
            getFloatObjVar(player, ORIGINAL_ACTION_REGEN));
        boolean actionRestored =
            setExactAttribute(
                player,
                ACTION,
                getIntObjVar(player, ORIGINAL_ACTION));
        boolean locomotionRestored =
            setLocomotion(
                player,
                getIntObjVar(player, ORIGINAL_LOCOMOTION));
        boolean postureRestored =
            setPostureClientImmediate(
                player,
                getIntObjVar(player, ORIGINAL_POSTURE));
        return
            xpRestored &&
            healingXpRestored &&
            quicknessRestored &&
            actionRestored &&
            locomotionRestored &&
            postureRestored &&
            !hasSkill(player, DANCER_MASTER) &&
            !hasSkill(player, DANCER_KNOWLEDGE_FOUR) &&
            !hasSkill(player, DANCER_KNOWLEDGE_THREE) &&
            !hasSkill(player, DANCER_KNOWLEDGE_TWO) &&
            !hasSkill(player, DANCER_KNOWLEDGE_ONE) &&
            !hasSkill(player, DANCER_SHOCK_FOUR) &&
            !hasSkill(player, DANCER_SHOCK_THREE) &&
            !hasSkill(player, DANCER_SHOCK_TWO) &&
            !hasSkill(player, DANCER_SHOCK_ONE) &&
            !hasSkill(player, DANCER_WOUND_FOUR) &&
            !hasSkill(player, DANCER_WOUND_THREE) &&
            !hasSkill(player, DANCER_WOUND_TWO) &&
            !hasSkill(player, DANCER_WOUND_ONE) &&
            !hasSkill(player, DANCER_ABILITY_FOUR) &&
            !hasSkill(player, DANCER_ABILITY_THREE) &&
            !hasSkill(player, DANCER_ABILITY_TWO) &&
            !hasSkill(player, DANCER_ABILITY_ONE) &&
            !hasSkill(player, DANCER_NOVICE) &&
            !hasSkill(player, HEALING_FOUR) &&
            !hasSkill(player, DANCE_FOUR) &&
            !hasSkill(player, DANCE_THREE) &&
            !hasSkill(player, DANCE_TWO) &&
            !hasSkill(player, DANCE_ONE) &&
            !hasSkill(player, NOVICE) &&
            !hasCommand(player, BASIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_ABILITY) &&
            !hasCommand(player, RHYTHMIC_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            !hasCommand(player, FOOTLOOSE_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            !hasCommand(player, FORMAL_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, POPULAR_ABILITY) &&
            !hasCommand(player, POPLOCK_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLOR_LIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            !hasCommand(player, DISTRACT_ABILITY) &&
            !hasCommand(player, NGE_COLOR_SWIRL_ABILITY) &&
            !hasCommand(player, SMOKE_BOMB_ABILITY) &&
            !hasCommand(player, NGE_CENTER_STAGE_ABILITY) &&
            !hasCommand(player, NGE_FLOOR_LIGHTS_ABILITY) &&
            !hasCommand(player, POPULAR_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_TWO_ABILITY) &&
            !hasCommand(player, POPLOCK_TWO_ABILITY) &&
            !hasCommand(player, TUMBLE_TWO_ABILITY) &&
            !hasCommand(player, LYRICAL_ABILITY) &&
            !hasCommand(player, BREAKDANCE_ABILITY) &&
            !hasCommand(player, BREAKDANCE_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_ABILITY) &&
            !hasCommand(player, EXOTIC_TWO_ABILITY) &&
            !hasCommand(player, LYRICAL_TWO_ABILITY) &&
            !hasCommand(player, EXOTIC_THREE_ABILITY) &&
            !hasCommand(player, EXOTIC_FOUR_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasCommand(player, NGE_DOUBLE_SPARK_RIBBON_ABILITY) &&
            !hasCommand(player, NGE_BUNDUKI_ABILITY) &&
            !hasCommand(player, NGE_PROP_RIBBON_ABILITY) &&
            !hasCommand(player, NGE_PROP_DUAL_ABILITY) &&
            !hasSchematic(player, NGE_PROP_SCHEMATIC) &&
            !hasSchematic(player, NGE_DOUBLE_SPARK_RIBBON_SCHEMATIC) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            getPerformanceType(player) == 0;
    }

    private void clearPerformanceState(obj_id player)
        throws InterruptedException
    {
        if (hasScript(player, performance.DANCE_HEARTBEAT_SCRIPT))
        {
            detachScript(
                player,
                performance.DANCE_HEARTBEAT_SCRIPT);
        }
        setPerformanceType(player, 0);
        setPerformanceStartTime(player, 0);
        setClientUsesAnimationLocomotion(player, false);
        if (hasObjVar(player, performance.VAR_PERFORM))
        {
            removeObjVar(player, performance.VAR_PERFORM);
        }
        if (hasObjVar(
            player,
            performance.VAR_PERFORM_NO_GROUP_DANCE))
        {
            removeObjVar(
                player,
                performance.VAR_PERFORM_NO_GROUP_DANCE);
        }
        if (utils.hasScriptVar(player, "stopDanceMessage"))
        {
            utils.removeScriptVar(player, "stopDanceMessage");
        }
        if (utils.hasScriptVar(
            player,
            "performance.performance_delay"))
        {
            utils.removeScriptVar(
                player,
                "performance.performance_delay");
        }
    }

    private boolean setXpExact(
        obj_id player,
        String xpType,
        int target)
        throws InterruptedException
    {
        int current = getExperiencePoints(player, xpType);
        if (current != target)
        {
            grantExperiencePoints(
                player,
                xpType,
                target - current);
        }
        return getExperiencePoints(player, xpType) == target;
    }

    private boolean setExactAttribute(
        obj_id player,
        int attribute,
        int value)
        throws InterruptedException
    {
        setAttrib(player, attribute, value);
        return getAttrib(player, attribute) == value;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        int baseMod = hasObjVar(player, BASE_DANCE_MOD)
            ? getIntObjVar(player, BASE_DANCE_MOD)
            : getSkillStatMod(player, DANCE_MOD);
        int baseWoundMod = hasObjVar(player, BASE_DANCE_WOUND_MOD)
            ? getIntObjVar(player, BASE_DANCE_WOUND_MOD)
            : getSkillStatMod(player, DANCE_WOUND_MOD);
        int baseShockMod = hasObjVar(player, BASE_DANCE_SHOCK_MOD)
            ? getIntObjVar(player, BASE_DANCE_SHOCK_MOD)
            : getSkillStatMod(player, DANCE_SHOCK_MOD);
        int baseMindMod = hasObjVar(player, BASE_DANCE_MIND_MOD)
            ? getIntObjVar(player, BASE_DANCE_MIND_MOD)
            : getSkillStatMod(player, DANCE_MIND_MOD);
        int baseMeleeDefense = hasObjVar(player, BASE_MELEE_DEFENSE_MOD)
            ? getIntObjVar(player, BASE_MELEE_DEFENSE_MOD)
            : getSkillStatMod(player, MELEE_DEFENSE_MOD);
        int baseRangedDefense = hasObjVar(player, BASE_RANGED_DEFENSE_MOD)
            ? getIntObjVar(player, BASE_RANGED_DEFENSE_MOD)
            : getSkillStatMod(player, RANGED_DEFENSE_MOD);
        int basePlaceCantina =
            hasObjVar(player, BASE_PRIVATE_PLACE_CANTINA_MOD)
                ? getIntObjVar(player, BASE_PRIVATE_PLACE_CANTINA_MOD)
                : getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD);
        int basePlaceTheater =
            hasObjVar(player, BASE_PRIVATE_PLACE_THEATER_MOD)
                ? getIntObjVar(player, BASE_PRIVATE_PLACE_THEATER_MOD)
                : getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD);
        return "player=" + player +
            " novice=" + (hasSkill(player, NOVICE) ? "1" : "0") +
            " danceOne=" +
                (hasSkill(player, DANCE_ONE) ? "1" : "0") +
            " danceTwo=" +
                (hasSkill(player, DANCE_TWO) ? "1" : "0") +
            " danceThree=" +
                (hasSkill(player, DANCE_THREE) ? "1" : "0") +
            " danceFour=" +
                (hasSkill(player, DANCE_FOUR) ? "1" : "0") +
            " healingFour=" +
                (hasSkill(player, HEALING_FOUR) ? "1" : "0") +
            " dancerNovice=" +
                (hasSkill(player, DANCER_NOVICE) ? "1" : "0") +
            " dancerAbilityOne=" +
                (hasSkill(player, DANCER_ABILITY_ONE) ? "1" : "0") +
            " dancerAbilityTwo=" +
                (hasSkill(player, DANCER_ABILITY_TWO) ? "1" : "0") +
            " dancerAbilityThree=" +
                (hasSkill(player, DANCER_ABILITY_THREE) ? "1" : "0") +
            " dancerAbilityFour=" +
                (hasSkill(player, DANCER_ABILITY_FOUR) ? "1" : "0") +
            " dancerWoundOne=" +
                (hasSkill(player, DANCER_WOUND_ONE) ? "1" : "0") +
            " dancerWoundTwo=" +
                (hasSkill(player, DANCER_WOUND_TWO) ? "1" : "0") +
            " dancerWoundThree=" +
                (hasSkill(player, DANCER_WOUND_THREE) ? "1" : "0") +
            " dancerWoundFour=" +
                (hasSkill(player, DANCER_WOUND_FOUR) ? "1" : "0") +
            " dancerShockOne=" +
                (hasSkill(player, DANCER_SHOCK_ONE) ? "1" : "0") +
            " dancerShockTwo=" +
                (hasSkill(player, DANCER_SHOCK_TWO) ? "1" : "0") +
            " dancerShockThree=" +
                (hasSkill(player, DANCER_SHOCK_THREE) ? "1" : "0") +
            " dancerShockFour=" +
                (hasSkill(player, DANCER_SHOCK_FOUR) ? "1" : "0") +
            " dancerKnowledgeOne=" +
                (hasSkill(player, DANCER_KNOWLEDGE_ONE) ? "1" : "0") +
            " dancerKnowledgeTwo=" +
                (hasSkill(player, DANCER_KNOWLEDGE_TWO) ? "1" : "0") +
            " dancerKnowledgeThree=" +
                (hasSkill(player, DANCER_KNOWLEDGE_THREE) ? "1" : "0") +
            " dancerKnowledgeFour=" +
                (hasSkill(player, DANCER_KNOWLEDGE_FOUR) ? "1" : "0") +
            " dancerMaster=" +
                (hasSkill(player, DANCER_MASTER) ? "1" : "0") +
            " commands=" +
                (hasCommand(player, BASIC_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(player, PRIVATE_ABILITY) ? "1" : "0") +
            " commandsTwo=" +
                (hasCommand(
                    player,
                    RHYTHMIC_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_TWO_ABILITY) ? "1" : "0") +
            " commandsThree=" +
                (hasCommand(
                    player,
                    FOOTLOOSE_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_THREE_ABILITY) ? "1" : "0") +
            " commandsFour=" +
                (hasCommand(
                    player,
                    FORMAL_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_FOUR_ABILITY) ? "1" : "0") +
            " dancerCommands=" +
                (hasCommand(player, POPULAR_ABILITY) ? "1" : "0") +
                (hasCommand(player, POPLOCK_ABILITY) ? "1" : "0") +
                (hasCommand(player, REGISTER_ABILITY) ? "1" : "0") +
            " abilityOneCommands=" +
                (hasCommand(player, SPOTLIGHT_ABILITY) ? "1" : "0") +
                (hasCommand(player, COLOR_LIGHTS_ABILITY) ? "1" : "0") +
                (hasCommand(player, DAZZLE_ABILITY) ? "1" : "0") +
            " abilityTwoCommands=" +
                (hasCommand(player, DISTRACT_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_COLOR_SWIRL_ABILITY) ? "1" : "0") +
            " abilityThreeCommands=" +
                (hasCommand(player, SMOKE_BOMB_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_CENTER_STAGE_ABILITY) ? "1" : "0") +
            " abilityFourCommands=" +
                (hasCommand(
                    player,
                    NGE_FLOOR_LIGHTS_ABILITY) ? "1" : "0") +
            " ngeCommands=" +
                (hasCommand(player, NGE_BUNDUKI_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_PROP_RIBBON_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_PROP_DUAL_ABILITY) ? "1" : "0") +
            " ngeShockOneCommand=" +
                (hasCommand(
                    player,
                    NGE_DOUBLE_RIBBON_ABILITY) ? "1" : "0") +
            " ngeShockTwoCommand=" +
                (hasCommand(
                    player,
                    NGE_MAGIC_RIBBON_ABILITY) ? "1" : "0") +
            " ngeShockThreeCommand=" +
                (hasCommand(
                    player,
                    NGE_DOUBLE_MAGIC_RIBBON_ABILITY) ? "1" : "0") +
            " ngeShockFourCommand=" +
                (hasCommand(
                    player,
                    NGE_SPARK_RIBBON_ABILITY) ? "1" : "0") +
            " knowledgeOneCommands=" +
                (hasCommand(player, POPULAR_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(player, TUMBLE_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_BUNDUKI_TWO_ABILITY) ? "1" : "0") +
            " knowledgeTwoCommands=" +
                (hasCommand(player, POPLOCK_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(player, TUMBLE_TWO_ABILITY) ? "1" : "0") +
            " knowledgeThreeCommands=" +
                (hasCommand(player, LYRICAL_ABILITY) ? "1" : "0") +
                (hasCommand(player, BREAKDANCE_ABILITY) ? "1" : "0") +
            " knowledgeFourCommands=" +
                (hasCommand(player, BREAKDANCE_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(player, EXOTIC_ABILITY) ? "1" : "0") +
                (hasCommand(player, EXOTIC_TWO_ABILITY) ? "1" : "0") +
            " masterCommands=" +
                (hasCommand(player, LYRICAL_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(player, EXOTIC_THREE_ABILITY) ? "1" : "0") +
                (hasCommand(player, EXOTIC_FOUR_ABILITY) ? "1" : "0") +
                (hasCommand(player, PLACE_CANTINA_ABILITY) ? "1" : "0") +
                (hasCommand(player, PLACE_THEATER_ABILITY) ? "1" : "0") +
            " ngeMasterCommand=" +
                (hasCommand(
                    player,
                    NGE_DOUBLE_SPARK_RIBBON_ABILITY) ? "1" : "0") +
            " danceModDelta=" +
                (getSkillStatMod(player, DANCE_MOD) - baseMod) +
            " danceWoundDelta=" +
                (getSkillStatMod(player, DANCE_WOUND_MOD) -
                    baseWoundMod) +
            " danceShockDelta=" +
                (getSkillStatMod(player, DANCE_SHOCK_MOD) -
                    baseShockMod) +
            " danceMindDelta=" +
                (getSkillStatMod(player, DANCE_MIND_MOD) -
                    baseMindMod) +
            " meleeDefenseDelta=" +
                (getSkillStatMod(player, MELEE_DEFENSE_MOD) -
                    baseMeleeDefense) +
            " rangedDefenseDelta=" +
                (getSkillStatMod(player, RANGED_DEFENSE_MOD) -
                    baseRangedDefense) +
            " placeCantinaDelta=" +
                (getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD) -
                    basePlaceCantina) +
            " placeTheaterDelta=" +
                (getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD) -
                    basePlaceTheater) +
            " propAssemblyDelta=" +
                (hasObjVar(player, BASE_PROP_ASSEMBLY_MOD)
                    ? getSkillStatMod(player, PROP_ASSEMBLY_MOD) -
                        getIntObjVar(player, BASE_PROP_ASSEMBLY_MOD)
                    : 0) +
            " danceXp=" + getExperiencePoints(player, DANCE_XP) +
            " danceCap=" + getExperienceCap(player, DANCE_XP) +
            " healingXp=" +
                getExperiencePoints(player, ENTERTAINER_HEALING_XP) +
            " healingCap=" +
                getExperienceCap(player, ENTERTAINER_HEALING_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player) +
            " performance=" + getPerformanceType(player) +
            " action=" + getAttrib(player, ACTION) +
            " purchased=" + readFlag(player, PURCHASED) +
            " started=" + readFlag(player, STARTED) +
            " stopped=" + readFlag(player, STOPPED) +
            " surrendered=" + readFlag(player, SURRENDERED);
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, LIFECYCLE) &&
            hasObjVar(player, ORIGINAL_DANCE_XP) &&
            hasObjVar(player, ORIGINAL_HEALING_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_QUICKNESS) &&
            hasObjVar(player, ORIGINAL_ACTION_REGEN) &&
            hasObjVar(player, ORIGINAL_POSTURE) &&
            hasObjVar(player, ORIGINAL_LOCOMOTION) &&
            hasObjVar(player, BASE_POINTS) &&
            hasObjVar(player, BASE_DANCE_MOD);
    }

    private String validateOwnership(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasCompleteSnapshot(player))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
    }

    private boolean isValidLifecycle(String lifecycle)
    {
        return lifecycle != null &&
            lifecycle.matches("[a-f0-9]{32}");
    }

    private void resetTelemetry(obj_id player)
        throws InterruptedException
    {
        setObjVar(player, PURCHASED, 0);
        setObjVar(player, STARTED, 0);
        setObjVar(player, STOPPED, 0);
        setObjVar(player, SURRENDERED, 0);
    }

    private boolean readFlag(obj_id player, String name)
        throws InterruptedException
    {
        return hasObjVar(player, name) &&
            getIntObjVar(player, name) == 1;
    }
}
