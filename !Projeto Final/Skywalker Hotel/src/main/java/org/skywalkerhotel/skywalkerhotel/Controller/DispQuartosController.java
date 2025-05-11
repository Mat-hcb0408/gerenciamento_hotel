package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Quartos;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;

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

        colValor.setCellFactory(column -> new TableCell<Quartos, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Carregar os dados do banco de dados
        loadQuartosFromDatabase();

        // Ação do botão de pesquisa
        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabela(newValue));

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
    private void handleCadastrarAction(ActionEvent event) {
        Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/CadastroQuartos.fxml");
    }

    // Método para o botão Editar
    @FXML
    private void handleEditarAction(ActionEvent event) {
        Quartos quartoSelecionado=tableViewQuartos.getSelectionModel().getSelectedItem();
        if (quartoSelecionado!=null){
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/skywalkerhotel/skywalkerhotel/Fxml/UpdateQuartos.fxml"));
                Parent root=loader.load();
                UpdateQuartoController controller=loader.getController();

                controller.setQuarto(quartoSelecionado, this::loadQuartosFromDatabase);
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
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

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Erro: Quarto vinculado a uma reserva. Delete não permitido.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("Nenhum quarto selecionado para excluir.");
        }
    }
}
