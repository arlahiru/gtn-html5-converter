package com.gtnexus.html5.rule.body.li;

import net.htmlparser.jericho.Attribute;
import static com.gtnexus.html5.util.HTML5Util.LIST_STYLE_TYPE;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class LiTypeRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();
		
		String attributeValue = ((Attribute)originalAttribute).getValue();
		
		replaceString.append(LIST_STYLE_TYPE + ":"+ attributeValue + ";");
		
		return replaceString;
	}

}
