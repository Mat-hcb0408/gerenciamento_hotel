package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Quartos;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Reservas;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.CsvExporter;
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GerencReservasController {

    @FXML private ImageView Logo_Imagem;
    @FXML private TextField txtPesquisar;
    @FXML private Button cadastrarButton;
    @FXML private Button editarButton;
    @FXML private Button excluirButton;
    @FXML private Button exportarCSVButton;


    @FXML private TableView<Reservas> tabelaReservas;
    @FXML private TableColumn<Reservas, Integer> columnID;
    @FXML private TableColumn<Reservas, String> columnHospedeNome;
    @FXML private TableColumn<Reservas, String> columnQuartoNome;
    @FXML private TableColumn<Reservas, Integer> columnidPagamento;
    @FXML private TableColumn<Reservas, LocalDate> columnDataInicio;
    @FXML private TableColumn<Reservas, LocalDate> columnDataFim;

    private ObservableList<Reservas> reservasList;

    @FXML
    public void initialize() {
        // Inicializando as colunas da tabela com as propriedades da classe Reservas
        columnID.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        columnHospedeNome.setCellValueFactory(new PropertyValueFactory<>("nomeHospede"));
        columnQuartoNome.setCellValueFactory(new PropertyValueFactory<>("nomeQuarto"));
        columnidPagamento.setCellValueFactory(new PropertyValueFactory<>("idPagamento"));
        columnDataInicio.setCellValueFactory(new PropertyValueFactory<>("inicioDataReserva"));
        columnDataFim.setCellValueFactory(new PropertyValueFactory<>("fimDataReserva"));

        configurarEfeitosHover();
        loadReservasFromDatabase();

        txtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> filtrarTabela(newValue));
        exportarCSVButton.setOnAction(event -> handleExportarCSV());
    }

    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml");
    }


    private void configurarEfeitosHover() {
        Button[] botoes = {cadastrarButton, editarButton, excluirButton};
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

    private void loadReservasFromDatabase() {
        reservasList = FXCollections.observableArrayList();
        String query = "SELECT rq.id_reserva, h.nome AS nome_hospede, q.nome_quarto AS nome_quarto, " +
                "p.id_pagamento AS id_pagamento, " +  // Removido a 'forma_pagamento' da consulta
                "rq.inicio_data_reserva, rq.fim_data_reserva " +
                "FROM reserva_quartos rq " +
                "JOIN hospedes h ON rq.id_hospede = h.id_hospede " +
                "JOIN quartos q ON rq.id_quarto = q.id_quarto " +
                "JOIN pagamentos p ON rq.id_pagamento = p.id_pagamento";

        try (Connection conexao = Conexao.getConnection();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Reservas reserva = new Reservas(
                        rs.getInt("id_reserva"),
                        rs.getString("nome_hospede"),
                        rs.getString("nome_quarto"),
                        rs.getInt("id_pagamento"),  // Aqui estamos apenas pegando o id_pagamento
                        rs.getDate("inicio_data_reserva").toLocalDate(),
                        rs.getDate("fim_data_reserva").toLocalDate()
                );
                reservasList.add(reserva);
            }
            tabelaReservas.setItems(reservasList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filtrarTabela(String filtro) {
        ObservableList<Reservas> resultados = FXCollections.observableArrayList();

        String query = "SELECT rq.id_reserva, h.nome AS nome_hospede, q.nome_quarto AS nome_quarto, " +
                "p.id_pagamento AS id_pagamento, " +  // Removido a 'forma_pagamento' da consulta
                "rq.inicio_data_reserva, rq.fim_data_reserva " +
                "FROM reserva_quartos rq " +
                "JOIN hospedes h ON rq.id_hospede = h.id_hospede " +
                "JOIN quartos q ON rq.id_quarto = q.id_quarto " +
                "JOIN pagamentos p ON rq.id_pagamento = p.id_pagamento " +
                "WHERE CAST(rq.id_reserva AS CHAR) LIKE ? OR " +
                "LOWER(h.nome) LIKE ? OR " +
                "LOWER(q.nome_quarto) LIKE ? OR " +
                "rq.inicio_data_reserva LIKE ? OR " +
                "rq.fim_data_reserva LIKE ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 1; i <= 5; i++) { // Atualizado para refletir 5 parâmetros, sem 'forma_pagamento'
                stmt.setString(i, "%" + filtro.toLowerCase() + "%");
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservas reserva = new Reservas(
                        rs.getInt("id_reserva"),
                        rs.getString("nome_hospede"),
                        rs.getString("nome_quarto"),
                        rs.getInt("id_pagamento"), // Aqui também estamos pegando apenas o id_pagamento
                        rs.getDate("inicio_data_reserva").toLocalDate(),
                        rs.getDate("fim_data_reserva").toLocalDate()
                );
                resultados.add(reserva);
            }

            tabelaReservas.setItems(resultados);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCadastrarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Check-in.fxml");
    }

    @FXML
    private void handleEditarAction(ActionEvent event) {
        Reservas reservasSelecionado=tabelaReservas.getSelectionModel().getSelectedItem();
        if (reservasSelecionado!=null){
            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/org/skywalkerhotel/skywalkerhotel/Fxml/UpdateReservas.fxml"));
                Parent root=loader.load();
                UpdateReservasController controller=loader.getController();

                controller.setReservas(reservasSelecionado, this::loadReservasFromDatabase);
                Stage stage=new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExcluirAction() {
        Reservas reservaSelecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (reservaSelecionada != null) {
            String deleteQuery = "DELETE FROM reserva_quartos WHERE id_reserva = ?";
            try (Connection conn = Conexao.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

                pstmt.setInt(1, reservaSelecionada.getIdReserva());
                pstmt.executeUpdate();
                reservasList.remove(reservaSelecionada); // Atualiza a lista na UI
                tabelaReservas.setItems(reservasList); // Atualiza a tabela na interface
                System.out.println("Reserva excluída: " + reservaSelecionada.getIdReserva());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nenhuma reserva selecionada para excluir.");
        }
    }
    @FXML
    private void handleExportarCSV() {
        try {
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"ID", "Hóspede", "Quarto", "ID Pagamento", "Início", "Fim"});

            for (Reservas r : reservasList) {
                data.add(new String[]{
                        String.valueOf(r.getIdReserva()),
                        r.getNomeHospede(),
                        r.getNomeQuarto(),
                        String.valueOf(r.getIdPagamento()),
                        r.getInicioDataReserva().toString(),
                        r.getFimDataReserva().toString()
                });
            }

            String downloadsPath = System.getProperty("user.home") + "/Downloads/relatorio_reservas.csv";
            CsvExporter.export(downloadsPath, data);
            System.out.println("CSV exportado com sucesso para: " + downloadsPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
