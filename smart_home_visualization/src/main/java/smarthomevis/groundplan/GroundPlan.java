package smarthomevis.groundplan;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector3;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.LightSource;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPDataType;

public class GroundPlan extends CgApplication
{

	private void run()
	{
		// LightSource light = new LightSource(LightSource.Type.POINT);
		// light.setColor(new Vector3(3.0, 3.0 , 3.0));
		// light.setPosition(new Vector3(15.0, 15.0 , 5.0));
		// getCgRootNode().addLight(light);
		renderTestRaum();
		// renderProjektHORA();
		// renderHaus02();
	}

	private void renderTestRaum()
	{
		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/TestRaum.dxf", "dxf/TestRaum.xml");

		GPRenderer renderer = new GPRenderer(renderData);
		// getCgRootNode().addChild(renderer.render2DViewFromGPDataType());
		// getCgRootNode().addChild(renderer.render3DGridViewFromGPDataType());
		getCgRootNode().addChild(renderer.render3DMeshViewFromGPDataType());
	}

	private void renderProjektHORA()
	{
		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/4H-HORA Projekt1.dxf", "dxf/4H-HORA Projekt1.xml");

		GPRenderer renderer = new GPRenderer(renderData);
		// getCgRootNode().addChild(renderer.render2DViewFromGPDataType());
		// getCgRootNode().addChild(renderer.render3DGridViewFromGPDataType());
		getCgRootNode().addChild(renderer.render3DMeshViewFromGPDataType());
	}

	private void renderHaus02()
	{
		Converter converter = new Converter();
		GPDataType renderData = converter.importData("dxf/Grundriss_Haus_02.dxf", "dxf/Grundriss_Haus_02.xml");

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
