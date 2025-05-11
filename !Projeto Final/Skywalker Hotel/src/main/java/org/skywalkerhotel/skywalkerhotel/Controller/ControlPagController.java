package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Pagamentos;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Quartos;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.*;
import java.time.LocalDate;

public class ControlPagController {

    @FXML
    private TableView<Pagamentos> tableViewPagamentos;
    @FXML
    private TableColumn<Pagamentos, Integer> colId;
    @FXML
    private TableColumn<Pagamentos, LocalDate> colDataPagamento;
    @FXML
    private TableColumn<Pagamentos, String> colDescricao;
    @FXML
    private TableColumn<Pagamentos, String> colTipoDespesa;
    @FXML
    private TableColumn<Pagamentos, Double> colPrecoQuarto;
    @FXML
    private TableColumn<Pagamentos, Double> colValorPagamento;
    @FXML
    private TableColumn<Pagamentos, Double> colTotalPagamento;

    @FXML
    private TextField txtPesquisar;

    private ObservableList<Pagamentos> pagamentosList;

    // Método para inicializar a tabela com dados do banco de dados
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idPagamentoProperty().asObject());
        colDataPagamento.setCellValueFactory(cellData -> cellData.getValue().dataPagamentoProperty());
        colDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoPagamentoProperty());
        colTipoDespesa.setCellValueFactory(cellData -> cellData.getValue().tipoDespesaProperty());
        colPrecoQuarto.setCellValueFactory(cellData -> cellData.getValue().precoQuartoProperty().asObject());
        colValorPagamento.setCellValueFactory(cellData -> cellData.getValue().valorPagamentoProperty().asObject());
        colTotalPagamento.setCellValueFactory(cellData -> cellData.getValue().totalPagamentoProperty().asObject());

        // Carregar os dados do banco de dados
        loadPagamentosFromDatabase();

        // Ação do botão de pesquisa
        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabela(newValue));
    }

    // Método para carregar pagamentos do banco de dados
    private void loadPagamentosFromDatabase() {
        pagamentosList = FXCollections.observableArrayList();
        String query = "SELECT * FROM pagamentos";
        try (Connection conexao = Conexao.getConnection();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Pagamentos pagamento = new Pagamentos(
                        rs.getInt("id_pagamento"),
                        rs.getDate("data_pagamento").toLocalDate(),
                        rs.getString("descricao_pagamento"),
                        rs.getString("tipo_despesa"),
                        rs.getDouble("preco_quarto"),
                        rs.getDouble("valor_pagamento"),
                        rs.getDouble("total_pagamento")
                );
                pagamentosList.add(pagamento);
            }
            tableViewPagamentos.setItems(pagamentosList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método de filtro de pesquisa
    private void filtrarTabela(String filtro) {
        ObservableList<Pagamentos> resultados = FXCollections.observableArrayList();

        String query = "SELECT * FROM pagamentos WHERE " +
                "CAST(id_pagamento AS CHAR) LIKE ? OR " +
                "data_pagamento LIKE ? OR " +
                "descricao_pagamento LIKE ? OR " +
                "tipo_despesa LIKE ? OR " +
                "preco_quarto LIKE ? OR " +
                "valor_pagamento LIKE ? OR " +
                "total_pagamento LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 1; i <= 7; i++) {
                stmt.setString(i, "%" + filtro + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pagamentos c = new Pagamentos(
                        rs.getInt("id_pagamento"),
                        rs.getDate("data_pagamento").toLocalDate(),
                        rs.getString("descricao_pagamento"),
                        rs.getString("tipo_despesa"),
                        rs.getDouble("preco_quarto"),
                        rs.getDouble("valor_pagamento"),
                        rs.getDouble("total_pagamento")
                );
                resultados.add(c);
            }
            this.tableViewPagamentos.setItems(resultados);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método de voltar para a tela anterior
    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml");
    }

    // Método para o botão Cadastrar
    @FXML
    private void handleCadastrarAction() {
        // Implementar cadastro de um novo pagamento
        System.out.println("Cadastrar novo pagamento");
        // Você pode abrir um formulário para o cadastro de um pagamento
    }

    // Método para o botão Editar
    @FXML
    private void handleEditarAction() {
        Pagamentos pagamentoSelecionado = tableViewPagamentos.getSelectionModel().getSelectedItem();
        if (pagamentoSelecionado != null) {
            System.out.println("Editar pagamento: " + pagamentoSelecionado.getDescricaoPagamento());
            // Aqui você pode abrir um formulário para editar as informações do pagamento
        } else {
            System.out.println("Nenhum pagamento selecionado.");
        }
    }

    private void mostrarMensagem(String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void handleExcluirAction() {
        Pagamentos pagamentoSelecionado = tableViewPagamentos.getSelectionModel().getSelectedItem();
        if (pagamentoSelecionado != null) {
            String deleteQuery = "DELETE FROM pagamentos WHERE id_pagamento = ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

                pstmt.setInt(1, pagamentoSelecionado.getIdPagamento());
                pstmt.executeUpdate();
                pagamentosList.remove(pagamentoSelecionado); // Atualiza a lista na UI
                System.out.println("Pagamento excluído: " + pagamentoSelecionado.getDescricaoPagamento());

            } catch (SQLIntegrityConstraintViolationException e) {
                mostrarMensagem("Erro: Pagamento vinculado a uma reserva. Delete não permitido.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum pagamento selecionado para excluir.");
        }
    }
}
