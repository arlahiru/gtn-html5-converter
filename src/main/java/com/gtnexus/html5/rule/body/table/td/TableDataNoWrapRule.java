package com.gtnexus.html5.rule.body.table.td;

import static com.gtnexus.html5.util.HTML5Util.NO_WRAP;
import static com.gtnexus.html5.util.HTML5Util.WHITE_SPACE;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableDataNoWrapRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		StringBuilder replaceString = new StringBuilder();

		// String attributeValue = ((Attribute)originalAttribute).getValue();
		replaceString.append(WHITE_SPACE + ":" + NO_WRAP + ";");

		return replaceString;
	}

}
