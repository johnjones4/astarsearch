package AStarDickinson.datastructs;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MapPath {
	private LinkedList<MapNode> path;
	private MapNode start;
	private MapNode end;

	public MapPath(MapNode start,MapNode end) {
		super();
		this.path = new LinkedList<MapNode>();
		this.start = start;
		this.end = end;
	}

	public List<MapNode> getPath() {
		return path;
	}
	
	public void addNode(MapNode node) {
		this.path.add(node);
	}

	/**
	 * @return the start
	 */
	public MapNode getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public MapNode getEnd() {
		return end;
	}
	
	public MapPath clonePath() {
		MapPath path = new MapPath(start,end);
		path.path = new LinkedList<MapNode>(this.path);
		return path;
	}
	
	public MapPath cloneWithAddedNode(MapNode node) {
		MapPath path = this.clonePath();
		path.addNode(node);
		return path;
	}
	
	public MapNode getLastComponent() {
		return path.getLast();
	}
	
	public double getPathDistance() {
		if (this.path.size() > 1) {
			ListIterator<MapNode> iterator = this.path.listIterator();
			MapNode node = iterator.next();
			double distance = 0;
			while(iterator.hasNext()) {
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
		for (MapNode node: this.path) {
			if (!first)
				builder.append(", ");
			first = false;
			builder.append(node.toString());
		}
		return builder.toString();
	}
}
