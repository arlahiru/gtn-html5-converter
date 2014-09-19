package jsouptest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.nodes.Attribute;

import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class TestJSoupParser {

	public static void main(String args[]) {

		File input = new File("tmp/login.jsp");

		try {

			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

			System.out
					.println("----------------------------------------------original html--------------------------------------------------");

			System.out.println(doc.html());

			System.out
					.println("----------------------------------------------end original html--------------------------------------------------");

			// replace doctype
			//replaceDoctypeWithHtml5(doc);

			// get all effecting root tag types from the html
			Elements body = doc.select("body");

			Elements table_tags = doc.select("table");

			Elements font_tags = doc.select("font");

			Elements img_tags = doc.select("img");

			// parse table tags
			//parseTables(table_tags);

			// parse font tags
			//parseFontTags(font_tags);

			System.out
					.println("----------------------------------------------modified html--------------------------------------------------");

			//System.out.println(doc.html());

			System.out
					.println("----------------------------------------------end modified html--------------------------------------------------");

			File output = new File("tmp/login_modified.jsp");

			BufferedWriter htmlWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(output),
							"UTF-8"));
			
			String modifiedHtml = doc.html();
			
			modifiedHtml = modifiedHtml.replaceAll("&lt;%", "<%");
			
			modifiedHtml = modifiedHtml.replaceAll("%&gt;", "%>");
			
			modifiedHtml = modifiedHtml.replaceAll("&quot;", "\"");

			htmlWriter.write(modifiedHtml);

			htmlWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void replaceDoctypeWithHtml5(Document doc) {

		// remove existing doctype
		List<Node> nods = doc.childNodes();
		for (Node node : nods) {
			if (node instanceof DocumentType) {
				DocumentType documentType = (DocumentType) node;
				System.out.println("Existing doctype: "
						+ documentType.toString());
				node.remove();
			}
		}

		// add html5 doctype
		DocumentType html5 = new DocumentType("html", "", "", "");
		doc.select("html").first().before(html5);

	}

	public static void parseTables(Elements tables) {

		// iterate through tables and remove obsolete attributes
		for (Element e : tables) {

			parseTable(e);

		}

	}

	public static void parseTable(Node table) {

		removeAndReplaceTableLevelObsoleteAttributes(table);

		List<Node> tBodyNodes = table.childNodes();

		for (Node tbody : tBodyNodes) {

			// <tr> level
			List<Node> trNodes = tbody.childNodes();

			for (Node tr : trNodes) {

				removeAndReplaceTableRowLevelObsoleteAttributes(tr);

				// <td> level
				List<Node> tdNodes = tr.childNodes();

				for (Node td : tdNodes) {

					removeAndReplaceTableDataLevelObsoleteAttributes(td);

					// check if td contains another table
					if (td.childNodeSize() > 0) {

						List<Node> tdChildNodes = td.childNodes();

						for (Node node : tdChildNodes) {

							if (node.nodeName().equals("table")) {

								// recursively parse inner table
								parseTable(node);
							}

						}

					}

				}
			}

		}

	}

	public static void removeAndReplaceTableLevelObsoleteAttributes(Node table) {

		String style = "border-collapse: collapse;font-size: 1px; line-height: 0;";

		// remove table level obsolete attributes
		Iterator<Attribute> attributeIterator = table.attributes().iterator();

		while (attributeIterator.hasNext()) {

			Attribute attribute = attributeIterator.next();

			if (attribute.getKey().equals("width")) {

				style = style + "width:" + attribute.getValue() + ";";

				table.removeAttr("width");
			}

			if (attribute.getKey().equals("border")) {

				style = style + "border:" + attribute.getValue() + ";";

				table.removeAttr("border");
			}
			if (attribute.getKey().equals("cellspacing")) {

				style = style + "border-spacing:" + attribute.getValue() + ";";

				table.removeAttr("cellspacing");
			}
			if (attribute.getKey().equals("cellpadding")) {

				style = style + "padding:" + attribute.getValue() + ";";

				table.removeAttr("cellpadding");
			}
			if (attribute.getKey().equals("align")) {

				style = style + "margin-left: auto; margin-right: auto;";

				table.removeAttr("align");
			}

		}

		// System.out.println("table Style:" + style);

		table.attr("style", style);

	}

	public static void removeAndReplaceTableRowLevelObsoleteAttributes(
			Node tableRow) {

		String style = "";

		// remove tr level obsolete attributes
		Iterator<Attribute> attributeIterator = tableRow.attributes()
				.iterator();

		while (attributeIterator.hasNext()) {

			Attribute attribute = attributeIterator.next();

			if (attribute.getKey().equals("align")) {

				style = style + "text-align:" + attribute.getValue() + ";";

				tableRow.removeAttr("align");
			}
			if (attribute.getKey().equals("valign")) {

				style = style + "vertical-align:" + attribute.getValue() + ";";

				tableRow.removeAttr("valign");
			}

		}

		// System.out.println("tr Style:" + style);

		tableRow.attr("style", style);

	}

	public static void removeAndReplaceTableDataLevelObsoleteAttributes(
			Node tableData) {

		String style = "line-height: 0; padding: 0;";

		// remove td level obsolete attributes
		Iterator<Attribute> attributeIterator = tableData.attributes()
				.iterator();

		while (attributeIterator.hasNext()) {

			Attribute attribute = attributeIterator.next();

			if (attribute.getKey().equals("height")) {

				style = style + "height:" + attribute.getValue() + ";";

				tableData.removeAttr("height");
			}
			if (attribute.getKey().equals("width")) {

				style = style + "width:" + attribute.getValue() + ";";

				tableData.removeAttr("width");
			}
			if (attribute.getKey().equals("valign")) {

				style = style + "vertical-align:" + attribute.getValue() + ";";

				tableData.removeAttr("valign");
			}
			if (attribute.getKey().equals("align")) {

				style = style + "text-align:" + attribute.getValue() + ";";

				tableData.removeAttr("align");
			}

		}

		// System.out.println("td Style:" + style);

		tableData.attr("style", style);

	}

	public static void parseFontTags(Elements fonts) {

		for (Element e : fonts) {

			replaceFontTagWithSpanTag(e);
		}
	}

	// replace font tag with a span tag
	public static void replaceFontTagWithSpanTag(Element font) {

		String className = font.className();

		Element spanTag = new Element(Tag.valueOf("span"), "");

		spanTag.attr("class", className);

		spanTag.html(font.html());

		font.replaceWith(spanTag);

	}

}
