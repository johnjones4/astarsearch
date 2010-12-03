package AStarDickinson.algs;

import java.util.Map;
import java.util.TreeMap;

import AStarDickinson.algs.implementations.AStarSearch;
import AStarDickinson.algs.implementations.BreadthFirstSearch;
import AStarDickinson.algs.implementations.DepthFirstSearch;
import AStarDickinson.datastructs.MapNode;

public abstract class PathFinder implements Comparable<PathFinder> {
	public abstract AlgorithmReport findPath(PathFinderDelegate delegate,MapNode start,MapNode end);
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
