
package object;

import database.Config;
import utils.Utils;

public class WorldDataTuple implements Comparable<WorldDataTuple> {
	private String dateAsStr = "";
	private int infectedCount = 0;
	private int recoveredCount = 0;
	private int deathCount = 0;
	private float infectionRate = 0;
	private int date = 0;
	
	public WorldDataTuple(int date, int infectedCount, int recoveredCount, int deathCount, float infectionRate)
	{
		super();
		this.date = date;
		this.dateAsStr = Utils.getDateStringFromEpoch(date);
		this.infectedCount = infectedCount;
		this.recoveredCount = recoveredCount;
		this.deathCount = deathCount;
		this.infectionRate = infectionRate;
	}
	public WorldDataTuple(String dateAsStr, int infectedCount, int recoveredCount, int deathCount)
	{
		super();
		this.date = Utils.getEpochFromDateString(dateAsStr);
		this.dateAsStr = dateAsStr;
		this.infectedCount = infectedCount;
		this.recoveredCount = recoveredCount;
		this.deathCount = deathCount;
		this.infectionRate = (float)Config.getInfectionRate();
	}
	public WorldDataTuple(int date, int infectedCount, int recoveredCount, int deathCount)
	{
		super();
		this.date = date;
		this.dateAsStr = Utils.getDateStringFromEpoch(date);
		this.infectedCount = infectedCount;
		this.recoveredCount = recoveredCount;
		this.deathCount = deathCount;
		this.infectionRate = (float)Config.getInfectionRate();
	}
	public void setDateAsStr(String dateAsStr) { this.dateAsStr = dateAsStr; this.date = Utils.getEpochFromDateString(dateAsStr); }
	public String getDateAsStr() { return this.dateAsStr; }
	public int getDate() { return this.date; }
	public int getInfectedCount() { return this.infectedCount; }
	public int getRecoveredCount() { return this.recoveredCount; }
	public int getDeathCount() { return this.deathCount; }
	public float getInfectionRate() { return this.infectionRate; }
	public void setDate(int date) { this.date = date; this.dateAsStr = Utils.getDateStringFromEpoch(date); }
	public void setInfectedCount(int infectedCount) { this.infectedCount = infectedCount; }
	public void setRecoveredCount(int recoveredCount) { this.recoveredCount = recoveredCount; }
	public void setDeathCount(int deathCount) { this.deathCount = deathCount; }
	public void setInfectionRate(float infectionRate) { this.infectionRate = infectionRate; }
	@Override
	public int compareTo(WorldDataTuple arg0) {
		return this.date - arg0.date;
	}
	
}
