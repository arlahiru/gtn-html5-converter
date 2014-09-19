package com.gtnexus.html5.rule.body.table;

import static com.gtnexus.html5.util.HTML5Util.BACKGROUND_IMAGE;
import static com.gtnexus.html5.util.HTML5Util.URL;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableBgRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute)originalAttribute).getValue();
	
		replaceString.append(BACKGROUND_IMAGE + ":"+URL+"(" + attributeValue + ");");

		return replaceString;
	}

}
