package org.skywalkerhotel.skywalkerhotel.Controller;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.skywalkerhotel.skywalkerhotel.Directory.Conexao;
import org.skywalkerhotel.skywalkerhotel.Model.Entitys.Reservas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateReservasController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private TextField idHospedeField;

    @FXML
    private TextField idPagamentoField;

    @FXML
    private DatePicker inicioDaReserva;

    @FXML
    private DatePicker fimDaReserva;

    @FXML
    private Button salvarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    private ComboBox quartosComboBox;

    @FXML
    public void initialize(){
        carregarImagemLogo();
        configurarEfeitosHover();
        carregarQuartosDisponiveis();
    }

    private Reservas reservasSelecionado;
    private Runnable onUpdateCallback;

    @FXML
    public void setReservas(Reservas reservas, Runnable onUpdateCallback) {
        this.reservasSelecionado = reservas;
        this.onUpdateCallback = onUpdateCallback;

        idHospedeField.setText(String.valueOf(reservas.getNomeHospede()));
        quartosComboBox.setValue(String.valueOf(reservas.getNomeQuarto()));
        idPagamentoField.setText(String.valueOf(reservas.getIdPagamento()));
        inicioDaReserva.setValue(reservas.getInicioDataReserva());
        fimDaReserva.setValue(reservas.getFimDataReserva());
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
        if (reservasSelecionado == null) {
            alerta("Erro", "Nenhuma reserva selecionada.", "Selecione uma reserva para atualizar.");
            return;
        }

        try {
            int idHospede = Integer.parseInt(idHospedeField.getText().trim());
            int idPagamento = Integer.parseInt(idPagamentoField.getText().trim());

            QuartoItem quartoSelecionado = (QuartoItem) quartosComboBox.getValue();
            if (quartoSelecionado == null) {
                alerta("Erro", "Quarto inválido.", "Selecione um quarto válido.");
                return;
            }
            int idQuarto = quartoSelecionado.getId();

            LocalDate inicioReserva = inicioDaReserva.getValue();
            LocalDate fimReserva = fimDaReserva.getValue();

            if (inicioReserva == null || fimReserva == null) {
                alerta("Erro", "Datas inválidas.", "Selecione as datas de início e fim da reserva.");
                return;
            }

            String updateQuery = "UPDATE reserva_quartos SET id_hospede=?, id_quarto=?, id_pagamento=?, inicio_data_reserva=?, fim_data_reserva=? WHERE id_reserva=?";

            try (Connection conn = Conexao.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, idHospede);
                pstmt.setInt(2, idQuarto);
                pstmt.setInt(3, idPagamento);
                pstmt.setObject(4, inicioReserva);
                pstmt.setObject(5, fimReserva);
                pstmt.setInt(6, reservasSelecionado.getIdReserva());

                pstmt.executeUpdate();
                successAlert("Sucesso", "Reserva atualizada com sucesso!");

                if (onUpdateCallback != null) {
                    onUpdateCallback.run();
                }

                Stage stage = (Stage) salvarButton.getScene().getWindow();
                stage.close();

            } catch (SQLException e) {
                alerta("Erro ao atualizar", "Erro no banco de dados.", "Não foi possível atualizar a reserva.");
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            alerta("Erro", "Campos inválidos.", "Certifique-se de que os IDs são números válidos.");
        }
    }


    private void alerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void successAlert(String titulo, String cabecalho) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.showAndWait();
    }

    // Carregar quartos disponíveis
    private void carregarQuartosDisponiveis() {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT id_quarto, nome_quarto FROM quartos WHERE status_quarto = 'livre'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_quarto");
                String nome = rs.getString("nome_quarto");
                quartosComboBox.getItems().add(new QuartoItem(id, nome));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao carregar quartos disponíveis.");
        }
    }


    private void mostrarMensagem(String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static class QuartoItem {
        private final int id;
        private final String nome;

        public QuartoItem(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        public int getId() {
            return id;
        }

        public String toString() {
            return nome;
        }
    }

}
