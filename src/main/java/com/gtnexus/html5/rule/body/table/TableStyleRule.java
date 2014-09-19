package com.gtnexus.html5.rule.body.table;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableStyleRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc, Segment originalAttribute, Segment originalElement) {

		String originalValue = ((Attribute)originalAttribute).getValue();
		outputDoc.remove(originalAttribute);
		return new StringBuilder(originalValue);

	}

}
