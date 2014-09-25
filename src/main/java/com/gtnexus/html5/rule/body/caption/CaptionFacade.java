package com.gtnexus.html5.rule.body.caption;

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

public class CaptionFacade extends Facade{

	public static void fixCaptionElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <caption> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> captionElementList = source
				.getAllElements(HTMLElementName.CAPTION);

		for (Element caption : captionElementList) {

			if (HTML5Util.hasAttributes((caption))) {

				StringBuilder modifiedCaptionTag = new StringBuilder();

				StringBuilder newCaptionStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedCaptionTag.append("<" + HTMLElementName.CAPTION + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> captionAttributeIterator = caption
						.getAttributes().iterator();

				while (captionAttributeIterator.hasNext()) {

					Attribute captionAttribute = captionAttributeIterator
							.next();
					String attributeName = captionAttribute.getKey();
					String ruleKey = caption.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, captionAttribute, null);

						if (returnValue != null) {
							newCaptionStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedCaptionTag.append(captionAttribute);
						modifiedCaptionTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedCaptionTag.append(" " + HTML5Util.getInnerServerTagContent(caption) + " ");

				// close ul start tag
				modifiedCaptionTag.append(STYLE + "=\"" + newCaptionStyleValue
						+ "\">");

				replace(caption.getStartTag(),
						modifiedCaptionTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + caption.getDebugInfo() + " replace with "
						+ modifiedCaptionTag);

			}

			logger.debug("Fixing <caption> elements finished.");

		}
	}
}
