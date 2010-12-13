package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.graph.Landmark;
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
	private TreeNode[] roots;

	@Override
	public void pathsWereUpdated() {
	}

	@Override
	public void setFinalPath(MapPath finalPath) {
		System.out.println("Final path:\n\t\t" + finalPath.toString());
	}

	@Override
	public void setRootNodes(TreeNode[] nodes) {
		this.roots = nodes;
	}

	@Override
	public void setLandmarks(Collection<Landmark> landmarks) {
		System.out.println(landmarks.size() + " landmarks chosen.");
	}
}
