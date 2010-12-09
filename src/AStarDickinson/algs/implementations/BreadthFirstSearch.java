package AStarDickinson.algs.implementations;

import java.util.LinkedList;
import java.util.Queue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.tree.TreeNode;

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
		return super.buildTree(new CollectionWrapper() {
			Queue<TreeNode> frontier = new LinkedList<TreeNode>();

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
			
		}, delegate, start, end);
	}

	@Override
	public String getName() {
		return "Breadth First Search";
	}
}
