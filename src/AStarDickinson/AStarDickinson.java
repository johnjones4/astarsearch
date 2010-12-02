package AStarDickinson;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.gui.ControlPanel;
import AStarDickinson.gui.ImagePanel;
import AStarDickinson.gui.ReportPanel;

public class AStarDickinson {
	public static final String DEFAULT_IMAGE = "dickinson1.jpg";
	public static final String DEFAULT_DATA = "mapData1.xml";
	public static final boolean ASSERT_UNDIRECTED = true;
	public static final boolean NODE_MARKING = true;
	
	private static final String GUI_FLAG = "-g";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Collection<MapNode> nodes = readNodes();
		
		if (args.length == 1 && args[0].equals(GUI_FLAG)) {
			// Construct the GUI Windows
			ImagePanel panel = new ImagePanel(DEFAULT_IMAGE,nodes,.65);
			JFrame frame = makeFrame(panel);
			frame.addComponentListener(panel);
			
			// Construct the GUI Windows
			ReportPanel reportPanel = new ReportPanel();
			ControlPanel controlPanel = new ControlPanel(panel,reportPanel,nodes);
			JPanel panel1 = new JPanel(new BorderLayout());
			panel1.getInsets().set(10, 10, 10, 10);
			panel1.add(controlPanel,BorderLayout.PAGE_START);
			panel1.add(reportPanel,BorderLayout.PAGE_END);
			JFrame frame1 = makeFrame(panel1);
		} else {
			String startLocation = args[0];
			String endLocation = args[1];
			String algName = args[2];
			
			
		}
	}
	
	private static JFrame makeFrame(JPanel panel) {
		JFrame frame = new JFrame("A* Search");
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	private static Collection<MapNode> readNodes() throws Exception {
		// Create a map of vertex names to vertex objects 
		HashMap<String,MapNode> nodes = new HashMap<String,MapNode>();
		
		// Construct the XML reader
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(DEFAULT_DATA));
		doc.getDocumentElement().normalize();
		
		// Get the vertex node XML objects
		NodeList nodeLst = doc.getElementsByTagName("node");
		
		// Build a list of the names of the vertices
		Collection<String> names = getFirstPassNames(nodeLst);
		
		// For each XML element, build a vertex node object
		for (int s = 0; s < nodeLst.getLength(); s++) {
		    Node node = nodeLst.item(s);
		    MapNode mnode = generateMapNode(node,names);
		    nodes.put(mnode.getName(),mnode);
		}
		
		// Convert the edges from strings of names into objects
		// Also check for reciprocation if applicable
		for (MapNode mnode: nodes.values()) {
			for(String name: mnode.getEdgeNames()) {
				// Resolve the name to the object
				MapNode other = nodes.get(name);
				mnode.addEdge(other);
				
				// Assert undirectedness if applicable
				if (ASSERT_UNDIRECTED && !other.getEdgeNames().contains(mnode.getName()))
					throw new Exception("Undirected edges not properly defined for " + mnode.getName() + "<->" + other.getName());
			}
		}
		
		// Return the generate nodes
		return new LinkedList<MapNode>(nodes.values());
	}
	
	private static Collection<String> getFirstPassNames(NodeList nodeLst) throws DOMException, XPathExpressionException {
		// Setup the needed objects
		XPath xpath = XPathFactory.newInstance().newXPath();
		LinkedList<String> names = new LinkedList<String>();
		
		// Loop through all of the XML "node" nodes
		for (int s = 0; s < nodeLst.getLength(); s++) {
			// Get the string value of the "name" node
		    Node node = nodeLst.item(s);
		    String name = ((NodeList)xpath.compile(".//name//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue();
		    
		    // Add the name to the list
		    names.add(name);
		}
		
		// Return the list
		return names;
	}
	
	private static MapNode generateMapNode(Node node,Collection<String> names) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String name = ((NodeList)xpath.compile(".//name//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue();
		int x = Integer.parseInt(((NodeList)xpath.compile(".//point//x//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue());
		int y = Integer.parseInt(((NodeList)xpath.compile(".//point//y//text()").evaluate(node.getChildNodes() ,XPathConstants.NODESET)).item(0).getNodeValue());
		String str = ((Element)node).getAttribute("destination");
		boolean dest = Boolean.parseBoolean(str);
		
		MapNode mnode = new MapNode(name,x,y, dest);
		
		NodeList edges = (NodeList)xpath.compile(".//edges/*").evaluate(node.getChildNodes() ,XPathConstants.NODESET);
		for (int s = 0; s < edges.getLength(); s++) {
			String edgeName = edges.item(s).getFirstChild().getNodeValue();
			if (!names.contains(edgeName))
				throw new Exception("Unknown vertex defination on "+ name + " -> " + edgeName);
			mnode.addEdgeName(edgeName);
		}
		
		return mnode;
	}
}
