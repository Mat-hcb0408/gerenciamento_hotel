package org.example.projeto_hotel;

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
        carregarImagem();
    }

    @FXML
    public void carregarImagem() {
        try {
            String url = "https://cdn.palacepraia.com.br/wp-content/uploads/2022/11/hotel-8.webp"; // coloque aqui o link real da imagem
            Image imagem = new Image(url);
            Praia_Imagem.setImage(imagem);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }
}
