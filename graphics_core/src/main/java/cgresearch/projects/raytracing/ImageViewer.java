package cgresearch.projects.raytracing;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Simple viewer for images
 */
public class ImageViewer extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID =
      8140257581540101857L;

  /**
   * Constructor.
   */
  public ImageViewer(Image image) {
    JLabel label = new JLabel();
    label.setIcon(new ImageIcon(image));
    getContentPane().add(label);

    setLocation(100, 300);
    setSize(image.getWidth(null), image.getHeight(null));
    setVisible(true);
  }

}
