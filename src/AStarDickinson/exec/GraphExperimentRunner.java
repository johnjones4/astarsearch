package AStarDickinson.exec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.ConsolePathFinderDelegate;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.algs.implementations.StaticALTSearch;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.gui.GraphPanel;
import AStarDickinson.render.AnimationRenderer;
import AStarDickinson.render.GraphRenderer;

public abstract class GraphExperimentRunner {
	public static final String FORMAT = "PNG";
	public static final int SCREEN_OUTPUT = 0;
	public static final int IMAGE_OUTPUT = 1;
	public static final int TRIALS = 20;
	private static final String[] colHeaders = new String[]{"Algorithm","Path Nodes","Path Distance","Tree(s) size","Runtime"};

	private int width;
	private int height;
	private String outputFile;
	private int output;
	private Collection<PathFinder> algorithms;
	private MapNode startNode;
	private MapNode endNode;
	private List<MapNode> graph;
	private PrintWriter printOut;
	private long timestamp;

	public GraphExperimentRunner(int width, int height, int outputType, String outputFilePath) {
		this.width = width;
		this.height = height;
		this.output = outputType;
		this.outputFile = outputFilePath;
		this.timestamp = new Date().getTime();
		
		if (this.output == IMAGE_OUTPUT)
			System.setProperty("java.awt.headless", "true");
		
		System.out.println("Building vertices");
		this.graph = generateGraph();
		
		System.out.println("Building edges");
		int i=0;
		for(MapNode node: graph)
			addEdges(node,i++,graph);
		
		System.out.println("Adding start and end points");
		
		this.startNode = this.generateStartNode();
		this.endNode = this.generateEndNode();
	}
	
	public void setAlgorithms(Collection<PathFinder> algorithms) {
		this.algorithms = algorithms;
	}
	
	public List<MapNode> getGraph() {
		return this.graph;
	}
	
	protected abstract List<MapNode> generateGraph();
	protected abstract void addEdges(MapNode node, int nodeIndex, List<MapNode> graph);
	protected abstract MapNode generateStartNode();
	protected abstract MapNode generateEndNode();
	
	public void runExperiment() throws IOException {
		this.printOut = new PrintWriter(new FileWriter(this.getTextFile()));
		boolean first = true;
		for (String col: colHeaders) {
			if (!first)
				this.printOut.print(",");
			first = false;
			this.printOut.print(col);
		}
		this.printOut.println();
		for(PathFinder algorithm: this.algorithms)
			this.runAlgorithm(algorithm);
		this.printOut.close();
	}
	
	private void runAlgorithm(PathFinder algorithm) throws IOException  {		
		System.out.println(algorithm.getName());
		long totaltime = 0;
		AlgorithmReport report = null;
		for (int i=0;i<TRIALS;i++) {
			long start = System.nanoTime();
			PathFinderDelegate del = null;
			if (i < 5)
				del = new AnimationRenderer(algorithm.getName(),width,height,graph,.5);
			report = algorithm.findPath(new ConsolePathFinderDelegate(del), startNode, endNode);
			long end = System.nanoTime();
			totaltime += end-start;
		}
		
		this.printOut.print(report.getAlgName() + ",");
		this.printOut.print(report.getFinalPath().getPath().size() + ",");
		this.printOut.print(report.getFinalPath().getPathDistance() + ",");
		this.printOut.print(report.getTreesSize() + ",");
		this.printOut.print(totaltime/TRIALS);
		this.printOut.println();
		
		GraphRenderer panel = render(report,algorithm,graph);
		if (output == SCREEN_OUTPUT)
			showPanel(report,panel);
		else if (output == IMAGE_OUTPUT)
			saveGraphImage(report,panel);
	}
	
	private GraphRenderer render(AlgorithmReport report,PathFinder algorithm, List<MapNode> graph) throws IOException {
		GraphRenderer renderer = new GraphRenderer(width,height,graph);
		renderer.setTree(report.getRoots());
		renderer.setPath(report.getFinalPath());
		if (algorithm instanceof StaticALTSearch)
			renderer.setLandmarks(((StaticALTSearch)algorithm).getLandmarks());
		return renderer;
	}
	
	private void showPanel(AlgorithmReport report,GraphRenderer renderer) {
		JFrame frame = new JFrame(report.getAlgName());
		frame.getContentPane().add(new GraphPanel(renderer));
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private void saveGraphImage(AlgorithmReport report,GraphRenderer renderer) throws IOException {
		BufferedImage image = new BufferedImage(renderer.getGraphWidth(),renderer.getGraphHeight(),BufferedImage.TYPE_INT_RGB);
		renderer.render(image.getGraphics(),true,true,true,true,true);
		ImageIO.write(image, FORMAT, getImageFile(report));
		image.flush();
	}
	
	private File getImageFile(AlgorithmReport report) {
		return new File(outputFile + report.getAlgName() + " " + timestamp + "." + FORMAT);
	}
	
	private File getTextFile() {
		return new File(outputFile + "exp_" + timestamp + ".csv");
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	
}
