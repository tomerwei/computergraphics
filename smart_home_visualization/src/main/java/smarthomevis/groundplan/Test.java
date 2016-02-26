package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;

public class Test
{
	private void testRender(GroundPlan app)
	{
	app.renderAndDisplayPlan("4H-HORA Projekt1");
	// app.renderAndDisplayPlan("4H-HORA MessTest");
	// app.renderAndDisplayPlan("TestRaum");
	// app.renderAndDisplayPlan("Grundriss_Haus_02");
	
	}
	
	private void runAnalyzer(GroundPlan app)
	{
	// app.analyzePlan("4H-HORA Projekt1");
	app.analyzePlan("TestRaum");
	// app.analyzePlan("Grundriss_Haus_02");
	// app.analyzePlan("distanzVerteilungTest1");
	// app.analyzePlan("distanzVerteilungTest2");
	}
	
	private void runTest()
	{
	// System.out.println("Math.pow(3, 2) = " + Math.pow(3.0, 2.0));
	// System.out.println("Math.pow(4, 2) = " + Math.pow(4.0, 2.0));
	//
	// System.out.println("5.5 * (-1) = " + 5.5 * (-1));
	
	// System.out.println("1.00050: " + roundDown3(1.00050));
	// System.out.println("-1.00050: " + roundDown3(-1.00050));
	// System.out.println("-1.00350: " + roundDown3(-1.00350));
	
	// IVector3 alpha_0 = new Vector3(0.0, 2.0, 0.0);
	// IVector3 alpha_1 = new Vector3(0.0, 5.0, 0.0);
	// IVector3 beta_0 = new Vector3(1.6, 2.0, 0.0);
	// IVector3 beta_1 = new Vector3(1.6, 5.0, 0.0);
	//
	// GPLine A = new GPLine("Ahhh", alpha_0, alpha_1);
	// GPLine B = new GPLine("Beeh", beta_0, beta_1);
	//
	// System.out.println("Distance between " + A.getName() + " and "
	// + B.getName() + " is " + roundDown3(distanceBetween(A, B)));
	
	// double x = 3.4;
	// double y = 5.6;
	//
	// BigDecimal bX = BigDecimal.valueOf(x);
	// BigDecimal bY = BigDecimal.valueOf(y);
	//
	// double result = (bX.subtract(bY)).doubleValue();
	//
	// System.out.println("result x-y = " + result);
	//
	// double a = -0.5;
	// double b = 1.0;
	//
	// BigDecimal bA = BigDecimal.valueOf(a);
	// BigDecimal bB = BigDecimal.valueOf(b);
	//
	// double result1 = (bA.subtract(bB)).doubleValue();
	//
	// System.out.println("result x-y = " + result1);
	//
	// IVector3 testResult = GPUtility.substractOtherVector(
	// new Vector3(-0.5, 1.2, -32.0), new Vector3(1.0, -2.0, -33.0));
	//
	// System.out.println(
	// "result of GPUtility.substractOtherVector: " +
	// testResult.toString(3));
	
	BigDecimal interval = BigDecimal.valueOf(5.0);
	BigDecimal value = BigDecimal.valueOf(0.2);
	
	BigDecimal remainder = value.remainder(interval);
	
	BigDecimal tmp = value.subtract(remainder);
	
	BigDecimal interValue = tmp.divide(interval);
	System.out.println(tmp + " / " + interval + " = " + interValue + "; "
		+ value + " - " + remainder + " = " + tmp);
		
	BigDecimal intervalA = BigDecimal.valueOf(3.0);
	BigDecimal valueA = BigDecimal.valueOf(8.0);
	
	BigDecimal resultA = valueA.divide(intervalA, 3, RoundingMode.HALF_DOWN);
	BigDecimal restA = valueA.remainder(intervalA);
	
	System.out.println(
		valueA + " / " + intervalA + " = " + resultA + "; remainder: " + restA);
	}
	
	private void analyzeAndRender(GroundPlan app)
	{
	app.analyzeAndRenderPlan("4H-HORA Projekt1");
	// app.analyzeAndRenderPlan("TestRaum");
	// app.analyzeAndRenderPlan("Grundriss_Haus_02");
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
	test.analyzeAndRender(app);
	}
	
}
