package org.example.homehotel;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private ImageView Praia_Imagem;

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button reservarButton;

    @FXML
    private Button disponibilidadeButton;

    @FXML
    private Button menuButton1; // Gerenciar reservas
    @FXML
    private Button menuButton2; // Quartos
    @FXML
    private Button menuButton3; // Hóspedes
    @FXML
    private Button menuButton4; // Pagamentosf
    @FXML
    public void initialize() {
        configurarTelaCheia();
        carregarImagemFundo();
        carregarImagemLogo();

        // Aplicar animação de hover a todos os botões
        applyHoverEffect(reservarButton);
        applyHoverEffect(disponibilidadeButton);
        applyHoverEffect(menuButton1);
        applyHoverEffect(menuButton2);
        applyHoverEffect(menuButton3);
        applyHoverEffect(menuButton4);
    }

    private void configurarTelaCheia() {
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        rootPane.prefWidthProperty().bind(stage.widthProperty());
                        rootPane.prefHeightProperty().bind(stage.heightProperty());

                        Praia_Imagem.fitWidthProperty().bind(stage.widthProperty());
                        Praia_Imagem.fitHeightProperty().bind(stage.heightProperty());
                    }
                });
            }
        });
    }

    public void carregarImagemFundo() {
        new Thread(() -> {
            try {
                String url = "https://classic.exame.com/wp-content/uploads/2024/08/ED1266_FOTOS18.jpg?quality=70&strip=info";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> {
                    Praia_Imagem.setImage(imagem);
                    Praia_Imagem.setPreserveRatio(false);
                });
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do fundo: " + e.getMessage());
            }
        }).start();
    }

    public void carregarImagemLogo() {
        new Thread(() -> {
            try {
                String url = "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/refs/heads/main/SkyWalker%20Hot%C3%A9is_claro.png";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> Logo_Imagem.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }

    private void applyHoverEffect(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> scaleUp.playFromStart());
        button.setOnMouseExited(e -> scaleDown.playFromStart());
    }
}
