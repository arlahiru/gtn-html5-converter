package com.gtnexus.html5.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class LinkGrabber {
	private Scanner reader = new Scanner(System.in);
	private String pageTag = "<page>";
	private String pageTagEnd = "</page>";
	private String tabTag = "<tab>";
	private String tabTagEnd = "</tab>";
	private String linksTag = "<links>";
	private String linksTagEnd = "</links>";
	private String chunkTag = "<chunk>";
	private String chunkTagEnd = "</chunk>";
	private String endTag = "<end>";
	private String br = "\r\n";
	private String brBr = "\r\n\r\n";
	private String domain = "http://commerce.qa2.tradecard.com/en/";
	String whiteSpacesOrLineBreaks = "(\n)|(\\s)";
	String defaultInputFile = "C:/dev/Converter/gtn-html5-converter/LinkInput.txt";
	String defaultOutputFile = "C:/dev/Converter/gtn-html5-converter/LinkOutput.txt";
	String basePath = "C:/code/gtnexus/development/modules/main/tcard/web/tradecard/en/";
	
	
	private StringBuffer buffer = new StringBuffer();
	
	private void checkFile(String filename) throws FileNotFoundException{
		File f = new File(filename);
		if(!f.exists()) throw new FileNotFoundException();
		
	}
	
	public boolean checkFile(String fileName,String basePath) {

		File f = new File(basePath + fileName);		
		return (f.exists());
	}
	
	private String getFileContent(String filename){	
		StringBuilder fileContent = new StringBuilder();
		FileReader inputFile = null;
		try {
			inputFile = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = null;
		try {

			if (inputFile != null) {

				br = new BufferedReader(inputFile);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					fileContent.append(sCurrentLine);
				}

				inputFile.close();

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
	private String deletePart(String fileContent,String tag){
		return fileContent.substring(fileContent.indexOf(tag)+tag.length(),fileContent.length());
	}
	
	private void bufferContent(String fileContent){
		String tempString = fileContent.replace(whiteSpacesOrLineBreaks,"").toString();
		HashMap<String,String> urls = new HashMap<String,String>();
		if(!tempString.equals(endTag)){
			buffer.append(fileContent.substring(fileContent.indexOf(pageTag)+pageTag.length(), fileContent.indexOf(pageTagEnd)));
			fileContent = deletePart(fileContent,pageTagEnd);
			buffer.append(br);
			buffer.append(fileContent.substring(fileContent.indexOf(tabTag)+tabTag.length(), fileContent.indexOf(tabTagEnd)));
			fileContent = deletePart(fileContent,tabTagEnd);
			buffer.append(brBr);
			
			if(fileContent.startsWith(linksTag))fileContent = deletePart(fileContent,linksTag);
			try{
				
				while(!fileContent.startsWith(linksTagEnd)){
					fileContent = deletePart(fileContent,domain);
					
					String current = fileContent.substring(0,fileContent.indexOf("http://"));
					if(current.contains(linksTagEnd)) {
						current = fileContent.substring(0,fileContent.indexOf(linksTagEnd));
						fileContent = fileContent.substring(fileContent.indexOf(linksTagEnd),fileContent.length());
					}
					else fileContent = fileContent.substring(fileContent.indexOf("http://"),fileContent.length());
					current = removeParameters(current);		
					try{
						urls.put(current,current);
					}catch(Exception e){
						
					}
					
				}
			}catch(Exception e){
				String current = removeParameters(fileContent.substring(0,fileContent.indexOf(linksTagEnd)));
				try{
					urls.put(current,current);
				}catch(Exception e2){
					
				}
				fileContent = deletePart(fileContent,linksTagEnd);
			}
			
			 Iterator it = urls.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        String currentJsp = (String)pairs.getValue();
			        if(checkFile(currentJsp,basePath)) 
			        	buffer.append(pairs.getValue()+br);
			        it.remove();
			    }
			    buffer.append(brBr+br);

			
			if(fileContent.contains(chunkTag)) bufferContent(fileContent);
			fileContent = deletePart(fileContent,chunkTagEnd);
		}
		createNewTemplate();
		writeToFile(defaultOutputFile,buffer);
	}
	private String removeParameters(String file){
		return file.substring(0,file.indexOf("?"));
	}
	private void createNewTemplate(){
		StringBuffer templateBuffer = new StringBuffer(chunkTag+br+pageTag+pageTagEnd+br+tabTag+tabTagEnd+br+linksTag+linksTagEnd+br+chunkTagEnd+br+endTag);
		System.out.println(templateBuffer.toString());
		writeToFile(defaultInputFile,templateBuffer);
	}
	private void writeToFile(String filename,StringBuffer content){
		try {
			PrintWriter writer = new PrintWriter(filename);
			writer.println(content.toString());
			writer.close();

		} catch (FileNotFoundException e) {
			
			//printOnConsole("Creating Config file.", "info");
		}
	}
	private String getFileInput(){
		String filename = "";
		try{
			filename = reader.nextLine();
			checkFile(filename);
		}catch(FileNotFoundException e){
			System.out.println("Input file is not valid!!");
			filename = getFileInput();
		}
		return filename;
	}
	
	private void start(){

		bufferContent(getFileContent(defaultInputFile));
		System.out.println(buffer.toString());
		
	}
	public static void main(String[] args) {
		LinkGrabber obj = new LinkGrabber();
		obj.start();
	}

}
