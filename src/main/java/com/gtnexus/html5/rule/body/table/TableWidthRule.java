package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.util.HTML5Util.PX;
import static com.gtnexus.html5.util.HTML5Util.WIDTH;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableWidthRule implements Rule {


	@Override
	public StringBuilder execute(OutputDocument outputDoc,Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();
	
		if (HTML5Util.hasUnit(attributeValue)) {

			replaceString.append(WIDTH + ":" ).append(attributeValue).append(";");
		}
		// add px to the value
		else {

			replaceString.append(WIDTH + ":" ).append(attributeValue).append(PX).append(";");

		}

		return replaceString;
	}

}
