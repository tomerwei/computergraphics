package cgresearch.studentprojects.shapegrammar.visualize;

import java.util.Observable;
import java.util.Observer;

import cgresearch.studentprojects.shapegrammar.generator.CityGenerator;
import cgresearch.studentprojects.shapegrammar.settings.CitySettingManager;

public class CityVisualizer implements Observer{

	
	public void update(Observable o, Object arg) {
		CityGenerator cityGenerator = new CityGenerator();
		cityGenerator.generateCity();
	}

	public void start() {
		CitySettingManager.getInstance().addObserver(this);
	}

}
