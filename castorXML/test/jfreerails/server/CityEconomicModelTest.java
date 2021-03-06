/*
 * Created on Jul 10, 2004
 */
package jfreerails.server;

import jfreerails.world.terrain.CityModel;
import jfreerails.world.top.MapFixtureFactory;
import jfreerails.world.top.SKEY;
import jfreerails.world.top.World;
import junit.framework.TestCase;

/**
 * JUnit Test for CityEconomic.
 * 
 * @author Luke
 * 
 */
public class CityEconomicModelTest extends TestCase {
    /**
     * Tests generating populated CityEconomicModel from cities on the map.
     */
    public void testLoadFromMap() {
        World w = MapFixtureFactory.getWorld(100, 100);
        CityModel newYork = new CityModel("New York", 10, 20);
        w.add(SKEY.CITIES, newYork);

        CityEconomicModel city = new CityEconomicModel();
        city.loadFromMap(w, 0);
        assertEquals(0, city.industryTiles.size());
        assertEquals(0, city.urbanTiles.size());
        assertEquals("A city is a 7*7 area", 49, city.clearTiles.size());
    }

    /** Tests calculating the utility of a City. */
    public void testUtilityCalculation() {
    }
}