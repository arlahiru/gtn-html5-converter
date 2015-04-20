package com.gtnexus.html5.rule.header;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.DOCTYPE_HTML5;
import static com.gtnexus.html5.util.HTML5Util.HTML5_CONVERTED_COMMENT;
import static com.gtnexus.html5.util.HTML5Util.META_CHARSET_UTF8;

import java.util.Date;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

public class HeaderElementFacade {

	public static void fixHeaderElementObsoleteFeatures(Source source,
			OutputDocument outputDocument, boolean isIncludeFile) {

		changeDoctypeToHtml5(source, outputDocument, isIncludeFile);

		addHtml5ConvertedCommentTag(source, outputDocument);

		addCharSetUTF8MetaTag(source, outputDocument);

	}

	private static void changeDoctypeToHtml5(Source source,
			OutputDocument outputDocument, boolean isIncludeFile) {

		//check the header element exist before add doctype
		if (!isIncludeFile && !source.getAllElements(HTMLElementName.HTML).isEmpty()) {
			List<Element> docTypeList = source
					.getAllElements(StartTagType.DOCTYPE_DECLARATION);
			if (docTypeList.size() > 0) {

				Element docTypeElement = docTypeList.get(0);

				// remove existing doctype and add html5 doctype
				outputDocument.replace(docTypeElement, DOCTYPE_HTML5);
			} else {

				// add html5 doc type
				outputDocument.insert(0, DOCTYPE_HTML5 + "\n");
			}

			logger.debug("Doctype changed successfully.");
		}

	}

	private static void addHtml5ConvertedCommentTag(Source source,
			OutputDocument outputDocument) {

		List<Element> allHtmlTag = source.getAllElements(HTMLElementName.HTML);

		if (!allHtmlTag.isEmpty()) {

			Element htmlTag = allHtmlTag.get(0);

			outputDocument.insert(htmlTag.getBegin() + 6,"\n"+ HTML5_CONVERTED_COMMENT + "\n<!-- Converted Date:"+new Date()+" -->\n");

		} else {

			outputDocument.insert(source.getBegin(),"\n"+ HTML5_CONVERTED_COMMENT + "\n<!-- Converted Date:"+new Date()+" -->\n");
		}

		logger.debug("HTML5 comment tag added successfully!");

	}

	private static void addCharSetUTF8MetaTag(Source source,
			OutputDocument outputDocument) {

		List<Element> allHeadTag = source.getAllElements(HTMLElementName.HEAD);

		if (!allHeadTag.isEmpty()) {

			Element headTag = allHeadTag.get(0);

			outputDocument.insert(headTag.getBegin() + 6, META_CHARSET_UTF8
					+ "\n");

		}

		logger.debug("UTF8 charset added successfully!");
	}

}
