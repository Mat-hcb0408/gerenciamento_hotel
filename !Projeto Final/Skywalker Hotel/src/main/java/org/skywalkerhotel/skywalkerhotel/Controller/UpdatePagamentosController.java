package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Pagamentos;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdatePagamentosController {

    @FXML private ImageView Logo_Imagem;

    @FXML private DatePicker dataPagamentoPicker;
    @FXML private TextField descricaoField;
    @FXML private TextField tipoDespesaField;
    @FXML private TextField precoQuartoField;
    @FXML private TextField valorPagamentoField;
    @FXML private TextField totalPagamentoField;

    @FXML
    private TableView<Pagamentos> pagamentosTable;

    @FXML
    public void initialize(){
        carregarImagemLogo();
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

    private Pagamentos pagamentoAtual;

    public void setPagamento(Pagamentos pagamento) {
        this.pagamentoAtual = pagamento;
        preencherCampos(pagamento);
    }

    private Runnable onSaveCallback;

    public void setPagamento(Pagamentos pagamento, Runnable onSaveCallback) {
        this.pagamentoAtual = pagamento;
        this.onSaveCallback = onSaveCallback;

        // Preenche os campos da tela com os dados do pagamento
        dataPagamentoPicker.setValue(pagamento.getDataPagamento());
        descricaoField.setText(pagamento.getDescricaoPagamento());
        tipoDespesaField.setText(pagamento.getTipoDespesa());
        precoQuartoField.setText(String.valueOf(pagamento.getPrecoQuarto()));
        valorPagamentoField.setText(String.valueOf(pagamento.getValorPagamento()));
        totalPagamentoField.setText(String.valueOf(pagamento.getTotalPagamento()));
    }




    private void preencherCampos(Pagamentos pagamento) {
        dataPagamentoPicker.setValue(pagamento.getDataPagamento());
        descricaoField.setText(pagamento.getDescricaoPagamento());
        tipoDespesaField.setText(pagamento.getTipoDespesa());
        precoQuartoField.setText(String.valueOf(pagamento.getPrecoQuarto()));
        valorPagamentoField.setText(String.valueOf(pagamento.getValorPagamento()));
        totalPagamentoField.setText(String.valueOf(pagamento.getTotalPagamento()));
    }

    @FXML
    private void handleSalvar() {
        if (!validarCampos()) return;

        try (Connection conn = Conexao.getConnection()) {
            String updateSQL = "UPDATE pagamentos SET data_pagamento = ?, descricao_pagamento = ?, tipo_despesa = ?, preco_quarto = ?, valor_pagamento = ?, total_pagamento = ? WHERE id_pagamento = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSQL);

            stmt.setDate(1, java.sql.Date.valueOf(dataPagamentoPicker.getValue()));
            stmt.setString(2, descricaoField.getText());
            stmt.setString(3, tipoDespesaField.getText());
            stmt.setDouble(4, Double.parseDouble(precoQuartoField.getText()));
            stmt.setDouble(5, Double.parseDouble(valorPagamentoField.getText()));
            stmt.setDouble(6, Double.parseDouble(totalPagamentoField.getText()));
            stmt.setInt(7, pagamentoAtual.getIdPagamento());

            stmt.executeUpdate();

            mostrarAlerta("Pagamento atualizado com sucesso!");
            fecharJanela();

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao atualizar o pagamento no banco de dados.");
        }
    }

    private boolean validarCampos() {
        if (dataPagamentoPicker.getValue() == null ||
                descricaoField.getText().isEmpty() ||
                tipoDespesaField.getText().isEmpty() ||
                precoQuartoField.getText().isEmpty() ||
                valorPagamentoField.getText().isEmpty() ||
                totalPagamentoField.getText().isEmpty()) {

            mostrarAlerta("Por favor, preencha todos os campos.");
            return false;
        }

        try {
            Double.parseDouble(precoQuartoField.getText());
            Double.parseDouble(valorPagamentoField.getText());
            Double.parseDouble(totalPagamentoField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Valores numéricos inválidos.");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void handleVoltarAction() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) dataPagamentoPicker.getScene().getWindow();
        stage.close();
    }
}
