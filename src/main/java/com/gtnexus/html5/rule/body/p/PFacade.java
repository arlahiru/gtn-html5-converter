package com.gtnexus.html5.rule.body.p;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.ALIGN;
import static com.gtnexus.html5.util.HTML5Util.AUTO;
import static com.gtnexus.html5.util.HTML5Util.CENTER;
import static com.gtnexus.html5.util.HTML5Util.LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_LEFT;
import static com.gtnexus.html5.util.HTML5Util.MARGIN_RIGHT;
import static com.gtnexus.html5.util.HTML5Util.RIGHT;
import static com.gtnexus.html5.util.HTML5Util.STYLE;
import static com.gtnexus.html5.util.HTML5Util.WIDTH;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.util.HTML5Util;

public class PFacade extends Facade {

	public static void fixPElements(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing <p> elements started...");

		// get all the <table> tags from the source JSP file
		List<Element> pElementList = source.getAllElements(HTMLElementName.P);

		for (Element p : pElementList) {

			if (HTML5Util.hasAttributes((p))) {

				StringBuilder modifiedPTag = new StringBuilder();

				StringBuilder newPStyleValue = new StringBuilder();

				// initialize default values. have to test this!
				modifiedPTag.append("<" + HTMLElementName.P + " ");

				applyRules(p, outputDocument, newPStyleValue, modifiedPTag);

				newPStyleValue = applyPostPFixes(p, newPStyleValue);

				// append inner server tags if any
				modifiedPTag.append(" " + HTML5Util.getInnerServerTagContent(p)
						+ " ");

				// close ul start tag
				modifiedPTag.append(STYLE + "=\"" + newPStyleValue + "\">");

				replace(p.getStartTag(), modifiedPTag, outputDocument);
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.UL
				// + ">");

				logger.debug("\t" + p.getDebugInfo() + " replace with "
						+ modifiedPTag);

			}

			logger.debug("Fixing <p> elements finished.");

		}
	}

	private static StringBuilder applyPostPFixes(Element p,
			StringBuilder newPStyleValue) {

		// apply td align to the table if this table is inside a TD element
		Element parentElement = p.getParentElement();
		if (parentElement != null
				&& parentElement.getName().equals(HTMLElementName.TD)) {

			String tdAlignValue = parentElement.getAttributeValue(ALIGN);
			if (tdAlignValue != null) {

				if (tdAlignValue.equalsIgnoreCase(CENTER)) {

					newPStyleValue.append(MARGIN + ": 0 " + AUTO + ";");

					newPStyleValue.append(WIDTH + ": 50%;");

				} else if (tdAlignValue.equalsIgnoreCase(RIGHT)) {

					newPStyleValue.append(MARGIN_RIGHT + ": 0; " + MARGIN_LEFT
							+ ":" + AUTO + ";");

				} else if (tdAlignValue.equalsIgnoreCase(LEFT)) {

					newPStyleValue.append(MARGIN_LEFT + ": 0; " + MARGIN_RIGHT
							+ ":" + AUTO + ";");

				}
			}
		}

		return newPStyleValue;
	}

}
