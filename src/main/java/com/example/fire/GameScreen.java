package com.example.fire;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameScreen {

    Font customFont = Font.loadFont(
            getClass().getResource("/fonts-urban-jungle-font/Urbanjungledemo-ymAm.otf").toExternalForm(),
            50
    );

    private Stage stage;
    private HelloApplication mainApp;

    public GameScreen(Stage stage, HelloApplication mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    public Scene getScene(){

        String defaultStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-cursor: hand;";


        Label scoreLabel = new Label("Score: ");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        scoreLabel.setLayoutX(30);
        scoreLabel.setLayoutY(20);


        Label healthLabel = new Label("Health: 200HP");
        healthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        healthLabel.setLayoutX(180);
        healthLabel.setLayoutY(20);


        Label targetsLabel = new Label("Targets: 25");
        targetsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        targetsLabel.setLayoutX(400);
        targetsLabel.setLayoutY(20);


        Button pauseBtn = new Button("Pause");
        pauseBtn.setLayoutX(660);
        pauseBtn.setLayoutY(15);
        pauseBtn.setFont(Font.font(customFont.getFamily(), 12));
        pauseBtn.setStyle(defaultStyle);


        Button lazy = new Button("Give-UP");
        lazy.setLayoutX(770);
        lazy.setLayoutY(15);
        lazy.setFont(Font.font(customFont.getFamily(), 12));
        lazy.setStyle(defaultStyle);

        lazy.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: Black");
        pane.getChildren().addAll(scoreLabel, healthLabel, targetsLabel, pauseBtn, lazy);

        return new Scene(pane, 900, 700);
    }
}