package com.gtnexus.html5.rule.body.table;

import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;

public class TableCellPaddingRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,Segment originalAttribute, Segment originalElement) {
		
		//returns null if no fix available or this should go to child level of this element
		return null;
	}

}
