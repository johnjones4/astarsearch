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

/**
 * This abstract class sets up a framework to use the ALT algorithms defined in
 * Goldberg and Harrelson. What needs implementation in subclasses is a landmark
 * selection algorithm.
 * 
 * @author johnjones
 * 
 */
public abstract class StaticALTSearch extends PathFinder {
	public static final int DEFAULT_NUM_LANDMARKS = 16;
	public static final int DEFAULT_SUBSET_SIZE = 6;

	private List<Landmark> landmarks;

	public StaticALTSearch(List<MapNode> graph) {
		this(graph, DEFAULT_NUM_LANDMARKS);
	}

	public StaticALTSearch(List<MapNode> graph, int nLandmarks) {
		this.landmarks = this.precomputeImpl(graph, nLandmarks);
	}

	/**
	 * Get the collection of landmarks
	 * 
	 * @return A collection of landmarks
	 */
	public List<Landmark> getLandmarks() {
		return this.landmarks;
	}

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate,
			final MapNode start, final MapNode end) {
		delegate.setLandmarks(landmarks);
		return super.buildTree(new CollectionWrapper() {
			StaticALTComparator comp = new StaticALTComparator(end);
			PriorityQueue<TreeNode> frontier = new PriorityQueue<TreeNode>(20,
					comp);

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
				// Change the subset of landmarks to check for a lower-bound
				comp.rand();
				frontier.add(node);
			}

			@Override
			public boolean contains(TreeNode node) {
				return frontier.contains(node);
			}

		}, delegate, start, end);
	}

	/**
	 * Precompute the landmarks.
	 * 
	 * @param graph
	 *            The graph to draw-on for landmarks
	 * @param nLandmarks
	 *            The number of landmarks desired.
	 * @return
	 */
	protected abstract List<Landmark> precomputeImpl(List<MapNode> graph,
			int nLandmarks);

	/**
	 * This comparator analyzes some subset of the landmarks to compare two
	 * nodes.
	 * 
	 * @author johnjones
	 * 
	 */
	private class StaticALTComparator implements Comparator<TreeNode> {
		private MapNode end;
		int rands[];

		/**
		 * Initialize the comparator with the end node.
		 * 
		 * @param destination
		 */
		public StaticALTComparator(MapNode end) {
			this.end = end;
			rand();
		}

		/**
		 * Select a new random subset of landmarks
		 */
		public void rand() {
			rands = RandomGraphRunner.getRandoms(DEFAULT_SUBSET_SIZE,
					landmarks.size());
		}

		@Override
		public int compare(TreeNode o1, TreeNode o2) {
			double best1 = 0;
			double best2 = 0;

			for (int i = 0; i < DEFAULT_SUBSET_SIZE; i++) {
				Landmark landmark = landmarks.get(rands[i]);
				double o1d = this.d(o1.getValue(), landmark);
				double o2d = this.d(o2.getValue(), landmark);
				double toT = this.d(end, landmark);
				double thisVal1 = Math.abs(toT - o1d);
				double thisVal2 = Math.abs(toT - o2d);
				best1 += thisVal1;
				best2 += thisVal2;
			}

			if (best1 > best2)
				return 1;
			else if (best2 > best1)
				return -1;
			else
				return 0;
		}

		/**
		 * d() is the landmark function as defined in Goldberg and Harrelson
		 * that produces a lower bounds on the shortest-path problem.
		 * 
		 * @param node The node to get d() for
		 * @param landmark The landmark to base the d() result on
		 * @return The distance from landmark to node
		 */
		private double d(MapNode node, Landmark landmark) {
			return landmark.landmarkDistance(node);
		}
	}
}
