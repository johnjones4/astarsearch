package AStarDickinson.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.imageio.ImageIO;

import AStarDickinson.algs.PathFinderDelegate;
import AStarDickinson.datastructs.graph.Landmark;
import AStarDickinson.datastructs.graph.MapNode;
import AStarDickinson.datastructs.graph.MapPath;
import AStarDickinson.datastructs.tree.TreeNode;
import AStarDickinson.exec.GraphExperimentRunner;

public class AnimationRenderer implements PathFinderDelegate {
	private static final String ANIMATION_DIR = "animation/";
	
	private long stamp;
	private String name;
	private int frame;
	private int width;
	private int height;
	private double resizeFactor;
	private Collection<MapNode> nodes;
	
	private TreeNode[] roots;
	private Collection<Landmark> landmarks;
	private MapPath finalPath;
	
	
	public AnimationRenderer(String name,int width, int height,Collection<MapNode> nodes, double resizeFactor) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.resizeFactor = resizeFactor;
		this.nodes = nodes;
		this.stamp = new Date().getTime();
		this.frame = 0;
	}

	@Override
	public void setRootNodes(TreeNode[] nodes) {
		this.roots = nodes;
		this.writeFrame();
	}

	@Override
	public void setFinalPath(MapPath finalPath) {
		this.finalPath = finalPath;
		this.writeFrame();
	}

	@Override
	public void setLandmarks(Collection<Landmark> landmarks) {
		this.landmarks = landmarks;
		this.writeFrame();
	}

	@Override
	public void pathsWereUpdated() {
		this.writeFrame();
	}
	
	private void writeFrame() {
		try {
			nextFrame();
			GraphRenderer renderer = new GraphRenderer(width,height,nodes,resizeFactor);
			renderer.setTree(roots);
			renderer.setPath(finalPath);
			renderer.setLandmarks(landmarks);
			BufferedImage image = new BufferedImage(renderer.getGraphWidth(),renderer.getGraphHeight(),BufferedImage.TYPE_INT_RGB);
			renderer.render(image.getGraphics(),true,true,true,true,true);
			ImageIO.write(image, GraphExperimentRunner.FORMAT, this.getFrameDir());
			image.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int nextFrame() {
		return this.frame++;
	}
	
	private File getStorageDir() {
		File storageDir = new File(ANIMATION_DIR + stamp);
		if (!storageDir.exists())
			storageDir.mkdirs();
		return storageDir;
	}

	private File getFrameDir() {
		return new File(this.getStorageDir(),String.valueOf(this.frame) + "." + GraphExperimentRunner.FORMAT);
	}
}
