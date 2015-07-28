package com.gtnexus.html5.rule.body.hr;

import static com.gtnexus.html5.util.HTML5Util.BACKGROUND_COLOR;
import static com.gtnexus.html5.util.HTML5Util.BORDER_WIDTH;
import static com.gtnexus.html5.util.HTML5Util.COLOR;
import static com.gtnexus.html5.util.HTML5Util.HEIGHT;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class HrNoShadeRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		StringBuilder replaceString = new StringBuilder();

		//String attributeValue = ((Attribute)originalAttribute).getValue();
		
		// cross browser fix
		replaceString.append(HEIGHT + ":2px;"+BORDER_WIDTH+":0px;"+COLOR+":gray;"+BACKGROUND_COLOR+":gray;");
		
		return replaceString;
	}

}
