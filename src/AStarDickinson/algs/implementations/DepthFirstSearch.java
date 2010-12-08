package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.LinkedList;
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
		Stack<TreeNode> frontier = new Stack<TreeNode>();
		Collection<TreeNode> exploredTreeNodes = new LinkedList<TreeNode>();

		TreeNode root = new TreeNode(null,start);
		frontier.push(root);
		delegate.setRootNode(root);
		
		while (frontier.size() > 0) {
			TreeNode node = frontier.pop();
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
					frontier.push(childNode);
				}
				delegate.pathsWereUpdated();
			}
		}

		return null;
	}

	@Override
	public String getName() {
		return "Depth First Search";
	}

}
