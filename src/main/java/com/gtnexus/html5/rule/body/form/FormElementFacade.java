package com.gtnexus.html5.rule.body.form;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.FORM;

import java.util.List;

import com.gtnexus.html5.rule.Rule;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

public class FormElementFacade {

	public static void fixFormObsoleteFeatures(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing form tags started...");

		// get all <form> tags
		List<Element> allFormElements = source.getAllElements(FORM);
		
		// add <br> tag to end of the form tag
		Rule formBrRule = new FormBrRule();

		for (Element form : allFormElements) {
			
			formBrRule.execute(outputDocument, form, null);

		}
		
		logger.debug("Fixing form tags finished.");

	}
}
