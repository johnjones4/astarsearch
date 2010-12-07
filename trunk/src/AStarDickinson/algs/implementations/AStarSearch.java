package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

/**
 * This subclass of PathFinder uses the "A*" algorithm to find a path within a
 * graph.
 * 
 * @author johnjones
 * 
 */
public class AStarSearch extends PathFinder {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		PriorityQueue<MapPath> frontier = new PriorityQueue<MapPath>(20,
				new AStarComparator(end));
		Collection<MapNode> visited = new HashSet<MapNode>();
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
		return "A* Search";
	}

	/**
	 * This private implementation of a Comparator is used by the algorithm's
	 * priority queue to order MapPaths so that least g(n) + h(n) appears first.
	 * 
	 * @author johnjones
	 * 
	 */
	private class AStarComparator implements Comparator<MapPath> {
		private MapNode destination;

		/**
		 * Initialize the comparator with the end node.
		 * 
		 * @param destination
		 */
		public AStarComparator(MapNode destination) {
			this.destination = destination;
		}

		@Override
		public int compare(MapPath o1, MapPath o2) {
			Double fOfN1 = this.gOfN(o1) + this.hOfN(o1);
			Double fOfN2 = this.gOfN(o2) + this.hOfN(o2);
			return fOfN1.compareTo(fOfN2);
		}

		/**
		 * g(n) is the total distance traversed.
		 * @param path
		 * @return
		 */
		private double gOfN(MapPath path) {
			return path.getPathDistance();
		}

		/**
		 * h(n) is the straight-distance from our current location to the end node.
		 * @param path
		 * @return
		 */
		private double hOfN(MapPath path) {
			return path.getLastComponent().getDistanceToNode(destination);
		}
	}
}
