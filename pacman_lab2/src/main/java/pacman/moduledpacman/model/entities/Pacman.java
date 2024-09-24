package pacman.moduledpacman.model.entities;

import pacman.moduledpacman.model.Direction;


public class Pacman extends Entity {
    private int lives;
    private int initX, initY;
    private boolean isEatingGhosts;

    public Pacman(int width, int height) {
        super(width, height);
        lives = 3;
        block = 13;
        isEatingGhosts = false;
        isMoving = true;
    }

    public void setInitX(int x) {
        initX = x;
    }

    public void setInitY(int y) {
        initY = y;
    }

    public void resetLives() {
        this.lives = 3;
    }

    public void decreaseLives() {
        lives = lives - 1;
        if (lives == 0) {
            System.out.println("You died");
            isMoving = false;
        }
    }

    public int getLives() {
        return lives;
    }

    public void reset() {
        dx = initDx;
        dy = initDy;
        x = initX;
        y = initY;
        direction = Direction.NO_DIR;
        lastDirection = Direction.NO_DIR;
    }

    public boolean isEatingGhosts() {
        return isEatingGhosts;
    }

    public void eatGhosts() {
        isEatingGhosts = true;
    }

    public void notEatGhosts() {
        isEatingGhosts = false;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
