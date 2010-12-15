package AStarDickinson.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.datastructs.graph.MapNode;

public class RandomGraphRunner extends GraphExperimentRunner {
	
	public static final int GRAPH_SIZE = 999;
	public static final int MAX_X = 1000;
	public static final int MAX_Y = 1000;
	public static final int TOLERANCE = 100;
	public static final int INSET = 200;
	public static final String RENDER_FILE = "renders/";
	public static final String ALL_ALGS_FLAG = "*";
	public static final String SCREEN_FLAG = "-s";
	public static final String IMAGE_FLAG = "-i";
	
	public RandomGraphRunner(int width, int height, int outputType,
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
			
			RandomGraphRunner runner = new RandomGraphRunner(MAX_X,MAX_Y,outputType,RENDER_FILE);
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
		List<MapNode> graph = new ArrayList<MapNode>(GRAPH_SIZE+2);
		int x[] = getRandoms(GRAPH_SIZE,this.getWidth());
		Random rand2 = new Random(new Date().getTime());
		for(int i=0;i<GRAPH_SIZE;i++) {
			int y = rand2.nextInt(this.getHeight());
			graph.add(new MapNode(x[i] + "," + y,x[i],y,true));
		}
		return graph;
	}
	
	@Override
	protected MapNode generateStartNode() {
		MapNode node = new MapNode("start",INSET,INSET,true);
		this.getGraph().add(node);
		this.addEdges(node,this.getGraph().indexOf(node),this.getGraph());
		return node;
	}
	
	@Override
	protected MapNode generateEndNode() {
		MapNode node = new MapNode("end",this.getWidth()-INSET,this.getHeight()-INSET,true);
		this.getGraph().add(node);
		this.addEdges(node,this.getGraph().indexOf(node),this.getGraph());
		return node;
	}
	
	@Override
	protected void addEdges(MapNode node, int index, List<MapNode> graph) {
		for (MapNode otherNode: graph) {
			if (otherNode.getPoint().getX() <= node.getPoint().getX() + TOLERANCE
					&& otherNode.getPoint().getX() >= node.getPoint().getX() - TOLERANCE
					&& otherNode.getPoint().getY() <= node.getPoint().getY() + TOLERANCE
					&& otherNode.getPoint().getY() >= node.getPoint().getY() - TOLERANCE) {
				node.addEdge(otherNode);
				otherNode.addEdge(node);
			}
		}
	}
	
	public static int[] getRandoms(int n,int max) {
		Random rand = new Random(new Date().getTime());
		int[] rands = new int[n];
		for (int i=0;i<n;i++) {
			rands[i] = rand.nextInt(max);
			boolean pass = false;
			while(!pass) {
				boolean reset = false;
				checker: for (int j=0;j<i;j++) {
					if (rands[j] == rands[i]) {
						reset = true;
						break checker;
					}
				}
				if (reset)
					rands[i] = rand.nextInt(max);
				else
					pass = true;
			}
		}
		return rands;
	}
}
