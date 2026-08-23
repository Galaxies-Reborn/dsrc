package script.library;

import script.dictionary;
import script.location;
import script.obj_id;
import script.resource_density;

import java.util.Vector;

public class survey_droid extends script.base_script
{
    public survey_droid()
    {
    }

    public static final String LOG_CATEGORY = "survey_droid";
    public static final String RESOURCE_ROOT = "resource";
    public static final String RESOURCE_INORGANIC = "inorganic";
    public static final String RESOURCE_FLORA = "flora_resources";
    public static final String RESOURCE_ENERGY = "energy";
    public static final float MINIMUM_DENSITY = 0.50f;
    public static final float MAXIMUM_DENSITY = 1.00f;
    public static final int PLANET_COORDINATE_MIN = -7500;
    public static final int PLANET_COORDINATE_MAX = 7500;
    public static final int MAX_SCAN_ATTEMPTS = 256;

    public static final String[] PLANET_INTERNAL =
    {
        "tatooine",
        "naboo",
        "corellia",
        "rori",
        "talus",
        "endor",
        "dantooine",
        "dathomir",
        "lok",
        "yavin4"
    };

    public static boolean isSupportedPlanet(String planet) throws InterruptedException
    {
        if (planet == null || planet.equals(""))
        {
            return false;
        }
        for (String supportedPlanet : PLANET_INTERNAL)
        {
            if (supportedPlanet.equals(planet))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isSurveyableResource(obj_id resourceType) throws InterruptedException
    {
        if (!isValidId(resourceType))
        {
            return false;
        }
        return isResourceDerivedFrom(resourceType, RESOURCE_INORGANIC) ||
            isResourceDerivedFrom(resourceType, RESOURCE_FLORA) ||
            isResourceDerivedFrom(resourceType, RESOURCE_ENERGY);
    }

    public static dictionary findHighDensityResource(String planet) throws InterruptedException
    {
        if (!isSupportedPlanet(planet))
        {
            return null;
        }

        for (int attempt = 0; attempt < MAX_SCAN_ATTEMPTS; attempt++)
        {
            location surveyLocation = new location(
                rand(PLANET_COORDINATE_MIN, PLANET_COORDINATE_MAX),
                0.0f,
                rand(PLANET_COORDINATE_MIN, PLANET_COORDINATE_MAX),
                planet);
            resource_density[] resources = requestResourceList(
                surveyLocation,
                MINIMUM_DENSITY,
                MAXIMUM_DENSITY,
                RESOURCE_ROOT);
            if (resources == null || resources.length == 0)
            {
                continue;
            }

            Vector surveyableResources = new Vector();
            surveyableResources.setSize(0);
            for (resource_density resource : resources)
            {
                if (resource == null || resource.getDensity() < MINIMUM_DENSITY ||
                    !isSurveyableResource(resource.getResourceType()))
                {
                    continue;
                }
                surveyableResources.add(resource);
            }
            if (surveyableResources.size() == 0)
            {
                continue;
            }

            resource_density result = (resource_density)surveyableResources.get(
                rand(0, surveyableResources.size() - 1));
            dictionary report = new dictionary();
            report.put("planetName", planet);
            report.put("surveyLocation", surveyLocation);
            report.put("resourceType", result.getResourceType());
            report.put("density", result.getDensity());
            return report;
        }

        LOG(LOG_CATEGORY, "Unable to locate a 50 percent or greater surveyable resource on " + planet +
            " after " + MAX_SCAN_ATTEMPTS + " attempts.");
        return null;
    }

    public static void deliverSurveyReport(obj_id player, dictionary request) throws InterruptedException
    {
        if (!isValidId(player) || !isPlayer(player) || request == null || request.isEmpty())
        {
            return;
        }

        String planet = request.getString("planetName");
        dictionary report = findHighDensityResource(planet);
        String planetName = utils.packStringId(new script.string_id("planet_n", planet));
        if (report == null)
        {
            String subject = "Survey Droid Report: " + planetName;
            String body = "The Survey Droid completed its search, but no currently available resource " +
                "with a concentration of at least 50% could be located. No waypoint was generated.";
            chatSendPersistentMessage("Survey Droid", getName(player), subject, body, null);
            sendSystemMessage(player, "Your Survey Droid returned without locating a 50% resource concentration.", null);
            return;
        }

        location surveyLocation = report.getLocation("surveyLocation");
        obj_id resourceType = report.getObjId("resourceType");
        float density = report.getFloat("density");
        String resourceName = getResourceName(resourceType);
        int densityPercent = Math.round(density * 100.0f);
        String waypointName = "Survey: " + resourceName + " (" + densityPercent + "%)";

        obj_id waypoint = createSurveyWaypoint(player, report);

        String subject = "Survey Droid Report: " + planetName + " - " + resourceName;
        String body = "The Survey Droid located " + resourceName + " at " + densityPercent +
            "% concentration near " + (int)surveyLocation.x + ", " + (int)surveyLocation.z +
            " on " + planetName + ".";
        if (!isValidId(waypoint))
        {
            body += " Your datapad waypoint limit is full, so use the attached waypoint after freeing a slot.";
        }
        String waypointData = chatAppendPersistentMessageWaypointData(
            null,
            planet,
            surveyLocation.x,
            surveyLocation.z,
            null,
            waypointName);
        chatSendPersistentMessage("Survey Droid", getName(player), subject, body, waypointData);
        sendSystemMessage(player, "Your Survey Droid located " + resourceName + " at " + densityPercent +
            "% concentration on " + planetName + ".", null);
    }

    public static obj_id createSurveyWaypoint(obj_id player, dictionary report) throws InterruptedException
    {
        if (!isValidId(player) || !isPlayer(player) || report == null || report.isEmpty())
        {
            return obj_id.NULL_ID;
        }

        String planet = report.getString("planetName");
        location surveyLocation = report.getLocation("surveyLocation");
        obj_id resourceType = report.getObjId("resourceType");
        float density = report.getFloat("density");
        if (!isSupportedPlanet(planet) || surveyLocation == null ||
            !planet.equals(surveyLocation.area) || !isSurveyableResource(resourceType) ||
            density < MINIMUM_DENSITY || density > MAXIMUM_DENSITY)
        {
            return obj_id.NULL_ID;
        }

        String resourceName = getResourceName(resourceType);
        int densityPercent = Math.round(density * 100.0f);
        obj_id waypoint = createWaypointInDatapadWithLimits(player, surveyLocation);
        if (!isValidId(waypoint))
        {
            return obj_id.NULL_ID;
        }

        setWaypointName(waypoint, "Survey: " + resourceName + " (" + densityPercent + "%)");
        setWaypointColor(waypoint, "green");
        setWaypointVisible(waypoint, true);
        location playerLocation = getLocation(player);
        setWaypointActive(waypoint, playerLocation != null && planet.equals(playerLocation.area));
        return waypoint;
    }
}
