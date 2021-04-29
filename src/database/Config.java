
package database;

public final class Config {	
	private static boolean isAuthenticationComplete = false;
	public final static String TableName_PATIENTINFO = "PatientInfo";
	public final static String TableName_PERSONINFO = "PersonInfo";
	public final static String TableName_RECOVERYRECORD = "RecoveryRecord";
	public final static String TableName_DEATHINFO = "DeathInfo";
	public final static String TableName_INFECTIONDATA = "InfectionData";
	public final static String TableName_POPULATIONDATA = "PopulationData";
	public final static String TableName_WORLDDATA = "WorldData";
	public final static String TableName_CONFIGINFO = "ConfigInfo";
	public final static String CreateStr_PATIENTINFO = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_PATIENTINFO +"("
			+ "id INTEGER IDENTITY PRIMARY KEY,"
			+ "name VARCHAR(32),"
			+ "is_infected bit,"
			+ "test_count int,"
			+ "first_test_date INT,"
			+ "last_test_date INT)";
	public final static String CreateStr_PERSONINFO = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_PERSONINFO +"("
			+ "id INT NOT NULL,"
			+ "name VARCHAR(32),"
			+ "home_location_lat float,"
			+ "home_location_long float)";
	public final static String CreateStr_RECOVERYRECORD = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_RECOVERYRECORD +"("
			+ "id INTEGER IDENTITY PRIMARY KEY,"
			+ "name VARCHAR(32),"
			+ "recovery_date INT)";
	public final static String CreateStr_DEATHINFO = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_DEATHINFO +"("
			+ "id INTEGER IDENTITY PRIMARY KEY,"
			+ "name VARCHAR(32),"
			+ "date INT)";
	public final static String CreateStr_INFECTIONDATA = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_INFECTIONDATA +"("
			+ "date INT NOT NULL ,"
			+ "country_name VARCHAR(32) NOT NULL,"
			+ "infected_count INT,"
			+ "death_count INT)";
	public final static String CreateStr_POPULATIONDATA = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_POPULATIONDATA +"("
			+ "country_name VARCHAR(32) NOT NULL UNIQUE ,"
			+ "continent VARCHAR(20) NOT NULL,"
			+ "population INT)";
	public final static String CreateStr_WORLDDATA = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_WORLDDATA +"("
			+ "Date INT NOT NULL UNIQUE, "
			+ "Confirmed INT,"
			+ "Recovered INT,"
			+ "Deaths INT,"
			+ "Increase_rate VARCHAR(32))";
	public final static String CreateStr_CONFIGINFO = "CREATE TABLE IF NOT EXISTS "
			+ Config.TableName_CONFIGINFO +"("
			+ "variable VARCHAR(32) NOT NULL UNIQUE, "
			+ "data VARCHAR(255) NOT NULL)";
	private static double infectionRate = 0.02;
	public final static int daySec = 86400;
	public static String getCreateStrByTableName(String tableName)
	{
		String ret = "";
		if(tableName == TableName_PATIENTINFO)
			ret = CreateStr_PATIENTINFO;
		else if(tableName == TableName_PERSONINFO)
			ret = CreateStr_PERSONINFO;
		else if(tableName == TableName_RECOVERYRECORD)
			ret = CreateStr_RECOVERYRECORD;
		return ret;
	}
	public static double getInfectionRate() {
		return infectionRate;
	}
	public static void setInfectionRate(double infectionRate) {
		Config.infectionRate = infectionRate;
	}
	public static boolean getAuthenticationStatus() { return Config.isAuthenticationComplete; }
	public static void setAuthenticationStatus(boolean status) { Config.isAuthenticationComplete = status; }
}
