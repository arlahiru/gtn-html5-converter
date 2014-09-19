package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.util.HTML5Util.AUTO;
import static com.gtnexus.html5.util.HTML5Util.CENTER;
import static com.gtnexus.html5.util.HTML5Util.FLOAT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_RIGHT;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableAlignRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();

		if (attributeValue.equalsIgnoreCase(CENTER)) {

			replaceString.append(MARGIN_LEFT + ":" + AUTO + ";" + MARGIN_RIGHT
					+ ":" + AUTO + ";");
		} else {
			replaceString.append(FLOAT + ":" + attributeValue + ";");

		}

		return replaceString;

	}

}
