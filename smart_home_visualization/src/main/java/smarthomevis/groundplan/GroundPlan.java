package smarthomevis.groundplan;

import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.scenegraph.CgNode;
import smarthomevis.groundplan.config.Converter;
import smarthomevis.groundplan.config.GPConfig;
import smarthomevis.groundplan.config.GPConfigXMLReader;
import smarthomevis.groundplan.config.GPDataType;

public class GroundPlan extends CgApplication implements IGroundPlan
{
	public GPDataType analyzePlan(String planName)
	{
	GPConfigXMLReader reader = new GPConfigXMLReader();
	GPConfig config = reader.readConfig("dxf/" + planName + ".xml");
	System.out.println(config.toString());
	
	Converter converter = new Converter();
	GPDataType renderData = converter.importData("dxf/" + planName + ".dxf",
		config);
		
	renderData.setGPConfig(config);
	new GPAnalyzer().analyzeAndProcessData(renderData);
	return renderData;
	}
	
	@Override
	public CgNode convertDXFPlanToCgNode(String gpName)
	{
	GPConfigXMLReader reader = new GPConfigXMLReader();
	GPConfig config = reader.readConfig("dxf/" + gpName + ".xml");
	
	Converter converter = new Converter();
	GPDataType renderData = converter.importData("dxf/" + gpName + ".dxf",
		config);
		
	renderData.setGPConfig(config);
	
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
	
	public GroundPlan()
	{
	
	}
}
