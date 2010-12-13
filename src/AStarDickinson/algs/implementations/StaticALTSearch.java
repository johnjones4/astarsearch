package AStarDickinson.algs.implementations;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.tree.TreeNode;
import AStarDickinson.exec.RandomGraphRunner;

public abstract class StaticALTSearch extends PathFinder {
	public static final int DEFAULT_NUM_LANDMARKS = 16;
	public static final int DEFAULT_SUBSET_SIZE = 4;	
	
	private List<Landmark> landmarks;
	
	public StaticALTSearch(List<MapNode> graph) {
		this(graph,DEFAULT_NUM_LANDMARKS);
	}
	
	public StaticALTSearch(List<MapNode> graph, int nLandmarks) {
		this.landmarks = this.precomputeImpl(graph,nLandmarks);
	}

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, final MapNode start,
			final MapNode end) {
		
		int rands[] = RandomGraphRunner.getRandoms(DEFAULT_SUBSET_SIZE,landmarks.size());
		final Landmark[] subset = new Landmark[DEFAULT_SUBSET_SIZE];
		for (int i=0;i<DEFAULT_SUBSET_SIZE;i++) {
			subset[i] = landmarks.get(rands[i]);
		}
		
		delegate.setLandmarks(landmarks);
		
		return super.buildTree(new CollectionWrapper() {
			PriorityQueue<TreeNode> frontier = new PriorityQueue<TreeNode>(20,new StaticALTComparator(start,end,subset));

			@Override
			public TreeNode get() {
				return frontier.poll();
			}

			@Override
			public boolean isEmpty() {
				return frontier.isEmpty();
			}

			@Override
			public void put(TreeNode node) {
				frontier.add(node);
			}
			
		}, delegate, start, end);
	}
	
	protected abstract List<Landmark> precomputeImpl(List<MapNode> graph, int nLandmarks);
	
	private class StaticALTComparator implements Comparator<TreeNode> {
		private MapNode start;
		private MapNode end;
		private Landmark[] subset;

		/**
		 * Initialize the comparator with the end node.
		 * 
		 * @param destination
		 */
		public StaticALTComparator(MapNode start, MapNode end,Landmark[] subset) {
			this.start = start;
			this.end = end;
			this.subset = subset;
		}
		
		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			double best = 0;
			for (Landmark landmark: subset) {
				double o1d = this.d(o1.getValue(), landmark);
				double o2d = this.d(o2.getValue(), landmark);
				double val = Math.abs(o1d-o2d) + Math.abs(o2d-o1d);
				if (val > best)
					best = val;
			}
			return (int)(best*10);
		}
		
		private double d(MapNode node, Landmark landmark) {
			return landmark.landmarkDistance(node);
		}
	}
}
