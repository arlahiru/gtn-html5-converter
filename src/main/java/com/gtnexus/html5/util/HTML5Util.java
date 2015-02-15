package com.gtnexus.html5.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

import org.apache.commons.lang3.StringUtils;

public class HTML5Util {

	// HTML 5 doctype
	public final static String DOCTYPE_HTML5 = "<!DOCTYPE html>";

	// charset meta tag
	public final static String META_CHARSET_UTF8 = "<meta charset=\"utf-8\">";

	// HTML 5 converted meta tag
	public final static String META_IS_HTML5 = "<meta isHtml5Page=\"true\">";

	// HTML 5 converted comment tag
	public final static String HTML5_CONVERTED_COMMENT = "<!-- HTML5 Converted Page:Phase1 -->";

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

	// HTML ELEMENT NAMES

	// check this page already a html5 page by looking at the meta tag
	public static boolean isHtml5Page(Source source) {

		// get all meta tags
		List<Element> allCommentTags = source
				.getAllElements(StartTagType.COMMENT);

		for (Element commentTag : allCommentTags) {

			if (commentTag.toString().equals(HTML5_CONVERTED_COMMENT)) {

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

	public static List<String> getIncludeFilePaths(String mainFilePath)
			throws FileNotFoundException, IOException {

		File sourceFile = new File(mainFilePath);

		Source source = new Source(new FileInputStream(sourceFile));

		List<String> includeFilePaths = new ArrayList<String>(0);

		// get all server tags <% %>
		List<Element> allElements = source
				.getAllElements(StartTagType.SERVER_COMMON);

		for (Element e : allElements) {

			String tagContent = e.toString().toLowerCase();

			// find include tags
			if (tagContent.contains("<%@") && tagContent.contains("include")) {

				// extract file attribute value by matching value between
				// " "
				Pattern pattern = Pattern.compile("\"(.*?)\"");
				Matcher matcher = pattern.matcher(tagContent);
				if (matcher.find()) {

					String includeFileName = matcher.group(1);

					int numOfDirectoriesBack = StringUtils.countMatches(
							includeFileName, "../");

					// remove ../ from the path
					includeFileName = includeFileName.replace("../", "");

					String parentJspAbsolutePath = sourceFile.getAbsolutePath();

					Path parentFolderPath = Paths.get(parentJspAbsolutePath)
							.getParent();

					Path includeFileFolderPath = parentFolderPath;

					// construct include file parent directory
					for (int i = 0; i < numOfDirectoriesBack; i++) {

						includeFileFolderPath = includeFileFolderPath
								.getParent();
					}

					// remove ./ from the path
					includeFileName = includeFileName.replace("./", "");

					String includeFilePath = includeFileFolderPath
							+ File.separator + includeFileName;

					includeFilePaths.add(includeFilePath);

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

	// e.g <td <%=containerPlugIn.getCellStyle(rowIndex, columnIndex)%> > </td>
	public static String getInnerServerTagContent(Element e) {

		List<String> quotedContentList = new ArrayList<String>(0);

		String startTagContent = e.getStartTag().toString();

		Pattern quotePattern = Pattern.compile("['\"](.*?)['\"]");
		Matcher quoteMatcher = quotePattern.matcher(startTagContent);

		while (!quoteMatcher.hitEnd()) {

			if (quoteMatcher.find()) {
				// populate quoted content array
				quotedContentList.add(quoteMatcher.group(1));
			}

		}

		String serverCode = "";

		Pattern pattern = Pattern.compile("<%(.*?)%>");
		Matcher matcher = pattern.matcher(startTagContent);
		while (!matcher.hitEnd()) {

			if (matcher.find()) {

				// ignore onclick="<%=actionButtonLink%>" like patterns by
				// checking is this code inside quotes.
				if (!isInsideQuotes(quotedContentList, matcher.group(1))) {
					// enclose with matching scriptlet tags
					serverCode = serverCode + "<%" + matcher.group(1) + "%>";
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

		if (source.getAllElements(HTMLElementName.BODY).size() != output
				.getAllElements(HTMLElementName.BODY).size())
			return false;
		else if (source.getAllElements(HTMLElementName.HEAD).size() != output
				.getAllElements(HTMLElementName.HEAD).size())
			return false;
		if (source.getAllElements(HTMLElementName.FORM).size() != output
				.getAllElements(HTMLElementName.FORM).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TABLE).size() != output
				.getAllElements(HTMLElementName.TABLE).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TD).size() != output
				.getAllElements(HTMLElementName.TD).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TR).size() != output
				.getAllElements(HTMLElementName.TR).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TH).size() != output
				.getAllElements(HTMLElementName.TH).size())
			return false;
		else if (source.getAllElements(HTMLElementName.THEAD).size() != output
				.getAllElements(HTMLElementName.THEAD).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TFOOT).size() != output
				.getAllElements(HTMLElementName.TFOOT).size())
			return false;
		else if (source.getAllElements(HTMLElementName.TBODY).size() != output
				.getAllElements(HTMLElementName.TBODY).size())
			return false;
		else if (source.getAllElements(HTMLElementName.INPUT).size() != output
				.getAllElements(HTMLElementName.INPUT).size())
			return false;
		else if (source.getAllElements(HTMLElementName.IFRAME).size() != output
				.getAllElements(HTMLElementName.IFRAME).size())
			return false;
		else if (source.getAllElements(HTMLElementName.BR).size() != output
				.getAllElements(HTMLElementName.BR).size())
			return false;

		else if (source.getAllElements(HTMLElementName.CAPTION).size() != output
				.getAllElements(HTMLElementName.CAPTION).size())
			return false;
		else if (source.getAllElements(HTMLElementName.HR).size() != output
				.getAllElements(HTMLElementName.HR).size())
			return false;
		else if (source.getAllElements(HTMLElementName.LEGEND).size() != output
				.getAllElements(HTMLElementName.LEGEND).size())
			return false;
		else if (source.getAllElements(HTMLElementName.LI).size() != output
				.getAllElements(HTMLElementName.LI).size())
			return false;
		else if (source.getAllElements(HTMLElementName.OL).size() != output
				.getAllElements(HTMLElementName.OL).size())
			return false;
		else if (source.getAllElements(HTMLElementName.P).size() != output
				.getAllElements(HTMLElementName.P).size())
			return false;
		else if (source.getAllElements(HTMLElementName.PRE).size() != output
				.getAllElements(HTMLElementName.PRE).size())
			return false;
		else if (source.getAllElements(HTMLElementName.UL).size() != output
				.getAllElements(HTMLElementName.UL).size())
			return false;
		else if ((source.getAllElements(HTMLElementName.SPAN).size() + source.getAllElements(HTMLElementName.FONT).size())!= output
				.getAllElements(HTMLElementName.SPAN).size())
			return false;
		
		return true;

	}
	
	public static boolean containsLinkInTd(Segment td,String cssClass){
		List<Element> immediateElements = td.getChildElements();
		for(Element e : immediateElements){
			
			if(e.getName().equals(ANCHOR) ){
				try{
					if(e.getAttributeValue("class").equals(cssClass)) return true;
				}catch(NullPointerException e2){
					
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

	
}
