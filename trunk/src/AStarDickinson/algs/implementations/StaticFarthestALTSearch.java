package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.List;

import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;

public class StaticFarthestALTSearch extends StaticALTSearch {

	@Override
	protected Collection<Landmark> precomputeImpl(List<MapNode> graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "ALT Search with Static Farthest Heuristic Landmarks";
	}

}
