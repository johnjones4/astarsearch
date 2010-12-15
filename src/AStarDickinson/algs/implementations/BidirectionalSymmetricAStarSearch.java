package AStarDickinson.algs.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

public class BidirectionalSymmetricAStarSearch extends AStarSearch {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		Map<MapNode,TreeNode> startExploredTreeNodes = new HashMap<MapNode,TreeNode>();
		Map<MapNode,TreeNode> endExploredTreeNodes = new HashMap<MapNode,TreeNode>();
		PriorityQueue<TreeNode> startFrontier = new PriorityQueue<TreeNode>(20,new AStarComparator(start,end));
		PriorityQueue<TreeNode> endFrontier = new PriorityQueue<TreeNode>(20,new AStarComparator(end,start));
		
		TreeNode root = new TreeNode(null,start);
		startFrontier.add(root);
		
		TreeNode rootEnd = new TreeNode(null,end);
		endFrontier.add(rootEnd);
		
		delegate.setRootNodes(new TreeNode[]{root,rootEnd});
		
		MapPath best = null;
		double bestPathDistance = Double.MAX_VALUE;

		while (!startFrontier.isEmpty() && !endFrontier.isEmpty()) {
			// Check if the only available nodes are way off
			if (f(startFrontier.peek(),start,end) > bestPathDistance || f(endFrontier.peek(),end,start) > bestPathDistance)
				return new AlgorithmReport(toString(),best,new TreeNode[]{root,rootEnd});
			
			// Start step
			TreeNode startNode = startFrontier.poll();
			startExploredTreeNodes.put(startNode.getValue(),startNode);
			for (MapNode child : startNode.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(startNode,child);
				if (startExploredTreeNodes.get(childNode.getValue()) == null) {
					startNode.addChild(childNode);
					startFrontier.add(childNode);
					if (endExploredTreeNodes.get(childNode.getValue()) != null) {
						MapPath path = new MapPath(start, end);
						joinPaths(path, childNode, endExploredTreeNodes.get(childNode.getValue()));
						if (path.getPathDistance() < bestPathDistance) {
							best = path;
							bestPathDistance = path.getPathDistance();
							delegate.setFinalPath(path);
						}
					}
				}
			}
			
			// End Step
			TreeNode endNode = endFrontier.poll();
			endExploredTreeNodes.put(endNode.getValue(),endNode);
			for (MapNode child : endNode.getValue().getEdges()) {
				TreeNode childNode = new TreeNode(endNode,child);
				if (endExploredTreeNodes.get(childNode.getValue()) == null) {
					endNode.addChild(childNode);
					endFrontier.add(childNode);
					if (startExploredTreeNodes.get(childNode.getValue()) != null) {
						MapPath path = new MapPath(start, end);
						joinPaths(path, startExploredTreeNodes.get(childNode.getValue()), endNode);
						if (path.getPathDistance() < bestPathDistance) {
							best = path;
							bestPathDistance = path.getPathDistance();
							delegate.setFinalPath(path);
						}
					} 
				}
			}
			
			
			delegate.pathsWereUpdated();
		}
		return new AlgorithmReport(toString(),best,new TreeNode[]{root,rootEnd});
	}
	
	protected static void joinPaths(MapPath path,TreeNode startNode,TreeNode endNode) {
		startNode.assemblePath(path);
		endNode.assembleInversePath(path);
	}

	@Override
	public String getName() {
		return "Bidirectional Symmetric A Star Search";
	}
}
