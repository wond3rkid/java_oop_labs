package pacman.moduledpacman.model.entities;

//blue ghost
public class StylistInky extends Ghost {
    public StylistInky(int width, int height) {
        super(width, height);
        resetTime = 6000;
        dx = 0;
        dy = 1;
        color = GhostColor.BLUE;
        spawnGhost();
    }

}
