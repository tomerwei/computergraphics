package cgresearch.studentprojects.shapegrammar.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The Class CitySettingManager managed the city settings
 * @author Thorben Watzl
 */
public class CitySettingManager extends Observable{

	/** The city settings list. */
	private List<CitySettings> citySettingsList;
	

	/** The actual settings. */
	private CitySettings actualSettings;
	

	/** The instance. */
	private static CitySettingManager instance;

	/**
	 * Instantiates a new city setting manager.
	 */
	private CitySettingManager(){
		citySettingsList = new ArrayList<CitySettings>();
		actualSettings = null;
	}

	/**
	 * Gets the single instance of CitySettingManager.
	 *
	 * @return single instance of CitySettingManager
	 */
	public static CitySettingManager getInstance() {
        if (instance == null) {
            instance = new CitySettingManager();
        }
        return instance;
    }
	

	/**
	 * Creates the new city settings.
	 */
	public void createNewCitySettings(){
		saveActualSettings();
		actualSettings = new CitySettings();
	}
	

	/**
	 * Gets the actual settings.
	 *
	 * @return the actual settings
	 */
	public CitySettings getActualSettings(){
		if(actualSettings == null){
			if(citySettingsList.size() > 0){
				actualSettings = citySettingsList.get(0);
			}else{
				actualSettings = new CitySettings();
			}
		}
		return actualSettings;
	}
	
	/**
	 * Save actual settings.
	 */
	private void saveActualSettings(){
		if(actualSettings != null){
			if(citySettingsList.contains(actualSettings)){
				citySettingsList.set(citySettingsList.indexOf(actualSettings), actualSettings);
			}else{
				citySettingsList.add(actualSettings);
			}
		}		
	}

	/**
	 * Notify changes.
	 */
	public void notifyChanges() {
		notifyObservers();
	}

	/**
	 * Start changes.
	 */
	public void startChanges() {
		setChanged();
	}
}
