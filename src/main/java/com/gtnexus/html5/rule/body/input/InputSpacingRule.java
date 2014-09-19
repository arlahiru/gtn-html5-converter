package com.gtnexus.html5.rule.body.input;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.rule.body.iframe.IframeFacade;
import com.gtnexus.html5.rule.body.img.ImageElementFacade;
import com.gtnexus.html5.util.HTML5Util;

public class InputSpacingRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {
		String[] argOrder = new String[] { HTML5Util.VSPACE, HTML5Util.HSPACE };

		if (InputElementFacade.hspaceOrVspaceFound) {
			return new StringBuilder("");
		}
		InputElementFacade.hspaceOrVspaceFound = true;
		ImageElementFacade.hspaceOrVspaceFound = true;
		IframeFacade.hspaceOrVspaceFound = true;
		return new StringBuilder(HTML5Util.generateMultiValuedAttribute(
				HTML5Util.MARGIN, HTML5Util.getAttributeValues(
						(Element) originalElement, argOrder), "0", argOrder));
	}
}
