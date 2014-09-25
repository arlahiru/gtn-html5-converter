package com.gtnexus.html5.rule.body;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;


public class BodyMarginRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		String type = ((Attribute)originalAttribute).getName();
		String value = ((Attribute)originalAttribute).getValue();
		switch(type.toLowerCase()){
			case HTML5Util.TOP_MARGIN:
				return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.MARGIN_TOP,HTML5Util.appendPx(value)));	
			case HTML5Util.LEFT_MARGIN:
				return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.MARGIN_LEFT,HTML5Util.appendPx(value)));	
			case HTML5Util.MARGIN_WIDTH:
				return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.MARGIN_LEFT,HTML5Util.appendPx(value)));	
			case HTML5Util.MARGIN_HEIGHT:
				return new StringBuilder(HTML5Util.formatAttribute(HTML5Util.MARGIN_TOP,HTML5Util.appendPx(value)));	
			 
		}
		return null;
	}
}
