package com.gtnexus.html5.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;
import net.htmlparser.jericho.Tag;

import org.apache.commons.lang3.StringUtils;

import sun.font.EAttribute;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;

public class HTML5Util {

	// HTML 5 doctype
	public final static String DOCTYPE_HTML5 = "<!DOCTYPE html>";

	// charset meta tag
	public final static String META_CHARSET_UTF8 = "<meta charset=\"utf-8\">";

	// HTML 5 converted meta tag
	public final static String META_IS_HTML5 = "<meta isHtml5Page=\"true\">";

	// HTML 5 converted comment tag
	public final static String HTML5_CONVERTED_COMMENT_PHASE1 = "<!-- HTML5 Converted Page:Phase1 -->";
	public final static String HTML5_CONVERTED_COMMENT_PHASE2 = "<!-- HTML5 Converted Page:Phase 2 -->";

	// HTML AND CSS ATTRIBUTE NAMES
	public final static String WIDTH = "width";
	public final static String HEIGHT = "height";
	public final static String BORDER = "border";
	public final static String CELLSPACING = "cellspacing";
	public final static String BORDER_SPACING = "border-spacing";
	public final static String CELLPADDING = "cellpadding";
	public final static String PADDING = "padding";
	public final static String PADDING_BOTTOM = "padding-bottom";
	public final static String PADDING_TOP = "padding-top";
	public final static String MARGIN_LEFT = "margin-left";
	public final static String MARGIN_RIGHT = "margin-right";
	public final static String MARGIN = "margin";
	public final static String ALIGN = "align";
	public final static String TEXT_ALIGN = "text-align";
	public final static String VALIGN = "valign";
	public final static String VERTICAL_ALIGN = "vertical-align";
	public final static String BORDER_COLLAPSE = "border-collapse";
	public final static String FONT_SIZE = "font-size";
	public final static String LINE_HEIGHT = "line-height";
	public final static String CLASS = "class";
	public final static String STYLE = "style";
	public final static String FLOAT = "float";
	public final static String BGCOLOR = "bgcolor";
	public final static String BACKGROUND_COLOR = "background-color";
	public final static String BACKGROUND_IMAGE = "background-image";
	public final static String URL = "url";
	public final static String BACKGROUND = "background";
	public final static String NO_WRAP = "nowrap";
	public final static String WHITE_SPACE = "white-space";
	public final static String COLOR = "color";
	public final static String BORDER_WIDTH = "border-width";
	public final static String NOSHADE = "noshade";
	public final static String LINK = "link";
	public final static String FONT_FAMILY = "font-family";
	public final static String FACE = "face";
	public final static String FRAME_BORDER = "frameborder";

	public final static String CLEAR = "clear";
	public final static String LIST_STYLE_TYPE = "list-style-type";
	public final static String TYPE = "type";
	public final static String COMPACT = "compact";

	public final static String ALINK = "alink";
	public final static String VLINK = "vlink";
	public final static String TOP_MARGIN = "topmargin";
	public final static String MARGIN_HEIGHT = "marginheight";
	public final static String MARGIN_WIDTH = "marginwidth";
	public final static String LEFT_MARGIN = "leftmargin";
	public final static String MARGIN_TOP = "margin-top";
	public final static String HSPACE = "hspace";
	public final static String VSPACE = "vspace";
	public final static String SIZE = "size";
	public final static String OVERFLOW = "overflow";
	public final static String SCROLLING = "scrolling";

	// HTML AND CSS TAG NAMES
	public final static String SPAN = "span";
	public final static String FONT = "font";
	public final static String IMG = "img";
	public final static String TABLE = "table";
	public final static String THEAD = "thead";
	public final static String TFOOT = "tfoot";
	public final static String TBODY = "tbody";
	public final static String TR = "tr";
	public final static String TD = "td";
	public final static String TH = "th";
	public final static String DIV = "div";
	public final static String FORM = "form";
	public final static String HR = "hr";
	public final static String BR = "br";
	public final static String NO_BR = "nobr";
	public final static String SPACER = "spacer";
	public final static String ANCHOR = "a";
	public final static String ACTIVE_LINK = "a:active";
	public final static String VISITED_LINK = "a:visited";

	// HTML/CSS STYLE VALUES
	public final static String COLLAPSE = "collapse";
	public final static String SEPERATE = "seperate";
	public final static String LEFT = "left";
	public final static String RIGHT = "right";
	public final static String AUTO = "auto";
	public final static String CENTER = "center";
	public final static String BOTTOM = "bottom";
	public final static String TOP = "top";
	public final static String MIDDLE = "middle";
	public final static String BOTH = "both";
	public final static String SQUARE = "square";
	public final static String SCROLL = "scroll";
	public final static String HIDDEN = "hidden";

	// Measurements
	public final static String PX = "px";
	public final static String EM = "em";
	public final static String PERCENT = "%";

	// CSS colors
	public final static String WHITE = "white";
	
	//converter running mode flags
	public final static String DEFAULT = "default";
	public final static String STYLEANALYZE = "styleanalyze";
	public static String MODE = DEFAULT;
	public final static String H5_EXTENSION = "_h5.jspf";

	// HTML ELEMENT NAMES
	
	// check this page already a html5 page by looking at the meta tag
		public static boolean isPhase1Html5ConvertedPage(Source source) {

			// get all meta tags
			List<Element> allCommentTags = source
					.getAllElements(StartTagType.COMMENT);

			for (Element commentTag : allCommentTags) {

				if (commentTag.toString().equals(HTML5_CONVERTED_COMMENT_PHASE1)) {

					return true;
				}
			}

			return false;

		}

	// check this page already a html5 page by looking at the meta tag
	public static boolean isPhase2Html5ConvertedPage(Source source) {

		// get all meta tags
		List<Element> allCommentTags = source
				.getAllElements(StartTagType.COMMENT);

		for (Element commentTag : allCommentTags) {

			if (commentTag.toString().equals(HTML5_CONVERTED_COMMENT_PHASE2)) {

				return true;
			}
		}

		return false;

	}

	// check given attribute value has a unit
	public static boolean hasUnit(String attributeValue) {
		
		attributeValue = attributeValue.toLowerCase();

		if (attributeValue.endsWith("%") || attributeValue.endsWith("in")
				|| attributeValue.endsWith("cm")
				|| attributeValue.endsWith("mm")
				|| attributeValue.endsWith("em")
				|| attributeValue.endsWith("ex")
				|| attributeValue.endsWith("pt")
				|| attributeValue.endsWith("pc")
				|| attributeValue.endsWith("px"))
			return true;
		else
			return false;
	}

	// print all elements in the source file
	public static void showAllElements(Source source) {

		List<Element> allElements = source.getAllElements(NO_BR);

		for (Element e : allElements) {

			System.out.println(e);

		}

	}

	// string formatting utility methods
	public static String parentheses(String value) {
		return "(" + value + ")";
	}

	public static String addSemicolon(String statement) {
		return statement + ";";
	}

	public static String addColon(String attribute, String value) {
		return attribute + ":" + value;
	}

	public static String braces(String statement) {
		return "{" + statement + "}";
	}

	public static String formatAttribute(String attribute, String value) {
		return addSemicolon(addColon(attribute, value));
	}

	public static String cssElementFix(String element, String attribute,
			String value) {
		return element + braces(formatAttribute(attribute, value));
	}

	public static String cssElementFix(String element, String[] attributes,
			String[] values) {
		StringBuilder fixed = new StringBuilder(element + "{");

		for (int i = 0; i < attributes.length; i++) {
			fixed.append(formatAttribute(attributes[i], values[i]));
		}
		fixed.append("}");
		return fixed.toString();
	}

	public static String appendPx(String value) {
		if (!hasUnit(value))
			return value + PX;
		return value;
	}

	public static String formatKey(String element, String attribute) {
		return element + "_" + attribute;
	}

	public static String getAttributeName(String keyValuePair) {
		return keyValuePair.substring(0, keyValuePair.indexOf("="));
	}

	public static String getAttributeValue(String keyValuePair) {
		return keyValuePair.substring(keyValuePair.indexOf("=") + 1,
				keyValuePair.length());
	}

	public static String getValue(ArrayList<String> keyValuePair, String key,
			String defaultValue) {
		for (String keyValue : keyValuePair) {
			if (keyValue.contains(key))
				return appendPx(getAttributeValue(keyValue));
		}
		return appendPx(defaultValue);
	}

	public static ArrayList<String> getAttributeValues(Element element,
			String[] attributeSet) {
		ArrayList<String> attributesAndValues = new ArrayList<String>();
		for (int i = 0; i < attributeSet.length; i++) {
			if (element.getAttributes().get(attributeSet[i]) != null) {

				attributesAndValues.add(attributeSet[i]
						+ "="
						+ element.getAttributes().get(attributeSet[i])
								.getValue());
			}
		}
		return attributesAndValues;
	}

	public static String generateMultiValuedAttribute(String attribute,
			ArrayList<String> keyValuePairs, String defaultValue, String[] order) {
		StringBuilder statement = new StringBuilder();
		for (String argOrder : order) {
			statement.append(getValue(keyValuePairs, argOrder, defaultValue)
					+ " ");
		}
		return addColon(attribute, statement.toString() + ";");
	}

	public static String makeStartTag(String type) {
		return "<" + type + " style=\"";
	}

	/**
	 * 
	 * @param mainFilePath
	 * @param outputDoc - pass null for output document to get only the include file list
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Set<String> getIncludeFilePathsAndReplaceWithH5ExtensionInOutputDoc(String mainFilePath, OutputDocument outputDoc)
			throws FileNotFoundException, IOException {

		File sourceFile = new File(mainFilePath);

		Source source = new Source(new FileInputStream(sourceFile));

		Set<String> includeFilePaths = new HashSet<String>(0);

		// get all server tags <% %>
		List<Element> allElements = source
				.getAllElements(StartTagType.SERVER_COMMON);

		for (Element e : allElements) {

			String tagContent = e.toString();

			// find include tags
			if (tagContent.contains("<%@") && tagContent.toLowerCase().contains("include")) {

				// extract file attribute value by matching value between
				// " "
				Pattern pattern = Pattern.compile("\"(.*?)\"");
				Matcher matcher = pattern.matcher(tagContent);
				if (matcher.find()) {
					
					//this file name may contains ../ or ./ at the begin
					String includeFileName = matcher.group(1);
					
					//check if the include file is a jsp
					if(includeFileName.endsWith(".jsp")){

						int numOfDirectoriesBack = StringUtils.countMatches(
								includeFileName, "../");
	
						String parentJspAbsolutePath = sourceFile.getAbsolutePath();
	
						Path parentFolderPath = Paths.get(parentJspAbsolutePath)
								.getParent();
	
						Path includeFileFolderPath = parentFolderPath;
	
						// construct include file parent directory
						for (int i = 0; i < numOfDirectoriesBack; i++) {
	
							includeFileFolderPath = includeFileFolderPath
									.getParent();
						}				
						
						// remove ../ from the path and assign to onlyTheIncludeFileName
						String onlyTheIncludeFileName = includeFileName.replace("../", "");
	
						// remove ./ from the onlyTheIncludeFileName
						onlyTheIncludeFileName = onlyTheIncludeFileName.replace("./", "");
	
						String includeFilePath = includeFileFolderPath + File.separator + onlyTheIncludeFileName;
						
						//add this file to include file list
						includeFilePaths.add(includeFilePath);
						
						//check if this include file link with both admin and trade stack
						if(outputDoc != null && isCommonJspFile(formatToWindowsPath(HTML5Util.filePathToLowercase(includeFilePath)))){
								//if this include file is a common file in admin and trade then change the extension to h5.jsp								
								//replace include file name with fileName.h5.jsp extension in the output document - This should only applied when Admin stack conversion.
								//TODO This should be ROLLBACK after the trade stack conversion
								String newIncludeFileName = includeFileName.replace(".jsp", HTML5Util.H5_EXTENSION);
								String newIncludeTagWithModifiedExtension = "<%@ include file=\""+newIncludeFileName+"\" %>";
								//replace include tag in the output doc with modified file extension
								outputDoc.replace(e, newIncludeTagWithModifiedExtension);
							}
						}						

				} else {

					System.out
							.println("No file attribute inside the include tag! "
									+ e.getDebugInfo());
				}

			}

		}

		return includeFilePaths;
	}
	
	public static boolean hasAttributes(Element e) {

		return e.getAttributes() == null ? false : true;

	}

	// e.g <td <%=containerPlugIn.getCellStyle(rowIndex, columnIndex)%> >... </td>
	public static String getInnerServerTagContent(Element e) {

		//TODO jsp java code bug e.g id="<%= "fieldListRow_" + substitutionNamer.name() %>"
		List<String> quotedDynamicJavaCodeList = new ArrayList<String>(0);

		String startTagContent = e.getStartTag().toString();

		Pattern serverCodeQuotePattern = Pattern.compile("['\"]\\s*<%=\\s*(.*?)\\s*%>\\s*['\"]");
		Matcher quoteMatcher = serverCodeQuotePattern.matcher(startTagContent);

		while (!quoteMatcher.hitEnd()) {

			if (quoteMatcher.find()) {
				//populate quoted dynamic jsp code content list
				quotedDynamicJavaCodeList.add(quoteMatcher.group(1));
			}

		}

		String serverCode = "";

		Pattern pattern = Pattern.compile("<%=\\s*(.*?)\\s*%>");
		Matcher matcher = pattern.matcher(startTagContent);
		while (!matcher.hitEnd()) {

			if (matcher.find()) {

				// ignore onclick="<%=actionButtonLink%>" like patterns by
				// checking is this code inside quotes.
				if (!isInsideQuotes(quotedDynamicJavaCodeList, matcher.group(1))) {
					// enclose with matching scriptlet tags
					serverCode = serverCode + "<%=" + matcher.group(1) + "%>";
				}
			}
		}

		return serverCode;

	}

	private static boolean isInsideQuotes(List<String> quotedContentList,
			String serverCode) {

		for (String quotedText : quotedContentList) {

			if (quotedText.contains(serverCode))
				return true;
		}

		return false;
	}

	// verify new html output file is not missing any html element from source file by comparing tag counts
	public static boolean isCommonTagsCountMatch(Source source,
			OutputDocument outputDocument) {

		// check if source file basic starting tag count and output starting
		// basic tag count are equals.
		// Be careful when comparing newly added tags and removed tags
		Source output = new Source(outputDocument.toString());
		output.fullSequentialParse();
		
		//check the html5 doctype and verify it's there
		if(!isHTML5DocTypeAddedToMainJsp(outputDocument)){
			System.out.println("HTML5 doctype missing => "+output.getAllElements(HTMLElementName.TITLE));
			throw new HTML5ParserException("Tag Missing Exception","HTML5 doctype missing", "doctype");
		}

		else if (source.getAllElements(HTMLElementName.BODY).size() != output
				.getAllElements(HTMLElementName.BODY).size())
			throw new HTML5ParserException("Tag Missing Exception","Body tag missing", "body");
		else if (source.getAllElements(HTMLElementName.HEAD).size() != output
				.getAllElements(HTMLElementName.HEAD).size())
			throw new HTML5ParserException("Tag Missing Exception","Head tag missing", "head");
		if (source.getAllElements(HTMLElementName.FORM).size() != output
				.getAllElements(HTMLElementName.FORM).size())
			throw new HTML5ParserException("Tag Missing Exception","Form tag missing", "form");
		else if (source.getAllElements(HTMLElementName.TABLE).size() != output
				.getAllElements(HTMLElementName.TABLE).size())
			throw new HTML5ParserException("Tag Missing Exception","Table tag missing", "table");
		else if (source.getAllElements(HTMLElementName.TD).size() != output
				.getAllElements(HTMLElementName.TD).size())
			throw new HTML5ParserException("Tag Missing Exception","TD tag missing", "td");
		//jsp java code bug e.g id="<%= "fieldListRow_" + substitutionNamer.name() %>" returns incorrect tr count in output
		else if (source.getAllElements(HTMLElementName.TR).size() != output
				.getAllElements(HTMLElementName.TR).size())
			throw new HTML5ParserException("Tag Missing Exception","TR tag missing", "tr");
		else if (source.getAllElements(HTMLElementName.TH).size() != output
				.getAllElements(HTMLElementName.TH).size())
			throw new HTML5ParserException("Tag Missing Exception","TH tag missing", "th");
		else if (source.getAllElements(HTMLElementName.THEAD).size() != output
				.getAllElements(HTMLElementName.THEAD).size())
			throw new HTML5ParserException("Tag Missing Exception","THEAD tag missing", "thead");
		else if (source.getAllElements(HTMLElementName.TFOOT).size() != output
				.getAllElements(HTMLElementName.TFOOT).size())
			throw new HTML5ParserException("Tag Missing Exception","TFOOT tag missing", "tfoot");
		else if (source.getAllElements(HTMLElementName.TBODY).size() != output
				.getAllElements(HTMLElementName.TBODY).size())
			throw new HTML5ParserException("Tag Missing Exception","TBODY tag missing", "tbody");
		else if (source.getAllElements(HTMLElementName.INPUT).size() != output
				.getAllElements(HTMLElementName.INPUT).size())
			throw new HTML5ParserException("Tag Missing Exception","INPUT tag missing", "input");
		else if (source.getAllElements(HTMLElementName.IFRAME).size() != output
				.getAllElements(HTMLElementName.IFRAME).size())
			throw new HTML5ParserException("Tag Missing Exception","IFRAME tag missing", "iframe");
		else if (source.getAllElements(HTMLElementName.BR).size() != output
				.getAllElements(HTMLElementName.BR).size())
			throw new HTML5ParserException("Tag Missing Exception","BR tag missing", "br");
		else if (source.getAllElements(HTMLElementName.CAPTION).size() != output
				.getAllElements(HTMLElementName.CAPTION).size())
			throw new HTML5ParserException("Tag Missing Exception","CAPTION tag missing", "caption");
		else if (source.getAllElements(HTMLElementName.HR).size() != output
				.getAllElements(HTMLElementName.HR).size())
			throw new HTML5ParserException("Tag Missing Exception","HR tag missing", "hr");
		else if (source.getAllElements(HTMLElementName.LEGEND).size() != output
				.getAllElements(HTMLElementName.LEGEND).size())
			throw new HTML5ParserException("Tag Missing Exception","LEGEND tag missing", "legend");
		else if (source.getAllElements(HTMLElementName.LI).size() != output
				.getAllElements(HTMLElementName.LI).size())
			throw new HTML5ParserException("Tag Missing Exception","LI tag missing", "li");
		else if (source.getAllElements(HTMLElementName.OL).size() != output
				.getAllElements(HTMLElementName.OL).size())
			throw new HTML5ParserException("Tag Missing Exception","OL tag missing", "ol");
		else if (source.getAllElements(HTMLElementName.P).size() != output
				.getAllElements(HTMLElementName.P).size())
			throw new HTML5ParserException("Tag Missing Exception","P tag missing", "p");
		else if (source.getAllElements(HTMLElementName.PRE).size() != output
				.getAllElements(HTMLElementName.PRE).size())
			throw new HTML5ParserException("Tag Missing Exception","PRE tag missing", "pre");
		else if (source.getAllElements(HTMLElementName.UL).size() != output
				.getAllElements(HTMLElementName.UL).size())
			throw new HTML5ParserException("Tag Missing Exception","UL tag missing", "ul");
		else if ((source.getAllElements(HTMLElementName.SPAN).size() + source.getAllElements(HTMLElementName.FONT).size() + source.getAllElements(HTML5Util.NO_BR).size())!= output
				.getAllElements(HTMLElementName.SPAN).size())
			throw new HTML5ParserException("Tag Missing Exception","SPAN tag missing", "span");
		
		return true;

	}
	
	public static boolean isHTML5DocTypeAddedToMainJsp(OutputDocument outputDocument) {
		
		Source output = new Source(outputDocument.toString());
		
		if(!isPhase1Html5ConvertedPage(output)){
		
			if(!output.getAllElements(HTMLElementName.HTML).isEmpty()){
				List<Element> docTypeList = output.getAllElements(StartTagType.DOCTYPE_DECLARATION);
				if (docTypeList.size() > 0) {
					Element docTypeElement = docTypeList.get(0);
					return docTypeElement.getStartTag().toString().equals(DOCTYPE_HTML5);
				}else{
					return false;
				}
				
			}else{
				//not a main jsp file
				return true;
			}
		}else{
			//phase 1 converted page
			return true;
		}
		
	}
	
	public static boolean containsLinkInTd(Segment td,String cssClass){
		List<Element> immediateElements = td.getChildElements();
		for(Element e : immediateElements){
			
			if(e.getName().equals(ANCHOR) ){
				try{
					if(e.getAttributeValue("class").equals(cssClass)) return true;
				}catch(NullPointerException ex){
					//ex.printStackTrace();
				}
			}
		}
		return false;
	}
	public static boolean isContainAlignableComponent(Segment e) {

		// check element contains table
		boolean hasTable = e.getAllElements(HTML5Util.TABLE).size() > 0;
		boolean hasServerElement = false;

		List<Element> allServerElements = e
				.getAllElements(StartTagType.SERVER_COMMON);

		for (Element serverElement : allServerElements) {

			if (serverElement.toString().contains("include")) {

				// check whether file include a table
				Pattern pattern = Pattern.compile("\"(.*?)\"");
				Matcher matcher = pattern.matcher(serverElement.toString());
				if (matcher.find()) {

					String includeFileName = matcher.group(1);

					// this is not the best way to detect this!!! but no simple
					// option other than this
					if (includeFileName.toLowerCase().contains("table")) {
						hasServerElement = true;
						break;
					}
				}

			}

		}

		return hasTable || hasServerElement;
	}
	
	public static String replaceInlineStyleWithClass(String newElement, Element originalElement){
		
		String styleRegex = "(?i)style\\s*=\\s*\"(.*?)\"";
		String classRegex = "(?i)class\\s*=\\s*\"(.*?)\"";
		
		//remove empty in line style attributes before proceed
		newElement = removeEmptyInlineStyleAttribute(newElement);
		//to lower case to avoid missing STYLE, CLASS cases
		//newElement = newElement.toLowerCase();
		
		Pattern stylepattern = Pattern.compile(styleRegex);
		Matcher stylematcher = stylepattern.matcher(newElement);
		
		Pattern classpattern = Pattern.compile(classRegex);
		Matcher classmatcher = classpattern.matcher(newElement);

		String inlineStyleValue=null;
		
		if (stylematcher.find()) {			
			inlineStyleValue = stylematcher.group(1);
		}
		//ignore apixel DIVs
		if(inlineStyleValue != null && inlineStyleValue.trim().length()>0 && !isApixelDiv(newElement)){
			
			//replace in line style with relevant class name from the class map in given new element
			
			StringBuilder ignoreStylesOutputParam = new StringBuilder();
			String cleanedInlineStyleValue = removeIgnoreStyleAttributes(inlineStyleValue, ignoreStylesOutputParam);
			StringBuffer inlineStyles = new StringBuffer();
			StringBuffer positionalStyles = new StringBuffer();
			String positionalClassName = null;
			String inlineClassName = null;
			
			/*break the inline style into positional and inline two categories. It helps in two ways:
			 * 
			 * 1. Style analyzer to log styles to db and analayze them according to the category
			 * 2. Find the relevant css class name from the map
			 * 
			 */
			StyleAnalyzer.breakdownToInlineAndPositionalCssStyles(cleanedInlineStyleValue, inlineStyles, positionalStyles);
			
			//log styles to db if style analyzer flag is enable. This should be run at least one time to record in line styles
			if(HTML5Util.MODE.equals(HTML5Util.STYLEANALYZE)){
				//record in line styles to the db to analyze common styles
				StyleAnalyzer.recordInlineStyle(originalElement.getName(),originalElement.getDebugInfo(),inlineStyles,positionalStyles);
			}
			
			if(!inlineStyles.toString().isEmpty()){
				inlineClassName = JerichoJspParserUtil.STYLES_MAP.get(inlineStyles.toString());
			}
			if(!positionalStyles.toString().isEmpty()){
				positionalClassName = JerichoJspParserUtil.STYLES_MAP.get(positionalStyles.toString());
			}
			
			//build string with new class names into one string including existing class names
			StringBuilder finalClassPropertyValue = new StringBuilder();
			if(inlineClassName != null){
				finalClassPropertyValue.append(inlineClassName).append(" ");
			}
			if(positionalClassName != null){
				finalClassPropertyValue.append(positionalClassName).append(" ");
			}
			//if there are new class names available in the map, replace in line styles with class names along with the existing class names
			if(finalClassPropertyValue.length() != 0){
				//get the existing class value from original element
				String existingOldClassAttributeValue = originalElement.getAttributeValue(HTML5Util.CLASS);
				//get the existing class value from new element e.g <td class="toolBugFixWithAclass" ..>
				String newElementClassAttributeValue = null;
				if (classmatcher.find()) {			
					newElementClassAttributeValue = classmatcher.group(1);
				}
				//check if the element contains a class attribute already and append new class name next to existing class name(Multiple CSS classes supported in HTML5)
				//e.g class="no-padding no-margin some-class some-other-class"
				if(existingOldClassAttributeValue != null){
					finalClassPropertyValue.append(existingOldClassAttributeValue).append(" ");
				}
				//check and make sure we do not add same class name twice from new and old element
				if(newElementClassAttributeValue != null && !newElementClassAttributeValue.equals(existingOldClassAttributeValue)){
					finalClassPropertyValue.append(newElementClassAttributeValue);
				}
				if(finalClassPropertyValue != null){
					//replace existing class attribute with new value
					if(existingOldClassAttributeValue != null || newElementClassAttributeValue != null)
					{
						newElement = newElement.replaceAll(classRegex, "class=\""+finalClassPropertyValue+"\"");
					}
					//add new class attribute to at the end of the replacing element tag
					else{
						newElement = newElement.substring(0, newElement.length()-1); // remove last closing > from the new tag
						//add class value at the end of the tag
						newElement = newElement + " class=\""+finalClassPropertyValue+"\" >";
					}
				}
				//replace in line style with ignore style set when mapping to css classes(e.g width)
				
				if(!ignoreStylesOutputParam.toString().isEmpty()){
					newElement = newElement.replaceAll(styleRegex, "style=\""+ignoreStylesOutputParam.toString()+"\"");
				}else{
					//remove in line style attribute from the new tag
					newElement = newElement.replaceAll(styleRegex, "");
				}
			}else{
				//do nothing. keep in line styles as it is. TODO YOU HAVE TO POPULATE CSS CLASS MAP!
				
			}
			
		}
		return newElement;
	}
	
	public static String removeEmptyInlineStyleAttribute(String newElement){		
		String emptyStyleRegex = "style=\"\"";
		return newElement.replaceAll(emptyStyleRegex, "");		
	}
	
	public static String removeIgnoreStyleAttributes(String inlinestyle, StringBuilder ignoreStylesOutputParam){
		
	
		List<String> styleList = inlineStyleToList(inlinestyle);
		StringBuilder cleanedInlineSyle = new StringBuilder();
		for(String style:styleList){
			//System.out.println(style);
			String name = style.split(":")[0];
			String value = style.split(":")[1];
			//ignore width style and keep in line as it is - Gil suggested this to improve CSS re-usability
			if(name.trim().equals(WIDTH)){
				ignoreStylesOutputParam.append(style).append(";");
				continue;
			}
			//ignore runtime jsp expression values
			if(value.contains("<%=")){
				ignoreStylesOutputParam.append(style).append(";");
				continue;
			}
			cleanedInlineSyle.append(style).append(";");
		}
		//style="<%= containerPlugIn.getHeaderCell(columnIndex).getAttribute("style") %>"
		//handle above scenario
		if(cleanedInlineSyle.length() == 0 && inlinestyle.contains("<%=")){
			ignoreStylesOutputParam.append(inlinestyle);
		}
		return cleanedInlineSyle.toString();		
	}
	
	public static List<String> inlineStyleToList(String style){
		System.out.println("inline style => "+style);
		List<String> styleList = new ArrayList<String>();
		//check if the in line style contains more than one property and add them all to list via ; split
		if(style.contains(";")){
			String[] values = style.split(";");
			for(int i=0;i<values.length;i++){
				//check if the style contains name and value pair separate with a colon before add
				if(values[i].split(":").length == 2){
					styleList.add(values[i]);
				}
			}
		}else{
			//check if the style contains name and value pair separate with a colon before add
			if(style.split(":").length == 2){
				styleList.add(style);
			}
		}
		return styleList;
	}
	
	public static String formatToWindowsPath(String path){
		return path.replace("/", "\\");
	}
	
	public static boolean isCommonJspFile(String fileName){
		if(JerichoJspParserUtil.COMMON_INCLUDE_FILE_SET != null && !JerichoJspParserUtil.COMMON_INCLUDE_FILE_SET.isEmpty()){
			return JerichoJspParserUtil.COMMON_INCLUDE_FILE_SET.contains(fileName);
		}else{
			return false;
		}
	}
	
	public static boolean isApixelDiv(String element){
		return element.contains("id=\"afpixel");	
	}
	
	public static boolean isTagContainsScriptlet(Tag element){
		
		String tagContent = element.toString();
		//match if there any scriptlet code <% %>(not expressions:<%= %>) inside the tag
		String regex = "<%[^=]\\s*(.*?)\\s*%>";
		Pattern stylepattern = Pattern.compile(regex);
		Matcher stylematcher = stylepattern.matcher(tagContent);
		return stylematcher.find();
	}
	
	public static String filePathToLowercase(String filePath){
		
		//String fileName = filePath.substring(filePath.lastIndexOf('\\')+1,filePath.length());
		//String lowerCasedPath = filePath.substring(0,filePath.lastIndexOf('\\')+1)+fileName.toLowerCase();
		return filePath.toLowerCase().replace("c:", "C:");
	}
}
