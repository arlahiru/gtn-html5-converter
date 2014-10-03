package com.gtnexus.html5.main;



public class JerichoTest {

	public static void main(String[] args) throws Exception {
		
		String baseFilePath = "C:/code/gtnexus/development/modules/main/tcard";
		
		String sourceFile = baseFilePath+"/web/tradecard/en/administration/login.jsp";

		if (args.length == 0)
			System.err.println("Using default argument of \"" + sourceFile
					+ '"');
		else
			sourceFile = args[0];
		
		JerichoJspParserUtil.initialize(); // load rules map

	//	JerichoJspParserUtil.convertToHTML5(sourceFile,false);
		
	}

}
