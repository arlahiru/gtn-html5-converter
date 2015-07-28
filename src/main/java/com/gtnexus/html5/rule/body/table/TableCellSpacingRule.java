package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.util.HTML5Util.BORDER_COLLAPSE;
import static com.gtnexus.html5.util.HTML5Util.BORDER_SPACING;
import static com.gtnexus.html5.util.HTML5Util.PX;
import static com.gtnexus.html5.util.HTML5Util.SEPERATE;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableCellSpacingRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();

		if (attributeValue.equals("0")) {
			// not work in IE
			//replaceString.append(BORDER_COLLAPSE + ":" + COLLAPSE + ";");
		} else {

			replaceString.append(BORDER_COLLAPSE + ":" + SEPERATE + ";");

		}

		if (HTML5Util.hasUnit(attributeValue)) {

			replaceString.append(BORDER_SPACING + ":" + attributeValue + ";");
		} else {

			replaceString.append(BORDER_SPACING + ":" + attributeValue + PX
					+ ";");

		}

		return replaceString;
	}

}
