package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.util.HTML5Util.BORDER;
import static com.gtnexus.html5.util.HTML5Util.PX;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableBorderRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();

		if (HTML5Util.hasUnit(attributeValue)) {

			replaceString.append(BORDER).append(":").append(attributeValue);
					

		} else {

			replaceString.append(BORDER).append(":").append(attributeValue)
					.append(PX);
		}
		
		replaceString.append(" solid black").append(";");

		return replaceString;

	}
}
