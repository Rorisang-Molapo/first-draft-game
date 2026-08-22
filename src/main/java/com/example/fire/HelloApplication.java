package com.example.fire;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    private AudioClip clickSound;
    private MediaPlayer bgMusicPlayer;

    @Override
    public void start(Stage stage) throws IOException {

       //Background Music
try {
        String musicPath = getClass().getResource("/audio/KORDHELL - MURDER IN MY MIND.mp3").toExternalForm();
        Media media = new Media(musicPath);
        bgMusicPlayer = new MediaPlayer(media);
        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgMusicPlayer.setVolume(0.3);
        bgMusicPlayer.play();
        } catch (Exception e) {
        System.err.println("Failed to load background music: " + e.getMessage());
        }

        // Click Sound Effect
        try {
        String soundPath = getClass().getResource("/audio/click.mp3").toExternalForm();
        clickSound = new AudioClip(soundPath);
        clickSound.setVolume(0.6);
        } catch (Exception e) {
        System.err.println("Failed to load sound effect: " + e.getMessage());
        }

        // custom font for title and buttons
        Font customFont = Font.loadFont(
                getClass().getResource("/fonts-urban-jungle-font/Urbanjungledemo-ymAm.otf").toExternalForm(),
                20
        );

        // Image logic
        ImageView backgroundImage = new ImageView();
        try {
            Image img = new Image(getClass().getResource("/images/free.jpg").toExternalForm());
            backgroundImage.setImage(img);
            backgroundImage.setFitWidth(900);
            backgroundImage.setFitHeight(700);
            backgroundImage.setPreserveRatio(false);

            Glow glow = new Glow(0.4);
            backgroundImage.setEffect(glow);
        } catch (Exception e) {
            System.err.println("Background image not found.");
        }

        //title logic
        Label titleLabel = new Label("Garena");
        titleLabel.setFont(Font.font(customFont.getFamily(), 70));
        titleLabel.setStyle("-fx-text-fill: #FF4500;");
        titleLabel.setEffect(new DropShadow(20, Color.BLACK));
        titleLabel.setLayoutX(320);
        titleLabel.setLayoutY(130);

        // Button logic
        Button playBtn = new Button("Play Game");
        Button instructionsBtn = new Button("Instructions");
        Button settingsBtn = new Button("Settings");
        Button exitBtn = new Button("Exit");


        String defaultStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px;";

        String hoverStyle =
                "-fx-background-color: #FF4500; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-cursor: hand;";

        playBtn.setStyle(defaultStyle);
        playBtn.setOnMouseEntered(e -> playBtn.setStyle(hoverStyle));
        playBtn.setOnMouseExited(e -> playBtn.setStyle(defaultStyle));

        exitBtn.setStyle(defaultStyle);
        exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(hoverStyle));
        exitBtn.setOnMouseExited(e -> exitBtn.setStyle(defaultStyle));
       exitBtn.setOnAction(event -> stage.close());
        exitBtn.setOnAction(event -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Game");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Any unsaved progress will be lost.");

            java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                stage.close();
            }
        });


        settingsBtn.setStyle(defaultStyle);
        settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle(hoverStyle));
        settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle(defaultStyle));

        instructionsBtn.setStyle(defaultStyle);
        instructionsBtn.setOnMouseEntered(e -> instructionsBtn.setStyle(hoverStyle));
        instructionsBtn.setOnMouseExited(e -> instructionsBtn.setStyle(defaultStyle));

        playBtn.setLayoutX(360);
        playBtn.setLayoutY(440);

        instructionsBtn.setLayoutX(360);
        instructionsBtn.setLayoutY(490);

        settingsBtn.setLayoutX(360);
        settingsBtn.setLayoutY(540);

        exitBtn.setLayoutX(360);
        exitBtn.setLayoutY(590);


        playBtn.setFont(customFont);
        instructionsBtn.setFont(customFont);
        settingsBtn.setFont(customFont);
        exitBtn.setFont(customFont);

        playBtn.setPrefWidth(200);
        instructionsBtn.setPrefWidth(200);
        settingsBtn.setPrefWidth(200);
        exitBtn.setPrefWidth(200);

        playBtn.setOnAction(e -> {
            playClickSound();

        });

        settingsBtn.setOnAction(e -> {
            playClickSound();

        });

        instructionsBtn.setOnAction(e -> {
            playClickSound();

        });

        exitBtn.setOnAction(e -> {
            playClickSound();

        });



        Pane pan = new Pane(backgroundImage,titleLabel, playBtn,instructionsBtn,settingsBtn,exitBtn);
        pan.setStyle("-fx-background-color:black");
        Scene scene = new Scene(pan, 900, 700);
        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    private void playClickSound() {
        if (clickSound != null) {
            clickSound.play();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}


