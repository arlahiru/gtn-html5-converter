package com.gtnexus.html5.facade;

import java.util.Iterator;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Tag;
import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;

public abstract class Facade {

	public static void replace(Segment originalElement,
			StringBuilder replacement, OutputDocument output) {
		try {
			Tag original_Element = (Tag) originalElement;
			output.replace(originalElement, replacement.toString());
			dbLogger.log(original_Element.getName(),
					originalElement.toString(), replacement.toString(),
					original_Element.getDebugInfo());
		} catch (NullPointerException e) {
			HTML5ParserException ex = new HTML5ParserException(
					"Runtime Exception", "Tag Missing",
					dbLogger.getLastConvertedLine());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		} catch (Exception e) {
			HTML5ParserException ex = new HTML5ParserException(
					"Runtime Exception", e.getMessage(),
					originalElement.getDebugInfo());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}

	public static void applyRules(Element element,
			OutputDocument outputDocument, StringBuilder newStyle,
			StringBuilder modifiedTag) {

		// remove table level obsolete attributes
		Iterator<Attribute> attributeIterator = element.getAttributes()
				.iterator();

		while (attributeIterator.hasNext()) {

			Attribute tableAttribute = attributeIterator.next();
			String attributeName = tableAttribute.getKey();
			String ruleKey = element.getName() + "_"
					+ attributeName.toLowerCase();

			Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

			if (rule != null) {

				StringBuilder returnValue = rule.execute(outputDocument,
						tableAttribute, element);

				if (returnValue != null) {
					newStyle.append(returnValue);
				} else {

					// do else logic here if return fix is null
				}

			} else {

				logger.debug("Rule key not found for key: " + ruleKey);
				modifiedTag.append(tableAttribute);
				modifiedTag.append(" ");
				logger.debug(ruleKey + " appended as it is.");
			}

		}

	}

}
