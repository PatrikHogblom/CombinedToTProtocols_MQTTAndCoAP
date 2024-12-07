package com.combinediot.revisitiot.InterfaceProgram;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			URL fxmlUrl = getClass().getResource("/com/combinediot/revisitiot/mainInter.fxml");

			if (fxmlUrl == null) {
				System.out.println(getClass().getResource("com/combinediot/revisitiot/mainInter.fxml"));
				throw new IllegalStateException("Cannot find FXML file at: " + fxmlUrl);
			}
			AnchorPane root = FXMLLoader.load(fxmlUrl);
			Scene scene = new Scene(root);
			primaryStage.setTitle("FontEndMqttClient");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);

			// Handle window close event
			primaryStage.setOnCloseRequest(event -> {
				Platform.exit(); // Gracefully exit the JavaFX runtime
				System.exit(0); // Forcefully exit the entire application
			});

			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
