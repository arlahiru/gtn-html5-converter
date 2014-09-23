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

public class TableElementFacade extends Facade{

	private static String cellPadding;
	private static Element thead;
	private static Element tfoot;
	private static Element tbody;
	private static List<Element> trElements;

	public static void fixLegacyTables(Source source,
			OutputDocument outputDocument) throws HTML5ParserException{

		logger.debug("Fixing legacy tables started...");

		// get all the <table> tags from the source JSP file
		List<Element> tableElementList = source
				.getAllElements(HTMLElementName.TABLE);

		for (Element table : tableElementList) {

			loadChildElements(table);
			removeAndReplaceTableElementObsoleteFeatures(table, outputDocument);

		}

		logger.debug("Fixing legacy tables finished.");

	}

	private static void removeAndReplaceTableElementObsoleteFeatures(
			Element table, OutputDocument outputDocument) throws HTML5ParserException{

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

			// remove table level obsolete attributes
			Iterator<Attribute> attributeIterator = table.getAttributes()
					.iterator();

			while (attributeIterator.hasNext()) {

				Attribute tableAttribute = attributeIterator.next();
				String attributeName = tableAttribute.getKey();
				String ruleKey = table.getName() + "_"
						+ attributeName.toLowerCase();

				Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

				if (rule != null) {

					StringBuilder returnValue = rule.execute(outputDocument,
							tableAttribute, table);

					if (returnValue != null) {
						newTableStyleValue.append(returnValue);
					} else {

						// this padding should go to td and tr level of the
						// table
						if (attributeName.equals(CELLPADDING)) {
							cellPadding = tableAttribute.getValue();
						}
					}

				} else {

					logger.debug("Rule key not found for key: " + ruleKey);
					modifiedTableTag.append(tableAttribute);
					modifiedTableTag.append(" ");
					logger.debug(ruleKey + " appended as it is.");
				}

			}

			applyPostTableFixes(table, newTableStyleValue);
			
			// append inner server tags if any
			modifiedTableTag.append(" " + HTML5Util.getInnerServerTagContent(table) + " ");

			// close table start tag
			modifiedTableTag.append(STYLE + "=\"" + newTableStyleValue + "\">");
			try{
				replace(table.getStartTag(), modifiedTableTag,outputDocument);
				replace(table.getEndTag(), new StringBuilder("</" + TABLE + ">"),outputDocument);
	
				logger.debug(table.getDebugInfo() + " replace with "
						+ modifiedTableTag);
				
				fixTableHead(table, outputDocument);
	
				fixTableBody(table, outputDocument);
	
				fixTableRow(table, outputDocument);
	
				fixTableFooter(table, outputDocument);
			}catch(HTML5ParserException ex){
				throw ex;
			}
		}
	}

	private static void fixTableRow(Element table, OutputDocument outputDocument) throws HTML5ParserException {

		for (Element tr : trElements) {

			// first check if this a TR element and it has attributes
			if (tr.getName().toLowerCase().equals(HTML5Util.TR) && HTML5Util.hasAttributes((tr))) {

				StringBuilder modifiedTRTag = new StringBuilder();

				StringBuilder newTrStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTRTag.append("<" + TR + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = tr.getAttributes()
						.iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = tr.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, tr);

						if (returnValue != null) {
							newTrStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedTRTag.append(trAttribute);
						modifiedTRTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedTRTag.append(" " + HTML5Util.getInnerServerTagContent(tr) + " ");

				// close tr start tag
				modifiedTRTag.append(STYLE + "=\"" + newTrStyleValue + "\">");
							

				outputDocument.replace(tr.getStartTag(), modifiedTRTag);
				
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + tr.getDebugInfo() + " replace with "
						+ modifiedTRTag);

				fixTableData(tr, outputDocument);

			}
		}

	}

	private static void fixTableData(Element tr, OutputDocument outputDocument) throws HTML5ParserException {

		// get all the td elements of this tr
		List<Element> tdElementList =getTdElementList(tr);

		for (Element td : tdElementList) {

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

				// remove td level obsolete attributes
				Iterator<Attribute> tdAttributeIterator = td.getAttributes()
						.iterator();

				while (tdAttributeIterator.hasNext()) {

					Attribute tdAttribute = tdAttributeIterator.next();
					String attributeName = tdAttribute.getKey();
					String ruleKey = td.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, tdAttribute, td);

						if (returnValue != null) {
							newTdStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate
							// fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedTDTag.append(tdAttribute);
						modifiedTDTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}

				// apply table level attributes if necessary. e.g. cell
				// padding
				if (cellPadding != null) {

					newTdStyleValue.append(PADDING + ":" + cellPadding + "px;");

				}

				newTdStyleValue = applyPostTDFixes(td, newTdStyleValue);
				
				// append inner server tags if any
				modifiedTDTag.append(" " + HTML5Util.getInnerServerTagContent(td) + " ");

				// close tr start tag
				modifiedTDTag.append(STYLE + "=\"" + newTdStyleValue + "\">");

				try{
					replace(td.getStartTag(), modifiedTDTag,outputDocument);
				}catch(HTML5ParserException e){
					throw e;
					// outputDocument.replace(td.getEndTag(), "</" + TD + ">");
				}

				logger.debug("\t\t" + td.getDebugInfo() + " replace with "
						+ modifiedTDTag);

			}

		}
	}

	private static void fixTableHead(Element table,
			OutputDocument outputDocument) throws HTML5ParserException{

		if (thead != null) {

			if (HTML5Util.hasAttributes((thead))) {

				StringBuilder modifiedTheadTag = new StringBuilder();

				StringBuilder newTheadStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTheadTag.append("<" + THEAD + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = thead.getAttributes()
						.iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = thead.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, null);

						if (returnValue != null) {
							newTheadStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedTheadTag.append(trAttribute);
						modifiedTheadTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}

				// close tr start tag
				modifiedTheadTag.append(STYLE + "=\"" + newTheadStyleValue
						+ "\">");
				try{
					replace(thead.getStartTag(), modifiedTheadTag,outputDocument);
				}catch(HTML5ParserException e){
					throw e;
				}
					// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + thead.getDebugInfo() + " replace with "
						+ modifiedTheadTag);

			}
		}

	}

	private static void fixTableBody(Element table,
			OutputDocument outputDocument) throws HTML5ParserException{

		if (tbody != null) {

			if (HTML5Util.hasAttributes((tbody))) {

				StringBuilder modifiedTbodyTag = new StringBuilder();

				StringBuilder newTbodyStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTbodyTag.append("<" + TBODY + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = tbody.getAttributes()
						.iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = tbody.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, null);

						if (returnValue != null) {
							newTbodyStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedTbodyTag.append(trAttribute);
						modifiedTbodyTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}

				// close tr start tag
				modifiedTbodyTag.append(STYLE + "=\"" + newTbodyStyleValue
						+ "\">");
				try{
					
					replace(tbody.getStartTag(), modifiedTbodyTag,outputDocument);
				}catch(HTML5ParserException ex){
					throw ex;
				}
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + tbody.getDebugInfo() + " replace with "
						+ modifiedTbodyTag);
			}
		}
	}

	private static void fixTableFooter(Element table,
			OutputDocument outputDocument) throws HTML5ParserException{

		if (tfoot != null) {

			if (HTML5Util.hasAttributes((tfoot))) {

				StringBuilder modifiedTfootTag = new StringBuilder();

				StringBuilder newTfootStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedTfootTag.append("<" + TFOOT + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = tfoot.getAttributes()
						.iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = tfoot.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, null);

						if (returnValue != null) {
							newTfootStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedTfootTag.append(trAttribute);
						modifiedTfootTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}

				// close tr start tag
				modifiedTfootTag.append(STYLE + "=\"" + newTfootStyleValue
						+ "\">");
				try{
					replace(tfoot.getStartTag(),modifiedTfootTag,outputDocument);
				}catch(HTML5ParserException ex){
					throw ex;
				}


				logger.debug("\t" + tfoot.getDebugInfo() + " replace with "
						+ modifiedTfootTag);
			}
		}
	}

	private static void applyPostTableFixes(Element table,
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
	
	private static List<Element> getTdElementList(Element tr){
		
		List<Element> childElements = tr.getChildElements();
		
		List<Element> tdElements = new ArrayList<>(0);
		
		for(Element e: childElements){
			
			if(e.getName().toLowerCase().equals(HTML5Util.TD) || e.getName().toLowerCase().equals(HTML5Util.TH))
				tdElements.add(e);
			
		}
		
		return tdElements;
		
	}
		
		
		
	

}
