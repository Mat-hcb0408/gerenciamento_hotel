package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Hospedes;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.*;

public class GerencHospedesController {

    @FXML private ImageView Logo_Imagem;
    @FXML
    private TextField txtPesquisar;
    @FXML private Button cadastrarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;

    @FXML private TableView<Hospedes> tabelaHospedes;
    @FXML private TableColumn<Hospedes, Integer> columnID;
    @FXML private TableColumn<Hospedes, String> columnNome;
    @FXML private TableColumn<Hospedes, String> columnNascimento;
    @FXML private TableColumn<Hospedes, String> columnTelefone;
    @FXML private TableColumn<Hospedes, String> columnCPF;
    @FXML private TableColumn<Hospedes, String> columnCNPJ;

    private ObservableList<Hospedes> hospedesList;

    @FXML
    public void initialize() {
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnNascimento.setCellValueFactory(new PropertyValueFactory<>("nascimento"));
        columnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        columnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        columnCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));

        configurarEfeitosHover();

        // Carregar dados na tabela
        loadHospedesFromDatabase();

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabela(newValue));
    }

    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml");
    }

    private void configurarEfeitosHover() {
        Button[] botoes = { cadastrarButton, editarButton, excluirButton };
        for (Button botao : botoes) {
            if (botao != null) {
                aplicarEfeitoHover(botao);
                aplicarEfeitoClique(botao);
            }
        }
    }

    private void aplicarEfeitoHover(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        button.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 0);");
        });

        button.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            button.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.2, 0, 0);");
        });
    }

    private void aplicarEfeitoClique(Button button) {
        ScaleTransition clickScale = new ScaleTransition(Duration.millis(100), button);
        clickScale.setToX(0.95);
        clickScale.setToY(0.95);

        ScaleTransition releaseScale = new ScaleTransition(Duration.millis(100), button);
        releaseScale.setToX(1.0);
        releaseScale.setToY(1.0);

        button.setOnMousePressed(e -> clickScale.playFromStart());
        button.setOnMouseReleased(e -> releaseScale.playFromStart());
    }

    // Método para carregar hóspedes do banco de dados
    private void loadHospedesFromDatabase() {
        hospedesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM hospedes";
        try (Connection conexao = Conexao.getConnection();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Hospedes hospede = new Hospedes(
                        rs.getInt("id_hospede"),
                        rs.getString("nome"),
                        rs.getString("nascimento"),
                        rs.getString("telefone"),
                        rs.getString("tipo_pessoa"),
                        rs.getString("cpf"),
                        rs.getString("cnpj")
                );
                hospedesList.add(hospede);
            }
            tabelaHospedes.setItems(hospedesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método de filtro de pesquisa
    private void filtrarTabela(String filtro) {
        ObservableList<Hospedes> resultados = FXCollections.observableArrayList();

        String query = "SELECT * FROM hospedes WHERE " +
                "CAST(id_hospede AS CHAR) LIKE ? OR " +
                "nome LIKE ? OR " +
                "telefone LIKE ? OR " +
                "cpf LIKE ? OR " +
                "cnpj LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Configura todos os parâmetros com o filtro
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, "%" + filtro + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hospedes hospede = new Hospedes(
                        rs.getInt("id_hospede"),
                        rs.getString("nome"),
                        rs.getString("nascimento"),
                        rs.getString("telefone"),
                        rs.getString("tipo_pessoa"),
                        rs.getString("cpf"),
                        rs.getString("cnpj")
                );
                resultados.add(hospede);
            }

            // Atualiza a tabela com os resultados filtrados
            tabelaHospedes.setItems(resultados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para o botão Cadastrar
    @FXML
    private void handleCadastrarAction(ActionEvent event) {
        Stage stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/CadastroHospedes.fxml");
    }

    // Método para o botão Editar
    @FXML
    private void handleEditarAction() {
        Hospedes hospedeSelecionado = tabelaHospedes.getSelectionModel().getSelectedItem();
        if (hospedeSelecionado != null) {
            System.out.println("Editar hóspede: " + hospedeSelecionado.getNome());
            // Aqui você pode abrir um formulário para editar as informações do hóspede
        } else {
            System.out.println("Nenhum hóspede selecionado.");
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
        Hospedes hospedeSelecionado = tabelaHospedes.getSelectionModel().getSelectedItem();
        if (hospedeSelecionado != null) {
            String deleteQuery = "DELETE FROM hospedes WHERE id_hospede = ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

                pstmt.setInt(1, hospedeSelecionado.getId());
                pstmt.executeUpdate();
                hospedesList.remove(hospedeSelecionado); // Atualiza a lista na UI
                System.out.println("Hóspede excluído: " + hospedeSelecionado.getNome());

            } catch (SQLIntegrityConstraintViolationException e) {
                mostrarMensagem("Erro: Hóspede vinculado a uma reserva. Delete não permitido.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhum hóspede selecionado para excluir.");
        }
    }

}
