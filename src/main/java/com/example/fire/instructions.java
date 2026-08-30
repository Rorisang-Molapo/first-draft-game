package com.example.fire;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class instructions {

    Font customFont = Font.loadFont(
            getClass().getResource("/fonts-urban-jungle-font/Urbanjungledemo-ymAm.otf").toExternalForm(),
            50
    );

    private Stage stage;
    private HelloApplication mainApp;

    public instructions(Stage stage, HelloApplication mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    public Scene getScene() {

        // Title
        Label titleLabel = new Label("Instructions");
        titleLabel.setFont(Font.font(customFont.getFamily(), 90));
        titleLabel.setLayoutX(180);
        titleLabel.setLayoutY(50);
        titleLabel.setStyle("-fx-text-fill: #FF4500;");

        // Game Objective
        Label heading1 = new Label("Objective");
        heading1.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 20px; -fx-font-weight: bold;");
        heading1.setLayoutX(150);
        heading1.setLayoutY(180);

        Label desc1 = new Label("Destroy all red targets before they shrink and vanish!");
        desc1.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        desc1.setLayoutX(150);
        desc1.setLayoutY(210);

        // Controls
        Label heading2 = new Label("Controls");
        heading2.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 20px; -fx-font-weight: bold;");
        heading2.setLayoutX(150);
        heading2.setLayoutY(260);

        Label desc2 = new Label("Use your Mouse Left-Click to click on appearing targets.");
        desc2.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        desc2.setLayoutX(150);
        desc2.setLayoutY(290);

        // Rules
        Label heading3 = new Label("Rules");
        heading3.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 20px; -fx-font-weight: bold;");
        heading3.setLayoutX(150);
        heading3.setLayoutY(340);

        Label desc3 = new Label("• You start with 200 HP – missing a target costs 25 HP");
        desc3.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        desc3.setLayoutX(150);
        desc3.setLayoutY(370);

        Label desc3b = new Label("• Clear all 25 targets to claim victory!");
        desc3b.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 16px; -fx-font-weight: bold;");
        desc3b.setLayoutX(150);
        desc3b.setLayoutY(400);

        // Back Button
        Button backBtn = new Button("Back to Main");
        backBtn.setLayoutX(350);
        backBtn.setLayoutY(500);
        backBtn.setPrefWidth(200);
        backBtn.setFont(Font.font(customFont.getFamily(), 12));

        String defaultStyle =
                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-text-fill: black; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-padding: 8px 16px; " +
                        "-fx-cursor: hand;";
        backBtn.setStyle(defaultStyle);

        DropShadow buttonGlow = new DropShadow();
        buttonGlow.setColor(Color.web("#FF4500"));
        buttonGlow.setRadius(15);
        buttonGlow.setSpread(0.4);
        backBtn.setEffect(buttonGlow);

        backBtn.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: black");
        pane.getChildren().addAll(
                titleLabel,
                heading1, desc1,
                heading2, desc2,
                heading3, desc3, desc3b,
                backBtn
        );

        return new Scene(pane, 900, 700);
    }
}