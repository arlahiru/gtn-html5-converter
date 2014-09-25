package com.gtnexus.html5.rule.body;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class BodyLinkRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		String value =((Attribute)originalAttribute).getValue();
		String type = ((Attribute)originalAttribute).getName();
		
		switch(type.toLowerCase()){
			case HTML5Util.LINK:
				return new StringBuilder(HTML5Util.cssElementFix(HTML5Util.LINK,HTML5Util.COLOR, value));
			case HTML5Util.VLINK:
				return new StringBuilder(HTML5Util.cssElementFix(HTML5Util.VISITED_LINK,HTML5Util.COLOR, value));
			case HTML5Util.ALINK:
				return new StringBuilder(HTML5Util.cssElementFix(HTML5Util.ACTIVE_LINK,HTML5Util.COLOR, value));
		
		}
		return null;
		}

}
