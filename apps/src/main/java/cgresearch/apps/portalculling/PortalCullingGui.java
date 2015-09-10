package cgresearch.apps.portalculling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;

import cgresearch.ui.IApplicationControllerGui;

public class PortalCullingGui extends IApplicationControllerGui implements
		ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6129236805059357309L;
	/**
	 * Constants
	 */
	private static final String ACTION_COMMAND_SHOW_ALL_WALLS = "ACTION_COMMAND_SHOW_ALL_WALLS";
	private static final String ACTION_COMMAND_SHOW_PVS_WALLS = "ACTION_COMMAND_SHOW_PVS_WALLS";
	private static final String ACTION_COMMAND_SHOW_VIEW_VOLUME = "ACTION_COMMAND_SHOW_VIEW_VOLUME";
	private static final String ACTION_COMMAND_SHOW_CELLS = "ACTION_COMMAND_SHOW_CELLS";
	private static final String ACTION_COMMAND_SHOW_PVS = "ACTION_COMMAND_SHOW_PVS";

	/**
	 * Settings object
	 */
	private final PortalCullingSettings settings;

	/**
	 * GUI components
	 */
	private JCheckBox checkBoxShowAllWalls;
	private JCheckBox checkBoxShowPvsWalls;
	private JCheckBox checkBoxShowViewVolume;
	private JCheckBox checkBoxShowCells;
	private JCheckBox checkBoxShowPvs;

	/**
	 * Constructor.
	 */
	public PortalCullingGui(PortalCullingSettings settings) {
		this.settings = settings;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		checkBoxShowAllWalls = new JCheckBox("Show all walls");
		checkBoxShowAllWalls.addActionListener(this);
		checkBoxShowAllWalls.setActionCommand(ACTION_COMMAND_SHOW_ALL_WALLS);
		checkBoxShowAllWalls.setSelected(settings.showAllWalls);
		add(checkBoxShowAllWalls);

		checkBoxShowPvsWalls = new JCheckBox("Show PVS walls");
		checkBoxShowPvsWalls.addActionListener(this);
		checkBoxShowPvsWalls.setActionCommand(ACTION_COMMAND_SHOW_PVS_WALLS);
		checkBoxShowPvsWalls.setSelected(settings.showPvsWalls);
		add(checkBoxShowPvsWalls);

		checkBoxShowViewVolume = new JCheckBox("Show view volume");
		checkBoxShowViewVolume.addActionListener(this);
		checkBoxShowViewVolume
				.setActionCommand(ACTION_COMMAND_SHOW_VIEW_VOLUME);
		checkBoxShowViewVolume.setSelected(settings.showViewVolume);
		add(checkBoxShowViewVolume);

		checkBoxShowCells = new JCheckBox("Show cells");
		checkBoxShowCells.addActionListener(this);
		checkBoxShowCells.setActionCommand(ACTION_COMMAND_SHOW_CELLS);
		checkBoxShowCells.setSelected(settings.showCells);
		add(checkBoxShowCells);

		checkBoxShowPvs = new JCheckBox("Show PVS");
		checkBoxShowPvs.addActionListener(this);
		checkBoxShowPvs.setActionCommand(ACTION_COMMAND_SHOW_PVS);
		checkBoxShowPvs.setSelected(settings.showPvs);
		add(checkBoxShowPvs);
	}

	@Override
	public String getName() {
		return "Portal Culling";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ACTION_COMMAND_SHOW_ALL_WALLS)) {
			settings.showAllWalls = checkBoxShowAllWalls.isSelected();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SHOW_PVS_WALLS)) {
			settings.showPvsWalls = checkBoxShowPvsWalls.isSelected();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SHOW_VIEW_VOLUME)) {
			settings.showViewVolume = checkBoxShowViewVolume.isSelected();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SHOW_CELLS)) {
			settings.showCells = checkBoxShowCells.isSelected();
		} else if (e.getActionCommand().equals(ACTION_COMMAND_SHOW_PVS)) {
			settings.showPvs = checkBoxShowPvs.isSelected();
		}
	}

}
