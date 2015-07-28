package com.gtnexus.html5.rule.body.img;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.BORDER;
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

public class ImageApixelRule implements Rule {

	@Override
	public StringBuilder execute(OutputDocument outputDoc,
			Segment originalAttribute, Segment originalElement) {

		logger.debug("Fixing apixel img tag started...");

		Element image = (Element) originalAttribute;
		
		String srcContent = image.getAttributes().get("src").getValue();
		
		ImageElementFacade.apixelDivCount++;

		// new div tag
		StringBuilder apixelDivTag = new StringBuilder("<" + DIV
				+ " id=\"afpixel"+ImageElementFacade.apixelDivCount+"\" ");		

		// new div style
		StringBuilder apixelDivStyle = new StringBuilder();

		String imageWidth = image.getAttributeValue(WIDTH);

		if (imageWidth != null) {

			if (HTML5Util.hasUnit(imageWidth)) {

				apixelDivStyle.append(WIDTH + ":" + imageWidth + ";");
			} else {

				apixelDivStyle.append(WIDTH + ":" + imageWidth + PX + ";");
			}
		}
		String imageHeight = image.getAttributeValue(HEIGHT);

		if (imageHeight != null) {

			if (HTML5Util.hasUnit(imageHeight)) {

				apixelDivStyle.append(HEIGHT + ":" + imageHeight + ";");
				apixelDivStyle.append(LINE_HEIGHT + ":" + imageHeight + ";");
			} else {

				apixelDivStyle.append(HEIGHT + ":" + imageHeight + PX + ";");
				apixelDivStyle.append(LINE_HEIGHT + ":" + imageHeight + PX
						+ ";");

			}
		}
		String imageBorder = image.getAttributeValue(BORDER);
		if (imageBorder != null) {
			apixelDivStyle.append(BORDER + ":" + imageBorder);

			if (HTML5Util.hasUnit(imageBorder)) {
				apixelDivStyle.append(" solid black;");
			} else {

				apixelDivStyle.append("px solid black;");
			}
		}
		
		apixelDivStyle.append(HTML5Util.formatAttribute(HTML5Util.BACKGROUND_IMAGE,HTML5Util.URL+HTML5Util.parentheses(srcContent)));
		
		apixelDivTag.append(STYLE + "=\"" + apixelDivStyle + "\">");

		apixelDivTag.append("</" + DIV + ">");

		logger.debug("replaced with apixel div tag:" + apixelDivTag);

		return apixelDivTag;
	}

}
