package com.gtnexus.html5.rule.body.form;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import static com.gtnexus.html5.util.HTML5Util.PADDING_BOTTOM;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class FormBrRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		Element form = (Element) originalAttribute;

		String existingStyle = form.getAttributeValue(STYLE);

		if (form.getAttributes().get(STYLE) != null) {
			outputDoc.remove(form.getAttributes().get(STYLE));
		}

		String newFormStyle = HTML5Util
				.formatAttribute(PADDING_BOTTOM, "0.8em");

		if (existingStyle != null)
			newFormStyle = existingStyle + newFormStyle;

		outputDoc.insert(form.getBegin() + "<form>".length() - 1, " " + STYLE
				+ "=\"" + newFormStyle + "\" ");

		// outputDoc.replace(form.getEndTag(),"</FORM><br>");

		return replaceString;
	}

}
