package AStarDickinson.algs;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;
import AStarDickinson.gui.ImagePanel;

public class AStarSearch extends PathFinder {

	@Override
	public AlgorithmReport findPath(ImagePanel panel,MapNode start, MapNode end) {
		PriorityQueue<MapPath> frontier = new PriorityQueue<MapPath>(20,new AStarComparator(end));
		Collection<MapNode> visited = new HashSet<MapNode>();
		Collection<MapPath> exploredPaths = new LinkedList<MapPath>();
		return super.treeSearch(panel, start, end, frontier, visited, exploredPaths);
	}

	@Override
	public String toString() {
		return "A* Search";
	}
	
	private class AStarComparator implements Comparator<MapPath>
	{
		private MapNode destination;
		
		public AStarComparator(MapNode destination) {
			this.destination = destination;
		}
		
		@Override
		public int compare(MapPath o1, MapPath o2) {
			Double fOfN1 = this.gOfN(o1) + this.hOfN(o1);
			Double fOfN2 = this.gOfN(o2) + this.hOfN(o2);
			return fOfN1.compareTo(fOfN2);
		}
		
		private double gOfN(MapPath path) {
			return path.getPathDistance();
		}
		
		private double hOfN(MapPath path) {
			return path.getLastComponent().getDistanceToNode(destination);
		}
	}
}
