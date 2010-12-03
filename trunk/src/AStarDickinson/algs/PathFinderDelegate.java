package AStarDickinson.algs;

import java.util.Collection;

import AStarDickinson.datastructs.MapNode;
import AStarDickinson.datastructs.MapPath;

public interface PathFinderDelegate {
	public void setCandidatePathsCollection(Collection<MapPath> candidatePaths);
	public void setExploredNodes(Collection<MapNode> nodes);
	public void setFinalPath(MapPath finalPath);
	public void pathsWereUpdated();
}
