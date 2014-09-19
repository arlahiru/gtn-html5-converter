package com.gtnexus.html5.rule.body.ol;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class OlFacade {

	public static void fixOlElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <ol> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> olElementList = source.getAllElements(HTMLElementName.UL);

		for (Element ol : olElementList) {

			if (HTML5Util.hasAttributes((ol))) {

				StringBuilder modifiedOlTag = new StringBuilder();

				StringBuilder newOlStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedOlTag.append("<" + HTMLElementName.OL + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> olAttributeIterator = ol.getAttributes()
						.iterator();

				while (olAttributeIterator.hasNext()) {

					Attribute olAttribute = olAttributeIterator.next();
					String attributeName = olAttribute.getKey();
					String ruleKey = ol.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, olAttribute, null);

						if (returnValue != null) {
							newOlStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedOlTag.append(olAttribute);
						modifiedOlTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedOlTag.append(" " + HTML5Util.getInnerServerTagContent(ol) + " ");

				// close ul start tag
				modifiedOlTag.append(STYLE + "=\"" + newOlStyleValue + "\">");

				outputDocument.replace(ol.getStartTag(), modifiedOlTag);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + ol.getDebugInfo() + " replace with "
						+ modifiedOlTag);

			}

			logger.debug("Fixing <ol> elements finished.");

		}
	}
}
