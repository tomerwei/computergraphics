package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;

public class Test
{
//	private void testRender(GroundPlan app)
//	{
//		// app.renderAndDisplayPlan("4H-HORA Projekt1");
//		// app.renderAndDisplayPlan("4H-HORA MessTest");
//		// app.renderAndDisplayPlan("Grundriss_Haus_02");
//		// app.renderAndDisplayPlan("TestRaum");
//		app.renderAndDisplayPlan("TestRaum2");
//
//	}
//
//	private void runAnalyzer(GroundPlan app)
//	{
//		app.analyzePlan("4H-HORA Projekt1");
//		// app.analyzePlan("TestRaum");
//		// app.analyzePlan("Grundriss_Haus_02");
//		// app.analyzePlan("distanzVerteilungTest1");
//		// app.analyzePlan("distanzVerteilungTest2");
//		// app.analyzePlan("lueckenTest");
//	}
//
//	private void analyzeAndRender(GroundPlan app)
//	{
//		// app.analyzeAndMeshRenderPlan("4H-HORA Projekt1");
//		// app.analyzeAndMeshRenderPlan("TestRaum");
//		// app.analyzeAndMeshRenderPlan("Grundriss_Haus_02");
//
//		// app.analyzeAndGridRenderPlan("4H-HORA Projekt1");
//		app.analyzeAndGridRenderPlan("TestRaum");
//		// app.analyzeAndGridRenderPlan("Grundriss_Haus_02");
//
//	}

	private void testSolidRendering(GroundPlan app)
	{
		// app.analyzeAndRenderSolids("4H-HORA Projekt1");
		// app.analyzeAndRenderSolids("Grundriss_Haus_02");
		// app.analyzeAndRenderSolids("TestRaum");
		app.analyzeAndRenderSolids("TestRaum2");
		// app.analyzeAndRenderSolids("lueckenTest");
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
		// test.runTest();
		// test.runAnalyzer(app);
		// test.testRender(app);
		// test.analyzeAndRender(app);
		test.testSolidRendering(app);
	}

}
