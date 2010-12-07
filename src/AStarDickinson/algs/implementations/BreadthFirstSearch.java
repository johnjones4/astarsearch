package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

/**
 * This subclass of PathFinder uses the "Breadth First Search" algorithm to find
 * a path within a graph.
 * 
 * @author johnjones
 * 
 */
public class BreadthFirstSearch extends PathFinder {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		Collection<MapNode> visited = new HashSet<MapNode>();
		Queue<MapPath> frontier = new LinkedList<MapPath>();
		Collection<MapPath> exploredPaths = new LinkedList<MapPath>();

		delegate.setCandidatePathsCollection(exploredPaths);
		delegate.setExploredNodes(visited);

		MapPath path = new MapPath(start, end);
		path.addNode(start);
		frontier.add(path);
		visited.add(start);

		while (frontier.size() > 0) {
			MapPath path1 = frontier.poll();
			exploredPaths.add(path1);
			for (MapNode child : path1.getLastComponent().getEdges()) {
				if (child.equals(end)) {
					MapPath finalPath = path1.cloneWithAddedNode(child);
					delegate.setFinalPath(finalPath);
					return new AlgorithmReport(finalPath, exploredPaths,
							visited);
				} else if (!visited.contains(child)) {
					visited.add(child);
					frontier.add(path1.cloneWithAddedNode(child));
				}
				delegate.pathsWereUpdated();
			}
		}

		return null;
	}

	@Override
	public String getName() {
		return "Breadth First Search";
	}
}
