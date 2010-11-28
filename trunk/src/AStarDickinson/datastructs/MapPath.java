package AStarDickinson.datastructs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
}
