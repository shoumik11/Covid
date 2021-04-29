

package database;
import utils.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import object.Patient;


public class BulkLoad {
	//	public static boolean insertFromCSV(String path, String tableName) throws ReflectiveOperationException, SQLException, IOException   
	//	{
	//		
	//		String line = "";  
	//		String splitBy = ",";  
	//
	//        Connection connection = Connector.connect();
	//        try 
	//        {
	//            Random rand = new Random();
	//            Statement stmt = connection.createStatement();
	//            // test several SQL operation
	//            // create table if not exists
	//            stmt.execute(
	//                "CREATE TABLE IF NOT EXISTS "+ tableName + "( id BIGINT NOT NULL IDENTITY,"
	//                + "patient_name VARCHAR(32),"
	//                + "home_location_lat float,"
	//                + "home_location_long float,"
	//                + "affected_on VARCHAR(12),"
	//                + "recovered_on VARCHAR(12),"
	//                + "is_affected bit,"
	//                + "is_isolated bit);");
	//            try   
	//    		{
	//            	String infDate, recoDate;
	//    			//parsing a CSV file into BufferedReader class constructor  
	//    			//BufferedReader br = new BufferedReader(new FileReader(path));  
	//    			//while ((line = br.readLine()) != null)
	//            	int rr = 100;
	//            	while (rr-- > 0)
	//    			{
	//    				infDate = Utils.getRandomDateAsString();
	//    				recoDate = Utils.getRandomRecoveryDateAsString(infDate);
	//    				stmt.executeUpdate(
	//    		                "INSERT INTO "+ tableName 
	//    		                + " (patient_name,"
	//    		                + " home_location_lat,"
	//	                		+ " home_location_long,"
	//	                		+ " affected_on,"
	//	                		+ " recovered_on,"
	//	                		+ " is_affected,"
	//	                		+ " is_isolated)"
	//	                		+ " VALUES("
	//    		                + "'"+ Utils.createRandomString(15, true)+ " "+ Utils.createRandomString(15, true) +"',"
	//    		                + Utils.getRandomLat()+","
	//    		                + Utils.getRandomLong()+","
	//    		                + "'" + infDate + "',"
	//    		                + "'" + recoDate + "',"
	//    		                + (recoDate.isEmpty()?1:0) + ","
	//	                		+ (recoDate.isEmpty()? ((int)Math.random()*10)%2: 0)
	//    		                + ")", Statement.RETURN_GENERATED_KEYS);
	//    			}  
	//    		}   
	//    		catch (Exception e)   
	//    		{  
	//    			e.printStackTrace();
	//    			Connector.disconnect(connection);
	//    			return false;
	//    		}
	//
	//            // query data
	//            ResultSet rs = stmt.executeQuery("select * from " + tableName + ";");
	//
	//            while (rs.next()) {
	//                // show all data
	//                System.out.println(//rs.getBigDecimal(1) + ","
	//                		rs.getString(2) + ","
	//                		+ rs.getFloat(3) + ","
	//                		+ rs.getFloat(4) + ","
	//                		+ rs.getString(5) + ","
	//                		+ rs.getString(6) + ","
	//                		+ rs.getBoolean(7) + ","
	//                		+ rs.getBoolean(8));
	//            }
	//        } finally {
	//            System.out.println("close connection");
	//            Connector.disconnect(connection);
	//        }
	//		return true;
	//	}

	public static boolean generateRandomData(String outputCSVFileName, String tableName, int count, String... inputCSV) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";  
		try 
		{
			List<String> randLineList = new LinkedList<String>();
			if(inputCSV != null && inputCSV.length > 0)
			{
				if(tableName == Config.TableName_PATIENTINFO)
				{
					RandomAccessFile f = new RandomAccessFile(inputCSV[0], "r");
					Random rand = new Random();

					for(int i = 0; i < count && f.length() > 1; ++i)
					{
						f.seek(rand.nextInt((int) (f.length() - 2)));
						f.readLine();
						line = f.readLine();
						while(randLineList.contains(line))
						{
							f.seek(rand.nextInt((int) (f.length() - 2)));
							f.readLine();
							line = f.readLine();
						}
						randLineList.add(line);
					}
					f.close();
				}
				else if(tableName == Config.TableName_RECOVERYRECORD)
				{
					BufferedReader br = new BufferedReader(new FileReader(inputCSV[0]));  
					while ((line = br.readLine()) != null)
					{
						String[] info = line.split(splitBy);
						if(Integer.parseInt(info[2]) == 0)
						{
							randLineList.add(info[0]+splitBy+info[1]+splitBy+info[5]);
						}
					}
					br.close();
					count = randLineList.size();
				}
			}
			File f = new File(outputCSVFileName);

			// create file if not exists
			if (!f.exists()) {
				f.createNewFile();
			}
			FileWriter fw = new FileWriter(f, false);
			String name;
			int id = 0, firstTestDate, lastTestDate;
			while (count-- > 0)
			{
				if(tableName == Config.TableName_PERSONINFO)
				{
					name = Utils.createRandomString(15, true)+ " "+ Utils.createRandomString(15, true);					
					line = id++ + splitBy 
							+name + splitBy
							+Utils.getRandomLat()+splitBy
							+Utils.getRandomLong();
				}
				else if(tableName == Config.TableName_PATIENTINFO)
				{
					String[] lineParts = randLineList.get(count).split(splitBy);
					firstTestDate = Utils.getEpochFromDateString(Utils.getRandomDateAsString());
					int testCount = Utils.getRandomTestCount();
					int is_infected = ((int)(Math.random()*10)%2);
					if(testCount == 1)
					{
						is_infected = 1;
						lastTestDate = firstTestDate; 
					}
					else
						lastTestDate = Utils.getEpochFromDateString(
								Utils.getRandomLastTestDateAsString(Utils.getDateStringFromEpoch(firstTestDate)));
					line =  lineParts[0] +splitBy
							+ lineParts[1]+splitBy
							+ is_infected+splitBy
							+ testCount +splitBy
							+ firstTestDate +splitBy
							+ lastTestDate;
				}
				else if(tableName == Config.TableName_RECOVERYRECORD)
				{
					line = randLineList.get(count);
				}
				fw.write(line+"\n");
			}
			fw.close();
		}   
		catch (Exception e)   
		{  
			e.printStackTrace();
			return false;
		}

		return true;
	}
	//	public static boolean insertFromCSV(String path, String tableName) throws ReflectiveOperationException, SQLException, IOException   
	//	{
	//
	//		String line = "";  
	//		String splitBy = ",";  
	//
	//		Connection connection = Connector.connect();
	//		try 
	//		{
	//			Statement stmt = connection.createStatement();
	//			// test several SQL operation
	//			// create table if not exists
	//			stmt.execute(
	//					"CREATE TABLE IF NOT EXISTS "+ tableName + "( id BIGINT NOT NULL IDENTITY,"
	//							+ "patient_name VARCHAR(32),"
	//							+ "home_location_lat float,"
	//							+ "home_location_long float,"
	//							+ "affected_on VARCHAR(12),"
	//							+ "recovered_on VARCHAR(12),"
	//							+ "is_affected bit,"
	//							+ "is_isolated bit);");
	//			try   
	//			{
	//				//parsing a CSV file into BufferedReader class constructor  
	//				BufferedReader br = new BufferedReader(new FileReader(path));  
	//				while ((line = br.readLine()) != null)
	//				{
	//					String[] info = line.split(splitBy);
	//					stmt.executeUpdate(
	//							"INSERT INTO "+ tableName 
	//							+ " (patient_name,"
	//							+ " home_location_lat,"
	//							+ " home_location_long,"
	//							+ " affected_on,"
	//							+ " recovered_on,"
	//							+ " is_affected,"
	//							+ " is_isolated)"
	//							+ " VALUES("
	//							+ "'"+ info[0] +"',"
	//							+ info[1] +","
	//							+ info[2] +","
	//							+ "'" + info[3] + "',"
	//							+ "'" + info[4] + "',"
	//							+ info[5] + ","
	//							+ info[6]
	//									+ ")", Statement.RETURN_GENERATED_KEYS);
	//					System.out.println(line);
	//				}  
	//			}   
	//			catch (Exception e)   
	//			{  
	//				e.printStackTrace();
	//				Connector.disconnect(connection);
	//				return false;
	//			}
	//		} finally {
	//			System.out.println("close connection");
	//			Connector.disconnect(connection);
	//		}
	//		return true;
	//	}
	public static boolean insertPopulationDataFromCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";  

		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_POPULATIONDATA);
			try   
			{
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader(path));
				br.readLine();//skip header
				while ((line = br.readLine()) != null)
				{
					if(line.contains("International"))
						continue;
					line = line.replaceAll(", ", " ").replaceAll("\"", "").replaceAll("\'", " ");
					String[] info = line.split(splitBy);
					System.out.println(line);
					stmt.executeUpdate(
							"INSERT IGNORE INTO " + Config.TableName_POPULATIONDATA  
							+ " VALUES("
							+ "'"+ info[1] +"',"
							+ "'"+ info[2] +"',"
							+ info[4] +")"
							, Statement.RETURN_GENERATED_KEYS);

				}
				br.close();
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
	public static boolean insertInfectedDataFromCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";  

		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_INFECTIONDATA);
			try   
			{
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader(path));
				line = br.readLine();//skip header
				line = line.replaceAll(", ", " ").replaceAll("\"", "").replaceAll("\'", " ");
				String [][] data = new String[250000][4];
				String[] colNames = line.split(",");
				int colLim = colNames.length - 1;
				int totalRows = 0;
				System.out.println("colNames = " + colLim);

				while ((line = br.readLine()) != null)
				{
					System.out.println(line);
					String[] info = line.split(splitBy);
					int limit = info.length - 1;
					String date = info[0];
					if(date == null)
						continue;
					//System.out.println("info = " + info.length);
					int newLim1 = totalRows+colLim;
					int newLim2 = totalRows+limit;
					for (int j = totalRows, k = 1; j < newLim1 && k < colNames.length; j++, k++)
					{
						data[j][0] = date;
						data[j][1] = colNames[k];
					}
					for (int j = totalRows, k = 1; j < newLim2 && k < info.length; j++, k++)
					{
						data[j][2] = info[k];
					}
					totalRows += colLim;
				}
				//				for(int i = 0; i < totalRows; i++)
				//				{
				//					System.out.println();
				//					for(int j = 0; j < 4; j++)
				//						System.out.print(data[i][j] + ",");
				//				}
				for(int i = 0; i < totalRows; i++)
				{
					//					if(data[i][0] == null)
					//						continue;
					String Q = "INSERT IGNORE INTO " + Config.TableName_INFECTIONDATA  
							+ " VALUES("
							+ Utils.getEpochFromDateString(data[i][0]) +","
							+ "'"+ data[i][1] +"',"
							+ (data[i][2] == null || data[i][2].isEmpty()?0:Integer.parseInt(data[i][2])) +","
							+ "0);";
					//System.out.println("Q = " + Q);
					stmt.executeUpdate(
							Q
							, Statement.RETURN_GENERATED_KEYS);
				}
				br.close();
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

	public static boolean insertPersonDataFromCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";

		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_PERSONINFO);
			try   
			{
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader(path));  
				while ((line = br.readLine()) != null)
				{
					String[] info = line.split(splitBy);
					stmt.executeUpdate(
							"INSERT IGNORE INTO " + Config.TableName_PERSONINFO  
							+ " VALUES("
							+ info[0]+","
							+ "'"+ info[1] +"',"
							+ info[2] +","
							+ info[3] +")"
							, Statement.RETURN_GENERATED_KEYS);
					System.out.println(line);
				}
				br.close();
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

	public static boolean insertPatientDataFromCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";  

		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_PATIENTINFO);
			try   
			{
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader(path));  
				List<Patient> listOfRecoveredPatient = new LinkedList<Patient>();
				while ((line = br.readLine()) != null)
				{
					String[] info = line.split(splitBy);
					stmt.executeUpdate(
							"INSERT IGNORE INTO " + Config.TableName_PATIENTINFO
							+ " VALUES("
							+ info[0] +","
							+ "'"+ info[1] +"',"
							+ info[2] +","
							+ info[3] +","
							+ info[4] +","
							+ info[5] +")"
							, Statement.RETURN_GENERATED_KEYS);
					if(Integer.parseInt(info[2]) == 0)
					{
						Patient p = new Patient(Integer.parseInt(info[0]), info[1]);
						p.setLastTestDate(Integer.parseInt(info[5]));
						listOfRecoveredPatient.add(p);
					}
					System.out.println(line);
				}
				br.close();
				Statement s = connection.createStatement();
				s.execute(Config.CreateStr_RECOVERYRECORD);

				for (int i = 0; i < listOfRecoveredPatient.size(); i++) {
					Patient patient = listOfRecoveredPatient.get(i);
					try   
					{
						s.executeUpdate(
								"INSERT IGNORE INTO " + Config.TableName_RECOVERYRECORD
								+ " VALUES("
								+ patient.getId() +","
								+ "'"+ patient.getName() +"',"
								+ patient.getLastTestDate() +")"
								, Statement.RETURN_GENERATED_KEYS);
						System.out.println(patient.toString());
					}   
					catch (Exception e)   
					{  
						e.printStackTrace();
						Connector.disconnect(connection);
						return false;
					}
				}

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

	public static boolean insertRecoveredPatientData(Patient patient, Connection...connections) throws ReflectiveOperationException, SQLException, IOException   
	{
		Connection connection;
		if(connections != null && connections.length > 0)
			connection = connections[0];
		else
			connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_RECOVERYRECORD);
			try   
			{
				stmt.executeUpdate(
						"INSERT IGNORE INTO " + Config.TableName_RECOVERYRECORD
						+ " VALUES("
						+ patient.getId() +","
						+ "'"+ patient.getName() +"',"
						+ patient.getLastTestDate() +")"
						, Statement.RETURN_GENERATED_KEYS);
				System.out.println(patient.toString());
			}   
			catch (Exception e)   
			{  
				e.printStackTrace();
				Connector.disconnect(connection);
				return false;
			}
		} finally {
			System.out.println("close connection");
			if(connections == null)
				Connector.disconnect(connection);
		}
		return true;
	}

	public static boolean insertRandomPatientDataFromWorldCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";  
		int todate = (int)((new Date()).getTime()/1000);
		final int monthEpochDiff = Config.daySec*4;
		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			//Statement stmt2 = connection.createStatement();
			//Statement stmt3 = connection.createStatement();
			stmt.execute(Config.CreateStr_PATIENTINFO);
			stmt.execute(Config.CreateStr_RECOVERYRECORD);
			stmt.execute(Config.CreateStr_DEATHINFO);
			try   
			{
				int currentInfected = 0;
				int currentRecovered = 0;
				int currentDeath = 0;
				BufferedReader br = new BufferedReader(new FileReader(path));
				line = br.readLine();// skip the header
				while ((line = br.readLine()) != null)
				{
					String[] info = line.split(splitBy);
					int date = Utils.getEpochFromDateString(info[0]);
					if(todate - date > monthEpochDiff)
					{
						currentInfected = Integer.parseInt(info[1]);
						currentRecovered = Integer.parseInt(info[2]);
						currentDeath = Integer.parseInt(info[3]);
						continue;
					}
					int newInfected = Integer.parseInt(info[1]) - currentInfected;
					int newRecovered = Integer.parseInt(info[2]) - currentRecovered;
					int newDeath = Integer.parseInt(info[3]) - currentDeath;
					//int max = Math.max(newInfected, Math.max(newRecovered, newDeath));
					for(int i = 0; i < newInfected; i++)
					{
						stmt.executeUpdate("INSERT IGNORE INTO "
								+ Config.TableName_PATIENTINFO
								+ " (name, is_infected, test_count, first_test_date, last_test_date) VALUES("
								+ "'"+ Utils.createRandomString(15, true)+ " " + Utils.createRandomString(15, true) +"', "
								+ 1 +", "
								+ Utils.getRandomTestCount() +", "
								+ date +", "
								+ date +")", Statement.RETURN_GENERATED_KEYS);
					}
					for(int i = 0; i < newRecovered; i++)
					{
						stmt.executeUpdate(
								"INSERT IGNORE INTO " + Config.TableName_RECOVERYRECORD
								+ " (name, recovery_date) VALUES("
								+ "'"+ Utils.createRandomString(15, true)+ " " + Utils.createRandomString(15, true) +"',"
								+ date +")"
								, Statement.RETURN_GENERATED_KEYS);
					}
					for(int i = 0; i < newDeath; i++)
					{
						stmt.executeUpdate(
								"INSERT IGNORE INTO " + Config.TableName_DEATHINFO
								+ " (name, date) VALUES("
								+ "'"+ Utils.createRandomString(15, true)+ " " + Utils.createRandomString(15, true) +"',"
								+ date +")"
								, Statement.RETURN_GENERATED_KEYS);
					}
					currentInfected += newInfected;
					currentRecovered += newRecovered;
					currentDeath += newDeath;
					System.out.println(line);
				}
				br.close();
				stmt.execute("CREATE UNIQUE INDEX "+Config.TableName_PATIENTINFO+"_INDEX ON "
						+Config.TableName_PATIENTINFO+" (ID)");
				stmt.execute("CREATE UNIQUE INDEX "+Config.TableName_RECOVERYRECORD+"_INDEX ON "
						+Config.TableName_RECOVERYRECORD+" (ID)");
				stmt.execute("CREATE UNIQUE INDEX "+Config.TableName_DEATHINFO+"_INDEX ON "
						+Config.TableName_DEATHINFO+" (ID)");
				stmt.execute("CREATE UNIQUE INDEX "+Config.TableName_WORLDDATA+"_INDEX ON "
						+Config.TableName_WORLDDATA+" (Date)");
				stmt.execute("CREATE UNIQUE INDEX "+Config.TableName_CONFIGINFO+"_INDEX ON "
						+Config.TableName_CONFIGINFO+" (variable)");
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

	public static boolean insertWorldDataFromCSV(String path) throws ReflectiveOperationException, SQLException, IOException   
	{

		String line = "";  
		String splitBy = ",";

		Connection connection = Connector.connect();
		try 
		{
			Statement stmt = connection.createStatement();
			// test several SQL operation
			// create table if not exists
			stmt.execute(Config.CreateStr_WORLDDATA);
			try   
			{
				//parsing a CSV file into BufferedReader class constructor  
				BufferedReader br = new BufferedReader(new FileReader(path));  
				line = br.readLine();//skip the header
				line = br.readLine();//skip incomplete data
				while ((line = br.readLine()) != null)
				{
					line = line.replaceAll("\"", "").replaceAll("\'", " ");
					String[] info = line.split(splitBy);
					stmt.executeUpdate(
							"INSERT IGNORE INTO " + Config.TableName_WORLDDATA  
							+ " VALUES("
							+ Utils.getEpochFromDateString(info[0])+","
							+ info[1] +","
							+ info[2] +","
							+ info[3] +","
							+ "'" + ((info[4] == null || info[4].isEmpty())?0:info[4]) +"')"
							, Statement.RETURN_GENERATED_KEYS);
					System.out.println(line);
				}
				br.close();
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
}