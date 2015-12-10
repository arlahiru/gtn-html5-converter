package com.gtnexus.html5.rule.body.form;

import static com.gtnexus.html5.util.HTML5Util.PADDING_BOTTOM;
import static com.gtnexus.html5.util.HTML5Util.CLASS;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class FormBrRule implements Rule {

	//@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		
		Element form = (Element) originalAttribute;
		//ignore tags with scriptlet and empty forms
		if(!HTML5Util.isTagContainsScriptlet(form.getStartTag()) && !isEmptyForm(form)){

			StringBuilder replaceString = new StringBuilder();			
	
			String existingCssClasses = form.getAttributeValue(CLASS);
	
			if (form.getAttributes().get(CLASS) != null) {
				outputDoc.remove(form.getAttributes().get(CLASS));
			}
	
			String newFormCssClass = "html5-form-padding";
	
			if (existingCssClasses != null)
				newFormCssClass = existingCssClasses +" " +newFormCssClass;
	
			outputDoc.insert(form.getBegin() + "<form>".length() - 1, " " + CLASS
					+ "=\"" + newFormCssClass + "\" ");
	
			// outputDoc.replace(form.getEndTag(),"</FORM><br>");
	
			return replaceString;
		}else{
			return null;
		}
	}
	
	private boolean isEmptyForm(Element form){
		if(form.getAllElements() != null && !form.getAllElements().isEmpty())
			 return false;
		else
			return true;
	}

}
