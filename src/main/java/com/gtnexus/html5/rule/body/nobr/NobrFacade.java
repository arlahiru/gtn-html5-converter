package com.gtnexus.html5.rule.body.nobr;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.NO_BR;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.rule.Rule;

public class NobrFacade {
	
	public static void fixNobrElements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing font tags started...");

		// get all the <font> tags from the source JSP file
		List<Element> fontElementList = source
				.getAllElements(NO_BR);
		
		Rule nobrRule = new NobrRule();

		for (Element font : fontElementList) {
			
			StringBuilder spanTag = nobrRule.execute(outputDocument, font, null);

			logger.debug(font.getDebugInfo() + " replaced with " + spanTag);

		}

		logger.debug("Fixing font tags finished");

	}


}
