package com.gtnexus.html5.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JTextArea;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


public class DbLogger {

	private static DbLogger instance = null;
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/Logs";
	private static final String CONFIG_FILE = "config.ini";
	private int id;
	private int idMain;
	private Connection con = null;
	private static String USER;
	private static String PASSWORD;
	private boolean enabled = true;

	private static final String PAGE_TABLE = "Page";
	private static final String ID = "ID";
	private static final String PATH = "Path";
	private static final String IS_INCLUDE = "IncludeFile";
	private static final String CONVERTED_DATE = "ConvertedDate";
	private static final String CONVERTED_STATUS = "Converted";
	
	private static final String INCLUDE_FILE_TABLE = "IncludeFiles";
	private static final String PARENT_ID = "ParentID";

	private static final String CHANGE_LOG_TABLE = "ChangeLog";
	private static final String SOURCE = "Source";
	private static final String FIX = "Fix";
	private static final String TAG = "Tag";
	private static final String LINE = "Line";
	
	private static final String ERRORS_TABLE = "Errors";
	private static final String ERROR_MESSAGE="Error";
	private static final String ERROR_TYPE = "ErrorType";
	private static final String LAST_CONVERTED_LINE ="lastConvertedLine";
	private PreparedStatement statement;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdMain() {
		return idMain;
	}

	public void setIdMain(int idMain) {
		this.idMain = idMain;
	}

	private DbLogger() {
		//initialize();
		enabled=true;
	}

	public static synchronized DbLogger getInstance() {
		
		if (instance == null) {
			instance = new DbLogger();
		}
		return instance;
	}

	// initialize db connection
	public void initialize() {

		readCredentials();
		setCon();


	}

	private synchronized void setCon() {
		if (con == null) {

			try {

				Class.forName(JDBC_DRIVER).newInstance();

				con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
	}

	private void readCredentials() {

		StringBuilder str = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				str.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		USER = str.substring(str.indexOf("<USERNAME>") + 10,
				str.indexOf("</USERNAME>"));
		PASSWORD = str.substring(str.lastIndexOf("<PASSWORD>") + 10,
				str.lastIndexOf("</PASSWORD>"));

	}

	private void revokeCon() {
		con = null;
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	public void insertPage(String filepath, boolean isIncludeFile) {

		if (isEnabled()) {

			String query = "INSERT INTO " + PAGE_TABLE + "(" + PATH + ","
					+ IS_INCLUDE + "," + CONVERTED_DATE + ","+CONVERTED_STATUS+") VALUES(?,?,?,?);";
			//PreparedStatement statement = null;
			try {

				statement = con.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);

				statement.setString(1, filepath);
				statement.setBoolean(2, isIncludeFile);
				statement.setString(3, getDate());
				statement.setBoolean(4, false);
				statement.executeUpdate();

				ResultSet set = statement.getGeneratedKeys();
				if (set.next())
					id = set.getInt(1);

				statement.close();

				if (isIncludeFile) {
					insertIncludeFile();
				} else
					idMain = id;

			} catch (SQLException e) {

				if (e.getMessage().contains("Duplicate entry ")) {
					id = queryID(filepath);
					updateConvertedDate(id);
				}

			} finally {

				if (statement != null) {
					try {
						statement.close();

					} catch (SQLException e) {

						e.printStackTrace();
					}
				}

			}
		}
	}

	private void insertIncludeFile() {
		//PreparedStatement statement = null;
		try {
			String query = "INSERT INTO " + INCLUDE_FILE_TABLE + " (" + ID
					+ "," + PARENT_ID + ") VALUES " + "(?,?) ;";
			statement = con.prepareStatement(query);
			statement.setInt(1, id);
			statement.setInt(2, idMain);
			writeQuery(statement.toString());
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int queryID(String path) {
		int id = -1;
		String query = "SELECT " + ID + " FROM " + PAGE_TABLE + " WHERE "
				+ PATH + "=? ;";
	//	PreparedStatement statement = null;
		try {

			statement = con.prepareStatement(query);

			statement.setString(1, path);
			ResultSet set = statement.executeQuery();
			set.next();
			id = set.getInt(1);
		} catch (Exception e) {
			if (e.getMessage()
					.contains("Illegal operation on empty result set"))
				System.out.println("It's okay baby!");

		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return id;
	}

	private void updateConvertedDate(int id) {

		//PreparedStatement statement = null;
		try {
			String query = "UPDATE " + PAGE_TABLE + " SET " + CONVERTED_DATE
					+ "= ? ,"+CONVERTED_STATUS+"=? WHERE " + ID + "= ?  ;";
			statement = con.prepareStatement(query);

			statement.setString(1, getDate());
			statement.setBoolean(2, true);
			statement.setInt(3, id);
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void remove(String filepath) {

	}

	/*
	 * @param-basetag: Tag type. Doesn't contain any attributes.
	 * 
	 * @param-startTag: Tag with attribute.
	 * 
	 * @param-newTag: Fix for the tag original tag.
	 * 
	 * @param-debugInfo: contains line numbers etc.
	 */
	public void log(String baseTag, String startTag, String fix,
			String debugInfo) {
		if (isEnabled()) {
			PreparedStatement statement = null;
			try {
				String query = "INSERT INTO " + CHANGE_LOG_TABLE + " (" + ID
						+ "," + SOURCE + "," + FIX + "," + TAG + "," + LINE
						+ ") VALUES (?,?,?,?,?);";
				statement = con.prepareStatement(query);

				statement.setInt(1, id);
				statement.setString(2, startTag);
				statement.setString(3, fix);
				statement.setString(4, baseTag);
				statement.setInt(5, extractLineNumber(debugInfo));
				statement.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void deleteFromPage() {
		String query = "DELETE FROM " + PAGE_TABLE + " WHERE " + ID + "=? ;";
		PreparedStatement statement = null;
		try {

			statement = con.prepareStatement(query);

			statement.setInt(1, id);
			statement.execute();

		} catch (Exception e) {

			

		}
	}

	private void deleteFromIncludeFiles() {
		String query = "DELETE FROM " + INCLUDE_FILE_TABLE + " WHERE " + ID
				+ "=? OR " + PARENT_ID + " = ? ;";
		PreparedStatement statement = null;
		try {

			statement = con.prepareStatement(query);

			statement.setInt(1, id);
			statement.setInt(2, id);
			statement.execute();

		} catch (Exception e) {

		

		}

	}

	private void deleteFromChangeLog() {

		String query = "DELETE FROM " + CHANGE_LOG_TABLE + " WHERE " + ID
				+ "=? ;";
		PreparedStatement statement = null;
		try {

			statement = con.prepareStatement(query);

			statement.setInt(1, id);
			statement.execute();

		} catch (Exception e) {

	
		}

	}

	public void delete(String filepath) {

		if (isEnabled()) {

			setId(queryID(filepath));
			deleteFromChangeLog();
			deleteFromIncludeFiles();
			deleteFromPage();
		}

	}

	private int extractLineNumber(String debugInfo) {
		return Integer.parseInt(debugInfo.substring(
				debugInfo.indexOf("((r") + 3, debugInfo.indexOf(",c")));
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void enable(boolean b) {
		enabled = b;
	}
	
	public int getNumberOfErrors(){
		
		String query = "SELECT COUNT(*) AS entries FROM " + ERRORS_TABLE+";";
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
			ResultSet set = statement.executeQuery();
			set.next();
			return set.getInt("entries");

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
	
	public ArrayList<Error> getErrors(){
		String query = "SELECT * FROM " + ERRORS_TABLE+";";
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
			ResultSet set = statement.executeQuery();
			ArrayList<Error> errorList = new ArrayList<Error>();
			Error errorObject;
			while(set.next()){
				errorObject = new Error();
				errorObject.setPageId(set.getInt(ID));
				errorObject.setLastConvertedLine(set.getInt(LAST_CONVERTED_LINE));
				errorObject.setErrorType(set.getString(ERROR_TYPE));
				errorObject.setErrorMessage(set.getString(ERROR_MESSAGE));
				errorList.add(errorObject);
			}
			
			return errorList;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	public String getPath(int id){
		String query = "SELECT "+PATH+" FROM " + PAGE_TABLE+" WHERE "+ID+"=?;";
		PreparedStatement statement = null;
		try {
			
			statement = con.prepareStatement(query);
			statement.setInt(1,id);
			ResultSet set = statement.executeQuery();
			set.next();
			return set.getString(PATH);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public void changeConvertedStatus(boolean status){
		
		if(isEnabled()){
			PreparedStatement statement = null;
			try {
				String query = "UPDATE " + PAGE_TABLE + " SET " + CONVERTED_DATE
						+ "= ?, "+CONVERTED_STATUS+"=? WHERE " + ID + "= ?  ;";
				statement = con.prepareStatement(query);
	
				statement.setString(1, getDate());
				statement.setBoolean(2, status);
				statement.setInt(3, id);
				statement.executeUpdate();
	
			} 
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void logError(String path,String errorType,String errorMessage,String debugInfo){
		//enabled=true;
		//if(isEnabled()) System.out.println("enabled");
		if(isEnabled()){
			//delete any previous errors recoded.
			
			
			
			PreparedStatement statement = null;
			try {
				String query = "INSERT INTO " + ERRORS_TABLE + "(" + ID +","+ERROR_MESSAGE+","+ERROR_TYPE+","+LAST_CONVERTED_LINE
						+") VALUES(?,?,?,?);";
				statement = con.prepareStatement(query);
				
				statement.setInt(1, queryID(path));
				statement.setString(2, errorMessage);
				statement.setString(3, errorType);
				if(debugInfo==null) statement.setInt(4, 0);
				else statement.setInt(4, extractLineNumber(debugInfo));
				statement.execute();
				changeConvertedStatus(false);
	
			} catch(MySQLIntegrityConstraintViolationException ex){
				deleteFromErrorLog(path);
				logError(path,errorType,errorMessage,debugInfo);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}
	
	public void deleteFromErrorLog(String path){
		int id = queryID(path);
		if(isEnabled()){
			
			PreparedStatement statement = null;
			try {
				String query = "DELETE FROM "+ERRORS_TABLE+
						" WHERE " + ID + "= ?  ;";
				statement = con.prepareStatement(query);

				statement.setInt(1, id);
				statement.executeUpdate();
				changeConvertedStatus(true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public String getLastConvertedLine(){
		PreparedStatement statement = null;
		try {
			String query = "SELECT "+LINE+" FROM "+CHANGE_LOG_TABLE
					+" WHERE ID =? ORDER BY "+LINE+" DESC LIMIT 1;";
			statement = con.prepareStatement(query);
			statement.setInt(1,id);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				int lastLine = set.getInt(LINE);
				System.out.println(lastLine);
				return "((r"+lastLine+",c";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public void writeQuery(String query){
		try{
		java.io.PrintWriter writer = new java.io.PrintWriter("queries.txt", "UTF-8");
		writer.append(query +"\n");
		writer.close();
	
		}catch(IOException  e){
			
		}
	}
}
