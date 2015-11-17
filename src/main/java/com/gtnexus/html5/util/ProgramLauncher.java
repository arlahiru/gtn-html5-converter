package com.gtnexus.html5.util;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.ui.MainUI;

public class ProgramLauncher {

	private String araxis_path = "C:/Program Files/Araxis/Araxis Merge/merge.exe";
	private String dreamweaver_path = "C:/Program Files (x86)/Macromedia/Dreamweaver 8/Dreamweaver.exe";
	private String firefox_path = "C:/Program Files (x86)/Mozilla Firefox/firefox.exe";
	private String internetExplorer_path = "C:/Program Files (x86)/Internet Explorer/iexplorer.exe";
	private String notepadPath = "";
	public final String DEFAULT_BACKUP_PATH = "C:/Html5BackupDir";
	public static String adminBasePath = "C:/code/gtnexus/devl/modules/main/tcard";
	public static String adminBasePath2 = "C:/code/gtnexus/devl/";
	public static String adminPath = "C:/code/gtnexus/devl/modules/main/tcard/web/tradecard/en";
	public static String tradeBasePath = "";
	public final String CONFIG_FILE = "config.ini";
	public final String LOCALHOST = "http://localhost:8080/";
	public final String QA2HOST = "http://commerce.qa2.tradecard.com/";
	private String backupPath = DEFAULT_BACKUP_PATH;
	public String otherHost ="";
	private MainUI mainUI;
	private boolean isBackupValid = false;
	public static final String HTML5_BACKUP_DIR = "Html5BackupDir";

	public ProgramLauncher(MainUI main){
		mainUI=main;
		//setBackupPath(backupPath);
		//setBasePaths();
	}
	public boolean isBackupValid(){
		return isBackupValid;
	}
	public void setIsBackValid(boolean value){
		isBackupValid=value;
	}
	public String getAraxis_path() {
		return araxis_path;
	}

	public void setAraxis_path(String araxis_path) {
		this.araxis_path = araxis_path;
	}

	public String getDreamweaver_path() {
		return dreamweaver_path;
	}

	public void setDreamweaver_path(String dreamweaver_path) {
		this.dreamweaver_path = dreamweaver_path;
	}

	public String getFirefox_path() {
		return firefox_path;
	}

	public void setFirefox_path(String firefox_path) {
		this.firefox_path = firefox_path;
	}

	private String getInternetExplorer_path() {
		return internetExplorer_path;
	}

	
	private String getBrowserPath(String browserName){
		switch(browserName){
			case "FIREFOX":
				return firefox_path;
			case "IE":
				return internetExplorer_path;
			default:
				return null;		
		}
	}
	private void setBrowserPath(String browserName,String browserPath){
		switch(browserName){
			case "FIREFOX":
				firefox_path=browserPath;
				break;
			case "IE":
				internetExplorer_path=browserPath;
		}
	}
	private String getHost(String host){
		switch(host){
		case "LOCALHOST":
			return firefox_path;
		case "QA2":
			return internetExplorer_path;
		default:
			return otherHost;		
	}
	}

	public void openWithBrowser(String browser,
			String host, String file) {
		HTML5ParserException exception = null;
		String browserPath = getBrowserPath(browser);
		ProcessBuilder pb = new ProcessBuilder(browserPath, host + file);
		try {
			pb.start();
		} catch (IOException ex) {
			
			if (ex.getMessage().contains(
					"The system cannot find the file specified")) {
				String directoryInConfigFile=null;
				try{
					directoryInConfigFile = extractValue(
							"<" + browser + ">", "</" + browser + ">");
				}catch(HTML5ParserException e){
					exception = e;
				}
				
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(browserPath))
						setBrowserPath(browser,directoryInConfigFile);
					else {
						browserPath = readFilePaths("Set "+browser+".");
						appendPathToConfigFile(browserPath, "<" + browser + ">");
						openWithBrowser(browser, host, file);
					}

				} catch (NullPointerException ex2) {
					browserPath = readFilePaths("Set "+browser+".");
					appendPathToConfigFile(browserPath, "<" + browser + ">");
					openWithBrowser(browser, host, file);
				}
			}

		}
	}
	
	public String extractValue(String key, String ending) throws HTML5ParserException{
		try {
			return readFromConfigFile().substring(
					readFromConfigFile().indexOf(key) + key.length(),
					readFromConfigFile().indexOf(ending));
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}catch(HTML5ParserException e){
			throw e;
		}
	}
	public String readFromConfigFile() throws HTML5ParserException{
		StringBuilder fileContent = new StringBuilder();
		FileReader configFile = null;
		try {
			configFile = new FileReader(CONFIG_FILE);
		} catch (FileNotFoundException e) {
			File newConfigFile = new File(CONFIG_FILE);
			newConfigFile.mkdir();
			try {
				newConfigFile.createNewFile();
			} catch (IOException e2) {
				throw new HTML5ParserException("ERROR: Config file not found. Creating a new config file failed. Consider creating it manually.","error");
				
			}
		}
		BufferedReader br = null;
		try {

			if (configFile != null) {

				br = new BufferedReader(configFile);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					fileContent.append(sCurrentLine);
				}

				configFile.close();

			}

		} catch (IOException e2) {

			e2.printStackTrace();

		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
		return fileContent.toString();

	}
	
	/*
	 * Replace/Append a value in/to the config file. elements are in xml format.
	 * If the tag doesn't exist, program will append it to the file. else will
	 * replace the value.
	 * 
	 * @param-newPath : the new value that should replace/should be appended.
	 * 
	 * @param-tag : tag name
	 */
	public void appendPathToConfigFile(String newPath, String tag) {
		String fileContent = readFromConfigFile();
		try {

			String currentLine = fileContent.substring(
					fileContent.indexOf(tag),
					fileContent.indexOf(getEndingTag(tag)) + tag.length() + 1);
			fileContent = fileContent.substring(0,
					fileContent.indexOf(currentLine))
					+ getAsXmlValue(newPath, tag)
					+ fileContent.substring(fileContent.indexOf(currentLine)
							+ currentLine.length(), fileContent.length());

		} catch (StringIndexOutOfBoundsException e) {
			fileContent = readFromConfigFile();
			fileContent += getAsXmlValue(newPath, tag);
		}
		try {
			PrintWriter writer = new PrintWriter(CONFIG_FILE);
			writer.println(fileContent);
			writer.close();

		} catch (FileNotFoundException e) {
			
			//printOnConsole("Creating Config file.", "info");
		}
	}
	
	/*
	 * returns the absolute file path of an executable file. provides a file
	 * chooser to select the file.
	 */
	public String readFilePaths(String message) {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setApproveButtonText(message);
			chooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"Executable Files", "exe"));
			chooser.setAcceptAllFileFilterUsed(false);
			Frame browser = new Frame();
			browser.setTitle(message);
			int returnVal = chooser.showOpenDialog(browser);
			if (returnVal == JFileChooser.FILES_ONLY) {

				return chooser.getSelectedFile().getAbsolutePath();
			}
		} catch (Exception ex) {

		}
		return null;
	}
	public String readDirectories(String message){
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setApproveButtonText(message);
			Frame browser = new Frame();
			browser.setTitle(message);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(browser);
			if (returnVal == JFileChooser.APPROVE_OPTION)
				return chooser.getCurrentDirectory().getAbsolutePath();

		}catch (Exception ex) {

		}
		return null;
	}
	public String getFileName(String path) {
		return path.substring(path.lastIndexOf("\\"));
	}

	
	

	public String getAsXmlValue(String value, String tag) {
		return tag + value + getEndingTag(tag);
	}

	public String getEndingTag(String tag) {
		return tag.substring(0, 1) + "/" + tag.substring(1, tag.length());
	}

	/*
	 * Opens a file using Dreamweaver
	 */
	public void openDreamweaver(String path) {
		ProcessBuilder pb = new ProcessBuilder(dreamweaver_path, path);
		try {
			pb.start();
		} catch (IOException ex) {
	//		printOnConsole(ex.getMessage(), "error");
			if (ex.getMessage().contains(
					"The system cannot find the file specified")||ex.getMessage().contains("The parameter is incorrect")) {

				String directoryInConfigFile = extractValue("<DREAMWEAVER>",
						"</DREAMWEAVER>");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(dreamweaver_path))
						dreamweaver_path = directoryInConfigFile;
					else {
						dreamweaver_path = readFilePaths("Set Dreamweaver.");
						appendPathToConfigFile(dreamweaver_path,
								"<DREAMWEAVER>");
					}
					openDreamweaver(path);
				} catch (NullPointerException ex2) {
					dreamweaver_path = readFilePaths("Set Dreamweaver.");
					appendPathToConfigFile(dreamweaver_path, "<DREAMWEAVER>");
					openDreamweaver(path);
				}
			}

		}
	}
	
	public void openNotePad(String path) {
		ProcessBuilder pb = new ProcessBuilder(notepadPath, path);
		try {
			pb.start();
		} catch (IOException ex) {
			
			mainUI.printOnConsole(ex.getMessage(), "error");
			if (ex.getMessage().contains(
					"The system cannot find the file specified")||ex.getMessage().contains("The parameter is incorrect")) {

				String directoryInConfigFile = extractValue("<NOTEPAD++>",
						"</NOTEPAD++>");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(notepadPath))
						 notepadPath = directoryInConfigFile;
					else {
						notepadPath = readFilePaths("Set Notepad++.");
						appendPathToConfigFile(notepadPath,
								"<NOTEPAD++>");
					}
					openNotePad(path);
				} catch (NullPointerException ex2) {
					ex2.printStackTrace();
					notepadPath = readFilePaths("Set Notepad++.");
					appendPathToConfigFile(notepadPath, "<NOTEPAD++>");
					openNotePad(path);
				}
			}

		}
	}

	public void openAraxis(String filename){

		ProcessBuilder pb = new ProcessBuilder(araxis_path, formatBackupFilePath(filename),filename);
		try {
			pb.start();
		} catch (IOException ex) {
		//	printOnConsole(ex.getMessage(), "error");
			if (ex.getMessage().contains(
					"The system cannot find the file specified")) {

				String directoryInConfigFile = extractValue("<ARAXIS>",
						"</ARAXIS>");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(araxis_path))
						araxis_path = directoryInConfigFile;
					else {
						araxis_path = readFilePaths("Please select Araxis.");
						appendPathToConfigFile(araxis_path, "<ARAXIS>");
					}
					openAraxis(filename);
				} catch (NullPointerException ex2) {
					araxis_path = readFilePaths("Please select Araxis.");
					appendPathToConfigFile(araxis_path, "<ARAXIS>");
					openAraxis(filename);
				}
			}

		}
	}

	public String formatFilePath(String fileName) {
		return adminBasePath.substring(0,adminBasePath.indexOf("\\en\\")+4) + fileName;
	}

	public String formatBackupFilePath(String fileName) {
		return fileName.replaceFirst("code", ProgramLauncher.HTML5_BACKUP_DIR+"/code");	
	}

	/**
	 * @return the backupPath
	 */
	public String getBackupPath() {
		return backupPath;
	}
	
	public boolean checkFile(String fileName,String basePath) {
		File f = new File(basePath + fileName);		
		return (f.exists() && !f.isDirectory());
	}
 
	public boolean checkBackup(String filePathString) {

		File f = new File(backupPath + filePathString);
		mainUI.printOnConsole(backupPath + filePathString, "info");
		return (f.exists() && !f.isDirectory());
	}
	public void setBackupPath(String backupPath) {

		File loginFile = new File(backupPath
				+ "\\web\\tradecard\\en\\administration\\login.jsp");
		try {

			if (loginFile.exists()) {
				this.backupPath = backupPath;
				mainUI.getBackupLocationField().setText(backupPath);
				setIsBackValid(true);
				mainUI.printOnConsole("Backup path changed.", "info");
			} else {
				mainUI.printOnConsole(
						"Error: Backup directory file path seems to be incorrect.\n Resetting back to default path.\n Consider moving the files to : "
								+ DEFAULT_BACKUP_PATH, "error");
			setIsBackValid(false);
			}
		} catch (Exception e1) {
			mainUI.printOnConsole(e1.getMessage(), "error");
			mainUI.printOnConsole(mainUI.printStacktrace(e1), "error");
		}
	}
	public void setBasePaths(){
		
		if(extractValue("<adminBasePath>","</adminBasePath>")!=null && checkFile("/web/tradecard/en/administration/login.jsp",adminBasePath)){
			adminBasePath = extractValue("<adminBasePath>","</adminBasePath>");
			
		}else{
			adminBasePath = readDirectories("Set Admin Site Path")+"/";
			if(checkFile("login.jsp",adminBasePath))
				appendPathToConfigFile(adminBasePath,"<adminBasePath>");
			else setBasePaths();
		}
		if(extractValue("<tradeBasePath>","</tradeBasePath>")!=null && checkFile("/web/tradecard/en/trade/login.jsp",adminBasePath) ){
			tradeBasePath = extractValue("<tradeBasePath>","</tradeBasePath>");
		}else{
			tradeBasePath = readDirectories("Set Trade Site Path")+"/";
			if(checkFile("Home.jsp",tradeBasePath))	
				appendPathToConfigFile(tradeBasePath,"<tradeBasePath>");
			else setBasePaths();
		}
	}
}
