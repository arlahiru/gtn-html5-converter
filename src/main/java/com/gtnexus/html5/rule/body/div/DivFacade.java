package com.gtnexus.html5.rule.body.div;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

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

public class DivFacade extends Facade{

	public static void fixDivElements(Source source,
			OutputDocument outputDocument) throws HTML5ParserException{

		logger.debug("Fixing <div> elements started...");

		// get all the <div> tags from the source JSP file
		List<Element> divElementList = source
				.getAllElements(HTMLElementName.DIV);

		for (Element div : divElementList) {

			if (HTML5Util.hasAttributes((div))) {

				StringBuilder modifiedDivTag = new StringBuilder();

				StringBuilder newDivStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedDivTag.append("<" + HTMLElementName.DIV + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> divAttributeIterator = div.getAttributes()
						.iterator();

				while (divAttributeIterator.hasNext()) {

					Attribute divAttribute = divAttributeIterator.next();
					String attributeName = divAttribute.getKey();
					String ruleKey = div.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, divAttribute, div);

						if (returnValue != null) {
							newDivStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedDivTag.append(divAttribute);
						modifiedDivTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedDivTag.append(" " + HTML5Util.getInnerServerTagContent(div) + " ");

				// close div start tag
				modifiedDivTag.append(STYLE + "=\"" + newDivStyleValue + "\">");

				//outputDocument.replace(div.getStartTag(), modifiedDivTag);
				try{
					replace(div.getStartTag(), modifiedDivTag,outputDocument);
					
				}
				catch(HTML5ParserException e){
					throw e;
				}
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + div.getDebugInfo() + " replace with "
						+ modifiedDivTag);

			}

			logger.debug("Fixing <div> elements finished.");

		}
	}

}
