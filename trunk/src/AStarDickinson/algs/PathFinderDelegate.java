package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

/**
 * This interface is used to give subclasses of the PathFinder class a way to report progress.
 * 
 * @author johnjones
 *
 */
public interface PathFinderDelegate {
	/**
	 * Send a reference to the collection storing all of the candidate pats.  A candidate path is a path from the starting node to any other node that was conisedered as a possible solution path.
	 * @param candidatePaths The candidate paths
	 */
	public void setCandidatePathsCollection(Collection<MapPath> candidatePaths);
	
	/**
	 * Send a reference to the collection storing all of the explored nodes.  An explored node is a node that the algorithm conisidered as part of the solution.
	 * @param nodes The explored nodes
	 */
	public void setExploredNodes(Collection<MapNode> nodes);
	
	/**
	 * Set the final path found from the start to the end node
	 * @param finalPath
	 */
	public void setFinalPath(MapPath finalPath);
	
	/**
	 * Signal method to tell the delegate if one of the data structures has changed
	 */
	public void pathsWereUpdated();
}
