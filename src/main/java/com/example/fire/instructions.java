package com.example.fire;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        titleLabel.setStyle("-fx-text-fill: #FF4500;");

        // Game Objective
        Label heading1 = new Label("Objective");
        heading1.setStyle("-fx-text-fill: #FF4500; -fx-font-weight: bold;");

        Label desc1 = new Label("Destroy all red targets before they shrink and vanish!");
        desc1.setStyle("-fx-text-fill: white;");

        // Controls
        Label heading2 = new Label("Controls");
        heading2.setStyle("-fx-text-fill: #FF4500; -fx-font-weight: bold;");

        Label desc2 = new Label("Use your Mouse Left-Click to click on appearing targets.");
        desc2.setStyle("-fx-text-fill: white;");

        // Rules
        Label heading3 = new Label("Rules");
        heading3.setStyle("-fx-text-fill: #FF4500; -fx-font-weight: bold;");

        Label desc3 = new Label("• You start with 200 HP – missing a target costs 25 HP");
        desc3.setStyle("-fx-text-fill: white;");

        Label desc3b = new Label("• Clear all 25 targets to claim victory!");
        desc3b.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");

        // Back Button
        Button backBtn = new Button("Back to Main");

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

        Pane pan = new Pane();

        // Responsive Ratios

        titleLabel.layoutXProperty().bind(pan.widthProperty().multiply(180.0 / 900.0));
        titleLabel.layoutYProperty().bind(pan.heightProperty().multiply(50.0 / 700.0));


        heading1.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        heading1.layoutYProperty().bind(pan.heightProperty().multiply(180.0 / 700.0));
        desc1.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        desc1.layoutYProperty().bind(pan.heightProperty().multiply(210.0 / 700.0));


        heading2.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        heading2.layoutYProperty().bind(pan.heightProperty().multiply(260.0 / 700.0));
        desc2.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        desc2.layoutYProperty().bind(pan.heightProperty().multiply(290.0 / 700.0));


        heading3.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        heading3.layoutYProperty().bind(pan.heightProperty().multiply(340.0 / 700.0));
        desc3.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        desc3.layoutYProperty().bind(pan.heightProperty().multiply(370.0 / 700.0));
        desc3b.layoutXProperty().bind(pan.widthProperty().multiply(150.0 / 900.0));
        desc3b.layoutYProperty().bind(pan.heightProperty().multiply(400.0 / 700.0));


        backBtn.layoutXProperty().bind(pan.widthProperty().multiply(350.0 / 900.0));
        backBtn.layoutYProperty().bind(pan.heightProperty().multiply(500.0 / 700.0));
        backBtn.prefWidthProperty().bind(pan.widthProperty().multiply(200.0 / 900.0));


        pan.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal) {
                double scaleFactor = newVal.doubleValue() / 900.0;

                // Scale Main Custom Fonts
                titleLabel.setFont(Font.font(customFont.getFamily(), 90 * scaleFactor));
                backBtn.setFont(Font.font(customFont.getFamily(), 20 * scaleFactor));

                // Scale Headings (Original size: 20px)
                Font scaledHeadingFont = Font.font("System", 20 * scaleFactor);
                heading1.setFont(scaledHeadingFont);
                heading2.setFont(scaledHeadingFont);
                heading3.setFont(scaledHeadingFont);

                // Scale Descriptions (Original size: 16px)
                Font scaledDescFont = Font.font("System", 16 * scaleFactor);
                desc1.setFont(scaledDescFont);
                desc2.setFont(scaledDescFont);
                desc3.setFont(scaledDescFont);
                desc3b.setFont(scaledDescFont);

                System.out.println("widthProperty changed from " + oldVal.doubleValue() + " to " + newVal.doubleValue());
            }
        });

        pan.setStyle("-fx-background-color: black");
        pan.getChildren().addAll(
                titleLabel,
                heading1, desc1,
                heading2, desc2,
                heading3, desc3, desc3b,
                backBtn
        );

        return new Scene(pan, 900, 700);
    }
}
