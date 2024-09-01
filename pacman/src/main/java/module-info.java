module pacman.moduledpacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens pacman.moduledpacman to javafx.fxml;
    opens pacman.moduledpacman.controller to javafx.fxml;
    opens pacman.moduledpacman.view to javafx.fxml;

    exports pacman.moduledpacman.controller;
    exports pacman.moduledpacman;
}
