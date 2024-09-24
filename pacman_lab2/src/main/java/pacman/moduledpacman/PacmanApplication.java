package pacman.moduledpacman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import pacman.moduledpacman.controller.PacmanController;

import java.io.IOException;

public class PacmanApplication extends Application {

    private static PacmanController pacmanController;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Start Pacman Application");
        FXMLLoader fxmlLoader = new FXMLLoader(PacmanApplication.class.getResource("fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.getIcons().add(new Image(String.valueOf(PacmanApplication.class.getResource("images/icon.png"))));
        stage.setTitle("Pacman Game");
        stage.setScene(scene);
        stage.show();

        // Установка обработчика закрытия окна
        stage.setOnCloseRequest(event -> {
            if (pacmanController != null) {
                pacmanController.stopGame();
            }
        });
    }

    @Override
    public void stop() {
        if (pacmanController != null) {
            pacmanController.stopGame();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
