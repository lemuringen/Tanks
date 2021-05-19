package com.utriainen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TankApp extends Application {
    public static double GRAVITY_CONSTANT = 0.5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.<Parent>load(getClass().getResource("/fxml/MainView.fxml"));
        Scene scene = new Scene(root);

        KeyPolling.getInstance().pollScene(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("EdenCoding - SimpleSpaceShooter");

        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/landscape.png")));

        primaryStage.setScene(scene);
        primaryStage.show();

    }


}