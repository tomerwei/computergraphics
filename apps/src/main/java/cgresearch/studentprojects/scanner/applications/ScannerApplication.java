package cgresearch.studentprojects.scanner.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cgresearch.JoglAppLauncher;
import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.points.IPointCloud;
import cgresearch.graphics.datastructures.points.PointCloud;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.graphics.scenegraph.CoordinateSystem;
import cgresearch.studentprojects.scanner.calibration.CalibrationGui;
import cgresearch.studentprojects.scanner.gui.ScannerGui;
import cgresearch.studentprojects.scanner.gui.ScannerInformationGui;
import cgresearch.studentprojects.scanner.scanner.Scanner;

public class ScannerApplication extends CgApplication implements ActionListener {

  /*
   * The main scanner class to cordinate the motors and the distance sensor
   */
  private Scanner scanner;

  /*
   * The UI for controlling the scanner
   */
  private ScannerGui scannerGui;

  /*
   * The default constructor of the Scanner Application
   */
  public ScannerApplication() {

    // Create invisible coordinate System
    getCgRootNode().addChild(new CoordinateSystem());
    getCgRootNode().getChildNode(0).setVisible(false);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_INIT)) {
      scanner = Scanner.getInstance(scannerGui.getPort(), scannerGui.getPointsPerLine(), scannerGui.getAngle());
      scanner.initStartPosition();
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_TEST)) {
      scanner.fireTest();
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_LASER_ON)) {
      scanner.laserOn();
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_LASER_OFF)) {
      scanner.laserOff();
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_SAVE_FILE)) {
      scanner.saveLastPointCloud(scannerGui.getSaveFilename());
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_LOAD_FILE)) {
      scanner.loadLastPointCloud(getCgRootNode(), scannerGui.getLoadFilename());
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_DEBUG_ANGLE)) {
      scanner.rotateAngle(scannerGui.getDebugAngle());
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_DEBUG_HEIGHT)) {
      scanner.moveHeight(scannerGui.getDebugHeight());
    } else if (e.getActionCommand().equals(ScannerGui.ACTION_COMMAND_SCAN)) {
      new Thread(new Runnable() {

        @Override
        public void run() {
          IPointCloud pointCloud = new PointCloud();
          // Point zero = new Point(new Vector3(0, 0, 0));
          // zero.setColor(new Vector3(1f, 0f, 0f));
          // pointCloud.addPoint(zero);
          getCgRootNode().addChild(new CgNode(pointCloud, "Scan"));
          scanner.startScan(pointCloud, scannerGui.getPointsPerLine(), scannerGui.getAngle());
        }
      }).start();
    }
  }

  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    ScannerApplication app = new ScannerApplication();
    ScannerGui scannerGui = new ScannerGui(app);
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(scannerGui);
    ScannerInformationGui tinkerforgeGui = new ScannerInformationGui();
    appLauncher.addCustomUi(tinkerforgeGui);
    CalibrationGui calibrationGui = new CalibrationGui();
    appLauncher.addCustomUi(calibrationGui);
  }
}
