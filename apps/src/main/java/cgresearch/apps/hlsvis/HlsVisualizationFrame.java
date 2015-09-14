/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.apps.hlsvis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import cgresearch.AppLauncher.RenderSystem;
import cgresearch.AppLauncher.UI;
import cgresearch.apps.hlsvis.Package.Vehicle;
import cgresearch.apps.hlsvis.hls.HlsConstants;
import cgresearch.apps.hlsvis.hls.HlsSimulator;
import cgresearch.apps.hlsvis.hls.TransportOrder;
import cgresearch.apps.hlsvis.rabbitmq.IMessageCallback;
import cgresearch.apps.hlsvis.rabbitmq.RabbitMqCommunication;
import cgresearch.core.assets.ResourcesLocator;
import cgresearch.core.logging.Logger;
import cgresearch.graphics.bricks.CgApplication;
import cgresearch.graphics.datastructures.trianglemesh.ITriangleMesh;
import cgresearch.graphics.material.CgTexture;
import cgresearch.graphics.material.ResourceManager;
import cgresearch.graphics.misc.AnimationTimer;
import cgresearch.graphics.scenegraph.CgNode;
import cgresearch.rendering.jogl.JoglAppLauncher;

/**
 * Central frame for the mesh exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class HlsVisualizationFrame extends CgApplication implements IMessageCallback {

  /**
   * Container for all movables in the scene
   */
  private List<Movable> movables;

  /**
   * Current simulation date
   */
  private Date currentDate;

  /**
   * Height field
   */
  private HeightField heightField;

  /**
   * Diplay current time.
   */
  private TimeDisplay timeDisplay;

  /**
   * Cached movables (will be reused)
   */
  private List<Package> unusedMovables = new ArrayList<Package>();

  /**
   * Simulator für das HLS-System.
   */
  private HlsSimulator hlsSimulator;

  /**
   * RabbitMQ queue für die Frachtaufträge
   */
  private RabbitMqCommunication frachtAuftragsQueue;

  /**
   * Queue for the package events
   */
  private RabbitMqCommunication packageMessageQueue;

  /**
   * Queue for the connections
   */
  private RabbitMqCommunication connectionsQueue;

  /**
   * Queue for the orders which have not yet started.
   */
  private List<TransportOrder> orderQueue;

  /**
   * Constructor.
   */
  public HlsVisualizationFrame() {
    // Initialize date
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      currentDate = formatter.parse("2014-12-08 00:00:00");
    } catch (ParseException e) {
      Logger.getInstance().error("Failed to parse date.");
    }

    hlsSimulator = new HlsSimulator();

    // Initialize queue for unstarted orders
    orderQueue = new ArrayList<TransportOrder>();

    timeDisplay = new TimeDisplay();

    heightField =
        new HeightField(ResourcesLocator.getInstance().getPathToResource("logisticsSim/hoehenkarte_deutschland.png"),
            ResourcesLocator.getInstance().getPathToResource("logisticsSim/karte_deutschland.jpg"), 0.1);
    ITriangleMesh mesh = heightField.createHeightFieldMesh(256, 256);
    ResourceManager.getTextureManagerInstance().addResource(Movable.DHL_TEXTURE_ID,
        new CgTexture(ResourcesLocator.getInstance().getPathToResource("logisticsSim/dhl_logo.png")));

    if (mesh == null) {
      return;
    }
    CgNode heightFieldNode = new CgNode(mesh, "height field");
    getCgRootNode().addChild(heightFieldNode);

    // Create some content
    movables = new ArrayList<Movable>();

    AnimationTimer timer = AnimationTimer.getInstance();
    timer.addObserver(this);
    timer.startTimer(100);
    timer.setMaxValue(4000);

    // RabbitMQ settings
    String user = "hls";
    String password = "hls";
    String localhost = "127.0.0.1";

    // Erzeuge Queue für die Frachtaufträge
    frachtAuftragsQueue = new RabbitMqCommunication(HlsConstants.FRACHTAUFTRAG_QUEUE, localhost, user, password);
    frachtAuftragsQueue.registerMessageReceiver(this);
    frachtAuftragsQueue.connect();
    frachtAuftragsQueue.waitForMessages();

    packageMessageQueue = new RabbitMqCommunication(HlsConstants.SENDUNGSEREIGNIS_QUEUE, localhost, user, password);
    packageMessageQueue.connect();

    // Initially send connections
    connectionsQueue = new RabbitMqCommunication(HlsConstants.TRANSPORZBEZIEHUNGEN_QUEUE, localhost, user, password);
    connectionsQueue.connect();
    connectionsQueue.sendMessage(hlsSimulator.getConnections().toJson());
    connectionsQueue.disconnect();

  }

  @Override
  public void update(Observable o, Object argObservable) {
    super.update(o, argObservable);
    if (o instanceof AnimationTimer && currentDate != null) {

      // Update time
      int fiveMinutes = HlsConstants.MINUTES_PER_TICK * 60 * 1000;
      currentDate.setTime(currentDate.getTime() + fiveMinutes);

      // Display current time
      timeDisplay.setTime(currentDate);

      // Heartbeat for HLS simulator
      if (hlsSimulator != null) {
        hlsSimulator.tick(currentDate);
      }

      // Generate orders if required
      for (int i = 0; i < orderQueue.size(); i++) {
        TransportOrder order = orderQueue.get(i);
        if (order.getStartTime().getTime() <= currentDate.getTime()) {
          Package sendung = getNewPackage();
          sendung.init(order, heightField, packageMessageQueue);
          movables.add(sendung);
          orderQueue.remove(order);
        }
      }

      // Heartbeat for all packages
      for (int i = 0; i < movables.size(); i++) {
        Movable movable = movables.get(i);
        if (movable.destinationReached()) {
          movable.setVisible(false);
          unusedMovables.add((Package) movable);
          movables.remove(i);
          i--;
        } else {
          movable.tick(currentDate);
        }
      }
    }
  }

  @Override
  public void messageReceived(String nachricht) {
    TransportOrder order = new TransportOrder();
    order.fromJson(nachricht);
    Logger.getInstance().debug("Order received: " + order.getDeliveryNumber());
    if (order.getStartTime().getTime() > currentDate.getTime()) {
      Logger.getInstance().debug("Order " + order + " received and enqueued.");
      orderQueue.add(order);
    }
  }

  /**
   * Package recycling: packages which reached its goal are set to invisible and
   * reused later.
   */
  public Package getNewPackage() {
    if (unusedMovables.size() > 0) {
      Package p = unusedMovables.get(0);
      unusedMovables.remove(0);
      p.setVisible(true);
      return p;
    } else {
      Package p = new Package((Math.random() < 1.0 / 3.0) ? Vehicle.AIRPLANE : Vehicle.TRUCK);
      getCgRootNode().addChild(p);
      return p;
    }
  }

  /**
   * Getter.
   */
  private TimeDisplay getTimeDisplay() {
    return timeDisplay;
  }

  /**
   * Program entry point.
   */
  public static void main(String[] args) {
    ResourcesLocator.getInstance().parseIniFile("resources.ini");
    HlsVisualizationFrame app = new HlsVisualizationFrame();
    JoglAppLauncher appLauncher = JoglAppLauncher.getInstance();
    appLauncher.create(app);
    appLauncher.setRenderSystem(RenderSystem.JOGL);
    appLauncher.setUiSystem(UI.JOGL_SWING);
    appLauncher.addCustomUi(new HlsVIsGui(app.getTimeDisplay()));
  }
}