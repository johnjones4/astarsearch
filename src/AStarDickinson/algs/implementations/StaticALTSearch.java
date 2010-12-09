package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.List;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;

public abstract class StaticALTSearch extends PathFinder {
	private Collection<Landmark> landmarks;

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		
		return null;
	}
	
	public void precompute(List<MapNode> graph) {
		this.landmarks = this.precomputeImpl(graph);
	}

	protected abstract Collection<Landmark> precomputeImpl(List<MapNode> graph);
}
