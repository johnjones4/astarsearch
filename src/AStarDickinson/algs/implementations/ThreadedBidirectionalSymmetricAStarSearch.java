package AStarDickinson.algs.implementations;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.algs.implementations.AStarSearch.AStarComparator;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

public class ThreadedBidirectionalSymmetricAStarSearch extends BidirectionalSymmetricAStarSearch {

	@Override
	public AlgorithmReport findPath(PathFinderDelegate delegate, MapNode start,
			MapNode end) {
		Map<MapNode,TreeNode> startExploredTreeNodes = new Hashtable<MapNode,TreeNode>();
		Map<MapNode,TreeNode> endExploredTreeNodes = new Hashtable<MapNode,TreeNode>();
		PriorityBlockingQueue<TreeNode> startFrontier = new PriorityBlockingQueue<TreeNode>(20,new AStarComparator(start,end));
		PriorityBlockingQueue<TreeNode> endFrontier = new PriorityBlockingQueue<TreeNode>(20,new AStarComparator(end,start));
		
		TreeNode root = new TreeNode(null,start);
		startFrontier.add(root);
		
		TreeNode rootEnd = new TreeNode(null,end);
		endFrontier.add(rootEnd);
		
		delegate.setRootNodes(new TreeNode[]{root,rootEnd});
		
		Explorer startExplorer = new Explorer(delegate,start,end,startExploredTreeNodes,startFrontier);
		Explorer endExplorer = new Explorer(delegate,end,start,endExploredTreeNodes,endFrontier);
		Intermediary intermediary = new Intermediary(startExplorer,endExplorer);
		startExplorer.setObjects(intermediary, endExplorer);
		endExplorer.setObjects(intermediary, startExplorer);
		
		new Thread(startExplorer).start();
		new Thread(endExplorer).start();
		
		int i=0;
		System.out.println("searching ...");
		while(!intermediary.doesSolutionExist()) {
			i++;
			if (i > 10000) {
				System.out.println("searching ...");
				i=0;
			}
		}
		
		return new AlgorithmReport(this.toString(),intermediary.getBest(),new TreeNode[]{root,rootEnd});
	}

	@Override
	public String getName() {
		return "Threaded Bidirectional Symmetric A Star Search";
	}
	
	private class Intermediary {
		private MapPath best;
		private double bestPathDistance = Double.MAX_VALUE;
		private Explorer[] explorers;
		private Semaphore mutex = new Semaphore(1);
		private boolean solutionExists = false;
		
		public Intermediary(Explorer ... explorers) {
			this.explorers = explorers;
		}
		
		/**
		 * @param best the best to set
		 */
		public void setBest(MapPath best,double bestPathDistance) {
			try {
				mutex.acquire();
				this.best = best;
				this.bestPathDistance = bestPathDistance;
				mutex.release();
			} catch (InterruptedException e) {
			}
		}
		
		/**
		 * @return the best
		 */
		public MapPath getBest() {
			return best;
		}

		/**
		 * @return the bestPathDistance
		 */
		public double getBestPathDistance() {
			try {
				mutex.acquire();
				double bestPathDistance = this.bestPathDistance;
				mutex.release();
				return bestPathDistance;
			} catch (InterruptedException e) {
				return 0;
			}
		}
		
		public void killThreads() {
			for(Explorer explorer: explorers)
				explorer.kill();
			solutionExists = true;
		}
		
		public boolean doesSolutionExist() {
			return this.solutionExists;
		}
	}

	private class Explorer implements Runnable {
		private Intermediary intermediary;
		private PathFinderDelegate delegate;
		private MapNode start;
		private MapNode end;
		private Map<MapNode,TreeNode> exploredTreeNodes;
		private PriorityBlockingQueue<TreeNode> frontier;
		private Explorer other;
		private boolean stop;
		
		public Explorer(PathFinderDelegate delegate,
				MapNode start, MapNode end,
				Map<MapNode, TreeNode> exploredTreeNodes,
				PriorityBlockingQueue<TreeNode> startFrontier) {
			super();
			this.delegate = delegate;
			this.start = start;
			this.end = end;
			this.exploredTreeNodes = exploredTreeNodes;
			this.frontier = startFrontier;
			this.stop = false;
		}

		/**
		 * @param other the other to set
		 */
		public void setObjects(Intermediary intermediary,Explorer other) {
			this.intermediary = intermediary; 
			this.other = other;
		}
		
		public void kill() {
			this.stop = true;
		}

		@Override
		public void run() {
			whileloop: while (!stop && !frontier.isEmpty()) {
				// Check if the only available nodes are way off
				if (f(frontier.peek(),start,end) > intermediary.getBestPathDistance() || f(other.frontier.peek(),end,start) > intermediary.getBestPathDistance()) {
					intermediary.killThreads();
					break whileloop;
				}
				
				TreeNode startNode = frontier.poll();
				exploredTreeNodes.put(startNode.getValue(),startNode);
				for (MapNode child : startNode.getValue().getEdges()) {
					TreeNode childNode = new TreeNode(startNode,child);
					if (other.exploredTreeNodes.get(childNode.getValue()) != null) {
						startNode.addChild(childNode);
						MapPath path = new MapPath(start, end);
						joinPaths(path, childNode, other.exploredTreeNodes.get(childNode.getValue()));
						if (path.getPathDistance() < intermediary.getBestPathDistance()) {
							intermediary.setBest(path,path.getPathDistance());
							delegate.setFinalPath(path);
						}
					} else if (exploredTreeNodes.get(childNode.getValue()) == null && other.exploredTreeNodes.get(childNode.getValue()) == null) {
						startNode.addChild(childNode);
						exploredTreeNodes.put(childNode.getValue(),childNode);
						frontier.add(childNode);
					}
				}
			}
			intermediary.killThreads();
		}
		
	}
}
