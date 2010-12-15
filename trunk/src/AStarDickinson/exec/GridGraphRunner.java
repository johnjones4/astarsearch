package AStarDickinson.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.datastructs.graph.MapNode;

public class GridGraphRunner extends GraphExperimentRunner {

	public static final int EDGE_LENGTH = 20;
	public static final int MAX_X = 1000;
	public static final int MAX_Y = 1000;
	public static final int SPACING_X = MAX_X / EDGE_LENGTH;
	public static final int SPACING_Y = MAX_Y / EDGE_LENGTH;
	public static final String RENDER_FILE = "renders/";
	public static final String ALL_ALGS_FLAG = "*";
	public static final String SCREEN_FLAG = "-s";
	public static final String IMAGE_FLAG = "-i";
	
	public GridGraphRunner(int width, int height, int outputType,
			String outputFilePath) {
		super(width, height, outputType, outputFilePath);
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length == 2) {	
			int outputType;
			if (args[1].equals(IMAGE_FLAG))
				outputType = GraphExperimentRunner.IMAGE_OUTPUT;
			else
				outputType = GraphExperimentRunner.SCREEN_OUTPUT;
			
			GridGraphRunner runner = new GridGraphRunner(MAX_X,MAX_Y,outputType,RENDER_FILE);
			
			Collection<PathFinder> algs;
			if (args[0].equals(ALL_ALGS_FLAG)) {
				algs = PathFinder.getAvailableAlgorithms(runner.getGraph()).values();
			} else {
				PathFinder algorithm = PathFinder.getAvailableAlgorithms(runner.getGraph()).get(args[0]);
				algs = Arrays.asList(new PathFinder[]{algorithm});
			}
			
			runner.setAlgorithms(algs);
			runner.runExperiment();
			
		} else {
			System.err.println("Bad arguments");
		}
	}

	@Override
	protected List<MapNode> generateGraph() {
		List<MapNode> graph = new ArrayList<MapNode>(EDGE_LENGTH*EDGE_LENGTH);
		for(int y=1;y<=EDGE_LENGTH;y++) {
			for(int x=1;x<=EDGE_LENGTH;x++) {
				int realX = (x * SPACING_X) - (SPACING_X/2);
				int realY = (y * SPACING_Y) - (SPACING_Y/2);
				graph.add(new MapNode(realX + "x" + realY, realX, realY, true));
			}
		}
		return graph;
	}

	@Override
	protected void addEdges(MapNode node, int index, List<MapNode> graph) {
		if (index > 0 && index % EDGE_LENGTH != 0)
			node.addEdge(graph.get(index-1));
		if (index < graph.size()-1 && (index+1) % EDGE_LENGTH != 0)
			node.addEdge(graph.get(index+1));
		if (index > EDGE_LENGTH)
			node.addEdge(graph.get(index-EDGE_LENGTH));
		if (index < graph.size()-EDGE_LENGTH)
			node.addEdge(graph.get(index+EDGE_LENGTH));
	}

	@Override
	protected MapNode generateStartNode() {
		return this.getGraph().get(0);
	}

	@Override
	protected MapNode generateEndNode() {
		return this.getGraph().get(this.getGraph().size()-1);
	}

}
