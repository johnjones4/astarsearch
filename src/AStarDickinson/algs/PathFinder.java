package AStarDickinson.algs;

import java.util.Map;
import java.util.TreeMap;

import AStarDickinson.algs.implementations.AStarSearch;
import AStarDickinson.algs.implementations.BreadthFirstSearch;
import AStarDickinson.algs.implementations.DepthFirstSearch;
import AStarDickinson.datastructs.MapNode;

public abstract class PathFinder implements Comparable<PathFinder> {
	/**
	 * This method should contain the kernel of the path-finding algorithm.
	 * 
	 * @param delegate The PathFinderDelegate to report progress to
	 * @param start The starting node
	 * @param end The ending node
	 * @return Returns an AlgorithmReport object that contains information about the solution found.
	 */
	public abstract AlgorithmReport findPath(PathFinderDelegate delegate,MapNode start,MapNode end);
	
	/**
	 * This method should return the name of the algorithm
	 * 
	 * @return The name of the algorithm
	 */
	public abstract String toString();
	
	@Override
	public int compareTo(PathFinder o) {
		return this.toString().compareTo(o.toString());
	}
	
	public static Map<String,PathFinder> getAvailableAlgorithms() {
		Map<String,PathFinder> algs = new TreeMap<String,PathFinder>();
		
		BreadthFirstSearch bfs = new BreadthFirstSearch();
		algs.put(bfs.toString(),bfs);
		
		AStarSearch astar = new AStarSearch();
		algs.put(astar.toString(), astar);
		
		DepthFirstSearch dfs = new DepthFirstSearch();
		algs.put(dfs.toString(), dfs);
		
		return algs;
	}
}
