package AStarDickinson.algs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import AStarDickinson.algs.implementations.AStarSearch;
import AStarDickinson.algs.implementations.BreadthFirstSearch;
import AStarDickinson.algs.implementations.DepthFirstSearch;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

public abstract class PathFinder implements Comparable<PathFinder> {
	/**
	 * This method should contain the kernel of the path-finding algorithm.
	 * 
	 * @param delegate
	 *            The PathFinderDelegate to report progress to
	 * @param start
	 *            The starting node
	 * @param end
	 *            The ending node
	 * @return Returns an AlgorithmReport object that contains information about
	 *         the solution found.
	 */
	protected abstract AlgorithmReport findPath(PathFinderDelegate delegate,
			MapNode start, MapNode end);
	
	public run(PathFinderDelegate delegate,
			MapNode start, MapNode end)

	/**
	 * This method should return the name of the algorithm
	 * 
	 * @return The name of the algorithm
	 */
	public abstract String getName();

	/**
	 * Compare based on the algorithm name
	 */
	@Override
	public int compareTo(PathFinder o) {
		return this.toString().compareTo(o.toString());
	}

	/**
	 * Call the getName() method
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	protected AlgorithmReport buildTree(CollectionWrapper frontier, PathFinderDelegate delegate, MapNode start, MapNode end) {
		Collection<TreeNode> exploredTreeNodes = new LinkedList<TreeNode>();
		
		TreeNode root = new TreeNode(null,start);
		frontier.put(root);
		delegate.setRootNode(root);

		while (!frontier.isEmpty()) {
			TreeNode node = frontier.get();
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
					frontier.put(childNode);
				}
				delegate.pathsWereUpdated();
			}
		}

		return null;
	}

	/**
	 * This static method returns a Map of name->object of available subclasses
	 * of PathFinder
	 * 
	 * @return Map of name->PathFinder
	 */
	public static Map<String, PathFinder> getAvailableAlgorithms() {
		Map<String, PathFinder> algs = new TreeMap<String, PathFinder>();

		BreadthFirstSearch bfs = new BreadthFirstSearch();
		algs.put(bfs.toString(), bfs);

		AStarSearch astar = new AStarSearch();
		algs.put(astar.toString(), astar);

		DepthFirstSearch dfs = new DepthFirstSearch();
		algs.put(dfs.toString(), dfs);

		return algs;
	}
	
	protected interface CollectionWrapper {
		public void put(TreeNode node);
		public TreeNode get();
		public boolean isEmpty();
	}
}
