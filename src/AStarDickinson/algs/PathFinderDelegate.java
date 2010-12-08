package AStarDickinson.algs;

import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This interface is used to give subclasses of the PathFinder class a way to
 * report progress.
 * 
 * @author johnjones
 * 
 */
public interface PathFinderDelegate {
	/**
	 * Set the root of the tree.
	 * @param node
	 */
	public void setRootNode(TreeNode node);

	/**
	 * Set the final path found from the start to the end node
	 * 
	 * @param finalPath
	 */
	public void setFinalPath(MapPath finalPath);

	/**
	 * Signal method to tell the delegate if one of the data structures has
	 * changed
	 */
	public void pathsWereUpdated();
}
