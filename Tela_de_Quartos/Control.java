package org.ativ12.quartos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    @FXML
    private ImageView imagem1;

    @FXML
    private ImageView imagem2;

    @FXML
    private ImageView imagem3;

    @FXML
    private TextField txtPesquisar;

    @FXML
    public void initialize(){
        imagem1();
        imagem2();
        imagem3();
    }


    public void imagem1() {
        new Thread(() -> {
            try {
                String url = "https://webdesignmastery.github.io/Hotel_Miranda_16-10-23/assets/room-1.jpg";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> imagem1.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }

    public void imagem2() {
        new Thread(() -> {
            try {
                String url = "https://webdesignmastery.github.io/Hotel_Miranda_16-10-23/assets/room-2.jpg";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> imagem2.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }

    public void imagem3() {
        new Thread(() -> {
            try {
                String url = "https://webdesignmastery.github.io/Hotel_Miranda_16-10-23/assets/room-3.jpg";
                Image imagem = new Image(url, true);
                Platform.runLater(() -> imagem3.setImage(imagem));
            } catch (Exception e) {
                System.out.println("Erro ao carregar a imagem do logo: " + e.getMessage());
            }
        }).start();
    }
}
