package pacman.moduledpacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;
import pacman.moduledpacman.PacmanApplication;
import pacman.moduledpacman.model.Direction;
import pacman.moduledpacman.model.GameObject;
import pacman.moduledpacman.model.PacmanModel;
import pacman.moduledpacman.model.entities.*;

import java.util.*;

public class PacmanView extends Group {
    int currSprite = 0;
    final private int size = 33;
    private final Map<Direction, List<Image>> pacmanImages = new HashMap<>();
    private final Map<Integer, Image> livesImage = new HashMap<>();
    private final Map<Direction, Image> redGhost = new HashMap<>();
    private final Map<Direction, Image> blueGhost = new HashMap<>();
    private final Map<Direction, Image> pinkGhost = new HashMap<>();
    private final Map<Direction, Image> yellowGhost = new HashMap<>();
    private Image wall, smallDot, bigDot, empty, eatableGhost, chain, whiteGhost, two00;
    public final static double CELL_WIDTH = 20.0;

    private final Canvas canvas = new Canvas(size * CELL_WIDTH, size * CELL_WIDTH);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    private final Timeline spriteTimeline;

    public PacmanView() {
        initPacman();
        initRedGhost();
        initBlueGhost();
        initPinkGhost();
        initYellowGhost();
        initOthers();
        initLives();
        this.getChildren().add(canvas);
        spriteTimeline = new Timeline(new KeyFrame(Duration.millis(200), event -> updateSprite()));
        spriteTimeline.setCycleCount(Timeline.INDEFINITE);
        spriteTimeline.play();
    }

    private void initOthers() {
        wall = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Wall.png")));
        smallDot = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/SmallDot.png")));
        bigDot = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/BigDot.png")));
        empty = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Empty.png")));
        eatableGhost = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/EatableGhost.png")));
        chain = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Chain.png")));
        whiteGhost = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/WhiteGhost.png")));
        two00 = new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/200.png")));
    }

    private void initPacman() {
        ArrayList<Image> ups = new ArrayList<>();
        ups.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman1.png"))));
        ups.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanUp1.png"))));
        ups.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanUp.png"))));
        ups.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanUp1.png"))));
        ups.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman1.png"))));
        pacmanImages.put(Direction.UP, ups);

        ArrayList<Image> downs = new ArrayList<>();
        downs.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman1.png"))));
        downs.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanDown1.png"))));
        downs.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanDown.png"))));
        downs.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanDown1.png"))));
        downs.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman1.png"))));
        pacmanImages.put(Direction.DOWN, downs);

        ArrayList<Image> lefts = new ArrayList<>();
        lefts.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman2.png"))));
        lefts.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanLeft1.png"))));
        lefts.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanLeft.png"))));
        lefts.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanLeft1.png"))));
        lefts.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman2.png"))));
        pacmanImages.put(Direction.LEFT, lefts);

        ArrayList<Image> rights = new ArrayList<>();
        rights.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman2.png"))));
        rights.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanRight1.png"))));
        rights.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanRight.png"))));
        rights.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PacmanRight1.png"))));
        rights.add(new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/Pacman2.png"))));
        pacmanImages.put(Direction.RIGHT, rights);
    }

    private void initLives() {
        livesImage.put(1, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/1heart.png"))));
        livesImage.put(2, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/2hearts.png"))));
        livesImage.put(3, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/3hearts.png"))));
    }

    private void initRedGhost() {
        redGhost.put(Direction.UP, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/BlinkyUp.png"))));
        redGhost.put(Direction.DOWN, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/BlinkyDown.png"))));
        redGhost.put(Direction.RIGHT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/BlinkyRight.png"))));
        redGhost.put(Direction.LEFT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/BlinkyLeft.png"))));
    }

    private void initBlueGhost() {
        blueGhost.put(Direction.UP, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/InkyUp.png"))));
        blueGhost.put(Direction.DOWN, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/InkyDown.png"))));
        blueGhost.put(Direction.RIGHT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/InkyRight.png"))));
        blueGhost.put(Direction.LEFT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/InkyLeft.png"))));
    }

    private void initPinkGhost() {
        pinkGhost.put(Direction.UP, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PinkyUp.png"))));
        pinkGhost.put(Direction.DOWN, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PinkyDown.png"))));
        pinkGhost.put(Direction.RIGHT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PinkyRight.png"))));
        pinkGhost.put(Direction.LEFT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/PinkyLeft.png"))));
    }

    private void initYellowGhost() {
        yellowGhost.put(Direction.UP, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/ClydeUp.png"))));
        yellowGhost.put(Direction.DOWN, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/ClydeDown.png"))));
        yellowGhost.put(Direction.RIGHT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/ClydeRight.png"))));
        yellowGhost.put(Direction.LEFT, new Image(Objects.requireNonNull(PacmanApplication.class.getResourceAsStream("images/ClydeLeft.png"))));
    }

    public Image getLiveImage(int count) {
        return livesImage.get(count);
    }

    public void paintField(PacmanModel model) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                GameObject curr = model.getCell(r, c);
                switch (curr) {
                    case Wall -> gc.drawImage(wall, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                    case SmallCoin -> gc.drawImage(smallDot, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                    case BigBerry -> gc.drawImage(bigDot, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                    case Chain -> gc.drawImage(chain, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                    default -> gc.drawImage(empty, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
                }
            }
        }
    }

    public void makeCellEmpty(int r, int c) {
        gc.drawImage(empty, c * CELL_WIDTH, r * CELL_WIDTH, CELL_WIDTH, CELL_WIDTH);
    }

    public void paintGhosts(PacmanModel model, List<Ghost> ghosts) {
        for (Ghost ghost : ghosts) {
            Image ghostImage = getGhostImage(ghost, model);
            if (ghostImage == null) {
                continue;
            }
            gc.drawImage(ghostImage, ghost.getX(), ghost.getY(), CELL_WIDTH, CELL_WIDTH);
        }
    }

    private Image getGhostImage(Ghost ghost, PacmanModel model) {
        Direction direction = ghost.getDirection();
        if (direction == Direction.NO_DIR) {
            direction = Direction.LEFT;
        }
        if (ghost.isScarried() && !model.isGhostBlink()) {
            return eatableGhost;
        } else if (ghost.isScarried() && model.isGhostBlink() ) {
            return ghost.updateSprite() == 0 ? eatableGhost : whiteGhost;
        } else if (ghost instanceof ChaserBlinky) {
            return redGhost.get(direction);
        } else if (ghost instanceof StylistInky) {
            return blueGhost.get(direction);
        } else if (ghost instanceof SpeedyPinky) {
            return pinkGhost.get(direction);
        } else if (ghost instanceof CrybabyClyde) {
            return yellowGhost.get(direction);
        }
        return null;
    }

    public void paintPacman(Pacman player) {
        Direction direction = player.getLastDirection();
        if (direction == Direction.NO_DIR) {
            direction = Direction.LEFT;
        }
        List<Image> pacmanImagesForDirection = pacmanImages.get(direction);
        if (pacmanImagesForDirection == null || pacmanImagesForDirection.isEmpty()) {
            return;
        }
        Image pacmanImage = pacmanImagesForDirection.get(currSprite);
        gc.drawImage(pacmanImage, player.getX(), player.getY(), player.getHeight(), player.getWidth());
    }

    private void updateSprite() {
        currSprite = (currSprite + 1) % 5;
    }

    public void updateView(PacmanModel model) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        paintField(model);
        paintGhosts(model, model.getGhosts());
        paintPacman(model.getPacman());
    }

    public void drawPoints(PacmanModel model) {
        int x = model.getPacman().getX();
        int y = model.getPacman().getY();
        gc.drawImage(two00, x, y, CELL_WIDTH, CELL_WIDTH);
    }
}
