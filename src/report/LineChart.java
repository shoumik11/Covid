
package report;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

import database.Config;
import database.Query;
import gui.ChartViewer;
import mlcore.Simulator;
import object.WorldDataTuple;
import utils.Utils;
public class LineChart {
	private final static Color[] cmap = {Color.BLUE, Color.GREEN, Color.RED};
	
	public static void draw(Date fromDate, Date toDate) throws SQLException, IOException, Exception {
		int from = (int)(fromDate.getTime()/1000);
		int to = (int)(toDate.getTime()/1000);
		int count;
		
		//sanitize date integer
		from -= from % Config.daySec;
		to -= to % Config.daySec;
		if(from >= to)
		{
			System.out.println("Wrong date range selected");
			return;
		}
		XYChart chart = new XYChartBuilder()
				.xAxisTitle("Days from " + Utils.getDateStringFromEpoch(from) +" to "+ Utils.getDateStringFromEpoch(to))
				.yAxisTitle("Count").width(1024).height(720).build();
		List<WorldDataTuple> data = Query.getWorldDataByDateRange(from, to);
		if(data.size() == 0)
		{
			System.out.println("Import world data first");
			return;
		}
		int minDate = data.get(0).getDate();
		int maxDate = data.get(data.size() - 1).getDate();
		if(minDate > from)
		{
			//Need to get prediction data
			int diff = (minDate - from)/Config.daySec - 1;
			List<Integer> c = Simulator.getPredictionList(from, diff, Simulator.AttrName_CONFIRMED);
			List<Integer> r = Simulator.getPredictionList(from, diff, Simulator.AttrName_RECOVERED);
			List<Integer> d = Simulator.getPredictionList(from, diff, Simulator.AttrName_DEATHS);
			List<WorldDataTuple> tmp = new ArrayList<WorldDataTuple>();
			for(int i = 0; i < c.size(); i++)
			{
				tmp.add(new WorldDataTuple(from + (i * Config.daySec), c.get(i), r.get(i), d.get(i)));
			}
			data.addAll(tmp);
		}
		if(maxDate < to)
		{
			//Need to get prediction data
			int diff = (to - maxDate)/Config.daySec;
			int start = maxDate + Config.daySec;
			List<Integer> c = Simulator.getPredictionList(start, diff, Simulator.AttrName_CONFIRMED);
			List<Integer> r = Simulator.getPredictionList(start, diff, Simulator.AttrName_RECOVERED);
			List<Integer> d = Simulator.getPredictionList(start, diff, Simulator.AttrName_DEATHS);
			List<WorldDataTuple> tmp = new ArrayList<WorldDataTuple>();
			for(int i = 0; i < c.size(); i++)
			{
				tmp.add(new WorldDataTuple(start + (i * Config.daySec), c.get(i), r.get(i), d.get(i)));
			}
			data.addAll(tmp);
		}
		Collections.sort(data);
		double limit = Utils.getMaxLimit(data);
		count = data.size();
		int [] infectedData = new int[count];
		int [] recoveredData = new int[count];
		int [] deathData = new int[count];
		for(int i = 0; i < count; i++)
		{
			WorldDataTuple w = data.get(i);
			infectedData[i] = w.getInfectedCount();
			recoveredData[i] = w.getRecoveredCount();
			deathData[i] = w.getDeathCount();
		}
		chart.getStyler().setYAxisMin((double) 0);
		chart.getStyler().setYAxisMax((double) limit + 10);
		chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
		chart.addSeries("Infected", null, infectedData).setMarker(SeriesMarkers.NONE);
		chart.addSeries("Recovered", null, recoveredData).setMarker(SeriesMarkers.NONE);
		chart.addSeries("Deaths", null, deathData).setMarker(SeriesMarkers.NONE).setFillColor(Color.RED);
		chart.getStyler().setSeriesColors(cmap);
		new ChartViewer(chart);
	}

	/**
	 * Generates a set of random walk data
	 *
	 * @param numPoints
	 * @return
	 */
//	private static double[] getRandomWalk(int days, int population, int patientCount)
//	{
//		double[] y = new double[days];
//		max = y[0] = patientCount;
////		double k, x0 = population * 0.7, L = population;
////		k = 1-Utils.getRandomRate(Config.getInfectionRate());
////		
////		double k = 1, x0 = -0.5, L = 1;
//		double u = 3, o2 = 1.8; 
//		/*So far the best
//		 * y[i] = i*Math.exp(-Math.pow(0.2*i-u,2)/(2*o2))/Math.sqrt(2*Math.PI*o2);
//		 * 			double u = 4.05, o2 = 1.8;
//		*/
//		for (int i = 1; i < y.length; i++)
//		{
//			//y[i] = y[i-1] + Math.exp(-Math.pow(0.2*i-u,2)/(2*o2))/Math.sqrt(2*Math.PI*o2);
////			System.out.println("# = " + Utils.getRandomRate(Config.getInfectionRate()));
//			y[i] = y[i-1] + (1-(y[i-1]/population))*i*Math.exp(-Math.pow(Utils.getRandomRate(Config.getInfectionRate())*10 
//					* (1-(y[i-1]/population))*i-u,2)/(2*o2))/Math.sqrt(2*Math.PI*o2);
////			k = Utils.getRandomRate(Config.getInfectionRate());
////			y[i] = y[i-1] + Math.pow(2, 1+k);
////			y[i] = Math.pow(1+k, i)*patientCount;
//			//y[i] = population/(1+Math.exp(-k*(i-x0)));
////			y[i] = y[i - 1]*(1+Utils.getRandomRate(Config.getInfectionRate()));
////			y[i] = y[i - 1]*(1+Utils.getRandomRate(0.2, 0.001));
//			//System.out.println("#"+y[i]);
//			if(y[i] > max)
//				max = y[i];
//		}
//		return y;
//	}

}