package com.gtnexus.html5.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gtnexus.html5.ui.MainUI;

/**
 * @author lruhunage
 *This is a utility scan wrote to scan admin and trade folders to find common page list, affected trade/admin pages with details/
 *This class seems messy as it modified frequently to scan different folders to get different results in detail :)
 */
public class UsageScanner {
	
	private static Set<String> scannedPagesSet = new HashSet<String>();
	
	public UsageScanner(){}
	public UsageScanner(MainUI main){
			
	}
	public static void checkAdminPageLinkWithTrade(String targetFilePath, final File tradeFolder,Set<String> setOfLeakedCommonFiles,Set<String> setOfConflictedTradeFiles) {
		
		for (final File tradeFileEntry : tradeFolder.listFiles()) {
			
				if (tradeFileEntry.isDirectory()) {
					checkAdminPageLinkWithTrade(targetFilePath, tradeFileEntry,	setOfLeakedCommonFiles,setOfConflictedTradeFiles);
	
				} else {
						if (tradeFileEntry.getName().toLowerCase().endsWith(".jsp") || tradeFileEntry.getName().toLowerCase().endsWith(".html")) {
	
							try {
								
								System.out.println("Scanning -> "+tradeFileEntry.getAbsolutePath());
	
								// get include file paths
								List<String> includeFilePathList = HTML5Util.getIncludeFilePaths(tradeFileEntry.getAbsolutePath());
	
								for (String includeFilePath : includeFilePathList) {
									if(!setOfConflictedTradeFiles.contains(tradeFileEntry.getAbsolutePath())){	
										// check include file link to the given file name
										if (targetFilePath.equals(includeFilePath)) {		
											setOfConflictedTradeFiles.add(tradeFileEntry.getAbsolutePath());
											setOfLeakedCommonFiles.add(targetFilePath);
										}
									}
									/*
									if(!setOfLeakedCommonFiles.contains(targetFilePath)){	
										// check include file link to the given file name
										if (targetFilePath.equals(includeFilePath)) {		
											setOfLeakedCommonFiles.add(targetFilePath);
											break;
										}
									}else{
										break;
									}
									*/
	
								}	
							}
	
							catch (IOException e) {
							
								e.printStackTrace();
							}
						}
				}
			}
	}


	public void performScan(String filepath,String textfileName){
		try{
			Set<String> setOfConflicts = new HashSet<String>(0); 
			//checkAdminPageLinkWithTrade(filepath,new File(ProgramLauncher.tradeBasePath),setOfConflicts);
			//dbLogger.insertConflictingPages(filepath, setOfConflicts);
		}catch(InvalidPathException e){
			e.printStackTrace();
		}
		//dbLogger.insertPage(filepath, true, textfileName, false);
		//dbLogger.updateScannedState(filepath, true);
	}
	
	public static void traverseDirAndScan(File directory,Set<String> setOfLeakedCommonFiles,Set<String> setOfConflictedTradeFiles, String tradeFolder){
		
		for (final File file : directory.listFiles()) {
			if (file.isDirectory()) {
				traverseDirAndScan(file,setOfLeakedCommonFiles,setOfConflictedTradeFiles,tradeFolder);

			} else {
				//scan admin site and find common files in the trade site
				if ((file.getName().toLowerCase().endsWith(".jsp") || file.getName().toLowerCase().endsWith(".html"))) {
					
					try {						
							// get include file paths
							List<String> adminIncludeFilePathList = HTML5Util.getIncludeFilePaths(file.getAbsolutePath());	
							
							for (String includeFilePath : adminIncludeFilePathList) {
								if(scannedPagesSet.add(includeFilePath)){
									UsageScanner.checkAdminPageLinkWithTrade(includeFilePath,new File(tradeFolder),setOfLeakedCommonFiles,setOfConflictedTradeFiles);
								}
							}
					}
					 catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
		}
	}
	
public static void getAdminPagesWithCommonFileList(File directory,List<String> effectedAdminPageList){
	
	String txtFile = "EffectedAdminPagesWithDetails.txt";
	try {
	
	//populate common file list to a set
	Set<String> commonFileSet = populateTextFileLinesToSet("CommonIncludeFileList.txt");
		
		for (final File file : directory.listFiles()) {
			if (file.isDirectory()) {
				getAdminPagesWithCommonFileList(file,effectedAdminPageList);

			} else {
				//scan admin site and find effected pages with include files
				if ((file.getName().toLowerCase().endsWith(".jsp") || file.getName().toLowerCase().endsWith(".html"))) {					
							
							System.out.println("Scanning ->"+file.getName());							
							// get include file paths
							List<String> adminIncludeFilePathList = HTML5Util.getIncludeFilePaths(file.getAbsolutePath());	
							Set<String> commonIncludesFileSet = new HashSet<String>();
							for (String includeFilePath : adminIncludeFilePathList) {
								if(commonFileSet.contains(includeFilePath)){
									commonIncludesFileSet.add(includeFilePath);
								}
							}
							if(!commonIncludesFileSet.isEmpty()){								
								effectedAdminPageList.add(file.getAbsolutePath());
								effectedAdminPageList.add("<========================================================================>");
								effectedAdminPageList.addAll(commonIncludesFileSet);
								effectedAdminPageList.add(".");
							}
					
				}
			}
		}
		writeListToFile(effectedAdminPageList,txtFile);
	}
	 catch (Exception e) {
			e.printStackTrace();
	}
}
	
	public static void writeResultToFile(Set<String> conflictedPageList,String fileName){
		
		File txtFile = new File(fileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(txtFile);			
			for(String page : conflictedPageList){
				fileWriter.write(page+"\n");			 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
public static void writeListToFile(List<String> list,String fileName){
		
		File txtFile = new File(fileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(txtFile);			
			for(String page : list){
				if(page.equals(".")){
					fileWriter.write("\n");		
				}else{
					fileWriter.write(page+"\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void analyzeTradeConflictPages(){
		
		String fileName="TradeConflictedPageList.txt";
		File txtFile = new File(fileName);
		FileReader fileReader = null;
		BufferedReader br = null;
		try {
			fileReader = new FileReader(txtFile);			
			br = new BufferedReader(fileReader);
		    String line;
		    while ((line = br.readLine()) != null) {
		    	System.out.println();
		    	System.out.println(line);
		    	System.out.println("=====================================================================");
		       List<String> includeList =  HTML5Util.getIncludeFilePaths(line);
		       Set<String> filteredList =  new HashSet<String>();
		       for(String path:includeList){
		    	   if(path.contains("\\en\\includes/") || path.contains("\\en\\style") || path.contains("\\en\\common")){
		    		   filteredList.add(path);
		    	   } 
		       }
		       for(String path:filteredList){
		    	   System.out.println(path);
		       }
		       
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fileReader.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static Set<String> populateTextFileLinesToSet(String fileName){
		
		Set<String> fileSet = new HashSet<String>();
		File txtFile = new File(fileName);
		FileReader fileReader = null;
		BufferedReader br = null;
		try {
			fileReader = new FileReader(txtFile);			
			br = new BufferedReader(fileReader);
		    String line;
		    while ((line = br.readLine()) != null) {
		    	fileSet.add(line);		       
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fileReader.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileSet;
		
	}
	
	
	public static void main(String args[]){
		
		/*
		
		//String inputFile="C://code//gtnexus//development//modules//main//tcard//web//tradecard//en//includes/common/sectionspacer.include.jsp";
		String tradeFolder = "C://code//gtnexus//development//modules//main//tcard//web//tradecard//en//trade";
		String directoryPathToAnalyzeConflicts = "C:\\code\\gtnexus\\development\\modules\\main\\tcard\\web\\tradecard\\en\\administration";
		File directory = new File(directoryPathToAnalyzeConflicts);
		//go thru full admin site and find common files link with trade site		
		Set<String> setOfLeakedCommonFiles = new HashSet<String>(0);
		Set<String> setOfConflictedTradeFiles = new HashSet<String>(0);
		String startTime = new Date().toString();
		traverseDirAndScan(directory,setOfLeakedCommonFiles,setOfConflictedTradeFiles,tradeFolder);
		String endTime = new Date().toString();
		writeResultToFile(setOfLeakedCommonFiles,"leaked.txt");
		writeResultToFile(setOfConflictedTradeFiles,"trade.txt");
		System.out.println("Start at:"+startTime+" End at:"+endTime);
		System.out.println("Scan Finished!");
		*/
		
		//analyzeTradeConflictPages();
		String directoryPathToAnalyzeConflicts = "C:\\code\\gtnexus\\development\\modules\\main\\tcard\\web\\tradecard\\en\\administration";
		File directory = new File(directoryPathToAnalyzeConflicts);
		List<String> effectedAdminFileList = new ArrayList<String>();
		getAdminPagesWithCommonFileList(directory,effectedAdminFileList);
		
	}
	
}
