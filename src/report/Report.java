
package report;
import object.*;
import utils.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;

import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.chart.DJChart;
import ar.com.fdvs.dj.domain.chart.DJChartOptions;
import ar.com.fdvs.dj.domain.chart.builder.DJBarChartBuilder;
import ar.com.fdvs.dj.domain.chart.builder.DJPieChartBuilder;
import ar.com.fdvs.dj.domain.chart.plot.DJAxisFormat;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import database.Config;
import database.Query;
import mlcore.Simulator;

public class Report extends ReportBase {
	private DynamicReportBuilder builder;

	public Report() throws Exception {
		builder = new DynamicReportBuilder();

		builder.addColumn(date);
		builder.addColumn(infected);
		builder.addColumn(recovered);
		builder.addColumn(died);
		builder.addColumn(infectionRate);

		builder.setUseFullPageWidth(true);
		builder.setDefaultStyles(null, null, getHeaderStyle(), null);
	}

	private DJChart createBarChart(String fromDate, String toDate) {

		DJAxisFormat categoryAxisFormat = new DJAxisFormat("days");
		categoryAxisFormat.setLabelFont(Font.ARIAL_MEDIUM);
		categoryAxisFormat.setLabelColor(Color.DARK_GRAY);
		categoryAxisFormat.setTickLabelFont(Font.ARIAL_SMALL);
		categoryAxisFormat.setTickLabelColor(Color.DARK_GRAY);
		categoryAxisFormat.setTickLabelMask("dd");
		categoryAxisFormat.setLineColor(Color.DARK_GRAY);

		DJAxisFormat valueAxisFormat = new DJAxisFormat("count");
		valueAxisFormat.setLabelFont(Font.ARIAL_MEDIUM);
		valueAxisFormat.setLabelColor(Color.DARK_GRAY);
		valueAxisFormat.setTickLabelFont(Font.ARIAL_SMALL);
		valueAxisFormat.setTickLabelColor(Color.DARK_GRAY);
		valueAxisFormat.setTickLabelMask("");
		valueAxisFormat.setLineColor(Color.DARK_GRAY);
		valueAxisFormat.setLineColor(Color.DARK_GRAY);

		DJChart chart = new DJBarChartBuilder()
				.setX(20).setY(10).setWidth(500)
				.setHeight(300).setCentered(false)
				.setBackColor(Color.LIGHT_GRAY).setShowLegend(true)
				.setPosition(DJChartOptions.POSITION_FOOTER)
				.setTitle("COVID Status").setTitleColor(Color.DARK_GRAY)
				.setTitleFont(Font.ARIAL_BIG_BOLD)
				.setSubtitle("Daily count from " + fromDate + " to " + toDate)
				.setSubtitleColor(Color.DARK_GRAY)
				.setSubtitleFont(Font.GEORGIA_SMALL_BOLD)
				.setLegendColor(Color.DARK_GRAY)
				.setLegendFont(Font.ARIAL_SMALL_BOLD)
				.setLegendBackgroundColor(Color.WHITE)
				.setLegendPosition(DJChartOptions.EDGE_BOTTOM)
				.setTitlePosition(DJChartOptions.EDGE_TOP)
				.setLineStyle(DJChartOptions.LINE_STYLE_DOTTED)
				.setLineWidth(1)
				.setLineColor(Color.DARK_GRAY).setPadding(5)
				.setCategory((PropertyColumn) date).addSerie(died).addSerie(infected).addSerie(recovered)
				.setShowLabels(false).setCategoryAxisFormat(categoryAxisFormat)
				.setValueAxisFormat(valueAxisFormat).build();
		return chart;
	}

	private DJChart createPieChart() {
		DJChart chart = new DJPieChartBuilder()
				.setX(20).setY(10).setWidth(500)
				.setHeight(250).setCentered(false)
				.setBackColor(Color.LIGHT_GRAY).setShowLegend(true)
				.setPosition(DJChartOptions.POSITION_FOOTER)
				.setTitle("COVID Status").setTitleColor(Color.DARK_GRAY)
				.setTitleFont(Font.ARIAL_BIG_BOLD)
				.setSubtitle("on Date")
				.setSubtitleColor(Color.DARK_GRAY)
				.setSubtitleFont(Font.GEORGIA_SMALL_BOLD)
				.setLegendColor(Color.DARK_GRAY)
				.setLegendFont(Font.ARIAL_SMALL_BOLD)
				.setLegendBackgroundColor(Color.WHITE)
				.setLegendPosition(DJChartOptions.EDGE_BOTTOM)
				.setTitlePosition(DJChartOptions.EDGE_TOP)
				.setLineStyle(DJChartOptions.LINE_STYLE_DOTTED)
				.setLineWidth(1)
				.setLineColor(Color.DARK_GRAY).setPadding(5)
				.setKey((PropertyColumn) date).addSerie(infected)
				.setCircular(true).build();
		return chart;
	}

	public DynamicReport getPieReport() throws Exception {
		builder.addChart(createPieChart());
		return builder.build();
	}
	public DynamicReport getBarReport(int fromDate, int toDate) throws Exception {
		builder.addChart(createBarChart(Utils.getDateStringFromEpoch(fromDate), Utils.getDateStringFromEpoch(toDate)));
		return builder.build();
	}
	public void displayReport() throws Exception
	{

//		JasperPrint jp2 = DynamicJasperHelper.generateJasperPrint(
//				new Report().getPieReport(), new ClassicLayoutManager(),
//				summary);
//		JasperPrint jp1 = DynamicJasperHelper.generateJasperPrint(
//				new Report().getBarReport(), new ClassicLayoutManager(),
//				summary);

//		List<JRPrintPage> pages = jp2.getPages();
//		for (int j = 0; j < pages.size(); j++) {
//			JRPrintPage object = (JRPrintPage) pages.get(j);
//			jp1.addPage(object);
//
//		}
//		JasperViewer.viewReport(jp1, false);
	}
//	public void displayReportBetweenDates(java.util.Date fromDate, java.util.Date toDate) throws Exception
//	{
//		int from = (int)(fromDate.getTime()/1000);
//		int to = (int)(toDate.getTime()/1000);
//		List<Summary> summary = Query.getSummeryBetweenDates(from, to);
//		JasperPrint jp1 = DynamicJasperHelper.generateJasperPrint(
//				new Report().getBarReport(from, to), new ClassicLayoutManager(),
//				summary);
//		jp1.setName("COVID Report");
//		JasperViewer.viewReport(jp1, false);
//	}
	public void displaySimulatedReportBetweenDates(Date fromDate, Date toDate) throws Exception {
		int from = (int)(fromDate.getTime()/1000);
		int to = (int)(toDate.getTime()/1000);
		
		//sanitize date integer
		from -= from % Config.daySec;
		to -= to % Config.daySec;
		if(from >= to)
		{
			System.out.println("Wrong date range selected");
			return;
		}
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
			int diff = (minDate - from)/Config.daySec;
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
			int diff = (to - maxDate)/Config.daySec+1;
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
		for(int i = 1; i < data.size(); i++)
		{
			WorldDataTuple w = data.get(i);
			float p = data.get(i-1).getInfectedCount();
			w.setInfectionRate(Utils.round((w.getInfectedCount()-p)/p, 3));
		}
		JasperPrint jp1 = DynamicJasperHelper.generateJasperPrint(
				new Report().getBarReport(from, to), new ClassicLayoutManager(),
				data);
		jp1.setName("COVID Report");
		JasperViewer.viewReport(jp1, false);
	}
}
