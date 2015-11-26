package com.gtnexus.html5.main;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;



public class JerichoTest {

	public static void main(String[] args) throws Exception {
		
/*		File sourceFile = new File("C:\\test\\statementcriteria.include.jsp");
		Source source = new Source(new FileInputStream(sourceFile));
		
		List<Element> divElementList = source.getAllElements(HTMLElementName.DIV);
		
		System.out.println(divElementList.get(0).getAttributeValue("id"));*/
				
/*		String baseFilePath = "C:/code/gtnexus/development/modules/main/tcard";
		
		String sourceFile = baseFilePath+"/web/tradecard/en/administration/login.jsp";

		if (args.length == 0)
			System.err.println("Using default argument of \"" + sourceFile
					+ '"');
		else
			sourceFile = args[0];*/
		
		File sourceFile = new File("C:\\test\\statementcriteria.include.jsp");
		Source source = new Source(new FileInputStream(sourceFile));
		
		JerichoJspParserUtil.initialize(true); // load rules map

		JerichoJspParserUtil.convertToHTML5("C:\\test\\statementcriteria.include.jsp",false,"");
	    
		
	}

}
