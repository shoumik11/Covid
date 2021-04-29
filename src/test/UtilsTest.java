
package test;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import database.Config;
import database.Query;
import junit.framework.TestCase;
import mlcore.Simulator;

class ProjectTestCase extends TestCase {

	@Test
	void testUtils() {
		//fail("Not yet implemented");
		float f = (float) 2.335;
		assertEquals("Checking Utils.round", f, Utils.round(new Float("2.33469"), 3));
		assertEquals("Checking Utils.getDateStringFromEpoch", "2020-06-28", Utils.getDateStringFromEpoch(1593337483));
		
		String s = Utils.createRandomString(5, true);
		assertEquals(true, s.length() <= 5);
		char[] a = s.substring(0,1).toCharArray();
		assertEquals(true, (a[0] >= 'A' && a[0] <= 'Z'));
	}
	@Test
	void testConfig() {
		assertEquals("Checking Config.getInfectionRate", 0.02, Config.getInfectionRate());
	}
	@Test
	void testQuery() throws SQLException, IOException, Exception {
		Query.setInfectionRate(0.002);
		assertEquals("Checking Query.getInfectionRate()", 0.002, Query.getInfectionRate());
	}
	@Test
	void testSimulator() throws SQLException, IOException, Exception {
		int from = (int)((new Date()).getTime()/1000);
		List<Integer> d = Simulator.getPredictionList(from, 4, Simulator.AttrName_DEATHS);
		assertEquals("Checking Simulator.getPredictionList()", 4, d.size());
	}
	
}
