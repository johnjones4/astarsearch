package AStarDickinson.datastructs.graph;

import java.util.Collection;
import java.util.HashMap;

public class Landmark extends MapNode {
	private HashMap<Landmark,Double> distances;
	
	public Landmark(String name, int x, int y, boolean destination) {
		super(name, x, y, destination);
		distances = new HashMap<Landmark,Double>();
	}
	
	public void setLandmarks(Collection<Landmark> landmarks) {
		for (Landmark landmark: landmarks) {
			
		}
	}
}
