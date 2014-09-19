package com.gtnexus.html5.rule.body.nobr;

import static com.gtnexus.html5.util.HTML5Util.NO_WRAP;
import static com.gtnexus.html5.util.HTML5Util.SPAN;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.WHITE_SPACE;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class NobrRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		Element nobr = (Element) originalAttribute;

		// replace nobr tag
		StringBuilder spanTag = new StringBuilder("<" + SPAN + " " + STYLE
				+ "=\"" + WHITE_SPACE + ":" + NO_WRAP + ";" + "\">");

		outputDoc.replace(nobr.getStartTag(), spanTag);

		outputDoc.replace(nobr.getEndTag(), "</" + SPAN + ">");

		return spanTag;

	}
}
