package script.test;

import script.dictionary;
import script.obj_id;

/**
 * Identity-locked player-attached lifecycle driver for Publish 14.1 Life Day.
 */
public class precu_lifeday_2004_fixture extends script.base_script
{
    private static final long PLAYER_OID = 39008597L;
    private static final int PLAYER_STATION_ID = 1001;
    private static final String DRIVER =
        "test.precu_lifeday_2004_fixture";
    private static final String ACTION_VAR =
        "precu.fixture.lifeday.active";
    private static final String RESULT_VAR =
        "precu.fixture.lifeday.notified";
    private static final String[] PLANETS =
    {
        "tatooine",
        "corellia",
        "naboo",
        "dathomir",
        "endor",
        "yavin4"
    };

    public int OnAttach(obj_id self) throws InterruptedException
    {
        if (self == null || self == obj_id.NULL_ID ||
            self.getValue() != PLAYER_OID ||
            !isPlayer(self) ||
            getPlayerStationId(self) != PLAYER_STATION_ID ||
            !hasObjVar(self, ACTION_VAR))
        {
            detachScript(self, DRIVER);
            return SCRIPT_CONTINUE;
        }
        boolean active = getIntObjVar(self, ACTION_VAR) == 1;
        dictionary refresh = new dictionary();
        refresh.put("active", active);
        int notified = 0;
        for (String planetName : PLANETS)
        {
            obj_id planet = getPlanetByName(planetName);
            if (isIdValid(planet) && exists(planet))
            {
                messageTo(
                    planet,
                    "refreshPrecuLifeDayAnchors",
                    refresh,
                    0.0f,
                    false);
                ++notified;
            }
        }
        setObjVar(self, RESULT_VAR, notified);
        removeObjVar(self, ACTION_VAR);
        detachScript(self, DRIVER);
        return SCRIPT_CONTINUE;
    }
}
