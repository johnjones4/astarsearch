package AStarDickinson.algs.implementations;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.tree.TreeNode;

public class StaticAvoidALTSearch extends StaticALTSearch {
	private static final int LABEL_UNREACHED = 0;
	private static final int LABEL_LABELED = 1;
	private static final int LABEL_SCANNED = 2;

	public StaticAvoidALTSearch(List<MapNode> graph) {
		super(graph);
	}

	@Override
	protected List<Landmark> precomputeImpl(List<MapNode> graph, int nLandmarks) {
		mst(graph,graph.get(0));
		return null;
	}

	@Override
	public String getName() {
		return "ALT Search with Static Avoid-Selected Landmarks";
	}

	/**
	 * Find and Min-Spanning-Tree using Dijkstra's algorithm.
	 * 
	 * @param graph
	 * @return
	 */
	private MSTNode mst(List<MapNode> graph, MapNode root) {
		double[] d = new double[graph.size()];
		MapNode[] p = new MapNode[graph.size()];
		int[] S = new int[graph.size()];
		
		for (int i=0;i<graph.size();i++) {
			d[i] = Double.MAX_VALUE;
			p[i] = null;
			S[i] = LABEL_UNREACHED;
		}
		
		d[graph.indexOf(root)] = 0;
		S[graph.indexOf(root)] = LABEL_LABELED;
		
		while (Arrays.asList(S).contains(LABEL_UNREACHED)) {
			for (int i=0;i<graph.size();i++) {
				if (S[i] != LABEL_LABELED) {
					S[i] = LABEL_SCANNED;
					
					for (MapNode node: graph.get(i).getEdges()) {
						int nodei = graph.indexOf(node);
						if (d[nodei] > d[i] + graph.get(i).getDistanceToNode(node)) {
							d[nodei] = d[i] + graph.get(i).getDistanceToNode(node);
							p[nodei] = graph.get(i);
							S[nodei] = LABEL_LABELED;
						}
					}
				}
			}
		}
		
		HashMap<MapNode,MSTNode> treeNodes = new HashMap<MapNode,MSTNode>();
		for (MapNode node: graph) {
			treeNodes.put(node, new MSTNode(node));
		}
		
		for (int i=0;i<graph.size();i++) {
			if (p[i] != null) {
				MapNode child = graph.get(i);
				MapNode parent = p[i];
				treeNodes.get(parent).addChild(treeNodes.get(child));
			}
		}
		
		return treeNodes.get(root);
	}
	
	private class MSTNode {
		public MSTNode parent;
		public Collection<MSTNode> children = new LinkedList<MSTNode>();
		public MapNode value;
		
		public MSTNode(MapNode value) {
			this.value = value;
		}
		
		public void addChild(MSTNode node) {
			children.add(node);
			node.parent = this;
		}
	}
}
