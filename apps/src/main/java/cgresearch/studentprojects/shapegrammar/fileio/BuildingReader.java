package cgresearch.studentprojects.shapegrammar.fileio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cgresearch.core.assets.ResourcesLocator;

/**
 * The Class BuildingReader read all directories from the buildings directory.
 * @author Thorben Watzl
 */
public class BuildingReader {
	
	/**
	 * Read buildings.
	 *
	 * @return the list of all directories in the buildings directory
	 */
	public List<String> readBuildings(){
		String path = ResourcesLocator.getInstance().getPathToResource("studentprojects/shapegrammar/buildings");
		System.out.println(path);
		File root = new File(path);
		List<String> result = new ArrayList<String>();
		if(root != null){
			for (File file : root.listFiles()) {
				if (file.isDirectory()) {
					if (!file.getName().equals("basic rules")) {
						result.add(file.getName());
					}
				}
			}
		}
		return result;
	}
}
