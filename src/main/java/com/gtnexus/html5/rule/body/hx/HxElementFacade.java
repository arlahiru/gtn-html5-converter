package com.gtnexus.html5.rule.body.hx;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.ALIGN;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class HxElementFacade {

	private static String hxAlignRuleKey = "HX_" + ALIGN;

	public static void fixH1to6Elements(Source source,
			OutputDocument outputDocument) {

		logger.debug("Fixing <hx> elements started...");

		// get all the <h1> tags from the source JSP file
		List<Element> h1ElementList = source.getAllElements(HTMLElementName.H1);

		for (Element h : h1ElementList) {

			replaceHxTag(h, outputDocument);

		}

		// get all the <h2> tags from the source JSP file
		List<Element> h2ElementList = source.getAllElements(HTMLElementName.H2);

		for (Element h : h2ElementList) {

			replaceHxTag(h, outputDocument);

		}

		// get all the <h3> tags from the source JSP file
		List<Element> h3ElementList = source.getAllElements(HTMLElementName.H3);

		for (Element h : h3ElementList) {

			replaceHxTag(h, outputDocument);

		}

		// get all the <h4> tags from the source JSP file
		List<Element> h4ElementList = source.getAllElements(HTMLElementName.H4);

		for (Element h : h4ElementList) {

			replaceHxTag(h, outputDocument);

		}

		// get all the <h5> tags from the source JSP file
		List<Element> h5ElementList = source.getAllElements(HTMLElementName.H5);

		for (Element h : h5ElementList) {

			replaceHxTag(h, outputDocument);

		}

		// get all the <h6> tags from the source JSP file
		List<Element> h6ElementList = source.getAllElements(HTMLElementName.H6);

		for (Element h : h6ElementList) {

			replaceHxTag(h, outputDocument);

		}

		logger.debug("Fixing <hx> elements finished.");

	}

	private static void replaceHxTag(Element h, OutputDocument outputDocument) {

		if (h.getAttributes().get(HTML5Util.ALIGN) != null) {

			StringBuilder modifiedHTag = new StringBuilder();

			StringBuilder newHStyleValue = new StringBuilder();

			// initialize default values.

			switch (h.getName()) {

			case HTMLElementName.H1:
				modifiedHTag.append("<" + HTMLElementName.H1 + " ");
				break;

			case HTMLElementName.H2:
				modifiedHTag.append("<" + HTMLElementName.H2 + " ");
				break;

			case HTMLElementName.H3:
				modifiedHTag.append("<" + HTMLElementName.H3 + " ");
				break;

			case HTMLElementName.H4:
				modifiedHTag.append("<" + HTMLElementName.H4 + " ");
				break;

			case HTMLElementName.H5:
				modifiedHTag.append("<" + HTMLElementName.H5 + " ");
				break;

			case HTMLElementName.H6:
				modifiedHTag.append("<" + HTMLElementName.H6 + " ");
				break;

			}

			Rule rule = JerichoJspParserUtil.RULES_MAP.get(hxAlignRuleKey);

			if (rule != null) {

				StringBuilder returnValue = rule.execute(outputDocument, h.getAttributes().get(HTML5Util.ALIGN), null);
				newHStyleValue.append(returnValue);

			} else {
				logger.info("Rule key not found for key: " + hxAlignRuleKey);

			}

			// append existing style if any
			String existingStyle = h.getAttributeValue(HTML5Util.STYLE);

			if (existingStyle != null)
				newHStyleValue.append(existingStyle);

			// close tr start tag
			modifiedHTag.append(STYLE + "=\"" + newHStyleValue + "\">");

			outputDocument.replace(h.getStartTag(), modifiedHTag);
			// outputDocument.replace(tr.getEndTag(), "</" + TR + ">");

			logger.debug("\t" + h.getDebugInfo() + " replace with "
					+ modifiedHTag);

		}
	}

}
