package pacman.moduledpacman.model.entities;

import pacman.moduledpacman.model.Direction;
import pacman.moduledpacman.model.PacmanModel;

public class ChaserBlinky extends Ghost {
    public ChaserBlinky(int width, int height) {
        super(width, height);
        color = GhostColor.RED;
        spawnGhost();
        leaveHouse();
    }
    @Override
    public void respawnGhost() {
        makeNotScarried();
        makeNotMoving();
        leaveHouse();
        setInitCoords();
        setDirection(Direction.NO_DIR);
        makeMovingAfterReset();
        System.out.println("Ghost respawned : " + this.color + " coords: " + this.initX + ", " + this.initY);
    }
}
