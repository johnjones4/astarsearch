package AStarDickinson.algs;

import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This implementation of PathFinderDelegate prints nothing while the algorithm
 * is running and prints a summary of the algorithm's work when a solution is
 * reported.
 * 
 * @author johnjones
 * 
 */
public class ConsolePathFinderDelegate implements PathFinderDelegate {
	private TreeNode root;

	@Override
	public void pathsWereUpdated() {
	}

	@Override
	public void setFinalPath(MapPath finalPath) {
		System.out.println("Nodes Explored: " + this.root.getNumNodes());
		System.out.println("Final path:\n\t\t" + finalPath.toString());
	}

	@Override
	public void setRootNode(TreeNode node) {
		this.root = node;
	}

}
