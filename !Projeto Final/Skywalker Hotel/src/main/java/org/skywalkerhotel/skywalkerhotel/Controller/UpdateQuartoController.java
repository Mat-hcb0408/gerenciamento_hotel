package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Quartos;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateQuartoController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private TextField nomeQuartoField;

    @FXML
    private ComboBox<Integer> qtdCamaSolteiro;

    @FXML
    private ComboBox<Integer> qtdCamaCasal;

    @FXML
    private Label maxPessoas;

    @FXML
    private ComboBox<String> status;

    @FXML
    private TextField valorQuartoField;


    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    private Quartos quartoSelecionado;
    private Runnable onUpdateCallback;
    @FXML
    public void setQuarto(Quartos quarto,Runnable onUpdateCallback) {
        this.quartoSelecionado=quarto;
        this.onUpdateCallback=onUpdateCallback;

        nomeQuartoField.setText(quarto.getNome());
        qtdCamaSolteiro.setValue(quarto.isSolteiro());
        qtdCamaCasal.setValue(quarto.isCasal());
        maxPessoas.setText(String.valueOf(quarto.getMaxPessoas()));
        valorQuartoField.setText(String.format("%.2f",quarto.getPreco()));
        status.setValue(quarto.getStatus());
    }

    @FXML
    private void initialize(){
        carregarImagemLogo();
        configurarEfeitosHover();
        configurarListeners();
        popularComboBox();
    }

    private void popularComboBox(){
        ObservableList<Integer> qtdCamas= FXCollections.observableArrayList(0,1,2,3);
        qtdCamaSolteiro.setItems(qtdCamas);
        qtdCamaCasal.setItems(qtdCamas);

        if (!qtdCamaSolteiro.getItems().isEmpty()){
            qtdCamaSolteiro.setValue(qtdCamaSolteiro.getItems().get(0));
        }
        if (!qtdCamaCasal.getItems().isEmpty()){
            qtdCamaCasal.setValue(qtdCamaCasal.getItems().get(0));
        }

        ObservableList<String> statusQuarto= FXCollections.observableArrayList("Livre","Ocupado","Manutenção");
        status.setItems(statusQuarto);
        status.setValue(status.getItems().get(0));

    }
    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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
        aplicarEfeitoHover(salvarButton);
        aplicarEfeitoHover(cancelarButton);
    }

    private void configurarListeners() {
        qtdCamaSolteiro.setOnAction(e->updateMaxPessoas());
        qtdCamaCasal.setOnAction(e->updateMaxPessoas());
        salvarButton.setOnAction(e-> handleSalvarAction());
    }

    private void updateMaxPessoas(){
        Integer camasSolteiro= (Integer) qtdCamaSolteiro.getValue();
        Integer camasCasal= (Integer) qtdCamaCasal.getValue();

        if (camasSolteiro==null){
            camasSolteiro=0;
        }
        if (camasCasal==null){
            camasCasal=0;
        }

        Integer capacity=(camasSolteiro*1)+(camasCasal*2);
        maxPessoas.setText(String.valueOf(capacity));
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

    @FXML
    private void handleSalvarAction() {
        if (quartoSelecionado==null){
        alerta("Erro", "Nenhum quarto selecionado.", "Selecione um quarto para atualizar.");
        return;
        }

        String nomeQuarto = nomeQuartoField.getText().trim();
        Integer camasSolteiro =  qtdCamaSolteiro.getValue();
        Integer camasCasal =  qtdCamaCasal.getValue();
        String valorQuartoStr = valorQuartoField.getText().trim();
        String statusComboBox =  status.getValue();


        try{
        }catch (NumberFormatException e){
        alerta("Erro", "Valor inválido.", "Insira um valor numérico válido para o preço.");
        return;
        }

        if (nomeQuarto.isEmpty() || valorQuartoStr.isEmpty() || (camasSolteiro == 0 && camasCasal == 0)) {
            alerta("Erro de formato","Formato inválido!","Preencha todos os campos e selecione pelo menos uma cama.");
        }

        Double valorQuarto;
        try {valorQuarto=Double.parseDouble(valorQuartoStr);
            if (valorQuarto<=0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            alerta("Erro de formato","Valor inválido!","O valor do quarto deve ser um número positivo.");
            return;
        }

        Integer capacidade=(camasSolteiro*1)+(camasCasal*2);

        String insertQuery="UPDATE quartos SET " +
                "nome_quarto=?," +
                "cama_casal_quarto=?," +
                "cama_solteiro_quarto=?," +
                "quant_pessoas=?," +
                "status_quarto=?," +
                "preco_quarto=? WHERE id_quarto=?";

        try(Connection conn=Conexao.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(insertQuery)){
            pstmt.setString(1,nomeQuarto);
            pstmt.setInt(2,camasCasal);
            pstmt.setInt(3,camasSolteiro);
            pstmt.setInt(4,capacidade);
            pstmt.setString(5,statusComboBox);
            pstmt.setDouble(6,valorQuarto);
            pstmt.setDouble(7,quartoSelecionado.getIdQuarto());

            pstmt.executeUpdate();
            successAlert("Sucesso","Atualização realizado com sucesso!");

            if (onUpdateCallback!=null){
                onUpdateCallback.run();
            }

            Stage stage=(Stage) salvarButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            alerta("Erro ao atualizar","Erro ao atualizar no banco de dados","Não foi possível atualizar o quarto.");
            e.printStackTrace();
        }
    }
    private void alerta(String titulo,String cabecalho, String mensagem){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void successAlert(String titulo,String cabecalho){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.showAndWait();
    }}