package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

/**
 * This implementation of PathFinderDelegate prints nothing while the algorithm
 * is running and prints a summary of the algorithm's work when a solution is
 * reported.
 * 
 * @author johnjones
 * 
 */
public class ConsolePathFinderDelegate implements PathFinderDelegate {
	private Collection<MapNode> exploredNodes;

	@Override
	public void pathsWereUpdated() {
	}

	@Override
	public void setCandidatePathsCollection(Collection<MapPath> candidatePaths) {
	}

	@Override
	public void setFinalPath(MapPath finalPath) {
		System.out.println("Nodes Explored: " + this.exploredNodes.size());
		System.out.println("Final path:\n\t\t" + finalPath.toString());
	}

	@Override
	public void setExploredNodes(Collection<MapNode> nodes) {
		this.exploredNodes = nodes;
	}

}
