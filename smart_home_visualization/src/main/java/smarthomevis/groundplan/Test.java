package smarthomevis.groundplan;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPConfigXMLReader;
import smarthomevis.groundplan.config.GPDataType;

public class Test extends CgApplication
{
	private void runAnalyzer()
	{
		// runPlan("4H-HORA Projekt1");
		runPlan("TestRaum");
		// runPlan("Grundriss_Haus_02");
		// runPlan("distanzVerteilungTest1");

	}

	private void runPlan(String planName)
	{
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/" + planName + ".xml");
		System.out.println(config.toString());

		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/" + planName + ".dxf", config);

		renderData.setGPConfig(config);
		new GPAnalyzer().calculateDistancesInPlan(renderData);
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
		System.out
			.println(tmp + " / " + interval + " = " + interValue + "; " + value + " - " + remainder + " = " + tmp);

		BigDecimal intervalA = BigDecimal.valueOf(3.0);
		BigDecimal valueA = BigDecimal.valueOf(8.0);

		BigDecimal resultA = valueA.divide(intervalA, 3, RoundingMode.HALF_DOWN);
		BigDecimal restA = valueA.remainder(intervalA);

		System.out.println(valueA + " / " + intervalA + " = " + resultA + "; remainder: " + restA);
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
		CgApplication app = new Test();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		Test test = new Test();

		test.runAnalyzer();
		// test.runTest();
	}

}
