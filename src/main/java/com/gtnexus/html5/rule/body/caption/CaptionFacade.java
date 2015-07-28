package com.gtnexus.html5.rule.body.caption;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.util.HTML5Util;

public class CaptionFacade extends Facade {

	public static void fixCaptionElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <caption> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> captionElementList = source
				.getAllElements(HTMLElementName.CAPTION);

		for (Element caption : captionElementList) {

			if (HTML5Util.hasAttributes((caption))) {

				StringBuilder modifiedCaptionTag = new StringBuilder();

				StringBuilder newCaptionStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedCaptionTag.append("<" + HTMLElementName.CAPTION + " ");

				applyRules(caption, outputDocument, newCaptionStyleValue,
						modifiedCaptionTag);

				// append inner server tags if any
				modifiedCaptionTag.append(" "
						+ HTML5Util.getInnerServerTagContent(caption) + " ");

				// close ul start tag
				modifiedCaptionTag.append(STYLE + "=\"" + newCaptionStyleValue
						+ "\">");

				replace(caption.getStartTag(), modifiedCaptionTag,
						outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + caption.getDebugInfo() + " replace with "
						+ modifiedCaptionTag);

			}

			logger.debug("Fixing <caption> elements finished.");

		}
	}
}
