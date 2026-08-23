package script.test;

import script.*;
import script.library.account_containers;
import script.library.player_structure;
import script.library.sui;
import script.library.utils;

import java.util.Vector;

/**
 * Identity-bound live fixture for the account containers, Worker Droid, and
 * Survey Droid features. All persistent test objects are lifecycle-tagged.
 */
public class precu_container_droid_feature_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String ROOT = "precu.containerDroidFeatureFixture";
    private static final String LIFECYCLE = ROOT + ".lifecycle";
    private static final String WORKER = ROOT + ".worker";
    private static final String WORKER_TOKEN = ROOT + ".workerToken";
    private static final String WORKER_LAUNCH_STATE =
        ROOT + ".workerLaunchState";
    private static final String WORKER_LAUNCH_RESULT =
        ROOT + ".workerLaunchResult";
    private static final String WORKER_LAUNCH_QUEUED_AT =
        ROOT + ".workerLaunchQueuedAt";
    private static final String WORKER_COOLDOWN_VALUE =
        ROOT + ".workerCooldownValue";
    private static final String WORKER_SELECTION_STATE =
        ROOT + ".workerSelectionState";
    private static final String WORKER_SELECTION_ERROR =
        ROOT + ".workerSelectionError";
    private static final String WORKER_SELECTION_QUEUED_AT =
        ROOT + ".workerSelectionQueuedAt";
    private static final String WORKER_SELECTED_TARGET =
        ROOT + ".workerSelectedTarget";
    private static final String WORKER_COUNT_BEFORE =
        ROOT + ".workerCountBefore";
    private static final String WORKER_COUNT_AFTER =
        ROOT + ".workerCountAfter";
    private static final String WORKER_CONSUMED =
        ROOT + ".workerConsumed";
    private static final String WORKER_TIMEOUT_EXPECTED =
        ROOT + ".workerTimeoutExpected";
    private static final String WORKER_TIMEOUT_COMPLETE =
        ROOT + ".workerTimeoutComplete";
    private static final String STRUCTURE = ROOT + ".structure";
    private static final String STRUCTURE_INITIALIZED =
        ROOT + ".structureInitialized";
    private static final String STRUCTURE_REMOVED = ROOT + ".structureRemoved";
    private static final String OBJECT_LIFECYCLE = ROOT + ".objectLifecycle";
    private static final int FIXTURE_HANDLER_TIMEOUT_SECONDS = 75;
    private static final String FIXTURE_SCRIPT =
        "test.precu_container_droid_feature_fixture";

    private static final String SEEKER_SCHEMATIC =
        "object/draft_schematic/droid/bounty_seeker_transmitter.iff";
    private static final String WORKER_SCHEMATIC =
        "object/draft_schematic/droid/worker_droid_transmitter.iff";
    private static final String SURVEY_SCHEMATIC =
        "object/draft_schematic/droid/survey_droid_transmitter.iff";
    private static final String SEEKER_TEMPLATE =
        "object/tangible/mission/mission_bounty_droid_seeker.iff";
    private static final String WORKER_TEMPLATE =
        "object/tangible/mission/mission_worker_droid.iff";
    private static final String SURVEY_TEMPLATE =
        "object/tangible/mission/mission_survey_droid.iff";
    private static final String WORKER_SCRIPT = "item.droid.worker_droid";
    private static final String SURVEY_SCRIPT =
        "item.survey_droid.survey_droid_device";
    private static final String WORKER_TARGET_SCRIPT =
        "structure.worker_droid_target";
    private static final String HARVESTER_TEMPLATE =
        "object/installation/mining_ore/mining_ore_harvester_style_1.iff";
    private static final String SCHEMATIC_GROUP_TABLE =
        "datatables/crafting/schematic_group.iff";
    private static final String DROID_SCHEMATIC_GROUP = "craftdroidGroupB";

    private static final String USAGE =
        "usage: status|craft|survey <playerOid>; " +
        "workerPrepare|workerBegin|workerStatus|workerTimeout " +
        "<playerOid> <32-hex-lifecycle>; " +
        "workerSelect <playerOid> <32-hex-lifecycle> <targetIndex> <actionIndex>; " +
        "cleanup <playerOid> <32-hex-lifecycle>";

    public String executeFixture(String params) throws InterruptedException
    {
        String[] args = params == null || params.trim().length() == 0
            ? new String[0] : params.trim().split("[ ]+");
        if (args.length < 2)
        {
            return USAGE;
        }

        long oid;
        try
        {
            oid = Long.parseLong(args[1]);
        }
        catch (NumberFormatException exception)
        {
            return "error=invalidPlayerOid";
        }
        if (oid != PLAYER_OID)
        {
            return "error=playerIdentityRejected";
        }
        obj_id player = obj_id.getObjId(oid);
        if (!isAuthoritativeFixturePlayer(player))
        {
            return "error=playerUnavailableOrStationMismatch";
        }

        String action = args[0];
        if ("status".equalsIgnoreCase(action) && args.length == 2)
        {
            return containerStatus(player);
        }
        if ("craft".equalsIgnoreCase(action) && args.length == 2)
        {
            return craftStatus(player);
        }
        if ("survey".equalsIgnoreCase(action) && args.length == 2)
        {
            return surveyStatus(player);
        }
        if (("workerPrepare".equalsIgnoreCase(action) ||
            "workerBegin".equalsIgnoreCase(action) ||
            "workerStatus".equalsIgnoreCase(action) ||
            "workerTimeout".equalsIgnoreCase(action) ||
            "cleanup".equalsIgnoreCase(action)) && args.length == 3 &&
            isValidLifecycle(args[2]))
        {
            if ("workerPrepare".equalsIgnoreCase(action))
            {
                return workerPrepare(player, args[2]);
            }
            if ("workerBegin".equalsIgnoreCase(action))
            {
                return workerBegin(player, args[2]);
            }
            if ("workerStatus".equalsIgnoreCase(action))
            {
                return workerStatus(player, args[2]);
            }
            if ("workerTimeout".equalsIgnoreCase(action))
            {
                return workerTimeout(player, args[2]);
            }
            return cleanup(player, args[2]);
        }
        if ("workerSelect".equalsIgnoreCase(action) && args.length == 5 &&
            isValidLifecycle(args[2]))
        {
            try
            {
                return workerSelect(
                    player,
                    args[2],
                    Integer.parseInt(args[3]),
                    Integer.parseInt(args[4]));
            }
            catch (NumberFormatException exception)
            {
                return "error=invalidSelectionIndex";
            }
        }
        return USAGE;
    }

    private String containerStatus(obj_id player) throws InterruptedException
    {
        account_containers.ensureContainers(player);
        obj_id[] first = new obj_id[account_containers.KINDS.length];
        for (int i = 0; i < first.length; ++i)
        {
            first[i] = account_containers.getContainer(
                player, account_containers.KINDS[i]);
        }
        account_containers.ensureContainers(player);

        obj_id datapad = utils.getPlayerDatapad(player);
        int managedCount = 0;
        obj_id[] contents = isLive(datapad) ? getContents(datapad) : null;
        if (contents != null)
        {
            for (obj_id content : contents)
            {
                if (isLive(content) &&
                    account_containers.isManagedContainer(content))
                {
                    ++managedCount;
                }
            }
        }

        boolean stable = true;
        boolean valid = managedCount == account_containers.KINDS.length;
        StringBuilder details = new StringBuilder();
        for (int i = 0; i < account_containers.KINDS.length; ++i)
        {
            obj_id container = account_containers.getContainer(
                player, account_containers.KINDS[i]);
            boolean sameOid = isLive(first[i]) && first[i] == container;
            boolean direct = isLive(container) && getContainedBy(container) == datapad;
            boolean bound = isLive(container) &&
                hasObjVar(container, account_containers.VAR_STATION_ID) &&
                getIntObjVar(container, account_containers.VAR_STATION_ID) ==
                    PLAYER_STATION_ID;
            boolean scripts = isLive(container) &&
                hasScript(container, account_containers.SCRIPT_ACCOUNT_BOUND) &&
                hasScript(container, account_containers.SCRIPT_NO_DESTROY);
            boolean tradeVars = isLive(container) &&
                hasObjVar(container, "noTrade") &&
                getIntObjVar(container, "noTrade") == 1 &&
                hasObjVar(container, "noTradeShared") &&
                getIntObjVar(container, "noTradeShared") == 1;
            boolean name = isLive(container) &&
                account_containers.DEFAULT_NAMES[i].equals(getName(container));
            boolean kind = isLive(container) &&
                account_containers.KINDS[i].equals(
                    account_containers.getContainerKind(container));
            boolean itemValid = sameOid && direct && bound && scripts &&
                tradeVars && name && kind;
            stable &= sameOid;
            valid &= itemValid;
            details.append(" ").append(account_containers.KINDS[i])
                .append("Oid=").append(container)
                .append(" ").append(account_containers.KINDS[i])
                .append("Valid=").append(itemValid)
                .append(" ").append(account_containers.KINDS[i])
                .append("Name=").append(token(isLive(container)
                    ? getName(container) : "missing"));
        }
        return "action=status player=" + player + " station=" +
            getPlayerStationId(player) + " datapad=" + datapad +
            " managedCount=" + managedCount + " exactCount=" +
            (managedCount == account_containers.KINDS.length) +
            " stableAfterSecondEnsure=" + stable + " valid=" + valid +
            details;
    }

    private String craftStatus(obj_id player) throws InterruptedException
    {
        draft_schematic seekerData = getSchematicData(SEEKER_SCHEMATIC);
        draft_schematic workerData = getSchematicData(WORKER_SCHEMATIC);
        draft_schematic surveyData = getSchematicData(SURVEY_SCHEMATIC);
        if (seekerData == null || workerData == null || surveyData == null)
        {
            return "action=craft dataLoaded=false seekerData=" +
                (seekerData != null) + " workerData=" + (workerData != null) +
                " surveyData=" + (surveyData != null);
        }

        boolean workerSlotParity = sameSlots(seekerData, workerData);
        boolean surveySlotParity = sameSlots(seekerData, surveyData);
        boolean workerComplexityParity =
            seekerData.getBaseComplexity() == workerData.getBaseComplexity();
        boolean surveyComplexityParity =
            seekerData.getBaseComplexity() == surveyData.getBaseComplexity();
        boolean workerCategoryParity =
            seekerData.getCategory() == workerData.getCategory();
        boolean surveyCategoryParity =
            seekerData.getCategory() == surveyData.getCategory();
        boolean workerManufactureParity = sameStrings(
            seekerData.getScripts(), workerData.getScripts());
        boolean surveyManufactureParity = sameStrings(
            seekerData.getScripts(), surveyData.getScripts());
        boolean workerGroupRegistered = isSchematicInGroup(
            DROID_SCHEMATIC_GROUP, WORKER_SCHEMATIC);
        boolean surveyGroupRegistered = isSchematicInGroup(
            DROID_SCHEMATIC_GROUP, SURVEY_SCHEMATIC);

        obj_id inventory = utils.getInventoryContainer(player);
        obj_id seeker = obj_id.NULL_ID;
        obj_id worker = obj_id.NULL_ID;
        obj_id survey = obj_id.NULL_ID;
        String result;
        try
        {
            seeker = makeCraftedItem(SEEKER_SCHEMATIC, 100.0f, inventory);
            worker = makeCraftedItem(WORKER_SCHEMATIC, 100.0f, inventory);
            survey = makeCraftedItem(SURVEY_SCHEMATIC, 100.0f, inventory);
            tagOneShotCraftObject(seeker);
            tagOneShotCraftObject(worker);
            tagOneShotCraftObject(survey);

            boolean outputsCreated = isLive(seeker) && isLive(worker) &&
                isLive(survey);
            boolean outputTemplates = outputsCreated &&
                SEEKER_TEMPLATE.equals(getTemplateName(seeker)) &&
                WORKER_TEMPLATE.equals(getTemplateName(worker)) &&
                SURVEY_TEMPLATE.equals(getTemplateName(survey));
            boolean outputCounts = outputsCreated && getCount(seeker) == 20 &&
                getCount(worker) == 0 && getCount(survey) == 0;
            boolean outputScripts = outputsCreated &&
                hasScript(worker, WORKER_SCRIPT) &&
                hasScript(survey, SURVEY_SCRIPT);
            string_id workerNameId =
                script.item.droid.worker_droid.DISPLAY_NAME;
            string_id surveyNameId =
                script.item.survey_droid.survey_droid_device.DISPLAY_NAME;
            boolean workerNameIdMatch = outputsCreated &&
                sameStringId(workerNameId, getNameStringId(worker));
            boolean surveyNameIdMatch = outputsCreated &&
                sameStringId(surveyNameId, getNameStringId(survey));
            String workerLocalizedName = getString(workerNameId);
            String surveyLocalizedName = getString(surveyNameId);
            boolean workerNameLocalized =
                "Worker Droid".equals(workerLocalizedName);
            boolean surveyNameLocalized =
                "Survey Droid".equals(surveyLocalizedName);
            boolean outputNames = workerNameIdMatch && surveyNameIdMatch &&
                workerNameLocalized && surveyNameLocalized;
            boolean valid = workerSlotParity && surveySlotParity &&
                workerComplexityParity && surveyComplexityParity &&
                workerCategoryParity && surveyCategoryParity &&
                workerManufactureParity && surveyManufactureParity &&
                workerGroupRegistered && surveyGroupRegistered &&
                outputTemplates && outputCounts && outputScripts && outputNames;

            result = "action=craft dataLoaded=true seekerSlots=" +
                slotCount(seekerData) + " workerSlots=" + slotCount(workerData) +
                " surveySlots=" + slotCount(surveyData) +
                " workerSlotParity=" + workerSlotParity +
                " surveySlotParity=" + surveySlotParity +
                " workerComplexityParity=" + workerComplexityParity +
                " surveyComplexityParity=" + surveyComplexityParity +
                " workerCategoryParity=" + workerCategoryParity +
                " surveyCategoryParity=" + surveyCategoryParity +
                " workerManufactureScriptParity=" + workerManufactureParity +
                " surveyManufactureScriptParity=" + surveyManufactureParity +
                " workerGroupRegistered=" + workerGroupRegistered +
                " surveyGroupRegistered=" + surveyGroupRegistered +
                " seeker=" + objectCraftSummary(seeker) +
                " worker=" + objectCraftSummary(worker) +
                " survey=" + objectCraftSummary(survey) +
                " outputTemplates=" + outputTemplates +
                " outputCounts=" + outputCounts +
                " outputScripts=" + outputScripts +
                " workerNameIdMatch=" + workerNameIdMatch +
                " surveyNameIdMatch=" + surveyNameIdMatch +
                " workerLocalizedName=" + token(workerLocalizedName) +
                " surveyLocalizedName=" + token(surveyLocalizedName) +
                " outputNames=" + outputNames + " valid=" + valid;
        }
        finally
        {
            destroyOneShotCraftObject(seeker);
            destroyOneShotCraftObject(worker);
            destroyOneShotCraftObject(survey);
        }
        return result + " createdObjectsDestroyed=true";
    }

    private String surveyStatus(obj_id player) throws InterruptedException
    {
        dictionary report = script.library.survey_droid.findHighDensityResource(
            "tatooine");
        boolean reportFound = report != null && !report.isEmpty();
        obj_id resourceType = reportFound
            ? report.getObjId("resourceType") : obj_id.NULL_ID;
        float density = reportFound ? report.getFloat("density") : 0.0f;
        location surveyLocation = reportFound
            ? report.getLocation("surveyLocation") : null;
        boolean surveyable = reportFound &&
            script.library.survey_droid.isSurveyableResource(resourceType);
        boolean densityValid = density >=
            script.library.survey_droid.MINIMUM_DENSITY;
        boolean inorganic = reportFound &&
            isResourceDerivedFrom(resourceType,
                script.library.survey_droid.RESOURCE_INORGANIC);
        boolean flora = reportFound &&
            isResourceDerivedFrom(resourceType,
                script.library.survey_droid.RESOURCE_FLORA);
        boolean energy = reportFound &&
            isResourceDerivedFrom(resourceType,
                script.library.survey_droid.RESOURCE_ENERGY);

        Vector before = new Vector();
        obj_id[] beforeWaypoints = getWaypointsInDatapad(player);
        if (beforeWaypoints != null)
        {
            for (obj_id waypoint : beforeWaypoints)
            {
                before.add(waypoint);
            }
        }

        obj_id deliveredWaypoint = reportFound
            ? script.library.survey_droid.createSurveyWaypoint(player, report)
            : obj_id.NULL_ID;
        int newWaypointCount = 0;
        obj_id[] afterWaypoints = getWaypointsInDatapad(player);
        if (afterWaypoints != null)
        {
            for (obj_id waypoint : afterWaypoints)
            {
                if (!before.contains(waypoint) && isValidId(waypoint))
                {
                    ++newWaypointCount;
                }
            }
        }

        String waypointName = isValidId(deliveredWaypoint)
            ? getWaypointName(deliveredWaypoint) : "none";
        location waypointLocation = isValidId(deliveredWaypoint)
            ? getWaypointLocation(deliveredWaypoint) : null;
        boolean waypointDestroyed = false;
        if (isValidId(deliveredWaypoint))
        {
            destroyWaypointInDatapad(deliveredWaypoint, player);
            waypointDestroyed = true;
        }

        String reportResourceName = reportFound
            ? getResourceName(resourceType) : "";
        boolean waypointMatchesReport = sameWorldLocation(
            waypointLocation, surveyLocation) &&
            reportResourceName != null && reportResourceName.length() > 0 &&
            waypointName.contains(reportResourceName);
        boolean valid = reportFound && densityValid && surveyable &&
            (inorganic || flora || energy) && newWaypointCount == 1 &&
            waypointDestroyed && waypointMatchesReport;
        return "action=survey reportFound=" + reportFound +
            " planet=" + (reportFound ? token(report.getString("planetName"))
                : "none") +
            " resourceType=" + resourceType +
            " resourceName=" + token(reportFound
                ? getResourceName(resourceType) : "none") +
            " density=" + density + " densityValid=" + densityValid +
            " surveyable=" + surveyable + " inorganic=" + inorganic +
            " flora=" + flora + " energy=" + energy +
            " surveyLocation=" + locationToken(surveyLocation) +
            " deliveryNewWaypointCount=" + newWaypointCount +
            " deliveryWaypoint=" + deliveredWaypoint +
            " deliveryWaypointName=" + token(waypointName) +
            " deliveryWaypointLocation=" + locationToken(waypointLocation) +
            " deliveryWaypointDestroyed=" + waypointDestroyed +
            " deliveryWaypointMatchesReport=" + waypointMatchesReport +
            " persistentMailSent=false" +
            " valid=" + valid;
    }

    private String workerPrepare(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (hasObjVar(player, ROOT))
        {
            String ownership = validateLifecycle(player, lifecycle);
            if (ownership != null)
            {
                return ownership;
            }
            obj_id trackedStructure = readObjId(player, STRUCTURE);
            if (isValidId(trackedStructure))
            {
                return "action=workerPrepare resumed=true " +
                    buildWorkerStatus(player, lifecycle);
            }
            // A crash before the newly created structure OID was recorded can
            // leave only the lifecycle root. No persistent structure exists at
            // that stage; remove any separately tracked worker before retrying.
            obj_id trackedWorker = readObjId(player, WORKER);
            boolean partialWorkerDestroyed = !isValidId(trackedWorker) ||
                destroyObject(trackedWorker);
            if (!partialWorkerDestroyed)
            {
                return "error=partialPrepareCleanupRequired retry=true";
            }
            removeObjVar(player, ROOT);
        }
        if (hasUnownedWorkerFlow(player))
        {
            return "error=preexistingWorkerFlow";
        }
        if (!isPlayerConnected(player))
        {
            return "error=playerNotConnected";
        }
        if (hasObjVar(player,
            script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN))
        {
            int cooldownUntil = getIntObjVar(
                player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN);
            if (cooldownUntil > getCalendarTime())
            {
                return "error=preexistingWorkerCooldown remaining=" +
                    (cooldownUntil - getCalendarTime());
            }
            removeObjVar(
                player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN);
        }
        location playerLocation = getLocation(player);
        if (playerLocation == null || isValidId(playerLocation.cell) ||
            isSpaceScene())
        {
            return "error=playerMustBeOutdoors";
        }
        obj_id inventory = utils.getInventoryContainer(player);
        if (!isLive(inventory))
        {
            return "error=inventoryUnavailable";
        }
        location structureLocation = findSafeStructureLocation(playerLocation);
        if (structureLocation == null)
        {
            return "error=safeHarvesterPlacementUnavailable";
        }

        obj_id structure = createObject(HARVESTER_TEMPLATE, structureLocation);
        if (!isLive(structure))
        {
            return "error=harvesterCreateFailed";
        }
        setObjVar(structure, OBJECT_LIFECYCLE, lifecycle);
        setObjVar(player, LIFECYCLE, lifecycle);
        setObjVar(player, STRUCTURE, structure);
        setName(structure, "Fixture Worker Harvester " +
            lifecycle.substring(0, 8));
        // These APIs operate on their explicit target. Configure the raw
        // authoritative installation synchronously so the fixture has no
        // dependency on a temporary structure-side message handler.
        boolean ownerAssigned = setOwner(structure, player);
        if (!ownerAssigned || !structure.isAuthoritative() ||
            !isInWorldCell(structure) ||
            player_structure.getStructureOwnerObjId(structure) != player)
        {
            return failWorkerPrepare(player, lifecycle,
                "harvesterOwnerSetupFailed", obj_id.NULL_ID, structure);
        }
        if (!hasScript(structure, WORKER_TARGET_SCRIPT))
        {
            attachScript(structure, WORKER_TARGET_SCRIPT);
        }
        if (!hasScript(structure, WORKER_TARGET_SCRIPT))
        {
            return failWorkerPrepare(player, lifecycle,
                "harvesterTargetScriptAttachFailed", obj_id.NULL_ID,
                structure);
        }
        if (!persistObject(structure))
        {
            return failWorkerPrepare(player, lifecycle,
                "harvesterPersistFailed", obj_id.NULL_ID, structure);
        }
        setObjVar(player, STRUCTURE_INITIALIZED, 1);

        obj_id worker = makeCraftedItem(WORKER_SCHEMATIC, 100.0f, inventory);
        if (!isLive(worker))
        {
            return failWorkerPrepare(player, lifecycle,
                "workerCreateFailed", worker, structure);
        }
        setObjVar(worker, OBJECT_LIFECYCLE, lifecycle);
        setObjVar(player, WORKER, worker);
        if (!hasScript(worker, WORKER_SCRIPT))
        {
            return failWorkerPrepare(player, lifecycle,
                "workerCreateFailed", worker, structure);
        }
        // Exercise the explicit final-charge representation as well as the
        // ordinary unstacked count-0 representation checked by craftStatus.
        // The durable Worker protocol destroys this final charge only after a
        // nonce-bound target acknowledgement.
        setCount(worker, 1);
        if (getCount(worker) != 1)
        {
            return failWorkerPrepare(player, lifecycle,
                "workerSingleChargeSetupFailed", worker, structure);
        }
        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            attachScript(worker, FIXTURE_SCRIPT);
        }
        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            return failWorkerPrepare(player, lifecycle,
                "workerFixtureScriptAttachFailed", worker, structure);
        }

        return "action=workerPrepare resumed=false initializationComplete=true " +
            buildWorkerStatus(player, lifecycle);
    }

    private String failWorkerPrepare(obj_id player, String lifecycle,
        String reason, obj_id worker, obj_id structure)
        throws InterruptedException
    {
        boolean workerDestroyed = true;
        if (isValidId(worker))
        {
            if (isLive(worker))
            {
                setObjVar(worker, OBJECT_LIFECYCLE, lifecycle);
            }
            setObjVar(player, WORKER, worker);
            workerDestroyed = destroyObject(worker);
        }
        boolean structureDestroyed = true;
        if (isValidId(structure))
        {
            structureDestroyed = destroyObject(structure);
            if (structureDestroyed)
            {
                setObjVar(player, STRUCTURE_REMOVED, 1);
            }
        }
        boolean clean = workerDestroyed && structureDestroyed;
        if (clean)
        {
            removeObjVar(player, ROOT);
        }
        return "error=" + reason + " cleanupComplete=" + clean +
            " cleanupRequired=" + !clean + " workerDestroyed=" +
            workerDestroyed + " structureDestroyed=" + structureDestroyed;
    }

    private location findSafeStructureLocation(location playerLocation)
        throws InterruptedException
    {
        // The player-structure row has no footprint override, so the library
        // falls back to this template. Avoid its LOG() call here because a
        // ServerConsole handler has no object-script owner context.
        String footprint = HARVESTER_TEMPLATE;
        float[] radii = { 48.0f, 72.0f, 96.0f, 128.0f, 160.0f,
            192.0f, 256.0f };
        // Prefer nearby placement, then fall back to remote wilderness anchors.
        // The latter also makes this a stronger remote-management fixture when
        // the acceptance avatar happens to be standing inside a no-build city.
        float[][] anchors =
        {
            { playerLocation.x, playerLocation.z },
            { 5000.0f, 5000.0f }, { -5000.0f, -5000.0f },
            { 5000.0f, -5000.0f }, { -5000.0f, 5000.0f },
            { 0.0f, 6000.0f }, { 0.0f, -6000.0f },
            { 6000.0f, 0.0f }, { -6000.0f, 0.0f }
        };
        float[][] directions =
        {
            { 1.0f, 0.0f }, { -1.0f, 0.0f },
            { 0.0f, 1.0f }, { 0.0f, -1.0f },
            { 0.7071f, 0.7071f }, { -0.7071f, 0.7071f },
            { 0.7071f, -0.7071f }, { -0.7071f, -0.7071f }
        };
        for (float[] anchor : anchors)
        {
            for (float radius : radii)
            {
                for (float[] direction : directions)
                {
                    location candidate = new location(playerLocation);
                    candidate.x = anchor[0] + direction[0] * radius;
                    candidate.z = anchor[1] + direction[1] * radius;
                    candidate.y = getHeightAtLocation(
                        candidate.x, candidate.z);
                    if (!isValidLocation(candidate, 8.0f) ||
                        getRegionsWithBuildableAtPoint(candidate,
                            script.library.regions.BUILD_FALSE) != null)
                    {
                        continue;
                    }
                    float placementHeight = canPlaceStructure(
                        footprint, candidate, 0);
                    if (placementHeight == -9997.0f ||
                        placementHeight == -9998.0f ||
                        placementHeight == -9999.0f)
                    {
                        continue;
                    }
                    return candidate;
                }
            }
        }
        return null;
    }

    private String workerBegin(obj_id player, String lifecycle)
        throws InterruptedException
    {
        refreshWorkerFixtureState(player);
        String ownership = validateWorkerOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        int initializationState = hasObjVar(player, STRUCTURE_INITIALIZED)
            ? getIntObjVar(player, STRUCTURE_INITIALIZED) : 0;
        if (initializationState != 1)
        {
            return "error=structureInitialization" +
                (initializationState < 0 ? "Failed " : "Pending ") +
                buildWorkerStatus(player, lifecycle);
        }
        if (!isPlayerConnected(player))
        {
            return "error=playerNotConnected " +
                buildWorkerStatus(player, lifecycle);
        }
        obj_id worker = getObjIdObjVar(player, WORKER);
        if (utils.hasScriptVar(worker,
            script.item.droid.worker_droid.SCRIPT_VAR_PHASE))
        {
            return "action=workerBegin resumed=true " +
                buildWorkerStatus(player, lifecycle);
        }
        int launchState = hasObjVar(player, WORKER_LAUNCH_STATE)
            ? getIntObjVar(player, WORKER_LAUNCH_STATE) : -2;
        if (launchState == 0)
        {
            return "action=workerBegin resumed=true launchQueued=true " +
                buildWorkerStatus(player, lifecycle);
        }
        if (launchState < 0 && launchState != -2)
        {
            return "error=workerLaunchFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            attachScript(worker, FIXTURE_SCRIPT);
        }
        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            setObjVar(player, WORKER_LAUNCH_STATE, -1);
            return "error=workerFixtureScriptAttachFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        dictionary launchRequest = new dictionary();
        launchRequest.put("player", player);
        launchRequest.put("lifecycle", lifecycle);
        setObjVar(player, WORKER_LAUNCH_STATE, 0);
        setObjVar(player, WORKER_LAUNCH_QUEUED_AT, getCalendarTime());
        if (!messageTo(
            worker,
            "beginFixtureWorker",
            launchRequest,
            1.0f,
            true))
        {
            setObjVar(player, WORKER_LAUNCH_STATE, -1);
            removeObjVar(player, WORKER_LAUNCH_QUEUED_AT);
            return "error=workerLaunchQueueFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        return "action=workerBegin resumed=false launchQueued=true " +
            buildWorkerStatus(player, lifecycle);
    }

    public int beginFixtureWorker(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() || !isLive(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        String lifecycle = params.getString("lifecycle");
        if (!isValidLifecycle(lifecycle) ||
            !hasMatchingObjectLifecycle(self, lifecycle))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isAuthoritativeFixturePlayer(player) ||
            !isPlayerConnected(player))
        {
            if (!requeueFixtureHandler(
                    self, "beginFixtureWorker", params) &&
                isLive(player) && player.isAuthoritative())
            {
                setObjVar(player, WORKER_LAUNCH_RESULT,
                    script.item.droid.worker_droid.
                        LAUNCH_RESULT_INVALID_STATE);
                setObjVar(player, WORKER_LAUNCH_STATE, -1);
            }
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)) ||
            readObjId(player, WORKER) != self ||
            getIntObjVar(player, STRUCTURE_INITIALIZED) != 1 ||
            getIntObjVar(player, WORKER_LAUNCH_STATE) != 0)
        {
            setObjVar(player, WORKER_LAUNCH_STATE, -1);
            return SCRIPT_CONTINUE;
        }
        if (utils.hasScriptVar(self,
            script.item.droid.worker_droid.SCRIPT_VAR_PHASE))
        {
            setObjVar(player, WORKER_LAUNCH_STATE, 1);
            return SCRIPT_CONTINUE;
        }
        int launchResult =
            new script.item.droid.worker_droid().beginWorkerDroidSearch(
                self, player);
        setObjVar(player, WORKER_LAUNCH_RESULT, launchResult);
        if (launchResult ==
                script.item.droid.worker_droid.LAUNCH_RESULT_QUEUED &&
            hasObjVar(player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN))
        {
            setObjVar(
                player,
                WORKER_COOLDOWN_VALUE,
                getIntObjVar(
                    player,
                    script.item.droid.worker_droid.
                        PLAYER_LOCATE_COOLDOWN));
        }
        setObjVar(
            player,
            WORKER_LAUNCH_STATE,
            launchResult ==
                script.item.droid.worker_droid.LAUNCH_RESULT_QUEUED ? 1 : -1);
        removeObjVar(player, WORKER_LAUNCH_QUEUED_AT);
        return SCRIPT_CONTINUE;
    }

    private String workerStatus(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        refreshWorkerFixtureState(player);
        return "action=workerStatus " + buildWorkerStatus(player, lifecycle);
    }

    private String workerTimeout(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String ownership = validateWorkerOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        obj_id worker = readObjId(player, WORKER);
        obj_id structure = readObjId(player, STRUCTURE);
        if (!isLive(worker) || !isLive(structure) ||
            utils.hasScriptVar(worker,
                script.item.droid.worker_droid.SCRIPT_VAR_PHASE))
        {
            return "error=workerTimeoutStateInvalid " +
                buildWorkerStatus(player, lifecycle);
        }

        script.item.droid.worker_droid logic =
            new script.item.droid.worker_droid();
        int token = rand(1, 2000000000);
        int beforeCount = getCount(worker);
        if (!logic.storePendingAction(
                worker,
                player,
                structure,
                token,
                "deactivate"))
        {
            return "error=workerTimeoutStoreFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        if (!logic.holdPendingActionFlow(worker, player, token))
        {
            removeObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
            logic.clearPendingActionFlow(worker, player);
            return "error=workerTimeoutLockFailed " +
                buildWorkerStatus(player, lifecycle);
        }

        setObjVar(player, WORKER_TOKEN, token);
        setObjVar(player, WORKER_SELECTED_TARGET, structure);
        setObjVar(player, WORKER_COUNT_BEFORE, beforeCount);
        if (!hasMatchingObjectLifecycle(structure, lifecycle) ||
            !destroyObject(structure))
        {
            removeObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
            logic.clearPendingActionFlow(worker, player);
            return "error=workerTimeoutTargetDestroyFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        setObjVar(player, STRUCTURE_REMOVED, 1);
        if (!setObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_DEADLINE,
                getCalendarTime() - 1))
        {
            removeObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
            logic.clearPendingActionFlow(worker, player);
            return "error=workerTimeoutDeadlineSetupFailed " +
                buildWorkerStatus(player, lifecycle);
        }

        dictionary timeout = new dictionary();
        timeout.put("token", token);
        setObjVar(player, WORKER_SELECTION_STATE, 2);
        setObjVar(player, WORKER_SELECTION_QUEUED_AT, getCalendarTime());
        setObjVar(player, WORKER_TIMEOUT_EXPECTED, 1);
        removeObjVar(player, WORKER_TIMEOUT_COMPLETE);
        removeObjVar(player, WORKER_SELECTION_ERROR);
        if (!messageTo(
                worker,
                "expirePendingWorkerDroidAction",
                timeout,
                1.0f,
                true))
        {
            removeObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
            logic.clearPendingActionFlow(worker, player);
            removeObjVar(player, WORKER_TIMEOUT_EXPECTED);
            failWorkerSelection(player, "workerTimeoutQueueFailed");
            return "error=workerTimeoutQueueFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        return "action=workerTimeout deadlineExpiredQueued=true terminal=false " +
            buildWorkerStatus(player, lifecycle);
    }

    private String workerSelect(obj_id player, String lifecycle,
        int targetIndex, int actionIndex) throws InterruptedException
    {
        refreshWorkerFixtureState(player);
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        if (targetIndex < 0 || actionIndex < 0 || actionIndex > 1)
        {
            return "error=selectionOutOfRange";
        }
        if (!isPlayerConnected(player))
        {
            return "error=playerNotConnected";
        }

        int selectionState = hasObjVar(player, WORKER_SELECTION_STATE)
            ? getIntObjVar(player, WORKER_SELECTION_STATE) : -2;
        if (selectionState == 0)
        {
            return "action=workerSelect resumed=true selectionQueued=true " +
                buildWorkerStatus(player, lifecycle);
        }
        if (selectionState == 1)
        {
            return "action=workerSelect resumed=true selectionComplete=true " +
                buildWorkerStatus(player, lifecycle);
        }
        if (selectionState == 2)
        {
            return "action=workerSelect resumed=true actionDispatched=true " +
                buildWorkerStatus(player, lifecycle);
        }
        if (selectionState < 0 && selectionState != -2)
        {
            return "error=workerSelectionFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        ownership = validateWorkerOwnership(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        obj_id worker = getObjIdObjVar(player, WORKER);
        if (!utils.hasScriptVar(worker,
            script.item.droid.worker_droid.SCRIPT_VAR_PHASE) ||
            !script.item.droid.worker_droid.PHASE_SELECTING_TARGET.equals(
                utils.getStringScriptVar(worker,
                    script.item.droid.worker_droid.SCRIPT_VAR_PHASE)))
        {
            return "error=workerNotSelectingTarget " +
                buildWorkerStatus(player, lifecycle);
        }
        obj_id[] targets = utils.getObjIdArrayScriptVar(
            worker, script.item.droid.worker_droid.SCRIPT_VAR_TARGETS);
        if (targets == null || targetIndex >= targets.length)
        {
            return "error=targetIndexOutOfRange targets=" +
                (targets == null ? 0 : targets.length);
        }
        if (targets[targetIndex] != readObjId(player, STRUCTURE))
        {
            return "error=targetNotFixture";
        }
        if (!sui.hasPid(player, script.item.droid.worker_droid.PID_NAME))
        {
            return "error=targetSelectionPidMissing";
        }

        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            attachScript(worker, FIXTURE_SCRIPT);
        }
        if (!hasScript(worker, FIXTURE_SCRIPT))
        {
            failWorkerSelection(player, "workerFixtureScriptAttachFailed");
            return "error=workerFixtureScriptAttachFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        dictionary selectionRequest = new dictionary();
        selectionRequest.put("player", player);
        selectionRequest.put("lifecycle", lifecycle);
        selectionRequest.put("targetIndex", targetIndex);
        selectionRequest.put("actionIndex", actionIndex);
        setObjVar(player, WORKER_SELECTION_STATE, 0);
        setObjVar(player, WORKER_SELECTION_QUEUED_AT, getCalendarTime());
        removeObjVar(player, WORKER_SELECTION_ERROR);
        if (!messageTo(
            worker,
            "selectFixtureWorker",
            selectionRequest,
            1.0f,
            true))
        {
            failWorkerSelection(player, "selectionQueueFailed");
            return "error=workerSelectionQueueFailed " +
                buildWorkerStatus(player, lifecycle);
        }
        return "action=workerSelect resumed=false selectionQueued=true " +
            buildWorkerStatus(player, lifecycle);
    }

    public int selectFixtureWorker(obj_id self, dictionary params)
        throws InterruptedException
    {
        if (params == null || params.isEmpty() || !isLive(self))
        {
            return SCRIPT_CONTINUE;
        }
        obj_id player = params.getObjId("player");
        String lifecycle = params.getString("lifecycle");
        if (!isValidLifecycle(lifecycle) ||
            !hasMatchingObjectLifecycle(self, lifecycle))
        {
            return SCRIPT_CONTINUE;
        }
        if (!isAuthoritativeFixturePlayer(player) ||
            !isPlayerConnected(player))
        {
            if (!requeueFixtureHandler(
                    self, "selectFixtureWorker", params) &&
                isLive(player) && player.isAuthoritative())
            {
                failWorkerSelection(player, "playerUnavailable");
            }
            return SCRIPT_CONTINUE;
        }
        if (!hasObjVar(player, LIFECYCLE) ||
            !lifecycle.equals(getStringObjVar(player, LIFECYCLE)) ||
            readObjId(player, WORKER) != self)
        {
            failWorkerSelection(player, "lifecycleOwnershipLost");
            return SCRIPT_CONTINUE;
        }
        int selectionState = hasObjVar(player, WORKER_SELECTION_STATE)
            ? getIntObjVar(player, WORKER_SELECTION_STATE) : -2;
        if (selectionState != 0)
        {
            return SCRIPT_CONTINUE;
        }
        if (!utils.hasScriptVar(self,
            script.item.droid.worker_droid.SCRIPT_VAR_PHASE) ||
            !script.item.droid.worker_droid.PHASE_SELECTING_TARGET.equals(
                utils.getStringScriptVar(self,
                    script.item.droid.worker_droid.SCRIPT_VAR_PHASE)))
        {
            failWorkerSelection(player, "workerNotSelectingTarget");
            return SCRIPT_CONTINUE;
        }
        obj_id[] targets = utils.getObjIdArrayScriptVar(
            self, script.item.droid.worker_droid.SCRIPT_VAR_TARGETS);
        int targetIndex = params.getInt("targetIndex");
        int actionIndex = params.getInt("actionIndex");
        if (targets == null || targetIndex < 0 ||
            targetIndex >= targets.length || actionIndex < 0 ||
            actionIndex > 1 ||
            !sui.hasPid(player, script.item.droid.worker_droid.PID_NAME))
        {
            failWorkerSelection(player, "selectionStateInvalid");
            return SCRIPT_CONTINUE;
        }
        if (targets[targetIndex] != readObjId(player, STRUCTURE))
        {
            failWorkerSelection(player, "targetNotFixture");
            return SCRIPT_CONTINUE;
        }

        script.item.droid.worker_droid logic =
            new script.item.droid.worker_droid();
        int targetPid = sui.getPid(
            player, script.item.droid.worker_droid.PID_NAME);
        dictionary targetSelection = makeListboxSelection(
            player, targetPid, targetIndex);
        forceCloseSUIPage(targetPid);
        logic.handleWorkerDroidTargetSelection(self, targetSelection);
        if (!isLive(self) || !utils.hasScriptVar(self,
            script.item.droid.worker_droid.SCRIPT_VAR_PHASE) ||
            !script.item.droid.worker_droid.PHASE_SELECTING_ACTION.equals(
                utils.getStringScriptVar(self,
                    script.item.droid.worker_droid.SCRIPT_VAR_PHASE)) ||
            !sui.hasPid(player, script.item.droid.worker_droid.PID_NAME))
        {
            failWorkerSelection(player, "actionSelectionNotOpened");
            return SCRIPT_CONTINUE;
        }

        int beforeCount = getCount(self);
        int requestToken = utils.getIntScriptVar(
            self, script.item.droid.worker_droid.SCRIPT_VAR_TOKEN);
        setObjVar(player, WORKER_TOKEN, requestToken);
        setObjVar(player, WORKER_SELECTED_TARGET, targets[targetIndex]);
        setObjVar(player, WORKER_COUNT_BEFORE, beforeCount);
        int actionPid = sui.getPid(
            player, script.item.droid.worker_droid.PID_NAME);
        dictionary actionSelection = makeListboxSelection(
            player, actionPid, actionIndex);
        forceCloseSUIPage(actionPid);
        logic.handleWorkerDroidActionSelection(self, actionSelection);
        int afterCount = isLive(self) ? getCount(self) : 0;
        boolean consumed = beforeCount > 0 &&
            (afterCount == beforeCount - 1 ||
                (beforeCount == 1 && !isLive(self)));
        boolean dispatched = isLive(self) &&
            logic.hasValidPendingAction(self) &&
            getObjIdObjVar(self,
                script.item.droid.worker_droid.ITEM_PENDING_PLAYER) == player &&
            getObjIdObjVar(self,
                script.item.droid.worker_droid.ITEM_PENDING_TARGET) ==
                    targets[targetIndex] &&
            getIntObjVar(self,
                script.item.droid.worker_droid.ITEM_PENDING_TOKEN) ==
                    requestToken &&
            (actionIndex == 0 ? "activate" : "deactivate").equals(
                getStringObjVar(self,
                    script.item.droid.worker_droid.ITEM_PENDING_ACTION));
        setObjVar(player, WORKER_COUNT_AFTER, afterCount);
        setObjVar(player, WORKER_CONSUMED, consumed ? 1 : 0);
        // Dispatch is intermediate. Terminal success requires both the exact
        // target result and the matching ACK-driven charge transition.
        setObjVar(player, WORKER_SELECTION_STATE,
            dispatched || consumed ? 2 : -1);
        if (!dispatched && !consumed)
        {
            setObjVar(player, WORKER_SELECTION_ERROR, "droidNotDispatched");
        }
        return SCRIPT_CONTINUE;
    }

    private void refreshWorkerFixtureState(obj_id player)
        throws InterruptedException
    {
        if (!isValidId(player) || !hasObjVar(player, ROOT))
        {
            return;
        }
        int now = getCalendarTime();
        int launchState = hasObjVar(player, WORKER_LAUNCH_STATE)
            ? getIntObjVar(player, WORKER_LAUNCH_STATE) : -2;
        obj_id worker = readObjId(player, WORKER);
        if (launchState == 0 && isLive(worker) && utils.hasScriptVar(
            worker, script.item.droid.worker_droid.SCRIPT_VAR_PHASE))
        {
            setObjVar(player, WORKER_LAUNCH_STATE, 1);
            removeObjVar(player, WORKER_LAUNCH_QUEUED_AT);
            launchState = 1;
        }
        if (launchState == 0 && hasObjVar(player, WORKER_LAUNCH_QUEUED_AT) &&
            now - getIntObjVar(player, WORKER_LAUNCH_QUEUED_AT) >
                FIXTURE_HANDLER_TIMEOUT_SECONDS)
        {
            setObjVar(player, WORKER_LAUNCH_RESULT,
                script.item.droid.worker_droid.LAUNCH_RESULT_INVALID_STATE);
            setObjVar(player, WORKER_LAUNCH_STATE, -1);
            removeObjVar(player, WORKER_LAUNCH_QUEUED_AT);
        }

        int selectionState = hasObjVar(player, WORKER_SELECTION_STATE)
            ? getIntObjVar(player, WORKER_SELECTION_STATE) : -2;
        obj_id structure = readObjId(player, STRUCTURE);
        int expectedToken = hasObjVar(player, WORKER_TOKEN)
            ? getIntObjVar(player, WORKER_TOKEN) : 0;
        if ((selectionState == 0 || selectionState == 2) &&
            isLive(structure) && expectedToken > 0 &&
            utils.hasScriptVar(structure,
                script.structure.worker_droid_target.RESULT_TOKEN) &&
            utils.getObjIdScriptVar(structure,
                script.structure.worker_droid_target.RESULT_PLAYER) == player &&
            utils.getObjIdScriptVar(structure,
                script.structure.worker_droid_target.RESULT_DROID) == worker &&
            utils.getIntScriptVar(structure,
                script.structure.worker_droid_target.RESULT_TOKEN) ==
                    expectedToken)
        {
            int countBefore = hasObjVar(player, WORKER_COUNT_BEFORE)
                ? getIntObjVar(player, WORKER_COUNT_BEFORE) : -1;
            int countAfter = isLive(worker) ? getCount(worker) : 0;
            boolean consumed = countBefore > 0 &&
                (countAfter == countBefore - 1 ||
                    (countBefore == 1 && !isLive(worker)));
            setObjVar(player, WORKER_COUNT_AFTER, countAfter);
            setObjVar(player, WORKER_CONSUMED, consumed ? 1 : 0);
            if (!consumed)
            {
                return;
            }
            if (utils.getIntScriptVar(structure,
                    script.structure.worker_droid_target.RESULT_SUCCESS) == 1)
            {
                setObjVar(player, WORKER_SELECTION_STATE, 1);
                removeObjVar(player, WORKER_SELECTION_ERROR);
                removeObjVar(player, WORKER_SELECTION_QUEUED_AT);
            }
            else
            {
                failWorkerSelection(player, "remoteActionFailed");
            }
            return;
        }
        if (selectionState == 2)
        {
            int countBefore = hasObjVar(player, WORKER_COUNT_BEFORE)
                ? getIntObjVar(player, WORKER_COUNT_BEFORE) : -1;
            int countAfter = isLive(worker) ? getCount(worker) : 0;
            boolean productionPending = isLive(worker) && hasObjVar(
                worker,
                script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
            boolean consumed = countBefore > 0 &&
                (countAfter == countBefore - 1 ||
                    (countBefore == 1 && !isLive(worker)));
            if (!productionPending && consumed)
            {
                setObjVar(player, WORKER_COUNT_AFTER, countAfter);
                setObjVar(player, WORKER_CONSUMED, 1);
                boolean timeoutExpected = hasObjVar(
                    player, WORKER_TIMEOUT_EXPECTED) &&
                    getIntObjVar(player, WORKER_TIMEOUT_EXPECTED) == 1;
                if (timeoutExpected)
                {
                    setObjVar(player, WORKER_TIMEOUT_COMPLETE, 1);
                    removeObjVar(player, WORKER_TIMEOUT_EXPECTED);
                    setObjVar(player, WORKER_SELECTION_STATE, 1);
                    removeObjVar(player, WORKER_SELECTION_ERROR);
                    removeObjVar(player, WORKER_SELECTION_QUEUED_AT);
                }
                else
                {
                    failWorkerSelection(
                        player, "remoteActionUnconfirmed");
                }
                return;
            }
        }
        if (selectionState == 0 &&
            hasObjVar(player, WORKER_SELECTION_QUEUED_AT) &&
            now - getIntObjVar(player, WORKER_SELECTION_QUEUED_AT) >
                FIXTURE_HANDLER_TIMEOUT_SECONDS)
        {
            failWorkerSelection(player, "selectionHandlerTimeout");
        }
    }

    private void failWorkerSelection(obj_id player, String reason)
        throws InterruptedException
    {
        if (isValidId(player))
        {
            setObjVar(player, WORKER_SELECTION_STATE, -1);
            setObjVar(player, WORKER_SELECTION_ERROR, reason);
            removeObjVar(player, WORKER_SELECTION_QUEUED_AT);
        }
    }

    private String cleanup(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, ROOT))
        {
            return "action=cleanup alreadyClean=true";
        }
        String ownership = validateLifecycle(player, lifecycle);
        if (ownership != null)
        {
            return ownership;
        }
        refreshWorkerFixtureState(player);
        int selectionState = hasObjVar(player, WORKER_SELECTION_STATE)
            ? getIntObjVar(player, WORKER_SELECTION_STATE) : -2;
        obj_id worker = readObjId(player, WORKER);
        boolean productionPending = isLive(worker) && hasObjVar(
            worker,
            script.item.droid.worker_droid.ITEM_PENDING_ACTION_ROOT);
        if (selectionState == 0 || selectionState == 2 ||
            productionPending)
        {
            return "error=workerActionPending cleanupAllowed=false " +
                buildWorkerStatus(player, lifecycle);
        }

        boolean workerDestroyed = false;
        boolean structureDestroyed = false;
        int removalState = hasObjVar(player, STRUCTURE_REMOVED)
            ? getIntObjVar(player, STRUCTURE_REMOVED) : 0;
        obj_id structure = readObjId(player, STRUCTURE);

        if (isLive(worker))
        {
            if (!hasMatchingObjectLifecycle(worker, lifecycle))
            {
                return "error=fixtureObjectOwnershipLost object=worker";
            }
        }

        if (removalState != 1)
        {
            if (!isValidId(structure))
            {
                int initializationState = hasObjVar(player,
                    STRUCTURE_INITIALIZED)
                    ? getIntObjVar(player, STRUCTURE_INITIALIZED) : 0;
                if (initializationState == 1)
                {
                    return "error=trackedStructureMissing retry=false";
                }
                structureDestroyed = true;
                setObjVar(player, STRUCTURE_REMOVED, 1);
            }
            else if (isLive(structure) &&
                !hasMatchingObjectLifecycle(structure, lifecycle))
            {
                return "error=fixtureObjectOwnershipLost object=structure";
            }
            else
            {
                structureDestroyed = destroyObject(structure);
                if (!structureDestroyed)
                {
                    return "error=structureDestroyFailed retry=true";
                }
                setObjVar(player, STRUCTURE_REMOVED, 1);
            }
        }
        else
        {
            structureDestroyed = true;
        }

        if (isLive(worker))
        {
            new script.item.droid.worker_droid().cleanupFlow(worker, player);
            workerDestroyed = destroyObject(worker);
            if (!workerDestroyed)
            {
                return "error=workerDestroyFailed retry=true";
            }
        }
        else
        {
            clearTrackedMissingWorkerHandoff(player, worker);
        }
        if (hasObjVar(player, WORKER_COOLDOWN_VALUE) &&
            hasObjVar(player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN) &&
            getIntObjVar(player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN) ==
                getIntObjVar(player, WORKER_COOLDOWN_VALUE))
        {
            removeObjVar(
                player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN);
        }
        removeObjVar(player, ROOT);
        return "action=cleanup alreadyClean=false workerDestroyed=" +
            workerDestroyed + " structureRemoved=true" +
            " structureDestroyed=" + structureDestroyed + " clean=true";
    }

    private String buildWorkerStatus(obj_id player, String lifecycle)
        throws InterruptedException
    {
        obj_id worker = readObjId(player, WORKER);
        obj_id structure = readObjId(player, STRUCTURE);
        String phase = "none";
        int targetCount = 0;
        String labels = "none";
        int pid = -1;
        if (isLive(worker))
        {
            if (utils.hasScriptVar(worker,
                script.item.droid.worker_droid.SCRIPT_VAR_PHASE))
            {
                phase = utils.getStringScriptVar(worker,
                    script.item.droid.worker_droid.SCRIPT_VAR_PHASE);
            }
            if (utils.hasScriptVar(worker,
                script.item.droid.worker_droid.SCRIPT_VAR_TARGETS))
            {
                obj_id[] targets = utils.getObjIdArrayScriptVar(worker,
                    script.item.droid.worker_droid.SCRIPT_VAR_TARGETS);
                targetCount = targets == null ? 0 : targets.length;
            }
            if (utils.hasScriptVar(worker,
                script.item.droid.worker_droid.SCRIPT_VAR_LABELS))
            {
                labels = token(join(utils.getStringArrayScriptVar(
                    worker,
                    script.item.droid.worker_droid.SCRIPT_VAR_LABELS), "|"));
            }
        }
        if (sui.hasPid(player, script.item.droid.worker_droid.PID_NAME))
        {
            pid = sui.getPid(player, script.item.droid.worker_droid.PID_NAME);
        }
        boolean structureOwner = isLive(structure) &&
            player_structure.getStructureOwnerObjId(structure) == player;
        int initializationState = hasObjVar(player, STRUCTURE_INITIALIZED)
            ? getIntObjVar(player, STRUCTURE_INITIALIZED) : 0;
        boolean structureInitialized = initializationState == 1;
        location playerLocation = getLocation(player);
        boolean playerOutdoors = playerLocation != null &&
            !isValidId(playerLocation.cell);
        boolean playerConnected = isPlayerConnected(player);
        boolean workerContainedByPlayer = isLive(worker) &&
            utils.getContainingPlayer(worker) == player;
        boolean playerDead = isDead(player);
        boolean playerIncapacitated = isIncapacitated(player);
        int cooldownRemaining = 0;
        if (hasObjVar(player,
            script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN))
        {
            cooldownRemaining = Math.max(0, getIntObjVar(
                player,
                script.item.droid.worker_droid.PLAYER_LOCATE_COOLDOWN) -
                getCalendarTime());
        }
        int launchState = hasObjVar(player, WORKER_LAUNCH_STATE)
            ? getIntObjVar(player, WORKER_LAUNCH_STATE) : -2;
        int launchResult = hasObjVar(player, WORKER_LAUNCH_RESULT)
            ? getIntObjVar(player, WORKER_LAUNCH_RESULT) : 0;
        int fixtureCooldownValue = hasObjVar(player, WORKER_COOLDOWN_VALUE)
            ? getIntObjVar(player, WORKER_COOLDOWN_VALUE) : 0;
        int selectionState = hasObjVar(player, WORKER_SELECTION_STATE)
            ? getIntObjVar(player, WORKER_SELECTION_STATE) : -2;
        String selectionError = hasObjVar(player, WORKER_SELECTION_ERROR)
            ? getStringObjVar(player, WORKER_SELECTION_ERROR) : "none";
        obj_id selectedTarget = readObjId(player, WORKER_SELECTED_TARGET);
        int countBefore = hasObjVar(player, WORKER_COUNT_BEFORE)
            ? getIntObjVar(player, WORKER_COUNT_BEFORE) : -1;
        int countAfter = hasObjVar(player, WORKER_COUNT_AFTER)
            ? getIntObjVar(player, WORKER_COUNT_AFTER) : -1;
        boolean consumed = hasObjVar(player, WORKER_CONSUMED) &&
            getIntObjVar(player, WORKER_CONSUMED) == 1;
        boolean timeoutExpected = hasObjVar(
            player, WORKER_TIMEOUT_EXPECTED) &&
            getIntObjVar(player, WORKER_TIMEOUT_EXPECTED) == 1;
        boolean timeoutComplete = hasObjVar(
            player, WORKER_TIMEOUT_COMPLETE) &&
            getIntObjVar(player, WORKER_TIMEOUT_COMPLETE) == 1;
        boolean eligible = isLive(structure) &&
            hasScript(structure, WORKER_TARGET_SCRIPT) && structureOwner &&
            (player_structure.isHarvester(structure) ||
                player_structure.isGenerator(structure) ||
                player_structure.isFactory(structure)) &&
            !player_structure.isStructureCondemned(structure) &&
            !player_structure.isPreAbandoned(structure) &&
            !player_structure.isAbandoned(structure);
        int expectedToken = hasObjVar(player, WORKER_TOKEN)
            ? getIntObjVar(player, WORKER_TOKEN) : 0;
        if (expectedToken <= 0 && isLive(worker) && utils.hasScriptVar(
            worker, script.item.droid.worker_droid.SCRIPT_VAR_TOKEN))
        {
            expectedToken = utils.getIntScriptVar(
                worker, script.item.droid.worker_droid.SCRIPT_VAR_TOKEN);
        }
        boolean grantPresent = isLive(structure) && expectedToken > 0 &&
            new script.structure.worker_droid_target().grantMatches(
                structure, player, worker, expectedToken);
        boolean resultPresent = isLive(structure) && utils.hasScriptVar(
            structure,
            script.structure.worker_droid_target.RESULT_TOKEN);
        boolean resultSuccess = resultPresent && utils.getIntScriptVar(
            structure,
            script.structure.worker_droid_target.RESULT_SUCCESS) == 1;
        String resultAction = resultPresent ? utils.getStringScriptVar(
            structure,
            script.structure.worker_droid_target.RESULT_ACTION) : "none";
        boolean resultMatches = resultPresent && expectedToken > 0 &&
            utils.getObjIdScriptVar(
                structure,
                script.structure.worker_droid_target.RESULT_PLAYER) == player &&
            utils.getObjIdScriptVar(
                structure,
                script.structure.worker_droid_target.RESULT_DROID) == worker &&
            utils.getIntScriptVar(
                structure,
                script.structure.worker_droid_target.RESULT_TOKEN) == expectedToken;
        return "lifecycle=" + lifecycle + " worker=" + worker +
            " workerLoaded=" + isLive(worker) + " workerTagged=" +
            hasMatchingObjectLifecycle(worker, lifecycle) +
            " workerCount=" + (isLive(worker) ? getCount(worker) : 0) +
            " phase=" + token(phase) + " targetCount=" + targetCount +
            " labels=" + labels + " pid=" + pid + " structure=" +
            structure + " structureLoaded=" + isLive(structure) +
            " structureTagged=" +
            hasMatchingObjectLifecycle(structure, lifecycle) +
            " structureInitializationState=" + initializationState +
            " structureInitialized=" + structureInitialized +
            " playerOutdoors=" + playerOutdoors +
            " playerConnected=" + playerConnected +
            " playerDead=" + playerDead +
            " playerIncapacitated=" + playerIncapacitated +
            " workerContainedByPlayer=" + workerContainedByPlayer +
            " locateCooldownRemaining=" + cooldownRemaining +
            " launchState=" + launchState +
            " launchResult=" + launchResult +
            " fixtureCooldownValue=" + fixtureCooldownValue +
            " selectionState=" + selectionState +
            " selectionError=" + token(selectionError) +
            " selectedTarget=" + selectedTarget +
            " countBefore=" + countBefore +
            " countAfter=" + countAfter +
            " consumed=" + consumed +
            " timeoutExpected=" + timeoutExpected +
            " timeoutComplete=" + timeoutComplete +
            " structureOwner=" + structureOwner + " eligible=" + eligible +
            " expectedToken=" + expectedToken +
            " grantPresent=" + grantPresent +
            " actionResultPresent=" + resultPresent +
            " actionResultMatches=" + resultMatches +
            " actionResultSuccess=" + resultSuccess +
            " actionResultAction=" + token(resultAction) +
            " structureActive=" + (isLive(structure) &&
                isHarvesterActive(structure));
    }

    private dictionary makeListboxSelection(obj_id player, int pageId,
        int selectedRow) throws InterruptedException
    {
        dictionary params = new dictionary();
        params.put("player", player);
        params.put("pageId", pageId);
        params.put(sui.PROP_BUTTONPRESSED, sui.OK);
        params.put(sui.LISTBOX_SELECTEDROW, Integer.toString(selectedRow));
        return params;
    }

    private String validateWorkerOwnership(obj_id player, String lifecycle)
        throws InterruptedException
    {
        String lifecycleError = validateLifecycle(player, lifecycle);
        if (lifecycleError != null)
        {
            return lifecycleError;
        }
        obj_id worker = readObjId(player, WORKER);
        obj_id structure = readObjId(player, STRUCTURE);
        if (!isLive(worker) || !isLive(structure))
        {
            return "error=fixtureObjectsUnavailable";
        }
        if (!hasMatchingObjectLifecycle(worker, lifecycle) ||
            !hasMatchingObjectLifecycle(structure, lifecycle))
        {
            return "error=fixtureObjectOwnershipLost";
        }
        if (utils.getContainingPlayer(worker) != player)
        {
            return "error=workerNotInPlayerInventory";
        }
        return null;
    }

    private String validateLifecycle(obj_id player, String lifecycle)
        throws InterruptedException
    {
        if (!hasObjVar(player, LIFECYCLE))
        {
            return "error=fixtureAbsent";
        }
        return lifecycle.equals(getStringObjVar(player, LIFECYCLE))
            ? null : "error=lifecycleMismatch";
    }

    private boolean hasUnownedWorkerFlow(obj_id player)
        throws InterruptedException
    {
        return utils.hasScriptVar(player,
                script.item.droid.worker_droid.PLAYER_ACTIVE_ITEM) ||
            hasObjVar(player,
                script.item.droid.worker_droid.PLAYER_REQUESTOR_OBJVAR) ||
            hasObjVar(player,
                script.item.droid.worker_droid.PLAYER_REQUEST_TOKEN_OBJVAR) ||
            hasObjVar(player,
                script.item.droid.worker_droid.PLAYER_PENDING_ITEM);
    }

    private void clearTrackedMissingWorkerHandoff(obj_id player,
        obj_id trackedWorker) throws InterruptedException
    {
        boolean ownsHandoff = false;
        boolean ownsRequestor = false;
        if (utils.hasScriptVar(player,
            script.item.droid.worker_droid.PLAYER_ACTIVE_ITEM) &&
            utils.getObjIdScriptVar(player,
                script.item.droid.worker_droid.PLAYER_ACTIVE_ITEM) ==
                    trackedWorker)
        {
            utils.removeScriptVar(player,
                script.item.droid.worker_droid.PLAYER_ACTIVE_ITEM);
            ownsHandoff = true;
        }
        if (hasObjVar(player,
            script.item.droid.worker_droid.PLAYER_REQUESTOR_OBJVAR) &&
            getObjIdObjVar(player,
                script.item.droid.worker_droid.PLAYER_REQUESTOR_OBJVAR) ==
                    trackedWorker)
        {
            removeObjVar(player,
                script.item.droid.worker_droid.PLAYER_REQUESTOR_OBJVAR);
            ownsHandoff = true;
            ownsRequestor = true;
        }
        if (hasObjVar(player,
            script.item.droid.worker_droid.PLAYER_PENDING_ITEM) &&
            getObjIdObjVar(player,
                script.item.droid.worker_droid.PLAYER_PENDING_ITEM) ==
                    trackedWorker)
        {
            removeObjVar(player,
                script.item.droid.worker_droid.PLAYER_PENDING_ITEM);
            ownsHandoff = true;
        }
        if (ownsRequestor && hasObjVar(player,
            script.item.droid.worker_droid.PLAYER_REQUEST_TOKEN_OBJVAR))
        {
            removeObjVar(player,
                script.item.droid.worker_droid.PLAYER_REQUEST_TOKEN_OBJVAR);
        }
        if (ownsHandoff &&
            sui.hasPid(player, script.item.droid.worker_droid.PID_NAME))
        {
            sui.removePid(player, script.item.droid.worker_droid.PID_NAME);
        }
    }

    private boolean requeueFixtureHandler(obj_id self, String handler,
        dictionary params) throws InterruptedException
    {
        int attempts = params.containsKey("fixtureRetryCount")
            ? params.getInt("fixtureRetryCount") : 0;
        if (attempts >= 60)
        {
            return false;
        }
        params.put("fixtureRetryCount", attempts + 1);
        return messageTo(self, handler, params, 1.0f, true);
    }

    private void tagOneShotCraftObject(obj_id object)
        throws InterruptedException
    {
        if (isLive(object))
        {
            setObjVar(object, OBJECT_LIFECYCLE, "craftOneShot");
        }
    }

    private void destroyOneShotCraftObject(obj_id object)
        throws InterruptedException
    {
        if (isLive(object) && hasObjVar(object, OBJECT_LIFECYCLE) &&
            "craftOneShot".equals(
                getStringObjVar(object, OBJECT_LIFECYCLE)))
        {
            destroyObject(object);
        }
    }

    private boolean isSchematicInGroup(String group, String schematic)
        throws InterruptedException
    {
        int rows = dataTableGetNumRows(SCHEMATIC_GROUP_TABLE);
        for (int row = 0; row < rows; ++row)
        {
            if (group.equals(dataTableGetString(
                    SCHEMATIC_GROUP_TABLE, row, "GroupId")) &&
                schematic.equals(dataTableGetString(
                    SCHEMATIC_GROUP_TABLE, row, "SchematicName")))
            {
                return true;
            }
        }
        return false;
    }

    private boolean sameSlots(draft_schematic left, draft_schematic right)
        throws InterruptedException
    {
        draft_schematic.slot[] leftSlots = left.getSlots();
        draft_schematic.slot[] rightSlots = right.getSlots();
        if (leftSlots == null || rightSlots == null ||
            leftSlots.length != rightSlots.length)
        {
            return false;
        }
        for (int i = 0; i < leftSlots.length; ++i)
        {
            draft_schematic.slot a = leftSlots[i];
            draft_schematic.slot b = rightSlots[i];
            if (!sameStringId(a.name, b.name) ||
                a.slotOption != b.slotOption ||
                a.ingredientType != b.ingredientType ||
                !sameString(a.ingredientName, b.ingredientName) ||
                a.amountRequired != b.amountRequired ||
                a.complexity != b.complexity ||
                !sameString(a.appearance, b.appearance))
            {
                return false;
            }
        }
        return true;
    }

    private boolean sameStringId(string_id left, string_id right)
        throws InterruptedException
    {
        if (left == null || right == null)
        {
            return left == right;
        }
        return left.equals(right);
    }

    private boolean sameString(String left, String right)
        throws InterruptedException
    {
        return left == null ? right == null : left.equals(right);
    }

    private boolean sameStrings(String[] left, String[] right)
        throws InterruptedException
    {
        if (left == null || right == null)
        {
            return left == right;
        }
        if (left.length != right.length)
        {
            return false;
        }
        for (int i = 0; i < left.length; ++i)
        {
            if (!sameString(left[i], right[i]))
            {
                return false;
            }
        }
        return true;
    }

    private int slotCount(draft_schematic data) throws InterruptedException
    {
        return data.getSlots() == null ? 0 : data.getSlots().length;
    }

    private String objectCraftSummary(obj_id object)
        throws InterruptedException
    {
        if (!isLive(object))
        {
            return "missing";
        }
        return object + ",template:" + token(getTemplateName(object)) +
            ",count:" + getCount(object) + ",name:" +
            token(getName(object)) + ",scripts:" +
            token(join(getScriptList(object), "|"));
    }

    private String join(String[] values, String separator)
        throws InterruptedException
    {
        if (values == null || values.length == 0)
        {
            return "none";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < values.length; ++i)
        {
            if (i > 0)
            {
                result.append(separator);
            }
            result.append(values[i]);
        }
        return result.toString();
    }

    private String locationToken(location value) throws InterruptedException
    {
        if (value == null)
        {
            return "none";
        }
        return token(value.area) + ":" + (int)value.x + ":" +
            (int)value.y + ":" + (int)value.z;
    }

    private boolean sameWorldLocation(location left, location right)
        throws InterruptedException
    {
        return left != null && right != null &&
            left.area != null && left.area.equals(right.area) &&
            Math.abs(left.x - right.x) < 0.01f &&
            Math.abs(left.z - right.z) < 0.01f;
    }

    private String token(String value) throws InterruptedException
    {
        if (value == null || value.length() == 0)
        {
            return "none";
        }
        return value.replace(' ', '_').replace('\t', '_')
            .replace('\r', '_').replace('\n', '_');
    }

    private obj_id readObjId(obj_id player, String variable)
        throws InterruptedException
    {
        return hasObjVar(player, variable)
            ? getObjIdObjVar(player, variable) : obj_id.NULL_ID;
    }

    private boolean hasMatchingObjectLifecycle(obj_id object,
        String lifecycle) throws InterruptedException
    {
        return isLive(object) && hasObjVar(object, OBJECT_LIFECYCLE) &&
            lifecycle.equals(getStringObjVar(object, OBJECT_LIFECYCLE));
    }

    private boolean isLive(obj_id object) throws InterruptedException
    {
        return isIdValid(object) && object.isLoaded() && exists(object);
    }

    private boolean isAuthoritativeFixturePlayer(obj_id player)
        throws InterruptedException
    {
        return isLive(player) && player.getValue() == PLAYER_OID &&
            player.isAuthoritative() && isPlayer(player) &&
            getPlayerStationId(player) == PLAYER_STATION_ID;
    }

    private boolean isValidLifecycle(String lifecycle)
        throws InterruptedException
    {
        return lifecycle != null && lifecycle.matches("[0-9a-fA-F]{32}");
    }
}
