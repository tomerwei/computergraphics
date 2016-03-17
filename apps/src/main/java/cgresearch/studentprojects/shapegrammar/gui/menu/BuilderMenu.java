package cgresearch.studentprojects.shapegrammar.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import cgresearch.ui.menu.CgApplicationMenu;
import cgresearch.graphics.scenegraph.CgRootNode;
import cgresearch.studentprojects.shapegrammar.gui.frame.NewBuildingFrame;
import cgresearch.studentprojects.shapegrammar.gui.frame.NewCityFrame;

/**
 * The Class BuilderMenu is the menu for the building part.
 * 
 * @author Thorben Watzl
 */
public class BuilderMenu extends CgApplicationMenu implements ActionListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -792743526055210858L;

	/**
	 * Action Commands.
	 */
	public static final String ACTION_COMMAND_NEWBUILDING = "ACTION_COMMAND_NEWBUILDING";
	public static final String ACTION_COMMAND_NEWCITY = "ACTION_COMMAND_NEWCITY";

	private final CgRootNode rootNode;

	/**
	 * Instantiates a new builder menu.
	 */
	public BuilderMenu(CgRootNode rootNode) {
		super("Builder");
		this.rootNode = rootNode;
		JMenuItem itemNewBuilding = new JMenuItem("New Building");
		itemNewBuilding.addActionListener(this);
		itemNewBuilding.setActionCommand(ACTION_COMMAND_NEWBUILDING);
		JMenuItem itemNewCity = new JMenuItem("New City");
		itemNewCity.addActionListener(this);
		itemNewCity.setActionCommand(ACTION_COMMAND_NEWCITY);
		add(itemNewBuilding);
		add(itemNewCity);

	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(BuilderMenu.ACTION_COMMAND_NEWBUILDING)) {
			NewBuildingFrame newFrame = new NewBuildingFrame(rootNode);
			newFrame.setVisible(true);
		} else if (e.getActionCommand().equals(BuilderMenu.ACTION_COMMAND_NEWCITY)) {
			NewCityFrame cityFrame = new NewCityFrame();
			cityFrame.setVisible(true);
		}
	}
}
