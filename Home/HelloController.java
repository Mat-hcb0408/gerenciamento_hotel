package org.example.homehotel;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    // Botões da barra superior
    @FXML
    private Button menuButton1;
    @FXML
    private Button menuButton2;
    @FXML
    private Button menuButton3;
    @FXML
    private Button menuButton4;
    @FXML
    private Button loginButton;

    // Botões principais
    @FXML
    private Button disponibilidadeButton;

    @FXML
    public void initialize() {
        try {
            configurarTelaCheia();
            carregarImagemFundo();
            carregarImagemLogo();

            // Aplica animações em todos os botões
            applyHoverEffectsToAllButtons();
        } catch (Exception e) {
            System.err.println("Erro na inicialização: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarTelaCheia() {
        if (rootPane != null) {
            rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                        if (newWindow != null) {
                            Stage stage = (Stage) newWindow;
                            rootPane.prefWidthProperty().bind(stage.widthProperty());
                            rootPane.prefHeightProperty().bind(stage.heightProperty());

                            if (Praia_Imagem != null) {
                                Praia_Imagem.fitWidthProperty().bind(stage.widthProperty());
                                Praia_Imagem.fitHeightProperty().bind(stage.heightProperty());
                            }
                        }
                    });
                }
            });
        }
    }

    private void applyHoverEffectsToAllButtons() {
        // Lista de todos os botões que devem receber o efeito
        Button[] allButtons = {
                menuButton1, menuButton2, menuButton3, menuButton4,
                loginButton, disponibilidadeButton
        };

        for (Button button : allButtons) {
            if (button != null) {
                applyStandardHoverEffect(button);
                applyClickEffect(button);
            }
        }
    }

    private void applyStandardHoverEffect(Button button) {
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

    private void applyClickEffect(Button button) {
        ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), button);
        clickScale.setToX(0.95);
        clickScale.setToY(0.95);

        ScaleTransition releaseScale = new ScaleTransition(Duration.millis(100), button);
        releaseScale.setToX(1.0);
        releaseScale.setToY(1.0);

        button.setOnMousePressed(e -> clickScale.playFromStart());
        button.setOnMouseReleased(e -> releaseScale.playFromStart());
    }

    public void carregarImagemFundo() {
        new Thread(() -> {
            try {
                String url = getClass().getResource("/org/example/homehotel/images/background.jpg") != null ?
                        getClass().getResource("/org/example/homehotel/images/background.jpg").toString() :
                        "https://classic.exame.com/wp-content/uploads/2024/08/ED1266_FOTOS18.jpg";

                Image imagem = new Image(url, true);
                Platform.runLater(() -> {
                    if (Praia_Imagem != null) {
                        Praia_Imagem.setImage(imagem);
                        Praia_Imagem.setPreserveRatio(false);
                    }
                });
            } catch (Exception e) {
                System.err.println("Erro ao carregar a imagem do fundo: " + e.getMessage());
            }
        }).start();
    }

    public void carregarImagemLogo() {
        new Thread(() -> {
            try {
                String url = getClass().getResource("/org/example/homehotel/images/logo.png") != null ?
                        getClass().getResource("/org/example/homehotel/images/logo.png").toString() :
                        "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/main/SkyWalker%20Hot%C3%A9is_claro.png";

                Image imagem = new Image(url, true);
                Platform.runLater(() -> {
                    if (Logo_Imagem != null) {
                        Logo_Imagem.setImage(imagem);
                    }
                });
            } catch (Exception e) {
                System.err.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }
}
