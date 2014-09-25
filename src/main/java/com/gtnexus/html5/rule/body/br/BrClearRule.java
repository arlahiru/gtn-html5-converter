package com.gtnexus.html5.rule.body.br;

import static com.gtnexus.html5.util.HTML5Util.BOTH;
import static com.gtnexus.html5.util.HTML5Util.CLEAR;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class BrClearRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		StringBuilder replaceString = new StringBuilder();
		
		//String attributeValue = ((Attribute)originalAttribute).getValue();
		
		replaceString.append(CLEAR + ":"+ BOTH + ";");
		
		return replaceString;
		
	}

}
