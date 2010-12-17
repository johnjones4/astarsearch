package AStarDickinson.algs.implementations;

import java.util.Comparator;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This subclass of PathFinder uses the "A*" algorithm to find a path within a
 * graph.
 * 
 * (implementation from Russell and Norvig)
 * 
 * @author johnjones
 * 
 */
public class AStarSearch extends PathFinder {
	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, final MapNode start,
			final MapNode end) {
		return super.buildTree(new CollectionWrapper() {
			PriorityQueue<TreeNode> frontier = new PriorityQueue<TreeNode>(20,new AStarComparator(start,end));

			@Override
			public TreeNode get() {
				return frontier.poll();
			}

			@Override
			public boolean isEmpty() {
				return frontier.isEmpty();
			}

			@Override
			public void put(TreeNode node) {
				frontier.add(node);
			}

			@Override
			public boolean contains(TreeNode node) {
				return frontier.contains(node);
			}
			
		}, delegate, start, end);
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
	protected class AStarComparator implements Comparator<TreeNode> {
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
			Double fOfN1 = f(o1,end);
			Double fOfN2 = f(o2,end);
			return fOfN1.compareTo(fOfN2);
		}
	}
	
	/**
	 * The node priority function of A* Search.
	 * (implementation from Russell and Norvig)
	 * 
	 * @param node The node to return the value of f()
	 * @param end The target vertex
	 * @return value of f()
	 */
	protected static double f(TreeNode node,MapNode end) {
		return g(node) + h(node,end);
	}
	
	/**
	 * The value of the cost to get to this node.
	 * (implementation from Russell and Norvig)
	 * 
	 * @param node The node to get the cost for
	 * @return the cost
	 */
	protected static double g(TreeNode node) {
		return node.getCost();
	}
	
	/**
	 * The value of the lower-bound to get to the target.
	 * (implementation from Russell and Norvig)
	 * 
	 * @param node The current node
	 * @param end The target
	 * @return a lower bound to reach the target
	 */
	protected static double h(TreeNode node, MapNode end) {
		return node.getValue().getDistanceToNode(end);
	}
}
