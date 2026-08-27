package com.example.fire;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class settings {

    Font customFont = Font.loadFont(
            getClass().getResource("/fonts-urban-jungle-font/Urbanjungledemo-ymAm.otf").toExternalForm(),
            50
    );

    private Stage stage;
    private HelloApplication mainApp;

    public settings(Stage stage, HelloApplication mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    public Scene getScene() {

        Label label = new Label("Settings");
        label.setFont(Font.font(customFont.getFamily(), 90));
        label.setLayoutX(300);
        label.setLayoutY(80);
        label.setStyle("-fx-text-fill: #FF4500;");

        // Toggle Button
        ToggleButton soundToggle = new ToggleButton();
        soundToggle.setLayoutX(350);
        soundToggle.setLayoutY(250);
        soundToggle.setPrefWidth(200);
        soundToggle.setFont(Font.font(customFont.getFamily(), 15));

        // Media reference
        javafx.scene.media.MediaPlayer bgMusic = mainApp.getBgMusicPlayer();

        // Check app mute state
        boolean isMuted = mainApp.isMuted();
        soundToggle.setSelected(!isMuted);
        soundToggle.setText(!isMuted ? "Music: ON" : "Music: OFF");

        // Toggle action
        soundToggle.setOnAction(e -> {
            mainApp.playClickSound();

            if (soundToggle.isSelected()) {
                if (bgMusic != null) bgMusic.play();
                soundToggle.setText("Music: ON");
                mainApp.setMuted(false);
            } else {
                if (bgMusic != null) bgMusic.pause();
                soundToggle.setText("Music: OFF");
                mainApp.setMuted(true);
            }
        });

        // Volume Slider
        Slider volumeSlider = new Slider(0, 1, bgMusic != null ? bgMusic.getVolume() : 0.8);
        volumeSlider.setLayoutX(350);
        volumeSlider.setLayoutY(330);
        volumeSlider.setPrefWidth(200);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setStyle(
                "-fx-tick-label-fill: white; " +
                        "-fx-control-inner-background: #FF4500;"
        );

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (bgMusic != null) {
                bgMusic.setVolume(newVal.doubleValue());
            }
        });

        // Back Button
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setLayoutX(350);
        backBtn.setLayoutY(450);
        backBtn.setPrefWidth(200);
        backBtn.setFont(Font.font(customFont.getFamily(), 12));
        String defaultStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px;";

        backBtn.setStyle(defaultStyle);

        DropShadow buttonGlow = new DropShadow();
        buttonGlow.setColor(Color.web("#FF4500"));
        buttonGlow.setRadius(20);
        buttonGlow.setSpread(0.5);

        backBtn.setEffect(buttonGlow);

        backBtn.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px;"
        );

        backBtn.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color:black");
        pane.getChildren().addAll(label, soundToggle, volumeSlider, backBtn);

        return new Scene(pane, 900, 700);
    }
}