package com.gtnexus.html5.rule.body.font;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.CLASS;
import static com.gtnexus.html5.util.HTML5Util.SPAN;
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

public class FontElementFacade extends Facade{

	public static void fixFontTags(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing font tags started...");

		// get all the <font> tags from the source JSP file
		List<Element> fontElementList = source
				.getAllElements(HTMLElementName.FONT);

		for (Element font : fontElementList) {

			// replace tag
			StringBuilder spanTag = new StringBuilder("<" + SPAN + " ");

			StringBuilder spanStyleValue = new StringBuilder();

			if (HTML5Util.hasAttributes((font))) {

				// remove font level obsolete attributes
				Iterator<Attribute> fontAttributeIterator = font
						.getAttributes().iterator();

				while (fontAttributeIterator.hasNext()) {

					Attribute fontAttribute = fontAttributeIterator.next();
					String attributeName = fontAttribute.getKey();
					String ruleKey = font.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, fontAttribute, null);

						if (returnValue != null) {
							spanStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						spanTag.append(fontAttribute);
						spanTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				spanTag.append(" " + HTML5Util.getInnerServerTagContent(font) + " ");

				// close span start tag
				spanTag.append(STYLE + "=\"" + spanStyleValue + "\">");

				replace(font.getStartTag(), spanTag,outputDocument);

				if (font.getEndTag() != null)
					outputDocument.replace(font.getEndTag(), "</" + SPAN + ">");

				logger.debug(font.getDebugInfo() + " replaced with " + spanTag);

			}

		}

		logger.debug("Fixing font tags finished");
	}

}
