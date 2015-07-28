package com.gtnexus.html5.rule.body.table.td;

import static com.gtnexus.html5.util.HTML5Util.TEXT_ALIGN;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableDataAlignRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		StringBuilder replaceString = new StringBuilder();

		String attributeValue = ((Attribute) originalAttribute).getValue();

		// check if td contains a another element(e.g. table,include file) or text then apply fix accordingly
		if (originalElement != null && ((Element)originalElement).getName().toLowerCase().equals((HTML5Util.TD))) {
			
			if(!HTML5Util.isContainAlignableComponent(originalElement))
				replaceString.append(TEXT_ALIGN + ":" + attributeValue + ";");
			
		} else {

			replaceString.append(TEXT_ALIGN + ":" + attributeValue + ";");
		}

		return replaceString;
	}

}
