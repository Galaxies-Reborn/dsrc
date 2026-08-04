package script.test;

import script.library.skill;
import script.obj_id;

/**
 * Identity-bound reversible fixture for exact Publish 14.1 Entertainer
 * Hairstyle I-IV purchase and production surrender.
 */
public class precu_entertainer_hairstyle_fixture
    extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String NOVICE = "social_entertainer_novice";
    private static final String HAIRSTYLE_ONE =
        "social_entertainer_hairstyle_01";
    private static final String HAIRSTYLE_TWO =
        "social_entertainer_hairstyle_02";
    private static final String HAIRSTYLE_THREE =
        "social_entertainer_hairstyle_03";
    private static final String HAIRSTYLE_FOUR =
        "social_entertainer_hairstyle_04";
    private static final String HAIR_ONE_COMMAND =
        "private_entertainer_hair_1";
    private static final String HAIR_TWO_COMMAND =
        "private_entertainer_hair_2";
    private static final String HAIR_THREE_COMMAND =
        "private_entertainer_hair_3";
    private static final String HAIR_FOUR_COMMAND =
        "private_entertainer_hair_4";
    private static final String IMAGE_DESIGNER_XP = "imagedesigner";
    private static final String HAIR_MOD = "hair";
    private static final String FACE_MOD = "face";
    private static final String MARKINGS_MOD = "markings";
    private static final int XP_COST = 1000;
    private static final int POINT_COST = 2;
    private static final int TWO_XP_COST = 5000;
    private static final int TWO_POINT_COST = 3;
    private static final int THREE_XP_COST = 10000;
    private static final int THREE_POINT_COST = 4;
    private static final int FOUR_XP_COST = 15000;
    private static final int FOUR_POINT_COST = 5;
    private static final String ROOT =
        "precu.entertainerHairstyleFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String ORIGINAL_XP = ROOT + ".originalXp";
    private static final String ORIGINAL_POINTS =
        ROOT + ".originalPoints";
    private static final String ORIGINAL_HAIR =
        ROOT + ".originalHair";
    private static final String ORIGINAL_FACE =
        ROOT + ".originalFace";
    private static final String ORIGINAL_MARKINGS =
        ROOT + ".originalMarkings";
    private static final String BASE_POINTS = ROOT + ".basePoints";
    private static final String BASE_HAIR = ROOT + ".baseHair";
    private static final String BASE_FACE = ROOT + ".baseFace";
    private static final String BASE_MARKINGS =
        ROOT + ".baseMarkings";
    private static final String PURCHASED = ROOT + ".purchased";
    private static final String SURRENDERED = ROOT + ".surrendered";
    private static final String USAGE =
        "usage: prepare|purchase|observeSurrender|prepareTwo|" +
        "purchaseTwo|observeSurrenderTwo|prepareThree|" +
        "purchaseThree|observeSurrenderThree|prepareFour|" +
        "purchaseFour|observeSurrenderFour|status|cleanup " +
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
            !args[2].matches("[a-f0-9]{32}"))
        {
            return "error=identityNotAllowed";
        }
        obj_id player = obj_id.getObjId(playerValue);
        if (player == null || player == obj_id.NULL_ID ||
            !player.isLoaded() || !player.isAuthoritative() ||
            !isPlayer(player) ||
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
        if (action.equalsIgnoreCase("cleanup") &&
            !hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true restored=true";
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !args[2].equals(getStringObjVar(player, LIFECYCLE)))
        {
            return "error=fixtureAbsentOrLifecycleMismatch";
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
        if (action.equalsIgnoreCase("observeSurrender"))
        {
            return observeSurrender(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderTwo"))
        {
            return observeSurrenderTwo(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderThree"))
        {
            return observeSurrenderThree(player);
        }
        if (action.equalsIgnoreCase("observeSurrenderFour"))
        {
            return observeSurrenderFour(player);
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
            return lifecycle.equals(
                getStringObjVar(player, LIFECYCLE))
                ? "action=prepare resumed=true " + buildStatus(player)
                : "error=lifecycleMismatch";
        }
        if (hasSkill(player, NOVICE) ||
            hasSkill(player, HAIRSTYLE_ONE) ||
            hasSkill(player, HAIRSTYLE_TWO) ||
            hasSkill(player, HAIRSTYLE_THREE) ||
            hasSkill(player, HAIRSTYLE_FOUR) ||
            hasCommand(player, HAIR_ONE_COMMAND) ||
            hasCommand(player, HAIR_TWO_COMMAND) ||
            hasCommand(player, HAIR_THREE_COMMAND) ||
            hasCommand(player, HAIR_FOUR_COMMAND))
        {
            return "error=fixtureRequiresUntrainedEntertainer";
        }

        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(
            player,
            ORIGINAL_XP,
            getExperiencePoints(player, IMAGE_DESIGNER_XP));
        setObjVar(
            player,
            ORIGINAL_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            ORIGINAL_HAIR,
            getSkillStatMod(player, HAIR_MOD));
        setObjVar(
            player,
            ORIGINAL_FACE,
            getSkillStatMod(player, FACE_MOD));
        setObjVar(
            player,
            ORIGINAL_MARKINGS,
            getSkillStatMod(player, MARKINGS_MOD));
        boolean noviceGranted =
            skill.grantSkillToPlayer(player, NOVICE) &&
            hasSkill(player, NOVICE);
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_HAIR,
            getSkillStatMod(player, HAIR_MOD));
        setObjVar(
            player,
            BASE_FACE,
            getSkillStatMod(player, FACE_MOD));
        setObjVar(
            player,
            BASE_MARKINGS,
            getSkillStatMod(player, MARKINGS_MOD));
        boolean xpReady = setXpExact(player, XP_COST);
        setObjVar(player, PURCHASED, 0);
        setObjVar(player, SURRENDERED, 0);

        boolean prepared =
            noviceGranted &&
            xpReady &&
            !hasSkill(player, HAIRSTYLE_ONE) &&
            !hasCommand(player, HAIR_ONE_COMMAND) &&
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
        if (hasSkill(player, HAIRSTYLE_TWO))
        {
            return "action=prepareTwo resumed=true " +
                buildStatus(player);
        }
        boolean prerequisiteGranted =
            hasSkill(player, HAIRSTYLE_ONE) ||
            (skill.grantSkillToPlayer(player, HAIRSTYLE_ONE) &&
                hasSkill(player, HAIRSTYLE_ONE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_HAIR,
            getSkillStatMod(player, HAIR_MOD));
        setObjVar(
            player,
            BASE_FACE,
            getSkillStatMod(player, FACE_MOD));
        setObjVar(
            player,
            BASE_MARKINGS,
            getSkillStatMod(player, MARKINGS_MOD));
        boolean xpReady = setXpExact(player, TWO_XP_COST);
        setObjVar(player, PURCHASED, 0);
        setObjVar(player, SURRENDERED, 0);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            !hasSkill(player, HAIRSTYLE_TWO) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            !hasCommand(player, HAIR_TWO_COMMAND) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - POINT_COST;
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
        if (hasSkill(player, HAIRSTYLE_THREE))
        {
            return "action=prepareThree resumed=true " +
                buildStatus(player);
        }
        boolean prerequisiteGranted =
            hasSkill(player, HAIRSTYLE_TWO) ||
            (skill.grantSkillToPlayer(player, HAIRSTYLE_TWO) &&
                hasSkill(player, HAIRSTYLE_TWO));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_HAIR,
            getSkillStatMod(player, HAIR_MOD));
        setObjVar(
            player,
            BASE_FACE,
            getSkillStatMod(player, FACE_MOD));
        setObjVar(
            player,
            BASE_MARKINGS,
            getSkillStatMod(player, MARKINGS_MOD));
        boolean xpReady = setXpExact(player, THREE_XP_COST);
        setObjVar(player, PURCHASED, 0);
        setObjVar(player, SURRENDERED, 0);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            hasSkill(player, HAIRSTYLE_TWO) &&
            !hasSkill(player, HAIRSTYLE_THREE) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            hasCommand(player, HAIR_TWO_COMMAND) &&
            !hasCommand(player, HAIR_THREE_COMMAND) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - POINT_COST - TWO_POINT_COST;
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
        if (hasSkill(player, HAIRSTYLE_FOUR))
        {
            return "action=prepareFour resumed=true " +
                buildStatus(player);
        }
        boolean prerequisiteGranted =
            hasSkill(player, HAIRSTYLE_THREE) ||
            (skill.grantSkillToPlayer(player, HAIRSTYLE_THREE) &&
                hasSkill(player, HAIRSTYLE_THREE));
        setObjVar(
            player,
            BASE_POINTS,
            skill.getAvailableSkillPoints(player));
        setObjVar(
            player,
            BASE_HAIR,
            getSkillStatMod(player, HAIR_MOD));
        setObjVar(
            player,
            BASE_FACE,
            getSkillStatMod(player, FACE_MOD));
        setObjVar(
            player,
            BASE_MARKINGS,
            getSkillStatMod(player, MARKINGS_MOD));
        boolean xpReady = setXpExact(player, FOUR_XP_COST);
        setObjVar(player, PURCHASED, 0);
        setObjVar(player, SURRENDERED, 0);

        boolean prepared =
            prerequisiteGranted &&
            xpReady &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            hasSkill(player, HAIRSTYLE_TWO) &&
            hasSkill(player, HAIRSTYLE_THREE) &&
            !hasSkill(player, HAIRSTYLE_FOUR) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            hasCommand(player, HAIR_TWO_COMMAND) &&
            hasCommand(player, HAIR_THREE_COMMAND) &&
            !hasCommand(player, HAIR_FOUR_COMMAND) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS) -
                    15 - POINT_COST - TWO_POINT_COST -
                    THREE_POINT_COST;
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

    private String purchase(obj_id player)
        throws InterruptedException
    {
        int cost = skill.getSkillPointCost(HAIRSTYLE_ONE);
        boolean purchased =
            cost == POINT_COST &&
            skill.hasRequiredSkillsForSkillPurchase(
                player,
                HAIRSTYLE_ONE) &&
            skill.hasRequiredXpForSkillPurchase(
                player,
                HAIRSTYLE_ONE) &&
            skill.grantSkillToPlayer(player, HAIRSTYLE_ONE);
        if (purchased &&
            !skill.deductXpCostForSkillPurchase(
                player,
                HAIRSTYLE_ONE))
        {
            revokeSkill(player, HAIRSTYLE_ONE);
            purchased = false;
        }
        boolean passed =
            purchased &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) -
                getIntObjVar(player, BASE_HAIR) == 1 &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 10000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) - POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchase passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String observeSurrender(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            getIntObjVar(player, PURCHASED) == 1 &&
            !hasSkill(player, HAIRSTYLE_ONE) &&
            !hasCommand(player, HAIR_ONE_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, BASE_HAIR) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 2000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrender passed=" +
            passed + " " + buildStatus(player);
    }

    private String purchaseTwo(obj_id player)
        throws InterruptedException
    {
        int cost = skill.getSkillPointCost(HAIRSTYLE_TWO);
        boolean purchased =
            cost == TWO_POINT_COST &&
            skill.hasRequiredSkillsForSkillPurchase(
                player,
                HAIRSTYLE_TWO) &&
            skill.hasRequiredXpForSkillPurchase(
                player,
                HAIRSTYLE_TWO) &&
            skill.grantSkillToPlayer(player, HAIRSTYLE_TWO);
        if (purchased &&
            !skill.deductXpCostForSkillPurchase(
                player,
                HAIRSTYLE_TWO))
        {
            revokeSkill(player, HAIRSTYLE_TWO);
            purchased = false;
        }
        boolean passed =
            purchased &&
            hasSkill(player, HAIRSTYLE_TWO) &&
            hasCommand(player, HAIR_TWO_COMMAND) &&
            getSkillStatMod(player, FACE_MOD) -
                getIntObjVar(player, BASE_FACE) == 1 &&
            getSkillStatMod(player, MARKINGS_MOD) -
                getIntObjVar(player, BASE_MARKINGS) == 1 &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, BASE_HAIR) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 20000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    TWO_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseTwo passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String observeSurrenderTwo(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            getIntObjVar(player, PURCHASED) == 1 &&
            !hasSkill(player, HAIRSTYLE_TWO) &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            !hasCommand(player, HAIR_TWO_COMMAND) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            getSkillStatMod(player, FACE_MOD) ==
                getIntObjVar(player, BASE_FACE) &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, BASE_MARKINGS) &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, BASE_HAIR) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 10000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderTwo passed=" +
            passed + " " + buildStatus(player);
    }

    private String purchaseThree(obj_id player)
        throws InterruptedException
    {
        int cost = skill.getSkillPointCost(HAIRSTYLE_THREE);
        boolean purchased =
            cost == THREE_POINT_COST &&
            skill.hasRequiredSkillsForSkillPurchase(
                player,
                HAIRSTYLE_THREE) &&
            skill.hasRequiredXpForSkillPurchase(
                player,
                HAIRSTYLE_THREE) &&
            skill.grantSkillToPlayer(player, HAIRSTYLE_THREE);
        if (purchased &&
            !skill.deductXpCostForSkillPurchase(
                player,
                HAIRSTYLE_THREE))
        {
            revokeSkill(player, HAIRSTYLE_THREE);
            purchased = false;
        }
        boolean passed =
            purchased &&
            hasSkill(player, HAIRSTYLE_THREE) &&
            hasCommand(player, HAIR_THREE_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) -
                getIntObjVar(player, BASE_HAIR) == 1 &&
            getSkillStatMod(player, FACE_MOD) ==
                getIntObjVar(player, BASE_FACE) &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, BASE_MARKINGS) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    THREE_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseThree passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String observeSurrenderThree(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            getIntObjVar(player, PURCHASED) == 1 &&
            !hasSkill(player, HAIRSTYLE_THREE) &&
            hasSkill(player, HAIRSTYLE_TWO) &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            !hasCommand(player, HAIR_THREE_COMMAND) &&
            hasCommand(player, HAIR_TWO_COMMAND) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, BASE_HAIR) &&
            getSkillStatMod(player, FACE_MOD) ==
                getIntObjVar(player, BASE_FACE) &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, BASE_MARKINGS) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 20000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderThree passed=" +
            passed + " " + buildStatus(player);
    }

    private String purchaseFour(obj_id player)
        throws InterruptedException
    {
        int cost = skill.getSkillPointCost(HAIRSTYLE_FOUR);
        boolean purchased =
            cost == FOUR_POINT_COST &&
            skill.hasRequiredSkillsForSkillPurchase(
                player,
                HAIRSTYLE_FOUR) &&
            skill.hasRequiredXpForSkillPurchase(
                player,
                HAIRSTYLE_FOUR) &&
            skill.grantSkillToPlayer(player, HAIRSTYLE_FOUR);
        if (purchased &&
            !skill.deductXpCostForSkillPurchase(
                player,
                HAIRSTYLE_FOUR))
        {
            revokeSkill(player, HAIRSTYLE_FOUR);
            purchased = false;
        }
        boolean passed =
            purchased &&
            hasSkill(player, HAIRSTYLE_FOUR) &&
            hasCommand(player, HAIR_FOUR_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) -
                getIntObjVar(player, BASE_HAIR) == 1 &&
            getSkillStatMod(player, FACE_MOD) -
                getIntObjVar(player, BASE_FACE) == 1 &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, BASE_MARKINGS) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS) -
                    FOUR_POINT_COST;
        setObjVar(player, PURCHASED, passed ? 1 : 0);
        return "action=purchaseFour passed=" + passed +
            " purchased=" + purchased + " " + buildStatus(player);
    }

    private String observeSurrenderFour(obj_id player)
        throws InterruptedException
    {
        boolean passed =
            getIntObjVar(player, PURCHASED) == 1 &&
            !hasSkill(player, HAIRSTYLE_FOUR) &&
            hasSkill(player, HAIRSTYLE_THREE) &&
            hasSkill(player, HAIRSTYLE_TWO) &&
            hasSkill(player, HAIRSTYLE_ONE) &&
            !hasCommand(player, HAIR_FOUR_COMMAND) &&
            hasCommand(player, HAIR_THREE_COMMAND) &&
            hasCommand(player, HAIR_TWO_COMMAND) &&
            hasCommand(player, HAIR_ONE_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, BASE_HAIR) &&
            getSkillStatMod(player, FACE_MOD) ==
                getIntObjVar(player, BASE_FACE) &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, BASE_MARKINGS) &&
            getExperiencePoints(player, IMAGE_DESIGNER_XP) == 0 &&
            getExperienceCap(player, IMAGE_DESIGNER_XP) == 30000 &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, BASE_POINTS);
        setObjVar(player, SURRENDERED, passed ? 1 : 0);
        return "action=observeSurrenderFour passed=" +
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
            " xp=" +
                getExperiencePoints(player, IMAGE_DESIGNER_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player);
    }

    private boolean restoreSnapshot(obj_id player)
        throws InterruptedException
    {
        if (!hasObjVar(player, ORIGINAL_XP) ||
            !hasObjVar(player, ORIGINAL_POINTS) ||
            !hasObjVar(player, ORIGINAL_HAIR) ||
            !hasObjVar(player, ORIGINAL_FACE) ||
            !hasObjVar(player, ORIGINAL_MARKINGS))
        {
            return false;
        }
        if (hasSkill(player, HAIRSTYLE_FOUR))
        {
            revokeSkill(player, HAIRSTYLE_FOUR);
        }
        if (hasSkill(player, HAIRSTYLE_THREE))
        {
            revokeSkill(player, HAIRSTYLE_THREE);
        }
        if (hasSkill(player, HAIRSTYLE_TWO))
        {
            revokeSkill(player, HAIRSTYLE_TWO);
        }
        if (hasSkill(player, HAIRSTYLE_ONE))
        {
            revokeSkill(player, HAIRSTYLE_ONE);
        }
        if (hasSkill(player, NOVICE))
        {
            revokeSkill(player, NOVICE);
        }
        return
            setXpExact(player, getIntObjVar(player, ORIGINAL_XP)) &&
            !hasSkill(player, HAIRSTYLE_ONE) &&
            !hasSkill(player, HAIRSTYLE_TWO) &&
            !hasSkill(player, HAIRSTYLE_THREE) &&
            !hasSkill(player, HAIRSTYLE_FOUR) &&
            !hasSkill(player, NOVICE) &&
            !hasCommand(player, HAIR_ONE_COMMAND) &&
            !hasCommand(player, HAIR_TWO_COMMAND) &&
            !hasCommand(player, HAIR_THREE_COMMAND) &&
            !hasCommand(player, HAIR_FOUR_COMMAND) &&
            getSkillStatMod(player, HAIR_MOD) ==
                getIntObjVar(player, ORIGINAL_HAIR) &&
            getSkillStatMod(player, FACE_MOD) ==
                getIntObjVar(player, ORIGINAL_FACE) &&
            getSkillStatMod(player, MARKINGS_MOD) ==
                getIntObjVar(player, ORIGINAL_MARKINGS) &&
            skill.getAvailableSkillPoints(player) ==
                getIntObjVar(player, ORIGINAL_POINTS);
    }

    private boolean setXpExact(obj_id player, int target)
        throws InterruptedException
    {
        int current =
            getExperiencePoints(player, IMAGE_DESIGNER_XP);
        if (current != target)
        {
            grantExperiencePoints(
                player,
                IMAGE_DESIGNER_XP,
                target - current);
        }
        return getExperiencePoints(
            player,
            IMAGE_DESIGNER_XP) == target;
    }

    private String buildStatus(obj_id player)
        throws InterruptedException
    {
        int baseHair = hasObjVar(player, BASE_HAIR)
            ? getIntObjVar(player, BASE_HAIR)
            : getSkillStatMod(player, HAIR_MOD);
        int baseFace = hasObjVar(player, BASE_FACE)
            ? getIntObjVar(player, BASE_FACE)
            : getSkillStatMod(player, FACE_MOD);
        int baseMarkings = hasObjVar(player, BASE_MARKINGS)
            ? getIntObjVar(player, BASE_MARKINGS)
            : getSkillStatMod(player, MARKINGS_MOD);
        return "player=" + player +
            " novice=" + (hasSkill(player, NOVICE) ? "1" : "0") +
            " hairstyleOne=" +
                (hasSkill(player, HAIRSTYLE_ONE) ? "1" : "0") +
            " hairstyleTwo=" +
                (hasSkill(player, HAIRSTYLE_TWO) ? "1" : "0") +
            " hairstyleThree=" +
                (hasSkill(player, HAIRSTYLE_THREE) ? "1" : "0") +
            " hairstyleFour=" +
                (hasSkill(player, HAIRSTYLE_FOUR) ? "1" : "0") +
            " commands=" +
                (hasCommand(player, HAIR_ONE_COMMAND) ? "1" : "0") +
                (hasCommand(player, HAIR_TWO_COMMAND) ? "1" : "0") +
                (hasCommand(player, HAIR_THREE_COMMAND) ? "1" : "0") +
                (hasCommand(player, HAIR_FOUR_COMMAND) ? "1" : "0") +
            " hairDelta=" +
                (getSkillStatMod(player, HAIR_MOD) - baseHair) +
            " faceDelta=" +
                (getSkillStatMod(player, FACE_MOD) - baseFace) +
            " markingsDelta=" +
                (getSkillStatMod(player, MARKINGS_MOD) -
                    baseMarkings) +
            " xp=" +
                getExperiencePoints(player, IMAGE_DESIGNER_XP) +
            " cap=" +
                getExperienceCap(player, IMAGE_DESIGNER_XP) +
            " availablePoints=" +
                skill.getAvailableSkillPoints(player) +
            " purchased=" +
                (hasObjVar(player, PURCHASED) &&
                    getIntObjVar(player, PURCHASED) == 1) +
            " surrendered=" +
                (hasObjVar(player, SURRENDERED) &&
                    getIntObjVar(player, SURRENDERED) == 1);
    }
}
