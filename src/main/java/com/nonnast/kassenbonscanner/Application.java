package com.nonnast.kassenbonscanner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main_window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Kassenbonscanner");
        stage.setScene(scene);
        var controller = (Controller)fxmlLoader.getController();
        controller.set_root_stage(stage);
        controller.register_callbacks();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}