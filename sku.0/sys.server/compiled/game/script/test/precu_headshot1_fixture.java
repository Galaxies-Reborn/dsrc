package script.test;

import script.dictionary;
import script.location;
import script.obj_id;
import script.combat_engine;
import script.combat_engine.combat_data;
import script.combat_engine.weapon_data;
import script.library.ai_lib;
import script.library.buff;
import script.library.combat;
import script.library.create;
import script.library.dot;
import script.library.utils;
import script.systems.combat.combat_base;

/**
 * ServerConsole-only, identity-bound live fixture for the first Publish 14.1
 * combat-command slice. It owns reversible skill, location, PvP, and current
 * HAM preparation, but never queues the combat command or fabricates damage.
 */
public class precu_headshot1_fixture extends script.base_script
{
    private static final long ATTACKER_OID = 44003778L;
    private static final int ATTACKER_STATION_ID = 91001;
    private static final long DEFENDER_OID = 39008597L;
    private static final int DEFENDER_STATION_ID = 1001;
    private static final String MARKSMAN_NOVICE = "combat_marksman_novice";
    private static final String RIFLE_ONE = "combat_marksman_rifle_01";
    private static final String RIFLE_TWO = "combat_marksman_rifle_02";
    private static final String RIFLE_THREE = "combat_marksman_rifle_03";
    private static final String RIFLE_FOUR = "combat_marksman_rifle_04";
    private static final String RIFLEMAN_NOVICE = "combat_rifleman_novice";
    private static final String BRAWLER_ROOT = "combat_brawler";
    private static final String BRAWLER_NOVICE = "combat_brawler_novice";
    private static final String BRAWLER_ONE_HAND_ONE =
        "combat_brawler_1handmelee_01";
    private static final String BRAWLER_ONE_HAND_TWO =
        "combat_brawler_1handmelee_02";
    private static final String BRAWLER_ONE_HAND_THREE =
        "combat_brawler_1handmelee_03";
    private static final String BRAWLER_ONE_HAND_FOUR =
        "combat_brawler_1handmelee_04";
    private static final String BRAWLER_TWO_HAND_ONE =
        "combat_brawler_2handmelee_01";
    private static final String BRAWLER_TWO_HAND_TWO =
        "combat_brawler_2handmelee_02";
    private static final String BRAWLER_TWO_HAND_THREE =
        "combat_brawler_2handmelee_03";
    private static final String BRAWLER_TWO_HAND_FOUR =
        "combat_brawler_2handmelee_04";
    private static final String TWO_HAND_SWORD_NOVICE =
        "combat_2hsword_novice";
    private static final String TWO_HAND_SWORD_ACCURACY_ONE =
        "combat_2hsword_accuracy_01";
    private static final String TWO_HAND_SWORD_ACCURACY_TWO =
        "combat_2hsword_accuracy_02";
    private static final String TWO_HAND_SWORD_ACCURACY_THREE =
        "combat_2hsword_accuracy_03";
    private static final String TWO_HAND_SWORD_ACCURACY_FOUR =
        "combat_2hsword_accuracy_04";
    private static final String TWO_HAND_SWORD_SPEED_ONE =
        "combat_2hsword_speed_01";
    private static final String TWO_HAND_SWORD_SPEED_TWO =
        "combat_2hsword_speed_02";
    private static final String TWO_HAND_SWORD_SPEED_THREE =
        "combat_2hsword_speed_03";
    private static final String TWO_HAND_SWORD_SPEED_FOUR =
        "combat_2hsword_speed_04";
    private static final String TWO_HAND_SWORD_ABILITY_ONE =
        "combat_2hsword_ability_01";
    private static final String TWO_HAND_SWORD_ABILITY_TWO =
        "combat_2hsword_ability_02";
    private static final String TWO_HAND_SWORD_ABILITY_THREE =
        "combat_2hsword_ability_03";
    private static final String TWO_HAND_SWORD_ABILITY_FOUR =
        "combat_2hsword_ability_04";
    private static final String TWO_HAND_SWORD_SUPPORT_ONE =
        "combat_2hsword_support_01";
    private static final String TWO_HAND_SWORD_SUPPORT_TWO =
        "combat_2hsword_support_02";
    private static final String TWO_HAND_SWORD_SUPPORT_THREE =
        "combat_2hsword_support_03";
    private static final String TWO_HAND_SWORD_SUPPORT_FOUR =
        "combat_2hsword_support_04";
    private static final String TWO_HAND_SWORD_MASTER =
        "combat_2hsword_master";
    private static final String TERAS_KASI_ROOT = "combat_unarmed";
    private static final String TERAS_KASI_NOVICE = "combat_unarmed_novice";
    private static final String TERAS_KASI_SPEED_ONE =
        "combat_unarmed_speed_01";
    private static final String TERAS_KASI_SPEED_TWO =
        "combat_unarmed_speed_02";
    private static final String TERAS_KASI_SPEED_THREE =
        "combat_unarmed_speed_03";
    private static final String TERAS_KASI_SPEED_FOUR =
        "combat_unarmed_speed_04";
    private static final String[] TERAS_KASI_MASTER_SKILLS =
    {
        "combat_unarmed", "combat_unarmed_novice",
        "combat_unarmed_accuracy_01", "combat_unarmed_accuracy_02",
        "combat_unarmed_accuracy_03", "combat_unarmed_accuracy_04",
        "combat_unarmed_speed_01", "combat_unarmed_speed_02",
        "combat_unarmed_speed_03", "combat_unarmed_speed_04",
        "combat_unarmed_ability_01", "combat_unarmed_ability_02",
        "combat_unarmed_ability_03", "combat_unarmed_ability_04",
        "combat_unarmed_support_01", "combat_unarmed_support_02",
        "combat_unarmed_support_03", "combat_unarmed_support_04",
        "combat_unarmed_master"
    };
    private static final String[] BOUNTY_HUNTER_DROID_CONTROL_SKILLS =
    {
        "combat_bountyhunter", "combat_bountyhunter_novice",
        "combat_bountyhunter_droidcontrol_01",
        "combat_bountyhunter_droidcontrol_02",
        "combat_bountyhunter_droidcontrol_03",
        "combat_bountyhunter_droidcontrol_04"
    };
    private static final String[] BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES =
    {
        "combat_marksman_master", "outdoors_scout",
        "outdoors_scout_novice", "outdoors_scout_movement_01",
        "outdoors_scout_movement_02", "outdoors_scout_movement_03",
        "outdoors_scout_movement_04"
    };
    private static final String[] BOUNTY_HUNTER_DROID_RESPONSE_SKILLS =
    {
        "combat_bountyhunter", "combat_bountyhunter_novice",
        "combat_bountyhunter_droidresponse_01",
        "combat_bountyhunter_droidresponse_02",
        "combat_bountyhunter_droidresponse_03",
        "combat_bountyhunter_droidresponse_04"
    };
    private static final String[] BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITES =
        BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES;
    private static final String[] BOUNTY_HUNTER_MASTER_SKILLS =
    {
        "combat_bountyhunter", "combat_bountyhunter_novice",
        "combat_bountyhunter_investigation_01",
        "combat_bountyhunter_investigation_02",
        "combat_bountyhunter_investigation_03",
        "combat_bountyhunter_investigation_04",
        "combat_bountyhunter_droidcontrol_01",
        "combat_bountyhunter_droidcontrol_02",
        "combat_bountyhunter_droidcontrol_03",
        "combat_bountyhunter_droidcontrol_04",
        "combat_bountyhunter_droidresponse_01",
        "combat_bountyhunter_droidresponse_02",
        "combat_bountyhunter_droidresponse_03",
        "combat_bountyhunter_droidresponse_04",
        "combat_bountyhunter_support_01",
        "combat_bountyhunter_support_02",
        "combat_bountyhunter_support_03",
        "combat_bountyhunter_support_04",
        "combat_bountyhunter_master"
    };
    private static final String[] BOUNTY_HUNTER_MASTER_PREREQUISITES =
        BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES;
    private static final String[] SMUGGLER_COMBAT_SKILLS =
    {
        "combat_smuggler", "combat_smuggler_novice",
        "combat_smuggler_combat_01", "combat_smuggler_combat_02",
        "combat_smuggler_combat_03", "combat_smuggler_combat_04"
    };
    private static final String[] SMUGGLER_COMBAT_PREREQUISITES =
    {
        "combat_marksman", "combat_marksman_novice",
        "combat_marksman_pistol_01", "combat_marksman_pistol_02",
        "combat_marksman_pistol_03", "combat_marksman_pistol_04",
        "combat_brawler", "combat_brawler_novice",
        "combat_brawler_unarmed_01", "combat_brawler_unarmed_02",
        "combat_brawler_unarmed_03", "combat_brawler_unarmed_04"
    };
    private static final String BRAWLER_POLEARM_ONE =
        "combat_brawler_polearm_01";
    private static final String BRAWLER_POLEARM_TWO =
        "combat_brawler_polearm_02";
    private static final String BRAWLER_POLEARM_THREE =
        "combat_brawler_polearm_03";
    private static final String BRAWLER_POLEARM_FOUR =
        "combat_brawler_polearm_04";
    private static final String POLEARM_NOVICE = "combat_polearm_novice";
    private static final String POLEARM_ACCURACY_ONE =
        "combat_polearm_accuracy_01";
    private static final String POLEARM_ACCURACY_TWO =
        "combat_polearm_accuracy_02";
    private static final String POLEARM_ACCURACY_THREE =
        "combat_polearm_accuracy_03";
    private static final String POLEARM_ACCURACY_FOUR =
        "combat_polearm_accuracy_04";
    private static final String POLEARM_SPEED_ONE =
        "combat_polearm_speed_01";
    private static final String POLEARM_SPEED_TWO =
        "combat_polearm_speed_02";
    private static final String POLEARM_SPEED_THREE =
        "combat_polearm_speed_03";
    private static final String POLEARM_SPEED_FOUR =
        "combat_polearm_speed_04";
    private static final String POLEARM_ABILITY_ONE =
        "combat_polearm_ability_01";
    private static final String POLEARM_ABILITY_TWO =
        "combat_polearm_ability_02";
    private static final String POLEARM_ABILITY_THREE =
        "combat_polearm_ability_03";
    private static final String POLEARM_ABILITY_FOUR =
        "combat_polearm_ability_04";
    private static final String POLEARM_SUPPORT_ONE =
        "combat_polearm_support_01";
    private static final String POLEARM_SUPPORT_TWO =
        "combat_polearm_support_02";
    private static final String POLEARM_SUPPORT_THREE =
        "combat_polearm_support_03";
    private static final String POLEARM_SUPPORT_FOUR =
        "combat_polearm_support_04";
    private static final String POLEARM_MASTER = "combat_polearm_master";
    private static final String BRAWLER_UNARMED_ONE =
        "combat_brawler_unarmed_01";
    private static final String BRAWLER_UNARMED_TWO =
        "combat_brawler_unarmed_02";
    private static final String BRAWLER_UNARMED_THREE =
        "combat_brawler_unarmed_03";
    private static final String BRAWLER_UNARMED_FOUR =
        "combat_brawler_unarmed_04";
    private static final String BRAWLER_MASTER = "combat_brawler_master";
    private static final String ONE_HAND_SWORD_NOVICE =
        "combat_1hsword_novice";
    private static final String ONE_HAND_SWORD_SUPPORT_ONE =
        "combat_1hsword_support_01";
    private static final String ONE_HAND_SWORD_SUPPORT_TWO =
        "combat_1hsword_support_02";
    private static final String ONE_HAND_SWORD_SUPPORT_THREE =
        "combat_1hsword_support_03";
    private static final String ONE_HAND_SWORD_SUPPORT_FOUR =
        "combat_1hsword_support_04";
    private static final String ONE_HAND_SWORD_ACCURACY_ONE =
        "combat_1hsword_accuracy_01";
    private static final String ONE_HAND_SWORD_ACCURACY_TWO =
        "combat_1hsword_accuracy_02";
    private static final String ONE_HAND_SWORD_ACCURACY_THREE =
        "combat_1hsword_accuracy_03";
    private static final String ONE_HAND_SWORD_ACCURACY_FOUR =
        "combat_1hsword_accuracy_04";
    private static final String ONE_HAND_SWORD_SPEED_ONE =
        "combat_1hsword_speed_01";
    private static final String ONE_HAND_SWORD_SPEED_TWO =
        "combat_1hsword_speed_02";
    private static final String ONE_HAND_SWORD_SPEED_THREE =
        "combat_1hsword_speed_03";
    private static final String ONE_HAND_SWORD_SPEED_FOUR =
        "combat_1hsword_speed_04";
    private static final String ONE_HAND_SWORD_ABILITY_ONE =
        "combat_1hsword_ability_01";
    private static final String ONE_HAND_SWORD_ABILITY_TWO =
        "combat_1hsword_ability_02";
    private static final String ONE_HAND_SWORD_ABILITY_THREE =
        "combat_1hsword_ability_03";
    private static final String ONE_HAND_SWORD_ABILITY_FOUR =
        "combat_1hsword_ability_04";
    private static final String ONE_HAND_SWORD_MASTER =
        "combat_1hsword_master";
    private static final String RIFLEMAN_ACCURACY_ONE =
        "combat_rifleman_accuracy_01";
    private static final String RIFLEMAN_ACCURACY_TWO =
        "combat_rifleman_accuracy_02";
    private static final String RIFLEMAN_ACCURACY_THREE =
        "combat_rifleman_accuracy_03";
    private static final String RIFLEMAN_ACCURACY_FOUR =
        "combat_rifleman_accuracy_04";
    private static final String RIFLEMAN_SPEED_ONE =
        "combat_rifleman_speed_01";
    private static final String RIFLEMAN_SPEED_TWO =
        "combat_rifleman_speed_02";
    private static final String RIFLEMAN_SPEED_THREE =
        "combat_rifleman_speed_03";
    private static final String RIFLEMAN_SPEED_FOUR =
        "combat_rifleman_speed_04";
    private static final String RIFLEMAN_ABILITY_ONE =
        "combat_rifleman_ability_01";
    private static final String RIFLEMAN_ABILITY_TWO =
        "combat_rifleman_ability_02";
    private static final String RIFLEMAN_ABILITY_THREE =
        "combat_rifleman_ability_03";
    private static final String RIFLEMAN_ABILITY_FOUR =
        "combat_rifleman_ability_04";
    private static final String RIFLEMAN_SUPPORT_ONE =
        "combat_rifleman_support_01";
    private static final String RIFLEMAN_SUPPORT_TWO =
        "combat_rifleman_support_02";
    private static final String RIFLEMAN_SUPPORT_THREE =
        "combat_rifleman_support_03";
    private static final String RIFLEMAN_SUPPORT_FOUR =
        "combat_rifleman_support_04";
    private static final String RIFLEMAN_MASTER = "combat_rifleman_master";
    private static final String MARKSMAN_CARBINE_ONE =
        "combat_marksman_carbine_01";
    private static final String MARKSMAN_CARBINE_TWO =
        "combat_marksman_carbine_02";
    private static final String MARKSMAN_CARBINE_THREE =
        "combat_marksman_carbine_03";
    private static final String MARKSMAN_CARBINE_FOUR =
        "combat_marksman_carbine_04";
    private static final String CARBINE_NOVICE = "combat_carbine_novice";
    private static final String CARBINE_ACCURACY_ONE =
        "combat_carbine_accuracy_01";
    private static final String CARBINE_ACCURACY_TWO =
        "combat_carbine_accuracy_02";
    private static final String CARBINE_ACCURACY_THREE =
        "combat_carbine_accuracy_03";
    private static final String CARBINE_ACCURACY_FOUR =
        "combat_carbine_accuracy_04";
    private static final String CARBINE_SUPPORT_ONE =
        "combat_carbine_support_01";
    private static final String CARBINE_SUPPORT_TWO =
        "combat_carbine_support_02";
    private static final String CARBINE_SUPPORT_THREE =
        "combat_carbine_support_03";
    private static final String CARBINE_SUPPORT_FOUR =
        "combat_carbine_support_04";
    private static final String CARBINE_SPEED_ONE = "combat_carbine_speed_01";
    private static final String CARBINE_ABILITY_ONE =
        "combat_carbine_ability_01";
    private static final String CARBINE_ABILITY_TWO =
        "combat_carbine_ability_02";
    private static final String CARBINE_ABILITY_THREE =
        "combat_carbine_ability_03";
    private static final String CARBINE_ABILITY_FOUR =
        "combat_carbine_ability_04";
    private static final String MARKSMAN_PISTOL_ONE = "combat_marksman_pistol_01";
    private static final String MARKSMAN_PISTOL_TWO = "combat_marksman_pistol_02";
    private static final String MARKSMAN_PISTOL_THREE =
        "combat_marksman_pistol_03";
    private static final String MARKSMAN_PISTOL_FOUR =
        "combat_marksman_pistol_04";
    private static final String PISTOL_NOVICE = "combat_pistol_novice";
    private static final String PISTOL_SUPPORT_ONE =
        "combat_pistol_support_01";
    private static final String PISTOL_SUPPORT_TWO =
        "combat_pistol_support_02";
    private static final String PISTOL_SUPPORT_THREE =
        "combat_pistol_support_03";
    private static final String MARKSMAN_SUPPORT_ONE = "combat_marksman_support_01";
    private static final String MARKSMAN_SUPPORT_TWO = "combat_marksman_support_02";
    private static final String MARKSMAN_SUPPORT_FOUR = "combat_marksman_support_04";
    private static final String COMMAND = "headShot1";
    private static final String DURATION_CONTROL_COMMAND = "headShot2";
    private static final String HEAD_SHOT_THREE_COMMAND = "headShot3";
    private static final String BODY_SHOT_TWO_COMMAND = "bodyShot2";
    private static final String BODY_SHOT_THREE_COMMAND = "bodyShot3";
    private static final String HEALTH_SHOT_ONE_COMMAND = "healthShot1";
    private static final String HEALTH_SHOT_TWO_COMMAND = "healthShot2";
    private static final String PISTOL_MELEE_DEFENSE_ONE_COMMAND =
        "pistolMeleeDefense1";
    private static final String PISTOL_MELEE_DEFENSE_TWO_COMMAND =
        "pistolMeleeDefense2";
    private static final String TUMBLE_TO_PRONE_COMMAND = "tumbleToProne";
    private static final String TUMBLE_TO_KNEELING_COMMAND =
        "tumbleToKneeling";
    private static final String TUMBLE_TO_STANDING_COMMAND =
        "tumbleToStanding";
    private static final String ACTION_SHOT_ONE_COMMAND = "actionShot1";
    private static final String ACTION_SHOT_TWO_COMMAND = "actionShot2";
    private static final String MIND_SHOT_ONE_COMMAND = "mindShot1";
    private static final String MIND_SHOT_TWO_COMMAND = "mindShot2";
    private static final String SURPRISE_SHOT_COMMAND = "surpriseShot";
    private static final String SNIPER_SHOT_COMMAND = "sniperShot";
    private static final String CONCEAL_SHOT_COMMAND = "concealShot";
    private static final String FLURRY_SHOT_ONE_COMMAND = "flurryShot1";
    private static final String FLURRY_SHOT_TWO_COMMAND = "flurryShot2";
    private static final String CDEF_CERTIFICATION = "cert_rifle_cdef";
    private static final String PISTOL_CDEF_CERTIFICATION = "cert_pistol_cdef";
    private static final String CARBINE_CDEF_CERTIFICATION = "cert_carbine_cdef";
    private static final String POLEARM_COMMAND = "polearmLegHit1";
    private static final String POLEARM_LEG_TWO_COMMAND = "polearmLegHit2";
    private static final String POLEARM_LEG_THREE_COMMAND = "polearmLegHit3";
    private static final String POLEARM_HIT_ONE_COMMAND = "polearmHit1";
    private static final String POLEARM_HIT_TWO_COMMAND = "polearmHit2";
    private static final String POLEARM_HIT_THREE_COMMAND = "polearmHit3";
    private static final String POLEARM_STUN_TWO_COMMAND = "polearmStun2";
    private static final String POLEARM_SPIN_TWO_COMMAND =
        "polearmSpinAttack2";
    private static final String POLEARM_AREA_ONE_COMMAND = "polearmArea1";
    private static final String POLEARM_AREA_TWO_COMMAND = "polearmArea2";
    private static final String POLEARM_SWEEP_ONE_COMMAND = "polearmSweep1";
    private static final String POLEARM_SWEEP_TWO_COMMAND = "polearmSweep2";
    private static final String POLEARM_ACTION_HIT_ONE_COMMAND =
        "polearmActionHit1";
    private static final String POLEARM_ACTION_HIT_TWO_COMMAND =
        "polearmActionHit2";
    private static final String UNARMED_COMMAND = "unarmedHeadHit1";
    private static final String UNARMED_HIT_ONE_COMMAND = "unarmedHit1";
    private static final String UNARMED_HIT_TWO_COMMAND = "unarmedHit2";
    private static final String UNARMED_BODY_ONE_COMMAND = "unarmedBodyHit1";
    private static final String UNARMED_LEG_ONE_COMMAND = "unarmedLegHit1";
    private static final String UNARMED_SPIN_ONE_COMMAND = "unarmedSpinAttack1";
    private static final String UNARMED_SPIN_TWO_COMMAND = "unarmedSpinAttack2";
    private static final String OVERCHARGE_ONE_COMMAND = "overChargeShot1";
    private static final String OVERCHARGE_TWO_COMMAND = "overChargeShot2";
    private static final String POINT_BLANK_SINGLE_ONE_COMMAND = "pointBlankSingle1";
    private static final String AIM_COMMAND = "aim";
    private static final String THREATEN_SHOT_COMMAND = "threatenShot";
    private static final String WARNING_SHOT_COMMAND = "warningShot";
    private static final String SUPPRESSION_FIRE_ONE_COMMAND = "suppressionFire1";
    private static final String SUPPRESSION_FIRE_TWO_COMMAND = "suppressionFire2";
    private static final String ROLL_SHOT_COMMAND = "rollShot";
    private static final String DIVE_SHOT_COMMAND = "diveShot";
    private static final String KIP_UP_SHOT_COMMAND = "kipUpShot";
    private static final String TAKE_COVER_COMMAND = "takeCover";
    private static final String FULL_AUTO_SINGLE_ONE_COMMAND =
        "fullAutoSingle1";
    private static final String FULL_AUTO_SINGLE_TWO_COMMAND =
        "fullAutoSingle2";
    private static final String FULL_AUTO_AREA_ONE_COMMAND =
        "fullAutoArea1";
    private static final String FULL_AUTO_AREA_TWO_COMMAND =
        "fullAutoArea2";
    private static final String CHARGE_SHOT_ONE_COMMAND = "chargeShot1";
    private static final String CHARGE_SHOT_TWO_COMMAND = "chargeShot2";
    private static final String STRAFE_SHOT_ONE_COMMAND = "strafeShot1";
    private static final String STRAFE_SHOT_TWO_COMMAND = "strafeShot2";
    private static final String STARTLE_SHOT_ONE_COMMAND = "startleShot1";
    private static final String STARTLE_SHOT_TWO_COMMAND = "startleShot2";
    private static final String FLUSHING_SHOT_ONE_COMMAND = "flushingShot1";
    private static final String FLUSHING_SHOT_TWO_COMMAND = "flushingShot2";
    private static final String POLEARM_LUNGE_ONE_COMMAND = "polearmLunge1";
    private static final String UNARMED_LUNGE_ONE_COMMAND = "unarmedLunge1";
    private static final String ONE_HAND_LUNGE_ONE_COMMAND = "melee1hLunge1";
    private static final String TWO_HAND_LUNGE_ONE_COMMAND = "melee2hLunge1";
    private static final String POLEARM_LUNGE_TWO_COMMAND = "polearmLunge2";
    private static final String UNARMED_LUNGE_TWO_COMMAND = "unarmedLunge2";
    private static final String ONE_HAND_LUNGE_TWO_COMMAND = "melee1hLunge2";
    private static final String TWO_HAND_LUNGE_TWO_COMMAND = "melee2hLunge2";
    private static final String TAUNT_COMMAND = "taunt";
    private static final String ONE_HAND_DIZZY_HIT_ONE_COMMAND =
        "melee1hDizzyHit1";
    private static final String ONE_HAND_BLIND_HIT_ONE_COMMAND =
        "melee1hBlindHit1";
    private static final String ONE_HAND_BLIND_HIT_TWO_COMMAND =
        "melee1hBlindHit2";
    private static final String ONE_HAND_SCATTER_HIT_ONE_COMMAND =
        "melee1hScatterHit1";
    private static final String ONE_HAND_DIZZY_HIT_TWO_COMMAND =
        "melee1hDizzyHit2";
    private static final String ONE_HAND_SCATTER_HIT_TWO_COMMAND =
        "melee1hScatterHit2";
    private static final String ONE_HAND_HEALTH_HIT_ONE_COMMAND =
        "melee1hHealthHit1";
    private static final String ONE_HAND_SPIN_ATTACK_TWO_COMMAND =
        "melee1hSpinAttack2";
    private static final String ONE_HAND_HEALTH_HIT_TWO_COMMAND =
        "melee1hHealthHit2";
    private static final String TWO_HAND_SWEEP_ONE_COMMAND = "melee2hSweep1";
    private static final String TWO_HAND_SWEEP_TWO_COMMAND = "melee2hSweep2";
    private static final String TWO_HAND_MIND_HIT_ONE_COMMAND =
        "melee2hMindHit1";
    private static final String TWO_HAND_MIND_HIT_TWO_COMMAND =
        "melee2hMindHit2";
    private static final String TWO_HAND_HIT_THREE_COMMAND = "melee2hHit3";
    private static final String UNARMED_HIT_THREE_COMMAND = "unarmedHit3";
    private static final String[] BOUNTY_HUNTER_DROID_CONTROL_COMMANDS =
    {
        "underHandShot", "knockdownFire", "confusionShot"
    };
    private static final String[] BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS =
    {
        "bleedingShot", "eyeShot", "torsoShot"
    };
    private static final String[] BOUNTY_HUNTER_MASTER_COMMANDS =
    {
        "sprayShot", "fastBlast"
    };
    private static final String[] SMUGGLER_COMBAT_COMMANDS =
    {
        "panicShot", "lowBlow", "lastDitch"
    };
    private static final String UNARMED_KNOCKDOWN_ONE_COMMAND =
        "unarmedKnockdown1";
    private static final String UNARMED_KNOCKDOWN_TWO_COMMAND =
        "unarmedKnockdown2";
    private static final String POLEARM_STUN_ONE_COMMAND = "polearmStun1";
    private static final String UNARMED_BLIND_ONE_COMMAND = "unarmedBlind1";
    private static final String UNARMED_STUN_ONE_COMMAND = "unarmedStun1";
    private static final String INTIMIDATE_ONE_COMMAND = "intimidate1";
    private static final String INTIMIDATE_TWO_COMMAND = "intimidate2";
    private static final String WARCRY_ONE_COMMAND = "warcry1";
    private static final String WARCRY_TWO_COMMAND = "warcry2";
    private static final String SCATTER_SHOT_ONE_COMMAND = "scatterShot1";
    private static final String SCATTER_SHOT_TWO_COMMAND = "scatterShot2";
    private static final String WILD_SHOT_ONE_COMMAND = "wildShot1";
    private static final String WILD_SHOT_TWO_COMMAND = "wildShot2";
    private static final String LEG_SHOT_TWO_COMMAND = "legShot2";
    private static final String LEG_SHOT_THREE_COMMAND = "legShot3";
    private static final String ACID_SINGLE_ONE_COMMAND = "fireAcidSingle1";
    private static final String ACID_CONE_ONE_COMMAND = "fireAcidCone1";
    private static final String ACID_CONE_TWO_COMMAND = "fireAcidCone2";
    private static final String ACID_SINGLE_TWO_COMMAND = "fireAcidSingle2";
    private static final String FLAME_SINGLE_ONE_COMMAND = "flameSingle1";
    private static final String FLAME_SINGLE_TWO_COMMAND = "flameSingle2";
    private static final String FLAME_CONE_ONE_COMMAND = "flameCone1";
    private static final String FLAME_CONE_TWO_COMMAND = "flameCone2";
    private static final String LIGHTNING_SINGLE_ONE_COMMAND = "fireLightningSingle1";
    private static final String LIGHTNING_CONE_ONE_COMMAND = "fireLightningCone1";
    private static final String LIGHTNING_CONE_TWO_COMMAND = "fireLightningCone2";
    private static final String LIGHTNING_SINGLE_TWO_COMMAND = "fireLightningSingle2";
    private static final String POLEARM_AREA_COMMAND = "polearmSpinAttack1";
    private static final String ONE_HAND_AREA_COMMAND = "melee1hSpinAttack1";
    private static final String ONE_HAND_BODY_ONE_COMMAND = "melee1hBodyHit1";
    private static final String ONE_HAND_BODY_TWO_COMMAND = "melee1hBodyHit2";
    private static final String ONE_HAND_BODY_THREE_COMMAND = "melee1hBodyHit3";
    private static final String ONE_HAND_HIT_ONE_COMMAND = "melee1hHit1";
    private static final String ONE_HAND_HIT_TWO_COMMAND = "melee1hHit2";
    private static final String ONE_HAND_HIT_THREE_COMMAND = "melee1hHit3";
    private static final String TWO_HAND_AREA_COMMAND = "melee2hSpinAttack1";
    private static final String TWO_HAND_AREA_TWO_COMMAND = "melee2hSpinAttack2";
    private static final String TWO_HAND_HEAD_ONE_COMMAND = "melee2hHeadHit1";
    private static final String TWO_HAND_HEAD_TWO_COMMAND = "melee2hHeadHit2";
    private static final String TWO_HAND_HEAD_THREE_COMMAND = "melee2hHeadHit3";
    private static final String TWO_HAND_HIT_ONE_COMMAND = "melee2hHit1";
    private static final String TWO_HAND_HIT_TWO_COMMAND = "melee2hHit2";
    private static final String TWO_HAND_ACCURACY_AREA_ONE_COMMAND =
        "melee2hArea1";
    private static final String TWO_HAND_ACCURACY_AREA_TWO_COMMAND =
        "melee2hArea2";
    private static final String TWO_HAND_ACCURACY_AREA_THREE_COMMAND =
        "melee2hArea3";
    private static final String POLEARM_CERTIFICATION = "cert_lance_staff_wood_s2";
    private static final String ONE_HAND_CERTIFICATION = "cert_sword_blade_rantok";
    private static final String TWO_HAND_CERTIFICATION = "cert_sword_2h_cleaver";
    private static final String ACID_CERTIFICATION = "cert_heavy_acid_beam";
    private static final String FLAME_CERTIFICATION = "cert_rifle_flame_thrower";
    private static final String LIGHTNING_CERTIFICATION = "cert_rifle_lightning";
    private static final String COMBAT_ACTIONS_SCRIPT = "systems.combat.combat_actions";
    private static final String DIAGNOSTIC_ROOT =
        "precu.p14.marksmanTier1Fixture.liveDiagnostic";
    private static final String DIAGNOSTIC_ENABLED =
        DIAGNOSTIC_ROOT + ".enabled";
    private static final String ROOT = "precu.p14.headShot1Fixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PEER = ROOT + ".peer";
    private static final String ORIGINAL_LOCATION = ROOT + ".originalLocation";
    private static final String ORIGINAL_POSTURE = ROOT + ".originalPosture";
    private static final String ORIGINAL_LOCOMOTION = ROOT + ".originalLocomotion";
    private static final String ORIGINAL_COVER_STATE = ROOT + ".originalCoverState";
    private static final String ORIGINAL_DIZZY_BUFF =
        ROOT + ".originalDizzyBuff";
    private static final String ORIGINAL_BLIND_BUFF =
        ROOT + ".originalBlindBuff";
    private static final String ORIGINAL_STUN_BUFF =
        ROOT + ".originalStunBuff";
    private static final String ORIGINAL_INTIMIDATE_BUFF =
        ROOT + ".originalIntimidateBuff";
    private static final String ORIGINAL_HEALTH = ROOT + ".originalHealth";
    private static final String ORIGINAL_MAX_HEALTH = ROOT + ".originalMaxHealth";
    private static final String ORIGINAL_STRENGTH = ROOT + ".originalStrength";
    private static final String ORIGINAL_ACTION = ROOT + ".originalAction";
    private static final String ORIGINAL_MAX_ACTION = ROOT + ".originalMaxAction";
    private static final String ORIGINAL_QUICKNESS = ROOT + ".originalQuickness";
    private static final String ORIGINAL_MIND = ROOT + ".originalMind";
    private static final String ORIGINAL_MAX_MIND = ROOT + ".originalMaxMind";
    private static final String ORIGINAL_FOCUS = ROOT + ".originalFocus";
    private static final String ORIGINAL_WOUNDS = ROOT + ".originalWounds";
    private static final String ORIGINAL_SHOCK = ROOT + ".originalShock";
    private static final String ORIGINAL_HEALTH_REGEN = ROOT + ".originalHealthRegen";
    private static final String ORIGINAL_ACTION_REGEN = ROOT + ".originalActionRegen";
    private static final String ORIGINAL_MIND_REGEN = ROOT + ".originalMindRegen";
    private static final String ORIGINAL_NOVICE = ROOT + ".originalNovice";
    private static final String ORIGINAL_RIFLE_ONE = ROOT + ".originalRifleOne";
    private static final String ORIGINAL_RIFLE_TWO = ROOT + ".originalRifleTwo";
    private static final String ORIGINAL_RIFLE_THREE =
        ROOT + ".originalRifleThree";
    private static final String ORIGINAL_RIFLE_FOUR =
        ROOT + ".originalRifleFour";
    private static final String ORIGINAL_RIFLEMAN_NOVICE =
        ROOT + ".originalRiflemanNovice";
    private static final String ORIGINAL_BRAWLER_ROOT =
        ROOT + ".originalBrawlerRoot";
    private static final String ORIGINAL_BRAWLER_NOVICE =
        ROOT + ".originalBrawlerNovice";
    private static final String ORIGINAL_BRAWLER_ONE_HAND_ONE =
        ROOT + ".originalBrawlerOneHandOne";
    private static final String ORIGINAL_BRAWLER_ONE_HAND_TWO =
        ROOT + ".originalBrawlerOneHandTwo";
    private static final String ORIGINAL_BRAWLER_ONE_HAND_THREE =
        ROOT + ".originalBrawlerOneHandThree";
    private static final String ORIGINAL_BRAWLER_ONE_HAND_FOUR =
        ROOT + ".originalBrawlerOneHandFour";
    private static final String ORIGINAL_ONE_HAND_SWORD_NOVICE =
        ROOT + ".originalOneHandSwordNovice";
    private static final String ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE =
        ROOT + ".originalOneHandSwordSupportOne";
    private static final String ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO =
        ROOT + ".originalOneHandSwordSupportTwo";
    private static final String ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE =
        ROOT + ".originalOneHandSwordSupportThree";
    private static final String ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR =
        ROOT + ".originalOneHandSwordSupportFour";
    private static final String ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE =
        ROOT + ".originalOneHandSwordAccuracyOne";
    private static final String ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO =
        ROOT + ".originalOneHandSwordAccuracyTwo";
    private static final String ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE =
        ROOT + ".originalOneHandSwordAccuracyThree";
    private static final String ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR =
        ROOT + ".originalOneHandSwordAccuracyFour";
    private static final String ORIGINAL_ONE_HAND_SWORD_SPEED_ONE =
        ROOT + ".originalOneHandSwordSpeedOne";
    private static final String ORIGINAL_ONE_HAND_SWORD_SPEED_TWO =
        ROOT + ".originalOneHandSwordSpeedTwo";
    private static final String ORIGINAL_ONE_HAND_SWORD_SPEED_THREE =
        ROOT + ".originalOneHandSwordSpeedThree";
    private static final String ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR =
        ROOT + ".originalOneHandSwordSpeedFour";
    private static final String ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE =
        ROOT + ".originalOneHandSwordAbilityOne";
    private static final String ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO =
        ROOT + ".originalOneHandSwordAbilityTwo";
    private static final String ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE =
        ROOT + ".originalOneHandSwordAbilityThree";
    private static final String ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR =
        ROOT + ".originalOneHandSwordAbilityFour";
    private static final String ORIGINAL_ONE_HAND_SWORD_MASTER =
        ROOT + ".originalOneHandSwordMaster";
    private static final String ORIGINAL_BRAWLER_TWO_HAND_ONE =
        ROOT + ".originalBrawlerTwoHandOne";
    private static final String ORIGINAL_BRAWLER_TWO_HAND_TWO =
        ROOT + ".originalBrawlerTwoHandTwo";
    private static final String ORIGINAL_BRAWLER_TWO_HAND_THREE =
        ROOT + ".originalBrawlerTwoHandThree";
    private static final String ORIGINAL_BRAWLER_TWO_HAND_FOUR =
        ROOT + ".originalBrawlerTwoHandFour";
    private static final String ORIGINAL_TWO_HAND_SWORD_NOVICE =
        ROOT + ".originalTwoHandSwordNovice";
    private static final String ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE =
        ROOT + ".originalTwoHandSwordAccuracyOne";
    private static final String ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO =
        ROOT + ".originalTwoHandSwordAccuracyTwo";
    private static final String ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE =
        ROOT + ".originalTwoHandSwordAccuracyThree";
    private static final String ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR =
        ROOT + ".originalTwoHandSwordAccuracyFour";
    private static final String ORIGINAL_TWO_HAND_SWORD_SPEED_ONE =
        ROOT + ".originalTwoHandSwordSpeedOne";
    private static final String ORIGINAL_TWO_HAND_SWORD_SPEED_TWO =
        ROOT + ".originalTwoHandSwordSpeedTwo";
    private static final String ORIGINAL_TWO_HAND_SWORD_SPEED_THREE =
        ROOT + ".originalTwoHandSwordSpeedThree";
    private static final String ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR =
        ROOT + ".originalTwoHandSwordSpeedFour";
    private static final String ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE =
        ROOT + ".originalTwoHandSwordAbilityOne";
    private static final String ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO =
        ROOT + ".originalTwoHandSwordAbilityTwo";
    private static final String ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE =
        ROOT + ".originalTwoHandSwordAbilityThree";
    private static final String ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR =
        ROOT + ".originalTwoHandSwordAbilityFour";
    private static final String ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE =
        ROOT + ".originalTwoHandSwordSupportOne";
    private static final String ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO =
        ROOT + ".originalTwoHandSwordSupportTwo";
    private static final String ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE =
        ROOT + ".originalTwoHandSwordSupportThree";
    private static final String ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR =
        ROOT + ".originalTwoHandSwordSupportFour";
    private static final String ORIGINAL_TWO_HAND_SWORD_MASTER =
        ROOT + ".originalTwoHandSwordMaster";
    private static final String ORIGINAL_TERAS_KASI_ROOT =
        ROOT + ".m321.originalTerasKasiRoot";
    private static final String ORIGINAL_TERAS_KASI_NOVICE =
        ROOT + ".m321.originalTerasKasiNovice";
    private static final String ORIGINAL_TERAS_KASI_SPEED_ONE =
        ROOT + ".m321.originalTerasKasiSpeedOne";
    private static final String ORIGINAL_TERAS_KASI_SPEED_TWO =
        ROOT + ".m321.originalTerasKasiSpeedTwo";
    private static final String ORIGINAL_TERAS_KASI_SPEED_THREE =
        ROOT + ".m321.originalTerasKasiSpeedThree";
    private static final String ORIGINAL_TERAS_KASI_SPEED_FOUR =
        ROOT + ".m321.originalTerasKasiSpeedFour";
    private static final String ORIGINAL_TERAS_KASI_MASTER_SKILL_BITS =
        ROOT + ".m324.originalTerasKasiMasterSkillBits";
    private static final String ORIGINAL_UNARMED_HIT_THREE_COMMAND =
        ROOT + ".m324.originalUnarmedHitThreeCommand";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS =
        ROOT + ".m325.originalBountyHunterDroidControlSkillBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS =
        ROOT + ".m325.originalBountyHunterDroidControlPrerequisiteBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS =
        ROOT + ".m325.originalBountyHunterDroidControlCommandBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_SKILL_BITS =
        ROOT + ".m326.originalBountyHunterDroidResponseSkillBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITE_BITS =
        ROOT + ".m326.originalBountyHunterDroidResponsePrerequisiteBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_COMMAND_BITS =
        ROOT + ".m326.originalBountyHunterDroidResponseCommandBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_MASTER_SKILL_BITS =
        ROOT + ".m327.originalBountyHunterMasterSkillBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_MASTER_PREREQUISITE_BITS =
        ROOT + ".m327.originalBountyHunterMasterPrerequisiteBits";
    private static final String ORIGINAL_BOUNTY_HUNTER_MASTER_COMMAND_BITS =
        ROOT + ".m327.originalBountyHunterMasterCommandBits";
    private static final String ORIGINAL_SMUGGLER_COMBAT_SKILL_BITS =
        ROOT + ".m328.originalSmugglerCombatSkillBits";
    private static final String ORIGINAL_SMUGGLER_COMBAT_PREREQUISITE_BITS =
        ROOT + ".m328.originalSmugglerCombatPrerequisiteBits";
    private static final String ORIGINAL_SMUGGLER_COMBAT_COMMAND_BITS =
        ROOT + ".m328.originalSmugglerCombatCommandBits";
    private static final String M329_ROOT = ROOT + ".m329";
    private static final String M329_ORIGINAL_FEIGN_COMMAND =
        M329_ROOT + ".originalFeignCommand";
    private static final String M329_ORIGINAL_DEFENDER_HEADSHOT_COMMAND =
        M329_ROOT + ".originalDefenderHeadShotCommand";
    private static final String M329_ORIGINAL_DEFENDER_CERTIFICATION =
        M329_ROOT + ".originalDefenderCertification";
    private static final String M329_ORIGINAL_DEFENDER_WEAPON =
        M329_ROOT + ".originalDefenderWeapon";
    private static final String M329_ORIGINAL_DEFENDER_WEAPON_PRESENT =
        M329_ROOT + ".originalDefenderWeaponPresent";
    private static final String M329_FIXTURE_DEFENDER_RIFLE =
        M329_ROOT + ".fixtureDefenderRifle";
    private static final String ORIGINAL_BRAWLER_POLEARM_ONE =
        ROOT + ".originalBrawlerPolearmOne";
    private static final String ORIGINAL_BRAWLER_POLEARM_TWO =
        ROOT + ".originalBrawlerPolearmTwo";
    private static final String ORIGINAL_BRAWLER_POLEARM_THREE =
        ROOT + ".originalBrawlerPolearmThree";
    private static final String ORIGINAL_BRAWLER_POLEARM_FOUR =
        ROOT + ".originalBrawlerPolearmFour";
    private static final String ORIGINAL_POLEARM_NOVICE =
        ROOT + ".originalPolearmNovice";
    private static final String ORIGINAL_POLEARM_ACCURACY_ONE =
        ROOT + ".originalPolearmAccuracyOne";
    private static final String ORIGINAL_POLEARM_ACCURACY_TWO =
        ROOT + ".originalPolearmAccuracyTwo";
    private static final String ORIGINAL_POLEARM_ACCURACY_THREE =
        ROOT + ".originalPolearmAccuracyThree";
    private static final String ORIGINAL_POLEARM_ACCURACY_FOUR =
        ROOT + ".originalPolearmAccuracyFour";
    private static final String ORIGINAL_POLEARM_SPEED_ONE =
        ROOT + ".originalPolearmSpeedOne";
    private static final String ORIGINAL_POLEARM_SPEED_TWO =
        ROOT + ".originalPolearmSpeedTwo";
    private static final String ORIGINAL_POLEARM_SPEED_THREE =
        ROOT + ".originalPolearmSpeedThree";
    private static final String ORIGINAL_POLEARM_SPEED_FOUR =
        ROOT + ".originalPolearmSpeedFour";
    private static final String ORIGINAL_POLEARM_ABILITY_ONE =
        ROOT + ".originalPolearmAbilityOne";
    private static final String ORIGINAL_POLEARM_ABILITY_TWO =
        ROOT + ".originalPolearmAbilityTwo";
    private static final String ORIGINAL_POLEARM_ABILITY_THREE =
        ROOT + ".originalPolearmAbilityThree";
    private static final String ORIGINAL_POLEARM_ABILITY_FOUR =
        ROOT + ".originalPolearmAbilityFour";
    private static final String ORIGINAL_POLEARM_SUPPORT_ONE =
        ROOT + ".originalPolearmSupportOne";
    private static final String ORIGINAL_POLEARM_SUPPORT_TWO =
        ROOT + ".originalPolearmSupportTwo";
    private static final String ORIGINAL_POLEARM_SUPPORT_THREE =
        ROOT + ".originalPolearmSupportThree";
    private static final String ORIGINAL_POLEARM_SUPPORT_FOUR =
        ROOT + ".originalPolearmSupportFour";
    private static final String ORIGINAL_POLEARM_MASTER =
        ROOT + ".originalPolearmMaster";
    private static final String ORIGINAL_BRAWLER_UNARMED_ONE =
        ROOT + ".originalBrawlerUnarmedOne";
    private static final String ORIGINAL_BRAWLER_UNARMED_TWO =
        ROOT + ".originalBrawlerUnarmedTwo";
    private static final String ORIGINAL_BRAWLER_UNARMED_THREE =
        ROOT + ".originalBrawlerUnarmedThree";
    private static final String ORIGINAL_BRAWLER_UNARMED_FOUR =
        ROOT + ".originalBrawlerUnarmedFour";
    private static final String ORIGINAL_BRAWLER_MASTER =
        ROOT + ".originalBrawlerMaster";
    private static final String ORIGINAL_RIFLEMAN_ACCURACY_ONE =
        ROOT + ".originalRiflemanAccuracyOne";
    private static final String ORIGINAL_RIFLEMAN_ACCURACY_TWO =
        ROOT + ".originalRiflemanAccuracyTwo";
    private static final String ORIGINAL_RIFLEMAN_ACCURACY_THREE =
        ROOT + ".originalRiflemanAccuracyThree";
    private static final String ORIGINAL_RIFLEMAN_ACCURACY_FOUR =
        ROOT + ".originalRiflemanAccuracyFour";
    private static final String ORIGINAL_RIFLEMAN_SPEED_ONE =
        ROOT + ".originalRiflemanSpeedOne";
    private static final String ORIGINAL_RIFLEMAN_SPEED_TWO =
        ROOT + ".originalRiflemanSpeedTwo";
    private static final String ORIGINAL_RIFLEMAN_SPEED_THREE =
        ROOT + ".originalRiflemanSpeedThree";
    private static final String ORIGINAL_RIFLEMAN_SPEED_FOUR =
        ROOT + ".originalRiflemanSpeedFour";
    private static final String ORIGINAL_RIFLEMAN_ABILITY_ONE =
        ROOT + ".originalRiflemanAbilityOne";
    private static final String ORIGINAL_RIFLEMAN_ABILITY_TWO =
        ROOT + ".originalRiflemanAbilityTwo";
    private static final String ORIGINAL_RIFLEMAN_ABILITY_THREE =
        ROOT + ".originalRiflemanAbilityThree";
    private static final String ORIGINAL_RIFLEMAN_ABILITY_FOUR =
        ROOT + ".originalRiflemanAbilityFour";
    private static final String ORIGINAL_RIFLEMAN_SUPPORT_ONE =
        ROOT + ".originalRiflemanSupportOne";
    private static final String ORIGINAL_RIFLEMAN_SUPPORT_TWO =
        ROOT + ".originalRiflemanSupportTwo";
    private static final String ORIGINAL_RIFLEMAN_SUPPORT_THREE =
        ROOT + ".originalRiflemanSupportThree";
    private static final String ORIGINAL_RIFLEMAN_SUPPORT_FOUR =
        ROOT + ".originalRiflemanSupportFour";
    private static final String ORIGINAL_RIFLEMAN_MASTER =
        ROOT + ".originalRiflemanMaster";
    private static final String ORIGINAL_CARBINE_ONE =
        ROOT + ".originalCarbineOne";
    private static final String ORIGINAL_CARBINE_TWO =
        ROOT + ".originalCarbineTwo";
    private static final String ORIGINAL_CARBINE_THREE =
        ROOT + ".originalCarbineThree";
    private static final String ORIGINAL_CARBINE_FOUR =
        ROOT + ".originalCarbineFour";
    private static final String ORIGINAL_CARBINE_NOVICE =
        ROOT + ".originalCarbineNovice";
    private static final String ORIGINAL_CARBINE_ACCURACY_ONE =
        ROOT + ".originalCarbineAccuracyOne";
    private static final String ORIGINAL_CARBINE_ACCURACY_TWO =
        ROOT + ".originalCarbineAccuracyTwo";
    private static final String ORIGINAL_CARBINE_ACCURACY_THREE =
        ROOT + ".originalCarbineAccuracyThree";
    private static final String ORIGINAL_CARBINE_ACCURACY_FOUR =
        ROOT + ".originalCarbineAccuracyFour";
    private static final String ORIGINAL_CARBINE_SUPPORT_ONE =
        ROOT + ".originalCarbineSupportOne";
    private static final String ORIGINAL_CARBINE_SUPPORT_TWO =
        ROOT + ".originalCarbineSupportTwo";
    private static final String ORIGINAL_CARBINE_SUPPORT_THREE =
        ROOT + ".originalCarbineSupportThree";
    private static final String ORIGINAL_CARBINE_SUPPORT_FOUR =
        ROOT + ".originalCarbineSupportFour";
    private static final String ORIGINAL_CARBINE_SPEED_ONE =
        ROOT + ".originalCarbineSpeedOne";
    private static final String ORIGINAL_CARBINE_ABILITY_ONE =
        ROOT + ".originalCarbineAbilityOne";
    private static final String ORIGINAL_CARBINE_ABILITY_TWO =
        ROOT + ".originalCarbineAbilityTwo";
    private static final String ORIGINAL_CARBINE_ABILITY_THREE =
        ROOT + ".originalCarbineAbilityThree";
    private static final String ORIGINAL_CARBINE_ABILITY_FOUR =
        ROOT + ".originalCarbineAbilityFour";
    private static final String ORIGINAL_PISTOL_ONE = ROOT + ".originalPistolOne";
    private static final String ORIGINAL_PISTOL_TWO = ROOT + ".originalPistolTwo";
    private static final String ORIGINAL_PISTOL_THREE =
        ROOT + ".originalPistolThree";
    private static final String ORIGINAL_PISTOL_FOUR =
        ROOT + ".originalPistolFour";
    private static final String ORIGINAL_PISTOL_NOVICE =
        ROOT + ".originalPistolNovice";
    private static final String ORIGINAL_PISTOL_SUPPORT_ONE =
        ROOT + ".originalPistolSupportOne";
    private static final String ORIGINAL_PISTOL_SUPPORT_TWO =
        ROOT + ".originalPistolSupportTwo";
    private static final String ORIGINAL_PISTOL_SUPPORT_THREE =
        ROOT + ".originalPistolSupportThree";
    private static final String ORIGINAL_SUPPORT_ONE = ROOT + ".originalSupportOne";
    private static final String ORIGINAL_SUPPORT_TWO = ROOT + ".originalSupportTwo";
    private static final String ORIGINAL_SUPPORT_FOUR = ROOT + ".originalSupportFour";
    private static final String ORIGINAL_DURATION_CONTROL =
        ROOT + ".originalDurationControl";
    private static final String ORIGINAL_HEAD_SHOT_THREE_COMMAND =
        ROOT + ".originalHeadShotThreeCommand";
    private static final String ORIGINAL_CDEF_CERTIFICATION =
        ROOT + ".originalCdefCertification";
    private static final String ORIGINAL_BODY_SHOT_TWO_COMMAND =
        ROOT + ".originalBodyShotTwoCommand";
    private static final String ORIGINAL_BODY_SHOT_THREE_COMMAND =
        ROOT + ".originalBodyShotThreeCommand";
    private static final String ORIGINAL_HEALTH_SHOT_ONE_COMMAND =
        ROOT + ".originalHealthShotOneCommand";
    private static final String ORIGINAL_HEALTH_SHOT_TWO_COMMAND =
        ROOT + ".originalHealthShotTwoCommand";
    private static final String ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND =
        ROOT + ".originalPistolMeleeDefenseOneCommand";
    private static final String ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND =
        ROOT + ".originalPistolMeleeDefenseTwoCommand";
    private static final String ORIGINAL_TUMBLE_TO_PRONE_COMMAND =
        ROOT + ".originalTumbleToProneCommand";
    private static final String ORIGINAL_TUMBLE_TO_KNEELING_COMMAND =
        ROOT + ".originalTumbleToKneelingCommand";
    private static final String ORIGINAL_TUMBLE_TO_STANDING_COMMAND =
        ROOT + ".originalTumbleToStandingCommand";
    private static final String ORIGINAL_ACTION_SHOT_ONE_COMMAND =
        ROOT + ".originalActionShotOneCommand";
    private static final String ORIGINAL_ACTION_SHOT_TWO_COMMAND =
        ROOT + ".originalActionShotTwoCommand";
    private static final String ORIGINAL_MIND_SHOT_ONE_COMMAND =
        ROOT + ".originalMindShotOneCommand";
    private static final String ORIGINAL_MIND_SHOT_TWO_COMMAND =
        ROOT + ".originalMindShotTwoCommand";
    private static final String ORIGINAL_SURPRISE_SHOT_COMMAND =
        ROOT + ".originalSurpriseShotCommand";
    private static final String ORIGINAL_SNIPER_SHOT_COMMAND =
        ROOT + ".originalSniperShotCommand";
    private static final String ORIGINAL_CONCEAL_SHOT_COMMAND =
        ROOT + ".originalConcealShotCommand";
    private static final String ORIGINAL_FLURRY_SHOT_ONE_COMMAND =
        ROOT + ".originalFlurryShotOneCommand";
    private static final String ORIGINAL_FLURRY_SHOT_TWO_COMMAND =
        ROOT + ".originalFlurryShotTwoCommand";
    private static final String ORIGINAL_PISTOL_CDEF_CERTIFICATION =
        ROOT + ".originalPistolCdefCertification";
    private static final String ORIGINAL_CARBINE_CDEF_CERTIFICATION =
        ROOT + ".originalCarbineCdefCertification";
    private static final String ORIGINAL_POLEARM_COMMAND =
        ROOT + ".originalPolearmCommand";
    private static final String ORIGINAL_POLEARM_LEG_TWO_COMMAND =
        ROOT + ".originalPolearmLegTwoCommand";
    private static final String ORIGINAL_POLEARM_LEG_THREE_COMMAND =
        ROOT + ".originalPolearmLegThreeCommand";
    private static final String ORIGINAL_POLEARM_HIT_ONE_COMMAND =
        ROOT + ".originalPolearmHitOneCommand";
    private static final String ORIGINAL_POLEARM_HIT_TWO_COMMAND =
        ROOT + ".originalPolearmHitTwoCommand";
    private static final String ORIGINAL_POLEARM_HIT_THREE_COMMAND =
        ROOT + ".originalPolearmHitThreeCommand";
    private static final String ORIGINAL_POLEARM_STUN_TWO_COMMAND =
        ROOT + ".originalPolearmStunTwoCommand";
    private static final String ORIGINAL_POLEARM_SPIN_TWO_COMMAND =
        ROOT + ".originalPolearmSpinTwoCommand";
    private static final String ORIGINAL_POLEARM_AREA_ONE_COMMAND =
        ROOT + ".originalPolearmAreaOneCommand";
    private static final String ORIGINAL_POLEARM_AREA_TWO_COMMAND =
        ROOT + ".originalPolearmAreaTwoCommand";
    private static final String ORIGINAL_POLEARM_SWEEP_ONE_COMMAND =
        ROOT + ".originalPolearmSweepOneCommand";
    private static final String ORIGINAL_POLEARM_SWEEP_TWO_COMMAND =
        ROOT + ".originalPolearmSweepTwoCommand";
    private static final String ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND =
        ROOT + ".originalPolearmActionHitOneCommand";
    private static final String ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND =
        ROOT + ".originalPolearmActionHitTwoCommand";
    private static final String ORIGINAL_UNARMED_COMMAND =
        ROOT + ".originalUnarmedCommand";
    private static final String ORIGINAL_UNARMED_HIT_ONE_COMMAND =
        ROOT + ".originalUnarmedHitOneCommand";
    private static final String ORIGINAL_UNARMED_HIT_TWO_COMMAND =
        ROOT + ".originalUnarmedHitTwoCommand";
    private static final String ORIGINAL_UNARMED_BODY_ONE_COMMAND =
        ROOT + ".originalUnarmedBodyOneCommand";
    private static final String ORIGINAL_UNARMED_LEG_ONE_COMMAND =
        ROOT + ".originalUnarmedLegOneCommand";
    private static final String ORIGINAL_UNARMED_SPIN_ONE_COMMAND =
        ROOT + ".originalUnarmedSpinOneCommand";
    private static final String ORIGINAL_UNARMED_SPIN_TWO_COMMAND =
        ROOT + ".originalUnarmedSpinTwoCommand";
    private static final String ORIGINAL_OVERCHARGE_ONE_COMMAND =
        ROOT + ".originalOverchargeOneCommand";
    private static final String ORIGINAL_OVERCHARGE_TWO_COMMAND =
        ROOT + ".originalOverchargeTwoCommand";
    private static final String ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND =
        ROOT + ".originalPointBlankSingleOneCommand";
    private static final String ORIGINAL_AIM_COMMAND =
        ROOT + ".originalAimCommand";
    private static final String ORIGINAL_THREATEN_SHOT_COMMAND =
        ROOT + ".originalThreatenShotCommand";
    private static final String ORIGINAL_WARNING_SHOT_COMMAND =
        ROOT + ".originalWarningShotCommand";
    private static final String ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND =
        ROOT + ".originalSuppressionFireOneCommand";
    private static final String ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND =
        ROOT + ".originalSuppressionFireTwoCommand";
    private static final String ORIGINAL_ROLL_SHOT_COMMAND =
        ROOT + ".originalRollShotCommand";
    private static final String ORIGINAL_DIVE_SHOT_COMMAND =
        ROOT + ".originalDiveShotCommand";
    private static final String ORIGINAL_KIP_UP_SHOT_COMMAND =
        ROOT + ".originalKipUpShotCommand";
    private static final String ORIGINAL_TAKE_COVER_COMMAND =
        ROOT + ".originalTakeCoverCommand";
    private static final String ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND =
        ROOT + ".originalFullAutoSingleOneCommand";
    private static final String ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND =
        ROOT + ".originalFullAutoSingleTwoCommand";
    private static final String ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND =
        ROOT + ".originalFullAutoAreaOneCommand";
    private static final String ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND =
        ROOT + ".originalFullAutoAreaTwoCommand";
    private static final String ORIGINAL_CHARGE_SHOT_ONE_COMMAND =
        ROOT + ".originalChargeShotOneCommand";
    private static final String ORIGINAL_CHARGE_SHOT_TWO_COMMAND =
        ROOT + ".originalChargeShotTwoCommand";
    private static final String ORIGINAL_STRAFE_SHOT_ONE_COMMAND =
        ROOT + ".originalStrafeShotOneCommand";
    private static final String ORIGINAL_STRAFE_SHOT_TWO_COMMAND =
        ROOT + ".originalStrafeShotTwoCommand";
    private static final String ORIGINAL_STARTLE_SHOT_ONE_COMMAND =
        ROOT + ".originalStartleShotOneCommand";
    private static final String ORIGINAL_STARTLE_SHOT_TWO_COMMAND =
        ROOT + ".originalStartleShotTwoCommand";
    private static final String ORIGINAL_FLUSHING_SHOT_ONE_COMMAND =
        ROOT + ".originalFlushingShotOneCommand";
    private static final String ORIGINAL_FLUSHING_SHOT_TWO_COMMAND =
        ROOT + ".originalFlushingShotTwoCommand";
    private static final String ORIGINAL_POLEARM_LUNGE_ONE_COMMAND =
        ROOT + ".originalPolearmLungeOneCommand";
    private static final String ORIGINAL_UNARMED_LUNGE_ONE_COMMAND =
        ROOT + ".originalUnarmedLungeOneCommand";
    private static final String ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND =
        ROOT + ".originalOneHandLungeOneCommand";
    private static final String ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND =
        ROOT + ".originalTwoHandLungeOneCommand";
    private static final String ORIGINAL_POLEARM_LUNGE_TWO_COMMAND =
        ROOT + ".originalPolearmLungeTwoCommand";
    private static final String ORIGINAL_UNARMED_LUNGE_TWO_COMMAND =
        ROOT + ".originalUnarmedLungeTwoCommand";
    private static final String ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND =
        ROOT + ".originalOneHandLungeTwoCommand";
    private static final String ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND =
        ROOT + ".originalTwoHandLungeTwoCommand";
    private static final String ORIGINAL_TAUNT_COMMAND =
        ROOT + ".originalTauntCommand";
    private static final String ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND =
        ROOT + ".originalOneHandDizzyHitOneCommand";
    private static final String ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND =
        ROOT + ".originalOneHandBlindHitOneCommand";
    private static final String ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND =
        ROOT + ".originalOneHandBlindHitTwoCommand";
    private static final String ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND =
        ROOT + ".originalOneHandScatterHitOneCommand";
    private static final String ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND =
        ROOT + ".originalOneHandDizzyHitTwoCommand";
    private static final String ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND =
        ROOT + ".originalOneHandScatterHitTwoCommand";
    private static final String ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND =
        ROOT + ".originalOneHandHealthHitOneCommand";
    private static final String ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND =
        ROOT + ".originalOneHandSpinAttackTwoCommand";
    private static final String ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND =
        ROOT + ".originalOneHandHealthHitTwoCommand";
    private static final String ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND =
        ROOT + ".originalTwoHandSweepOneCommand";
    private static final String ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND =
        ROOT + ".originalTwoHandSweepTwoCommand";
    private static final String ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND =
        ROOT + ".originalTwoHandMindHitOneCommand";
    private static final String ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND =
        ROOT + ".originalTwoHandMindHitTwoCommand";
    private static final String ORIGINAL_TWO_HAND_HIT_THREE_COMMAND =
        ROOT + ".originalTwoHandHitThreeCommand";
    private static final String ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND =
        ROOT + ".m321.originalUnarmedKnockdownOneCommand";
    private static final String ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND =
        ROOT + ".m321.originalUnarmedKnockdownTwoCommand";
    private static final String ORIGINAL_POLEARM_STUN_ONE_COMMAND =
        ROOT + ".originalPolearmStunOneCommand";
    private static final String ORIGINAL_UNARMED_BLIND_ONE_COMMAND =
        ROOT + ".originalUnarmedBlindOneCommand";
    private static final String ORIGINAL_UNARMED_STUN_ONE_COMMAND =
        ROOT + ".originalUnarmedStunOneCommand";
    private static final String ORIGINAL_INTIMIDATE_ONE_COMMAND =
        ROOT + ".originalIntimidateOneCommand";
    private static final String ORIGINAL_INTIMIDATE_TWO_COMMAND =
        ROOT + ".originalIntimidateTwoCommand";
    private static final String ORIGINAL_WARCRY_ONE_COMMAND =
        ROOT + ".originalWarcryOneCommand";
    private static final String ORIGINAL_WARCRY_TWO_COMMAND =
        ROOT + ".originalWarcryTwoCommand";
    private static final String ORIGINAL_SCATTER_SHOT_ONE_COMMAND =
        ROOT + ".originalScatterShotOneCommand";
    private static final String ORIGINAL_SCATTER_SHOT_TWO_COMMAND =
        ROOT + ".originalScatterShotTwoCommand";
    private static final String ORIGINAL_WILD_SHOT_ONE_COMMAND =
        ROOT + ".originalWildShotOneCommand";
    private static final String ORIGINAL_WILD_SHOT_TWO_COMMAND =
        ROOT + ".originalWildShotTwoCommand";
    private static final String ORIGINAL_LEG_SHOT_TWO_COMMAND =
        ROOT + ".originalLegShotTwoCommand";
    private static final String ORIGINAL_LEG_SHOT_THREE_COMMAND =
        ROOT + ".originalLegShotThreeCommand";
    private static final String ORIGINAL_ACID_SINGLE_ONE_COMMAND =
        ROOT + ".originalAcidSingleOneCommand";
    private static final String ORIGINAL_ACID_CONE_ONE_COMMAND =
        ROOT + ".originalAcidConeOneCommand";
    private static final String ORIGINAL_ACID_CONE_TWO_COMMAND =
        ROOT + ".originalAcidConeTwoCommand";
    private static final String ORIGINAL_ACID_SINGLE_TWO_COMMAND =
        ROOT + ".originalAcidSingleTwoCommand";
    private static final String ORIGINAL_FLAME_SINGLE_ONE_COMMAND =
        ROOT + ".originalFlameSingleOneCommand";
    private static final String ORIGINAL_FLAME_SINGLE_TWO_COMMAND =
        ROOT + ".originalFlameSingleTwoCommand";
    private static final String ORIGINAL_FLAME_CONE_ONE_COMMAND =
        ROOT + ".originalFlameConeOneCommand";
    private static final String ORIGINAL_FLAME_CONE_TWO_COMMAND =
        ROOT + ".originalFlameConeTwoCommand";
    private static final String ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND =
        ROOT + ".originalLightningSingleOneCommand";
    private static final String ORIGINAL_LIGHTNING_CONE_ONE_COMMAND =
        ROOT + ".originalLightningConeOneCommand";
    private static final String ORIGINAL_LIGHTNING_CONE_TWO_COMMAND =
        ROOT + ".originalLightningConeTwoCommand";
    private static final String ORIGINAL_LIGHTNING_SINGLE_TWO_COMMAND =
        ROOT + ".originalLightningSingleTwoCommand";
    private static final String ORIGINAL_POLEARM_AREA_COMMAND =
        ROOT + ".originalPolearmAreaCommand";
    private static final String ORIGINAL_ONE_HAND_AREA_COMMAND =
        ROOT + ".originalOneHandAreaCommand";
    private static final String ORIGINAL_ONE_HAND_BODY_ONE_COMMAND =
        ROOT + ".originalOneHandBodyOneCommand";
    private static final String ORIGINAL_ONE_HAND_BODY_TWO_COMMAND =
        ROOT + ".originalOneHandBodyTwoCommand";
    private static final String ORIGINAL_ONE_HAND_BODY_THREE_COMMAND =
        ROOT + ".originalOneHandBodyThreeCommand";
    private static final String ORIGINAL_ONE_HAND_HIT_ONE_COMMAND =
        ROOT + ".originalOneHandHitOneCommand";
    private static final String ORIGINAL_ONE_HAND_HIT_TWO_COMMAND =
        ROOT + ".originalOneHandHitTwoCommand";
    private static final String ORIGINAL_ONE_HAND_HIT_THREE_COMMAND =
        ROOT + ".originalOneHandHitThreeCommand";
    private static final String ORIGINAL_TWO_HAND_AREA_COMMAND =
        ROOT + ".originalTwoHandAreaCommand";
    private static final String ORIGINAL_TWO_HAND_AREA_TWO_COMMAND =
        ROOT + ".originalTwoHandAreaTwoCommand";
    private static final String ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND =
        ROOT + ".originalTwoHandHeadOneCommand";
    private static final String ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND =
        ROOT + ".originalTwoHandHeadTwoCommand";
    private static final String ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND =
        ROOT + ".originalTwoHandHeadThreeCommand";
    private static final String ORIGINAL_TWO_HAND_HIT_ONE_COMMAND =
        ROOT + ".originalTwoHandHitOneCommand";
    private static final String ORIGINAL_TWO_HAND_HIT_TWO_COMMAND =
        ROOT + ".originalTwoHandHitTwoCommand";
    private static final String ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND =
        ROOT + ".originalTwoHandAccuracyAreaOneCommand";
    private static final String ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND =
        ROOT + ".originalTwoHandAccuracyAreaTwoCommand";
    private static final String ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND =
        ROOT + ".originalTwoHandAccuracyAreaThreeCommand";
    private static final String ORIGINAL_POLEARM_CERTIFICATION =
        ROOT + ".originalPolearmCertification";
    private static final String ORIGINAL_ONE_HAND_CERTIFICATION =
        ROOT + ".originalOneHandCertification";
    private static final String ORIGINAL_TWO_HAND_CERTIFICATION =
        ROOT + ".originalTwoHandCertification";
    private static final String ORIGINAL_ACID_CERTIFICATION =
        ROOT + ".originalAcidCertification";
    private static final String ORIGINAL_FLAME_CERTIFICATION =
        ROOT + ".originalFlameCertification";
    private static final String ORIGINAL_LIGHTNING_CERTIFICATION =
        ROOT + ".originalLightningCertification";
    private static final String ORIGINAL_PERSONAL_ENEMY = ROOT + ".originalPersonalEnemy";
    private static final String ORIGINAL_COMBAT_ACTIONS = ROOT + ".originalCombatActions";
    private static final String ORIGINAL_POSTURE_DOWN_RECOVERY_PRESENT =
        ROOT + ".originalPostureDownRecoveryPresent";
    private static final String ORIGINAL_POSTURE_DOWN_RECOVERY =
        ROOT + ".originalPostureDownRecovery";
    private static final String ORIGINAL_POSTURE_UP_RECOVERY_PRESENT =
        ROOT + ".originalPostureUpRecoveryPresent";
    private static final String ORIGINAL_POSTURE_UP_RECOVERY =
        ROOT + ".originalPostureUpRecovery";
    private static final String ORIGINAL_KNOCKDOWN_RECOVERY_PRESENT =
        ROOT + ".originalKnockdownRecoveryPresent";
    private static final String ORIGINAL_KNOCKDOWN_RECOVERY =
        ROOT + ".originalKnockdownRecovery";
    private static final String ORIGINAL_KNOCKDOWN_POSTURE_PRESENT =
        ROOT + ".originalKnockdownPosturePresent";
    private static final String ORIGINAL_KNOCKDOWN_POSTURE =
        ROOT + ".originalKnockdownPosture";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String FIXTURE_WEAPON = ROOT + ".fixtureWeapon";
    private static final String FIXTURE_PISTOL = ROOT + ".fixturePistol";
    private static final String FIXTURE_CARBINE = ROOT + ".fixtureCarbine";
    private static final String FIXTURE_POLEARM = ROOT + ".fixturePolearm";
    private static final String FIXTURE_ONE_HAND = ROOT + ".fixtureOneHand";
    private static final String FIXTURE_TWO_HAND = ROOT + ".fixtureTwoHand";
    private static final String FIXTURE_ACID = ROOT + ".fixtureAcid";
    private static final String FIXTURE_FLAME = ROOT + ".fixtureFlame";
    private static final String FIXTURE_LIGHTNING = ROOT + ".fixtureLightning";
    private static final String FIXTURE_CONCEAL_TARGET =
        ROOT + ".fixtureConcealTarget";
    private static final String CONCEAL_TARGET_OWNED =
        ROOT + ".concealTargetOwned";
    private static final String CONCEAL_TARGET_OWNER =
        ROOT + ".concealTargetOwner";
    private static final String CONCEAL_TARGET_CREATURE = "worrt";
    private static final int FIXTURE_HAM_MAXIMUM = 20000;
    private static final String CDEF_TEMPLATE = "object/weapon/ranged/rifle/rifle_cdef.iff";
    private static final String CDEF_PISTOL_TEMPLATE =
        "object/weapon/ranged/pistol/pistol_cdef.iff";
    private static final String CDEF_CARBINE_TEMPLATE =
        "object/weapon/ranged/carbine/carbine_cdef.iff";
    private static final String POLEARM_TEMPLATE =
        "object/weapon/melee/polearm/lance_staff_wood_s2.iff";
    private static final String ONE_HAND_TEMPLATE =
        "object/weapon/melee/sword/sword_rantok.iff";
    private static final String TWO_HAND_TEMPLATE =
        "object/weapon/melee/2h_sword/2h_sword_cleaver.iff";
    private static final String ACID_TEMPLATE =
        "object/weapon/ranged/heavy/heavy_acid_beam.iff";
    private static final String FLAME_TEMPLATE =
        "object/weapon/ranged/rifle/rifle_flame_thrower.iff";
    private static final String LIGHTNING_TEMPLATE =
        "object/weapon/ranged/rifle/rifle_lightning.iff";
    private static final String USAGE =
        "usage: inspect|recover|prepare|status|prepareUnarmedSpeed|statusUnarmedSpeed|armUnarmedSpeed|cleanupUnarmedSpeed|prepareUnarmedMaster|statusUnarmedMaster|armUnarmedMaster|cleanupUnarmedMaster|prepareBountyHunterDroidControl|statusBountyHunterDroidControl|armBountyHunterDroidControl|cleanupBountyHunterDroidControl|recoverBountyHunterDroidControl|prepareBountyHunterDroidResponse|statusBountyHunterDroidResponse|armBountyHunterDroidResponse|cleanupBountyHunterDroidResponse|prepareBountyHunterMaster|statusBountyHunterMaster|armBountyHunterMasterCarbine|armBountyHunterMasterPistol|cleanupBountyHunterMaster|prepareSmugglerCombat|statusSmugglerCombat|armSmugglerCombat|cleanupSmugglerCombat|prepareFeignDeath|statusFeignDeath|armFeignDeathNoCombat|armFeignDeathFailure|armFeignDeathSuccess|cleanupFeignDeath|armNoPartial|armGenerated|armHealthShotTwo|armPistolMeleeDefense|armMarksmanTumble|armOneHandBlind|armOneHandSupport|armOneHandAccuracy|armOneHandSpeed|armOneHandAbility|armOneHandMaster|armTwoHandAccuracy|armTwoHandSpeed|armTwoHandAbility|armTwoHandSupport|armTwoHandMaster|armPolearmNovice|armPolearmAccuracy|armPolearmSpeed|armPolearmAbility|armPolearmSupport|armPolearmMaster|armWarcryOne|armWarcryTwo|armLungeTwoFamily|armTaunt|armConceal|armStrafeCover|armPostureUp|probeStrafeDelay|equipFixtureAcid|equipFixtureFlame|cleanup 44003778 39008597 <32-hex-lifecycle>";

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
            return recoverPartial(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepare"))
        {
            return prepare(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareUnarmedSpeed"))
        {
            return prepareUnarmedSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusUnarmedSpeed"))
        {
            return statusUnarmedSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armUnarmedSpeed"))
        {
            return armUnarmedSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("cleanupUnarmedSpeed"))
        {
            return cleanupUnarmedSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareUnarmedMaster"))
        {
            return prepareUnarmedMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusUnarmedMaster"))
        {
            return statusUnarmedMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armUnarmedMaster"))
        {
            return armUnarmedMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("cleanupUnarmedMaster"))
        {
            return cleanupUnarmedMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareBountyHunterDroidControl"))
        {
            return prepareBountyHunterDroidControl(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusBountyHunterDroidControl"))
        {
            return statusBountyHunterDroidControl(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armBountyHunterDroidControl"))
        {
            return armBountyHunterDroidControl(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("cleanupBountyHunterDroidControl"))
        {
            return cleanupBountyHunterDroidControl(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("recoverBountyHunterDroidControl"))
        {
            return recoverBountyHunterDroidControl(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareBountyHunterDroidResponse"))
        {
            return prepareBountyHunterDroidResponse(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusBountyHunterDroidResponse"))
        {
            return statusBountyHunterDroidResponse(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armBountyHunterDroidResponse"))
        {
            return armBountyHunterDroidResponse(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("cleanupBountyHunterDroidResponse"))
        {
            return cleanupBountyHunterDroidResponse(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareBountyHunterMaster"))
        {
            return prepareBountyHunterMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusBountyHunterMaster"))
        {
            return statusBountyHunterMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armBountyHunterMasterCarbine"))
        {
            return armBountyHunterMaster(attacker, defender, args[3], true);
        }
        if (args[0].equalsIgnoreCase("armBountyHunterMasterPistol"))
        {
            return armBountyHunterMaster(attacker, defender, args[3], false);
        }
        if (args[0].equalsIgnoreCase("cleanupBountyHunterMaster"))
        {
            return cleanupBountyHunterMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareSmugglerCombat"))
        {
            return prepareSmugglerCombat(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusSmugglerCombat"))
        {
            return statusSmugglerCombat(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armSmugglerCombat"))
        {
            return armSmugglerCombat(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("cleanupSmugglerCombat"))
        {
            return cleanupSmugglerCombat(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("prepareFeignDeath"))
        {
            return prepareFeignDeath(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("statusFeignDeath"))
        {
            return statusFeignDeath(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armFeignDeathNoCombat"))
        {
            return armFeignDeath(attacker, defender, args[3], 35, false);
        }
        if (args[0].equalsIgnoreCase("armFeignDeathFailure"))
        {
            return armFeignDeath(attacker, defender, args[3], 71, true);
        }
        if (args[0].equalsIgnoreCase("armFeignDeathSuccess"))
        {
            return armFeignDeath(attacker, defender, args[3], 35, true);
        }
        if (args[0].equalsIgnoreCase("cleanupFeignDeath"))
        {
            return cleanupFeignDeath(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            validation = validateOwnership(attacker, defender, args[3], true);
            return validation == null ? buildStatus(attacker, defender, args[3]) : validation;
        }
        if (args[0].equalsIgnoreCase("armNoPartial"))
        {
            return armNoPartial(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armGenerated"))
        {
            return armGenerated(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armHealthShotTwo"))
        {
            return armHealthShotTwo(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPistolMeleeDefense"))
        {
            return armPistolMeleeDefense(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armMarksmanTumble"))
        {
            return armMarksmanTumble(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandBlind"))
        {
            return armOneHandBlind(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandSupport"))
        {
            return armOneHandSupport(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandAccuracy"))
        {
            return armOneHandAccuracy(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandSpeed"))
        {
            return armOneHandSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandAbility"))
        {
            return armOneHandAbility(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armOneHandMaster"))
        {
            return armOneHandMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTwoHandAccuracy"))
        {
            return armTwoHandAccuracy(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTwoHandSpeed"))
        {
            return armTwoHandSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTwoHandAbility"))
        {
            return armTwoHandAbility(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTwoHandSupport"))
        {
            return armTwoHandSupport(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTwoHandMaster"))
        {
            return armTwoHandMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmNovice"))
        {
            return armPolearmNovice(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmAccuracy"))
        {
            return armPolearmAccuracy(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmSpeed"))
        {
            return armPolearmSpeed(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmAbility"))
        {
            return armPolearmAbility(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmSupport"))
        {
            return armPolearmSupport(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPolearmMaster"))
        {
            return armPolearmMaster(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armWarcryOne"))
        {
            return armWarcryOne(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armWarcryTwo"))
        {
            return armWarcryTwo(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armLungeTwoFamily"))
        {
            return armLungeTwoFamily(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armTaunt"))
        {
            return armTaunt(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armConceal"))
        {
            return armConceal(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armStrafeCover"))
        {
            return armStrafeCover(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("armPostureUp"))
        {
            return armPostureUp(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("probeStrafeDelay"))
        {
            return probeStrafeDelay(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("equipFixtureAcid"))
        {
            return equipFixtureAcid(attacker, defender, args[3]);
        }
        if (args[0].equalsIgnoreCase("equipFixtureFlame"))
        {
            return equipFixtureFlame(attacker, defender, args[3]);
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
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if (ownership == null)
        {
            if (getIntObjVar(attacker, PREPARED) == 1 &&
                getIntObjVar(defender, PREPARED) == 1)
            {
                if (!reassertPreparedState(attacker, defender) ||
                    !prepareFixtureHam(attacker) ||
                    !prepareFixtureHam(defender))
                {
                    return "error=fixtureReassertionFailed " +
                        buildStatus(attacker, defender, lifecycle);
                }
                return "action=prepare resumed=true " + buildStatus(attacker, defender, lifecycle);
            }
            return "error=fixturePartial";
        }
        if (!"fixtureAbsent".equals(ownership))
        {
            return ownership;
        }
        if (getState(attacker, STATE_AIMING) != 0 ||
            hasSkillModModifier(attacker, combat_base.PRECU_AIM_MODIFIER))
        {
            return "error=preexistingAimState";
        }
        if (utils.hasScriptVar(
                attacker, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL) ||
            utils.hasScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL))
        {
            return "error=preexistingNextAttackDelay";
        }
        if (getState(defender, STATE_DIZZY) != 0 ||
            getState(defender, STATE_BLINDED) != 0 ||
            getState(defender, STATE_STUNNED) != 0 ||
            getState(defender, STATE_INTIMIDATED) != 0 ||
            buff.hasBuff(defender, "dizzy") ||
            buff.hasBuff(defender, "blind") ||
            buff.hasBuff(defender, "stun") ||
            buff.hasBuff(defender, "intimidate"))
        {
            return "error=preexistingFullAutoState";
        }

        location attackerOriginal = getLocation(attacker);
        location defenderOriginal = getLocation(defender);
        if (attackerOriginal == null || defenderOriginal == null)
        {
            return "error=locationUnavailable";
        }

        snapshot(attacker, defender, lifecycle, attackerOriginal);
        snapshot(defender, attacker, lifecycle, defenderOriginal);
        String snapshotOwnership = validateOwnership(attacker, defender, lifecycle, false);
        if (snapshotOwnership != null)
        {
            String recovery = recoverPartial(attacker, defender, lifecycle).replace(' ', '_');
            return "error=snapshotOwnershipFailed detail=" + snapshotOwnership +
                " recovery=" + recovery;
        }
        boolean combatActionsReady = hasScript(attacker, COMBAT_ACTIONS_SCRIPT);
        if (!combatActionsReady)
        {
            attachScript(attacker, COMBAT_ACTIONS_SCRIPT);
            combatActionsReady = hasScript(attacker, COMBAT_ACTIONS_SCRIPT);
        }
        if (!combatActionsReady)
        {
            restore(attacker, defender);
            return "error=combatActionsPreparationFailed";
        }
        boolean noviceGranted = hasSkill(attacker, MARKSMAN_NOVICE) ||
            grantSkill(attacker, MARKSMAN_NOVICE);
        boolean rifleGranted = noviceGranted &&
            (hasSkill(attacker, RIFLE_ONE) || grantSkill(attacker, RIFLE_ONE));
        boolean rifleTwoGranted = rifleGranted &&
            (hasSkill(attacker, RIFLE_TWO) || grantSkill(attacker, RIFLE_TWO));
        boolean rifleThreeGranted = rifleTwoGranted &&
            (hasSkill(attacker, RIFLE_THREE) ||
                grantSkill(attacker, RIFLE_THREE));
        boolean rifleFourGranted = rifleThreeGranted &&
            (hasSkill(attacker, RIFLE_FOUR) ||
                grantSkill(attacker, RIFLE_FOUR));
        boolean riflemanNoviceGranted = rifleFourGranted &&
            (hasSkill(attacker, RIFLEMAN_NOVICE) ||
                grantSkill(attacker, RIFLEMAN_NOVICE));
        boolean brawlerRootGranted = hasSkill(attacker, BRAWLER_ROOT) ||
            grantSkill(attacker, BRAWLER_ROOT);
        boolean brawlerNoviceGranted = brawlerRootGranted &&
            (hasSkill(attacker, BRAWLER_NOVICE) ||
                grantSkill(attacker, BRAWLER_NOVICE));
        boolean brawlerOneHandOneGranted = brawlerNoviceGranted &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_ONE) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_ONE));
        boolean brawlerOneHandTwoGranted = brawlerOneHandOneGranted &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_TWO) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_TWO));
        boolean brawlerOneHandThreeGranted = brawlerOneHandTwoGranted &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_THREE) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_THREE));
        boolean brawlerOneHandFourGranted = brawlerOneHandThreeGranted &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_FOUR));
        boolean oneHandSwordNoviceGranted = brawlerOneHandFourGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_NOVICE) ||
                grantSkill(attacker, ONE_HAND_SWORD_NOVICE));
        boolean oneHandSwordSupportOneGranted = oneHandSwordNoviceGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE));
        boolean oneHandSwordSupportTwoGranted =
            oneHandSwordSupportOneGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO));
        boolean oneHandSwordSupportThreeGranted =
            oneHandSwordSupportTwoGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE));
        boolean oneHandSwordSupportFourGranted =
            oneHandSwordSupportThreeGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR));
        boolean oneHandSwordAccuracyOneGranted = oneHandSwordNoviceGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE));
        boolean oneHandSwordAccuracyTwoGranted = oneHandSwordAccuracyOneGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO));
        boolean oneHandSwordAccuracyThreeGranted = oneHandSwordAccuracyTwoGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE));
        boolean oneHandSwordAccuracyFourGranted = oneHandSwordAccuracyThreeGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR));
        boolean oneHandSwordSpeedOneGranted = oneHandSwordNoviceGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_ONE));
        boolean oneHandSwordSpeedTwoGranted = oneHandSwordSpeedOneGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_TWO));
        boolean oneHandSwordSpeedThreeGranted = oneHandSwordSpeedTwoGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_THREE));
        boolean oneHandSwordSpeedFourGranted = oneHandSwordSpeedThreeGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR));
        boolean oneHandSwordAbilityOneGranted = oneHandSwordNoviceGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE));
        boolean oneHandSwordAbilityTwoGranted = oneHandSwordAbilityOneGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO));
        boolean oneHandSwordAbilityThreeGranted = oneHandSwordAbilityTwoGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE));
        boolean oneHandSwordAbilityFourGranted = oneHandSwordAbilityThreeGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR));
        boolean oneHandSwordMasterGranted =
            oneHandSwordSupportFourGranted &&
            oneHandSwordAccuracyFourGranted &&
            oneHandSwordSpeedFourGranted &&
            oneHandSwordAbilityFourGranted &&
            (hasSkill(attacker, ONE_HAND_SWORD_MASTER) ||
                grantSkill(attacker, ONE_HAND_SWORD_MASTER));
        boolean brawlerTwoHandOneGranted = brawlerNoviceGranted &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_ONE) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_ONE));
        boolean brawlerTwoHandTwoGranted = brawlerTwoHandOneGranted &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_TWO) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_TWO));
        boolean brawlerTwoHandThreeGranted = brawlerTwoHandTwoGranted &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_THREE) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_THREE));
        boolean brawlerTwoHandFourGranted = brawlerTwoHandThreeGranted &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_FOUR));
        boolean twoHandSwordNoviceGranted = brawlerTwoHandFourGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_NOVICE) ||
                grantSkill(attacker, TWO_HAND_SWORD_NOVICE));
        boolean twoHandSwordAccuracyOneGranted = twoHandSwordNoviceGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE));
        boolean twoHandSwordAccuracyTwoGranted =
            twoHandSwordAccuracyOneGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO));
        boolean twoHandSwordAccuracyThreeGranted =
            twoHandSwordAccuracyTwoGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE));
        boolean twoHandSwordAccuracyFourGranted =
            twoHandSwordAccuracyThreeGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR));
        boolean twoHandSwordSpeedOneGranted = twoHandSwordNoviceGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_ONE));
        boolean twoHandSwordSpeedTwoGranted = twoHandSwordSpeedOneGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_TWO));
        boolean twoHandSwordSpeedThreeGranted = twoHandSwordSpeedTwoGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_THREE));
        boolean twoHandSwordSpeedFourGranted = twoHandSwordSpeedThreeGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR));
        boolean twoHandSwordAbilityOneGranted = twoHandSwordNoviceGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE));
        boolean twoHandSwordAbilityTwoGranted =
            twoHandSwordAbilityOneGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO));
        boolean twoHandSwordAbilityThreeGranted =
            twoHandSwordAbilityTwoGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE));
        boolean twoHandSwordAbilityFourGranted =
            twoHandSwordAbilityThreeGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR));
        boolean twoHandSwordSupportOneGranted = twoHandSwordNoviceGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE));
        boolean twoHandSwordSupportTwoGranted =
            twoHandSwordSupportOneGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO));
        boolean twoHandSwordSupportThreeGranted =
            twoHandSwordSupportTwoGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE));
        boolean twoHandSwordSupportFourGranted =
            twoHandSwordSupportThreeGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR));
        boolean twoHandSwordMasterGranted =
            twoHandSwordAccuracyFourGranted && twoHandSwordSpeedFourGranted &&
            twoHandSwordAbilityFourGranted && twoHandSwordSupportFourGranted &&
            (hasSkill(attacker, TWO_HAND_SWORD_MASTER) ||
                grantSkill(attacker, TWO_HAND_SWORD_MASTER));
        boolean brawlerPolearmOneGranted = brawlerNoviceGranted &&
            (hasSkill(attacker, BRAWLER_POLEARM_ONE) ||
                grantSkill(attacker, BRAWLER_POLEARM_ONE));
        boolean brawlerPolearmTwoGranted = brawlerPolearmOneGranted &&
            (hasSkill(attacker, BRAWLER_POLEARM_TWO) ||
                grantSkill(attacker, BRAWLER_POLEARM_TWO));
        boolean brawlerPolearmThreeGranted = brawlerPolearmTwoGranted &&
            (hasSkill(attacker, BRAWLER_POLEARM_THREE) ||
                grantSkill(attacker, BRAWLER_POLEARM_THREE));
        boolean brawlerPolearmFourGranted = brawlerPolearmThreeGranted &&
            (hasSkill(attacker, BRAWLER_POLEARM_FOUR) ||
                grantSkill(attacker, BRAWLER_POLEARM_FOUR));
        boolean polearmNoviceGranted = brawlerPolearmFourGranted &&
            (hasSkill(attacker, POLEARM_NOVICE) ||
                grantSkill(attacker, POLEARM_NOVICE));
        boolean polearmAccuracyOneGranted = polearmNoviceGranted &&
            (hasSkill(attacker, POLEARM_ACCURACY_ONE) ||
                grantSkill(attacker, POLEARM_ACCURACY_ONE));
        boolean polearmAccuracyTwoGranted = polearmAccuracyOneGranted &&
            (hasSkill(attacker, POLEARM_ACCURACY_TWO) ||
                grantSkill(attacker, POLEARM_ACCURACY_TWO));
        boolean polearmAccuracyThreeGranted = polearmAccuracyTwoGranted &&
            (hasSkill(attacker, POLEARM_ACCURACY_THREE) ||
                grantSkill(attacker, POLEARM_ACCURACY_THREE));
        boolean polearmAccuracyFourGranted = polearmAccuracyThreeGranted &&
            (hasSkill(attacker, POLEARM_ACCURACY_FOUR) ||
                grantSkill(attacker, POLEARM_ACCURACY_FOUR));
        boolean polearmSpeedOneGranted = polearmNoviceGranted &&
            (hasSkill(attacker, POLEARM_SPEED_ONE) ||
                grantSkill(attacker, POLEARM_SPEED_ONE));
        boolean polearmSpeedTwoGranted = polearmSpeedOneGranted &&
            (hasSkill(attacker, POLEARM_SPEED_TWO) ||
                grantSkill(attacker, POLEARM_SPEED_TWO));
        boolean polearmSpeedThreeGranted = polearmSpeedTwoGranted &&
            (hasSkill(attacker, POLEARM_SPEED_THREE) ||
                grantSkill(attacker, POLEARM_SPEED_THREE));
        boolean polearmSpeedFourGranted = polearmSpeedThreeGranted &&
            (hasSkill(attacker, POLEARM_SPEED_FOUR) ||
                grantSkill(attacker, POLEARM_SPEED_FOUR));
        boolean polearmAbilityOneGranted = polearmNoviceGranted &&
            (hasSkill(attacker, POLEARM_ABILITY_ONE) ||
                grantSkill(attacker, POLEARM_ABILITY_ONE));
        boolean polearmAbilityTwoGranted = polearmAbilityOneGranted &&
            (hasSkill(attacker, POLEARM_ABILITY_TWO) ||
                grantSkill(attacker, POLEARM_ABILITY_TWO));
        boolean polearmAbilityThreeGranted = polearmAbilityTwoGranted &&
            (hasSkill(attacker, POLEARM_ABILITY_THREE) ||
                grantSkill(attacker, POLEARM_ABILITY_THREE));
        boolean polearmAbilityFourGranted = polearmAbilityThreeGranted &&
            (hasSkill(attacker, POLEARM_ABILITY_FOUR) ||
                grantSkill(attacker, POLEARM_ABILITY_FOUR));
        boolean polearmSupportOneGranted = polearmNoviceGranted &&
            (hasSkill(attacker, POLEARM_SUPPORT_ONE) ||
                grantSkill(attacker, POLEARM_SUPPORT_ONE));
        boolean polearmSupportTwoGranted = polearmSupportOneGranted &&
            (hasSkill(attacker, POLEARM_SUPPORT_TWO) ||
                grantSkill(attacker, POLEARM_SUPPORT_TWO));
        boolean polearmSupportThreeGranted = polearmSupportTwoGranted &&
            (hasSkill(attacker, POLEARM_SUPPORT_THREE) ||
                grantSkill(attacker, POLEARM_SUPPORT_THREE));
        boolean polearmSupportFourGranted = polearmSupportThreeGranted &&
            (hasSkill(attacker, POLEARM_SUPPORT_FOUR) ||
                grantSkill(attacker, POLEARM_SUPPORT_FOUR));
        boolean polearmMasterGranted =
            polearmAccuracyFourGranted && polearmSpeedFourGranted &&
            polearmAbilityFourGranted && polearmSupportFourGranted &&
            (hasSkill(attacker, POLEARM_MASTER) ||
                grantSkill(attacker, POLEARM_MASTER));
        boolean brawlerUnarmedOneGranted = brawlerNoviceGranted &&
            (hasSkill(attacker, BRAWLER_UNARMED_ONE) ||
                grantSkill(attacker, BRAWLER_UNARMED_ONE));
        boolean brawlerUnarmedTwoGranted = brawlerUnarmedOneGranted &&
            (hasSkill(attacker, BRAWLER_UNARMED_TWO) ||
                grantSkill(attacker, BRAWLER_UNARMED_TWO));
        boolean brawlerUnarmedThreeGranted = brawlerUnarmedTwoGranted &&
            (hasSkill(attacker, BRAWLER_UNARMED_THREE) ||
                grantSkill(attacker, BRAWLER_UNARMED_THREE));
        boolean brawlerUnarmedFourGranted = brawlerUnarmedThreeGranted &&
            (hasSkill(attacker, BRAWLER_UNARMED_FOUR) ||
                grantSkill(attacker, BRAWLER_UNARMED_FOUR));
        boolean brawlerMasterGranted =
            brawlerOneHandFourGranted && brawlerTwoHandFourGranted &&
            brawlerPolearmFourGranted && brawlerUnarmedFourGranted &&
            (hasSkill(attacker, BRAWLER_MASTER) ||
                grantSkill(attacker, BRAWLER_MASTER));
        boolean riflemanAccuracyOneGranted = riflemanNoviceGranted &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_ONE) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_ONE));
        boolean riflemanAccuracyTwoGranted = riflemanAccuracyOneGranted &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_TWO) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_TWO));
        boolean riflemanAccuracyThreeGranted = riflemanAccuracyTwoGranted &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_THREE) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_THREE));
        boolean riflemanAccuracyFourGranted = riflemanAccuracyThreeGranted &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_FOUR) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_FOUR));
        boolean riflemanSpeedOneGranted = riflemanNoviceGranted &&
            (hasSkill(attacker, RIFLEMAN_SPEED_ONE) ||
                grantSkill(attacker, RIFLEMAN_SPEED_ONE));
        boolean riflemanSpeedTwoGranted = riflemanSpeedOneGranted &&
            (hasSkill(attacker, RIFLEMAN_SPEED_TWO) ||
                grantSkill(attacker, RIFLEMAN_SPEED_TWO));
        boolean riflemanSpeedThreeGranted = riflemanSpeedTwoGranted &&
            (hasSkill(attacker, RIFLEMAN_SPEED_THREE) ||
                grantSkill(attacker, RIFLEMAN_SPEED_THREE));
        boolean riflemanSpeedFourGranted = riflemanSpeedThreeGranted &&
            (hasSkill(attacker, RIFLEMAN_SPEED_FOUR) ||
                grantSkill(attacker, RIFLEMAN_SPEED_FOUR));
        boolean riflemanAbilityOneGranted = riflemanNoviceGranted &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_ONE) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_ONE));
        boolean riflemanAbilityTwoGranted = riflemanAbilityOneGranted &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_TWO) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_TWO));
        boolean riflemanAbilityThreeGranted = riflemanAbilityTwoGranted &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_THREE) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_THREE));
        boolean riflemanAbilityFourGranted = riflemanAbilityThreeGranted &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_FOUR) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_FOUR));
        boolean riflemanSupportOneGranted = riflemanNoviceGranted &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_ONE) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_ONE));
        boolean riflemanSupportTwoGranted = riflemanSupportOneGranted &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_TWO) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_TWO));
        boolean riflemanSupportThreeGranted = riflemanSupportTwoGranted &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_THREE) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_THREE));
        boolean riflemanSupportFourGranted = riflemanSupportThreeGranted &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_FOUR) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_FOUR));
        boolean riflemanMasterGranted = riflemanAccuracyFourGranted &&
            riflemanSpeedFourGranted && riflemanAbilityFourGranted &&
            riflemanSupportFourGranted &&
            (hasSkill(attacker, RIFLEMAN_MASTER) ||
                grantSkill(attacker, RIFLEMAN_MASTER));
        boolean carbineOneGranted = noviceGranted &&
            (hasSkill(attacker, MARKSMAN_CARBINE_ONE) ||
                grantSkill(attacker, MARKSMAN_CARBINE_ONE));
        boolean carbineTwoGranted = carbineOneGranted &&
            (hasSkill(attacker, MARKSMAN_CARBINE_TWO) ||
                grantSkill(attacker, MARKSMAN_CARBINE_TWO));
        boolean carbineThreeGranted = carbineTwoGranted &&
            (hasSkill(attacker, MARKSMAN_CARBINE_THREE) ||
                grantSkill(attacker, MARKSMAN_CARBINE_THREE));
        boolean carbineFourGranted = carbineThreeGranted &&
            (hasSkill(attacker, MARKSMAN_CARBINE_FOUR) ||
                grantSkill(attacker, MARKSMAN_CARBINE_FOUR));
        boolean carbineNoviceGranted = carbineFourGranted &&
            (hasSkill(attacker, CARBINE_NOVICE) ||
                grantSkill(attacker, CARBINE_NOVICE));
        boolean carbineAccuracyOneGranted = carbineNoviceGranted &&
            (hasSkill(attacker, CARBINE_ACCURACY_ONE) ||
                grantSkill(attacker, CARBINE_ACCURACY_ONE));
        boolean carbineAccuracyTwoGranted = carbineAccuracyOneGranted &&
            (hasSkill(attacker, CARBINE_ACCURACY_TWO) ||
                grantSkill(attacker, CARBINE_ACCURACY_TWO));
        boolean carbineAccuracyThreeGranted = carbineAccuracyTwoGranted &&
            (hasSkill(attacker, CARBINE_ACCURACY_THREE) ||
                grantSkill(attacker, CARBINE_ACCURACY_THREE));
        boolean carbineAccuracyFourGranted = carbineAccuracyThreeGranted &&
            (hasSkill(attacker, CARBINE_ACCURACY_FOUR) ||
                grantSkill(attacker, CARBINE_ACCURACY_FOUR));
        boolean carbineSupportOneGranted = carbineNoviceGranted &&
            (hasSkill(attacker, CARBINE_SUPPORT_ONE) ||
                grantSkill(attacker, CARBINE_SUPPORT_ONE));
        boolean carbineSupportTwoGranted = carbineSupportOneGranted &&
            (hasSkill(attacker, CARBINE_SUPPORT_TWO) ||
                grantSkill(attacker, CARBINE_SUPPORT_TWO));
        boolean carbineSupportThreeGranted = carbineSupportTwoGranted &&
            (hasSkill(attacker, CARBINE_SUPPORT_THREE) ||
                grantSkill(attacker, CARBINE_SUPPORT_THREE));
        boolean carbineSupportFourGranted = carbineSupportThreeGranted &&
            (hasSkill(attacker, CARBINE_SUPPORT_FOUR) ||
                grantSkill(attacker, CARBINE_SUPPORT_FOUR));
        boolean carbineSpeedOneGranted = carbineNoviceGranted &&
            (hasSkill(attacker, CARBINE_SPEED_ONE) ||
                grantSkill(attacker, CARBINE_SPEED_ONE));
        boolean carbineAbilityFourGranted = carbineNoviceGranted &&
            (hasSkill(attacker, CARBINE_ABILITY_FOUR) ||
                grantSkill(attacker, CARBINE_ABILITY_FOUR));
        boolean pistolOneGranted = noviceGranted &&
            (hasSkill(attacker, MARKSMAN_PISTOL_ONE) ||
                grantSkill(attacker, MARKSMAN_PISTOL_ONE));
        boolean pistolTwoGranted = pistolOneGranted &&
            (hasSkill(attacker, MARKSMAN_PISTOL_TWO) ||
                grantSkill(attacker, MARKSMAN_PISTOL_TWO));
        boolean pistolThreeGranted = pistolTwoGranted &&
            (hasSkill(attacker, MARKSMAN_PISTOL_THREE) ||
                grantSkill(attacker, MARKSMAN_PISTOL_THREE));
        boolean pistolFourGranted = pistolThreeGranted &&
            (hasSkill(attacker, MARKSMAN_PISTOL_FOUR) ||
                grantSkill(attacker, MARKSMAN_PISTOL_FOUR));
        boolean pistolNoviceGranted = pistolFourGranted &&
            (hasSkill(attacker, PISTOL_NOVICE) ||
                grantSkill(attacker, PISTOL_NOVICE));
        boolean pistolSupportOneGranted = pistolNoviceGranted &&
            (hasSkill(attacker, PISTOL_SUPPORT_ONE) ||
                grantSkill(attacker, PISTOL_SUPPORT_ONE));
        boolean pistolSupportTwoGranted = pistolSupportOneGranted &&
            (hasSkill(attacker, PISTOL_SUPPORT_TWO) ||
                grantSkill(attacker, PISTOL_SUPPORT_TWO));
        boolean pistolSupportThreeGranted = pistolSupportTwoGranted &&
            (hasSkill(attacker, PISTOL_SUPPORT_THREE) ||
                grantSkill(attacker, PISTOL_SUPPORT_THREE));
        boolean supportOneGranted = noviceGranted &&
            (hasSkill(attacker, MARKSMAN_SUPPORT_ONE) ||
                grantSkill(attacker, MARKSMAN_SUPPORT_ONE));
        boolean supportTwoGranted = supportOneGranted &&
            (hasSkill(attacker, MARKSMAN_SUPPORT_TWO) ||
                grantSkill(attacker, MARKSMAN_SUPPORT_TWO));
        boolean supportFourGranted = supportTwoGranted &&
            (hasSkill(attacker, MARKSMAN_SUPPORT_FOUR) ||
                grantSkill(attacker, MARKSMAN_SUPPORT_FOUR));
        boolean durationControlGranted = hasCommand(attacker, DURATION_CONTROL_COMMAND) ||
            grantCommand(attacker, DURATION_CONTROL_COMMAND);
        boolean headShotThreeGranted = hasCommand(attacker, HEAD_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, HEAD_SHOT_THREE_COMMAND);
        boolean bodyShotTwoGranted = hasCommand(attacker, BODY_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, BODY_SHOT_TWO_COMMAND);
        boolean bodyShotThreeGranted = hasCommand(attacker, BODY_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, BODY_SHOT_THREE_COMMAND);
        boolean healthShotOneGranted =
            hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, HEALTH_SHOT_ONE_COMMAND);
        boolean healthShotTwoGranted =
            hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, HEALTH_SHOT_TWO_COMMAND);
        boolean pistolMeleeDefenseOneGranted =
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) ||
            grantCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        boolean pistolMeleeDefenseTwoGranted =
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND) ||
            grantCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        boolean tumbleToProneGranted =
            hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_PRONE_COMMAND);
        boolean tumbleToKneelingGranted =
            hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_KNEELING_COMMAND);
        boolean tumbleToStandingGranted =
            hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_STANDING_COMMAND);
        boolean actionShotOneGranted =
            hasCommand(attacker, ACTION_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, ACTION_SHOT_ONE_COMMAND);
        boolean actionShotTwoGranted =
            hasCommand(attacker, ACTION_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, ACTION_SHOT_TWO_COMMAND);
        boolean mindShotOneGranted =
            hasCommand(attacker, MIND_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, MIND_SHOT_ONE_COMMAND);
        boolean mindShotTwoGranted =
            hasCommand(attacker, MIND_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, MIND_SHOT_TWO_COMMAND);
        boolean surpriseShotGranted =
            hasCommand(attacker, SURPRISE_SHOT_COMMAND) ||
            grantCommand(attacker, SURPRISE_SHOT_COMMAND);
        boolean sniperShotGranted =
            hasCommand(attacker, SNIPER_SHOT_COMMAND) ||
            grantCommand(attacker, SNIPER_SHOT_COMMAND);
        boolean concealShotGranted =
            hasCommand(attacker, CONCEAL_SHOT_COMMAND) ||
            grantCommand(attacker, CONCEAL_SHOT_COMMAND);
        boolean flurryShotOneGranted =
            hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, FLURRY_SHOT_ONE_COMMAND);
        boolean flurryShotTwoGranted =
            hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, FLURRY_SHOT_TWO_COMMAND);
        boolean cdefCertificationGranted = hasCommand(attacker, CDEF_CERTIFICATION) ||
            grantCommand(attacker, CDEF_CERTIFICATION);
        boolean pistolCdefCertificationGranted =
            hasCommand(attacker, PISTOL_CDEF_CERTIFICATION) ||
            grantCommand(attacker, PISTOL_CDEF_CERTIFICATION);
        boolean carbineCdefCertificationGranted =
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) ||
            grantCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        boolean polearmCommandGranted = hasCommand(attacker, POLEARM_COMMAND) ||
            grantCommand(attacker, POLEARM_COMMAND);
        boolean polearmLegTwoCommandGranted =
            hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_LEG_TWO_COMMAND);
        boolean polearmLegThreeCommandGranted =
            hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) ||
            grantCommand(attacker, POLEARM_LEG_THREE_COMMAND);
        boolean polearmHitOneCommandGranted =
            hasCommand(attacker, POLEARM_HIT_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_ONE_COMMAND);
        boolean polearmHitTwoCommandGranted =
            hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_TWO_COMMAND);
        boolean polearmStunTwoCommandGranted =
            hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_STUN_TWO_COMMAND);
        boolean polearmSpinTwoCommandGranted =
            hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_SPIN_TWO_COMMAND);
        boolean polearmAreaOneCommandGranted =
            hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_ONE_COMMAND);
        boolean polearmAreaTwoCommandGranted =
            hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_TWO_COMMAND);
        boolean polearmSweepOneCommandGranted =
            hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_SWEEP_ONE_COMMAND);
        boolean polearmSweepTwoCommandGranted =
            hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_SWEEP_TWO_COMMAND);
        boolean polearmActionHitOneCommandGranted =
            hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND);
        boolean polearmActionHitTwoCommandGranted =
            hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND);
        boolean polearmHitThreeCommandGranted =
            hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_THREE_COMMAND);
        boolean unarmedCommandGranted = hasCommand(attacker, UNARMED_COMMAND) ||
            grantCommand(attacker, UNARMED_COMMAND);
        boolean unarmedHitOneCommandGranted =
            hasCommand(attacker, UNARMED_HIT_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_HIT_ONE_COMMAND);
        boolean unarmedHitTwoCommandGranted =
            hasCommand(attacker, UNARMED_HIT_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_HIT_TWO_COMMAND);
        boolean unarmedBodyOneCommandGranted =
            hasCommand(attacker, UNARMED_BODY_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_BODY_ONE_COMMAND);
        boolean unarmedLegOneCommandGranted =
            hasCommand(attacker, UNARMED_LEG_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_LEG_ONE_COMMAND);
        boolean unarmedSpinOneCommandGranted =
            hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_SPIN_ONE_COMMAND);
        boolean unarmedSpinTwoCommandGranted =
            hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_SPIN_TWO_COMMAND);
        boolean overchargeOneCommandGranted =
            hasCommand(attacker, OVERCHARGE_ONE_COMMAND) ||
            grantCommand(attacker, OVERCHARGE_ONE_COMMAND);
        boolean overchargeTwoCommandGranted =
            hasCommand(attacker, OVERCHARGE_TWO_COMMAND) ||
            grantCommand(attacker, OVERCHARGE_TWO_COMMAND);
        boolean pointBlankSingleOneCommandGranted =
            hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND);
        boolean aimCommandGranted =
            hasCommand(attacker, AIM_COMMAND) ||
            grantCommand(attacker, AIM_COMMAND);
        boolean threatenShotCommandGranted =
            hasCommand(attacker, THREATEN_SHOT_COMMAND) ||
            grantCommand(attacker, THREATEN_SHOT_COMMAND);
        boolean warningShotCommandGranted =
            hasCommand(attacker, WARNING_SHOT_COMMAND) ||
            grantCommand(attacker, WARNING_SHOT_COMMAND);
        boolean suppressionFireOneCommandGranted =
            hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND) ||
            grantCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND);
        boolean suppressionFireTwoCommandGranted =
            hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND) ||
            grantCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND);
        boolean rollShotCommandGranted =
            hasCommand(attacker, ROLL_SHOT_COMMAND) ||
            grantCommand(attacker, ROLL_SHOT_COMMAND);
        boolean diveShotCommandGranted =
            hasCommand(attacker, DIVE_SHOT_COMMAND) ||
            grantCommand(attacker, DIVE_SHOT_COMMAND);
        boolean kipUpShotCommandGranted =
            hasCommand(attacker, KIP_UP_SHOT_COMMAND) ||
            grantCommand(attacker, KIP_UP_SHOT_COMMAND);
        boolean takeCoverCommandGranted =
            hasCommand(attacker, TAKE_COVER_COMMAND) ||
            grantCommand(attacker, TAKE_COVER_COMMAND);
        boolean fullAutoSingleOneCommandGranted =
            hasCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND);
        boolean fullAutoSingleTwoCommandGranted =
            hasCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND);
        boolean fullAutoAreaOneCommandGranted =
            hasCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND);
        boolean fullAutoAreaTwoCommandGranted =
            hasCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND);
        boolean chargeShotOneCommandGranted =
            hasCommand(attacker, CHARGE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, CHARGE_SHOT_ONE_COMMAND);
        boolean chargeShotTwoCommandGranted =
            hasCommand(attacker, CHARGE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, CHARGE_SHOT_TWO_COMMAND);
        boolean strafeShotOneCommandGranted =
            hasCommand(attacker, STRAFE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, STRAFE_SHOT_ONE_COMMAND);
        boolean strafeShotTwoCommandGranted =
            hasCommand(attacker, STRAFE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, STRAFE_SHOT_TWO_COMMAND);
        boolean startleShotOneCommandGranted =
            hasCommand(attacker, STARTLE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, STARTLE_SHOT_ONE_COMMAND);
        boolean startleShotTwoCommandGranted =
            hasCommand(attacker, STARTLE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, STARTLE_SHOT_TWO_COMMAND);
        boolean flushingShotOneCommandGranted =
            hasCommand(attacker, FLUSHING_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, FLUSHING_SHOT_ONE_COMMAND);
        boolean flushingShotTwoCommandGranted =
            hasCommand(attacker, FLUSHING_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, FLUSHING_SHOT_TWO_COMMAND);
        boolean polearmLungeOneCommandGranted =
            hasCommand(attacker, POLEARM_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_LUNGE_ONE_COMMAND);
        boolean unarmedLungeOneCommandGranted =
            hasCommand(attacker, UNARMED_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_LUNGE_ONE_COMMAND);
        boolean oneHandLungeOneCommandGranted =
            hasCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND);
        boolean twoHandLungeOneCommandGranted =
            hasCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND);
        boolean polearmLungeTwoCommandGranted =
            hasCommand(attacker, POLEARM_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_LUNGE_TWO_COMMAND);
        boolean unarmedLungeTwoCommandGranted =
            hasCommand(attacker, UNARMED_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_LUNGE_TWO_COMMAND);
        boolean oneHandLungeTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND);
        boolean twoHandLungeTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND);
        boolean tauntCommandGranted =
            hasCommand(attacker, TAUNT_COMMAND) ||
            grantCommand(attacker, TAUNT_COMMAND);
        boolean oneHandDizzyHitOneCommandGranted =
            hasCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND);
        boolean oneHandBlindHitOneCommandGranted =
            hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND);
        boolean oneHandBlindHitTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND);
        boolean oneHandScatterHitOneCommandGranted =
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND);
        boolean oneHandDizzyHitTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND);
        boolean oneHandScatterHitTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND);
        boolean oneHandHealthHitOneCommandGranted =
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND);
        boolean oneHandSpinAttackTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
        boolean oneHandHealthHitTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND);
        boolean twoHandSweepOneCommandGranted =
            hasCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND);
        boolean twoHandSweepTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND);
        boolean twoHandMindHitOneCommandGranted =
            hasCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND);
        boolean twoHandMindHitTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND);
        boolean twoHandHitThreeCommandGranted =
            hasCommand(attacker, TWO_HAND_HIT_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_THREE_COMMAND);
        boolean polearmStunOneCommandGranted =
            hasCommand(attacker, POLEARM_STUN_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_STUN_ONE_COMMAND);
        boolean unarmedBlindOneCommandGranted =
            hasCommand(attacker, UNARMED_BLIND_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_BLIND_ONE_COMMAND);
        boolean unarmedStunOneCommandGranted =
            hasCommand(attacker, UNARMED_STUN_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_STUN_ONE_COMMAND);
        boolean intimidateOneCommandGranted =
            hasCommand(attacker, INTIMIDATE_ONE_COMMAND) ||
            grantCommand(attacker, INTIMIDATE_ONE_COMMAND);
        boolean intimidateTwoCommandGranted =
            hasCommand(attacker, INTIMIDATE_TWO_COMMAND) ||
            grantCommand(attacker, INTIMIDATE_TWO_COMMAND);
        boolean warcryOneCommandGranted =
            hasCommand(attacker, WARCRY_ONE_COMMAND) ||
            grantCommand(attacker, WARCRY_ONE_COMMAND);
        boolean warcryTwoCommandGranted =
            hasCommand(attacker, WARCRY_TWO_COMMAND) ||
            grantCommand(attacker, WARCRY_TWO_COMMAND);
        boolean scatterShotOneCommandGranted =
            hasCommand(attacker, SCATTER_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, SCATTER_SHOT_ONE_COMMAND);
        boolean scatterShotTwoCommandGranted =
            hasCommand(attacker, SCATTER_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, SCATTER_SHOT_TWO_COMMAND);
        boolean wildShotOneCommandGranted =
            hasCommand(attacker, WILD_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, WILD_SHOT_ONE_COMMAND);
        boolean wildShotTwoCommandGranted =
            hasCommand(attacker, WILD_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, WILD_SHOT_TWO_COMMAND);
        boolean legShotTwoCommandGranted =
            hasCommand(attacker, LEG_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, LEG_SHOT_TWO_COMMAND);
        boolean legShotThreeCommandGranted =
            hasCommand(attacker, LEG_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, LEG_SHOT_THREE_COMMAND);
        boolean acidSingleOneCommandGranted =
            hasCommand(attacker, ACID_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, ACID_SINGLE_ONE_COMMAND);
        boolean acidConeOneCommandGranted =
            hasCommand(attacker, ACID_CONE_ONE_COMMAND) ||
            grantCommand(attacker, ACID_CONE_ONE_COMMAND);
        boolean acidConeTwoCommandGranted =
            hasCommand(attacker, ACID_CONE_TWO_COMMAND) ||
            grantCommand(attacker, ACID_CONE_TWO_COMMAND);
        boolean acidSingleTwoCommandGranted =
            hasCommand(attacker, ACID_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, ACID_SINGLE_TWO_COMMAND);
        boolean flameSingleOneCommandGranted =
            hasCommand(attacker, FLAME_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, FLAME_SINGLE_ONE_COMMAND);
        boolean flameSingleTwoCommandGranted =
            hasCommand(attacker, FLAME_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, FLAME_SINGLE_TWO_COMMAND);
        boolean flameConeOneCommandGranted =
            hasCommand(attacker, FLAME_CONE_ONE_COMMAND) ||
            grantCommand(attacker, FLAME_CONE_ONE_COMMAND);
        boolean flameConeTwoCommandGranted =
            hasCommand(attacker, FLAME_CONE_TWO_COMMAND) ||
            grantCommand(attacker, FLAME_CONE_TWO_COMMAND);
        boolean lightningSingleOneCommandGranted =
            hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND);
        boolean lightningConeOneCommandGranted =
            hasCommand(attacker, LIGHTNING_CONE_ONE_COMMAND) ||
            grantCommand(attacker, LIGHTNING_CONE_ONE_COMMAND);
        boolean lightningConeTwoCommandGranted =
            hasCommand(attacker, LIGHTNING_CONE_TWO_COMMAND) ||
            grantCommand(attacker, LIGHTNING_CONE_TWO_COMMAND);
        boolean lightningSingleTwoCommandGranted =
            hasCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND);
        boolean polearmAreaCommandGranted =
            hasCommand(attacker, POLEARM_AREA_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_COMMAND);
        boolean oneHandAreaCommandGranted =
            hasCommand(attacker, ONE_HAND_AREA_COMMAND) ||
            grantCommand(attacker, ONE_HAND_AREA_COMMAND);
        boolean oneHandBodyOneCommandGranted =
            hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_ONE_COMMAND);
        boolean oneHandBodyTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_TWO_COMMAND);
        boolean oneHandBodyThreeCommandGranted =
            hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_THREE_COMMAND);
        boolean oneHandHitOneCommandGranted =
            hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_ONE_COMMAND);
        boolean oneHandHitTwoCommandGranted =
            hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_TWO_COMMAND);
        boolean oneHandHitThreeCommandGranted =
            hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_THREE_COMMAND);
        boolean twoHandAreaCommandGranted =
            hasCommand(attacker, TWO_HAND_AREA_COMMAND) ||
            grantCommand(attacker, TWO_HAND_AREA_COMMAND);
        boolean twoHandAreaTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_AREA_TWO_COMMAND);
        boolean twoHandAccuracyAreaOneCommandGranted =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND);
        boolean twoHandAccuracyAreaTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND);
        boolean twoHandAccuracyAreaThreeCommandGranted =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND);
        boolean twoHandHeadOneCommandGranted =
            hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND);
        boolean twoHandHeadTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND);
        boolean twoHandHeadThreeCommandGranted =
            hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND);
        boolean twoHandHitOneCommandGranted =
            hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_ONE_COMMAND);
        boolean twoHandHitTwoCommandGranted =
            hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_TWO_COMMAND);
        boolean polearmCertificationGranted =
            hasCommand(attacker, POLEARM_CERTIFICATION) ||
            grantCommand(attacker, POLEARM_CERTIFICATION);
        boolean oneHandCertificationGranted =
            hasCommand(attacker, ONE_HAND_CERTIFICATION) ||
            grantCommand(attacker, ONE_HAND_CERTIFICATION);
        boolean twoHandCertificationGranted =
            hasCommand(attacker, TWO_HAND_CERTIFICATION) ||
            grantCommand(attacker, TWO_HAND_CERTIFICATION);
        boolean acidCertificationGranted =
            hasCommand(attacker, ACID_CERTIFICATION) ||
            grantCommand(attacker, ACID_CERTIFICATION);
        boolean flameCertificationGranted =
            hasCommand(attacker, FLAME_CERTIFICATION) ||
            grantCommand(attacker, FLAME_CERTIFICATION);
        boolean lightningCertificationGranted =
            hasCommand(attacker, LIGHTNING_CERTIFICATION) ||
            grantCommand(attacker, LIGHTNING_CERTIFICATION);
        if (!noviceGranted || !rifleGranted || !rifleTwoGranted ||
            !rifleThreeGranted || !rifleFourGranted ||
            !riflemanNoviceGranted || !riflemanAccuracyOneGranted ||
            !riflemanAccuracyTwoGranted || !riflemanAccuracyThreeGranted ||
            !riflemanAccuracyFourGranted || !riflemanSpeedOneGranted ||
            !riflemanSpeedTwoGranted || !riflemanSpeedThreeGranted ||
            !riflemanSpeedFourGranted || !riflemanAbilityOneGranted ||
            !riflemanAbilityTwoGranted || !riflemanAbilityThreeGranted ||
            !riflemanAbilityFourGranted ||
            !riflemanSupportOneGranted || !riflemanSupportTwoGranted ||
            !riflemanSupportThreeGranted || !riflemanSupportFourGranted ||
            !riflemanMasterGranted ||
            !carbineOneGranted || !carbineTwoGranted ||
            !carbineThreeGranted || !carbineFourGranted ||
            !carbineNoviceGranted || !carbineAccuracyOneGranted ||
            !carbineAccuracyTwoGranted || !carbineAccuracyThreeGranted ||
            !carbineAccuracyFourGranted ||
            !carbineSupportOneGranted ||
            !carbineSupportTwoGranted ||
            !carbineSupportThreeGranted ||
            !carbineSupportFourGranted ||
            !carbineSpeedOneGranted ||
            !carbineAbilityFourGranted ||
            !pistolOneGranted || !pistolTwoGranted ||
            !pistolThreeGranted || !pistolFourGranted ||
            !pistolNoviceGranted || !pistolSupportOneGranted ||
            !pistolSupportTwoGranted || !pistolSupportThreeGranted ||
            !supportOneGranted || !supportTwoGranted ||
            !supportFourGranted ||
            !strafeShotOneCommandGranted ||
            !hasCommand(attacker, STRAFE_SHOT_ONE_COMMAND) ||
            !strafeShotTwoCommandGranted ||
            !hasCommand(attacker, STRAFE_SHOT_TWO_COMMAND) ||
            !startleShotOneCommandGranted ||
            !hasCommand(attacker, STARTLE_SHOT_ONE_COMMAND) ||
            !startleShotTwoCommandGranted ||
            !hasCommand(attacker, STARTLE_SHOT_TWO_COMMAND) ||
            !flushingShotOneCommandGranted ||
            !hasCommand(attacker, FLUSHING_SHOT_ONE_COMMAND) ||
            !flushingShotTwoCommandGranted ||
            !hasCommand(attacker, FLUSHING_SHOT_TWO_COMMAND) ||
            !brawlerRootGranted || !hasSkill(attacker, BRAWLER_ROOT) ||
            !brawlerNoviceGranted || !hasSkill(attacker, BRAWLER_NOVICE) ||
            !brawlerOneHandOneGranted ||
            !hasSkill(attacker, BRAWLER_ONE_HAND_ONE) ||
            !brawlerOneHandTwoGranted ||
            !hasSkill(attacker, BRAWLER_ONE_HAND_TWO) ||
            !brawlerOneHandThreeGranted ||
            !hasSkill(attacker, BRAWLER_ONE_HAND_THREE) ||
            !brawlerOneHandFourGranted ||
            !hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) ||
            !oneHandSwordNoviceGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_NOVICE) ||
            !oneHandSwordSupportOneGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) ||
            !oneHandSwordSupportTwoGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) ||
            !oneHandSwordSupportThreeGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) ||
            !oneHandSwordSupportFourGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) ||
            !oneHandSwordAccuracyOneGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE) ||
            !oneHandSwordAccuracyTwoGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO) ||
            !oneHandSwordAccuracyThreeGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE) ||
            !oneHandSwordAccuracyFourGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) ||
            !oneHandSwordSpeedOneGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE) ||
            !oneHandSwordSpeedTwoGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO) ||
            !oneHandSwordSpeedThreeGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE) ||
            !oneHandSwordSpeedFourGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) ||
            !oneHandSwordAbilityOneGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE) ||
            !oneHandSwordAbilityTwoGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO) ||
            !oneHandSwordAbilityThreeGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE) ||
            !oneHandSwordAbilityFourGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) ||
            !oneHandSwordMasterGranted ||
            !hasSkill(attacker, ONE_HAND_SWORD_MASTER) ||
            !brawlerTwoHandOneGranted ||
            !hasSkill(attacker, BRAWLER_TWO_HAND_ONE) ||
            !brawlerTwoHandTwoGranted ||
            !hasSkill(attacker, BRAWLER_TWO_HAND_TWO) ||
            !brawlerTwoHandThreeGranted ||
            !hasSkill(attacker, BRAWLER_TWO_HAND_THREE) ||
            !brawlerTwoHandFourGranted ||
            !hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) ||
            !twoHandSwordNoviceGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_NOVICE) ||
            !twoHandSwordAccuracyOneGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE) ||
            !twoHandSwordAccuracyTwoGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO) ||
            !twoHandSwordAccuracyThreeGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE) ||
            !twoHandSwordAccuracyFourGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) ||
            !twoHandSwordSpeedOneGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE) ||
            !twoHandSwordSpeedTwoGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO) ||
            !twoHandSwordSpeedThreeGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE) ||
            !twoHandSwordSpeedFourGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) ||
            !twoHandSwordAbilityOneGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE) ||
            !twoHandSwordAbilityTwoGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO) ||
            !twoHandSwordAbilityThreeGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE) ||
            !twoHandSwordAbilityFourGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) ||
            !twoHandSwordSupportOneGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE) ||
            !twoHandSwordSupportTwoGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO) ||
            !twoHandSwordSupportThreeGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE) ||
            !twoHandSwordSupportFourGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) ||
            !twoHandSwordMasterGranted ||
            !hasSkill(attacker, TWO_HAND_SWORD_MASTER) ||
            !brawlerPolearmOneGranted ||
            !hasSkill(attacker, BRAWLER_POLEARM_ONE) ||
            !brawlerPolearmTwoGranted ||
            !hasSkill(attacker, BRAWLER_POLEARM_TWO) ||
            !brawlerPolearmThreeGranted ||
            !hasSkill(attacker, BRAWLER_POLEARM_THREE) ||
            !brawlerPolearmFourGranted ||
            !hasSkill(attacker, BRAWLER_POLEARM_FOUR) ||
            !polearmNoviceGranted ||
            !hasSkill(attacker, POLEARM_NOVICE) ||
            !polearmAccuracyOneGranted ||
            !hasSkill(attacker, POLEARM_ACCURACY_ONE) ||
            !polearmAccuracyTwoGranted ||
            !hasSkill(attacker, POLEARM_ACCURACY_TWO) ||
            !polearmAccuracyThreeGranted ||
            !hasSkill(attacker, POLEARM_ACCURACY_THREE) ||
            !polearmAccuracyFourGranted ||
            !hasSkill(attacker, POLEARM_ACCURACY_FOUR) ||
            !polearmSpeedOneGranted ||
            !hasSkill(attacker, POLEARM_SPEED_ONE) ||
            !polearmSpeedTwoGranted ||
            !hasSkill(attacker, POLEARM_SPEED_TWO) ||
            !polearmSpeedThreeGranted ||
            !hasSkill(attacker, POLEARM_SPEED_THREE) ||
            !polearmSpeedFourGranted ||
            !hasSkill(attacker, POLEARM_SPEED_FOUR) ||
            !polearmAbilityOneGranted ||
            !hasSkill(attacker, POLEARM_ABILITY_ONE) ||
            !polearmAbilityTwoGranted ||
            !hasSkill(attacker, POLEARM_ABILITY_TWO) ||
            !polearmAbilityThreeGranted ||
            !hasSkill(attacker, POLEARM_ABILITY_THREE) ||
            !polearmAbilityFourGranted ||
            !hasSkill(attacker, POLEARM_ABILITY_FOUR) ||
            !polearmSupportOneGranted ||
            !hasSkill(attacker, POLEARM_SUPPORT_ONE) ||
            !polearmSupportTwoGranted ||
            !hasSkill(attacker, POLEARM_SUPPORT_TWO) ||
            !polearmSupportThreeGranted ||
            !hasSkill(attacker, POLEARM_SUPPORT_THREE) ||
            !polearmSupportFourGranted ||
            !hasSkill(attacker, POLEARM_SUPPORT_FOUR) ||
            !polearmMasterGranted ||
            !hasSkill(attacker, POLEARM_MASTER) ||
            !brawlerUnarmedOneGranted ||
            !hasSkill(attacker, BRAWLER_UNARMED_ONE) ||
            !brawlerUnarmedTwoGranted ||
            !hasSkill(attacker, BRAWLER_UNARMED_TWO) ||
            !brawlerUnarmedThreeGranted ||
            !hasSkill(attacker, BRAWLER_UNARMED_THREE) ||
            !brawlerUnarmedFourGranted ||
            !hasSkill(attacker, BRAWLER_UNARMED_FOUR) ||
            !brawlerMasterGranted ||
            !hasSkill(attacker, BRAWLER_MASTER) ||
            !polearmLungeOneCommandGranted ||
            !hasCommand(attacker, POLEARM_LUNGE_ONE_COMMAND) ||
            !unarmedLungeOneCommandGranted ||
            !hasCommand(attacker, UNARMED_LUNGE_ONE_COMMAND) ||
            !oneHandLungeOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND) ||
            !twoHandLungeOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND) ||
            !polearmLungeTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_LUNGE_TWO_COMMAND) ||
            !unarmedLungeTwoCommandGranted ||
            !hasCommand(attacker, UNARMED_LUNGE_TWO_COMMAND) ||
            !oneHandLungeTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND) ||
            !twoHandLungeTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND) ||
            !tauntCommandGranted ||
            !hasCommand(attacker, TAUNT_COMMAND) ||
            !oneHandDizzyHitOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND) ||
            !oneHandBlindHitOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) ||
            !oneHandBlindHitTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND) ||
            !oneHandScatterHitOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND) ||
            !oneHandDizzyHitTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND) ||
            !oneHandScatterHitTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND) ||
            !oneHandHealthHitOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND) ||
            !oneHandSpinAttackTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) ||
            !oneHandHealthHitTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND) ||
            !twoHandSweepOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND) ||
            !twoHandSweepTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND) ||
            !twoHandMindHitOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND) ||
            !twoHandMindHitTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND) ||
            !twoHandHitThreeCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HIT_THREE_COMMAND) ||
            !polearmStunOneCommandGranted ||
            !hasCommand(attacker, POLEARM_STUN_ONE_COMMAND) ||
            !unarmedBlindOneCommandGranted ||
            !hasCommand(attacker, UNARMED_BLIND_ONE_COMMAND) ||
            !unarmedStunOneCommandGranted ||
            !hasCommand(attacker, UNARMED_STUN_ONE_COMMAND) ||
            !intimidateOneCommandGranted ||
            !hasCommand(attacker, INTIMIDATE_ONE_COMMAND) ||
            !intimidateTwoCommandGranted ||
            !hasCommand(attacker, INTIMIDATE_TWO_COMMAND) ||
            !warcryOneCommandGranted ||
            !hasCommand(attacker, WARCRY_ONE_COMMAND) ||
            !warcryTwoCommandGranted ||
            !hasCommand(attacker, WARCRY_TWO_COMMAND) ||
            !hasCommand(attacker, COMMAND) ||
            !durationControlGranted || !hasCommand(attacker, DURATION_CONTROL_COMMAND) ||
            !headShotThreeGranted || !hasCommand(attacker, HEAD_SHOT_THREE_COMMAND) ||
            !bodyShotTwoGranted || !hasCommand(attacker, BODY_SHOT_TWO_COMMAND) ||
            !bodyShotThreeGranted || !hasCommand(attacker, BODY_SHOT_THREE_COMMAND) ||
            !healthShotOneGranted ||
            !hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND) ||
            !healthShotTwoGranted ||
            !hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND) ||
            !pistolMeleeDefenseOneGranted ||
            !hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) ||
            !pistolMeleeDefenseTwoGranted ||
            !hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND) ||
            !tumbleToProneGranted ||
            !hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) ||
            !tumbleToKneelingGranted ||
            !hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) ||
            !tumbleToStandingGranted ||
            !hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND) ||
            !actionShotOneGranted ||
            !hasCommand(attacker, ACTION_SHOT_ONE_COMMAND) ||
            !actionShotTwoGranted ||
            !hasCommand(attacker, ACTION_SHOT_TWO_COMMAND) ||
            !mindShotOneGranted ||
            !hasCommand(attacker, MIND_SHOT_ONE_COMMAND) ||
            !mindShotTwoGranted ||
            !hasCommand(attacker, MIND_SHOT_TWO_COMMAND) ||
            !surpriseShotGranted ||
            !hasCommand(attacker, SURPRISE_SHOT_COMMAND) ||
            !sniperShotGranted ||
            !hasCommand(attacker, SNIPER_SHOT_COMMAND) ||
            !concealShotGranted ||
            !hasCommand(attacker, CONCEAL_SHOT_COMMAND) ||
            !flurryShotOneGranted ||
            !hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND) ||
            !flurryShotTwoGranted ||
            !hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND) ||
            !cdefCertificationGranted || !hasCommand(attacker, CDEF_CERTIFICATION) ||
            !pistolCdefCertificationGranted ||
            !hasCommand(attacker, PISTOL_CDEF_CERTIFICATION) ||
            !carbineCdefCertificationGranted ||
            !hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) ||
            !polearmCommandGranted || !hasCommand(attacker, POLEARM_COMMAND) ||
            !polearmLegTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) ||
            !polearmLegThreeCommandGranted ||
            !hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) ||
            !polearmHitOneCommandGranted ||
            !hasCommand(attacker, POLEARM_HIT_ONE_COMMAND) ||
            !polearmHitTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) ||
            !polearmStunTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) ||
            !polearmSpinTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) ||
            !polearmAreaOneCommandGranted ||
            !hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) ||
            !polearmAreaTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) ||
            !polearmSweepOneCommandGranted ||
            !hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) ||
            !polearmSweepTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) ||
            !polearmActionHitOneCommandGranted ||
            !hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) ||
            !polearmActionHitTwoCommandGranted ||
            !hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) ||
            !polearmHitThreeCommandGranted ||
            !hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) ||
            !unarmedCommandGranted || !hasCommand(attacker, UNARMED_COMMAND) ||
            !unarmedHitOneCommandGranted ||
            !hasCommand(attacker, UNARMED_HIT_ONE_COMMAND) ||
            !unarmedHitTwoCommandGranted ||
            !hasCommand(attacker, UNARMED_HIT_TWO_COMMAND) ||
            !unarmedBodyOneCommandGranted ||
            !hasCommand(attacker, UNARMED_BODY_ONE_COMMAND) ||
            !unarmedLegOneCommandGranted ||
            !hasCommand(attacker, UNARMED_LEG_ONE_COMMAND) ||
            !unarmedSpinOneCommandGranted ||
            !hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND) ||
            !unarmedSpinTwoCommandGranted ||
            !hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND) ||
            !overchargeOneCommandGranted ||
            !hasCommand(attacker, OVERCHARGE_ONE_COMMAND) ||
            !overchargeTwoCommandGranted ||
            !hasCommand(attacker, OVERCHARGE_TWO_COMMAND) ||
            !pointBlankSingleOneCommandGranted ||
            !hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND) ||
            !aimCommandGranted ||
            !hasCommand(attacker, AIM_COMMAND) ||
            !threatenShotCommandGranted ||
            !hasCommand(attacker, THREATEN_SHOT_COMMAND) ||
            !warningShotCommandGranted ||
            !hasCommand(attacker, WARNING_SHOT_COMMAND) ||
            !suppressionFireOneCommandGranted ||
            !hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND) ||
            !suppressionFireTwoCommandGranted ||
            !hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND) ||
            !rollShotCommandGranted || !hasCommand(attacker, ROLL_SHOT_COMMAND) ||
            !diveShotCommandGranted || !hasCommand(attacker, DIVE_SHOT_COMMAND) ||
            !kipUpShotCommandGranted || !hasCommand(attacker, KIP_UP_SHOT_COMMAND) ||
            !takeCoverCommandGranted || !hasCommand(attacker, TAKE_COVER_COMMAND) ||
            !fullAutoSingleOneCommandGranted ||
            !hasCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND) ||
            !fullAutoSingleTwoCommandGranted ||
            !hasCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND) ||
            !fullAutoAreaOneCommandGranted ||
            !hasCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND) ||
            !fullAutoAreaTwoCommandGranted ||
            !hasCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND) ||
            !chargeShotOneCommandGranted ||
            !hasCommand(attacker, CHARGE_SHOT_ONE_COMMAND) ||
            !chargeShotTwoCommandGranted ||
            !hasCommand(attacker, CHARGE_SHOT_TWO_COMMAND) ||
            !scatterShotOneCommandGranted ||
            !hasCommand(attacker, SCATTER_SHOT_ONE_COMMAND) ||
            !scatterShotTwoCommandGranted ||
            !hasCommand(attacker, SCATTER_SHOT_TWO_COMMAND) ||
            !wildShotOneCommandGranted ||
            !hasCommand(attacker, WILD_SHOT_ONE_COMMAND) ||
            !wildShotTwoCommandGranted ||
            !hasCommand(attacker, WILD_SHOT_TWO_COMMAND) ||
            !legShotTwoCommandGranted ||
            !hasCommand(attacker, LEG_SHOT_TWO_COMMAND) ||
            !legShotThreeCommandGranted ||
            !hasCommand(attacker, LEG_SHOT_THREE_COMMAND) ||
            !acidSingleOneCommandGranted ||
            !hasCommand(attacker, ACID_SINGLE_ONE_COMMAND) ||
            !acidConeOneCommandGranted ||
            !hasCommand(attacker, ACID_CONE_ONE_COMMAND) ||
            !acidConeTwoCommandGranted ||
            !hasCommand(attacker, ACID_CONE_TWO_COMMAND) ||
            !acidSingleTwoCommandGranted ||
            !hasCommand(attacker, ACID_SINGLE_TWO_COMMAND) ||
            !flameSingleOneCommandGranted ||
            !hasCommand(attacker, FLAME_SINGLE_ONE_COMMAND) ||
            !flameSingleTwoCommandGranted ||
            !hasCommand(attacker, FLAME_SINGLE_TWO_COMMAND) ||
            !flameConeOneCommandGranted ||
            !hasCommand(attacker, FLAME_CONE_ONE_COMMAND) ||
            !flameConeTwoCommandGranted ||
            !hasCommand(attacker, FLAME_CONE_TWO_COMMAND) ||
            !lightningSingleOneCommandGranted ||
            !hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND) ||
            !lightningConeOneCommandGranted ||
            !hasCommand(attacker, LIGHTNING_CONE_ONE_COMMAND) ||
            !lightningConeTwoCommandGranted ||
            !hasCommand(attacker, LIGHTNING_CONE_TWO_COMMAND) ||
            !lightningSingleTwoCommandGranted ||
            !hasCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND) ||
            !polearmAreaCommandGranted ||
            !hasCommand(attacker, POLEARM_AREA_COMMAND) ||
            !oneHandAreaCommandGranted ||
            !hasCommand(attacker, ONE_HAND_AREA_COMMAND) ||
            !oneHandBodyOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND) ||
            !oneHandBodyTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) ||
            !oneHandBodyThreeCommandGranted ||
            !hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND) ||
            !oneHandHitOneCommandGranted ||
            !hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND) ||
            !oneHandHitTwoCommandGranted ||
            !hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND) ||
            !oneHandHitThreeCommandGranted ||
            !hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) ||
            !twoHandAreaCommandGranted ||
            !hasCommand(attacker, TWO_HAND_AREA_COMMAND) ||
            !twoHandAreaTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) ||
            !twoHandAccuracyAreaOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) ||
            !twoHandAccuracyAreaTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) ||
            !twoHandAccuracyAreaThreeCommandGranted ||
            !hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) ||
            !twoHandHeadOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND) ||
            !twoHandHeadTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) ||
            !twoHandHeadThreeCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) ||
            !twoHandHitOneCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND) ||
            !twoHandHitTwoCommandGranted ||
            !hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND) ||
            !polearmCertificationGranted ||
            !hasCommand(attacker, POLEARM_CERTIFICATION) ||
            !oneHandCertificationGranted ||
            !hasCommand(attacker, ONE_HAND_CERTIFICATION) ||
            !twoHandCertificationGranted ||
            !hasCommand(attacker, TWO_HAND_CERTIFICATION) ||
            !acidCertificationGranted ||
            !hasCommand(attacker, ACID_CERTIFICATION) ||
            !flameCertificationGranted ||
            !hasCommand(attacker, FLAME_CERTIFICATION) ||
            !lightningCertificationGranted ||
            !hasCommand(attacker, LIGHTNING_CERTIFICATION))
        {
            restore(attacker, defender);
            return "error=skillPreparationFailed novice=" + noviceGranted +
                " rifleOne=" + rifleGranted + " hasCommand=" + hasCommand(attacker, COMMAND) +
                " supportOne=" + supportOneGranted +
                " durationControl=" + hasCommand(attacker, DURATION_CONTROL_COMMAND) +
                " headShotThree=" + hasCommand(attacker, HEAD_SHOT_THREE_COMMAND) +
                " bodyShotTwo=" + hasCommand(attacker, BODY_SHOT_TWO_COMMAND) +
                " bodyShotThree=" + hasCommand(attacker, BODY_SHOT_THREE_COMMAND) +
                " healthShotOne=" + hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND) +
                " healthShotTwo=" + hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND) +
                " pistolMeleeDefenseOne=" +
                    hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) +
                " pistolMeleeDefenseTwo=" +
                    hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND) +
                " tumbleToProne=" +
                    hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) +
                " tumbleToKneeling=" +
                    hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) +
                " tumbleToStanding=" +
                    hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND) +
                " mindShotOne=" + hasCommand(attacker, MIND_SHOT_ONE_COMMAND) +
                " mindShotTwo=" + hasCommand(attacker, MIND_SHOT_TWO_COMMAND) +
                " surpriseShot=" + hasCommand(attacker, SURPRISE_SHOT_COMMAND) +
                " sniperShot=" + hasCommand(attacker, SNIPER_SHOT_COMMAND) +
                " concealShot=" + hasCommand(attacker, CONCEAL_SHOT_COMMAND) +
                " flurryShotOne=" +
                    hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND) +
                " flurryShotTwo=" +
                    hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND) +
                " cdefCertification=" + hasCommand(attacker, CDEF_CERTIFICATION) +
                " pistolCdefCertification=" +
                    hasCommand(attacker, PISTOL_CDEF_CERTIFICATION) +
                " polearmCommand=" + hasCommand(attacker, POLEARM_COMMAND) +
                " polearmLegTwoCommand=" +
                    hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) +
                " polearmLegThreeCommand=" +
                    hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) +
                " polearmHitOneCommand=" +
                    hasCommand(attacker, POLEARM_HIT_ONE_COMMAND) +
                " polearmHitTwoCommand=" +
                    hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) +
                " polearmStunTwoCommand=" +
                    hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) +
                " polearmSpinTwoCommand=" +
                    hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) +
                " polearmAreaOneCommand=" +
                    hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) +
                " polearmAreaTwoCommand=" +
                    hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) +
                " polearmSweepOneCommand=" +
                    hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) +
                " polearmSweepTwoCommand=" +
                    hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) +
                " polearmActionHitOneCommand=" +
                    hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) +
                " polearmActionHitTwoCommand=" +
                    hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) +
                " polearmHitThreeCommand=" +
                    hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) +
                " unarmedCommand=" + hasCommand(attacker, UNARMED_COMMAND) +
                " unarmedHitOneCommand=" +
                    hasCommand(attacker, UNARMED_HIT_ONE_COMMAND) +
                " unarmedHitTwoCommand=" +
                    hasCommand(attacker, UNARMED_HIT_TWO_COMMAND) +
                " unarmedBodyOneCommand=" +
                    hasCommand(attacker, UNARMED_BODY_ONE_COMMAND) +
                " unarmedLegOneCommand=" +
                    hasCommand(attacker, UNARMED_LEG_ONE_COMMAND) +
                " unarmedSpinOneCommand=" +
                    hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND) +
                " unarmedSpinTwoCommand=" +
                    hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND) +
                " overchargeTwoCommand=" +
                    hasCommand(attacker, OVERCHARGE_TWO_COMMAND) +
                " acidSingleOneCommand=" +
                    hasCommand(attacker, ACID_SINGLE_ONE_COMMAND) +
                " acidConeOneCommand=" +
                    hasCommand(attacker, ACID_CONE_ONE_COMMAND) +
                " acidConeTwoCommand=" +
                    hasCommand(attacker, ACID_CONE_TWO_COMMAND) +
                " acidSingleTwoCommand=" +
                    hasCommand(attacker, ACID_SINGLE_TWO_COMMAND) +
                " flameSingleOneCommand=" +
                    hasCommand(attacker, FLAME_SINGLE_ONE_COMMAND) +
                " flameSingleTwoCommand=" +
                    hasCommand(attacker, FLAME_SINGLE_TWO_COMMAND) +
                " flameConeOneCommand=" +
                    hasCommand(attacker, FLAME_CONE_ONE_COMMAND) +
                " flameConeTwoCommand=" +
                    hasCommand(attacker, FLAME_CONE_TWO_COMMAND) +
                " lightningSingleOneCommand=" +
                    hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND) +
                " lightningConeOneCommand=" +
                    hasCommand(attacker, LIGHTNING_CONE_ONE_COMMAND) +
                " lightningConeTwoCommand=" +
                    hasCommand(attacker, LIGHTNING_CONE_TWO_COMMAND) +
                " lightningSingleTwoCommand=" +
                    hasCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND) +
                " polearmAreaCommand=" +
                    hasCommand(attacker, POLEARM_AREA_COMMAND) +
                " oneHandAreaCommand=" +
                    hasCommand(attacker, ONE_HAND_AREA_COMMAND) +
                " oneHandBodyOneCommand=" +
                    hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND) +
                " oneHandBodyTwoCommand=" +
                    hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) +
                " oneHandBodyThreeCommand=" +
                    hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND) +
                " oneHandHitOneCommand=" +
                    hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND) +
                " oneHandHitTwoCommand=" +
                    hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND) +
                " oneHandHitThreeCommand=" +
                    hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) +
                " twoHandAreaCommand=" +
                    hasCommand(attacker, TWO_HAND_AREA_COMMAND) +
                " twoHandAreaTwoCommand=" +
                    hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) +
                " twoHandHeadOneCommand=" +
                    hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND) +
                " twoHandHeadTwoCommand=" +
                    hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) +
                " twoHandHeadThreeCommand=" +
                    hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) +
                " twoHandHitOneCommand=" +
                    hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND) +
                " twoHandHitTwoCommand=" +
                    hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND) +
                " polearmCertification=" +
                    hasCommand(attacker, POLEARM_CERTIFICATION) +
                " oneHandCertification=" +
                    hasCommand(attacker, ONE_HAND_CERTIFICATION) +
                " twoHandCertification=" +
                    hasCommand(attacker, TWO_HAND_CERTIFICATION) +
                " acidCertification=" + hasCommand(attacker, ACID_CERTIFICATION) +
                " flameCertification=" + hasCommand(attacker, FLAME_CERTIFICATION) +
                " lightningCertification=" + hasCommand(attacker, LIGHTNING_CERTIFICATION);
        }

        obj_id fixtureWeapon = createObjectInInventoryAllowOverload(CDEF_TEMPLATE, attacker);
        if (!isIdValid(fixtureWeapon) || !CDEF_TEMPLATE.equals(getTemplateName(fixtureWeapon)))
        {
            if (isIdValid(fixtureWeapon))
            {
                destroyObject(fixtureWeapon);
            }
            restore(attacker, defender);
            return "error=fixtureWeaponCreationFailed";
        }
        setObjVar(attacker, FIXTURE_WEAPON, fixtureWeapon);
        obj_id fixturePistol =
            createObjectInInventoryAllowOverload(CDEF_PISTOL_TEMPLATE, attacker);
        if (!isIdValid(fixturePistol) ||
            !CDEF_PISTOL_TEMPLATE.equals(getTemplateName(fixturePistol)))
        {
            if (isIdValid(fixturePistol))
            {
                destroyObject(fixturePistol);
            }
            restore(attacker, defender);
            return "error=fixturePistolCreationFailed";
        }
        setObjVar(attacker, FIXTURE_PISTOL, fixturePistol);
        obj_id fixtureCarbine =
            createObjectInInventoryAllowOverload(CDEF_CARBINE_TEMPLATE, attacker);
        if (!isIdValid(fixtureCarbine) ||
            !CDEF_CARBINE_TEMPLATE.equals(getTemplateName(fixtureCarbine)))
        {
            if (isIdValid(fixtureCarbine))
            {
                destroyObject(fixtureCarbine);
            }
            restore(attacker, defender);
            return "error=fixtureCarbineCreationFailed";
        }
        setObjVar(attacker, FIXTURE_CARBINE, fixtureCarbine);
        obj_id fixturePolearm =
            createObjectInInventoryAllowOverload(POLEARM_TEMPLATE, attacker);
        if (!isIdValid(fixturePolearm) ||
            !POLEARM_TEMPLATE.equals(getTemplateName(fixturePolearm)))
        {
            if (isIdValid(fixturePolearm))
            {
                destroyObject(fixturePolearm);
            }
            restore(attacker, defender);
            return "error=fixturePolearmCreationFailed";
        }
        setObjVar(attacker, FIXTURE_POLEARM, fixturePolearm);
        obj_id fixtureOneHand =
            createObjectInInventoryAllowOverload(ONE_HAND_TEMPLATE, attacker);
        if (!isIdValid(fixtureOneHand) ||
            !ONE_HAND_TEMPLATE.equals(getTemplateName(fixtureOneHand)))
        {
            if (isIdValid(fixtureOneHand))
            {
                destroyObject(fixtureOneHand);
            }
            restore(attacker, defender);
            return "error=fixtureOneHandCreationFailed";
        }
        setObjVar(attacker, FIXTURE_ONE_HAND, fixtureOneHand);
        obj_id fixtureTwoHand =
            createObjectInInventoryAllowOverload(TWO_HAND_TEMPLATE, attacker);
        if (!isIdValid(fixtureTwoHand) ||
            !TWO_HAND_TEMPLATE.equals(getTemplateName(fixtureTwoHand)))
        {
            if (isIdValid(fixtureTwoHand))
            {
                destroyObject(fixtureTwoHand);
            }
            restore(attacker, defender);
            return "error=fixtureTwoHandCreationFailed";
        }
        setObjVar(attacker, FIXTURE_TWO_HAND, fixtureTwoHand);
        obj_id fixtureAcid =
            createObjectInInventoryAllowOverload(ACID_TEMPLATE, attacker);
        if (!isIdValid(fixtureAcid) ||
            !ACID_TEMPLATE.equals(getTemplateName(fixtureAcid)))
        {
            if (isIdValid(fixtureAcid))
            {
                destroyObject(fixtureAcid);
            }
            restore(attacker, defender);
            return "error=fixtureAcidCreationFailed";
        }
        setObjVar(attacker, FIXTURE_ACID, fixtureAcid);
        obj_id fixtureFlame =
            createObjectInInventoryAllowOverload(FLAME_TEMPLATE, attacker);
        if (!isIdValid(fixtureFlame) ||
            !FLAME_TEMPLATE.equals(getTemplateName(fixtureFlame)))
        {
            if (isIdValid(fixtureFlame))
            {
                destroyObject(fixtureFlame);
            }
            restore(attacker, defender);
            return "error=fixtureFlameCreationFailed";
        }
        setObjVar(attacker, FIXTURE_FLAME, fixtureFlame);
        obj_id fixtureLightning =
            createObjectInInventoryAllowOverload(LIGHTNING_TEMPLATE, attacker);
        if (!isIdValid(fixtureLightning) ||
            !LIGHTNING_TEMPLATE.equals(getTemplateName(fixtureLightning)))
        {
            if (isIdValid(fixtureLightning))
            {
                destroyObject(fixtureLightning);
            }
            restore(attacker, defender);
            return "error=fixtureLightningCreationFailed";
        }
        setObjVar(attacker, FIXTURE_LIGHTNING, fixtureLightning);

        location attackerDestination =
            new location(3500.0f, 5.0f, -4800.0f, "tatooine", null);
        location defenderDestination =
            new location(3506.0f, 5.0f, -4800.0f, "tatooine", null);
        boolean attackerMoved = setLocation(attacker, attackerDestination);
        boolean defenderMoved = setLocation(defender, defenderDestination);
        boolean stateReady = reassertPreparedState(attacker, defender);
        boolean hamReady =
            prepareFixtureHam(attacker) & prepareFixtureHam(defender);
        if (!attackerMoved || !defenderMoved || !stateReady || !hamReady)
        {
            String failedStatus = buildStatus(attacker, defender, lifecycle).replace(' ', '_');
            boolean restored = restore(attacker, defender);
            return "error=combatPreparationFailed attackerMoved=" + attackerMoved +
                " defenderMoved=" + defenderMoved + " stateReady=" + stateReady +
                " hamReady=" + hamReady +
                " restored=" + restored + " failedStatus=" + failedStatus;
        }

        setObjVar(attacker, PREPARED, 1);
        setObjVar(defender, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(attacker, defender, lifecycle);
    }

    private String recoverPartial(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        boolean attackerRoot = hasObjVar(attacker, ROOT);
        boolean defenderRoot = hasObjVar(defender, ROOT);
        if (!attackerRoot && !defenderRoot)
        {
            return "action=recover alreadyClean=true lifecycle=" + lifecycle;
        }
        if ((attackerRoot && hasObjVar(attacker, LIFECYCLE) &&
                !lifecycle.equals(getStringObjVar(attacker, LIFECYCLE))) ||
            (defenderRoot && hasObjVar(defender, LIFECYCLE) &&
                !lifecycle.equals(getStringObjVar(defender, LIFECYCLE))))
        {
            return "error=fixtureOwnershipMismatch";
        }

        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        combat.clearCombatDebuffs(attacker);
        combat.clearCombatDebuffs(defender);
        String fireDotId = dot.DOT_FIRE + attacker;
        if (dot.getDotStrength(defender, fireDotId) >= 0)
        {
            dot.removeDotEffect(defender, fireDotId, false);
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }

        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PERSONAL_ENEMY) &&
            getIntObjVar(attacker, ORIGINAL_PERSONAL_ENEMY) == 0)
        {
            pvpRemovePersonalEnemyFlags(attacker, defender);
        }
        if (defenderRoot && hasObjVar(defender, ORIGINAL_PERSONAL_ENEMY) &&
            getIntObjVar(defender, ORIGINAL_PERSONAL_ENEMY) == 0)
        {
            pvpRemovePersonalEnemyFlags(defender, attacker);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_DURATION_CONTROL) &&
            getIntObjVar(attacker, ORIGINAL_DURATION_CONTROL) == 0 &&
            hasCommand(attacker, DURATION_CONTROL_COMMAND))
        {
            revokeCommand(attacker, DURATION_CONTROL_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_HEAD_SHOT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEAD_SHOT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, HEAD_SHOT_THREE_COMMAND))
        {
            revokeCommand(attacker, HEAD_SHOT_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BODY_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_BODY_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, BODY_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, BODY_SHOT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BODY_SHOT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_BODY_SHOT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, BODY_SHOT_THREE_COMMAND))
        {
            revokeCommand(attacker, BODY_SHOT_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_HEALTH_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEALTH_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, HEALTH_SHOT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, HEALTH_SHOT_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND))
        {
            revokeCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND))
        {
            revokeCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(
                attacker, ORIGINAL_TUMBLE_TO_STANDING_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_TUMBLE_TO_STANDING_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_STANDING_COMMAND);
        }
        if (attackerRoot && hasObjVar(
                attacker, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_KNEELING_COMMAND);
        }
        if (attackerRoot && hasObjVar(
                attacker, ORIGINAL_TUMBLE_TO_PRONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TUMBLE_TO_PRONE_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_PRONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ACTION_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACTION_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ACTION_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, ACTION_SHOT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ACTION_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACTION_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ACTION_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, ACTION_SHOT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_MIND_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_MIND_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, MIND_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, MIND_SHOT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, MIND_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, MIND_SHOT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, SURPRISE_SHOT_COMMAND))
        {
            revokeCommand(attacker, SURPRISE_SHOT_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_SNIPER_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SNIPER_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, SNIPER_SHOT_COMMAND))
        {
            revokeCommand(attacker, SNIPER_SHOT_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, CONCEAL_SHOT_COMMAND))
        {
            revokeCommand(attacker, CONCEAL_SHOT_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, FLURRY_SHOT_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, FLURRY_SHOT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, CDEF_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, PISTOL_CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, PISTOL_CDEF_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_COMMAND))
        {
            revokeCommand(attacker, POLEARM_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_LEG_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_LEG_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_LEG_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_LEG_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_LEG_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_LEG_THREE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_LEG_THREE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_LEG_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_POLEARM_STUN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_STUN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_STUN_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_STUN_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_POLEARM_SPIN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPIN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SPIN_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_AREA_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SWEEP_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SWEEP_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_HIT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_THREE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_UNARMED_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_COMMAND))
        {
            revokeCommand(attacker, UNARMED_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_HIT_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, UNARMED_HIT_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_BODY_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_BODY_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_BODY_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_BODY_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_LEG_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_LEG_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_LEG_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_LEG_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_SPIN_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_SPIN_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_SPIN_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_UNARMED_SPIN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_SPIN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND))
        {
            revokeCommand(attacker, UNARMED_SPIN_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_OVERCHARGE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_OVERCHARGE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, OVERCHARGE_ONE_COMMAND))
        {
            revokeCommand(attacker, OVERCHARGE_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_OVERCHARGE_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_OVERCHARGE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, OVERCHARGE_TWO_COMMAND))
        {
            revokeCommand(attacker, OVERCHARGE_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND);
        }
        if (attackerRoot)
        {
            removeAttribOrSkillModModifier(
                attacker, combat_base.PRECU_AIM_MODIFIER);
            setState(attacker, STATE_AIMING, false);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_AIM_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_AIM_COMMAND) == 0 &&
            hasCommand(attacker, AIM_COMMAND))
        {
            revokeCommand(attacker, AIM_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_THREATEN_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_THREATEN_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, THREATEN_SHOT_COMMAND))
        {
            revokeCommand(attacker, THREATEN_SHOT_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_WARNING_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_WARNING_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, WARNING_SHOT_COMMAND))
        {
            revokeCommand(attacker, WARNING_SHOT_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND))
        {
            revokeCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND))
        {
            revokeCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND);
        }
        if (attackerRoot)
        {
            restoreCommand(attacker, ORIGINAL_ROLL_SHOT_COMMAND,
                ROLL_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_DIVE_SHOT_COMMAND,
                DIVE_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_KIP_UP_SHOT_COMMAND,
                KIP_UP_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_TAKE_COVER_COMMAND,
                TAKE_COVER_COMMAND);
            restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND,
                FULL_AUTO_SINGLE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND,
                FULL_AUTO_SINGLE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND,
                FULL_AUTO_AREA_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND,
                FULL_AUTO_AREA_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_ONE_COMMAND,
                CHARGE_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_TWO_COMMAND,
                CHARGE_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_ONE_COMMAND,
                STRAFE_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_TWO_COMMAND,
                STRAFE_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_ONE_COMMAND,
                STARTLE_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_TWO_COMMAND,
                STARTLE_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND,
                FLUSHING_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND,
                FLUSHING_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND,
                POLEARM_LUNGE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND,
                UNARMED_LUNGE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND,
                ONE_HAND_LUNGE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND,
                TWO_HAND_LUNGE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND,
                POLEARM_LUNGE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND,
                UNARMED_LUNGE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND,
                ONE_HAND_LUNGE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND,
                TWO_HAND_LUNGE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_TAUNT_COMMAND, TAUNT_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND,
                ONE_HAND_DIZZY_HIT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND,
                ONE_HAND_BLIND_HIT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND,
                ONE_HAND_BLIND_HIT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND,
                ONE_HAND_SCATTER_HIT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND,
                ONE_HAND_DIZZY_HIT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND,
                ONE_HAND_SCATTER_HIT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND,
                ONE_HAND_HEALTH_HIT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND,
                ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND,
                ONE_HAND_HEALTH_HIT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND,
                TWO_HAND_SWEEP_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND,
                TWO_HAND_SWEEP_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND,
                TWO_HAND_MIND_HIT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND,
                TWO_HAND_MIND_HIT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND,
                TWO_HAND_HIT_THREE_COMMAND);
            restoreCommand(attacker, ORIGINAL_POLEARM_STUN_ONE_COMMAND,
                POLEARM_STUN_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_UNARMED_BLIND_ONE_COMMAND,
                UNARMED_BLIND_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_UNARMED_STUN_ONE_COMMAND,
                UNARMED_STUN_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_INTIMIDATE_ONE_COMMAND,
                INTIMIDATE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_INTIMIDATE_TWO_COMMAND,
                INTIMIDATE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_WARCRY_ONE_COMMAND,
                WARCRY_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_WARCRY_TWO_COMMAND,
                WARCRY_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND,
                MIND_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND,
                SURPRISE_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_SNIPER_SHOT_COMMAND,
                SNIPER_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND,
                CONCEAL_SHOT_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND,
                FLURRY_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND,
                FLURRY_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_ONE_COMMAND,
                SCATTER_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_TWO_COMMAND,
                SCATTER_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_WILD_SHOT_ONE_COMMAND,
                WILD_SHOT_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_WILD_SHOT_TWO_COMMAND,
                WILD_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_LEG_SHOT_TWO_COMMAND,
                LEG_SHOT_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_LEG_SHOT_THREE_COMMAND,
                LEG_SHOT_THREE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ACID_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACID_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ACID_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, ACID_SINGLE_ONE_COMMAND);
        }
        if (attackerRoot)
        {
            restoreCommand(attacker, ORIGINAL_ACID_CONE_ONE_COMMAND,
                ACID_CONE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_ACID_CONE_TWO_COMMAND,
                ACID_CONE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_ACID_SINGLE_TWO_COMMAND,
                ACID_SINGLE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLAME_SINGLE_ONE_COMMAND,
                FLAME_SINGLE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLAME_SINGLE_TWO_COMMAND,
                FLAME_SINGLE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLAME_CONE_ONE_COMMAND,
                FLAME_CONE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_FLAME_CONE_TWO_COMMAND,
                FLAME_CONE_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND);
        }
        if (attackerRoot)
        {
            restoreCommand(attacker, ORIGINAL_LIGHTNING_CONE_ONE_COMMAND,
                LIGHTNING_CONE_ONE_COMMAND);
            restoreCommand(attacker, ORIGINAL_LIGHTNING_CONE_TWO_COMMAND,
                LIGHTNING_CONE_TWO_COMMAND);
            restoreCommand(attacker, ORIGINAL_LIGHTNING_SINGLE_TWO_COMMAND,
                LIGHTNING_SINGLE_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_AREA_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_AREA_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_AREA_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_AREA_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_AREA_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_AREA_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_AREA_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HIT_ONE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HIT_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_CERTIFICATION) == 0 &&
            hasCommand(attacker, POLEARM_CERTIFICATION))
        {
            revokeCommand(attacker, POLEARM_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_CERTIFICATION) == 0 &&
            hasCommand(attacker, ONE_HAND_CERTIFICATION))
        {
            revokeCommand(attacker, ONE_HAND_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_TWO_HAND_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_CERTIFICATION) == 0 &&
            hasCommand(attacker, TWO_HAND_CERTIFICATION))
        {
            revokeCommand(attacker, TWO_HAND_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ACID_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_ACID_CERTIFICATION) == 0 &&
            hasCommand(attacker, ACID_CERTIFICATION))
        {
            revokeCommand(attacker, ACID_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_FLAME_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_FLAME_CERTIFICATION) == 0 &&
            hasCommand(attacker, FLAME_CERTIFICATION))
        {
            revokeCommand(attacker, FLAME_CERTIFICATION);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_LIGHTNING_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_LIGHTNING_CERTIFICATION) == 0 &&
            hasCommand(attacker, LIGHTNING_CERTIFICATION))
        {
            revokeCommand(attacker, LIGHTNING_CERTIFICATION);
        }
        if (attackerRoot)
        {
            // Release the fixture carbine before revoking its profession tree.
            destroyFixtureCarbine(attacker);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_THREE))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_TWO))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_ONE))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_NOVICE) == 0 &&
            hasSkill(attacker, PISTOL_NOVICE))
        {
            revokeSkill(attacker, PISTOL_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_THREE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_TWO) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_PISTOL_ONE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_ONE))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) == 0 &&
            hasSkill(attacker, CARBINE_SPEED_ONE))
        {
            revokeSkill(attacker, CARBINE_SPEED_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_ABILITY_FOUR))
        {
            revokeSkill(attacker, CARBINE_ABILITY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_FOUR))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_THREE))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_TWO))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_ONE))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_FOUR))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_THREE))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_TWO))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_ONE))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_NOVICE) == 0 &&
            hasSkill(attacker, CARBINE_NOVICE))
        {
            revokeSkill(attacker, CARBINE_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_THREE))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_TWO))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_CARBINE_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_ONE))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLEMAN_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_MASTER) == 0 &&
            hasSkill(attacker, RIFLEMAN_MASTER))
        {
            revokeSkill(attacker, RIFLEMAN_MASTER);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLEMAN_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_NOVICE) == 0 &&
            hasSkill(attacker, RIFLEMAN_NOVICE))
        {
            revokeSkill(attacker, RIFLEMAN_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_MASTER) == 0 &&
            hasSkill(attacker, BRAWLER_MASTER))
        {
            revokeSkill(attacker, BRAWLER_MASTER);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_MASTER) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_MASTER))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_MASTER);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_NOVICE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_NOVICE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_FOUR))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_FOUR);
        }
        // The master box depends on every polearm branch. Revoke it before
        // restoring branch snapshots so a single recovery pass is sufficient.
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_MASTER) == 0 &&
            hasSkill(attacker, POLEARM_MASTER))
        {
            revokeSkill(attacker, POLEARM_MASTER);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_FOUR))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_THREE))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_TWO))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_ONE))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_FOUR))
        {
            revokeSkill(attacker, POLEARM_SPEED_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_THREE) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_THREE))
        {
            revokeSkill(attacker, POLEARM_SPEED_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_TWO) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_TWO))
        {
            revokeSkill(attacker, POLEARM_SPEED_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_ONE) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_ONE))
        {
            revokeSkill(attacker, POLEARM_SPEED_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_FOUR))
        {
            revokeSkill(attacker, POLEARM_ABILITY_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_THREE) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_THREE))
        {
            revokeSkill(attacker, POLEARM_ABILITY_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_TWO) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_TWO))
        {
            revokeSkill(attacker, POLEARM_ABILITY_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_ONE) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_ONE))
        {
            revokeSkill(attacker, POLEARM_ABILITY_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_FOUR))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_THREE))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_TWO))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_ONE))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_POLEARM_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_NOVICE) == 0 &&
            hasSkill(attacker, POLEARM_NOVICE))
        {
            revokeSkill(attacker, POLEARM_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_FOUR))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_FOUR))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_MASTER) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_MASTER))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_MASTER);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO);
        }
        if (attackerRoot &&
            hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_NOVICE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_FOUR))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_THREE))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_TWO))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_ONE))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_THREE))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_TWO))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_ONE))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_THREE))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_TWO))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_ONE))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_THREE))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_TWO))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_ONE))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_NOVICE) == 0 &&
            hasSkill(attacker, BRAWLER_NOVICE))
        {
            revokeSkill(attacker, BRAWLER_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_BRAWLER_ROOT) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ROOT) == 0 &&
            hasSkill(attacker, BRAWLER_ROOT))
        {
            revokeSkill(attacker, BRAWLER_ROOT);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLE_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_FOUR) == 0 &&
            hasSkill(attacker, RIFLE_FOUR))
        {
            revokeSkill(attacker, RIFLE_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLE_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_THREE) == 0 &&
            hasSkill(attacker, RIFLE_THREE))
        {
            revokeSkill(attacker, RIFLE_THREE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLE_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_TWO) == 0 &&
            hasSkill(attacker, RIFLE_TWO))
        {
            revokeSkill(attacker, RIFLE_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_RIFLE_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_ONE) == 0 && hasSkill(attacker, RIFLE_ONE))
        {
            revokeSkill(attacker, RIFLE_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_FOUR);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_TWO))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_TWO);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_ONE))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_ONE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_NOVICE) == 0 && hasSkill(attacker, MARKSMAN_NOVICE))
        {
            revokeSkill(attacker, MARKSMAN_NOVICE);
        }
        if (attackerRoot && hasObjVar(attacker, ORIGINAL_COMBAT_ACTIONS) &&
            getIntObjVar(attacker, ORIGINAL_COMBAT_ACTIONS) == 0 &&
            hasScript(attacker, COMBAT_ACTIONS_SCRIPT))
        {
            detachScript(attacker, COMBAT_ACTIONS_SCRIPT);
        }

        if (attackerRoot)
        {
            retryCarbineRestoration(attacker);
        }

        boolean attackerSnapshotComplete = attackerRoot && hasCompleteSnapshot(attacker);
        boolean defenderSnapshotComplete = defenderRoot && hasCompleteSnapshot(defender);
        boolean fixtureWeaponDestroyed = !attackerRoot || destroyFixtureWeapon(attacker);
        boolean fixturePistolDestroyed = !attackerRoot || destroyFixturePistol(attacker);
        boolean fixtureCarbineDestroyed =
            !attackerRoot || destroyFixtureCarbine(attacker);
        boolean fixturePolearmDestroyed = !attackerRoot || destroyFixturePolearm(attacker);
        boolean fixtureOneHandDestroyed = !attackerRoot || destroyFixtureOneHand(attacker);
        boolean fixtureTwoHandDestroyed = !attackerRoot || destroyFixtureTwoHand(attacker);
        boolean fixtureAcidDestroyed = !attackerRoot || destroyFixtureAcid(attacker);
        boolean fixtureFlameDestroyed = !attackerRoot || destroyFixtureFlame(attacker);
        boolean fixtureLightningDestroyed = !attackerRoot || destroyFixtureLightning(attacker);
        boolean attackerRestored = !attackerRoot ||
            (attackerSnapshotComplete && restorePlayer(attacker));
        boolean defenderRestored = !defenderRoot ||
            (defenderSnapshotComplete && restorePlayer(defender));
        if (attackerRoot)
        {
            removeObjVar(attacker, ROOT);
        }
        if (defenderRoot)
        {
            removeObjVar(defender, ROOT);
        }
        return "action=recover alreadyClean=false attackerHadRoot=" + attackerRoot +
            " defenderHadRoot=" + defenderRoot +
            " attackerSnapshotComplete=" + attackerSnapshotComplete +
            " defenderSnapshotComplete=" + defenderSnapshotComplete +
            " fixtureWeaponDestroyed=" + fixtureWeaponDestroyed +
            " fixturePistolDestroyed=" + fixturePistolDestroyed +
            " fixtureCarbineDestroyed=" + fixtureCarbineDestroyed +
            " fixturePolearmDestroyed=" + fixturePolearmDestroyed +
            " fixtureOneHandDestroyed=" + fixtureOneHandDestroyed +
            " fixtureTwoHandDestroyed=" + fixtureTwoHandDestroyed +
            " fixtureAcidDestroyed=" + fixtureAcidDestroyed +
            " fixtureFlameDestroyed=" + fixtureFlameDestroyed +
            " fixtureLightningDestroyed=" + fixtureLightningDestroyed +
            " attackerRestored=" + attackerRestored + " defenderRestored=" + defenderRestored +
            " lifecycle=" + lifecycle;
    }

    private String armNoPartial(obj_id attacker, obj_id defender, String lifecycle)
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

        boolean armed =
            setAttribAndVerify(attacker, HEALTH, 3) &
            setAttribAndVerify(attacker, ACTION, 7) &
            setAttribAndVerify(attacker, MIND, 12) &
            setAttribAndVerify(defender, HEALTH, getMaxAttrib(defender, HEALTH)) &
            setAttribAndVerify(defender, ACTION, getMaxAttrib(defender, ACTION)) &
            setAttribAndVerify(defender, MIND, getMaxAttrib(defender, MIND));
        if (!armed)
        {
            return "error=noPartialPreparationFailed";
        }
        return "action=armNoPartial expectedHealth=3 expectedAction=7 expectedMind=12 " +
            buildStatus(attacker, defender, lifecycle);
    }

    private String equipFixtureAcid(obj_id attacker, obj_id defender, String lifecycle)
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
        obj_id weapon = getObjIdObjVar(attacker, FIXTURE_ACID);
        if (!isIdValid(weapon) || !ACID_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureAcidMissing";
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current == weapon)
        {
            return "action=equipFixtureAcid resumed=true equipped=true " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached = hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = equipOverride(weapon, attacker) &&
            getObjectInSlot(attacker, "hold_r") == weapon;
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean scriptRestored = !combatWeaponAttached ||
            hasScript(weapon, "systems.combat.combat_weapon");
        return equipped && scriptRestored ?
            "action=equipFixtureAcid resumed=false equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=fixtureAcidEquipFailed equipped=" + equipped +
                " combatWeaponScriptRestored=" + scriptRestored + " " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String equipFixtureFlame(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
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
        obj_id weapon = getObjIdObjVar(attacker, FIXTURE_FLAME);
        if (!isIdValid(weapon) ||
            !FLAME_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureFlameMissing";
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current == weapon)
        {
            return "action=equipFixtureFlame resumed=true equipped=true " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = equipOverride(weapon, attacker) &&
            getObjectInSlot(attacker, "hold_r") == weapon;
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean scriptRestored = !combatWeaponAttached ||
            hasScript(weapon, "systems.combat.combat_weapon");
        return equipped && scriptRestored ?
            "action=equipFixtureFlame resumed=false equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=fixtureFlameEquipFailed equipped=" + equipped +
                " combatWeaponScriptRestored=" + scriptRestored + " " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armGenerated(obj_id attacker, obj_id defender, String lifecycle)
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

        location attackerDestination =
            new location(3500.0f, 5.0f, -4800.0f, "tatooine", null);
        location defenderDestination =
            new location(3503.0f, 5.0f, -4800.0f, "tatooine", null);
        boolean moved = setLocation(attacker, attackerDestination) &
            setLocation(defender, defenderDestination);
        boolean woundsReady =
            restoreFixtureWounds(attacker) & restoreFixtureWounds(defender);
        boolean ready = reassertPreparedState(attacker, defender);
        removeAttribOrSkillModModifier(attacker, combat_base.PRECU_AIM_MODIFIER);
        setState(attacker, STATE_AIMING, false);
        resetLiveDiagnostic(attacker);
        String fireDotId = dot.DOT_FIRE + attacker;
        if (dot.getDotStrength(defender, fireDotId) >= 0)
        {
            dot.removeDotEffect(defender, fireDotId, false);
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_UP_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        utils.removeScriptVar(
            defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        boolean hamReady =
            prepareFixtureHam(attacker) & prepareFixtureHam(defender);
        return moved && woundsReady && ready && hamReady ?
            "action=armGenerated expectedDistanceMeters=3 " +
                buildStatus(attacker, defender, lifecycle) :
            "error=generatedCombatPreparationFailed moved=" + moved +
                " woundsReady=" + woundsReady +
                " ready=" + ready + " hamReady=" + hamReady + " " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armPostureUp(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_UP_RECOVERY);
        boolean prone = setPostureClientImmediate(defender, POSTURE_PRONE) &&
            getPosture(defender) == POSTURE_PRONE;
        return prone ?
            "action=armPostureUp defenderPosture=prone " +
                buildStatus(attacker, defender, lifecycle) :
            "error=postureUpPreparationFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armHealthShotTwo(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        boolean boundary = hasSkill(attacker, MARKSMAN_NOVICE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_ONE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO) &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR) &&
            hasSkill(attacker, PISTOL_NOVICE) &&
            hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND);
        return boundary ?
            "action=armHealthShotTwo pistolAccuracy=" +
                getSkillStatisticModifier(attacker, "pistol_accuracy") +
                " pistolSpeed=" +
                getSkillStatisticModifier(attacker, "pistol_speed") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=healthShotTwoOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armPistolMeleeDefense(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        boolean boundary = hasSkill(attacker, MARKSMAN_NOVICE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_ONE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO) &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE) &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR) &&
            hasSkill(attacker, PISTOL_NOVICE) &&
            hasSkill(attacker, PISTOL_SUPPORT_ONE) &&
            hasSkill(attacker, PISTOL_SUPPORT_TWO) &&
            hasSkill(attacker, PISTOL_SUPPORT_THREE) &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        return boundary ?
            "action=armPistolMeleeDefense meleeDefense=" +
                getSkillStatisticModifier(attacker, "melee_defense") +
                " pistolSpeed=" +
                getSkillStatisticModifier(attacker, "pistol_speed") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=pistolMeleeDefenseOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armMarksmanTumble(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_MELEE_MODIFIER);
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_RANGED_MODIFIER);
        setState(attacker, STATE_TUMBLING, false);
        setAttrib(attacker, ACTION, getMaxAttrib(attacker, ACTION));
        setPostureClientImmediate(attacker, POSTURE_UPRIGHT);
        boolean boundary = hasSkill(attacker, MARKSMAN_NOVICE) &&
            hasSkill(attacker, MARKSMAN_SUPPORT_ONE) &&
            hasSkill(attacker, MARKSMAN_SUPPORT_TWO) &&
            hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) &&
            hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) &&
            hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND);
        return boundary ?
            "action=armMarksmanTumble meleeDefense=" +
                getSkillStatisticModifier(attacker, "melee_defense") +
                " rangedDefense=" +
                getSkillStatisticModifier(attacker, "ranged_defense") +
                " action=" + getAttrib(attacker, ACTION) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=marksmanTumbleOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandBlind(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_ONE_HAND) ?
            getObjIdObjVar(attacker, FIXTURE_ONE_HAND) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !ONE_HAND_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureOneHandMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean boundary = equipped &&
            hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) &&
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) &&
            hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) &&
            hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND);
        return boundary ?
            "action=armOneHandBlind equipped=true blindState=" +
                getState(defender, STATE_BLINDED) + " blindBuff=" +
                buff.hasBuff(defender, "blind") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandBlindOwnershipBoundaryFailed equipped=" + equipped +
                " " + buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandSupport(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armOneHandBlind(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armOneHandBlind"))
        {
            return armed;
        }
        boolean boundary =
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) &&
            hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) &&
            hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND);
        return boundary ?
            "action=armOneHandSupport equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandSupportOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandAccuracy(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armOneHandBlind(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armOneHandBlind"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO) &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) &&
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND) &&
            hasCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND) &&
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy");
        return boundary ?
            "action=armOneHandAccuracy equipped=true dizzyState=" +
                getState(defender, STATE_DIZZY) + " dizzyBuff=" +
                buff.hasBuff(defender, "dizzy") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandAccuracyOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armOneHandAccuracy(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armOneHandAccuracy"))
        {
            return armed;
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO) &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE) &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) &&
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND) &&
            hasCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) &&
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_BLINDED) == 0 &&
            !buff.hasBuff(defender, "blind") &&
            dot.getDotStrength(defender, bleedingDotId) < 0;
        return boundary ?
            "action=armOneHandSpeed equipped=true blindState=" +
                getState(defender, STATE_BLINDED) + " blindBuff=" +
                buff.hasBuff(defender, "blind") + " bleedingDotStrength=" +
                dot.getDotStrength(defender, bleedingDotId) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandSpeedOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandAbility(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armOneHandSpeed(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armOneHandSpeed"))
        {
            return armed;
        }
        boolean boundary =
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO) &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE) &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) &&
            hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) &&
            hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND);
        return boundary ?
            "action=armOneHandAbility equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandAbilityOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armOneHandMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armOneHandAbility(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armOneHandAbility"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) &&
            hasSkill(attacker, ONE_HAND_SWORD_MASTER) &&
            hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_BLINDED) == 0 &&
            !buff.hasBuff(defender, "blind");
        return boundary ?
            "action=armOneHandMaster equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=oneHandMasterOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armTwoHandAccuracy(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_TWO_HAND) ?
            getObjIdObjVar(attacker, FIXTURE_TWO_HAND) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !TWO_HAND_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureTwoHandMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary = equipped &&
            hasSkill(attacker, BRAWLER_TWO_HAND_ONE) &&
            hasSkill(attacker, BRAWLER_TWO_HAND_TWO) &&
            hasSkill(attacker, BRAWLER_TWO_HAND_THREE) &&
            hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) &&
            hasSkill(attacker, TWO_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO) &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) &&
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_ONE_COMMAND, attacker) == 0 &&
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_TWO_COMMAND, attacker) == 0 &&
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_THREE_COMMAND, attacker) == 0 &&
            proofStateBuffsCleared &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy");
        return boundary ?
            "action=armTwoHandAccuracy equipped=true dizzyState=" +
                getState(defender, STATE_DIZZY) + " dizzyBuff=" +
                buff.hasBuff(defender, "dizzy") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=twoHandAccuracyOwnershipBoundaryFailed equipped=" +
                equipped + " " + buildStatus(attacker, defender, lifecycle);
    }

    private String armTwoHandSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armTwoHandAccuracy(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armTwoHandAccuracy"))
        {
            return armed;
        }
        boolean boundary =
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE) &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO) &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE) &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) &&
            hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) &&
            hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) &&
            combat.canPerformAction(TWO_HAND_HEAD_TWO_COMMAND, attacker) == 0 &&
            combat.canPerformAction(TWO_HAND_HEAD_THREE_COMMAND, attacker) == 0;
        return boundary ?
            "action=armTwoHandSpeed equipped=true " +
                buildStatus(attacker, defender, lifecycle) :
            "error=twoHandSpeedOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armTwoHandAbility(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armTwoHandSpeed(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armTwoHandSpeed"))
        {
            return armed;
        }
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_UP_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        utils.removeScriptVar(
            defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        boolean upright = setPostureClientImmediate(defender, POSTURE_UPRIGHT) &&
            getPosture(defender) == POSTURE_UPRIGHT;
        boolean boundary =
            hasSkill(attacker, TWO_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO) &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE) &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) &&
            hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) &&
            hasCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND) &&
            combat.canPerformAction(TWO_HAND_AREA_TWO_COMMAND, attacker) == 0 &&
            combat.canPerformAction(TWO_HAND_SWEEP_TWO_COMMAND, attacker) == 0 &&
            upright;
        return boundary ?
            "action=armTwoHandAbility equipped=true defenderPosture=" +
                getPosture(defender) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=twoHandAbilityOwnershipBoundaryFailed upright=" + upright +
                " " + buildStatus(attacker, defender, lifecycle);
    }

    private String armTwoHandSupport(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armTwoHandAbility(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armTwoHandAbility"))
        {
            return armed;
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, TWO_HAND_SWORD_NOVICE) &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE) &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO) &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE) &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) &&
            hasCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND) &&
            hasCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND) &&
            combat.canPerformAction(TWO_HAND_MIND_HIT_ONE_COMMAND, attacker) == 0 &&
            combat.canPerformAction(TWO_HAND_MIND_HIT_TWO_COMMAND, attacker) == 0 &&
            proofStateBuffsCleared &&
            dot.getDotStrength(defender, bleedingDotId) < 0;
        return boundary ?
            "action=armTwoHandSupport equipped=true bleedingDotStrength=" +
                dot.getDotStrength(defender, bleedingDotId) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=twoHandSupportOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armTwoHandMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armTwoHandSupport(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armTwoHandSupport"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) &&
            hasSkill(attacker, TWO_HAND_SWORD_MASTER) &&
            hasCommand(attacker, TWO_HAND_HIT_THREE_COMMAND) &&
            combat.canPerformAction(TWO_HAND_HIT_THREE_COMMAND, attacker) == 0 &&
            proofStateBuffsCleared &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy") &&
            getPosture(defender) == POSTURE_UPRIGHT;
        return boundary ?
            "action=armTwoHandMaster equipped=true defenderPosture=" +
                getPosture(defender) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=twoHandMasterOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String prepareUnarmedSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }

        boolean anySnapshot =
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_ROOT) ||
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_NOVICE) ||
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_ONE) ||
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_TWO) ||
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_THREE) ||
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_FOUR) ||
            hasObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND) ||
            hasObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND);
        boolean completeSnapshot =
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_ROOT) &&
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_NOVICE) &&
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_ONE) &&
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_TWO) &&
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_THREE) &&
            hasObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_FOUR) &&
            hasObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND) &&
            hasObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND);
        if (anySnapshot && !completeSnapshot)
        {
            return "error=unarmedSpeedSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!completeSnapshot)
        {
            setObjVar(attacker, ORIGINAL_TERAS_KASI_ROOT,
                hasSkill(attacker, TERAS_KASI_ROOT) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_TERAS_KASI_NOVICE,
                hasSkill(attacker, TERAS_KASI_NOVICE) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_ONE,
                hasSkill(attacker, TERAS_KASI_SPEED_ONE) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_TWO,
                hasSkill(attacker, TERAS_KASI_SPEED_TWO) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_THREE,
                hasSkill(attacker, TERAS_KASI_SPEED_THREE) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_TERAS_KASI_SPEED_FOUR,
                hasSkill(attacker, TERAS_KASI_SPEED_FOUR) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND,
                hasCommand(attacker, UNARMED_KNOCKDOWN_ONE_COMMAND) ? 1 : 0);
            setObjVar(attacker, ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND,
                hasCommand(attacker, UNARMED_KNOCKDOWN_TWO_COMMAND) ? 1 : 0);
        }

        boolean rootReady = hasSkill(attacker, TERAS_KASI_ROOT) ||
            grantSkill(attacker, TERAS_KASI_ROOT);
        boolean noviceReady = rootReady &&
            (hasSkill(attacker, TERAS_KASI_NOVICE) ||
                grantSkill(attacker, TERAS_KASI_NOVICE));
        boolean speedOneReady = noviceReady &&
            (hasSkill(attacker, TERAS_KASI_SPEED_ONE) ||
                grantSkill(attacker, TERAS_KASI_SPEED_ONE));
        boolean speedTwoReady = speedOneReady &&
            (hasSkill(attacker, TERAS_KASI_SPEED_TWO) ||
                grantSkill(attacker, TERAS_KASI_SPEED_TWO));
        boolean speedThreeReady = speedTwoReady &&
            (hasSkill(attacker, TERAS_KASI_SPEED_THREE) ||
                grantSkill(attacker, TERAS_KASI_SPEED_THREE));
        boolean speedFourReady = speedThreeReady &&
            (hasSkill(attacker, TERAS_KASI_SPEED_FOUR) ||
                grantSkill(attacker, TERAS_KASI_SPEED_FOUR));
        boolean knockdownOneReady =
            hasCommand(attacker, UNARMED_KNOCKDOWN_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_KNOCKDOWN_ONE_COMMAND);
        boolean knockdownTwoReady =
            hasCommand(attacker, UNARMED_KNOCKDOWN_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_KNOCKDOWN_TWO_COMMAND);
        if (!speedFourReady || !knockdownOneReady || !knockdownTwoReady)
        {
            return "error=unarmedSpeedGrantFailed rootReady=" + rootReady +
                " noviceReady=" + noviceReady +
                " speedFourReady=" + speedFourReady +
                " knockdownOneReady=" + knockdownOneReady +
                " knockdownTwoReady=" + knockdownTwoReady + " " +
                buildStatus(attacker, defender, lifecycle);
        }
        return "action=prepareUnarmedSpeed resumed=" + completeSnapshot + " " +
            buildUnarmedSpeedStatus(attacker, defender, lifecycle);
    }

    private String statusUnarmedSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        return ownership == null ?
            "action=statusUnarmedSpeed " +
                buildUnarmedSpeedStatus(attacker, defender, lifecycle) :
            ownership;
    }

    private String armUnarmedSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (isIdValid(current))
        {
            obj_id inventory = utils.getInventoryContainer(attacker);
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unarmedSpeedUnequipFailed " +
                    buildUnarmedSpeedStatus(attacker, defender, lifecycle);
            }
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        utils.removeScriptVar(defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        utils.removeScriptVar(
            defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        boolean boundary =
            hasSkill(attacker, BRAWLER_UNARMED_FOUR) &&
            hasSkill(attacker, TERAS_KASI_ROOT) &&
            hasSkill(attacker, TERAS_KASI_NOVICE) &&
            hasSkill(attacker, TERAS_KASI_SPEED_ONE) &&
            hasSkill(attacker, TERAS_KASI_SPEED_TWO) &&
            hasSkill(attacker, TERAS_KASI_SPEED_THREE) &&
            hasSkill(attacker, TERAS_KASI_SPEED_FOUR) &&
            hasCommand(attacker, UNARMED_KNOCKDOWN_ONE_COMMAND) &&
            hasCommand(attacker, UNARMED_KNOCKDOWN_TWO_COMMAND) &&
            combat.canPerformAction(
                UNARMED_KNOCKDOWN_ONE_COMMAND, attacker) == 0 &&
            combat.canPerformAction(
                UNARMED_KNOCKDOWN_TWO_COMMAND, attacker) == 0 &&
            !isIdValid(getObjectInSlot(attacker, "hold_r")) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy") &&
            getPosture(defender) == POSTURE_UPRIGHT;
        return boundary ?
            "action=armUnarmedSpeed unarmed=true defenderPosture=" +
                getPosture(defender) + " " +
                buildUnarmedSpeedStatus(attacker, defender, lifecycle) :
            "error=unarmedSpeedOwnershipBoundaryFailed " +
                buildUnarmedSpeedStatus(attacker, defender, lifecycle);
    }

    private String cleanupUnarmedSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupUnarmedSpeed alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }

        restoreCommand(attacker,
            ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND,
            UNARMED_KNOCKDOWN_ONE_COMMAND);
        restoreCommand(attacker,
            ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND,
            UNARMED_KNOCKDOWN_TWO_COMMAND);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_SPEED_FOUR,
            TERAS_KASI_SPEED_FOUR);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_SPEED_THREE,
            TERAS_KASI_SPEED_THREE);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_SPEED_TWO,
            TERAS_KASI_SPEED_TWO);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_SPEED_ONE,
            TERAS_KASI_SPEED_ONE);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_NOVICE,
            TERAS_KASI_NOVICE);
        restoreSkill(attacker, ORIGINAL_TERAS_KASI_ROOT,
            TERAS_KASI_ROOT);
        boolean restored =
            isCommandRestored(attacker,
                ORIGINAL_UNARMED_KNOCKDOWN_ONE_COMMAND,
                UNARMED_KNOCKDOWN_ONE_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_UNARMED_KNOCKDOWN_TWO_COMMAND,
                UNARMED_KNOCKDOWN_TWO_COMMAND) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_SPEED_FOUR,
                TERAS_KASI_SPEED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_SPEED_THREE,
                TERAS_KASI_SPEED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_SPEED_TWO,
                TERAS_KASI_SPEED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_SPEED_ONE,
                TERAS_KASI_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_NOVICE,
                TERAS_KASI_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_TERAS_KASI_ROOT,
                TERAS_KASI_ROOT);
        if (!restored)
        {
            return "error=unarmedSpeedRestorationFailed " +
                buildUnarmedSpeedStatus(attacker, defender, lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupUnarmedSpeed alreadyClean=false restored=true " +
                baseCleanup :
            baseCleanup;
    }

    private String buildUnarmedSpeedStatus(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        obj_id weapon = getObjectInSlot(attacker, "hold_r");
        return buildStatus(attacker, defender, lifecycle) +
            " terasKasiRoot=" + hasSkill(attacker, TERAS_KASI_ROOT) +
            " terasKasiNovice=" + hasSkill(attacker, TERAS_KASI_NOVICE) +
            " terasKasiSpeedOne=" +
                hasSkill(attacker, TERAS_KASI_SPEED_ONE) +
            " terasKasiSpeedTwo=" +
                hasSkill(attacker, TERAS_KASI_SPEED_TWO) +
            " terasKasiSpeedThree=" +
                hasSkill(attacker, TERAS_KASI_SPEED_THREE) +
            " terasKasiSpeedFour=" +
                hasSkill(attacker, TERAS_KASI_SPEED_FOUR) +
            " unarmedKnockdownOneCommand=" +
                hasCommand(attacker, UNARMED_KNOCKDOWN_ONE_COMMAND) +
            " unarmedKnockdownTwoCommand=" +
                hasCommand(attacker, UNARMED_KNOCKDOWN_TWO_COMMAND) +
            " unarmedKnockdownOneCanPerform=" +
                combat.canPerformAction(
                    UNARMED_KNOCKDOWN_ONE_COMMAND, attacker) +
            " unarmedKnockdownTwoCanPerform=" +
                combat.canPerformAction(
                    UNARMED_KNOCKDOWN_TWO_COMMAND, attacker) +
            " unarmedSpeedModifier=" +
                getSkillStatisticModifier(attacker, "unarmed_speed") +
            " heldWeapon=" + (isIdValid(weapon) ? weapon : "none") +
            " defenderDizzyState=" + getState(defender, STATE_DIZZY) +
            " defenderDizzyBuff=" + buff.hasBuff(defender, "dizzy") +
            " defenderPosture=" + getPosture(defender) +
            " knockdownRecovery=" +
                utils.hasScriptVar(
                    defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
    }

    private String prepareUnarmedMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }
        boolean skillSnapshot = hasObjVar(attacker,
            ORIGINAL_TERAS_KASI_MASTER_SKILL_BITS);
        boolean commandSnapshot = hasObjVar(attacker,
            ORIGINAL_UNARMED_HIT_THREE_COMMAND);
        if (skillSnapshot != commandSnapshot)
        {
            return "error=unarmedMasterSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!skillSnapshot)
        {
            String bits = "";
            for (String skillName : TERAS_KASI_MASTER_SKILLS)
            {
                bits += hasSkill(attacker, skillName) ? "1" : "0";
            }
            setObjVar(attacker, ORIGINAL_TERAS_KASI_MASTER_SKILL_BITS, bits);
            setObjVar(attacker, ORIGINAL_UNARMED_HIT_THREE_COMMAND,
                hasCommand(attacker, UNARMED_HIT_THREE_COMMAND) ? 1 : 0);
        }
        boolean skillsReady = true;
        for (String skillName : TERAS_KASI_MASTER_SKILLS)
        {
            if (!hasSkill(attacker, skillName) &&
                !grantSkill(attacker, skillName))
            {
                skillsReady = false;
                break;
            }
        }
        boolean commandReady =
            hasCommand(attacker, UNARMED_HIT_THREE_COMMAND) ||
            grantCommand(attacker, UNARMED_HIT_THREE_COMMAND);
        if (!skillsReady || !commandReady ||
            !"1111111111111111111".equals(
                buildUnarmedMasterSkillBits(attacker)))
        {
            return "error=unarmedMasterGrantFailed skillsReady=" +
                skillsReady + " commandReady=" + commandReady + " " +
                buildUnarmedMasterStatus(attacker, defender, lifecycle);
        }
        return "action=prepareUnarmedMaster resumed=" + skillSnapshot + " " +
            buildUnarmedMasterStatus(attacker, defender, lifecycle);
    }

    private String statusUnarmedMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        return ownership == null ?
            "action=statusUnarmedMaster " +
                buildUnarmedMasterStatus(attacker, defender, lifecycle) :
            ownership;
    }

    private String armUnarmedMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (isIdValid(current))
        {
            obj_id inventory = utils.getInventoryContainer(attacker);
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unarmedMasterUnequipFailed " +
                    buildUnarmedMasterStatus(attacker, defender, lifecycle);
            }
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            "1111111111111111111".equals(
                buildUnarmedMasterSkillBits(attacker)) &&
            hasCommand(attacker, UNARMED_HIT_THREE_COMMAND) &&
            combat.canPerformAction(UNARMED_HIT_THREE_COMMAND, attacker) == 0 &&
            !isIdValid(getObjectInSlot(attacker, "hold_r")) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_BLINDED) == 0 &&
            !buff.hasBuff(defender, "blind") &&
            getPosture(defender) == POSTURE_UPRIGHT;
        return boundary ?
            "action=armUnarmedMaster unarmed=true defenderPosture=" +
                getPosture(defender) + " " +
                buildUnarmedMasterStatus(attacker, defender, lifecycle) :
            "error=unarmedMasterOwnershipBoundaryFailed " +
                buildUnarmedMasterStatus(attacker, defender, lifecycle);
    }

    private String cleanupUnarmedMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupUnarmedMaster alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker, ORIGINAL_TERAS_KASI_MASTER_SKILL_BITS) ||
            !hasObjVar(attacker, ORIGINAL_UNARMED_HIT_THREE_COMMAND))
        {
            return "error=unarmedMasterSnapshotMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        String bits = getStringObjVar(attacker,
            ORIGINAL_TERAS_KASI_MASTER_SKILL_BITS);
        if (bits == null || bits.length() != TERAS_KASI_MASTER_SKILLS.length)
        {
            return "error=unarmedMasterSnapshotInvalid " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (getIntObjVar(attacker,
            ORIGINAL_UNARMED_HIT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_HIT_THREE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_HIT_THREE_COMMAND);
        }
        for (int index = TERAS_KASI_MASTER_SKILLS.length - 1;
            index >= 0; --index)
        {
            if (bits.charAt(index) == '0' &&
                hasSkill(attacker, TERAS_KASI_MASTER_SKILLS[index]))
            {
                revokeSkillSilent(attacker, TERAS_KASI_MASTER_SKILLS[index]);
            }
        }
        boolean restored = true;
        for (int index = 0; index < TERAS_KASI_MASTER_SKILLS.length; ++index)
        {
            boolean expected = bits.charAt(index) == '1';
            if (expected && !hasSkill(attacker,
                TERAS_KASI_MASTER_SKILLS[index]))
            {
                restored = grantSkill(attacker,
                    TERAS_KASI_MASTER_SKILLS[index]);
            }
            if (hasSkill(attacker, TERAS_KASI_MASTER_SKILLS[index]) != expected)
            {
                restored = false;
            }
        }
        boolean commandExpected = getIntObjVar(attacker,
            ORIGINAL_UNARMED_HIT_THREE_COMMAND) != 0;
        if (commandExpected && !hasCommand(attacker,
            UNARMED_HIT_THREE_COMMAND))
        {
            restored = grantCommand(attacker, UNARMED_HIT_THREE_COMMAND) &&
                restored;
        }
        restored = restored &&
            hasCommand(attacker, UNARMED_HIT_THREE_COMMAND) == commandExpected;
        if (!restored)
        {
            return "error=unarmedMasterRestorationFailed " +
                buildUnarmedMasterStatus(attacker, defender, lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupUnarmedMaster alreadyClean=false restored=true " +
                baseCleanup :
            baseCleanup;
    }

    private String buildUnarmedMasterSkillBits(obj_id attacker)
        throws InterruptedException
    {
        String bits = "";
        for (String skillName : TERAS_KASI_MASTER_SKILLS)
        {
            bits += hasSkill(attacker, skillName) ? "1" : "0";
        }
        return bits;
    }

    private String buildUnarmedMasterStatus(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        combat_data data = combat_engine.getCombatData(
            UNARMED_HIT_THREE_COMMAND);
        weapon_data weaponData = getWeaponData(getCurrentWeapon(attacker));
        int[] costs = data != null && weaponData != null ?
            combat.getActionCost(attacker, weaponData, data) :
            new int[] {-1, -1, -1};
        obj_id held = getObjectInSlot(attacker, "hold_r");
        return buildStatus(attacker, defender, lifecycle) +
            " unarmedMasterSkillBits=" +
                buildUnarmedMasterSkillBits(attacker) +
            " unarmedHitThreeCommand=" +
                hasCommand(attacker, UNARMED_HIT_THREE_COMMAND) +
            " unarmedHitThreeCanPerform=" +
                combat.canPerformAction(UNARMED_HIT_THREE_COMMAND, attacker) +
            " unarmedHitThreeHealthCost=" + costs[0] +
            " unarmedHitThreeActionCost=" + costs[1] +
            " unarmedHitThreeMindCost=" + costs[2] +
            " unarmedHitThreePrecuHamCostModel=" +
                (data == null ? -1 : data.precuHamCostModel) +
            " unarmedHitThreeDamageMultiplier=" +
                (data == null ? -1.0f : data.percentAddFromWeapon) +
            " unarmedHitThreeTargetPool=" +
                (data == null ? -1 : data.precuTargetPool) +
            " unarmedMasterMeditateModifier=" +
                getSkillStatisticModifier(attacker, "meditate") +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " defenderBlindState=" + getState(defender, STATE_BLINDED) +
            " defenderBlindBuff=" + buff.hasBuff(defender, "blind") +
            " defenderPosture=" + getPosture(defender);
    }

    private String prepareBountyHunterDroidControl(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }
        boolean skillSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS);
        boolean prerequisiteSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS);
        boolean commandSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS);
        if (skillSnapshot != prerequisiteSnapshot ||
            skillSnapshot != commandSnapshot)
        {
            return "error=bountyHunterDroidControlSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!skillSnapshot)
        {
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true));
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true));
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false));
        }
        boolean prerequisitesReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true);
        boolean skillsReady = prerequisitesReady && grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true);
        boolean commandsReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false);
        if (!prerequisitesReady || !skillsReady || !commandsReady ||
            !"1111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true)) ||
            !"111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true)) ||
            !"111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false)))
        {
            return "error=bountyHunterDroidControlGrantFailed prerequisitesReady=" +
                prerequisitesReady + " skillsReady=" + skillsReady +
                " commandsReady=" + commandsReady + " " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle);
        }
        return "action=prepareBountyHunterDroidControl resumed=" +
            skillSnapshot + " " + buildBountyHunterDroidControlStatus(
                attacker, defender, lifecycle);
    }

    private String statusBountyHunterDroidControl(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        return ownership == null ?
            "action=statusBountyHunterDroidControl " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle) :
            ownership;
    }

    private String armBountyHunterDroidControl(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_CARBINE) ?
            getObjIdObjVar(attacker, FIXTURE_CARBINE) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !CDEF_CARBINE_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureCarbineMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        utils.removeScriptVar(defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        utils.removeScriptVar(
            defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        boolean boundary = equipped && proofStateBuffsCleared &&
            "111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true)) &&
            "111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false)) &&
            combat.canPerformAction("underHandShot", attacker) == 0 &&
            combat.canPerformAction("knockdownFire", attacker) == 0 &&
            combat.canPerformAction("confusionShot", attacker) == 0 &&
            getState(defender, STATE_DIZZY) == 0 &&
            getState(defender, STATE_STUNNED) == 0 &&
            getPosture(defender) == POSTURE_UPRIGHT;
        return boundary ?
            "action=armBountyHunterDroidControl equipped=true " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle) :
            "error=bountyHunterDroidControlOwnershipBoundaryFailed " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle);
    }

    private String cleanupBountyHunterDroidControl(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupBountyHunterDroidControl alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS))
        {
            return "error=bountyHunterDroidControlSnapshotMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        String skillBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS);
        String prerequisiteBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS);
        String commandBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS);
        boolean restored = restoreOwnershipBits(attacker,
            BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, commandBits, false) &&
            restoreOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_SKILLS, skillBits, true) &&
            restoreOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES,
                prerequisiteBits, true);
        if (!restored)
        {
            return "error=bountyHunterDroidControlRestorationFailed " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupBountyHunterDroidControl alreadyClean=false restored=true " +
                baseCleanup :
            baseCleanup;
    }

    private String recoverBountyHunterDroidControl(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if (ownership != null)
        {
            return ownership;
        }
        if (hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS))
        {
            String recorded = getStringObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS);
            String current = buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true);
            if (!"0000000".equals(recorded) || !"0011111".equals(current))
            {
                return "error=bountyHunterDroidControlRecoveryNotRequired " +
                    buildBountyHunterDroidControlStatus(
                        attacker, defender, lifecycle);
            }
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS,
                "0011111");
            String resumedCleanup = cleanupBountyHunterDroidControl(
                attacker, defender, lifecycle);
            return resumedCleanup.startsWith(
                    "action=cleanupBountyHunterDroidControl") ?
                "action=recoverBountyHunterDroidControl restored=true " +
                    resumedCleanup : resumedCleanup;
        }
        if (!hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_SKILL_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_COMMAND_BITS) ||
            !"000000".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true)) ||
            !"000".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false)) ||
            !("1011111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true)) ||
              "0011111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true))) ||
            getIntObjVar(attacker, ORIGINAL_RIFLE_FOUR) != 0 ||
            getIntObjVar(attacker, ORIGINAL_PISTOL_FOUR) != 0 ||
            getIntObjVar(attacker, ORIGINAL_CARBINE_FOUR) != 0)
        {
            return "error=bountyHunterDroidControlRecoveryBoundaryFailed " +
                buildBountyHunterDroidControlStatus(
                    attacker, defender, lifecycle);
        }
        setObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITE_BITS,
            "0011111");
        String cleanup = cleanupBountyHunterDroidControl(
            attacker, defender, lifecycle);
        return cleanup.startsWith("action=cleanupBountyHunterDroidControl") ?
            "action=recoverBountyHunterDroidControl restored=true " + cleanup :
            cleanup;
    }

    private String prepareBountyHunterDroidResponse(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }
        boolean skillSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_SKILL_BITS);
        boolean prerequisiteSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITE_BITS);
        boolean commandSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_COMMAND_BITS);
        if (skillSnapshot != prerequisiteSnapshot ||
            skillSnapshot != commandSnapshot)
        {
            return "error=bountyHunterDroidResponseSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!skillSnapshot)
        {
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_SKILL_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, true));
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITE_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true));
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_COMMAND_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, false));
        }
        boolean prerequisitesReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true);
        boolean skillsReady = prerequisitesReady && grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, true);
        boolean commandsReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, false);
        if (!prerequisitesReady || !skillsReady || !commandsReady ||
            !"1111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true)) ||
            !"111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, true)) ||
            !"111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, false)))
        {
            return "error=bountyHunterDroidResponseGrantFailed " +
                buildBountyHunterDroidResponseStatus(attacker, defender,
                    lifecycle);
        }
        return "action=prepareBountyHunterDroidResponse resumed=" +
            skillSnapshot + " " + buildBountyHunterDroidResponseStatus(
                attacker, defender, lifecycle);
    }

    private String statusBountyHunterDroidResponse(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, false);
        return ownership == null ?
            "action=statusBountyHunterDroidResponse " +
                buildBountyHunterDroidResponseStatus(attacker, defender,
                    lifecycle) : ownership;
    }

    private String armBountyHunterDroidResponse(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_PISTOL) ?
            getObjIdObjVar(attacker, FIXTURE_PISTOL) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !CDEF_PISTOL_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixturePistolMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean scriptAttached = hasScript(weapon,
            "systems.combat.combat_weapon");
        if (scriptAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (scriptAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean statesCleared = clearProofStateBuffs(defender);
        boolean boundary = equipped && statesCleared &&
            "111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, true)) &&
            "111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, false)) &&
            combat.canPerformAction("bleedingShot", attacker) == 0 &&
            combat.canPerformAction("eyeShot", attacker) == 0 &&
            combat.canPerformAction("torsoShot", attacker) == 0 &&
            getState(defender, STATE_BLINDED) == 0;
        return boundary ?
            "action=armBountyHunterDroidResponse equipped=true " +
                buildBountyHunterDroidResponseStatus(attacker, defender,
                    lifecycle) :
            "error=bountyHunterDroidResponseOwnershipBoundaryFailed " +
                buildBountyHunterDroidResponseStatus(attacker, defender,
                    lifecycle);
    }

    private String cleanupBountyHunterDroidResponse(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupBountyHunterDroidResponse alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_SKILL_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITE_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_COMMAND_BITS))
        {
            return "error=bountyHunterDroidResponseSnapshotMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        String skillBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_SKILL_BITS);
        String prerequisiteBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_PREREQUISITE_BITS);
        String commandBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_DROID_RESPONSE_COMMAND_BITS);
        boolean restored = restoreOwnershipBits(attacker,
            BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, commandBits, false) &&
            restoreOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, skillBits, true) &&
            restoreOwnershipBits(attacker,
                BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES,
                prerequisiteBits, true);
        if (!restored)
        {
            return "error=bountyHunterDroidResponseRestorationFailed " +
                buildBountyHunterDroidResponseStatus(attacker, defender,
                    lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupBountyHunterDroidResponse alreadyClean=false restored=true " +
                baseCleanup : baseCleanup;
    }

    private String buildBountyHunterDroidResponseStatus(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        int[] bleedingCosts = getFixtureActionCosts(attacker, "bleedingShot");
        int[] eyeCosts = getFixtureActionCosts(attacker, "eyeShot");
        int[] torsoCosts = getFixtureActionCosts(attacker, "torsoShot");
        obj_id held = getObjectInSlot(attacker, "hold_r");
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        String fireDotId = dot.DOT_FIRE + attacker;
        return buildStatus(attacker, defender, lifecycle) +
            " bountyHunterDroidResponseSkillBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_RESPONSE_SKILLS, true) +
            " bountyHunterDroidResponsePrerequisiteBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true) +
            " bountyHunterDroidResponseCommandBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_RESPONSE_COMMANDS, false) +
            " bleedingShotCanPerform=" +
                combat.canPerformAction("bleedingShot", attacker) +
            " eyeShotCanPerform=" +
                combat.canPerformAction("eyeShot", attacker) +
            " torsoShotCanPerform=" +
                combat.canPerformAction("torsoShot", attacker) +
            " bleedingShotCosts=" + bleedingCosts[0] + "," +
                bleedingCosts[1] + "," + bleedingCosts[2] +
            " eyeShotCosts=" + eyeCosts[0] + "," + eyeCosts[1] + "," +
                eyeCosts[2] +
            " torsoShotCosts=" + torsoCosts[0] + "," + torsoCosts[1] + "," +
                torsoCosts[2] +
            " pistolAccuracy=" +
                getSkillStatisticModifier(attacker, "pistol_accuracy") +
            " pistolSpeed=" +
                getSkillStatisticModifier(attacker, "pistol_speed") +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " defenderBlindState=" + getState(defender, STATE_BLINDED) +
            " bleedingShotDotStrength=" +
                dot.getDotStrength(defender, bleedingDotId) +
            " bleedingShotDotDuration=" +
                dot.getDotDuration(defender, bleedingDotId) +
            " bleedingShotDotAttribute=" +
                dot.getDotAttribute(defender, bleedingDotId) +
            " torsoShotDotStrength=" +
                dot.getDotStrength(defender, fireDotId) +
            " torsoShotDotDuration=" +
                dot.getDotDuration(defender, fireDotId) +
            " torsoShotDotAttribute=" +
                dot.getDotAttribute(defender, fireDotId);
    }

    private String prepareBountyHunterMaster(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }
        boolean skillSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_SKILL_BITS);
        boolean prerequisiteSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_PREREQUISITE_BITS);
        boolean commandSnapshot = hasObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_COMMAND_BITS);
        if (skillSnapshot != prerequisiteSnapshot ||
            skillSnapshot != commandSnapshot)
        {
            return "error=bountyHunterMasterSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!skillSnapshot)
        {
            setObjVar(attacker, ORIGINAL_BOUNTY_HUNTER_MASTER_SKILL_BITS,
                buildOwnershipBits(attacker, BOUNTY_HUNTER_MASTER_SKILLS,
                    true));
            setObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_MASTER_PREREQUISITE_BITS,
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_MASTER_PREREQUISITES, true));
            setObjVar(attacker, ORIGINAL_BOUNTY_HUNTER_MASTER_COMMAND_BITS,
                buildOwnershipBits(attacker, BOUNTY_HUNTER_MASTER_COMMANDS,
                    false));
        }
        boolean prerequisitesReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_MASTER_PREREQUISITES, true);
        boolean skillsReady = prerequisitesReady && grantOwnershipChain(
            attacker, BOUNTY_HUNTER_MASTER_SKILLS, true);
        boolean commandsReady = grantOwnershipChain(attacker,
            BOUNTY_HUNTER_MASTER_COMMANDS, false);
        if (!prerequisitesReady || !skillsReady || !commandsReady ||
            !"1111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_PREREQUISITES, true)) ||
            !"1111111111111111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_SKILLS, true)) ||
            !"11".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_COMMANDS, false)))
        {
            return "error=bountyHunterMasterGrantFailed " +
                buildBountyHunterMasterStatus(attacker, defender, lifecycle);
        }
        return "action=prepareBountyHunterMaster resumed=" + skillSnapshot +
            " " + buildBountyHunterMasterStatus(attacker, defender,
                lifecycle);
    }

    private String statusBountyHunterMaster(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            false);
        return ownership == null ? "action=statusBountyHunterMaster " +
            buildBountyHunterMasterStatus(attacker, defender, lifecycle) :
            ownership;
    }

    private String armBountyHunterMaster(obj_id attacker, obj_id defender,
        String lifecycle, boolean carbine) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        String fixtureKey = carbine ? FIXTURE_CARBINE : FIXTURE_PISTOL;
        String expectedTemplate = carbine ? CDEF_CARBINE_TEMPLATE :
            CDEF_PISTOL_TEMPLATE;
        obj_id weapon = hasObjVar(attacker, fixtureKey) ?
            getObjIdObjVar(attacker, fixtureKey) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !expectedTemplate.equals(getTemplateName(weapon)))
        {
            return "error=fixtureMasterWeaponMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean scriptAttached = hasScript(weapon,
            "systems.combat.combat_weapon");
        if (scriptAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (scriptAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean statesCleared = clearProofStateBuffs(defender);
        boolean boundary = equipped && statesCleared &&
            "1111111111111111111".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_SKILLS, true)) &&
            "11".equals(buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_COMMANDS, false)) &&
            combat.canPerformAction(carbine ? "sprayShot" : "fastBlast",
                attacker) == 0;
        return boundary ? "action=armBountyHunterMaster weapon=" +
            (carbine ? "carbine" : "pistol") + " equipped=true " +
            buildBountyHunterMasterStatus(attacker, defender, lifecycle) :
            "error=bountyHunterMasterOwnershipBoundaryFailed " +
            buildBountyHunterMasterStatus(attacker, defender, lifecycle);
    }

    private String cleanupBountyHunterMaster(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupBountyHunterMaster alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker, ORIGINAL_BOUNTY_HUNTER_MASTER_SKILL_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_BOUNTY_HUNTER_MASTER_PREREQUISITE_BITS) ||
            !hasObjVar(attacker, ORIGINAL_BOUNTY_HUNTER_MASTER_COMMAND_BITS))
        {
            return "error=bountyHunterMasterSnapshotMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        String skillBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_SKILL_BITS);
        String prerequisiteBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_PREREQUISITE_BITS);
        String commandBits = getStringObjVar(attacker,
            ORIGINAL_BOUNTY_HUNTER_MASTER_COMMAND_BITS);
        boolean restored = restoreOwnershipBits(attacker,
            BOUNTY_HUNTER_MASTER_COMMANDS, commandBits, false) &&
            restoreOwnershipBits(attacker, BOUNTY_HUNTER_MASTER_SKILLS,
                skillBits, true) &&
            restoreOwnershipBits(attacker, BOUNTY_HUNTER_MASTER_PREREQUISITES,
                prerequisiteBits, true);
        if (!restored)
        {
            return "error=bountyHunterMasterRestorationFailed " +
                buildBountyHunterMasterStatus(attacker, defender, lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupBountyHunterMaster alreadyClean=false restored=true " +
                baseCleanup : baseCleanup;
    }

    private String buildBountyHunterMasterStatus(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        int[] sprayCosts = getFixtureActionCosts(attacker, "sprayShot");
        int[] fastCosts = getFixtureActionCosts(attacker, "fastBlast");
        combat_data fastData = combat_engine.getCombatData("fastBlast");
        obj_id held = getObjectInSlot(attacker, "hold_r");
        return buildStatus(attacker, defender, lifecycle) +
            " bountyHunterMasterSkillBits=" + buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_SKILLS, true) +
            " bountyHunterMasterPrerequisiteBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_MASTER_PREREQUISITES, true) +
            " bountyHunterMasterCommandBits=" + buildOwnershipBits(attacker,
                BOUNTY_HUNTER_MASTER_COMMANDS, false) +
            " sprayShotCanPerform=" +
                combat.canPerformAction("sprayShot", attacker) +
            " fastBlastCanPerform=" +
                combat.canPerformAction("fastBlast", attacker) +
            " sprayShotCosts=" + sprayCosts[0] + "," + sprayCosts[1] +
                "," + sprayCosts[2] +
            " fastBlastCosts=" + fastCosts[0] + "," + fastCosts[1] +
                "," + fastCosts[2] +
            " carbineAccuracy=" +
                getSkillStatisticModifier(attacker, "carbine_accuracy") +
            " pistolAccuracy=" +
                getSkillStatisticModifier(attacker, "pistol_accuracy") +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " defenderDizzyState=" + getState(defender, STATE_DIZZY) +
            " defenderBlindState=" + getState(defender, STATE_BLINDED) +
            " defenderStunState=" + getState(defender, STATE_STUNNED) +
            " fastBlastHealthMultiplier=" + (fastData == null ? -1.0f :
                fastData.precuHealthDamageMultiplier) +
            " fastBlastActionMultiplier=" + (fastData == null ? -1.0f :
                fastData.precuActionDamageMultiplier) +
            " fastBlastMindMultiplier=" + (fastData == null ? -1.0f :
                fastData.precuMindDamageMultiplier) +
            " poolDamageActiveMask=" +
                readDiagnosticInt(attacker, "poolDamage.activeMask", -1) +
            " poolDamageHealthMultiplier=" + readDiagnosticFloat(attacker,
                "poolDamage.multiplier0", -1.0f) +
            " poolDamageActionMultiplier=" + readDiagnosticFloat(attacker,
                "poolDamage.multiplier1", -1.0f) +
            " poolDamageMindMultiplier=" + readDiagnosticFloat(attacker,
                "poolDamage.multiplier2", -1.0f) +
            " poolDamageHealthApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied0", -1) +
            " poolDamageActionApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied1", -1) +
            " poolDamageMindApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied2", -1);
    }

    private String prepareSmugglerCombat(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String prepared = prepare(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepare"))
        {
            return prepared;
        }
        boolean skillSnapshot = hasObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_SKILL_BITS);
        boolean prerequisiteSnapshot = hasObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_PREREQUISITE_BITS);
        boolean commandSnapshot = hasObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_COMMAND_BITS);
        if (skillSnapshot != prerequisiteSnapshot ||
            skillSnapshot != commandSnapshot)
        {
            return "error=smugglerCombatSnapshotPartial " +
                buildStatus(attacker, defender, lifecycle);
        }
        if (!skillSnapshot)
        {
            setObjVar(attacker, ORIGINAL_SMUGGLER_COMBAT_SKILL_BITS,
                buildOwnershipBits(attacker, SMUGGLER_COMBAT_SKILLS, true));
            setObjVar(attacker,
                ORIGINAL_SMUGGLER_COMBAT_PREREQUISITE_BITS,
                buildOwnershipBits(attacker, SMUGGLER_COMBAT_PREREQUISITES,
                    true));
            setObjVar(attacker, ORIGINAL_SMUGGLER_COMBAT_COMMAND_BITS,
                buildOwnershipBits(attacker, SMUGGLER_COMBAT_COMMANDS,
                    false));
        }
        boolean prerequisitesReady = grantOwnershipChain(attacker,
            SMUGGLER_COMBAT_PREREQUISITES, true);
        boolean skillsReady = prerequisitesReady && grantOwnershipChain(
            attacker, SMUGGLER_COMBAT_SKILLS, true);
        boolean commandsReady = grantOwnershipChain(attacker,
            SMUGGLER_COMBAT_COMMANDS, false);
        if (!prerequisitesReady || !skillsReady || !commandsReady ||
            !"111111111111".equals(buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_PREREQUISITES, true)) ||
            !"111111".equals(buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_SKILLS, true)) ||
            !"111".equals(buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_COMMANDS, false)))
        {
            return "error=smugglerCombatGrantFailed " +
                buildSmugglerCombatStatus(attacker, defender, lifecycle);
        }
        return "action=prepareSmugglerCombat resumed=" + skillSnapshot +
            " " + buildSmugglerCombatStatus(attacker, defender, lifecycle);
    }

    private String statusSmugglerCombat(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            false);
        return ownership == null ? "action=statusSmugglerCombat " +
            buildSmugglerCombatStatus(attacker, defender, lifecycle) :
            ownership;
    }

    private String armSmugglerCombat(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_PISTOL) ?
            getObjIdObjVar(attacker, FIXTURE_PISTOL) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !CDEF_PISTOL_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixtureSmugglerPistolMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean scriptAttached = hasScript(weapon,
            "systems.combat.combat_weapon");
        if (scriptAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (scriptAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean statesCleared = clearProofStateBuffs(defender);
        boolean boundary = equipped && statesCleared &&
            "111111".equals(buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_SKILLS, true)) &&
            "111".equals(buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_COMMANDS, false)) &&
            combat.canPerformAction("panicShot", attacker) == 0 &&
            combat.canPerformAction("lowBlow", attacker) == 0 &&
            combat.canPerformAction("lastDitch", attacker) == 0;
        return boundary ? "action=armSmugglerCombat equipped=true " +
            buildSmugglerCombatStatus(attacker, defender, lifecycle) :
            "error=smugglerCombatOwnershipBoundaryFailed " +
            buildSmugglerCombatStatus(attacker, defender, lifecycle);
    }

    private String cleanupSmugglerCombat(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupSmugglerCombat alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker, ORIGINAL_SMUGGLER_COMBAT_SKILL_BITS) ||
            !hasObjVar(attacker,
                ORIGINAL_SMUGGLER_COMBAT_PREREQUISITE_BITS) ||
            !hasObjVar(attacker, ORIGINAL_SMUGGLER_COMBAT_COMMAND_BITS))
        {
            return "error=smugglerCombatSnapshotMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        String skillBits = getStringObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_SKILL_BITS);
        String prerequisiteBits = getStringObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_PREREQUISITE_BITS);
        String commandBits = getStringObjVar(attacker,
            ORIGINAL_SMUGGLER_COMBAT_COMMAND_BITS);
        boolean restored = restoreOwnershipBits(attacker,
            SMUGGLER_COMBAT_COMMANDS, commandBits, false) &&
            restoreOwnershipBits(attacker, SMUGGLER_COMBAT_SKILLS,
                skillBits, true) &&
            restoreOwnershipBits(attacker, SMUGGLER_COMBAT_PREREQUISITES,
                prerequisiteBits, true);
        if (!restored)
        {
            return "error=smugglerCombatRestorationFailed " +
                buildSmugglerCombatStatus(attacker, defender, lifecycle);
        }
        String baseCleanup = cleanup(attacker, defender, lifecycle);
        return baseCleanup.startsWith("action=cleanup") ?
            "action=cleanupSmugglerCombat alreadyClean=false restored=true " +
                baseCleanup : baseCleanup;
    }

    private String buildSmugglerCombatStatus(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        int[] panicCosts = getFixtureActionCosts(attacker, "panicShot");
        int[] lowCosts = getFixtureActionCosts(attacker, "lowBlow");
        int[] ditchCosts = getFixtureActionCosts(attacker, "lastDitch");
        obj_id held = getObjectInSlot(attacker, "hold_r");
        return buildStatus(attacker, defender, lifecycle) +
            " smugglerCombatSkillBits=" + buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_SKILLS, true) +
            " smugglerCombatPrerequisiteBits=" +
                buildOwnershipBits(attacker,
                    SMUGGLER_COMBAT_PREREQUISITES, true) +
            " smugglerCombatCommandBits=" + buildOwnershipBits(attacker,
                SMUGGLER_COMBAT_COMMANDS, false) +
            " panicShotCanPerform=" +
                combat.canPerformAction("panicShot", attacker) +
            " lowBlowCanPerform=" +
                combat.canPerformAction("lowBlow", attacker) +
            " lastDitchCanPerform=" +
                combat.canPerformAction("lastDitch", attacker) +
            " panicShotCosts=" + panicCosts[0] + "," + panicCosts[1] +
                "," + panicCosts[2] +
            " lowBlowCosts=" + lowCosts[0] + "," + lowCosts[1] + "," +
                lowCosts[2] +
            " lastDitchCosts=" + ditchCosts[0] + "," + ditchCosts[1] +
                "," + ditchCosts[2] +
            " feignDeathModifier=" +
                getSkillStatisticModifier(attacker, "feign_death") +
            " pistolAccuracy=" +
                getSkillStatisticModifier(attacker, "pistol_accuracy") +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " defenderStunState=" + getState(defender, STATE_STUNNED) +
            " defenderPosture=" + getPosture(defender);
    }

    private String prepareFeignDeath(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        boolean snapshotPresent = hasObjVar(attacker,
            M329_ORIGINAL_FEIGN_COMMAND);
        int originalFeignCommand = hasCommand(attacker, "feignDeath") ? 1 : 0;
        String prepared = prepareSmugglerCombat(attacker, defender, lifecycle);
        if (!prepared.startsWith("action=prepareSmugglerCombat"))
        {
            return prepared;
        }
        if (!snapshotPresent)
        {
            setObjVar(attacker, M329_ORIGINAL_FEIGN_COMMAND,
                originalFeignCommand);
            setObjVar(attacker, M329_ORIGINAL_DEFENDER_HEADSHOT_COMMAND,
                hasCommand(defender, COMMAND) ? 1 : 0);
            setObjVar(attacker, M329_ORIGINAL_DEFENDER_CERTIFICATION,
                hasCommand(defender, CDEF_CERTIFICATION) ? 1 : 0);
            obj_id held = getObjectInSlot(defender, "hold_r");
            boolean heldPresent = isIdValid(held);
            setObjVar(attacker, M329_ORIGINAL_DEFENDER_WEAPON_PRESENT,
                heldPresent ? 1 : 0);
            if (heldPresent)
            {
                setObjVar(attacker, M329_ORIGINAL_DEFENDER_WEAPON, held);
            }
        }
        else if (!hasObjVar(attacker,
            M329_ORIGINAL_DEFENDER_WEAPON_PRESENT))
        {
            // Heal snapshots created by the original M329 fixture revision:
            // NULL_ID objvars are not retained, so an absent weapon objvar
            // unambiguously represented an originally empty hand slot.
            setObjVar(attacker, M329_ORIGINAL_DEFENDER_WEAPON_PRESENT, 0);
        }
        if (!hasCommand(attacker, "feignDeath") ||
            (!hasCommand(defender, COMMAND) &&
                !grantCommand(defender, COMMAND)) ||
            (!hasCommand(defender, CDEF_CERTIFICATION) &&
                !grantCommand(defender, CDEF_CERTIFICATION)))
        {
            return "error=feignDeathCommandPreparationFailed " +
                buildFeignDeathStatus(attacker, defender, lifecycle);
        }
        obj_id defenderRifle = hasObjVar(attacker,
            M329_FIXTURE_DEFENDER_RIFLE) ? getObjIdObjVar(attacker,
                M329_FIXTURE_DEFENDER_RIFLE) : obj_id.NULL_ID;
        if (!isIdValid(defenderRifle) || !exists(defenderRifle))
        {
            defenderRifle = createObjectInInventoryAllowOverload(
                CDEF_TEMPLATE, defender);
            if (!isIdValid(defenderRifle) || !exists(defenderRifle) ||
                !CDEF_TEMPLATE.equals(getTemplateName(defenderRifle)))
            {
                return "error=feignDeathDefenderRifleCreationFailed " +
                    buildFeignDeathStatus(attacker, defender, lifecycle);
            }
            setObjVar(attacker, M329_FIXTURE_DEFENDER_RIFLE,
                defenderRifle);
        }
        setObjVar(attacker,
            combat.PRECU_FEIGN_DIAGNOSTIC_ROOT + ".enabled", 1);
        return "action=prepareFeignDeath resumed=" + snapshotPresent + " " +
            buildFeignDeathStatus(attacker, defender, lifecycle);
    }

    private String statusFeignDeath(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            false);
        return ownership == null ? "action=statusFeignDeath " +
            buildFeignDeathStatus(attacker, defender, lifecycle) : ownership;
    }

    private String armFeignDeath(obj_id attacker, obj_id defender,
        String lifecycle, int forcedRoll, boolean enterCombat)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            false);
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker, M329_ORIGINAL_FEIGN_COMMAND) ||
            !hasObjVar(attacker,
                M329_ORIGINAL_DEFENDER_HEADSHOT_COMMAND) ||
            !hasObjVar(attacker, M329_ORIGINAL_DEFENDER_CERTIFICATION) ||
            !hasObjVar(attacker,
                M329_ORIGINAL_DEFENDER_WEAPON_PRESENT) ||
            (getIntObjVar(attacker,
                M329_ORIGINAL_DEFENDER_WEAPON_PRESENT) == 1 &&
                !hasObjVar(attacker, M329_ORIGINAL_DEFENDER_WEAPON)) ||
            !hasObjVar(attacker, M329_FIXTURE_DEFENDER_RIFLE))
        {
            return "error=feignDeathSnapshotMissing " +
                buildFeignDeathStatus(attacker, defender, lifecycle);
        }
        String generated = armSmugglerCombat(attacker, defender, lifecycle);
        if (!generated.startsWith("action=armSmugglerCombat"))
        {
            return generated;
        }
        obj_id defenderRifle = getObjIdObjVar(attacker,
            M329_FIXTURE_DEFENDER_RIFLE);
        if (!equipM329Weapon(defender, defenderRifle))
        {
            return "error=feignDeathDefenderRifleEquipFailed " +
                buildFeignDeathStatus(attacker, defender, lifecycle);
        }
        combat.revealPrecuFeignDeath(attacker, "fixtureArm");
        combat.clearPrecuFeignPending(attacker);
        if (hasObjVar(attacker, combat.PRECU_FEIGN_DIAGNOSTIC_ROOT))
        {
            removeObjVar(attacker, combat.PRECU_FEIGN_DIAGNOSTIC_ROOT);
        }
        setObjVar(attacker,
            combat.PRECU_FEIGN_DIAGNOSTIC_ROOT + ".enabled", 1);
        setObjVar(attacker, combat.PRECU_FEIGN_FORCED_ROLL, forcedRoll);
        setPostureClientImmediate(attacker, POSTURE_UPRIGHT);
        setLocomotion(attacker, LOCOMOTION_STANDING);
        setPostureClientImmediate(defender, POSTURE_UPRIGHT);
        setLocomotion(defender, LOCOMOTION_STANDING);
        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, enterCombat ? defender : obj_id.NULL_ID);
        setCombatTarget(defender, enterCombat ? attacker : obj_id.NULL_ID);
        if (enterCombat)
        {
            startCombat(attacker, defender);
            startCombat(defender, attacker);
        }
        boolean boundary = hasCommand(attacker, "feignDeath") &&
            hasCommand(defender, COMMAND) &&
            hasCommand(defender, CDEF_CERTIFICATION) &&
            getObjectInSlot(defender, "hold_r") == defenderRifle &&
            combat.canPerformAction(COMMAND, defender) == 0 &&
            (enterCombat == combat.isInCombat(attacker));
        return boundary ? "action=armFeignDeath mode=" +
            (enterCombat ? (forcedRoll == 35 ? "success" : "failure") :
                "noCombat") + " forcedRoll=" + forcedRoll + " " +
            buildFeignDeathStatus(attacker, defender, lifecycle) :
            "error=feignDeathBoundaryFailed enterCombat=" + enterCombat +
                " " + buildFeignDeathStatus(attacker, defender, lifecycle);
    }

    private String cleanupFeignDeath(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle,
            true);
        if ("fixtureAbsent".equals(ownership))
        {
            return "action=cleanupFeignDeath alreadyClean=true restored=true";
        }
        if (ownership != null)
        {
            return ownership;
        }
        if (!hasObjVar(attacker, M329_ORIGINAL_FEIGN_COMMAND) ||
            !hasObjVar(attacker,
                M329_ORIGINAL_DEFENDER_HEADSHOT_COMMAND) ||
            !hasObjVar(attacker, M329_ORIGINAL_DEFENDER_CERTIFICATION) ||
            !hasObjVar(attacker,
                M329_ORIGINAL_DEFENDER_WEAPON_PRESENT) ||
            (getIntObjVar(attacker,
                M329_ORIGINAL_DEFENDER_WEAPON_PRESENT) == 1 &&
                !hasObjVar(attacker, M329_ORIGINAL_DEFENDER_WEAPON)))
        {
            return "error=feignDeathSnapshotMissing " +
                buildFeignDeathStatus(attacker, defender, lifecycle);
        }
        int originalFeign = getIntObjVar(attacker,
            M329_ORIGINAL_FEIGN_COMMAND);
        int originalDefenderCommand = getIntObjVar(attacker,
            M329_ORIGINAL_DEFENDER_HEADSHOT_COMMAND);
        int originalDefenderCertification = getIntObjVar(attacker,
            M329_ORIGINAL_DEFENDER_CERTIFICATION);
        obj_id originalDefenderWeapon = getIntObjVar(attacker,
            M329_ORIGINAL_DEFENDER_WEAPON_PRESENT) == 1 ?
                getObjIdObjVar(attacker,
                    M329_ORIGINAL_DEFENDER_WEAPON) : obj_id.NULL_ID;
        combat.revealPrecuFeignDeath(attacker, "fixtureCleanup");
        combat.clearPrecuFeignPending(attacker);
        stopCombat(attacker);
        stopCombat(defender);
        obj_id defenderRifle = hasObjVar(attacker,
            M329_FIXTURE_DEFENDER_RIFLE) ? getObjIdObjVar(attacker,
                M329_FIXTURE_DEFENDER_RIFLE) : obj_id.NULL_ID;
        if (isIdValid(defenderRifle) && exists(defenderRifle))
        {
            destroyObject(defenderRifle);
        }
        boolean defenderWeaponRestored = !isIdValid(originalDefenderWeapon) ||
            (exists(originalDefenderWeapon) &&
                equipM329Weapon(defender, originalDefenderWeapon));
        if (originalDefenderCommand == 0 && hasCommand(defender, COMMAND))
        {
            revokeCommand(defender, COMMAND);
        }
        else if (originalDefenderCommand == 1 &&
            !hasCommand(defender, COMMAND))
        {
            grantCommand(defender, COMMAND);
        }
        if (originalDefenderCertification == 0 &&
            hasCommand(defender, CDEF_CERTIFICATION))
        {
            revokeCommand(defender, CDEF_CERTIFICATION);
        }
        else if (originalDefenderCertification == 1 &&
            !hasCommand(defender, CDEF_CERTIFICATION))
        {
            grantCommand(defender, CDEF_CERTIFICATION);
        }
        String baseCleanup = cleanupSmugglerCombat(attacker, defender,
            lifecycle);
        if (!baseCleanup.startsWith("action=cleanupSmugglerCombat"))
        {
            return baseCleanup;
        }
        if (originalFeign == 0 && hasCommand(attacker, "feignDeath"))
        {
            revokeCommand(attacker, "feignDeath");
        }
        else if (originalFeign == 1 && !hasCommand(attacker, "feignDeath"))
        {
            grantCommand(attacker, "feignDeath");
        }
        boolean restored = defenderWeaponRestored &&
            hasCommand(attacker, "feignDeath") == (originalFeign == 1) &&
            hasCommand(defender, COMMAND) ==
                (originalDefenderCommand == 1) &&
            hasCommand(defender, CDEF_CERTIFICATION) ==
                (originalDefenderCertification == 1) &&
            getState(attacker, STATE_FEIGN_DEATH) == 0 &&
            !buff.hasBuff(attacker, "feign_death") &&
            !hasObjVar(attacker, combat.PRECU_FEIGN_PENDING);
        return restored ?
            "action=cleanupFeignDeath alreadyClean=false restored=true" :
            "error=feignDeathRestorationFailed";
    }

    private boolean equipM329Weapon(obj_id player, obj_id weapon)
        throws InterruptedException
    {
        if (!isIdValid(weapon) || !exists(weapon))
        {
            return false;
        }
        obj_id current = getObjectInSlot(player, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(player, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return false;
            }
        }
        boolean scriptAttached = hasScript(weapon,
            "systems.combat.combat_weapon");
        if (scriptAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(player, "hold_r") == weapon ||
            equipOverride(weapon, player);
        if (scriptAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        return equipped && getObjectInSlot(player, "hold_r") == weapon;
    }

    private String buildFeignDeathStatus(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        obj_id[] targeting = getWhoIsTargetingMe(attacker);
        obj_id defenderRifle = hasObjVar(attacker,
            M329_FIXTURE_DEFENDER_RIFLE) ? getObjIdObjVar(attacker,
                M329_FIXTURE_DEFENDER_RIFLE) : obj_id.NULL_ID;
        String diagnostic = combat.PRECU_FEIGN_DIAGNOSTIC_ROOT;
        return buildSmugglerCombatStatus(attacker, defender, lifecycle) +
            " feignCommand=" + hasCommand(attacker, "feignDeath") +
            " feignCanPerform=" +
                combat.canPerformAction("feignDeath", attacker) +
            " feignModifier=" +
                getSkillStatisticModifier(attacker, "feign_death") +
            " feignPending=" +
                hasObjVar(attacker, combat.PRECU_FEIGN_PENDING) +
            " pendingDefenseModifier=" + hasSkillModModifier(attacker,
                combat.PRECU_FEIGN_PENDING_DEFENSE_MODIFIER) +
            " feignBuff=" + buff.hasBuff(attacker, "feign_death") +
            " feignState=" + getState(attacker, STATE_FEIGN_DEATH) +
            " feignPosture=" + getPosture(attacker) +
            " feignLocomotion=" + getLocomotion(attacker) +
            " feignInCombat=" + combat.isInCombat(attacker) +
            " targetingCount=" + (targeting == null ? 0 : targeting.length) +
            " damageDivisorModifier=" + hasSkillModModifier(attacker,
                combat.PRECU_FEIGN_DAMAGE_DIVISOR_MODIFIER) +
            " damageMultiplierModifier=" + hasSkillModModifier(attacker,
                combat.PRECU_FEIGN_DAMAGE_MULTIPLIER_MODIFIER) +
            " feignCooldown=" + getCooldownTimeLeft(attacker, "feignDeath") +
            " diagnosticOutcome=" + readM329String(attacker,
                diagnostic + ".outcome", "NONE") +
            " diagnosticRolls=" + readM329String(attacker,
                diagnostic + ".rolls", "NONE") +
            " diagnosticSkillMod=" + readM329Int(attacker,
                diagnostic + ".skillMod", -1) +
            " diagnosticDefenderCount=" + readM329Int(attacker,
                diagnostic + ".defenderCount", -1) +
            " diagnosticRevealSource=" + readM329String(attacker,
                diagnostic + ".revealSource", "NONE") +
            " defenderHeadShotCommand=" + hasCommand(defender, COMMAND) +
            " defenderCertification=" +
                hasCommand(defender, CDEF_CERTIFICATION) +
            " defenderRifle=" + defenderRifle +
            " defenderHeldWeapon=" + getObjectInSlot(defender, "hold_r");
    }

    private int readM329Int(obj_id player, String path, int fallback)
        throws InterruptedException
    {
        return hasObjVar(player, path) ? getIntObjVar(player, path) : fallback;
    }

    private String readM329String(obj_id player, String path,
        String fallback) throws InterruptedException
    {
        return hasObjVar(player, path) ? getStringObjVar(player, path) :
            fallback;
    }

    private String buildOwnershipBits(obj_id player, String[] names,
        boolean skills) throws InterruptedException
    {
        String bits = "";
        for (String name : names)
        {
            bits += skills ? (hasSkill(player, name) ? "1" : "0") :
                (hasCommand(player, name) ? "1" : "0");
        }
        return bits;
    }

    private boolean grantOwnershipChain(obj_id player, String[] names,
        boolean skills) throws InterruptedException
    {
        for (String name : names)
        {
            boolean owned = skills ? hasSkill(player, name) :
                hasCommand(player, name);
            if (!owned && !(skills ? grantSkill(player, name) :
                grantCommand(player, name)))
            {
                return false;
            }
        }
        return true;
    }

    private boolean restoreOwnershipBits(obj_id player, String[] names,
        String bits, boolean skills) throws InterruptedException
    {
        if (bits == null || bits.length() != names.length)
        {
            return false;
        }
        for (int index = names.length - 1; index >= 0; --index)
        {
            boolean expected = bits.charAt(index) == '1';
            boolean owned = skills ? hasSkill(player, names[index]) :
                hasCommand(player, names[index]);
            if (!expected && owned)
            {
                if (skills)
                {
                    revokeSkill(player, names[index]);
                }
                else
                {
                    revokeCommand(player, names[index]);
                }
            }
        }
        for (int index = 0; index < names.length; ++index)
        {
            boolean expected = bits.charAt(index) == '1';
            boolean owned = skills ? hasSkill(player, names[index]) :
                hasCommand(player, names[index]);
            if (expected && !owned && !(skills ? grantSkill(player,
                names[index]) : grantCommand(player, names[index])))
            {
                return false;
            }
            if ((skills ? hasSkill(player, names[index]) :
                hasCommand(player, names[index])) != expected)
            {
                return false;
            }
        }
        return true;
    }

    private int[] getFixtureActionCosts(obj_id attacker, String action)
        throws InterruptedException
    {
        combat_data data = combat_engine.getCombatData(action);
        weapon_data weaponData = getWeaponData(getCurrentWeapon(attacker));
        return data != null && weaponData != null ?
            combat.getActionCost(attacker, weaponData, data) :
            new int[] {-1, -1, -1};
    }

    private String buildBountyHunterDroidControlStatus(obj_id attacker,
        obj_id defender, String lifecycle) throws InterruptedException
    {
        int[] underHandCosts = getFixtureActionCosts(attacker,
            "underHandShot");
        int[] knockdownCosts = getFixtureActionCosts(attacker,
            "knockdownFire");
        int[] confusionCosts = getFixtureActionCosts(attacker,
            "confusionShot");
        obj_id held = getObjectInSlot(attacker, "hold_r");
        return buildStatus(attacker, defender, lifecycle) +
            " bountyHunterDroidControlSkillBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_SKILLS, true) +
            " bountyHunterDroidControlPrerequisiteBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_PREREQUISITES, true) +
            " bountyHunterDroidControlCommandBits=" +
                buildOwnershipBits(attacker,
                    BOUNTY_HUNTER_DROID_CONTROL_COMMANDS, false) +
            " underHandShotCanPerform=" +
                combat.canPerformAction("underHandShot", attacker) +
            " knockdownFireCanPerform=" +
                combat.canPerformAction("knockdownFire", attacker) +
            " confusionShotCanPerform=" +
                combat.canPerformAction("confusionShot", attacker) +
            " underHandShotCosts=" + underHandCosts[0] + "," +
                underHandCosts[1] + "," + underHandCosts[2] +
            " knockdownFireCosts=" + knockdownCosts[0] + "," +
                knockdownCosts[1] + "," + knockdownCosts[2] +
            " confusionShotCosts=" + confusionCosts[0] + "," +
                confusionCosts[1] + "," + confusionCosts[2] +
            " carbineAccuracy=" +
                getSkillStatisticModifier(attacker, "carbine_accuracy") +
            " carbineSpeed=" +
                getSkillStatisticModifier(attacker, "carbine_speed") +
            " marksmanMaster=" +
                hasSkill(attacker, "combat_marksman_master") +
            " scoutMovementFour=" +
                hasSkill(attacker, "outdoors_scout_movement_04") +
            " heldWeapon=" + (isIdValid(held) ? held : "none") +
            " defenderDizzyState=" + getState(defender, STATE_DIZZY) +
            " defenderStunState=" + getState(defender, STATE_STUNNED) +
            " defenderPosture=" + getPosture(defender) +
            " knockdownRecovery=" + utils.hasScriptVar(
                defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
    }

    private String armPolearmNovice(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        obj_id weapon = hasObjVar(attacker, FIXTURE_POLEARM) ?
            getObjIdObjVar(attacker, FIXTURE_POLEARM) : obj_id.NULL_ID;
        if (!isIdValid(weapon) ||
            !POLEARM_TEMPLATE.equals(getTemplateName(weapon)))
        {
            return "error=fixturePolearmMissing " +
                buildStatus(attacker, defender, lifecycle);
        }
        obj_id current = getObjectInSlot(attacker, "hold_r");
        if (current != weapon && isIdValid(current))
        {
            obj_id inventory = getObjectInSlot(attacker, "inventory");
            if (!isIdValid(inventory) || !putInOverloaded(current, inventory))
            {
                return "error=unexpectedAttackerWeapon " +
                    buildStatus(attacker, defender, lifecycle);
            }
        }
        boolean combatWeaponAttached =
            hasScript(weapon, "systems.combat.combat_weapon");
        if (combatWeaponAttached)
        {
            detachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean equipped = getObjectInSlot(attacker, "hold_r") == weapon ||
            equipOverride(weapon, attacker);
        if (combatWeaponAttached)
        {
            attachScript(weapon, "systems.combat.combat_weapon");
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary = equipped &&
            hasSkill(attacker, BRAWLER_POLEARM_ONE) &&
            hasSkill(attacker, BRAWLER_POLEARM_TWO) &&
            hasSkill(attacker, BRAWLER_POLEARM_THREE) &&
            hasSkill(attacker, BRAWLER_POLEARM_FOUR) &&
            hasSkill(attacker, POLEARM_NOVICE) &&
            hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_STUNNED) == 0 &&
            !buff.hasBuff(defender, "stun");
        return boundary ?
            "action=armPolearmNovice equipped=true stunState=" +
                getState(defender, STATE_STUNNED) + " stunBuff=" +
                buff.hasBuff(defender, "stun") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmNoviceOwnershipBoundaryFailed equipped=" + equipped +
                " " + buildStatus(attacker, defender, lifecycle);
    }

    private String armPolearmAccuracy(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armPolearmNovice(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armPolearmNovice"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, POLEARM_NOVICE) &&
            hasSkill(attacker, POLEARM_ACCURACY_ONE) &&
            hasSkill(attacker, POLEARM_ACCURACY_TWO) &&
            hasSkill(attacker, POLEARM_ACCURACY_THREE) &&
            hasSkill(attacker, POLEARM_ACCURACY_FOUR) &&
            hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) &&
            hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_STUNNED) == 0 &&
            !buff.hasBuff(defender, "stun") &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy");
        return boundary ?
            "action=armPolearmAccuracy equipped=true stunState=" +
                getState(defender, STATE_STUNNED) + " stunBuff=" +
                buff.hasBuff(defender, "stun") + " dizzyState=" +
                getState(defender, STATE_DIZZY) + " dizzyBuff=" +
                buff.hasBuff(defender, "dizzy") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmAccuracyOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armPolearmSpeed(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armPolearmNovice(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armPolearmNovice"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, POLEARM_NOVICE) &&
            hasSkill(attacker, POLEARM_SPEED_ONE) &&
            hasSkill(attacker, POLEARM_SPEED_TWO) &&
            hasSkill(attacker, POLEARM_SPEED_THREE) &&
            hasSkill(attacker, POLEARM_SPEED_FOUR) &&
            hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) &&
            hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) &&
            hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) &&
            hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_STUNNED) == 0 &&
            !buff.hasBuff(defender, "stun") &&
            getState(defender, STATE_DIZZY) == 0 &&
            !buff.hasBuff(defender, "dizzy");
        return boundary ?
            "action=armPolearmSpeed equipped=true stunState=" +
                getState(defender, STATE_STUNNED) + " stunBuff=" +
                buff.hasBuff(defender, "stun") + " dizzyState=" +
                getState(defender, STATE_DIZZY) + " dizzyBuff=" +
                buff.hasBuff(defender, "dizzy") + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmSpeedOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armPolearmAbility(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armPolearmNovice(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armPolearmNovice"))
        {
            return armed;
        }
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_POSTURE_UP_RECOVERY);
        utils.removeScriptVar(defender, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        utils.removeScriptVar(
            defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        boolean upright = setPostureClientImmediate(defender, POSTURE_UPRIGHT) &&
            getPosture(defender) == POSTURE_UPRIGHT;
        boolean boundary =
            hasSkill(attacker, POLEARM_NOVICE) &&
            hasSkill(attacker, POLEARM_ABILITY_ONE) &&
            hasSkill(attacker, POLEARM_ABILITY_TWO) &&
            hasSkill(attacker, POLEARM_ABILITY_THREE) &&
            hasSkill(attacker, POLEARM_ABILITY_FOUR) &&
            hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) &&
            hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) &&
            upright;
        return boundary ?
            "action=armPolearmAbility equipped=true defenderPosture=" +
                getPosture(defender) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmAbilityOwnershipBoundaryFailed upright=" + upright +
                " " + buildStatus(attacker, defender, lifecycle);
    }

    private String armPolearmSupport(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armPolearmNovice(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armPolearmNovice"))
        {
            return armed;
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }
        boolean boundary =
            hasSkill(attacker, POLEARM_NOVICE) &&
            hasSkill(attacker, POLEARM_SUPPORT_ONE) &&
            hasSkill(attacker, POLEARM_SUPPORT_TWO) &&
            hasSkill(attacker, POLEARM_SUPPORT_THREE) &&
            hasSkill(attacker, POLEARM_SUPPORT_FOUR) &&
            hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) &&
            hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) &&
            dot.getDotStrength(defender, bleedingDotId) < 0;
        return boundary ?
            "action=armPolearmSupport equipped=true bleedingDotStrength=" +
                dot.getDotStrength(defender, bleedingDotId) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmSupportOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armPolearmMaster(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armPolearmAbility(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armPolearmAbility"))
        {
            return armed;
        }
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        boolean boundary =
            hasSkill(attacker, POLEARM_ACCURACY_FOUR) &&
            hasSkill(attacker, POLEARM_SPEED_FOUR) &&
            hasSkill(attacker, POLEARM_ABILITY_FOUR) &&
            hasSkill(attacker, POLEARM_SUPPORT_FOUR) &&
            hasSkill(attacker, POLEARM_MASTER) &&
            hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) &&
            proofStateBuffsCleared &&
            getState(defender, STATE_STUNNED) == 0 &&
            !buff.hasBuff(defender, "stun") &&
            getPosture(defender) == POSTURE_UPRIGHT;
        return boundary ?
            "action=armPolearmMaster equipped=true defenderPosture=" +
                getPosture(defender) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=polearmMasterOwnershipBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armWarcryOne(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        if (getIntObjVar(attacker, ORIGINAL_BRAWLER_MASTER) == 0 &&
            hasSkill(attacker, BRAWLER_MASTER))
        {
            revokeSkill(attacker, BRAWLER_MASTER);
        }
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_REMAINING);
        boolean noviceBoundary = hasSkill(attacker, BRAWLER_ROOT) &&
            hasSkill(attacker, BRAWLER_NOVICE) &&
            !hasSkill(attacker, BRAWLER_MASTER) &&
            hasCommand(attacker, WARCRY_ONE_COMMAND) &&
            getSkillStatisticModifier(attacker, "warcry") == 0;
        return noviceBoundary ?
            "action=armWarcryOne expectedDistanceMeters=3 " +
                buildStatus(attacker, defender, lifecycle) :
            "error=warcryOneNoviceBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armWarcryTwo(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_REMAINING);
        boolean masterBoundary = hasSkill(attacker, BRAWLER_ROOT) &&
            hasSkill(attacker, BRAWLER_NOVICE) &&
            hasSkill(attacker, BRAWLER_MASTER) &&
            hasCommand(attacker, WARCRY_TWO_COMMAND) &&
            getSkillStatisticModifier(attacker, "warcry") == 20;
        return masterBoundary ?
            "action=armWarcryTwo expectedDistanceMeters=3 " +
                buildStatus(attacker, defender, lifecycle) :
            "error=warcryTwoMasterBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armLungeTwoFamily(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        boolean masterBoundary =
            hasSkill(attacker, BRAWLER_ROOT) &&
            hasSkill(attacker, BRAWLER_NOVICE) &&
            hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) &&
            hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) &&
            hasSkill(attacker, BRAWLER_POLEARM_FOUR) &&
            hasSkill(attacker, BRAWLER_UNARMED_FOUR) &&
            hasSkill(attacker, BRAWLER_MASTER) &&
            hasCommand(attacker, POLEARM_LUNGE_TWO_COMMAND) &&
            hasCommand(attacker, UNARMED_LUNGE_TWO_COMMAND) &&
            hasCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND) &&
            hasCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND);
        return masterBoundary ?
            "action=armLungeTwoFamily expectedDistanceMeters=3 " +
                buildStatus(attacker, defender, lifecycle) :
            "error=lungeTwoFamilyMasterBoundaryFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armTaunt(obj_id attacker, obj_id defender,
        String lifecycle) throws InterruptedException
    {
        String armed = armGenerated(attacker, defender, lifecycle);
        if (!armed.startsWith("action=armGenerated"))
        {
            return armed;
        }
        if (!destroyFixtureConcealTarget(attacker))
        {
            return "error=tauntTargetCleanupFailed";
        }

        location attackerLocation = getLocation(attacker);
        location targetLocation = new location(
            attackerLocation.x + 5.0f,
            getHeightAtLocation(attackerLocation.x + 5.0f,
                attackerLocation.z),
            attackerLocation.z,
            attackerLocation.area,
            null);
        obj_id tauntTarget = createFixtureConcealTarget(
            targetLocation, attacker);
        if (!isIdValid(tauntTarget))
        {
            return "error=tauntTargetCreationFailed";
        }
        setName(tauntTarget, "Precu Taunt Worrt");
        setObjVar(tauntTarget, CONCEAL_TARGET_OWNED, 1);
        setObjVar(tauntTarget, CONCEAL_TARGET_OWNER, attacker);
        setObjVar(attacker, FIXTURE_CONCEAL_TARGET, tauntTarget);
        pvpSetPermanentPersonalEnemyFlag(tauntTarget, defender);
        pvpSetPermanentPersonalEnemyFlag(defender, tauntTarget);
        stopCombat(attacker);
        stopCombat(defender);
        stopCombat(tauntTarget);
        clearHateList(tauntTarget);
        addHate(tauntTarget, defender, 200.0f);
        setCombatTarget(attacker, obj_id.NULL_ID);
        resetLiveDiagnostic(attacker);

        int tauntModifier =
            getSkillStatisticModifier(attacker, "taunt");
        boolean boundary = hasSkill(attacker, BRAWLER_ROOT) &&
            hasSkill(attacker, BRAWLER_NOVICE) &&
            hasCommand(attacker, TAUNT_COMMAND) &&
            tauntModifier >= 10 &&
            isMob(tauntTarget) && !isPlayer(tauntTarget) &&
            !isIdValid(getMaster(tauntTarget)) &&
            ai_lib.isTauntable(tauntTarget) &&
            pvpCanAttack(attacker, tauntTarget) &&
            getDistance(attacker, tauntTarget) <= 64.0f &&
            getHateTarget(tauntTarget) == defender;
        return boundary ?
            "action=armTaunt tauntTarget=" + tauntTarget +
                " initialTop=" + defender +
                " tauntModifier=" + tauntModifier +
                " initialDefenderHate=" +
                    getHate(tauntTarget, defender) +
                " initialAttackerHate=" +
                    getHate(tauntTarget, attacker) + " " +
                buildStatus(attacker, defender, lifecycle) :
            "error=tauntBoundaryFailed tauntTarget=" + tauntTarget + " " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String armConceal(obj_id attacker, obj_id defender, String lifecycle)
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
        if (!destroyFixtureConcealTarget(attacker))
        {
            return "error=concealTargetCleanupFailed";
        }

        location attackerDestination =
            new location(3500.0f,
                getHeightAtLocation(3500.0f, -4800.0f),
                -4800.0f, "tatooine", null);
        location defenderDestination =
            new location(3503.0f,
                getHeightAtLocation(3503.0f, -4800.0f),
                -4800.0f, "tatooine", null);
        location[] targetDestinations = new location[]
        {
            new location(3545.0f,
                getHeightAtLocation(3545.0f, -4800.0f),
                -4800.0f, "tatooine", null),
            new location(3500.0f,
                getHeightAtLocation(3500.0f, -4755.0f),
                -4755.0f, "tatooine", null),
            new location(3455.0f,
                getHeightAtLocation(3455.0f, -4800.0f),
                -4800.0f, "tatooine", null),
            new location(3500.0f,
                getHeightAtLocation(3500.0f, -4845.0f),
                -4845.0f, "tatooine", null),
            new location(3532.0f,
                getHeightAtLocation(3532.0f, -4768.0f),
                -4768.0f, "tatooine", null),
            new location(3468.0f,
                getHeightAtLocation(3468.0f, -4768.0f),
                -4768.0f, "tatooine", null),
            new location(3468.0f,
                getHeightAtLocation(3468.0f, -4832.0f),
                -4832.0f, "tatooine", null),
            new location(3532.0f,
                getHeightAtLocation(3532.0f, -4832.0f),
                -4832.0f, "tatooine", null)
        };
        boolean moved = setLocation(attacker, attackerDestination) &
            setLocation(defender, defenderDestination);
        boolean ready = reassertPreparedState(attacker, defender);
        resetLiveDiagnostic(attacker);
        obj_id concealTarget = obj_id.NULL_ID;
        int selectedTargetCandidate = -1;
        for (int i = 0; i < targetDestinations.length; ++i)
        {
            obj_id candidate = createFixtureConcealTarget(
                targetDestinations[i], attacker);
            if (!isIdValid(candidate))
            {
                continue;
            }
            if (getDistance(attacker, candidate) >= 40.0f &&
                canSee(attacker, candidate))
            {
                concealTarget = candidate;
                selectedTargetCandidate = i;
                break;
            }
            pvpRemovePersonalEnemyFlags(candidate, attacker);
            pvpRemovePersonalEnemyFlags(attacker, candidate);
            pvpSetAttackableOverride(candidate, false);
            destroyObject(candidate);
        }
        if (!isIdValid(concealTarget))
        {
            return "error=concealTargetVisibilityFailed moved=" + moved +
                " ready=" + ready;
        }
        setObjVar(concealTarget, CONCEAL_TARGET_OWNED, 1);
        setObjVar(concealTarget, CONCEAL_TARGET_OWNER, attacker);
        setObjVar(attacker, FIXTURE_CONCEAL_TARGET, concealTarget);
        stopCombat(attacker);
        stopCombat(concealTarget);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setPostureClientImmediate(attacker, POSTURE_UPRIGHT);
        addHate(concealTarget, attacker, 100.0f);
        float distance = getDistance(attacker, concealTarget);
        float hate = getHate(concealTarget, attacker);
        boolean attackable = pvpCanAttack(attacker, concealTarget);
        boolean visible = canSee(attacker, concealTarget);
        return moved && ready && distance >= 40.0f && hate > 0.0f &&
            attackable && visible ?
              "action=armConceal concealTarget=" + concealTarget +
                  " targetCandidate=" + selectedTargetCandidate +
                  " expectedMinimumDistanceMeters=40 distanceMeters=" + distance +
                  " initialHate=" + hate + " attackable=" + attackable +
                  " visible=" + visible + " " +
                  buildStatus(attacker, defender, lifecycle) :
              "error=concealPreparationFailed moved=" + moved +
                  " ready=" + ready +
                  " targetCandidate=" + selectedTargetCandidate +
                  " distanceMeters=" + distance +
                  " hate=" + hate + " attackable=" + attackable +
                  " visible=" + visible + " " +
                  buildStatus(attacker, defender, lifecycle);
    }

    private obj_id createFixtureConcealTarget(
        location targetLocation, obj_id attacker)
        throws InterruptedException
    {
        dictionary creatureData =
            dataTableGetRow(create.CREATURE_TABLE, CONCEAL_TARGET_CREATURE);
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
        create.initializeCreature(
            target, CONCEAL_TARGET_CREATURE, creatureData, -1);
        setName(target, "Precu Conceal Shot Worrt");
        pvpSetAttackableOverride(target, true);
        pvpSetPermanentPersonalEnemyFlag(target, attacker);
        pvpSetPermanentPersonalEnemyFlag(attacker, target);
        return target;
    }

    private String armStrafeCover(
        obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        String generated = armGenerated(attacker, defender, lifecycle);
        if (!generated.startsWith("action=armGenerated"))
        {
            return generated;
        }
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT);
        utils.removeScriptVar(
            defender, combat_base.PRECU_NEXT_ATTACK_DELAY_REMAINING);
        setState(defender, STATE_COVER, true);
        return getState(defender, STATE_COVER) != 0 ?
            "action=armStrafeCover expectedDistanceMeters=3 " +
                buildStatus(attacker, defender, lifecycle) :
            "error=strafeCoverPreparationFailed " +
                buildStatus(attacker, defender, lifecycle);
    }

    private String probeStrafeDelay(
        obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateOwnership(attacker, defender, lifecycle, true);
        if (ownership != null)
        {
            return ownership;
        }
        boolean productionAccepted = new combat_base().combatStandardAction(
            "headShot1", defender, attacker, "", "", "");
        String delayResult = utils.hasScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT) ?
            utils.getStringScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT) :
            "NONE";
        return "action=probeStrafeDelay productionAccepted=" +
            productionAccepted + " delayResult=" + delayResult + " " +
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

        boolean restored = restore(attacker, defender);
        if (!restored)
        {
            return "error=fixtureRestoreFailed " + buildStatus(attacker, defender, lifecycle);
        }
        if (hasObjVar(attacker, DIAGNOSTIC_ROOT))
        {
            removeObjVar(attacker, DIAGNOSTIC_ROOT);
        }
        return "action=cleanup alreadyClean=false restored=true lifecycle=" + lifecycle;
    }

    private void resetLiveDiagnostic(obj_id attacker) throws InterruptedException
    {
        if (hasObjVar(attacker, DIAGNOSTIC_ROOT))
        {
            removeObjVar(attacker, DIAGNOSTIC_ROOT);
        }
        setObjVar(attacker, DIAGNOSTIC_ENABLED, 1);
    }

    private void snapshot(obj_id player, obj_id peer, String lifecycle, location original)
        throws InterruptedException
    {
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, PEER, peer);
        setObjVar(player, ORIGINAL_LOCATION, original);
        setObjVar(player, ORIGINAL_POSTURE, getPosture(player));
        setObjVar(player, ORIGINAL_LOCOMOTION, getLocomotion(player));
        setObjVar(player, ORIGINAL_COVER_STATE, getState(player, STATE_COVER));
        setObjVar(player, ORIGINAL_DIZZY_BUFF,
            buff.hasBuff(player, "dizzy") ? 1 : 0);
        setObjVar(player, ORIGINAL_BLIND_BUFF,
            buff.hasBuff(player, "blind") ? 1 : 0);
        setObjVar(player, ORIGINAL_STUN_BUFF,
            buff.hasBuff(player, "stun") ? 1 : 0);
        setObjVar(player, ORIGINAL_INTIMIDATE_BUFF,
            buff.hasBuff(player, "intimidate") ? 1 : 0);
        setObjVar(player, ORIGINAL_HEALTH, getAttrib(player, HEALTH));
        setObjVar(player, ORIGINAL_MAX_HEALTH, getMaxAttrib(player, HEALTH));
        setObjVar(player, ORIGINAL_STRENGTH, getAttrib(player, STRENGTH));
        setObjVar(player, ORIGINAL_ACTION, getAttrib(player, ACTION));
        setObjVar(player, ORIGINAL_MAX_ACTION, getMaxAttrib(player, ACTION));
        setObjVar(player, ORIGINAL_QUICKNESS, getAttrib(player, QUICKNESS));
        setObjVar(player, ORIGINAL_MIND, getAttrib(player, MIND));
        setObjVar(player, ORIGINAL_MAX_MIND, getMaxAttrib(player, MIND));
        setObjVar(player, ORIGINAL_FOCUS, getAttrib(player, FOCUS));
        int[] wounds = new int[NUM_ATTRIBUTES];
        for (int attribute = 0; attribute < NUM_ATTRIBUTES; ++attribute)
        {
            wounds[attribute] = getAttribWound(player, attribute);
        }
        setObjVar(player, ORIGINAL_WOUNDS, wounds);
        setObjVar(player, ORIGINAL_SHOCK, getShockWound(player));
        setObjVar(player, ORIGINAL_HEALTH_REGEN, getRegenRate(player, HEALTH));
        setObjVar(player, ORIGINAL_ACTION_REGEN, getRegenRate(player, ACTION));
        setObjVar(player, ORIGINAL_MIND_REGEN, getRegenRate(player, MIND));
        setObjVar(player, ORIGINAL_NOVICE, hasSkill(player, MARKSMAN_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLE_ONE, hasSkill(player, RIFLE_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLE_TWO, hasSkill(player, RIFLE_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLE_THREE,
            hasSkill(player, RIFLE_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLE_FOUR,
            hasSkill(player, RIFLE_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_NOVICE,
            hasSkill(player, RIFLEMAN_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_ROOT,
            hasSkill(player, BRAWLER_ROOT) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_NOVICE,
            hasSkill(player, BRAWLER_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_ONE,
            hasSkill(player, BRAWLER_ONE_HAND_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_TWO,
            hasSkill(player, BRAWLER_ONE_HAND_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_THREE,
            hasSkill(player, BRAWLER_ONE_HAND_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_FOUR,
            hasSkill(player, BRAWLER_ONE_HAND_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_NOVICE,
            hasSkill(player, ONE_HAND_SWORD_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE,
            hasSkill(player, ONE_HAND_SWORD_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO,
            hasSkill(player, ONE_HAND_SWORD_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE,
            hasSkill(player, ONE_HAND_SWORD_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR,
            hasSkill(player, ONE_HAND_SWORD_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE,
            hasSkill(player, ONE_HAND_SWORD_ACCURACY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO,
            hasSkill(player, ONE_HAND_SWORD_ACCURACY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE,
            hasSkill(player, ONE_HAND_SWORD_ACCURACY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR,
            hasSkill(player, ONE_HAND_SWORD_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE,
            hasSkill(player, ONE_HAND_SWORD_SPEED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO,
            hasSkill(player, ONE_HAND_SWORD_SPEED_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE,
            hasSkill(player, ONE_HAND_SWORD_SPEED_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR,
            hasSkill(player, ONE_HAND_SWORD_SPEED_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE,
            hasSkill(player, ONE_HAND_SWORD_ABILITY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO,
            hasSkill(player, ONE_HAND_SWORD_ABILITY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE,
            hasSkill(player, ONE_HAND_SWORD_ABILITY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR,
            hasSkill(player, ONE_HAND_SWORD_ABILITY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SWORD_MASTER,
            hasSkill(player, ONE_HAND_SWORD_MASTER) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_ONE,
            hasSkill(player, BRAWLER_TWO_HAND_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_TWO,
            hasSkill(player, BRAWLER_TWO_HAND_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_THREE,
            hasSkill(player, BRAWLER_TWO_HAND_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_FOUR,
            hasSkill(player, BRAWLER_TWO_HAND_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_NOVICE,
            hasSkill(player, TWO_HAND_SWORD_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE,
            hasSkill(player, TWO_HAND_SWORD_ACCURACY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO,
            hasSkill(player, TWO_HAND_SWORD_ACCURACY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE,
            hasSkill(player, TWO_HAND_SWORD_ACCURACY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR,
            hasSkill(player, TWO_HAND_SWORD_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE,
            hasSkill(player, TWO_HAND_SWORD_SPEED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO,
            hasSkill(player, TWO_HAND_SWORD_SPEED_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE,
            hasSkill(player, TWO_HAND_SWORD_SPEED_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR,
            hasSkill(player, TWO_HAND_SWORD_SPEED_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE,
            hasSkill(player, TWO_HAND_SWORD_ABILITY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO,
            hasSkill(player, TWO_HAND_SWORD_ABILITY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE,
            hasSkill(player, TWO_HAND_SWORD_ABILITY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR,
            hasSkill(player, TWO_HAND_SWORD_ABILITY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE,
            hasSkill(player, TWO_HAND_SWORD_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO,
            hasSkill(player, TWO_HAND_SWORD_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE,
            hasSkill(player, TWO_HAND_SWORD_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR,
            hasSkill(player, TWO_HAND_SWORD_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWORD_MASTER,
            hasSkill(player, TWO_HAND_SWORD_MASTER) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_POLEARM_ONE,
            hasSkill(player, BRAWLER_POLEARM_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_POLEARM_TWO,
            hasSkill(player, BRAWLER_POLEARM_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_POLEARM_THREE,
            hasSkill(player, BRAWLER_POLEARM_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_POLEARM_FOUR,
            hasSkill(player, BRAWLER_POLEARM_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_NOVICE,
            hasSkill(player, POLEARM_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACCURACY_ONE,
            hasSkill(player, POLEARM_ACCURACY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACCURACY_TWO,
            hasSkill(player, POLEARM_ACCURACY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACCURACY_THREE,
            hasSkill(player, POLEARM_ACCURACY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACCURACY_FOUR,
            hasSkill(player, POLEARM_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SPEED_ONE,
            hasSkill(player, POLEARM_SPEED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SPEED_TWO,
            hasSkill(player, POLEARM_SPEED_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SPEED_THREE,
            hasSkill(player, POLEARM_SPEED_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SPEED_FOUR,
            hasSkill(player, POLEARM_SPEED_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ABILITY_ONE,
            hasSkill(player, POLEARM_ABILITY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ABILITY_TWO,
            hasSkill(player, POLEARM_ABILITY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ABILITY_THREE,
            hasSkill(player, POLEARM_ABILITY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ABILITY_FOUR,
            hasSkill(player, POLEARM_ABILITY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SUPPORT_ONE,
            hasSkill(player, POLEARM_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SUPPORT_TWO,
            hasSkill(player, POLEARM_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SUPPORT_THREE,
            hasSkill(player, POLEARM_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SUPPORT_FOUR,
            hasSkill(player, POLEARM_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_MASTER,
            hasSkill(player, POLEARM_MASTER) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_UNARMED_ONE,
            hasSkill(player, BRAWLER_UNARMED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_UNARMED_TWO,
            hasSkill(player, BRAWLER_UNARMED_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_UNARMED_THREE,
            hasSkill(player, BRAWLER_UNARMED_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_UNARMED_FOUR,
            hasSkill(player, BRAWLER_UNARMED_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_BRAWLER_MASTER,
            hasSkill(player, BRAWLER_MASTER) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_ONE,
            hasSkill(player, RIFLEMAN_ACCURACY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_TWO,
            hasSkill(player, RIFLEMAN_ACCURACY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_THREE,
            hasSkill(player, RIFLEMAN_ACCURACY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_FOUR,
            hasSkill(player, RIFLEMAN_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SPEED_ONE,
            hasSkill(player, RIFLEMAN_SPEED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SPEED_TWO,
            hasSkill(player, RIFLEMAN_SPEED_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SPEED_THREE,
            hasSkill(player, RIFLEMAN_SPEED_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SPEED_FOUR,
            hasSkill(player, RIFLEMAN_SPEED_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_ONE,
            hasSkill(player, RIFLEMAN_ABILITY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_TWO,
            hasSkill(player, RIFLEMAN_ABILITY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_THREE,
            hasSkill(player, RIFLEMAN_ABILITY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_FOUR,
            hasSkill(player, RIFLEMAN_ABILITY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_ONE,
            hasSkill(player, RIFLEMAN_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_TWO,
            hasSkill(player, RIFLEMAN_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_THREE,
            hasSkill(player, RIFLEMAN_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_FOUR,
            hasSkill(player, RIFLEMAN_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_RIFLEMAN_MASTER,
            hasSkill(player, RIFLEMAN_MASTER) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ONE,
            hasSkill(player, MARKSMAN_CARBINE_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_TWO,
            hasSkill(player, MARKSMAN_CARBINE_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_THREE,
            hasSkill(player, MARKSMAN_CARBINE_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_FOUR,
            hasSkill(player, MARKSMAN_CARBINE_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_NOVICE,
            hasSkill(player, CARBINE_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ACCURACY_ONE,
            hasSkill(player, CARBINE_ACCURACY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ACCURACY_TWO,
            hasSkill(player, CARBINE_ACCURACY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ACCURACY_THREE,
            hasSkill(player, CARBINE_ACCURACY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ACCURACY_FOUR,
            hasSkill(player, CARBINE_ACCURACY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_SUPPORT_ONE,
            hasSkill(player, CARBINE_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_SUPPORT_TWO,
            hasSkill(player, CARBINE_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_SUPPORT_THREE,
            hasSkill(player, CARBINE_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_SUPPORT_FOUR,
            hasSkill(player, CARBINE_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_SPEED_ONE,
            hasSkill(player, CARBINE_SPEED_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ABILITY_ONE,
            hasSkill(player, CARBINE_ABILITY_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ABILITY_TWO,
            hasSkill(player, CARBINE_ABILITY_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ABILITY_THREE,
            hasSkill(player, CARBINE_ABILITY_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_ABILITY_FOUR,
            hasSkill(player, CARBINE_ABILITY_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_ONE,
            hasSkill(player, MARKSMAN_PISTOL_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_TWO,
            hasSkill(player, MARKSMAN_PISTOL_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_THREE,
            hasSkill(player, MARKSMAN_PISTOL_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_FOUR,
            hasSkill(player, MARKSMAN_PISTOL_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_NOVICE,
            hasSkill(player, PISTOL_NOVICE) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_SUPPORT_ONE,
            hasSkill(player, PISTOL_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_SUPPORT_TWO,
            hasSkill(player, PISTOL_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_SUPPORT_THREE,
            hasSkill(player, PISTOL_SUPPORT_THREE) ? 1 : 0);
        setObjVar(player, ORIGINAL_SUPPORT_ONE,
            hasSkill(player, MARKSMAN_SUPPORT_ONE) ? 1 : 0);
        setObjVar(player, ORIGINAL_SUPPORT_TWO,
            hasSkill(player, MARKSMAN_SUPPORT_TWO) ? 1 : 0);
        setObjVar(player, ORIGINAL_SUPPORT_FOUR,
            hasSkill(player, MARKSMAN_SUPPORT_FOUR) ? 1 : 0);
        setObjVar(player, ORIGINAL_DURATION_CONTROL,
            hasCommand(player, DURATION_CONTROL_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_HEAD_SHOT_THREE_COMMAND,
            hasCommand(player, HEAD_SHOT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_BODY_SHOT_TWO_COMMAND,
            hasCommand(player, BODY_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_BODY_SHOT_THREE_COMMAND,
            hasCommand(player, BODY_SHOT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_HEALTH_SHOT_ONE_COMMAND,
            hasCommand(player, HEALTH_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_HEALTH_SHOT_TWO_COMMAND,
            hasCommand(player, HEALTH_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND,
            hasCommand(player, PISTOL_MELEE_DEFENSE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND,
            hasCommand(player, PISTOL_MELEE_DEFENSE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TUMBLE_TO_PRONE_COMMAND,
            hasCommand(player, TUMBLE_TO_PRONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND,
            hasCommand(player, TUMBLE_TO_KNEELING_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TUMBLE_TO_STANDING_COMMAND,
            hasCommand(player, TUMBLE_TO_STANDING_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACTION_SHOT_ONE_COMMAND,
            hasCommand(player, ACTION_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACTION_SHOT_TWO_COMMAND,
            hasCommand(player, ACTION_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_MIND_SHOT_ONE_COMMAND,
            hasCommand(player, MIND_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_MIND_SHOT_TWO_COMMAND,
            hasCommand(player, MIND_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SURPRISE_SHOT_COMMAND,
            hasCommand(player, SURPRISE_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SNIPER_SHOT_COMMAND,
            hasCommand(player, SNIPER_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_CONCEAL_SHOT_COMMAND,
            hasCommand(player, CONCEAL_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLURRY_SHOT_ONE_COMMAND,
            hasCommand(player, FLURRY_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLURRY_SHOT_TWO_COMMAND,
            hasCommand(player, FLURRY_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_CDEF_CERTIFICATION,
            hasCommand(player, CDEF_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_PISTOL_CDEF_CERTIFICATION,
            hasCommand(player, PISTOL_CDEF_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_CARBINE_CDEF_CERTIFICATION,
            hasCommand(player, CARBINE_CDEF_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_COMMAND,
            hasCommand(player, POLEARM_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_LEG_TWO_COMMAND,
            hasCommand(player, POLEARM_LEG_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_LEG_THREE_COMMAND,
            hasCommand(player, POLEARM_LEG_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_HIT_ONE_COMMAND,
            hasCommand(player, POLEARM_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_HIT_TWO_COMMAND,
            hasCommand(player, POLEARM_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_STUN_TWO_COMMAND,
            hasCommand(player, POLEARM_STUN_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SPIN_TWO_COMMAND,
            hasCommand(player, POLEARM_SPIN_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_AREA_ONE_COMMAND,
            hasCommand(player, POLEARM_AREA_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_AREA_TWO_COMMAND,
            hasCommand(player, POLEARM_AREA_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND,
            hasCommand(player, POLEARM_SWEEP_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND,
            hasCommand(player, POLEARM_SWEEP_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND,
            hasCommand(player, POLEARM_ACTION_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND,
            hasCommand(player, POLEARM_ACTION_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_HIT_THREE_COMMAND,
            hasCommand(player, POLEARM_HIT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_COMMAND,
            hasCommand(player, UNARMED_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_HIT_ONE_COMMAND,
            hasCommand(player, UNARMED_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_HIT_TWO_COMMAND,
            hasCommand(player, UNARMED_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_BODY_ONE_COMMAND,
            hasCommand(player, UNARMED_BODY_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_LEG_ONE_COMMAND,
            hasCommand(player, UNARMED_LEG_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_SPIN_ONE_COMMAND,
            hasCommand(player, UNARMED_SPIN_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_SPIN_TWO_COMMAND,
            hasCommand(player, UNARMED_SPIN_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_OVERCHARGE_ONE_COMMAND,
            hasCommand(player, OVERCHARGE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_OVERCHARGE_TWO_COMMAND,
            hasCommand(player, OVERCHARGE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND,
            hasCommand(player, POINT_BLANK_SINGLE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_AIM_COMMAND,
            hasCommand(player, AIM_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_THREATEN_SHOT_COMMAND,
            hasCommand(player, THREATEN_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_WARNING_SHOT_COMMAND,
            hasCommand(player, WARNING_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND,
            hasCommand(player, SUPPRESSION_FIRE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND,
            hasCommand(player, SUPPRESSION_FIRE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ROLL_SHOT_COMMAND,
            hasCommand(player, ROLL_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_DIVE_SHOT_COMMAND,
            hasCommand(player, DIVE_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_KIP_UP_SHOT_COMMAND,
            hasCommand(player, KIP_UP_SHOT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TAKE_COVER_COMMAND,
            hasCommand(player, TAKE_COVER_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND,
            hasCommand(player, FULL_AUTO_SINGLE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND,
            hasCommand(player, FULL_AUTO_SINGLE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND,
            hasCommand(player, FULL_AUTO_AREA_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND,
            hasCommand(player, FULL_AUTO_AREA_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_CHARGE_SHOT_ONE_COMMAND,
            hasCommand(player, CHARGE_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_CHARGE_SHOT_TWO_COMMAND,
            hasCommand(player, CHARGE_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_STRAFE_SHOT_ONE_COMMAND,
            hasCommand(player, STRAFE_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_STRAFE_SHOT_TWO_COMMAND,
            hasCommand(player, STRAFE_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_STARTLE_SHOT_ONE_COMMAND,
            hasCommand(player, STARTLE_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_STARTLE_SHOT_TWO_COMMAND,
            hasCommand(player, STARTLE_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND,
            hasCommand(player, FLUSHING_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND,
            hasCommand(player, FLUSHING_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND,
            hasCommand(player, POLEARM_LUNGE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND,
            hasCommand(player, UNARMED_LUNGE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND,
            hasCommand(player, ONE_HAND_LUNGE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND,
            hasCommand(player, TWO_HAND_LUNGE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND,
            hasCommand(player, POLEARM_LUNGE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND,
            hasCommand(player, UNARMED_LUNGE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND,
            hasCommand(player, ONE_HAND_LUNGE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND,
            hasCommand(player, TWO_HAND_LUNGE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TAUNT_COMMAND,
            hasCommand(player, TAUNT_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND,
            hasCommand(player, ONE_HAND_DIZZY_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND,
            hasCommand(player, ONE_HAND_BLIND_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND,
            hasCommand(player, ONE_HAND_BLIND_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND,
            hasCommand(player, ONE_HAND_SCATTER_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND,
            hasCommand(player, ONE_HAND_DIZZY_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND,
            hasCommand(player, ONE_HAND_SCATTER_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND,
            hasCommand(player, ONE_HAND_HEALTH_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND,
            hasCommand(player, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND,
            hasCommand(player, ONE_HAND_HEALTH_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND,
            hasCommand(player, TWO_HAND_SWEEP_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND,
            hasCommand(player, TWO_HAND_SWEEP_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND,
            hasCommand(player, TWO_HAND_MIND_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND,
            hasCommand(player, TWO_HAND_MIND_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND,
            hasCommand(player, TWO_HAND_HIT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_STUN_ONE_COMMAND,
            hasCommand(player, POLEARM_STUN_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_BLIND_ONE_COMMAND,
            hasCommand(player, UNARMED_BLIND_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_UNARMED_STUN_ONE_COMMAND,
            hasCommand(player, UNARMED_STUN_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_INTIMIDATE_ONE_COMMAND,
            hasCommand(player, INTIMIDATE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_INTIMIDATE_TWO_COMMAND,
            hasCommand(player, INTIMIDATE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_WARCRY_ONE_COMMAND,
            hasCommand(player, WARCRY_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_WARCRY_TWO_COMMAND,
            hasCommand(player, WARCRY_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SCATTER_SHOT_ONE_COMMAND,
            hasCommand(player, SCATTER_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_SCATTER_SHOT_TWO_COMMAND,
            hasCommand(player, SCATTER_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_WILD_SHOT_ONE_COMMAND,
            hasCommand(player, WILD_SHOT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_WILD_SHOT_TWO_COMMAND,
            hasCommand(player, WILD_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LEG_SHOT_TWO_COMMAND,
            hasCommand(player, LEG_SHOT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LEG_SHOT_THREE_COMMAND,
            hasCommand(player, LEG_SHOT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACID_SINGLE_ONE_COMMAND,
            hasCommand(player, ACID_SINGLE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACID_CONE_ONE_COMMAND,
            hasCommand(player, ACID_CONE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACID_CONE_TWO_COMMAND,
            hasCommand(player, ACID_CONE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACID_SINGLE_TWO_COMMAND,
            hasCommand(player, ACID_SINGLE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLAME_SINGLE_ONE_COMMAND,
            hasCommand(player, FLAME_SINGLE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLAME_SINGLE_TWO_COMMAND,
            hasCommand(player, FLAME_SINGLE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLAME_CONE_ONE_COMMAND,
            hasCommand(player, FLAME_CONE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLAME_CONE_TWO_COMMAND,
            hasCommand(player, FLAME_CONE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND,
            hasCommand(player, LIGHTNING_SINGLE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LIGHTNING_CONE_ONE_COMMAND,
            hasCommand(player, LIGHTNING_CONE_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LIGHTNING_CONE_TWO_COMMAND,
            hasCommand(player, LIGHTNING_CONE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_LIGHTNING_SINGLE_TWO_COMMAND,
            hasCommand(player, LIGHTNING_SINGLE_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_AREA_COMMAND,
            hasCommand(player, POLEARM_AREA_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_AREA_COMMAND,
            hasCommand(player, ONE_HAND_AREA_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND,
            hasCommand(player, ONE_HAND_BODY_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND,
            hasCommand(player, ONE_HAND_BODY_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND,
            hasCommand(player, ONE_HAND_BODY_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND,
            hasCommand(player, ONE_HAND_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND,
            hasCommand(player, ONE_HAND_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND,
            hasCommand(player, ONE_HAND_HIT_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_AREA_COMMAND,
            hasCommand(player, TWO_HAND_AREA_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND,
            hasCommand(player, TWO_HAND_AREA_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND,
            hasCommand(player, TWO_HAND_HEAD_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND,
            hasCommand(player, TWO_HAND_HEAD_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND,
            hasCommand(player, TWO_HAND_HEAD_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND,
            hasCommand(player, TWO_HAND_HIT_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND,
            hasCommand(player, TWO_HAND_HIT_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND,
            hasCommand(player, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND,
            hasCommand(player, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND,
            hasCommand(player, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) ? 1 : 0);
        setObjVar(player, ORIGINAL_POLEARM_CERTIFICATION,
            hasCommand(player, POLEARM_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_ONE_HAND_CERTIFICATION,
            hasCommand(player, ONE_HAND_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_TWO_HAND_CERTIFICATION,
            hasCommand(player, TWO_HAND_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_ACID_CERTIFICATION,
            hasCommand(player, ACID_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_FLAME_CERTIFICATION,
            hasCommand(player, FLAME_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_LIGHTNING_CERTIFICATION,
            hasCommand(player, LIGHTNING_CERTIFICATION) ? 1 : 0);
        setObjVar(player, ORIGINAL_PERSONAL_ENEMY,
            pvpHasPersonalEnemyFlag(player, peer) ? 1 : 0);
        setObjVar(player, ORIGINAL_COMBAT_ACTIONS,
            hasScript(player, COMBAT_ACTIONS_SCRIPT) ? 1 : 0);
        boolean hadPostureDownRecovery =
            utils.hasScriptVar(player, combat_base.PRECU_POSTURE_DOWN_RECOVERY);
        setObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY_PRESENT,
            hadPostureDownRecovery ? 1 : 0);
        setObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY,
            hadPostureDownRecovery ?
                utils.getIntScriptVar(
                    player, combat_base.PRECU_POSTURE_DOWN_RECOVERY) : 0);
        boolean hadPostureUpRecovery =
            utils.hasScriptVar(player, combat_base.PRECU_POSTURE_UP_RECOVERY);
        setObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY_PRESENT,
            hadPostureUpRecovery ? 1 : 0);
        setObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY,
            hadPostureUpRecovery ?
                utils.getIntScriptVar(
                    player, combat_base.PRECU_POSTURE_UP_RECOVERY) : 0);
        boolean hadKnockdownRecovery =
            utils.hasScriptVar(player, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        setObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY_PRESENT,
            hadKnockdownRecovery ? 1 : 0);
        setObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY,
            hadKnockdownRecovery ?
                utils.getIntScriptVar(
                    player, combat_base.PRECU_KNOCKDOWN_RECOVERY) : 0);
        boolean hadKnockdownPosture = utils.hasScriptVar(
            player, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        setObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE_PRESENT,
            hadKnockdownPosture ? 1 : 0);
        setObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE,
            hadKnockdownPosture ?
                utils.getIntScriptVar(
                    player, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE) : 0);
        setObjVar(player, PREPARED, 0);
    }

    private boolean restore(obj_id attacker, obj_id defender) throws InterruptedException
    {
        stopCombat(attacker);
        stopCombat(defender);
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_MELEE_MODIFIER);
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_RANGED_MODIFIER);
        setState(attacker, STATE_TUMBLING, false);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        combat.clearCombatDebuffs(attacker);
        combat.clearCombatDebuffs(defender);
        for (obj_id player : new obj_id[] {attacker, defender})
        {
            utils.removeScriptVar(
                player, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL);
            utils.removeScriptVar(
                player, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT);
            utils.removeScriptVar(
                player, combat_base.PRECU_NEXT_ATTACK_DELAY_REMAINING);
        }
        String fireDotId = dot.DOT_FIRE + attacker;
        if (dot.getDotStrength(defender, fireDotId) >= 0)
        {
            dot.removeDotEffect(defender, fireDotId, false);
        }
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        if (dot.getDotStrength(defender, bleedingDotId) >= 0)
        {
            dot.removeDotEffect(defender, bleedingDotId, false);
        }

        if (hasObjVar(attacker, ORIGINAL_PERSONAL_ENEMY) &&
            getIntObjVar(attacker, ORIGINAL_PERSONAL_ENEMY) == 0)
        {
            pvpRemovePersonalEnemyFlags(attacker, defender);
        }
        if (hasObjVar(defender, ORIGINAL_PERSONAL_ENEMY) &&
            getIntObjVar(defender, ORIGINAL_PERSONAL_ENEMY) == 0)
        {
            pvpRemovePersonalEnemyFlags(defender, attacker);
        }

        if (hasObjVar(attacker, ORIGINAL_DURATION_CONTROL) &&
            getIntObjVar(attacker, ORIGINAL_DURATION_CONTROL) == 0 &&
            hasCommand(attacker, DURATION_CONTROL_COMMAND))
        {
            revokeCommand(attacker, DURATION_CONTROL_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_HEAD_SHOT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEAD_SHOT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, HEAD_SHOT_THREE_COMMAND))
        {
            revokeCommand(attacker, HEAD_SHOT_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_BODY_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_BODY_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, BODY_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, BODY_SHOT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_BODY_SHOT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_BODY_SHOT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, BODY_SHOT_THREE_COMMAND))
        {
            revokeCommand(attacker, BODY_SHOT_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_HEALTH_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEALTH_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, HEALTH_SHOT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, HEALTH_SHOT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND))
        {
            revokeCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND) &&
            getIntObjVar(
                attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND))
        {
            revokeCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TUMBLE_TO_STANDING_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TUMBLE_TO_STANDING_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_STANDING_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_KNEELING_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TUMBLE_TO_PRONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TUMBLE_TO_PRONE_COMMAND) == 0 &&
            hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND))
        {
            revokeCommand(attacker, TUMBLE_TO_PRONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ACTION_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACTION_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ACTION_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, ACTION_SHOT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ACTION_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACTION_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ACTION_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, ACTION_SHOT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_MIND_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_MIND_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, MIND_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, MIND_SHOT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, MIND_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, MIND_SHOT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, SURPRISE_SHOT_COMMAND))
        {
            revokeCommand(attacker, SURPRISE_SHOT_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_SNIPER_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SNIPER_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, SNIPER_SHOT_COMMAND))
        {
            revokeCommand(attacker, SNIPER_SHOT_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, CONCEAL_SHOT_COMMAND))
        {
            revokeCommand(attacker, CONCEAL_SHOT_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND))
        {
            revokeCommand(attacker, FLURRY_SHOT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND))
        {
            revokeCommand(attacker, FLURRY_SHOT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, CDEF_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, PISTOL_CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, PISTOL_CDEF_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_CDEF_CERTIFICATION) == 0 &&
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION))
        {
            revokeCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_COMMAND))
        {
            revokeCommand(attacker, POLEARM_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_LEG_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_LEG_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_LEG_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_LEG_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_LEG_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_LEG_THREE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_LEG_THREE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_LEG_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_STUN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_STUN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_STUN_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_STUN_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SPIN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPIN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SPIN_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_AREA_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SWEEP_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_SWEEP_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_HIT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_HIT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_HIT_THREE_COMMAND))
        {
            revokeCommand(attacker, POLEARM_HIT_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_COMMAND))
        {
            revokeCommand(attacker, UNARMED_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_HIT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, UNARMED_HIT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_BODY_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_BODY_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_BODY_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_BODY_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_LEG_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_LEG_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_LEG_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_LEG_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_SPIN_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_SPIN_ONE_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND))
        {
            revokeCommand(attacker, UNARMED_SPIN_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_UNARMED_SPIN_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_UNARMED_SPIN_TWO_COMMAND) == 0 &&
            hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND))
        {
            revokeCommand(attacker, UNARMED_SPIN_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_OVERCHARGE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_OVERCHARGE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, OVERCHARGE_ONE_COMMAND))
        {
            revokeCommand(attacker, OVERCHARGE_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_OVERCHARGE_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_OVERCHARGE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, OVERCHARGE_TWO_COMMAND))
        {
            revokeCommand(attacker, OVERCHARGE_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND);
        }
        removeAttribOrSkillModModifier(attacker, combat_base.PRECU_AIM_MODIFIER);
        setState(attacker, STATE_AIMING, false);
        if (hasObjVar(attacker, ORIGINAL_AIM_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_AIM_COMMAND) == 0 &&
            hasCommand(attacker, AIM_COMMAND))
        {
            revokeCommand(attacker, AIM_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_THREATEN_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_THREATEN_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, THREATEN_SHOT_COMMAND))
        {
            revokeCommand(attacker, THREATEN_SHOT_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_WARNING_SHOT_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_WARNING_SHOT_COMMAND) == 0 &&
            hasCommand(attacker, WARNING_SHOT_COMMAND))
        {
            revokeCommand(attacker, WARNING_SHOT_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND))
        {
            revokeCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND) == 0 &&
            hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND))
        {
            revokeCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND);
        }
        restoreCommand(attacker, ORIGINAL_ROLL_SHOT_COMMAND,
            ROLL_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_DIVE_SHOT_COMMAND,
            DIVE_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_KIP_UP_SHOT_COMMAND,
            KIP_UP_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_TAKE_COVER_COMMAND,
            TAKE_COVER_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND,
            FULL_AUTO_SINGLE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND,
            FULL_AUTO_SINGLE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND,
            FULL_AUTO_AREA_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND,
            FULL_AUTO_AREA_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_ONE_COMMAND,
            CHARGE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_TWO_COMMAND,
            CHARGE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_ONE_COMMAND,
            STRAFE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_TWO_COMMAND,
            STRAFE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_ONE_COMMAND,
            STARTLE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_TWO_COMMAND,
            STARTLE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND,
            FLUSHING_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND,
            FLUSHING_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND,
            POLEARM_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND,
            UNARMED_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND,
            ONE_HAND_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND,
            TWO_HAND_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND,
            POLEARM_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND,
            UNARMED_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND,
            ONE_HAND_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND,
            TWO_HAND_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TAUNT_COMMAND, TAUNT_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND,
            ONE_HAND_DIZZY_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND,
            ONE_HAND_BLIND_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND,
            ONE_HAND_BLIND_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND,
            ONE_HAND_SCATTER_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND,
            ONE_HAND_DIZZY_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND,
            ONE_HAND_SCATTER_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND,
            ONE_HAND_HEALTH_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND,
            ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND,
            ONE_HAND_HEALTH_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND,
            TWO_HAND_SWEEP_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND,
            TWO_HAND_SWEEP_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND,
            TWO_HAND_MIND_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND,
            TWO_HAND_MIND_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND,
            TWO_HAND_HIT_THREE_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_STUN_ONE_COMMAND,
            POLEARM_STUN_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_BLIND_ONE_COMMAND,
            UNARMED_BLIND_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_STUN_ONE_COMMAND,
            UNARMED_STUN_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_INTIMIDATE_ONE_COMMAND,
            INTIMIDATE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_INTIMIDATE_TWO_COMMAND,
            INTIMIDATE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_WARCRY_ONE_COMMAND,
            WARCRY_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_WARCRY_TWO_COMMAND,
            WARCRY_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND,
            MIND_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND,
            SURPRISE_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_SNIPER_SHOT_COMMAND,
            SNIPER_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND,
            CONCEAL_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND,
            FLURRY_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND,
            FLURRY_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_ONE_COMMAND,
            SCATTER_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_TWO_COMMAND,
            SCATTER_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_WILD_SHOT_ONE_COMMAND,
            WILD_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_WILD_SHOT_TWO_COMMAND,
            WILD_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_LEG_SHOT_TWO_COMMAND,
            LEG_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_LEG_SHOT_THREE_COMMAND,
            LEG_SHOT_THREE_COMMAND);
        if (hasObjVar(attacker, ORIGINAL_ACID_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ACID_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ACID_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, ACID_SINGLE_ONE_COMMAND);
        }
        restoreCommand(attacker, ORIGINAL_ACID_CONE_ONE_COMMAND,
            ACID_CONE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ACID_CONE_TWO_COMMAND,
            ACID_CONE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ACID_SINGLE_TWO_COMMAND,
            ACID_SINGLE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLAME_SINGLE_ONE_COMMAND,
            FLAME_SINGLE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLAME_SINGLE_TWO_COMMAND,
            FLAME_SINGLE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLAME_CONE_ONE_COMMAND,
            FLAME_CONE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLAME_CONE_TWO_COMMAND,
            FLAME_CONE_TWO_COMMAND);
        if (hasObjVar(attacker, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND) == 0 &&
            hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND))
        {
            revokeCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND);
        }
        restoreCommand(attacker, ORIGINAL_LIGHTNING_CONE_ONE_COMMAND,
            LIGHTNING_CONE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_LIGHTNING_CONE_TWO_COMMAND,
            LIGHTNING_CONE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_LIGHTNING_SINGLE_TWO_COMMAND,
            LIGHTNING_SINGLE_TWO_COMMAND);
        if (hasObjVar(attacker, ORIGINAL_POLEARM_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_AREA_COMMAND) == 0 &&
            hasCommand(attacker, POLEARM_AREA_COMMAND))
        {
            revokeCommand(attacker, POLEARM_AREA_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_AREA_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_AREA_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_AREA_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_BODY_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND) == 0 &&
            hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND))
        {
            revokeCommand(attacker, ONE_HAND_HIT_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_AREA_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_AREA_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_AREA_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_AREA_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_AREA_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HIT_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_HIT_TWO_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND);
        }
        if (hasObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND) &&
            getIntObjVar(attacker,
                ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND) == 0 &&
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND))
        {
            revokeCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_CERTIFICATION) == 0 &&
            hasCommand(attacker, POLEARM_CERTIFICATION))
        {
            revokeCommand(attacker, POLEARM_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_CERTIFICATION) == 0 &&
            hasCommand(attacker, ONE_HAND_CERTIFICATION))
        {
            revokeCommand(attacker, ONE_HAND_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_CERTIFICATION) == 0 &&
            hasCommand(attacker, TWO_HAND_CERTIFICATION))
        {
            revokeCommand(attacker, TWO_HAND_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_ACID_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_ACID_CERTIFICATION) == 0 &&
            hasCommand(attacker, ACID_CERTIFICATION))
        {
            revokeCommand(attacker, ACID_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_FLAME_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_FLAME_CERTIFICATION) == 0 &&
            hasCommand(attacker, FLAME_CERTIFICATION))
        {
            revokeCommand(attacker, FLAME_CERTIFICATION);
        }
        if (hasObjVar(attacker, ORIGINAL_LIGHTNING_CERTIFICATION) &&
            getIntObjVar(attacker, ORIGINAL_LIGHTNING_CERTIFICATION) == 0 &&
            hasCommand(attacker, LIGHTNING_CERTIFICATION))
        {
            revokeCommand(attacker, LIGHTNING_CERTIFICATION);
        }
        // Release the fixture carbine before revoking its profession tree.
        destroyFixtureCarbine(attacker);
        if (hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_THREE))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_TWO))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, PISTOL_SUPPORT_ONE))
        {
            revokeSkill(attacker, PISTOL_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_NOVICE) == 0 &&
            hasSkill(attacker, PISTOL_NOVICE))
        {
            revokeSkill(attacker, PISTOL_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_THREE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_THREE))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_TWO) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_TWO))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_PISTOL_ONE) &&
            getIntObjVar(attacker, ORIGINAL_PISTOL_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_PISTOL_ONE))
        {
            revokeSkill(attacker, MARKSMAN_PISTOL_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SPEED_ONE) == 0 &&
            hasSkill(attacker, CARBINE_SPEED_ONE))
        {
            revokeSkill(attacker, CARBINE_SPEED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_ABILITY_FOUR))
        {
            revokeSkill(attacker, CARBINE_ABILITY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_FOUR))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_THREE))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_TWO))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, CARBINE_ACCURACY_ONE))
        {
            revokeSkill(attacker, CARBINE_ACCURACY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_FOUR))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_THREE))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_TWO))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, CARBINE_SUPPORT_ONE))
        {
            revokeSkill(attacker, CARBINE_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_NOVICE) == 0 &&
            hasSkill(attacker, CARBINE_NOVICE))
        {
            revokeSkill(attacker, CARBINE_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_THREE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_THREE) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_THREE))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_TWO) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_TWO))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_CARBINE_ONE) &&
            getIntObjVar(attacker, ORIGINAL_CARBINE_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_CARBINE_ONE))
        {
            revokeSkill(attacker, MARKSMAN_CARBINE_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_MASTER) == 0 &&
            hasSkill(attacker, RIFLEMAN_MASTER))
        {
            revokeSkill(attacker, RIFLEMAN_MASTER);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ABILITY_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ABILITY_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_ABILITY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SPEED_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SPEED_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_SPEED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_FOUR))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_SUPPORT_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_THREE))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_TWO))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, RIFLEMAN_ACCURACY_ONE))
        {
            revokeSkill(attacker, RIFLEMAN_ACCURACY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLEMAN_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLEMAN_NOVICE) == 0 &&
            hasSkill(attacker, RIFLEMAN_NOVICE))
        {
            revokeSkill(attacker, RIFLEMAN_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_MASTER) == 0 &&
            hasSkill(attacker, BRAWLER_MASTER))
        {
            revokeSkill(attacker, BRAWLER_MASTER);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_MASTER) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_MASTER))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_MASTER);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_SPEED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_TWO_HAND_SWORD_NOVICE) == 0 &&
            hasSkill(attacker, TWO_HAND_SWORD_NOVICE))
        {
            revokeSkill(attacker, TWO_HAND_SWORD_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_FOUR))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_FOUR);
        }
        // The master box depends on every polearm branch. Revoke it before
        // restoring branch snapshots so a single cleanup pass is sufficient.
        if (hasObjVar(attacker, ORIGINAL_POLEARM_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_MASTER) == 0 &&
            hasSkill(attacker, POLEARM_MASTER))
        {
            revokeSkill(attacker, POLEARM_MASTER);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_FOUR))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_THREE))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_TWO))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, POLEARM_ACCURACY_ONE))
        {
            revokeSkill(attacker, POLEARM_ACCURACY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_FOUR))
        {
            revokeSkill(attacker, POLEARM_SPEED_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_THREE) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_THREE))
        {
            revokeSkill(attacker, POLEARM_SPEED_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_TWO) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_TWO))
        {
            revokeSkill(attacker, POLEARM_SPEED_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SPEED_ONE) == 0 &&
            hasSkill(attacker, POLEARM_SPEED_ONE))
        {
            revokeSkill(attacker, POLEARM_SPEED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_FOUR))
        {
            revokeSkill(attacker, POLEARM_ABILITY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_THREE) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_THREE))
        {
            revokeSkill(attacker, POLEARM_ABILITY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_TWO) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_TWO))
        {
            revokeSkill(attacker, POLEARM_ABILITY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_ABILITY_ONE) == 0 &&
            hasSkill(attacker, POLEARM_ABILITY_ONE))
        {
            revokeSkill(attacker, POLEARM_ABILITY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_FOUR))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_THREE))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_TWO))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, POLEARM_SUPPORT_ONE))
        {
            revokeSkill(attacker, POLEARM_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_POLEARM_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_POLEARM_NOVICE) == 0 &&
            hasSkill(attacker, POLEARM_NOVICE))
        {
            revokeSkill(attacker, POLEARM_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_FOUR))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_FOUR))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_MASTER) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_MASTER) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_MASTER))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_MASTER);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SPEED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_ONE_HAND_SWORD_NOVICE) == 0 &&
            hasSkill(attacker, ONE_HAND_SWORD_NOVICE))
        {
            revokeSkill(attacker, ONE_HAND_SWORD_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_FOUR) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_FOUR))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_THREE))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_TWO))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_TWO_HAND_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_TWO_HAND_ONE))
        {
            revokeSkill(attacker, BRAWLER_TWO_HAND_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_THREE))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_TWO))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_POLEARM_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_POLEARM_ONE))
        {
            revokeSkill(attacker, BRAWLER_POLEARM_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_THREE))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_TWO))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_UNARMED_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_UNARMED_ONE))
        {
            revokeSkill(attacker, BRAWLER_UNARMED_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_THREE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_THREE) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_THREE))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_TWO) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_TWO) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_TWO))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_ONE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ONE_HAND_ONE) == 0 &&
            hasSkill(attacker, BRAWLER_ONE_HAND_ONE))
        {
            revokeSkill(attacker, BRAWLER_ONE_HAND_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_NOVICE) == 0 &&
            hasSkill(attacker, BRAWLER_NOVICE))
        {
            revokeSkill(attacker, BRAWLER_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_BRAWLER_ROOT) &&
            getIntObjVar(attacker, ORIGINAL_BRAWLER_ROOT) == 0 &&
            hasSkill(attacker, BRAWLER_ROOT))
        {
            revokeSkill(attacker, BRAWLER_ROOT);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLE_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_FOUR) == 0 &&
            hasSkill(attacker, RIFLE_FOUR))
        {
            revokeSkill(attacker, RIFLE_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLE_THREE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_THREE) == 0 &&
            hasSkill(attacker, RIFLE_THREE))
        {
            revokeSkill(attacker, RIFLE_THREE);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLE_TWO) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_TWO) == 0 &&
            hasSkill(attacker, RIFLE_TWO))
        {
            revokeSkill(attacker, RIFLE_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_RIFLE_ONE) &&
            getIntObjVar(attacker, ORIGINAL_RIFLE_ONE) == 0 &&
            hasSkill(attacker, RIFLE_ONE))
        {
            revokeSkill(attacker, RIFLE_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_SUPPORT_FOUR) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_FOUR) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_FOUR))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_FOUR);
        }
        if (hasObjVar(attacker, ORIGINAL_SUPPORT_TWO) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_TWO) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_TWO))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_TWO);
        }
        if (hasObjVar(attacker, ORIGINAL_SUPPORT_ONE) &&
            getIntObjVar(attacker, ORIGINAL_SUPPORT_ONE) == 0 &&
            hasSkill(attacker, MARKSMAN_SUPPORT_ONE))
        {
            revokeSkill(attacker, MARKSMAN_SUPPORT_ONE);
        }
        if (hasObjVar(attacker, ORIGINAL_NOVICE) &&
            getIntObjVar(attacker, ORIGINAL_NOVICE) == 0 &&
            hasSkill(attacker, MARKSMAN_NOVICE))
        {
            revokeSkill(attacker, MARKSMAN_NOVICE);
        }
        if (hasObjVar(attacker, ORIGINAL_COMBAT_ACTIONS) &&
            getIntObjVar(attacker, ORIGINAL_COMBAT_ACTIONS) == 0 &&
            hasScript(attacker, COMBAT_ACTIONS_SCRIPT))
        {
            detachScript(attacker, COMBAT_ACTIONS_SCRIPT);
        }

        retryCarbineRestoration(attacker);

        boolean fixtureWeaponDestroyed = destroyFixtureWeapon(attacker);
        boolean fixturePistolDestroyed = destroyFixturePistol(attacker);
        boolean fixtureCarbineDestroyed = destroyFixtureCarbine(attacker);
        boolean fixturePolearmDestroyed = destroyFixturePolearm(attacker);
        boolean fixtureOneHandDestroyed = destroyFixtureOneHand(attacker);
        boolean fixtureTwoHandDestroyed = destroyFixtureTwoHand(attacker);
        boolean fixtureAcidDestroyed = destroyFixtureAcid(attacker);
        boolean fixtureFlameDestroyed = destroyFixtureFlame(attacker);
        boolean fixtureLightningDestroyed = destroyFixtureLightning(attacker);
        boolean fixtureConcealTargetDestroyed =
            destroyFixtureConcealTarget(attacker);
        boolean attackerRestored = restorePlayer(attacker);
        boolean defenderRestored = restorePlayer(defender);
        boolean pistolSkillsRestored =
            isSkillRestored(attacker, ORIGINAL_PISTOL_SUPPORT_THREE,
                PISTOL_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_SUPPORT_TWO,
                PISTOL_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_SUPPORT_ONE,
                PISTOL_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_NOVICE,
                PISTOL_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_FOUR,
                MARKSMAN_PISTOL_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_THREE,
                MARKSMAN_PISTOL_THREE) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_TWO,
                MARKSMAN_PISTOL_TWO) &&
            isSkillRestored(attacker, ORIGINAL_PISTOL_ONE,
                MARKSMAN_PISTOL_ONE);
        boolean pistolCommandsRestored =
            isCommandRestored(attacker,
                ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND,
                PISTOL_MELEE_DEFENSE_TWO_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND,
                PISTOL_MELEE_DEFENSE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND,
                HEALTH_SHOT_TWO_COMMAND);
        boolean marksmanTumbleSkillsRestored =
            isSkillRestored(attacker, ORIGINAL_SUPPORT_TWO,
                MARKSMAN_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_SUPPORT_ONE,
                MARKSMAN_SUPPORT_ONE);
        boolean marksmanTumbleCommandsRestored =
            isCommandRestored(attacker,
                ORIGINAL_TUMBLE_TO_STANDING_COMMAND,
                TUMBLE_TO_STANDING_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_TUMBLE_TO_KNEELING_COMMAND,
                TUMBLE_TO_KNEELING_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_TUMBLE_TO_PRONE_COMMAND,
                TUMBLE_TO_PRONE_COMMAND);
        boolean carbineSkillsRestored =
            isSkillRestored(attacker, ORIGINAL_CARBINE_ABILITY_FOUR,
                CARBINE_ABILITY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ABILITY_THREE,
                CARBINE_ABILITY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ABILITY_TWO,
                CARBINE_ABILITY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ABILITY_ONE,
                CARBINE_ABILITY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_SPEED_ONE,
                CARBINE_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR,
                CARBINE_ACCURACY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ACCURACY_THREE,
                CARBINE_ACCURACY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ACCURACY_TWO,
                CARBINE_ACCURACY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ACCURACY_ONE,
                CARBINE_ACCURACY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_SUPPORT_TWO,
                CARBINE_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_SUPPORT_THREE,
                CARBINE_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR,
                CARBINE_SUPPORT_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_SUPPORT_ONE,
                CARBINE_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_NOVICE,
                CARBINE_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_FOUR,
                MARKSMAN_CARBINE_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_THREE,
                MARKSMAN_CARBINE_THREE) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_TWO,
                MARKSMAN_CARBINE_TWO) &&
            isSkillRestored(attacker, ORIGINAL_CARBINE_ONE,
                MARKSMAN_CARBINE_ONE);
        boolean carbineCommandsRestored =
            isCommandRestored(attacker,
                ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND,
                SUPPRESSION_FIRE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND,
                FULL_AUTO_SINGLE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND,
                FULL_AUTO_SINGLE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND,
                FULL_AUTO_AREA_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND,
                FULL_AUTO_AREA_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_CHARGE_SHOT_ONE_COMMAND,
                CHARGE_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_CHARGE_SHOT_TWO_COMMAND,
                CHARGE_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_STRAFE_SHOT_ONE_COMMAND,
                STRAFE_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_STRAFE_SHOT_TWO_COMMAND,
                STRAFE_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_STARTLE_SHOT_ONE_COMMAND,
                STARTLE_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_STARTLE_SHOT_TWO_COMMAND,
                STARTLE_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND,
                FLUSHING_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND,
                FLUSHING_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND,
                POLEARM_LUNGE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND,
                UNARMED_LUNGE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND,
                ONE_HAND_LUNGE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND,
                TWO_HAND_LUNGE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND,
                POLEARM_LUNGE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND,
                UNARMED_LUNGE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND,
                ONE_HAND_LUNGE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND,
                TWO_HAND_LUNGE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TAUNT_COMMAND,
                TAUNT_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND,
                ONE_HAND_DIZZY_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND,
                ONE_HAND_BLIND_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND,
                ONE_HAND_BLIND_HIT_TWO_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND,
                ONE_HAND_SCATTER_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND,
                ONE_HAND_DIZZY_HIT_TWO_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND,
                ONE_HAND_SCATTER_HIT_TWO_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND,
                ONE_HAND_HEALTH_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND,
                ONE_HAND_SPIN_ATTACK_TWO_COMMAND) &&
            isCommandRestored(attacker,
                ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND,
                ONE_HAND_HEALTH_HIT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND,
                TWO_HAND_SWEEP_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND,
                TWO_HAND_SWEEP_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND,
                TWO_HAND_MIND_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND,
                TWO_HAND_MIND_HIT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND,
                TWO_HAND_HIT_THREE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_STUN_ONE_COMMAND,
                POLEARM_STUN_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_UNARMED_BLIND_ONE_COMMAND,
                UNARMED_BLIND_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_UNARMED_STUN_ONE_COMMAND,
                UNARMED_STUN_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_INTIMIDATE_ONE_COMMAND,
                INTIMIDATE_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_INTIMIDATE_TWO_COMMAND,
                INTIMIDATE_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_WARCRY_ONE_COMMAND,
                WARCRY_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_WARCRY_TWO_COMMAND,
                WARCRY_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND,
                MIND_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND,
                SURPRISE_SHOT_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_SNIPER_SHOT_COMMAND,
                SNIPER_SHOT_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND,
                CONCEAL_SHOT_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND,
                FLURRY_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND,
                FLURRY_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_SCATTER_SHOT_ONE_COMMAND,
                SCATTER_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_SCATTER_SHOT_TWO_COMMAND,
                SCATTER_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_WILD_SHOT_ONE_COMMAND,
                WILD_SHOT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_WILD_SHOT_TWO_COMMAND,
                WILD_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_LEG_SHOT_TWO_COMMAND,
                LEG_SHOT_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_LEG_SHOT_THREE_COMMAND,
                LEG_SHOT_THREE_COMMAND);
        boolean riflemanSkillsRestored =
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_MASTER,
                RIFLEMAN_MASTER) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SUPPORT_FOUR,
                RIFLEMAN_SUPPORT_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SUPPORT_THREE,
                RIFLEMAN_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SUPPORT_TWO,
                RIFLEMAN_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SUPPORT_ONE,
                RIFLEMAN_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ABILITY_FOUR,
                RIFLEMAN_ABILITY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ABILITY_THREE,
                RIFLEMAN_ABILITY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ABILITY_TWO,
                RIFLEMAN_ABILITY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ABILITY_ONE,
                RIFLEMAN_ABILITY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SPEED_FOUR,
                RIFLEMAN_SPEED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SPEED_THREE,
                RIFLEMAN_SPEED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SPEED_TWO,
                RIFLEMAN_SPEED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_SPEED_ONE,
                RIFLEMAN_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ACCURACY_FOUR,
                RIFLEMAN_ACCURACY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ACCURACY_THREE,
                RIFLEMAN_ACCURACY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ACCURACY_TWO,
                RIFLEMAN_ACCURACY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_ACCURACY_ONE,
                RIFLEMAN_ACCURACY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLEMAN_NOVICE,
                RIFLEMAN_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_NOVICE,
                BRAWLER_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_ONE_HAND_ONE,
                BRAWLER_ONE_HAND_ONE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_ONE_HAND_TWO,
                BRAWLER_ONE_HAND_TWO) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_ONE_HAND_THREE,
                BRAWLER_ONE_HAND_THREE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_ONE_HAND_FOUR,
                BRAWLER_ONE_HAND_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_NOVICE,
                ONE_HAND_SWORD_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE,
                ONE_HAND_SWORD_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO,
                ONE_HAND_SWORD_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE,
                ONE_HAND_SWORD_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR,
                ONE_HAND_SWORD_SUPPORT_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE,
                ONE_HAND_SWORD_ACCURACY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO,
                ONE_HAND_SWORD_ACCURACY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE,
                ONE_HAND_SWORD_ACCURACY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR,
                ONE_HAND_SWORD_ACCURACY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE,
                ONE_HAND_SWORD_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO,
                ONE_HAND_SWORD_SPEED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE,
                ONE_HAND_SWORD_SPEED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR,
                ONE_HAND_SWORD_SPEED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE,
                ONE_HAND_SWORD_ABILITY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO,
                ONE_HAND_SWORD_ABILITY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE,
                ONE_HAND_SWORD_ABILITY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR,
                ONE_HAND_SWORD_ABILITY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_ONE_HAND_SWORD_MASTER,
                ONE_HAND_SWORD_MASTER) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_TWO_HAND_ONE,
                BRAWLER_TWO_HAND_ONE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_TWO_HAND_TWO,
                BRAWLER_TWO_HAND_TWO) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_TWO_HAND_THREE,
                BRAWLER_TWO_HAND_THREE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_TWO_HAND_FOUR,
                BRAWLER_TWO_HAND_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_NOVICE,
                TWO_HAND_SWORD_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE,
                TWO_HAND_SWORD_ACCURACY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO,
                TWO_HAND_SWORD_ACCURACY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE,
                TWO_HAND_SWORD_ACCURACY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR,
                TWO_HAND_SWORD_ACCURACY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE,
                TWO_HAND_SWORD_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO,
                TWO_HAND_SWORD_SPEED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE,
                TWO_HAND_SWORD_SPEED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR,
                TWO_HAND_SWORD_SPEED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE,
                TWO_HAND_SWORD_ABILITY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO,
                TWO_HAND_SWORD_ABILITY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE,
                TWO_HAND_SWORD_ABILITY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR,
                TWO_HAND_SWORD_ABILITY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE,
                TWO_HAND_SWORD_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO,
                TWO_HAND_SWORD_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE,
                TWO_HAND_SWORD_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR,
                TWO_HAND_SWORD_SUPPORT_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_TWO_HAND_SWORD_MASTER,
                TWO_HAND_SWORD_MASTER) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_POLEARM_ONE,
                BRAWLER_POLEARM_ONE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_POLEARM_TWO,
                BRAWLER_POLEARM_TWO) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_POLEARM_THREE,
                BRAWLER_POLEARM_THREE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_POLEARM_FOUR,
                BRAWLER_POLEARM_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_NOVICE,
                POLEARM_NOVICE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ACCURACY_ONE,
                POLEARM_ACCURACY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ACCURACY_TWO,
                POLEARM_ACCURACY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ACCURACY_THREE,
                POLEARM_ACCURACY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ACCURACY_FOUR,
                POLEARM_ACCURACY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SPEED_ONE,
                POLEARM_SPEED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SPEED_TWO,
                POLEARM_SPEED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SPEED_THREE,
                POLEARM_SPEED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SPEED_FOUR,
                POLEARM_SPEED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ABILITY_ONE,
                POLEARM_ABILITY_ONE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ABILITY_TWO,
                POLEARM_ABILITY_TWO) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ABILITY_THREE,
                POLEARM_ABILITY_THREE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_ABILITY_FOUR,
                POLEARM_ABILITY_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SUPPORT_ONE,
                POLEARM_SUPPORT_ONE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SUPPORT_TWO,
                POLEARM_SUPPORT_TWO) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SUPPORT_THREE,
                POLEARM_SUPPORT_THREE) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_SUPPORT_FOUR,
                POLEARM_SUPPORT_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_POLEARM_MASTER,
                POLEARM_MASTER) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_UNARMED_ONE,
                BRAWLER_UNARMED_ONE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_UNARMED_TWO,
                BRAWLER_UNARMED_TWO) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_UNARMED_THREE,
                BRAWLER_UNARMED_THREE) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_UNARMED_FOUR,
                BRAWLER_UNARMED_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_MASTER,
                BRAWLER_MASTER) &&
            isSkillRestored(attacker, ORIGINAL_BRAWLER_ROOT,
                BRAWLER_ROOT) &&
            isSkillRestored(attacker, ORIGINAL_RIFLE_FOUR, RIFLE_FOUR) &&
            isSkillRestored(attacker, ORIGINAL_RIFLE_THREE, RIFLE_THREE) &&
            isSkillRestored(attacker, ORIGINAL_RIFLE_TWO, RIFLE_TWO) &&
            isSkillRestored(attacker, ORIGINAL_RIFLE_ONE, RIFLE_ONE);
        boolean polearmAccuracyCommandsRestored =
            isCommandRestored(attacker, ORIGINAL_POLEARM_STUN_TWO_COMMAND,
                POLEARM_STUN_TWO_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_SPIN_TWO_COMMAND,
                POLEARM_SPIN_TWO_COMMAND);
        boolean polearmSpeedCommandsRestored =
            isCommandRestored(attacker, ORIGINAL_POLEARM_AREA_TWO_COMMAND,
                POLEARM_AREA_TWO_COMMAND);
        boolean polearmAbilityCommandsRestored =
            isCommandRestored(attacker, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND,
                POLEARM_SWEEP_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND,
                POLEARM_SWEEP_TWO_COMMAND);
        boolean polearmSupportCommandsRestored =
            isCommandRestored(attacker, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND,
                POLEARM_ACTION_HIT_ONE_COMMAND) &&
            isCommandRestored(attacker, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND,
                POLEARM_ACTION_HIT_TWO_COMMAND);
        boolean polearmMasterCommandRestored =
            isCommandRestored(attacker, ORIGINAL_POLEARM_HIT_THREE_COMMAND,
                POLEARM_HIT_THREE_COMMAND);
        boolean restored = fixtureWeaponDestroyed && fixturePistolDestroyed &&
            fixtureCarbineDestroyed &&
            fixturePolearmDestroyed &&
            fixtureOneHandDestroyed && fixtureTwoHandDestroyed &&
            fixtureAcidDestroyed && fixtureFlameDestroyed &&
            fixtureLightningDestroyed && fixtureConcealTargetDestroyed &&
            attackerRestored && defenderRestored &&
            pistolSkillsRestored && pistolCommandsRestored &&
            marksmanTumbleSkillsRestored && marksmanTumbleCommandsRestored &&
            carbineSkillsRestored && carbineCommandsRestored &&
            riflemanSkillsRestored && polearmAccuracyCommandsRestored &&
            polearmSpeedCommandsRestored && polearmAbilityCommandsRestored &&
            polearmSupportCommandsRestored && polearmMasterCommandRestored;
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureWeapon",
            fixtureWeaponDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixturePistol",
            fixturePistolDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureCarbine",
            fixtureCarbineDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixturePolearm",
            fixturePolearmDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureOneHand",
            fixtureOneHandDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureTwoHand",
            fixtureTwoHandDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureAcid",
            fixtureAcidDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureFlame",
            fixtureFlameDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.fixtureLightning",
            fixtureLightningDestroyed ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.attackerRestored",
            attackerRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.defenderRestored",
            defenderRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.pistolSkills",
            pistolSkillsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.pistolCommands",
            pistolCommandsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.marksmanTumbleSkills",
            marksmanTumbleSkillsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.marksmanTumbleCommands",
            marksmanTumbleCommandsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.carbineSkills",
            carbineSkillsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.carbineCommands",
            carbineCommandsRestored ? 1 : 0);
        setObjVar(attacker,
            "precu.p14.marksmanTier1Fixture.liveDiagnostic.cleanup.riflemanSkills",
            riflemanSkillsRestored ? 1 : 0);
        if (!restored)
        {
            return false;
        }

        removeObjVar(attacker, ROOT);
        removeObjVar(defender, ROOT);
        return !hasObjVar(attacker, ROOT) && !hasObjVar(defender, ROOT);
    }

    private void restoreCommand(obj_id player, String originalObjVar,
        String commandName) throws InterruptedException
    {
        if (hasObjVar(player, originalObjVar) &&
            getIntObjVar(player, originalObjVar) == 0 &&
            hasCommand(player, commandName))
        {
            revokeCommand(player, commandName);
        }
    }

    private void restoreSkill(obj_id player, String originalObjVar,
        String skillName) throws InterruptedException
    {
        if (hasObjVar(player, originalObjVar) &&
            getIntObjVar(player, originalObjVar) == 0 &&
            hasSkill(player, skillName))
        {
            revokeSkillSilent(player, skillName);
        }
    }

    private boolean isSkillRestored(obj_id player, String originalObjVar,
        String skillName) throws InterruptedException
    {
        return getIntObjVar(player, originalObjVar) != 0 ||
            !hasSkill(player, skillName);
    }

    private boolean isCommandRestored(obj_id player, String originalObjVar,
        String commandName) throws InterruptedException
    {
        return getIntObjVar(player, originalObjVar) != 0 ||
            !hasCommand(player, commandName);
    }

    private void retryCarbineRestoration(obj_id attacker)
        throws InterruptedException
    {
        restoreSkill(attacker, ORIGINAL_SUPPORT_TWO, MARKSMAN_SUPPORT_TWO);
        restoreSkill(attacker, ORIGINAL_SUPPORT_ONE, MARKSMAN_SUPPORT_ONE);
        restoreCommand(attacker, ORIGINAL_TUMBLE_TO_STANDING_COMMAND,
            TUMBLE_TO_STANDING_COMMAND);
        restoreCommand(attacker, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND,
            TUMBLE_TO_KNEELING_COMMAND);
        restoreCommand(attacker, ORIGINAL_TUMBLE_TO_PRONE_COMMAND,
            TUMBLE_TO_PRONE_COMMAND);
        restoreSkill(attacker, ORIGINAL_PISTOL_SUPPORT_THREE,
            PISTOL_SUPPORT_THREE);
        restoreSkill(attacker, ORIGINAL_PISTOL_SUPPORT_TWO,
            PISTOL_SUPPORT_TWO);
        restoreSkill(attacker, ORIGINAL_PISTOL_SUPPORT_ONE,
            PISTOL_SUPPORT_ONE);
        restoreSkill(attacker, ORIGINAL_PISTOL_NOVICE, PISTOL_NOVICE);
        restoreSkill(attacker, ORIGINAL_PISTOL_FOUR, MARKSMAN_PISTOL_FOUR);
        restoreSkill(attacker, ORIGINAL_PISTOL_THREE, MARKSMAN_PISTOL_THREE);
        restoreSkill(attacker, ORIGINAL_PISTOL_TWO, MARKSMAN_PISTOL_TWO);
        restoreSkill(attacker, ORIGINAL_PISTOL_ONE, MARKSMAN_PISTOL_ONE);
        restoreCommand(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND,
            PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND,
            PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_HEALTH_SHOT_TWO_COMMAND,
            HEALTH_SHOT_TWO_COMMAND);
        restoreSkill(attacker, ORIGINAL_CARBINE_ABILITY_FOUR,
            CARBINE_ABILITY_FOUR);
        restoreSkill(attacker, ORIGINAL_CARBINE_ABILITY_THREE,
            CARBINE_ABILITY_THREE);
        restoreSkill(attacker, ORIGINAL_CARBINE_ABILITY_TWO,
            CARBINE_ABILITY_TWO);
        restoreSkill(attacker, ORIGINAL_CARBINE_ABILITY_ONE,
            CARBINE_ABILITY_ONE);
        restoreSkill(attacker, ORIGINAL_CARBINE_SPEED_ONE,
            CARBINE_SPEED_ONE);
        restoreSkill(attacker, ORIGINAL_CARBINE_ACCURACY_FOUR,
            CARBINE_ACCURACY_FOUR);
        restoreSkill(attacker, ORIGINAL_CARBINE_ACCURACY_THREE,
            CARBINE_ACCURACY_THREE);
        restoreSkill(attacker, ORIGINAL_CARBINE_ACCURACY_TWO,
            CARBINE_ACCURACY_TWO);
        restoreSkill(attacker, ORIGINAL_CARBINE_ACCURACY_ONE,
            CARBINE_ACCURACY_ONE);
        restoreSkill(attacker, ORIGINAL_CARBINE_SUPPORT_FOUR,
            CARBINE_SUPPORT_FOUR);
        restoreSkill(attacker, ORIGINAL_CARBINE_SUPPORT_THREE,
            CARBINE_SUPPORT_THREE);
        restoreSkill(attacker, ORIGINAL_CARBINE_SUPPORT_TWO,
            CARBINE_SUPPORT_TWO);
        restoreSkill(attacker, ORIGINAL_CARBINE_SUPPORT_ONE,
            CARBINE_SUPPORT_ONE);
        restoreSkill(attacker, ORIGINAL_CARBINE_NOVICE, CARBINE_NOVICE);
        restoreSkill(attacker, ORIGINAL_CARBINE_FOUR, MARKSMAN_CARBINE_FOUR);
        restoreSkill(attacker, ORIGINAL_CARBINE_THREE, MARKSMAN_CARBINE_THREE);
        restoreSkill(attacker, ORIGINAL_CARBINE_TWO, MARKSMAN_CARBINE_TWO);
        restoreSkill(attacker, ORIGINAL_CARBINE_ONE, MARKSMAN_CARBINE_ONE);
        restoreCommand(attacker, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND,
            SUPPRESSION_FIRE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND,
            FULL_AUTO_SINGLE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND,
            FULL_AUTO_SINGLE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND,
            FULL_AUTO_AREA_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND,
            FULL_AUTO_AREA_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_ONE_COMMAND,
            CHARGE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_CHARGE_SHOT_TWO_COMMAND,
            CHARGE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_ONE_COMMAND,
            STRAFE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_STRAFE_SHOT_TWO_COMMAND,
            STRAFE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_ONE_COMMAND,
            STARTLE_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_STARTLE_SHOT_TWO_COMMAND,
            STARTLE_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND,
            FLUSHING_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND,
            FLUSHING_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND,
            POLEARM_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND,
            UNARMED_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND,
            ONE_HAND_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND,
            TWO_HAND_LUNGE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND,
            POLEARM_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND,
            UNARMED_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND,
            ONE_HAND_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND,
            TWO_HAND_LUNGE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TAUNT_COMMAND, TAUNT_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND,
            ONE_HAND_DIZZY_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND,
            ONE_HAND_BLIND_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND,
            ONE_HAND_BLIND_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND,
            ONE_HAND_SCATTER_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND,
            ONE_HAND_DIZZY_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND,
            ONE_HAND_SCATTER_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND,
            ONE_HAND_HEALTH_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND,
            ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND,
            ONE_HAND_HEALTH_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND,
            TWO_HAND_SWEEP_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND,
            TWO_HAND_SWEEP_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND,
            TWO_HAND_MIND_HIT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND,
            TWO_HAND_MIND_HIT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND,
            TWO_HAND_HIT_THREE_COMMAND);
        restoreCommand(attacker, ORIGINAL_POLEARM_STUN_ONE_COMMAND,
            POLEARM_STUN_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_BLIND_ONE_COMMAND,
            UNARMED_BLIND_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_UNARMED_STUN_ONE_COMMAND,
            UNARMED_STUN_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_INTIMIDATE_ONE_COMMAND,
            INTIMIDATE_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_INTIMIDATE_TWO_COMMAND,
            INTIMIDATE_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_WARCRY_ONE_COMMAND,
            WARCRY_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_WARCRY_TWO_COMMAND,
            WARCRY_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_MIND_SHOT_TWO_COMMAND,
            MIND_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_SURPRISE_SHOT_COMMAND,
            SURPRISE_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_SNIPER_SHOT_COMMAND,
            SNIPER_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_CONCEAL_SHOT_COMMAND,
            CONCEAL_SHOT_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_ONE_COMMAND,
            FLURRY_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_FLURRY_SHOT_TWO_COMMAND,
            FLURRY_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_ONE_COMMAND,
            SCATTER_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_SCATTER_SHOT_TWO_COMMAND,
            SCATTER_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_WILD_SHOT_ONE_COMMAND,
            WILD_SHOT_ONE_COMMAND);
        restoreCommand(attacker, ORIGINAL_WILD_SHOT_TWO_COMMAND,
            WILD_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_LEG_SHOT_TWO_COMMAND,
            LEG_SHOT_TWO_COMMAND);
        restoreCommand(attacker, ORIGINAL_LEG_SHOT_THREE_COMMAND,
            LEG_SHOT_THREE_COMMAND);
    }

    private boolean clearProofStateBuffs(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_DIZZY_BUFF) ||
            !hasObjVar(player, ORIGINAL_BLIND_BUFF) ||
            !hasObjVar(player, ORIGINAL_STUN_BUFF) ||
            !hasObjVar(player, ORIGINAL_INTIMIDATE_BUFF) ||
            getIntObjVar(player, ORIGINAL_DIZZY_BUFF) != 0 ||
            getIntObjVar(player, ORIGINAL_BLIND_BUFF) != 0 ||
            getIntObjVar(player, ORIGINAL_STUN_BUFF) != 0 ||
            getIntObjVar(player, ORIGINAL_INTIMIDATE_BUFF) != 0)
        {
            return false;
        }
        if (buff.hasBuff(player, "dizzy"))
        {
            buff.removeBuff(player, "dizzy");
        }
        if (buff.hasBuff(player, "blind"))
        {
            buff.removeBuff(player, "blind");
        }
        if (buff.hasBuff(player, "stun"))
        {
            buff.removeBuff(player, "stun");
        }
        if (buff.hasBuff(player, "intimidate"))
        {
            buff.removeBuff(player, "intimidate");
        }
        return !buff.hasBuff(player, "dizzy") &&
            !buff.hasBuff(player, "blind") &&
            !buff.hasBuff(player, "stun") &&
            !buff.hasBuff(player, "intimidate") &&
            getState(player, STATE_DIZZY) == 0 &&
            getState(player, STATE_BLINDED) == 0 &&
            getState(player, STATE_STUNNED) == 0 &&
            getState(player, STATE_INTIMIDATED) == 0;
    }

    private boolean restoreProofStateBuffs(obj_id player)
        throws InterruptedException
    {
        boolean restored = true;
        if (getIntObjVar(player, ORIGINAL_DIZZY_BUFF) == 0 &&
            buff.hasBuff(player, "dizzy"))
        {
            buff.removeBuff(player, "dizzy");
        }
        if (getIntObjVar(player, ORIGINAL_BLIND_BUFF) == 0 &&
            buff.hasBuff(player, "blind"))
        {
            buff.removeBuff(player, "blind");
        }
        if (getIntObjVar(player, ORIGINAL_STUN_BUFF) == 0 &&
            buff.hasBuff(player, "stun"))
        {
            buff.removeBuff(player, "stun");
        }
        if (getIntObjVar(player, ORIGINAL_INTIMIDATE_BUFF) == 0 &&
            buff.hasBuff(player, "intimidate"))
        {
            buff.removeBuff(player, "intimidate");
        }
        restored &= getIntObjVar(player, ORIGINAL_DIZZY_BUFF) != 0 ||
            (!buff.hasBuff(player, "dizzy") &&
                getState(player, STATE_DIZZY) == 0);
        restored &= getIntObjVar(player, ORIGINAL_BLIND_BUFF) != 0 ||
            (!buff.hasBuff(player, "blind") &&
                getState(player, STATE_BLINDED) == 0);
        restored &= getIntObjVar(player, ORIGINAL_STUN_BUFF) != 0 ||
            (!buff.hasBuff(player, "stun") &&
                getState(player, STATE_STUNNED) == 0);
        restored &= getIntObjVar(player, ORIGINAL_INTIMIDATE_BUFF) != 0 ||
            (!buff.hasBuff(player, "intimidate") &&
                getState(player, STATE_INTIMIDATED) == 0);
        return restored;
    }

    private boolean restorePlayer(obj_id player) throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        boolean moved = setLocation(player, getLocationObjVar(player, ORIGINAL_LOCATION));
        boolean locomotionRestored =
            setLocomotion(player, getIntObjVar(player, ORIGINAL_LOCOMOTION));
        boolean postureRestored =
            setPostureClientImmediate(player, getIntObjVar(player, ORIGINAL_POSTURE));
        setState(
            player, STATE_COVER, getIntObjVar(player, ORIGINAL_COVER_STATE) != 0);
        boolean coverStateRestored =
            getState(player, STATE_COVER) ==
                getIntObjVar(player, ORIGINAL_COVER_STATE);
        boolean stateBuffsRestored = restoreProofStateBuffs(player);
        setRegenRate(player, HEALTH, 0.0f);
        setRegenRate(player, ACTION, 0.0f);
        setRegenRate(player, MIND, 0.0f);
        boolean woundsRestored = restoreFixtureWounds(player);
        boolean maxHealthRestored =
            setMaxAttrib(player, HEALTH, getIntObjVar(player, ORIGINAL_MAX_HEALTH));
        boolean maxActionRestored =
            setMaxAttrib(player, ACTION, getIntObjVar(player, ORIGINAL_MAX_ACTION));
        boolean maxMindRestored =
            setMaxAttrib(player, MIND, getIntObjVar(player, ORIGINAL_MAX_MIND));
        boolean healthRestored =
            setAttribAndVerify(player, HEALTH, getIntObjVar(player, ORIGINAL_HEALTH));
        boolean actionRestored =
            setAttribAndVerify(player, ACTION, getIntObjVar(player, ORIGINAL_ACTION));
        boolean mindRestored =
            setAttribAndVerify(player, MIND, getIntObjVar(player, ORIGINAL_MIND));
        if (!healthRestored || !actionRestored || !mindRestored)
        {
            healthRestored =
                setAttribAndVerify(player, HEALTH,
                    getIntObjVar(player, ORIGINAL_HEALTH));
            actionRestored =
                setAttribAndVerify(player, ACTION,
                    getIntObjVar(player, ORIGINAL_ACTION));
            mindRestored =
                setAttribAndVerify(player, MIND,
                    getIntObjVar(player, ORIGINAL_MIND));
        }
        healthRestored = healthRestored ||
            isNaturalRegenerationRestored(
                player, HEALTH, ORIGINAL_HEALTH, ORIGINAL_MAX_HEALTH,
                ORIGINAL_HEALTH_REGEN);
        actionRestored = actionRestored ||
            isNaturalRegenerationRestored(
                player, ACTION, ORIGINAL_ACTION, ORIGINAL_MAX_ACTION,
                ORIGINAL_ACTION_REGEN);
        mindRestored = mindRestored ||
            isNaturalRegenerationRestored(
                player, MIND, ORIGINAL_MIND, ORIGINAL_MAX_MIND,
                ORIGINAL_MIND_REGEN);
        setRegenRate(player, HEALTH, getFloatObjVar(player, ORIGINAL_HEALTH_REGEN));
        setRegenRate(player, ACTION, getFloatObjVar(player, ORIGINAL_ACTION_REGEN));
        setRegenRate(player, MIND, getFloatObjVar(player, ORIGINAL_MIND_REGEN));
        if (getIntObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY_PRESENT) == 1)
        {
            utils.setScriptVar(
                player,
                combat_base.PRECU_POSTURE_DOWN_RECOVERY,
                getIntObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY));
        }
        else
        {
            utils.removeScriptVar(player, combat_base.PRECU_POSTURE_DOWN_RECOVERY);
        }
        if (getIntObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY_PRESENT) == 1)
        {
            utils.setScriptVar(
                player,
                combat_base.PRECU_POSTURE_UP_RECOVERY,
                getIntObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY));
        }
        else
        {
            utils.removeScriptVar(player, combat_base.PRECU_POSTURE_UP_RECOVERY);
        }
        if (getIntObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY_PRESENT) == 1)
        {
            utils.setScriptVar(
                player,
                combat_base.PRECU_KNOCKDOWN_RECOVERY,
                getIntObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY));
        }
        else
        {
            utils.removeScriptVar(player, combat_base.PRECU_KNOCKDOWN_RECOVERY);
        }
        if (getIntObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE_PRESENT) == 1)
        {
            utils.setScriptVar(
                player,
                combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE,
                getIntObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE));
        }
        else
        {
            utils.removeScriptVar(
                player, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE);
        }
        utils.removeScriptVar(
            player, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL);
        utils.removeScriptVar(
            player, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT);
        utils.removeScriptVar(
            player, combat_base.PRECU_NEXT_ATTACK_DELAY_REMAINING);
        setObjVar(player, ROOT + ".cleanup.moved", moved ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.postureRestored",
            postureRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.locomotionRestored",
            locomotionRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.coverStateRestored",
            coverStateRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.stateBuffsRestored",
            stateBuffsRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.maxHealthRestored",
            maxHealthRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.maxActionRestored",
            maxActionRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.maxMindRestored",
            maxMindRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.woundsRestored",
            woundsRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.healthRestored",
            healthRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.actionRestored",
            actionRestored ? 1 : 0);
        setObjVar(player, ROOT + ".cleanup.mindRestored",
            mindRestored ? 1 : 0);
        return moved && postureRestored && locomotionRestored &&
            coverStateRestored && stateBuffsRestored &&
            maxHealthRestored && maxActionRestored && maxMindRestored &&
            woundsRestored && healthRestored && actionRestored && mindRestored;
    }

    private boolean prepareFixtureHam(obj_id player) throws InterruptedException
    {
        boolean maxHealthReady =
            setMaxAttrib(player, HEALTH,
                Math.max(FIXTURE_HAM_MAXIMUM, getMaxAttrib(player, HEALTH)));
        boolean maxActionReady =
            setMaxAttrib(player, ACTION,
                Math.max(FIXTURE_HAM_MAXIMUM, getMaxAttrib(player, ACTION)));
        boolean maxMindReady =
            setMaxAttrib(player, MIND,
                Math.max(FIXTURE_HAM_MAXIMUM, getMaxAttrib(player, MIND)));
        return maxHealthReady && maxActionReady && maxMindReady &&
            setAttribAndVerify(player, HEALTH, getMaxAttrib(player, HEALTH)) &&
            setAttribAndVerify(player, ACTION, getMaxAttrib(player, ACTION)) &&
            setAttribAndVerify(player, MIND, getMaxAttrib(player, MIND));
    }

    private boolean restoreFixtureWounds(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_WOUNDS) ||
            !hasObjVar(player, ORIGINAL_SHOCK))
        {
            return false;
        }
        int[] originalWounds = getIntArrayObjVar(player, ORIGINAL_WOUNDS);
        if (originalWounds == null || originalWounds.length != NUM_ATTRIBUTES)
        {
            return false;
        }
        for (int attribute = 0; attribute < NUM_ATTRIBUTES; ++attribute)
        {
            int current = getAttribWound(player, attribute);
            int delta = current - originalWounds[attribute];
            if (current == ATTRIB_ERROR || delta < 0 ||
                (delta > 0 && healWound(player, attribute, delta) != delta) ||
                getAttribWound(player, attribute) != originalWounds[attribute])
            {
                return false;
            }
        }
        int originalShock = getIntObjVar(player, ORIGINAL_SHOCK);
        return setShockWound(player, originalShock) &&
            getShockWound(player) == originalShock;
    }

    private boolean setAttribAndVerify(obj_id player, int attrib, int value)
        throws InterruptedException
    {
        setAttrib(player, attrib, value);
        return getAttrib(player, attrib) == value;
    }

    private boolean isNaturalRegenerationRestored(
        obj_id player, int attrib, String originalValueObjVar,
        String originalMaximumObjVar, String originalRegenObjVar)
        throws InterruptedException
    {
        int value = getAttrib(player, attrib);
        int originalValue = getIntObjVar(player, originalValueObjVar);
        int originalMaximum = getIntObjVar(player, originalMaximumObjVar);
        return getFloatObjVar(player, originalRegenObjVar) > 0.0f &&
            value >= originalValue && value <= originalMaximum;
    }

    private boolean reassertPreparedState(obj_id attacker, obj_id defender)
        throws InterruptedException
    {
        stopCombat(attacker);
        stopCombat(defender);
        setCombatTarget(attacker, obj_id.NULL_ID);
        setCombatTarget(defender, obj_id.NULL_ID);
        boolean attackerStationary = setLocomotion(attacker, LOCOMOTION_STANDING);
        boolean defenderStationary = setLocomotion(defender, LOCOMOTION_STANDING);
        boolean attackerStanding = setPostureClientImmediate(attacker, POSTURE_UPRIGHT);
        boolean defenderStanding = setPostureClientImmediate(defender, POSTURE_UPRIGHT);
        setState(attacker, STATE_COVER, false);
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_MELEE_MODIFIER);
        removeAttribOrSkillModModifier(
            attacker, combat_base.PRECU_TUMBLE_RANGED_MODIFIER);
        setState(attacker, STATE_TUMBLING, false);
        boolean coverCleared = getState(attacker, STATE_COVER) == 0;
        boolean proofStateBuffsCleared = clearProofStateBuffs(defender);
        setRegenRate(attacker, HEALTH, 0.0f);
        setRegenRate(attacker, ACTION, 0.0f);
        setRegenRate(attacker, MIND, 0.0f);
        setRegenRate(defender, HEALTH, 0.0f);
        setRegenRate(defender, ACTION, 0.0f);
        setRegenRate(defender, MIND, 0.0f);
        pvpSetPermanentPersonalEnemyFlag(attacker, defender);
        pvpSetPermanentPersonalEnemyFlag(defender, attacker);
        boolean supportOneReady =
            hasSkill(attacker, MARKSMAN_SUPPORT_ONE) ||
            grantSkill(attacker, MARKSMAN_SUPPORT_ONE);
        boolean supportTwoReady = supportOneReady &&
            (hasSkill(attacker, MARKSMAN_SUPPORT_TWO) ||
                grantSkill(attacker, MARKSMAN_SUPPORT_TWO));
        boolean rifleOneReady =
            hasSkill(attacker, RIFLE_ONE) || grantSkill(attacker, RIFLE_ONE);
        boolean rifleTwoReady = rifleOneReady &&
            (hasSkill(attacker, RIFLE_TWO) || grantSkill(attacker, RIFLE_TWO));
        boolean rifleThreeReady = rifleTwoReady &&
            (hasSkill(attacker, RIFLE_THREE) ||
                grantSkill(attacker, RIFLE_THREE));
        boolean rifleFourReady = rifleThreeReady &&
            (hasSkill(attacker, RIFLE_FOUR) ||
                grantSkill(attacker, RIFLE_FOUR));
        boolean riflemanNoviceReady = rifleFourReady &&
            (hasSkill(attacker, RIFLEMAN_NOVICE) ||
                grantSkill(attacker, RIFLEMAN_NOVICE));
        boolean brawlerRootReady = hasSkill(attacker, BRAWLER_ROOT) ||
            grantSkill(attacker, BRAWLER_ROOT);
        boolean brawlerNoviceReady = brawlerRootReady &&
            (hasSkill(attacker, BRAWLER_NOVICE) ||
                grantSkill(attacker, BRAWLER_NOVICE));
        boolean brawlerOneHandOneReady = brawlerNoviceReady &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_ONE) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_ONE));
        boolean brawlerOneHandTwoReady = brawlerOneHandOneReady &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_TWO) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_TWO));
        boolean brawlerOneHandThreeReady = brawlerOneHandTwoReady &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_THREE) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_THREE));
        boolean brawlerOneHandFourReady = brawlerOneHandThreeReady &&
            (hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) ||
                grantSkill(attacker, BRAWLER_ONE_HAND_FOUR));
        boolean oneHandSwordNoviceReady = brawlerOneHandFourReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_NOVICE) ||
                grantSkill(attacker, ONE_HAND_SWORD_NOVICE));
        boolean oneHandSwordSupportOneReady = oneHandSwordNoviceReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE));
        boolean oneHandSwordSupportTwoReady = oneHandSwordSupportOneReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO));
        boolean oneHandSwordSupportThreeReady = oneHandSwordSupportTwoReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE));
        boolean oneHandSwordSupportFourReady = oneHandSwordSupportThreeReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR));
        boolean oneHandSwordAccuracyOneReady = oneHandSwordNoviceReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE));
        boolean oneHandSwordAccuracyTwoReady = oneHandSwordAccuracyOneReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO));
        boolean oneHandSwordAccuracyThreeReady = oneHandSwordAccuracyTwoReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE));
        boolean oneHandSwordAccuracyFourReady = oneHandSwordAccuracyThreeReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR));
        boolean oneHandSwordSpeedOneReady = oneHandSwordNoviceReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_ONE));
        boolean oneHandSwordSpeedTwoReady = oneHandSwordSpeedOneReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_TWO));
        boolean oneHandSwordSpeedThreeReady = oneHandSwordSpeedTwoReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_THREE));
        boolean oneHandSwordSpeedFourReady = oneHandSwordSpeedThreeReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR));
        boolean oneHandSwordAbilityOneReady = oneHandSwordNoviceReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE));
        boolean oneHandSwordAbilityTwoReady = oneHandSwordAbilityOneReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO));
        boolean oneHandSwordAbilityThreeReady = oneHandSwordAbilityTwoReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE));
        boolean oneHandSwordAbilityFourReady = oneHandSwordAbilityThreeReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) ||
                grantSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR));
        boolean oneHandSwordMasterReady =
            oneHandSwordSupportFourReady &&
            oneHandSwordAccuracyFourReady &&
            oneHandSwordSpeedFourReady &&
            oneHandSwordAbilityFourReady &&
            (hasSkill(attacker, ONE_HAND_SWORD_MASTER) ||
                grantSkill(attacker, ONE_HAND_SWORD_MASTER));
        boolean brawlerTwoHandOneReady = brawlerNoviceReady &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_ONE) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_ONE));
        boolean brawlerTwoHandTwoReady = brawlerTwoHandOneReady &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_TWO) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_TWO));
        boolean brawlerTwoHandThreeReady = brawlerTwoHandTwoReady &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_THREE) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_THREE));
        boolean brawlerTwoHandFourReady = brawlerTwoHandThreeReady &&
            (hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) ||
                grantSkill(attacker, BRAWLER_TWO_HAND_FOUR));
        boolean twoHandSwordNoviceReady = brawlerTwoHandFourReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_NOVICE) ||
                grantSkill(attacker, TWO_HAND_SWORD_NOVICE));
        boolean twoHandSwordAccuracyOneReady = twoHandSwordNoviceReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE));
        boolean twoHandSwordAccuracyTwoReady =
            twoHandSwordAccuracyOneReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO));
        boolean twoHandSwordAccuracyThreeReady =
            twoHandSwordAccuracyTwoReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE));
        boolean twoHandSwordAccuracyFourReady =
            twoHandSwordAccuracyThreeReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR));
        boolean twoHandSwordSpeedOneReady = twoHandSwordNoviceReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_ONE));
        boolean twoHandSwordSpeedTwoReady = twoHandSwordSpeedOneReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_TWO));
        boolean twoHandSwordSpeedThreeReady = twoHandSwordSpeedTwoReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_THREE));
        boolean twoHandSwordSpeedFourReady = twoHandSwordSpeedThreeReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR));
        boolean twoHandSwordAbilityOneReady = twoHandSwordNoviceReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE));
        boolean twoHandSwordAbilityTwoReady = twoHandSwordAbilityOneReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO));
        boolean twoHandSwordAbilityThreeReady = twoHandSwordAbilityTwoReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE));
        boolean twoHandSwordAbilityFourReady = twoHandSwordAbilityThreeReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR));
        boolean twoHandSwordSupportOneReady = twoHandSwordNoviceReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE));
        boolean twoHandSwordSupportTwoReady = twoHandSwordSupportOneReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO));
        boolean twoHandSwordSupportThreeReady = twoHandSwordSupportTwoReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE));
        boolean twoHandSwordSupportFourReady = twoHandSwordSupportThreeReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) ||
                grantSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR));
        boolean twoHandSwordMasterReady =
            twoHandSwordAccuracyFourReady && twoHandSwordSpeedFourReady &&
            twoHandSwordAbilityFourReady && twoHandSwordSupportFourReady &&
            (hasSkill(attacker, TWO_HAND_SWORD_MASTER) ||
                grantSkill(attacker, TWO_HAND_SWORD_MASTER));
        boolean brawlerPolearmOneReady = brawlerNoviceReady &&
            (hasSkill(attacker, BRAWLER_POLEARM_ONE) ||
                grantSkill(attacker, BRAWLER_POLEARM_ONE));
        boolean brawlerPolearmTwoReady = brawlerPolearmOneReady &&
            (hasSkill(attacker, BRAWLER_POLEARM_TWO) ||
                grantSkill(attacker, BRAWLER_POLEARM_TWO));
        boolean brawlerPolearmThreeReady = brawlerPolearmTwoReady &&
            (hasSkill(attacker, BRAWLER_POLEARM_THREE) ||
                grantSkill(attacker, BRAWLER_POLEARM_THREE));
        boolean brawlerPolearmFourReady = brawlerPolearmThreeReady &&
            (hasSkill(attacker, BRAWLER_POLEARM_FOUR) ||
                grantSkill(attacker, BRAWLER_POLEARM_FOUR));
        boolean polearmNoviceReady = brawlerPolearmFourReady &&
            (hasSkill(attacker, POLEARM_NOVICE) ||
                grantSkill(attacker, POLEARM_NOVICE));
        boolean polearmAccuracyOneReady = polearmNoviceReady &&
            (hasSkill(attacker, POLEARM_ACCURACY_ONE) ||
                grantSkill(attacker, POLEARM_ACCURACY_ONE));
        boolean polearmAccuracyTwoReady = polearmAccuracyOneReady &&
            (hasSkill(attacker, POLEARM_ACCURACY_TWO) ||
                grantSkill(attacker, POLEARM_ACCURACY_TWO));
        boolean polearmAccuracyThreeReady = polearmAccuracyTwoReady &&
            (hasSkill(attacker, POLEARM_ACCURACY_THREE) ||
                grantSkill(attacker, POLEARM_ACCURACY_THREE));
        boolean polearmAccuracyFourReady = polearmAccuracyThreeReady &&
            (hasSkill(attacker, POLEARM_ACCURACY_FOUR) ||
                grantSkill(attacker, POLEARM_ACCURACY_FOUR));
        boolean polearmSpeedOneReady = polearmNoviceReady &&
            (hasSkill(attacker, POLEARM_SPEED_ONE) ||
                grantSkill(attacker, POLEARM_SPEED_ONE));
        boolean polearmSpeedTwoReady = polearmSpeedOneReady &&
            (hasSkill(attacker, POLEARM_SPEED_TWO) ||
                grantSkill(attacker, POLEARM_SPEED_TWO));
        boolean polearmSpeedThreeReady = polearmSpeedTwoReady &&
            (hasSkill(attacker, POLEARM_SPEED_THREE) ||
                grantSkill(attacker, POLEARM_SPEED_THREE));
        boolean polearmSpeedFourReady = polearmSpeedThreeReady &&
            (hasSkill(attacker, POLEARM_SPEED_FOUR) ||
                grantSkill(attacker, POLEARM_SPEED_FOUR));
        boolean polearmAbilityOneReady = polearmNoviceReady &&
            (hasSkill(attacker, POLEARM_ABILITY_ONE) ||
                grantSkill(attacker, POLEARM_ABILITY_ONE));
        boolean polearmAbilityTwoReady = polearmAbilityOneReady &&
            (hasSkill(attacker, POLEARM_ABILITY_TWO) ||
                grantSkill(attacker, POLEARM_ABILITY_TWO));
        boolean polearmAbilityThreeReady = polearmAbilityTwoReady &&
            (hasSkill(attacker, POLEARM_ABILITY_THREE) ||
                grantSkill(attacker, POLEARM_ABILITY_THREE));
        boolean polearmAbilityFourReady = polearmAbilityThreeReady &&
            (hasSkill(attacker, POLEARM_ABILITY_FOUR) ||
                grantSkill(attacker, POLEARM_ABILITY_FOUR));
        boolean polearmSupportOneReady = polearmNoviceReady &&
            (hasSkill(attacker, POLEARM_SUPPORT_ONE) ||
                grantSkill(attacker, POLEARM_SUPPORT_ONE));
        boolean polearmSupportTwoReady = polearmSupportOneReady &&
            (hasSkill(attacker, POLEARM_SUPPORT_TWO) ||
                grantSkill(attacker, POLEARM_SUPPORT_TWO));
        boolean polearmSupportThreeReady = polearmSupportTwoReady &&
            (hasSkill(attacker, POLEARM_SUPPORT_THREE) ||
                grantSkill(attacker, POLEARM_SUPPORT_THREE));
        boolean polearmSupportFourReady = polearmSupportThreeReady &&
            (hasSkill(attacker, POLEARM_SUPPORT_FOUR) ||
                grantSkill(attacker, POLEARM_SUPPORT_FOUR));
        boolean polearmMasterReady =
            polearmAccuracyFourReady && polearmSpeedFourReady &&
            polearmAbilityFourReady && polearmSupportFourReady &&
            (hasSkill(attacker, POLEARM_MASTER) ||
                grantSkill(attacker, POLEARM_MASTER));
        boolean brawlerUnarmedOneReady = brawlerNoviceReady &&
            (hasSkill(attacker, BRAWLER_UNARMED_ONE) ||
                grantSkill(attacker, BRAWLER_UNARMED_ONE));
        boolean brawlerUnarmedTwoReady = brawlerUnarmedOneReady &&
            (hasSkill(attacker, BRAWLER_UNARMED_TWO) ||
                grantSkill(attacker, BRAWLER_UNARMED_TWO));
        boolean brawlerUnarmedThreeReady = brawlerUnarmedTwoReady &&
            (hasSkill(attacker, BRAWLER_UNARMED_THREE) ||
                grantSkill(attacker, BRAWLER_UNARMED_THREE));
        boolean brawlerUnarmedFourReady = brawlerUnarmedThreeReady &&
            (hasSkill(attacker, BRAWLER_UNARMED_FOUR) ||
                grantSkill(attacker, BRAWLER_UNARMED_FOUR));
        boolean brawlerMasterReady =
            brawlerOneHandFourReady && brawlerTwoHandFourReady &&
            brawlerPolearmFourReady && brawlerUnarmedFourReady &&
            (hasSkill(attacker, BRAWLER_MASTER) ||
                grantSkill(attacker, BRAWLER_MASTER));
        boolean riflemanAccuracyOneReady = riflemanNoviceReady &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_ONE) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_ONE));
        boolean riflemanAccuracyTwoReady = riflemanAccuracyOneReady &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_TWO) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_TWO));
        boolean riflemanAccuracyThreeReady = riflemanAccuracyTwoReady &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_THREE) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_THREE));
        boolean riflemanAccuracyFourReady = riflemanAccuracyThreeReady &&
            (hasSkill(attacker, RIFLEMAN_ACCURACY_FOUR) ||
                grantSkill(attacker, RIFLEMAN_ACCURACY_FOUR));
        boolean riflemanSpeedOneReady = riflemanNoviceReady &&
            (hasSkill(attacker, RIFLEMAN_SPEED_ONE) ||
                grantSkill(attacker, RIFLEMAN_SPEED_ONE));
        boolean riflemanSpeedTwoReady = riflemanSpeedOneReady &&
            (hasSkill(attacker, RIFLEMAN_SPEED_TWO) ||
                grantSkill(attacker, RIFLEMAN_SPEED_TWO));
        boolean riflemanSpeedThreeReady = riflemanSpeedTwoReady &&
            (hasSkill(attacker, RIFLEMAN_SPEED_THREE) ||
                grantSkill(attacker, RIFLEMAN_SPEED_THREE));
        boolean riflemanSpeedFourReady = riflemanSpeedThreeReady &&
            (hasSkill(attacker, RIFLEMAN_SPEED_FOUR) ||
                grantSkill(attacker, RIFLEMAN_SPEED_FOUR));
        boolean riflemanAbilityOneReady = riflemanNoviceReady &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_ONE) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_ONE));
        boolean riflemanAbilityTwoReady = riflemanAbilityOneReady &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_TWO) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_TWO));
        boolean riflemanAbilityThreeReady = riflemanAbilityTwoReady &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_THREE) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_THREE));
        boolean riflemanAbilityFourReady = riflemanAbilityThreeReady &&
            (hasSkill(attacker, RIFLEMAN_ABILITY_FOUR) ||
                grantSkill(attacker, RIFLEMAN_ABILITY_FOUR));
        boolean riflemanSupportOneReady = riflemanNoviceReady &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_ONE) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_ONE));
        boolean riflemanSupportTwoReady = riflemanSupportOneReady &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_TWO) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_TWO));
        boolean riflemanSupportThreeReady = riflemanSupportTwoReady &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_THREE) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_THREE));
        boolean riflemanSupportFourReady = riflemanSupportThreeReady &&
            (hasSkill(attacker, RIFLEMAN_SUPPORT_FOUR) ||
                grantSkill(attacker, RIFLEMAN_SUPPORT_FOUR));
        boolean riflemanMasterReady = riflemanAccuracyFourReady &&
            riflemanSpeedFourReady && riflemanAbilityFourReady &&
            riflemanSupportFourReady &&
            (hasSkill(attacker, RIFLEMAN_MASTER) ||
                grantSkill(attacker, RIFLEMAN_MASTER));
        boolean carbineOneReady =
            hasSkill(attacker, MARKSMAN_CARBINE_ONE) ||
            grantSkill(attacker, MARKSMAN_CARBINE_ONE);
        boolean carbineTwoReady = carbineOneReady &&
            (hasSkill(attacker, MARKSMAN_CARBINE_TWO) ||
                grantSkill(attacker, MARKSMAN_CARBINE_TWO));
        boolean carbineThreeReady = carbineTwoReady &&
            (hasSkill(attacker, MARKSMAN_CARBINE_THREE) ||
                grantSkill(attacker, MARKSMAN_CARBINE_THREE));
        boolean carbineFourReady = carbineThreeReady &&
            (hasSkill(attacker, MARKSMAN_CARBINE_FOUR) ||
                grantSkill(attacker, MARKSMAN_CARBINE_FOUR));
        boolean carbineNoviceReady = carbineFourReady &&
            (hasSkill(attacker, CARBINE_NOVICE) ||
                grantSkill(attacker, CARBINE_NOVICE));
        boolean carbineAccuracyOneReady = carbineNoviceReady &&
            (hasSkill(attacker, CARBINE_ACCURACY_ONE) ||
                grantSkill(attacker, CARBINE_ACCURACY_ONE));
        boolean carbineAccuracyTwoReady = carbineAccuracyOneReady &&
            (hasSkill(attacker, CARBINE_ACCURACY_TWO) ||
                grantSkill(attacker, CARBINE_ACCURACY_TWO));
        boolean carbineAccuracyThreeReady = carbineAccuracyTwoReady &&
            (hasSkill(attacker, CARBINE_ACCURACY_THREE) ||
                grantSkill(attacker, CARBINE_ACCURACY_THREE));
        boolean carbineAccuracyFourReady = carbineAccuracyThreeReady &&
            (hasSkill(attacker, CARBINE_ACCURACY_FOUR) ||
                grantSkill(attacker, CARBINE_ACCURACY_FOUR));
        boolean carbineSupportOneReady = carbineNoviceReady &&
            (hasSkill(attacker, CARBINE_SUPPORT_ONE) ||
                grantSkill(attacker, CARBINE_SUPPORT_ONE));
        boolean carbineSupportTwoReady = carbineSupportOneReady &&
            (hasSkill(attacker, CARBINE_SUPPORT_TWO) ||
                grantSkill(attacker, CARBINE_SUPPORT_TWO));
        boolean carbineSupportThreeReady = carbineSupportTwoReady &&
            (hasSkill(attacker, CARBINE_SUPPORT_THREE) ||
                grantSkill(attacker, CARBINE_SUPPORT_THREE));
        boolean carbineSupportFourReady = carbineSupportThreeReady &&
            (hasSkill(attacker, CARBINE_SUPPORT_FOUR) ||
                grantSkill(attacker, CARBINE_SUPPORT_FOUR));
        boolean carbineSpeedOneReady = carbineNoviceReady &&
            (hasSkill(attacker, CARBINE_SPEED_ONE) ||
                grantSkill(attacker, CARBINE_SPEED_ONE));
        boolean carbineAbilityFourReady = carbineNoviceReady &&
            (hasSkill(attacker, CARBINE_ABILITY_FOUR) ||
                grantSkill(attacker, CARBINE_ABILITY_FOUR));
        boolean supportFourReady = supportTwoReady &&
            (hasSkill(attacker, MARKSMAN_SUPPORT_FOUR) ||
                grantSkill(attacker, MARKSMAN_SUPPORT_FOUR));
        boolean pistolOneReady =
            hasSkill(attacker, MARKSMAN_PISTOL_ONE) ||
            grantSkill(attacker, MARKSMAN_PISTOL_ONE);
        boolean pistolTwoReady = pistolOneReady &&
            (hasSkill(attacker, MARKSMAN_PISTOL_TWO) ||
                grantSkill(attacker, MARKSMAN_PISTOL_TWO));
        boolean pistolThreeReady = pistolTwoReady &&
            (hasSkill(attacker, MARKSMAN_PISTOL_THREE) ||
                grantSkill(attacker, MARKSMAN_PISTOL_THREE));
        boolean pistolFourReady = pistolThreeReady &&
            (hasSkill(attacker, MARKSMAN_PISTOL_FOUR) ||
                grantSkill(attacker, MARKSMAN_PISTOL_FOUR));
        boolean pistolNoviceReady = pistolFourReady &&
            (hasSkill(attacker, PISTOL_NOVICE) ||
                grantSkill(attacker, PISTOL_NOVICE));
        boolean pistolSupportOneReady = pistolNoviceReady &&
            (hasSkill(attacker, PISTOL_SUPPORT_ONE) ||
                grantSkill(attacker, PISTOL_SUPPORT_ONE));
        boolean pistolSupportTwoReady = pistolSupportOneReady &&
            (hasSkill(attacker, PISTOL_SUPPORT_TWO) ||
                grantSkill(attacker, PISTOL_SUPPORT_TWO));
        boolean pistolSupportThreeReady = pistolSupportTwoReady &&
            (hasSkill(attacker, PISTOL_SUPPORT_THREE) ||
                grantSkill(attacker, PISTOL_SUPPORT_THREE));
        boolean durationControlReady = hasCommand(attacker, DURATION_CONTROL_COMMAND) ||
            grantCommand(attacker, DURATION_CONTROL_COMMAND);
        boolean headShotThreeReady = hasCommand(attacker, HEAD_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, HEAD_SHOT_THREE_COMMAND);
        boolean bodyShotTwoReady = hasCommand(attacker, BODY_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, BODY_SHOT_TWO_COMMAND);
        boolean bodyShotThreeReady = hasCommand(attacker, BODY_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, BODY_SHOT_THREE_COMMAND);
        boolean healthShotOneReady =
            hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, HEALTH_SHOT_ONE_COMMAND);
        boolean healthShotTwoReady =
            hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, HEALTH_SHOT_TWO_COMMAND);
        boolean pistolMeleeDefenseOneReady =
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) ||
            grantCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        boolean pistolMeleeDefenseTwoReady =
            hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND) ||
            grantCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        boolean tumbleToProneReady =
            hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_PRONE_COMMAND);
        boolean tumbleToKneelingReady =
            hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_KNEELING_COMMAND);
        boolean tumbleToStandingReady =
            hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND) ||
            grantCommand(attacker, TUMBLE_TO_STANDING_COMMAND);
        boolean actionShotOneReady =
            hasCommand(attacker, ACTION_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, ACTION_SHOT_ONE_COMMAND);
        boolean actionShotTwoReady =
            hasCommand(attacker, ACTION_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, ACTION_SHOT_TWO_COMMAND);
        boolean mindShotOneReady =
            hasCommand(attacker, MIND_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, MIND_SHOT_ONE_COMMAND);
        boolean mindShotTwoReady =
            hasCommand(attacker, MIND_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, MIND_SHOT_TWO_COMMAND);
        boolean surpriseShotReady =
            hasCommand(attacker, SURPRISE_SHOT_COMMAND) ||
            grantCommand(attacker, SURPRISE_SHOT_COMMAND);
        boolean sniperShotReady =
            hasCommand(attacker, SNIPER_SHOT_COMMAND) ||
            grantCommand(attacker, SNIPER_SHOT_COMMAND);
        boolean concealShotReady =
            hasCommand(attacker, CONCEAL_SHOT_COMMAND) ||
            grantCommand(attacker, CONCEAL_SHOT_COMMAND);
        boolean flurryShotOneReady =
            hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, FLURRY_SHOT_ONE_COMMAND);
        boolean flurryShotTwoReady =
            hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, FLURRY_SHOT_TWO_COMMAND);
        boolean cdefCertificationReady = hasCommand(attacker, CDEF_CERTIFICATION) ||
            grantCommand(attacker, CDEF_CERTIFICATION);
        boolean pistolCdefCertificationReady =
            hasCommand(attacker, PISTOL_CDEF_CERTIFICATION) ||
            grantCommand(attacker, PISTOL_CDEF_CERTIFICATION);
        boolean carbineCdefCertificationReady =
            hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) ||
            grantCommand(attacker, CARBINE_CDEF_CERTIFICATION);
        boolean polearmCommandReady = hasCommand(attacker, POLEARM_COMMAND) ||
            grantCommand(attacker, POLEARM_COMMAND);
        boolean polearmLegTwoCommandReady =
            hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_LEG_TWO_COMMAND);
        boolean polearmLegThreeCommandReady =
            hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) ||
            grantCommand(attacker, POLEARM_LEG_THREE_COMMAND);
        boolean polearmHitOneCommandReady =
            hasCommand(attacker, POLEARM_HIT_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_ONE_COMMAND);
        boolean polearmHitTwoCommandReady =
            hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_TWO_COMMAND);
        boolean polearmStunTwoCommandReady =
            hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_STUN_TWO_COMMAND);
        boolean polearmSpinTwoCommandReady =
            hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_SPIN_TWO_COMMAND);
        boolean polearmAreaOneCommandReady =
            hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_ONE_COMMAND);
        boolean polearmAreaTwoCommandReady =
            hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_TWO_COMMAND);
        boolean polearmSweepOneCommandReady =
            hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_SWEEP_ONE_COMMAND);
        boolean polearmSweepTwoCommandReady =
            hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_SWEEP_TWO_COMMAND);
        boolean polearmActionHitOneCommandReady =
            hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND);
        boolean polearmActionHitTwoCommandReady =
            hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND);
        boolean polearmHitThreeCommandReady =
            hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) ||
            grantCommand(attacker, POLEARM_HIT_THREE_COMMAND);
        boolean unarmedCommandReady = hasCommand(attacker, UNARMED_COMMAND) ||
            grantCommand(attacker, UNARMED_COMMAND);
        boolean unarmedHitOneCommandReady =
            hasCommand(attacker, UNARMED_HIT_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_HIT_ONE_COMMAND);
        boolean unarmedHitTwoCommandReady =
            hasCommand(attacker, UNARMED_HIT_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_HIT_TWO_COMMAND);
        boolean unarmedBodyOneCommandReady =
            hasCommand(attacker, UNARMED_BODY_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_BODY_ONE_COMMAND);
        boolean unarmedLegOneCommandReady =
            hasCommand(attacker, UNARMED_LEG_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_LEG_ONE_COMMAND);
        boolean unarmedSpinOneCommandReady =
            hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_SPIN_ONE_COMMAND);
        boolean unarmedSpinTwoCommandReady =
            hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_SPIN_TWO_COMMAND);
        boolean overchargeOneCommandReady =
            hasCommand(attacker, OVERCHARGE_ONE_COMMAND) ||
            grantCommand(attacker, OVERCHARGE_ONE_COMMAND);
        boolean overchargeTwoCommandReady =
            hasCommand(attacker, OVERCHARGE_TWO_COMMAND) ||
            grantCommand(attacker, OVERCHARGE_TWO_COMMAND);
        boolean pointBlankSingleOneCommandReady =
            hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND);
        boolean aimCommandReady =
            hasCommand(attacker, AIM_COMMAND) ||
            grantCommand(attacker, AIM_COMMAND);
        boolean threatenShotCommandReady =
            hasCommand(attacker, THREATEN_SHOT_COMMAND) ||
            grantCommand(attacker, THREATEN_SHOT_COMMAND);
        boolean warningShotCommandReady =
            hasCommand(attacker, WARNING_SHOT_COMMAND) ||
            grantCommand(attacker, WARNING_SHOT_COMMAND);
        boolean suppressionFireOneCommandReady =
            hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND) ||
            grantCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND);
        boolean suppressionFireTwoCommandReady =
            hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND) ||
            grantCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND);
        boolean rollShotCommandReady =
            hasCommand(attacker, ROLL_SHOT_COMMAND) ||
            grantCommand(attacker, ROLL_SHOT_COMMAND);
        boolean diveShotCommandReady =
            hasCommand(attacker, DIVE_SHOT_COMMAND) ||
            grantCommand(attacker, DIVE_SHOT_COMMAND);
        boolean kipUpShotCommandReady =
            hasCommand(attacker, KIP_UP_SHOT_COMMAND) ||
            grantCommand(attacker, KIP_UP_SHOT_COMMAND);
        boolean takeCoverCommandReady =
            hasCommand(attacker, TAKE_COVER_COMMAND) ||
            grantCommand(attacker, TAKE_COVER_COMMAND);
        boolean fullAutoSingleOneCommandReady =
            hasCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND);
        boolean fullAutoSingleTwoCommandReady =
            hasCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND);
        boolean fullAutoAreaOneCommandReady =
            hasCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND);
        boolean fullAutoAreaTwoCommandReady =
            hasCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND) ||
            grantCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND);
        boolean chargeShotOneCommandReady =
            hasCommand(attacker, CHARGE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, CHARGE_SHOT_ONE_COMMAND);
        boolean chargeShotTwoCommandReady =
            hasCommand(attacker, CHARGE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, CHARGE_SHOT_TWO_COMMAND);
        boolean strafeShotOneCommandReady =
            hasCommand(attacker, STRAFE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, STRAFE_SHOT_ONE_COMMAND);
        boolean strafeShotTwoCommandReady =
            hasCommand(attacker, STRAFE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, STRAFE_SHOT_TWO_COMMAND);
        boolean startleShotOneCommandReady =
            hasCommand(attacker, STARTLE_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, STARTLE_SHOT_ONE_COMMAND);
        boolean startleShotTwoCommandReady =
            hasCommand(attacker, STARTLE_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, STARTLE_SHOT_TWO_COMMAND);
        boolean flushingShotOneCommandReady =
            hasCommand(attacker, FLUSHING_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, FLUSHING_SHOT_ONE_COMMAND);
        boolean flushingShotTwoCommandReady =
            hasCommand(attacker, FLUSHING_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, FLUSHING_SHOT_TWO_COMMAND);
        boolean polearmLungeOneCommandReady =
            hasCommand(attacker, POLEARM_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_LUNGE_ONE_COMMAND);
        boolean unarmedLungeOneCommandReady =
            hasCommand(attacker, UNARMED_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_LUNGE_ONE_COMMAND);
        boolean oneHandLungeOneCommandReady =
            hasCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND);
        boolean twoHandLungeOneCommandReady =
            hasCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND);
        boolean polearmLungeTwoCommandReady =
            hasCommand(attacker, POLEARM_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, POLEARM_LUNGE_TWO_COMMAND);
        boolean unarmedLungeTwoCommandReady =
            hasCommand(attacker, UNARMED_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, UNARMED_LUNGE_TWO_COMMAND);
        boolean oneHandLungeTwoCommandReady =
            hasCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND);
        boolean twoHandLungeTwoCommandReady =
            hasCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND);
        boolean tauntCommandReady =
            hasCommand(attacker, TAUNT_COMMAND) ||
            grantCommand(attacker, TAUNT_COMMAND);
        boolean oneHandDizzyHitOneCommandReady =
            hasCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND);
        boolean oneHandBlindHitOneCommandReady =
            hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND);
        boolean oneHandBlindHitTwoCommandReady =
            hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND);
        boolean oneHandScatterHitOneCommandReady =
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND);
        boolean oneHandDizzyHitTwoCommandReady =
            hasCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND);
        boolean oneHandScatterHitTwoCommandReady =
            hasCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND);
        boolean oneHandHealthHitOneCommandReady =
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND);
        boolean oneHandSpinAttackTwoCommandReady =
            hasCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
        boolean oneHandHealthHitTwoCommandReady =
            hasCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND);
        boolean twoHandSweepOneCommandReady =
            hasCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND);
        boolean twoHandSweepTwoCommandReady =
            hasCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND);
        boolean twoHandMindHitOneCommandReady =
            hasCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND);
        boolean twoHandMindHitTwoCommandReady =
            hasCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND);
        boolean twoHandHitThreeCommandReady =
            hasCommand(attacker, TWO_HAND_HIT_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_THREE_COMMAND);
        boolean polearmStunOneCommandReady =
            hasCommand(attacker, POLEARM_STUN_ONE_COMMAND) ||
            grantCommand(attacker, POLEARM_STUN_ONE_COMMAND);
        boolean unarmedBlindOneCommandReady =
            hasCommand(attacker, UNARMED_BLIND_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_BLIND_ONE_COMMAND);
        boolean unarmedStunOneCommandReady =
            hasCommand(attacker, UNARMED_STUN_ONE_COMMAND) ||
            grantCommand(attacker, UNARMED_STUN_ONE_COMMAND);
        boolean intimidateOneCommandReady =
            hasCommand(attacker, INTIMIDATE_ONE_COMMAND) ||
            grantCommand(attacker, INTIMIDATE_ONE_COMMAND);
        boolean intimidateTwoCommandReady =
            hasCommand(attacker, INTIMIDATE_TWO_COMMAND) ||
            grantCommand(attacker, INTIMIDATE_TWO_COMMAND);
        boolean warcryOneCommandReady =
            hasCommand(attacker, WARCRY_ONE_COMMAND) ||
            grantCommand(attacker, WARCRY_ONE_COMMAND);
        boolean warcryTwoCommandReady =
            hasCommand(attacker, WARCRY_TWO_COMMAND) ||
            grantCommand(attacker, WARCRY_TWO_COMMAND);
        boolean scatterShotOneCommandReady =
            hasCommand(attacker, SCATTER_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, SCATTER_SHOT_ONE_COMMAND);
        boolean scatterShotTwoCommandReady =
            hasCommand(attacker, SCATTER_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, SCATTER_SHOT_TWO_COMMAND);
        boolean wildShotOneCommandReady =
            hasCommand(attacker, WILD_SHOT_ONE_COMMAND) ||
            grantCommand(attacker, WILD_SHOT_ONE_COMMAND);
        boolean wildShotTwoCommandReady =
            hasCommand(attacker, WILD_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, WILD_SHOT_TWO_COMMAND);
        boolean legShotTwoCommandReady =
            hasCommand(attacker, LEG_SHOT_TWO_COMMAND) ||
            grantCommand(attacker, LEG_SHOT_TWO_COMMAND);
        boolean legShotThreeCommandReady =
            hasCommand(attacker, LEG_SHOT_THREE_COMMAND) ||
            grantCommand(attacker, LEG_SHOT_THREE_COMMAND);
        boolean acidSingleOneCommandReady =
            hasCommand(attacker, ACID_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, ACID_SINGLE_ONE_COMMAND);
        boolean acidConeOneCommandReady =
            hasCommand(attacker, ACID_CONE_ONE_COMMAND) ||
            grantCommand(attacker, ACID_CONE_ONE_COMMAND);
        boolean acidConeTwoCommandReady =
            hasCommand(attacker, ACID_CONE_TWO_COMMAND) ||
            grantCommand(attacker, ACID_CONE_TWO_COMMAND);
        boolean acidSingleTwoCommandReady =
            hasCommand(attacker, ACID_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, ACID_SINGLE_TWO_COMMAND);
        boolean flameSingleOneCommandReady =
            hasCommand(attacker, FLAME_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, FLAME_SINGLE_ONE_COMMAND);
        boolean flameSingleTwoCommandReady =
            hasCommand(attacker, FLAME_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, FLAME_SINGLE_TWO_COMMAND);
        boolean flameConeOneCommandReady =
            hasCommand(attacker, FLAME_CONE_ONE_COMMAND) ||
            grantCommand(attacker, FLAME_CONE_ONE_COMMAND);
        boolean flameConeTwoCommandReady =
            hasCommand(attacker, FLAME_CONE_TWO_COMMAND) ||
            grantCommand(attacker, FLAME_CONE_TWO_COMMAND);
        boolean lightningSingleOneCommandReady =
            hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND) ||
            grantCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND);
        boolean lightningConeOneCommandReady =
            hasCommand(attacker, LIGHTNING_CONE_ONE_COMMAND) ||
            grantCommand(attacker, LIGHTNING_CONE_ONE_COMMAND);
        boolean lightningConeTwoCommandReady =
            hasCommand(attacker, LIGHTNING_CONE_TWO_COMMAND) ||
            grantCommand(attacker, LIGHTNING_CONE_TWO_COMMAND);
        boolean lightningSingleTwoCommandReady =
            hasCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND) ||
            grantCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND);
        boolean polearmAreaCommandReady =
            hasCommand(attacker, POLEARM_AREA_COMMAND) ||
            grantCommand(attacker, POLEARM_AREA_COMMAND);
        boolean oneHandAreaCommandReady =
            hasCommand(attacker, ONE_HAND_AREA_COMMAND) ||
            grantCommand(attacker, ONE_HAND_AREA_COMMAND);
        boolean oneHandBodyOneCommandReady =
            hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_ONE_COMMAND);
        boolean oneHandBodyTwoCommandReady =
            hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_TWO_COMMAND);
        boolean oneHandBodyThreeCommandReady =
            hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_BODY_THREE_COMMAND);
        boolean oneHandHitOneCommandReady =
            hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_ONE_COMMAND);
        boolean oneHandHitTwoCommandReady =
            hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_TWO_COMMAND);
        boolean oneHandHitThreeCommandReady =
            hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) ||
            grantCommand(attacker, ONE_HAND_HIT_THREE_COMMAND);
        boolean twoHandAreaCommandReady =
            hasCommand(attacker, TWO_HAND_AREA_COMMAND) ||
            grantCommand(attacker, TWO_HAND_AREA_COMMAND);
        boolean twoHandAreaTwoCommandReady =
            hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_AREA_TWO_COMMAND);
        boolean twoHandAccuracyAreaOneCommandReady =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND);
        boolean twoHandAccuracyAreaTwoCommandReady =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND);
        boolean twoHandAccuracyAreaThreeCommandReady =
            hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND);
        boolean twoHandHeadOneCommandReady =
            hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND);
        boolean twoHandHeadTwoCommandReady =
            hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND);
        boolean twoHandHeadThreeCommandReady =
            hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND);
        boolean twoHandHitOneCommandReady =
            hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_ONE_COMMAND);
        boolean twoHandHitTwoCommandReady =
            hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND) ||
            grantCommand(attacker, TWO_HAND_HIT_TWO_COMMAND);
        boolean polearmCertificationReady =
            hasCommand(attacker, POLEARM_CERTIFICATION) ||
            grantCommand(attacker, POLEARM_CERTIFICATION);
        boolean oneHandCertificationReady =
            hasCommand(attacker, ONE_HAND_CERTIFICATION) ||
            grantCommand(attacker, ONE_HAND_CERTIFICATION);
        boolean twoHandCertificationReady =
            hasCommand(attacker, TWO_HAND_CERTIFICATION) ||
            grantCommand(attacker, TWO_HAND_CERTIFICATION);
        boolean acidCertificationReady =
            hasCommand(attacker, ACID_CERTIFICATION) ||
            grantCommand(attacker, ACID_CERTIFICATION);
        boolean flameCertificationReady =
            hasCommand(attacker, FLAME_CERTIFICATION) ||
            grantCommand(attacker, FLAME_CERTIFICATION);
        boolean lightningCertificationReady =
            hasCommand(attacker, LIGHTNING_CERTIFICATION) ||
            grantCommand(attacker, LIGHTNING_CERTIFICATION);
        return rifleOneReady && rifleTwoReady && rifleThreeReady &&
            rifleFourReady && riflemanNoviceReady &&
            brawlerRootReady && brawlerNoviceReady &&
            brawlerOneHandOneReady && brawlerOneHandTwoReady &&
            brawlerOneHandThreeReady && brawlerOneHandFourReady &&
            oneHandSwordNoviceReady && oneHandSwordSupportOneReady &&
            oneHandSwordSupportTwoReady && oneHandSwordSupportThreeReady &&
            brawlerTwoHandOneReady && brawlerTwoHandTwoReady &&
            brawlerTwoHandThreeReady && brawlerTwoHandFourReady &&
            twoHandSwordNoviceReady && twoHandSwordAccuracyOneReady &&
            twoHandSwordAccuracyTwoReady && twoHandSwordAccuracyThreeReady &&
            twoHandSwordAccuracyFourReady && twoHandSwordSpeedOneReady &&
            twoHandSwordSpeedTwoReady && twoHandSwordSpeedThreeReady &&
            twoHandSwordSpeedFourReady && twoHandSwordAbilityOneReady &&
            twoHandSwordAbilityTwoReady && twoHandSwordAbilityThreeReady &&
            twoHandSwordAbilityFourReady && twoHandSwordSupportOneReady &&
            twoHandSwordSupportTwoReady && twoHandSwordSupportThreeReady &&
            twoHandSwordSupportFourReady && twoHandSwordMasterReady &&
            brawlerPolearmOneReady && brawlerPolearmTwoReady &&
            brawlerPolearmThreeReady && brawlerPolearmFourReady &&
            polearmNoviceReady && polearmAccuracyOneReady &&
            polearmAccuracyTwoReady && polearmAccuracyThreeReady &&
            polearmAccuracyFourReady &&
            polearmSpeedOneReady && polearmSpeedTwoReady &&
            polearmSpeedThreeReady && polearmSpeedFourReady &&
            polearmAbilityOneReady && polearmAbilityTwoReady &&
            polearmAbilityThreeReady && polearmAbilityFourReady &&
            polearmSupportOneReady && polearmSupportTwoReady &&
            polearmSupportThreeReady && polearmSupportFourReady &&
            polearmMasterReady &&
            brawlerUnarmedOneReady && brawlerUnarmedTwoReady &&
            brawlerUnarmedThreeReady && brawlerUnarmedFourReady &&
            brawlerMasterReady &&
            riflemanAccuracyOneReady && riflemanAccuracyTwoReady &&
            riflemanAccuracyThreeReady && riflemanAccuracyFourReady &&
            riflemanSpeedOneReady && riflemanSpeedTwoReady &&
            riflemanSpeedThreeReady && riflemanSpeedFourReady &&
            riflemanAbilityOneReady && riflemanAbilityTwoReady &&
            riflemanAbilityThreeReady && riflemanAbilityFourReady &&
            riflemanSupportOneReady && riflemanSupportTwoReady &&
            riflemanSupportThreeReady && riflemanSupportFourReady &&
            riflemanMasterReady &&
            carbineOneReady && carbineTwoReady &&
            carbineThreeReady && carbineFourReady &&
            carbineNoviceReady && carbineAccuracyOneReady &&
            carbineAccuracyTwoReady && carbineAccuracyThreeReady &&
            carbineAccuracyFourReady &&
            carbineSupportOneReady &&
            carbineSupportTwoReady &&
            carbineSupportThreeReady &&
            carbineSupportFourReady &&
            carbineSpeedOneReady &&
            carbineAbilityFourReady &&
            supportOneReady && supportTwoReady && supportFourReady &&
            pistolOneReady && pistolTwoReady && pistolThreeReady &&
            pistolFourReady && pistolNoviceReady && pistolSupportOneReady &&
            pistolSupportTwoReady && pistolSupportThreeReady &&
            durationControlReady && headShotThreeReady &&
            bodyShotTwoReady && bodyShotThreeReady &&
            healthShotOneReady && healthShotTwoReady &&
            pistolMeleeDefenseOneReady && pistolMeleeDefenseTwoReady &&
            tumbleToProneReady && tumbleToKneelingReady &&
            tumbleToStandingReady &&
            actionShotOneReady && actionShotTwoReady &&
            mindShotOneReady && mindShotTwoReady && surpriseShotReady &&
            sniperShotReady && concealShotReady && flurryShotOneReady &&
            flurryShotTwoReady &&
            cdefCertificationReady && pistolCdefCertificationReady &&
            carbineCdefCertificationReady &&
            polearmCommandReady && polearmLegTwoCommandReady &&
            polearmLegThreeCommandReady && polearmHitOneCommandReady &&
            polearmHitTwoCommandReady && polearmStunTwoCommandReady &&
            polearmSpinTwoCommandReady &&
            polearmAreaOneCommandReady && polearmAreaTwoCommandReady &&
            polearmSweepOneCommandReady && polearmSweepTwoCommandReady &&
            polearmActionHitOneCommandReady &&
            polearmActionHitTwoCommandReady && polearmHitThreeCommandReady &&
            unarmedCommandReady &&
            unarmedHitOneCommandReady && unarmedHitTwoCommandReady &&
            unarmedBodyOneCommandReady && unarmedLegOneCommandReady &&
            unarmedSpinOneCommandReady && unarmedSpinTwoCommandReady &&
            overchargeOneCommandReady && overchargeTwoCommandReady &&
            pointBlankSingleOneCommandReady && aimCommandReady &&
            threatenShotCommandReady &&
            warningShotCommandReady && suppressionFireOneCommandReady &&
            suppressionFireTwoCommandReady &&
            rollShotCommandReady && diveShotCommandReady &&
            kipUpShotCommandReady && takeCoverCommandReady &&
            fullAutoSingleOneCommandReady &&
            fullAutoSingleTwoCommandReady &&
            fullAutoAreaOneCommandReady &&
            fullAutoAreaTwoCommandReady &&
            chargeShotOneCommandReady &&
            chargeShotTwoCommandReady &&
            strafeShotOneCommandReady &&
            strafeShotTwoCommandReady &&
            startleShotOneCommandReady &&
            startleShotTwoCommandReady &&
            flushingShotOneCommandReady &&
            flushingShotTwoCommandReady &&
            polearmLungeOneCommandReady &&
            unarmedLungeOneCommandReady &&
            oneHandLungeOneCommandReady &&
            twoHandLungeOneCommandReady &&
            polearmLungeTwoCommandReady &&
            unarmedLungeTwoCommandReady &&
            oneHandLungeTwoCommandReady &&
            twoHandLungeTwoCommandReady &&
            tauntCommandReady &&
            oneHandDizzyHitOneCommandReady &&
            oneHandBlindHitOneCommandReady &&
            oneHandBlindHitTwoCommandReady &&
            oneHandSwordSupportFourReady &&
            oneHandSwordAccuracyOneReady &&
            oneHandSwordAccuracyTwoReady &&
            oneHandSwordAccuracyThreeReady &&
            oneHandSwordAccuracyFourReady &&
            oneHandScatterHitOneCommandReady &&
            oneHandDizzyHitTwoCommandReady &&
            oneHandScatterHitTwoCommandReady &&
            oneHandSwordSpeedOneReady &&
            oneHandSwordSpeedTwoReady &&
            oneHandSwordSpeedThreeReady &&
            oneHandSwordSpeedFourReady &&
            oneHandSwordAbilityOneReady &&
            oneHandSwordAbilityTwoReady &&
            oneHandSwordAbilityThreeReady &&
            oneHandSwordAbilityFourReady &&
            oneHandSwordMasterReady &&
            oneHandHealthHitOneCommandReady &&
            oneHandSpinAttackTwoCommandReady &&
            oneHandHealthHitTwoCommandReady &&
            twoHandSweepOneCommandReady &&
            twoHandSweepTwoCommandReady &&
            twoHandMindHitOneCommandReady &&
            twoHandMindHitTwoCommandReady && twoHandHitThreeCommandReady &&
            polearmStunOneCommandReady &&
            unarmedBlindOneCommandReady &&
            unarmedStunOneCommandReady &&
            intimidateOneCommandReady &&
            intimidateTwoCommandReady &&
            warcryOneCommandReady &&
            warcryTwoCommandReady &&
            scatterShotOneCommandReady && scatterShotTwoCommandReady &&
            wildShotOneCommandReady && wildShotTwoCommandReady &&
            legShotTwoCommandReady && legShotThreeCommandReady &&
            acidSingleOneCommandReady &&
            acidConeOneCommandReady && acidConeTwoCommandReady &&
            acidSingleTwoCommandReady && flameSingleOneCommandReady &&
            flameSingleTwoCommandReady && flameConeOneCommandReady &&
            flameConeTwoCommandReady && lightningSingleOneCommandReady &&
            lightningConeOneCommandReady && lightningConeTwoCommandReady &&
            lightningSingleTwoCommandReady &&
            polearmAreaCommandReady && oneHandAreaCommandReady &&
            oneHandBodyOneCommandReady &&
            oneHandBodyTwoCommandReady && oneHandBodyThreeCommandReady &&
            oneHandHitOneCommandReady && oneHandHitTwoCommandReady &&
            oneHandHitThreeCommandReady &&
            twoHandAreaCommandReady && twoHandAreaTwoCommandReady &&
            twoHandAccuracyAreaOneCommandReady &&
            twoHandAccuracyAreaTwoCommandReady &&
            twoHandAccuracyAreaThreeCommandReady &&
            twoHandHeadOneCommandReady &&
            twoHandHeadTwoCommandReady && twoHandHeadThreeCommandReady &&
            twoHandHitOneCommandReady && twoHandHitTwoCommandReady &&
            polearmCertificationReady &&
            oneHandCertificationReady && twoHandCertificationReady &&
            acidCertificationReady && flameCertificationReady &&
            lightningCertificationReady &&
            attackerStanding && defenderStanding &&
            coverCleared && proofStateBuffsCleared &&
            attackerStationary && defenderStationary &&
            getPosture(attacker) == POSTURE_UPRIGHT &&
            getPosture(defender) == POSTURE_UPRIGHT &&
            getLocomotion(attacker) == LOCOMOTION_STANDING &&
            getLocomotion(defender) == LOCOMOTION_STANDING &&
            getRegenRate(attacker, HEALTH) == 0.0f &&
            getRegenRate(attacker, ACTION) == 0.0f &&
            getRegenRate(attacker, MIND) == 0.0f &&
            getRegenRate(defender, HEALTH) == 0.0f &&
            getRegenRate(defender, ACTION) == 0.0f &&
            getRegenRate(defender, MIND) == 0.0f;
    }

    private boolean destroyFixtureWeapon(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_WEAPON))
        {
            return true;
        }
        obj_id fixtureWeapon = getObjIdObjVar(attacker, FIXTURE_WEAPON);
        if (!isIdValid(fixtureWeapon))
        {
            removeObjVar(attacker, FIXTURE_WEAPON);
            return true;
        }
        if (!CDEF_TEMPLATE.equals(getTemplateName(fixtureWeapon)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureWeapon);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_WEAPON);
        }
        return destroyed;
    }

    private boolean destroyFixturePistol(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_PISTOL))
        {
            return true;
        }
        obj_id fixturePistol = getObjIdObjVar(attacker, FIXTURE_PISTOL);
        if (!isIdValid(fixturePistol))
        {
            removeObjVar(attacker, FIXTURE_PISTOL);
            return true;
        }
        if (!CDEF_PISTOL_TEMPLATE.equals(getTemplateName(fixturePistol)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixturePistol);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_PISTOL);
        }
        return destroyed;
    }

    private boolean destroyFixtureCarbine(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_CARBINE))
        {
            return true;
        }
        obj_id fixtureCarbine = getObjIdObjVar(attacker, FIXTURE_CARBINE);
        if (!isIdValid(fixtureCarbine))
        {
            removeObjVar(attacker, FIXTURE_CARBINE);
            return true;
        }
        if (!CDEF_CARBINE_TEMPLATE.equals(getTemplateName(fixtureCarbine)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureCarbine);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_CARBINE);
        }
        return destroyed;
    }

    private boolean destroyFixturePolearm(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_POLEARM))
        {
            return true;
        }
        obj_id fixturePolearm = getObjIdObjVar(attacker, FIXTURE_POLEARM);
        if (!isIdValid(fixturePolearm))
        {
            removeObjVar(attacker, FIXTURE_POLEARM);
            return true;
        }
        if (!POLEARM_TEMPLATE.equals(getTemplateName(fixturePolearm)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixturePolearm);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_POLEARM);
        }
        return destroyed;
    }

    private boolean destroyFixtureOneHand(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_ONE_HAND))
        {
            return true;
        }
        obj_id fixtureOneHand = getObjIdObjVar(attacker, FIXTURE_ONE_HAND);
        if (!isIdValid(fixtureOneHand))
        {
            removeObjVar(attacker, FIXTURE_ONE_HAND);
            return true;
        }
        if (!ONE_HAND_TEMPLATE.equals(getTemplateName(fixtureOneHand)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureOneHand);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_ONE_HAND);
        }
        return destroyed;
    }

    private boolean destroyFixtureTwoHand(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_TWO_HAND))
        {
            return true;
        }
        obj_id fixtureTwoHand = getObjIdObjVar(attacker, FIXTURE_TWO_HAND);
        if (!isIdValid(fixtureTwoHand))
        {
            removeObjVar(attacker, FIXTURE_TWO_HAND);
            return true;
        }
        if (!TWO_HAND_TEMPLATE.equals(getTemplateName(fixtureTwoHand)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureTwoHand);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_TWO_HAND);
        }
        return destroyed;
    }

    private boolean destroyFixtureAcid(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_ACID))
        {
            return true;
        }
        obj_id fixtureAcid = getObjIdObjVar(attacker, FIXTURE_ACID);
        if (!isIdValid(fixtureAcid))
        {
            removeObjVar(attacker, FIXTURE_ACID);
            return true;
        }
        if (!ACID_TEMPLATE.equals(getTemplateName(fixtureAcid)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureAcid);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_ACID);
        }
        return destroyed;
    }

    private boolean destroyFixtureFlame(obj_id attacker)
        throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_FLAME))
        {
            return true;
        }
        obj_id fixtureFlame = getObjIdObjVar(attacker, FIXTURE_FLAME);
        if (!isIdValid(fixtureFlame))
        {
            removeObjVar(attacker, FIXTURE_FLAME);
            return true;
        }
        if (!FLAME_TEMPLATE.equals(getTemplateName(fixtureFlame)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureFlame);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_FLAME);
        }
        return destroyed;
    }

    private boolean destroyFixtureLightning(obj_id attacker) throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_LIGHTNING))
        {
            return true;
        }
        obj_id fixtureLightning = getObjIdObjVar(attacker, FIXTURE_LIGHTNING);
        if (!isIdValid(fixtureLightning))
        {
            removeObjVar(attacker, FIXTURE_LIGHTNING);
            return true;
        }
        if (!LIGHTNING_TEMPLATE.equals(getTemplateName(fixtureLightning)))
        {
            return false;
        }
        boolean destroyed = destroyObject(fixtureLightning);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_LIGHTNING);
        }
        return destroyed;
    }

    private boolean destroyFixtureConcealTarget(obj_id attacker)
        throws InterruptedException
    {
        if (!hasObjVar(attacker, FIXTURE_CONCEAL_TARGET))
        {
            return true;
        }
        obj_id target = getObjIdObjVar(attacker, FIXTURE_CONCEAL_TARGET);
        if (!isIdValid(target) || !exists(target))
        {
            removeObjVar(attacker, FIXTURE_CONCEAL_TARGET);
            return true;
        }
        if (!hasObjVar(target, CONCEAL_TARGET_OWNED) ||
            getIntObjVar(target, CONCEAL_TARGET_OWNED) != 1 ||
            !hasObjVar(target, CONCEAL_TARGET_OWNER) ||
            getObjIdObjVar(target, CONCEAL_TARGET_OWNER) != attacker)
        {
            return false;
        }
        stopCombat(target);
        clearHateList(target);
        pvpRemovePersonalEnemyFlags(target, attacker);
        pvpRemovePersonalEnemyFlags(attacker, target);
        obj_id defender = obj_id.getObjId(DEFENDER_OID);
        if (isIdValid(defender) && exists(defender))
        {
            pvpRemovePersonalEnemyFlags(target, defender);
            pvpRemovePersonalEnemyFlags(defender, target);
        }
        pvpSetAttackableOverride(target, false);
        boolean destroyed = destroyObject(target);
        if (destroyed)
        {
            removeObjVar(attacker, FIXTURE_CONCEAL_TARGET);
        }
        return destroyed;
    }

    private boolean hasCompleteSnapshot(obj_id player) throws InterruptedException
    {
        return hasObjVar(player, ORIGINAL_LOCATION) &&
            hasObjVar(player, ORIGINAL_POSTURE) &&
            hasObjVar(player, ORIGINAL_LOCOMOTION) &&
            hasObjVar(player, ORIGINAL_COVER_STATE) &&
            hasObjVar(player, ORIGINAL_DIZZY_BUFF) &&
            hasObjVar(player, ORIGINAL_BLIND_BUFF) &&
            hasObjVar(player, ORIGINAL_STUN_BUFF) &&
            hasObjVar(player, ORIGINAL_INTIMIDATE_BUFF) &&
            hasObjVar(player, ORIGINAL_HEALTH) &&
            hasObjVar(player, ORIGINAL_MAX_HEALTH) &&
            hasObjVar(player, ORIGINAL_STRENGTH) &&
            hasObjVar(player, ORIGINAL_ACTION) &&
            hasObjVar(player, ORIGINAL_MAX_ACTION) &&
            hasObjVar(player, ORIGINAL_QUICKNESS) &&
            hasObjVar(player, ORIGINAL_MIND) &&
            hasObjVar(player, ORIGINAL_MAX_MIND) &&
            hasObjVar(player, ORIGINAL_FOCUS) &&
            hasObjVar(player, ORIGINAL_WOUNDS) &&
            hasObjVar(player, ORIGINAL_SHOCK) &&
            hasObjVar(player, ORIGINAL_HEALTH_REGEN) &&
            hasObjVar(player, ORIGINAL_ACTION_REGEN) &&
            hasObjVar(player, ORIGINAL_MIND_REGEN) &&
            hasObjVar(player, ORIGINAL_NOVICE) &&
            hasObjVar(player, ORIGINAL_RIFLE_ONE) &&
            hasObjVar(player, ORIGINAL_RIFLE_TWO) &&
            hasObjVar(player, ORIGINAL_RIFLE_THREE) &&
            hasObjVar(player, ORIGINAL_RIFLE_FOUR) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_NOVICE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_ROOT) &&
            hasObjVar(player, ORIGINAL_BRAWLER_NOVICE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_ONE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_TWO) &&
            hasObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_THREE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_ONE_HAND_FOUR) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_NOVICE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_ONE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_TWO) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_THREE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ACCURACY_FOUR) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_TWO) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_THREE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_SPEED_FOUR) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_ONE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_TWO) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_THREE) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_ABILITY_FOUR) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SWORD_MASTER) &&
            hasObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_ONE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_TWO) &&
            hasObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_THREE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_TWO_HAND_FOUR) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_NOVICE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_ONE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_TWO) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_THREE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ACCURACY_FOUR) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_TWO) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_THREE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SPEED_FOUR) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_ONE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_TWO) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_THREE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_ABILITY_FOUR) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWORD_MASTER) &&
            hasObjVar(player, ORIGINAL_BRAWLER_POLEARM_ONE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_POLEARM_TWO) &&
            hasObjVar(player, ORIGINAL_BRAWLER_POLEARM_THREE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_POLEARM_FOUR) &&
            hasObjVar(player, ORIGINAL_POLEARM_NOVICE) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACCURACY_ONE) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACCURACY_TWO) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACCURACY_THREE) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACCURACY_FOUR) &&
            hasObjVar(player, ORIGINAL_POLEARM_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_POLEARM_SPEED_TWO) &&
            hasObjVar(player, ORIGINAL_POLEARM_SPEED_THREE) &&
            hasObjVar(player, ORIGINAL_POLEARM_SPEED_FOUR) &&
            hasObjVar(player, ORIGINAL_POLEARM_ABILITY_ONE) &&
            hasObjVar(player, ORIGINAL_POLEARM_ABILITY_TWO) &&
            hasObjVar(player, ORIGINAL_POLEARM_ABILITY_THREE) &&
            hasObjVar(player, ORIGINAL_POLEARM_ABILITY_FOUR) &&
            hasObjVar(player, ORIGINAL_POLEARM_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_POLEARM_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_POLEARM_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_POLEARM_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_POLEARM_MASTER) &&
            hasObjVar(player, ORIGINAL_BRAWLER_UNARMED_ONE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_UNARMED_TWO) &&
            hasObjVar(player, ORIGINAL_BRAWLER_UNARMED_THREE) &&
            hasObjVar(player, ORIGINAL_BRAWLER_UNARMED_FOUR) &&
            hasObjVar(player, ORIGINAL_BRAWLER_MASTER) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_ONE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_TWO) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_THREE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ACCURACY_FOUR) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SPEED_TWO) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SPEED_THREE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SPEED_FOUR) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_ONE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_TWO) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_THREE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_ABILITY_FOUR) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_RIFLEMAN_MASTER) &&
            hasObjVar(player, ORIGINAL_CARBINE_ONE) &&
            hasObjVar(player, ORIGINAL_CARBINE_TWO) &&
            hasObjVar(player, ORIGINAL_CARBINE_THREE) &&
            hasObjVar(player, ORIGINAL_CARBINE_FOUR) &&
            hasObjVar(player, ORIGINAL_CARBINE_NOVICE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ACCURACY_ONE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ACCURACY_TWO) &&
            hasObjVar(player, ORIGINAL_CARBINE_ACCURACY_THREE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ACCURACY_FOUR) &&
            hasObjVar(player, ORIGINAL_CARBINE_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_CARBINE_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_CARBINE_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_CARBINE_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_CARBINE_SPEED_ONE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_ONE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_TWO) &&
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_THREE) &&
            hasObjVar(player, ORIGINAL_CARBINE_ABILITY_FOUR) &&
            hasObjVar(player, ORIGINAL_PISTOL_ONE) &&
            hasObjVar(player, ORIGINAL_PISTOL_TWO) &&
            hasObjVar(player, ORIGINAL_PISTOL_THREE) &&
            hasObjVar(player, ORIGINAL_PISTOL_FOUR) &&
            hasObjVar(player, ORIGINAL_PISTOL_NOVICE) &&
            hasObjVar(player, ORIGINAL_PISTOL_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_PISTOL_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_PISTOL_SUPPORT_THREE) &&
            hasObjVar(player, ORIGINAL_SUPPORT_ONE) &&
            hasObjVar(player, ORIGINAL_SUPPORT_TWO) &&
            hasObjVar(player, ORIGINAL_SUPPORT_FOUR) &&
            hasObjVar(player, ORIGINAL_DURATION_CONTROL) &&
            hasObjVar(player, ORIGINAL_HEAD_SHOT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_BODY_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_BODY_SHOT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_HEALTH_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_HEALTH_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_PISTOL_MELEE_DEFENSE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_PISTOL_MELEE_DEFENSE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TUMBLE_TO_PRONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TUMBLE_TO_KNEELING_COMMAND) &&
            hasObjVar(player, ORIGINAL_TUMBLE_TO_STANDING_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACTION_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACTION_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_MIND_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_MIND_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_SURPRISE_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_SNIPER_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_CONCEAL_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLURRY_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLURRY_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_CDEF_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_PISTOL_CDEF_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_CARBINE_CDEF_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_POLEARM_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_LEG_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_LEG_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_STUN_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_SPIN_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_AREA_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_AREA_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_SWEEP_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_SWEEP_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACTION_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_ACTION_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_HIT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_BODY_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_LEG_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_SPIN_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_SPIN_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_OVERCHARGE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_OVERCHARGE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POINT_BLANK_SINGLE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_AIM_COMMAND) &&
            hasObjVar(player, ORIGINAL_THREATEN_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_WARNING_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_SUPPRESSION_FIRE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_SUPPRESSION_FIRE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ROLL_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_DIVE_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_KIP_UP_SHOT_COMMAND) &&
            hasObjVar(player, ORIGINAL_TAKE_COVER_COMMAND) &&
            hasObjVar(player, ORIGINAL_FULL_AUTO_SINGLE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FULL_AUTO_SINGLE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_FULL_AUTO_AREA_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FULL_AUTO_AREA_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_CHARGE_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_CHARGE_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_STRAFE_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_STRAFE_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_STARTLE_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_STARTLE_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLUSHING_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLUSHING_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_LUNGE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_LUNGE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_LUNGE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_LUNGE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_LUNGE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_LUNGE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_LUNGE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_LUNGE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TAUNT_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_DIZZY_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_BLIND_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_BLIND_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SCATTER_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_DIZZY_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SCATTER_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_HEALTH_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_SPIN_ATTACK_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_HEALTH_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWEEP_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_SWEEP_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_MIND_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_MIND_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HIT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_STUN_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_BLIND_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_UNARMED_STUN_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_INTIMIDATE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_INTIMIDATE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_WARCRY_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_WARCRY_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_SCATTER_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_SCATTER_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_WILD_SHOT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_WILD_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_LEG_SHOT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_LEG_SHOT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACID_SINGLE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACID_CONE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACID_CONE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ACID_SINGLE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLAME_SINGLE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLAME_SINGLE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLAME_CONE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_FLAME_CONE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_LIGHTNING_SINGLE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_LIGHTNING_CONE_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_LIGHTNING_CONE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_LIGHTNING_SINGLE_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_AREA_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_AREA_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_BODY_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_BODY_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_BODY_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_HIT_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_AREA_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_AREA_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HEAD_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HEAD_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HEAD_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HIT_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_HIT_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_ONE_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_TWO_COMMAND) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_ACCURACY_AREA_THREE_COMMAND) &&
            hasObjVar(player, ORIGINAL_POLEARM_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_ONE_HAND_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_TWO_HAND_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_ACID_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_FLAME_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_LIGHTNING_CERTIFICATION) &&
            hasObjVar(player, ORIGINAL_COMBAT_ACTIONS) &&
            hasObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY_PRESENT) &&
            hasObjVar(player, ORIGINAL_POSTURE_DOWN_RECOVERY) &&
            hasObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY_PRESENT) &&
            hasObjVar(player, ORIGINAL_POSTURE_UP_RECOVERY) &&
            hasObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY_PRESENT) &&
            hasObjVar(player, ORIGINAL_KNOCKDOWN_RECOVERY) &&
            hasObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE_PRESENT) &&
            hasObjVar(player, ORIGINAL_KNOCKDOWN_POSTURE);
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
        if (getPlayerStationId(player) != stationId)
        {
            return "error=" + role + "StationNotAllowed oid=" + player;
        }
        return null;
    }

    private String validateOwnership(
        obj_id attacker, obj_id defender, String lifecycle, boolean allowAbsent)
        throws InterruptedException
    {
        boolean attackerRoot = hasObjVar(attacker, ROOT);
        boolean defenderRoot = hasObjVar(defender, ROOT);
        if (!attackerRoot && !defenderRoot)
        {
            return allowAbsent ? "fixtureAbsent" : "error=fixtureAbsent";
        }
        if (!attackerRoot || !defenderRoot ||
            !hasObjVar(attacker, LIFECYCLE) || !hasObjVar(defender, LIFECYCLE) ||
            !hasObjVar(attacker, PEER) || !hasObjVar(defender, PEER))
        {
            return "error=fixturePartial";
        }
        if (!lifecycle.equals(getStringObjVar(attacker, LIFECYCLE)) ||
            !lifecycle.equals(getStringObjVar(defender, LIFECYCLE)) ||
            getObjIdObjVar(attacker, PEER) != defender ||
            getObjIdObjVar(defender, PEER) != attacker)
        {
            return "error=fixtureOwnershipMismatch";
        }
        return null;
    }

    private int calculateTakeCoverActionCost(obj_id player)
        throws InterruptedException
    {
        float cost = 50.0f -
            (((float)(getAttrib(player, QUICKNESS) - 300) / 1200.0f) * 50.0f);
        return (int)Math.max(0.0f, cost);
    }

    private String buildStatus(obj_id attacker, obj_id defender, String lifecycle)
        throws InterruptedException
    {
        location attackerLocation = getLocation(attacker);
        location defenderLocation = getLocation(defender);
        obj_id concealTarget = hasObjVar(attacker, FIXTURE_CONCEAL_TARGET) ?
            getObjIdObjVar(attacker, FIXTURE_CONCEAL_TARGET) : obj_id.NULL_ID;
        boolean concealTargetExists = isIdValid(concealTarget) &&
            exists(concealTarget);
        float concealTargetDistance = concealTargetExists ?
            getDistance(attacker, concealTarget) : -1.0f;
        float concealTargetHate = concealTargetExists ?
            getHate(concealTarget, attacker) : -1.0f;
        obj_id concealTargetTop = concealTargetExists ?
            getHateTarget(concealTarget) : obj_id.NULL_ID;
        float concealTargetDefenderHate = concealTargetExists ?
            getHate(concealTarget, defender) : -1.0f;
        obj_id weapon = getObjectInSlot(attacker, "hold_r");
        String weaponTemplate =
            weapon == null || weapon == obj_id.NULL_ID ? "none" : getTemplateName(weapon);
        int canPerformAction = isIdValid(weapon) ? combat.canPerformAction(COMMAND, attacker) : -1;
        int canPerformHeadShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(DURATION_CONTROL_COMMAND, attacker) : -1;
        int canPerformHeadShotThree = isIdValid(weapon) ?
            combat.canPerformAction(HEAD_SHOT_THREE_COMMAND, attacker) : -1;
        int canPerformBodyShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(BODY_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformBodyShotThree = isIdValid(weapon) ?
            combat.canPerformAction(BODY_SHOT_THREE_COMMAND, attacker) : -1;
        int canPerformHealthShotOne = isIdValid(weapon) ?
            combat.canPerformAction(HEALTH_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformHealthShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(HEALTH_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformPistolMeleeDefenseOne = isIdValid(weapon) ?
            combat.canPerformAction(
                PISTOL_MELEE_DEFENSE_ONE_COMMAND, attacker) : -1;
        int canPerformPistolMeleeDefenseTwo = isIdValid(weapon) ?
            combat.canPerformAction(
                PISTOL_MELEE_DEFENSE_TWO_COMMAND, attacker) : -1;
        int canPerformTumbleToProne = isIdValid(weapon) ?
            combat.canPerformAction(TUMBLE_TO_PRONE_COMMAND, attacker) : -1;
        int canPerformTumbleToKneeling = isIdValid(weapon) ?
            combat.canPerformAction(TUMBLE_TO_KNEELING_COMMAND, attacker) : -1;
        int canPerformTumbleToStanding = isIdValid(weapon) ?
            combat.canPerformAction(TUMBLE_TO_STANDING_COMMAND, attacker) : -1;
        int canPerformActionShotOne = isIdValid(weapon) ?
            combat.canPerformAction(ACTION_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformActionShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(ACTION_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformMindShotOne = isIdValid(weapon) ?
            combat.canPerformAction(MIND_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformMindShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(MIND_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformSurpriseShot = isIdValid(weapon) ?
            combat.canPerformAction(SURPRISE_SHOT_COMMAND, attacker) : -1;
        int canPerformSniperShot = isIdValid(weapon) ?
            combat.canPerformAction(SNIPER_SHOT_COMMAND, attacker) : -1;
        int canPerformConcealShot = isIdValid(weapon) ?
            combat.canPerformAction(CONCEAL_SHOT_COMMAND, attacker) : -1;
        int canPerformFlurryShotOne = isIdValid(weapon) ?
            combat.canPerformAction(FLURRY_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformFlurryShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(FLURRY_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformPolearm =
            isIdValid(weapon) ? combat.canPerformAction(POLEARM_COMMAND, attacker) : -1;
        int canPerformPolearmLegTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_LEG_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmLegThree = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_LEG_THREE_COMMAND, attacker) : -1;
        int canPerformPolearmHitOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformPolearmHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmStunTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_STUN_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmSpinTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_SPIN_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmAreaOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_AREA_ONE_COMMAND, attacker) : -1;
        int canPerformPolearmAreaTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_AREA_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmSweepOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_SWEEP_ONE_COMMAND, attacker) : -1;
        int canPerformPolearmSweepTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_SWEEP_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmActionHitOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_ACTION_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformPolearmActionHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_ACTION_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmHitThree = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_HIT_THREE_COMMAND, attacker) : -1;
        int canPerformUnarmed =
            combat.canPerformAction(UNARMED_COMMAND, attacker);
        int canPerformUnarmedHitOne =
            combat.canPerformAction(UNARMED_HIT_ONE_COMMAND, attacker);
        int canPerformUnarmedHitTwo =
            combat.canPerformAction(UNARMED_HIT_TWO_COMMAND, attacker);
        int canPerformUnarmedBodyOne =
            combat.canPerformAction(UNARMED_BODY_ONE_COMMAND, attacker);
        int canPerformUnarmedLegOne =
            combat.canPerformAction(UNARMED_LEG_ONE_COMMAND, attacker);
        int canPerformUnarmedSpinOne =
            combat.canPerformAction(UNARMED_SPIN_ONE_COMMAND, attacker);
        int canPerformUnarmedSpinTwo =
            combat.canPerformAction(UNARMED_SPIN_TWO_COMMAND, attacker);
        int canPerformOverchargeOne = isIdValid(weapon) ?
            combat.canPerformAction(OVERCHARGE_ONE_COMMAND, attacker) : -1;
        int canPerformOverchargeTwo = isIdValid(weapon) ?
            combat.canPerformAction(OVERCHARGE_TWO_COMMAND, attacker) : -1;
        int canPerformPointBlankSingleOne = isIdValid(weapon) ?
            combat.canPerformAction(POINT_BLANK_SINGLE_ONE_COMMAND, attacker) : -1;
        int canPerformAim = isIdValid(weapon) ?
            combat.canPerformAction(AIM_COMMAND, attacker) : -1;
        int canPerformThreatenShot = isIdValid(weapon) ?
            combat.canPerformAction(THREATEN_SHOT_COMMAND, attacker) : -1;
        int canPerformWarningShot = isIdValid(weapon) ?
            combat.canPerformAction(WARNING_SHOT_COMMAND, attacker) : -1;
        int canPerformSuppressionFireOne = isIdValid(weapon) ?
            combat.canPerformAction(SUPPRESSION_FIRE_ONE_COMMAND, attacker) : -1;
        int canPerformSuppressionFireTwo = isIdValid(weapon) ?
            combat.canPerformAction(SUPPRESSION_FIRE_TWO_COMMAND, attacker) : -1;
        int canPerformRollShot = isIdValid(weapon) ?
            combat.canPerformAction(ROLL_SHOT_COMMAND, attacker) : -1;
        int canPerformDiveShot = isIdValid(weapon) ?
            combat.canPerformAction(DIVE_SHOT_COMMAND, attacker) : -1;
        int canPerformKipUpShot = isIdValid(weapon) ?
            combat.canPerformAction(KIP_UP_SHOT_COMMAND, attacker) : -1;
        int canPerformTakeCover =
            combat.canPerformAction(TAKE_COVER_COMMAND, attacker);
        int canPerformFullAutoSingleOne = isIdValid(weapon) ?
            combat.canPerformAction(FULL_AUTO_SINGLE_ONE_COMMAND, attacker) : -1;
        int canPerformFullAutoSingleTwo = isIdValid(weapon) ?
            combat.canPerformAction(FULL_AUTO_SINGLE_TWO_COMMAND, attacker) : -1;
        int canPerformFullAutoAreaOne = isIdValid(weapon) ?
            combat.canPerformAction(FULL_AUTO_AREA_ONE_COMMAND, attacker) : -1;
        int canPerformFullAutoAreaTwo = isIdValid(weapon) ?
            combat.canPerformAction(FULL_AUTO_AREA_TWO_COMMAND, attacker) : -1;
        int canPerformChargeShotOne = isIdValid(weapon) ?
            combat.canPerformAction(CHARGE_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformChargeShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(CHARGE_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformStrafeShotOne = isIdValid(weapon) ?
            combat.canPerformAction(STRAFE_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformStrafeShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(STRAFE_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformStartleShotOne = isIdValid(weapon) ?
            combat.canPerformAction(STARTLE_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformStartleShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(STARTLE_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformFlushingShotOne = isIdValid(weapon) ?
            combat.canPerformAction(FLUSHING_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformFlushingShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(FLUSHING_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmLungeOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_LUNGE_ONE_COMMAND, attacker) : -1;
        int canPerformUnarmedLungeOne =
            combat.canPerformAction(UNARMED_LUNGE_ONE_COMMAND, attacker);
        int canPerformOneHandLungeOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_LUNGE_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandLungeOne = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_LUNGE_ONE_COMMAND, attacker) : -1;
        int canPerformPolearmLungeTwo = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_LUNGE_TWO_COMMAND, attacker) : -1;
        int canPerformUnarmedLungeTwo =
            combat.canPerformAction(UNARMED_LUNGE_TWO_COMMAND, attacker);
        int canPerformOneHandLungeTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_LUNGE_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandLungeTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_LUNGE_TWO_COMMAND, attacker) : -1;
        int canPerformTaunt = combat.canPerformAction(TAUNT_COMMAND, attacker);
        int canPerformOneHandDizzyHitOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_DIZZY_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandBlindHitOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_BLIND_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandBlindHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_BLIND_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandScatterHitOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_SCATTER_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandDizzyHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_DIZZY_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandScatterHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_SCATTER_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandHealthHitOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_HEALTH_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandSpinAttackTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_SPIN_ATTACK_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandHealthHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_HEALTH_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandSweepOne = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_SWEEP_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandSweepTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_SWEEP_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandMindHitOne = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_MIND_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandMindHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_MIND_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandHitThree = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HIT_THREE_COMMAND, attacker) : -1;
        int canPerformPolearmStunOne = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_STUN_ONE_COMMAND, attacker) : -1;
        int canPerformUnarmedBlindOne =
            combat.canPerformAction(UNARMED_BLIND_ONE_COMMAND, attacker);
        int canPerformUnarmedStunOne =
            combat.canPerformAction(UNARMED_STUN_ONE_COMMAND, attacker);
        int canPerformIntimidateOne =
            combat.canPerformAction(INTIMIDATE_ONE_COMMAND, attacker);
        int canPerformIntimidateTwo =
            combat.canPerformAction(INTIMIDATE_TWO_COMMAND, attacker);
        int canPerformWarcryOne =
            combat.canPerformAction(WARCRY_ONE_COMMAND, attacker);
        int canPerformWarcryTwo =
            combat.canPerformAction(WARCRY_TWO_COMMAND, attacker);
        int canPerformScatterShotOne = isIdValid(weapon) ?
            combat.canPerformAction(SCATTER_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformScatterShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(SCATTER_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformWildShotOne = isIdValid(weapon) ?
            combat.canPerformAction(WILD_SHOT_ONE_COMMAND, attacker) : -1;
        int canPerformWildShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(WILD_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformLegShotTwo = isIdValid(weapon) ?
            combat.canPerformAction(LEG_SHOT_TWO_COMMAND, attacker) : -1;
        int canPerformLegShotThree = isIdValid(weapon) ?
            combat.canPerformAction(LEG_SHOT_THREE_COMMAND, attacker) : -1;
        int canPerformAcidSingleOne = isIdValid(weapon) ?
            combat.canPerformAction(ACID_SINGLE_ONE_COMMAND, attacker) : -1;
        int canPerformAcidConeOne = isIdValid(weapon) ?
            combat.canPerformAction(ACID_CONE_ONE_COMMAND, attacker) : -1;
        int canPerformAcidConeTwo = isIdValid(weapon) ?
            combat.canPerformAction(ACID_CONE_TWO_COMMAND, attacker) : -1;
        int canPerformAcidSingleTwo = isIdValid(weapon) ?
            combat.canPerformAction(ACID_SINGLE_TWO_COMMAND, attacker) : -1;
        int canPerformFlameSingleOne = isIdValid(weapon) ?
            combat.canPerformAction(FLAME_SINGLE_ONE_COMMAND, attacker) : -1;
        int canPerformFlameSingleTwo = isIdValid(weapon) ?
            combat.canPerformAction(FLAME_SINGLE_TWO_COMMAND, attacker) : -1;
        int canPerformFlameConeOne = isIdValid(weapon) ?
            combat.canPerformAction(FLAME_CONE_ONE_COMMAND, attacker) : -1;
        int canPerformFlameConeTwo = isIdValid(weapon) ?
            combat.canPerformAction(FLAME_CONE_TWO_COMMAND, attacker) : -1;
        int canPerformLightningSingleOne = isIdValid(weapon) ?
            combat.canPerformAction(LIGHTNING_SINGLE_ONE_COMMAND, attacker) : -1;
        int canPerformLightningConeOne = isIdValid(weapon) ?
            combat.canPerformAction(LIGHTNING_CONE_ONE_COMMAND, attacker) : -1;
        int canPerformLightningConeTwo = isIdValid(weapon) ?
            combat.canPerformAction(LIGHTNING_CONE_TWO_COMMAND, attacker) : -1;
        int canPerformLightningSingleTwo = isIdValid(weapon) ?
            combat.canPerformAction(LIGHTNING_SINGLE_TWO_COMMAND, attacker) : -1;
        int canPerformPolearmArea = isIdValid(weapon) ?
            combat.canPerformAction(POLEARM_AREA_COMMAND, attacker) : -1;
        int canPerformOneHandArea = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_AREA_COMMAND, attacker) : -1;
        int canPerformOneHandBodyOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_BODY_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandBodyTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_BODY_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandBodyThree = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_BODY_THREE_COMMAND, attacker) : -1;
        int canPerformOneHandHitOne = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformOneHandHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_HIT_TWO_COMMAND, attacker) : -1;
        int canPerformOneHandHitThree = isIdValid(weapon) ?
            combat.canPerformAction(ONE_HAND_HIT_THREE_COMMAND, attacker) : -1;
        int canPerformTwoHandArea = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_AREA_COMMAND, attacker) : -1;
        int canPerformTwoHandAreaTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_AREA_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandAccuracyAreaOne = isIdValid(weapon) ?
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandAccuracyAreaTwo = isIdValid(weapon) ?
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandAccuracyAreaThree = isIdValid(weapon) ?
            combat.canPerformAction(
                TWO_HAND_ACCURACY_AREA_THREE_COMMAND, attacker) : -1;
        int canPerformTwoHandHeadOne = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HEAD_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandHeadTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HEAD_TWO_COMMAND, attacker) : -1;
        int canPerformTwoHandHeadThree = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HEAD_THREE_COMMAND, attacker) : -1;
        int canPerformTwoHandHitOne = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HIT_ONE_COMMAND, attacker) : -1;
        int canPerformTwoHandHitTwo = isIdValid(weapon) ?
            combat.canPerformAction(TWO_HAND_HIT_TWO_COMMAND, attacker) : -1;
        String fixtureWeapon = hasObjVar(attacker, FIXTURE_WEAPON) ?
            getObjIdObjVar(attacker, FIXTURE_WEAPON).toString() : "none";
        String fixturePistol = hasObjVar(attacker, FIXTURE_PISTOL) ?
            getObjIdObjVar(attacker, FIXTURE_PISTOL).toString() : "none";
        String fixtureCarbine = hasObjVar(attacker, FIXTURE_CARBINE) ?
            getObjIdObjVar(attacker, FIXTURE_CARBINE).toString() : "none";
        String fixturePolearm = hasObjVar(attacker, FIXTURE_POLEARM) ?
            getObjIdObjVar(attacker, FIXTURE_POLEARM).toString() : "none";
        String fixtureOneHand = hasObjVar(attacker, FIXTURE_ONE_HAND) ?
            getObjIdObjVar(attacker, FIXTURE_ONE_HAND).toString() : "none";
        String fixtureTwoHand = hasObjVar(attacker, FIXTURE_TWO_HAND) ?
            getObjIdObjVar(attacker, FIXTURE_TWO_HAND).toString() : "none";
        String fixtureAcid = hasObjVar(attacker, FIXTURE_ACID) ?
            getObjIdObjVar(attacker, FIXTURE_ACID).toString() : "none";
        String fixtureFlame = hasObjVar(attacker, FIXTURE_FLAME) ?
            getObjIdObjVar(attacker, FIXTURE_FLAME).toString() : "none";
        String fireDotId = dot.DOT_FIRE + attacker;
        String bleedingDotId = dot.DOT_BLEEDING + attacker;
        combat_data flameData =
            combat_engine.getCombatData(FLAME_SINGLE_ONE_COMMAND);
        weapon_data flameWeaponData =
            isIdValid(weapon) ? getWeaponData(weapon) : null;
        int[] flameCosts = flameData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, flameData) :
            new int[] {-1, -1, -1};
        combat_data healthShotData =
            combat_engine.getCombatData(HEALTH_SHOT_ONE_COMMAND);
        combat_data healthShotTwoData =
            combat_engine.getCombatData(HEALTH_SHOT_TWO_COMMAND);
        combat_data pistolMeleeDefenseOneData =
            combat_engine.getCombatData(PISTOL_MELEE_DEFENSE_ONE_COMMAND);
        combat_data pistolMeleeDefenseTwoData =
            combat_engine.getCombatData(PISTOL_MELEE_DEFENSE_TWO_COMMAND);
        combat_data actionShotData =
            combat_engine.getCombatData(ACTION_SHOT_ONE_COMMAND);
        combat_data actionShotTwoData =
            combat_engine.getCombatData(ACTION_SHOT_TWO_COMMAND);
        combat_data overchargeOneData =
            combat_engine.getCombatData(OVERCHARGE_ONE_COMMAND);
        combat_data pointBlankSingleOneData =
            combat_engine.getCombatData(POINT_BLANK_SINGLE_ONE_COMMAND);
        combat_data aimData =
            combat_engine.getCombatData(AIM_COMMAND);
        combat_data threatenShotData =
            combat_engine.getCombatData(THREATEN_SHOT_COMMAND);
        combat_data warningShotData =
            combat_engine.getCombatData(WARNING_SHOT_COMMAND);
        combat_data suppressionFireOneData =
            combat_engine.getCombatData(SUPPRESSION_FIRE_ONE_COMMAND);
        combat_data suppressionFireTwoData =
            combat_engine.getCombatData(SUPPRESSION_FIRE_TWO_COMMAND);
        combat_data rollShotData =
            combat_engine.getCombatData(ROLL_SHOT_COMMAND);
        combat_data diveShotData =
            combat_engine.getCombatData(DIVE_SHOT_COMMAND);
        combat_data kipUpShotData =
            combat_engine.getCombatData(KIP_UP_SHOT_COMMAND);
        combat_data fullAutoSingleOneData =
            combat_engine.getCombatData(FULL_AUTO_SINGLE_ONE_COMMAND);
        combat_data fullAutoSingleTwoData =
            combat_engine.getCombatData(FULL_AUTO_SINGLE_TWO_COMMAND);
        combat_data fullAutoAreaOneData =
            combat_engine.getCombatData(FULL_AUTO_AREA_ONE_COMMAND);
        combat_data fullAutoAreaTwoData =
            combat_engine.getCombatData(FULL_AUTO_AREA_TWO_COMMAND);
        combat_data chargeShotOneData =
            combat_engine.getCombatData(CHARGE_SHOT_ONE_COMMAND);
        combat_data chargeShotTwoData =
            combat_engine.getCombatData(CHARGE_SHOT_TWO_COMMAND);
        combat_data strafeShotOneData =
            combat_engine.getCombatData(STRAFE_SHOT_ONE_COMMAND);
        combat_data strafeShotTwoData =
            combat_engine.getCombatData(STRAFE_SHOT_TWO_COMMAND);
        combat_data startleShotOneData =
            combat_engine.getCombatData(STARTLE_SHOT_ONE_COMMAND);
        combat_data startleShotTwoData =
            combat_engine.getCombatData(STARTLE_SHOT_TWO_COMMAND);
        combat_data flushingShotOneData =
            combat_engine.getCombatData(FLUSHING_SHOT_ONE_COMMAND);
        combat_data flushingShotTwoData =
            combat_engine.getCombatData(FLUSHING_SHOT_TWO_COMMAND);
        combat_data polearmLungeOneData =
            combat_engine.getCombatData(POLEARM_LUNGE_ONE_COMMAND);
        combat_data unarmedLungeOneData =
            combat_engine.getCombatData(UNARMED_LUNGE_ONE_COMMAND);
        combat_data oneHandLungeOneData =
            combat_engine.getCombatData(ONE_HAND_LUNGE_ONE_COMMAND);
        combat_data twoHandLungeOneData =
            combat_engine.getCombatData(TWO_HAND_LUNGE_ONE_COMMAND);
        combat_data polearmLungeTwoData =
            combat_engine.getCombatData(POLEARM_LUNGE_TWO_COMMAND);
        combat_data unarmedLungeTwoData =
            combat_engine.getCombatData(UNARMED_LUNGE_TWO_COMMAND);
        combat_data oneHandLungeTwoData =
            combat_engine.getCombatData(ONE_HAND_LUNGE_TWO_COMMAND);
        combat_data twoHandLungeTwoData =
            combat_engine.getCombatData(TWO_HAND_LUNGE_TWO_COMMAND);
        combat_data tauntData = combat_engine.getCombatData(TAUNT_COMMAND);
        combat_data oneHandDizzyHitOneData =
            combat_engine.getCombatData(ONE_HAND_DIZZY_HIT_ONE_COMMAND);
        combat_data oneHandBlindHitOneData =
            combat_engine.getCombatData(ONE_HAND_BLIND_HIT_ONE_COMMAND);
        combat_data oneHandBlindHitTwoData =
            combat_engine.getCombatData(ONE_HAND_BLIND_HIT_TWO_COMMAND);
        combat_data oneHandScatterHitOneData =
            combat_engine.getCombatData(ONE_HAND_SCATTER_HIT_ONE_COMMAND);
        combat_data oneHandDizzyHitTwoData =
            combat_engine.getCombatData(ONE_HAND_DIZZY_HIT_TWO_COMMAND);
        combat_data oneHandScatterHitTwoData =
            combat_engine.getCombatData(ONE_HAND_SCATTER_HIT_TWO_COMMAND);
        combat_data oneHandHealthHitOneData =
            combat_engine.getCombatData(ONE_HAND_HEALTH_HIT_ONE_COMMAND);
        combat_data oneHandSpinAttackTwoData =
            combat_engine.getCombatData(ONE_HAND_SPIN_ATTACK_TWO_COMMAND);
        combat_data oneHandHealthHitTwoData =
            combat_engine.getCombatData(ONE_HAND_HEALTH_HIT_TWO_COMMAND);
        combat_data twoHandSweepOneData =
            combat_engine.getCombatData(TWO_HAND_SWEEP_ONE_COMMAND);
        combat_data twoHandMindHitOneData =
            combat_engine.getCombatData(TWO_HAND_MIND_HIT_ONE_COMMAND);
        combat_data twoHandMindHitTwoData =
            combat_engine.getCombatData(TWO_HAND_MIND_HIT_TWO_COMMAND);
        combat_data twoHandHitThreeData =
            combat_engine.getCombatData(TWO_HAND_HIT_THREE_COMMAND);
        combat_data polearmStunOneData =
            combat_engine.getCombatData(POLEARM_STUN_ONE_COMMAND);
        combat_data unarmedBlindOneData =
            combat_engine.getCombatData(UNARMED_BLIND_ONE_COMMAND);
        combat_data unarmedStunOneData =
            combat_engine.getCombatData(UNARMED_STUN_ONE_COMMAND);
        combat_data intimidateOneData =
            combat_engine.getCombatData(INTIMIDATE_ONE_COMMAND);
        combat_data intimidateTwoData =
            combat_engine.getCombatData(INTIMIDATE_TWO_COMMAND);
        combat_data warcryOneData =
            combat_engine.getCombatData(WARCRY_ONE_COMMAND);
        combat_data warcryTwoData =
            combat_engine.getCombatData(WARCRY_TWO_COMMAND);
        combat_data scatterShotOneData =
            combat_engine.getCombatData(SCATTER_SHOT_ONE_COMMAND);
        combat_data scatterShotTwoData =
            combat_engine.getCombatData(SCATTER_SHOT_TWO_COMMAND);
        combat_data wildShotOneData =
            combat_engine.getCombatData(WILD_SHOT_ONE_COMMAND);
        combat_data wildShotTwoData =
            combat_engine.getCombatData(WILD_SHOT_TWO_COMMAND);
        combat_data legShotTwoData =
            combat_engine.getCombatData(LEG_SHOT_TWO_COMMAND);
        combat_data legShotThreeData =
            combat_engine.getCombatData(LEG_SHOT_THREE_COMMAND);
        combat_data mindShotData =
            combat_engine.getCombatData(MIND_SHOT_ONE_COMMAND);
        combat_data mindShotTwoData =
            combat_engine.getCombatData(MIND_SHOT_TWO_COMMAND);
        combat_data surpriseShotData =
            combat_engine.getCombatData(SURPRISE_SHOT_COMMAND);
        combat_data sniperShotData =
            combat_engine.getCombatData(SNIPER_SHOT_COMMAND);
        combat_data concealShotData =
            combat_engine.getCombatData(CONCEAL_SHOT_COMMAND);
        combat_data flurryShotOneData =
            combat_engine.getCombatData(FLURRY_SHOT_ONE_COMMAND);
        combat_data flurryShotTwoData =
            combat_engine.getCombatData(FLURRY_SHOT_TWO_COMMAND);
        int[] healthShotCosts =
            healthShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, healthShotData) :
            new int[] {-1, -1, -1};
        int[] healthShotTwoCosts =
            healthShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, healthShotTwoData) :
            new int[] {-1, -1, -1};
        int[] pistolMeleeDefenseOneCosts =
            pistolMeleeDefenseOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, pistolMeleeDefenseOneData) :
            new int[] {-1, -1, -1};
        int[] pistolMeleeDefenseTwoCosts =
            pistolMeleeDefenseTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, pistolMeleeDefenseTwoData) :
            new int[] {-1, -1, -1};
        int[] actionShotCosts =
            actionShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, actionShotData) :
            new int[] {-1, -1, -1};
        int[] actionShotTwoCosts =
            actionShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, actionShotTwoData) :
            new int[] {-1, -1, -1};
        int[] overchargeOneCosts =
            overchargeOneData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, overchargeOneData) :
            new int[] {-1, -1, -1};
        int[] pointBlankSingleOneCosts =
            pointBlankSingleOneData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, pointBlankSingleOneData) :
            new int[] {-1, -1, -1};
        int[] aimCosts =
            aimData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, aimData) :
            new int[] {-1, -1, -1};
        int[] threatenShotCosts =
            threatenShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, threatenShotData) :
            new int[] {-1, -1, -1};
        int[] warningShotCosts =
            warningShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, warningShotData) :
            new int[] {-1, -1, -1};
        int[] suppressionFireOneCosts =
            suppressionFireOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, suppressionFireOneData) :
            new int[] {-1, -1, -1};
        int[] rollShotCosts =
            rollShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, rollShotData) :
            new int[] {-1, -1, -1};
        int[] diveShotCosts =
            diveShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, diveShotData) :
            new int[] {-1, -1, -1};
        int[] kipUpShotCosts =
            kipUpShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, kipUpShotData) :
            new int[] {-1, -1, -1};
        int[] suppressionFireTwoCosts =
            suppressionFireTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, suppressionFireTwoData) :
            new int[] {-1, -1, -1};
        int[] fullAutoSingleOneCosts =
            fullAutoSingleOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, fullAutoSingleOneData) :
            new int[] {-1, -1, -1};
        int[] fullAutoSingleTwoCosts =
            fullAutoSingleTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, fullAutoSingleTwoData) :
            new int[] {-1, -1, -1};
        int[] fullAutoAreaOneCosts =
            fullAutoAreaOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, fullAutoAreaOneData) :
            new int[] {-1, -1, -1};
        int[] fullAutoAreaTwoCosts =
            fullAutoAreaTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, fullAutoAreaTwoData) :
            new int[] {-1, -1, -1};
        int[] chargeShotOneCosts =
            chargeShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, chargeShotOneData) :
            new int[] {-1, -1, -1};
        int[] chargeShotTwoCosts =
            chargeShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, chargeShotTwoData) :
            new int[] {-1, -1, -1};
        int[] strafeShotOneCosts =
            strafeShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, strafeShotOneData) :
            new int[] {-1, -1, -1};
        int[] strafeShotTwoCosts =
            strafeShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, strafeShotTwoData) :
            new int[] {-1, -1, -1};
        int[] startleShotOneCosts =
            startleShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, startleShotOneData) :
            new int[] {-1, -1, -1};
        int[] startleShotTwoCosts =
            startleShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, startleShotTwoData) :
            new int[] {-1, -1, -1};
        int[] flushingShotOneCosts =
            flushingShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, flushingShotOneData) :
            new int[] {-1, -1, -1};
        int[] flushingShotTwoCosts =
            flushingShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, flushingShotTwoData) :
            new int[] {-1, -1, -1};
        int[] polearmLungeOneCosts =
            polearmLungeOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, polearmLungeOneData) :
            new int[] {-1, -1, -1};
        weapon_data currentWeaponData =
            getWeaponData(getCurrentWeapon(attacker));
        int[] unarmedLungeOneCosts =
            unarmedLungeOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, unarmedLungeOneData) :
            new int[] {-1, -1, -1};
        int[] oneHandLungeOneCosts =
            oneHandLungeOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, oneHandLungeOneData) :
            new int[] {-1, -1, -1};
        int[] twoHandLungeOneCosts =
            twoHandLungeOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, twoHandLungeOneData) :
            new int[] {-1, -1, -1};
        int[] polearmLungeTwoCosts =
            polearmLungeTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, polearmLungeTwoData) :
            new int[] {-1, -1, -1};
        int[] unarmedLungeTwoCosts =
            unarmedLungeTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, unarmedLungeTwoData) :
            new int[] {-1, -1, -1};
        int[] oneHandLungeTwoCosts =
            oneHandLungeTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, oneHandLungeTwoData) :
            new int[] {-1, -1, -1};
        int[] twoHandLungeTwoCosts =
            twoHandLungeTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, twoHandLungeTwoData) :
            new int[] {-1, -1, -1};
        int[] tauntCosts = tauntData != null && currentWeaponData != null ?
            combat.getActionCost(attacker, currentWeaponData, tauntData) :
            new int[] {-1, -1, -1};
        int[] oneHandDizzyHitOneCosts =
            oneHandDizzyHitOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, oneHandDizzyHitOneData) :
            new int[] {-1, -1, -1};
        int[] oneHandBlindHitOneCosts =
            oneHandBlindHitOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandBlindHitOneData) :
            new int[] {-1, -1, -1};
        int[] oneHandBlindHitTwoCosts =
            oneHandBlindHitTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandBlindHitTwoData) :
            new int[] {-1, -1, -1};
        int[] oneHandScatterHitOneCosts =
            oneHandScatterHitOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandScatterHitOneData) :
            new int[] {-1, -1, -1};
        int[] oneHandDizzyHitTwoCosts =
            oneHandDizzyHitTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandDizzyHitTwoData) :
            new int[] {-1, -1, -1};
        int[] oneHandScatterHitTwoCosts =
            oneHandScatterHitTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandScatterHitTwoData) :
            new int[] {-1, -1, -1};
        int[] oneHandHealthHitOneCosts =
            oneHandHealthHitOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandHealthHitOneData) :
            new int[] {-1, -1, -1};
        int[] oneHandSpinAttackTwoCosts =
            oneHandSpinAttackTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandSpinAttackTwoData) :
            new int[] {-1, -1, -1};
        int[] oneHandHealthHitTwoCosts =
            oneHandHealthHitTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, oneHandHealthHitTwoData) :
            new int[] {-1, -1, -1};
        int[] twoHandSweepOneCosts =
            twoHandSweepOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, twoHandSweepOneData) :
            new int[] {-1, -1, -1};
        int[] twoHandMindHitOneCosts =
            twoHandMindHitOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, twoHandMindHitOneData) :
            new int[] {-1, -1, -1};
        int[] twoHandMindHitTwoCosts =
            twoHandMindHitTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, twoHandMindHitTwoData) :
            new int[] {-1, -1, -1};
        int[] twoHandHitThreeCosts =
            twoHandHitThreeData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, twoHandHitThreeData) :
            new int[] {-1, -1, -1};
        int[] polearmStunOneCosts =
            polearmStunOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, polearmStunOneData) :
            new int[] {-1, -1, -1};
        int[] unarmedBlindOneCosts =
            unarmedBlindOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, unarmedBlindOneData) :
            new int[] {-1, -1, -1};
        int[] unarmedStunOneCosts =
            unarmedStunOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, unarmedStunOneData) :
            new int[] {-1, -1, -1};
        int[] intimidateOneCosts =
            intimidateOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, intimidateOneData) :
            new int[] {-1, -1, -1};
        int[] intimidateTwoCosts =
            intimidateTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, intimidateTwoData) :
            new int[] {-1, -1, -1};
        int[] warcryOneCosts =
            warcryOneData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, warcryOneData) :
            new int[] {-1, -1, -1};
        int[] warcryTwoCosts =
            warcryTwoData != null && currentWeaponData != null ?
            combat.getActionCost(
                attacker, currentWeaponData, warcryTwoData) :
            new int[] {-1, -1, -1};
        int[] scatterShotOneCosts =
            scatterShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, scatterShotOneData) :
            new int[] {-1, -1, -1};
        int[] scatterShotTwoCosts =
            scatterShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, scatterShotTwoData) :
            new int[] {-1, -1, -1};
        int[] wildShotOneCosts =
            wildShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, wildShotOneData) :
            new int[] {-1, -1, -1};
        int[] wildShotTwoCosts =
            wildShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, wildShotTwoData) :
            new int[] {-1, -1, -1};
        int[] legShotTwoCosts =
            legShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, legShotTwoData) :
            new int[] {-1, -1, -1};
        int[] legShotThreeCosts =
            legShotThreeData != null && flameWeaponData != null ?
            combat.getActionCost(
                attacker, flameWeaponData, legShotThreeData) :
            new int[] {-1, -1, -1};
        int[] mindShotCosts =
            mindShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, mindShotData) :
            new int[] {-1, -1, -1};
        int[] mindShotTwoCosts =
            mindShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, mindShotTwoData) :
            new int[] {-1, -1, -1};
        int[] surpriseShotCosts =
            surpriseShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, surpriseShotData) :
            new int[] {-1, -1, -1};
        int[] sniperShotCosts =
            sniperShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, sniperShotData) :
            new int[] {-1, -1, -1};
        int[] concealShotCosts =
            concealShotData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, concealShotData) :
            new int[] {-1, -1, -1};
        int[] flurryShotOneCosts =
            flurryShotOneData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, flurryShotOneData) :
            new int[] {-1, -1, -1};
        int[] flurryShotTwoCosts =
            flurryShotTwoData != null && flameWeaponData != null ?
            combat.getActionCost(attacker, flameWeaponData, flurryShotTwoData) :
            new int[] {-1, -1, -1};
        String fixtureLightning = hasObjVar(attacker, FIXTURE_LIGHTNING) ?
            getObjIdObjVar(attacker, FIXTURE_LIGHTNING).toString() : "none";
        int nextAttackDelayUntil = utils.hasScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL) ?
            utils.getIntScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_UNTIL) : -1;
        String nextAttackDelayResult = utils.hasScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT) ?
            utils.getStringScriptVar(
                defender, combat_base.PRECU_NEXT_ATTACK_DELAY_RESULT) :
            "NONE";
        return "lifecycle=" + lifecycle +
            " prepared=" + (getIntObjVar(attacker, PREPARED) == 1 &&
                getIntObjVar(defender, PREPARED) == 1) +
            " distanceCentimeters=" + (int)(getDistance(attacker, defender) * 100.0f) +
            " attackerScene=" + attackerLocation.area +
            " defenderScene=" + defenderLocation.area +
            " attackerPosture=" + getPosture(attacker) +
            " attackerLocomotion=" + getLocomotion(attacker) +
            " defenderPosture=" + getPosture(defender) +
            " defenderLocomotion=" + getLocomotion(defender) +
            " defenderCoverState=" + getState(defender, STATE_COVER) +
            " novice=" + hasSkill(attacker, MARKSMAN_NOVICE) +
            " rifleOne=" + hasSkill(attacker, RIFLE_ONE) +
            " rifleTwo=" + hasSkill(attacker, RIFLE_TWO) +
            " rifleThree=" + hasSkill(attacker, RIFLE_THREE) +
            " rifleFour=" + hasSkill(attacker, RIFLE_FOUR) +
            " riflemanNovice=" + hasSkill(attacker, RIFLEMAN_NOVICE) +
            " brawlerRoot=" + hasSkill(attacker, BRAWLER_ROOT) +
            " brawlerNovice=" + hasSkill(attacker, BRAWLER_NOVICE) +
            " brawlerOneHandOne=" +
                hasSkill(attacker, BRAWLER_ONE_HAND_ONE) +
            " brawlerOneHandTwo=" +
                hasSkill(attacker, BRAWLER_ONE_HAND_TWO) +
            " brawlerOneHandThree=" +
                hasSkill(attacker, BRAWLER_ONE_HAND_THREE) +
            " brawlerOneHandFour=" +
                hasSkill(attacker, BRAWLER_ONE_HAND_FOUR) +
            " oneHandSwordNovice=" +
                hasSkill(attacker, ONE_HAND_SWORD_NOVICE) +
            " oneHandSwordSupportOne=" +
                hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_ONE) +
            " oneHandSwordSupportTwo=" +
                hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_TWO) +
            " oneHandSwordSupportThree=" +
                hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_THREE) +
            " oneHandSwordSupportFour=" +
                hasSkill(attacker, ONE_HAND_SWORD_SUPPORT_FOUR) +
            " oneHandSwordAccuracyOne=" +
                hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_ONE) +
            " oneHandSwordAccuracyTwo=" +
                hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_TWO) +
            " oneHandSwordAccuracyThree=" +
                hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_THREE) +
            " oneHandSwordAccuracyFour=" +
                hasSkill(attacker, ONE_HAND_SWORD_ACCURACY_FOUR) +
            " oneHandSwordSpeedOne=" +
                hasSkill(attacker, ONE_HAND_SWORD_SPEED_ONE) +
            " oneHandSwordSpeedTwo=" +
                hasSkill(attacker, ONE_HAND_SWORD_SPEED_TWO) +
            " oneHandSwordSpeedThree=" +
                hasSkill(attacker, ONE_HAND_SWORD_SPEED_THREE) +
            " oneHandSwordSpeedFour=" +
                hasSkill(attacker, ONE_HAND_SWORD_SPEED_FOUR) +
            " oneHandSwordAbilityOne=" +
                hasSkill(attacker, ONE_HAND_SWORD_ABILITY_ONE) +
            " oneHandSwordAbilityTwo=" +
                hasSkill(attacker, ONE_HAND_SWORD_ABILITY_TWO) +
            " oneHandSwordAbilityThree=" +
                hasSkill(attacker, ONE_HAND_SWORD_ABILITY_THREE) +
            " oneHandSwordAbilityFour=" +
                hasSkill(attacker, ONE_HAND_SWORD_ABILITY_FOUR) +
            " oneHandSwordMaster=" +
                hasSkill(attacker, ONE_HAND_SWORD_MASTER) +
            " brawlerTwoHandOne=" +
                hasSkill(attacker, BRAWLER_TWO_HAND_ONE) +
            " brawlerTwoHandTwo=" +
                hasSkill(attacker, BRAWLER_TWO_HAND_TWO) +
            " brawlerTwoHandThree=" +
                hasSkill(attacker, BRAWLER_TWO_HAND_THREE) +
            " brawlerTwoHandFour=" +
                hasSkill(attacker, BRAWLER_TWO_HAND_FOUR) +
            " twoHandSwordNovice=" +
                hasSkill(attacker, TWO_HAND_SWORD_NOVICE) +
            " twoHandSwordAccuracyOne=" +
                hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_ONE) +
            " twoHandSwordAccuracyTwo=" +
                hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_TWO) +
            " twoHandSwordAccuracyThree=" +
                hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_THREE) +
            " twoHandSwordAccuracyFour=" +
                hasSkill(attacker, TWO_HAND_SWORD_ACCURACY_FOUR) +
            " twoHandSwordSpeedOne=" +
                hasSkill(attacker, TWO_HAND_SWORD_SPEED_ONE) +
            " twoHandSwordSpeedTwo=" +
                hasSkill(attacker, TWO_HAND_SWORD_SPEED_TWO) +
            " twoHandSwordSpeedThree=" +
                hasSkill(attacker, TWO_HAND_SWORD_SPEED_THREE) +
            " twoHandSwordSpeedFour=" +
                hasSkill(attacker, TWO_HAND_SWORD_SPEED_FOUR) +
            " twoHandSwordAbilityOne=" +
                hasSkill(attacker, TWO_HAND_SWORD_ABILITY_ONE) +
            " twoHandSwordAbilityTwo=" +
                hasSkill(attacker, TWO_HAND_SWORD_ABILITY_TWO) +
            " twoHandSwordAbilityThree=" +
                hasSkill(attacker, TWO_HAND_SWORD_ABILITY_THREE) +
            " twoHandSwordAbilityFour=" +
                hasSkill(attacker, TWO_HAND_SWORD_ABILITY_FOUR) +
            " twoHandSwordSupportOne=" +
                hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_ONE) +
            " twoHandSwordSupportTwo=" +
                hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_TWO) +
            " twoHandSwordSupportThree=" +
                hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_THREE) +
            " twoHandSwordSupportFour=" +
                hasSkill(attacker, TWO_HAND_SWORD_SUPPORT_FOUR) +
            " twoHandSwordMaster=" +
                hasSkill(attacker, TWO_HAND_SWORD_MASTER) +
            " brawlerPolearmOne=" +
                hasSkill(attacker, BRAWLER_POLEARM_ONE) +
            " brawlerPolearmTwo=" +
                hasSkill(attacker, BRAWLER_POLEARM_TWO) +
            " brawlerPolearmThree=" +
                hasSkill(attacker, BRAWLER_POLEARM_THREE) +
            " brawlerPolearmFour=" +
                hasSkill(attacker, BRAWLER_POLEARM_FOUR) +
            " polearmNovice=" + hasSkill(attacker, POLEARM_NOVICE) +
            " polearmAccuracyOne=" +
                hasSkill(attacker, POLEARM_ACCURACY_ONE) +
            " polearmAccuracyTwo=" +
                hasSkill(attacker, POLEARM_ACCURACY_TWO) +
            " polearmAccuracyThree=" +
                hasSkill(attacker, POLEARM_ACCURACY_THREE) +
            " polearmAccuracyFour=" +
                hasSkill(attacker, POLEARM_ACCURACY_FOUR) +
            " polearmSpeedOne=" + hasSkill(attacker, POLEARM_SPEED_ONE) +
            " polearmSpeedTwo=" + hasSkill(attacker, POLEARM_SPEED_TWO) +
            " polearmSpeedThree=" + hasSkill(attacker, POLEARM_SPEED_THREE) +
            " polearmSpeedFour=" + hasSkill(attacker, POLEARM_SPEED_FOUR) +
            " polearmAbilityOne=" + hasSkill(attacker, POLEARM_ABILITY_ONE) +
            " polearmAbilityTwo=" + hasSkill(attacker, POLEARM_ABILITY_TWO) +
            " polearmAbilityThree=" +
                hasSkill(attacker, POLEARM_ABILITY_THREE) +
            " polearmAbilityFour=" + hasSkill(attacker, POLEARM_ABILITY_FOUR) +
            " polearmSupportOne=" + hasSkill(attacker, POLEARM_SUPPORT_ONE) +
            " polearmSupportTwo=" + hasSkill(attacker, POLEARM_SUPPORT_TWO) +
            " polearmSupportThree=" + hasSkill(attacker, POLEARM_SUPPORT_THREE) +
            " polearmSupportFour=" + hasSkill(attacker, POLEARM_SUPPORT_FOUR) +
            " polearmMaster=" + hasSkill(attacker, POLEARM_MASTER) +
            " brawlerUnarmedOne=" +
                hasSkill(attacker, BRAWLER_UNARMED_ONE) +
            " brawlerUnarmedTwo=" +
                hasSkill(attacker, BRAWLER_UNARMED_TWO) +
            " brawlerUnarmedThree=" +
                hasSkill(attacker, BRAWLER_UNARMED_THREE) +
            " brawlerUnarmedFour=" +
                hasSkill(attacker, BRAWLER_UNARMED_FOUR) +
            " brawlerMaster=" + hasSkill(attacker, BRAWLER_MASTER) +
            " riflemanAccuracyOne=" +
                hasSkill(attacker, RIFLEMAN_ACCURACY_ONE) +
            " riflemanAccuracyTwo=" +
                hasSkill(attacker, RIFLEMAN_ACCURACY_TWO) +
            " riflemanAccuracyThree=" +
                hasSkill(attacker, RIFLEMAN_ACCURACY_THREE) +
            " riflemanAccuracyFour=" +
                hasSkill(attacker, RIFLEMAN_ACCURACY_FOUR) +
            " riflemanSpeedOne=" + hasSkill(attacker, RIFLEMAN_SPEED_ONE) +
            " riflemanSpeedTwo=" + hasSkill(attacker, RIFLEMAN_SPEED_TWO) +
            " riflemanSpeedThree=" + hasSkill(attacker, RIFLEMAN_SPEED_THREE) +
            " riflemanSpeedFour=" + hasSkill(attacker, RIFLEMAN_SPEED_FOUR) +
            " riflemanAbilityOne=" + hasSkill(attacker, RIFLEMAN_ABILITY_ONE) +
            " riflemanAbilityTwo=" + hasSkill(attacker, RIFLEMAN_ABILITY_TWO) +
            " riflemanAbilityThree=" +
                hasSkill(attacker, RIFLEMAN_ABILITY_THREE) +
            " riflemanAbilityFour=" +
                hasSkill(attacker, RIFLEMAN_ABILITY_FOUR) +
            " riflemanSupportOne=" + hasSkill(attacker, RIFLEMAN_SUPPORT_ONE) +
            " riflemanSupportTwo=" + hasSkill(attacker, RIFLEMAN_SUPPORT_TWO) +
            " riflemanSupportThree=" +
                hasSkill(attacker, RIFLEMAN_SUPPORT_THREE) +
            " riflemanSupportFour=" +
                hasSkill(attacker, RIFLEMAN_SUPPORT_FOUR) +
            " riflemanMaster=" + hasSkill(attacker, RIFLEMAN_MASTER) +
            " carbineOne=" + hasSkill(attacker, MARKSMAN_CARBINE_ONE) +
            " carbineTwo=" + hasSkill(attacker, MARKSMAN_CARBINE_TWO) +
            " carbineThree=" + hasSkill(attacker, MARKSMAN_CARBINE_THREE) +
            " carbineFour=" + hasSkill(attacker, MARKSMAN_CARBINE_FOUR) +
            " carbineNovice=" + hasSkill(attacker, CARBINE_NOVICE) +
            " carbineAccuracyOne=" +
                hasSkill(attacker, CARBINE_ACCURACY_ONE) +
            " carbineAccuracyTwo=" +
                hasSkill(attacker, CARBINE_ACCURACY_TWO) +
            " carbineAccuracyThree=" +
                hasSkill(attacker, CARBINE_ACCURACY_THREE) +
            " carbineAccuracyFour=" +
                hasSkill(attacker, CARBINE_ACCURACY_FOUR) +
            " carbineSupportOne=" +
                hasSkill(attacker, CARBINE_SUPPORT_ONE) +
            " carbineSupportTwo=" +
                hasSkill(attacker, CARBINE_SUPPORT_TWO) +
            " carbineSupportThree=" +
                hasSkill(attacker, CARBINE_SUPPORT_THREE) +
            " carbineSupportFour=" +
                hasSkill(attacker, CARBINE_SUPPORT_FOUR) +
            " carbineSpeedOne=" + hasSkill(attacker, CARBINE_SPEED_ONE) +
            " carbineAbilityOne=" + hasSkill(attacker, CARBINE_ABILITY_ONE) +
            " carbineAbilityTwo=" + hasSkill(attacker, CARBINE_ABILITY_TWO) +
            " carbineAbilityThree=" +
                hasSkill(attacker, CARBINE_ABILITY_THREE) +
            " carbineAbilityFour=" +
                hasSkill(attacker, CARBINE_ABILITY_FOUR) +
            " pistolOne=" + hasSkill(attacker, MARKSMAN_PISTOL_ONE) +
            " pistolTwo=" + hasSkill(attacker, MARKSMAN_PISTOL_TWO) +
            " pistolThree=" + hasSkill(attacker, MARKSMAN_PISTOL_THREE) +
            " pistolFour=" + hasSkill(attacker, MARKSMAN_PISTOL_FOUR) +
            " pistolNovice=" + hasSkill(attacker, PISTOL_NOVICE) +
            " pistolSupportOne=" + hasSkill(attacker, PISTOL_SUPPORT_ONE) +
            " pistolSupportTwo=" + hasSkill(attacker, PISTOL_SUPPORT_TWO) +
            " pistolSupportThree=" + hasSkill(attacker, PISTOL_SUPPORT_THREE) +
            " supportOne=" + hasSkill(attacker, MARKSMAN_SUPPORT_ONE) +
            " supportTwo=" + hasSkill(attacker, MARKSMAN_SUPPORT_TWO) +
            " supportFour=" + hasSkill(attacker, MARKSMAN_SUPPORT_FOUR) +
            " hasCommand=" + hasCommand(attacker, COMMAND) +
            " durationControl=" + hasCommand(attacker, DURATION_CONTROL_COMMAND) +
            " headShotThree=" + hasCommand(attacker, HEAD_SHOT_THREE_COMMAND) +
            " bodyShotTwo=" + hasCommand(attacker, BODY_SHOT_TWO_COMMAND) +
            " bodyShotThree=" + hasCommand(attacker, BODY_SHOT_THREE_COMMAND) +
            " healthShotOne=" + hasCommand(attacker, HEALTH_SHOT_ONE_COMMAND) +
            " healthShotTwo=" + hasCommand(attacker, HEALTH_SHOT_TWO_COMMAND) +
            " pistolMeleeDefenseOne=" +
                hasCommand(attacker, PISTOL_MELEE_DEFENSE_ONE_COMMAND) +
            " pistolMeleeDefenseTwo=" +
                hasCommand(attacker, PISTOL_MELEE_DEFENSE_TWO_COMMAND) +
            " tumbleToProne=" + hasCommand(attacker, TUMBLE_TO_PRONE_COMMAND) +
            " tumbleToKneeling=" +
                hasCommand(attacker, TUMBLE_TO_KNEELING_COMMAND) +
            " tumbleToStanding=" +
                hasCommand(attacker, TUMBLE_TO_STANDING_COMMAND) +
            " actionShotOne=" + hasCommand(attacker, ACTION_SHOT_ONE_COMMAND) +
            " actionShotTwo=" + hasCommand(attacker, ACTION_SHOT_TWO_COMMAND) +
            " mindShotOne=" + hasCommand(attacker, MIND_SHOT_ONE_COMMAND) +
            " mindShotTwo=" + hasCommand(attacker, MIND_SHOT_TWO_COMMAND) +
            " surpriseShot=" + hasCommand(attacker, SURPRISE_SHOT_COMMAND) +
            " sniperShot=" + hasCommand(attacker, SNIPER_SHOT_COMMAND) +
            " concealShot=" + hasCommand(attacker, CONCEAL_SHOT_COMMAND) +
            " flurryShotOne=" + hasCommand(attacker, FLURRY_SHOT_ONE_COMMAND) +
            " flurryShotTwo=" + hasCommand(attacker, FLURRY_SHOT_TWO_COMMAND) +
            " concealTarget=" +
                (concealTargetExists ? concealTarget.toString() : "none") +
            " concealTargetExists=" + concealTargetExists +
            " concealTargetIsMob=" +
                (concealTargetExists && isMob(concealTarget)) +
            " concealTargetDistanceMeters=" + concealTargetDistance +
            " concealTargetHate=" + concealTargetHate +
            " tauntTargetTop=" +
                (isIdValid(concealTargetTop) ?
                    concealTargetTop.toString() : "none") +
            " tauntTargetDefenderHate=" + concealTargetDefenderHate +
            " cdefCertification=" + hasCommand(attacker, CDEF_CERTIFICATION) +
            " pistolCdefCertification=" +
                hasCommand(attacker, PISTOL_CDEF_CERTIFICATION) +
            " carbineCdefCertification=" +
                hasCommand(attacker, CARBINE_CDEF_CERTIFICATION) +
            " polearmCommand=" + hasCommand(attacker, POLEARM_COMMAND) +
            " polearmLegTwoCommand=" +
                hasCommand(attacker, POLEARM_LEG_TWO_COMMAND) +
            " polearmLegThreeCommand=" +
                hasCommand(attacker, POLEARM_LEG_THREE_COMMAND) +
            " polearmHitOneCommand=" +
                hasCommand(attacker, POLEARM_HIT_ONE_COMMAND) +
            " polearmHitTwoCommand=" +
                hasCommand(attacker, POLEARM_HIT_TWO_COMMAND) +
            " polearmStunTwoCommand=" +
                hasCommand(attacker, POLEARM_STUN_TWO_COMMAND) +
            " polearmSpinTwoCommand=" +
                hasCommand(attacker, POLEARM_SPIN_TWO_COMMAND) +
            " polearmAreaOneCommand=" +
                hasCommand(attacker, POLEARM_AREA_ONE_COMMAND) +
            " polearmAreaTwoCommand=" +
                hasCommand(attacker, POLEARM_AREA_TWO_COMMAND) +
            " polearmSweepOneCommand=" +
                hasCommand(attacker, POLEARM_SWEEP_ONE_COMMAND) +
            " polearmSweepTwoCommand=" +
                hasCommand(attacker, POLEARM_SWEEP_TWO_COMMAND) +
            " polearmActionHitOneCommand=" +
                hasCommand(attacker, POLEARM_ACTION_HIT_ONE_COMMAND) +
            " polearmActionHitTwoCommand=" +
                hasCommand(attacker, POLEARM_ACTION_HIT_TWO_COMMAND) +
            " polearmHitThreeCommand=" +
                hasCommand(attacker, POLEARM_HIT_THREE_COMMAND) +
            " unarmedCommand=" + hasCommand(attacker, UNARMED_COMMAND) +
            " unarmedHitOneCommand=" +
                hasCommand(attacker, UNARMED_HIT_ONE_COMMAND) +
            " unarmedHitTwoCommand=" +
                hasCommand(attacker, UNARMED_HIT_TWO_COMMAND) +
            " unarmedBodyOneCommand=" +
                hasCommand(attacker, UNARMED_BODY_ONE_COMMAND) +
            " unarmedLegOneCommand=" +
                hasCommand(attacker, UNARMED_LEG_ONE_COMMAND) +
            " unarmedSpinOneCommand=" +
                hasCommand(attacker, UNARMED_SPIN_ONE_COMMAND) +
            " unarmedSpinTwoCommand=" +
                hasCommand(attacker, UNARMED_SPIN_TWO_COMMAND) +
            " overchargeOneCommand=" +
                hasCommand(attacker, OVERCHARGE_ONE_COMMAND) +
            " overchargeTwoCommand=" +
                hasCommand(attacker, OVERCHARGE_TWO_COMMAND) +
            " pointBlankSingleOneCommand=" +
                hasCommand(attacker, POINT_BLANK_SINGLE_ONE_COMMAND) +
            " aimCommand=" + hasCommand(attacker, AIM_COMMAND) +
            " threatenShotCommand=" + hasCommand(attacker, THREATEN_SHOT_COMMAND) +
            " warningShotCommand=" + hasCommand(attacker, WARNING_SHOT_COMMAND) +
            " suppressionFireOneCommand=" +
                hasCommand(attacker, SUPPRESSION_FIRE_ONE_COMMAND) +
            " suppressionFireTwoCommand=" +
                hasCommand(attacker, SUPPRESSION_FIRE_TWO_COMMAND) +
            " rollShotCommand=" + hasCommand(attacker, ROLL_SHOT_COMMAND) +
            " diveShotCommand=" + hasCommand(attacker, DIVE_SHOT_COMMAND) +
            " kipUpShotCommand=" + hasCommand(attacker, KIP_UP_SHOT_COMMAND) +
            " takeCoverCommand=" + hasCommand(attacker, TAKE_COVER_COMMAND) +
            " fullAutoSingleOneCommand=" +
                hasCommand(attacker, FULL_AUTO_SINGLE_ONE_COMMAND) +
            " fullAutoSingleTwoCommand=" +
                hasCommand(attacker, FULL_AUTO_SINGLE_TWO_COMMAND) +
            " fullAutoAreaOneCommand=" +
                hasCommand(attacker, FULL_AUTO_AREA_ONE_COMMAND) +
            " fullAutoAreaTwoCommand=" +
                hasCommand(attacker, FULL_AUTO_AREA_TWO_COMMAND) +
            " chargeShotOneCommand=" +
                hasCommand(attacker, CHARGE_SHOT_ONE_COMMAND) +
            " chargeShotTwoCommand=" +
                hasCommand(attacker, CHARGE_SHOT_TWO_COMMAND) +
            " strafeShotOneCommand=" +
                hasCommand(attacker, STRAFE_SHOT_ONE_COMMAND) +
            " strafeShotTwoCommand=" +
                hasCommand(attacker, STRAFE_SHOT_TWO_COMMAND) +
            " startleShotOneCommand=" +
                hasCommand(attacker, STARTLE_SHOT_ONE_COMMAND) +
            " startleShotTwoCommand=" +
                hasCommand(attacker, STARTLE_SHOT_TWO_COMMAND) +
            " flushingShotOneCommand=" +
                hasCommand(attacker, FLUSHING_SHOT_ONE_COMMAND) +
            " flushingShotTwoCommand=" +
                hasCommand(attacker, FLUSHING_SHOT_TWO_COMMAND) +
            " polearmLungeOneCommand=" +
                hasCommand(attacker, POLEARM_LUNGE_ONE_COMMAND) +
            " unarmedLungeOneCommand=" +
                hasCommand(attacker, UNARMED_LUNGE_ONE_COMMAND) +
            " oneHandLungeOneCommand=" +
                hasCommand(attacker, ONE_HAND_LUNGE_ONE_COMMAND) +
            " twoHandLungeOneCommand=" +
                hasCommand(attacker, TWO_HAND_LUNGE_ONE_COMMAND) +
            " polearmLungeTwoCommand=" +
                hasCommand(attacker, POLEARM_LUNGE_TWO_COMMAND) +
            " unarmedLungeTwoCommand=" +
                hasCommand(attacker, UNARMED_LUNGE_TWO_COMMAND) +
            " oneHandLungeTwoCommand=" +
                hasCommand(attacker, ONE_HAND_LUNGE_TWO_COMMAND) +
            " twoHandLungeTwoCommand=" +
                hasCommand(attacker, TWO_HAND_LUNGE_TWO_COMMAND) +
            " tauntCommand=" + hasCommand(attacker, TAUNT_COMMAND) +
            " oneHandDizzyHitOneCommand=" +
                hasCommand(attacker, ONE_HAND_DIZZY_HIT_ONE_COMMAND) +
            " oneHandBlindHitOneCommand=" +
                hasCommand(attacker, ONE_HAND_BLIND_HIT_ONE_COMMAND) +
            " oneHandBlindHitTwoCommand=" +
                hasCommand(attacker, ONE_HAND_BLIND_HIT_TWO_COMMAND) +
            " oneHandScatterHitOneCommand=" +
                hasCommand(attacker, ONE_HAND_SCATTER_HIT_ONE_COMMAND) +
            " oneHandDizzyHitTwoCommand=" +
                hasCommand(attacker, ONE_HAND_DIZZY_HIT_TWO_COMMAND) +
            " oneHandScatterHitTwoCommand=" +
                hasCommand(attacker, ONE_HAND_SCATTER_HIT_TWO_COMMAND) +
            " oneHandHealthHitOneCommand=" +
                hasCommand(attacker, ONE_HAND_HEALTH_HIT_ONE_COMMAND) +
            " oneHandSpinAttackTwoCommand=" +
                hasCommand(attacker, ONE_HAND_SPIN_ATTACK_TWO_COMMAND) +
            " oneHandHealthHitTwoCommand=" +
                hasCommand(attacker, ONE_HAND_HEALTH_HIT_TWO_COMMAND) +
            " twoHandSweepOneCommand=" +
                hasCommand(attacker, TWO_HAND_SWEEP_ONE_COMMAND) +
            " twoHandSweepTwoCommand=" +
                hasCommand(attacker, TWO_HAND_SWEEP_TWO_COMMAND) +
            " twoHandMindHitOneCommand=" +
                hasCommand(attacker, TWO_HAND_MIND_HIT_ONE_COMMAND) +
            " twoHandMindHitTwoCommand=" +
                hasCommand(attacker, TWO_HAND_MIND_HIT_TWO_COMMAND) +
            " twoHandHitThreeCommand=" +
                hasCommand(attacker, TWO_HAND_HIT_THREE_COMMAND) +
            " polearmStunOneCommand=" +
                hasCommand(attacker, POLEARM_STUN_ONE_COMMAND) +
            " unarmedBlindOneCommand=" +
                hasCommand(attacker, UNARMED_BLIND_ONE_COMMAND) +
            " unarmedStunOneCommand=" +
                hasCommand(attacker, UNARMED_STUN_ONE_COMMAND) +
            " intimidateOneCommand=" +
                hasCommand(attacker, INTIMIDATE_ONE_COMMAND) +
            " intimidateTwoCommand=" +
                hasCommand(attacker, INTIMIDATE_TWO_COMMAND) +
            " warcryOneCommand=" +
                hasCommand(attacker, WARCRY_ONE_COMMAND) +
            " warcryTwoCommand=" +
                hasCommand(attacker, WARCRY_TWO_COMMAND) +
            " scatterShotOneCommand=" +
                hasCommand(attacker, SCATTER_SHOT_ONE_COMMAND) +
            " scatterShotTwoCommand=" +
                hasCommand(attacker, SCATTER_SHOT_TWO_COMMAND) +
            " wildShotOneCommand=" +
                hasCommand(attacker, WILD_SHOT_ONE_COMMAND) +
            " wildShotTwoCommand=" +
                hasCommand(attacker, WILD_SHOT_TWO_COMMAND) +
            " legShotTwoCommand=" + hasCommand(attacker, LEG_SHOT_TWO_COMMAND) +
            " legShotThreeCommand=" +
                hasCommand(attacker, LEG_SHOT_THREE_COMMAND) +
            " acidSingleOneCommand=" +
                hasCommand(attacker, ACID_SINGLE_ONE_COMMAND) +
            " acidConeOneCommand=" +
                hasCommand(attacker, ACID_CONE_ONE_COMMAND) +
            " acidConeTwoCommand=" +
                hasCommand(attacker, ACID_CONE_TWO_COMMAND) +
            " acidSingleTwoCommand=" +
                hasCommand(attacker, ACID_SINGLE_TWO_COMMAND) +
            " flameSingleOneCommand=" +
                hasCommand(attacker, FLAME_SINGLE_ONE_COMMAND) +
            " flameSingleTwoCommand=" +
                hasCommand(attacker, FLAME_SINGLE_TWO_COMMAND) +
            " flameConeOneCommand=" +
                hasCommand(attacker, FLAME_CONE_ONE_COMMAND) +
            " flameConeTwoCommand=" +
                hasCommand(attacker, FLAME_CONE_TWO_COMMAND) +
            " lightningSingleOneCommand=" +
                hasCommand(attacker, LIGHTNING_SINGLE_ONE_COMMAND) +
            " lightningConeOneCommand=" +
                hasCommand(attacker, LIGHTNING_CONE_ONE_COMMAND) +
            " lightningConeTwoCommand=" +
                hasCommand(attacker, LIGHTNING_CONE_TWO_COMMAND) +
            " lightningSingleTwoCommand=" +
                hasCommand(attacker, LIGHTNING_SINGLE_TWO_COMMAND) +
            " polearmAreaCommand=" +
                hasCommand(attacker, POLEARM_AREA_COMMAND) +
            " oneHandAreaCommand=" +
                hasCommand(attacker, ONE_HAND_AREA_COMMAND) +
            " oneHandBodyOneCommand=" +
                hasCommand(attacker, ONE_HAND_BODY_ONE_COMMAND) +
            " oneHandBodyTwoCommand=" +
                hasCommand(attacker, ONE_HAND_BODY_TWO_COMMAND) +
            " oneHandBodyThreeCommand=" +
                hasCommand(attacker, ONE_HAND_BODY_THREE_COMMAND) +
            " oneHandHitOneCommand=" +
                hasCommand(attacker, ONE_HAND_HIT_ONE_COMMAND) +
            " oneHandHitTwoCommand=" +
                hasCommand(attacker, ONE_HAND_HIT_TWO_COMMAND) +
            " oneHandHitThreeCommand=" +
                hasCommand(attacker, ONE_HAND_HIT_THREE_COMMAND) +
            " twoHandAreaCommand=" +
                hasCommand(attacker, TWO_HAND_AREA_COMMAND) +
            " twoHandAreaTwoCommand=" +
                hasCommand(attacker, TWO_HAND_AREA_TWO_COMMAND) +
            " twoHandAccuracyAreaOneCommand=" +
                hasCommand(attacker, TWO_HAND_ACCURACY_AREA_ONE_COMMAND) +
            " twoHandAccuracyAreaTwoCommand=" +
                hasCommand(attacker, TWO_HAND_ACCURACY_AREA_TWO_COMMAND) +
            " twoHandAccuracyAreaThreeCommand=" +
                hasCommand(attacker, TWO_HAND_ACCURACY_AREA_THREE_COMMAND) +
            " twoHandHeadOneCommand=" +
                hasCommand(attacker, TWO_HAND_HEAD_ONE_COMMAND) +
            " twoHandHeadTwoCommand=" +
                hasCommand(attacker, TWO_HAND_HEAD_TWO_COMMAND) +
            " twoHandHeadThreeCommand=" +
                hasCommand(attacker, TWO_HAND_HEAD_THREE_COMMAND) +
            " twoHandHitOneCommand=" +
                hasCommand(attacker, TWO_HAND_HIT_ONE_COMMAND) +
            " twoHandHitTwoCommand=" +
                hasCommand(attacker, TWO_HAND_HIT_TWO_COMMAND) +
            " polearmCertification=" +
                hasCommand(attacker, POLEARM_CERTIFICATION) +
            " oneHandCertification=" +
                hasCommand(attacker, ONE_HAND_CERTIFICATION) +
            " twoHandCertification=" +
                hasCommand(attacker, TWO_HAND_CERTIFICATION) +
            " acidCertification=" + hasCommand(attacker, ACID_CERTIFICATION) +
            " flameCertification=" + hasCommand(attacker, FLAME_CERTIFICATION) +
            " lightningCertification=" + hasCommand(attacker, LIGHTNING_CERTIFICATION) +
            " combatActions=" + hasScript(attacker, COMBAT_ACTIONS_SCRIPT) +
            " weapon=" + weaponTemplate +
            " canPerformAction=" + canPerformAction +
            " canPerformHeadShotTwo=" + canPerformHeadShotTwo +
            " canPerformHeadShotThree=" + canPerformHeadShotThree +
            " canPerformBodyShotTwo=" + canPerformBodyShotTwo +
            " canPerformBodyShotThree=" + canPerformBodyShotThree +
            " canPerformHealthShotOne=" + canPerformHealthShotOne +
            " canPerformHealthShotTwo=" + canPerformHealthShotTwo +
            " canPerformPistolMeleeDefenseOne=" +
                canPerformPistolMeleeDefenseOne +
            " canPerformPistolMeleeDefenseTwo=" +
                canPerformPistolMeleeDefenseTwo +
            " canPerformTumbleToProne=" + canPerformTumbleToProne +
            " canPerformTumbleToKneeling=" + canPerformTumbleToKneeling +
            " canPerformTumbleToStanding=" + canPerformTumbleToStanding +
            " canPerformActionShotOne=" + canPerformActionShotOne +
            " canPerformActionShotTwo=" + canPerformActionShotTwo +
            " canPerformMindShotOne=" + canPerformMindShotOne +
            " canPerformMindShotTwo=" + canPerformMindShotTwo +
            " canPerformSurpriseShot=" + canPerformSurpriseShot +
            " canPerformSniperShot=" + canPerformSniperShot +
            " canPerformConcealShot=" + canPerformConcealShot +
            " canPerformPolearm=" + canPerformPolearm +
            " canPerformPolearmLegTwo=" + canPerformPolearmLegTwo +
            " canPerformPolearmLegThree=" + canPerformPolearmLegThree +
            " canPerformPolearmHitOne=" + canPerformPolearmHitOne +
            " canPerformPolearmHitTwo=" + canPerformPolearmHitTwo +
            " canPerformPolearmStunTwo=" + canPerformPolearmStunTwo +
            " canPerformPolearmSpinTwo=" + canPerformPolearmSpinTwo +
            " canPerformPolearmAreaOne=" + canPerformPolearmAreaOne +
            " canPerformPolearmAreaTwo=" + canPerformPolearmAreaTwo +
            " canPerformPolearmSweepOne=" + canPerformPolearmSweepOne +
            " canPerformPolearmSweepTwo=" + canPerformPolearmSweepTwo +
            " canPerformPolearmActionHitOne=" +
                canPerformPolearmActionHitOne +
            " canPerformPolearmActionHitTwo=" +
                canPerformPolearmActionHitTwo +
            " canPerformPolearmHitThree=" + canPerformPolearmHitThree +
            " canPerformUnarmed=" + canPerformUnarmed +
            " canPerformUnarmedHitOne=" + canPerformUnarmedHitOne +
            " canPerformUnarmedHitTwo=" + canPerformUnarmedHitTwo +
            " canPerformUnarmedBodyOne=" + canPerformUnarmedBodyOne +
            " canPerformUnarmedLegOne=" + canPerformUnarmedLegOne +
            " canPerformUnarmedSpinOne=" + canPerformUnarmedSpinOne +
            " canPerformUnarmedSpinTwo=" + canPerformUnarmedSpinTwo +
            " canPerformOverchargeOne=" + canPerformOverchargeOne +
            " canPerformOverchargeTwo=" + canPerformOverchargeTwo +
            " canPerformPointBlankSingleOne=" + canPerformPointBlankSingleOne +
            " canPerformAim=" + canPerformAim +
            " canPerformThreatenShot=" + canPerformThreatenShot +
            " canPerformWarningShot=" + canPerformWarningShot +
            " canPerformSuppressionFireOne=" + canPerformSuppressionFireOne +
            " canPerformSuppressionFireTwo=" + canPerformSuppressionFireTwo +
            " canPerformRollShot=" + canPerformRollShot +
            " canPerformDiveShot=" + canPerformDiveShot +
            " canPerformKipUpShot=" + canPerformKipUpShot +
            " canPerformTakeCover=" + canPerformTakeCover +
            " canPerformFullAutoSingleOne=" + canPerformFullAutoSingleOne +
            " canPerformFullAutoSingleTwo=" + canPerformFullAutoSingleTwo +
            " canPerformFullAutoAreaOne=" + canPerformFullAutoAreaOne +
            " canPerformFullAutoAreaTwo=" + canPerformFullAutoAreaTwo +
            " canPerformChargeShotOne=" + canPerformChargeShotOne +
            " canPerformChargeShotTwo=" + canPerformChargeShotTwo +
            " canPerformStrafeShotOne=" + canPerformStrafeShotOne +
            " canPerformStrafeShotTwo=" + canPerformStrafeShotTwo +
            " canPerformStartleShotOne=" + canPerformStartleShotOne +
            " canPerformScatterShotOne=" + canPerformScatterShotOne +
            " canPerformScatterShotTwo=" + canPerformScatterShotTwo +
            " canPerformWildShotOne=" + canPerformWildShotOne +
            " canPerformWildShotTwo=" + canPerformWildShotTwo +
            " canPerformLegShotTwo=" + canPerformLegShotTwo +
            " canPerformLegShotThree=" + canPerformLegShotThree +
            " canPerformAcidSingleOne=" + canPerformAcidSingleOne +
            " canPerformAcidConeOne=" + canPerformAcidConeOne +
            " canPerformAcidConeTwo=" + canPerformAcidConeTwo +
            " canPerformAcidSingleTwo=" + canPerformAcidSingleTwo +
            " canPerformFlameSingleOne=" + canPerformFlameSingleOne +
            " canPerformFlameSingleTwo=" + canPerformFlameSingleTwo +
            " canPerformFlameConeOne=" + canPerformFlameConeOne +
            " canPerformFlameConeTwo=" + canPerformFlameConeTwo +
            " canPerformLightningSingleOne=" + canPerformLightningSingleOne +
            " canPerformLightningConeOne=" + canPerformLightningConeOne +
            " canPerformLightningConeTwo=" + canPerformLightningConeTwo +
            " canPerformLightningSingleTwo=" + canPerformLightningSingleTwo +
            " canPerformPolearmArea=" + canPerformPolearmArea +
            " canPerformOneHandArea=" + canPerformOneHandArea +
            " canPerformOneHandBodyOne=" + canPerformOneHandBodyOne +
            " canPerformOneHandBodyTwo=" + canPerformOneHandBodyTwo +
            " canPerformOneHandBodyThree=" + canPerformOneHandBodyThree +
            " canPerformOneHandHitOne=" + canPerformOneHandHitOne +
            " canPerformOneHandHitTwo=" + canPerformOneHandHitTwo +
            " canPerformOneHandHitThree=" + canPerformOneHandHitThree +
            " canPerformTwoHandArea=" + canPerformTwoHandArea +
            " canPerformTwoHandAreaTwo=" + canPerformTwoHandAreaTwo +
            " canPerformTwoHandAccuracyAreaOne=" +
                canPerformTwoHandAccuracyAreaOne +
            " canPerformTwoHandAccuracyAreaTwo=" +
                canPerformTwoHandAccuracyAreaTwo +
            " canPerformTwoHandAccuracyAreaThree=" +
                canPerformTwoHandAccuracyAreaThree +
            " canPerformTwoHandHeadOne=" + canPerformTwoHandHeadOne +
            " canPerformTwoHandHeadTwo=" + canPerformTwoHandHeadTwo +
            " canPerformTwoHandHeadThree=" + canPerformTwoHandHeadThree +
            " canPerformTwoHandHitOne=" + canPerformTwoHandHitOne +
            " canPerformTwoHandHitTwo=" + canPerformTwoHandHitTwo +
            " fixtureWeapon=" + fixtureWeapon +
            " fixturePistol=" + fixturePistol +
            " fixtureCarbine=" + fixtureCarbine +
            " fixturePolearm=" + fixturePolearm +
            " fixtureOneHand=" + fixtureOneHand +
            " fixtureTwoHand=" + fixtureTwoHand +
            " fixtureAcid=" + fixtureAcid +
            " fixtureFlame=" + fixtureFlame +
            " fixtureLightning=" + fixtureLightning +
            " flamePrecuHamCostModel=" +
                (flameData == null ? -1 : flameData.precuHamCostModel) +
            " flameWeaponType=" +
                (flameWeaponData == null ? -1 : flameWeaponData.weaponType) +
            " flameHealthCost=" + flameCosts[0] +
            " flameActionCost=" + flameCosts[1] +
            " flameMindCost=" + flameCosts[2] +
            " fireDotStrength=" + dot.getDotStrength(defender, fireDotId) +
            " fireDotDuration=" + dot.getDotDuration(defender, fireDotId) +
            " healthShotDotAttribute=" +
                (healthShotData == null ? -2 : healthShotData.precuDotAttribute) +
            " healthShotTwoDotAttribute=" +
                (healthShotTwoData == null ? -2 :
                    healthShotTwoData.precuDotAttribute) +
            " healthShotTwoDotIntensity=" +
                (healthShotTwoData == null ? -1 :
                    healthShotTwoData.dotIntensity) +
            " healthShotTwoDotDuration=" +
                (healthShotTwoData == null ? -1 :
                    healthShotTwoData.dotDuration) +
            " healthShotTwoDamageMultiplier=" +
                (healthShotTwoData == null ? -1.0f :
                    healthShotTwoData.percentAddFromWeapon) +
            " pistolMeleeDefenseOneDamageMultiplier=" +
                (pistolMeleeDefenseOneData == null ? -1.0f :
                    pistolMeleeDefenseOneData.percentAddFromWeapon) +
            " pistolMeleeDefenseOneKnockdownChance=" +
                (pistolMeleeDefenseOneData == null ? -1 :
                    pistolMeleeDefenseOneData.precuKnockdownChance) +
            " pistolMeleeDefenseTwoDamageMultiplier=" +
                (pistolMeleeDefenseTwoData == null ? -1.0f :
                    pistolMeleeDefenseTwoData.percentAddFromWeapon) +
            " pistolMeleeDefenseTwoKnockdownChance=" +
                (pistolMeleeDefenseTwoData == null ? -1 :
                    pistolMeleeDefenseTwoData.precuKnockdownChance) +
            " actionShotDotAttribute=" +
                (actionShotData == null ? -2 : actionShotData.precuDotAttribute) +
            " actionShotPostureDownChance=" +
                (actionShotData == null ? -1 :
                    actionShotData.precuPostureDownChance) +
            " actionShotTwoDotAttribute=" +
                (actionShotTwoData == null ? -2 :
                    actionShotTwoData.precuDotAttribute) +
            " actionShotTwoPostureDownChance=" +
                (actionShotTwoData == null ? -1 :
                    actionShotTwoData.precuPostureDownChance) +
            " mindShotDotAttribute=" +
                (mindShotData == null ? -2 : mindShotData.precuDotAttribute) +
            " healthShotHealthCost=" + healthShotCosts[0] +
            " healthShotActionCost=" + healthShotCosts[1] +
            " healthShotMindCost=" + healthShotCosts[2] +
            " healthShotTwoHealthCost=" + healthShotTwoCosts[0] +
            " healthShotTwoActionCost=" + healthShotTwoCosts[1] +
            " healthShotTwoMindCost=" + healthShotTwoCosts[2] +
            " pistolMeleeDefenseOneHealthCost=" +
                pistolMeleeDefenseOneCosts[0] +
            " pistolMeleeDefenseOneActionCost=" +
                pistolMeleeDefenseOneCosts[1] +
            " pistolMeleeDefenseOneMindCost=" +
                pistolMeleeDefenseOneCosts[2] +
            " pistolMeleeDefenseTwoHealthCost=" +
                pistolMeleeDefenseTwoCosts[0] +
            " pistolMeleeDefenseTwoActionCost=" +
                pistolMeleeDefenseTwoCosts[1] +
            " pistolMeleeDefenseTwoMindCost=" +
                pistolMeleeDefenseTwoCosts[2] +
            " actionShotHealthCost=" + actionShotCosts[0] +
            " actionShotActionCost=" + actionShotCosts[1] +
            " actionShotMindCost=" + actionShotCosts[2] +
            " actionShotTwoHealthCost=" + actionShotTwoCosts[0] +
            " actionShotTwoActionCost=" + actionShotTwoCosts[1] +
            " actionShotTwoMindCost=" + actionShotTwoCosts[2] +
            " overchargeOneHealthCost=" + overchargeOneCosts[0] +
            " overchargeOneActionCost=" + overchargeOneCosts[1] +
            " overchargeOneMindCost=" + overchargeOneCosts[2] +
            " pointBlankSingleOneHealthCost=" + pointBlankSingleOneCosts[0] +
            " pointBlankSingleOneActionCost=" + pointBlankSingleOneCosts[1] +
            " pointBlankSingleOneMindCost=" + pointBlankSingleOneCosts[2] +
            " aimHealthCost=" + aimCosts[0] +
            " aimActionCost=" + aimCosts[1] +
            " aimMindCost=" + aimCosts[2] +
            " aimState=" + getState(attacker, STATE_AIMING) +
            " aimModifierPresent=" +
                hasSkillModModifier(attacker, combat_base.PRECU_AIM_MODIFIER) +
            " aimPrivateBonus=" +
                getSkillStatisticModifier(attacker, "private_aim") +
            " aimConfiguredBonus=" +
                readDiagnosticInt(attacker, "aim.bonus", -1) +
            " aimAlreadyActive=" +
                readDiagnosticInt(attacker, "aim.alreadyActive", -1) +
            " aimConsumed=" +
                readDiagnosticInt(attacker, "aim.consumed", -1) +
            " aimExpired=" +
                readDiagnosticInt(attacker, "aim.expired", -1) +
            " tumbleState=" + getState(attacker, STATE_TUMBLING) +
            " tumbleMeleeModifierPresent=" + hasSkillModModifier(
                attacker, combat_base.PRECU_TUMBLE_MELEE_MODIFIER) +
            " tumbleRangedModifierPresent=" + hasSkillModModifier(
                attacker, combat_base.PRECU_TUMBLE_RANGED_MODIFIER) +
            " tumbleCommand=" +
                readDiagnosticString(attacker, "tumble.command", "none") +
            " tumbleActionCost=" +
                readDiagnosticInt(attacker, "tumble.actionCost", -1) +
            " tumbleActionBefore=" +
                readDiagnosticInt(attacker, "tumble.actionBefore", -1) +
            " tumbleActionAfter=" +
                readDiagnosticInt(attacker, "tumble.actionAfter", -1) +
            " tumbleStartPosture=" +
                readDiagnosticInt(attacker, "tumble.startPosture", -1) +
            " tumbleEndPosture=" +
                readDiagnosticInt(attacker, "tumble.endPosture", -1) +
            " tumbleAnimation=" +
                readDiagnosticString(attacker, "tumble.animation", "none") +
            " tumbleDizzyRoll=" +
                readDiagnosticInt(attacker, "tumble.dizzyRoll", -1) +
            " tumbleResult=" +
                readDiagnosticString(attacker, "tumble.result", "none") +
            " tumbleMeleeDefenseBefore=" + readDiagnosticInt(
                attacker, "tumble.meleeDefenseBefore", -1) +
            " tumbleMeleeDefenseAfter=" + readDiagnosticInt(
                attacker, "tumble.meleeDefenseAfter", -1) +
            " tumbleRangedDefenseBefore=" + readDiagnosticInt(
                attacker, "tumble.rangedDefenseBefore", -1) +
            " tumbleRangedDefenseAfter=" + readDiagnosticInt(
                attacker, "tumble.rangedDefenseAfter", -1) +
            " tumbleModifiersApplied=" + readDiagnosticInt(
                attacker, "tumble.modifiersApplied", -1) +
            " tumbleMeleeDefenseBonus=" + readDiagnosticInt(
                attacker, "tumble.meleeDefenseBonus", -1) +
            " tumbleRangedDefenseBonus=" + readDiagnosticInt(
                attacker, "tumble.rangedDefenseBonus", -1) +
            " tumbleInitialState=" +
                readDiagnosticInt(attacker, "tumble.initialState", -1) +
            " tumbleDiagnosticState=" +
                readDiagnosticInt(attacker, "tumble.state", -1) +
            " tumbleBuffSeconds=" +
                readDiagnosticFloat(attacker, "tumble.buffSeconds", -1.0f) +
            " tumbleExpired=" +
                readDiagnosticInt(attacker, "tumble.expired", -1) +
            " diagnosticAccuracyPrivate=" +
                readDiagnosticInt(attacker, "primary.accuracyPrivate", -1) +
            " threatenShotHealthCost=" + threatenShotCosts[0] +
            " threatenShotActionCost=" + threatenShotCosts[1] +
            " threatenShotMindCost=" + threatenShotCosts[2] +
            " warningShotHealthCost=" + warningShotCosts[0] +
            " warningShotActionCost=" + warningShotCosts[1] +
            " warningShotMindCost=" + warningShotCosts[2] +
            " suppressionFireOneHealthCost=" + suppressionFireOneCosts[0] +
            " suppressionFireOneActionCost=" + suppressionFireOneCosts[1] +
            " suppressionFireOneMindCost=" + suppressionFireOneCosts[2] +
            " suppressionFireOnePostureDownChance=" +
                (suppressionFireOneData == null ? -1 :
                    suppressionFireOneData.precuPostureDownChance) +
            " suppressionFireTwoHealthCost=" + suppressionFireTwoCosts[0] +
            " suppressionFireTwoActionCost=" + suppressionFireTwoCosts[1] +
            " suppressionFireTwoMindCost=" + suppressionFireTwoCosts[2] +
            " suppressionFireTwoPostureDownChance=" +
                (suppressionFireTwoData == null ? -1 :
                    suppressionFireTwoData.precuPostureDownChance) +
            " rollShotHealthCost=" + rollShotCosts[0] +
            " rollShotActionCost=" + rollShotCosts[1] +
            " rollShotMindCost=" + rollShotCosts[2] +
            " diveShotHealthCost=" + diveShotCosts[0] +
            " diveShotActionCost=" + diveShotCosts[1] +
            " diveShotMindCost=" + diveShotCosts[2] +
            " kipUpShotHealthCost=" + kipUpShotCosts[0] +
            " kipUpShotActionCost=" + kipUpShotCosts[1] +
            " kipUpShotMindCost=" + kipUpShotCosts[2] +
            " takeCoverState=" + getState(attacker, STATE_COVER) +
            " takeCoverActionCost=" + calculateTakeCoverActionCost(attacker) +
            " takeCoverSkillMod=" +
                getSkillStatisticModifier(attacker, "take_cover") +
            " takeCoverDiagnosticActionCost=" +
                readDiagnosticInt(attacker, "takeCover.actionCost", -1) +
            " takeCoverDiagnosticActionBefore=" +
                readDiagnosticInt(attacker, "takeCover.actionBefore", -1) +
            " takeCoverDiagnosticActionAfter=" +
                readDiagnosticInt(attacker, "takeCover.actionAfter", -1) +
            " takeCoverDiagnosticChance=" +
                readDiagnosticInt(attacker, "takeCover.chance", -1) +
            " takeCoverDiagnosticRoll=" +
                readDiagnosticInt(attacker, "takeCover.roll", -1) +
            " takeCoverDiagnosticResult=" +
                readDiagnosticString(attacker, "takeCover.result", "none") +
            " takeCoverDiagnosticState=" +
                readDiagnosticInt(attacker, "takeCover.coverState", -1) +
            " fullAutoSingleOneHealthCost=" + fullAutoSingleOneCosts[0] +
            " fullAutoSingleOneActionCost=" + fullAutoSingleOneCosts[1] +
            " fullAutoSingleOneMindCost=" + fullAutoSingleOneCosts[2] +
            " fullAutoSingleOnePrecuHamCostModel=" +
                (fullAutoSingleOneData == null ? -1 :
                    fullAutoSingleOneData.precuHamCostModel) +
            " fullAutoSingleTwoHealthCost=" + fullAutoSingleTwoCosts[0] +
            " fullAutoSingleTwoActionCost=" + fullAutoSingleTwoCosts[1] +
            " fullAutoSingleTwoMindCost=" + fullAutoSingleTwoCosts[2] +
            " fullAutoSingleTwoPrecuHamCostModel=" +
                (fullAutoSingleTwoData == null ? -1 :
                    fullAutoSingleTwoData.precuHamCostModel) +
            " fullAutoAreaOneHealthCost=" + fullAutoAreaOneCosts[0] +
            " fullAutoAreaOneActionCost=" + fullAutoAreaOneCosts[1] +
            " fullAutoAreaOneMindCost=" + fullAutoAreaOneCosts[2] +
            " fullAutoAreaOnePrecuHamCostModel=" +
                (fullAutoAreaOneData == null ? -1 :
                    fullAutoAreaOneData.precuHamCostModel) +
            " fullAutoAreaTwoHealthCost=" + fullAutoAreaTwoCosts[0] +
            " fullAutoAreaTwoActionCost=" + fullAutoAreaTwoCosts[1] +
            " fullAutoAreaTwoMindCost=" + fullAutoAreaTwoCosts[2] +
            " fullAutoAreaTwoPrecuHamCostModel=" +
                (fullAutoAreaTwoData == null ? -1 :
                    fullAutoAreaTwoData.precuHamCostModel) +
            " chargeShotOneHealthCost=" + chargeShotOneCosts[0] +
            " chargeShotOneActionCost=" + chargeShotOneCosts[1] +
            " chargeShotOneMindCost=" + chargeShotOneCosts[2] +
            " chargeShotOnePrecuHamCostModel=" +
                (chargeShotOneData == null ? -1 :
                    chargeShotOneData.precuHamCostModel) +
            " chargeShotOneKnockdownChance=" +
                (chargeShotOneData == null ? -1 :
                    chargeShotOneData.precuKnockdownChance) +
            " chargeShotTwoHealthCost=" + chargeShotTwoCosts[0] +
            " chargeShotTwoActionCost=" + chargeShotTwoCosts[1] +
            " chargeShotTwoMindCost=" + chargeShotTwoCosts[2] +
            " chargeShotTwoPrecuHamCostModel=" +
                (chargeShotTwoData == null ? -1 :
                    chargeShotTwoData.precuHamCostModel) +
            " chargeShotTwoKnockdownChance=" +
                (chargeShotTwoData == null ? -1 :
                    chargeShotTwoData.precuKnockdownChance) +
            " strafeShotOneHealthCost=" + strafeShotOneCosts[0] +
            " strafeShotOneActionCost=" + strafeShotOneCosts[1] +
            " strafeShotOneMindCost=" + strafeShotOneCosts[2] +
            " strafeShotOnePrecuHamCostModel=" +
                (strafeShotOneData == null ? -1 :
                    strafeShotOneData.precuHamCostModel) +
            " strafeShotTwoHealthCost=" + strafeShotTwoCosts[0] +
            " strafeShotTwoActionCost=" + strafeShotTwoCosts[1] +
            " strafeShotTwoMindCost=" + strafeShotTwoCosts[2] +
            " strafeShotTwoPrecuHamCostModel=" +
                (strafeShotTwoData == null ? -1 :
                    strafeShotTwoData.precuHamCostModel) +
            " strafeShotTwoDamageMultiplier=" +
                (strafeShotTwoData == null ? -1.0f :
                    strafeShotTwoData.percentAddFromWeapon) +
            " startleShotOneHealthCost=" + startleShotOneCosts[0] +
            " startleShotOneActionCost=" + startleShotOneCosts[1] +
            " startleShotOneMindCost=" + startleShotOneCosts[2] +
            " startleShotOnePrecuHamCostModel=" +
                (startleShotOneData == null ? -1 :
                    startleShotOneData.precuHamCostModel) +
            " startleShotOneDamageMultiplier=" +
                (startleShotOneData == null ? -1.0f :
                    startleShotOneData.percentAddFromWeapon) +
            " startleShotTwoCanPerform=" + canPerformStartleShotTwo +
            " startleShotTwoHealthCost=" + startleShotTwoCosts[0] +
            " startleShotTwoActionCost=" + startleShotTwoCosts[1] +
            " startleShotTwoMindCost=" + startleShotTwoCosts[2] +
            " startleShotTwoPrecuHamCostModel=" +
                (startleShotTwoData == null ? -1 :
                    startleShotTwoData.precuHamCostModel) +
            " startleShotTwoDamageMultiplier=" +
                (startleShotTwoData == null ? -1.0f :
                    startleShotTwoData.percentAddFromWeapon) +
            " flushingShotOneCanPerform=" + canPerformFlushingShotOne +
            " flushingShotOneHealthCost=" + flushingShotOneCosts[0] +
            " flushingShotOneActionCost=" + flushingShotOneCosts[1] +
            " flushingShotOneMindCost=" + flushingShotOneCosts[2] +
            " flushingShotOnePrecuHamCostModel=" +
                (flushingShotOneData == null ? -1 :
                    flushingShotOneData.precuHamCostModel) +
            " flushingShotOneDamageMultiplier=" +
                (flushingShotOneData == null ? -1.0f :
                    flushingShotOneData.percentAddFromWeapon) +
            " flushingShotTwoCanPerform=" + canPerformFlushingShotTwo +
            " flushingShotTwoHealthCost=" + flushingShotTwoCosts[0] +
            " flushingShotTwoActionCost=" + flushingShotTwoCosts[1] +
            " flushingShotTwoMindCost=" + flushingShotTwoCosts[2] +
            " flushingShotTwoPrecuHamCostModel=" +
                (flushingShotTwoData == null ? -1 :
                    flushingShotTwoData.precuHamCostModel) +
            " flushingShotTwoDamageMultiplier=" +
                (flushingShotTwoData == null ? -1.0f :
                    flushingShotTwoData.percentAddFromWeapon) +
            " polearmLungeOneCanPerform=" + canPerformPolearmLungeOne +
            " polearmLungeOneHealthCost=" + polearmLungeOneCosts[0] +
            " polearmLungeOneActionCost=" + polearmLungeOneCosts[1] +
            " polearmLungeOneMindCost=" + polearmLungeOneCosts[2] +
            " polearmLungeOnePrecuHamCostModel=" +
                (polearmLungeOneData == null ? -1 :
                    polearmLungeOneData.precuHamCostModel) +
            " polearmLungeOneDamageMultiplier=" +
                (polearmLungeOneData == null ? -1.0f :
                    polearmLungeOneData.percentAddFromWeapon) +
            " unarmedLungeOneCanPerform=" + canPerformUnarmedLungeOne +
            " unarmedLungeOneHealthCost=" + unarmedLungeOneCosts[0] +
            " unarmedLungeOneActionCost=" + unarmedLungeOneCosts[1] +
            " unarmedLungeOneMindCost=" + unarmedLungeOneCosts[2] +
            " unarmedLungeOnePrecuHamCostModel=" +
                (unarmedLungeOneData == null ? -1 :
                    unarmedLungeOneData.precuHamCostModel) +
            " unarmedLungeOneDamageMultiplier=" +
                (unarmedLungeOneData == null ? -1.0f :
                    unarmedLungeOneData.percentAddFromWeapon) +
            " oneHandLungeOneCanPerform=" + canPerformOneHandLungeOne +
            " oneHandLungeOneHealthCost=" + oneHandLungeOneCosts[0] +
            " oneHandLungeOneActionCost=" + oneHandLungeOneCosts[1] +
            " oneHandLungeOneMindCost=" + oneHandLungeOneCosts[2] +
            " oneHandLungeOnePrecuHamCostModel=" +
                (oneHandLungeOneData == null ? -1 :
                    oneHandLungeOneData.precuHamCostModel) +
            " oneHandLungeOneDamageMultiplier=" +
                (oneHandLungeOneData == null ? -1.0f :
                    oneHandLungeOneData.percentAddFromWeapon) +
            " twoHandLungeOneCanPerform=" + canPerformTwoHandLungeOne +
            " twoHandLungeOneHealthCost=" + twoHandLungeOneCosts[0] +
            " twoHandLungeOneActionCost=" + twoHandLungeOneCosts[1] +
            " twoHandLungeOneMindCost=" + twoHandLungeOneCosts[2] +
            " twoHandLungeOnePrecuHamCostModel=" +
                (twoHandLungeOneData == null ? -1 :
                    twoHandLungeOneData.precuHamCostModel) +
            " twoHandLungeOneDamageMultiplier=" +
                (twoHandLungeOneData == null ? -1.0f :
                    twoHandLungeOneData.percentAddFromWeapon) +
            " polearmLungeTwoCanPerform=" + canPerformPolearmLungeTwo +
            " polearmLungeTwoHealthCost=" + polearmLungeTwoCosts[0] +
            " polearmLungeTwoActionCost=" + polearmLungeTwoCosts[1] +
            " polearmLungeTwoMindCost=" + polearmLungeTwoCosts[2] +
            " polearmLungeTwoPrecuHamCostModel=" +
                (polearmLungeTwoData == null ? -1 :
                    polearmLungeTwoData.precuHamCostModel) +
            " polearmLungeTwoDamageMultiplier=" +
                (polearmLungeTwoData == null ? -1.0f :
                    polearmLungeTwoData.percentAddFromWeapon) +
            " polearmLungeTwoKnockdownChance=" +
                (polearmLungeTwoData == null ? -1 :
                    polearmLungeTwoData.precuKnockdownChance) +
            " unarmedLungeTwoCanPerform=" + canPerformUnarmedLungeTwo +
            " unarmedLungeTwoHealthCost=" + unarmedLungeTwoCosts[0] +
            " unarmedLungeTwoActionCost=" + unarmedLungeTwoCosts[1] +
            " unarmedLungeTwoMindCost=" + unarmedLungeTwoCosts[2] +
            " unarmedLungeTwoPrecuHamCostModel=" +
                (unarmedLungeTwoData == null ? -1 :
                    unarmedLungeTwoData.precuHamCostModel) +
            " unarmedLungeTwoDamageMultiplier=" +
                (unarmedLungeTwoData == null ? -1.0f :
                    unarmedLungeTwoData.percentAddFromWeapon) +
            " unarmedLungeTwoKnockdownChance=" +
                (unarmedLungeTwoData == null ? -1 :
                    unarmedLungeTwoData.precuKnockdownChance) +
            " oneHandLungeTwoCanPerform=" + canPerformOneHandLungeTwo +
            " oneHandLungeTwoHealthCost=" + oneHandLungeTwoCosts[0] +
            " oneHandLungeTwoActionCost=" + oneHandLungeTwoCosts[1] +
            " oneHandLungeTwoMindCost=" + oneHandLungeTwoCosts[2] +
            " oneHandLungeTwoPrecuHamCostModel=" +
                (oneHandLungeTwoData == null ? -1 :
                    oneHandLungeTwoData.precuHamCostModel) +
            " oneHandLungeTwoDamageMultiplier=" +
                (oneHandLungeTwoData == null ? -1.0f :
                    oneHandLungeTwoData.percentAddFromWeapon) +
            " oneHandLungeTwoKnockdownChance=" +
                (oneHandLungeTwoData == null ? -1 :
                    oneHandLungeTwoData.precuKnockdownChance) +
            " twoHandLungeTwoCanPerform=" + canPerformTwoHandLungeTwo +
            " twoHandLungeTwoHealthCost=" + twoHandLungeTwoCosts[0] +
            " twoHandLungeTwoActionCost=" + twoHandLungeTwoCosts[1] +
            " twoHandLungeTwoMindCost=" + twoHandLungeTwoCosts[2] +
            " twoHandLungeTwoPrecuHamCostModel=" +
                (twoHandLungeTwoData == null ? -1 :
                    twoHandLungeTwoData.precuHamCostModel) +
            " twoHandLungeTwoDamageMultiplier=" +
                (twoHandLungeTwoData == null ? -1.0f :
                    twoHandLungeTwoData.percentAddFromWeapon) +
            " twoHandLungeTwoKnockdownChance=" +
                (twoHandLungeTwoData == null ? -1 :
                    twoHandLungeTwoData.precuKnockdownChance) +
            " tauntCanPerform=" + canPerformTaunt +
            " tauntHealthCost=" + tauntCosts[0] +
            " tauntActionCost=" + tauntCosts[1] +
            " tauntMindCost=" + tauntCosts[2] +
            " tauntPrecuHamCostModel=" +
                (tauntData == null ? -1 : tauntData.precuHamCostModel) +
            " tauntDamageMultiplier=" +
                (tauntData == null ? -1.0f :
                    tauntData.percentAddFromWeapon) +
            " tauntTargetPool=" +
                (tauntData == null ? -1 : tauntData.precuTargetPool) +
            " tauntAccuracySkill=" +
                getSkillStatisticModifier(attacker, "taunt") +
            " oneHandDizzyHitOneCanPerform=" +
                canPerformOneHandDizzyHitOne +
            " oneHandDizzyHitOneHealthCost=" +
                oneHandDizzyHitOneCosts[0] +
            " oneHandDizzyHitOneActionCost=" +
                oneHandDizzyHitOneCosts[1] +
            " oneHandDizzyHitOneMindCost=" + oneHandDizzyHitOneCosts[2] +
            " oneHandDizzyHitOnePrecuHamCostModel=" +
                (oneHandDizzyHitOneData == null ? -1 :
                    oneHandDizzyHitOneData.precuHamCostModel) +
            " oneHandDizzyHitOneDamageMultiplier=" +
                (oneHandDizzyHitOneData == null ? -1.0f :
                    oneHandDizzyHitOneData.percentAddFromWeapon) +
            " oneHandBlindHitOneCanPerform=" + canPerformOneHandBlindHitOne +
            " oneHandBlindHitOneHealthCost=" + oneHandBlindHitOneCosts[0] +
            " oneHandBlindHitOneActionCost=" + oneHandBlindHitOneCosts[1] +
            " oneHandBlindHitOneMindCost=" + oneHandBlindHitOneCosts[2] +
            " oneHandBlindHitOneDamageMultiplier=" +
                (oneHandBlindHitOneData == null ? -1.0f :
                    oneHandBlindHitOneData.percentAddFromWeapon) +
            " oneHandBlindHitOneAttackType=" +
                (oneHandBlindHitOneData == null ? -1 :
                    oneHandBlindHitOneData.attackType) +
            " oneHandBlindHitTwoCanPerform=" + canPerformOneHandBlindHitTwo +
            " oneHandBlindHitTwoHealthCost=" + oneHandBlindHitTwoCosts[0] +
            " oneHandBlindHitTwoActionCost=" + oneHandBlindHitTwoCosts[1] +
            " oneHandBlindHitTwoMindCost=" + oneHandBlindHitTwoCosts[2] +
            " oneHandBlindHitTwoDamageMultiplier=" +
                (oneHandBlindHitTwoData == null ? -1.0f :
                    oneHandBlindHitTwoData.percentAddFromWeapon) +
            " oneHandBlindHitTwoAttackType=" +
                (oneHandBlindHitTwoData == null ? -1 :
                    oneHandBlindHitTwoData.attackType) +
            " oneHandBlindHitTwoAreaRange=" +
                (oneHandBlindHitTwoData == null ? -1.0f :
                    oneHandBlindHitTwoData.coneLength) +
            " oneHandScatterHitOneCanPerform=" + canPerformOneHandScatterHitOne +
            " oneHandScatterHitOneHealthCost=" + oneHandScatterHitOneCosts[0] +
            " oneHandScatterHitOneActionCost=" + oneHandScatterHitOneCosts[1] +
            " oneHandScatterHitOneMindCost=" + oneHandScatterHitOneCosts[2] +
            " oneHandScatterHitOneDamageMultiplier=" +
                (oneHandScatterHitOneData == null ? -1.0f :
                    oneHandScatterHitOneData.percentAddFromWeapon) +
            " oneHandScatterHitOneTargetPool=" +
                (oneHandScatterHitOneData == null ? -1 :
                    oneHandScatterHitOneData.precuTargetPool) +
            " oneHandDizzyHitTwoCanPerform=" + canPerformOneHandDizzyHitTwo +
            " oneHandDizzyHitTwoHealthCost=" + oneHandDizzyHitTwoCosts[0] +
            " oneHandDizzyHitTwoActionCost=" + oneHandDizzyHitTwoCosts[1] +
            " oneHandDizzyHitTwoMindCost=" + oneHandDizzyHitTwoCosts[2] +
            " oneHandDizzyHitTwoDamageMultiplier=" +
                (oneHandDizzyHitTwoData == null ? -1.0f :
                    oneHandDizzyHitTwoData.percentAddFromWeapon) +
            " oneHandDizzyHitTwoAttackType=" +
                (oneHandDizzyHitTwoData == null ? -1 :
                    oneHandDizzyHitTwoData.attackType) +
            " oneHandDizzyHitTwoAreaRange=" +
                (oneHandDizzyHitTwoData == null ? -1.0f :
                    oneHandDizzyHitTwoData.coneLength) +
            " oneHandScatterHitTwoCanPerform=" + canPerformOneHandScatterHitTwo +
            " oneHandScatterHitTwoHealthCost=" + oneHandScatterHitTwoCosts[0] +
            " oneHandScatterHitTwoActionCost=" + oneHandScatterHitTwoCosts[1] +
            " oneHandScatterHitTwoMindCost=" + oneHandScatterHitTwoCosts[2] +
            " oneHandScatterHitTwoDamageMultiplier=" +
                (oneHandScatterHitTwoData == null ? -1.0f :
                    oneHandScatterHitTwoData.percentAddFromWeapon) +
            " oneHandScatterHitTwoTargetPool=" +
                (oneHandScatterHitTwoData == null ? -1 :
                    oneHandScatterHitTwoData.precuTargetPool) +
            " oneHandHealthHitOneCanPerform=" + canPerformOneHandHealthHitOne +
            " oneHandHealthHitOneHealthCost=" + oneHandHealthHitOneCosts[0] +
            " oneHandHealthHitOneActionCost=" + oneHandHealthHitOneCosts[1] +
            " oneHandHealthHitOneMindCost=" + oneHandHealthHitOneCosts[2] +
            " oneHandHealthHitOneDamageMultiplier=" +
                (oneHandHealthHitOneData == null ? -1.0f :
                    oneHandHealthHitOneData.percentAddFromWeapon) +
            " oneHandHealthHitOneTargetPool=" +
                (oneHandHealthHitOneData == null ? -1 :
                    oneHandHealthHitOneData.precuTargetPool) +
            " oneHandHealthHitOneDotAttribute=" +
                (oneHandHealthHitOneData == null ? -2 :
                    oneHandHealthHitOneData.precuDotAttribute) +
            " oneHandHealthHitOneDotIntensity=" +
                (oneHandHealthHitOneData == null ? -1 :
                    oneHandHealthHitOneData.dotIntensity) +
            " oneHandHealthHitOneDotDuration=" +
                (oneHandHealthHitOneData == null ? -1 :
                    oneHandHealthHitOneData.dotDuration) +
            " oneHandSpinAttackTwoCanPerform=" + canPerformOneHandSpinAttackTwo +
            " oneHandSpinAttackTwoHealthCost=" + oneHandSpinAttackTwoCosts[0] +
            " oneHandSpinAttackTwoActionCost=" + oneHandSpinAttackTwoCosts[1] +
            " oneHandSpinAttackTwoMindCost=" + oneHandSpinAttackTwoCosts[2] +
            " oneHandSpinAttackTwoDamageMultiplier=" +
                (oneHandSpinAttackTwoData == null ? -1.0f :
                    oneHandSpinAttackTwoData.percentAddFromWeapon) +
            " oneHandSpinAttackTwoAttackType=" +
                (oneHandSpinAttackTwoData == null ? -1 :
                    oneHandSpinAttackTwoData.attackType) +
            " oneHandSpinAttackTwoAreaRange=" +
                (oneHandSpinAttackTwoData == null ? -1.0f :
                    oneHandSpinAttackTwoData.coneLength) +
            " oneHandHealthHitTwoCanPerform=" + canPerformOneHandHealthHitTwo +
            " oneHandHealthHitTwoHealthCost=" + oneHandHealthHitTwoCosts[0] +
            " oneHandHealthHitTwoActionCost=" + oneHandHealthHitTwoCosts[1] +
            " oneHandHealthHitTwoMindCost=" + oneHandHealthHitTwoCosts[2] +
            " oneHandHealthHitTwoDamageMultiplier=" +
                (oneHandHealthHitTwoData == null ? -1.0f :
                    oneHandHealthHitTwoData.percentAddFromWeapon) +
            " oneHandHealthHitTwoTargetPool=" +
                (oneHandHealthHitTwoData == null ? -1 :
                    oneHandHealthHitTwoData.precuTargetPool) +
            " oneHandHealthHitTwoDotAttribute=" +
                (oneHandHealthHitTwoData == null ? -2 :
                    oneHandHealthHitTwoData.precuDotAttribute) +
            " oneHandHealthHitTwoDotIntensity=" +
                (oneHandHealthHitTwoData == null ? -1 :
                    oneHandHealthHitTwoData.dotIntensity) +
            " oneHandHealthHitTwoDotDuration=" +
                (oneHandHealthHitTwoData == null ? -1 :
                    oneHandHealthHitTwoData.dotDuration) +
            " twoHandSweepOneCanPerform=" + canPerformTwoHandSweepOne +
            " twoHandSweepTwoCanPerform=" + canPerformTwoHandSweepTwo +
            " twoHandMindHitOneCanPerform=" + canPerformTwoHandMindHitOne +
            " twoHandMindHitTwoCanPerform=" + canPerformTwoHandMindHitTwo +
            " twoHandHitThreeCanPerform=" + canPerformTwoHandHitThree +
            " twoHandHitThreeHealthCost=" + twoHandHitThreeCosts[0] +
            " twoHandHitThreeActionCost=" + twoHandHitThreeCosts[1] +
            " twoHandHitThreeMindCost=" + twoHandHitThreeCosts[2] +
            " twoHandHitThreePrecuHamCostModel=" +
                (twoHandHitThreeData == null ? -1 :
                    twoHandHitThreeData.precuHamCostModel) +
            " twoHandHitThreeDamageMultiplier=" +
                (twoHandHitThreeData == null ? -1.0f :
                    twoHandHitThreeData.percentAddFromWeapon) +
            " twoHandHitThreeTargetPool=" +
                (twoHandHitThreeData == null ? -1 :
                    twoHandHitThreeData.precuTargetPool) +
            " twoHandMindHitOneHealthCost=" + twoHandMindHitOneCosts[0] +
            " twoHandMindHitOneActionCost=" + twoHandMindHitOneCosts[1] +
            " twoHandMindHitOneMindCost=" + twoHandMindHitOneCosts[2] +
            " twoHandMindHitOnePrecuHamCostModel=" +
                (twoHandMindHitOneData == null ? -1 :
                    twoHandMindHitOneData.precuHamCostModel) +
            " twoHandMindHitOneDamageMultiplier=" +
                (twoHandMindHitOneData == null ? -1.0f :
                    twoHandMindHitOneData.percentAddFromWeapon) +
            " twoHandMindHitOneTargetPool=" +
                (twoHandMindHitOneData == null ? -1 :
                    twoHandMindHitOneData.precuTargetPool) +
            " twoHandMindHitOneDotAttribute=" +
                (twoHandMindHitOneData == null ? -2 :
                    twoHandMindHitOneData.precuDotAttribute) +
            " twoHandMindHitOneDotIntensity=" +
                (twoHandMindHitOneData == null ? -1 :
                    twoHandMindHitOneData.dotIntensity) +
            " twoHandMindHitOneDotDuration=" +
                (twoHandMindHitOneData == null ? -1 :
                    twoHandMindHitOneData.dotDuration) +
            " twoHandMindHitTwoHealthCost=" + twoHandMindHitTwoCosts[0] +
            " twoHandMindHitTwoActionCost=" + twoHandMindHitTwoCosts[1] +
            " twoHandMindHitTwoMindCost=" + twoHandMindHitTwoCosts[2] +
            " twoHandMindHitTwoPrecuHamCostModel=" +
                (twoHandMindHitTwoData == null ? -1 :
                    twoHandMindHitTwoData.precuHamCostModel) +
            " twoHandMindHitTwoDamageMultiplier=" +
                (twoHandMindHitTwoData == null ? -1.0f :
                    twoHandMindHitTwoData.percentAddFromWeapon) +
            " twoHandMindHitTwoTargetPool=" +
                (twoHandMindHitTwoData == null ? -1 :
                    twoHandMindHitTwoData.precuTargetPool) +
            " twoHandMindHitTwoDotAttribute=" +
                (twoHandMindHitTwoData == null ? -2 :
                    twoHandMindHitTwoData.precuDotAttribute) +
            " twoHandMindHitTwoDotIntensity=" +
                (twoHandMindHitTwoData == null ? -1 :
                    twoHandMindHitTwoData.dotIntensity) +
            " twoHandMindHitTwoDotDuration=" +
                (twoHandMindHitTwoData == null ? -1 :
                    twoHandMindHitTwoData.dotDuration) +
            " twoHandSweepOneHealthCost=" + twoHandSweepOneCosts[0] +
            " twoHandSweepOneActionCost=" + twoHandSweepOneCosts[1] +
            " twoHandSweepOneMindCost=" + twoHandSweepOneCosts[2] +
            " twoHandSweepOnePrecuHamCostModel=" +
                (twoHandSweepOneData == null ? -1 :
                    twoHandSweepOneData.precuHamCostModel) +
            " twoHandSweepOneDamageMultiplier=" +
                (twoHandSweepOneData == null ? -1.0f :
                    twoHandSweepOneData.percentAddFromWeapon) +
            " polearmStunOneCanPerform=" + canPerformPolearmStunOne +
            " polearmStunOneHealthCost=" + polearmStunOneCosts[0] +
            " polearmStunOneActionCost=" + polearmStunOneCosts[1] +
            " polearmStunOneMindCost=" + polearmStunOneCosts[2] +
            " polearmStunOnePrecuHamCostModel=" +
                (polearmStunOneData == null ? -1 :
                    polearmStunOneData.precuHamCostModel) +
            " polearmStunOneDamageMultiplier=" +
                (polearmStunOneData == null ? -1.0f :
                    polearmStunOneData.percentAddFromWeapon) +
            " unarmedBlindOneCanPerform=" + canPerformUnarmedBlindOne +
            " unarmedBlindOneHealthCost=" + unarmedBlindOneCosts[0] +
            " unarmedBlindOneActionCost=" + unarmedBlindOneCosts[1] +
            " unarmedBlindOneMindCost=" + unarmedBlindOneCosts[2] +
            " unarmedBlindOnePrecuHamCostModel=" +
                (unarmedBlindOneData == null ? -1 :
                    unarmedBlindOneData.precuHamCostModel) +
            " unarmedBlindOneDamageMultiplier=" +
                (unarmedBlindOneData == null ? -1.0f :
                    unarmedBlindOneData.percentAddFromWeapon) +
            " unarmedStunOneCanPerform=" + canPerformUnarmedStunOne +
            " unarmedStunOneHealthCost=" + unarmedStunOneCosts[0] +
            " unarmedStunOneActionCost=" + unarmedStunOneCosts[1] +
            " unarmedStunOneMindCost=" + unarmedStunOneCosts[2] +
            " unarmedStunOnePrecuHamCostModel=" +
                (unarmedStunOneData == null ? -1 :
                    unarmedStunOneData.precuHamCostModel) +
            " unarmedStunOneDamageMultiplier=" +
                (unarmedStunOneData == null ? -1.0f :
                    unarmedStunOneData.percentAddFromWeapon) +
            " intimidateOneCanPerform=" + canPerformIntimidateOne +
            " intimidateOneHealthCost=" + intimidateOneCosts[0] +
            " intimidateOneActionCost=" + intimidateOneCosts[1] +
            " intimidateOneMindCost=" + intimidateOneCosts[2] +
            " intimidateOnePrecuHamCostModel=" +
                (intimidateOneData == null ? -1 :
                    intimidateOneData.precuHamCostModel) +
            " intimidateOneDamageMultiplier=" +
                (intimidateOneData == null ? -1.0f :
                    intimidateOneData.percentAddFromWeapon) +
            " intimidateOneAccuracySkill=" +
                getSkillStatisticModifier(attacker, "intimidate") +
            " intimidateTwoCanPerform=" + canPerformIntimidateTwo +
            " intimidateTwoHealthCost=" + intimidateTwoCosts[0] +
            " intimidateTwoActionCost=" + intimidateTwoCosts[1] +
            " intimidateTwoMindCost=" + intimidateTwoCosts[2] +
            " intimidateTwoPrecuHamCostModel=" +
                (intimidateTwoData == null ? -1 :
                    intimidateTwoData.precuHamCostModel) +
            " intimidateTwoDamageMultiplier=" +
                (intimidateTwoData == null ? -1.0f :
                    intimidateTwoData.percentAddFromWeapon) +
            " intimidateTwoAccuracySkill=" +
                getSkillStatisticModifier(attacker, "intimidate") +
            " warcryOneCanPerform=" + canPerformWarcryOne +
            " warcryOneHealthCost=" + warcryOneCosts[0] +
            " warcryOneActionCost=" + warcryOneCosts[1] +
            " warcryOneMindCost=" + warcryOneCosts[2] +
            " warcryOnePrecuHamCostModel=" +
                (warcryOneData == null ? -1 :
                    warcryOneData.precuHamCostModel) +
            " warcryOneDamageMultiplier=" +
                (warcryOneData == null ? -1.0f :
                    warcryOneData.percentAddFromWeapon) +
            " warcryOneAccuracySkill=" +
                getSkillStatisticModifier(attacker, "warcry") +
            " warcryTwoCanPerform=" + canPerformWarcryTwo +
            " warcryTwoHealthCost=" + warcryTwoCosts[0] +
            " warcryTwoActionCost=" + warcryTwoCosts[1] +
            " warcryTwoMindCost=" + warcryTwoCosts[2] +
            " warcryTwoPrecuHamCostModel=" +
                (warcryTwoData == null ? -1 :
                    warcryTwoData.precuHamCostModel) +
            " warcryTwoDamageMultiplier=" +
                (warcryTwoData == null ? -1.0f :
                    warcryTwoData.percentAddFromWeapon) +
            " warcryTwoAccuracySkill=" +
                getSkillStatisticModifier(attacker, "warcry") +
            " scatterShotOneHealthCost=" + scatterShotOneCosts[0] +
            " scatterShotOneActionCost=" + scatterShotOneCosts[1] +
            " scatterShotOneMindCost=" + scatterShotOneCosts[2] +
            " scatterShotOnePrecuHamCostModel=" +
                (scatterShotOneData == null ? -1 :
                    scatterShotOneData.precuHamCostModel) +
            " scatterShotOnePoolDamageRolls=" +
                (scatterShotOneData == null ? -1 :
                    scatterShotOneData.precuPoolDamageRolls) +
            " scatterShotOnePoolDamageIncrement=" +
                (scatterShotOneData == null ? -1.0f :
                    scatterShotOneData.precuPoolDamageIncrement) +
            " scatterShotTwoHealthCost=" + scatterShotTwoCosts[0] +
            " scatterShotTwoActionCost=" + scatterShotTwoCosts[1] +
            " scatterShotTwoMindCost=" + scatterShotTwoCosts[2] +
            " scatterShotTwoPrecuHamCostModel=" +
                (scatterShotTwoData == null ? -1 :
                    scatterShotTwoData.precuHamCostModel) +
            " scatterShotTwoPoolDamageRolls=" +
                (scatterShotTwoData == null ? -1 :
                    scatterShotTwoData.precuPoolDamageRolls) +
            " scatterShotTwoPoolDamageIncrement=" +
                (scatterShotTwoData == null ? -1.0f :
                    scatterShotTwoData.precuPoolDamageIncrement) +
            " wildShotOneHealthCost=" + wildShotOneCosts[0] +
            " wildShotOneActionCost=" + wildShotOneCosts[1] +
            " wildShotOneMindCost=" + wildShotOneCosts[2] +
            " wildShotOnePrecuHamCostModel=" +
                (wildShotOneData == null ? -1 :
                    wildShotOneData.precuHamCostModel) +
            " wildShotTwoHealthCost=" + wildShotTwoCosts[0] +
            " wildShotTwoActionCost=" + wildShotTwoCosts[1] +
            " wildShotTwoMindCost=" + wildShotTwoCosts[2] +
            " wildShotTwoPrecuHamCostModel=" +
                (wildShotTwoData == null ? -1 :
                    wildShotTwoData.precuHamCostModel) +
            " legShotTwoHealthCost=" + legShotTwoCosts[0] +
            " legShotTwoActionCost=" + legShotTwoCosts[1] +
            " legShotTwoMindCost=" + legShotTwoCosts[2] +
            " legShotTwoPrecuHamCostModel=" +
                (legShotTwoData == null ? -1 :
                    legShotTwoData.precuHamCostModel) +
            " legShotThreeHealthCost=" + legShotThreeCosts[0] +
            " legShotThreeActionCost=" + legShotThreeCosts[1] +
            " legShotThreeMindCost=" + legShotThreeCosts[2] +
            " legShotThreePrecuHamCostModel=" +
                (legShotThreeData == null ? -1 :
                    legShotThreeData.precuHamCostModel) +
            " poolDamageRolls=" +
                readDiagnosticInt(attacker, "poolDamage.rolls", -1) +
            " poolDamageIncrement=" +
                readDiagnosticFloat(
                    attacker, "poolDamage.increment", -1.0f) +
            " poolDamageActiveMask=" +
                readDiagnosticInt(attacker, "poolDamage.activeMask", -1) +
            " poolDamageRoll1=" +
                readDiagnosticInt(attacker, "poolDamage.roll1", -1) +
            " poolDamageRoll2=" +
                readDiagnosticInt(attacker, "poolDamage.roll2", -1) +
            " poolDamageRoll3=" +
                readDiagnosticInt(attacker, "poolDamage.roll3", -1) +
            " poolDamageHealthMultiplier=" +
                readDiagnosticFloat(
                    attacker, "poolDamage.multiplier0", -1.0f) +
            " poolDamageActionMultiplier=" +
                readDiagnosticFloat(
                    attacker, "poolDamage.multiplier1", -1.0f) +
            " poolDamageMindMultiplier=" +
                readDiagnosticFloat(
                    attacker, "poolDamage.multiplier2", -1.0f) +
            " poolDamageHealthDirect=" +
                readDiagnosticInt(attacker, "poolDamage.direct0", -1) +
            " poolDamageActionDirect=" +
                readDiagnosticInt(attacker, "poolDamage.direct1", -1) +
            " poolDamageMindDirect=" +
                readDiagnosticInt(attacker, "poolDamage.direct2", -1) +
            " poolDamageHealthApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied0", -1) +
            " poolDamageActionApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied1", -1) +
            " poolDamageMindApplied=" +
                readDiagnosticInt(attacker, "poolDamage.applied2", -1) +
            " poolDamageTotalApplied=" +
                readDiagnosticInt(
                    attacker, "poolDamage.totalApplied", -1) +
            " defenderLevel=" + getLevel(defender) +
            " defenderDizzyDefense=" +
                getSkillStatisticModifier(defender, "dizzy_defense") +
            " defenderBlindDefense=" +
                getSkillStatisticModifier(defender, "blind_defense") +
            " defenderStunDefense=" +
                getSkillStatisticModifier(defender, "stun_defense") +
            " defenderDizzyState=" + getState(defender, STATE_DIZZY) +
            " defenderBlindState=" + getState(defender, STATE_BLINDED) +
            " defenderStunState=" + getState(defender, STATE_STUNNED) +
            " defenderIntimidateState=" +
                getState(defender, STATE_INTIMIDATED) +
            " defenderDizzyBuff=" + buff.hasBuff(defender, "dizzy") +
            " defenderBlindBuff=" + buff.hasBuff(defender, "blind") +
            " defenderStunBuff=" + buff.hasBuff(defender, "stun") +
            " defenderIntimidateBuff=" +
                buff.hasBuff(defender, "intimidate") +
            " cleanupFixtureWeapon=" +
                readDiagnosticInt(attacker, "cleanup.fixtureWeapon", -1) +
            " cleanupFixturePistol=" +
                readDiagnosticInt(attacker, "cleanup.fixturePistol", -1) +
            " cleanupFixtureCarbine=" +
                readDiagnosticInt(attacker, "cleanup.fixtureCarbine", -1) +
            " cleanupFixturePolearm=" +
                readDiagnosticInt(attacker, "cleanup.fixturePolearm", -1) +
            " cleanupFixtureOneHand=" +
                readDiagnosticInt(attacker, "cleanup.fixtureOneHand", -1) +
            " cleanupFixtureTwoHand=" +
                readDiagnosticInt(attacker, "cleanup.fixtureTwoHand", -1) +
            " cleanupFixtureAcid=" +
                readDiagnosticInt(attacker, "cleanup.fixtureAcid", -1) +
            " cleanupFixtureFlame=" +
                readDiagnosticInt(attacker, "cleanup.fixtureFlame", -1) +
            " cleanupFixtureLightning=" +
                readDiagnosticInt(attacker, "cleanup.fixtureLightning", -1) +
            " cleanupAttackerRestored=" +
                readDiagnosticInt(attacker, "cleanup.attackerRestored", -1) +
            " cleanupDefenderRestored=" +
                readDiagnosticInt(attacker, "cleanup.defenderRestored", -1) +
            " cleanupDefenderMoved=" +
                readRootInt(defender, "cleanup.moved", -1) +
            " cleanupDefenderPosture=" +
                readRootInt(defender, "cleanup.postureRestored", -1) +
            " cleanupDefenderLocomotion=" +
                readRootInt(defender, "cleanup.locomotionRestored", -1) +
            " cleanupDefenderCover=" +
                readRootInt(defender, "cleanup.coverStateRestored", -1) +
            " cleanupDefenderStateBuffs=" +
                readRootInt(defender, "cleanup.stateBuffsRestored", -1) +
            " cleanupDefenderMaxHealth=" +
                readRootInt(defender, "cleanup.maxHealthRestored", -1) +
            " cleanupDefenderMaxAction=" +
                readRootInt(defender, "cleanup.maxActionRestored", -1) +
            " cleanupDefenderMaxMind=" +
                readRootInt(defender, "cleanup.maxMindRestored", -1) +
            " cleanupDefenderWounds=" +
                readRootInt(defender, "cleanup.woundsRestored", -1) +
            " cleanupDefenderHealth=" +
                readRootInt(defender, "cleanup.healthRestored", -1) +
            " cleanupDefenderAction=" +
                readRootInt(defender, "cleanup.actionRestored", -1) +
            " cleanupDefenderMind=" +
                readRootInt(defender, "cleanup.mindRestored", -1) +
            " cleanupPistolSkills=" +
                readDiagnosticInt(attacker, "cleanup.pistolSkills", -1) +
            " cleanupPistolCommands=" +
                readDiagnosticInt(attacker, "cleanup.pistolCommands", -1) +
            " cleanupMarksmanTumbleSkills=" + readDiagnosticInt(
                attacker, "cleanup.marksmanTumbleSkills", -1) +
            " cleanupMarksmanTumbleCommands=" + readDiagnosticInt(
                attacker, "cleanup.marksmanTumbleCommands", -1) +
            " cleanupCarbineSkills=" +
                readDiagnosticInt(attacker, "cleanup.carbineSkills", -1) +
            " cleanupCarbineCommands=" +
                readDiagnosticInt(attacker, "cleanup.carbineCommands", -1) +
            " cleanupRiflemanSkills=" +
                readDiagnosticInt(attacker, "cleanup.riflemanSkills", -1) +
            " cleanupAttackerMoved=" +
                readRootInt(attacker, "cleanup.moved", -1) +
            " cleanupAttackerPosture=" +
                readRootInt(attacker, "cleanup.postureRestored", -1) +
            " cleanupAttackerLocomotion=" +
                readRootInt(attacker, "cleanup.locomotionRestored", -1) +
            " cleanupAttackerCover=" +
                readRootInt(attacker, "cleanup.coverStateRestored", -1) +
            " cleanupAttackerStateBuffs=" +
                readRootInt(attacker, "cleanup.stateBuffsRestored", -1) +
            " cleanupAttackerMaxHealth=" +
                readRootInt(attacker, "cleanup.maxHealthRestored", -1) +
            " cleanupAttackerMaxAction=" +
                readRootInt(attacker, "cleanup.maxActionRestored", -1) +
            " cleanupAttackerMaxMind=" +
                readRootInt(attacker, "cleanup.maxMindRestored", -1) +
            " cleanupAttackerWounds=" +
                readRootInt(attacker, "cleanup.woundsRestored", -1) +
            " cleanupAttackerHealth=" +
                readRootInt(attacker, "cleanup.healthRestored", -1) +
            " cleanupAttackerAction=" +
                readRootInt(attacker, "cleanup.actionRestored", -1) +
            " cleanupAttackerMind=" +
                readRootInt(attacker, "cleanup.mindRestored", -1) +
            " stateEffectAppliedCount=" +
                readDiagnosticInt(attacker, "stateEffect.appliedCount", -1) +
            " stateEffectAppliedTotal=" +
                readDiagnosticInt(attacker, "stateEffect.appliedTotal", -1) +
            " stateEffect1Type=" +
                readDiagnosticInt(attacker, "stateEffect.1.type", -1) +
            " stateEffect1Chance=" +
                readDiagnosticInt(attacker, "stateEffect.1.chance", -1) +
            " stateEffect1Strength=" +
                readDiagnosticInt(attacker, "stateEffect.1.strength", -1) +
            " stateEffect1DurationBase=" +
                readDiagnosticInt(attacker, "stateEffect.1.durationBase", -1) +
            " stateEffect1Roll=" +
                readDiagnosticInt(attacker, "stateEffect.1.roll", -1) +
            " stateEffect1Threshold=" +
                readDiagnosticInt(attacker, "stateEffect.1.threshold", -1) +
            " stateEffect1ResolvedDuration=" +
                readDiagnosticInt(
                    attacker, "stateEffect.1.resolvedDuration", -1) +
            " stateEffect1Result=" +
                readDiagnosticString(
                    attacker, "stateEffect.1.result", "none") +
            " stateEffect1StateBefore=" +
                readDiagnosticInt(attacker, "stateEffect.1.stateBefore", -1) +
            " stateEffect1StateAfter=" +
                readDiagnosticInt(attacker, "stateEffect.1.stateAfter", -1) +
            " stateEffect1DelayUntil=" +
                readDiagnosticInt(attacker, "stateEffect.1.delayUntil", -1) +
            " stateEffect1DelaySeconds=" +
                readDiagnosticInt(attacker, "stateEffect.1.delaySeconds", -1) +
            " stateEffect1AppliedRoll=" +
                readDiagnosticInt(attacker, "stateEffect.1.appliedRoll", -1) +
            " stateEffect1AppliedThreshold=" +
                readDiagnosticInt(
                    attacker, "stateEffect.1.appliedThreshold", -1) +
            " stateEffect1AppliedDuration=" +
                readDiagnosticInt(
                    attacker, "stateEffect.1.appliedDuration", -1) +
            " stateEffect1AppliedStateBefore=" +
                readDiagnosticInt(
                    attacker, "stateEffect.1.appliedStateBefore", -1) +
            " stateEffect1AppliedStateAfter=" +
                readDiagnosticInt(
                    attacker, "stateEffect.1.appliedStateAfter", -1) +
            " nextAttackDelayUntil=" + nextAttackDelayUntil +
            " nextAttackDelayRemaining=" +
                (nextAttackDelayUntil < 0 ? -1 :
                    Math.max(nextAttackDelayUntil - getGameTime(), 0)) +
            " nextAttackDelayResult=" + nextAttackDelayResult +
            " stateEffect1PlayerLevel=" +
                readDiagnosticInt(attacker, "stateEffect.1.playerLevel", -1) +
            " stateEffect1Defense=" +
                readDiagnosticInt(attacker, "stateEffect.1.defense", -1) +
            " stateEffect1JediDefense=" +
                readDiagnosticInt(attacker, "stateEffect.1.jediDefense", -1) +
            " stateEffect1Resistance=" +
                readDiagnosticInt(attacker, "stateEffect.1.resistance", -1) +
            " stateEffect2Type=" +
                readDiagnosticInt(attacker, "stateEffect.2.type", -1) +
            " stateEffect2Chance=" +
                readDiagnosticInt(attacker, "stateEffect.2.chance", -1) +
            " stateEffect2Strength=" +
                readDiagnosticInt(attacker, "stateEffect.2.strength", -1) +
            " stateEffect2DurationBase=" +
                readDiagnosticInt(attacker, "stateEffect.2.durationBase", -1) +
            " stateEffect2Roll=" +
                readDiagnosticInt(attacker, "stateEffect.2.roll", -1) +
            " stateEffect2Threshold=" +
                readDiagnosticInt(attacker, "stateEffect.2.threshold", -1) +
            " stateEffect2ResolvedDuration=" +
                readDiagnosticInt(
                    attacker, "stateEffect.2.resolvedDuration", -1) +
            " stateEffect2Result=" +
                readDiagnosticString(
                    attacker, "stateEffect.2.result", "none") +
            " stateEffect2StateBefore=" +
                readDiagnosticInt(attacker, "stateEffect.2.stateBefore", -1) +
            " stateEffect2StateAfter=" +
                readDiagnosticInt(attacker, "stateEffect.2.stateAfter", -1) +
            " stateEffect2PlayerLevel=" +
                readDiagnosticInt(attacker, "stateEffect.2.playerLevel", -1) +
            " stateEffect2Defense=" +
                readDiagnosticInt(attacker, "stateEffect.2.defense", -1) +
            " stateEffect2JediDefense=" +
                readDiagnosticInt(attacker, "stateEffect.2.jediDefense", -1) +
            " stateEffect2Resistance=" +
                readDiagnosticInt(attacker, "stateEffect.2.resistance", -1) +
            " stateEffect3Type=" +
                readDiagnosticInt(attacker, "stateEffect.3.type", -1) +
            " stateEffect3Chance=" +
                readDiagnosticInt(attacker, "stateEffect.3.chance", -1) +
            " stateEffect3Strength=" +
                readDiagnosticInt(attacker, "stateEffect.3.strength", -1) +
            " stateEffect3DurationBase=" +
                readDiagnosticInt(attacker, "stateEffect.3.durationBase", -1) +
            " stateEffect3Roll=" +
                readDiagnosticInt(attacker, "stateEffect.3.roll", -1) +
            " stateEffect3Threshold=" +
                readDiagnosticInt(attacker, "stateEffect.3.threshold", -1) +
            " stateEffect3ResolvedDuration=" +
                readDiagnosticInt(
                    attacker, "stateEffect.3.resolvedDuration", -1) +
            " stateEffect3Result=" +
                readDiagnosticString(
                    attacker, "stateEffect.3.result", "none") +
            " stateEffect3StateBefore=" +
                readDiagnosticInt(attacker, "stateEffect.3.stateBefore", -1) +
            " stateEffect3StateAfter=" +
                readDiagnosticInt(attacker, "stateEffect.3.stateAfter", -1) +
            " stateEffect3PlayerLevel=" +
                readDiagnosticInt(attacker, "stateEffect.3.playerLevel", -1) +
            " stateEffect3Defense=" +
                readDiagnosticInt(attacker, "stateEffect.3.defense", -1) +
            " stateEffect3JediDefense=" +
                readDiagnosticInt(attacker, "stateEffect.3.jediDefense", -1) +
            " stateEffect3Resistance=" +
                readDiagnosticInt(attacker, "stateEffect.3.resistance", -1) +
            " mindShotHealthCost=" + mindShotCosts[0] +
            " mindShotActionCost=" + mindShotCosts[1] +
            " mindShotMindCost=" + mindShotCosts[2] +
            " mindShotTwoHealthCost=" + mindShotTwoCosts[0] +
            " mindShotTwoActionCost=" + mindShotTwoCosts[1] +
            " mindShotTwoMindCost=" + mindShotTwoCosts[2] +
            " mindShotTwoPrecuHamCostModel=" +
                (mindShotTwoData == null ? -1 :
                    mindShotTwoData.precuHamCostModel) +
            " mindShotTwoDotAttribute=" +
                (mindShotTwoData == null ? -2 :
                    mindShotTwoData.precuDotAttribute) +
            " mindShotTwoDotIntensity=" +
                (mindShotTwoData == null ? -1 : mindShotTwoData.dotIntensity) +
            " mindShotTwoDotDuration=" +
                (mindShotTwoData == null ? -1 : mindShotTwoData.dotDuration) +
            " surpriseShotHealthCost=" + surpriseShotCosts[0] +
            " surpriseShotActionCost=" + surpriseShotCosts[1] +
            " surpriseShotMindCost=" + surpriseShotCosts[2] +
            " surpriseShotPrecuHamCostModel=" +
                (surpriseShotData == null ? -1 :
                    surpriseShotData.precuHamCostModel) +
            " sniperShotHealthCost=" + sniperShotCosts[0] +
            " sniperShotActionCost=" + sniperShotCosts[1] +
            " sniperShotMindCost=" + sniperShotCosts[2] +
            " sniperShotPrecuHamCostModel=" +
                (sniperShotData == null ? -1 :
                    sniperShotData.precuHamCostModel) +
            " sniperShotFixedMinDamage=" +
                (sniperShotData == null ? -1 :
                    sniperShotData.precuFixedMinDamage) +
            " sniperShotFixedMaxDamage=" +
                (sniperShotData == null ? -1 :
                    sniperShotData.precuFixedMaxDamage) +
            " sniperShotHitIncapacitatedTarget=" +
                (sniperShotData == null ? -1 :
                    sniperShotData.precuHitIncapacitatedTarget) +
            " concealShotHealthCost=" + concealShotCosts[0] +
            " concealShotActionCost=" + concealShotCosts[1] +
            " concealShotMindCost=" + concealShotCosts[2] +
            " concealShotPrecuHamCostModel=" +
                (concealShotData == null ? -1 :
                    concealShotData.precuHamCostModel) +
            " concealShotDamageMultiplier=" +
                (concealShotData == null ? -1.0f :
                    concealShotData.percentAddFromWeapon) +
            " canPerformFlurryShotOne=" + canPerformFlurryShotOne +
            " flurryShotOneHealthCost=" + flurryShotOneCosts[0] +
            " flurryShotOneActionCost=" + flurryShotOneCosts[1] +
            " flurryShotOneMindCost=" + flurryShotOneCosts[2] +
            " flurryShotOnePrecuHamCostModel=" +
                (flurryShotOneData == null ? -1 :
                    flurryShotOneData.precuHamCostModel) +
            " flurryShotOneDamageMultiplier=" +
                (flurryShotOneData == null ? -1.0f :
                    flurryShotOneData.percentAddFromWeapon) +
            " canPerformFlurryShotTwo=" + canPerformFlurryShotTwo +
            " flurryShotTwoHealthCost=" + flurryShotTwoCosts[0] +
            " flurryShotTwoActionCost=" + flurryShotTwoCosts[1] +
            " flurryShotTwoMindCost=" + flurryShotTwoCosts[2] +
            " flurryShotTwoPrecuHamCostModel=" +
                (flurryShotTwoData == null ? -1 :
                    flurryShotTwoData.precuHamCostModel) +
            " flurryShotTwoDamageMultiplier=" +
                (flurryShotTwoData == null ? -1.0f :
                    flurryShotTwoData.percentAddFromWeapon) +
            " bleedingDotStrength=" +
                dot.getDotStrength(defender, bleedingDotId) +
            " bleedingDotDuration=" +
                dot.getDotDuration(defender, bleedingDotId) +
            " bleedingDotAttribute=" +
                dot.getDotAttribute(defender, bleedingDotId) +
            " diagnosticSpamKey=" +
                readDiagnosticString(attacker, "spam.key", "none") +
            " diagnosticSpamResult=" +
                readDiagnosticInt(attacker, "spam.result", -1) +
            " diagnosticSpamDamage=" +
                readDiagnosticInt(attacker, "spam.damage", -1) +
            " diagnosticTargetPoolConfigured=" +
                readDiagnosticInt(attacker, "targetPool.configured", -1) +
            " diagnosticTargetPoolResolved=" +
                readDiagnosticInt(attacker, "targetPool.resolved", -1) +
            " diagnosticHitLocation=" +
                readDiagnosticInt(attacker, "armor.hitLocation", -1) +
            " diagnosticAnimation=" +
                readDiagnosticString(attacker, "animation.generated", "none") +
            " diagnosticAnimationType=" +
                readDiagnosticInt(attacker, "animation.type", -1) +
            " diagnosticConcealTarget=" +
                readDiagnosticString(attacker, "conceal.target", "none") +
            " diagnosticConcealAiTarget=" +
                readDiagnosticInt(attacker, "conceal.aiTarget", -1) +
            " diagnosticConcealDistance=" +
                readDiagnosticFloat(attacker, "conceal.distance", -1.0f) +
            " diagnosticConcealPosture=" +
                readDiagnosticInt(attacker, "conceal.posture", -1) +
            " diagnosticConcealThreshold=" +
                readDiagnosticInt(attacker, "conceal.threshold", -1) +
            " diagnosticConcealMissBefore=" +
                readDiagnosticInt(attacker, "conceal.missBefore", -1) +
            " diagnosticConcealMissAfter=" +
                readDiagnosticInt(attacker, "conceal.missAfter", -1) +
            " diagnosticConcealHateBefore=" +
                readDiagnosticFloat(attacker, "conceal.hateBefore", -1.0f) +
            " diagnosticConcealHateAfter=" +
                readDiagnosticFloat(attacker, "conceal.hateAfter", -1.0f) +
            " diagnosticConcealResult=" +
                readDiagnosticString(attacker, "conceal.result", "none") +
            " diagnosticTauntTarget=" +
                readDiagnosticString(attacker, "taunt.target", "none") +
            " diagnosticTauntEligible=" +
                readDiagnosticInt(attacker, "taunt.eligible", -1) +
            " diagnosticTauntMod=" +
                readDiagnosticInt(attacker, "taunt.mod", -1) +
            " diagnosticTauntAttackerLevel=" +
                readDiagnosticInt(attacker, "taunt.attackerLevel", -1) +
            " diagnosticTauntTargetLevel=" +
                readDiagnosticInt(attacker, "taunt.targetLevel", -1) +
            " diagnosticTauntLevelCombine=" +
                readDiagnosticInt(attacker, "taunt.levelCombine", -1) +
            " diagnosticTauntUpperBound=" +
                readDiagnosticInt(attacker, "taunt.upperBound", -1) +
            " diagnosticTauntLowerBound=" +
                readDiagnosticInt(attacker, "taunt.lowerBound", -1) +
            " diagnosticTauntUpperRoll=" +
                readDiagnosticInt(attacker, "taunt.upperRoll", -1) +
            " diagnosticTauntLowerRoll=" +
                readDiagnosticInt(attacker, "taunt.lowerRoll", -1) +
            " diagnosticTauntDuration=" +
                readDiagnosticInt(attacker, "taunt.duration", -1) +
            " diagnosticTauntAggro=" +
                readDiagnosticInt(attacker, "taunt.aggro", -1) +
            " diagnosticTauntHateBefore=" +
                readDiagnosticFloat(attacker, "taunt.hateBefore", -1.0f) +
            " diagnosticTauntTopHateBefore=" +
                readDiagnosticFloat(attacker, "taunt.topHateBefore", -1.0f) +
            " diagnosticTauntAppliedDelta=" +
                readDiagnosticFloat(attacker, "taunt.appliedDelta", -1.0f) +
            " diagnosticTauntHateAfter=" +
                readDiagnosticFloat(attacker, "taunt.hateAfter", -1.0f) +
            " diagnosticTauntTopBefore=" +
                readDiagnosticString(attacker, "taunt.topBefore", "none") +
            " diagnosticTauntTopAfter=" +
                readDiagnosticString(attacker, "taunt.topAfter", "none") +
            " diagnosticTauntResult=" +
                readDiagnosticString(attacker, "taunt.result", "none") +
            " diagnosticTauntExpired=" +
                readDiagnosticInt(attacker, "taunt.expired", -1) +
            " diagnosticTauntHateBeforeExpiry=" +
                readDiagnosticFloat(
                    attacker, "taunt.hateBeforeExpiry", -1.0f) +
            " diagnosticTauntHateAfterExpiry=" +
                readDiagnosticFloat(
                    attacker, "taunt.hateAfterExpiry", -1.0f) +
            " diagnosticTauntTopAfterExpiry=" +
                readDiagnosticString(
                    attacker, "taunt.topAfterExpiry", "none") +
            " diagnosticPostureDownChance=" +
                readDiagnosticInt(attacker, "postureDown.chance", -1) +
            " diagnosticPostureDownDefense=" +
                readDiagnosticInt(attacker, "postureDown.defense", -1) +
            " diagnosticPostureDownRoll=" +
                readDiagnosticInt(attacker, "postureDown.roll", -1) +
            " diagnosticPostureDownStart=" +
                readDiagnosticInt(attacker, "postureDown.start", -1) +
            " diagnosticPostureDownEnd=" +
                readDiagnosticInt(attacker, "postureDown.end", -1) +
            " diagnosticPostureDownResult=" +
                readDiagnosticString(attacker, "postureDown.result", "none") +
            " diagnosticPostureUpChance=" +
                readDiagnosticInt(attacker, "postureUp.chance", -1) +
            " diagnosticPostureUpDefense=" +
                readDiagnosticInt(attacker, "postureUp.defense", -1) +
            " diagnosticPostureUpRoll=" +
                readDiagnosticInt(attacker, "postureUp.roll", -1) +
            " diagnosticPostureUpThreshold=" +
                readDiagnosticInt(attacker, "postureUp.threshold", -1) +
            " diagnosticPostureUpStart=" +
                readDiagnosticInt(attacker, "postureUp.start", -1) +
            " diagnosticPostureUpEnd=" +
                readDiagnosticInt(attacker, "postureUp.end", -1) +
            " diagnosticPostureUpResult=" +
                readDiagnosticString(attacker, "postureUp.result", "none") +
            " diagnosticPostureUpAppliedRoll=" +
                readDiagnosticInt(attacker, "postureUp.appliedRoll", -1) +
            " diagnosticPostureUpAppliedThreshold=" +
                readDiagnosticInt(
                    attacker, "postureUp.appliedThreshold", -1) +
            " diagnosticPostureUpAppliedStart=" +
                readDiagnosticInt(attacker, "postureUp.appliedStart", -1) +
            " diagnosticPostureUpAppliedEnd=" +
                readDiagnosticInt(attacker, "postureUp.appliedEnd", -1) +
            " diagnosticAttackerPostureAction=" +
                readDiagnosticString(attacker, "attackerPosture.action", "none") +
            " diagnosticAttackerPostureStart=" +
                readDiagnosticInt(attacker, "attackerPosture.start", -1) +
            " diagnosticAttackerPostureEnd=" +
                readDiagnosticInt(attacker, "attackerPosture.end", -1) +
            " diagnosticAttackerPostureResult=" +
                readDiagnosticString(attacker, "attackerPosture.result", "none") +
            " postureDownRecoveryPresent=" +
                utils.hasScriptVar(
                    defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY) +
            " postureDownRecovery=" +
                (utils.hasScriptVar(
                    defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY) ?
                    utils.getIntScriptVar(
                        defender, combat_base.PRECU_POSTURE_DOWN_RECOVERY) : -1) +
            " postureUpRecoveryPresent=" +
                utils.hasScriptVar(
                    defender, combat_base.PRECU_POSTURE_UP_RECOVERY) +
            " postureUpRecovery=" +
                (utils.hasScriptVar(
                    defender, combat_base.PRECU_POSTURE_UP_RECOVERY) ?
                    utils.getIntScriptVar(
                        defender, combat_base.PRECU_POSTURE_UP_RECOVERY) : -1) +
            " diagnosticKnockdownChance=" +
                readDiagnosticInt(attacker, "knockdown.chance", -1) +
            " diagnosticKnockdownDefense=" +
                readDiagnosticInt(attacker, "knockdown.defense", -1) +
            " diagnosticKnockdownRoll=" +
                readDiagnosticInt(attacker, "knockdown.roll", -1) +
            " diagnosticKnockdownStart=" +
                readDiagnosticInt(attacker, "knockdown.start", -1) +
            " diagnosticKnockdownEnd=" +
                readDiagnosticInt(attacker, "knockdown.end", -1) +
            " diagnosticKnockdownResult=" +
                readDiagnosticString(attacker, "knockdown.result", "none") +
            " knockdownRecoveryPresent=" +
                utils.hasScriptVar(
                    defender, combat_base.PRECU_KNOCKDOWN_RECOVERY) +
            " knockdownRecovery=" +
                (utils.hasScriptVar(
                    defender, combat_base.PRECU_KNOCKDOWN_RECOVERY) ?
                    utils.getIntScriptVar(
                        defender, combat_base.PRECU_KNOCKDOWN_RECOVERY) : -1) +
            " knockdownOriginalPosturePresent=" +
                utils.hasScriptVar(
                    defender, combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE) +
            " knockdownOriginalPosture=" +
                (utils.hasScriptVar(
                    defender,
                    combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE) ?
                    utils.getIntScriptVar(
                        defender,
                        combat_base.PRECU_KNOCKDOWN_ORIGINAL_POSTURE) : -1) +
            " pvpCanAttack=" + pvpCanAttack(attacker, defender) +
            " attackerInCombat=" + combat.isInCombat(attacker) +
            " defenderInCombat=" + combat.isInCombat(defender) +
            " attackerHealth=" + getAttrib(attacker, HEALTH) +
            " attackerMaxHealth=" + getMaxAttrib(attacker, HEALTH) +
            " attackerStrength=" + getAttrib(attacker, STRENGTH) +
            " attackerMaxStrength=" + getMaxAttrib(attacker, STRENGTH) +
            " attackerAction=" + getAttrib(attacker, ACTION) +
            " attackerMaxAction=" + getMaxAttrib(attacker, ACTION) +
            " attackerQuickness=" + getAttrib(attacker, QUICKNESS) +
            " attackerMaxQuickness=" + getMaxAttrib(attacker, QUICKNESS) +
            " attackerMind=" + getAttrib(attacker, MIND) +
            " attackerMaxMind=" + getMaxAttrib(attacker, MIND) +
            " attackerFocus=" + getAttrib(attacker, FOCUS) +
            " attackerMaxFocus=" + getMaxAttrib(attacker, FOCUS) +
            " attackerHealthRegen=" + getRegenRate(attacker, HEALTH) +
            " attackerActionRegen=" + getRegenRate(attacker, ACTION) +
            " attackerMindRegen=" + getRegenRate(attacker, MIND) +
            " defenderHealth=" + getAttrib(defender, HEALTH) +
            " defenderMaxHealth=" + getMaxAttrib(defender, HEALTH) +
            " defenderAction=" + getAttrib(defender, ACTION) +
            " defenderMaxAction=" + getMaxAttrib(defender, ACTION) +
            " defenderMind=" + getAttrib(defender, MIND) +
            " defenderMaxMind=" + getMaxAttrib(defender, MIND) +
            " defenderMindRegen=" + getRegenRate(defender, MIND);
    }

    private int readDiagnosticInt(obj_id attacker, String leaf, int fallback)
        throws InterruptedException
    {
        String path = "precu.p14.marksmanTier1Fixture.liveDiagnostic." + leaf;
        return hasObjVar(attacker, path) ? getIntObjVar(attacker, path) : fallback;
    }

    private int readRootInt(obj_id player, String leaf, int fallback)
        throws InterruptedException
    {
        String path = ROOT + "." + leaf;
        return hasObjVar(player, path) ? getIntObjVar(player, path) : fallback;
    }

    private float readDiagnosticFloat(
        obj_id attacker, String leaf, float fallback)
        throws InterruptedException
    {
        String path = "precu.p14.marksmanTier1Fixture.liveDiagnostic." + leaf;
        return hasObjVar(attacker, path) ?
            getFloatObjVar(attacker, path) : fallback;
    }

    private String readDiagnosticString(obj_id attacker, String leaf, String fallback)
        throws InterruptedException
    {
        String path = "precu.p14.marksmanTier1Fixture.liveDiagnostic." + leaf;
        return hasObjVar(attacker, path) ? getStringObjVar(attacker, path) : fallback;
    }
}
