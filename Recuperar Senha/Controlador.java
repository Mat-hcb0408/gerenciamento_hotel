package org.example.demo3;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class HelloController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private Button enviarButton;

    @FXML
    private Hyperlink voltarLoginLink;

    @FXML
    private TextField emailField;

    @FXML
    public void initialize() {
        carregarImagemLogo();
        applyHoverEffect(enviarButton);
        configurarEventos();
    }

    private void carregarImagemLogo() {
        // Mesma implementação do seu controller original
        String url = "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/main/SkyWalker%20Hot%C3%A9is_claro.png";
        Logo_Imagem.setImage(new Image(url, true));
    }

    private void applyHoverEffect(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    private void configurarEventos() {
        enviarButton.setOnAction(event -> {
            // Lógica para enviar email de recuperação
            System.out.println("Email de recuperação enviado para: " + emailField.getText());
        });

        voltarLoginLink.setOnAction(event -> {
            try {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Scene scene = new Scene(loginRoot);
                Stage stage = (Stage) voltarLoginLink.getScene().getWindow();
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
