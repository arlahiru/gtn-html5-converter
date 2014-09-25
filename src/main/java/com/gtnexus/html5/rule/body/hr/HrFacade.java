package com.gtnexus.html5.rule.body.hr;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.HR;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

public class HrFacade {

	public static void fixHRElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <hr> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> hrElementList = source.getAllElements(HTMLElementName.HR);

		for (Element hr : hrElementList) {

			if (HTML5Util.hasAttributes((hr))) {

				StringBuilder modifiedHRTag = new StringBuilder();

				StringBuilder newHrStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedHRTag.append("<" + HR + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> trAttributeIterator = hr.getAttributes()
						.iterator();

				while (trAttributeIterator.hasNext()) {

					Attribute trAttribute = trAttributeIterator.next();
					String attributeName = trAttribute.getKey();
					String ruleKey = hr.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, trAttribute, null);

						if (returnValue != null) {
							newHrStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedHRTag.append(trAttribute);
						modifiedHRTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}

				// close tr start tag
				modifiedHRTag.append(STYLE + "=\"" + newHrStyleValue + "\">");

				outputDocument.replace(hr.getStartTag(), modifiedHRTag);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + hr.getDebugInfo() + " replace with "
						+ modifiedHRTag);

			}

			logger.debug("Fixing <hr> elements finished.");

		}
	}

}
