package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Quartos;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.*;

public class DispQuartosController {

    // Referências dos controles FXML
    @FXML
    private TableView<Quartos> tableViewQuartos;

    @FXML
    private TableColumn<Quartos, Integer> colId;
    @FXML
    private TableColumn<Quartos, String> colNome;
    @FXML
    private TableColumn<Quartos, Integer> colCasal;
    @FXML
    private TableColumn<Quartos, Integer> colSolteiro;
    @FXML
    private TableColumn<Quartos, Integer> colMax;
    @FXML
    private TableColumn<Quartos, String> colStatus;
    @FXML
    private TableColumn<Quartos, Double> colValor;


    @FXML
    private TextField txtPesquisar;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnExcluir;

    private ObservableList<Quartos> quartosList;

    // Método para inicializar a tabela com dados do banco de dados
    public void initialize() {
        // Inicializa as colunas
        colId.setCellValueFactory(cellData -> cellData.getValue().idQuartoProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colCasal.setCellValueFactory(cellData -> cellData.getValue().casalProperty().asObject());
        colSolteiro.setCellValueFactory(cellData -> cellData.getValue().solteiroProperty().asObject());
        colMax.setCellValueFactory(cellData -> cellData.getValue().maxPessoasProperty().asObject());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colValor.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());


        // Carregar os dados do banco de dados
        loadQuartosFromDatabase();

        // Ação do botão de pesquisa
        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabela(newValue));

        // Ações dos botões
        btnCadastrar.setOnAction(event -> handleCadastrarAction());
        btnEditar.setOnAction(event -> handleEditarAction());
        btnExcluir.setOnAction(event -> handleExcluirAction());
    }

    // Método para carregar quartos do banco de dados
    private void loadQuartosFromDatabase() {
        quartosList = FXCollections.observableArrayList();
        String query = "SELECT * FROM quartos";
        try (Connection conexao = Conexao.getConnection();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Quartos quarto = new Quartos(
                        rs.getInt("id_quarto"),
                        rs.getString("nome_quarto"),
                        rs.getInt("cama_casal_quarto"),
                        rs.getInt("cama_solteiro_quarto"),
                        rs.getInt("quant_pessoas"),
                        rs.getString("status_quarto"),
                        rs.getDouble("preco_quarto")
                );
                quartosList.add(quarto);
            }
            tableViewQuartos.setItems(quartosList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método de filtro de pesquisa
    private void filtrarTabela(String filtro) {
        ObservableList<Quartos> resultados = FXCollections.observableArrayList();

        String query="SELECT * FROM quartos WHERE " +
                "CAST(id_quarto AS CHAR) LIKE ? OR " +
                "nome_quarto LIKE ? OR " +
                "cama_casal_quarto LIKE ? OR " +
                "cama_solteiro_quarto LIKE ? OR " +
                "quant_pessoas LIKE ? OR " +
                "status_quarto LIKE ? OR " +
                "preco_quarto LIKE ?";

        try (Connection conn= Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 1; i <= 7; i++) {
                stmt.setString(i, "%" + filtro + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Quartos c=new Quartos(
                        rs.getInt("id_quarto"),
                        rs.getString("nome_quarto"),
                        rs.getInt("cama_casal_quarto"),
                        rs.getInt("cama_solteiro_quarto"),
                        rs.getInt("quant_pessoas"),
                        rs.getString("status_quarto"),
                        rs.getDouble("preco_quarto")
                );
                resultados.add(c);
            }
            this.tableViewQuartos.setItems(resultados);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml");
    }

    // Método para o botão Cadastrar
    @FXML
    private void handleCadastrarAction() {
        // Implementar cadastro de um novo quarto
        System.out.println("Cadastrar novo quarto");
        // Você pode abrir um formulário para o cadastro de um quarto
    }

    // Método para o botão Editar
    @FXML
    private void handleEditarAction() {
        Quartos quartoSelecionado = tableViewQuartos.getSelectionModel().getSelectedItem();
        if (quartoSelecionado != null) {
            System.out.println("Editar quarto: " + quartoSelecionado.getNome());
            // Aqui você pode abrir um formulário para editar as informações do quarto
        } else {
            System.out.println("Nenhum quarto selecionado.");
        }
    }

    // Método para o botão Excluir
    @FXML
    private void handleExcluirAction() {
        Quartos quartoSelecionado = tableViewQuartos.getSelectionModel().getSelectedItem();
        if (quartoSelecionado != null) {
            String deleteQuery = "DELETE FROM quartos WHERE id_quarto = ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

                pstmt.setInt(1, quartoSelecionado.getIdQuarto());
                pstmt.executeUpdate();
                quartosList.remove(quartoSelecionado); // Atualiza a lista na UI
                System.out.println("Quarto excluído: " + quartoSelecionado.getNome());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum quarto selecionado para excluir.");
        }
    }
}
