
package report;

import java.awt.Color;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

public abstract class ReportBase {
	public static final String ColName_INFECTED = "Infected"; 
	public static final String ColName_RECOVERED = "Recovered";
	public static final String ColName_DATE = "Date";
	public static final String ColName_DEATHS = "Deaths";
	public static final String ColName_INFECTIONRATE = "Daily Infection rate";
	
	protected AbstractColumn date;
	protected AbstractColumn infected;
	protected AbstractColumn recovered;
	protected AbstractColumn died;
	protected AbstractColumn infectionRate;
	
	public ReportBase() {
		date = ColumnBuilder.getNew()
				.setColumnProperty("dateAsStr", String.class.getName())
				.setTitle(ColName_DATE).setWidth(70).build();
		infected = ColumnBuilder.getNew()
				.setColumnProperty("infectedCount", Integer.class.getName())
				.setTitle(ColName_INFECTED).setWidth(70).build();
		recovered = ColumnBuilder.getNew()
				.setColumnProperty("recoveredCount", Integer.class.getName())
				.setTitle(ColName_RECOVERED).setWidth(70).build();
		died = ColumnBuilder.getNew()
				.setColumnProperty("deathCount", Integer.class.getName())
				.setTitle(ColName_DEATHS).setWidth(70).build();
		infectionRate = ColumnBuilder.getNew()
				.setColumnProperty("infectionRate", Float.class.getName())
				.setTitle(ColName_INFECTIONRATE).setWidth(70).build();
	}

	public Style getHeaderStyle() {
		Style headerStyle = new Style();
		headerStyle.setFont(Font.VERDANA_MEDIUM_BOLD);
		headerStyle.setBorderBottom(Border.PEN_2_POINT());
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setBackgroundColor(Color.BLUE);
		headerStyle.setTextColor(Color.WHITE);
		headerStyle.setTransparency(Transparency.OPAQUE);
		return headerStyle;
	}
}
