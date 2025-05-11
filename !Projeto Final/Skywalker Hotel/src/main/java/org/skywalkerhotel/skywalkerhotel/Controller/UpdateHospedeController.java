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
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Hospedes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class UpdateHospedeController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private TextField nomeHospedeField;

    @FXML
    private DatePicker nascimentoPicker;

    @FXML
    private ComboBox<String> tipoPessoaComboBox;

    @FXML
    private TextField tipoPessoaField;

    @FXML
    private TextField telefoneField;

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    private Hospedes hospedeSelecionado;
    private Runnable onUpdateCallback;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void setHospede(Hospedes hospedes, Runnable onUpdateCallback) {
        this.hospedeSelecionado=hospedes;
        this.onUpdateCallback=onUpdateCallback;

        nomeHospedeField.setText(hospedes.getNome());
        nascimentoPicker.setValue(LocalDate.parse(hospedes.getNascimento()));
        telefoneField.setText(hospedes.getTelefone());
        tipoPessoaField.setText(hospedes.getCpf().isEmpty() ? hospedes.getCnpj():hospedes.getCpf());
        tipoPessoaComboBox.setValue(hospedes.getCpf().isEmpty() ? "CNPJ":"CPF");
    }

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
        configurarListeners();
        popularComboBox();
    }

    private void popularComboBox(){
        ObservableList<String> tipos= FXCollections.observableArrayList("CNPJ","CPF");
        tipoPessoaComboBox.setItems(tipos);
        tipoPessoaComboBox.setValue("CPF");

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
        salvarButton.setOnAction(e-> handleSalvarAction());
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
        if (hospedeSelecionado==null){
            alerta("Erro", "Nenhum hospede selecionado.", "Selecione um hospede para atualizar.");
            return;
        }

        String nome = nomeHospedeField.getText().trim();
        String nascimento = (nascimentoPicker.getValue() !=null?nascimentoPicker.getValue().toString():"");
        String telefone =  telefoneField.getText();
        String tipoPessoa = tipoPessoaField.getText().trim();

        if (tipoPessoa.isEmpty()) {
            alerta("Erro de formato","Formato inválido!","Preencha o campo CPF/CNPJ.");
            return;
        }

        String tipoSelecionado=tipoPessoaComboBox.getValue();

        if (nome.isEmpty() || nascimento.isEmpty() || telefone.isEmpty() || tipoSelecionado.isEmpty() || tipoPessoaField.getText().trim().isEmpty()) {
            alerta("Erro", "Campos obrigatórios em branco.", "Preencha todos os campos.");
            return;
        }
        String updateQuery = "UPDATE hospedes SET " +
                "nome=?, " +
                "nascimento=?, " +
                "telefone=?, " +
                "cpf=?, " +
                "cnpj=?" +
                "WHERE id_hospede=?";

        try(Connection conn=Conexao.getConnection();
            PreparedStatement pstmt=conn.prepareStatement(updateQuery)){
            pstmt.setString(1,nome);
            pstmt.setString(2, nascimento);
            pstmt.setString(3, telefone);
            pstmt.setString(4,tipoSelecionado.equals("CPF") ? tipoPessoa: "");
            pstmt.setString(5,tipoSelecionado.equals("CNPJ") ? tipoPessoa: "");
            pstmt.setInt(6,hospedeSelecionado.getId());

            pstmt.executeUpdate();
            successAlert("Sucesso","Atualização realizado com sucesso!");

            if (onUpdateCallback!=null){
                onUpdateCallback.run();
            }

            Stage stage=(Stage) salvarButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            alerta("Erro ao atualizar","Erro ao atualizar no banco de dados","Não foi possível atualizar o hóspede.");
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