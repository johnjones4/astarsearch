package AStarDickinson.algs;

import java.util.Collection;
import AStarDickinson.datastructs.MapPath;

public class ConsolePathFinderDelegate implements PathFinderDelegate {
	private Collection<MapPath> candidatePaths;

	@Override
	public void pathsWereUpdated() {
		System.out.println("Paths considered: " + candidatePaths.size());
	}

	@Override
	public void setCandidatePathsCollection(Collection<MapPath> candidatePaths) {
		this.candidatePaths = candidatePaths;
	}

	@Override
	public void setFinalPath(MapPath finalPath) {
		System.out.println("Final path:\n" + finalPath.toString());
	}

}
