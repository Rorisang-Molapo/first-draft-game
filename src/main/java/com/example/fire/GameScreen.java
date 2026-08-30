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

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        scoreLabel.setLayoutX(20);
        scoreLabel.setLayoutY(20);

        Button playBtn = new Button("Give-UP");
        playBtn.setLayoutX(800);
        playBtn.setLayoutY(20);
        playBtn.setFont(Font.font(customFont.getFamily(), 12));
        String defaultStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-cursor: hand;";
        playBtn.setStyle(defaultStyle);

        playBtn.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: Black");
        pane.getChildren().addAll(playBtn, scoreLabel);

        return new Scene(pane, 900, 700);
    }
}