package com.gtnexus.html5.rule.body;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;
public class BodyBackgroundRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		String value = ((Attribute)originalAttribute).getValue();
		return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.BACKGROUND_IMAGE,HTML5Util.URL+HTML5Util.parentheses(value)));
	}

}
