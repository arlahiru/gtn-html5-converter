package com.gtnexus.html5.rule.body.br;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.BR;
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

public class BrFacade extends Facade {

	public static void fixBRElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <br> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> brElementList = source.getAllElements(HTMLElementName.BR);

		for (Element br : brElementList) {

			if (HTML5Util.hasAttributes((br))) {

				StringBuilder modifiedBRTag = new StringBuilder();

				StringBuilder newBrStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedBRTag.append("<" + BR + " ");

				applyRules(br, outputDocument, newBrStyleValue,
						modifiedBRTag);

				// close tr start tag
				modifiedBRTag.append(STYLE + "=\"" + newBrStyleValue + "\">");

				replace(br.getStartTag(), modifiedBRTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + br.getDebugInfo() + " replace with "
						+ modifiedBRTag);

			}

			logger.debug("Fixing <br> elements finished.");

		}
	}
}
