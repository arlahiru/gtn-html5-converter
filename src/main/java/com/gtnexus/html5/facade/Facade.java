package com.gtnexus.html5.facade;


import com.gtnexus.html5.exception.HTML5ParserException;

import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;
import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;
public abstract class Facade {

	
	
	public static void replace(Segment originalElement,StringBuilder replacement, OutputDocument output) throws HTML5ParserException{
		try{
			StartTag original_Element = (StartTag) originalElement;
			output.replace(originalElement,replacement.toString());
			dbLogger.log(original_Element.getName(),originalElement.toString(),replacement.toString(),original_Element.getDebugInfo());
		}catch(Exception e){
			HTML5ParserException ex = new HTML5ParserException("Runtime Exception",e.getMessage(),originalElement.getDebugInfo());
			ex.setStackTrace(e.getStackTrace());
			throw ex;
		}
	}
	
}
