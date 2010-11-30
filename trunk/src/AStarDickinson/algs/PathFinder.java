package AStarDickinson.algs;

import java.util.Collection;
import java.util.Queue;
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
	
	protected AlgorithmReport treeSearch(ImagePanel panel,MapNode start, MapNode end, Queue<MapPath> frontier, Collection<MapNode> visited, Collection<MapPath> exploredPaths) {
		panel.setCandidatePaths(exploredPaths);
		panel.repaint();
		
		MapPath path = new MapPath(start,end);
		path.addNode(start);
		frontier.add(path);
		visited.add(start);
		
		while(frontier.size() > 0) {
			MapPath path1 = frontier.poll();
			exploredPaths.add(path1);
			for(MapNode child: path1.getLastComponent().getEdges()) {
				if (child.equals(end)) {
					MapPath finalPath = path1.cloneWithAddedNode(child);
					panel.setPath(finalPath);
					return new AlgorithmReport(finalPath,exploredPaths,visited);
				} else if (!visited.contains(child)) {
					visited.add(child);
					frontier.add(path1.cloneWithAddedNode(child));
				}
				panel.repaint();
			}
		}
		
		return null;
	}
}
