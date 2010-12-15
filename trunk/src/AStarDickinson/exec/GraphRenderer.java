package AStarDickinson.exec;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;

public class GraphRenderer {
	
	private Image image;
	private int width;
	private int height;
	private Collection<MapNode> nodes;
	private MapPath path;
	private TreeNode[] trees;
	private double resizeFactor;
	private Collection<Landmark> landmarks;
	
	public GraphRenderer(int width, int height,Collection<MapNode> nodes) {
		this.width = width;
		this.height = height;
		this.resizeFactor = 1;
		this.nodes = nodes;
	}
	
	public GraphRenderer(String pathToImage,Collection<MapNode> nodes) throws IOException {
		this(pathToImage,nodes,1);
	}
	
	public GraphRenderer(String pathToImage,Collection<MapNode> nodes, double resizeFactor) throws IOException {
		this.image = ImageIO.read(new File(pathToImage));
		this.resizeFactor = resizeFactor;
		this.nodes = nodes;
	}

	public MapPath getPath() {
		return path;
	}

	public void setPath(MapPath path) {
		this.path = path;
	}
	
	public int getRawWidth() {
		if (image != null)
			return image.getWidth(null);
		else
			return width;
	}
	
	public int getRawHeight() {
		if (image != null)
			return image.getHeight(null);
		else
			return width;
	}
	
	public int getGraphWidth() {
		if (image != null)
			return (int)((double)image.getWidth(null) * this.resizeFactor);
		else
			return width;
	}
	
	public int getGraphHeight() {
		if (image != null)
			return (int)((double)image.getHeight(null) * this.resizeFactor);
		else
			return height;
	}
	
	
	/**
	 * @return the candidatePaths
	 */
	public TreeNode[] getTree() {
		return trees;
	}

	/**
	 * @param candidatePaths the candidatePaths to set
	 */
	public void setTree(TreeNode[] tree) {
		this.trees = tree;
	}

	/**
	 * @param landmarks the landmarks to set
	 */
	public void setLandmarks(Collection<Landmark> landmarks) {
		this.landmarks = landmarks;
	}
	
	/**
	 * @return the resizeFactor
	 */
	public double getResizeFactor() {
		return resizeFactor;
	}

	/**
	 * @param resizeFactor the resizeFactor to set
	 */
	public void setResizeFactor(double resizeFactor) {
		this.resizeFactor = resizeFactor;
	}

	/**
	 * @return the nodes
	 */
	public Collection<MapNode> getNodes() {
		return nodes;
	}

	public void render(Graphics g,boolean drawImage, boolean drawAllNodes, boolean drawLandmarks, boolean drawCandidates, boolean drawFinalPath) {
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getGraphWidth(), this.getGraphHeight());
		if (drawImage)
			g.drawImage(image, 0, 0, this.getGraphWidth(), this.getGraphHeight(), null);
		
		if (drawAllNodes) {
			for(MapNode node: nodes) {
				node.draw(g, Color.blue, this.resizeFactor);
				
				for(MapNode edgeNode: node.getEdges()) {
					node.drawEdge(g, edgeNode, Color.blue, this.resizeFactor);
				}
			}
		}
		
		if (drawCandidates && trees != null) {
			int d = 200 / trees.length;
			int v = 200;
			for (TreeNode tree: trees) {
				tree.drawMapNodes(g, new Color(255,255,v,255), this.resizeFactor);
				v -= d;
			}
		}
		
		if (drawFinalPath && path != null) {
			if (path.getStart() != null)
				path.getStart().draw(g, Color.red, this.resizeFactor);
			if (path.getEnd() != null)
				path.getEnd().draw(g, Color.red, this.resizeFactor);
			this.drawPath(path, Color.green, g);
		}
		
		if (drawLandmarks && landmarks != null) {
			for(Landmark landmark: landmarks)
				landmark.draw(g, Color.red, resizeFactor);
		}
	}
	
	private void drawPath(MapPath path, Color c, Graphics g) {
		MapNode lastNode = null;
		for(MapNode node: path.getPath()) {
			node.draw(g, c, this.resizeFactor);
			if (lastNode != null)
				node.drawEdge(g, lastNode, c, this.resizeFactor);
			lastNode = node;
		}
	}
}
