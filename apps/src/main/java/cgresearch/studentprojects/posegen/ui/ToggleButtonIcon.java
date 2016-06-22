package cgresearch.studentprojects.posegen.ui;

import javax.swing.ImageIcon;

import cgresearch.graphics.scenegraph.IconLoader;

public class ToggleButtonIcon {
	private final String ICON_NOTACTIVE_PATH;
	private final String ICON_ACTIVE_PATH;
	private boolean isActive = false;

	ImageIcon iconNotActive;
	ImageIcon iconActive;

	public ToggleButtonIcon(String iconNotActivePath, String iconActivePath) {
		this.ICON_NOTACTIVE_PATH = iconNotActivePath;
		this.ICON_ACTIVE_PATH = iconActivePath;
		iconNotActive = IconLoader.getIcon(this.getICON_NOTACTIVE_PATH());
		iconActive = IconLoader.getIcon(this.getICON_ACTIVE_PATH());
	}

	public void clicked() {
		toggleActive();
	}

	public void toggleActive() {
		isActive = !isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	private String getICON_NOTACTIVE_PATH() {
		return ICON_NOTACTIVE_PATH;
	}

	private String getICON_ACTIVE_PATH() {
		return ICON_ACTIVE_PATH;
	}

	public ImageIcon getCurrentIcon() {
		if (isActive) {
			return iconActive;
			
		} else {
			return iconNotActive;
		}
	}
}
