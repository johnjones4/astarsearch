package AStarDickinson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Converter {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 * @throws DOMException 
	 * @throws NumberFormatException 
	 * @throws TransformerException 
	 */
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, NumberFormatException, DOMException, XPathExpressionException, TransformerException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File("mapData.xml"));
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("node");
		for (int s = 0; s < nodeLst.getLength(); s++) {
		    Node node = nodeLst.item(s);
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    int x = Integer.parseInt(((NodeList)xpath.compile(".//point//x//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue());
			int y = Integer.parseInt(((NodeList)xpath.compile(".//point//y//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue());
			
			x = (int)((447d/1148d)*(double)x) + 865;
			y = (int)((322d/826d)*(double)y) + 171;
			
			Node xnode = ((NodeList)xpath.compile(".//point//x").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0);
			Node ynode = ((NodeList)xpath.compile(".//point//y").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0);
			
			xnode.removeChild(xnode.getFirstChild());
			xnode.appendChild(doc.createTextNode(String.valueOf(x)));
			
			ynode.removeChild(ynode.getFirstChild());
			ynode.appendChild(doc.createTextNode(String.valueOf(y)));
		}
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		System.out.println(xmlString);
		
		PrintWriter out = new PrintWriter(new FileWriter(new File("mapData1.xml")));
		out.println(xmlString);
		out.close();
	}

}
