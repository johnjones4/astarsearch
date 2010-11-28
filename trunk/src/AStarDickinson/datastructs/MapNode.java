package AStarDickinson.datastructs;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapNode implements Comparable<MapNode> {
	private String name;
	private MapPoint point;
	private Collection<MapNode> edges;
	private Collection<String> edgeNames;
	private boolean destination;

	public MapNode(String name, int x, int y, boolean destination) {
		super();
		this.name = name;
		this.point = new MapPoint(x,y);
		this.edges = new LinkedList<MapNode>();
		this.edgeNames = new LinkedList<String>();
		this.destination = destination;
	}
	
	public void draw(Graphics g, Color color, double reduction) {
		int x = (int)((double)point.getx()*reduction);
		int y = (int)((double)point.gety()*reduction);
		g.setColor(color);
		g.fillOval(x-5, y-5, 10, 10);
	}
	
	public void drawEdge(Graphics g, MapNode edge, Color color, double reduction) {
		if (!edges.contains(edge))
			throw new IllegalArgumentException("That edge is not in this graph.");
		
		int x1 = (int)((double)point.getx()*reduction);
		int y1 = (int)((double)point.gety()*reduction);
		
		int x2 = (int)((double)edge.getPoint().getx()*reduction);
		int y2 = (int)((double)edge.getPoint().gety()*reduction);
		
		g.setColor(color);
		g.drawLine(x1,y1,x2,y2);
	}
	
	public String getName() {
		return name;
	}

	public MapPoint getPoint() {
		return point;
	}

	public Collection<MapNode> getEdges() {
		return edges;
	}

	public boolean isDestination() {
		return destination;
	}
	
	public void addEdgeName(String name) {
		this.edgeNames.add(name);
	}
	
	public Collection<String> getEdgeNames() {
		return this.edgeNames;
	}
	
	public void addEdge(MapNode node) {
		this.edges.add(node);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public double getEdgeDistance(MapNode vertex) {
		if (this.edges.contains(vertex)) {
			int x1 = this.point.getx();
			int y1 = this.point.gety();
			int x2 = vertex.point.getx();
			int y2 = vertex.point.gety();
			return Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
		} else {
			return 0;
		}
	}

	public Element xml(Document doc) {
		Element node = doc.createElement("node");
		
		node.setAttribute("destination", String.valueOf(destination));
		
		Element name = doc.createElement("name");
		name.appendChild(doc.createTextNode(this.name));
		node.appendChild(name);
		
		node.appendChild(point.xml(doc));
		
		Element edges = doc.createElement("edges");
		for(MapNode edgenode: this.edges) {
			Element edgexml = doc.createElement("edge");
			edgexml.appendChild(doc.createTextNode(edgenode.name));
			edges.appendChild(edgexml);
		}
		node.appendChild(edges);
		
		return node;
	}

	@Override
	public int compareTo(MapNode o) {
		return this.name.compareTo(o.name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((point == null) ? 0 : point.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
