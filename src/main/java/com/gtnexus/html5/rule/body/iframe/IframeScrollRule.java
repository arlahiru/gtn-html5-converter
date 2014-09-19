package com.gtnexus.html5.rule.body.iframe;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class IframeScrollRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		String value = ((Attribute) originalAttribute).getValue();

		if (value.equalsIgnoreCase("yes")) {

			return new StringBuilder(HTML5Util.formatAttribute(
					HTML5Util.OVERFLOW, HTML5Util.SCROLL));

		} else if (value.equalsIgnoreCase("no")) {

			return new StringBuilder(HTML5Util.formatAttribute(
					HTML5Util.OVERFLOW, HTML5Util.HIDDEN));

		} else {

			return new StringBuilder(HTML5Util.formatAttribute(
					HTML5Util.OVERFLOW, HTML5Util.AUTO));

		}
	}

}
