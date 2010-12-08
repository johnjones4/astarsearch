package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

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
		PriorityQueue<TreeNode> frontier = new PriorityQueue<TreeNode>(20,new AStarComparator(start,end));
		Collection<TreeNode> exploredTreeNodes = new LinkedList<TreeNode>();
		
		TreeNode root = new TreeNode(null,start);
		frontier.add(root);
		delegate.setRootNode(root);

		while (frontier.size() > 0) {
			TreeNode node = frontier.poll();
			exploredTreeNodes.add(node);
			for (MapNode child : node.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(node,child);
				if (child.equals(end)) {
					node.addChild(childNode);
					MapPath finalPath = new MapPath(start, end);
					childNode.assembleInversePath(finalPath);
					delegate.setFinalPath(finalPath);
					return new AlgorithmReport(finalPath, root);
				} else if (!exploredTreeNodes.contains(childNode)) {
					node.addChild(childNode);
					exploredTreeNodes.add(childNode);
					frontier.add(childNode);
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
	private class AStarComparator implements Comparator<TreeNode> {
		private MapNode start;
		private MapNode end;

		/**
		 * Initialize the comparator with the end node.
		 * 
		 * @param destination
		 */
		public AStarComparator(MapNode start, MapNode end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			Double fOfN1 = this.gOfN(o1) + this.hOfN(o1);
			Double fOfN2 = this.gOfN(o2) + this.hOfN(o2);
			return fOfN1.compareTo(fOfN2);
		}

		/**
		 * g(n) is the total distance traversed.
		 * @param path
		 * @return
		 */
		private double gOfN(TreeNode node) {
			MapPath path = new MapPath(start,end);
			node.assembleInversePath(path);
			return path.getPathDistance();
		}

		/**
		 * h(n) is the straight-distance from our current location to the end node.
		 * @param path
		 * @return
		 */
		private double hOfN(TreeNode node) {
			return node.getValue().getDistanceToNode(end);
		}
	}
}
