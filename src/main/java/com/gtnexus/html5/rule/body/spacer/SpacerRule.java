package com.gtnexus.html5.rule.body.spacer;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.DIV;
import static com.gtnexus.html5.util.HTML5Util.HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.LINE_HEIGHT;
import static com.gtnexus.html5.util.HTML5Util.PX;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.WIDTH;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Segment;

import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class SpacerRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		Element spacer = (Element) originalAttribute;

		// new div tag
		StringBuilder spacerDivTag = new StringBuilder("<" + DIV
				+ " id=\"spacer\" ");

		// new div style
		StringBuilder spacerDivStyle = new StringBuilder();

		String spacerWidth = spacer.getAttributeValue(WIDTH);

		if (spacerWidth != null) {

			if (HTML5Util.hasUnit(spacerWidth)) {

				spacerDivStyle.append(WIDTH + ":" + spacerWidth + ";");
			} else {

				spacerDivStyle.append(WIDTH + ":" + spacerWidth + PX + ";");
			}
		}
		String spacerHeight = spacer.getAttributeValue(HEIGHT);

		if (spacerHeight != null) {

			if (HTML5Util.hasUnit(spacerHeight)) {

				spacerDivStyle.append(HEIGHT + ":" + spacerHeight + ";");
				spacerDivStyle.append(LINE_HEIGHT + ":" + spacerHeight + ";");
			} else {

				spacerDivStyle.append(HEIGHT + ":" + spacerHeight + PX + ";");
				spacerDivStyle.append(LINE_HEIGHT + ":" + spacerHeight + PX
						+ ";");

			}
		}

		spacerDivTag.append(STYLE + "=\"" + spacerDivStyle + "\">");

		spacerDivTag.append("</" + DIV + ">");

		logger.debug("replaced with apixel div tag:" + spacerDivTag);

		return spacerDivTag;
	}
}
