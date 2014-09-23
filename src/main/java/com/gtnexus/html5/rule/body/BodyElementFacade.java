package com.gtnexus.html5.rule.body;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.rule.body.br.BrFacade;
import com.gtnexus.html5.rule.body.caption.CaptionFacade;
import com.gtnexus.html5.rule.body.center.CenterFacade;
import com.gtnexus.html5.rule.body.div.DivFacade;
import com.gtnexus.html5.rule.body.font.FontElementFacade;
import com.gtnexus.html5.rule.body.form.FormElementFacade;
import com.gtnexus.html5.rule.body.hr.HrFacade;
import com.gtnexus.html5.rule.body.hx.HxElementFacade;
import com.gtnexus.html5.rule.body.img.ImageElementFacade;
import com.gtnexus.html5.rule.body.input.InputElementFacade;
import com.gtnexus.html5.rule.body.legend.LegendFacade;
import com.gtnexus.html5.rule.body.li.LiFacade;
import com.gtnexus.html5.rule.body.nobr.NobrFacade;
import com.gtnexus.html5.rule.body.ol.OlFacade;
import com.gtnexus.html5.rule.body.p.PFacade;
import com.gtnexus.html5.rule.body.pre.PreFacade;
import com.gtnexus.html5.rule.body.spacer.SpacerElementFacade;
import com.gtnexus.html5.rule.body.table.TableElementFacade;
import com.gtnexus.html5.rule.body.ul.UlFacade;
import com.gtnexus.html5.util.HTML5Util;

public class BodyElementFacade extends Facade {

	// fix obsolete features in all the elements inside html body including the
	// body tag
	public static void fixAllBodyElementObsoleteFeatures(Source source,
			OutputDocument outputDocument) throws HTML5ParserException{
		try{
			fixBodyElementObsoleteFeatures(source, outputDocument);
	
			TableElementFacade.fixLegacyTables(source, outputDocument);
	
			FontElementFacade.fixFontTags(source, outputDocument);
	
			ImageElementFacade.fixImgTags(source, outputDocument);
	
			FormElementFacade.fixFormObsoleteFeatures(source, outputDocument);
	
			BrFacade.fixBRElements(source, outputDocument);
	
			CaptionFacade.fixCaptionElements(source, outputDocument);
	
			CenterFacade.fixCenterTags(source, outputDocument);
	
			DivFacade.fixDivElements(source, outputDocument);
	
			HrFacade.fixHRElements(source, outputDocument);
	
			HxElementFacade.fixH1to6Elements(source, outputDocument);
	
			ImageElementFacade.fixImgTags(source, outputDocument);
	
			InputElementFacade.fixInputTags(source, outputDocument);
	
			LegendFacade.fixLegendElements(source, outputDocument);
	
			LiFacade.fixLiElements(source, outputDocument);
	
			NobrFacade.fixNobrElements(source, outputDocument);
	
			OlFacade.fixOlElements(source, outputDocument);
	
			PFacade.fixPElements(source, outputDocument);
	
			PreFacade.fixPreElements(source, outputDocument);
	
			SpacerElementFacade.fixSpacerTags(source, outputDocument);
	
			UlFacade.fixUlElements(source, outputDocument);
		}catch(HTML5ParserException e){
			throw e;
		}
	}

	public static void fixBodyElementObsoleteFeatures(Source source,
			OutputDocument outputDocument) throws HTML5ParserException {

		logger.debug("Fixing body tag started...");

		// get <body> tag
		List<Element> bodyElement = source.getAllElements(HTMLElementName.BODY);

		// Apparently this loop run only once for a given main jsp page.
		for (Element body : bodyElement) {

			if (HTML5Util.hasAttributes((body))) {

				StringBuilder modifiedBodyTag = new StringBuilder();

				StringBuilder newBodyStyleValue = new StringBuilder();

				// initialize replacing start tag
				modifiedBodyTag.append("<" + HTMLElementName.BODY + " ");

				// remove table level obsolete attributes
				Iterator<Attribute> bodyAttributeIterator = body
						.getAttributes().iterator();

				while (bodyAttributeIterator.hasNext()) {

					Attribute bodyAttribute = bodyAttributeIterator.next();
					String attributeName = bodyAttribute.getKey();
					String ruleKey = body.getName() + "_"
							+ attributeName.toLowerCase();

					Rule rule = JerichoJspParserUtil.RULES_MAP.get(ruleKey);

					if (rule != null) {

						StringBuilder returnValue = rule.execute(
								outputDocument, bodyAttribute, body); //@lahiru r: passed the body element as the orignal element

						if (returnValue != null) {
							newBodyStyleValue.append(returnValue);
						} else {

							// if rule does not return value, do appropriate fix
							// here.

						}

					} else {

						logger.info("Rule key not found for key: " + ruleKey);
						modifiedBodyTag.append(bodyAttribute);
						modifiedBodyTag.append(" ");
						logger.info(ruleKey + " appended as it is.");
					}

				}
				
				// append inner server tags if any
				modifiedBodyTag.append(" " + HTML5Util.getInnerServerTagContent(body) + " ");

				// close body start tag
				modifiedBodyTag.append(STYLE + "=\"" + newBodyStyleValue
						+ "\">");
				
				//outputDocument.replace(body.getStartTag(), modifiedBodyTag);
				try{
					replace(body.getStartTag(),modifiedBodyTag,outputDocument);
				}catch(HTML5ParserException e){
					throw e;
				}
				// outputDocument.replace(tr.getEndTag(), "</" +
				// HTMLElementName.BODY + ">");

				logger.debug("\t" + body.getDebugInfo() + " replace with "
						+ modifiedBodyTag);
			}

			logger.debug("Fixing body tag finished.");

		}
	}
}
