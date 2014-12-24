package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.ALIGN;
import static com.gtnexus.html5.util.HTML5Util.AUTO;
import static com.gtnexus.html5.util.HTML5Util.CELLPADDING;
import static com.gtnexus.html5.util.HTML5Util.CENTER;
import static com.gtnexus.html5.util.HTML5Util.LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_RIGHT;
import static com.gtnexus.html5.util.HTML5Util.PADDING;
import static com.gtnexus.html5.util.HTML5Util.RIGHT;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.TABLE;
import static com.gtnexus.html5.util.HTML5Util.TBODY;
import static com.gtnexus.html5.util.HTML5Util.TD;
import static com.gtnexus.html5.util.HTML5Util.TFOOT;
import static com.gtnexus.html5.util.HTML5Util.TH;
import static com.gtnexus.html5.util.HTML5Util.THEAD;
import static com.gtnexus.html5.util.HTML5Util.TR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableElementFacade extends Facade {

	private static String cellPadding;
	private static Element thead;
	private static Element tfoot;
	private static Element tbody;
	private static List<Element> trElements;

	public static void fixLegacyTables(Source source,
			OutputDocument outputDocument) throws HTML5ParserException {

		logger.debug("Fixing legacy tables started...");

		// get all the <table> tags from the source JSP file
		List<Element> tableElementList = source
				.getAllElements(HTMLElementName.TABLE);

		for (Element table : tableElementList) {

			loadChildElements(table);
			removeAndReplaceTableElementObsoleteFeatures(table, outputDocument);

		}

		// clear cellpadding variable
		cellPadding = null;

		// fix non well-formed elements
		fixNonWellformedTdThTags(source, outputDocument);
		fixNonWellformedTrTags(source, outputDocument);

		logger.debug("Fixing legacy tables finished.");

	}

	private static void removeAndReplaceTableElementObsoleteFeatures(
			Element table, OutputDocument outputDocument) {

		// apply rules to replace obsolete table,tr,td,thead,tbody,tfoot
		// elements with
		// valid html5 attribute

		if (HTML5Util.hasAttributes((table))) {

			StringBuilder modifiedTableTag = new StringBuilder();

			StringBuilder newTableStyleValue = new StringBuilder();

			// initialize default values. have to test this!
			modifiedTableTag.append("<" + TABLE + " ");

			// keep table cellpadding value to apply in <td> level
			cellPadding = null;

			applyRules(table, outputDocument, newTableStyleValue,
					modifiedTableTag);

			newTableStyleValue = applyPostTableFixes(table, newTableStyleValue);

			// append inner server tags if any
			modifiedTableTag.append(" "
					+ HTML5Util.getInnerServerTagContent(table) + " ");

			// close table start tag
			modifiedTableTag.append(STYLE + "=\"" + newTableStyleValue + "\">");

			replace(table.getStartTag(), modifiedTableTag, outputDocument);
			replace(table.getEndTag(), new StringBuilder("</" + TABLE + ">"),
					outputDocument);

			logger.debug(table.getDebugInfo() + " replace with "
					+ modifiedTableTag);

			fixTableHead(table, outputDocument);

			fixTableBody(table, outputDocument);

			fixTableRow(table, outputDocument);

			fixTableFooter(table, outputDocument);

		}
	}

	private static void fixTableRow(Element table, OutputDocument outputDocument)
			throws HTML5ParserException {

		for (Element tr : trElements) {
			fixTrTag(tr, outputDocument);
		}

	}

	private static void fixTrTag(Element tr, OutputDocument outputDocument) {

		// first check if this a TR element and it has attributes
		if (tr.getName().toLowerCase().equals(HTML5Util.TR)
				&& HTML5Util.hasAttributes((tr))) {

			StringBuilder modifiedTRTag = new StringBuilder();

			StringBuilder newTrStyleValue = new StringBuilder();

			// initialize default values. have to test this!
			modifiedTRTag.append("<" + TR + " ");

			applyRules(tr, outputDocument, newTrStyleValue, modifiedTRTag);

			// append inner server tags if any
			modifiedTRTag.append(" " + HTML5Util.getInnerServerTagContent(tr)
					+ " ");

			// close tr start tag
			modifiedTRTag.append(STYLE + "=\"" + newTrStyleValue + "\">");

			outputDocument.replace(tr.getStartTag(), modifiedTRTag);

			// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

			logger.debug("\t" + tr.getDebugInfo() + " replace with "
					+ modifiedTRTag);

			fixTableData(tr, outputDocument);

		}

	}

	private static void fixTableData(Element tr, OutputDocument outputDocument) {
		
		// get all the td elements of this tr
		List<Element> tdElementList = getTdElementList(tr);

		for (Element td : tdElementList) {

			fixTdTag(td, outputDocument);
		}
	}

	private static void fixTdTag(Element td, OutputDocument outputDocument) {

		if (HTML5Util.hasAttributes((td))) {

			StringBuilder modifiedTDTag = new StringBuilder();

			StringBuilder newTdStyleValue = new StringBuilder();

			// initialize start tag

			// check if the child tag is th or td
			if (td.getName().equals(HTMLElementName.TH)) {

				modifiedTDTag.append("<" + TH + " ");

			} else {
				modifiedTDTag.append("<" + TD + " ");
				
				
			}

			applyRules(td, outputDocument, newTdStyleValue, modifiedTDTag);

			// apply table level attributes if necessary. e.g. cell
			// padding
			if (cellPadding != null) {

				newTdStyleValue.append(PADDING + ":" + cellPadding + "px;");

			}else{
				//this means td element does not have a proper parent. So we apply 0 paddings to them
				//GTBUG-6674
				newTdStyleValue.append(PADDING + ":" + "0" + "px;");
			}
			

			newTdStyleValue = applyPostTDFixes(td, newTdStyleValue);

			// append inner server tags if any
			modifiedTDTag.append(" " + HTML5Util.getInnerServerTagContent(td)
					+ " ");

			//append class to the td
			if(HTML5Util.containsLinkInTd(td,"smallbuttontext")&&td.getAttributeValue("class")==null){
				modifiedTDTag.append(" class=\"listtablecell\" ");
			}
			
			// close tr start tag
			modifiedTDTag.append(STYLE + "=\"" + newTdStyleValue + "\">");
			
			replace(td.getStartTag(), modifiedTDTag, outputDocument);

			// outputDocument.replace(td.getEndTag(), "</" + TD + ">");

			logger.debug("\t\t" + td.getDebugInfo() + " replace with "
					+ modifiedTDTag);

		}

	}

	private static void fixTableHead(Element table,
			OutputDocument outputDocument) {

		if (thead != null) {

			if (HTML5Util.hasAttributes((thead))) {

				StringBuilder modifiedTheadTag = new StringBuilder();

				StringBuilder newTheadStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTheadTag.append("<" + THEAD + " ");

				applyRules(thead, outputDocument, newTheadStyleValue,
						modifiedTheadTag);

				// close tr start tag
				modifiedTheadTag.append(STYLE + "=\"" + newTheadStyleValue
						+ "\">");

				replace(thead.getStartTag(), modifiedTheadTag, outputDocument);

				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + thead.getDebugInfo() + " replace with "
						+ modifiedTheadTag);

			}
		}

	}

	private static void fixTableBody(Element table,
			OutputDocument outputDocument) {

		if (tbody != null) {

			if (HTML5Util.hasAttributes((tbody))) {

				StringBuilder modifiedTbodyTag = new StringBuilder();

				StringBuilder newTbodyStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTbodyTag.append("<" + TBODY + " ");

				applyRules(tbody, outputDocument, newTbodyStyleValue,
						modifiedTbodyTag);

				// close tr start tag
				modifiedTbodyTag.append(STYLE + "=\"" + newTbodyStyleValue
						+ "\">");

				replace(tbody.getStartTag(), modifiedTbodyTag, outputDocument);

				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + tbody.getDebugInfo() + " replace with "
						+ modifiedTbodyTag);
			}
		}
	}

	private static void fixTableFooter(Element table,
			OutputDocument outputDocument) {

		if (tfoot != null) {

			if (HTML5Util.hasAttributes((tfoot))) {

				StringBuilder modifiedTfootTag = new StringBuilder();

				StringBuilder newTfootStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTfootTag.append("<" + TFOOT + " ");

				applyRules(tbody, outputDocument, newTfootStyleValue,
						modifiedTfootTag);

				// close tr start tag
				modifiedTfootTag.append(STYLE + "=\"" + newTfootStyleValue
						+ "\">");

				replace(tfoot.getStartTag(), modifiedTfootTag, outputDocument);

				logger.debug("\t" + tfoot.getDebugInfo() + " replace with "
						+ modifiedTfootTag);
			}
		}
	}

	private static StringBuilder applyPostTableFixes(Element table,
			StringBuilder newTableStyleValue) {

		// apply td align to the table if this table is inside a TD element
		Element parentElement = table.getParentElement();
		if (parentElement != null
				&& parentElement.getName().equals(HTMLElementName.TD)) {

			String tdAlignValue = parentElement.getAttributeValue(ALIGN);
			if (tdAlignValue != null) {

				if (tdAlignValue.equalsIgnoreCase(CENTER)) {

					newTableStyleValue.append(MARGIN + ": 0 " + AUTO + ";");

				} else if (tdAlignValue.equalsIgnoreCase(RIGHT)) {

					newTableStyleValue.append(MARGIN_RIGHT + ": 0; "
							+ MARGIN_LEFT + ":" + AUTO + ";");

				} else if (tdAlignValue.equalsIgnoreCase(LEFT)) {

					newTableStyleValue.append(MARGIN_LEFT + ": 0; "
							+ MARGIN_RIGHT + ":" + AUTO + ";");

				}
			}

		}
		
		// remove border from style if the table has a class and border is zero. This fixes the rounded corner table issue
		if (table.getAttributeValue("class") != null && table.getAttributeValue("border") != null && table.getAttributeValue("border").equals("0")) {

			String newStyle = newTableStyleValue.toString()
					.replaceAll("border:\\s*[A-Za-z0-9 ]+\\s*;", "");
			
			newTableStyleValue = new StringBuilder(newStyle);

		}
		
		return newTableStyleValue;

	}

	private static StringBuilder applyPostTDFixes(Element td,
			StringBuilder newTdStyleValue) {

		// check for inner tables/includes and remove font-size from
		// td style
		if (!td.getAllElements(TABLE).isEmpty()
				|| td.toString().contains("<%@")) {

			newTdStyleValue = new StringBuilder(newTdStyleValue.toString()
					.replaceAll("font-size:\\s*[0-9]+\\s*(px);", ""));

		}

		return newTdStyleValue;
	}

	private static void loadChildElements(Element table) {

		trElements = new ArrayList<Element>(0);
		thead = tfoot = tbody = null;

		List<Element> childElements = table.getChildElements();

		for (Element child : childElements) {

			if (child.getName().equals(HTMLElementName.THEAD)) {

				thead = child;
				trElements.addAll(child.getChildElements());
			} else if (child.getName().equals(HTMLElementName.TBODY)) {

				tbody = child;
				trElements.addAll(child.getChildElements());
			} else if (child.getName().equals(HTMLElementName.TFOOT)) {

				tfoot = child;
				trElements.addAll(child.getChildElements());
			} else if (child.getName().equals(HTMLElementName.TR)) {

				trElements.add(child);
			}

		}

	}

	private static List<Element> getTdElementList(Element tr) {

		List<Element> childElements = tr.getChildElements();

		List<Element> tdElements = new ArrayList<>(0);

		for (Element e : childElements) {

			if (e.getName().toLowerCase().equals(HTML5Util.TD)
					|| e.getName().toLowerCase().equals(HTML5Util.TH))
				tdElements.add(e);

		}

		return tdElements;

	}

	private static void fixNonWellformedTdThTags(Source source,
			OutputDocument outputDocument) {

		List<Element> allTdThTags = source.getAllElements(HTML5Util.TD);
		allTdThTags.addAll(source.getAllElements(HTML5Util.TH));

		for (Element td : allTdThTags) {

			if (td.getParentElement() == null) {

				// fix each td tag
				fixTdTag(td, outputDocument);
			} else if (!td.getParentElement().getName().equals(HTML5Util.TR)) {

				// fix each td tag
				fixTdTag(td, outputDocument);

			}

		}
	}

	private static void fixNonWellformedTrTags(Source source,
			OutputDocument outputDocument) {

		List<Element> allTrTags = source.getAllElements(HTML5Util.TR);

		for (Element tr : allTrTags) {

			if (tr.getParentElement() == null) {

				// fix each tr tag
				fixTrTag(tr, outputDocument);
			} else if (!tr.getParentElement().getName().equals(HTML5Util.TABLE)) {

				// fix each tr tag
				fixTrTag(tr, outputDocument);
			}

		}

	}

	// override this method from Facade class
	public static void applyRules(Element element,
			OutputDocument outputDocument, StringBuilder newStyle,
			StringBuilder modifiedTag) {

		// remove table level obsolete attributes
		Iterator<Attribute> attributeIterator = element.getAttributes()
				.iterator();

		while (attributeIterator.hasNext()) {

			Attribute tableAttribute = attributeIterator.next();
			String attributeName = tableAttribute.getKey();
			String ruleKey = element.getName() + "_"
					+ attributeName.toLowerCase();

			Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

			if (rule != null) {

				StringBuilder returnValue = rule.execute(outputDocument,
						tableAttribute, element);

				if (returnValue != null) {
					newStyle.append(returnValue);
				} else {

					// this padding should go to td and tr level of the
					// table
					if (attributeName.equals(CELLPADDING)) {
						cellPadding = tableAttribute.getValue();
					}
				}

			} else {

				logger.debug("Rule key not found for key: " + ruleKey);
				modifiedTag.append(tableAttribute);
				modifiedTag.append(" ");
				logger.debug(ruleKey + " appended as it is.");
			}

		}

	}

}
