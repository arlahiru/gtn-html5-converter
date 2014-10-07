package com.gtnexus.html5.main;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import static com.gtnexus.html5.util.HTML5Util.ALIGN;
import static com.gtnexus.html5.util.HTML5Util.ALINK;
import static com.gtnexus.html5.util.HTML5Util.BACKGROUND;
import static com.gtnexus.html5.util.HTML5Util.BGCOLOR;
import static com.gtnexus.html5.util.HTML5Util.BORDER;
import static com.gtnexus.html5.util.HTML5Util.CELLPADDING;
import static com.gtnexus.html5.util.HTML5Util.CELLSPACING;
import static com.gtnexus.html5.util.HTML5Util.CLEAR;
import static com.gtnexus.html5.util.HTML5Util.COLOR;
import static com.gtnexus.html5.util.HTML5Util.COMPACT;
import static com.gtnexus.html5.util.HTML5Util.FACE;
import static com.gtnexus.html5.util.HTML5Util.FONT_SIZE;
import static com.gtnexus.html5.util.HTML5Util.FRAME_BORDER;
import static com.gtnexus.html5.util.HTML5Util.HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.HSPACE;
import static com.gtnexus.html5.util.HTML5Util.LEFT_MARGIN;
import static com.gtnexus.html5.util.HTML5Util.LINK;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_WIDTH;
import static com.gtnexus.html5.util.HTML5Util.NOSHADE;
import static com.gtnexus.html5.util.HTML5Util.NO_WRAP;
import static com.gtnexus.html5.util.HTML5Util.SCROLLING;
import static com.gtnexus.html5.util.HTML5Util.SIZE;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.TOP_MARGIN;
import static com.gtnexus.html5.util.HTML5Util.TYPE;
import static com.gtnexus.html5.util.HTML5Util.VALIGN;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
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
import com.gtnexus.html5.rule.body.table.td.TableDataStyleRule;
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

/*
 * A JSP Parser class with static utility methods
 */
public class JerichoJspParserUtil {

	// Create Rules Map
	public static Map<String, Rule> RULES_MAP = new HashMap<String, Rule>();

	public static final Logger logger = Logger
			.getLogger(JerichoJspParserUtil.class);
	public static final DbLogger dbLogger = DbLogger.getInstance();
	private static StringWriter consoleWriter;
	private static WriterAppender appender;

	static {

		// Configure logger
		BasicConfigurator.configure();
		logger.debug("Logger configured.");

	}

	public static void clearConsoleWriter() {
		consoleWriter.getBuffer().setLength(0);
	}

	public static String getDebuggerOutput() {
		return consoleWriter.getBuffer().toString();
	}

	// Initialize rule map
	public static void initialize() {


		// configure log4j to output logs to the UI
		consoleWriter = new StringWriter();
		appender = new WriterAppender(
				new PatternLayout("%d{ISO8601} %p - %m%n"), consoleWriter);
		appender.setName("CONSOLE_APPENDER");
		// appender.setThreshold(org.apache.log4j.Level.ERROR);
		Logger.getRootLogger().addAppender(appender);

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
				new TableDataStyleRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, WIDTH),
				new TableDataWidthRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, VALIGN),
				new TableDataValignRule());
		RULES_MAP.put(formatKey(HTMLElementName.TD, NO_WRAP),
				new TableDataNoWrapRule());

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

		// disable dblogger
		dbLogger.enable(true);

		if (dbLogger.isEnabled()) {
			dbLogger.initialize();
		}

	}

	/*
	 * public method that convert given JSP file to HTML5 ready JSP file
	 */
	public static void convertToHTML5(String filePath, boolean isIncludeFile,String textfileName)

			throws FileNotFoundException, IOException, HTML5ParserException{
		

		// Parse JSP file and remove obsolete html5 tags and apply relevant
		// workaround.

		dbLogger.insertPage(filePath, isIncludeFile, textfileName);
		if (!isIncludeFile) {
			logger.info("Input File: " + filePath);
		} else {

			logger.info("Include File: " + filePath);
		}

		File sourceFile = new File(filePath);

		Source source = new Source(new FileInputStream(sourceFile));

		// this should be called in order to use getParentElement() method
		source.fullSequentialParse();

		if (!HTML5Util.isHtml5Page(source)) {

			int numOfConvertedIncludeFiles = 0;

			// get include file paths
			List<String> includeFilePathList = HTML5Util
					.getIncludeFilePaths(filePath);

			printIncludeFiles(includeFilePathList);

			logger.debug("Conversion started...");

			OutputDocument outputDocument = new OutputDocument(source);

			HeaderElementFacade.fixHeaderElementObsoleteFeatures(source,
					outputDocument, isIncludeFile);

			BodyElementFacade.fixAllBodyElementObsoleteFeatures(source,
					outputDocument);

			// recursively convert include files
			for (String includeFilePath : includeFilePathList) {


				// Catch if any exception occurred and proceed with the other
				// include files.
				// It will allows to save other include files without breaking
				// the program.
				try {
					convertToHTML5(includeFilePath, true,textfileName);
					numOfConvertedIncludeFiles = numOfConvertedIncludeFiles + 1;
				} catch (HTML5ParserException e) {
					e.printStackTrace();
					dbLogger.logError(filePath, e.getType(), e.getMessage(), e.getTagInfo());

				}

			}

			// check if all the include files have converted successfully
			// before saving
			if (includeFilePathList.size() == numOfConvertedIncludeFiles) {

				if (HTML5Util.isCommonTagsCountMatch(source, outputDocument)) {
					// overwrite source file with new output doc
					saveOutputDoc(sourceFile, outputDocument);

					logger.info(filePath + " converted successfully!");

				} else {

					logger.error(filePath
							+ " has not been saved. Source and output elements not matched!");

					throw new HTML5ParserException("Content Exception","File Formatting Error: Tags Missing",null);


				}

			} else {

				logger.error(filePath
						+ " has not been saved. Errors in include file(s).");

				throw new HTML5ParserException("Content Exception","Include file conversion failed.",null);


			}

		} else {

			logger.info("This page is already a HTML 5 page!");

		}

	}

	public static void saveOutputDoc(File sourceFile, OutputDocument outputDoc)
			throws IOException {   

		// File output = new File("tmp\\login_modified2.jsp");

		// overwrite final output jsp on the disk
		BufferedWriter jspWriter = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(sourceFile), "UTF-8"));

		jspWriter.write(outputDoc.toString());

		jspWriter.close();

	}

	public static void printIncludeFiles(List<String> includeFilePathList) {

		logger.info("Include File list:");

		// recursively convert include files
		for (String includeFilePath : includeFilePathList) {

			logger.info(includeFilePath);
		}

	}

}
