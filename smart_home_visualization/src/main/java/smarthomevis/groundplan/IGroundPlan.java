package smarthomevis.groundplan;

import cgresearch.graphics.scenegraph.CgNode;
import smarthomevis.groundplan.data.GPDataType;

public interface IGroundPlan
{
	/**
	 * Analysiert einen DXF Grundriss unter Beruecksichtigung der zugehoerigen
	 * Konfigurationsparameter und generiert eine auf Solids basierende
	 * Darstellung zur Rueckgabe.
	 * 
	 * @param planName
	 *            der Name der darzustellenden dxf Datei und der dazu
	 *            gehoerenden Konfigurationsdatei ohne die Dateiendungen
	 * @return die dreidimensionale Darstellung in Form einer CgNode des
	 *         computergraphics Framework
	 */
	CgNode convertDXFPlanToCgNode(String planName);

	/**
	 * Analysiert einen DXF Grundriss unter Beruecksichtigung der zugehoerigen
	 * Konfigurationsparameter und generiert eine auf Solids basierende
	 * Darstellung. Diese wird dann mit der JOGL Anwendung des computergraphics
	 * Frameworks dargestellt.
	 * 
	 * @param planName
	 *            der Name der darzustellenden dxf Datei und der dazu
	 *            gehoerenden Konfigurationsdatei ohne die Dateiendungen
	 */
	void renderAndDisplayPlan(String planName);

	/**
	 * @deprecated waehrend der Entwicklung fuer Testausgaben implementiert;
	 *             sollte nicht mehr verwendet werden
	 *
	 *             Erstellt ein 3D-Mesh auf Basis von aufbereiteten
	 *             Grundplan-Vektordaten
	 * 
	 * @param data
	 *            die aufbereiteten Vektordaten des Grundplans
	 * @return eine CgNode mit dem Mesh des Grundplans
	 */
	CgNode construct3DMeshFromData(GPDataType data);

}
