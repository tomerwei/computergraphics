package smarthomevis.groundplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.JoglAppLauncher;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.math.Vector;
import cgresearch.graphics.bricks.CgApplication;
import smarthomevis.groundplan.config.GPDataType;
import smarthomevis.groundplan.config.GPLine;

public class Test extends CgApplication
	{

		public void calculateDistancesInPlan(GPDataType type)
		{
			List<List<GPLine>> layerList = type.getLayerLists();
			
			Map<Vector, List<GPLine>> directionMap = new HashMap<>();
			for(List linesOfLayerList : layerList)
				{
					
				}
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
		}

	}
