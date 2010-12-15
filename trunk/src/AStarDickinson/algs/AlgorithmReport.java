package AStarDickinson.algs;

import java.util.Collection;
import java.util.HashSet;

import AStarDickinson.datastructs.graph.Landmark;
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
	private final TreeNode[] roots;
	private final String algName;

	/**
	 * Construct a new AlgorithmReport using the parameters. These values are
	 * final in the class.
	 * 
	 * @param finalPath
	 *            The path found by the algorithm
	 * @param root
	 *            The tree of explored nodes
	 */
	public AlgorithmReport(String algName,MapPath finalPath, TreeNode[] roots) {
		super();
		this.algName = algName;
		this.finalPath = finalPath;
		this.roots = roots;
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
	public TreeNode[] getRoots() {
		return roots;
	}
	
	public int getTreesSize() {
		HashSet<TreeNode> nodes = new HashSet<TreeNode>();
		for (TreeNode root: this.roots) {
			nodes.addAll(root.getAllDescendents());
		}
		return nodes.size();
	}

	/**
	 * @return the number of nodes in the final path
	 */
	public int getNumSolutionNodes() {
		return finalPath.getPath().size();
	}


	/**
	 * @return the algName
	 */
	public String getAlgName() {
		return algName;
	}
}
