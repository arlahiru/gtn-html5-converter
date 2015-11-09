package com.gtnexus.html5.rule.body.table.td;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Tag;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class TableDataStyleRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		Element element = (Element) originalElement;
		//ignore tags with scriptlet
		if(!HTML5Util.isTagContainsScriptlet((Tag)element.getStartTag())){
			String originalValue = ((Attribute)originalAttribute).getValue();
			outputDoc.remove(originalAttribute);
			return new StringBuilder(originalValue);
		}else{
			return null;
		}
	}

}
