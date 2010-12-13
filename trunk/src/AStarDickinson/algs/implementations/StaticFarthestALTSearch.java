package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.List;

import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;

public class StaticFarthestALTSearch extends StaticALTSearch {

	public StaticFarthestALTSearch(List<MapNode> graph) {
		super(graph);
	}

	@Override
	protected List<Landmark> precomputeImpl(List<MapNode> graph, int nLandmarks) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "ALT Search with Static Farthest Heuristic Landmarks";
	}

}
