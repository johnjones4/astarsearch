package AStarDickinson.algs.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This class implements a Bidirectional Symmetric A* Search as defined in
 * Goldberg and Harrelson.
 * 
 * @author johnjones
 * 
 */
public class BidirectionalSymmetricAStarSearch extends AStarSearch {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		Map<MapNode, TreeNode> startExploredTreeNodes = new HashMap<MapNode, TreeNode>();
		Map<MapNode, TreeNode> endExploredTreeNodes = new HashMap<MapNode, TreeNode>();
		PriorityQueue<TreeNode> startFrontier = new PriorityQueue<TreeNode>(20,
				new AStarComparator(start, end));
		PriorityQueue<TreeNode> endFrontier = new PriorityQueue<TreeNode>(20,
				new AStarComparator(end, start));

		TreeNode root = new TreeNode(null, start);
		startFrontier.add(root);

		TreeNode rootEnd = new TreeNode(null, end);
		endFrontier.add(rootEnd);

		delegate.setRootNodes(new TreeNode[] { root, rootEnd });

		MapPath best = null;
		double bestPathDistance = Double.MAX_VALUE;

		while (!startFrontier.isEmpty() && !endFrontier.isEmpty()) {
			// Check if the only available nodes are way off. If so, quit
			if (f(startFrontier.peek(), end) > bestPathDistance
					|| f(endFrontier.peek(), start) > bestPathDistance)
				return new AlgorithmReport(toString(), best, new TreeNode[] {
						root, rootEnd });

			// Expand from the start node
			TreeNode startNode = startFrontier.poll();
			startExploredTreeNodes.put(startNode.getValue(), startNode);
			for (MapNode child : startNode.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(startNode, child);
				if (startExploredTreeNodes.get(childNode.getValue()) == null
						&& !startFrontier.contains(childNode)) {
					startNode.addChild(childNode);
					startFrontier.add(childNode);
					if (endExploredTreeNodes.get(childNode.getValue()) != null) {
						MapPath path = new MapPath(start, end);
						joinPaths(path, childNode,
								endExploredTreeNodes.get(childNode.getValue()));
						if (path.getPathDistance() < bestPathDistance) {
							best = path;
							bestPathDistance = path.getPathDistance();
							delegate.setFinalPath(path);
						}
					}
					delegate.pathsWereUpdated();
				}
			}

			// Expand from the target node
			TreeNode endNode = endFrontier.poll();
			endExploredTreeNodes.put(endNode.getValue(), endNode);
			for (MapNode child : endNode.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(endNode, child);
				if (endExploredTreeNodes.get(childNode.getValue()) == null) {
					endNode.addChild(childNode);
					endFrontier.add(childNode);
					if (startExploredTreeNodes.get(childNode.getValue()) != null
							&& !endFrontier.contains(childNode)) {
						MapPath path = new MapPath(start, end);
						joinPaths(path, startExploredTreeNodes.get(childNode
								.getValue()), endNode);
						if (path.getPathDistance() < bestPathDistance) {
							best = path;
							bestPathDistance = path.getPathDistance();
							delegate.setFinalPath(path);
						}
					}
					delegate.pathsWereUpdated();
				}
			}
		}
		return new AlgorithmReport(toString(), best, new TreeNode[] { root,
				rootEnd });
	}

	/**
	 * This method is used to join a path when the bidirectional search meets
	 * the same node. Note that startNode and endNode should have the same
	 * value.
	 * 
	 * @param path
	 *            A path with start and target vertices defined.
	 * @param startNode
	 *            The node reached from the start vertex
	 * @param endNode
	 *            The node reached from the end vertex
	 */
	protected static void joinPaths(MapPath path, TreeNode startNode,
			TreeNode endNode) {
		startNode.getParent().assemblePath(path);
		endNode.assembleInversePath(path);
	}

	@Override
	public String getName() {
		return "Bidirectional Symmetric A Star Search";
	}
}
