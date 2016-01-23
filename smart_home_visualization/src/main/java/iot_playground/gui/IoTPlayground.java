package iot_playground.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IoTPlayground extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("IoT Playground");
    StackPane root = new StackPane();
    primaryStage.setScene(new Scene(root, 400, 300));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch();
  }

}
