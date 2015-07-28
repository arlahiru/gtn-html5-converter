package com.gtnexus.html5.util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Tag;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;

public class StyleAnalyzer {
	
	//hold currently parsing file name
	public static File currentFile = null;
	
	public static void recordInlineStyle(String newElement,Tag origElement){
		
		Pattern pattern = Pattern.compile("style=\\s*\"(.*?)\"\\s*");
		Matcher matcher = pattern.matcher(newElement);
		String styleValue=null;
		StringBuffer inlineStyles = new StringBuffer();
		StringBuffer positionalStyles = new StringBuffer();
		
		if (matcher.find()) {			
			styleValue = matcher.group(1);
			if(styleValue != null && !styleValue.isEmpty()){				
				breakdownToInlineAndPositionalCssStyles(styleValue,inlineStyles,positionalStyles);
				//log inline and positional css style break downs to db ==> this is for admin site styles. make last flag false when analyze trade site styles
				if(inlineStyles.length() > 0){
					dbLogger.insertInlineStyle(currentFile.getAbsolutePath(), origElement.getName(), inlineStyles.toString(), origElement.getDebugInfo(),false,true);
				}
				if(positionalStyles.length() > 0){
					dbLogger.insertInlineStyle(currentFile.getAbsolutePath(), origElement.getName(), positionalStyles.toString(), origElement.getDebugInfo(),true,true);
				}
			}
		}
	}
	
	public static void breakdownToInlineAndPositionalCssStyles(String styleValue, StringBuffer inlineStyles, StringBuffer positionalStyles){
		
		String[] styleArray = styleValue.split(";");
		String cssAttributeName=null;
		for (int i = 0; i < styleArray.length; i++) {
			if (styleArray[i] != null && !styleArray[i].isEmpty() && styleArray[i].split(":").length == 2) {
				cssAttributeName = styleArray[i].trim().toLowerCase().split(":")[0];
				//check is this a positional style
				if(JerichoJspParserUtil.POSITIONAL_STYLES_SET.contains(cssAttributeName)){
					positionalStyles.append(styleArray[i]).append(";");
				}else{
					inlineStyles.append(styleArray[i]).append(";");
				}
			}
		}
	}
	
	public static void main(String[] args) {

		String directoryPathToAnalyzeStyles = "C:\\code\\gtnexus\\development\\modules\\main\\tcard\\web\\tradecard\\en\\administration";

		File directory = new File(directoryPathToAnalyzeStyles);
		
		JerichoJspParserUtil.initialize(true);
		
		dbLogger.clearAllData();
		
		HTML5Util.MODE = HTML5Util.STYLEANALYZE;
		
		testAnalyzeNewHTML5Styles(directory);

	}
	
	public static void testAnalyzeNewHTML5Styles(File directory){

		for (final File file : directory.listFiles()) {
			if (file.isDirectory()) {
				testAnalyzeNewHTML5Styles(file);

			} else {
				//analyze new html5 styles of all the jsp and html files in the current directory
				if ((file.getName().toLowerCase().endsWith(".jsp")|| file.getName().toLowerCase().endsWith(".html"))) {
					
					try {
						currentFile = file;
						JerichoJspParserUtil.convertToHTML5(file.getPath(),
								false, "Style Analyzer");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	


}
