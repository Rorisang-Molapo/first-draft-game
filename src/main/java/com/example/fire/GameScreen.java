//package com.example.fire;
//
//import javafx.animation.AnimationTimer;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.VBox;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//
//public class GameScreen {
//
//    private Stage stage;
//    private HelloApplication mainApp;
//
//    // Game variables
//    private int score = 0;
//    private int lives = 3;
//    private int level = 1;
//    private boolean isPaused = false;
//
//    // Entities
//    private Rectangle player;
//    private List<Circle> bullets = new ArrayList<>();
//    private List<Rectangle> targets = new ArrayList<>();
//
//    // Controls tracking
//    private boolean goLeft = false;
//    private boolean goRight = false;
//
//    private AnimationTimer gameLoop;
//    private Random random = new Random();
//    private long lastTargetSpawn = 0;
//
//    public GameScreen(Stage stage, HelloApplication mainApp) {
//        this.stage = stage;
//        this.mainApp = mainApp;
//    }
//
//    public Scene getScene() {
//        BorderPane root = new BorderPane();
//        root.setStyle("-fx-background-color: #0A0A0A;");
//        root.setFocusTraversable(true); // FIX 1: Allow root layout to hold keyboard focus
//
//        // TOP BAR
//        Label scoreLabel = new Label("Score: 0");
//        Label livesLabel = new Label("Lives: ❤️❤️❤️");
//        Label levelLabel = new Label("Level: 1");
//        Button pauseBtn = new Button("Pause");
//
//        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
//        livesLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 18px; -fx-font-weight: bold;");
//        levelLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
//
//        pauseBtn.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
//        pauseBtn.setFocusTraversable(false);
//
//        HBox topBar = new HBox(40, scoreLabel, livesLabel, levelLabel, pauseBtn);
//        topBar.setAlignment(Pos.CENTER);
//        topBar.setPadding(new Insets(15));
//        topBar.setStyle("-fx-background-color: #1A1A1A; -fx-border-color: #FF4500; -fx-border-width: 0 0 2 0;");
//        root.setTop(topBar);
//
//        // GAME PLAY AREA
//        Pane gamePane = new Pane();
//        root.setCenter(gamePane);
//
//        // Player Cannon Sprite
//        player = new Rectangle(50, 20, Color.web("#FF4500"));
//        player.setY(580);
//        player.setX(425);
//        gamePane.getChildren().add(player);
//
//        // Pause Action
//        pauseBtn.setOnAction(e -> {
//            mainApp.playClickSound();
//            isPaused = !isPaused;
//            pauseBtn.setText(isPaused ? "Resume" : "Pause");
//            if (!isPaused) root.requestFocus(); // Re-claim focus on resume
//        });
//
//        // GAME LOOP
//        gameLoop = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (isPaused) return;
//
//                // Move Player
//                if (goLeft && player.getX() > 0) {
//                    player.setX(player.getX() - 6);
//                }
//                if (goRight && player.getX() < 850) {
//                    player.setX(player.getX() + 6);
//                }
//
//                // Spawn Targets
//                if (now - lastTargetSpawn > 1_500_000_000L) { // Every 1.5s
//                    Rectangle target = new Rectangle(40, 30, Color.LIGHTGRAY);
//                    target.setX(random.nextInt(840));
//                    target.setY(0);
//                    targets.add(target);
//                    gamePane.getChildren().add(target);
//                    lastTargetSpawn = now;
//                }
//
//                // Move Bullets
//                Iterator<Circle> bulletIter = bullets.iterator();
//                while (bulletIter.hasNext()) {
//                    Circle bullet = bulletIter.next();
//                    bullet.setCenterY(bullet.getCenterY() - 8);
//
//                    if (bullet.getCenterY() < 0) {
//                        gamePane.getChildren().remove(bullet);
//                        bulletIter.remove();
//                    }
//                }
//
//                // Move Targets & Collision Check
//                Iterator<Rectangle> targetIter = targets.iterator();
//                while (targetIter.hasNext()) {
//                    Rectangle target = targetIter.next();
//                    target.setY(target.getY() + (2 + level));
//
//                    // Check bullet hit
//                    Iterator<Circle> bIter = bullets.iterator();
//                    boolean hit = false;
//                    while (bIter.hasNext()) {
//                        Circle b = bIter.next();
//                        if (target.getBoundsInParent().intersects(b.getBoundsInParent())) {
//                            gamePane.getChildren().removeAll(target, b);
//                            targetIter.remove();
//                            bIter.remove();
//                            hit = true;
//                            score += 10;
//                            scoreLabel.setText("Score: " + score);
//
//                            // Level upgrade logic
//                            if (score % 50 == 0) {
//                                level++;
//                                levelLabel.setText("Level: " + level);
//                            }
//                            break;
//                        }
//                    }
//
//                    // Check target reaching bottom
//                    if (!hit && target.getY() > 620) {
//                        gamePane.getChildren().remove(target);
//                        targetIter.remove();
//                        lives--;
//                        updateLivesDisplay(livesLabel);
//
//                        if (lives <= 0) {
//                            gameLoop.stop();
//                            triggerGameOver();
//                        }
//                    }
//                }
//            }
//        };
//
//        Scene scene = new Scene(root, 900, 700);
//
//        // KEY LISTENERS
//        scene.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) goLeft = true;
//            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) goRight = true;
//            if (e.getCode() == KeyCode.SPACE) {
//                shootBullet(gamePane);
//            }
//        });
//
//        scene.setOnKeyReleased(e -> {
//            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) goLeft = false;
//            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) goRight = false;
//        });
//
//        root.requestFocus(); //Instantly capture keyboard inputs when game starts
//        gameLoop.start();
//        return scene;
//    }
//
//    private void shootBullet(Pane pane) {
//        if (isPaused) return;
//        mainApp.playClickSound();
//        Circle bullet = new Circle(player.getX() + 25, player.getY(), 5, Color.web("#FF4500"));
//        bullets.add(bullet);
//        pane.getChildren().add(bullet);
//    }
//
//    private void updateLivesDisplay(Label label) {
//        StringBuilder hearts = new StringBuilder("Lives: ");
//        for (int i = 0; i < lives; i++) hearts.append("❤️");
//        label.setText(hearts.toString());
//    }
//
//    private void triggerGameOver() {
//        Platform.runLater(this::showGameOverView);
//    }
//
//    private void showGameOverView() {
//        Label titleLabel = new Label("GAME OVER");
//        titleLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 60px; -fx-font-weight: bold;");
//
//        Label scoreLabel = new Label("Final Score: " + score);
//        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
//
//        Button playAgainBtn = new Button("Play Again");
//        Button mainMenuBtn = new Button("Main Menu");
//
//        String buttonStyle = "-fx-background-color: rgba(255, 255, 255, 0.9); " +
//                "-fx-text-fill: black; " +
//                "-fx-background-radius: 8px; " +
//                "-fx-padding: 10px 20px; " +
//                "-fx-font-size: 16px; " +
//                "-fx-cursor: hand;";
//
//        playAgainBtn.setStyle(buttonStyle);
//        mainMenuBtn.setStyle(buttonStyle);
//
//        playAgainBtn.setOnAction(e -> {
//            mainApp.playClickSound();
//            GameScreen gameView = new GameScreen(stage, mainApp);
//            stage.setScene(gameView.getScene());
//        });
//
//        mainMenuBtn.setOnAction(e -> {
//            mainApp.playClickSound();
//            try {
//                mainApp.start(stage);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        VBox layout = new VBox(25, titleLabel, scoreLabel, playAgainBtn, mainMenuBtn);
//        layout.setAlignment(Pos.CENTER);
//        layout.setStyle("-fx-background-color: #0A0A0A;");
//
//        stage.setScene(new Scene(layout, 900, 700));
//    }
//}