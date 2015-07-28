package com.gtnexus.html5.rule.body.iframe;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.util.HTML5Util;

public class IframeFacade extends Facade{
	
	public static boolean hspaceOrVspaceFound = false;

	public static void fixHRElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <iframe> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> iframeElementList = source
				.getAllElements(HTMLElementName.IFRAME);

		for (Element iframe : iframeElementList) {

			if (HTML5Util.hasAttributes((iframe))) {

				StringBuilder modifiedIframeTag = new StringBuilder();

				StringBuilder newIframeStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedIframeTag.append("<" + HTMLElementName.IFRAME + " ");

				applyRules(iframe, outputDocument, newIframeStyleValue,
						modifiedIframeTag);
				
				// append inner server tags if any
				modifiedIframeTag.append(" " + HTML5Util.getInnerServerTagContent(iframe) + " ");

				// close tr start tag
				modifiedIframeTag.append(STYLE + "=\"" + newIframeStyleValue
						+ "\">");

				replace(iframe.getStartTag(), modifiedIframeTag,outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

				logger.debug("\t" + iframe.getDebugInfo() + " replace with "
						+ modifiedIframeTag);
				
				IframeFacade.hspaceOrVspaceFound = false;

			}

			logger.debug("Fixing <iframe> elements finished.");

		}

	}
}
