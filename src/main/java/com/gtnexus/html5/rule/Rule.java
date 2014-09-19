/**
 * 
 */
package com.gtnexus.html5.rule;

import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

public interface Rule {
	
	StringBuilder execute (OutputDocument outputDoc, Segment originalAttribute, Segment originalElement);

}
