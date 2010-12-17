package AStarDickinson.datastructs.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Map path defines a path from some start node to some end node. A path is a
 * list of two or more MapNodes.
 * 
 * @author johnjones
 * 
 */
public class MapPath {
	private LinkedList<MapNode> path;
	private MapNode start;
	private MapNode end;

	/**
	 * Create a new path with start and end points
	 * 
	 * @param start
	 *            The starting point of the path
	 * @param end
	 *            The ending point of the path
	 */
	public MapPath(MapNode start, MapNode end) {
		super();
		this.path = new LinkedList<MapNode>();
		this.start = start;
		this.end = end;
	}

	/**
	 * Get a list of the MapNodes in the path such that the first list item in
	 * the list is the start node.
	 * 
	 * @return A List of MapNodes
	 */
	public List<MapNode> getPath() {
		return path;
	}

	/**
	 * Add a node to end of the MapPath.
	 * 
	 * @param node
	 */
	public void addNode(MapNode node) {
		this.path.add(node);
	}

	/**
	 * @return the start node
	 */
	public MapNode getStart() {
		return start;
	}

	/**
	 * @return the end node
	 */
	public MapNode getEnd() {
		return end;
	}

	/**
	 * Create a deep clone of the path.
	 * 
	 * @return A new path that has the same nodes as this path
	 */
	public MapPath clonePath() {
		MapPath path = new MapPath(start, end);
		path.path = new LinkedList<MapNode>(this.path);
		return path;
	}

	/**
	 * Create a deep clone of the path with a new
	 * 
	 * @param node
	 * @return
	 */
	public MapPath cloneWithAddedNode(MapNode node) {
		MapPath path = this.clonePath();
		path.addNode(node);
		return path;
	}

	/**
	 * Get the last component in the path list. This may or may not be equal to
	 * getEnd().
	 * 
	 * @return the last component in the path
	 */
	public MapNode getLastComponent() {
		return path.getLast();
	}

	/**
	 * Get the total distance of this path based on each vertex and edge in the
	 * path.
	 * 
	 * @return The total distance.
	 */
	public double getPathDistance() {
		if (this.path.size() > 1) {
			ListIterator<MapNode> iterator = this.path.listIterator();
			MapNode node = iterator.next();
			double distance = 0;
			while (iterator.hasNext()) {
				MapNode thisNode = iterator.next();
				distance += node.getDistanceToNode(thisNode);
				node = thisNode;
			}
			return distance;
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		for (MapNode node : this.path) {
			if (!first)
				builder.append("; ");
			first = false;
			builder.append(node.toString());
		}
		return builder.toString();
	}
}
