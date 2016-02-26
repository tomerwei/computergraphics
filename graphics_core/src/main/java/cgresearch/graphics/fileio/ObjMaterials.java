/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.fileio;

import java.util.ArrayList;
import java.util.List;

public class ObjMaterials {

	private List<ObjMaterial> materials = new ArrayList<ObjMaterial>();

	public ObjMaterials() {
	}

	public ObjMaterial getMaterial(String materialName) {
		for (int i = 0; i < materials.size(); i++) {
			if (materials.get(i).getName().compareTo(materialName) == 0) {
				return materials.get(i);
			}
		}
		return null;
	}

	/**
	 * Returns the texture filename for a given material. Returns empty string
	 * if the material does not exist.
	 */
	public String getTextureFilename(String materialName) {

		ObjMaterial material = getMaterial(materialName);
		if (material != null) {
			return material.getTextureFilename();
		}
		return "";
	}

	public void clear() {
		materials.clear();
	}

	public int getNumberOfMaterials() {
		return materials.size();
	}

	public void addMaterial(final ObjMaterial material) {
		materials.add(material);
	}
}
