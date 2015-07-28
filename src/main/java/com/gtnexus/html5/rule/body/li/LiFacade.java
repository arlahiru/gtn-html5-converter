package com.gtnexus.html5.rule.body.li;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.util.HTML5Util;

public class LiFacade extends Facade {

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

				applyRules(li, outputDocument, newLiStyleValue, modifiedLiTag);

				// append inner server tags if any
				modifiedLiTag.append(" "
						+ HTML5Util.getInnerServerTagContent(li) + " ");

				// close tr start tag
				modifiedLiTag.append(STYLE + "=\"" + newLiStyleValue + "\">");

				replace(li.getStartTag(), modifiedLiTag, outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + li.getDebugInfo() + " replace with "
						+ modifiedLiTag);

			}

			logger.debug("Fixing <li> elements finished.");

		}
	}

}
