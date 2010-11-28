package AStarDickinson.algs;

import java.util.Collection;
import java.util.TreeSet;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;
import AStarDickinson.gui.ImagePanel;

public abstract class PathFinder implements Comparable<PathFinder> {
	public abstract AlgorithmReport findPath(ImagePanel panel,MapNode start,MapNode end);
	public abstract String toString();
	
	@Override
	public int compareTo(PathFinder o) {
		return this.toString().compareTo(o.toString());
	}
	
	public static Collection<PathFinder> getAvailableAlgorithms() {
		Collection<PathFinder> algs = new TreeSet<PathFinder>();
		algs.add(new BreadthFirstSearch());
		algs.add(new AStarSearch());
		return algs;
	}
}
