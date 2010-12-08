package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
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
		Queue<TreeNode> frontier = new LinkedList<TreeNode>();
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
		return "Breadth First Search";
	}
}
