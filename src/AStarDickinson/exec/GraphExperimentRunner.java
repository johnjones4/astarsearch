package AStarDickinson.exec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import AStarDickinson.algs.AlgorithmReport;
import AStarDickinson.algs.ConsolePathFinderDelegate;
import AStarDickinson.algs.PathFinder;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.gui.GraphPanel;

public abstract class GraphExperimentRunner {
	public static final String FORMAT = "PNG";
	
	private int width;
	private int height;
	
	private String outputFile;
	
	private int output;
	private static final int SCREEN_OUTPUT = 0;
	private static final int IMAGE_OUTPUT = 1;
	
	
	
	private void runAlgorithm(PathFinder algorithm,MapNode startNode,MapNode endNode,List<MapNode> graph) throws IOException {
		System.out.println(algorithm.getName());
		AlgorithmReport report = algorithm.findPath(new ConsolePathFinderDelegate(), startNode, endNode);
		GraphRenderer panel = render(report,graph);
		if (output == SCREEN_OUTPUT)
			showPanel(report,panel);
		else if (output == IMAGE_OUTPUT)
			saveGraphImage(report,panel);
	}
	
	private GraphRenderer render(AlgorithmReport report,List<MapNode> graph) throws IOException {
		GraphRenderer renderer = new GraphRenderer(width,height,graph);
		renderer.setTree(report.getRoots());
		renderer.setPath(report.getFinalPath());
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
		ImageIO.write(image, FORMAT, getFile(report));
	}
	
	private File getFile(AlgorithmReport report) {
		return new File(outputFile + report.getAlgName() + " " + new Date().getTime() + "." + FORMAT);
	}
}
