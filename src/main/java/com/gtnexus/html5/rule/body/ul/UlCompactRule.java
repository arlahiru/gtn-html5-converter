package com.gtnexus.html5.rule.body.ul;

import static com.gtnexus.html5.util.HTML5Util.LINE_HEIGHT;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class UlCompactRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		// String attributeValue = ((Attribute)originalAttribute).getValue();

		replaceString.append(LINE_HEIGHT + ":80%;");

		return replaceString;

	}

}
