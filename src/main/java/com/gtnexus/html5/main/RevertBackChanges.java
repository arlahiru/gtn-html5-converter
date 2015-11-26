package com.gtnexus.html5.main;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import net.htmlparser.jericho.Source;

import com.gtnexus.html5.util.DbLogger;
import com.gtnexus.html5.util.HTML5Util;
import com.gtnexus.html5.util.ProgramLauncher;

public class RevertBackChanges {

	private static DbLogger dblogger = DbLogger.getInstance();

	public static void main(String args[]) throws FileNotFoundException,
			IOException {

		// disable dblogger
		dblogger.enable(false);

		String convertedBaseFilePath = "C:/code/gtnexus/development/modules/main/tcard";

		String convertedFilePath = convertedBaseFilePath
				+ "/web/tradecard/en/administration/login.jsp";

		String backupBasePath = "C:/TcardWebBackup";

		revertChanges(convertedFilePath);

	}

	public static void revertChanges(String convertedFilePath) throws FileNotFoundException, IOException {

		File convertedFile = new File(convertedFilePath);

		// get include file paths. 
		Set<String> includeFilePathList = HTML5Util.getIncludeFilePaths(convertedFilePath,null);

		// recursively revert include files
		for (String includeFilePath : includeFilePathList) {			
			//revert h5.jsp to .jsp
			//includeFilePath = includeFilePath.replace(HTML5Util.H5_EXTENSION, ".jsp");
			revertChanges(includeFilePath);
		} 
		String backupFilePath = convertedFile.getAbsolutePath().replaceFirst("code", ProgramLauncher.HTML5_BACKUP_DIR+"/code");	

		Path originalBackupFilePath = Paths.get(backupFilePath);

		// Files.copy(originalBackupFilePath,
		// convertedFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

		// this way system will apply restored changes to JSP while server is up
		// by compiling the JSP again
		BufferedWriter jspWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(convertedFile), "UTF-8"));

		Source revertedSourceFile = new Source(new FileInputStream(
				originalBackupFilePath.toFile()));

		jspWriter.write(revertedSourceFile.toString());

		jspWriter.close();

		// clear log tables
		dblogger.rollback(convertedFilePath);

		logger.info("Backup file path:" + backupFilePath
				+ " copied successfully!");
	}
	
	public static void backupOriginalFileToLocalDisk(File originalFile) throws IOException{
		
		String backupDirectoryPath = originalFile.getParent().replace("code", ProgramLauncher.HTML5_BACKUP_DIR+"\\code");		
		Path source = Paths.get(originalFile.getAbsolutePath());
	    Path target = Paths.get(backupDirectoryPath);
	    //create target dir structure if not exist
	    target.toFile().mkdirs();
	    Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
	}
}
