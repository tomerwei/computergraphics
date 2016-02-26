package cgresearch.graphics.bricks;

import cgresearch.ui.menu.CgApplicationMenu;

public interface IUserInterface {
  /**
   * Register an additional menu in the menu bar. This can be done by all
   * implementing subclasses/applications.
   */
  public void registerApplicationMenu(CgApplicationMenu cgApplicationMenu);
}
