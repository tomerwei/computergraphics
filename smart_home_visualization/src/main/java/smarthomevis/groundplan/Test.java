package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;

public class Test
{
	@SuppressWarnings("unused")
	private void analyzeAndRender(GroundPlan app)
	{
		// app.analyzeAndMeshRenderPlan("4H-HORA Projekt1");
		// app.analyzeAndMeshRenderPlan("TestRaum");
		// app.analyzeAndMeshRenderPlan("Grundriss_Haus_02");

		// app.analyzeAndGridRenderPlan("4H-HORA Projekt1");
		// app.analyzeAndGridRenderPlan("TestRaum");
		app.analyzeAndGridRenderPlan("ElementTypen");
		// app.analyzeAndGridRenderPlan("Grundriss_Haus_02");

	}

	private void testSolidRendering(GroundPlan app)
	{
		// app.renderAndDisplayPlan("4H-HORA Projekt1");
		app.renderAndDisplayPlan("Grundriss_Haus_02");
		// app.renderAndDisplayPlan("TestRaum");
		// app.renderAndDisplayPlan("TestRaum2");
		// app.renderAndDisplayPlan("TestRaum3");
		// app.renderAndDisplayPlan("lueckenTest");
	}

	/*
	 *
	 * ==============================================================
	 */

	public Test()
	{

	}

	public static void main(String[] args)
	{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		GroundPlan app = new GroundPlan();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		Test test = new Test();

		// test.analyzeAndRender(app);
		test.testSolidRendering(app);
	}

}
