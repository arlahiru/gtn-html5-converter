package com.gtnexus.html5.rule.body.center;

import static com.gtnexus.html5.util.HTML5Util.AUTO;
import static com.gtnexus.html5.util.HTML5Util.CENTER;
import static com.gtnexus.html5.util.HTML5Util.DIV;
import static com.gtnexus.html5.util.HTML5Util.MARGIN;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.TEXT_ALIGN;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class CenterRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		Element center = (Element) originalAttribute;

		// new div tag
		StringBuilder centerDivTag = new StringBuilder("<" + DIV
				+ " id=\"center\" ");

		// new div style
		StringBuilder centerDivStyle = new StringBuilder();

		centerDivStyle.append(TEXT_ALIGN + ":" + CENTER + ";");

		centerDivStyle.append(MARGIN + ": 0 " + AUTO + ";");

		centerDivTag.append(STYLE + "=\"" + centerDivStyle + "\">");

		outputDoc.replace(center.getStartTag(), centerDivTag);
		if (center.getEndTag() != null)
			outputDoc.replace(center.getEndTag(), "</" + DIV + ">");

		return centerDivTag;
	}

}
