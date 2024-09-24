package pacman.moduledpacman.model;

import pacman.moduledpacman.model.entities.*;

public class MovingRules {

    public static void moveBlueGhost(Ghost inky, Pacman pacman, Ghost blinky, GameObject[][] gameObjects, int blockSize) {
        int targetX = 2 * pacman.getX() - blinky.getX();
        int targetY = 2 * pacman.getY() - blinky.getY();
        moveGhostTowards(inky, targetX, targetY, gameObjects, blockSize);
    }

    public static void moveRedGhost(Ghost blinky, Pacman pacman, GameObject[][] gameObjects, int blockSize) {
        moveGhostTowards(blinky, pacman.getX(), pacman.getY(), gameObjects, blockSize);
    }

    public static void movePinkGhost(Ghost pinky, Pacman pacman, GameObject[][] gameObjects, int blockSize) {
        int targetX = pacman.getX();
        int targetY = pacman.getY();
        Direction pacmanDirection = pacman.getDirection();

        switch (pacmanDirection) {
            case UP -> {
                targetX -= 4 * pinky.getBlock();
                targetY -= 4 * pinky.getBlock();
            }
            case DOWN -> targetY += 4 * pinky.getBlock();
            case LEFT -> targetX -= 4 * pinky.getBlock();
            case RIGHT -> targetX += 4 * pinky.getBlock();
        }
        moveGhostTowards(pinky, targetX, targetY, gameObjects, blockSize);
    }

    public static void moveYellowGhost(Ghost clyde, Pacman pacman, GameObject[][] gameObjects, int blockSize) {
        int distance = calculateDistance(clyde.getX(), clyde.getY(), pacman.getX(), pacman.getY());
        if (distance > 8 * clyde.getBlock()) {
            moveGhostTowards(clyde, pacman.getX(), pacman.getY(), gameObjects, blockSize);
        } else {
            moveGhostTowards(clyde, clyde.getInitX(), clyde.getInitY(), gameObjects, blockSize);
        }
    }

    private static void moveGhostTowards(Ghost ghost, int targetX, int targetY, GameObject[][] gameObjects, int blockSize) {
        int dx = targetX - ghost.getX();
        int dy = targetY - ghost.getY();

        Direction newDirection = ghost.getDirection();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                newDirection = Direction.RIGHT;
            } else if (dx < 0) {
                newDirection = Direction.LEFT;
            }
        } else {
            if (dy > 0) {
                newDirection = Direction.DOWN;
            } else if (dy < 0) {
                newDirection = Direction.UP;
            }
        }

        if (canMoveInDirection(ghost, newDirection, gameObjects, blockSize)) {
            ghost.setDirection(newDirection);
            ghost.move();
        }
    }

    private static boolean canMoveInDirection(Ghost ghost, Direction direction, GameObject[][] gameObjects, int blockSize) {
        int nextX = ghost.getX();
        int nextY = ghost.getY();

        switch (direction) {
            case UP -> nextY -= blockSize;
            case DOWN -> nextY += blockSize;
            case LEFT -> nextX -= blockSize;
            case RIGHT -> nextX += blockSize;
        }

        int rowTop = nextY / blockSize;
        int colLeft = nextX / blockSize;
        int rowBottom = (nextY + ghost.getHeight() - 1) / blockSize;
        int colRight = (nextX + ghost.getWidth() - 1) / blockSize;

        if (rowTop < 0 || rowBottom >= gameObjects.length || colLeft < 0 || colRight >= gameObjects[0].length) {
            return false;
        }

        GameObject topLeftCell = gameObjects[rowTop][colLeft];
        GameObject topRightCell = gameObjects[rowTop][colRight];
        GameObject bottomLeftCell = gameObjects[rowBottom][colLeft];
        GameObject bottomRightCell = gameObjects[rowBottom][colRight];

        return topLeftCell != GameObject.Wall &&
                topRightCell != GameObject.Wall &&
                bottomLeftCell != GameObject.Wall &&
                bottomRightCell != GameObject.Wall;
    }

    private static int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}
