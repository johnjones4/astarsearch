package AStarDickinson.algs;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import AStarDickinson.algs.implementations.AStarSearch;
import AStarDickinson.algs.implementations.BidirectionalSymmetricAStarSearch;
import AStarDickinson.algs.implementations.StaticFarthestALTSearch;
import AStarDickinson.algs.implementations.StaticRandomALTSearch;
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
	public abstract AlgorithmReport findPath(PathFinderDelegate delegate,
			MapNode start, MapNode end);

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

	/**
	 * This generic tree searching algorithm is used by subclasses of
	 * PathFinder. It uses a wrapper class for the frontier so subclasses can
	 * implement specific behavior.
	 * 
	 * (implementation from Russell and Norvig)
	 * 
	 * @param frontier
	 *            Nodes to explore
	 * @param delegate
	 *            Delegate to inform when actions take place
	 * @param start
	 *            The start vertex
	 * @param end
	 *            The end vertex
	 * @return A path solution
	 */
	protected AlgorithmReport buildTree(CollectionWrapper frontier,
			PathFinderDelegate delegate, MapNode start, MapNode end) {
		Collection<TreeNode> exploredTreeNodes = new HashSet<TreeNode>();

		TreeNode root = new TreeNode(null, start);
		frontier.put(root);
		delegate.setRootNodes(new TreeNode[] { root });

		while (!frontier.isEmpty()) {
			TreeNode node = frontier.get();
			exploredTreeNodes.add(node);
			for (MapNode child : node.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(node, child);
				if (!exploredTreeNodes.contains(childNode)
						&& !frontier.contains(childNode)) {
					node.addChild(childNode);
					frontier.put(childNode);
					if (child.equals(end)) {
						MapPath finalPath = new MapPath(start, end);
						childNode.assemblePath(finalPath);
						delegate.setFinalPath(finalPath);
						return new AlgorithmReport(toString(), finalPath,
								new TreeNode[] { root });
					}
					delegate.pathsWereUpdated();
				}
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
	public static Map<String, PathFinder> getAvailableAlgorithms(
			List<MapNode> graph) {
		Map<String, PathFinder> algs = new TreeMap<String, PathFinder>();

		AStarSearch astar = new AStarSearch();
		algs.put(astar.toString(), astar);

		StaticRandomALTSearch staticAltRand = new StaticRandomALTSearch(graph);
		algs.put(staticAltRand.toString(), staticAltRand);

		StaticFarthestALTSearch staticAltFar = new StaticFarthestALTSearch(
				graph);
		algs.put(staticAltFar.toString(), staticAltFar);

		BidirectionalSymmetricAStarSearch biSymA = new BidirectionalSymmetricAStarSearch();
		algs.put(biSymA.toString(), biSymA);

		return algs;
	}

	/**
	 * This wrapper interface is used by buildTree's frontier object. Subclasses
	 * may implement this interface to provide specific frontier behaviors.
	 * 
	 * @author johnjones
	 * 
	 */
	protected interface CollectionWrapper {
		public void put(TreeNode node);

		public TreeNode get();

		public boolean isEmpty();

		public boolean contains(TreeNode node);
	}
}
