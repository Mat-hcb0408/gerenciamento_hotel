package org.example.homehotel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private ImageView Praia_Imagem;
    @FXML
    private ImageView Logo_Imagem;
    @FXML
    private AnchorPane rootPane; // Adicione esta referência

    @FXML
    public void initialize() {
        // Configura o redimensionamento automático
        configurarTelaCheia();
        carregarImagemFundo();
        carregarImagemLogo();
    }


    private void configurarTelaCheia() {
        // Vincula o tamanho do AnchorPane ao tamanho da janela
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obs2, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        Stage stage = (Stage) newWindow;
                        // Faz o AnchorPane acompanhar o tamanho da janela
                        rootPane.prefWidthProperty().bind(stage.widthProperty());
                        rootPane.prefHeightProperty().bind(stage.heightProperty());

                        // Configura a imagem de fundo para preencher todo o espaço
                        Praia_Imagem.fitWidthProperty().bind(stage.widthProperty());
                        Praia_Imagem.fitHeightProperty().bind(stage.heightProperty());
                    }
                });
            }
        });
    }

    @FXML
    public void carregarImagemFundo() {
        new Thread(() -> {
            try {
                String url = "https://classic.exame.com/wp-content/uploads/2024/08/ED1266_FOTOS18.jpg?quality=70&strip=info";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> {
                    Praia_Imagem.setImage(imagem);
                    Praia_Imagem.setPreserveRatio(false); // Para preencher todo o espaço
                });
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do fundo: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    public void carregarImagemLogo() {
        new Thread(() -> {
            try {
                String url = "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/refs/heads/main/SkyWalker%20Hot%C3%A9is.png";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> Logo_Imagem.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }
}
