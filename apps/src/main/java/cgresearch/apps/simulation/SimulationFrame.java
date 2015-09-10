package cgresearch.apps.simulation;

import cgresearch.AppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.Animation;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.projects.simulation.Simulation;

public class SimulationFrame extends CgApplication {

	/**
	 * Cloth object
	 */
	protected Simulation simulator;

	/**
	 * Constructor
	 */
	public SimulationFrame() {
		AnimationTimer.getInstance().setMaxValue(500);

		CgNode animationNode = new CgNode(new Animation(), "simulation");
		CgNode supplementNode = new CgNode(null, "simulation_supplement");
		getCgRootNode().addChild(animationNode);
		getCgRootNode().addChild(supplementNode);
		getCgRootNode().addChild(new CoordinateSystem());

		simulator = new cgresearch.projects.simulation.ClothSimulation(
				animationNode, supplementNode);
		// simulator = new edu.haw.cg.simulation.ball.BallSimulation(
		// animationNode, supplementNode);
	}

	/**
	 * Getter.
	 */
	public Simulation getSimulator() {
		return simulator;
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		SimulationFrame app = new SimulationFrame();
		AppLauncher.getInstance().create(app);
		AppLauncher.getInstance().setRenderSystem(RenderSystem.JOGL);
		AppLauncher.getInstance().setUiSystem(UI.JOGL_SWING);
		AppLauncher.getInstance().addCustomUi(
				new SimulationGui(app.getSimulator()));
	}
}