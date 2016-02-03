package smarthomevis.groundplan;

import cgresearch.graphics.scenegraph.CgNode;
import smarthomevis.groundplan.config.GPDataType;

public interface IGroundPlan
{
	/**
	 * Wandelt einen DXF-Grundriss mit dem Namen gpName in eine dreidimensionale
	 * Darstellung um
	 * 
	 * @param gpName
	 *          der Name des Grundriss
	 * @return eine CgNode mit dem Mesh des Grundplans
	 */
	CgNode convertDXFPlanToCgNode(String gpName);
	
	/**
	 * Erstellt ein 3D-Mesh auf Basis von aufbereiteten Grundplan-Vektordaten
	 * 
	 * @param data
	 *          die aufbereiteten Vektordaten des Grundplans
	 * @return eine CgNode mit dem Mesh des Grundplans
	 */
	CgNode construct3DMeshFromData(GPDataType data);
	
	void renderAndDisplayPlan(String planName);
}
