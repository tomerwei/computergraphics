package cgresearch.studentprojects.autogenerator;

import cgresearch.AppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;

public class Generator extends CgApplication {

	/**
	 * Constructor
	 */

	public Generator() {

	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {

		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		Generator frame = new Generator();
		AppLauncher.getInstance().create(frame);
		AppLauncher.getInstance().setRenderSystem(RenderSystem.JOGL);
		AppLauncher.getInstance().setUiSystem(UI.JOGL_SWING);
		GeneratorGUI gui = new GeneratorGUI(frame.getCgRootNode());
		AppLauncher.getInstance().addCustomUi(gui);

	}
}
