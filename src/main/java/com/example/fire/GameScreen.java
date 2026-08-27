package com.example.fire;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameScreen {


    private final Stage stage;
    private final HelloApplication mainApp;

    //Game state
    private int score = 0;
    private int health = 200;
    private final int MAX_HEALTH = 200;
    private final int TOTAL_ENEMIES = 25;
    private int enemiesSpawned = 0;
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isVictory = false;

    // Player
    private final Rectangle player;

    //Entities
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Rectangle> medkits = new ArrayList<>();

    //Controls
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private double mouseX = 0;
    private double mouseY = 0;

    // Spawning
    private final Random random = new Random();
    private long lastSpawnTime = 0;
    private final long SPAWN_INTERVAL = 1_800_000_000L; // 1.8 seconds


    private Label scoreLabel;
    private Label healthLabel;
    private ProgressBar healthBar;
    private Label enemiesLeftLabel;
    private Button pauseBtn;

    // Game Loop
    private AnimationTimer gameLoop;


    private static class Bullet {
        final Circle circle;
        final double vx;
        final double vy;
        Bullet(Circle circle, double vx, double vy) {
            this.circle = circle;
            this.vx = vx;
            this.vy = vy;
        }
    }


    private static class Enemy {
        final Rectangle rect;
        final double speed;
        Enemy(Rectangle rect, double speed) {
            this.rect = rect;
            this.speed = speed;
        }
    }


    public GameScreen(Stage stage, HelloApplication mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;

        // Player starts in the centre of the arena
        player = new Rectangle(50, 20, Color.web("#FF4500"));
        player.setX(425);
        player.setY(340);
    }


    public Scene getScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0A0A0A;");
        root.setFocusTraversable(true);

        //  TOP BAR
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Health bar
        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(150);
        healthBar.setStyle("-fx-accent: #00FF00; -fx-background-color: #333;");
        healthLabel = new Label("200/200 HP");
        healthLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        HBox healthBox = new HBox(10, healthBar, healthLabel);
        healthBox.setAlignment(Pos.CENTER);

        enemiesLeftLabel = new Label("Enemies: 25");
        enemiesLeftLabel.setStyle("-fx-text-fill: #FF4500; -fx-font-size: 16px; -fx-font-weight: bold;");

        pauseBtn = new Button("Pause");
        pauseBtn.setStyle("-fx-background-color: #FF4500; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        pauseBtn.setFocusTraversable(false);

        HBox topBar = new HBox(30, scoreLabel, healthBox, enemiesLeftLabel, pauseBtn);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(12));
        topBar.setStyle("-fx-background-color: #1A1A1A; -fx-border-color: #FF4500; -fx-border-width: 0 0 2 0;");
        root.setTop(topBar);

        //  GAME PLAY AREA
        Pane gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: #0A0A0A;");
        root.setCenter(gamePane);

        // Add player to arena
        gamePane.getChildren().add(player);


        Scene scene = new Scene(root, 900, 700);

        // Mouse tracking (for aiming)
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        // Keyboard
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.W || code == KeyCode.UP) moveUp = true;
            if (code == KeyCode.S || code == KeyCode.DOWN) moveDown = true;
            if (code == KeyCode.A || code == KeyCode.LEFT) moveLeft = true;
            if (code == KeyCode.D || code == KeyCode.RIGHT) moveRight = true;
            if (code == KeyCode.SPACE) shootBullet(gamePane);
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.W || code == KeyCode.UP) moveUp = false;
            if (code == KeyCode.S || code == KeyCode.DOWN) moveDown = false;
            if (code == KeyCode.A || code == KeyCode.LEFT) moveLeft = false;
            if (code == KeyCode.D || code == KeyCode.RIGHT) moveRight = false;
        });

        //  Pause button
        pauseBtn.setOnAction(e -> {
            mainApp.playClickSound();
            togglePause();
        });

        // Start the game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused || isGameOver || isVictory) return;

                // 1. Move player
                movePlayer();

                // 2. Spawn enemies (finite pool)
                if (enemiesSpawned < TOTAL_ENEMIES && now - lastSpawnTime > SPAWN_INTERVAL) {
                    spawnEnemy(gamePane);
                    lastSpawnTime = now;
                }

                // 3. Move bullets
                Iterator<Bullet> bulletIt = bullets.iterator();
                while (bulletIt.hasNext()) {
                    Bullet b = bulletIt.next();
                    b.circle.setCenterX(b.circle.getCenterX() + b.vx);
                    b.circle.setCenterY(b.circle.getCenterY() + b.vy);

                    // Remove if off-screen
                    if (b.circle.getCenterX() < -20 || b.circle.getCenterX() > 920 ||
                            b.circle.getCenterY() < -20 || b.circle.getCenterY() > 720) {
                        gamePane.getChildren().remove(b.circle);
                        bulletIt.remove();
                    }
                }

                // 4. Move enemies (chase player relentlessly)
                for (Enemy enemy : enemies) {
                    chasePlayer(enemy);
                }

                // 5. Move medkits (slowly float down)
                Iterator<Rectangle> medIt = medkits.iterator();
                while (medIt.hasNext()) {
                    Rectangle med = medIt.next();
                    med.setY(med.getY() + 0.5);
                    if (med.getY() > 720) {
                        gamePane.getChildren().remove(med);
                        medIt.remove();
                    }
                }

                // 6. Collision: bullets vs enemies
                bulletIt = bullets.iterator();
                while (bulletIt.hasNext()) {
                    Bullet b = bulletIt.next();
                    Iterator<Enemy> enemyIt = enemies.iterator();
                    boolean bulletUsed = false;
                    while (enemyIt.hasNext()) {
                        Enemy enemy = enemyIt.next();
                        if (b.circle.getBoundsInParent().intersects(enemy.rect.getBoundsInParent())) {
                            // Hit!
                            gamePane.getChildren().remove(b.circle);
                            bulletIt.remove();
                            bulletUsed = true;

                            gamePane.getChildren().remove(enemy.rect);
                            enemyIt.remove();

                            score += 10;
                            updateUI();

                            // Medkit drop (20% chance)
                            if (random.nextInt(100) < 20) {
                                Rectangle med = new Rectangle(15, 15, Color.LIMEGREEN);
                                med.setX(enemy.rect.getX() + 10);
                                med.setY(enemy.rect.getY() + 10);
                                medkits.add(med);
                                gamePane.getChildren().add(med);
                            }
                            break; // bullet destroyed, exit enemy loop
                        }
                    }
                    // If bullet wasn't used, it continues
                }

                // 7. Collision: enemies vs player
                Iterator<Enemy> enemyIt = enemies.iterator();
                while (enemyIt.hasNext()) {
                    Enemy enemy = enemyIt.next();
                    if (player.getBoundsInParent().intersects(enemy.rect.getBoundsInParent())) {
                        // Player takes damage
                        health -= 25;
                        if (health < 0) health = 0;
                        updateUI();

                        // Remove the enemy that hit us
                        gamePane.getChildren().remove(enemy.rect);
                        enemyIt.remove();

                        if (health <= 0) {
                            triggerGameOver();
                            return; // stop processing this frame
                        }
                    }
                }

                // 8. Collision: player picks up medkits
                Iterator<Rectangle> medkitIt = medkits.iterator();
                while (medkitIt.hasNext()) {
                    Rectangle med = medkitIt.next();
                    if (player.getBoundsInParent().intersects(med.getBoundsInParent())) {
                        health = Math.min(health + 40, MAX_HEALTH);
                        updateUI();
                        gamePane.getChildren().remove(med);
                        medkitIt.remove();
                        mainApp.playClickSound(); // feedback
                    }
                }

                // 9. Check victory condition
                if (enemiesSpawned >= TOTAL_ENEMIES && enemies.isEmpty()) {
                    triggerVictory();
                }
            }
        };

        gameLoop.start();

        // Focus the root so keyboard works immediately
        root.requestFocus();

        return scene;
    }


    private void movePlayer() {
        double speed = 5;
        if (moveUp) player.setY(player.getY() - speed);
        if (moveDown) player.setY(player.getY() + speed);
        if (moveLeft) player.setX(player.getX() - speed);
        if (moveRight) player.setX(player.getX() + speed);

        // Boundaries (keep inside the arena)
        double pX = player.getX();
        double pY = player.getY();
        if (pX < 0) player.setX(0);
        if (pX > 850) player.setX(850);
        if (pY < 0) player.setY(0);
        if (pY > 660) player.setY(660);
    }

    private void shootBullet(Pane pane) {
        if (isPaused || isGameOver || isVictory) return;
        mainApp.playClickSound();

        // Bullet starts at the centre of the player
        double startX = player.getX() + 25;
        double startY = player.getY() + 10;

        // Vector from player to mouse
        double dx = mouseX - startX;
        double dy = mouseY - startY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        // If mouse is exactly on player, shoot in a default direction (up)
        double vx, vy;
        if (dist > 0) {
            double speed = 10;
            vx = (dx / dist) * speed;
            vy = (dy / dist) * speed;
        } else {
            vx = 0;
            vy = -10;
        }

        Circle bulletCircle = new Circle(startX, startY, 5, Color.web("#FFD700"));
        Bullet bullet = new Bullet(bulletCircle, vx, vy);
        bullets.add(bullet);
        pane.getChildren().add(bulletCircle);
    }

    private void spawnEnemy(Pane pane) {
        // Spawn from one of the four edges
        double x, y;
        int edge = random.nextInt(4); // 0=top, 1=right, 2=bottom, 3=left
        switch (edge) {
            case 0 -> { x = random.nextDouble() * 860; y = -30; }
            case 1 -> { x = 930; y = random.nextDouble() * 660; }
            case 2 -> { x = random.nextDouble() * 860; y = 730; }
            default -> { x = -30; y = random.nextDouble() * 660; }
        }

        Rectangle rect = new Rectangle(35, 25, Color.rgb(200, 50, 50));
        rect.setX(x);
        rect.setY(y);

        // Random speed between 1.5 and 2.5 for variety
        double speed = 1.5 + random.nextDouble() * 1.0;
        Enemy enemy = new Enemy(rect, speed);
        enemies.add(enemy);
        pane.getChildren().add(rect);
        enemiesSpawned++;
        updateUI();
    }

    private void chasePlayer(Enemy enemy) {
        // Enemy centre
        double ex = enemy.rect.getX() + 17.5;
        double ey = enemy.rect.getY() + 12.5;
        // Player centre
        double px = player.getX() + 25;
        double py = player.getY() + 10;

        double dx = px - ex;
        double dy = py - ey;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > 1) {
            // Add a tiny random jitter to the direction so they don't perfectly stack
            double jitterX = (random.nextDouble() - 0.5) * 0.4;
            double jitterY = (random.nextDouble() - 0.5) * 0.4;
            double normX = (dx / dist) + jitterX;
            double normY = (dy / dist) + jitterY;
            double normDist = Math.sqrt(normX * normX + normY * normY);
            if (normDist > 0) {
                normX /= normDist;
                normY /= normDist;
            }
            double speed = enemy.speed;
            enemy.rect.setX(enemy.rect.getX() + normX * speed);
            enemy.rect.setY(enemy.rect.getY() + normY * speed);
        }
    }


    private void updateUI() {
        scoreLabel.setText("Score: " + score);
        healthLabel.setText(health + "/" + MAX_HEALTH + " HP");
        healthBar.setProgress((double) health / MAX_HEALTH);
        // Change bar colour based on health
        if (health > 100) {
            healthBar.setStyle("-fx-accent: #00FF00; -fx-background-color: #333;");
        } else if (health > 50) {
            healthBar.setStyle("-fx-accent: #FFA500; -fx-background-color: #333;");
        } else {
            healthBar.setStyle("-fx-accent: #FF0000; -fx-background-color: #333;");
        }
        int remaining = TOTAL_ENEMIES - enemiesSpawned + enemies.size();
        enemiesLeftLabel.setText("Enemies: " + remaining);
    }


    private void togglePause() {
        isPaused = !isPaused;
        pauseBtn.setText(isPaused ? "Resume" : "Pause");
        // Pause/Resume background music if you want (optional)
        if (isPaused && mainApp.getBgMusicPlayer() != null) {
            mainApp.getBgMusicPlayer().pause();
        } else if (!isPaused && mainApp.getBgMusicPlayer() != null && !mainApp.isMuted()) {
            mainApp.getBgMusicPlayer().play();
        }
    }


    private void triggerGameOver() {
        isGameOver = true;
        gameLoop.stop();
        Platform.runLater(this::showGameOverScreen);
    }

    private void triggerVictory() {
        isVictory = true;
        gameLoop.stop();
        Platform.runLater(this::showVictoryScreen);
    }

    private void showGameOverScreen() {
        VBox layout = buildEndScreen(
                "GAME OVER",
                "You were eliminated!",
                Color.web("#FF4500")
        );
        stage.setScene(new Scene(layout, 900, 700));
    }

    private void showVictoryScreen() {
        VBox layout = buildEndScreen(
                "BOOYAH!! ",
                "You are the last man standing!",
                Color.web("#FFD700")
        );
        stage.setScene(new Scene(layout, 900, 700));
    }

    private VBox buildEndScreen(String title, String subtitle, Color titleColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: " + toRgbString(titleColor) + "; -fx-font-size: 60px; -fx-font-weight: bold;");

        Label subLabel = new Label(subtitle);
        subLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");

        Label scoreLabelEnd = new Label("Final Score: " + score);
        scoreLabelEnd.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");

        Button playAgainBtn = new Button("Play Again");
        Button mainMenuBtn = new Button("Main Menu");

        String btnStyle = "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                "-fx-text-fill: black; " +
                "-fx-background-radius: 8px; " +
                "-fx-padding: 10px 20px; " +
                "-fx-font-size: 16px; " +
                "-fx-cursor: hand;";
        playAgainBtn.setStyle(btnStyle);
        mainMenuBtn.setStyle(btnStyle);

        playAgainBtn.setOnAction(e -> {
            mainApp.playClickSound();
            GameScreen newGame = new GameScreen(stage, mainApp);
            stage.setScene(newGame.getScene());
        });

        mainMenuBtn.setOnAction(e -> {
            mainApp.playClickSound();
            try {
                mainApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(25, titleLabel, subLabel, scoreLabelEnd, playAgainBtn, mainMenuBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #0A0A0A;");
        return layout;
    }

    // Helper to convert Color to CSS-friendly rgb string
    private String toRgbString(Color c) {
        return String.format("rgb(%d, %d, %d)",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }
}