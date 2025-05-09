package org.skywalkerhotel.skywalkerhotel.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/skywalkerhotel/skywalkerhotel/Images/icone.png")));
        stage.setTitle("Skywalker Hotel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}