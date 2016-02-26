package homeautomation.gui;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import homeautomation.data.DoubleDataset;

public class DoubleDatasetChart extends Canvas implements Observer {

  /**
   * Dataset to be displayed.
   */
  private final DoubleDataset dataset;

  /**
   * (Copy) Constructor.
   * 
   * @param dataset
   *          Copy instance.
   */
  public DoubleDatasetChart(DoubleDataset dataset) {
    super(200, 200);
    this.dataset = dataset;
    dataset.addObserver(this);

    addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        drawChart();
      }
    });
  }

  /**
   * Draw chart.
   */
  public void drawChart() {

    if (dataset.getNumberOfItems() < 2) {
      return;
    }

    final double borderX = 10;
    final double borderY = 10;
    final double maxValue = dataset.getMaxValue();
    final double minValue = dataset.getMinValue();
    final double scaleX =
        (getWidth() - 2 * borderX) / (dataset.getNumberOfItems() - 1);
    final double scaleY = (getHeight() - 2 * borderY) / (maxValue - minValue);

    GraphicsContext gc = getGraphicsContext2D();
    gc.clearRect(0, 0, getWidth(), getHeight());
    gc.setStroke(Color.BLUE);
    gc.setLineWidth(2);

    for (int i = 1; i < dataset.getNumberOfItems(); i++) {
      double x0 = borderX + (i - 1) * scaleX;
      double x1 = borderX + i * scaleX;
      double y0 =
          getHeight() - borderY
              - (dataset.getItem(i - 1).getValue() - minValue) * scaleY;
      double y1 =
          getHeight() - borderY - (dataset.getItem(i).getValue() - minValue)
              * scaleY;
      getGraphicsContext2D().strokeLine(x0, y0, x1, y1);
    }
  }

  @Override
  public void update(Observable observable, Object arg) {

    if (dataset.getNumberOfItems() > 1) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          // drawChart();
        }
      });
    }
  }
}
