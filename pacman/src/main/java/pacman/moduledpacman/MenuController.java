package pacman.moduledpacman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pacman.moduledpacman.controller.PacmanController;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label lmenu;
    private boolean rules_clicked = false;
    private boolean about_clicked = false;
    private boolean start_clicked = false;

    private Scene getScene(String view, double h, double w) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PacmanApplication.class.getResource(view));
        return new Scene(fxmlLoader.load(), h, w);
    }

    @FXML
    protected void onRulesButtonClick() throws Exception {
        lmenu.setText("Oh, good, you want to know more about rules, well...");
        if (!rules_clicked) {
            rules_clicked = true;
            Scene scene = getScene("rules-view.fxml", 900, 500);
            Stage stage = new Stage();
            stage.setTitle("Pacman Rules");
            stage.setScene(scene);
            stage.getIcons().add(new Image(String.valueOf(PacmanApplication.class.getResource("icon.png"))));
            stage.show();
            stage.setOnCloseRequest(event -> rules_clicked = false);
        }
    }

    @FXML
    protected void onAboutBottomClick() throws Exception {
        lmenu.setText("I'm glad you're interested in learning about how I created this game!");
        if (!about_clicked) {
            about_clicked = true;
            Scene scene = getScene("fxml/about-view.fxml", 500, 300);
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.getIcons().add(new Image(String.valueOf(PacmanApplication.class.getResource("images/icon.png"))));
            stage.show();
            stage.setOnCloseRequest(event -> about_clicked = false);
        }
    }

    @FXML
    private void handleButtonClick(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        int level = Integer.parseInt(buttonText);
        loadLevel(level);
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        stage.close();
    }

    private void loadLevel(int level) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/pacman-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(PacmanApplication.class.getResource("images/icon.png"))));
        PacmanController controller = loader.getController();
        controller.setLevel(level);
        root.setOnKeyPressed(controller);
        Scene scene = new Scene(root, 1000, 800);
        stage.setScene(scene);
        stage.show();
        root.requestFocus();
        stage.setOnCloseRequest(event -> {
            controller.stopGame();
            start_clicked = false;
        });
    }

    @FXML
    protected void onPlayButtonClick() throws IOException {
        if (!start_clicked) {
            start_clicked = true;
            Scene scene = getScene("fxml/level-select-view.fxml", 400, 400);
            Stage stage = new Stage();
            stage.setTitle("Level selector");
            stage.setScene(scene);
            stage.getIcons().add(new Image(String.valueOf(PacmanApplication.class.getResource("images/icon.png"))));
            stage.show();
            stage.setOnCloseRequest(event -> start_clicked = false);
        }
    }
}