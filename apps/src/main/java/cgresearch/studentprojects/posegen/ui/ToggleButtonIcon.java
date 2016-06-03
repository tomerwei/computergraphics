package cgresearch.studentprojects.posegen.ui;

public class ToggleButtonIcon {
	private final String ICON_NOTACTIVE_PATH;
	private final String ICON_ACTIVE_PATH;
	private boolean isActive = false;
	
	public ToggleButtonIcon(String iconNotActivePath, String iconActivePath) {
		this.ICON_NOTACTIVE_PATH = iconNotActivePath;
		this.ICON_ACTIVE_PATH = iconActivePath;
	}

	public void clicked() {
		toggleActive();
	}
	
	public void toggleActive(){
		isActive = !isActive;
	}
	
	public boolean isActive(){
		return isActive;
	}

	public String getICON_NOTACTIVE_PATH() {
		return ICON_NOTACTIVE_PATH;
	}

	public String getICON_ACTIVE_PATH() {
		return ICON_ACTIVE_PATH;
	}
	
	
}
