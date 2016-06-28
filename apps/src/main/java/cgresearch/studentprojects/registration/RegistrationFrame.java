package cgresearch.studentprojects.registration;



import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;

/**
 * Initial frame for the registration project (J�ckel)
 *
 */
public class RegistrationFrame extends CgApplication {



	/**
	 * Constructor
	 */
	public RegistrationFrame() {


		// New version: Jaeckel

	}


	public static void main(String[] args) {

		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		RegistrationFrame app = new RegistrationFrame();
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();

		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		appLauncher.addCustomUi(new RegistrationButton(app.getCgRootNode()));


	}

}
