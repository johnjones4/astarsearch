package AStarDickinson.exec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.ConsolePathFinderDelegate;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.implementations.AStarSearch;
import AStarDickinson.algs.implementations.BidirectionalSymmetricAStarSearch;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.gui.GraphPanel;

public class RandomGraphRunner {
	public static final int GRAPH_SIZE = 5000;
	public static final int MAX_X = 5000;
	public static final int MAX_Y = 5000;
	public static final int TOLERANCE = 100;
	public static final int INSET = 1000;
	public static final String RENDER_FILE = "renders/";
	public static final String FORMAT = "PNG";
	public static final String ALL_ALGS_FLAG = "*";
	public static final String SCREEN_FLAG = "-s";
	public static final String IMAGE_FLAG = "-i";
	
	public static void main(String[] args) throws IOException {
		if (args.length == 2) {	
			if (args[1].equals(IMAGE_FLAG))
				System.setProperty("java.awt.headless", "true");
			
			List<MapNode> graph = randomGraph(GRAPH_SIZE,MAX_X,MAX_Y);
			
			System.out.println("Adding start and end points");
			MapNode startNode = new MapNode("start",INSET,INSET,true);
			addEdges(startNode,graph);
			graph.add(startNode);
			MapNode endNode = new MapNode("end",MAX_X-INSET,MAX_Y-INSET,true);
			addEdges(endNode,graph);
			graph.add(endNode);
			
			System.out.println("Running algorithms");
			
			if (args[0].equals(ALL_ALGS_FLAG)) {
				for(PathFinder algorithm : PathFinder.getAvailableAlgorithms(graph).values()) {
					runAlgorithm(algorithm,startNode,endNode,graph,args);
				}
			} else {
				PathFinder algorithm = PathFinder.getAvailableAlgorithms(graph).get(args[0]);
				runAlgorithm(algorithm,startNode,endNode,graph,args);
			}
		} else {
			System.err.println("Bad arguments");
		}
	}
	
	private static void runAlgorithm(PathFinder algorithm,MapNode startNode,MapNode endNode,List<MapNode> graph,String[] args) throws IOException {
		System.out.println(algorithm.getName());
		AlgorithmReport report = algorithm.findPath(new ConsolePathFinderDelegate(), startNode, endNode);
		GraphRenderer panel = render(report,graph);
		if (args[1].equals(SCREEN_FLAG))
			showPanel(report,panel);
		else if (args[1].equals(IMAGE_FLAG))
			saveGraphImage(report,panel);
	}
	
	private static GraphRenderer render(AlgorithmReport report,List<MapNode> graph) throws IOException {
		GraphRenderer renderer = new GraphRenderer(MAX_X,MAX_Y,graph);
		renderer.setTree(report.getRoots());
		renderer.setPath(report.getFinalPath());
		return renderer;
	}
	
	private static void showPanel(AlgorithmReport report,GraphRenderer renderer) {
		JFrame frame = new JFrame(report.getAlgName());
		frame.getContentPane().add(new GraphPanel(renderer));
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private static void saveGraphImage(AlgorithmReport report,GraphRenderer renderer) throws IOException {
		BufferedImage image = new BufferedImage(renderer.getGraphWidth(),renderer.getGraphHeight(),BufferedImage.TYPE_INT_RGB);
		renderer.render(image.getGraphics(),true,true,true,true,true);
		ImageIO.write(image, FORMAT, getFile(report));
	}
	
	private static File getFile(AlgorithmReport report) {
		return new File(RENDER_FILE + report.getAlgName() + " " + new Date().getTime() + "." + FORMAT);
	}
	
	private static List<MapNode> randomGraph(int n,int maxX, int maxY) {
		System.out.println("Building vertices");
		List<MapNode> graph = new Vector<MapNode>(n+2);
		int x[] = getRandoms(n,maxX);
		int y[] = getRandoms(n,maxY);
		for(int i=0;i<n;i++) {
			graph.add(new MapNode(x[i] + "," + y[i],x[i],y[i],true));
		}
		
		System.out.println("Building edges");
		for(MapNode node: graph)
			addEdges(node,graph);
		
		return graph;
	}
	
	private static void addEdges(MapNode node, List<MapNode> graph) {
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
