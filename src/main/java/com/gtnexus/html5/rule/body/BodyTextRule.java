package com.gtnexus.html5.rule.body;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class BodyTextRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		String value = ((Attribute)originalAttribute).getValue();
		return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.COLOR,value));
	}

}
