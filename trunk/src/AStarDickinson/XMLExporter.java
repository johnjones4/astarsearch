package AStarDickinson;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import AStarDickinson.datastructs.graph.MapNode;

public class XMLExporter {
	public String writeXML(Collection<MapNode> nodes) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			Node root = doc.createElement("nodes");
			doc.appendChild(root);
			for(MapNode node: nodes) {
				root.appendChild(node.xml(doc));
			}
			TransformerFactory transfact = TransformerFactory.newInstance();
			transfact.setAttribute("indent-number", 4);
			Transformer transformer = transfact.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			//System.out.println(xmlString);
			return xmlString;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void saveXML(Collection<MapNode> nodes, String filename) {
		try {
			String xml = this.writeXML(nodes);
			PrintWriter out = new PrintWriter(new FileWriter(new File(filename)));
			out.print(xml);
			out.close();
			System.out.println("saved");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
