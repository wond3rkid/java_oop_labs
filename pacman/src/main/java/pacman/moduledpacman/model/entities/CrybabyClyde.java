package pacman.moduledpacman.model.entities;

public class CrybabyClyde extends Ghost {
    public CrybabyClyde(int width, int height) {
        super(width, height);
        resetTime = 6000;
        dx = 0;
        dy = 1;
        color = GhostColor.YELLOW;
        spawnGhost();
    }
}
