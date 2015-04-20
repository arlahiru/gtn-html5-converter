package com.gtnexus.html5.util;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Tag;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;

public class StyleAnalyzer {
	
	private static File currentFileName = null;

	public static void main(String[] args) {

		String directoryPathToAnalyzeStyles = "C:\\code\\gtnexus\\development\\modules\\main\\tcard\\web\\tradecard\\en\\administration\\pages\\Price";

		File directory = new File(directoryPathToAnalyzeStyles);
		
		JerichoJspParserUtil.initialize(true);
		
		dbLogger.clearAllData();
		
		HTML5Util.MODE = HTML5Util.STYLEANALYZE;
		
		analyzeNewHTML5Styles(directory);

	}
	
	public static void analyzeNewHTML5Styles(File directory){

		for (final File file : directory.listFiles()) {
			if (file.isDirectory()) {
				analyzeNewHTML5Styles(file);

			} else {
				//analyze new html5 styles of all the jsp and html files in the current directory
				if ((file.getName().toLowerCase().endsWith(".jsp")
						|| file.getName().toLowerCase()
								.endsWith(".html"))) {
					
					try {
						currentFileName = file;
						JerichoJspParserUtil.convertToHTML5(file.getPath(),
								false, "Style Analyzer");
					} catch (HTML5ParserException e) {
						e.printStackTrace();
					}
					 catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		
	}
	
	public static void recordInlineStyle(String newElement,Tag origElement){
		
		Pattern pattern = Pattern.compile("style=\\s*\"(.*?)\"\\s*");
		Matcher matcher = pattern.matcher(newElement);

		String styleValue=null;
		
		if (matcher.find()) {			
			styleValue = matcher.group(1);
			if(styleValue != null && !styleValue.isEmpty()){
				dbLogger.insertInlineStyle(currentFileName.getAbsolutePath(), origElement.getName(), styleValue, origElement.getDebugInfo(),true);
			}
		}
		
	}

}
