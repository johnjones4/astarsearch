package AStarDickinson.algs;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;
import AStarDickinson.gui.ImagePanel;

public class BreadthFirstSearch extends PathFinder {
	
	@Override
	public AlgorithmReport findPath(ImagePanel panel,MapNode start, MapNode end) {
		Collection<MapNode> visited = new HashSet<MapNode>();
		Queue<MapPath> frontier = new LinkedList<MapPath>();
		Collection<MapPath> exploredPaths = new LinkedList<MapPath>();
		return super.treeSearch(panel, start, end, frontier, visited, exploredPaths);
	}

	@Override
	public String toString() {
		return "Breadth First Search";
	}
}
