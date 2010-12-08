package AStarDickinson.algs;

import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

/**
 * This class encapsulates data produced by the execution of a PathFinder
 * algorithm.
 * 
 * @author dickinsonian
 * 
 */
public class AlgorithmReport {
	private final MapPath finalPath;
	private final TreeNode root;

	/**
	 * Construct a new AlgorithmReport using the parameters. These values are
	 * final in the class.
	 * 
	 * @param finalPath
	 *            The path found by the algorithm
	 * @param root
	 *            The tree of explored nodes
	 */
	public AlgorithmReport(MapPath finalPath, TreeNode root) {
		super();
		this.finalPath = finalPath;
		this.root = root;
	}


	/**
	 * @return the finalPath
	 */
	public MapPath getFinalPath() {
		return finalPath;
	}

	/**
	 * @return the root
	 */
	public TreeNode getRoot() {
		return root;
	}

	/**
	 * @return the number of nodes considered
	 */
	public int getNumNodesConsidered() {
		return root.getNumNodes();
	}

	/**
	 * @return the number of nodes in the final path
	 */
	public int getNumSolutionNodes() {
		return finalPath.getPath().size();
	}
}
