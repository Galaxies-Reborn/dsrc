package script.test;

import script.obj_id;
import script.library.skill;

/**
 * Identity-bound and reversible fixture for authentic Doctor tier IV.
 *
 * Preparation owns the complete Medic through Doctor tier-III prerequisite
 * vector. Purchase invokes the production validation, grant, and XP-deduction
 * operations for all four tier-IV boxes. ServerConsole has no owner context,
 * so only the post-purchase holocron notification is intentionally omitted.
 */
public class precu_doctor_tier4_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT = "precu.doctorTier4Fixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String ORIGINAL_CRAFTING_XP =
        ROOT + ".originalCraftingXp";
    private static final String ORIGINAL_POINTS = ROOT + ".originalPoints";
    private static final String BASE_MODS = ROOT + ".baseMods";
    private static final String MEDICAL_XP = "medical";
    private static final String CRAFTING_XP =
        "crafting_medicine_general";
    private static final int PREPARED_MEDICAL_XP = 60000;
    private static final int PREPARED_CRAFTING_XP = 33000;
    private static final String[] PREREQUISITES =
    {
        "science_medic_novice",
        "science_medic_injury_01",
        "science_medic_injury_speed_01",
        "science_medic_ability_01",
        "science_medic_crafting_01",
        "science_medic_injury_02",
        "science_medic_injury_speed_02",
        "science_medic_ability_02",
        "science_medic_crafting_02",
        "science_medic_injury_03",
        "science_medic_injury_speed_03",
        "science_medic_ability_03",
        "science_medic_crafting_03",
        "science_medic_injury_04",
        "science_medic_injury_speed_04",
        "science_medic_ability_04",
        "science_medic_crafting_04",
        "science_medic_master",
        "science_doctor_novice",
        "science_doctor_wound_01",
        "science_doctor_wound_speed_01",
        "science_doctor_ability_01",
        "science_doctor_support_01",
        "science_doctor_wound_02",
        "science_doctor_wound_speed_02",
        "science_doctor_ability_02",
        "science_doctor_support_02",
        "science_doctor_wound_03",
        "science_doctor_wound_speed_03",
        "science_doctor_ability_03",
        "science_doctor_support_03"
    };
    private static final String[] TIER_FOUR =
    {
        "science_doctor_wound_04",
        "science_doctor_wound_speed_04",
        "science_doctor_ability_04",
        "science_doctor_support_04"
    };
    private static final String[] MODS =
    {
        "healing_wound_treatment",
        "healing_wound_speed",
        "healing_ability",
        "medicine_assembly",
        "medicine_experimentation"
    };
    private static final int[] EXPECTED_MOD_DELTAS =
    {
        25, 25, 10, 10, 10
    };
    private static final String[] EXPECTED_COMMANDS =
    {
        "healState",
        "registerWithLocation",
        "healEnhance",
        "curePoison",
        "extinguishFire",
        "cureDisease",
        "revivePlayer"
    };
    private static final String[] NGE_COMMANDS =
    {
        "bactaShot_1",
        "healWound_1",
        "battle_move_mitigate_2",
        "disinfect",
        "diseaseInnoculation",
        "serotoninInjection",
        "adrenalBoost",
        "battle_accuracy_mitigate_2",
        "bactaJab_1",
        "bandage_1",
        "countertoxin_1",
        "stabilizers_1",
        "poisonInnoculation_1",
        "nutrientInjection_1",
        "endorphineInjection_1",
        "battle_firerate_mitigate_2",
        "bactaInfusion_2",
        "battle_move_mitigate_3",
        "extinguishFire_1",
        "disinfect_1",
        "diseaseInnoculation_1",
        "serotoninInjection_1",
        "adrenalBoost_1",
        "battle_accuracy_mitigate_3"
    };
    private static final String[] PREREQUISITE_SCHEMATICS =
    {
        "object/draft_schematic/chemistry/med_stimpack_state_blinded_a.iff",
        "object/draft_schematic/chemistry/med_stimpack_state_dizzy_a.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_a.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_area_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_action_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_health_a.iff",
        "object/draft_schematic/chemistry/med_stimpack_state_intimidated_a.iff",
        "object/draft_schematic/chemistry/med_stimpack_state_stunned_a.iff",
        "object/draft_schematic/chemistry/medpack_cure_disease_a.iff",
        "object/draft_schematic/chemistry/medpack_cure_disease_area_a.iff",
        "object/draft_schematic/chemistry/medpack_wound_action_c.iff",
        "object/draft_schematic/chemistry/medpack_wound_health_c.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_b.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_area_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_action_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_health_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_constitution_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_quickness_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_stamina_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_strength_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_poison_a.iff",
        "object/draft_schematic/chemistry/medpack_enhance_disease_a.iff",
        "object/draft_schematic/chemistry/med_stimpack_e.iff",
        "object/draft_schematic/chemistry/medpack_wound_quickness_c.iff",
        "object/draft_schematic/chemistry/medpack_wound_stamina_c.iff",
        "object/draft_schematic/chemistry/medpack_wound_strength_c.iff",
        "object/draft_schematic/chemistry/medpack_wound_constitution_c.iff",
        "object/draft_schematic/chemistry/medpack_cure_disease_b.iff",
        "object/draft_schematic/chemistry/medpack_cure_disease_area_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_action_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_health_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_constitution_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_quickness_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_stamina_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_strength_b.iff"
    };
    private static final String[] TIER_SCHEMATICS =
    {
        "object/draft_schematic/chemistry/medpack_wound_action_d.iff",
        "object/draft_schematic/chemistry/medpack_wound_health_d.iff",
        "object/draft_schematic/chemistry/medpack_revive.iff",
        "object/draft_schematic/chemistry/medpack_wound_quickness_d.iff",
        "object/draft_schematic/chemistry/medpack_wound_stamina_d.iff",
        "object/draft_schematic/chemistry/medpack_wound_strength_d.iff",
        "object/draft_schematic/chemistry/medpack_wound_constitution_d.iff",
        "object/draft_schematic/chemistry/med_fire_blanket.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_c.iff",
        "object/draft_schematic/chemistry/medpack_cure_poison_area_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_action_d.iff",
        "object/draft_schematic/chemistry/medpack_enhance_health_d.iff",
        "object/draft_schematic/chemistry/medpack_enhance_constitution_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_quickness_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_stamina_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_strength_c.iff",
        "object/draft_schematic/chemistry/medpack_enhance_poison_b.iff",
        "object/draft_schematic/chemistry/medpack_enhance_disease_b.iff"
    };
    private static final String USAGE =
        "usage: prepare|purchase|status|cleanup <playerOid> <lifecycle>";

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
        String ownership = validateOwnership(player, args[2]);
        if (ownership != null)
        {
            if (args[0].equalsIgnoreCase("cleanup") &&
                ownership.equals("error=fixtureAbsent"))
            {
                return "action=cleanup alreadyClean=true restored=true";
            }
            return ownership;
        }
        if (args[0].equalsIgnoreCase("purchase"))
        {
            return purchase(player);
        }
        if (args[0].equalsIgnoreCase("status"))
        {
            return "action=status " + buildStatus(player);
        }
        if (args[0].equalsIgnoreCase("cleanup"))
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
            if (ownership == null && getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " + buildStatus(player);
            }
            return ownership == null ? "error=fixtureIncomplete" : ownership;
        }
        if (hasAnySkill(player) || hasAnyCommand(player) ||
            hasAnySchematic(player))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, MEDICAL_XP));
        setObjVar(player, ORIGINAL_CRAFTING_XP,
            getExperiencePoints(player, CRAFTING_XP));
        setObjVar(player, ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));

        if (!grantPrerequisites(player))
        {
            revokePrerequisites(player);
            removeObjVar(player, ROOT);
            return "error=prerequisiteGrantFailed";
        }

        int[] baseMods = new int[MODS.length];
        for (int index = 0; index < MODS.length; ++index)
        {
            baseMods[index] = getSkillStatMod(player, MODS[index]);
        }
        setObjVar(player, BASE_MODS, baseMods);

        if (!setXpExact(player, MEDICAL_XP, PREPARED_MEDICAL_XP) ||
            !setXpExact(player, CRAFTING_XP, PREPARED_CRAFTING_XP))
        {
            restore(player);
            removeObjVar(player, ROOT);
            return "error=xpPreparationFailed";
        }
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " + buildStatus(player);
    }

    private String purchase(obj_id player)
        throws InterruptedException
    {
        if (getIntObjVar(player, PREPARED) != 1)
        {
            return "error=fixtureNotPrepared";
        }
        boolean wound = purchaseWithoutHolocron(player, TIER_FOUR[0]);
        boolean speedXp =
            setXpExact(player, MEDICAL_XP, PREPARED_MEDICAL_XP);
        boolean speed = speedXp &&
            purchaseWithoutHolocron(player, TIER_FOUR[1]);
        boolean abilityXp =
            setXpExact(player, MEDICAL_XP, PREPARED_MEDICAL_XP);
        boolean ability = abilityXp &&
            purchaseWithoutHolocron(player, TIER_FOUR[2]);
        boolean support =
            purchaseWithoutHolocron(player, TIER_FOUR[3]);
        return "action=purchase" +
            " wound=" + wound +
            " speed=" + speed +
            " ability=" + ability +
            " support=" + support +
            " " + buildStatus(player);
    }

    private boolean purchaseWithoutHolocron(obj_id player, String skillName)
        throws InterruptedException
    {
        int pointsRequired = skill.getSkillPointCost(skillName);
        if (pointsRequired < 0 ||
            skill.getAvailableSkillPoints(player) < pointsRequired ||
            !skill.hasRequiredSkillsForSkillPurchase(player, skillName) ||
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

    private boolean grantPrerequisites(obj_id player)
        throws InterruptedException
    {
        for (int index = 0; index < PREREQUISITES.length; ++index)
        {
            if (!grantSkill(player, PREREQUISITES[index]) ||
                !hasSkill(player, PREREQUISITES[index]))
            {
                return false;
            }
        }
        return true;
    }

    private String cleanup(obj_id player)
        throws InterruptedException
    {
        boolean restored = restore(player);
        if (restored)
        {
            removeObjVar(player, ROOT);
        }
        return "action=cleanup alreadyClean=false restored=" + restored;
    }

    private boolean restore(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        for (int index = TIER_FOUR.length - 1; index >= 0; --index)
        {
            if (hasSkill(player, TIER_FOUR[index]))
            {
                revokeSkill(player, TIER_FOUR[index]);
            }
        }
        revokePrerequisites(player);
        boolean xpRestored =
            setXpExact(player, MEDICAL_XP,
                getIntObjVar(player, ORIGINAL_MEDICAL_XP)) &&
            setXpExact(player, CRAFTING_XP,
                getIntObjVar(player, ORIGINAL_CRAFTING_XP));
        return xpRestored &&
            !hasAnySkill(player) &&
            !hasAnyCommand(player) &&
            !hasAnySchematic(player) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS);
    }

    private void revokePrerequisites(obj_id player)
        throws InterruptedException
    {
        for (int index = PREREQUISITES.length - 1; index >= 0; --index)
        {
            if (hasSkill(player, PREREQUISITES[index]))
            {
                revokeSkill(player, PREREQUISITES[index]);
            }
        }
    }

    private boolean setXpExact(obj_id player, String xpType, int target)
        throws InterruptedException
    {
        int current = getExperiencePoints(player, xpType);
        if (current != target)
        {
            grantExperiencePoints(player, xpType, target - current);
        }
        return getExperiencePoints(player, xpType) == target;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        int[] baseMods = hasObjVar(player, BASE_MODS)
            ? getIntArrayObjVar(player, BASE_MODS)
            : new int[0];
        String deltas = "";
        for (int index = 0; index < MODS.length; ++index)
        {
            int baseline = baseMods != null && index < baseMods.length
                ? baseMods[index]
                : 0;
            if (index > 0)
            {
                deltas += ",";
            }
            deltas += MODS[index] + ":" +
                (getSkillStatMod(player, MODS[index]) - baseline);
        }
        return "player=" + player +
            " prerequisites=" + buildSkillBits(player, PREREQUISITES) +
            " tierSkills=" + buildSkillBits(player, TIER_FOUR) +
            " commands=" + buildCommandBits(player, EXPECTED_COMMANDS) +
            " ngeCommands=" + buildCommandBits(player, NGE_COMMANDS) +
            " schematics=" + buildSchematicBits(player, TIER_SCHEMATICS) +
            " modDeltas=" + deltas +
            " modContract=" + buildExpectedMods() +
            " medicalXp=" + getExperiencePoints(player, MEDICAL_XP) +
            " medicalCap=" + getExperienceCap(player, MEDICAL_XP) +
            " craftingXp=" + getExperiencePoints(player, CRAFTING_XP) +
            " craftingCap=" + getExperienceCap(player, CRAFTING_XP) +
            " availablePoints=" + skill.getAvailableSkillPoints(player);
    }

    private String buildExpectedMods()
    {
        String values = "";
        for (int index = 0; index < EXPECTED_MOD_DELTAS.length; ++index)
        {
            if (index > 0)
            {
                values += ",";
            }
            values += EXPECTED_MOD_DELTAS[index];
        }
        return values;
    }

    private String buildSkillBits(obj_id player, String[] values)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < values.length; ++index)
        {
            bits += hasSkill(player, values[index]) ? "1" : "0";
        }
        return bits;
    }

    private String buildCommandBits(obj_id player, String[] values)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < values.length; ++index)
        {
            bits += hasCommand(player, values[index]) ? "1" : "0";
        }
        return bits;
    }

    private String buildSchematicBits(obj_id player, String[] values)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < values.length; ++index)
        {
            bits += hasSchematic(player, values[index]) ? "1" : "0";
        }
        return bits;
    }

    private boolean containsSkill(obj_id player, String[] values)
        throws InterruptedException
    {
        for (int index = 0; index < values.length; ++index)
        {
            if (hasSkill(player, values[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean containsCommand(obj_id player, String[] values)
        throws InterruptedException
    {
        for (int index = 0; index < values.length; ++index)
        {
            if (hasCommand(player, values[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean containsSchematic(obj_id player, String[] values)
        throws InterruptedException
    {
        for (int index = 0; index < values.length; ++index)
        {
            if (hasSchematic(player, values[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnySkill(obj_id player)
        throws InterruptedException
    {
        return containsSkill(player, PREREQUISITES) ||
            containsSkill(player, TIER_FOUR);
    }

    private boolean hasAnyCommand(obj_id player)
        throws InterruptedException
    {
        return containsCommand(player, EXPECTED_COMMANDS) ||
            containsCommand(player, NGE_COMMANDS);
    }

    private boolean hasAnySchematic(obj_id player)
        throws InterruptedException
    {
        return containsSchematic(player, PREREQUISITE_SCHEMATICS) ||
            containsSchematic(player, TIER_SCHEMATICS);
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return hasObjVar(player, ORIGINAL_MEDICAL_XP) &&
            hasObjVar(player, ORIGINAL_CRAFTING_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, BASE_MODS);
    }

    private String validateOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) || !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null
            : "error=lifecycleMismatch";
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
