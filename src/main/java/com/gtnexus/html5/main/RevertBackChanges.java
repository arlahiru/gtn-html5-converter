package com.gtnexus.html5.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.htmlparser.jericho.Source;
import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;

import com.gtnexus.html5.util.DbLogger;
import com.gtnexus.html5.util.HTML5Util;

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

		revertChanges(convertedFilePath, backupBasePath);

	}

	public static void revertChanges(String convertedFilePath,
			String backupBasePath) throws FileNotFoundException, IOException {

		File convertedFile = new File(convertedFilePath);

		// get include file paths
		List<String> includeFilePathList = HTML5Util
				.getIncludeFilePaths(convertedFilePath);

		// recursively convert include files
		for (String includeFilePath : includeFilePathList) {

			revertChanges(includeFilePath, backupBasePath);

		}

		String[] filePathSplitArray = convertedFilePath.split("tcard");

		String backupFilePath = backupBasePath + filePathSplitArray[1];

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
		dblogger.delete(convertedFilePath);

		logger.info("Backup file path:" + backupFilePath
				+ " copied successfully!");
	}
}
