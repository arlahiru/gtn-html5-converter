package com.gtnexus.html5.rule.body.img;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.IMG;
import static com.gtnexus.html5.util.HTML5Util.STYLE;

import java.util.List;

import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import com.gtnexus.html5.facade.Facade;
import com.gtnexus.html5.rule.Rule;
import com.gtnexus.html5.util.HTML5Util;

public class ImageElementFacade extends Facade {

	public static boolean hspaceOrVspaceFound = false;

	static int apixelDivCount = 0;

	public static void fixImgTags(Source source, OutputDocument outputDocument) {

		logger.debug("Fixing img tags started...");

		// get all <img> tags
		List<Element> allImageElements = source.getAllElements(IMG);

		for (Element img : allImageElements) {
			
			Attribute src = img.getAttributes().get("src");

			String srcContent = src != null?src.getValue():"";
			
			Element parent = img.getParentElement();
			
			boolean isATDwithMoreThanOneElementExceptImgTag = isTdContainMorethanOneElementExceptImgTags(parent);

			// fix apixel and fourpixel image tags by replacing them with a div
			// tag with background image
			if (!isATDwithMoreThanOneElementExceptImgTag && (srcContent.contains("apixel.gif")
					|| srcContent.contains("fourpixel.gif"))) {

				Rule apixelImgRule = new ImageApixelRule();

				StringBuilder replaceTag = apixelImgRule.execute(
						outputDocument, img, img);

				replace(img.getStartTag(), replaceTag,outputDocument);
				// outputDocument.replace(img.getEndTag(), "</" + DIV + ">");

			} else {

				if (HTML5Util.hasAttributes((img))) {

					// fix for other img tags goes here

					StringBuilder modifiedImageTag = new StringBuilder();

					StringBuilder newImageStyleValue = new StringBuilder();

					// initialize default values. have to test this!
					modifiedImageTag.append("<" + IMG + " ");

					applyRules(img, outputDocument, newImageStyleValue,
							modifiedImageTag);
					
					// append inner server tags if any
					modifiedImageTag.append(" "
							+ HTML5Util.getInnerServerTagContent(img) + " ");

					// close img start tag
					modifiedImageTag.append(STYLE + "=\"" + newImageStyleValue
							+ "\">");

					replace(img.getStartTag(), modifiedImageTag,outputDocument);
					
					// outputDocument.replace(tr.getEndTag(), "</" + IMG + ">");

					logger.debug("\t" + img.getDebugInfo() + " replace with "
							+ modifiedImageTag);
				}
			}

			hspaceOrVspaceFound = false;

		}

		logger.debug("Fixing img tags finished.");

		/*
		 * // alternatives 1- base64 encode replacement 2- add a div tag and set
		 * image with styles 3- style="empty-cells: show" in table tag
		 * (http://css
		 * -tricks.com/snippets/html/base64-encode-of-1x1px-transparent-gif/)
		 */

	}
	
	private static boolean isTdContainMorethanOneElementExceptImgTags(Element parent){
		//e.g <td> test <img apixel></img> <span>adas</span> </td> cases
		if(parent != null && parent.getName().equals(HTML5Util.TD) && parent.getChildElements().size() > 1){
			List<Element> chileElements = parent.getChildElements();
			for(Element e:chileElements){
				if(e.getName().equals(HTML5Util.IMG)){
					continue;
				}
				else{
					return true;
				}
			}
		}

		return false;
	}

}
