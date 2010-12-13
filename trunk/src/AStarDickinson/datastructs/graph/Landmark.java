package AStarDickinson.datastructs.graph;

import java.util.Collection;
import java.util.HashMap;

public class Landmark extends MapNode {
	private HashMap<MapNode,Double> distances;
	
	public Landmark(MapNode node,Collection<MapNode> graph) {
		super(node.getName(), node.getPoint().getX(), node.getPoint().getY(), node.isDestination());
		distances = new HashMap<MapNode,Double>();
		for(MapNode node1: graph)
			distances.put(node1, node1.getDistanceToNode(this));
	}
	
	public double landmarkDistance(MapNode landmark) {
		return this.distances.get(landmark);
	}
}
