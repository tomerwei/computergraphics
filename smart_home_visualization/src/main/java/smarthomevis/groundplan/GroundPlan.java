package smarthomevis.groundplan;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import smarthomevis.groundplan.config.GPDataImporter;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPConfigXMLReader;
import smarthomevis.groundplan.data.GPDataType;

public class GroundPlan extends CgApplication implements IGroundPlan
{
	/**
	 * Import Methode
	 * 
	 * @param planName
	 * @return
	 */
	public GPDataType analyzePlan(String planName)
	{
	GPConfigXMLReader reader = new GPConfigXMLReader();
	GPConfig config = reader.readConfig("dxf/" + planName + ".xml");
	System.out.println(config.toString());
	
	GPDataImporter converter = new GPDataImporter();
	GPDataType renderData = converter.importData("dxf/" + planName + ".dxf",
		config);
		
	new GPAnalyzer().analyzeAndProcessData(renderData);
	return renderData;
	}
	
	@Override
	public CgNode convertDXFPlanToCgNode(String gpName)
	{
	GPConfigXMLReader reader = new GPConfigXMLReader();
	GPConfig config = reader.readConfig("dxf/" + gpName + ".xml");
	
	GPDataImporter converter = new GPDataImporter();
	GPDataType renderData = converter.importData("dxf/" + gpName + ".dxf",
		config);
		
	
	return construct3DMeshFromData(renderData);
	}
	
	public CgNode construct3DMeshFromData(GPDataType data)
	{
	GPRenderer renderer = new GPRenderer(data);
	
	return renderer.render3DMeshViewFromGPDataType();
	}
	
	public void renderAndDisplayPlan(String planName)
	{
	getCgRootNode().addChild(convertDXFPlanToCgNode(planName));
	}
	
	public void analyzeAndRenderPlan(String planName)
	{
	GPDataType data = analyzePlan(planName);
	CgNode node = construct3DMeshFromData(data);
	getCgRootNode().addChild(node);
	}
	
	public void analyzeAndRenderSolids(String planName)
	{
		GPDataType data = analyzePlan(planName);
		
		GPRenderer renderer = new GPRenderer(data);
		
		CgNode node = renderer.render3DMeshViewFromSolids();
		getCgRootNode().addChild(node);
	}
	
	public GroundPlan()
	{
	
	}
}
