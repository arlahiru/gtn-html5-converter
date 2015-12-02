package com.gtnexus.html5.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.filechooser.FileSystemView;

import com.gtnexus.html5.main.JerichoJspParserUtil;
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
	private static final String ACCESSED_DATE= "AccessedDate";
	private static final String STATUS = "Status";
	private static final String FILENAME = "Filename";
	private static final String IS_ADMIN = "IsAdmin";
	private static final String SCANNED = "Scanned";
	
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
	
	private static final String CONFLICTING_PAGES = "ConflictingPages";
	private static final String ADMIN_PAGE_COL = "AdminPage";
	private static final String TRADE_PAGE_COL = "TradePage";
	
	private static final String INLINESTYLE_TABLE = "inlinestyle";
	private static final String FILE_NAME="fileName";
	private static final String ELEMENT_NAME="elementName";
	private static final String ELEMENT_LINE="line";
	private static final String STYLETEXT = "style";
	private static final String IS_POSITIONAL = "ispositional";
	
	private static final String CSSCLASS_TABLE="cssclass";
	private static final String CLASS_NAME="classname";
	private static final String STYLE="style";
	
	public static final String STATUS_FAILED = "Failed";
	public static final String STATUS_CONVERTED = "Converted";
	public static final String STATUS_NOT_CONVERTED = "Not Converted";
	public static final String STATUS_ROLLED_BACK = "Rolled Back";
	
	
	
	private volatile PreparedStatement insertPage;
	private volatile PreparedStatement insertIncludeFile;
	private volatile PreparedStatement queryId;
	private volatile PreparedStatement logChange;
	private volatile PreparedStatement logError;
	private volatile PreparedStatement updateConvertedDate;
	private volatile PreparedStatement deleteFromChangeLog; 
	private volatile PreparedStatement getErrors;
	private volatile PreparedStatement updateScannedState;
	private volatile PreparedStatement insertConflict;
	private volatile PreparedStatement insertInlinestyle;
	private volatile PreparedStatement insertCssClassName;
	
	//delete queries
	private String deleteError = "truncate " +ERRORS_TABLE;
	private String deleteIncludePage = "truncate " + INCLUDE_FILE_TABLE;
	private String deletePage = "truncate " + PAGE_TABLE;		
	private String deleteChangelog = "truncate " + CHANGE_LOG_TABLE;
	private String deleteConflictPage = "truncate " + CONFLICTING_PAGES;
	private String deleteInlineStyle = "truncate " + INLINESTYLE_TABLE;
	private String deleteCssClass = "truncate " + CSSCLASS_TABLE;
	
	private void makeStatements(){
		String queryInsertPage = "INSERT INTO " + PAGE_TABLE + "(" + PATH + ","
				+ IS_INCLUDE + "," + ACCESSED_DATE + ","+STATUS+","+FILENAME+","+IS_ADMIN+","+SCANNED+") VALUES(?,?,?,?,?,?,?);";
		
		String queryInsertIncludeFile ="INSERT INTO " + INCLUDE_FILE_TABLE + " (" + ID
				+ "," + PARENT_ID + ") VALUES " + "(?,?) ;";
		
		String queryIdString = "SELECT " + ID + " FROM " + PAGE_TABLE + " WHERE "
				+ PATH + "=? ;";
		
		String queryLogChange = "INSERT INTO " + CHANGE_LOG_TABLE + " (" + ID
				+ "," + SOURCE + "," + FIX + "," + TAG + "," + LINE
				+ ") VALUES (?,?,?,?,?);";

				
		String queryUpdateConvertedDate = "UPDATE " + PAGE_TABLE + " SET " + ACCESSED_DATE
				+ "= ? ,"+STATUS+"=? WHERE " + ID + "= ?  ;"; 		
		
		String queryDeleteFromChangeLog = "DELETE FROM " + CHANGE_LOG_TABLE + " WHERE " + ID
				+ "=? ;";
		
		String queryGetErrors = "SELECT "+PAGE_TABLE+"."+PATH+","+ERRORS_TABLE+".* FROM "+PAGE_TABLE+" INNER JOIN "+ERRORS_TABLE+" ON "+
				 PAGE_TABLE+"."+ID+"="+ERRORS_TABLE+"."+ID+";";
		
		String queryUpdateScannedState = "UPDATE "+PAGE_TABLE+" SET "+SCANNED+"=? WHERE "+ID+"=? ;";
		
		String quertInsertConflict = "INSERT INTO "+CONFLICTING_PAGES+" VALUES(?,?);";
		
		String queryInlineStyle = "INSERT INTO " + INLINESTYLE_TABLE + " ("
				+ FILE_NAME + "," + ELEMENT_NAME + "," +ELEMENT_LINE+","+ STYLETEXT +","+IS_POSITIONAL+","+ IS_ADMIN 
				+ ") VALUES (?,?,?,?,?,?);";
		
		String querytInsertCssStyles = "INSERT INTO "+CSSCLASS_TABLE+ "("+ CLASS_NAME + "," + STYLE + "," +IS_ADMIN+")"+" VALUES(?,?,?);";
		
		try{
			insertPage=con.prepareStatement(queryInsertPage,
					Statement.RETURN_GENERATED_KEYS);
			
			insertIncludeFile = con.prepareStatement(queryInsertIncludeFile);
			
			queryId = con.prepareStatement(queryIdString);
			
			updateConvertedDate = con.prepareStatement(queryUpdateConvertedDate);
			
			logChange = con.prepareStatement(queryLogChange);
			
			deleteFromChangeLog = con.prepareStatement(queryDeleteFromChangeLog);
			
			getErrors = con.prepareStatement(queryGetErrors);
			
			updateScannedState = con.prepareStatement(queryUpdateScannedState);
			
			insertConflict = con.prepareStatement(quertInsertConflict);
			
			insertInlinestyle = con.prepareStatement(queryInlineStyle);
			
			insertCssClassName = con.prepareStatement(querytInsertCssStyles);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
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
		initialize();

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
				makeStatements();

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				enabled=false;
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
	}

	private synchronized void readCredentials() {

		StringBuilder str = new StringBuilder();
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(CONFIG_FILE));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				str.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		USER = str.substring(str.indexOf("<USERNAME>") + 10,
				str.indexOf("</USERNAME>"));
		PASSWORD = str.substring(str.lastIndexOf("<PASSWORD>") + 10,
				str.lastIndexOf("</PASSWORD>"));

	}

	private synchronized void revokeCon() {
		con = null;
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	public synchronized void insertPage(String filepath, boolean isIncludeFile,String filename,boolean isConversion) {

		if (isEnabled()) {

			System.out.println(filepath+":"+isIncludeFile);
			//PreparedStatement statement = null;
			try {
				insertPage.setString(1, filepath);
				insertPage.setBoolean(2, isIncludeFile);
				insertPage.setString(3, getDate());
				if(isConversion)
					insertPage.setString(4, STATUS_CONVERTED);
				else 
					insertPage.setString(4, STATUS_NOT_CONVERTED);
				insertPage.setString(5,filename.substring(filename.lastIndexOf('\\')+1));
				if(filepath.contains("/administration/")||filepath.contains("\\administration\\"))
					insertPage.setBoolean(6, true);
				else
					insertPage.setBoolean(6, false);
				insertPage.setBoolean(7, false);
				insertPage.executeUpdate();
				
				ResultSet set = insertPage.getGeneratedKeys();
				if (set.next())
					id = set.getInt(1);
				System.out.println("ID="+id);
				

				if (isIncludeFile) {
					insertIncludeFile();
				} else
					idMain = id;

			} catch (SQLException e) {

				if (e.getMessage().contains("Duplicate entry ") && isConversion) {
					id = queryID(filepath);
					setStatus(id,STATUS_CONVERTED);
					System.out.println("Duplicate entry "+filepath);
				}				
				else{
					e.printStackTrace();
				}

			} 	
		}	
	}
	

	private synchronized void insertIncludeFile() {
		//PreparedStatement statement = null;
		try {
			
			insertIncludeFile.setInt(1, id);
			insertIncludeFile.setInt(2, idMain);
			writeQuery(insertIncludeFile.toString());
			insertIncludeFile.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			/*
			if (insertIncludeFile != null) {
				try {
					insertIncludeFile.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}*/
		}
	}

	private int queryID(String path) {
		int id = -1;
		

		try {
			 queryId.setString(1, path);
			 
			ResultSet set = queryId.executeQuery();
			if(set.next()){
				//set.next();
				id = set.getInt(1);
			}
			
		}/* catch(){
			writeQuery(queryId.toString());
			e.printStackTrace();
		}*/catch (Exception e) {
			e.printStackTrace();
			//if (e.getMessage().contains("Illegal operation on empty result set"))
			

		} finally {
			if (queryId != null) {
		/*		try {
					queryId.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		*/	}
		}
		return id;
	}

	private synchronized void setStatus(int id,String status) {

		//PreparedStatement statement = null;
		try {

			updateConvertedDate.setString(1, getDate());
			updateConvertedDate.setString(2, status);
			updateConvertedDate.setInt(3, id);
			updateConvertedDate.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (updateConvertedDate != null) {
		/*		try {
					updateConvertedDate.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		*/	}
		}
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
	public synchronized void log(String baseTag, String startTag, String fix,
			String debugInfo) {
		if (isEnabled()) {
//			PreparedStatement statement = null;
			try {
				
				logChange.setInt(1, id);
				logChange.setString(2, startTag);
				logChange.setString(3, fix);
				logChange.setString(4, baseTag);
				logChange.setInt(5, extractLineNumber(debugInfo));
				logChange.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (logChange != null) {
				/*	try {
						logChange.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			*/	}
			}
		}
	}

	private  synchronized void deleteFromPage() {
		String query = "DELETE FROM " + PAGE_TABLE + " WHERE " + ID + "=? ;";
		PreparedStatement statement = null;
		try {

			statement = con.prepareStatement(query);

			statement.setInt(1, id);
			statement.execute();

		} catch (Exception e) {

			

		}
	}

	private synchronized void deleteFromIncludeFiles() {
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

	private synchronized void deleteFromChangeLog() {

		try {

			deleteFromChangeLog.setInt(1, id);
			deleteFromChangeLog.execute();

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
	
	public void rollback(String filepath){
		if (isEnabled()) {
			setId(queryID(filepath));
			deleteFromChangeLog();
			setStatus(id,STATUS_ROLLED_BACK);
			
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
	//	String query = "SELECT * FROM " + ERRORS_TABLE+";";
	//	PreparedStatement statement = null;
		try {
			//getErrors = con.prepareStatement(query);
			ResultSet set = getErrors.executeQuery();
			ArrayList<Error> errorList = new ArrayList<Error>();
			Error errorObject;
			while(set.next()){
				errorObject = new Error();
				errorObject.setPageId(set.getInt(ID));
				errorObject.setLastConvertedLine(set.getInt(LAST_CONVERTED_LINE));
				errorObject.setErrorType(set.getString(ERROR_TYPE));
				errorObject.setErrorMessage(set.getString(ERROR_MESSAGE));
				errorObject.setPath(set.getString(PATH));
				
				errorList.add(errorObject);
			}
			
			return errorList;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	public synchronized String getPath(int id){
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
				String query = "UPDATE " + PAGE_TABLE + " SET " + ACCESSED_DATE
						+ "= ?, "+STATUS+"=? WHERE " + ID + "= ?  ;";
				statement = con.prepareStatement(query);
	
				statement.setString(1, getDate());
				if(status)
					statement.setString(2, STATUS_CONVERTED);
				else
					statement.setString(2, STATUS_FAILED);
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
	
	public synchronized void logError(String path,String errorType,String errorMessage,String debugInfo){
		
		if(isEnabled()){
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
	
	public  synchronized void deleteFromErrorLog(String path){
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
			
			 
			File file = new File("queries.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());			
			fw.append(getDate()+" : "+query +"\n");
			fw.close();
	
		}catch(IOException  e){
			
		}
	}
	
	public void updateScannedState(String filename,boolean state){
		try{
			updateScannedState.setBoolean(1,state);
			updateScannedState.setInt(2, queryID(filename));
			updateScannedState.execute();
		}catch(SQLException e){
				e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAllConflicts(){
		ArrayList<String> paths = new ArrayList<String>();		
		try{
			String query = "SELECT * FROM "+CONFLICTING_PAGES;
			PreparedStatement statement= con.prepareStatement(query);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				paths.add(set.getString(1));
				paths.add(set.getString(2));
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return paths;
	}
	
	public ArrayList<String> getCommonAdminPageList(){
		ArrayList<String> paths = new ArrayList<String>();		
		try{
			String query = "SELECT DISTINCT "+ADMIN_PAGE_COL+" FROM "+CONFLICTING_PAGES;
			PreparedStatement statement= con.prepareStatement(query);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				paths.add(set.getString(1));
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return paths;
	}
	
	public ArrayList<String> searchConflicts(String searchTag){
		ArrayList<String> paths = new ArrayList<String>();
		
		try{
			String query = "SELECT * FROM "+CONFLICTING_PAGES
					+" WHERE "+ADMIN_PAGE_COL+" LIKE '%"+searchTag+"%' OR "+TRADE_PAGE_COL+" LIKE '%"+searchTag+"%';";
			PreparedStatement statement= con.prepareStatement(query);
			ResultSet set = statement.executeQuery();
			while(set.next()){
				paths.add(set.getString(1));
				paths.add(set.getString(2));
			}
		}catch(SQLException e){
				e.printStackTrace();
		}
		return paths;
	}
	
	public void insertConflictingPages(String filename,List<String> list){
		for(String file : list){
			try{
				insertConflict.setString(1,filename);
				insertConflict.setString(2,file);
				insertConflict.execute();
			}catch(SQLException e){
					e.printStackTrace();
			}finally{
				try {
					insertConflict.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void insertInlineStyle(String filePath, String elementName, String style,
			String debugInfo,Boolean isPositional,Boolean isAdmin) {		
		try {
			insertInlinestyle.setString(1,filePath);
			insertInlinestyle.setString(2, elementName);
			insertInlinestyle.setInt(3,extractLineNumber(debugInfo));
			insertInlinestyle.setString(4, style);
			insertInlinestyle.setBoolean(5, isPositional);
			insertInlinestyle.setBoolean(6, isAdmin);
			insertInlinestyle.execute();			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private synchronized void insertCssClasses(String className,String style,boolean isAdmin) {
		try {			
			insertCssClassName.setString(1,className);
			insertCssClassName.setString(2,style);
			insertCssClassName.setBoolean(3, isAdmin);
			insertCssClassName.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Map<String,String> getCssClasses(Boolean isAdmin){
		
		String query = "SELECT DISTINCT className, style FROM "+CSSCLASS_TABLE+" WHERE "+IS_ADMIN+"="+isAdmin+";";
		Map<String,String> classMap = new HashMap<String, String>();
		
		Statement statement = null;
		try{
			statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()){
				//class name => style
				classMap.put(rs.getString(1), rs.getString(2));
			}
			
		}catch(SQLException e){
				e.printStackTrace();
		}finally{
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return classMap;
	}
	
	//clear all data except conversion errors
	public void clearAllData(){
		//if (isEnabled()) {
			Statement deleteStatement = null;
			try{
				deleteStatement = con.createStatement();
				//deleteStatement.execute(deleteError);
				deleteStatement.execute(deleteChangelog);
				//deleteStatement.execute(deleteIncludePage);
				//deleteStatement.execute(deletePage);
				deleteStatement.execute(deleteConflictPage);
				if(HTML5Util.MODE.equals(HTML5Util.STYLEANALYZE)){
					deleteStatement.execute(deleteInlineStyle);
				}
				//deleteStatement.execute(deleteCssClass);
				
			}catch(SQLException e){
					e.printStackTrace();
			}finally{
				if(deleteStatement != null){
					try {
						deleteStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		//}
	}
	//clear all data
	public void clearAllErrors(){	
		if (isEnabled()) {
			Statement deleteStatement = null;
			try{
				deleteStatement = con.createStatement();
				deleteStatement.execute(deleteError);			
			}catch(SQLException e){
					e.printStackTrace();
			}finally{
				if(deleteStatement != null){
					try {
						deleteStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}		
		}
	}
	
	public void clearLog4JLogfile(){
		try {
			BufferedWriter logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("html5.log"), "UTF-8"));
			logWriter.write("");
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDynamicStyleMapToDB(boolean isAdmin){
		Set<Entry<String,String>> entrySet = JerichoJspParserUtil.STYLES_MAP.entrySet();
		for(Entry<String,String> entry:entrySet){
			insertCssClasses(entry.getValue(),entry.getKey(),isAdmin);
		}
	}
	
	public FileLogger getFileLoggerInstance(){
		return new DbLogger.FileLogger();
	}
	
	//This file logger is obsolete! Log4j used instead.
	public class FileLogger{

		private File logFile;
		private File log4jFile;
		private BufferedWriter logWriter;
		
		FileLogger(){
			logFile = new File(FileSystemView.getFileSystemView().getRoots()[0]+File.separator+"html5log.txt");
			log4jFile = new File("C:\\html5.log");
			try {
				if(!logFile.exists()){
					//logFile.createNewFile();	
				}
				if(!logFile.exists()){
					log4jFile.createNewFile();	
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void log(String log){
			try {
				logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8"));
				PrintWriter out = new PrintWriter(logWriter);
				out.println(log);
				logWriter.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void clearLogfile(){
			try {
				logWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8"));
				logWriter.write("");
				logWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}


