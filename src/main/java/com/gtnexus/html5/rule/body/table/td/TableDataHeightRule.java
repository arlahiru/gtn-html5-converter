package com.gtnexus.html5.rule.body.table.td;

import static com.gtnexus.html5.util.HTML5Util.HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.PX;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableDataHeightRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();

		if (HTML5Util.hasUnit(attributeValue)) {

			replaceString.append(HEIGHT + ":" + attributeValue + ";");
		} else {

			replaceString.append(HEIGHT + ":" + attributeValue + PX + ";");
		}
		
		return replaceString;
	}

}
