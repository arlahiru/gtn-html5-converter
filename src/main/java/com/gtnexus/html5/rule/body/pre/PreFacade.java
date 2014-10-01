package com.gtnexus.html5.rule.body.pre;

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

public class PreFacade extends Facade{

	public static void fixPreElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <pre> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> preElementList = source
				.getAllElements(HTMLElementName.PRE);

		for (Element pre : preElementList) {

			if (HTML5Util.hasAttributes((pre))) {

				StringBuilder modifiedPreTag = new StringBuilder();

				StringBuilder newPreStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedPreTag.append("<" + HTMLElementName.PRE + " ");

				applyRules(pre, outputDocument, newPreStyleValue, modifiedPreTag);
				
				// append inner server tags if any
				modifiedPreTag.append(" " + HTML5Util.getInnerServerTagContent(pre) + " ");

				// close ul start tag
				modifiedPreTag.append(STYLE + "=\"" + newPreStyleValue + "\">");

				replace(pre.getStartTag(), modifiedPreTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.PRE
				// + ">");

				logger.debug("\t" + pre.getDebugInfo() + " replace with "
						+ modifiedPreTag);

			}

			logger.debug("Fixing <pre> elements finished.");

		}
	}
}
