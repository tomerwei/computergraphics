/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.motioncapture;

import cgresearch.AppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureConnection;
import cgresearch.graphics.datastructures.motioncapture.MotionCaptureTopology;
import cgresearch.graphics.fileio.MoCapImporter;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Central frame for the mesh exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class MotionCaptureFrame extends CgApplication {

	/**
	 * Constructor.
	 */
	public MotionCaptureFrame() {
		MoCapImporter importer = new MoCapImporter();
		MotionCaptureTopology topology = new MotionCaptureTopology();
		topology.add(new MotionCaptureConnection("0", "2"));
		topology.add(new MotionCaptureConnection("2", "4"));
		topology.add(new MotionCaptureConnection("5", "7"));
		CgNode animationNode = importer.readFromFile(
				"mocap/Trackdata_8_komplexe_bewegung.txt", topology);
		if (animationNode != null) {
			getCgRootNode().addChild(animationNode);
		}
		AnimationTimer.getInstance().setMaxValue(
				animationNode.getNumChildren() - 1);
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		AppLauncher.getInstance().create(new MotionCaptureFrame());
		AppLauncher.getInstance().setRenderSystem(RenderSystem.JOGL);
		AppLauncher.getInstance().setUiSystem(UI.JOGL_SWING);
	}

}
