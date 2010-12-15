package AStarDickinson.algs.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;

public class StaticFarthestALTSearch extends StaticALTSearch {

	public StaticFarthestALTSearch(List<MapNode> graph) {
		super(graph);
	}

	@Override
	protected List<Landmark> precomputeImpl(List<MapNode> graph, int nLandmarks) {
		List<Landmark> landmarks = new ArrayList<Landmark>(nLandmarks);
		if (graph.size() > 0) {
			Set<MapNode> selected = new HashSet<MapNode>();
			Landmark vertex = new Landmark(graph.get(0),graph);
			landmarks.add(vertex);
			selected.add(graph.get(0));
			for (int i=1;i<nLandmarks;i++) {
				vertex = getFarthest(landmarks,graph,selected);
				landmarks.add(vertex);
			}
		}
		return landmarks;
	}
	
	private static Landmark getFarthest(List<Landmark> landmarks,List<MapNode> graph,Set<MapNode> selected)
	{
		MapNode selection = null;
		double dists[] = new double[landmarks.size()];
		for (MapNode node: graph) {
			if (!selected.contains(node)) {
				int score = 0;
				double newdists[] = new double[landmarks.size()];
				for (int i=0;i<landmarks.size();i++) {
					newdists[i] = landmarks.get(i).landmarkDistance(node);
					if (newdists[i] > dists[i])
						score++;
				}
				if (score == landmarks.size()) {
					dists = newdists;
					selection = node;
				}
			}
		}
		selected.add(selection);
		return new Landmark(selection,graph);
	}

	@Override
	public String getName() {
		return "ALT Search with Static Farthest Heuristic Landmarks";
	}

}
