package com.gtnexus.html5.rule.body.iframe;

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

public class IframeFacade extends Facade{
	
	public static boolean hspaceOrVspaceFound = false;

	public static void fixHRElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <iframe> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> iframeElementList = source
				.getAllElements(HTMLElementName.IFRAME);

		for (Element iframe : iframeElementList) {

			if (HTML5Util.hasAttributes((iframe))) {

				StringBuilder modifiedIframeTag = new StringBuilder();

				StringBuilder newIframeStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedIframeTag.append("<" + HTMLElementName.IFRAME + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> iframeAttributeIterator = iframe
						.getAttributes().iterator();

				while (iframeAttributeIterator.hasNext()) {

					Attribute iframeAttribute = iframeAttributeIterator.next();
					String attributeName = iframeAttribute.getKey();
					String ruleKey = iframe.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, iframeAttribute, null);

						if (returnValue != null) {
							newIframeStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedIframeTag.append(iframeAttribute);
						modifiedIframeTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedIframeTag.append(" " + HTML5Util.getInnerServerTagContent(iframe) + " ");

				// close tr start tag
				modifiedIframeTag.append(STYLE + "=\"" + newIframeStyleValue
						+ "\">");

				replace(iframe.getStartTag(), modifiedIframeTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + iframe.getDebugInfo() + " replace with "
						+ modifiedIframeTag);
				
				IframeFacade.hspaceOrVspaceFound = false;

			}

			logger.debug("Fixing <iframe> elements finished.");

		}

	}
}
