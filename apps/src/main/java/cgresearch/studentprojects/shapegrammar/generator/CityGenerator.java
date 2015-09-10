package cgresearch.studentprojects.shapegrammar.generator;

import java.util.List;
import java.util.Random;

import cgresearch.studentprojects.shapegrammar.settings.BuildingSettingManager;
import cgresearch.studentprojects.shapegrammar.settings.CitySettingManager;
import cgresearch.studentprojects.shapegrammar.settings.CitySettings;

public class CityGenerator {

	public void generateCity() {
		BuildingGenerator buildingGenerator = new BuildingGenerator();
		CitySettings citySettings = CitySettingManager.getInstance().getActualSettings();
		Random random = new Random();
		double xPos = 0;
		double zPos = 0;
		double step = 2;
		List<String> rules = citySettings.getBuildingRulesDir();
		double minWidth = citySettings.getMinWidth();
		double maxWidth = citySettings.getMaxWidth();
		double minHeight = citySettings.getMinHeight();
		double maxHeight = citySettings.getMaxHeight();
		double minLendth = citySettings.getMinLength();
		double maxLendth = citySettings.getMaxLength();
		for(int xCounter = 0;xCounter < citySettings.getNumberBuildingsXInput();xCounter++){
			for(int zCount = 0; zCount < citySettings.getNumberBuildingsZInput();zCount++){
				int randNumberForRule = random.nextInt(rules.size());
				double randNumberWidth = minWidth + (maxWidth - minWidth) * random.nextDouble();
				double randNumberHeight = minHeight + (maxHeight - minHeight) * random.nextDouble();
				double randNumberLendth = minLendth + (maxLendth - minLendth) * random.nextDouble();
				String rule = rules.get(randNumberForRule);
				buildingGenerator.generateBuilding(rule, randNumberWidth, randNumberHeight, randNumberLendth, xPos, zPos);
				zPos += step + BuildingSettingManager.getInstance().getActualSettings().getLength();
			}
			zPos = 0;
			xPos += step + BuildingSettingManager.getInstance().getActualSettings().getWidth();
		}
	}
}
