package AStarDickinson.algs.implementations;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.tree.TreeNode;
import AStarDickinson.exec.RandomGraphRunner;

public abstract class StaticALTSearch extends PathFinder {
	public static final int DEFAULT_NUM_LANDMARKS = 16;
	public static final int DEFAULT_SUBSET_SIZE = 6;	
	
	private List<Landmark> landmarks;
	
	public StaticALTSearch(List<MapNode> graph) {
		this(graph,DEFAULT_NUM_LANDMARKS);
	}
	
	public StaticALTSearch(List<MapNode> graph, int nLandmarks) {
		this.landmarks = this.precomputeImpl(graph,nLandmarks);
	}
	
	public List<Landmark> getLandmarks()
	{
		return this.landmarks;
	}

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, final MapNode start,
			final MapNode end) {
		delegate.setLandmarks(landmarks);
		return super.buildTree(new CollectionWrapper() {
			PriorityQueue<TreeNode> frontier = new PriorityQueue<TreeNode>(20,new StaticALTComparator(end));

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
		private MapNode end;
		

		/**
		 * Initialize the comparator with the end node.
		 * 
		 * @param destination
		 */
		public StaticALTComparator(MapNode end) {
			this.end = end;
		}
		
		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			Double best1 = new Double(0);
			Double best2 = new Double(0);
			int rands[] = RandomGraphRunner.getRandoms(DEFAULT_SUBSET_SIZE,landmarks.size());
			for (int i=0;i<DEFAULT_SUBSET_SIZE;i++) {
				Landmark landmark = landmarks.get(rands[i]);
				double o1d = this.d(o1.getValue(), landmark);
				double o2d = this.d(o2.getValue(), landmark);
				double toT = this.d(end, landmark);
				double thisVal1 = o1d - toT;
				double thisVal2 = o2d - toT;
				if (thisVal1 > best1 && thisVal2 > best2) {
					best1 = thisVal1;
					best2 = thisVal2;
				}
			}
			return best1.compareTo(best2);
		}
		
		private double d(MapNode node, Landmark landmark) {
			return landmark.landmarkDistance(node);
		}
	}
}
