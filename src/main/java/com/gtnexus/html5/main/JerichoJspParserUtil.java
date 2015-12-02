package com.gtnexus.html5.main;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import static com.gtnexus.html5.util.HTML5Util.ALIGN;
import static com.gtnexus.html5.util.HTML5Util.ALINK;
import static com.gtnexus.html5.util.HTML5Util.BACKGROUND;
import static com.gtnexus.html5.util.HTML5Util.BGCOLOR;
import static com.gtnexus.html5.util.HTML5Util.BORDER;
import static com.gtnexus.html5.util.HTML5Util.BORDER_COLLAPSE;
import static com.gtnexus.html5.util.HTML5Util.BORDER_SPACING;
import static com.gtnexus.html5.util.HTML5Util.CELLPADDING;
import static com.gtnexus.html5.util.HTML5Util.CELLSPACING;
import static com.gtnexus.html5.util.HTML5Util.CLEAR;
import static com.gtnexus.html5.util.HTML5Util.COLOR;
import static com.gtnexus.html5.util.HTML5Util.COMPACT;
import static com.gtnexus.html5.util.HTML5Util.FACE;
import static com.gtnexus.html5.util.HTML5Util.FLOAT;
import static com.gtnexus.html5.util.HTML5Util.FONT_SIZE;
import static com.gtnexus.html5.util.HTML5Util.FRAME_BORDER;
import static com.gtnexus.html5.util.HTML5Util.HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.HSPACE;
import static com.gtnexus.html5.util.HTML5Util.LEFT_MARGIN;
import static com.gtnexus.html5.util.HTML5Util.LINE_HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.LINK;
import static com.gtnexus.html5.util.HTML5Util.MARGIN;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_RIGHT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_TOP;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_WIDTH;
import static com.gtnexus.html5.util.HTML5Util.NOSHADE;
import static com.gtnexus.html5.util.HTML5Util.NO_WRAP;
import static com.gtnexus.html5.util.HTML5Util.OVERFLOW;
import static com.gtnexus.html5.util.HTML5Util.PADDING;
import static com.gtnexus.html5.util.HTML5Util.PADDING_BOTTOM;
import static com.gtnexus.html5.util.HTML5Util.PADDING_TOP;
import static com.gtnexus.html5.util.HTML5Util.SCROLLING;
import static com.gtnexus.html5.util.HTML5Util.SIZE;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.TEXT_ALIGN;
import static com.gtnexus.html5.util.HTML5Util.TOP_MARGIN;
import static com.gtnexus.html5.util.HTML5Util.TYPE;
import static com.gtnexus.html5.util.HTML5Util.VALIGN;
import static com.gtnexus.html5.util.HTML5Util.VERTICAL_ALIGN;
import static com.gtnexus.html5.util.HTML5Util.VLINK;
import static com.gtnexus.html5.util.HTML5Util.VSPACE;
import static com.gtnexus.html5.util.HTML5Util.WIDTH;
import static com.gtnexus.html5.util.HTML5Util.formatKey;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.WriterAppender;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.rule.body.BodyBGcolorRule;
import com.gtnexus.html5.rule.body.BodyBackgroundRule;
import com.gtnexus.html5.rule.body.BodyElementFacade;
import com.gtnexus.html5.rule.body.BodyLinkRule;
import com.gtnexus.html5.rule.body.BodyMarginRule;
import com.gtnexus.html5.rule.body.br.BrClearRule;
import com.gtnexus.html5.rule.body.font.FontFaceRule;
import com.gtnexus.html5.rule.body.hr.HrNoShadeRule;
import com.gtnexus.html5.rule.body.iframe.IframeScrollRule;
import com.gtnexus.html5.rule.body.img.ImageAlignRule;
import com.gtnexus.html5.rule.body.input.InputSpacingRule;
import com.gtnexus.html5.rule.body.li.LiTypeRule;
import com.gtnexus.html5.rule.body.table.TableAlignRule;
import com.gtnexus.html5.rule.body.table.TableBgRule;
import com.gtnexus.html5.rule.body.table.TableBorderRule;
import com.gtnexus.html5.rule.body.table.TableCellPaddingRule;
import com.gtnexus.html5.rule.body.table.TableCellSpacingRule;
import com.gtnexus.html5.rule.body.table.TableStyleRule;
import com.gtnexus.html5.rule.body.table.TableWidthRule;
import com.gtnexus.html5.rule.body.table.td.TableDataAlignRule;
import com.gtnexus.html5.rule.body.table.td.TableDataBgColorRule;
import com.gtnexus.html5.rule.body.table.td.TableDataHeightRule;
import com.gtnexus.html5.rule.body.table.td.TableDataNoWrapRule;
import com.gtnexus.html5.rule.body.table.td.TableDataValignRule;
import com.gtnexus.html5.rule.body.table.td.TableDataWidthRule;
import com.gtnexus.html5.rule.body.table.tr.TableRowAlignRule;
import com.gtnexus.html5.rule.body.table.tr.TableRowStyleRule;
import com.gtnexus.html5.rule.body.table.tr.TableRowValignRule;
import com.gtnexus.html5.rule.body.ul.UlCompactRule;
import com.gtnexus.html5.rule.common.ColorRule;
import com.gtnexus.html5.rule.header.HeaderElementFacade;
import com.gtnexus.html5.util.DbLogger;
import com.gtnexus.html5.util.HTML5Util;
import com.gtnexus.html5.util.StyleAnalyzer;
import com.gtnexus.html5.util.UsageScanner;

/*
 * A JSP Parser class with static utility methods
 */
public class JerichoJspParserUtil {

	// Create Rules Map
	public static Map<String, Rule> RULES_MAP = new HashMap<String, Rule>();
	
	// Create Styles Map(in line style->class name)
	public static Map<String, String> STYLES_MAP;	
	
	public static Set<String> POSITIONAL_STYLES_SET = new HashSet<String>();
	//public static Set<String> INLINE_STYLES = new HashSet<String>();
	
	public static Set<String> COMMON_INCLUDE_FILE_SET = new HashSet<String>(0);

	public static final Logger logger = Logger
			.getLogger(JerichoJspParserUtil.class);
	public static final DbLogger dbLogger = DbLogger.getInstance();
	private static StringWriter consoleWriter;
	private static WriterAppender appender;
	
	public static Set<String> tag_missing_file_set = new HashSet<String>();

	static {

		// Configure logger
		//BasicConfigurator.configure();
		PropertyConfigurator.configure("log4j.properties");
		logger.debug("Logger configured.");

	}

	public static void clearConsoleWriter() {
		consoleWriter.getBuffer().setLength(0);
	}

	public static String getDebuggerOutput() {
		return consoleWriter.getBuffer().toString();
	}

	// Initialize rules map
	public static void initialize(Boolean isAdminCss) {
		initLogger();
		initRulesMap();
		initCssClassMap(isAdminCss);
		initPositionalStyleSet();
		//enable css replace mode
		HTML5Util.CSS_MODE = HTML5Util.REPLACEWITHCSS;
		//load common include file set to a String list
		//COMMON_INCLUDE_FILE_SET = UsageScanner.populateTextFileLinesToSet("CommonIncludeFileList.txt");
	}
	
	private static void initLogger(){		

		// configure log4j to output logs to the UI
		consoleWriter = new StringWriter();
		appender = new WriterAppender(
				new PatternLayout("%d{ISO8601} %p - %m%n"), consoleWriter);
		appender.setName("CONSOLE_APPENDER");
		// appender.setThreshold(org.apache.log4j.Level.ERROR);
		Logger.getRootLogger().addAppender(appender);			

		// disable dblogger
		dbLogger.enable(false);

		if (dbLogger.isEnabled()) {
			dbLogger.initialize();
			//clear db logs
			dbLogger.clearAllData();
			dbLogger.clearAllErrors();
		}
		dbLogger.clearAllData();
		
	}
	
	private static void initRulesMap(){
		
		logger.debug("Initializing rules map...");
		
		/*
		 * Note - Before you add a new rule class, please check if it is already
		 * implemented.
		 */

		// <table> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, WIDTH),
				new TableWidthRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, BORDER),
				new TableBorderRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, ALIGN),
				new TableAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, CELLPADDING),
				new TableCellPaddingRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, CELLSPACING),
				new TableCellSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, STYLE),
				new TableStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.TABLE, BACKGROUND),
				new TableBgRule());

		// <tr> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.TR, ALIGN),
				new TableRowAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TR, VALIGN),
				new TableRowValignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TR, STYLE),
				new TableRowStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.TR, BGCOLOR),
				new TableDataBgColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.TR, BACKGROUND),
				new TableBgRule());

		// <td> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.TD, ALIGN),
				new TableDataAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, BGCOLOR),
				new TableDataBgColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, HEIGHT),
				new TableDataHeightRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, STYLE),
				new TableStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, WIDTH),
				new TableDataWidthRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, VALIGN),
				new TableDataValignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, NO_WRAP),
				new TableDataNoWrapRule());
		
		//th
		RULES_MAP.put(formatKey(HTMLElementName.TH, STYLE),
				new TableStyleRule());

		// <tbody><thead><tfoot> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.TBODY, ALIGN),
				new TableAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.THEAD, ALIGN),
				new TableAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TFOOT, ALIGN),
				new TableAlignRule());

		RULES_MAP.put(formatKey(HTMLElementName.TBODY, VALIGN),
				new TableDataValignRule());
		RULES_MAP.put(formatKey(HTMLElementName.THEAD, VALIGN),
				new TableDataValignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TFOOT, VALIGN),
				new TableDataValignRule());

		RULES_MAP.put(formatKey(HTMLElementName.TBODY, BACKGROUND),
				new TableDataBgColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.THEAD, BACKGROUND),
				new TableDataBgColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.TFOOT, BACKGROUND),
				new TableDataBgColorRule());

		RULES_MAP.put(formatKey(HTMLElementName.TBODY, STYLE),
				new TableStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.THEAD, STYLE),
				new TableStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.TFOOT, STYLE),
				new TableStyleRule());

		// <hr> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.HR, ALIGN),
				new TableDataAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.HR, COLOR), new ColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.HR, NOSHADE),
				new HrNoShadeRule());
		RULES_MAP.put(formatKey(HTMLElementName.HR, SIZE),
				new TableDataHeightRule());
		RULES_MAP.put(formatKey(HTMLElementName.HR, WIDTH),
				new TableDataWidthRule());
		RULES_MAP.put(formatKey(HTMLElementName.HR, STYLE),
				new TableStyleRule());

		// <h1> to <h6> tag rules
		RULES_MAP.put("HX" + "_" + ALIGN, new TableDataAlignRule());

		// <img> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.IMG, STYLE),
				new TableStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.IMG, ALIGN),
				new ImageAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.IMG, BORDER),
				new TableBorderRule());
		RULES_MAP.put(formatKey(HTMLElementName.IMG, VSPACE),
				new InputSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.IMG, HSPACE),
				new InputSpacingRule());
		RULES_MAP
				.put(formatKey(HTMLElementName.IMG, VLINK), new BodyLinkRule());

		// <br> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.BR, CLEAR), new BrClearRule());

		// <legend> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.LEGEND, ALIGN),
				new TableDataAlignRule());

		// <li> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.LI, TYPE), new LiTypeRule());

		// <ul> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.UL, TYPE), new LiTypeRule());
		RULES_MAP.put(formatKey(HTMLElementName.UL, COMPACT),
				new UlCompactRule());

		// <ol> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.OL, COMPACT),
				new UlCompactRule());

		// <p> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.P, ALIGN),
				new TableDataAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.P, STYLE),
				new TableStyleRule());

		// <pre> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.PRE, WIDTH),
				new TableWidthRule());

		// <caption> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.CAPTION, WIDTH),
				new TableWidthRule());

		// <div> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.DIV, ALIGN),
				new TableDataAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.DIV, STYLE),
				new TableStyleRule());

		// <body> tag rules
		RULES_MAP
				.put(formatKey(HTMLElementName.BODY, LINK), new BodyLinkRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, VLINK),
				new BodyLinkRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, ALINK),
				new BodyLinkRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, TOP_MARGIN),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, MARGIN_WIDTH),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, MARGIN_HEIGHT),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, LEFT_MARGIN),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, BACKGROUND),
				new BodyBackgroundRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, BGCOLOR),
				new BodyBGcolorRule());
		RULES_MAP.put(formatKey(HTMLElementName.BODY, STYLE),
				new TableStyleRule());

		// <input> tag rules
		RULES_MAP.put(formatKey(HTMLElementName.INPUT, VSPACE),
				new InputSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.INPUT, HSPACE),
				new InputSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.INPUT, ALIGN),
				new TableAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.INPUT, STYLE),
				new TableStyleRule());

		// font tag rules
		RULES_MAP.put(formatKey(HTMLElementName.FONT, STYLE),
				new TableStyleRule());
		RULES_MAP
				.put(formatKey(HTMLElementName.FONT, FACE), new FontFaceRule());
		RULES_MAP.put(formatKey(HTMLElementName.FONT, COLOR), new ColorRule());
		RULES_MAP.put(formatKey(HTMLElementName.FONT, FONT_SIZE),
				new ColorRule());

		// iframe rules
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, ALIGN),
				new TableAlignRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, VSPACE),
				new InputSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, HSPACE),
				new InputSpacingRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, MARGIN_WIDTH),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, MARGIN_HEIGHT),
				new BodyMarginRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, FRAME_BORDER),
				new TableBorderRule());
		RULES_MAP.put(formatKey(HTMLElementName.IFRAME, SCROLLING),
				new IframeScrollRule());

		logger.debug("Rules map initialized successfully.");
	}
	
	private static void initCssClassMap(boolean isAdminCss){

		//load from db
		//STYLES_MAP = dbLogger.getCssClasses(isAdminCss);
		
		//load class names dynamically
		STYLES_MAP = new HashMap<String,String>(0);
		String HTML5_POSITIONAL_CLASS = "html5-trade-pos-";
		String HTML5_INLINE_CLASS = "html5-trade-inline-";
		int i,j;
		i=j=0;
		//load positional class names
		Set<String> positional_style_set = UsageScanner.populateTextFileLinesToSet("positional.txt");
		for(String style:positional_style_set){
			STYLES_MAP.put(style, HTML5_POSITIONAL_CLASS+(i++));
		}
		
		//load in line class names
		Set<String> inline_style_set = UsageScanner.populateTextFileLinesToSet("inline.txt");
		for(String style:inline_style_set){
			STYLES_MAP.put(style, HTML5_INLINE_CLASS+(j++));
		}
		
		//dbLogger.writeDynamicStyleMapToDB(true);
		
		//generate css file from the map
		Set<String> cssEntrySet = new HashSet<String>();
		Set<Entry<String,String>> entrySet = STYLES_MAP.entrySet();
		for(Entry<String,String> entry:entrySet){
			//remove escape chars from style e.g font-family: \'helvetica neue\',helvetica,arial,sans-serif;
			String style = entry.getKey().replaceAll("\\\\'", "'");
			cssEntrySet.add("."+entry.getValue()+" { "+style+" }\n");
		}
		UsageScanner.writeResultToFile(cssEntrySet,"html5-trade-style.css");
	}
	
	private static void initPositionalStyleSet(){
		
		POSITIONAL_STYLES_SET.add(WIDTH);
		POSITIONAL_STYLES_SET.add(HEIGHT);
		POSITIONAL_STYLES_SET.add(CELLSPACING);
		POSITIONAL_STYLES_SET.add(BORDER);
		POSITIONAL_STYLES_SET.add(BORDER_SPACING);
		POSITIONAL_STYLES_SET.add(CELLPADDING);
		POSITIONAL_STYLES_SET.add(PADDING);
		POSITIONAL_STYLES_SET.add(PADDING_BOTTOM);
		POSITIONAL_STYLES_SET.add(PADDING_TOP);
		POSITIONAL_STYLES_SET.add(MARGIN_LEFT);
		POSITIONAL_STYLES_SET.add(MARGIN_RIGHT);
		POSITIONAL_STYLES_SET.add(MARGIN);
		POSITIONAL_STYLES_SET.add(ALIGN);
		POSITIONAL_STYLES_SET.add(TEXT_ALIGN);
		POSITIONAL_STYLES_SET.add(VALIGN);
		POSITIONAL_STYLES_SET.add(VERTICAL_ALIGN);
		POSITIONAL_STYLES_SET.add(BORDER_COLLAPSE);
		POSITIONAL_STYLES_SET.add(LINE_HEIGHT);
		POSITIONAL_STYLES_SET.add(FLOAT);
		POSITIONAL_STYLES_SET.add(CLEAR);
		POSITIONAL_STYLES_SET.add(TOP_MARGIN);
		POSITIONAL_STYLES_SET.add(MARGIN_HEIGHT);
		POSITIONAL_STYLES_SET.add(MARGIN_WIDTH);
		POSITIONAL_STYLES_SET.add(LEFT_MARGIN);
		POSITIONAL_STYLES_SET.add(MARGIN_TOP);
		POSITIONAL_STYLES_SET.add(VSPACE);
		POSITIONAL_STYLES_SET.add(HSPACE);
		POSITIONAL_STYLES_SET.add(NO_WRAP);
		POSITIONAL_STYLES_SET.add("padding-right");
		POSITIONAL_STYLES_SET.add("padding-left");
		POSITIONAL_STYLES_SET.add("display");
		POSITIONAL_STYLES_SET.add("max-width");
		POSITIONAL_STYLES_SET.add("word-wrap");
		POSITIONAL_STYLES_SET.add("word-break");
		POSITIONAL_STYLES_SET.add("position");
		POSITIONAL_STYLES_SET.add("right");
		POSITIONAL_STYLES_SET.add("left");
		POSITIONAL_STYLES_SET.add("top");
		POSITIONAL_STYLES_SET.add("bottom");
		POSITIONAL_STYLES_SET.add("z-index");
		POSITIONAL_STYLES_SET.add("min-width");
		POSITIONAL_STYLES_SET.add("white-space");
		POSITIONAL_STYLES_SET.add("table-layout");
		POSITIONAL_STYLES_SET.add("margin-bottom");
		POSITIONAL_STYLES_SET.add("visibility");

		//TODO add all the positional styles here
		
		//rest of the css styles consider as inline styles
	}	
	
	/*
	 * public method that convert given JSP file to HTML5 compliant JSP file
	 */
	public static void convertToHTML5(String filePath, boolean isIncludeFile, String textfileName)
	throws FileNotFoundException, IOException, HTML5ParserException {

		File sourceFile = new File(filePath);
		//first check if the input file exist and then proceed. 
		//Bug: include file not exist
		//Ignore custom PDF jsps
		if(sourceFile.exists() && !sourceFile.getName().contains("PDF")){
			// Parse JSP file and remove obsolete html5 tags and apply relevant
			// workaround.
	
			dbLogger.insertPage(filePath, isIncludeFile, textfileName,true);
			if (!isIncludeFile) {
				logger.info("Input File: " + filePath);
			} else {
				logger.info("Include File: " + filePath);
			}
	
			
			Source source = new Source(new FileInputStream(sourceFile));
			//new output document generated from the source document
			OutputDocument outputDocument = new OutputDocument(source);
			//set current parsing file to style analyzer
			StyleAnalyzer.currentFile = sourceFile;
			
			// this should be called in order to call getParentElement() method
			source.fullSequentialParse();
	
			if (!HTML5Util.isPhase3Html5ConvertedPage(source)) {
				//backup the original file before convert
				RevertBackChanges.backupOriginalFileToLocalDisk(sourceFile);
				
				int numOfConvertedIncludeFiles = 0;		
	
				logger.debug("Conversion started...");
	
				Set<String> includeFilePathSet = HTML5Util.getIncludeFilePaths(filePath,source,outputDocument);
				
				printIncludeFiles(includeFilePathSet);
				
				HeaderElementFacade.fixHeaderElementObsoleteFeatures(source,outputDocument);	
				BodyElementFacade.fixAllBodyElementObsoleteFeatures(source,	outputDocument);
	
				// recursively convert include files
				for (String includeFilePath : includeFilePathSet) {
	
					// Catch if any exception occurred and proceed with the other
					// include files.
					// It will allows to save other include files without breaking
					// the program.
					try {
						convertToHTML5(includeFilePath, true, textfileName);
						numOfConvertedIncludeFiles = numOfConvertedIncludeFiles + 1;
					} catch (HTML5ParserException e) {
						e.printStackTrace();
						logger.error(e.getMessage());
						dbLogger.logError(includeFilePath, e.getType(), e.getMessage(),
								e.getTagInfo());
	
					}
	
				}
	
				// check if all the include files have converted successfully
				// before save
				if (includeFilePathSet.size() == numOfConvertedIncludeFiles) {
					//check with source file to make sure that output document is not missing any original html/jsp tags after the conversion
					try{
						if (HTML5Util.isCommonTagsCountMatch(source, outputDocument)) {
							// overwrite source file with new output doc
							saveOutputDoc(sourceFile, outputDocument);
							logger.info(filePath + " converted successfully!");
		
						} else {
						
						}
					//catch exception if tag missing
					}catch(HTML5ParserException e){
						logger.error("Tag Error:"+e.getMessage()+" - "+filePath);
						tag_missing_file_set.add(e.getTagInfo()+":"+filePath);
						//System.out.println("Tag missing error: "+filePath);
						//remove exception throw code and allow to save the page even with tag missing error.
						throw e;
						
						/*Following pages throws the exception. Please check them in browser after conversion  
						 *  labeldefinitionedit.include.jsp
							ProofOfDeliveryEditStep.include.jsp
							factorlist.include.jsp
							securityprofilepermissionlist.include.jsp
							TestDocumentMatching.jsp
						 */				 
						
						//saveOutputDoc(sourceFile, outputDocument);
						//logger.info(filePath + " converted successfully with tag count mismatch errors!");
					}
	
				} else {
					logger.error(filePath
							+ " has not been saved. Errors in include file(s).");
					throw new HTML5ParserException("Content Exception",
							"Include file conversion failed.", null);
	
				}
	
			}else{
				logger.info("This page is already HTML 5!");
			}
		}
	}

	public static void saveOutputDoc(File sourceFile, OutputDocument outputDoc)
			throws IOException {		    

			String newFileName = sourceFile.getAbsolutePath();			
			//save converted jsp output to the disk
			BufferedWriter jspWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(sourceFile), "UTF-8"));	
			jspWriter.write(outputDoc.toString());	
			jspWriter.close();
	}

	public static void printIncludeFiles(Collection<String> includeFilePathList) {

		for (String includeFilePath : includeFilePathList) {

			logger.info(includeFilePath);
			System.out.println(includeFilePath);
		}

	}
	
	public static void main(String args[]){
		
		try {
			JerichoJspParserUtil.initialize(true);
			JerichoJspParserUtil.convertToHTML5("C:\\code\\gtnexus\\devl\\modules\\main\\tcard\\web\\tradecard\\en\\includes\\common\\reportSpecList.include.jsp",false, "Style Analyzer");
		} catch (HTML5ParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
/*		
		File sourceFile = new File("C:\\Users\\lruhunage\\Desktop\\test.jsp");
		Source source;
		try {
			source = new Source(new FileInputStream(sourceFile));
			//new output document generated from the source document
			OutputDocument outputDocument = new OutputDocument(source);
			System.out.println(outputDocument.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}

}
