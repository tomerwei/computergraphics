package homeautomation.apps.todo;

import homeautomation.data.DoubleDataset;
import homeautomation.gui.DoubleDatasetChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TestTemperatureGui extends Application {

  private TestTemperature testTemperature = new TestTemperature();

  private List<DoubleDatasetChart> charts = new ArrayList<DoubleDatasetChart>();

  /**
   * Root node of the scene graph.
   */
  private FlowPane root = new FlowPane();

  @Override
  public void start(Stage primaryStage) throws Exception {
    testTemperature.setupSensors();
    testTemperature.startMeasurements();
    for (int i = 0; i < testTemperature.getNumberOfDatasets(); i++) {
      DoubleDataset dataset = testTemperature.getDataset(i);
      DoubleDatasetChart chart = new DoubleDatasetChart(dataset);
      charts.add(chart);
      root.getChildren().add(chart);
    }

    primaryStage.setTitle("Temperature Test");
    Scene scene = new Scene(root, 400, 400);
    primaryStage.setScene(scene);
    primaryStage.show();

    scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
      public void handle(WindowEvent ev) {
        System.out.println("Stopped timer - ended application.");
        System.exit(0);
      }
    });

    // Chart draw update
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        for (DoubleDatasetChart chart : charts) {
          chart.drawChart();
        }
      }
    }, 1000, 1000);

  }

  /**
   * Program entry point.
   * 
   * @param args
   *          Command line parameters.
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

}
