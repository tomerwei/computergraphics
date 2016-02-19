/**
 * Prof. Philipp Jenke
 * Hochschule f�r Angewandte Wissenschaften (HAW), Hamburg
 * CG1: Educational Java OpenGL framework with scene graph.
 */

package cgresearch.graphics.fileio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import cgresearch.core.assets.CgAssetManager;
import cgresearch.core.logging.Logger;
import cgresearch.core.math.VectorMatrixFactory;

public class ObjMaterialIO {

	private ObjMaterial currentMaterial = null;
	private ObjMaterials materials = null;

	public ObjMaterials readMaterialFile(String filename) {
		materials = new ObjMaterials();

		try {
			// Use the asset manager to locate and open the OBJ file.
			InputStream is = CgAssetManager.getInstance().getInputStream(
					filename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				parseLine(strLine);
			}

			// Save last open material
			saveMaterial();

			// Close the input stream
			in.close();
		} catch (Exception e) {
			Logger.getInstance().exception("Error: ", e);
			return null;
		}

		Logger.getInstance().debug(
				"Successfully read material file " + filename + " with "
						+ materials.getNumberOfMaterials() + " materials.");
		return materials;
	}

	private void parseLine(String strLine) {
		String line = strLine.trim();
		String operator = ObjFileReader.getOperator(line);
		if (operator.compareTo("newmtl") == 0) {
			parseNewMaterial(line);
		} else if (operator.compareTo("map_Kd") == 0) {
			parseTextureFilename(line);
		} else if (operator.compareTo("map_Ka") == 0) {
			parseTextureFilename(line);
		} else if (operator.compareTo("map_Ks") == 0) {
			parseTextureFilename(line);
		} else if (operator.compareTo("Kd") == 0) {
			parseDiffuseColor(line);
		}
	}

	private void parseDiffuseColor(String strLine) {
		String line = strLine.trim();
		String[] tokens = line.split("\\s+");
		if (tokens.length >= 4 && currentMaterial != null) {
			currentMaterial.setDiffuseColor(VectorMatrixFactory.newVector(
					Double.parseDouble(tokens[1]),
					Double.parseDouble(tokens[2]),
					Double.parseDouble(tokens[3])));
		}

	}

	private void parseTextureFilename(String strLine) {
		String line = strLine.trim();
		String[] texFilenameCommand = line.split(" ");
		if (texFilenameCommand.length == 2 && currentMaterial != null) {
			currentMaterial.setTextureFilename(texFilenameCommand[1]);
		}
	}

	private void parseNewMaterial(String strLine) {
		// Start new material, save old one first
		saveMaterial();

		String line = strLine.trim();
		String[] materialNameCommand = line.split(" ");
		if (materialNameCommand.length == 2) {
			currentMaterial = new ObjMaterial(materialNameCommand[1]);
		}
	}

	private void saveMaterial() {
		// Finalize last material
		if (currentMaterial != null) {
			materials.addMaterial(currentMaterial);
			currentMaterial = null;
		}
	}
}
