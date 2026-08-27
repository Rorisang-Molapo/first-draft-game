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

        public Scene getScene(){
            Label label = new Label("instructions");
            label.setFont(Font.font(customFont.getFamily(), 90));
            label.setLayoutX(180);
            label.setLayoutY(80);
            label.setStyle("-fx-text-fill: #FF4500;");

            Label headingLabel = new Label("Game Objective");
            headingLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 18px; -fx-font-weight: bold;");
            headingLabel.setLayoutX(150);
            headingLabel.setLayoutY(180);

            Label headingLabel2 = new Label("Controls");
            headingLabel2.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 18px; -fx-font-weight: bold;");
            headingLabel2.setLayoutX(150);
            headingLabel2.setLayoutY(200);

            Label headingLabel3 = new Label("Controls");
            headingLabel3.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 18px; -fx-font-weight: bold;");
            headingLabel3.setLayoutX(150);
            headingLabel3.setLayoutY(220);




            Button backBtn = new Button("Back to Main");
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
            pane.setStyle("-fx-background-color:black");
            pane.getChildren().addAll(label, backBtn,headingLabel,headingLabel2,headingLabel3);

            return new Scene(pane, 900, 700);
        }
    }

