package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

public class AlgorithmReport {
	private final MapPath finalPath;
	private final Collection<MapPath> consideredPaths;
	private final Collection<MapNode> considertedNodes;
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
}
