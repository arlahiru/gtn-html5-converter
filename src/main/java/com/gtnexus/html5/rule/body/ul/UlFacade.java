package com.gtnexus.html5.rule.body.ul;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class UlFacade extends Facade {

	public static void fixUlElements(Source source,
			OutputDocument outputDocument) throws HTML5ParserException{

		logger.debug("Fixing <ul> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> ulElementList = source.getAllElements(HTMLElementName.UL);

		for (Element ul : ulElementList) {

			if (HTML5Util.hasAttributes((ul))) {

				StringBuilder modifiedUlTag = new StringBuilder();

				StringBuilder newUlStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedUlTag.append("<" + HTMLElementName.UL + " ");

				applyRules(ul, outputDocument, newUlStyleValue, modifiedUlTag);
				
				// append inner server tags if any
				modifiedUlTag.append(" " + HTML5Util.getInnerServerTagContent(ul) + " ");

				// close ul start tag
				modifiedUlTag.append(STYLE + "=\"" + newUlStyleValue + "\">");

				//outputDocument.replace(ul.getStartTag(), modifiedUlTag);
				
				replace(ul.getStartTag(),modifiedUlTag,outputDocument);
					
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL + ">");

				logger.debug("\t" + ul.getDebugInfo() + " replace with "
						+ modifiedUlTag);

			}

			logger.debug("Fixing <ul> elements finished.");

		}
	}

}
