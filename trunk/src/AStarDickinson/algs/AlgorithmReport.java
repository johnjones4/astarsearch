package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

/**
 * This class encapsulates data produced by the execution of a PathFinder
 * algorithm.
 * 
 * @author dickinsonian
 * 
 */
public class AlgorithmReport {
	private final MapPath finalPath;
	private final Collection<MapPath> consideredPaths;
	private final Collection<MapNode> considertedNodes;

	/**
	 * Construct a new AlgorithmReport using the parameters. These values are
	 * final in the class.
	 * 
	 * @param finalPath
	 *            The path found by the algorithm
	 * @param consideredPaths
	 *            The paths considered as possible solutions
	 * @param considertedNodes
	 *            The nodes considered
	 */
	public AlgorithmReport(MapPath finalPath,
			Collection<MapPath> consideredPaths,
			Collection<MapNode> considertedNodes) {
		super();
		this.finalPath = finalPath;
		this.consideredPaths = consideredPaths;
		this.considertedNodes = considertedNodes;
	}

	/**
	 * @return the finalPath
	 */
	public MapPath getFinalPath() {
		return finalPath;
	}

	/**
	 * @return the consideredPaths
	 */
	public Collection<MapPath> getConsideredPaths() {
		return consideredPaths;
	}

	/**
	 * @return the considertedNodes
	 */
	public Collection<MapNode> getConsidertedNodes() {
		return considertedNodes;
	}

	/**
	 * @return the number of nodes considered
	 */
	public int getNumNodesConsidered() {
		return considertedNodes.size();
	}

	/**
	 * @return the number of nodes in the final path
	 */
	public int getNumSolutionNodes() {
		return finalPath.getPath().size();
	}
}
