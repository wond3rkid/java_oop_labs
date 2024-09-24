package pacman.moduledpacman.model;

import javafx.application.Platform;
import pacman.moduledpacman.model.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PacmanModel {
    private final int size = 33;
    private final int block = 20;
    private int allCoins = 0, eatedCoins = 0;
    private int points;
    private int level;
    private int scareTimeLeft = 0;
    private Timer scareTimer;
    private Timer blinkTimer;
    private TimerTask scareTask;
    private TimerTask blinkTask;
    Pacman pacman;
    ArrayList<Ghost> ghosts;
    private GameObject[][] gameObjects;
    private boolean isGhostBlink, isGhostEaten;
    Direction[] possibleDirections = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};

    boolean isCollision;

    public PacmanModel(int level) {
        isCollision = false;
        pacman = new Pacman(block, block);
        gameObjects = new GameObject[size][size];
        ghosts = new ArrayList<>();
        this.level = level;
        isGhostEaten = false;
        initialize();
    }

    public GameObject getCell(int r, int c) {
        return gameObjects[r][c];
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public void initialize() {
        points = 0;
        Level levelView = Level.fromNumber(level - 1);
        String levelStr = levelView.getLevelView();
        System.out.println(levelView.getLevelView());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char curr = levelStr.charAt(i * size + j);
                switch (curr) {
                    case 'O':
                        allCoins++;
                        gameObjects[i][j] = GameObject.BigBerry;
                        break;
                    case 'o':
                        allCoins++;
                        gameObjects[i][j] = GameObject.SmallCoin;
                        break;
                    case 'C':
                        gameObjects[i][j] = GameObject.Chain;
                        break;
                    case 'P':
                        pacman.setInitX(j * block);
                        pacman.setInitY(i * block);
                        pacman.setX(j * block);
                        pacman.setY(i * block);
                        gameObjects[i][j] = GameObject.Empty;
                        System.out.println(j * block + ", " + i * block);
                        break;
                    case 'R':
                        gameObjects[i][j] = GameObject.RedGhost;
                        ChaserBlinky blinky = new ChaserBlinky(block, block);
                        blinky.setInitX(j * block);
                        blinky.setInitY(i * block);
                        blinky.setX(j * block);
                        blinky.setY(i * block);
                        ghosts.add(blinky);

                        System.out.println(j * block + ", " + i * block + "Blinky");
                        break;
                    case 'I':
                        gameObjects[i][j] = GameObject.BlueGhost;
                        StylistInky inky = new StylistInky(block, block);
                        inky.setInitX(j * block);
                        inky.setInitY(i * block);
                        inky.setX(j * block);
                        inky.setY(i * block);
                        ghosts.add(inky);

                        System.out.println(j * block + ", " + i * block + "Inky");
                        break;
                    case 'B':
                        gameObjects[i][j] = GameObject.PinkGhost;
                        SpeedyPinky pinky = new SpeedyPinky(block, block);
                        pinky.setInitX(j * block);
                        pinky.setInitY(i * block);
                        pinky.setX(j * block);
                        pinky.setY(i * block);
                        ghosts.add(pinky);

                        System.out.println(j * block + ", " + i * block + "Pinky");
                        break;
                    case 'Y':
                        gameObjects[i][j] = GameObject.YellowGhost;
                        CrybabyClyde clyde = new CrybabyClyde(block, block);
                        clyde.setInitX(j * block);
                        clyde.setInitY(i * block);
                        clyde.setX(j * block);
                        clyde.setY(i * block);
                        ghosts.add(clyde);

                        System.out.println(j * block + ", " + i * block + "Clyde");
                        break;
                    case 'E':
                        gameObjects[i][j] = GameObject.Empty;
                        break;
                    case '#':
                        gameObjects[i][j] = GameObject.Wall;
                        break;
                    default:
                        System.err.println("Unknown character: " + curr);
                        gameObjects[i][j] = GameObject.Empty;
                }
            }
        }
        scareTimer = new Timer();
        blinkTimer = new Timer();
    }

    public Pacman getPacman() {
        return pacman;
    }

    public boolean isLevelDone() {
        return eatedCoins == allCoins;
    }

    public void loadNextLevel() {
        if (level >= Level.getSize()) {
            System.out.println("You done all levels");
        } else {
            this.level = level + 1;
        }
    }

    public boolean isCollision() {
        return isCollision;
    }

    public void resetCollision() {
        isCollision = false;
        isGhostBlink = false;
        isGhostEaten = false;
        stopBlinkTimer();
        makeGhostNotScarried();
        pacman.notEatGhosts();
    }

    private void makeGhostNotScarried() {
        for (Ghost ghost : ghosts) {
            ghost.makeNotScarried();
        }
    }

    public void handleKeyPress(Direction direction) {
        if (direction != Direction.NO_DIR) {
            pacman.setLastDirection(pacman.getDirection());
            pacman.setDirection(direction);
        }
    }

    private void moveInLastDirection() {
        pacman.move();
        int newX = pacman.getX();
        int newY = pacman.getY();

        if (newX < 0) {
            newX = (size - 1) * block;
            pacman.setX(newX);
        } else if (newX >= size * block) {
            newX = 0;
            pacman.setX(newX);
        }
        if (newY < 0) {
            newY = (size - 1) * block;
            pacman.setY(newY);
        } else if (newY >= size * block) {
            newY = 0;
            pacman.setY(newY);
        }

        int row = newY / block;
        int col = newX / block;

        GameObject currentCell = gameObjects[row][col];
        if (currentCell == GameObject.SmallCoin) {
            eatedCoins++;
            gameObjects[row][col] = GameObject.Empty;
            points += 10;
        }
        if (currentCell == GameObject.BigBerry) {
            eatedCoins++;
            gameObjects[row][col] = GameObject.Empty;
            points += 50;
            handleBigBerry();
        }
    }

    private void handleBigBerry() {
        scareTimeLeft += 10;
        if (scareTimeLeft > 10) {
            scareTimeLeft = 10;
        }
        for (Ghost ghost : ghosts) {
            ghost.makeScarried();
        }
        pacman.eatGhosts();
        startScareTimer();

        if (blinkTimer != null) {
            blinkTimer.cancel();
        }
    }

    private void startScareTimer() {
        if (scareTask != null) {
            scareTask.cancel();
        }
        scareTask = new TimerTask() {
            @Override
            public void run() {
                updateScareTime();
            }
        };
        scareTimer.scheduleAtFixedRate(scareTask, 1000, 1000);
    }

    private void updateScareTime() {
        if (scareTimeLeft > 0) {
            scareTimeLeft--;
        } else {
            scareTask.cancel();
            for (Ghost ghost : ghosts) {
                ghost.makeNotScarried();
                pacman.notEatGhosts();
            }

            startBlinkTimer();
        }
    }

    private void startBlinkTimer() {
        if (blinkTimer != null) {
            blinkTimer.cancel();
        }
        blinkTimer = new Timer();
        blinkTask = new TimerTask() {
            @Override
            public void run() {
                toggleGhostBlink();
            }
        };
        blinkTimer.scheduleAtFixedRate(blinkTask, 0, 500);
    }

    public void stopBlinkTimer() {
        blinkTimer.cancel();
    }

    private void toggleGhostBlink() {
        isGhostBlink = !isGhostBlink;
        Platform.runLater(() -> {
            for (Ghost ghost : ghosts) {
                ghost.makeScarried();
            }
        });
        if (!isGhostBlink) {
            blinkTask.cancel();
            Platform.runLater(() -> {
                for (Ghost ghost : ghosts) {
                    ghost.makeNotScarried();
                }
            });
        }
    }

    public int getPoints() {
        return points;
    }

    private boolean canEntityMoveInDirection(Entity entity, Direction direction) {
        if (direction == Direction.NO_DIR) {
            return false;
        }
        int dirDx = direction.getDx();
        int dirDy = direction.getDy();

        int nextX = entity.getX() + dirDx;
        int nextY = entity.getY() + dirDy;

        if (nextX < 0) {
            nextX = (size - 1) * block;
        } else if (nextX >= size * block) {
            nextX = 0;
        }
        if (nextY < 0) {
            nextY = (size - 1) * block;
        } else if (nextY >= size * block) {
            nextY = 0;
        }

        int rowTop = nextY / block;
        int colLeft = nextX / block;
        int rowBottom = (nextY + entity.getHeight() - 1) / block;
        int colRight = (nextX + entity.getWidth() - 1) / block;

        GameObject topLeftCell = gameObjects[rowTop][colLeft];
        GameObject topRightCell = gameObjects[rowTop][colRight];
        GameObject bottomLeftCell = gameObjects[rowBottom][colLeft];
        GameObject bottomRightCell = gameObjects[rowBottom][colRight];

        if (entity instanceof Ghost ghost && ghost.isHasLeftHouse()) {
            if (topLeftCell == GameObject.Chain || topRightCell == GameObject.Chain || bottomLeftCell == GameObject.Chain || bottomRightCell == GameObject.Chain) {
                return false;
            }
        }

        return topLeftCell != GameObject.Wall && topRightCell != GameObject.Wall && bottomLeftCell != GameObject.Wall && bottomRightCell != GameObject.Wall;
    }

    public void updatePacmanCoords() {
        if (pacman.getDirection() == Direction.NO_DIR) {
            return;
        }
        if (canEntityMoveInDirection(pacman, pacman.getDirection())) {
            pacman.setLastDirection(pacman.getDirection());
            updatePacmanDxDyforCurrDirection();
        }
        if (canEntityMoveInDirection(pacman, pacman.getLastDirection())) {
            moveInLastDirection();
        }
    }

    private void updatePacmanDxDyforCurrDirection() {
        Direction direction = pacman.getLastDirection();
        switch (direction) {
            case UP -> {
                pacman.setDx(0);
                pacman.setDy(pacman.isEatingGhosts() ? -2 : -1);
            }
            case DOWN -> {
                pacman.setDx(0);
                pacman.setDy(pacman.isEatingGhosts() ? 2 : 1);
            }
            case LEFT -> {
                pacman.setDx(pacman.isEatingGhosts() ? -2 : -1);
                pacman.setDy(0);
            }
            case RIGHT -> {
                pacman.setDx(pacman.isEatingGhosts() ? 2 : 1);
                pacman.setDy(0);
            }
        }
    }

    private Ghost getGhostByColor(GhostColor color) {
        for (Ghost ghost : ghosts) {
            if (ghost.getColor() == color) {
                return ghost;
            }
        }
        return null;
    }

    public void movePinkGhost() {
        Ghost pinkGhost = getGhostByColor(GhostColor.PINK);
        if (pinkGhost == null || !pinkGhost.isMoving()) {
            return;
        }

        int[] target = getPinkGhostTarget(pacman);
        int targetX = target[0];
        int targetY = target[1];
        if (pinkGhost.isScarried()) {
            moveGhostAwayFrom(pinkGhost, targetX, targetY);
        } else {
            moveGhostTowards(pinkGhost, targetX, targetY);
        }
    }

    public void moveBlueGhost() {
        Ghost blueGhost = getGhostByColor(GhostColor.BLUE);
        if (blueGhost == null || eatedCoins <= 30) {
            return;
        }

        int[] target = getBlueGhostTarget(getGhostByColor(GhostColor.BLUE));
        int targetX = target[0];
        int targetY = target[1];

        if (blueGhost.isScarried() && blueGhost.isHasLeftHouse() && eatedCoins > 30) {
            moveGhostAwayFrom(blueGhost, targetX, targetY);
        } else {
            moveGhostTowards(blueGhost, targetX, targetY);
        }
    }

    public void moveRedGhost() {
        Ghost redGhost = getGhostByColor(GhostColor.RED);
        if (redGhost == null) {
            return;
        }
        if (redGhost.isScarried() && redGhost.isHasLeftHouse()) {
            moveGhostAwayFrom(redGhost, pacman.getX(), pacman.getY());
        } else {
            moveGhostTowards(redGhost, pacman.getX(), pacman.getY());
        }
    }

    public void moveYellowGhost() {
        Ghost yellowGhost = getGhostByColor(GhostColor.YELLOW);
        if (yellowGhost == null || eatedCoins <= 0.33 * allCoins) {
            return;
        }

        int[] target = getYellowGhostTarget(pacman, yellowGhost);
        int targetX = target[0];
        int targetY = target[1];
        if (yellowGhost.isScarried() && yellowGhost.isHasLeftHouse() && eatedCoins <= 0.33 * allCoins) {
            moveGhostAwayFrom(yellowGhost, targetX, targetY);
        } else {
            moveGhostTowards(yellowGhost, targetX, targetY);
        }
    }

    private int[] getYellowGhostTarget(Pacman pacman, Ghost yellowGhost) {
        int pacmanX = pacman.getX();
        int pacmanY = pacman.getY();
        int yellowGhostX = yellowGhost.getX();
        int yellowGhostY = yellowGhost.getY();

        double distance = Math.sqrt(Math.pow(pacmanX - yellowGhostX, 2) + Math.pow(pacmanY - yellowGhostY, 2));

        if (distance > 8 * block) {
            return new int[]{pacmanX, pacmanY};
        } else {
            return new int[]{yellowGhost.getInitX(), yellowGhost.getInitY()};
        }
    }

    private int[] getBlueGhostTarget(Ghost blinky) {
        int pacmanX = pacman.getX();
        int pacmanY = pacman.getY();
        Direction direction = pacman.getDirection();
        int targetX = pacmanX;
        int targetY = pacmanY;

        switch (direction) {
            case UP -> {
                targetX -= 2 * block;
                targetY -= 2 * block;
            }
            case DOWN -> targetY += 2 * block;
            case LEFT -> targetX -= 2 * block;
            case RIGHT -> targetX += 2 * block;
        }

        int blinkyX = blinky.getX();
        int blinkyY = blinky.getY();
        int vectorX = targetX - blinkyX;
        int vectorY = targetY - blinkyY;
        int finalTargetX = blinkyX + 2 * vectorX;
        int finalTargetY = blinkyY + 2 * vectorY;

        return new int[]{finalTargetX, finalTargetY};
    }

    private int[] getPinkGhostTarget(Pacman pacman) {
        Direction direction = pacman.getDirection();
        int targetX = pacman.getX();
        int targetY = pacman.getY();

        switch (direction) {
            case UP -> targetY -= 4 * block;
            case DOWN -> targetY += 4 * block;
            case LEFT -> targetX -= 4 * block;
            case RIGHT -> targetX += 4 * block;
        }
        return new int[]{targetX, targetY};
    }

    private void moveGhostTowards(Ghost ghost, int targetX, int targetY) {
        int currX = ghost.getX();
        int currY = ghost.getY();

        if (ghost.isLeavingHouse()) {
            if (isOnChainCell(ghost)) {
                ghost.leaveHouse();
            } else {
                ghost.setDirection(Direction.UP);
            }
        } else {
            if (ghost.isScarried()) {
                moveGhostAwayFrom(ghost, targetX, targetY);
            } else {
                Direction newDirection = getTurnTowardsTarget(ghost, currX, currY, targetX, targetY);
                if (newDirection != Direction.NO_DIR) {
                    ghost.setDirection(newDirection);
                } else if (!canEntityMoveInDirection(ghost, ghost.getDirection())) {
                    newDirection = getNewDirectionTowardsTarget(ghost, currX, currY, targetX, targetY);
                    ghost.setDirection(newDirection);
                }
            }
        }

        ghost.move();
    }

    private void moveGhostAwayFrom(Ghost ghost, int targetX, int targetY) {
        int currX = ghost.getX();
        int currY = ghost.getY();

        if (ghost.isLeavingHouse()) {
            if (isOnChainCell(ghost)) {
                ghost.leaveHouse();
            } else {
                ghost.setDirection(Direction.UP);
            }
        } else {
            int vectorX = targetX - currX;
            int vectorY = targetY - currY;

            int awayX = currX - vectorX;
            int awayY = currY - vectorY;

            Direction newDirection = getTurnTowardsTarget(ghost, currX, currY, awayX, awayY);
            if (newDirection != Direction.NO_DIR) {
                ghost.setDirection(newDirection);
            } else if (!canEntityMoveInDirection(ghost, ghost.getDirection())) {
                newDirection = getNewDirectionTowardsTarget(ghost, currX, currY, awayX, awayY);
                ghost.setDirection(newDirection);
            }
        }

        ghost.move();
    }

    private boolean isOnChainCell(Ghost ghost) {
        int row = ghost.getY() / block;
        int col = ghost.getX() / block;
        return gameObjects[row][col] == GameObject.Chain;
    }

    private Direction getTurnTowardsTarget(Ghost ghost, int currX, int currY, int targetX, int targetY) {
        int diffX = currX - targetX;
        int diffY = currY - targetY;

        for (Direction direction : possibleDirections) {
            if (canEntityMoveInDirection(ghost, direction)) {
                int nextX = currX + direction.getDx() * block;
                int nextY = currY + direction.getDy() * block;
                int newDiffX = nextX - targetX;
                int newDiffY = nextY - targetY;

                if (Math.abs(newDiffX) < Math.abs(diffX) || Math.abs(newDiffY) < Math.abs(diffY)) {
                    return direction;
                }
            }
        }
        return Direction.NO_DIR;
    }

    private Direction getNewDirectionTowardsTarget(Ghost ghost, int currX, int currY, int targetX, int targetY) {
        int diffX = currX - targetX;
        int diffY = currY - targetY;
        Direction dirX = diffX > 0 ? Direction.LEFT : Direction.RIGHT;
        Direction dirY = diffY > 0 ? Direction.UP : Direction.DOWN;
        if (Math.abs(diffX) > Math.abs(diffY) && canEntityMoveInDirection(ghost, dirY)) {
            return dirY;
        } else if (canEntityMoveInDirection(ghost, dirX)) {
            return dirX;
        } else if (canEntityMoveInDirection(ghost, Direction.getOppositeDirection(dirX))) {
            return Direction.getOppositeDirection(dirX);
        } else {
            return Direction.getOppositeDirection(dirY);
        }
    }

    public void restartGame() {
        initialize();
        pacman.reset();
    }

    public boolean isGhostBlink() {
        return isGhostBlink;
    }

    public boolean isGhostEaten() {
        return isGhostEaten;
    }

    public void checkCollision() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() / block == ghost.getX() / block && pacman.getY() / block == ghost.getY() / block) {
                isCollision = true;
                handleCollisions(ghost);
            }
        }
    }

    public void handleCollisions(Ghost ghost) {
        if (ghost.isScarried()) {
            handleScaredCollision();
            isGhostEaten = true;
        } else {
            pacman.decreaseLives();
            handleNotScaredCollision();
        }
    }

    public void resetPositions() {
        pacman.reset();
        for (Ghost ghost : ghosts) {
            ghost.respawnGhost();
        }
    }

    public void stopScaring() {
        isGhostBlink = false;
    }

    private void handleScaredCollision() {
        points += 200;
    }

    private void handleNotScaredCollision() {
        pacman.decreaseLives();
        resetPositions();
    }
}
