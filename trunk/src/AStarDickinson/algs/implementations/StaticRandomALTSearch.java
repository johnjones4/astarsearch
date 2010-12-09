package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import AStarDickinson.datastructs.graph.MapNode;

public class StaticRandomALTSearch extends StaticALTSearch {

	@Override
	protected Collection<MapNode> precomputeImpl(List<MapNode> graph) {
		Random rand = new Random(new Date().getTime());
		int numLandmarks = rand.nextInt(graph.size());
		Collection<MapNode> landmarks = new HashSet<MapNode>();
		for (int i = 0; i<numLandmarks; i++) {
			MapNode landmark = graph.get(rand.nextInt(graph.size()));
			while(landmarks.contains(landmark)) {
				landmark = graph.get(rand.nextInt(graph.size()));
			}
			landmarks.add(landmark);
		}
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
