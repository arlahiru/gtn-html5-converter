package com.gtnexus.html5.rule.body.table;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableStyleRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		String originalValue = ((Attribute) originalAttribute).getValue();

		String[] styleValues = originalValue.split(";");

		StringBuilder modifiedStyle = new StringBuilder();

		for (int i = 0; i < styleValues.length; i++) {

			if (styleValues[i] != null && !styleValues[i].isEmpty() && styleValues[i].split(":").length == 2) {

				String attributeName = styleValues[i].trim().toLowerCase()
						.split(":")[0];
				String attributeValue = styleValues[i].trim().toLowerCase()
						.split(":")[1];

				// check width: 200; like value in the style and add px unit to
				// fix onlick js issue
				// GTBUG-6673
				if (attributeName.equals("width")) {
					if (!HTML5Util.hasUnit(attributeValue)) {
						modifiedStyle.append(attributeName+":"+ attributeValue + HTML5Util.PX+";");
					}

				} else {
					modifiedStyle.append(styleValues[i]+";");
				}
			}
		}
		outputDoc.remove(originalAttribute);

		return modifiedStyle;

	}

}
