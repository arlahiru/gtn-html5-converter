package com.gtnexus.html5.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

import com.gtnexus.html5.ui.MainUI;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;


public class UsageScanner {
	private ProgramLauncher launcher;
	
	public UsageScanner(){}
	public UsageScanner(MainUI main){
		launcher = main.getProgramLauncher();
		
	}
	public ArrayList<String> getTradePagesLinkWithThisFile(
			String targetFilePath, final File tradeFolder,
			ArrayList<String> tradePageList) {

		for (final File tradeFileEntry : tradeFolder.listFiles()) {
			if (tradeFileEntry.isDirectory()) {
				getTradePagesLinkWithThisFile(targetFilePath, tradeFileEntry,
						tradePageList);

			} else {
				if (tradeFileEntry.getName().toLowerCase().endsWith(".jsp")
						|| tradeFileEntry.getName().toLowerCase()
								.endsWith(".html")) {

					try {

						// get include file paths
						List<String> includeFilePathList = HTML5Util
								.getIncludeFilePaths(tradeFileEntry
										.getAbsolutePath());

						for (String includeFilePath : includeFilePathList) {

							// check include file link to the given file name
							if (new File(targetFilePath).getAbsolutePath()
									.equals(new File(includeFilePath)
											.getAbsolutePath())) {

								tradePageList.add(tradeFileEntry
										.getAbsolutePath());
							}

						}

					}

					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return tradePageList;
	}

	public void performScan(String filepath,String textfileName){
		try{
			ArrayList<String> setOfConflicts = getTradePagesLinkWithThisFile(filepath,new File(launcher.tradeBasePath),new ArrayList<String>());
			dbLogger.insertConflictingPages(filepath, setOfConflicts);
		}catch(InvalidPathException e){
			e.printStackTrace();
		}
		dbLogger.insertPage(filepath, true, textfileName, false);
		dbLogger.updateScannedState(filepath, true);
	}
	
}
