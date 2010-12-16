package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This subclass of PathFinder uses the "Depth First Search" method of finding a
 * path within a graph.
 * 
 * @author johnjones
 * 
 */
public class DepthFirstSearch extends PathFinder {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		return super.buildTree(new CollectionWrapper() {
			Stack<TreeNode> frontier = new Stack<TreeNode>();

			@Override
			public TreeNode get() {
				return frontier.pop();
			}

			@Override
			public boolean isEmpty() {
				return frontier.isEmpty();
			}

			@Override
			public void put(TreeNode node) {
				frontier.push(node);
			}
			
			@Override
			public boolean contains(TreeNode node) {
				return frontier.contains(node);
			}
			
			
		}, delegate, start, end);
	}

	@Override
	public String getName() {
		return "Depth First Search";
	}

}
