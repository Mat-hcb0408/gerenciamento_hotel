package org.skywalkerhotel.skywalkerhotel.Model.Utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JanelaUtil {

    public static void trocarCenaComEstado(Stage stage, String caminhoFXML) {
        trocarCenaComEstado(stage, caminhoFXML, null);
    }

    public static void trocarCenaComEstado(Stage stage, String caminhoFXML, Object controller) {
        try {
            // Salva estado atual
            boolean maximizada = stage.isMaximized();
            double largura = stage.getWidth();
            double altura = stage.getHeight();

            FXMLLoader loader = new FXMLLoader(JanelaUtil.class.getResource(caminhoFXML));
            if (controller != null) {
                loader.setController(controller);
            }

            Parent root = loader.load();
            Scene novaCena = new Scene(root);

            stage.setScene(novaCena);

            // Aplique o estado da janela depois da troca de cena
            Platform.runLater(() -> {
                stage.setMaximized(maximizada);
                if (!maximizada) {
                    stage.setWidth(largura);
                    stage.setHeight(altura);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
