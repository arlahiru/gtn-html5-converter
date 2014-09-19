package com.gtnexus.html5.rule.body.center;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.rule.Rule;

public class CenterFacade {
	
	public static void fixCenterTags(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing center tags started...");

		// get all the <font> tags from the source JSP file
		List<Element> fontElementList = source
				.getAllElements(HTMLElementName.CENTER);
		
		Rule centerRule = new CenterRule();

		for (Element center : fontElementList) {
			
			StringBuilder centerDivTag = centerRule.execute(outputDocument, center, null);

			logger.debug(center.getDebugInfo() + " replaced with " + centerDivTag);

		}

		logger.debug("Fixing center tags finished");

	}

}
