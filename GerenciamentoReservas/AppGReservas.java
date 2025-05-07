package org.example.demo3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Certifique-se de que o caminho está correto!
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/demo3/reservas-view.fxml"));

        // Carrega a cena e aplica ao estágio
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Hotel Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
