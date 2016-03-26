/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cnc;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Control a CNC mill.
 * 
 * @author Philipp Jenke
 * 
 */
public class CncMillFrame extends Application {

  private CncMill cncMill = new CncMill();

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("CNC Mill!");

    VBox root = new VBox();

    CheckBox enable = new CheckBox("enable");
    enable.setOnAction(event -> {
      if (enable.isSelected()) {
        cncMill.enable();
      } else {
        cncMill.disable();
      }
    });
    root.getChildren().add(enable);

    BorderPane control = new BorderPane();
    root.getChildren().add(control);

    ToggleButton xLeft = new ToggleButton("<-");
    control.setLeft(xLeft);
    BorderPane.setAlignment(xLeft, Pos.CENTER);
    xLeft.setOnAction(event -> {
      cncMill.moveX(100);
    });

    ToggleButton xRight = new ToggleButton("->");
    control.setRight(xRight);
    BorderPane.setAlignment(xRight, Pos.CENTER);
    xRight.setOnAction(event -> {
      cncMill.moveX(-100);
    });

    ToggleButton stop = new ToggleButton("Stop!");
    control.setCenter(stop);
    BorderPane.setAlignment(stop, Pos.CENTER);
    stop.setOnAction(event -> {
      cncMill.stop();
    });

    ToggleButton yUp = new ToggleButton("^");
    control.setTop(yUp);
    BorderPane.setAlignment(yUp, Pos.CENTER);
    yUp.setOnAction(event -> {
      cncMill.moveY(-100);
    });

    ToggleButton yDown = new ToggleButton("v");
    control.setBottom(yDown);
    BorderPane.setAlignment(yDown, Pos.CENTER);
    yDown.setOnAction(event -> {
      cncMill.moveY(100);
    });

    primaryStage.setScene(new Scene(root, 200, 200));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
