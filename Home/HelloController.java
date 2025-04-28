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
    public void initialize() {
        carregarImagem(); // Chamada ao iniciar a interface
    }

    @FXML
    public void carregarImagem() {
        try {
            String url = "https://classic.exame.com/wp-content/uploads/2024/08/ED1266_FOTOS18.jpg?quality=70&strip=info";
            Image imagem = new Image(url, true); // TRUE para carregar em background
            Praia_Imagem.setImage(imagem);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }
}