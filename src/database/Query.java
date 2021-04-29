
package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import object.*;
import utils.Utils;
public class Query {
//	public static List<Patient> getPatientDataByDate(String fromTable, int date) throws Exception, SQLException, IOException
//	{
//		List<Patient> pList = new ArrayList<Patient>();
//		Connection connection = Connector.connect();
//		String query = "";
//		if(fromTable == Config.TableName_PATIENTINFO)
//			query = "select * from " + fromTable + " where is_infected = 1 "
//					+ "AND first_test_date = "+date+";";
//		else
//			query = "select * from " + fromTable + " where "
//					+ " recovery_date = "+date+";";
//		try 
//		{
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			System.out.println("Q = " + query);
//			while (rs.next()) {
//				Patient p = new Patient(rs.getInt(1), rs.getString(2));
//				if(fromTable == Config.TableName_PATIENTINFO)
//				{
//					p.setTestCount(rs.getInt(4));
//					p.setFirstTestDate(rs.getInt(5));
//					p.setLastTestDate(rs.getInt(6));
//				}
//				else
//				{
//					p.setInfected(false);
//					p.setLastTestDate(rs.getInt(3));
//				}
//				pList.add(p);
//				//				// show all data
//				//				System.out.println(//rs.getBigDecimal(1) + ","
//				//						rs.getString(2) + ","
//				//						+ rs.getFloat(3) + ","
//				//						+ rs.getFloat(4) + ","
//				//						+ rs.getString(5) + ","
//				//						+ rs.getString(6) + ","
//				//						+ rs.getBoolean(7) + ","
//				//						+ rs.getBoolean(8));
//				p.toString();
//			}
//		} finally {
//			System.out.println("close connection");
//			Connector.disconnect(connection);
//		}
//		return pList;
//	}
//
//	public static List<Patient> getPatientDataBetweenDates(String fromTable, int fromDate, int toDate) throws Exception, SQLException, IOException
//	{
//		List<Patient> pList = new ArrayList<Patient>();
//		Connection connection = Connector.connect();
//		String query = "";
//		if(fromDate > toDate)
//			return pList;
//		if(fromTable == Config.TableName_PATIENTINFO)
//			query = "select * from " + fromTable + " where is_infected = 1"
//					+" AND first_test_date >= "+fromDate
//					+" AND first_test_date <= "+ toDate
//					+" ORDER BY first_test_date;";
//		else
//			query = "select * from " + fromTable + " where "
//					+ " recovery_date >= "+fromDate
//					+" AND recovery_date <= " + toDate +" ORDER BY recovery_date;";
//		try 
//		{
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			System.out.println("Q = " + query);
//			while (rs.next()) {
//				Patient p = new Patient(rs.getInt(1), rs.getString(2));
//				if(fromTable == Config.TableName_PATIENTINFO)
//				{
//					p.setTestCount(rs.getInt(4));
//					p.setFirstTestDate(rs.getInt(5));
//					p.setLastTestDate(rs.getInt(6));
//				}
//				else
//				{
//					p.setInfected(false);
//					p.setLastTestDate(rs.getInt(3));
//				}
//				pList.add(p);
//				//				// show all data
//				//				System.out.println(//rs.getBigDecimal(1) + ","
//				//						rs.getString(2) + ","
//				//						+ rs.getFloat(3) + ","
//				//						+ rs.getFloat(4) + ","
//				//						+ rs.getString(5) + ","
//				//						+ rs.getString(6) + ","
//				//						+ rs.getBoolean(7) + ","
//				//						+ rs.getBoolean(8));
//				p.toString();
//			}
//		} finally {
//			System.out.println("close connection");
//			Connector.disconnect(connection);
//		}
//		return pList;
//	}
//
//	public static List<Summary> getSummeryBetweenDates(int fromDate, int toDate) throws Exception, SQLException, IOException
//	{
//		List<Summary> pList = new ArrayList<Summary>();
//		Connection connection = Connector.connect();
//		String query = "select t1.date, t1.total, t2.total from "
//				+ " (select first_test_date date, count(*) total from "+ Config.TableName_PATIENTINFO
//				+ " where first_test_date >= "+fromDate
//				+ " AND first_test_date <= "+toDate
//				+ " group by first_test_date order by first_test_date) t1 left join ("
//				+ " select recovery_date date, count(*) total from " + Config.TableName_RECOVERYRECORD
//				+ " where recovery_date >= "+fromDate
//				+ " AND recovery_date <= "+toDate
//				+ " group by recovery_date order by recovery_date) t2"
//				+" on t1.date = t2.date";
//		if(fromDate > toDate)
//			return pList;
//		try 
//		{
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			System.out.println("Q = " + query);
//			while (rs.next()) {
//				Summary s = new Summary(rs.getInt(1), rs.getInt(2), rs.getInt(3));
//				pList.add(s);
//				s.toString();
//			}
//		} finally {
//			System.out.println("close connection");
//			Connector.disconnect(connection);
//		}
//		return pList;
//	}
//	public static int getCount(String tableName) throws Exception, SQLException, IOException
//	{
//		int ret = 0;
//		Connection connection = Connector.connect();
//		String query = "select count(*) from " + tableName;
//		if(tableName == Config.TableName_PATIENTINFO)
//			query += " where is_infected = 1";
//		try 
//		{
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			while(rs.next())
//				ret = rs.getInt(1);
//		} finally {
//			System.out.println("close connection");
//			Connector.disconnect(connection);
//		}
//		return ret;
//	}
	public static int getNextIDForPatient() throws Exception, SQLException, IOException
	{
		int ret = 0;
		Connection connection = Connector.connect();
		String query = "select MAX(id) from " + Config.TableName_PATIENTINFO;
		try 
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			//System.out.println("Q = " + query);
			while (rs.next()) {
				ret = rs.getInt(1) + 1;
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return ret;
	}
	public static int getInfectedDataByDate(int date) throws Exception, SQLException, IOException
	{
		int ret = 0;
		Connection connection = Connector.connect();
		String query = "select infected_count from " + Config.TableName_INFECTIONDATA
				+ " where country_name = 'United States' and date = " + date;
		try 
		{
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			//System.out.println("Q = " + query);
			while (rs.next()) {
				ret = rs.getInt(1);
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return ret;
	}
	public static int[] getInfectedDataByDateRange(int fromDate, int toDate) throws Exception, SQLException, IOException
	{
		List<Integer> ret = new ArrayList<Integer>();
		Connection connection = Connector.connect();
		String query = "select infected_count, date from " + Config.TableName_INFECTIONDATA
				+ " where country_name = 'United States' and date >= " + fromDate
				+ " and date <= " + toDate + " order by date;";
		try 
		{
			//System.out.println("Q = " + query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				//System.out.println("R = "+rs.getInt(1));
				ret.add(rs.getInt(1));
			}

		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return ret.stream().filter(Objects::nonNull).mapToInt(Integer::intValue).toArray();
	}
	public static List<WorldDataTuple> getWorldDataByDateRange(int fromDate, int toDate) 
			throws Exception, SQLException, IOException
	{
		List<WorldDataTuple> ret = new LinkedList<WorldDataTuple>();
		Connection connection = Connector.connect();
		String query = "select Date, Confirmed, Recovered, Deaths, Increase_rate from " + Config.TableName_WORLDDATA
				+ " where Date >= " + fromDate
				+ " and Date <= " + toDate + " order by date;";
		try 
		{
			//System.out.println("Q = " + query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				WorldDataTuple w = new WorldDataTuple(rs.getInt(1), rs.getInt(2), 
						rs.getInt(3), rs.getInt(4), Float.parseFloat(rs.getString(4)));
				//System.out.println("R = "+rs.getInt(1));
				ret.add(w);
			}
		}
		catch(Exception e)
		{
			System.out.println("Import data first!!\nDownload data from "
					+ "https://raw.githubusercontent.com/datasets/covid-19/master/data/worldwide-aggregated.csv");
		}
		finally {

			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return ret;
	}
	public static Patient getPatientInfoByID(int id) 
			throws Exception, SQLException, IOException
	{
		Connection connection = Connector.connect();
		String query = "select * from " + Config.TableName_PATIENTINFO
				+ " where id = " + id;
		try 
		{
			//System.out.println("Q = " + query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				Patient p = new Patient(id, rs.getString(2));
				p.setLastTestDate(rs.getInt(6));
				return p;
			}
		}
		catch(Exception e)
		{
			System.out.println("Import data first!!\nDownload data from "
					+ "https://raw.githubusercontent.com/datasets/covid-19/master/data/worldwide-aggregated.csv");
		}
		finally {

			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return null;
	}
	public static boolean insertPatientData(Patient p) throws ReflectiveOperationException, SQLException, IOException   
	{
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			stmt.execute(Config.CreateStr_PATIENTINFO);
			try   
			{
				stmt.executeUpdate("INSERT IGNORE INTO "
						+ Config.TableName_PATIENTINFO
						+ " (name, is_infected, test_count, first_test_date, last_test_date) VALUES("
						+ "'"+ p.getName() +"', "
						+ 1 +", "
						+ 1 +", "
						+ p.getLastTestDate() +", "
						+ p.getLastTestDate() +")", Statement.RETURN_GENERATED_KEYS);
			}   
			catch (Exception e)   
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
				return false;
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return true;
	}
	public static boolean updateUserCredential(String username, String hash) throws Exception, SQLException, IOException
	{
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			try   
			{
				String query = "update " + Config.TableName_CONFIGINFO 
						+ " set data='"+hash+"'" 
						+ " where variable='user_"+ username +"'";
				stmt.executeUpdate(query);
				return true;
			}   
			catch (Exception e)
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return false;
	}
	public static boolean updatePatientData(Patient p) throws ReflectiveOperationException, SQLException, IOException   
	{
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			stmt.execute(Config.CreateStr_PATIENTINFO);
			try   
			{
				stmt.executeUpdate("UPDATE "
						+ Config.TableName_PATIENTINFO
						+ " set name='" + p.getName() +"',"
						+ " last_test_date="+ p.getLastTestDate()
						+ " where id="+p.getId()
						, Statement.RETURN_GENERATED_KEYS);
			}   
			catch (Exception e)   
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
				return false;
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return true;
	}
	public static void setDefaultConfig() throws Exception, SQLException, IOException
	{
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			//stmt.executeUpdate("DROP TABLE " + Config.TableName_CONFIGINFO);
			stmt.execute(Config.CreateStr_CONFIGINFO);
			try   
			{
				String query = "select * from " + Config.TableName_CONFIGINFO;
				ResultSet rs = stmt.executeQuery(query);
				boolean isPassOk = false, isRateOk = false;
				while (rs.next()) {
					System.out.println(rs.getString(1));
					if(rs.getString(1).equals("infectionRate"))
						isRateOk = true;
					else if(rs.getString(1).equals("user_admin"))
						isPassOk = true;
					if(isPassOk && isRateOk)
						break;
				}
				if(!(isPassOk && isRateOk))
				{
					System.out.println("Resetting config!!");
					stmt.executeUpdate(
							"MERGE INTO " + Config.TableName_CONFIGINFO  
							+ " AS t USING (VALUES('infectionRate', '0.02')) AS vals(a,b) ON t.variable = vals.a "
							+ " WHEN MATCHED THEN UPDATE SET t.data=vals.b "
							+ " WHEN NOT MATCHED THEN INSERT VALUES vals.a, vals.b;"
							, Statement.RETURN_GENERATED_KEYS);
					stmt.executeUpdate(
							"MERGE INTO " + Config.TableName_CONFIGINFO  
							+ " AS t USING (VALUES('user_admin', '"+ Utils.encryptString("admin") +"')) AS vals(a,b) ON t.variable = vals.a "
							+ " WHEN MATCHED THEN UPDATE SET t.data=vals.b "
							+ " WHEN NOT MATCHED THEN INSERT VALUES vals.a, vals.b;"
							, Statement.RETURN_GENERATED_KEYS);
				}
			}   
			catch (Exception e)
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
	}
	public static boolean isValidUserCredential(String username, String hash) throws Exception, SQLException, IOException
	{
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			try   
			{
				String query = "select data from " + Config.TableName_CONFIGINFO 
						+ " where variable='user_"+ username +"'";
				ResultSet rs = stmt.executeQuery(query);

				while (rs.next()) {
					if(hash.equals(rs.getString(1)))
						return true;
				}
			}   
			catch (Exception e)
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		return false;
	}
	public static void setInfectionRate(double rate) throws Exception, SQLException, IOException
	{
		if(rate == Config.getInfectionRate())
			return;
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			stmt.execute(Config.CreateStr_CONFIGINFO);
			try   
			{
				stmt.executeUpdate(
						"MERGE INTO " + Config.TableName_CONFIGINFO  
						+ " AS t USING (VALUES('infectionRate', '"+rate+"')) AS vals(a,b) ON t.variable = vals.a "
						+ " WHEN MATCHED THEN UPDATE SET t.data=vals.b "
						+ " WHEN NOT MATCHED THEN INSERT VALUES vals.a, vals.b;"
						, Statement.RETURN_GENERATED_KEYS);
				//System.out.println(rate);
			}   
			catch (Exception e)   
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
			}
		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
	}
	public static double getInfectionRate() throws Exception, SQLException, IOException
	{
		double rate = 0.02;
		Connection connection = Connector.connect();
		String query = "select data from " + Config.TableName_CONFIGINFO
				+ " where variable = 'infectionRate';";
		try 
		{
			//System.out.println("Q = " + query);
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				rate = Double.parseDouble(rs.getString(1));
			}

		} finally {
			System.out.println("close connection");
			Connector.disconnect(connection);
		}
		//System.out.println("getInfectionRate = " + rate);
		return rate;
	}
}
