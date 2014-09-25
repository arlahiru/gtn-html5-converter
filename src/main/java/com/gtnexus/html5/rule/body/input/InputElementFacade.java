package com.gtnexus.html5.rule.body.input;

import static com.gtnexus.html5.main.JerichoJspParserUtil.RULES_MAP;
import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.*;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.rule.body.font.FontFaceRule;
import com.gtnexus.html5.util.HTML5Util;

public class InputElementFacade {
	
	public static boolean hspaceOrVspaceFound = false;

	public static void fixInputTags(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing font tags started...");

		// get all the <font> tags from the source JSP file
		List<Element> inputElementList = source
				.getAllElements(HTMLElementName.INPUT);

		for (Element input : inputElementList) {
			List<Attribute> allAttributes = input.getAttributes();
			StringBuilder validTags = new StringBuilder();
			StringBuilder newInputTag = new StringBuilder();
			newInputTag.append(makeStartTag(HTMLElementName.INPUT));

			for (Attribute attribute : allAttributes) {

				Rule inputRule = RULES_MAP.get(formatKey(HTMLElementName.INPUT,
						attribute.getName()));
				if (inputRule != null) {
					newInputTag.append(inputRule.execute(outputDocument,
							attribute, input));
				} else {
					validTags.append(attribute + " ");
				}
			}

			newInputTag.append("\" ");
			newInputTag.append(validTags);
			
			// append inner server tags if any
			newInputTag.append(" " + HTML5Util.getInnerServerTagContent(input) + " ");

			newInputTag.append(">");

			outputDocument.replace(input.getStartTag(), newInputTag);

			hspaceOrVspaceFound = false;

			logger.debug(input.getDebugInfo() + " replaced with " + newInputTag);

			dbLogger.log(HTMLElementName.INPUT, input.getStartTag().toString(),
					input.toString(), input.getDebugInfo());

		}

		logger.debug("Fixing font tags finished");

	}
}
