package org.example.projeto_hotel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {

    @FXML
    private Button entrar;
    @FXML
    private Button cadastreSe;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField senhaField;
    @FXML
    private ImageView Fundo_Praia;

    @FXML
    public void initialize() {
        carregarImagem(); // Chamada ao iniciar a interface
    }

    @FXML
    public void carregarImagem() {
        try {
            String url = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/28/24/c5/16/rooftop-swimming-pool.jpg?w=1200&h=-1&s=1"; // coloque aqui o link real da imagem
            Image imagem = new Image(url);
            Fundo_Praia.setImage(imagem);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: " + e.getMessage());
        }
    }

    // Outros métodos para manipular a interface de login
}
