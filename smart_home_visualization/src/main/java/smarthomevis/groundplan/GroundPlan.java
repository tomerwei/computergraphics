package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPDataType;

public class GroundPlan extends CgApplication
	{

	private void run()
		{
		renderProjektHORA();

		}

	private void renderProjektHORA()
		{
		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/4H-HORA Projekt1.dxf",
			"dxf/4H-HORA Projekt1.xml");

		System.out.println(renderData.toString());
		GPRenderer renderer = new GPRenderer();
		getCgRootNode().addChild(renderer.renderFromGPDataType(renderData));
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
