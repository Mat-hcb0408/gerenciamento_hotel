package org.example.cadastrodehospede;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private ImageView Logo_Imagem;


    @FXML
    private Button cadastrarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private Button editarButton;

    @FXML
    private Button excluirButton;

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
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
        // Atualizando a lista de botões
        Button[] botoes = { cadastrarButton, cancelarButton, editarButton, excluirButton };
        for (Button botao : botoes) {
            if (botao != null) {
                aplicarEfeitoHover(botao);
                aplicarEfeitoClique(botao);
            }
        }
    }

    private void aplicarEfeitoHover(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 0);");
        });

        button.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 0);");
        });
    }

    private void aplicarEfeitoClique(Button button) {
        ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), button);
        clickScale.setToX(0.95);
        clickScale.setToY(0.95);

        ScaleTransition releaseScale = new ScaleTransition(Duration.millis(100), button);
        releaseScale.setToX(1.0);
        releaseScale.setToY(1.0);

        button.setOnMousePressed(e -> clickScale.playFromStart());
        button.setOnMouseReleased(e -> releaseScale.playFromStart());
    }
}
