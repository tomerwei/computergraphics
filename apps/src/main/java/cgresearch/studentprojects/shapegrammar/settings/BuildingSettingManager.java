package cgresearch.studentprojects.shapegrammar.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The Class BuildingSettingManager managed the building settings.
 * 
 * @author Thorben Watzl
 */
public class BuildingSettingManager extends Observable {

  /** The building settings list. */
  private List<BuildingSettings> buildingSettingsList;

  /** The actual settings. */
  private BuildingSettings actualSettings;

  /** The instance. */
  private static BuildingSettingManager instance;

  /**
   * Parent directory of the buildings.
   */
  private String baseDirectory = "studentprojects/shapegrammar/buildings/";

  /**
   * Instantiates a new building setting manager.
   */
  private BuildingSettingManager() {
    buildingSettingsList = new ArrayList<BuildingSettings>();
    actualSettings = null;
  }

  /**
   * Gets the single instance of BuildingSettingManager.
   *
   * @return single instance of BuildingSettingManager
   */
  public static BuildingSettingManager getInstance() {
    if (instance == null) {
      instance = new BuildingSettingManager();
    }
    return instance;
  }

  /**
   * Creates the new building settings.
   */
  public void createNewBuildingSettings() {
    saveActualSettings();
    actualSettings = new BuildingSettings();
  }

  /**
   * Gets the actual settings.
   *
   * @return the actual settings
   */
  public BuildingSettings getActualSettings() {
    if (actualSettings == null) {
      if (buildingSettingsList.size() > 0) {
        actualSettings = buildingSettingsList.get(0);
      } else {
        actualSettings = new BuildingSettings();
      }
    }
    return actualSettings;
  }

  /**
   * Save actual settings.
   */
  private void saveActualSettings() {
    if (actualSettings != null) {
      if (buildingSettingsList.contains(actualSettings)) {
        buildingSettingsList.set(buildingSettingsList.indexOf(actualSettings),
            actualSettings);
      } else {
        buildingSettingsList.add(actualSettings);
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

  public String getBaseDirectory() {
    return baseDirectory;
  }
}
