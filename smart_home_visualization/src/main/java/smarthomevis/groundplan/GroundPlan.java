package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPConfigXMLReader;
import smarthomevis.groundplan.config.GPDataType;

public class GroundPlan extends CgApplication
{

	private void run()
	{
		// LightSource light = new LightSource(LightSource.Type.DIRECTIONAL);
		// light.setColor(new Vector3(1.0, 1.0 , -1.0));
		// light.setPosition(new Vector3(15.0, 15.0 , 5.0));
		// getCgRootNode().addLight(light);
		renderTestRaum();
		// renderProjektHORA();
		// renderHaus02();
	}

	private void renderTestRaum()
	{
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/TestRaum.xml");
		System.out.println(config.toString());

		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/TestRaum.dxf", config);
		System.out.println(renderData.toString());

		renderData.setGPConfig(config);

		GPRenderer renderer = new GPRenderer(renderData);
		// getCgRootNode().addChild(renderer.render2DViewFromGPDataType());
		// getCgRootNode().addChild(renderer.render3DGridViewFromGPDataType());
		getCgRootNode().addChild(renderer.render3DMeshViewFromGPDataType());
	}

	private void renderProjektHORA()
	{
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/4H-HORA Projekt1.xml");
		System.out.println(config.toString());

		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/4H-HORA Projekt1.dxf", config);

		renderData.setGPConfig(config);

		GPRenderer renderer = new GPRenderer(renderData);
		// getCgRootNode().addChild(renderer.render2DViewFromGPDataType());
		// getCgRootNode().addChild(renderer.render3DGridViewFromGPDataType());
		getCgRootNode().addChild(renderer.render3DMeshViewFromGPDataType());
	}

	private void renderHaus02()
	{
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/Grundriss_Haus_02.xml");
		System.out.println(config.toString());

		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/Grundriss_Haus_02.dxf", config);

		renderData.setGPConfig(config);

		GPRenderer renderer = new GPRenderer(renderData);
		// getCgRootNode().addChild(renderer.render2DViewFromGPDataType());
		// getCgRootNode().addChild(renderer.render3DGridViewFromGPDataType());
		getCgRootNode().addChild(renderer.render3DMeshViewFromGPDataType());
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
