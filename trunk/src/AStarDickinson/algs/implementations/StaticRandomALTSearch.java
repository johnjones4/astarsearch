package AStarDickinson.algs.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.exec.RandomGraphRunner;

public class StaticRandomALTSearch extends StaticALTSearch {

	public StaticRandomALTSearch(List<MapNode> graph) {
		super(graph);
	}

	@Override
	protected List<Landmark> precomputeImpl(List<MapNode> graph, int nLandmarks) {
		ArrayList<Landmark> landmarks = new ArrayList<Landmark>(nLandmarks);
		int[] rands = RandomGraphRunner.getRandoms(nLandmarks, graph.size());
		for (int i = 0; i < nLandmarks; i++) {
			Landmark landmark = new Landmark(graph.get(rands[i]),graph);
			landmarks.add(landmark);
		}
		return landmarks;
	}

	@Override
	public String getName() {
		return "ALT Search with Static Random Landmarks";
	}

}
