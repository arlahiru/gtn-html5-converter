package com.gtnexus.html5.rule.body.spacer;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.SPACER;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.rule.Rule;

public class SpacerElementFacade {

	public static void fixSpacerTags(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing <spacer> tags started...");

		// get all <spacer> tags
		List<Element> allSpacerElements = source.getAllElements(SPACER);

		for (Element spacer : allSpacerElements) {

			// fix spacer tags by replacing them with a div tag

			Rule spacerRule = new SpacerRule();

			StringBuilder replaceTag = spacerRule.execute(outputDocument,
					spacer, null);

			outputDocument.replace(spacer.getStartTag(), replaceTag);
			// outputDocument.replace(img.getEndTag(), "</" + DIV + ">");

		}

		logger.debug("Fixing <spacer> tags finished.");

	}

}
