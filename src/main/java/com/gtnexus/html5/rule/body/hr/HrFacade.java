package com.gtnexus.html5.rule.body.hr;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.HR;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

public class HrFacade extends Facade {

	public static void fixHRElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <hr> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> hrElementList = source.getAllElements(HTMLElementName.HR);

		for (Element hr : hrElementList) {

			if (HTML5Util.hasAttributes((hr))) {

				StringBuilder modifiedHRTag = new StringBuilder();

				StringBuilder newHrStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedHRTag.append("<" + HR + " ");

				applyRules(hr, outputDocument, newHrStyleValue,
						modifiedHRTag);

				// close tr start tag
				modifiedHRTag.append(STYLE + "=\"" + newHrStyleValue + "\">");

				replace(hr.getStartTag(), modifiedHRTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + hr.getDebugInfo() + " replace with "
						+ modifiedHRTag);

			}

			logger.debug("Fixing <hr> elements finished.");

		}
	}

}
