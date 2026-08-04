package script.test;

import script.library.performance;
import script.library.skill;
import script.library.utils;
import script.obj_id;

/**
 * Identity-bound reversible fixture for authentic Publish 14.1 Entertainer
 * Music progression and master purchase, real-client song use, and
 * production surrender.
 */
public class precu_entertainer_music_one_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String NOVICE =
        "social_entertainer_novice";
    private static final String MUSIC_ONE =
        "social_entertainer_music_01";
    private static final String MUSIC_TWO =
        "social_entertainer_music_02";
    private static final String MUSIC_THREE =
        "social_entertainer_music_03";
    private static final String MUSIC_FOUR =
        "social_entertainer_music_04";
    private static final String HAIRSTYLE_FOUR =
        "social_entertainer_hairstyle_04";
    private static final String DANCE_FOUR =
        "social_entertainer_dance_04";
    private static final String HEALING_FOUR =
        "social_entertainer_healing_04";
    private static final String MASTER =
        "social_entertainer_master";
    private static final String MUSICIAN_NOVICE =
        "social_musician_novice";
    private static final String MUSICIAN_ABILITY_ONE =
        "social_musician_ability_01";
    private static final String MUSICIAN_ABILITY_TWO =
        "social_musician_ability_02";
    private static final String MUSICIAN_ABILITY_THREE =
        "social_musician_ability_03";
    private static final String MUSICIAN_ABILITY_FOUR =
        "social_musician_ability_04";
    private static final String MUSICIAN_WOUND_ONE =
        "social_musician_wound_01";
    private static final String MUSICIAN_WOUND_TWO =
        "social_musician_wound_02";
    private static final String MUSICIAN_WOUND_THREE =
        "social_musician_wound_03";
    private static final String MUSICIAN_WOUND_FOUR =
        "social_musician_wound_04";
    private static final String MUSICIAN_SHOCK_ONE =
        "social_musician_shock_01";
    private static final String MUSICIAN_SHOCK_TWO =
        "social_musician_shock_02";
    private static final String MUSICIAN_SHOCK_THREE =
        "social_musician_shock_03";
    private static final String MUSICIAN_SHOCK_FOUR =
        "social_musician_shock_04";
    private static final String MUSICIAN_KNOWLEDGE_ONE =
        "social_musician_knowledge_01";
    private static final String MUSICIAN_KNOWLEDGE_TWO =
        "social_musician_knowledge_02";
    private static final String MUSICIAN_KNOWLEDGE_THREE =
        "social_musician_knowledge_03";
    private static final String MUSICIAN_KNOWLEDGE_FOUR =
        "social_musician_knowledge_04";
    private static final String MUSICIAN_MASTER =
        "social_musician_master";
    private static final String MUSIC_XP = "music";
    private static final String HEALING_XP = "entertainer_healing";
    private static final String ROCK_ABILITY = "startMusic+rock";
    private static final String FIZZ_ABILITY = "fizz";
    private static final String PRIVATE_ABILITY =
        "private_entertainer_music_1";
    private static final String STARWARS_TWO_ABILITY =
        "startMusic+starwars2";
    private static final String PRIVATE_TWO_ABILITY =
        "private_entertainer_music_2";
    private static final String FOLK_ABILITY =
        "startMusic+folk";
    private static final String FANFAR_ABILITY = "fanfar";
    private static final String PRIVATE_THREE_ABILITY =
        "private_entertainer_music_3";
    private static final String STARWARS_THREE_ABILITY =
        "startMusic+starwars3";
    private static final String KLOOHORN_ABILITY = "kloohorn";
    private static final String PRIVATE_FOUR_ABILITY =
        "private_entertainer_music_4";
    private static final String PRIVATE_MASTER_ABILITY =
        "private_entertainer_master";
    private static final String FOOTLOOSE_TWO_ABILITY =
        "startDance+footloose2";
    private static final String FORMAL_TWO_ABILITY =
        "startDance+formal2";
    private static final String CEREMONIAL_ABILITY =
        "startMusic+ceremonial";
    private static final String MANDOVIOL_ABILITY = "mandoviol";
    private static final String TRAZ_ABILITY = "traz";
    private static final String REGISTER_ABILITY =
        "registerWithLocation";
    private static final String STARWARS_FOUR_ABILITY =
        "startMusic+starwars4";
    private static final String SPOTLIGHT_ABILITY = "spotlight";
    private static final String COLORLIGHTS_ABILITY = "colorlights";
    private static final String DAZZLE_ABILITY = "dazzle";
    private static final String FIREJET_ABILITY = "firejet";
    private static final String NGE_LASER_SHOW_ABILITY = "laserShow";
    private static final String VENTRILOQUISM_ABILITY =
        "ventriloquism";
    private static final String NGE_FIREJET_TWO_ABILITY = "firejet2";
    private static final String NGE_FEATURED_SOLO_ABILITY =
        "featuredSolo";
    private static final String NGE_BANDFILL_ABILITY = "bandfill";
    private static final String NGE_FLUTEDROOPY_ABILITY = "flutedroopy";
    private static final String NGE_OMNIBOX_ABILITY = "omnibox";
    private static final String BALLAD_ABILITY =
        "startMusic+ballad";
    private static final String NGE_SWING_ABILITY =
        "startMusic+swing";
    private static final String FUNK_ABILITY =
        "startMusic+funk";
    private static final String WALTZ_ABILITY =
        "startMusic+waltz";
    private static final String JAZZ_ABILITY =
        "startMusic+jazz";
    private static final String VIRTUOSO_ABILITY =
        "startMusic+virtuoso";
    private static final String NALARGON_ABILITY = "nalargon";
    private static final String PLACE_CANTINA_ABILITY =
        "place_cantina";
    private static final String PLACE_THEATER_ABILITY =
        "place_theater";
    private static final String FIZZ_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_fizz.iff";
    private static final String FIZZ_CLASSIC_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_fizz_classic.iff";
    private static final String KLOO_HORN_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_kloo_horn.iff";
    private static final String TRAZ_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_traz.iff";
    private static final String BANDFILL_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_bandfill.iff";
    private static final String OMNI_BOX_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_omni_box.iff";
    private static final String NGE_FLUTEDROOPY_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_flute_droopy.iff";
    private static final String NALARGON_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_nalargon.iff";
    private static final String NALARGON_CLASSIC_SCHEMATIC =
        "object/draft_schematic/instrument/instrument_nalargon_classic.iff";
    private static final String INSTRUMENT_ABILITY = "slitherhorn";
    private static final String INSTRUMENT_TEMPLATE =
        "object/tangible/instrument/slitherhorn.iff";
    private static final String MUSIC_MOD = "healing_music_ability";
    private static final String DANCE_MOD = "healing_dance_ability";
    private static final String MUSIC_WOUND_MOD =
        "healing_music_wound";
    private static final String DANCE_WOUND_MOD =
        "healing_dance_wound";
    private static final String MUSIC_SHOCK_MOD =
        "healing_music_shock";
    private static final String MUSIC_MIND_MOD =
        "healing_music_mind";
    private static final String INSTRUMENT_ASSEMBLY_MOD =
        "instrument_assembly";
    private static final String MELEE_DEFENSE_MOD = "melee_defense";
    private static final String RANGED_DEFENSE_MOD = "ranged_defense";
    private static final String PRIVATE_PLACE_CANTINA_MOD =
        "private_place_cantina";
    private static final String PRIVATE_PLACE_THEATER_MOD =
        "private_place_theater";
    private static final int ROCK_INDEX = 15;
    private static final int STARWARS_TWO_INDEX = 29;
    private static final int FOLK_INDEX = 43;
    private static final int STARWARS_THREE_INDEX = 57;
    private static final int CEREMONIAL_INDEX = 71;
    private static final int CEREMONIAL_FIRST_LOOP_ACTION = 64;
    private static final int SLITHERHORN_AUDIO_ID = 2;
    private static final int MUSIC_ONE_XP_COST = 1000;
    private static final int MUSIC_ONE_POINT_COST = 2;
    private static final int MUSIC_TWO_XP_COST = 5000;
    private static final int MUSIC_TWO_POINT_COST = 3;
    private static final int MUSIC_THREE_XP_COST = 15000;
    private static final int MUSIC_THREE_POINT_COST = 4;
    private static final int MUSIC_FOUR_XP_COST = 45000;
    private static final int MUSIC_FOUR_POINT_COST = 5;
    private static final int MASTER_PREREQUISITE_POINT_COST = 5;
    private static final int MASTER_POINT_COST = 6;
    private static final int MUSICIAN_NOVICE_XP_COST = 50000;
    private static final int MUSICIAN_NOVICE_POINT_COST = 6;
    private static final int MUSICIAN_ABILITY_ONE_XP_COST = 87500;
    private static final int MUSICIAN_ABILITY_ONE_POINT_COST = 5;
    private static final int MUSICIAN_ABILITY_TWO_XP_COST = 125000;
    private static final int MUSICIAN_ABILITY_TWO_POINT_COST = 4;
    private static final int MUSICIAN_ABILITY_THREE_XP_COST = 175000;
    private static final int MUSICIAN_ABILITY_THREE_POINT_COST = 3;
    private static final int MUSICIAN_ABILITY_FOUR_XP_COST = 225000;
    private static final int MUSICIAN_ABILITY_FOUR_POINT_COST = 2;
    private static final int MUSICIAN_WOUND_ONE_XP_COST = 25000;
    private static final int MUSICIAN_WOUND_ONE_POINT_COST = 5;
    private static final int MUSICIAN_WOUND_TWO_XP_COST = 50000;
    private static final int MUSICIAN_WOUND_TWO_POINT_COST = 4;
    private static final int MUSICIAN_WOUND_THREE_XP_COST = 100000;
    private static final int MUSICIAN_WOUND_THREE_POINT_COST = 3;
    private static final int MUSICIAN_WOUND_FOUR_XP_COST = 125000;
    private static final int MUSICIAN_WOUND_FOUR_POINT_COST = 2;
    private static final int MUSICIAN_SHOCK_ONE_XP_COST = 25000;
    private static final int MUSICIAN_SHOCK_ONE_POINT_COST = 5;
    private static final int MUSICIAN_SHOCK_TWO_XP_COST = 50000;
    private static final int MUSICIAN_SHOCK_TWO_POINT_COST = 4;
    private static final int MUSICIAN_SHOCK_THREE_XP_COST = 100000;
    private static final int MUSICIAN_SHOCK_THREE_POINT_COST = 3;
    private static final int MUSICIAN_SHOCK_FOUR_XP_COST = 125000;
    private static final int MUSICIAN_SHOCK_FOUR_POINT_COST = 2;
    private static final int MUSICIAN_KNOWLEDGE_ONE_XP_COST = 87500;
    private static final int MUSICIAN_KNOWLEDGE_ONE_POINT_COST = 5;
    private static final int MUSICIAN_KNOWLEDGE_TWO_XP_COST = 125000;
    private static final int MUSICIAN_KNOWLEDGE_TWO_POINT_COST = 4;
    private static final int MUSICIAN_KNOWLEDGE_THREE_XP_COST = 175000;
    private static final int MUSICIAN_KNOWLEDGE_THREE_POINT_COST = 3;
    private static final int MUSICIAN_KNOWLEDGE_FOUR_XP_COST = 225000;
    private static final int MUSICIAN_KNOWLEDGE_FOUR_POINT_COST = 2;
    private static final int MUSICIAN_MASTER_POINT_COST = 1;
    private static final int REFERENCE_QUICKNESS = 400;
    private static final int START_ACTION = 100;

    private static final String ROOT =
        "precu.entertainerMusicOneFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_MUSIC_XP =
        ROOT + ".originalMusicXp";
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
    private static final String ORIGINAL_INSTRUMENT_AUDIO =
        ROOT + ".originalInstrumentAudio";
    private static final String BASE_POINTS = ROOT + ".basePoints";
    private static final String BASE_MUSIC_MOD =
        ROOT + ".baseMusicMod";
    private static final String BASE_DANCE_MOD =
        ROOT + ".baseDanceMod";
    private static final String BASE_MUSIC_WOUND_MOD =
        ROOT + ".baseMusicWoundMod";
    private static final String BASE_DANCE_WOUND_MOD =
        ROOT + ".baseDanceWoundMod";
    private static final String BASE_MUSIC_SHOCK_MOD =
        ROOT + ".baseMusicShockMod";
    private static final String BASE_MUSIC_MIND_MOD =
        ROOT + ".baseMusicMindMod";
    private static final String BASE_INSTRUMENT_ASSEMBLY_MOD =
        ROOT + ".baseInstrumentAssemblyMod";
    private static final String BASE_MELEE_DEFENSE_MOD =
        ROOT + ".baseMeleeDefenseMod";
    private static final String BASE_RANGED_DEFENSE_MOD =
        ROOT + ".baseRangedDefenseMod";
    private static final String BASE_PRIVATE_PLACE_CANTINA_MOD =
        ROOT + ".basePrivatePlaceCantinaMod";
    private static final String BASE_PRIVATE_PLACE_THEATER_MOD =
        ROOT + ".basePrivatePlaceTheaterMod";
    private static final String FIXTURE_INSTRUMENT =
        ROOT + ".fixtureInstrument";
    private static final String PURCHASED = ROOT + ".purchased";
    private static final String STARTED = ROOT + ".started";
    private static final String STOP_REQUESTED =
        ROOT + ".stopRequested";
    private static final String STOPPED = ROOT + ".stopped";
    private static final String SURRENDERED = ROOT + ".surrendered";
    private static final String USAGE =
        "usage: prepare|purchase|observeRockStart|" +
        "observeRockStopRequested|observeRockStopComplete|" +
        "observeSurrender|prepareTwo|purchaseTwo|" +
        "observeStarwarsTwoStart|" +
        "observeStarwarsTwoStopRequested|" +
        "observeStarwarsTwoStopComplete|observeSurrenderTwo|" +
        "prepareThree|purchaseThree|observeFolkStart|" +
        "observeFolkStopRequested|observeFolkStopComplete|" +
        "observeSurrenderThree|" +
        "prepareFour|purchaseFour|observeStarwarsThreeStart|" +
        "observeStarwarsThreeStopRequested|" +
        "observeStarwarsThreeStopComplete|observeSurrenderFour|" +
        "prepareMaster|purchaseMaster|observeCeremonialStart|" +
        "observeCeremonialStopRequested|" +
        "observeCeremonialStopComplete|observeSurrenderMaster|" +
        "prepareMusicianNovice|purchaseMusicianNovice|" +
        "observeSurrenderMusicianNovice|" +
        "prepareMusicianAbilityOne|purchaseMusicianAbilityOne|" +
        "observeSurrenderMusicianAbilityOne|" +
        "prepareMusicianAbilityTwo|purchaseMusicianAbilityTwo|" +
        "observeSurrenderMusicianAbilityTwo|" +
        "prepareMusicianAbilityThree|purchaseMusicianAbilityThree|" +
        "observeSurrenderMusicianAbilityThree|" +
        "prepareMusicianAbilityFour|purchaseMusicianAbilityFour|" +
        "observeSurrenderMusicianAbilityFour|" +
        "prepareMusicianWoundOne|purchaseMusicianWoundOne|" +
        "observeSurrenderMusicianWoundOne|" +
        "prepareMusicianWoundTwo|purchaseMusicianWoundTwo|" +
        "observeSurrenderMusicianWoundTwo|" +
        "prepareMusicianWoundThree|purchaseMusicianWoundThree|" +
        "observeSurrenderMusicianWoundThree|" +
        "prepareMusicianWoundFour|purchaseMusicianWoundFour|" +
        "observeSurrenderMusicianWoundFour|" +
        "prepareMusicianShockOne|purchaseMusicianShockOne|" +
        "observeSurrenderMusicianShockOne|" +
        "prepareMusicianShockTwo|purchaseMusicianShockTwo|" +
        "observeSurrenderMusicianShockTwo|" +
        "prepareMusicianShockThree|purchaseMusicianShockThree|" +
        "observeSurrenderMusicianShockThree|" +
        "prepareMusicianShockFour|purchaseMusicianShockFour|" +
        "observeSurrenderMusicianShockFour|" +
        "prepareMusicianKnowledgeOne|purchaseMusicianKnowledgeOne|" +
        "observeSurrenderMusicianKnowledgeOne|" +
        "prepareMusicianKnowledgeTwo|purchaseMusicianKnowledgeTwo|" +
        "observeSurrenderMusicianKnowledgeTwo|" +
        "prepareMusicianKnowledgeThree|purchaseMusicianKnowledgeThree|" +
        "observeSurrenderMusicianKnowledgeThree|" +
        "prepareMusicianKnowledgeFour|purchaseMusicianKnowledgeFour|" +
        "observeSurrenderMusicianKnowledgeFour|" +
        "prepareMusicianMaster|purchaseMusicianMaster|" +
        "observeSurrenderMusicianMaster|" +
        "status|cleanup <playerOid> <lifecycle>";

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
        if (action.equalsIgnoreCase("prepareMaster"))
        {
            return prepareMaster(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianNovice"))
        {
            return prepareMusicianNovice(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianAbilityOne"))
        {
            return prepareMusicianAbilityOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianAbilityTwo"))
        {
            return prepareMusicianAbilityTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianAbilityThree"))
        {
            return prepareMusicianAbilityThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianAbilityFour"))
        {
            return prepareMusicianAbilityFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianWoundOne"))
        {
            return prepareMusicianWoundOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianWoundTwo"))
        {
            return prepareMusicianWoundTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianWoundThree"))
        {
            return prepareMusicianWoundThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianWoundFour"))
        {
            return prepareMusicianWoundFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianShockOne"))
        {
            return prepareMusicianShockOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianShockTwo"))
        {
            return prepareMusicianShockTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianShockThree"))
        {
            return prepareMusicianShockThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianShockFour"))
        {
            return prepareMusicianShockFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianKnowledgeOne"))
        {
            return prepareMusicianKnowledgeOne(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianKnowledgeTwo"))
        {
            return prepareMusicianKnowledgeTwo(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianKnowledgeThree"))
        {
            return prepareMusicianKnowledgeThree(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianKnowledgeFour"))
        {
            return prepareMusicianKnowledgeFour(player, args[2]);
        }
        if (action.equalsIgnoreCase("prepareMusicianMaster"))
        {
            return prepareMusicianMaster(player, args[2]);
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
        if (action.equalsIgnoreCase("purchaseMaster"))
        {
            return purchaseMaster(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianNovice"))
        {
            return purchaseMusicianNovice(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianAbilityOne"))
        {
            return purchaseMusicianAbilityOne(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianAbilityTwo"))
        {
            return purchaseMusicianAbilityTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianAbilityThree"))
        {
            return purchaseMusicianAbilityThree(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianAbilityFour"))
        {
            return purchaseMusicianAbilityFour(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianWoundOne"))
        {
            return purchaseMusicianWoundOne(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianWoundTwo"))
        {
            return purchaseMusicianWoundTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianWoundThree"))
        {
            return purchaseMusicianWoundThree(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianWoundFour"))
        {
            return purchaseMusicianWoundFour(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianShockOne"))
        {
            return purchaseMusicianShockOne(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianShockTwo"))
        {
            return purchaseMusicianShockTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianShockThree"))
        {
            return purchaseMusicianShockThree(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianShockFour"))
        {
            return purchaseMusicianShockFour(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianKnowledgeOne"))
        {
            return purchaseMusicianKnowledgeOne(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianKnowledgeTwo"))
        {
            return purchaseMusicianKnowledgeTwo(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianKnowledgeThree"))
        {
            return purchaseMusicianKnowledgeThree(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianKnowledgeFour"))
        {
            return purchaseMusicianKnowledgeFour(player);
        }
        if (action.equalsIgnoreCase("purchaseMusicianMaster"))
        {
            return purchaseMusicianMaster(player);
        }
        if (action.equalsIgnoreCase("observeRockStart"))
        {
            return observeRockStart(player);
        }
        if (action.equalsIgnoreCase("observeRockStopRequested"))
        {
            return observeRockStopRequested(player);
        }
        if (action.equalsIgnoreCase("observeRockStopComplete"))
        {
            return observeRockStopComplete(player);
        }
        if (action.equalsIgnoreCase("observeSurrender"))
        {
            return observeSurrender(player);
        }
        if (action.equalsIgnoreCase("observeStarwarsTwoStart"))
        {
            return observeStarwarsTwoStart(player);
        }
        if (action.equalsIgnoreCase(
            "observeStarwarsTwoStopRequested"))
        {
            return observeStarwarsTwoStopRequested(player);
        }
        if (action.equalsIgnoreCase(
            "observeStarwarsTwoStopComplete"))
        {
            return observeStarwarsTwoStopComplete(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderTwo"))
        {
            return observeSurrenderTwo(player);
        }
        if (action.equalsIgnoreCase("observeFolkStart"))
        {
            return observeFolkStart(player);
        }
        if (action.equalsIgnoreCase("observeFolkStopRequested"))
        {
            return observeFolkStopRequested(player);
        }
        if (action.equalsIgnoreCase("observeFolkStopComplete"))
        {
            return observeFolkStopComplete(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderThree"))
        {
            return observeSurrenderThree(player);
        }
        if (action.equalsIgnoreCase("observeStarwarsThreeStart"))
        {
            return observeStarwarsThreeStart(player);
        }
        if (action.equalsIgnoreCase(
            "observeStarwarsThreeStopRequested"))
        {
            return observeStarwarsThreeStopRequested(player);
        }
        if (action.equalsIgnoreCase(
            "observeStarwarsThreeStopComplete"))
        {
            return observeStarwarsThreeStopComplete(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderFour"))
        {
            return observeSurrenderFour(player);
        }
        if (action.equalsIgnoreCase("observeCeremonialStart"))
        {
            return observeCeremonialStart(player);
        }
        if (action.equalsIgnoreCase(
            "observeCeremonialStopRequested"))
        {
            return observeCeremonialStopRequested(player);
        }
        if (action.equalsIgnoreCase(
            "observeCeremonialStopComplete"))
        {
            return observeCeremonialStopComplete(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderMaster"))
        {
            return observeSurrenderMaster(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianNovice"))
        {
            return observeSurrenderMusicianNovice(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianAbilityOne"))
        {
            return observeSurrenderMusicianAbilityOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianAbilityTwo"))
        {
            return observeSurrenderMusicianAbilityTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianAbilityThree"))
        {
            return observeSurrenderMusicianAbilityThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianAbilityFour"))
        {
            return observeSurrenderMusicianAbilityFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianWoundOne"))
        {
            return observeSurrenderMusicianWoundOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianWoundTwo"))
        {
            return observeSurrenderMusicianWoundTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianWoundThree"))
        {
            return observeSurrenderMusicianWoundThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianWoundFour"))
        {
            return observeSurrenderMusicianWoundFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianShockOne"))
        {
            return observeSurrenderMusicianShockOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianShockTwo"))
        {
            return observeSurrenderMusicianShockTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianShockThree"))
        {
            return observeSurrenderMusicianShockThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianShockFour"))
        {
            return observeSurrenderMusicianShockFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianKnowledgeOne"))
        {
            return observeSurrenderMusicianKnowledgeOne(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianKnowledgeTwo"))
        {
            return observeSurrenderMusicianKnowledgeTwo(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianKnowledgeThree"))
        {
            return observeSurrenderMusicianKnowledgeThree(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianKnowledgeFour"))
        {
            return observeSurrenderMusicianKnowledgeFour(player);
        }
        if (action.equalsIgnoreCase(
            "observeSurrenderMusicianMaster"))
        {
            return observeSurrenderMusicianMaster(player);
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
            hasSkill(player, MUSIC_ONE) ||
            hasSkill(player, MUSIC_TWO) ||
            hasSkill(player, MUSIC_THREE) ||
            hasSkill(player, MUSIC_FOUR) ||
            hasSkill(player, HAIRSTYLE_FOUR) ||
            hasSkill(player, DANCE_FOUR) ||
            hasSkill(player, HEALING_FOUR) ||
            hasSkill(player, MASTER) ||
            hasSkill(player, MUSICIAN_NOVICE) ||
            hasSkill(player, MUSICIAN_ABILITY_ONE) ||
            hasSkill(player, MUSICIAN_ABILITY_TWO) ||
            hasSkill(player, MUSICIAN_ABILITY_THREE) ||
            hasSkill(player, MUSICIAN_ABILITY_FOUR) ||
            hasSkill(player, MUSICIAN_WOUND_ONE) ||
            hasSkill(player, MUSICIAN_WOUND_TWO) ||
            hasSkill(player, MUSICIAN_WOUND_THREE) ||
            hasSkill(player, MUSICIAN_WOUND_FOUR) ||
            hasSkill(player, MUSICIAN_SHOCK_ONE) ||
            hasSkill(player, MUSICIAN_SHOCK_TWO) ||
            hasSkill(player, MUSICIAN_SHOCK_THREE) ||
            hasSkill(player, MUSICIAN_SHOCK_FOUR) ||
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) ||
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) ||
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) ||
            hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) ||
            hasSkill(player, MUSICIAN_MASTER) ||
            hasCommand(player, ROCK_ABILITY) ||
            hasCommand(player, FIZZ_ABILITY) ||
            hasCommand(player, PRIVATE_ABILITY) ||
            hasCommand(player, STARWARS_TWO_ABILITY) ||
            hasCommand(player, PRIVATE_TWO_ABILITY) ||
            hasCommand(player, FOLK_ABILITY) ||
            hasCommand(player, FANFAR_ABILITY) ||
            hasCommand(player, PRIVATE_THREE_ABILITY) ||
            hasCommand(player, STARWARS_THREE_ABILITY) ||
            hasCommand(player, KLOOHORN_ABILITY) ||
            hasCommand(player, PRIVATE_FOUR_ABILITY) ||
            hasCommand(player, PRIVATE_MASTER_ABILITY) ||
            hasCommand(player, FOOTLOOSE_TWO_ABILITY) ||
            hasCommand(player, FORMAL_TWO_ABILITY) ||
            hasCommand(player, CEREMONIAL_ABILITY) ||
            hasCommand(player, MANDOVIOL_ABILITY) ||
            hasCommand(player, TRAZ_ABILITY) ||
            hasCommand(player, REGISTER_ABILITY) ||
            hasCommand(player, STARWARS_FOUR_ABILITY) ||
            hasCommand(player, SPOTLIGHT_ABILITY) ||
            hasCommand(player, COLORLIGHTS_ABILITY) ||
            hasCommand(player, DAZZLE_ABILITY) ||
            hasCommand(player, FIREJET_ABILITY) ||
            hasCommand(player, NGE_LASER_SHOW_ABILITY) ||
            hasCommand(player, VENTRILOQUISM_ABILITY) ||
            hasCommand(player, NGE_FIREJET_TWO_ABILITY) ||
            hasCommand(player, NGE_FEATURED_SOLO_ABILITY) ||
            hasCommand(player, NGE_BANDFILL_ABILITY) ||
            hasCommand(player, NGE_FLUTEDROOPY_ABILITY) ||
            hasCommand(player, NGE_OMNIBOX_ABILITY) ||
            hasCommand(player, BALLAD_ABILITY) ||
            hasCommand(player, NGE_SWING_ABILITY) ||
            hasCommand(player, FUNK_ABILITY) ||
            hasCommand(player, WALTZ_ABILITY) ||
            hasCommand(player, JAZZ_ABILITY) ||
            hasCommand(player, VIRTUOSO_ABILITY) ||
            hasCommand(player, NALARGON_ABILITY) ||
            hasCommand(player, PLACE_CANTINA_ABILITY) ||
            hasCommand(player, PLACE_THEATER_ABILITY) ||
            hasSchematic(player, FIZZ_SCHEMATIC) ||
            hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) ||
            hasSchematic(player, KLOO_HORN_SCHEMATIC) ||
            hasSchematic(player, TRAZ_SCHEMATIC) ||
            hasSchematic(player, BANDFILL_SCHEMATIC) ||
            hasSchematic(player, OMNI_BOX_SCHEMATIC) ||
            hasSchematic(player, NGE_FLUTEDROOPY_SCHEMATIC) ||
            hasSchematic(player, NALARGON_SCHEMATIC) ||
            hasSchematic(player, NALARGON_CLASSIC_SCHEMATIC))
        {
            return "error=fixtureRequiresUntrainedEntertainer";
        }
        if (getPerformanceType(player) != 0 ||
            hasScript(player, performance.MUSIC_HEARTBEAT_SCRIPT) ||
            hasScript(player, performance.POST_PERFORMANCE) ||
            hasObjVar(player, performance.VAR_PERFORM) ||
            hasObjVar(player, performance.VAR_PERFORM_OUTRO) ||
            isIdValid(getGroupObject(player)) ||
            getInstrumentAudioId(player) != 0)
        {
            return "error=fixtureRequiresIdleUngroupedPlayer";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_MUSIC_XP,
            getExperiencePoints(player, MUSIC_XP));
        setObjVar(
            player,
            ORIGINAL_HEALING_XP,
            getExperiencePoints(player, HEALING_XP));
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
        setObjVar(
            player,
            ORIGINAL_INSTRUMENT_AUDIO,
            getInstrumentAudioId(player));
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
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        boolean xpReady =
            setXpExact(player, MUSIC_XP, MUSIC_ONE_XP_COST);
        boolean quicknessReady =
            setExactAttribute(
                player,
                QUICKNESS,
                REFERENCE_QUICKNESS);
        boolean actionReady =
            setExactAttribute(player, ACTION, START_ACTION);

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id instrument = obj_id.NULL_ID;
        if (isIdValid(inventory))
        {
            instrument =
                createObject(INSTRUMENT_TEMPLATE, inventory, "");
        }
        if (isIdValid(instrument))
        {
            setObjVar(player, FIXTURE_INSTRUMENT, instrument);
        }
        boolean instrumentReady =
            isIdValid(instrument) &&
            instrument.isLoaded() &&
            equip(instrument, player) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID;

        boolean prepared =
            standing &&
            noviceGranted &&
            xpReady &&
            quicknessReady &&
            actionReady &&
            instrumentReady &&
            !hasSkill(player, MUSIC_ONE) &&
            !hasCommand(player, ROCK_ABILITY) &&
            !hasCommand(player, FIZZ_ABILITY) &&
            hasCommand(player, INSTRUMENT_ABILITY) &&
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
        if (hasSkill(player, MUSIC_TWO))
        {
            return "action=prepareTwo resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, MUSIC_ONE) ||
            (skill.grantSkillToPlayer(player, MUSIC_ONE) &&
                hasSkill(player, MUSIC_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        boolean xpReady =
            setXpExact(player, MUSIC_XP, MUSIC_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, NOVICE) &&
            hasSkill(player, MUSIC_ONE) &&
            !hasSkill(player, MUSIC_TWO) &&
            hasCommand(player, ROCK_ABILITY) &&
            hasCommand(player, FIZZ_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            !hasCommand(player, STARWARS_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            !hasSchematic(player, FIZZ_SCHEMATIC) &&
            !hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST;
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

    private String prepareThree(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String preparedTwo = prepareTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, MUSIC_THREE))
        {
            return "action=prepareThree resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, MUSIC_TWO) ||
            (skill.grantSkillToPlayer(player, MUSIC_TWO) &&
                hasSkill(player, MUSIC_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        boolean xpReady =
            setXpExact(player, MUSIC_XP, MUSIC_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, MUSIC_ONE) &&
            hasSkill(player, MUSIC_TWO) &&
            !hasSkill(player, MUSIC_THREE) &&
            hasCommand(player, STARWARS_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasSchematic(player, FIZZ_SCHEMATIC) &&
            hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            !hasCommand(player, FOLK_ABILITY) &&
            !hasCommand(player, FANFAR_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST;
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
        if (hasSkill(player, MUSIC_FOUR))
        {
            return "action=prepareFour resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteGranted =
            hasSkill(player, MUSIC_THREE) ||
            (skill.grantSkillToPlayer(player, MUSIC_THREE) &&
                hasSkill(player, MUSIC_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        boolean xpReady =
            setXpExact(player, MUSIC_XP, MUSIC_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, MUSIC_TWO) &&
            hasSkill(player, MUSIC_THREE) &&
            !hasSkill(player, MUSIC_FOUR) &&
            hasCommand(player, FOLK_ABILITY) &&
            hasCommand(player, FANFAR_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            !hasCommand(player, STARWARS_THREE_ABILITY) &&
            !hasCommand(player, KLOOHORN_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST;
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

    private String prepareMaster(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String preparedFour = prepareFour(player, lifecycle);
        if (preparedFour.startsWith("error="))
        {
            return preparedFour;
        }
        if (hasSkill(player, MASTER))
        {
            return "action=prepareMaster resumed=true " +
                buildStatus(player);
        }

        boolean musicReady =
            hasSkill(player, MUSIC_FOUR) ||
            (skill.grantSkillToPlayer(player, MUSIC_FOUR) &&
                hasSkill(player, MUSIC_FOUR));
        boolean hairstyleReady =
            hasSkill(player, HAIRSTYLE_FOUR) ||
            (skill.grantSkillToPlayer(player, HAIRSTYLE_FOUR) &&
                hasSkill(player, HAIRSTYLE_FOUR));
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
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_DANCE_MOD,
            getSkillStatMod(player, DANCE_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_DANCE_WOUND_MOD,
            getSkillStatMod(player, DANCE_WOUND_MOD));
        boolean xpReady = setXpExact(player, MUSIC_XP, 0);
        resetTelemetry(player);

        boolean prepared =
            musicReady &&
            hairstyleReady &&
            danceReady &&
            healingReady &&
            xpReady &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HAIRSTYLE_FOUR) &&
            hasSkill(player, DANCE_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasSkill(player, MASTER) &&
            hasCommand(player, STARWARS_THREE_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, PRIVATE_MASTER_ABILITY) &&
            !hasCommand(player, FOOTLOOSE_TWO_ABILITY) &&
            !hasCommand(player, FORMAL_TWO_ABILITY) &&
            !hasCommand(player, CEREMONIAL_ABILITY) &&
            !hasCommand(player, MANDOVIOL_ABILITY) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    3 * MASTER_PREREQUISITE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMasterSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMaster resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianNovice(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedFour = prepareFour(player, lifecycle);
        if (preparedFour.startsWith("error="))
        {
            return preparedFour;
        }
        if (hasSkill(player, MUSICIAN_NOVICE))
        {
            return "action=prepareMusicianNovice resumed=true " +
                buildStatus(player);
        }

        boolean musicReady =
            hasSkill(player, MUSIC_FOUR) ||
            (skill.grantSkillToPlayer(player, MUSIC_FOUR) &&
                hasSkill(player, MUSIC_FOUR));
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
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(player, MUSIC_XP, MUSICIAN_NOVICE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            musicReady &&
            healingReady &&
            xpReady &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, STARWARS_THREE_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, TRAZ_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, STARWARS_FOUR_ABILITY) &&
            !hasSchematic(player, KLOO_HORN_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 150000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianNoviceSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianNovice resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianAbilityOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareMusicianNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, MUSICIAN_ABILITY_ONE))
        {
            return "action=prepareMusicianAbilityOne resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteReady =
            hasSkill(player, MUSICIAN_NOVICE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_NOVICE) &&
                hasSkill(player, MUSICIAN_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_ABILITY_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLORLIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianAbilityOneSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianAbilityOne resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianAbilityTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareMusicianAbilityOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, MUSICIAN_ABILITY_TWO))
        {
            return "action=prepareMusicianAbilityTwo resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteReady =
            hasSkill(player, MUSICIAN_ABILITY_ONE) ||
            (skill.grantSkillToPlayer(
                player,
                MUSICIAN_ABILITY_ONE) &&
                hasSkill(player, MUSICIAN_ABILITY_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_ABILITY_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            !hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLORLIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            !hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_ABILITY_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianAbilityTwoSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianAbilityTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianAbilityThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareMusicianAbilityTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, MUSICIAN_ABILITY_THREE))
        {
            return "action=prepareMusicianAbilityThree resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteReady =
            hasSkill(player, MUSICIAN_ABILITY_TWO) ||
            (skill.grantSkillToPlayer(
                player,
                MUSICIAN_ABILITY_TWO) &&
                hasSkill(player, MUSICIAN_ABILITY_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_ABILITY_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            !hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            hasSchematic(player, TRAZ_SCHEMATIC) &&
            !hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_ABILITY_ONE_POINT_COST -
                    MUSICIAN_ABILITY_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianAbilityThreeSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianAbilityThree resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianAbilityFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareMusicianAbilityThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, MUSICIAN_ABILITY_FOUR))
        {
            return "action=prepareMusicianAbilityFour resumed=true " +
                buildStatus(player);
        }

        boolean prerequisiteReady =
            hasSkill(player, MUSICIAN_ABILITY_THREE) ||
            (skill.grantSkillToPlayer(
                player,
                MUSICIAN_ABILITY_THREE) &&
                hasSkill(player, MUSICIAN_ABILITY_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_ABILITY_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            prerequisiteReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            !hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            hasSchematic(player, BANDFILL_SCHEMATIC) &&
            !hasCommand(player, NGE_FEATURED_SOLO_ABILITY) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_ABILITY_ONE_POINT_COST -
                    MUSICIAN_ABILITY_TWO_POINT_COST -
                    MUSICIAN_ABILITY_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianAbilityFourSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianAbilityFour resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianWoundOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareMusicianNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, MUSICIAN_WOUND_ONE))
        {
            return "action=prepareMusicianWoundOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, MUSICIAN_NOVICE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_NOVICE) &&
                hasSkill(player, MUSICIAN_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_WOUND_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianWoundOneSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianWoundOne resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianWoundTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareMusicianWoundOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, MUSICIAN_WOUND_TWO))
        {
            return "action=prepareMusicianWoundTwo resumed=true " +
                buildStatus(player);
        }

        boolean woundOneReady =
            hasSkill(player, MUSICIAN_WOUND_ONE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_WOUND_ONE) &&
                hasSkill(player, MUSICIAN_WOUND_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_WOUND_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            woundOneReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            !hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_WOUND_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianWoundTwoSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianWoundTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianWoundThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareMusicianWoundTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, MUSICIAN_WOUND_THREE))
        {
            return "action=prepareMusicianWoundThree resumed=true " +
                buildStatus(player);
        }

        boolean woundTwoReady =
            hasSkill(player, MUSICIAN_WOUND_TWO) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_WOUND_TWO) &&
                hasSkill(player, MUSICIAN_WOUND_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_WOUND_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            woundTwoReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            !hasSkill(player, MUSICIAN_WOUND_THREE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_WOUND_ONE_POINT_COST -
                    MUSICIAN_WOUND_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianWoundThreeSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianWoundThree resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianWoundFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareMusicianWoundThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, MUSICIAN_WOUND_FOUR))
        {
            return "action=prepareMusicianWoundFour resumed=true " +
                buildStatus(player);
        }

        boolean woundThreeReady =
            hasSkill(player, MUSICIAN_WOUND_THREE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_WOUND_THREE) &&
                hasSkill(player, MUSICIAN_WOUND_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_WOUND_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            woundThreeReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasSkill(player, MUSICIAN_WOUND_THREE) &&
            !hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_WOUND_ONE_POINT_COST -
                    MUSICIAN_WOUND_TWO_POINT_COST -
                    MUSICIAN_WOUND_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianWoundFourSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianWoundFour resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianShockOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareMusicianNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, MUSICIAN_SHOCK_ONE))
        {
            return "action=prepareMusicianShockOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, MUSICIAN_NOVICE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_NOVICE) &&
                hasSkill(player, MUSICIAN_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_SHOCK_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianShockOneSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianShockOne resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianShockTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareMusicianShockOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, MUSICIAN_SHOCK_TWO))
        {
            return "action=prepareMusicianShockTwo resumed=true " +
                buildStatus(player);
        }

        boolean oneReady =
            hasSkill(player, MUSICIAN_SHOCK_ONE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_SHOCK_ONE) &&
                hasSkill(player, MUSICIAN_SHOCK_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                HEALING_XP,
                MUSICIAN_SHOCK_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            oneReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            !hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_SHOCK_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianShockTwoSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianShockTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianShockThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareMusicianShockTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, MUSICIAN_SHOCK_THREE))
        {
            return "action=prepareMusicianShockThree resumed=true " +
                buildStatus(player);
        }

        boolean twoReady =
            hasSkill(player, MUSICIAN_SHOCK_TWO) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_SHOCK_TWO) &&
                hasSkill(player, MUSICIAN_SHOCK_TWO));
        setObjVar(player, BASE_POINTS, skill.getAvailableSkillPoints(player));
        setObjVar(player, BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(player, BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(player, BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(player, BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady = setXpExact(
            player,
            HEALING_XP,
            MUSICIAN_SHOCK_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            twoReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            !hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            !hasSchematic(player, NGE_FLUTEDROOPY_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_SHOCK_ONE_POINT_COST -
                    MUSICIAN_SHOCK_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianShockThreeSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianShockThree resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianShockFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareMusicianShockThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, MUSICIAN_SHOCK_FOUR))
        {
            return "action=prepareMusicianShockFour resumed=true " +
                buildStatus(player);
        }

        boolean threeReady =
            hasSkill(player, MUSICIAN_SHOCK_THREE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_SHOCK_THREE) &&
                hasSkill(player, MUSICIAN_SHOCK_THREE));
        setObjVar(player, BASE_POINTS, skill.getAvailableSkillPoints(player));
        setObjVar(player, BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(player, BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(player, BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(player, BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady = setXpExact(
            player,
            HEALING_XP,
            MUSICIAN_SHOCK_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            threeReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            !hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_SHOCK_ONE_POINT_COST -
                    MUSICIAN_SHOCK_TWO_POINT_COST -
                    MUSICIAN_SHOCK_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianShockFourSetupFailed restored=" +
                restored + " " + detail;
        }
        return "action=prepareMusicianShockFour resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianKnowledgeOne(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedNovice =
            prepareMusicianNovice(player, lifecycle);
        if (preparedNovice.startsWith("error="))
        {
            return preparedNovice;
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_ONE))
        {
            return "action=prepareMusicianKnowledgeOne resumed=true " +
                buildStatus(player);
        }

        boolean noviceReady =
            hasSkill(player, MUSICIAN_NOVICE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_NOVICE) &&
                hasSkill(player, MUSICIAN_NOVICE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_KNOWLEDGE_ONE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            noviceReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            !hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianKnowledgeOneSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareMusicianKnowledgeOne resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianKnowledgeTwo(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedOne =
            prepareMusicianKnowledgeOne(player, lifecycle);
        if (preparedOne.startsWith("error="))
        {
            return preparedOne;
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_TWO))
        {
            return "action=prepareMusicianKnowledgeTwo resumed=true " +
                buildStatus(player);
        }

        boolean oneReady =
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_KNOWLEDGE_ONE) &&
                hasSkill(player, MUSICIAN_KNOWLEDGE_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_KNOWLEDGE_TWO_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            oneReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasCommand(player, FUNK_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_ONE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianKnowledgeTwoSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareMusicianKnowledgeTwo resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianKnowledgeThree(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedTwo =
            prepareMusicianKnowledgeTwo(player, lifecycle);
        if (preparedTwo.startsWith("error="))
        {
            return preparedTwo;
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_THREE))
        {
            return "action=prepareMusicianKnowledgeThree resumed=true " +
                buildStatus(player);
        }

        boolean twoReady =
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_KNOWLEDGE_TWO) &&
                hasSkill(player, MUSICIAN_KNOWLEDGE_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_KNOWLEDGE_THREE_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            twoReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            hasCommand(player, BALLAD_ABILITY) &&
            hasCommand(player, NGE_BANDFILL_ABILITY) &&
            hasCommand(player, FUNK_ABILITY) &&
            !hasCommand(player, WALTZ_ABILITY) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_ONE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_TWO_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianKnowledgeThreeSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareMusicianKnowledgeThree resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianKnowledgeFour(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedThree =
            prepareMusicianKnowledgeThree(player, lifecycle);
        if (preparedThree.startsWith("error="))
        {
            return preparedThree;
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR))
        {
            return "action=prepareMusicianKnowledgeFour resumed=true " +
                buildStatus(player);
        }

        boolean threeReady =
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) ||
            (skill.grantSkillToPlayer(player, MUSICIAN_KNOWLEDGE_THREE) &&
                hasSkill(player, MUSICIAN_KNOWLEDGE_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
        boolean xpReady =
            setXpExact(
                player,
                MUSIC_XP,
                MUSICIAN_KNOWLEDGE_FOUR_XP_COST);
        resetTelemetry(player);

        boolean prepared =
            threeReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasCommand(player, WALTZ_ABILITY) &&
            hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            !hasCommand(player, JAZZ_ABILITY) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_ONE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_TWO_POINT_COST -
                    MUSICIAN_KNOWLEDGE_THREE_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianKnowledgeFourSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareMusicianKnowledgeFour resumed=false " +
            buildStatus(player);
    }

    private String prepareMusicianMaster(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        String preparedKnowledge =
            prepareMusicianKnowledgeFour(player, lifecycle);
        if (preparedKnowledge.startsWith("error="))
        {
            return preparedKnowledge;
        }
        if (hasSkill(player, MUSICIAN_MASTER))
        {
            return "action=prepareMusicianMaster resumed=true " +
                buildStatus(player);
        }

        boolean knowledgeReady =
            grantSkillIfMissing(player, MUSICIAN_KNOWLEDGE_FOUR);
        boolean abilityReady =
            grantSkillIfMissing(player, MUSICIAN_ABILITY_ONE) &&
            grantSkillIfMissing(player, MUSICIAN_ABILITY_TWO) &&
            grantSkillIfMissing(player, MUSICIAN_ABILITY_THREE) &&
            grantSkillIfMissing(player, MUSICIAN_ABILITY_FOUR);
        boolean woundReady =
            grantSkillIfMissing(player, MUSICIAN_WOUND_ONE) &&
            grantSkillIfMissing(player, MUSICIAN_WOUND_TWO) &&
            grantSkillIfMissing(player, MUSICIAN_WOUND_THREE) &&
            grantSkillIfMissing(player, MUSICIAN_WOUND_FOUR);
        boolean shockReady =
            grantSkillIfMissing(player, MUSICIAN_SHOCK_ONE) &&
            grantSkillIfMissing(player, MUSICIAN_SHOCK_TWO) &&
            grantSkillIfMissing(player, MUSICIAN_SHOCK_THREE) &&
            grantSkillIfMissing(player, MUSICIAN_SHOCK_FOUR);

        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_MUSIC_MOD,
            getSkillStatMod(player, MUSIC_MOD));
        setObjVar(
            player,
            BASE_MUSIC_WOUND_MOD,
            getSkillStatMod(player, MUSIC_WOUND_MOD));
        setObjVar(
            player,
            BASE_MUSIC_SHOCK_MOD,
            getSkillStatMod(player, MUSIC_SHOCK_MOD));
        setObjVar(
            player,
            BASE_MUSIC_MIND_MOD,
            getSkillStatMod(player, MUSIC_MIND_MOD));
        setObjVar(
            player,
            BASE_INSTRUMENT_ASSEMBLY_MOD,
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD));
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
        boolean xpReady =
            setXpExact(player, MUSIC_XP, 0) &&
            setXpExact(player, HEALING_XP, 0);
        resetTelemetry(player);

        boolean prepared =
            knowledgeReady &&
            abilityReady &&
            woundReady &&
            shockReady &&
            xpReady &&
            hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            !hasSkill(player, MUSICIAN_MASTER) &&
            hasCommand(player, JAZZ_ABILITY) &&
            hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasCommand(player, VIRTUOSO_ABILITY) &&
            !hasCommand(player, NALARGON_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasSchematic(player, NALARGON_SCHEMATIC) &&
            !hasSchematic(player, NALARGON_CLASSIC_SCHEMATIC) &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - MUSIC_ONE_POINT_COST -
                    MUSIC_TWO_POINT_COST -
                    MUSIC_THREE_POINT_COST -
                    MUSIC_FOUR_POINT_COST -
                    MASTER_PREREQUISITE_POINT_COST -
                    MUSICIAN_NOVICE_POINT_COST -
                    MUSICIAN_ABILITY_ONE_POINT_COST -
                    MUSICIAN_ABILITY_TWO_POINT_COST -
                    MUSICIAN_ABILITY_THREE_POINT_COST -
                    MUSICIAN_ABILITY_FOUR_POINT_COST -
                    MUSICIAN_WOUND_ONE_POINT_COST -
                    MUSICIAN_WOUND_TWO_POINT_COST -
                    MUSICIAN_WOUND_THREE_POINT_COST -
                    MUSICIAN_WOUND_FOUR_POINT_COST -
                    MUSICIAN_SHOCK_ONE_POINT_COST -
                    MUSICIAN_SHOCK_TWO_POINT_COST -
                    MUSICIAN_SHOCK_THREE_POINT_COST -
                    MUSICIAN_SHOCK_FOUR_POINT_COST -
                    MUSICIAN_KNOWLEDGE_ONE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_TWO_POINT_COST -
                    MUSICIAN_KNOWLEDGE_THREE_POINT_COST -
                    MUSICIAN_KNOWLEDGE_FOUR_POINT_COST;
        if (!prepared)
        {
            String detail = buildStatus(player);
            boolean restored = restoreSnapshot(player);
            if (restored)
            {
                removeObjVar(player, ROOT);
            }
            return "error=fixtureMusicianMasterSetupFailed " +
                "restored=" + restored + " " + detail;
        }
        return "action=prepareMusicianMaster resumed=false " +
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
                MUSIC_ONE,
                MUSIC_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSIC_ONE) &&
            hasCommand(player, ROCK_ABILITY) &&
            hasCommand(player, FIZZ_ABILITY) &&
            hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 5 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSIC_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchase passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
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
                MUSIC_TWO,
                MUSIC_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSIC_TWO) &&
            hasCommand(player, STARWARS_TWO_ABILITY) &&
            hasCommand(player, PRIVATE_TWO_ABILITY) &&
            hasSchematic(player, FIZZ_SCHEMATIC) &&
            hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 5 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSIC_TWO_POINT_COST;
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
                MUSIC_THREE,
                MUSIC_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSIC_THREE) &&
            hasCommand(player, FOLK_ABILITY) &&
            hasCommand(player, FANFAR_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 5 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 90000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSIC_THREE_POINT_COST;
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
                MUSIC_FOUR,
                MUSIC_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSIC_FOUR) &&
            hasCommand(player, STARWARS_THREE_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 10 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 150000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSIC_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMaster(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMaster resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MASTER,
                MASTER_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MASTER) &&
            hasCommand(player, PRIVATE_MASTER_ABILITY) &&
            hasCommand(player, FOOTLOOSE_TWO_ABILITY) &&
            hasCommand(player, FORMAL_TWO_ABILITY) &&
            hasCommand(player, CEREMONIAL_ABILITY) &&
            hasCommand(player, MANDOVIOL_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 10 &&
            getSkillStatMod(player, DANCE_MOD) -
                getIntObjVar(player, BASE_DANCE_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 10 &&
            getSkillStatMod(player, DANCE_WOUND_MOD) -
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) == 10 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 150000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MASTER_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMaster passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianNovice(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianNovice resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_NOVICE,
                MUSICIAN_NOVICE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasSchematic(player, KLOO_HORN_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 5 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 5 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 10 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) == 10 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_NOVICE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianNovice passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianAbilityOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianAbilityOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_ABILITY_ONE,
                MUSICIAN_ABILITY_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLORLIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 10 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) == 10 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_ABILITY_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianAbilityOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianAbilityTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianAbilityTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_ABILITY_TWO,
                MUSICIAN_ABILITY_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLORLIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            hasSchematic(player, TRAZ_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 10 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) == 15 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_ABILITY_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianAbilityTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianAbilityThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianAbilityThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_ABILITY_THREE,
                MUSICIAN_ABILITY_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            hasSchematic(player, TRAZ_SCHEMATIC) &&
            hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 20 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) == 15 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_ABILITY_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianAbilityThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianAbilityFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianAbilityFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_ABILITY_FOUR,
                MUSICIAN_ABILITY_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            !hasCommand(player, NGE_FEATURED_SOLO_ABILITY) &&
            hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            hasSchematic(player, BANDFILL_SCHEMATIC) &&
            hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 25 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) == 25 &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_ABILITY_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianAbilityFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianWoundOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianWoundOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_WOUND_ONE,
                MUSICIAN_WOUND_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 5 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_WOUND_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianWoundOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianWoundTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianWoundTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_WOUND_TWO,
                MUSICIAN_WOUND_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_WOUND_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianWoundTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianWoundThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianWoundThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_WOUND_THREE,
                MUSICIAN_WOUND_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_WOUND_THREE) &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_WOUND_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianWoundThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianWoundFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianWoundFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_WOUND_FOUR,
                MUSICIAN_WOUND_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasSkill(player, MUSICIAN_WOUND_THREE) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 15 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_WOUND_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianWoundFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianShockOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianShockOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_SHOCK_ONE,
                MUSICIAN_SHOCK_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_SHOCK_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianShockOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianShockTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianShockTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_SHOCK_TWO,
                MUSICIAN_SHOCK_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_SHOCK_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianShockTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianShockThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianShockThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_SHOCK_THREE,
                MUSICIAN_SHOCK_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            !hasSchematic(player, NGE_FLUTEDROOPY_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 20 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_SHOCK_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianShockThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianShockFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianShockFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_SHOCK_FOUR,
                MUSICIAN_SHOCK_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 25 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_SHOCK_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianShockFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianKnowledgeOne(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianKnowledgeOne resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_KNOWLEDGE_ONE,
                MUSICIAN_KNOWLEDGE_ONE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 5 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_KNOWLEDGE_ONE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianKnowledgeOne passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianKnowledgeTwo(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianKnowledgeTwo resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_KNOWLEDGE_TWO,
                MUSICIAN_KNOWLEDGE_TWO_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            hasCommand(player, NGE_BANDFILL_ABILITY) &&
            hasCommand(player, FUNK_ABILITY) &&
            hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_KNOWLEDGE_TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianKnowledgeTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String purchaseMusicianKnowledgeThree(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianKnowledgeThree resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_KNOWLEDGE_THREE,
                MUSICIAN_KNOWLEDGE_THREE_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            hasCommand(player, WALTZ_ABILITY) &&
            hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            hasCommand(player, NGE_BANDFILL_ABILITY) &&
            hasCommand(player, FUNK_ABILITY) &&
            hasCommand(player, BALLAD_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 10 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_KNOWLEDGE_THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianKnowledgeThree passed=" +
            passed + " purchased=" + purchased + " " +
            buildStatus(player);
    }

    private String purchaseMusicianKnowledgeFour(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianKnowledgeFour resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_KNOWLEDGE_FOUR,
                MUSICIAN_KNOWLEDGE_FOUR_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            hasCommand(player, JAZZ_ABILITY) &&
            hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            hasCommand(player, WALTZ_ABILITY) &&
            hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 15 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_KNOWLEDGE_FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianKnowledgeFour passed=" +
            passed + " purchased=" + purchased + " " +
            buildStatus(player);
    }

    private String purchaseMusicianMaster(obj_id player)
        throws InterruptedException
    {
        if (readFlag(player, PURCHASED))
        {
            return "action=purchaseMusicianMaster resumed=true " +
                buildStatus(player);
        }
        boolean purchased =
            purchaseWithoutHolocron(
                player,
                MUSICIAN_MASTER,
                MUSICIAN_MASTER_POINT_COST);
        boolean passed =
            purchased &&
            hasSkill(player, MUSICIAN_MASTER) &&
            hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            hasCommand(player, VIRTUOSO_ABILITY) &&
            hasCommand(player, NALARGON_ABILITY) &&
            hasCommand(player, PLACE_CANTINA_ABILITY) &&
            hasCommand(player, PLACE_THEATER_ABILITY) &&
            hasCommand(player, JAZZ_ABILITY) &&
            hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasSchematic(player, NALARGON_SCHEMATIC) &&
            !hasSchematic(player, NALARGON_CLASSIC_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) -
                getIntObjVar(player, BASE_MUSIC_MOD) == 15 &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) -
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) == 15 &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) == 25 &&
            getSkillStatMod(player, MUSIC_MIND_MOD) -
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) == 25 &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) ==
                    25 &&
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
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    MUSICIAN_MASTER_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseMusicianMaster passed=" + passed +
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

    private String observeRockStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                866729052,
                "rock",
                SLITHERHORN_AUDIO_ID);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == ROCK_INDEX &&
            getPerformanceType(player) == ROCK_INDEX &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID &&
            getAttrib(player, ACTION) == START_ACTION;
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeRockStart passed=" + passed +
            " lookup=" + lookup + " " + buildStatus(player);
    }

    private String observeRockStopRequested(obj_id player)
        throws InterruptedException
    {
        return observeStopRequested(
            player,
            ROCK_INDEX,
            "observeRockStopRequested");
    }

    private String observeStopRequested(
        obj_id player,
        int performanceIndex,
        String action)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STARTED) &&
            getPerformanceType(player) == performanceIndex &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            hasScript(player, performance.POST_PERFORMANCE) &&
            hasObjVar(player, performance.VAR_PERFORM_OUTRO) &&
            getIntObjVar(
                player,
                performance.VAR_PERFORM_OUTRO) == 1;
        setObjVar(player, STOP_REQUESTED, passed ? 1 : 0);
        return "action=" + action + " passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeRockStopComplete(obj_id player)
        throws InterruptedException
    {
        return observeStopComplete(
            player,
            "observeRockStopComplete");
    }

    private String observeStopComplete(
        obj_id player,
        String action)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOP_REQUESTED) &&
            getPerformanceType(player) == 0 &&
            !hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            !hasScript(player, performance.POST_PERFORMANCE) &&
            !hasObjVar(player, performance.VAR_PERFORM_OUTRO);
        setObjVar(player, STOPPED, passed ? 1 : 0);
        return "action=" + action + " passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrender(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, MUSIC_ONE) &&
            !hasCommand(player, ROCK_ABILITY) &&
            !hasCommand(player, FIZZ_ABILITY) &&
            !hasCommand(player, PRIVATE_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrender passed=" + passed +
            " " + buildStatus(player);
    }

    private String observeStarwarsTwoStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                866729052,
                "starwars2",
                SLITHERHORN_AUDIO_ID);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == STARWARS_TWO_INDEX &&
            getPerformanceType(player) == STARWARS_TWO_INDEX &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID &&
            getAttrib(player, ACTION) == START_ACTION;
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeStarwarsTwoStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeStarwarsTwoStopRequested(obj_id player)
        throws InterruptedException
    {
        return observeStopRequested(
            player,
            STARWARS_TWO_INDEX,
            "observeStarwarsTwoStopRequested");
    }

    private String observeStarwarsTwoStopComplete(obj_id player)
        throws InterruptedException
    {
        return observeStopComplete(
            player,
            "observeStarwarsTwoStopComplete");
    }

    private String observeSurrenderTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, MUSIC_TWO) &&
            hasSkill(player, MUSIC_ONE) &&
            !hasCommand(player, STARWARS_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            !hasSchematic(player, FIZZ_SCHEMATIC) &&
            !hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 10000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeFolkStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                866729052,
                "folk",
                SLITHERHORN_AUDIO_ID);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == FOLK_INDEX &&
            getPerformanceType(player) == FOLK_INDEX &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID &&
            getAttrib(player, ACTION) == START_ACTION;
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeFolkStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeFolkStopRequested(obj_id player)
        throws InterruptedException
    {
        return observeStopRequested(
            player,
            FOLK_INDEX,
            "observeFolkStopRequested");
    }

    private String observeFolkStopComplete(obj_id player)
        throws InterruptedException
    {
        return observeStopComplete(
            player,
            "observeFolkStopComplete");
    }

    private String observeSurrenderThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, MUSIC_THREE) &&
            hasSkill(player, MUSIC_TWO) &&
            !hasCommand(player, FOLK_ABILITY) &&
            !hasCommand(player, FANFAR_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            hasSchematic(player, FIZZ_SCHEMATIC) &&
            hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeStarwarsThreeStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                866729052,
                "starwars3",
                SLITHERHORN_AUDIO_ID);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == STARWARS_THREE_INDEX &&
            getPerformanceType(player) == STARWARS_THREE_INDEX &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID &&
            getAttrib(player, ACTION) == START_ACTION;
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeStarwarsThreeStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeStarwarsThreeStopRequested(obj_id player)
        throws InterruptedException
    {
        return observeStopRequested(
            player,
            STARWARS_THREE_INDEX,
            "observeStarwarsThreeStopRequested");
    }

    private String observeStarwarsThreeStopComplete(obj_id player)
        throws InterruptedException
    {
        return observeStopComplete(
            player,
            "observeStarwarsThreeStopComplete");
    }

    private String observeSurrenderFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, MUSIC_THREE) &&
            !hasCommand(player, STARWARS_THREE_ABILITY) &&
            !hasCommand(player, KLOOHORN_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            hasCommand(player, FOLK_ABILITY) &&
            hasCommand(player, FANFAR_ABILITY) &&
            hasCommand(player, PRIVATE_THREE_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 90000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeCeremonialStart(obj_id player)
        throws InterruptedException
    {
        int lookup =
            performance.lookupPerformanceIndex(
                866729052,
                "ceremonial",
                SLITHERHORN_AUDIO_ID);
        boolean passed =
            readFlag(player, PURCHASED) &&
            lookup == CEREMONIAL_INDEX &&
            getPerformanceType(player) == CEREMONIAL_INDEX &&
            hasScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT) &&
            getInstrumentAudioId(player) ==
                SLITHERHORN_AUDIO_ID &&
            (getAttrib(player, ACTION) == START_ACTION ||
                getAttrib(player, ACTION) ==
                    CEREMONIAL_FIRST_LOOP_ACTION);
        setObjVar(player, STARTED, passed ? 1 : 0);
        return "action=observeCeremonialStart passed=" +
            passed + " lookup=" + lookup + " " +
            buildStatus(player);
    }

    private String observeCeremonialStopRequested(obj_id player)
        throws InterruptedException
    {
        return observeStopRequested(
            player,
            CEREMONIAL_INDEX,
            "observeCeremonialStopRequested");
    }

    private String observeCeremonialStopComplete(obj_id player)
        throws InterruptedException
    {
        return observeStopComplete(
            player,
            "observeCeremonialStopComplete");
    }

    private String observeSurrenderMaster(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, STOPPED) &&
            !hasSkill(player, MASTER) &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HAIRSTYLE_FOUR) &&
            hasSkill(player, DANCE_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasCommand(player, PRIVATE_MASTER_ABILITY) &&
            !hasCommand(player, FOOTLOOSE_TWO_ABILITY) &&
            !hasCommand(player, FORMAL_TWO_ABILITY) &&
            !hasCommand(player, CEREMONIAL_ABILITY) &&
            !hasCommand(player, MANDOVIOL_ABILITY) &&
            hasCommand(player, STARWARS_THREE_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, DANCE_MOD) ==
                getIntObjVar(player, BASE_DANCE_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, DANCE_WOUND_MOD) ==
                getIntObjVar(player, BASE_DANCE_WOUND_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 150000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMaster passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianNovice(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasCommand(player, TRAZ_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, STARWARS_THREE_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasSchematic(player, KLOO_HORN_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 150000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianNovice passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianAbilityOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasSkill(player, MUSIC_FOUR) &&
            hasSkill(player, HEALING_FOUR) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLORLIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianAbilityOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianAbilityTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            hasCommand(player, SPOTLIGHT_ABILITY) &&
            hasCommand(player, COLORLIGHTS_ABILITY) &&
            hasCommand(player, DAZZLE_ABILITY) &&
            hasCommand(player, TRAZ_ABILITY) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianAbilityTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianAbilityThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            !hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            hasSchematic(player, TRAZ_SCHEMATIC) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianAbilityThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianAbilityFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            !hasCommand(player, NGE_FEATURED_SOLO_ABILITY) &&
            hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            hasSchematic(player, BANDFILL_SCHEMATIC) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianAbilityFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianWoundOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            !hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianWoundOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianWoundTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianWoundTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianWoundThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_WOUND_THREE) &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianWoundThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianWoundFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasSkill(player, MUSICIAN_WOUND_THREE) &&
            hasSkill(player, MUSICIAN_WOUND_TWO) &&
            hasSkill(player, MUSICIAN_WOUND_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianWoundFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianShockOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasCommand(player, KLOOHORN_ABILITY) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianShockOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianShockTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            hasCommand(player, TRAZ_ABILITY) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 200000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianShockTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianShockThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            !hasSchematic(player, NGE_FLUTEDROOPY_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 400000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianShockThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianShockFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianShockFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianKnowledgeOne(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            hasCommand(player, TRAZ_ABILITY) &&
            hasCommand(player, REGISTER_ABILITY) &&
            hasCommand(player, STARWARS_FOUR_ABILITY) &&
            hasSchematic(player, KLOO_HORN_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 350000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianKnowledgeOne passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianKnowledgeTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            hasSkill(player, MUSICIAN_NOVICE) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasCommand(player, FUNK_ABILITY) &&
            hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 500000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianKnowledgeTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianKnowledgeThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            !hasCommand(player, WALTZ_ABILITY) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            hasCommand(player, NGE_BANDFILL_ABILITY) &&
            hasCommand(player, FUNK_ABILITY) &&
            hasCommand(player, BALLAD_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 700000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianKnowledgeThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianKnowledgeFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            !hasCommand(player, JAZZ_ABILITY) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            hasCommand(player, WALTZ_ABILITY) &&
            hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 75000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianKnowledgeFour passed=" +
            passed + " " + buildStatus(player);
    }

    private String observeSurrenderMusicianMaster(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            readFlag(player, PURCHASED) &&
            !hasSkill(player, MUSICIAN_MASTER) &&
            hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            !hasCommand(player, VIRTUOSO_ABILITY) &&
            !hasCommand(player, NALARGON_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            hasCommand(player, JAZZ_ABILITY) &&
            hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasSchematic(player, NALARGON_SCHEMATIC) &&
            !hasSchematic(player, NALARGON_CLASSIC_SCHEMATIC) &&
            getSkillStatMod(player, MUSIC_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MOD) &&
            getSkillStatMod(player, MUSIC_WOUND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            getSkillStatMod(player, MUSIC_SHOCK_MOD) ==
                getIntObjVar(player, BASE_MUSIC_SHOCK_MOD) &&
            getSkillStatMod(player, MUSIC_MIND_MOD) ==
                getIntObjVar(player, BASE_MUSIC_MIND_MOD) &&
            getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) ==
                getIntObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD) &&
            getSkillStatMod(player, MELEE_DEFENSE_MOD) ==
                getIntObjVar(player, BASE_MELEE_DEFENSE_MOD) &&
            getSkillStatMod(player, RANGED_DEFENSE_MOD) ==
                getIntObjVar(player, BASE_RANGED_DEFENSE_MOD) &&
            getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD) ==
                getIntObjVar(player, BASE_PRIVATE_PLACE_CANTINA_MOD) &&
            getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD) ==
                getIntObjVar(player, BASE_PRIVATE_PLACE_THEATER_MOD) &&
            getExperiencePoints(player, MUSIC_XP) == 0 &&
            getExperiencePoints(player, HEALING_XP) == 0 &&
            getExperienceCap(player, MUSIC_XP) == 900000 &&
            getExperienceCap(player, HEALING_XP) == 500000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderMusicianMaster passed=" +
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
            " musicXp=" + getExperiencePoints(player, MUSIC_XP) +
            " healingXp=" + getExperiencePoints(player, HEALING_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player) +
            " performance=" + getPerformanceType(player) +
            " instrumentAudio=" + getInstrumentAudioId(player);
    }

    private boolean restoreSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        clearPerformanceState(player);
        boolean instrumentRemoved = removeFixtureInstrument(player);
        if (hasSkill(player, MUSICIAN_MASTER))
        {
            revokeSkill(player, MUSICIAN_MASTER);
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR))
        {
            revokeSkill(player, MUSICIAN_KNOWLEDGE_FOUR);
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_THREE))
        {
            revokeSkill(player, MUSICIAN_KNOWLEDGE_THREE);
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_TWO))
        {
            revokeSkill(player, MUSICIAN_KNOWLEDGE_TWO);
        }
        if (hasSkill(player, MUSICIAN_KNOWLEDGE_ONE))
        {
            revokeSkill(player, MUSICIAN_KNOWLEDGE_ONE);
        }
        if (hasSkill(player, MUSICIAN_SHOCK_FOUR))
        {
            revokeSkill(player, MUSICIAN_SHOCK_FOUR);
        }
        if (hasSkill(player, MUSICIAN_SHOCK_THREE))
        {
            revokeSkill(player, MUSICIAN_SHOCK_THREE);
        }
        if (hasSkill(player, MUSICIAN_SHOCK_TWO))
        {
            revokeSkill(player, MUSICIAN_SHOCK_TWO);
        }
        if (hasSkill(player, MUSICIAN_SHOCK_ONE))
        {
            revokeSkill(player, MUSICIAN_SHOCK_ONE);
        }
        if (hasSkill(player, MUSICIAN_WOUND_FOUR))
        {
            revokeSkill(player, MUSICIAN_WOUND_FOUR);
        }
        if (hasSkill(player, MUSICIAN_WOUND_THREE))
        {
            revokeSkill(player, MUSICIAN_WOUND_THREE);
        }
        if (hasSkill(player, MUSICIAN_WOUND_TWO))
        {
            revokeSkill(player, MUSICIAN_WOUND_TWO);
        }
        if (hasSkill(player, MUSICIAN_WOUND_ONE))
        {
            revokeSkill(player, MUSICIAN_WOUND_ONE);
        }
        if (hasSkill(player, MUSICIAN_ABILITY_FOUR))
        {
            revokeSkill(player, MUSICIAN_ABILITY_FOUR);
        }
        if (hasSkill(player, MUSICIAN_ABILITY_THREE))
        {
            revokeSkill(player, MUSICIAN_ABILITY_THREE);
        }
        if (hasSkill(player, MUSICIAN_ABILITY_TWO))
        {
            revokeSkill(player, MUSICIAN_ABILITY_TWO);
        }
        if (hasSkill(player, MUSICIAN_ABILITY_ONE))
        {
            revokeSkill(player, MUSICIAN_ABILITY_ONE);
        }
        if (hasSkill(player, MUSICIAN_NOVICE))
        {
            revokeSkill(player, MUSICIAN_NOVICE);
        }
        if (hasSkill(player, MASTER))
        {
            revokeSkill(player, MASTER);
        }
        if (hasSkill(player, HAIRSTYLE_FOUR))
        {
            revokeSkill(player, HAIRSTYLE_FOUR);
        }
        if (hasSkill(player, DANCE_FOUR))
        {
            revokeSkill(player, DANCE_FOUR);
        }
        if (hasSkill(player, HEALING_FOUR))
        {
            revokeSkill(player, HEALING_FOUR);
        }
        if (hasSkill(player, MUSIC_FOUR))
        {
            revokeSkill(player, MUSIC_FOUR);
        }
        if (hasSkill(player, MUSIC_THREE))
        {
            revokeSkill(player, MUSIC_THREE);
        }
        if (hasSkill(player, MUSIC_TWO))
        {
            revokeSkill(player, MUSIC_TWO);
        }
        if (hasSkill(player, MUSIC_ONE))
        {
            revokeSkill(player, MUSIC_ONE);
        }
        if (hasSkill(player, NOVICE))
        {
            revokeSkill(player, NOVICE);
        }
        boolean xpRestored =
            setXpExact(
                player,
                MUSIC_XP,
                getIntObjVar(player, ORIGINAL_MUSIC_XP));
        boolean healingXpRestored =
            setXpExact(
                player,
                HEALING_XP,
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
            instrumentRemoved &&
            xpRestored &&
            healingXpRestored &&
            quicknessRestored &&
            actionRestored &&
            locomotionRestored &&
            postureRestored &&
            !hasSkill(player, MUSIC_ONE) &&
            !hasSkill(player, MUSIC_TWO) &&
            !hasSkill(player, MUSIC_THREE) &&
            !hasSkill(player, MUSIC_FOUR) &&
            !hasSkill(player, HAIRSTYLE_FOUR) &&
            !hasSkill(player, DANCE_FOUR) &&
            !hasSkill(player, HEALING_FOUR) &&
            !hasSkill(player, MASTER) &&
            !hasSkill(player, MUSICIAN_NOVICE) &&
            !hasSkill(player, MUSICIAN_ABILITY_ONE) &&
            !hasSkill(player, MUSICIAN_ABILITY_TWO) &&
            !hasSkill(player, MUSICIAN_ABILITY_THREE) &&
            !hasSkill(player, MUSICIAN_ABILITY_FOUR) &&
            !hasSkill(player, MUSICIAN_WOUND_ONE) &&
            !hasSkill(player, MUSICIAN_WOUND_TWO) &&
            !hasSkill(player, MUSICIAN_WOUND_THREE) &&
            !hasSkill(player, MUSICIAN_WOUND_FOUR) &&
            !hasSkill(player, MUSICIAN_SHOCK_ONE) &&
            !hasSkill(player, MUSICIAN_SHOCK_TWO) &&
            !hasSkill(player, MUSICIAN_SHOCK_THREE) &&
            !hasSkill(player, MUSICIAN_SHOCK_FOUR) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) &&
            !hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) &&
            !hasSkill(player, MUSICIAN_MASTER) &&
            !hasSkill(player, NOVICE) &&
            !hasCommand(player, ROCK_ABILITY) &&
            !hasCommand(player, FIZZ_ABILITY) &&
            !hasCommand(player, STARWARS_TWO_ABILITY) &&
            !hasCommand(player, PRIVATE_TWO_ABILITY) &&
            !hasCommand(player, FOLK_ABILITY) &&
            !hasCommand(player, FANFAR_ABILITY) &&
            !hasCommand(player, PRIVATE_THREE_ABILITY) &&
            !hasCommand(player, STARWARS_THREE_ABILITY) &&
            !hasCommand(player, KLOOHORN_ABILITY) &&
            !hasCommand(player, PRIVATE_FOUR_ABILITY) &&
            !hasCommand(player, PRIVATE_MASTER_ABILITY) &&
            !hasCommand(player, FOOTLOOSE_TWO_ABILITY) &&
            !hasCommand(player, FORMAL_TWO_ABILITY) &&
            !hasCommand(player, CEREMONIAL_ABILITY) &&
            !hasCommand(player, MANDOVIOL_ABILITY) &&
            !hasCommand(player, TRAZ_ABILITY) &&
            !hasCommand(player, REGISTER_ABILITY) &&
            !hasCommand(player, STARWARS_FOUR_ABILITY) &&
            !hasCommand(player, SPOTLIGHT_ABILITY) &&
            !hasCommand(player, COLORLIGHTS_ABILITY) &&
            !hasCommand(player, DAZZLE_ABILITY) &&
            !hasCommand(player, FIREJET_ABILITY) &&
            !hasCommand(player, NGE_LASER_SHOW_ABILITY) &&
            !hasCommand(player, VENTRILOQUISM_ABILITY) &&
            !hasCommand(player, NGE_FIREJET_TWO_ABILITY) &&
            !hasCommand(player, NGE_FEATURED_SOLO_ABILITY) &&
            !hasCommand(player, NGE_BANDFILL_ABILITY) &&
            !hasCommand(player, NGE_FLUTEDROOPY_ABILITY) &&
            !hasCommand(player, NGE_OMNIBOX_ABILITY) &&
            !hasCommand(player, BALLAD_ABILITY) &&
            !hasCommand(player, NGE_SWING_ABILITY) &&
            !hasCommand(player, FUNK_ABILITY) &&
            !hasCommand(player, WALTZ_ABILITY) &&
            !hasCommand(player, JAZZ_ABILITY) &&
            !hasCommand(player, VIRTUOSO_ABILITY) &&
            !hasCommand(player, NALARGON_ABILITY) &&
            !hasCommand(player, PLACE_CANTINA_ABILITY) &&
            !hasCommand(player, PLACE_THEATER_ABILITY) &&
            !hasSchematic(player, FIZZ_SCHEMATIC) &&
            !hasSchematic(player, FIZZ_CLASSIC_SCHEMATIC) &&
            !hasSchematic(player, KLOO_HORN_SCHEMATIC) &&
            !hasSchematic(player, TRAZ_SCHEMATIC) &&
            !hasSchematic(player, BANDFILL_SCHEMATIC) &&
            !hasSchematic(player, OMNI_BOX_SCHEMATIC) &&
            !hasSchematic(player, NGE_FLUTEDROOPY_SCHEMATIC) &&
            !hasSchematic(player, NALARGON_SCHEMATIC) &&
            !hasSchematic(player, NALARGON_CLASSIC_SCHEMATIC) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) &&
            getPerformanceType(player) == 0 &&
            getInstrumentAudioId(player) ==
                getIntObjVar(
                    player,
                    ORIGINAL_INSTRUMENT_AUDIO);
    }

    private void clearPerformanceState(obj_id player)
        throws InterruptedException
    {
        if (hasScript(
            player,
            performance.MUSIC_HEARTBEAT_SCRIPT))
        {
            detachScript(
                player,
                performance.MUSIC_HEARTBEAT_SCRIPT);
        }
        if (hasScript(player, performance.POST_PERFORMANCE))
        {
            detachScript(player, performance.POST_PERFORMANCE);
        }
        setPerformanceType(player, 0);
        setPerformanceStartTime(player, 0);
        setClientUsesAnimationLocomotion(player, false);
        if (hasObjVar(player, performance.VAR_PERFORM))
        {
            removeObjVar(player, performance.VAR_PERFORM);
        }
        if (hasObjVar(player, performance.VAR_PERFORM_OUTRO))
        {
            removeObjVar(player, performance.VAR_PERFORM_OUTRO);
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

    private boolean removeFixtureInstrument(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, FIXTURE_INSTRUMENT))
        {
            return getInstrumentAudioId(player) ==
                getIntObjVar(player, ORIGINAL_INSTRUMENT_AUDIO);
        }
        obj_id instrument =
            getObjIdObjVar(player, FIXTURE_INSTRUMENT);
        if (isIdValid(instrument) && instrument.isLoaded())
        {
            obj_id inventory = utils.getInventoryContainer(player);
            if (isIdValid(inventory))
            {
                putInOverloaded(instrument, inventory);
            }
            if (!destroyObject(instrument))
            {
                return false;
            }
        }
        removeObjVar(player, FIXTURE_INSTRUMENT);
        return getInstrumentAudioId(player) ==
            getIntObjVar(player, ORIGINAL_INSTRUMENT_AUDIO);
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
        int baseMod = hasObjVar(player, BASE_MUSIC_MOD)
            ? getIntObjVar(player, BASE_MUSIC_MOD)
            : getSkillStatMod(player, MUSIC_MOD);
        int baseDanceMod = hasObjVar(player, BASE_DANCE_MOD)
            ? getIntObjVar(player, BASE_DANCE_MOD)
            : getSkillStatMod(player, DANCE_MOD);
        int baseMusicWoundMod =
            hasObjVar(player, BASE_MUSIC_WOUND_MOD)
                ? getIntObjVar(player, BASE_MUSIC_WOUND_MOD)
                : getSkillStatMod(player, MUSIC_WOUND_MOD);
        int baseDanceWoundMod =
            hasObjVar(player, BASE_DANCE_WOUND_MOD)
                ? getIntObjVar(player, BASE_DANCE_WOUND_MOD)
                : getSkillStatMod(player, DANCE_WOUND_MOD);
        int baseMusicShockMod =
            hasObjVar(player, BASE_MUSIC_SHOCK_MOD)
                ? getIntObjVar(player, BASE_MUSIC_SHOCK_MOD)
                : getSkillStatMod(player, MUSIC_SHOCK_MOD);
        int baseMusicMindMod =
            hasObjVar(player, BASE_MUSIC_MIND_MOD)
                ? getIntObjVar(player, BASE_MUSIC_MIND_MOD)
                : getSkillStatMod(player, MUSIC_MIND_MOD);
        int baseInstrumentAssemblyMod =
            hasObjVar(player, BASE_INSTRUMENT_ASSEMBLY_MOD)
                ? getIntObjVar(
                    player,
                    BASE_INSTRUMENT_ASSEMBLY_MOD)
                : getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD);
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
            " musicOne=" +
                (hasSkill(player, MUSIC_ONE) ? "1" : "0") +
            " musicTwo=" +
                (hasSkill(player, MUSIC_TWO) ? "1" : "0") +
            " musicThree=" +
                (hasSkill(player, MUSIC_THREE) ? "1" : "0") +
            " musicFour=" +
                (hasSkill(player, MUSIC_FOUR) ? "1" : "0") +
            " masterPrerequisites=" +
                (hasSkill(player, HAIRSTYLE_FOUR) ? "1" : "0") +
                (hasSkill(player, MUSIC_FOUR) ? "1" : "0") +
                (hasSkill(player, DANCE_FOUR) ? "1" : "0") +
                (hasSkill(player, HEALING_FOUR) ? "1" : "0") +
            " master=" +
                (hasSkill(player, MASTER) ? "1" : "0") +
            " musicianNovice=" +
                (hasSkill(player, MUSICIAN_NOVICE) ? "1" : "0") +
            " musicianAbilityOne=" +
                (hasSkill(player, MUSICIAN_ABILITY_ONE) ? "1" : "0") +
            " musicianAbilityTwo=" +
                (hasSkill(player, MUSICIAN_ABILITY_TWO) ? "1" : "0") +
            " musicianAbilityThree=" +
                (hasSkill(player, MUSICIAN_ABILITY_THREE) ? "1" : "0") +
            " musicianAbilityFour=" +
                (hasSkill(player, MUSICIAN_ABILITY_FOUR) ? "1" : "0") +
            " musicianWoundOne=" +
                (hasSkill(player, MUSICIAN_WOUND_ONE) ? "1" : "0") +
            " musicianWoundTwo=" +
                (hasSkill(player, MUSICIAN_WOUND_TWO) ? "1" : "0") +
            " musicianWoundThree=" +
                (hasSkill(player, MUSICIAN_WOUND_THREE) ? "1" : "0") +
            " musicianWoundFour=" +
                (hasSkill(player, MUSICIAN_WOUND_FOUR) ? "1" : "0") +
            " musicianShockOne=" +
                (hasSkill(player, MUSICIAN_SHOCK_ONE) ? "1" : "0") +
            " musicianShockTwo=" +
                (hasSkill(player, MUSICIAN_SHOCK_TWO) ? "1" : "0") +
            " musicianShockThree=" +
                (hasSkill(player, MUSICIAN_SHOCK_THREE) ? "1" : "0") +
            " musicianShockFour=" +
                (hasSkill(player, MUSICIAN_SHOCK_FOUR) ? "1" : "0") +
            " musicianKnowledgeOne=" +
                (hasSkill(player, MUSICIAN_KNOWLEDGE_ONE) ? "1" : "0") +
            " musicianKnowledgeTwo=" +
                (hasSkill(player, MUSICIAN_KNOWLEDGE_TWO) ? "1" : "0") +
            " musicianKnowledgeThree=" +
                (hasSkill(player, MUSICIAN_KNOWLEDGE_THREE) ? "1" : "0") +
            " musicianKnowledgeFour=" +
                (hasSkill(player, MUSICIAN_KNOWLEDGE_FOUR) ? "1" : "0") +
            " musicianMaster=" +
                (hasSkill(player, MUSICIAN_MASTER) ? "1" : "0") +
            " commands=" +
                (hasCommand(player, ROCK_ABILITY) ? "1" : "0") +
                (hasCommand(player, FIZZ_ABILITY) ? "1" : "0") +
                (hasCommand(player, PRIVATE_ABILITY) ? "1" : "0") +
            " commandsTwo=" +
                (hasCommand(
                    player,
                    STARWARS_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_TWO_ABILITY) ? "1" : "0") +
            " schematicsTwo=" +
                (hasSchematic(
                    player,
                    FIZZ_SCHEMATIC) ? "1" : "0") +
                (hasSchematic(
                    player,
                    FIZZ_CLASSIC_SCHEMATIC) ? "1" : "0") +
            " commandsThree=" +
                (hasCommand(player, FOLK_ABILITY) ? "1" : "0") +
                (hasCommand(player, FANFAR_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_THREE_ABILITY) ? "1" : "0") +
            " commandsFour=" +
                (hasCommand(
                    player,
                    STARWARS_THREE_ABILITY) ? "1" : "0") +
                (hasCommand(player, KLOOHORN_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    PRIVATE_FOUR_ABILITY) ? "1" : "0") +
            " commandsMaster=" +
                (hasCommand(
                    player,
                    PRIVATE_MASTER_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    FOOTLOOSE_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    FORMAL_TWO_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    CEREMONIAL_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    MANDOVIOL_ABILITY) ? "1" : "0") +
            " commandsMusicianNovice=" +
                (hasCommand(player, TRAZ_ABILITY) ? "1" : "0") +
                (hasCommand(player, REGISTER_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    STARWARS_FOUR_ABILITY) ? "1" : "0") +
            " schematicsMusicianNovice=" +
                (hasSchematic(
                    player,
                    KLOO_HORN_SCHEMATIC) ? "1" : "0") +
            " commandsMusicianAbilityOne=" +
                (hasCommand(player, SPOTLIGHT_ABILITY) ? "1" : "0") +
                (hasCommand(player, COLORLIGHTS_ABILITY) ? "1" : "0") +
                (hasCommand(player, DAZZLE_ABILITY) ? "1" : "0") +
            " commandsMusicianAbilityTwo=" +
                (hasCommand(player, FIREJET_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_LASER_SHOW_ABILITY) ? "1" : "0") +
            " schematicsMusicianAbilityTwo=" +
                (hasSchematic(player, TRAZ_SCHEMATIC) ? "1" : "0") +
            " commandsMusicianAbilityThree=" +
                (hasCommand(
                    player,
                    VENTRILOQUISM_ABILITY) ? "1" : "0") +
                (hasCommand(
                    player,
                    NGE_FIREJET_TWO_ABILITY) ? "1" : "0") +
            " schematicsMusicianAbilityThree=" +
                (hasSchematic(player, BANDFILL_SCHEMATIC) ? "1" : "0") +
            " commandsMusicianAbilityFour=" +
                (hasCommand(
                    player,
                    NGE_FEATURED_SOLO_ABILITY) ? "1" : "0") +
            " schematicsMusicianAbilityFour=" +
                (hasSchematic(player, OMNI_BOX_SCHEMATIC) ? "1" : "0") +
            " ngeMusicianShockTwoCommand=" +
                (hasCommand(player, NGE_BANDFILL_ABILITY) ? "1" : "0") +
            " ngeMusicianShockThreeCommand=" +
                (hasCommand(player, NGE_FLUTEDROOPY_ABILITY) ? "1" : "0") +
            " ngeMusicianShockFourCommand=" +
                (hasCommand(player, NGE_OMNIBOX_ABILITY) ? "1" : "0") +
            " commandsMusicianKnowledgeOne=" +
                (hasCommand(player, BALLAD_ABILITY) ? "1" : "0") +
                (hasCommand(player, NGE_SWING_ABILITY) ? "1" : "0") +
            " commandsMusicianKnowledgeTwo=" +
                (hasCommand(player, NGE_BANDFILL_ABILITY) ? "1" : "0") +
                (hasCommand(player, FUNK_ABILITY) ? "1" : "0") +
            " commandsMusicianKnowledgeThree=" +
                (hasCommand(player, WALTZ_ABILITY) ? "1" : "0") +
                (hasCommand(player, NGE_FLUTEDROOPY_ABILITY) ? "1" : "0") +
            " commandsMusicianKnowledgeFour=" +
                (hasCommand(player, JAZZ_ABILITY) ? "1" : "0") +
                (hasCommand(player, NGE_OMNIBOX_ABILITY) ? "1" : "0") +
            " commandsMusicianMaster=" +
                (hasCommand(player, VIRTUOSO_ABILITY) ? "1" : "0") +
                (hasCommand(player, NALARGON_ABILITY) ? "1" : "0") +
                (hasCommand(player, PLACE_CANTINA_ABILITY) ? "1" : "0") +
                (hasCommand(player, PLACE_THEATER_ABILITY) ? "1" : "0") +
            " schematicsMusicianMaster=" +
                (hasSchematic(player, NALARGON_SCHEMATIC) ? "1" : "0") +
                (hasSchematic(
                    player,
                    NALARGON_CLASSIC_SCHEMATIC) ? "1" : "0") +
            " musicModDelta=" +
                (getSkillStatMod(player, MUSIC_MOD) - baseMod) +
            " danceModDelta=" +
                (getSkillStatMod(player, DANCE_MOD) -
                    baseDanceMod) +
            " musicWoundModDelta=" +
                (getSkillStatMod(player, MUSIC_WOUND_MOD) -
                    baseMusicWoundMod) +
            " danceWoundModDelta=" +
                (getSkillStatMod(player, DANCE_WOUND_MOD) -
                    baseDanceWoundMod) +
            " musicShockModDelta=" +
                (getSkillStatMod(player, MUSIC_SHOCK_MOD) -
                    baseMusicShockMod) +
            " musicMindModDelta=" +
                (getSkillStatMod(player, MUSIC_MIND_MOD) -
                    baseMusicMindMod) +
            " instrumentAssemblyModDelta=" +
                (getSkillStatMod(player, INSTRUMENT_ASSEMBLY_MOD) -
                    baseInstrumentAssemblyMod) +
            " meleeDefenseModDelta=" +
                (getSkillStatMod(player, MELEE_DEFENSE_MOD) -
                    baseMeleeDefense) +
            " rangedDefenseModDelta=" +
                (getSkillStatMod(player, RANGED_DEFENSE_MOD) -
                    baseRangedDefense) +
            " privatePlaceCantinaModDelta=" +
                (getSkillStatMod(player, PRIVATE_PLACE_CANTINA_MOD) -
                    basePlaceCantina) +
            " privatePlaceTheaterModDelta=" +
                (getSkillStatMod(player, PRIVATE_PLACE_THEATER_MOD) -
                    basePlaceTheater) +
            " musicXp=" + getExperiencePoints(player, MUSIC_XP) +
            " healingXp=" + getExperiencePoints(player, HEALING_XP) +
            " musicCap=" + getExperienceCap(player, MUSIC_XP) +
            " healingCap=" + getExperienceCap(player, HEALING_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player) +
            " performance=" + getPerformanceType(player) +
            " action=" + getAttrib(player, ACTION) +
            " instrumentAudio=" + getInstrumentAudioId(player) +
            " purchased=" + readFlag(player, PURCHASED) +
            " started=" + readFlag(player, STARTED) +
            " stopRequested=" + readFlag(player, STOP_REQUESTED) +
            " stopped=" + readFlag(player, STOPPED) +
            " surrendered=" + readFlag(player, SURRENDERED);
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, LIFECYCLE) &&
            hasObjVar(player, ORIGINAL_MUSIC_XP) &&
            hasObjVar(player, ORIGINAL_HEALING_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_QUICKNESS) &&
            hasObjVar(player, ORIGINAL_ACTION_REGEN) &&
            hasObjVar(player, ORIGINAL_POSTURE) &&
            hasObjVar(player, ORIGINAL_LOCOMOTION) &&
            hasObjVar(player, ORIGINAL_INSTRUMENT_AUDIO) &&
            hasObjVar(player, BASE_POINTS) &&
            hasObjVar(player, BASE_MUSIC_MOD) &&
            hasObjVar(player, BASE_DANCE_MOD) &&
            hasObjVar(player, BASE_MUSIC_WOUND_MOD) &&
            hasObjVar(player, BASE_DANCE_WOUND_MOD);
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
        setObjVar(player, STOP_REQUESTED, 0);
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
