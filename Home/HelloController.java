package org.example.homehotel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ImageView Praia_Imagem;

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    public void initialize() {
        carregarImagemFundo(); // Chamada ao iniciar a interface
        carregarImagemLogo();
    }

    @FXML
    public void carregarImagemFundo() {
        try {
            String url = "https://classic.exame.com/wp-content/uploads/2024/08/ED1266_FOTOS18.jpg?quality=70&strip=info";
            Image imagem = new Image(url, true); // TRUE para carregar em background
            Praia_Imagem.setImage(imagem);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }
    @FXML
    public void carregarImagemLogo() {
        try {
            String url = "https://raw.githubusercontent.com/Mat-hcb0408/gerenciamento_hotel/refs/heads/main/SkyWalker%20Hot%C3%A9is.png";
            Image imagem = new Image(url, true); // TRUE para carregar em background
            Logo_Imagem.setImage(imagem);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }
}
