package AStarDickinson.datastructs.graph;

import java.util.Collection;
import java.util.HashMap;

public class Landmark extends MapNode {
	private HashMap<Landmark,Double> distances;
	
	public Landmark(MapNode node) {
		super(node.getName(), node.getPoint().getX(), node.getPoint().getY(), node.isDestination());
		distances = new HashMap<Landmark,Double>();
	}
	
	public void setLandmarks(Collection<Landmark> landmarks) {
		for (Landmark landmark: landmarks) {
			distances.put(landmark, this.getDistanceToNode(landmark));
		}
	}
	
	public double landmarkDistance(Landmark landmark) {
		return this.distances.get(landmark);
	}
}
