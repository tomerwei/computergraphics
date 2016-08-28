package smarthomevis.groundplan;

import java.util.List;
import java.util.Map;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.camera.Camera;
import cgresearch.graphics.scenegraph.CgNode;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPConfigXMLReader;
import smarthomevis.groundplan.config.GPDataImporter;
import smarthomevis.groundplan.data.GPDataType;

public class GroundPlan extends CgApplication implements IGroundPlan {
	/**
	 * Import Methode
	 * 
	 * @param planName
	 * @return
	 */
	public GPDataType analyzePlan(String planName) {
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/" + planName + ".xml");
		System.out.println(config.toString());

		GPDataImporter converter = new GPDataImporter();
		GPDataType renderData = converter.importData("dxf/" + planName + ".dxf", config);

		Map<Double, List<String[]>> pairsToDistanceMap = new GPAnalyzer().analyzeData(renderData);

		new GPEvaluator().processData(renderData, pairsToDistanceMap);

		return renderData;
	}

	@Override
	public CgNode convertDXFPlanToCgNode(String gpName) {
		GPConfigXMLReader reader = new GPConfigXMLReader();
		GPConfig config = reader.readConfig("dxf/" + gpName + ".xml");

		GPDataImporter converter = new GPDataImporter();
		GPDataType renderData = converter.importData("dxf/" + gpName + ".dxf", config);

		return construct3DMeshFromData(renderData);
	}

	public CgNode construct3DMeshFromData(GPDataType data) {
		GPRenderer renderer = new GPRenderer(data);

		return renderer.render3DMeshViewFromGPDataType();
	}
	
	public CgNode construct3DGridFromData(GPDataType data)
	{
		GPRenderer renderer = new GPRenderer(data);

		return renderer.render3DGridViewFromGPDataType();
	}

	public void renderAndDisplayPlan(String planName) {
		getCgRootNode().addChild(convertDXFPlanToCgNode(planName));
	}

	public void analyzeAndMeshRenderPlan(String planName) {
		GPDataType data = analyzePlan(planName);
		CgNode node = construct3DMeshFromData(data);
		getCgRootNode().addChild(node);
	}

	public void analyzeAndGridRenderPlan(String planName) {
		GPDataType data = analyzePlan(planName);
		CgNode node = construct3DGridFromData(data);
		getCgRootNode().addChild(node);
	}

	public void analyzeAndRenderSolids(String planName) {
		GPDataType data = analyzePlan(planName);

		GPRenderer renderer = new GPRenderer(data);

		CgNode node = renderer.render3DMeshViewFromSolids();
		getCgRootNode().addChild(node);
	}
	
	public static void main(String[] args)
	{
		ResourcesLocator.getInstance().parseIniFile("resources.ini");
		JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
		GroundPlan app = new GroundPlan();
		appLauncher.create(app);
		appLauncher.setRenderSystem(RenderSystem.JOGL);
		appLauncher.setUiSystem(UI.JOGL_SWING);

		if(args.length > 0)
			{
				if(args.length > 1)
					Logger.getInstance().error("Too many arguments");
				else
					{
						String groundplan = args[0];
						Logger.getInstance().message("Analyzing groundplan "+groundplan+"...");
						app.analyzeAndRenderSolids(groundplan);
						Camera.getInstance().setCenterViewRequired();
					}
			}
		else
			{
				app.analyzeAndRenderSolids("4H-HORA Projekt1");
				Camera.getInstance().setCenterViewRequired();
			}
	}

	public GroundPlan() {

	}
}
