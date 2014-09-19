package com.gtnexus.html5.rule.body.li;

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

public class LiFacade {

	public static void fixLiElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <li> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> liElementList = source.getAllElements(HTMLElementName.LI);

		for (Element li : liElementList) {

			if (HTML5Util.hasAttributes((li))) {

				StringBuilder modifiedLiTag = new StringBuilder();

				StringBuilder newLiStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedLiTag.append("<" + HTMLElementName.LI + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> liAttributeIterator = li.getAttributes()
						.iterator();

				while (liAttributeIterator.hasNext()) {

					Attribute liAttribute = liAttributeIterator.next();
					String attributeName = liAttribute.getKey();
					String ruleKey = li.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, liAttribute, null);

						if (returnValue != null) {
							newLiStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedLiTag.append(liAttribute);
						modifiedLiTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedLiTag.append(" " + HTML5Util.getInnerServerTagContent(li) + " ");

				// close tr start tag
				modifiedLiTag.append(STYLE + "=\"" + newLiStyleValue + "\">");

				outputDocument.replace(li.getStartTag(), modifiedLiTag);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + li.getDebugInfo() + " replace with "
						+ modifiedLiTag);

			}

			logger.debug("Fixing <li> elements finished.");

		}
	}

}
