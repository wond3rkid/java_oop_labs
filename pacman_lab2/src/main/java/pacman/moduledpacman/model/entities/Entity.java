package pacman.moduledpacman.model.entities;

import pacman.moduledpacman.model.Direction;

public abstract class Entity {
    protected int x = 0, y = 0;
    protected int dx = 0, dy = 0;
    protected int initDx, initDy;
    protected int initX, initY;
    protected int width, height;
    boolean isMoving;
    protected int block;
    protected Direction direction, lastDirection;

    public Entity(int width, int height) {
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.dy = 0;
        this.initDx = 0;
        this.initDy = 0;
        isMoving = false;
        block = Math.max(width, height);
        direction = Direction.NO_DIR;
        lastDirection = Direction.NO_DIR;
    }

    public void setInitX(int x) {
        this.initX = x;
    }

    public void setInitY(int y) {
        this.initY = y;
    }

    public int getInitX() {
        return initX;
    }

    public int getInitY() {
        return initY;
    }

    public int getBlock() {
        return block;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        updateDxDy();
    }

    public void makeNotMoving() {
        isMoving = false;
    }

    public void move() {
        if (!isMoving) {
            return;
        }
        y = y + dy;
        x = x + dx;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction dir) {
        lastDirection = dir;
    }

    protected void updateDxDy() {
        dx = direction.getDx();
        dy = direction.getDy();
    }

    protected void setInitCoords() {
        x = initX;
        y = initY;
    }

    protected void setInitSpeed() {
        this.dx = initDx;
        this.dy = initDy;
    }

    protected void setNewSpeed(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

}
