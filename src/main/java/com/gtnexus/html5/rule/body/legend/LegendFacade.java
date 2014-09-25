package com.gtnexus.html5.rule.body.legend;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class LegendFacade extends Facade{

	public static void fixLegendElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <legend> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> legendElementList = source
				.getAllElements(HTMLElementName.LEGEND);

		for (Element legend : legendElementList) {

			if (HTML5Util.hasAttributes((legend))) {

				StringBuilder modifiedLegendTag = new StringBuilder();

				StringBuilder newLegendStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedLegendTag.append("<" + HTMLElementName.LEGEND + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = legend
						.getAttributes().iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = legend.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, null);

						if (returnValue != null) {
							newLegendStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedLegendTag.append(trAttribute);
						modifiedLegendTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedLegendTag.append(" " + HTML5Util.getInnerServerTagContent(legend) + " ");

				// close tr start tag
				modifiedLegendTag.append(STYLE + "=\"" + newLegendStyleValue
						+ "\">");

				replace(legend.getStartTag(), modifiedLegendTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + legend.getDebugInfo() + " replace with "
						+ modifiedLegendTag);

			}

			logger.debug("Fixing <legend> elements finished.");

		}
	}

}
