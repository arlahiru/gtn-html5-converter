package com.gtnexus.html5.rule.body.font;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class FontSizeRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		String value = ((Attribute) originalAttribute).getValue();
		
		return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.FONT_SIZE,
				HTML5Util.appendPx(value)));
	}

}
