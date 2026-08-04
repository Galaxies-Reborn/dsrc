package script.test;

import script.obj_id;
import script.library.skill;

/**
 * Identity-bound and reversible fixture for authentic Medic tier III.
 *
 * Novice through tier II are granted only as ordered prerequisites. The
 * resulting modifier vector is snapshotted before all four tier-III boxes are
 * purchased through production validation, grant, and XP deduction.
 */
public class precu_medic_tier3_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT = "precu.medicTier3Fixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String PREPARED = ROOT + ".prepared";
    private static final String ORIGINAL_MEDICAL_XP =
        ROOT + ".originalMedicalXp";
    private static final String ORIGINAL_CRAFTING_XP =
        ROOT + ".originalCraftingXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String BASE_MODS = ROOT + ".baseMods";
    private static final String MEDICAL_XP = "medical";
    private static final String CRAFTING_XP =
        "crafting_medicine_general";
    private static final int PREPARED_MEDICAL_XP = 10000;
    private static final int PREPARED_CRAFTING_XP = 3000;
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
        "science_medic_crafting_02"
    };
    private static final String[] TIER_THREE =
    {
        "science_medic_injury_03",
        "science_medic_injury_speed_03",
        "science_medic_ability_03",
        "science_medic_crafting_03"
    };
    private static final String[] COMMANDS =
    {
        "private_medic_injury_3",
        "private_medic_speed_3",
        "private_medic_ability_3",
        "private_medic_crafting_3"
    };
    private static final String[] MODS =
    {
        "healing_injury_treatment",
        "healing_injury_speed",
        "healing_ability",
        "medical_foraging",
        "medicine_assembly",
        "medicine_experimentation"
    };
    private static final String[] SCHEMATICS =
    {
        "object/draft_schematic/chemistry/medpack_wound_quickness_a.iff",
        "object/draft_schematic/chemistry/medpack_wound_stamina_a.iff",
        "object/draft_schematic/chemistry/medpack_wound_strength_a.iff",
        "object/draft_schematic/chemistry/medpack_wound_constitution_a.iff",
        "object/draft_schematic/chemistry/medpack_wound_action_b.iff",
        "object/draft_schematic/chemistry/medpack_wound_health_b.iff"
    };
    private static final String USAGE =
        "usage: prepare|purchase|status|cleanup <playerOid> <lifecycle>";

    public String executeFixture(String params)
        throws InterruptedException
    {
        String[] args =
            params == null
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
                return
                    "action=cleanup alreadyClean=true restored=true";
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
            if (ownership == null &&
                getIntObjVar(player, PREPARED) == 1)
            {
                return "action=prepare resumed=true " +
                    buildStatus(player);
            }
            return ownership == null
                ? "error=fixtureIncomplete"
                : ownership;
        }
        if (hasAnySkill(player, PREREQUISITES) ||
            hasAnySkill(player, TIER_THREE))
        {
            return "error=fixtureSkillsAlreadyOwned";
        }
        if (hasAnyCommand(player) ||
            hasAnySchematic(player))
        {
            return "error=fixtureVectorAlreadyOwned";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_MEDICAL_XP,
            getExperiencePoints(player, MEDICAL_XP));
        setObjVar(
            player,
            ORIGINAL_CRAFTING_XP,
            getExperiencePoints(player, CRAFTING_XP));
        setObjVar(
            player,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));

        if (!grantPrerequisites(player))
        {
            revokeSkills(player, PREREQUISITES);
            removeObjVar(player, ROOT);
            return "error=prerequisiteGrantFailed";
        }
        int[] baseMods = new int[MODS.length];
        for (int index = 0; index < MODS.length; ++index)
        {
            baseMods[index] =
                getSkillStatMod(player, MODS[index]);
        }
        setObjVar(player, BASE_MODS, baseMods);

        if (!setXpExact(
                player,
                MEDICAL_XP,
                PREPARED_MEDICAL_XP) ||
            !setXpExact(
                player,
                CRAFTING_XP,
                PREPARED_CRAFTING_XP))
        {
            restore(player);
            removeObjVar(player, ROOT);
            return "error=xpPreparationFailed";
        }
        setObjVar(player, PREPARED, 1);
        return "action=prepare resumed=false " +
            buildStatus(player);
    }

    private String purchase(obj_id player)
        throws InterruptedException
    {
        if (getIntObjVar(player, PREPARED) != 1)
        {
            return "error=fixtureNotPrepared";
        }
        boolean injury =
            purchaseWithoutHolocron(player, TIER_THREE[0]);
        boolean speedXp =
            setXpExact(
                player,
                MEDICAL_XP,
                PREPARED_MEDICAL_XP);
        boolean speed =
            speedXp &&
            purchaseWithoutHolocron(player, TIER_THREE[1]);
        boolean abilityXp =
            setXpExact(
                player,
                MEDICAL_XP,
                PREPARED_MEDICAL_XP);
        boolean ability =
            abilityXp &&
            purchaseWithoutHolocron(player, TIER_THREE[2]);
        boolean crafting =
            purchaseWithoutHolocron(player, TIER_THREE[3]);
        return
            "action=purchase" +
            " injury=" + injury +
            " speed=" + speed +
            " ability=" + ability +
            " crafting=" + crafting +
            " " + buildStatus(player);
    }

    private boolean purchaseWithoutHolocron(
        obj_id player,
        String skillName)
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
        for (int index = 0;
            index < PREREQUISITES.length;
            ++index)
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
        return
            "action=cleanup alreadyClean=false restored=" +
            restored;
    }

    private boolean restore(obj_id player)
        throws InterruptedException
    {
        if (!hasCompleteSnapshot(player))
        {
            return false;
        }
        revokeSkills(player, TIER_THREE);
        revokeSkills(player, PREREQUISITES);
        boolean xpRestored =
            setXpExact(
                player,
                MEDICAL_XP,
                getIntObjVar(player, ORIGINAL_MEDICAL_XP)) &&
            setXpExact(
                player,
                CRAFTING_XP,
                getIntObjVar(player, ORIGINAL_CRAFTING_XP));
        return
            xpRestored &&
            !hasAnySkill(player, PREREQUISITES) &&
            !hasAnySkill(player, TIER_THREE) &&
            !hasAnyCommand(player) &&
            !hasAnySchematic(player) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS);
    }

    private void revokeSkills(
        obj_id player,
        String[] skills)
        throws InterruptedException
    {
        for (int index = skills.length - 1;
            index >= 0;
            --index)
        {
            if (hasSkill(player, skills[index]))
            {
                revokeSkill(player, skills[index]);
            }
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

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        int[] baseMods = hasObjVar(player, BASE_MODS)
            ? getIntArrayObjVar(player, BASE_MODS)
            : new int[0];
        String deltas = "";
        for (int index = 0; index < MODS.length; ++index)
        {
            int baseline =
                baseMods != null && index < baseMods.length
                    ? baseMods[index]
                    : 0;
            if (index > 0)
            {
                deltas += ",";
            }
            deltas +=
                MODS[index] + ":" +
                (getSkillStatMod(player, MODS[index]) -
                    baseline);
        }
        return
            "player=" + player +
            " prerequisites=" +
                buildSkillBits(player, PREREQUISITES) +
            " tierSkills=" +
                buildSkillBits(player, TIER_THREE) +
            " commands=" + buildCommandBits(player) +
            " schematics=" + buildSchematicBits(player) +
            " modDeltas=" + deltas +
            " medicalXp=" +
                getExperiencePoints(player, MEDICAL_XP) +
            " medicalCap=" +
                getExperienceCap(player, MEDICAL_XP) +
            " craftingXp=" +
                getExperiencePoints(player, CRAFTING_XP) +
            " craftingCap=" +
                getExperienceCap(player, CRAFTING_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player);
    }

    private String buildSkillBits(
        obj_id player,
        String[] skills)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < skills.length; ++index)
        {
            bits += hasSkill(player, skills[index])
                ? "1"
                : "0";
        }
        return bits;
    }

    private String buildCommandBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < COMMANDS.length; ++index)
        {
            bits += hasCommand(player, COMMANDS[index])
                ? "1"
                : "0";
        }
        return bits;
    }

    private String buildSchematicBits(obj_id player)
        throws InterruptedException
    {
        String bits = "";
        for (int index = 0; index < SCHEMATICS.length; ++index)
        {
            bits += hasSchematic(player, SCHEMATICS[index])
                ? "1"
                : "0";
        }
        return bits;
    }

    private boolean hasAnySkill(
        obj_id player,
        String[] skills)
        throws InterruptedException
    {
        for (int index = 0; index < skills.length; ++index)
        {
            if (hasSkill(player, skills[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnyCommand(obj_id player)
        throws InterruptedException
    {
        for (int index = 0; index < COMMANDS.length; ++index)
        {
            if (hasCommand(player, COMMANDS[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasAnySchematic(obj_id player)
        throws InterruptedException
    {
        for (int index = 0; index < SCHEMATICS.length; ++index)
        {
            if (hasSchematic(player, SCHEMATICS[index]))
            {
                return true;
            }
        }
        return false;
    }

    private boolean hasCompleteSnapshot(obj_id player)
        throws InterruptedException
    {
        return
            hasObjVar(player, ORIGINAL_MEDICAL_XP) &&
            hasObjVar(player, ORIGINAL_CRAFTING_XP) &&
            hasObjVar(player, ORIGINAL_POINTS) &&
            hasObjVar(player, BASE_MODS);
    }

    private String validateOwnership(
        obj_id player,
        String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT) ||
            !hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(
            getStringObjVar(player, LIFECYCLE))
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
