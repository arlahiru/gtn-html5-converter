package com.gtnexus.html5.rule.body.table.td;

import static com.gtnexus.html5.util.HTML5Util.VERTICAL_ALIGN;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableDataValignRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();

		replaceString.append(VERTICAL_ALIGN + ":" + attributeValue + ";");

		return replaceString;
	}

}
