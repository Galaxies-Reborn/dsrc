package script.library;

import script.combat_engine;
import script.combat_engine.buff_data;
import script.deltadictionary;
import script.obj_id;

import java.util.Vector;

public class buff extends script.base_script
{
    public buff()
    {
    }
    public static final int MAX_EFFECTS = 5;
    public static final int GROUP_BUFF_DISTANCE = 100;
    public static final String BUFF_TABLE = "datatables/buff/buff.iff";
    public static final String DEBUFF_STATE_PARALYZED = "buff.state.paralyzed";
    public static final String AGGRO_TRANSFER_TO = "aggroBuffTransfer";
    public static boolean isPostNgeBuffProgressionRetired()
    {
        return true;
    }
    private static final String RETIRED_POST_NGE_JUNK_DEALER_EXPERTISE_EFFECT = "expertise_junk_dealer";
    public static boolean isRetiredPostNgeJunkDealerExpertiseEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_JUNK_DEALER_EXPERTISE_EFFECT);
    }
    public static boolean isRetiredPostNgeJunkDealerExpertiseBuff(buff_data data) throws InterruptedException
    {
        if (data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgeJunkDealerExpertiseEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgeJunkDealerExpertiseState(obj_id dealer) throws InterruptedException
    {
        if (!isIdValid(dealer) || !exists(dealer))
        {
            return;
        }
        utils.removeScriptVar(dealer, "junkDealerBuffer");
        String[] retiredModifiers =
        {
            "junkDealerPrecision",
            "junkDealerDamageDecrease"
        };
        for (String retiredModifier : retiredModifiers)
        {
            if (hasSkillModModifier(dealer, retiredModifier))
            {
                removeAttribOrSkillModModifier(dealer, retiredModifier);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_FORCE_SENSITIVE_STANCE_BUFFS =
    {
        "fs_buff_def_1_1",
        "fs_buff_ca_1",
        "jedi_reflect_flurry",
        "fs_saber_shackle_1",
        "fs_saber_shackle_2",
        "fs_saber_shackle_3",
        "fs_saber_shackle_4",
        "fs_soothing_aura_1",
        "fs_soothing_aura_2",
        "fs_soothing_aura_3",
        "fs_soothing_aura_4",
        "fs_anticipate_aggression_1",
        "fs_anticipate_aggression_2",
        "fs_reactive_response_1",
        "fs_reactive_response_2",
        "fs_perceptive_sentinel_1",
        "fs_perceptive_sentinel_2",
        "fs_perceptive_sentinel_3",
        "fs_perceptive_sentinel_4",
        "fs_saber_reflect",
        "fs_ruthless_precision_1",
        "fs_ruthless_precision_2",
        "fs_ruthless_precision_3",
        "fs_ruthless_precision_4",
        "fs_tempt_hatred_1",
        "fs_tempt_hatred_2",
        "fs_wracking_energy_1",
        "fs_wracking_energy_2",
        "fs_wracking_energy_3",
        "fs_wracking_energy_4",
        "fs_imp_force_drain_1",
        "fs_imp_force_drain_2",
        "fs_imp_force_drain_3",
        "fs_imp_force_drain_4",
        "invis_fs_buff_invis_1"
    };
    public static boolean isRetiredPostNgeForceSensitiveStanceBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_FORCE_SENSITIVE_STANCE_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    private static final String[] RETIRED_POST_NGE_FORCE_SENSITIVE_CHOKE_FLURRY_BUFFS =
    {
        "fs_choke_handler",
        "fs_imp_choke_1",
        "fs_imp_choke_2",
        "jedi_reflect_flurry_proc_remove",
        "attack_override_fs_dm_1|fs_flurry_1",
        "attack_override_fs_dm_2|fs_flurry_2",
        "attack_override_fs_dm_3|fs_flurry_3",
        "attack_override_fs_dm_4|fs_flurry_4",
        "attack_override_fs_dm_5|fs_flurry_5",
        "attack_override_fs_dm_6|fs_flurry_6",
        "attack_override_fs_dm_7|fs_flurry_7"
    };
    public static boolean isRetiredPostNgeForceSensitiveChokeFlurryBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_FORCE_SENSITIVE_CHOKE_FLURRY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgeForceSensitiveChokeFlurryState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_FORCE_SENSITIVE_CHOKE_FLURRY_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
    }
    public static void retirePostNgeForceSensitiveStanceState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_FORCE_SENSITIVE_STANCE_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        String[] retiredModifiers =
        {
            "stanceParry",
            "stanceEvasion",
            "stanceConstitution",
            "focusStamina",
            "focusStrength",
            "expertise_fs_force_clarity_1_proc",
            "expertise_fs_flurry_charge_proc"
        };
        for (String retiredModifier : retiredModifiers)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
        }
        utils.removeScriptVarTree(player, "expertise_stance_critical");
        utils.removeScriptVarTree(player, "stance.expertise_stance");
        utils.removeScriptVarTree(player, "stance.expertise_focus");
        combat.removeCombatBuffEffect(player, "fs_buff_def_1_1");
        combat.removeCombatBuffEffect(player, "fs_buff_ca_1");
        retirePostNgeForceSensitiveChokeFlurryState(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_FORCE_SENSITIVE_EXPERTISE_IMMUNITY_BUFFS =
    {
        "fs_sh_0",
        "fs_sh_1",
        "fs_sh_2",
        "fs_sh_3",
        "fs_dot_immunity_recourse"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_FORCE_SENSITIVE_EXPERTISE_IMMUNITY_EFFECTS =
    {
        "expertise_dot_immunity",
        "expertise_movement_immunity"
    };
    public static boolean isRetiredPostNgePlayerForceSensitiveExpertiseImmunityBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_FORCE_SENSITIVE_EXPERTISE_IMMUNITY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerForceSensitiveExpertiseImmunityEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_FORCE_SENSITIVE_EXPERTISE_IMMUNITY_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerForceSensitiveExpertiseImmunityBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerForceSensitiveExpertiseImmunityBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerForceSensitiveExpertiseImmunityEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerForceSensitiveExpertiseImmunityResidue(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, "immunity.dot.all");
        utils.removeScriptVarTree(player, "immunity.movement.snare");
        utils.removeScriptVarTree(player, "immunity.movement.root");
        stopClientEffectObjByLabel(player, "expertise_dot");
        stopClientEffectObjByLabel(player, "expertise_movement");
    }
    public static void retirePostNgePlayerForceSensitiveExpertiseImmunityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_FORCE_SENSITIVE_EXPERTISE_IMMUNITY_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        clearPostNgePlayerForceSensitiveExpertiseImmunityResidue(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_BUFFS =
    {
        "set_bonus_bh_utility_a_1",
        "set_bonus_bh_utility_a_2",
        "set_bonus_bh_utility_a_3",
        "bh_flawless_strike",
        "bh_flawless_proc_chance_1",
        "flawless_bead_1",
        "flawless_bead_2",
        "flawless_bead_3"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_MODIFIERS =
    {
        "bh_flawless_bead",
        "flawless_bead",
        "expertise_cooldown_line_bh_flawless_strike",
        "set_bonus_bh_utility_a_1",
        "set_bonus_bh_utility_a_2",
        "set_bonus_bh_utility_a_3"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_ACTIONS =
    {
        "bh_flawless_strike",
        "set_bonus_bh_utility_a_1",
        "set_bonus_bh_utility_a_2",
        "set_bonus_bh_utility_a_3"
    };
    public static boolean isRetiredPostNgePlayerBountyHunterFlawlessBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerBountyHunterFlawlessEffect(String effectName)
    {
        return effectName != null && effectName.equals("bh_flawless_proc_chance");
    }
    public static boolean isRetiredPostNgePlayerBountyHunterFlawlessBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerBountyHunterFlawlessBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerBountyHunterFlawlessEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerBountyHunterFlawlessResidue(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        if (hasBuff(player, "bh_flawless_proc_chance_1"))
        {
            removeBuff(player, "bh_flawless_proc_chance_1");
        }
        for (String retiredModifier : RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_MODIFIERS)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
            int currentValue = getSkillStatMod(player, retiredModifier);
            if (currentValue != 0)
            {
                applySkillStatisticModifier(player, retiredModifier, -currentValue);
            }
        }
        for (String retiredAction : RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_ACTIONS)
        {
            while (hasCommand(player, retiredAction))
            {
                revokeCommand(player, retiredAction);
            }
        }
    }
    public static void retirePostNgePlayerBountyHunterFlawlessState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_BOUNTY_HUNTER_FLAWLESS_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        clearPostNgePlayerBountyHunterFlawlessResidue(player);
    }
    private static final String[] RETIRED_POST_NGE_GCW_BANNER_BUFFS =
    {
        "banner_buff_commando",
        "banner_buff_smuggler",
        "banner_buff_medic",
        "banner_buff_officer",
        "banner_buff_spy",
        "banner_buff_bounty_hunter",
        "banner_buff_force_sensitive",
        "banner_buff_trader",
        "banner_buff_entertainer"
    };
    public static boolean isRetiredPostNgeGcwBannerBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_GCW_BANNER_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgeGcwBannerBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_GCW_BANNER_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_GCW_CONSUMABLE_BUFFS =
    {
        "tcg_series3_hh_15_torpedo_warhead",
        "tcg_series7_rocket_launcher",
        "gcw_mini_turret",
        "gcw_rocket_turret"
    };
    public static boolean isRetiredPostNgeGcwConsumableBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_GCW_CONSUMABLE_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgeGcwConsumableBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_GCW_CONSUMABLE_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        utils.removeScriptVarTree(player, "buff.gcwBonusGeneral");
    }
    private static final String[] RETIRED_POST_P14_PLAYER_CONTROL_IMMUNITY_BUFFS =
    {
        "action_drain_immunity",
        "dazeBlockDebuff",
        "gcw_base_critical_heal_recourse",
        "mezBlockDebuff",
        "player_armor_break_immunity",
        "player_mez_immunity",
        "player_root_immunity",
        "player_slow_immunity",
        "player_snare_immunity",
        "towHk47MoveImmuneItem",
        "towMafosaMezImmune",
        "treasure_bonus_snare_immunity"
    };
    public static boolean isRetiredPostP14PlayerControlImmunityBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_P14_PLAYER_CONTROL_IMMUNITY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostP14PlayerControlImmunityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_P14_PLAYER_CONTROL_IMMUNITY_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_P14_PLAYER_AVOID_INCAP_HEAL_BUFFS =
    {
        "gcw_base_critical_heal_a",
        "gcw_base_critical_heal_b",
        "gcw_base_critical_heal_c",
        "gcw_base_critical_heal_d",
        "gcw_base_critical_heal_e",
        "pvp_last_man_ability",
        "pvp_last_man_rebel_ability",
        "tusken_endurance"
    };
    public static boolean isRetiredPostP14PlayerAvoidIncapHealBuff(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_P14_PLAYER_AVOID_INCAP_HEAL_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostP14PlayerAvoidIncapHealState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_P14_PLAYER_AVOID_INCAP_HEAL_BUFFS)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        utils.removeScriptVar(player, "buff_handler.gcw_critical_heal");
    }
    public static boolean isRetiredPostNgeBountyHunterShieldBuff(String buffName)
    {
        return buffName != null &&
            (buffName.equals("bh_shields_handler") ||
                buffName.equals("bh_shields") ||
                buffName.equals("bh_shields_block") ||
                buffName.equals("bh_shields_charged"));
    }
    public static void retirePostNgeBountyHunterShieldState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        String[] retiredBuffs =
        {
            "bh_shields_handler",
            "bh_shields",
            "bh_shields_block",
            "bh_shields_charged"
        };
        for (String retiredBuff : retiredBuffs)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
        if (hasScript(player, "player.skill.bh_shields"))
        {
            detachScript(player, "player.skill.bh_shields");
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_BUFF_COMMAND_GRANTS =
    {
        "bh_flawless_strike",
        "co_enrage_1",
        "fs_set_heroic_taunt_1",
        "sm_how_are_you",
        "trandoshan_ability_1",
        "of_deadeye_debuff",
        "trader_heal",
        "en_action_regen"
    };
    public static boolean isRetiredPostNgePlayerBuffCommandGrant(String commandName)
    {
        if (commandName == null)
        {
            return false;
        }
        for (String retiredCommand : RETIRED_POST_NGE_PLAYER_BUFF_COMMAND_GRANTS)
        {
            if (commandName.equals(retiredCommand))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCommandGrantBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerBuffCommandGrant(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerCommandGrantBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerCommandGrantBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        for (String retiredCommand : RETIRED_POST_NGE_PLAYER_BUFF_COMMAND_GRANTS)
        {
            while (hasCommand(player, retiredCommand))
            {
                revokeCommand(player, retiredCommand);
            }
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_ACTION_DRAIN_EFFECT = "immediate_action_drain";
    public static boolean isRetiredPostNgePlayerActionDrainEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_ACTION_DRAIN_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerActionDrainBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerActionDrainEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerActionDrainState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerActionDrainBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_ACTION_BURN_EFFECT = "action_burn";
    public static boolean isRetiredPostNgePlayerActionBurnEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_ACTION_BURN_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerActionBurnBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerActionBurnEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerActionBurnScriptVars(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, "buff.action_burn");
    }
    public static void retirePostNgePlayerActionBurnState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerActionBurnBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerActionBurnScriptVars(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_ACTION_REGEN_EFFECT = "action_regen";
    public static boolean isRetiredPostNgePlayerActionRegenEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_ACTION_REGEN_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerActionRegenBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerActionRegenEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerActionRegenState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerActionRegenBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_DAMAGE_DEALT_OVERRIDE_EFFECT = "damage_dealt_mod";
    public static boolean isRetiredPostNgePlayerDamageDealtOverrideEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_DAMAGE_DEALT_OVERRIDE_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerDamageDealtOverrideBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerDamageDealtOverrideEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void restorePostNgePlayerDamageDealtOverride(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player) ||
            (!utils.hasScriptVar(player, "damageDealtMod.value") &&
                !utils.hasScriptVar(player, "damageDealtMod.scale")))
        {
            return;
        }
        boolean hasRecordedScale = utils.hasScriptVar(player, "damageDealtMod.scale");
        float recordedScale = utils.getFloatScriptVar(player, "damageDealtMod.scale");
        utils.removeScriptVarTree(player, "damageDealtMod");
        if (hasRecordedScale && recordedScale > 0.0f)
        {
            setScale(player, recordedScale);
        }
    }
    public static void retirePostNgePlayerDamageDealtOverrideState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerDamageDealtOverrideBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        restorePostNgePlayerDamageDealtOverride(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_WEAPON_SPEED_OVERRIDE_EFFECT = "weapon_speed_mod";
    public static boolean isRetiredPostNgePlayerWeaponSpeedOverrideEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_WEAPON_SPEED_OVERRIDE_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerWeaponSpeedOverrideBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerWeaponSpeedOverrideEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void restorePostNgePlayerWeaponSpeedOverride(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player) ||
            !utils.hasScriptVar(player, "recordedAttackSpeed"))
        {
            return;
        }
        String weaponRecord = utils.getStringScriptVar(player, "recordedAttackSpeed");
        utils.removeScriptVar(player, "recordedAttackSpeed");
        if (weaponRecord == null || weaponRecord.length() == 0)
        {
            return;
        }
        String[] parse = split(weaponRecord, '-');
        if (parse.length != 2)
        {
            return;
        }
        obj_id weapon = utils.stringToObjId(parse[0]);
        float weaponSpeed = utils.stringToFloat(parse[1]);
        if (!isIdValid(weapon) || !exists(weapon) ||
            !utils.isNestedWithin(weapon, player) || weaponSpeed <= 0.0f)
        {
            return;
        }
        setWeaponAttackSpeed(weapon, weaponSpeed);
        weapons.setWeaponData(weapon);
        utils.removeScriptVar(weapon, "isCreatureWeapon");
    }
    public static void retirePostNgePlayerWeaponSpeedOverrideState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerWeaponSpeedOverrideBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        restorePostNgePlayerWeaponSpeedOverride(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_CRITICAL_OVERRIDE_EFFECTS =
    {
        "expertise_next_hit_crit",
        "expertise_crit_double_damage",
        "expertise_crit_root",
        "expertise_crit_remove_buff"
    };
    public static boolean isRetiredPostNgePlayerCriticalOverrideEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_CRITICAL_OVERRIDE_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCriticalOverrideBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerCriticalOverrideEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerCriticalOverrideScriptVars(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, "nextCritHit");
        utils.removeScriptVarTree(player, "critDoubleDamage");
        utils.removeScriptVarTree(player, "critRoot");
        utils.removeScriptVarTree(player, "critRemoveBuffNames");
    }
    public static void retirePostNgePlayerCriticalOverrideState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerCriticalOverrideBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerCriticalOverrideScriptVars(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_ON_ATTACK_REMOVE_EFFECT = "on_attack_remove";
    private static final String RETIRED_POST_NGE_PLAYER_ON_ATTACK_REMOVE_BUFF = "sp_shifty_setup";
    public static boolean isRetiredPostNgePlayerOnAttackRemoveEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_ON_ATTACK_REMOVE_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerOnAttackRemoveBuffName(String buffName)
    {
        return buffName != null && buffName.equals(RETIRED_POST_NGE_PLAYER_ON_ATTACK_REMOVE_BUFF);
    }
    public static boolean isRetiredPostNgePlayerOnAttackRemoveBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerOnAttackRemoveBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerOnAttackRemoveEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerOnAttackRemoveState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, ON_ATTACK_REMOVE);
    }
    public static void retirePostNgePlayerOnAttackRemoveState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerOnAttackRemoveBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerOnAttackRemoveState(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_LUCK_HIT_OVERRIDE_EFFECTS =
    {
        "sm_impossible_odds",
        "sm_skullduggery"
    };
    public static boolean isRetiredPostNgePlayerLuckHitOverrideEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_LUCK_HIT_OVERRIDE_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerLuckHitOverrideBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerLuckHitOverrideEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerLuckHitOverrideModifiers(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        String[] retiredModifiers =
        {
            "hitByLuck",
            "increaseHitByLuck",
            "missByLuck"
        };
        for (String retiredModifier : retiredModifiers)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
        }
    }
    public static void retirePostNgePlayerLuckHitOverrideState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerLuckHitOverrideBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerLuckHitOverrideModifiers(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_FORSAKE_FEAR_CHANNEL_EFFECT = "expertise_channel_action_heal";
    public static boolean isRetiredPostNgePlayerForsakeFearChannelEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_FORSAKE_FEAR_CHANNEL_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerForsakeFearChannelBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerForsakeFearChannelEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerForsakeFearChannelState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int forsakeFearSuiPid = -1;
        if (utils.hasScriptVar(player, "buff_handler.ForsakeFearSUIPID"))
        {
            forsakeFearSuiPid = utils.getIntScriptVar(player, "buff_handler.ForsakeFearSUIPID");
        }
        boolean ownsCountdown = forsakeFearSuiPid > -1 &&
            hasObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR) &&
            getIntObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR) == forsakeFearSuiPid;
        utils.removeScriptVar(player, "buff_handler.ForsakeFearSUIPID");
        utils.removeScriptVar(player, "buff_handler.lastForsakeFearPulse");
        utils.removeScriptVar(player, "buff_handler.totalForsakeFearPulses");
        utils.removeScriptVar(player, "buff_handler.channelForsakeFearCancelled");
        utils.removeScriptVar(player, "buff_handler.channelForsakeFearSuccessful");
        if (ownsCountdown)
        {
            forceCloseSUIPage(forsakeFearSuiPid);
            removeObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR);
            utils.removeScriptVarTree(player, sui.COUNTDOWNTIMER_VAR);
            if (hasScript(player, sui.COUNTDOWNTIMER_PLAYER_SCRIPT))
            {
                detachScript(player, sui.COUNTDOWNTIMER_PLAYER_SCRIPT);
            }
        }
    }
    public static void retirePostNgePlayerForsakeFearChannelState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerForsakeFearChannelBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerForsakeFearChannelState(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_CHANNEL_HEAL_EFFECT = "channel_heal_health";
    public static boolean isRetiredPostNgePlayerChannelHealEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_CHANNEL_HEAL_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerChannelHealBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerChannelHealEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerChannelHealState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int channelHealSuiPid = -1;
        if (utils.hasScriptVar(player, "channelHeal.suiPid"))
        {
            channelHealSuiPid = utils.getIntScriptVar(player, "channelHeal.suiPid");
        }
        boolean ownsCountdown = channelHealSuiPid > -1 &&
            hasObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR) &&
            getIntObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR) == channelHealSuiPid;
        utils.removeScriptVarTree(player, "channelHeal");
        if (ownsCountdown)
        {
            forceCloseSUIPage(channelHealSuiPid);
            removeObjVar(player, sui.COUNTDOWNTIMER_SUI_VAR);
            utils.removeScriptVarTree(player, sui.COUNTDOWNTIMER_VAR);
            if (hasScript(player, sui.COUNTDOWNTIMER_PLAYER_SCRIPT))
            {
                detachScript(player, sui.COUNTDOWNTIMER_PLAYER_SCRIPT);
            }
        }
    }
    public static void retirePostNgePlayerChannelHealState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerChannelHealBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerChannelHealState(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_QUEUED_BATTLEFIELD_COMMUNICATION_BUFF = "battlefield_communication_run";
    public static boolean isRetiredPostNgePlayerQueuedBattlefieldCommunicationBuffName(String buffName)
    {
        return buffName != null && buffName.equals(RETIRED_POST_NGE_PLAYER_QUEUED_BATTLEFIELD_COMMUNICATION_BUFF);
    }
    public static boolean isRetiredPostNgePlayerQueuedBattlefieldCommunicationBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null && isRetiredPostNgePlayerQueuedBattlefieldCommunicationBuffName(data.buffName);
    }
    public static void retirePostNgePlayerQueuedBattlefieldCommunicationState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        if (hasBuff(player, RETIRED_POST_NGE_PLAYER_QUEUED_BATTLEFIELD_COMMUNICATION_BUFF))
        {
            removeBuff(player, RETIRED_POST_NGE_PLAYER_QUEUED_BATTLEFIELD_COMMUNICATION_BUFF);
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_RADAR_INVISIBILITY_EFFECT = "radar_invis";
    public static boolean isRetiredPostNgePlayerRadarInvisibilityEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_RADAR_INVISIBILITY_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerRadarInvisibilityBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerRadarInvisibilityEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerRadarInvisibilityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        boolean removedOwnedRadarInvisibility = false;
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerRadarInvisibilityBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                    removedOwnedRadarInvisibility = true;
                }
            }
        }
        if (removedOwnedRadarInvisibility)
        {
            setVisibleOnMapAndRadar(player, true);
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_COOLDOWN_EXECUTION_EFFECT = "cooldown_execute_all";
    public static boolean isRetiredPostNgePlayerCooldownExecutionEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_COOLDOWN_EXECUTION_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerCooldownExecutionBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerCooldownExecutionEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerCooldownExecutionState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerCooldownExecutionBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_SABER_INTERCEPT_EFFECT = "saber_intercept";
    public static boolean isRetiredPostNgePlayerSaberInterceptEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_SABER_INTERCEPT_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerSaberInterceptBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerSaberInterceptEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerSaberInterceptState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerSaberInterceptBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_DAMAGE_REDIRECT_BUFFS =
    {
        "bm_shield_master_pet",
        "bm_shield_master_player",
        "bodyguard"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_DAMAGE_REDIRECT_EFFECTS =
    {
        "protect_master",
        "shield_master_pet",
        "shield_master_player"
    };
    public static boolean isRetiredPostNgePlayerDamageRedirectBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_DAMAGE_REDIRECT_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerDamageRedirectEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_DAMAGE_REDIRECT_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerDamageRedirectBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerDamageRedirectBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerDamageRedirectEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerDamageRedirectState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVar(player, combat.DAMAGE_REDIRECT);
    }
    public static void retirePostNgePlayerDamageRedirectState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerDamageRedirectBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerDamageRedirectState(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_PISTOL_WHIP_CONTROL_EFFECT = "sm_pistol_whip";
    public static boolean isRetiredPostNgePlayerPistolWhipControlEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_PISTOL_WHIP_CONTROL_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerPistolWhipControlBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerPistolWhipControlEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerPistolWhipControlState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerPistolWhipControlBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_SMUGGLER_TRICK_EFFECTS =
    {
        "expertise_sly_lie",
        "expertise_fast_talk"
    };
    public static boolean isRetiredPostNgePlayerSmugglerTrickEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_SMUGGLER_TRICK_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerSmugglerTrickBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerSmugglerTrickEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerSmugglerTrickModifiers(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        String[] retiredModifiers =
        {
            "slyLieDodge",
            "innocentCargoStrikethrough",
            "fastTalkAgility"
        };
        for (String retiredModifier : retiredModifiers)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
        }
    }
    public static void retirePostNgePlayerSmugglerTrickState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerSmugglerTrickBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerSmugglerTrickModifiers(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_AGGRO_CHANNEL_EFFECTS =
    {
        "aggro_channel_self",
        "aggro_channel_target"
    };
    public static boolean isRetiredPostNgePlayerAggroChannelEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_AGGRO_CHANNEL_EFFECTS)
        {
            if (effectName.equals(retiredEffect))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerAggroChannelBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerAggroChannelEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerAggroChannelState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerAggroChannelBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        utils.removeScriptVar(player, AGGRO_TRANSFER_TO);
    }
    private static final String RETIRED_POST_NGE_PLAYER_COMMANDO_SNARE_ARMOR_EFFECT = "commando_snare_bonus";
    private static final String RETIRED_POST_NGE_PLAYER_COMMANDO_SNARE_ARMOR_MODIFIER = "commandoInnateArmorBonus";
    public static boolean isRetiredPostNgePlayerCommandoSnareArmorEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_COMMANDO_SNARE_ARMOR_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerCommandoSnareArmorBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerCommandoSnareArmorEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerCommandoSnareArmorModifier(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        if (hasSkillModModifier(player, RETIRED_POST_NGE_PLAYER_COMMANDO_SNARE_ARMOR_MODIFIER))
        {
            removeAttribOrSkillModModifier(player, RETIRED_POST_NGE_PLAYER_COMMANDO_SNARE_ARMOR_MODIFIER);
            messageTo(player, "recalcArmor", null, 0.25f, false);
        }
    }
    public static void retirePostNgePlayerCommandoSnareArmorState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerCommandoSnareArmorBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerCommandoSnareArmorModifier(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_EFFECTS =
    {
        "expertise_flash_bang",
        "expertise_muscle_spasm",
        "expertise_riddle_armor",
        "expertise_on_target"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_BUFFS =
    {
        "co_flash_bang",
        "co_muscle_spasm",
        "co_riddle_armor",
        "co_armor_cracker",
        "grenadier_kinetic",
        "co_position_secured",
        "co_pos_sec_action_1",
        "co_pos_sec_action_2",
        "co_pos_sec_action_3",
        "co_pos_sec_proc_1",
        "co_pos_sec_proc_2",
        "co_pos_sec_critical_1",
        "co_pos_sec_critical_2",
        "co_pos_sec_critical_3",
        "co_pos_sec_critical_4",
        "co_base_of_operations"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_MODIFIERS =
    {
        "commandoFlashBang",
        "commandoMuscleSpasm",
        "precision_modified",
        "strength_modified",
        "glancing_blow_vulnerable",
        "expertise_riddle_armor",
        "expertise_innate_protection_all",
        "expertise_critical_hit_reduction",
        "expertise_critical_niche_all",
        "expertise_co_burst_fire_proc",
        "expertise_devastation_bonus",
        "expertise_action_all",
        "expertise_co_flash_bang",
        "expertise_co_muscle_spasm",
        "expertise_action_line_co_imp_pos_sec",
        "expertise_co_pos_secured_line_armor",
        "expertise_co_pos_secured_line_boo_critical",
        "expertise_co_pos_secured_line_burst_fire_devastation_bonus",
        "expertise_co_pos_secured_line_burst_fire_proc",
        "expertise_co_pos_secured_line_critical",
        "expertise_co_pos_secured_line_protection"
    };
    public static boolean isRetiredPostNgePlayerCommandoSpecializedEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_EFFECTS)
        {
            if (effectName.equals(retiredEffect) || effectName.startsWith(retiredEffect + "_"))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCommandoSpecializedBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCommandoSpecializedBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerCommandoSpecializedBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerCommandoSpecializedEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerCommandoSpecializedModifiers(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredModifier : RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_MODIFIERS)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
            for (int effect = 1; effect <= MAX_EFFECTS; effect++)
            {
                String indexedModifier = retiredModifier + "_" + effect;
                if (hasSkillModModifier(player, indexedModifier))
                {
                    removeAttribOrSkillModModifier(player, indexedModifier);
                }
            }
            int currentValue = getSkillStatMod(player, retiredModifier);
            if (currentValue != 0)
            {
                applySkillStatisticModifier(player, retiredModifier, -currentValue);
            }
        }
        messageTo(player, "recalcArmor", null, 0.25f, false);
        combat.cacheCombatData(player);
    }
    public static void clearPostNgePlayerCommandoSpecializedBuffs(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_COMMANDO_SPECIALIZED_BUFFS)
        {
            if (!retiredBuff.equals("co_position_secured") && hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
    }
    public static void retirePostNgePlayerCommandoSpecializedState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        if (hasBuff(player, "co_position_secured"))
        {
            removeBuff(player, "co_position_secured");
        }
        clearPostNgePlayerCommandoSpecializedBuffs(player);
        clearPostNgePlayerCommandoSpecializedModifiers(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_ELEMENTAL_VULNERABILITY_EFFECT_PREFIX = "dt_vulnerability_";
    private static final String RETIRED_POST_NGE_PLAYER_ELEMENTAL_VULNERABILITY_STATE = "elemental_vulnerability";
    public static boolean isRetiredPostNgePlayerElementalVulnerabilityEffect(String effectName)
    {
        return effectName != null &&
            effectName.startsWith(RETIRED_POST_NGE_PLAYER_ELEMENTAL_VULNERABILITY_EFFECT_PREFIX);
    }
    public static boolean isRetiredPostNgePlayerElementalVulnerabilityBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerElementalVulnerabilityEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerElementalVulnerabilityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, RETIRED_POST_NGE_PLAYER_ELEMENTAL_VULNERABILITY_STATE);
    }
    public static void retirePostNgePlayerElementalVulnerabilityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerElementalVulnerabilityBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerElementalVulnerabilityState(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_FORCE_THROW_EFFECT = "forceThrow";
    private static final String RETIRED_POST_NGE_PLAYER_FORCE_THROW_CONTROL_BUFF_PREFIX = "fs_force_throw_";
    public static boolean isRetiredPostNgePlayerForceThrowEffect(String effectName)
    {
        return effectName != null && effectName.equals(RETIRED_POST_NGE_PLAYER_FORCE_THROW_EFFECT);
    }
    public static boolean isRetiredPostNgePlayerForceThrowBuffName(String buffName)
    {
        return buffName != null &&
            (buffName.equals(RETIRED_POST_NGE_PLAYER_FORCE_THROW_EFFECT) ||
                buffName.startsWith(RETIRED_POST_NGE_PLAYER_FORCE_THROW_CONTROL_BUFF_PREFIX));
    }
    public static boolean isRetiredPostNgePlayerForceThrowBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerForceThrowBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerForceThrowEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerForceThrowState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerForceThrowBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_BUFF = "me_doom";
    private static final String RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_MODIFIER = "me_doom_chance";
    public static boolean isRetiredPostNgePlayerMedicDoomBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_BUFF.equals(data.buffName);
    }
    public static void clearPostNgePlayerMedicDoomState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_BUFF);
        if (hasSkillModModifier(player, RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_MODIFIER))
        {
            removeAttribOrSkillModModifier(player, RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_MODIFIER);
        }
    }
    public static void retirePostNgePlayerMedicDoomState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        if (hasBuff(player, RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_BUFF))
        {
            removeBuff(player, RETIRED_POST_NGE_PLAYER_MEDIC_DOOM_BUFF);
        }
        clearPostNgePlayerMedicDoomState(player);
    }
    private static final String RETIRED_POST_NGE_PLAYER_DOT_REDUCTION_EFFECT_PREFIX = "dot_reduction_";
    private static final String RETIRED_POST_NGE_PLAYER_DOT_DIVISOR_EFFECT_PREFIX = "dot_divisor_";
    public static boolean isRetiredPostNgePlayerDotStackMutationEffect(String effectName)
    {
        return effectName != null &&
            (effectName.startsWith(RETIRED_POST_NGE_PLAYER_DOT_REDUCTION_EFFECT_PREFIX) ||
                effectName.startsWith(RETIRED_POST_NGE_PLAYER_DOT_DIVISOR_EFFECT_PREFIX));
    }
    public static boolean isRetiredPostNgePlayerDotStackMutationBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerDotStackMutationEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerDotStackMutationState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerDotStackMutationBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_BEAST_FAMILY_BUFFS =
    {
        "bm_truffle_pig",
        "bm_helper_monkey_domestic",
        "bm_helper_monkey_engineering",
        "bm_helper_monkey_structure",
        "bm_helper_monkey_munitions",
        "bm_helper_monkey_jedi",
        "bm_helper_monkey_shipwright"
    };
    public static boolean isRetiredPostNgePlayerBeastFamilyBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_BEAST_FAMILY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerBeastFamilyBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerBeastFamilyBuffName(data.buffName);
    }
    public static void retirePostNgePlayerBeastFamilyBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerBeastFamilyBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_PROFESSION_MOVEMENT_BUFF_PREFIXES =
    {
        "bh_",
        "bm_",
        "co_",
        "en_",
        "fs_",
        "me_",
        "of_",
        "sm_",
        "sp_",
        "sl_group_"
    };
    public static boolean isRetiredPostNgePlayerProfessionMovementBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredPrefix : RETIRED_POST_NGE_PLAYER_PROFESSION_MOVEMENT_BUFF_PREFIXES)
        {
            if (buffName.startsWith(retiredPrefix))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerProfessionMovementBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null ||
            !isRetiredPostNgePlayerProfessionMovementBuffName(data.buffName))
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if ("movement".equals(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerProfessionMovementBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerProfessionMovementBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_PROFESSION_IMMUNITY_BUFFS =
    {
        "bm_pet_cure",
        "me_serotonin_boost_1",
        "me_serotonin_purge_1",
        "me_stasis_1",
        "me_stasis_self_1",
        "of_stimulator_1",
        "sp_covert_mastery"
    };
    public static boolean isRetiredPostNgePlayerProfessionImmunityBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_PROFESSION_IMMUNITY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerProfessionImmunityBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerProfessionImmunityBuffName(data.buffName);
    }
    public static void retirePostNgePlayerProfessionImmunityState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerProfessionImmunityBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_PROFESSION_INSPIRATION_BUFFS =
    {
        "general_inspiration",
        "artisan_inspiration",
        "entertainer_inspiration",
        "scout_inspiration",
        "chef_inspiration",
        "tailor_inspiration",
        "bioengineer_inspiration",
        "merchant_inspiration",
        "imagedesigner_inspiration",
        "musician_inspiration",
        "ranger_inspiration",
        "architect_inspiration",
        "droidengineer_inspiration",
        "weaponsmith_inspiration",
        "shipwright_inspiration",
        "armorsmith_inspiration",
        "dancer_inspiration"
    };
    public static boolean isRetiredPostNgePlayerProfessionInspirationBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_PROFESSION_INSPIRATION_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerProfessionInspirationBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerProfessionInspirationBuffName(data.buffName);
    }
    public static void clearPostNgePlayerProfessionInspirationScriptVars(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        utils.removeScriptVarTree(player, "buff.xpBonus");
        utils.removeScriptVarTree(player, "buff.xpBonusGeneral");
        utils.removeScriptVarTree(player, "buff.craftBonus");
        utils.removeScriptVarTree(player, "buff.faction");
        utils.removeScriptVarTree(player, "buff.instrument");
        utils.removeScriptVarTree(player, "buff.prop");
        utils.removeScriptVarTree(player, "buff.holoemote");
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_PROFESSION_INSPIRATION_BUFFS)
        {
            utils.removeScriptVarTree(player, "buff." + retiredBuff);
        }
    }
    public static void retirePostNgePlayerProfessionInspirationState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerProfessionInspirationBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerProfessionInspirationScriptVars(player);
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_PROFESSION_PROXY_BUFFS =
    {
        "exclusive_proxy_bh_del_cc_1",
        "exclusive_proxy_bh_del_cc_2",
        "exclusive_proxy_bh_del_cc_3",
        "exclusive_proxy_bh_del_dm_cc_dot_1",
        "exclusive_proxy_bh_del_dm_cc_dot_2",
        "exclusive_proxy_bh_del_dm_cc_dot_3",
        "exclusive_proxy_of_last_words",
        "exclude_self_exclusive_proxy_of_last_words",
        "exclusive_proxy_bh_dire_root_1",
        "exclusive_proxy_of_vortex_root_1",
        "exclusive_proxy_of_vortex_root_2",
        "exclusive_proxy_of_vortex_root_3",
        "exclusive_proxy_of_vortex_root_4",
        "exclusive_proxy_of_vortex_root_5",
        "of_pt_proxy_1",
        "of_pt_proxy_2",
        "of_pt_proxy_3",
        "of_pt_proxy_4",
        "of_pt_proxy_5",
        "of_pt_proxy_6",
        "of_pt_proxy_7",
        "of_pt_proxy_8",
        "bh_del_cc_1",
        "bh_del_cc_2",
        "bh_del_cc_3",
        "bh_del_dm_cc_dot_1",
        "bh_del_dm_cc_dot_2",
        "bh_del_dm_cc_dot_3",
        "of_last_words",
        "of_last_words_recourse",
        "bh_dire_root_1",
        "bh_dire_snare_1",
        "dire_root_recourse",
        "dire_snare_recourse",
        "of_vortex_root",
        "of_vortex_snare",
        "of_vortex_bleed_1",
        "of_vortex_bleed_2",
        "of_vortex_bleed_3",
        "of_vortex_bleed_4",
        "of_vortex_bleed_5"
    };
    public static boolean isRetiredPostNgePlayerProfessionProxyBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_PROFESSION_PROXY_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerProfessionProxyBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerProfessionProxyBuffName(data.buffName);
    }
    public static void retirePostNgePlayerProfessionProxyState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerProfessionProxyBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_BUFFS =
    {
        "co_supressing_handler",
        "co_supressing_fire_0",
        "co_supressing_fire_1",
        "co_supressing_fire_2",
        "co_supressing_fire_3",
        "co_supressing_fire_4"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_EFFECTS =
    {
        "supression_handler",
        "supress_movement"
    };
    private static final String[] RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_MODIFIERS =
    {
        "glancing_blow_vulnerable",
        "expertise_supression_speed",
        "expertise_supression_glance"
    };
    public static boolean isRetiredPostNgePlayerCommandoSuppressionBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCommandoSuppressionEffect(String effectName)
    {
        if (effectName == null)
        {
            return false;
        }
        for (String retiredEffect : RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_EFFECTS)
        {
            if (effectName.equals(retiredEffect) || effectName.startsWith(retiredEffect + "_"))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerCommandoSuppressionBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        if (isRetiredPostNgePlayerCommandoSuppressionBuffName(data.buffName))
        {
            return true;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerCommandoSuppressionEffect(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerCommandoSuppressionModifiers(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredModifier : RETIRED_POST_NGE_PLAYER_COMMANDO_SUPPRESSION_MODIFIERS)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
            for (int effect = 1; effect <= MAX_EFFECTS; effect++)
            {
                String indexedModifier = retiredModifier + "_" + effect;
                if (hasSkillModModifier(player, indexedModifier))
                {
                    removeAttribOrSkillModModifier(player, indexedModifier);
                }
            }
            int currentValue = getSkillStatMod(player, retiredModifier);
            if (currentValue != 0)
            {
                applySkillStatisticModifier(player, retiredModifier, -currentValue);
            }
        }
        combat.cacheCombatData(player);
    }
    public static void retirePostNgePlayerCommandoSuppressionState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        boolean removedMovementSuppression = false;
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerCommandoSuppressionBuff(player, data))
                {
                    for (int effect = 1; effect <= MAX_EFFECTS; effect++)
                    {
                        if ("supress_movement".equals(getEffectParam(data, effect)))
                        {
                            removedMovementSuppression = true;
                        }
                    }
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerCommandoSuppressionModifiers(player);
        if (removedMovementSuppression)
        {
            removeSlowDownEffect(player);
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_GROUP_BUFFS =
    {
        "sl_group_run",
        "sl_group_acc",
        "sl_group_def",
        "sl_group_crit_hit",
        "sl_group_armor",
        "sl_group_regen",
        "sl_group_armor_break",
        "sl_group_red_cooldown",
        "sl_group_retreat",
        "sl_group_charge",
        "co_base_of_operations",
        "veteranPlayerBuff",
        "fs_forsake_fear",
        "of_buff_def_1",
        "of_buff_def_2",
        "of_buff_def_3",
        "of_buff_def_4",
        "of_buff_def_5",
        "of_buff_def_6",
        "of_buff_def_7",
        "of_buff_def_8",
        "of_buff_def_9",
        "of_focus_fire_1",
        "of_focus_fire_2",
        "of_focus_fire_3",
        "of_focus_fire_4",
        "of_focus_fire_5",
        "of_focus_fire_6",
        "of_inspiration_1",
        "of_inspiration_2",
        "of_inspiration_3",
        "of_inspiration_4",
        "of_inspiration_5",
        "of_inspiration_6",
        "of_scatter_1",
        "of_charge_1",
        "of_drillmaster_1",
        "human_ability_1"
    };
    public static boolean isRetiredPostNgePlayerGroupBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_GROUP_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerGroupBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerGroupBuffName(data.buffName);
    }
    public static void retirePostNgePlayerGroupBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerGroupBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_FLAT_ATTRIBUTE_BUFFS =
    {
        "crystal_buff",
        "holocron_1",
        "holocron_4",
        "holocron_5",
        "holocron_6",
        "trivialComboRngSpeed",
        "towConstStamina_1",
        "towConstStamina_2",
        "towConstWillpower_1",
        "towConstWillpower_2",
        "towStaminaWillpower_1",
        "towStaminaWillpower_2",
        "forceCrystalForce"
    };
    public static boolean isRetiredPostNgePlayerFlatAttributeBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_FLAT_ATTRIBUTE_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerFlatAttributeBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerFlatAttributeBuffName(data.buffName);
    }
    public static void retirePostNgePlayerFlatAttributeState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerFlatAttributeBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_ATTRIBUTE_PERCENT_BUFFS =
    {
        "nutrientInjection",
        "nutrientInjection_1",
        "nutrientInjection_2",
        "endorphineInjection",
        "endorphineInjection_1",
        "serotoninInjection",
        "serotoninInjection_1",
        "hemorrhage",
        "hemorrhage_1",
        "traumatize",
        "traumatize_1",
        "forceSap",
        "forceSap_1",
        "holocron_8",
        "sl_group_regen",
        "sl_group_retreat",
        "combatRegenDebuff",
        "treasure_bonus_combat_critical_hit",
        "treasure_bonus_heal_health_action"
    };
    public static boolean isRetiredPostNgePlayerAttributePercentBuffName(String buffName)
    {
        if (buffName == null)
        {
            return false;
        }
        for (String retiredBuff : RETIRED_POST_NGE_PLAYER_ATTRIBUTE_PERCENT_BUFFS)
        {
            if (buffName.equals(retiredBuff))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerAttributePercentBuff(obj_id target, buff_data data) throws InterruptedException
    {
        return isPlayer(target) && data != null &&
            isRetiredPostNgePlayerAttributePercentBuffName(data.buffName);
    }
    public static void retirePostNgePlayerAttributePercentState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerAttributePercentBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    private static final String[] RETIRED_POST_NGE_PLAYER_DAMAGE_REDUCTION_MODIFIERS =
    {
        "expertise_damage_decrease_chance",
        "expertise_sm_rank_damage_bonus",
        "expertise_damage_reduce_anticipate_aggression",
        "damage_decrease_percentage",
        "area_damage_decrease_percentage",
        "area_damage_resist_full_percentage",
        "expertise_damage_decrease_percentage"
    };
    public static boolean isRetiredPostNgePlayerDamageReductionModifier(String modifierName)
    {
        if (modifierName == null)
        {
            return false;
        }
        for (String retiredModifier : RETIRED_POST_NGE_PLAYER_DAMAGE_REDUCTION_MODIFIERS)
        {
            if (modifierName.equals(retiredModifier))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isRetiredPostNgePlayerDamageReductionBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (isRetiredPostNgePlayerDamageReductionModifier(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void clearPostNgePlayerDamageReductionState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        for (String retiredModifier : RETIRED_POST_NGE_PLAYER_DAMAGE_REDUCTION_MODIFIERS)
        {
            if (hasSkillModModifier(player, retiredModifier))
            {
                removeAttribOrSkillModModifier(player, retiredModifier);
            }
            for (int effect = 1; effect <= MAX_EFFECTS; effect++)
            {
                String indexedModifier = retiredModifier + "_" + effect;
                if (hasSkillModModifier(player, indexedModifier))
                {
                    removeAttribOrSkillModModifier(player, indexedModifier);
                }
            }
            int currentValue = getSkillStatMod(player, retiredModifier);
            if (currentValue != 0)
            {
                applySkillStatisticModifier(player, retiredModifier, -currentValue);
            }
        }
        if (hasSkillModModifier(player, "junkDealerDamageDecrease"))
        {
            removeAttribOrSkillModModifier(player, "junkDealerDamageDecrease");
        }
    }
    public static void retirePostNgePlayerDamageReductionState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs != null && activeBuffs.length > 0)
        {
            for (int activeBuff : activeBuffs)
            {
                buff_data data = combat_engine.getBuffData(activeBuff);
                if (isRetiredPostNgePlayerDamageReductionBuff(player, data))
                {
                    removeBuff(player, activeBuff);
                }
            }
        }
        clearPostNgePlayerDamageReductionState(player);
    }
    public static boolean isRetiredPostNgePlayerModifierBuff(obj_id target, buff_data data) throws InterruptedException
    {
        if (!isPlayer(target) || data == null)
        {
            return false;
        }
        for (int effect = 1; effect <= MAX_EFFECTS; effect++)
        {
            if (static_item.isRetiredNgeBuffSkillModifier(getEffectParam(data, effect)))
            {
                return true;
            }
        }
        return false;
    }
    public static void retirePostNgePlayerModifierBuffState(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player) || !isPlayer(player))
        {
            return;
        }
        int[] activeBuffs = getAllBuffs(player);
        if (activeBuffs == null || activeBuffs.length == 0)
        {
            return;
        }
        for (int activeBuff : activeBuffs)
        {
            buff_data data = combat_engine.getBuffData(activeBuff);
            if (isRetiredPostNgePlayerModifierBuff(player, data))
            {
                removeBuff(player, activeBuff);
            }
        }
    }
    public static void retirePostNgeBuffProgression(obj_id player) throws InterruptedException
    {
        if (!isPostNgeBuffProgressionRetired() || !isIdValid(player) || !exists(player))
        {
            return;
        }
        if (hasBuff(player, "buildabuff_inspiration"))
        {
            removeBuff(player, "buildabuff_inspiration");
        }
        retirePostNgePlayerCommandGrantBuffState(player);
        retirePostNgePlayerActionDrainState(player);
        retirePostNgePlayerActionBurnState(player);
        retirePostNgePlayerActionRegenState(player);
        retirePostNgePlayerDamageDealtOverrideState(player);
        retirePostNgePlayerWeaponSpeedOverrideState(player);
        retirePostNgePlayerCriticalOverrideState(player);
        retirePostNgePlayerOnAttackRemoveState(player);
        retirePostNgePlayerLuckHitOverrideState(player);
        retirePostNgePlayerForsakeFearChannelState(player);
        retirePostNgePlayerChannelHealState(player);
        retirePostNgePlayerQueuedBattlefieldCommunicationState(player);
        retirePostNgePlayerRadarInvisibilityState(player);
        retirePostNgePlayerCooldownExecutionState(player);
        retirePostNgePlayerSaberInterceptState(player);
        retirePostNgePlayerDamageRedirectState(player);
        retirePostNgePlayerPistolWhipControlState(player);
        retirePostNgePlayerSmugglerTrickState(player);
        retirePostNgePlayerAggroChannelState(player);
        retirePostNgePlayerCommandoSnareArmorState(player);
        retirePostNgePlayerCommandoSpecializedState(player);
        retirePostNgePlayerElementalVulnerabilityState(player);
        retirePostNgePlayerForceThrowState(player);
        retirePostNgePlayerMedicDoomState(player);
        retirePostNgePlayerDotStackMutationState(player);
        retirePostNgePlayerBeastFamilyBuffState(player);
        retirePostNgePlayerProfessionMovementBuffState(player);
        retirePostNgePlayerProfessionImmunityState(player);
        retirePostNgePlayerProfessionInspirationState(player);
        retirePostNgePlayerProfessionProxyState(player);
        retirePostNgePlayerCommandoSuppressionState(player);
        retirePostNgePlayerGroupBuffState(player);
        retirePostNgePlayerFlatAttributeState(player);
        retirePostNgePlayerAttributePercentState(player);
        retirePostNgePlayerDamageReductionState(player);
        retirePostNgePlayerModifierBuffState(player);
        retirePostNgeMeditationBuffs(player);
        retirePostNgePlayerForceSensitiveExpertiseImmunityState(player);
        retirePostNgePlayerBountyHunterFlawlessState(player);
        retirePostNgeForceSensitiveStanceState(player);
        retirePostNgeGcwBannerBuffState(player);
        retirePostNgeGcwConsumableBuffState(player);
        retirePostP14PlayerControlImmunityState(player);
        retirePostP14PlayerAvoidIncapHealState(player);
        retirePostNgeBountyHunterShieldState(player);
        static_item.removeRetiredNgePlayerSkillStatistics(player);
        utils.removeScriptVarTree(player, "performance.buildabuff");
        if (hasScript(player, "systems.buff_builder.buff_builder_response"))
        {
            detachScript(player, "systems.buff_builder.buff_builder_response");
        }
        if (hasScript(player, "systems.buff_builder.buff_builder_cancel"))
        {
            detachScript(player, "systems.buff_builder.buff_builder_cancel");
        }
    }
    public static void retirePostNgeMeditationBuffs(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return;
        }
        String[] retiredBuffs =
        {
            "fs_meditate_1",
            "fs_meditate_2",
            "fs_meditate_3"
        };
        for (String retiredBuff : retiredBuffs)
        {
            if (hasBuff(player, retiredBuff))
            {
                removeBuff(player, retiredBuff);
            }
        }
    }
    public static final String DOT_BLEEDING = "dot_bleeding";
    public static final String DOT_POISON = "dot_poison";
    public static final String DOT_DISEASE = "dot_disease";
    public static final String DOT_FIRE = "dot_fire";
    public static final String DOT_ACID = "dot_acid";
    public static final String DOT_ENERGY = "dot_energy";
    public static final String DOT_COLD = "dot_cold";
    public static final String DOT_ELECTRICITY = "dot_electricity";
    public static final String DOT_KINETIC = "dot_kinetic";
    public static final String ON_ATTACK_REMOVE = "onAttackRemoveBuffList";
    public static final int STATE_NONE = -1;
    public static final int STATE_COVER = 0;
    public static final int STATE_ALERT = 4;
    public static final int STATE_BERSERK = 5;
    public static final int STATE_FEIGN_DEATH = 6;
    public static final int STATE_TUMBLING = 10;
    public static final int STATE_RALLIED = 11;
    public static final int STATE_STUNNED = 12;
    public static final int STATE_BLINDED = 13;
    public static final int STATE_DIZZY = 14;
    public static final int STATE_INTIMIDATED = 15;
    public static final int STATE_IMMOBILIZED = 16;
    public static final int STATE_FROZEN = 17;
    public static final int BUFF_DOT_TICK = 2;
    public static boolean isParalyzed(obj_id target) throws InterruptedException
    {
        deltadictionary dd = target.getScriptVars();
        java.util.Enumeration keys = dd.keys();
        while (keys.hasMoreElements())
        {
            String key = (String)(keys.nextElement());
            if (key.startsWith(DEBUFF_STATE_PARALYZED))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean canApplyBuff(obj_id target, String name) throws InterruptedException
    {
        return canApplyBuff(target, null, getStringCrc(name.toLowerCase()));
    }
    public static boolean canApplyBuff(obj_id target, obj_id owner, String name) throws InterruptedException
    {
        return canApplyBuff(target, owner, getStringCrc(name.toLowerCase()));
    }
    public static boolean canApplyBuff(obj_id target, int nameCrc) throws InterruptedException
    {
        return canApplyBuff(target, null, nameCrc);
    }
    public static boolean canApplyBuff(obj_id target, obj_id owner, int nameCrc) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return false;
        }
        if (!isPlayer(target) && !isMob(target))
        {
            return false;
        }
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return false;
        }
        if (isPostNgeBuffProgressionRetired() && isRetiredPostNgeJunkDealerExpertiseBuff(bdata))
        {
            return false;
        }
        if (isPlayer(target) &&
            (stealth.isRetiredPostP14PlayerInvisibilityName(bdata.buffName) ||
                isRetiredPostNgeForceSensitiveStanceBuff(bdata.buffName) ||
                isRetiredPostNgeForceSensitiveChokeFlurryBuff(bdata.buffName) ||
                isRetiredPostNgeGcwBannerBuff(bdata.buffName) ||
                isRetiredPostNgeGcwConsumableBuff(bdata.buffName) ||
                isRetiredPostP14PlayerControlImmunityBuff(bdata.buffName) ||
                isRetiredPostP14PlayerAvoidIncapHealBuff(bdata.buffName) ||
                factions.isRetiredPostNgePvpRewardBuff(bdata.buffName) ||
                proc.isRetiredPostNgePlayerProcBuff(target, bdata) ||
                isRetiredPostNgePlayerCommandGrantBuff(target, bdata) ||
                isRetiredPostNgePlayerActionDrainBuff(target, bdata) ||
                isRetiredPostNgePlayerActionBurnBuff(target, bdata) ||
                isRetiredPostNgePlayerActionRegenBuff(target, bdata) ||
                isRetiredPostNgePlayerDamageDealtOverrideBuff(target, bdata) ||
                isRetiredPostNgePlayerWeaponSpeedOverrideBuff(target, bdata) ||
                isRetiredPostNgePlayerCriticalOverrideBuff(target, bdata) ||
                isRetiredPostNgePlayerOnAttackRemoveBuff(target, bdata) ||
                isRetiredPostNgePlayerLuckHitOverrideBuff(target, bdata) ||
                isRetiredPostNgePlayerForsakeFearChannelBuff(target, bdata) ||
                isRetiredPostNgePlayerChannelHealBuff(target, bdata) ||
                isRetiredPostNgePlayerQueuedBattlefieldCommunicationBuff(target, bdata) ||
                isRetiredPostNgePlayerRadarInvisibilityBuff(target, bdata) ||
                isRetiredPostNgePlayerCooldownExecutionBuff(target, bdata) ||
                isRetiredPostNgePlayerSaberInterceptBuff(target, bdata) ||
                isRetiredPostNgePlayerDamageRedirectBuff(target, bdata) ||
                isRetiredPostNgePlayerPistolWhipControlBuff(target, bdata) ||
                isRetiredPostNgePlayerSmugglerTrickBuff(target, bdata) ||
                isRetiredPostNgePlayerAggroChannelBuff(target, bdata) ||
                isRetiredPostNgePlayerCommandoSnareArmorBuff(target, bdata) ||
                isRetiredPostNgePlayerCommandoSpecializedBuff(target, bdata) ||
                isRetiredPostNgePlayerForceSensitiveExpertiseImmunityBuff(target, bdata) ||
                isRetiredPostNgePlayerBountyHunterFlawlessBuff(target, bdata) ||
                isRetiredPostNgePlayerElementalVulnerabilityBuff(target, bdata) ||
                isRetiredPostNgePlayerForceThrowBuff(target, bdata) ||
                isRetiredPostNgePlayerMedicDoomBuff(target, bdata) ||
                isRetiredPostNgePlayerDotStackMutationBuff(target, bdata) ||
                isRetiredPostNgePlayerBeastFamilyBuff(target, bdata) ||
                isRetiredPostNgePlayerProfessionMovementBuff(target, bdata) ||
                isRetiredPostNgePlayerProfessionImmunityBuff(target, bdata) ||
                isRetiredPostNgePlayerProfessionInspirationBuff(target, bdata) ||
                isRetiredPostNgePlayerProfessionProxyBuff(target, bdata) ||
                isRetiredPostNgePlayerCommandoSuppressionBuff(target, bdata) ||
                isRetiredPostNgePlayerGroupBuff(target, bdata) ||
                isRetiredPostNgePlayerFlatAttributeBuff(target, bdata) ||
                isRetiredPostNgePlayerAttributePercentBuff(target, bdata) ||
                isRetiredPostNgePlayerDamageReductionBuff(target, bdata) ||
                isRetiredPostNgePlayerModifierBuff(target, bdata) ||
                isRetiredPostNgeBountyHunterShieldBuff(bdata.buffName)))
        {
            return false;
        }
        if (hasBuff(target, nameCrc))
        {
            return true;
        }
        if (checkForStateImmunity(target, bdata))
        {
            return false;
        }
        int buffCRCs[] = _getAllBuffs(target);
        if (buffCRCs == null || buffCRCs.length == 0)
        {
            return true;
        }
        int[] groups = getGroups(bdata);
        if (groups == null || groups.length != 3)
        {
            return false;
        }
        int groupOne = groups[0];
        int groupTwo = groups[1];
        if (groupOne != 0 || groupTwo != 0)
        {
            int priority = bdata.priority;
            buff_data oldBuffData;

            for (int buffCRC : buffCRCs) {
                oldBuffData = combat_engine.getBuffData(buffCRC);
                if (oldBuffData == null) {
                    continue;
                }
                int oldPriority = oldBuffData.priority;
                if (priority < oldPriority) {
                    int[] oldGroups = getGroups(oldBuffData);
                    if (oldGroups == null || oldGroups.length != 3) {
                        continue;
                    }
                    int oldGroupOne = oldGroups[0];
                    int oldGroupTwo = oldGroups[1];
                    int oldBlockGroup = oldGroups[2];
                    if ((groupOne != 0 && (groupOne == oldGroupOne || groupOne == oldGroupTwo)) || (groupTwo != 0 && (groupTwo == oldGroupOne || groupTwo == oldGroupTwo)) || (oldBlockGroup != 0 && (groupOne == oldBlockGroup || groupTwo == oldBlockGroup))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static boolean applyBuff(obj_id target, String name) throws InterruptedException
    {
        return applyBuff(target, null, getStringCrc(name.toLowerCase()), 0.0f, 0.0f);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, String name) throws InterruptedException
    {
        return applyBuff(target, owner, getStringCrc(name.toLowerCase()), 0.0f, 0.0f);
    }
    public static boolean[] applyBuff(obj_id[] targets, String name) throws InterruptedException
    {
        if (targets == null || targets.length == 0)
        {
            return null;
        }
        boolean[] returnList = new boolean[targets.length];
        for (int i = 0; i < targets.length; i++)
        {
            if (isIdValid(targets[i]))
            {
                returnList[i] = applyBuff(targets[i], name);
            }
            else 
            {
                returnList[i] = false;
            }
        }
        return returnList;
    }
    public static boolean[] applyBuff(obj_id target, String[] buffList) throws InterruptedException
    {
        boolean[] results = new boolean[buffList.length];
        for (int i = 0; i < buffList.length; i++)
        {
            results[i] = applyBuff(target, buffList[i]);
        }
        return results;
    }
    public static boolean[][] applyBuff(obj_id[] targets, String[] buffList) throws InterruptedException
    {
        boolean[][] results = new boolean[targets.length][];
        for (int i = 0; i < targets.length; i++)
        {
            results[i] = applyBuff(targets[i], buffList);
        }
        return results;
    }
    public static boolean[] applyBuff(obj_id[] targets, obj_id caster, String buffName) throws InterruptedException
    {
        boolean[] results = new boolean[targets.length];
        for (int i = 0; i < targets.length; i++)
        {
            results[i] = applyBuff(targets[i], caster, buffName);
        }
        return results;
    }
    public static boolean applyBuff(obj_id target, int nameCrc) throws InterruptedException
    {
        return applyBuff(target, null, nameCrc, 0.0f, 0.0f);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, int nameCrc) throws InterruptedException
    {
        return applyBuff(target, owner, nameCrc, 0.0f, 0.0f);
    }
    public static boolean applyBuffWithStackCount(obj_id target, String name, int stack) throws InterruptedException
    {
        return applyBuff(target, null, getStringCrc(name.toLowerCase()), 0.0f, 0.0f, stack);
    }
    public static boolean applyBuffWithStackCount(obj_id target, obj_id owner, String name, int stack) throws InterruptedException
    {
        return applyBuff(target, owner, getStringCrc(name.toLowerCase()), 0.0f, 0.0f, stack);
    }
    public static boolean applyBuff(obj_id target, String name, float duration) throws InterruptedException
    {
        return applyBuff(target, null, getStringCrc(name.toLowerCase()), duration, 0.0f);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, String name, float duration) throws InterruptedException
    {
        return applyBuff(target, owner, getStringCrc(name.toLowerCase()), duration, 0.0f);
    }
    public static boolean applyBuff(obj_id target, int nameCrc, float duration) throws InterruptedException
    {
        return applyBuff(target, null, nameCrc, duration, 0.0f);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, int nameCrc, float duration) throws InterruptedException
    {
        return applyBuff(target, owner, nameCrc, duration, 0.0f);
    }
    public static boolean applyBuff(obj_id target, String name, float duration, float customValue) throws InterruptedException
    {
        return applyBuff(target, null, getStringCrc(name.toLowerCase()), duration, customValue);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, String name, float duration, float customValue) throws InterruptedException
    {
        return applyBuff(target, owner, getStringCrc(name.toLowerCase()), duration, customValue);
    }
    public static boolean applyBuff(obj_id target, int nameCrc, float duration, float customValue) throws InterruptedException
    {
        return applyBuff(target, null, nameCrc, duration, customValue);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, int nameCrc, float duration, float customValue) throws InterruptedException
    {
        return applyBuff(target, owner, nameCrc, duration, customValue, 1);
    }
    public static boolean applyBuff(obj_id target, obj_id owner, int nameCrc, float duration, float customValue, int stack) throws InterruptedException
    {
        if (!canApplyBuff(target, owner, nameCrc))
        {
            return false;
        }
        int[] discarded = _getDiscardedBuffs(target, owner, nameCrc);
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            LOG("buff.scriptlib", "Buff CRC failed (" + nameCrc + ")");
            return false;
        }
        boolean isStackable = bdata.maxStacks > 1;
        if (discarded != null && discarded.length > 0)
        {
            obj_id caster;
            for (int aDiscarded : discarded) {
                caster = getBuffCaster(target, aDiscarded);
                if (nameCrc == aDiscarded) {
                    float oldBuffTime = getBuffTimeRemaining(target, aDiscarded);
                    if (!isStackable) {
                        if (oldBuffTime <= duration) {
                            _removeBuff(target, aDiscarded);
                        }
                    }
                } else {
                    if (owner != caster && isStackable) {
                        continue;
                    } else {
                        _removeBuff(target, aDiscarded);
                    }
                }
            }
        }
        String particle = bdata.particle;
        String hardpoint = bdata.particleHardpoint;
        String buffName = bdata.buffName;
        if (particle != null && particle.length() > 0 && buffName != null && buffName.length() > 0)
        {
            String particles[] = split(particle, ',');
            String hardpoints[] = split(hardpoint, ',');
            if (particles.length > 1)
            {
                for (int i = 0; i < particles.length; i++)
                {
                    if (hardpoints.length <= i || hardpoints[i].length() <= 0 || hardpoints[i].equals(""))
                    {
                        hardpoint = "";
                    }
                    else 
                    {
                        hardpoint = hardpoints[i];
                    }
                    playClientEffectObj(target, particles[i], target, hardpoint, null, buffName);
                }
            }
            else 
            {
                if (hardpoint.length() <= 0 || hardpoint.equals(""))
                {
                    hardpoint = "";
                }
                playClientEffectObj(target, particle, target, hardpoint, null, buffName);
            }
        }
        if (isIdValid(owner))
        {
            utils.setScriptVar(target, "buffOwner." + nameCrc, owner);
        }
        if (isGroupBuff(nameCrc))
        {
            if (!isIdValid(owner))
            {
                utils.setScriptVar(target, "groupBuff." + nameCrc, target);
            }
            else 
            {
                utils.setScriptVar(target, "groupBuff." + nameCrc, owner);
            }
        }
        if (stack < 1)
        {
            stack = 1;
        }
        return _addBuff(target, owner, nameCrc, duration, customValue, stack);
    }
    public static boolean removeBuff(obj_id target, String name) throws InterruptedException
    {
        return removeBuff(target, getStringCrc(name.toLowerCase()));
    }
    public static boolean removeBuffs(obj_id target, Vector names) throws InterruptedException
    {
        boolean success = true;
        for (Object name : names) {
            if (!removeBuff(target, ((String) name))) {
                success = false;
            }
        }
        return success;
    }
    public static boolean removeBuffs(obj_id target, String[] names) throws InterruptedException
    {
        boolean success = true;
        for (String name : names) {
            if (!removeBuff(target, name)) {
                success = false;
            }
        }
        return success;
    }
    public static boolean removeBuff(obj_id target, int nameCrc) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return false;
        }
        if (!isPlayer(target) && !isMob(target))
        {
            return false;
        }
        if (!isValidBuff(nameCrc))
        {
            return false;
        }
        utils.removeScriptVar(target, "buffOwner." + nameCrc);
        return _removeBuff(target, nameCrc);
    }
    public static boolean removeAllBuffs(obj_id target) throws InterruptedException
    {
        return removeAllBuffs(target, false);
    }
    public static boolean removeAllBuffs(obj_id target, boolean fromDeath) throws InterruptedException
    {
        return removeAllBuffs(target, fromDeath, false);
    }
    public static boolean removeAllBuffs(obj_id target, boolean fromDeath, boolean fromRespec) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return false;
        }
        if (!isPlayer(target) && !isMob(target))
        {
            return false;
        }
        if (isPlayer(target))
        {
            player_stomach.resetStomachs(target);
        }
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return true;
        }
        buff_data bdata;
        for (int b : buffs) {
            bdata = combat_engine.getBuffData(b);
            if (bdata == null) {
                LOG("buff.scriptlib", "removeAllBuffs bdata is null");
                continue;
            }
            int removeOnDeath = bdata.removeOnDeath;
            int removeOnRespec = bdata.removeOnRespec;
            if (removeOnDeath == 0 && fromDeath) {
                continue;
            }
            if (removeOnRespec == 0 && fromRespec) {
                continue;
            }
            if (isMob(target)) {
                int removeOnCombatEnd = bdata.aiRemoveOnCombatEnd;
                if (removeOnCombatEnd == 0 && !isDead(target) && !isIncapacitated(target)) {
                    continue;
                }
            }
            _removeBuff(target, b);
        }
        return true;
    }
    public static boolean removeAllDebuffsByOwner(obj_id target, obj_id owner) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target) || !isIdValid(owner) || !exists(owner))
        {
            return false;
        }
        if (target == owner)
        {
            return false;
        }
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return true;
        }
        for (int b : buffs) {
            buff_data bdata = combat_engine.getBuffData(b);
            if (bdata.debuff == 1 && getBuffOwner(target, b) == owner) {
                removeBuff(target, b);
            }
        }
        return true;
    }
    public static int[] getAllBuffsByEffect(obj_id target, String effect) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target))
        {
            return null;
        }
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return null;
        }
        Vector matchedBuffs = new Vector();
        matchedBuffs.setSize(0);
        buff_data bdata;
        String tempEffect;
        for (int b : buffs) {
            int j = 1;
            bdata = combat_engine.getBuffData(b);
            tempEffect = getEffectParam(bdata, 1);
            boolean matched = false;
            while (!matched && j < 6 && tempEffect != null && tempEffect.length() > 0) {
                if (tempEffect.equals(effect)) {
                    utils.addElement(matchedBuffs, b);
                    matched = true;
                }
                j++;
                tempEffect = getEffectParam(bdata, j);
            }
        }
        if (matchedBuffs.size() < 1)
        {
            return null;
        }
        int[] _matchedBuffs = new int[matchedBuffs.size()];
        for (int _i = 0; _i < matchedBuffs.size(); ++_i)
		{
			_matchedBuffs[_i] = (Integer) matchedBuffs.get(_i);
		}
        return _matchedBuffs;
    }
    public static int[] getAllBuffs(obj_id target) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return null;
        }
        if (!isPlayer(target) && !isMob(target))
        {
            return null;
        }
        return _getAllBuffs(target);
    }
    public static boolean hasBuff(obj_id target) throws InterruptedException
    {
        int[] buffCRCs = _getAllBuffs(target);
        if (buffCRCs == null || buffCRCs.length == 0)
        {
            return false;
        }
        return true;
    }
    public static boolean hasBuff(obj_id target, String name) throws InterruptedException
    {
        return _hasBuff(target, getStringCrc(name.toLowerCase()));
    }
    public static boolean hasBuff(obj_id target, int nameCrc) throws InterruptedException
    {
        return _hasBuff(target, nameCrc);
    }
    public static boolean hasAnyBuffInList(obj_id target, String buffList) throws InterruptedException
    {
        String[] buffs = split(buffList, ',');
        for (String buff : buffs) {
            if (hasBuff(target, buff)) {
                return true;
            }
        }
        return false;
    }
    public static boolean hasAnyBuffInList(obj_id target, String[] buffList) throws InterruptedException
    {
        for (String aBuffList : buffList) {
            if (hasBuff(target, aBuffList)) {
                return true;
            }
        }
        return false;
    }
    public static boolean refreshAllBuffs(obj_id target) throws InterruptedException
    {
        int[] buffs = _getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return true;
        }
        float[] buffs_d = new float[buffs.length];
        float[] buffs_v = new float[buffs.length];
        for (int i = 0; i < buffs.length; i++)
        {
            buffs_d[i] = _getBuffTimeRemaining(target, buffs[i]);
            buffs_v[i] = _getBuffCustomValue(target, buffs[i]);
        }
        removeAllBuffs(target, true);
        boolean success = true;
        obj_id owner;
        for (int i = 0; i < buffs.length; i++)
        {
            if (buffs_d[i] <= 0.0f)
            {
                continue;
            }
            owner = getBuffOwner(target, buffs[i]);
            if (isIdValid(owner) && owner != target)
            {
                continue;
            }
            success &= applyBuff(target, buffs[i], buffs_d[i], buffs_v[i]);
        }
        return success;
    }
    public static String getBuffNameFromCrc(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return null;
        }
        return bdata.buffName;
    }
    public static int getGroupOne(String name) throws InterruptedException
    {
        return getGroupOne(getStringCrc(name.toLowerCase()));
    }
    public static int getGroupOne(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0;
        }
        if (bdata.buffGroup1 == null || bdata.buffGroup1.length() <= 0)
        {
            return 0;
        }
        else 
        {
            return bdata.buffGroup1Crc;
        }
    }
    public static int getGroupTwo(String name) throws InterruptedException
    {
        return getGroupTwo(getStringCrc(name.toLowerCase()));
    }
    public static int getGroupTwo(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0;
        }
        if (bdata.buffGroup2 == null || bdata.buffGroup2.length() <= 0)
        {
            return 0;
        }
        else 
        {
            return bdata.buffGroup2Crc;
        }
    }
    public static String getStringGroupTwo(String name) throws InterruptedException
    {
        return getStringGroupTwo(getStringCrc(name.toLowerCase()));
    }
    public static String getStringGroupTwo(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return "";
        }
        if (bdata.buffGroup2 == null || bdata.buffGroup2.length() <= 0)
        {
            return "";
        }
        else 
        {
            return bdata.buffGroup2;
        }
    }
    public static int getBlockGroup(String name) throws InterruptedException
    {
        return getBlockGroup(getStringCrc(name.toLowerCase()));
    }
    public static int getBlockGroup(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0;
        }
        if (bdata.blockGroup == null || bdata.blockGroup.length() <= 0)
        {
            return 0;
        }
        else 
        {
            return bdata.blockGroupCrc;
        }
    }
    public static int[] getGroups(buff_data bdata) throws InterruptedException
    {
        if (bdata == null)
        {
            return null;
        }
        int[] groups = new int[3];
        if (bdata.buffGroup1 == null || bdata.buffGroup1.length() <= 0)
        {
            groups[0] = 0;
        }
        else 
        {
            groups[0] = bdata.buffGroup1Crc;
        }
        if (bdata.buffGroup2 == null || bdata.buffGroup2.length() <= 0)
        {
            groups[1] = 0;
        }
        else 
        {
            groups[1] = bdata.buffGroup2Crc;
        }
        if (bdata.blockGroup == null || bdata.blockGroup.length() <= 0)
        {
            groups[2] = 0;
        }
        else 
        {
            groups[2] = bdata.blockGroupCrc;
        }
        return groups;
    }
    public static int[] getGroups(String name) throws InterruptedException
    {
        return getGroups(getStringCrc(name.toLowerCase()));
    }
    public static int[] getGroups(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        return getGroups(bdata);
    }
    public static int getBuffOnTargetFromGroup(obj_id target, String group) throws InterruptedException
    {
        return getBuffOnTargetFromGroup(target, getStringCrc(group));
    }
    public static int getBuffOnTargetFromGroup(obj_id target, int groupCrc) throws InterruptedException
    {
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return 0;
        }
        for (int b : buffs) {
            int[] groups = getGroups(b);
            if (groups == null || groups.length == 0) {
                continue;
            }
            for (int group : groups) {
                if (group == groupCrc) {
                    return b;
                }
            }
        }
        return 0;
    }
    public static int[] getGroup2BuffsOnTarget(obj_id target, String groupName) throws InterruptedException
    {
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return null;
        }
        Vector allGroupBuffs = new Vector();
        allGroupBuffs.setSize(0);
        String tempGroupName;
        for (int b : buffs) {
            tempGroupName = getStringGroupTwo(b);
            if (tempGroupName.startsWith(groupName)) {
                utils.addElement(allGroupBuffs, b);
            }
        }
        int[] _allGroupBuffs = new int[0];
        _allGroupBuffs = new int[allGroupBuffs.size()];
        for (int _i = 0; _i < allGroupBuffs.size(); ++_i)
		{
			_allGroupBuffs[_i] = (Integer) allGroupBuffs.get(_i);
		}
        return _allGroupBuffs;
    }
    public static int getPriority(String name) throws InterruptedException
    {
        return getPriority(getStringCrc(name.toLowerCase()));
    }
    public static int getPriority(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0;
        }
        return bdata.priority;
    }
    public static float getDuration(String name) throws InterruptedException
    {
        return getDuration(getStringCrc(name.toLowerCase()));
    }
    public static float getDuration(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0.0f;
        }
        return bdata.duration;
    }
    public static int getState(String name) throws InterruptedException
    {
        return getState(getStringCrc(name.toLowerCase()));
    }
    public static int getState(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return STATE_NONE;
        }
        return bdata.buffState;
    }
    public static String getCallback(String name) throws InterruptedException
    {
        return getCallback(getStringCrc(name.toLowerCase()));
    }
    public static String getCallback(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return "";
        }
        return bdata.callback;
    }
    public static String getParticle(String name) throws InterruptedException
    {
        return getParticle(getStringCrc(name.toLowerCase()));
    }
    public static String getParticle(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return "";
        }
        return bdata.particle;
    }
    public static String getParticleHardpoint(String name) throws InterruptedException
    {
        return getParticle(getStringCrc(name.toLowerCase()));
    }
    public static String getParticleHardpoint(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return "";
        }
        return bdata.particleHardpoint;
    }
    public static String getEffectParam(String name, int effNum) throws InterruptedException
    {
        return getEffectParam(getStringCrc(name.toLowerCase()), effNum);
    }
    public static String getEffectParam(int nameCrc, int effNum) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return null;
        }
        return getEffectParam(bdata, effNum);
    }
    public static String getEffectParam(buff_data bdata, int effNum) throws InterruptedException
    {
        if (effNum <= 0 || effNum > MAX_EFFECTS)
        {
            return null;
        }
        if (bdata == null)
        {
            return null;
        }
        switch (effNum)
        {
            case 1:
                return bdata.effect1Param;
            case 2:
                return bdata.effect2Param;
            case 3:
                return bdata.effect3Param;
            case 4:
                return bdata.effect4Param;
            case 5:
                return bdata.effect5Param;
            default:
                return null;
        }
    }
    public static float getEffectValue(String name, int effNum) throws InterruptedException
    {
        return getEffectValue(getStringCrc(name.toLowerCase()), effNum);
    }
    public static float getEffectValue(int nameCrc, int effNum) throws InterruptedException
    {
        if (effNum <= 0 || effNum > MAX_EFFECTS)
        {
            return Float.NEGATIVE_INFINITY;
        }
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return 0;
        }
        switch (effNum)
        {
            case 1:
                return bdata.effect1Value;
            case 2:
                return bdata.effect2Value;
            case 3:
                return bdata.effect3Value;
            case 4:
                return bdata.effect4Value;
            case 5:
                return bdata.effect5Value;
            default:
                return 0;
        }
    }
    public static boolean isDebuff(String name) throws InterruptedException
    {
        return isDebuff(getStringCrc(name.toLowerCase()));
    }
    public static boolean isDebuff(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return false;
        }
        int debuff = bdata.debuff;
        return debuff == 1;
    }
    public static boolean canBeDispelled(String name) throws InterruptedException
    {
        return canBeDispelled(getStringCrc(name.toLowerCase()));
    }
    public static boolean canBeDispelled(int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return false;
        }
        return bdata.dispellPlayer == 1;
    }
    public static boolean isGroupBuff(String name) throws InterruptedException
    {
        return isGroupBuff(getStringCrc(name.toLowerCase()));
    }
    public static boolean isGroupBuff(int nameCrc) throws InterruptedException
    {
        String effect;
        for (int i = 1; i <= MAX_EFFECTS; i++)
        {
            effect = getEffectParam(nameCrc, i);
            if (effect != null && effect.equals("group"))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isAuraBuff(String name) throws InterruptedException
    {
        return isAuraBuff(getStringCrc(name.toLowerCase()));
    }
    public static boolean isAuraBuff(int nameCrc) throws InterruptedException
    {
        String groupTwo = getStringGroupTwo(nameCrc);
        return groupTwo.contains("aura");
    }
    public static boolean isOwnedBuff(String name) throws InterruptedException
    {
        return isOwnedBuff(getStringCrc(name.toLowerCase()));
    }
    public static boolean isOwnedBuff(int nameCrc) throws InterruptedException
    {
        obj_id self = getSelf();
        if (!utils.hasScriptVar(self, "groupBuff." + nameCrc))
        {
            return true;
        }
        obj_id owner = utils.getObjIdScriptVar(self, "groupBuff." + nameCrc);
        if (isIdValid(owner) && owner == self)
        {
            return true;
        }
        return false;
    }
    public static boolean isValidBuff(String name) throws InterruptedException
    {
        return isValidBuff(getStringCrc(name.toLowerCase()));
    }
    public static boolean isValidBuff(int nameCrc) throws InterruptedException
    {
        return (combat_engine.getBuffData(nameCrc) != null);
    }
    public static int[] _getDiscardedBuffs(obj_id target, int nameCrc) throws InterruptedException
    {
        return _getDiscardedBuffs(target, null, nameCrc);
    }
    public static int[] _getDiscardedBuffs(obj_id target, obj_id owner, int nameCrc) throws InterruptedException
    {
        buff_data bdata = combat_engine.getBuffData(nameCrc);
        if (bdata == null)
        {
            return null;
        }
        return _getDiscardedBuffs(target, owner, bdata);
    }
    public static int[] _getDiscardedBuffs(obj_id target, obj_id owner, buff_data bdata) throws InterruptedException
    {
        if (bdata == null)
        {
            return null;
        }
        int[] buffs = _getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return null;
        }
        int discardCount = 0;
        int[] discarded = new int[buffs.length];
        int[] groups = getGroups(bdata);
        if (groups == null || groups.length != 3)
        {
            return null;
        }
        int groupOne = groups[0];
        int groupTwo = groups[1];
        if (groupOne != 0 || groupTwo != 0)
        {
            int priority = bdata.priority;
            buff_data oldBuffData;
            obj_id effectOwner;
            for (int b : buffs) {
                oldBuffData = combat_engine.getBuffData(b);
                if (oldBuffData == null) {
                    continue;
                }
                effectOwner = getBuffOwner(target, b);
                if (isIdValid(effectOwner) && effectOwner != target) {
                    continue;
                }
                int oldPriority = oldBuffData.priority;
                if (priority >= oldPriority) {
                    int[] oldGroups = getGroups(oldBuffData);
                    if (oldGroups == null || oldGroups.length != 3) {
                        return null;
                    }
                    int oldGroupOne = oldGroups[0];
                    int oldGroupTwo = oldGroups[1];
                    if ((groupOne != 0 && (groupOne == oldGroupOne || groupOne == oldGroupTwo)) || (groupTwo != 0 && (groupTwo == oldGroupOne || groupTwo == oldGroupTwo))) {
                        discarded[discardCount++] = b;
                    }
                }
            }
        }
        int[] returnArray = new int[discardCount];
        System.arraycopy(discarded, 0, returnArray, 0, discardCount);
        return returnArray;
    }
    public static obj_id getBuffOwner(obj_id target, String name) throws InterruptedException
    {
        return getBuffOwner(target, getStringCrc(name.toLowerCase()));
    }
    public static obj_id getBuffOwner(obj_id target, int nameCrc) throws InterruptedException
    {
        if (!isGroupBuff(nameCrc))
        {
            return target;
        }
        if (!utils.hasScriptVar(target, "groupBuff." + nameCrc))
        {
            return null;
        }
        return utils.getObjIdScriptVar(target, "groupBuff." + nameCrc);
    }
    public static void addGroupBuffEffect(obj_id target, obj_id owner, int[] buffList, float[] strList, float[] durList) throws InterruptedException
    {
        if (!isIdValid(target) || !isIdValid(owner))
        {
            return;
        }
        if (buffList == null || buffList.length < 1)
        {
            return;
        }
        for (int i = 0; i < buffList.length; i++)
        {
            utils.setScriptVar(target, "groupBuff." + buffList[i], owner);
            applyBuff(target, owner, buffList[i], durList[i], strList[i]);
            if (beast_lib.isBeastMaster(target))
            {
                obj_id beast = beast_lib.getBeastOnPlayer(target);
                if (isIdValid(beast) && !isIdNull(beast))
                {
                    applyBuff(beast, owner, buffList[i], durList[i], strList[i]);
                }
            }
        }
    }
    public static void removeGroupBuffEffect(obj_id target, int[] buffList) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return;
        }
        if (buffList == null || buffList.length < 1)
        {
            return;
        }
        obj_id owner;
        obj_id beast;
        for (int aBuffList : buffList) {
            owner = utils.getObjIdScriptVar(target, "groupBuff." + aBuffList);
            if (owner != target) {
                _removeBuff(target, aBuffList);
                utils.removeScriptVar(target, "groupBuff." + aBuffList);
                if (beast_lib.isBeastMaster(target)) {
                    beast = beast_lib.getBeastOnPlayer(target);
                    if (isIdValid(beast) && !isIdNull(beast)) {
                        _removeBuff(beast, aBuffList);
                    }
                }
            }
        }
    }
    public static int[] getOwnedGroupBuffs(obj_id target) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return null;
        }
        int[] buffCrcList = getAllBuffs(target);
        if (buffCrcList == null || buffCrcList.length < 1)
        {
            return null;
        }
        Vector buffList = new Vector();
        buffList.setSize(0);
        obj_id owner;
        for (int aBuffCrcList : buffCrcList) {
            owner = utils.getObjIdScriptVar(target, "groupBuff." + aBuffCrcList);
            if (isIdValid(owner) && owner == target && isGroupBuff(aBuffCrcList)) {
                buffList = utils.addElement(buffList, aBuffCrcList);
            }
        }
        int[] _buffList = new int[0];
        if (buffList != null)
        {
            _buffList = new int[buffList.size()];
            for (int _i = 0; _i < buffList.size(); ++_i)
            {
                _buffList[_i] = (Integer) buffList.get(_i);
            }
        }
        return _buffList;
    }
    public static int[] getGroupBuffEffects(obj_id target) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return null;
        }
        int[] buffCrcList = getAllBuffs(target);
        if (buffCrcList == null || buffCrcList.length < 1)
        {
            return null;
        }
        Vector buffList = new Vector();
        buffList.setSize(0);
        obj_id owner;

        for (int aBuffCrcList : buffCrcList) {
            owner = utils.getObjIdScriptVar(target, "groupBuff." + aBuffCrcList);
            if (isIdValid(owner) && owner != target && isGroupBuff(aBuffCrcList)) {
                buffList = utils.addElement(buffList, aBuffCrcList);
            }
        }
        int[] _buffList = new int[0];
        if (buffList != null)
        {
            _buffList = new int[buffList.size()];
            for (int _i = 0; _i < buffList.size(); ++_i)
            {
                _buffList[_i] = (Integer) buffList.get(_i);
            }
        }
        return _buffList;
    }
    public static float[] getGroupBuffDuration(obj_id player, int[] buffList) throws InterruptedException
    {
        if (buffList == null || buffList.length == 0)
        {
            return null;
        }
        float[] durationList = new float[buffList.length];
        for (int i = 0; i < buffList.length; i++)
        {
            durationList[i] = _getBuffTimeRemaining(player, buffList[i]);
        }
        return durationList;
    }
    public static float[] getGroupBuffStrength(obj_id player, int[] buffList) throws InterruptedException
    {
        if (buffList == null || buffList.length == 0)
        {
            return null;
        }
        float[] strengthList = new float[buffList.length];
        for (int i = 0; i < buffList.length; i++)
        {
            strengthList[i] = _getBuffCustomValue(player, buffList[i]);
        }
        return strengthList;
    }
    public static boolean checkForStateImmunity(obj_id target, buff_data bdata) throws InterruptedException
    {
        if (bdata.buffState == STATE_NONE)
        {
            return false;
        }
        else if (bdata.buffState == STATE_STUNNED && utils.hasScriptVar(target, "immunity.state.stun"))
        {
            LOG("Immunity//STATE:", "Block a STUN state from being applied on ---" + getPlayerName(target));
            return true;
        }
        else 
        {
            return false;
        }
    }
    public static boolean removeAllBuffsOfStateType(obj_id target, int stateType) throws InterruptedException
    {
        boolean removed = false;
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return true;
        }
        for (int b : buffs) {
            if (isDebuff(b) && getState(b) == stateType) {
                _removeBuff(target, b);
                removed = true;
            }
        }
        return removed;
    }
    public static boolean toggleStance(obj_id player, String attemptedBuff) throws InterruptedException
    {
        int[] buffs = getAllBuffs(player);
        if (buffs == null)
        {
            return false;
        }
        buff_data attemptedBuffData = combat_engine.getBuffData(getStringCrc(attemptedBuff.toLowerCase()));
        if (attemptedBuffData == null)
        {
            return false;
        }
        String groupAtt = attemptedBuffData.buffGroup1;
        buff_data bdata;
        String curBuff;
        String groupCur;
        for (int b : buffs) {
            bdata = combat_engine.getBuffData(b);
            if (bdata == null) {
                continue;
            }
            curBuff = bdata.buffName;
            groupCur = bdata.buffGroup1;
            if (groupCur.equals(groupAtt)) {
                removeBuff(player, curBuff);
                if (!curBuff.equals(attemptedBuff.toLowerCase())) {
                    applyBuff(player, player, attemptedBuff);
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    public static boolean isInStance(obj_id player) throws InterruptedException
    {
        if (isPlayer(player))
        {
            retirePostNgeForceSensitiveStanceState(player);
            return false;
        }
        return true;
    }
    public static boolean isInFocus(obj_id player) throws InterruptedException
    {
        if (isPlayer(player))
        {
            retirePostNgeForceSensitiveStanceState(player);
            return false;
        }
        return true;
    }
    public static boolean playStanceVisual(obj_id target, String effectName) throws InterruptedException
    {
        effectName = effectName.substring(0, (effectName.lastIndexOf("_")));
        buff_data bdata = combat_engine.getBuffData(getStringCrc(effectName.toLowerCase()));
        if (bdata == null)
        {
            return false;
        }
        String effectPlayed = bdata.stanceParticle;
        if (effectPlayed != null && effectPlayed.length() > 0)
        {
            playClientEffectObj(target, effectPlayed, target, "");
            return true;
        }
        else 
        {
            return false;
        }
    }
    public static boolean isDotIconOnlyBuff(String name) throws InterruptedException
    {
        boolean isDotIconOnly = false;
        if (name.equals("bleeding") || name.equals("poisoned") || name.equals("diseased") || name.equals("onfire"))
        {
            isDotIconOnly = true;
        }
        return isDotIconOnly;
    }
    public static long getBuffStackCount(obj_id target, int nameCrc) throws InterruptedException
    {
        return _getBuffStackCount(target, nameCrc);
    }
    public static long getBuffStackCount(obj_id target, String nameCrc) throws InterruptedException
    {
        return _getBuffStackCount(target, getStringCrc(nameCrc.toLowerCase()));
    }
    public static boolean decrementBuffStack(obj_id target, int nameCrc, int stacksToRemove) throws InterruptedException
    {
        return _decrementBuffStack(target, nameCrc, stacksToRemove);
    }
    public static boolean decrementBuffStack(obj_id target, String nameCrc, int stacksToRemove) throws InterruptedException
    {
        return _decrementBuffStack(target, getStringCrc(nameCrc.toLowerCase()), stacksToRemove);
    }
    public static boolean decrementBuffStack(obj_id target, int nameCrc) throws InterruptedException
    {
        return _decrementBuffStack(target, nameCrc, 1);
    }
    public static boolean decrementBuffStack(obj_id target, String nameCrc) throws InterruptedException
    {
        return _decrementBuffStack(target, getStringCrc(nameCrc.toLowerCase()), 1);
    }
    public static obj_id getBuffCaster(obj_id target, int nameCrc) throws InterruptedException
    {
        return obj_id.getObjId(_getBuffCaster(target, nameCrc));
    }
    public static obj_id getBuffCaster(obj_id target, String nameCrc) throws InterruptedException
    {
        return obj_id.getObjId(_getBuffCaster(target, getStringCrc(nameCrc.toLowerCase())));
    }
    public static float getBuffTimeRemaining(obj_id target, int nameCrc) throws InterruptedException
    {
        return _getBuffTimeRemaining(target, nameCrc);
    }
    public static float getBuffTimeRemaining(obj_id target, String nameCrc) throws InterruptedException
    {
        return _getBuffTimeRemaining(target, getStringCrc(nameCrc.toLowerCase()));
    }
    public static int[] getAllDotBuffsOfType(obj_id target, String type) throws InterruptedException
    {
        int[] allDotBuffs = getAllBuffsByEffect(target, "dot");
        Vector allDotBuffsOfType = new Vector();
        allDotBuffsOfType.setSize(0);
        if (allDotBuffs == null || allDotBuffs.length <= 0)
        {
            return null;
        }
        String param1;
        for (int allDotBuff : allDotBuffs) {
            param1 = getEffectParam(allDotBuff, 1);
            if (param1.equals(type)) {
                utils.addElement(allDotBuffsOfType, allDotBuff);
            }
        }
        int[] _allDotBuffsOfType = new int[0];
        _allDotBuffsOfType = new int[allDotBuffsOfType.size()];
        for (int _i = 0; _i < allDotBuffsOfType.size(); ++_i)
		{
			_allDotBuffsOfType[_i] = (Integer) allDotBuffsOfType.get(_i);
		}
        return _allDotBuffsOfType;
    }
    public static boolean performBuffDotImmunity(obj_id target, String dotType) throws InterruptedException
    {
        if (dotType.equals("all"))
        {
            int[] allDotBuffs = getAllBuffsByEffect(target, "dot");
            if (allDotBuffs == null || allDotBuffs.length <= 0)
            {
                return false;
            }
            for (int allDotBuff : allDotBuffs) {
                removeBuff(target, allDotBuff);
            }
            return true;
        }
        int[] allBuffDotsOfType = buff.getAllDotBuffsOfType(target, dotType);
        if (allBuffDotsOfType == null || allBuffDotsOfType.length <= 0)
        {
            return false;
        }
        for (int anAllBuffDotsOfType : allBuffDotsOfType) {
            removeBuff(target, anAllBuffDotsOfType);
        }
        return true;
    }
    public static boolean isBuffDot(String buffName) throws InterruptedException
    {
        return buff.getEffectParam(buffName, 2).equals("dot");
    }
    public static void reduceBuffDotStackCount(obj_id target, String dotType, int count) throws InterruptedException
    {
        int[] allBuffsOfType = buff.getAllDotBuffsOfType(target, dotType);
        if (allBuffsOfType == null || allBuffsOfType.length == 0)
        {
            return;
        }
        for (int anAllBuffsOfType : allBuffsOfType) {
            long stackCount = getBuffStackCount(target, anAllBuffsOfType);
            if (stackCount <= count) {
                removeBuff(target, anAllBuffsOfType);
            } else {
                decrementBuffStack(target, anAllBuffsOfType, count);
            }
        }
    }
    public static void divideBuffDotStackCount(obj_id target, String dotType, int reduction) throws InterruptedException
    {
        int[] allBuffsOfType = buff.getAllDotBuffsOfType(target, dotType);
        if (allBuffsOfType == null || allBuffsOfType.length == 0)
        {
            return;
        }
        for (int anAllBuffsOfType : allBuffsOfType) {
            long stackCount = getBuffStackCount(target, anAllBuffsOfType);
            if (stackCount <= 1) {
                removeBuff(target, anAllBuffsOfType);
            } else {
                reduction = Math.round(stackCount * (reduction / 100.0f));
                reduction = reduction < 1 ? 1 : reduction;
                if (stackCount <= reduction) {
                    removeBuff(target, anAllBuffsOfType);
                } else {
                    decrementBuffStack(target, anAllBuffsOfType, reduction);
                }
            }
        }
    }
    public static boolean removeAllAuraBuffs(obj_id player) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return false;
        }
        int[] allBuffs = buff.getAllBuffs(player);
        if (allBuffs != null && allBuffs.length > 0)
        {
            for (int allBuff : allBuffs) {
                if (buff.isAuraBuff(allBuff)) {
                    buff.removeBuff(player, allBuff);
                }
            }
        }
        return true;
    }
    public static int getBuffWithEffect(obj_id player, String effect) throws InterruptedException
    {
        if (!isIdValid(player) || !exists(player))
        {
            return -1;
        }
        if (effect == null || effect.equals(""))
        {
            return -1;
        }
        int[] allBuffs = getAllBuffs(player);
        if (allBuffs != null && allBuffs.length > 0)
        {
            String tempEffect;
            for (int allBuff : allBuffs) {
                tempEffect = getEffectParam(allBuff, 1);
                if (effect.equals(tempEffect)) {
                    return allBuff;
                }
            }
        }
        return -1;
    }
    public static void partyBuff(obj_id caster, String buffName) throws InterruptedException
    {
        Vector toBuff = new Vector();
        toBuff.setSize(0);
        obj_id myBeast = beast_lib.getBeastOnPlayer(caster);
        obj_id groupId = getGroupObject(caster);
        if (isIdValid(myBeast) && exists(myBeast))
        {
            toBuff.add(myBeast);
        }
        if (isIdValid(groupId))
        {
            obj_id[] groupPlayers = getGroupMemberIds(groupId);
            obj_id thisBeast;
            for (obj_id groupPlayer : groupPlayers) {
                if (isIdValid(groupPlayer) && exists(groupPlayer) && pvpCanHelp(caster, groupPlayer) && getDistance(groupPlayer, caster) < 100.0f) {
                    toBuff.add(groupPlayer);
                    thisBeast = beast_lib.getBeastOnPlayer(groupPlayer);
                    if (isIdValid(thisBeast) && exists(thisBeast)) {
                        toBuff.add(thisBeast);
                    }
                }
            }
        }
        else 
        {
            toBuff.add(caster);
        }
        obj_id[] buffList = new obj_id[0];
        buffList = new obj_id[toBuff.size()];
        toBuff.toArray(buffList);
        applyBuff(buffList, caster, buffName);
    }
    public static boolean decayBuff(obj_id target, String buffName) throws InterruptedException
    {
        return decayBuff(target, getStringCrc(buffName.toLowerCase()), 0.10f);
    }
    public static boolean decayBuff(obj_id target, String buffName, float percent) throws InterruptedException
    {
        return decayBuff(target, getStringCrc(buffName.toLowerCase()), percent);
    }
    public static boolean decayBuff(obj_id target, int buffCrc, float percent) throws InterruptedException
    {
        if (!isIdValid(target) || !exists(target))
        {
            return false;
        }
        if (buffCrc == 0 || percent < 0.0f || percent > 1.0f)
        {
            return false;
        }
        return _decayBuff(target, buffCrc, percent);
    }
    public static boolean decayAllBuffsFromPvpDeath(obj_id target) throws InterruptedException
    {
        return decayAllBuffsFromPvpDeath(target, 0.10f);
    }
    public static boolean decayAllBuffsFromPvpDeath(obj_id target, float percent) throws InterruptedException
    {
        if (!isIdValid(target))
        {
            return false;
        }
        if (!isPlayer(target))
        {
            return false;
        }
        int[] buffs = getAllBuffs(target);
        if (buffs == null || buffs.length == 0)
        {
            return true;
        }
        buff_data bdata;
        for (int b : buffs) {
            bdata = combat_engine.getBuffData(b);
            if (bdata == null) {
                LOG("buff.scriptlib", "decayAllBuffsFromPvpDeath bdata is null");
                continue;
            }
            int decayOnPvpDeath = bdata.decayOnPvpDeath;
            int removalOnDeath = bdata.removeOnDeath;
            if (decayOnPvpDeath == 0 && removalOnDeath == 0) {
                continue;
            } else if (decayOnPvpDeath == 0 && removalOnDeath == 1) {
                _removeBuff(target, b);
                continue;
            } else if (removalOnDeath == 0) {
                continue;
            }
            _decayBuff(target, b, percent);
        }
        return true;
    }
}
