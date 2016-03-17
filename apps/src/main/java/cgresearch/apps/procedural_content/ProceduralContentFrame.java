/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.procedural_content;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;

/**
 * Camera path in a virtual scene.
 * 
 * @author Philipp Jenke
 * 
 */
public class ProceduralContentFrame extends CgApplication {

	private StoryGenerator storyGenerator;

	public ProceduralContentFrame() {
		storyGenerator = new StoryGenerator();
		CgNode node = storyGenerator.generate("procedural_content/procedural_content.xml");
		if (node != null) {
			getCgRootNode().addChild(node);
		}
	}

	public static void main(String[] args) {
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		ProceduralContentFrame app = new ProceduralContentFrame();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);
	}
}
