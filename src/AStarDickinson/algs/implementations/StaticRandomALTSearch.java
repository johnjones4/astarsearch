package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;

public class StaticRandomALTSearch extends StaticALTSearch {

	@Override
	protected Collection<Landmark> precomputeImpl(List<MapNode> graph) {
		Random rand = new Random(new Date().getTime());
		int numLandmarks = rand.nextInt(graph.size());
		Collection<Landmark> landmarks = new HashSet<Landmark>();
		for (int i = 0; i < numLandmarks; i++) {
			Landmark landmark = new Landmark(graph.get(rand.nextInt(graph
					.size())));
			while (landmarks.contains(landmark)) {
				landmark = new Landmark(graph.get(rand.nextInt(graph.size())));
			}
			landmarks.add(landmark);
		}
		return null;
	}

	@Override
	public String getName() {
		return "ALT Search with Static Random Landmarks";
	}

}
