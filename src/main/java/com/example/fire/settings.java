package com.example.fire;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        label.setStyle("-fx-text-fill: #FF4500;");

        // Toggle Button
        ToggleButton soundToggle = new ToggleButton();
        String toggleStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-cursor: hand;";
        soundToggle.setStyle(toggleStyle);

        // Media reference
        javafx.scene.media.MediaPlayer bgMusic = mainApp.getBgMusicPlayer();

        // Check app mute state
        boolean isMuted = mainApp.isMuted();
        soundToggle.setSelected(!isMuted);
        soundToggle.setText(!isMuted ? "Music: ON" : "Music: OFF");


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


        Button backBtn = new Button("Back to Main Menu");
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

        backBtn.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane pan = new Pane();
        pan.setStyle("-fx-background-color:black");


        label.layoutXProperty().bind(pan.widthProperty().multiply(300.0 / 900.0));
        label.layoutYProperty().bind(pan.heightProperty().multiply(80.0 / 700.0)); // Fixed: changed to heightProperty


        soundToggle.layoutXProperty().bind(pan.widthProperty().multiply(350.0 / 900.0));
        soundToggle.layoutYProperty().bind(pan.heightProperty().multiply(250.0 / 700.0));
        soundToggle.prefWidthProperty().bind(pan.widthProperty().multiply(200.0 / 900.0));

        volumeSlider.layoutXProperty().bind(pan.widthProperty().multiply(350.0 / 900.0));
        volumeSlider.layoutYProperty().bind(pan.heightProperty().multiply(330.0 / 700.0)); // Fixed: layoutYProperty instead of second X layout
        volumeSlider.prefWidthProperty().bind(pan.widthProperty().multiply(200.0 / 900.0));

        backBtn.layoutXProperty().bind(pan.widthProperty().multiply(350.0 / 900.0));
        backBtn.layoutYProperty().bind(pan.heightProperty().multiply(450.0 / 700.0)); // Fixed: changed to heightProperty
        backBtn.prefWidthProperty().bind(pan.widthProperty().multiply(200.0 / 900.0));


        pan.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                double scaleFactor = newVal.doubleValue() / 900.0;


                label.setFont(Font.font(customFont.getFamily(), 90 * scaleFactor));

                Font scaledBtnFont = Font.font(customFont.getFamily(), 15 * scaleFactor);
                soundToggle.setFont(scaledBtnFont);

                Font scaledBackBtnFont = Font.font(customFont.getFamily(), 12 * scaleFactor);
                backBtn.setFont(scaledBackBtnFont);

                System.out.println("widthProperty changed from " + oldVal.doubleValue() + " to " + newVal.doubleValue());
            }
        });

        pan.getChildren().addAll(label, soundToggle, volumeSlider, backBtn);

        return new Scene(pan, 900, 700);
    }
}
