package pacman.moduledpacman.controller;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import pacman.moduledpacman.model.Direction;
import pacman.moduledpacman.model.PacmanModel;
import pacman.moduledpacman.view.PacmanView;

import java.util.Timer;
import java.util.TimerTask;

public class PacmanController implements EventHandler<KeyEvent> {
    @FXML
    private PacmanView pacmanView;
    @FXML
    private Label time_label;
    @FXML
    private Label points_label;
    @FXML
    private Label restarted;
    @FXML
    private Label gameCondition;
    @FXML
    private Canvas livesCanvas;
    @FXML
    private Button next_level;
    private PacmanModel pacmanModel;
    private Timer timer;
    private int seconds;
    Timeline movePacman, updateView, moveInky, moveBlinky, movePinky, moveClyde;

    public PacmanController() {
    }

    public void stopGame() {
        stopTimer();
        stopTimelines();
    }

    @FXML
    public void initialize() {
        if (pacmanView != null) {
            pacmanView.setOnKeyPressed(this);
            pacmanView.setFocusTraversable(true);
        } else {
            System.err.println("pacmanView is not initialized");
        }
        next_level.setVisible(false);
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        Direction direction = Direction.NO_DIR;
        switch (code) {
            case UP -> direction = Direction.UP;
            case DOWN -> direction = Direction.DOWN;
            case LEFT -> direction = Direction.LEFT;
            case RIGHT -> direction = Direction.RIGHT;
        }
        pacmanModel.handleKeyPress(direction);
        pacmanView.updateView(pacmanModel);
    }

    public void setLevel(int level) {
        this.pacmanModel = new PacmanModel(level);
        this.update();
        startTimelines();
        startTimer();
        updateLifeImage();
        pacmanView.paintField(pacmanModel);
        pacmanView.updateView(pacmanModel);
    }

    private void updateLifeImage() {
        int count = pacmanModel.getPacman().getLives();
        Image liveImage = pacmanView.getLiveImage(count);
        GraphicsContext gc = livesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, livesCanvas.getWidth(), livesCanvas.getHeight());
        double aspectRatio = liveImage.getWidth() / liveImage.getHeight();
        double newHeight = 50;
        double newWidth = newHeight * aspectRatio;
        double x = (livesCanvas.getWidth() - newWidth) / 2;
        double y = (livesCanvas.getHeight() - newHeight) / 2;
        gc.drawImage(liveImage, x, y, newWidth, newHeight);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                updateTime();
            }
        }, 1000, 1000);
    }

    private void updateTime() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        Platform.runLater(() -> time_label.setText("Time: " + String.format("%02d:%02d", minutes, remainingSeconds)));
    }

    private void updatePoints() {
        Platform.runLater(() -> points_label.setText("Points: " + pacmanModel.getPoints()));
    }

    private void startTimelines() {
        stopTimelines();
        movePacman = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            pacmanModel.updatePacmanCoords();
            updatePoints();
            checkGameStatus();
            System.out.println("pacman moved");
        }));
        movePacman.setCycleCount(Timeline.INDEFINITE);
        movePacman.play();

        movePinky = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            pacmanModel.movePinkGhost();
            System.out.println("pink ghost moved");
        }));
        movePinky.setCycleCount(Timeline.INDEFINITE);
        movePinky.play();

        moveBlinky = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            pacmanModel.moveRedGhost();
            System.out.println("red ghost moved");
        }));
        moveBlinky.setCycleCount(Timeline.INDEFINITE);
        moveBlinky.play();

        moveClyde = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            pacmanModel.moveYellowGhost();
            System.out.println("yellow ghost moved");
        }));
        moveClyde.setCycleCount(Timeline.INDEFINITE);
        moveClyde.play();

        moveInky = new Timeline(new KeyFrame(Duration.millis(20), event -> {
            pacmanModel.moveBlueGhost();
            System.out.println("blue ghost moved");
        }));
        moveInky.setCycleCount(Timeline.INDEFINITE);
        moveInky.play();

        updateView = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            pacmanView.updateView(pacmanModel);
            System.out.println("Updated view");
        }));
        updateView.setCycleCount(Timeline.INDEFINITE);
        updateView.play();

        Timeline collision = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            pacmanModel.checkCollision();
            if (pacmanModel.isCollision()) {
                stopTimelines();
                handleCollision();
                pacmanModel.resetCollision();
            }
            pacmanView.updateView(pacmanModel);
        }));
        collision.setCycleCount(Timeline.INDEFINITE);
        collision.play();
    }

    private void stopTimelines() {
        if (movePacman != null) {
            movePacman.stop();
        }
        if (updateView != null) {
            updateView.stop();
        }
        if (moveInky != null) {
            moveInky.stop();
        }
        if (moveBlinky != null) {
            moveBlinky.stop();
        }
        if (movePinky != null) {
            movePinky.stop();
        }
        if (moveClyde != null) {
            moveClyde.stop();
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void update() {
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        handleKeyPress(keyEvent);
    }

    @FXML
    protected void onRestartButtonClicked() {
        System.out.println("Restart");
        initialize();
        restartGame(1);
    }

    private void checkGameStatus() {
        if (pacmanModel.isLevelDone()) {
            System.out.println("You won");
        } else if (pacmanModel.isCollision()) {
            handleCollision();
        }
    }

    private void handleCollision() {
        System.out.println("Ghost eaten");
        if (pacmanModel.getPacman().getLives() != 0) {
            resetAfterCollision();
        } else {
            gameCondition.setText("Game Over");
            stopTimelines();
        }
        updateLifeImage();
    }

    private void resetAfterCollision() {
        pacmanModel.resetPositions();
        pacmanModel.resetCollision();
        pacmanView.updateView(pacmanModel);
        startTimelines();
    }

    @FXML
    protected void onNextLevelCLicked() {
        pacmanModel.loadNextLevel();
        initialize();
        restartGame(0);
    }

    private void restartGame(int flag) {
        stopTimer();
        stopTimelines();
        pacmanModel.restartGame();
        pacmanView.updateView(pacmanModel);
        gameCondition.setText("");
        next_level.visibleProperty().set(false);
        time_label.setText("Time: 00:00");
        if (flag == 1) {
            showMessage("Game was restarted", 10);
        } else {
            showMessage("Next level", 10);
        }
        startTimelines();
        startTimer();
        pacmanView.updateView(pacmanModel);
        pacmanView.setOnKeyPressed(this);
        pacmanView.requestFocus();
        updateLifeImage();
    }

    public void showMessage(String message, double duration) {
        restarted.setAlignment(Pos.CENTER);
        restarted.setText(message);
        Timeline timelineTimer = new Timeline(new KeyFrame(Duration.seconds(duration), event -> restarted.setText("")));
        timelineTimer.play();
    }
}
