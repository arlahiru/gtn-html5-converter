package com.gtnexus.html5.rule.header;

import static com.gtnexus.html5.main.JerichoJspParserUtil.logger;
import static com.gtnexus.html5.util.HTML5Util.DOCTYPE_HTML5;
import static com.gtnexus.html5.util.HTML5Util.HTML5_CONVERTED_COMMENT_PHASE3;
import static com.gtnexus.html5.util.HTML5Util.META_CHARSET_UTF8;
import static com.gtnexus.html5.util.HTML5Util.ADMIN_STYLE_LINK;
import static com.gtnexus.html5.util.HTML5Util.TRADE_STYLE_LINK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import com.gtnexus.html5.main.JerichoJspParserUtil;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTagType;

public class HeaderElementFacade {

	public static void fixHeaderElementObsoleteFeatures(Source source,OutputDocument outputDocument) {

		changeDoctypeToHtml5(source, outputDocument);

		addHtml5ConvertedCommentTag(source, outputDocument);

		addCharSetUTF8MetaTag(source, outputDocument);
		
		//add trade css links inside the <head> element if its not in the main jsp
		addTradeCssStyleLink(source, outputDocument);
	}

	private static void changeDoctypeToHtml5(Source source,OutputDocument outputDocument) {
		
		if (!source.getAllElements(HTMLElementName.HTML).isEmpty()) {
			//check the header element exist before add doctype
			List<Element> docTypeList = source.getAllElements(StartTagType.DOCTYPE_DECLARATION);
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

	public static void addHtml5ConvertedCommentTag(Source source,
			OutputDocument outputDocument) {

		List<Element> allHtmlTag = source.getAllElements(HTMLElementName.HTML);

		if (!allHtmlTag.isEmpty()) {

			Element htmlTag = allHtmlTag.get(0);

			outputDocument.insert(htmlTag.getBegin() + 6,"\n"+ HTML5_CONVERTED_COMMENT_PHASE3+ "\n<!-- Converted Date:"+new Date()+" -->\n");

		} 
		else {

			outputDocument.insert(source.getBegin(),"\n"+ HTML5_CONVERTED_COMMENT_PHASE3 + "\n<!-- Converted Date:"+new Date()+" -->\n");
		}

		logger.debug("HTML5 comment tag added successfully!");

	}

	private static void addCharSetUTF8MetaTag(Source source,
			OutputDocument outputDocument) {

		List<Element> allHeadTag = source.getAllElements(HTMLElementName.HEAD);
		boolean utfMetaTagAdded = false;

		if (!allHeadTag.isEmpty()) {
			//check charset meta tag already exist. if true do a replacement
			List<Element> allMetaTags = source.getAllElements(HTMLElementName.META);
			String charsetValue = null;
			for(Element metaTag: allMetaTags){
				charsetValue = metaTag.getAttributeValue("charset");
				if(charsetValue != null){
					outputDocument.replace(metaTag,META_CHARSET_UTF8+ "\n");
					utfMetaTagAdded = true;
					break;
				}
			}
			if(!utfMetaTagAdded){
				Element headTag = allHeadTag.get(0);	
				outputDocument.insert(headTag.getBegin() + 6, "\n"+META_CHARSET_UTF8	+ "\n");
			}

		}

		logger.debug("UTF8 charset added successfully!");
	}
	
	private static boolean addAdminCssStyleLink(Source source,OutputDocument outputDocument) {
		boolean isAdminCssExist = false;
		List<Element> allHeadTag = source.getAllElements(HTMLElementName.HEAD);
		if (!source.getAllElements(HTMLElementName.HEAD).isEmpty()) {			
			List<Element> linkElements = source.getAllElements(HTMLElementName.LINK);
			for(Element linkElement:linkElements) {
				if(linkElement.getAttributeValue("HREF") != null && linkElement.getAttributeValue("HREF").contains("adminStyle.css")){
					isAdminCssExist = true;
				}
			} 
			if(!isAdminCssExist){
				Element headTag = allHeadTag.get(0);	
				outputDocument.insert(headTag.getEnd() - 7, "\n"+ADMIN_STYLE_LINK+ "\n");
				logger.debug("Admin style added successfully.");
				return true;
			}
		}
		return false;

	}
	
	private static boolean addTradeCssStyleLink(Source source,OutputDocument outputDocument) {
		boolean isAdminCssExist = false;
		List<Element> allHeadTag = source.getAllElements(HTMLElementName.HEAD);
		if (!source.getAllElements(HTMLElementName.HEAD).isEmpty()) {			
			List<Element> linkElements = source.getAllElements(HTMLElementName.LINK);
			for(Element linkElement:linkElements) {
				if(linkElement.getAttributeValue("HREF") != null && linkElement.getAttributeValue("HREF").contains("tradeStyle.css")){
					isAdminCssExist = true;
				}
			} 
			if(!isAdminCssExist){
				Element headTag = allHeadTag.get(0);	
				outputDocument.insert(headTag.getEnd() - 7, "\n"+TRADE_STYLE_LINK+ "\n");
				logger.debug("Trade style added successfully.");
				return true;
			}
		}
		return false;

	}
	
	/*
	public static void runAddAdminCssStyleLink(File directory){

		for (final File file : directory.listFiles()) {
			if (file.isDirectory()) {
				runAddAdminCssStyleLink(file);
			} else {
				//analyze new html5 styles of all the jsp and html files in the current directory
				if ((file.getName().toLowerCase().endsWith(".jsp")|| file.getName().toLowerCase().endsWith(".html"))) {					
					try {
						Source source = new Source(new FileInputStream(file));
						//new output document generated from the source document
						OutputDocument outputDocument = new OutputDocument(source);						
						if(HeaderElementFacade.addAdminCssStyleLink(source, outputDocument)){
							System.out.println("Running addAdminCssStyleLink-> "+file.getPath());
							//save converted jsp output to the disk
							BufferedWriter jspWriter = new BufferedWriter(new OutputStreamWriter(
									new FileOutputStream(file), "UTF-8"));	
							jspWriter.write(outputDocument.toString());	
							jspWriter.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public static void main(String args[]){
		String directoryPathToAnalyzeStyles = "C:\\code\\gtnexus\\devl\\modules\\main\\tcard\\web\\tradecard\\en\\administration";
		File directory = new File(directoryPathToAnalyzeStyles);
		runAddAdminCssStyleLink(directory);
	}
	*/

}
