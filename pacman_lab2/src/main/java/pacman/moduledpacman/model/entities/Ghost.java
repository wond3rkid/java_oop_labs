package pacman.moduledpacman.model.entities;

import javafx.application.Platform;
import pacman.moduledpacman.model.Direction;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Ghost extends Entity {
    int resetTime = 2000; // Время ожидания перед началом движения (в миллисекундах)
    GhostColor color;
    private boolean hasLeftHouse, isScarried, isLeavingHouse;
    private int sprite;

    public Ghost(int width, int height) {
        super(width, height);
        color = GhostColor.DEFAULT;
        hasLeftHouse = false;
        isLeavingHouse = true;
        makeNotMoving();
    }

    public void setNotLeftHouse() {
        isLeavingHouse = true;
        hasLeftHouse = false;
    }

    public boolean isHasLeftHouse() {
        return hasLeftHouse;
    }

    public void leaveHouse() {
        this.hasLeftHouse = true;
        this.isLeavingHouse = false;
    }

    public boolean isLeavingHouse() {
        return isLeavingHouse;
    }

    public GhostColor getColor() {
        return color;
    }

    public boolean isScarried() {
        return isScarried;
    }

    public void makeScarried() {
        isScarried = true;
    }

    public void makeNotScarried() {
        isScarried = false;
    }

    protected void makeMovingAfterReset() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    isMoving = true;
                });
            }
        }, resetTime);
    }

    public void spawnGhost() {
        makeNotMoving();
        makeMovingAfterReset();
    }

    public void respawnGhost() {
        makeNotScarried();
        makeNotMoving();
        setNotLeftHouse();
        setInitCoords();
        setDirection(Direction.NO_DIR);
        makeMovingAfterReset();
        System.out.println("Ghost respawned : " + this.color + " coords: " + this.initX + ", " + this.initY);
    }

    public int updateSprite() {
        return (sprite++) % 2;
    }
}
