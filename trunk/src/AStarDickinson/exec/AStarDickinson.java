package AStarDickinson.exec;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.gui.ControlPanel;
import AStarDickinson.gui.GraphPanel;
import AStarDickinson.gui.ReportPanel;
import AStarDickinson.render.GraphRenderer;

public class AStarDickinson {
	public static final String DEFAULT_IMAGE = "dickinson1.jpg";
	public static final String DEFAULT_DATA = "mapData1.xml";
	public static final boolean ASSERT_UNDIRECTED = true;
	public static final boolean NODE_MARKING = false;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		List<MapNode> graph = new Vector<MapNode>(readNodes().values());

		// Construct the GUI Windows
		GraphPanel panel = new GraphPanel(new GraphRenderer(DEFAULT_IMAGE,graph,.65));
		JFrame frame = makeFrame(panel);
		frame.addComponentListener(panel);
		
		// Construct the GUI Windows
		ReportPanel reportPanel = new ReportPanel();
		ControlPanel controlPanel = new ControlPanel(panel,reportPanel,graph);
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.getInsets().set(10, 10, 10, 10);
		panel1.add(controlPanel,BorderLayout.PAGE_START);
		panel1.add(reportPanel,BorderLayout.PAGE_END);
		JFrame frame1 = makeFrame(panel1);
		frame1.setResizable(false);
	}
	
	/**
	 * Make a JFrame and perform standard setup functions
	 * @param panel A panel to add directly to the JFrame's content pane
	 * @return A new JFrame
	 */
	private static JFrame makeFrame(JPanel panel) {
		JFrame frame = new JFrame("A* Search");
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	/**
	 * Read the XML document of map nodes
	 * @return A map of node name -> node object
	 * @throws Exception
	 */
	private static Map<String,MapNode> readNodes() throws Exception {
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
		return nodes;
	}
	
	/**
	 * Build a list of strings of names of nodes.  This is used for error checking
	 * @param nodeLst A list of xml nodes
	 * @return A collection of strings
	 * @throws DOMException
	 * @throws XPathExpressionException
	 */
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
		
		System.out.println(names.size());
		
		// Return the list
		return names;
	}
	
	/**
	 * Generate a map node from the given XML node
	 * @param node An XML node containing map node data
	 * @param names A list of names of all of the map nodes (read and unread)  This is used for error checking
	 * @return A new MapNode
	 * @throws Exception
	 */
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
