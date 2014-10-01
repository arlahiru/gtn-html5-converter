package com.gtnexus.html5.rule.body.p;

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

public class PFacade extends Facade{

	public static void fixPElements(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing <p> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> pElementList = source.getAllElements(HTMLElementName.P);

		for (Element p : pElementList) {

			if (HTML5Util.hasAttributes((p))) {

				StringBuilder modifiedPTag = new StringBuilder();

				StringBuilder newPStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedPTag.append("<" + HTMLElementName.P + " ");

				applyRules(p, outputDocument, newPStyleValue, modifiedPTag);
				
				// append inner server tags if any
				modifiedPTag.append(" " + HTML5Util.getInnerServerTagContent(p) + " ");

				// close ul start tag
				modifiedPTag.append(STYLE + "=\"" + newPStyleValue + "\">");

				replace(p.getStartTag(), modifiedPTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + p.getDebugInfo() + " replace with "
						+ modifiedPTag);

			}

			logger.debug("Fixing <p> elements finished.");

		}
	}

}
