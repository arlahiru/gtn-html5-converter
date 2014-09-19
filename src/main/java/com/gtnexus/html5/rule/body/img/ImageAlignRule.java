package com.gtnexus.html5.rule.body.img;

import static com.gtnexus.html5.util.HTML5Util.BOTTOM;
import static com.gtnexus.html5.util.HTML5Util.FLOAT;
import static com.gtnexus.html5.util.HTML5Util.MIDDLE;
import static com.gtnexus.html5.util.HTML5Util.TOP;
import static com.gtnexus.html5.util.HTML5Util.VERTICAL_ALIGN;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class ImageAlignRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute) originalAttribute).getValue();

		if (attributeValue.equalsIgnoreCase(TOP)
				|| attributeValue.equalsIgnoreCase(BOTTOM)
				|| attributeValue.equalsIgnoreCase(MIDDLE)) {

			replaceString.append(VERTICAL_ALIGN + ":" +attributeValue+";");
		} else {
			replaceString.append(FLOAT + ":" + attributeValue + ";");

		}

		return replaceString;
	}

}
