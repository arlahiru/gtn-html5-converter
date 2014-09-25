package com.gtnexus.html5.rule.body.ul;

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

public class UlFacade {

	public static void fixUlElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <ul> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> ulElementList = source.getAllElements(HTMLElementName.UL);

		for (Element ul : ulElementList) {

			if (HTML5Util.hasAttributes((ul))) {

				StringBuilder modifiedUlTag = new StringBuilder();

				StringBuilder newUlStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedUlTag.append("<" + HTMLElementName.UL + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> ulAttributeIterator = ul.getAttributes()
						.iterator();

				while (ulAttributeIterator.hasNext()) {

					Attribute ulAttribute = ulAttributeIterator.next();
					String attributeName = ulAttribute.getKey();
					String ruleKey = ul.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, ulAttribute, null);

						if (returnValue != null) {
							newUlStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedUlTag.append(ulAttribute);
						modifiedUlTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedUlTag.append(" " + HTML5Util.getInnerServerTagContent(ul) + " ");

				// close ul start tag
				modifiedUlTag.append(STYLE + "=\"" + newUlStyleValue + "\">");

				outputDocument.replace(ul.getStartTag(), modifiedUlTag);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL + ">");

				logger.debug("\t" + ul.getDebugInfo() + " replace with "
						+ modifiedUlTag);

			}

			logger.debug("Fixing <ul> elements finished.");

		}
	}

}
