package AStarDickinson.datastructs.graph;

import java.util.Collection;
import java.util.HashMap;

/**
 * Landmark is a sub-type of MapNode. The difference is that this contains a map
 * of distances to all nodes in the graph to which this vertex belongs.
 * 
 * @author johnjones
 * 
 */
public class Landmark extends MapNode {
	private HashMap<MapNode, Double> distances;

	/**
	 * Create a landmark based upon some MapNode and graph.
	 * 
	 * @param node
	 *            A node to base this landmark upon.
	 * @param graph
	 *            The graph to which the landmark will belong.
	 */
	public Landmark(MapNode node, Collection<MapNode> graph) {
		super(node.getName(), node.getPoint().getX(), node.getPoint().getY(),
				node.isDestination());
		distances = new HashMap<MapNode, Double>();
		for (MapNode node1 : graph)
			distances.put(node1, node1.getDistanceToNode(this));
	}

	/**
	 * The landmark distance is the Euclidean distance to some other vertex.
	 * 
	 * @param landmark
	 * @return
	 */
	public double landmarkDistance(MapNode landmark) {
		return this.distances.get(landmark);
	}
}
