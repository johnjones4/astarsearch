package AStarDickinson.datastructs;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A MapNode is a vertex within the graph. Its uniqueness is based upon is point
 * (x,y plain) on the map.
 * 
 * @author johnjones
 * 
 */
public class MapNode implements Comparable<MapNode> {
	private String name;
	private MapPoint point;
	private Collection<MapNode> edges;
	private Collection<String> edgeNames;
	private boolean destination;

	/**
	 * Construct a new node
	 * 
	 * @param name
	 *            The name of the node. This does not have to be unique
	 * @param x
	 *            The x coordinate of the node. This or the y coordinate make
	 *            the node unique
	 * @param y
	 *            The y coordinate of the node. This or the x coordinate make
	 *            the node unique
	 * @param destination
	 *            If true: this node appears in the GUI's list of starting and
	 *            ending points.
	 */
	public MapNode(String name, int x, int y, boolean destination) {
		super();
		this.name = name;
		this.point = new MapPoint(x, y);
		this.edges = new LinkedList<MapNode>();
		this.edgeNames = new LinkedList<String>();
		this.destination = destination;
	}

	/**
	 * Draw the node. The reduction value is a percentage (0<=reduction) to
	 * increase or decrease the proportion of the true and and y values.
	 * 
	 * @param g
	 *            The graphics context to draw to
	 * @param color
	 *            The color of the node
	 * @param reduction
	 *            The value that proportionally increases or decreases the
	 *            location in which the point is drawn.
	 */
	public void draw(Graphics g, Color color, double reduction) {
		int x = (int) ((double) point.getX() * reduction);
		int y = (int) ((double) point.getY() * reduction);
		g.setColor(color);
		g.fillOval(x - 5, y - 5, 10, 10);
	}

	/**
	 * Draw an edge between two vertices/edges. If the node provided is not
	 * connected to this node, an exception is thrown.
	 * 
	 * @param g
	 *            The graphics context to draw to
	 * @param edge
	 *            The other node to draw the edge for
	 * @param color
	 *            The color of the node
	 * @param reduction
	 *            The value that proportionally increases or decreases the
	 *            location in which the edge is drawn.
	 */
	public void drawEdge(Graphics g, MapNode edge, Color color, double reduction) {
		if (!edges.contains(edge))
			throw new IllegalArgumentException(
					"That edge is not in this graph.");

		int x1 = (int) ((double) point.getX() * reduction);
		int y1 = (int) ((double) point.getY() * reduction);

		int x2 = (int) ((double) edge.getPoint().getX() * reduction);
		int y2 = (int) ((double) edge.getPoint().getY() * reduction);

		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}

	/**
	 * The name of the node
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The x,y point of the node
	 * 
	 * @return the point
	 */
	public MapPoint getPoint() {
		return point;
	}

	/**
	 * The edges that this node has
	 * 
	 * @return a collection of edges
	 */
	public Collection<MapNode> getEdges() {
		return edges;
	}

	/**
	 * Return whether or not this node is a destination node. A destination node
	 * is one which appears in the GUI's list of starting and ending locations.
	 * 
	 * @return true: this is a destination
	 */
	public boolean isDestination() {
		return destination;
	}

	/**
	 * Add an edge name to this vertex. This method is used exclusively by the
	 * load-from-file routines.
	 * 
	 * @param name
	 *            The name of the vertex that shares the edge.
	 */
	public void addEdgeName(String name) {
		this.edgeNames.add(name);
	}

	/**
	 * Get a collection of the names of vertices that this vertex shares an edge
	 * with. This method is used exclusively by the load-from-file routines.
	 * 
	 * @return a collection of the names of vertices
	 */
	public Collection<String> getEdgeNames() {
		return this.edgeNames;
	}

	/**
	 * Add an actual edge to the graph.
	 * 
	 * @param node
	 *            The vertex that shares the edge.
	 */
	public void addEdge(MapNode node) {
		this.edges.add(node);
	}

	/**
	 * Return the name of the node
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Use the distance formula to compute a distance to another MapNode
	 * 
	 * @param vertex
	 *            The other node
	 * @return The distance to the provided node
	 */
	public double getDistanceToNode(MapNode vertex) {
		int x1 = this.point.getX();
		int y1 = this.point.getY();
		int x2 = vertex.point.getX();
		int y2 = vertex.point.getY();
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	/**
	 * Convert this object to XML.  This is used when saving the graph to disk.
	 * 
	 * @param doc The document that we are building the XML on
	 * @return An XML element
	 */
	public Element xml(Document doc) {
		Element node = doc.createElement("node");

		node.setAttribute("destination", String.valueOf(destination));

		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode(this.name));
		node.appendChild(name);

		node.appendChild(point.xml(doc));

		Element edges = doc.createElement("edges");
		for (MapNode edgenode : this.edges) {
			Element edgexml = doc.createElement("edge");
			edgexml.appendChild(doc.createTextNode(edgenode.name));
			edges.appendChild(edgexml);
		}
		node.appendChild(edges);

		return node;
	}

	/**
	 * Compare based on the name field.
	 */
	@Override
	public int compareTo(MapNode o) {
		return this.name.compareTo(o.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapNode other = (MapNode) obj;
		if (point == null) {
			if (other.point != null)
				return false;
		} else if (!point.equals(other.point))
			return false;
		return true;
	}
}
