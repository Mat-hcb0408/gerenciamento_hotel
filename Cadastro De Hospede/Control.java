package org.example.cadastro_hotel;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private TextField quartoField;

    @FXML
    private TextField cpfField;

    @FXML
    private DatePicker checkinPicker;

    @FXML
    private DatePicker checkoutPicker;

    @FXML
    private Label valorLabel;

    @FXML
    private Button finalizarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
        configurarDatePickers();
    }

    public void carregarImagemLogo() {
        new Thread(() -> {
            try {
                String url = "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/main/SkyWalker%20Hot%C3%A9is_claro.png";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> Logo_Imagem.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }

    private void configurarEfeitosHover() {
        aplicarEfeitoHover(finalizarButton);
        aplicarEfeitoHover(cancelarButton);
    }

    private void configurarDatePickers() {
        // Configura os date pickers para não permitir edição manual
        checkinPicker.getEditor().setDisable(true);
        checkinPicker.getEditor().setOpacity(1);
        checkoutPicker.getEditor().setDisable(true);
        checkoutPicker.getEditor().setOpacity(1);
    }

    private void aplicarEfeitoHover(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }
}
