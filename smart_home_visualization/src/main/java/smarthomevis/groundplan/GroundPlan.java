package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;

public class GroundPlan extends CgApplication
{

	private void run()
	{
		// TODO Auto-generated method stub

	}

	
	
	
	

	public GroundPlan()
	{

	}

	public static void main(String[] args)
	{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		GroundPlan plan = new GroundPlan();
		appLauncher.create(plan);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		plan.run();
	}

}
