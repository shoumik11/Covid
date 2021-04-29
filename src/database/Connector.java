
package database;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.hsqldb.Server;

public class Connector {
	private static Server _server = null;
	private static Connection _connection = null;
	private static boolean isMysqlCompatible = false; 
	static String dbName = "COVID_DB";
	public static String getDBName() { return dbName; }
	/**
	 * create a writer, log hsqldb server info to a file
	 * @param logFileName String, the log file name
	 * @param append boolean, true: append new content; false: clear old content 
	 * @param autoFlush boolean, true: auto flush; false: not auto flush
	 * @return PrintWriter
	 * @throws IOException
	 */
	private static PrintWriter createLogWriter (String logFileName,
			boolean append, boolean autoFlush)
					throws IOException {
		File f = new File(logFileName);

		// create file if not exists
		if (!f.exists()) {
			String logFilePath = f.getAbsolutePath();
			if(logFilePath == null)
			{
				System.out.println("invalid logFilePath: "+logFilePath);
				return null;
			}
			System.out.println("invalid logFileName: "+logFileName+logFilePath);
			// create parent folders
			//File folder = new File(logFilePath.split("[\\/]").substring(0, logFilePath.indexOf(logFileName)));
			//folder.mkdirs();

			// create file
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f, append);
		return new PrintWriter(fw, autoFlush);
	}
	/**
	 * get a db connection
	 * @param driverName
	 * @param dbUrl
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static Connection getConnection (String dbUrl,
			String userName, String password)
					throws SQLException, ClassNotFoundException {
		if (_connection == null
				|| _connection.isClosed()) {
			// Getting a connection to the newly started database
			Class.forName("org.hsqldb.jdbcDriver");
			// Default user of the HSQLDB is 'sa'
			// with an empty password
			return DriverManager.getConnection(dbUrl
					,userName , password);
		} else {
			return _connection;
		}
	}
//	public static void main(String[] args)
//			throws ClassNotFoundException, SQLException, IOException {
//		try {
//			BulkLoad.insertFromCSV("C:\\Users\\iamsr\\eclipse-workspace\\SE_Project\\csv_input.txt",
//					Config.TableName_PATIENTINFO);
//			//BulkLoad.insertFromCSV("", "PatientInfo");
//		} catch (ReflectiveOperationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static Connection connect()
			throws ClassNotFoundException, SQLException, IOException {
		
		String path = dbName;
		PrintWriter logWriter =
				createLogWriter(dbName+"_"+path+".log",
						true, true);
		// start the hsqldb server
		_server = HSQLDB.startServer(dbName, path, logWriter);
		Connection connection =
				getConnection("jdbc:hsqldb:hsql://localhost/"+dbName, "sa", "");
		if(!isMysqlCompatible)
		{
			Statement stmt = connection.createStatement();
			stmt.execute("SET DATABASE SQL SYNTAX MYS TRUE;");
		}
		return connection;
	}
	public static void disconnect(Connection conn)
			throws ClassNotFoundException, SQLException, IOException {
		// Closing the connection
		if (conn != null) {
			conn.close();
		}
		// Stop the server
		if (_server != null) {
			HSQLDB.stopServer(dbName);
		}
	}
}