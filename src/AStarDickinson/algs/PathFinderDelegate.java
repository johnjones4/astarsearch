package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.graph.Landmark;
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
	public void setRootNodes(TreeNode[] nodes);

	/**
	 * Set the final path found from the start to the end node
	 * 
	 * @param finalPath
	 */
	public void setFinalPath(MapPath finalPath);
	
	/**
	 * Set the landmarks used by the ALT algorithms
	 * @param landmarks A collection of landmarks
	 */
	public void setLandmarks(Collection<Landmark> landmarks);

	/**
	 * Signal method to tell the delegate if one of the data structures has
	 * changed
	 */
	public void pathsWereUpdated();
}
