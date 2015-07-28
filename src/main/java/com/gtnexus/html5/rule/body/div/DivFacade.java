package com.gtnexus.html5.rule.body.div;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.util.HTML5Util;

public class DivFacade extends Facade {

	public static void fixDivElements(Source source,
			OutputDocument outputDocument) {

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

				applyRules(div, outputDocument, newDivStyleValue,
						modifiedDivTag);

				// append inner server tags if any
				modifiedDivTag.append(" "
						+ HTML5Util.getInnerServerTagContent(div) + " ");

				// close div start tag
				modifiedDivTag.append(STYLE + "=\"" + newDivStyleValue + "\">");

				// outputDocument.replace(div.getStartTag(), modifiedDivTag);

				replace(div.getStartTag(), modifiedDivTag, outputDocument);

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
