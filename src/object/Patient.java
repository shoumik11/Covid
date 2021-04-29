
package object;

import java.time.LocalDate;

import utils.Utils;

public class Patient extends Person {
	boolean isInfected;
	int testCount;
	int firstTestDate;// = LocalDate.parse("2019-03-29")
	int lastTestDate;
	public Patient(int id, String name) {
		super(id, name);
		this.isInfected = true;
		this.firstTestDate = this.lastTestDate = (int)(LocalDate.now().toEpochDay() / 1000);
		this.testCount = 1;
	}
	public Patient(int id, String name, int firstTestDate) {
		super(id, name);
		this.isInfected = true;
		this.firstTestDate = this.lastTestDate = firstTestDate;
		this.testCount = 1;
	}
	public int getFirstTestDate()
	{
		return this.firstTestDate;
	}
	public int getLastTestDate()
	{
		return this.lastTestDate;
	}
	public void setLastTestDate(int epoch)
	{
		this.lastTestDate = epoch;
	}
	public void setInfected(boolean isInfected)
	{
		this.isInfected = isInfected;
	}
	public void setFirstTestDate(int date) { this.firstTestDate = date; }
	public void setTestCount(int c) { this.testCount = c; }
	public void increaseTestCount() { this.testCount++ ;}
	public String toString()
	{
		return "Patient# "+ this.getId() +", " + this.getName()+", "+ this.isInfected+", "
				+ this.testCount+", "+ Utils.getDateStringFromEpoch(this.firstTestDate)
				+", "+ Utils.getDateStringFromEpoch(this.lastTestDate);
	}
}
