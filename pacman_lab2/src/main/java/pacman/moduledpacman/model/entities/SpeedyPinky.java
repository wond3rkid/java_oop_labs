package pacman.moduledpacman.model.entities;

//pink ghost
public class SpeedyPinky extends Ghost {
    public SpeedyPinky(int width, int height) {
        super(width, height);
        resetTime = 12000;
        dx = 1;
        dy = 0;
        color = GhostColor.PINK;
        spawnGhost();
    }
    }
