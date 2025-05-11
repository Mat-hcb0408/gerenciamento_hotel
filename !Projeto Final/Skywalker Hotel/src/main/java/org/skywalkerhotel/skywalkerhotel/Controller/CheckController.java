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
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.sql.*;
import java.time.LocalDate;

public class CheckController {

    @FXML
    private ImageView Logo_Imagem;

    @FXML
    private TextField nomeField;

    @FXML
    private DatePicker nascimentoPicker;

    @FXML
    private ComboBox<String> tipoDocumento;

    @FXML
    private ComboBox<String> quartosComboBox;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField telefoneField;

    @FXML
    private DatePicker checkinPicker;

    @FXML
    private DatePicker checkoutPicker;

    @FXML
    private Label valorLabel;

    @FXML
    private Button finalizarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
        configurarDatePickers();
        applyPhoneMask(telefoneField);

        carregarTiposDePessoa();

        carregarQuartosDisponiveis();

        checkinPicker.valueProperty().addListener((observable, oldValue, newValue) -> atualizarValorEstadia());
        checkoutPicker.valueProperty().addListener((observable, oldValue, newValue) -> atualizarValorEstadia());

        configurarRestricoesDeDatas();


        configureCpfCnpjField(cpfField, tipoDocumento);

        quartosComboBox.setOnAction(event -> {
            String nomeQuarto = quartosComboBox.getValue();

            if (nomeQuarto == null || nomeQuarto.trim().isEmpty()) {
                valorLabel.setText("Nenhum quarto selecionado");
                return;
            }

            try {
                double preco = obterPrecoQuarto(nomeQuarto); // Passando nomeQuarto como argumento
                valorLabel.setText(String.format("R$ %.2f", preco));
            } catch (SQLException e) {
                valorLabel.setText("Erro ao obter preço");
                e.printStackTrace(); // Se quiser esconder totalmente o erro, remova isso também
            }
        });



    }

    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/Home.fxml");
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
        aplicarEfeitoHover(finalizarButton);
        aplicarEfeitoHover(cancelarButton);
    }

    // Configurações dos DatePickers
    private void configurarDatePickers() {
        checkinPicker.getEditor().setDisable(true);
        checkinPicker.getEditor().setOpacity(1);
        checkoutPicker.getEditor().setDisable(true);
        checkoutPicker.getEditor().setOpacity(1);
    }


    private boolean quartoNaoSelecionado = false;

    private void configurarRestricoesDeDatas() {
        LocalDate hoje = LocalDate.now();

        // Configura o checkinPicker
        checkinPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(hoje)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3; -fx-opacity: 0.5;");
                }

                // Evitar chamada se o quarto não for selecionado
                try {
                    // Tenta pegar o id do quarto selecionado, se houver
                    Integer idQuarto = pegarIdQuartoSelecionado();
                    if (idQuarto != null && verificarReserva(date)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ff0000; -fx-opacity: 0.5;");
                    }
                } catch (SQLException e) {
                    // Captura e filtra o erro sem exibir no console
                    if (e.getMessage().contains("Nenhum quarto selecionado")) {
                        // Se a mensagem de erro já foi exibida, não exibe novamente
                        if (!quartoNaoSelecionado) {
                            mostrarMensagem("Nenhum quarto selecionado. Verifique sua seleção.");
                            quartoNaoSelecionado = true; // Marcar que o erro foi exibido
                        }
                    } else {
                        // Se for outro erro, exibe normalmente
                        e.printStackTrace();
                    }
                }
            }
        });

        // Configura o nascimentoPicker
        nascimentoPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Desabilita as datas futuras
                if (date.isAfter(hoje)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3; -fx-opacity: 0.5;");
                }
            }
        });


        // Configura o checkoutPicker
        checkoutPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(hoje)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3; -fx-opacity: 0.5;");
                }

                // Evitar chamada se o quarto não for selecionado
                try {
                    // Tenta pegar o id do quarto selecionado, se houver
                    Integer idQuarto = pegarIdQuartoSelecionado();
                    if (idQuarto != null && verificarReserva(date)) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ff0000; -fx-opacity: 0.5;");
                    }
                } catch (SQLException e) {
                    // Captura e filtra o erro sem exibir no console
                    if (e.getMessage().contains("Nenhum quarto selecionado")) {
                        // Se a mensagem de erro já foi exibida, não exibe novamente
                        if (!quartoNaoSelecionado) {
                            mostrarMensagem("Nenhum quarto selecionado. Verifique sua seleção.");
                            quartoNaoSelecionado = true; // Marcar que o erro foi exibido
                        }
                    } else {
                        // Se for outro erro, exibe normalmente
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    // Método para verificar se a data está reservada para o quarto selecionado
    private boolean verificarReserva(LocalDate date) throws SQLException {
        Integer idQuarto = pegarIdQuartoSelecionado();

        // Se nenhum quarto estiver selecionado, não há como verificar reserva
        if (idQuarto == null) {
            return false; // Considera como "não reservado" ou trate conforme sua lógica
        }

        String sql = "SELECT COUNT(*) AS total FROM reserva_quartos " +
                "WHERE id_quarto = ? AND ? >= inicio_data_reserva AND ? <= fim_data_reserva";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idQuarto);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setDate(3, java.sql.Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                return total > 0; // Se houver alguma reserva, retorna true (ocupado)
            }
        }

        return false;
    }



    public static void configureCpfCnpjField(TextField cpfField, ComboBox<String> tipoDocumento) {
        // Adiciona um listener para a mudança na seleção do ComboBox
        tipoDocumento.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Limpar o campo de texto ao trocar a seleção
                cpfField.clear();

                // Alterar o comportamento do campo de acordo com a seleção
                if (newValue.equals("Física")) {
                    cpfField.setPromptText("Digite o CPF (11 dígitos)");
                    configureCpfField(cpfField);
                } else if (newValue.equals("Jurídica")) {
                    cpfField.setPromptText("Digite o CNPJ (14 dígitos)");
                    configureCnpjField(cpfField);
                }
            }
        });

        // Verifica a quantidade de caracteres do CPF/CNPJ
        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tipoDocumento.getValue() != null) {
                if (tipoDocumento.getValue().equals("Física") && newValue.length() > 11) {
                    cpfField.setText(newValue.substring(0, 11)); // Limita a 11 caracteres
                } else if (tipoDocumento.getValue().equals("Jurídica") && newValue.length() > 14) {
                    cpfField.setText(newValue.substring(0, 14)); // Limita a 14 caracteres
                }
            }
        });
    }

    private static void configureCpfField(TextField cpfField) {
        // Permite apenas números no CPF
        cpfField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        }));
    }

    private static void configureCnpjField(TextField cpfField) {
        // Permite apenas números no CNPJ
        cpfField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        }));
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

    public static void applyPhoneMask(TextField textField) {
            textField.textProperty().addListener((obs, oldValue, newValue) -> {
                String numeric = newValue.replaceAll("[^\\d]", "");

                StringBuilder result = new StringBuilder();

                if (numeric.length() >= 1) result.append("(");
                if (numeric.length() >= 2) result.append(numeric.substring(0, 2)).append(") ");
                else if (numeric.length() >= 1) result.append(numeric.charAt(0));
                if (numeric.length() >= 7) {
                    result.append(numeric.substring(2, 7)).append("-");
                    result.append(numeric.substring(7, Math.min(11, numeric.length())));
                } else if (numeric.length() > 2) {
                    result.append(numeric.substring(2));
                }

                // Remove listener antes de atualizar o valor, pra evitar recursão
                textField.setText(result.toString());
                textField.positionCaret(result.length());
            });
        }

    // Método para carregar tipos de pessoa para ComboBox
    private void carregarTiposDePessoa() {
        tipoDocumento.getItems().clear(); // Limpa itens anteriores, se necessário
        tipoDocumento.getItems().addAll("Física", "Jurídica");
    }


    // Carregar quartos disponíveis
    private void carregarQuartosDisponiveis() {
        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT nome_quarto FROM quartos WHERE status_quarto";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                quartosComboBox.getItems().add(rs.getString("nome_quarto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao carregar quartos disponíveis.");
        }
    }





    private double calcularValorTotal() {
        LocalDate checkinDate = checkinPicker.getValue();
        LocalDate checkoutDate = checkoutPicker.getValue();

        if (checkinDate != null && checkoutDate != null) {
            long diasDeEstadia = java.time.temporal.ChronoUnit.DAYS.between(checkinDate, checkoutDate);

            if (diasDeEstadia > 0) {
                try {
                    String nomeQuarto = quartosComboBox.getValue(); // ou de onde quer que venha o nome
                    double precoQuarto = obterPrecoQuarto(nomeQuarto); // ✅ Correto

                    return diasDeEstadia * precoQuarto;
                } catch (SQLException e) {
                    e.printStackTrace(); // Ou log para o usuário
                }
            }
        }

        return 0;
    }


    private void atualizarValorEstadia() {
        double valorTotal = calcularValorTotal();
        String valorFormatado = String.format("R$ %.2f", valorTotal);
        valorLabel.setText(valorFormatado);
    }



    public double obterPrecoQuarto(String nomeQuarto) throws SQLException {

        if (nomeQuarto == null || nomeQuarto.trim().isEmpty()) {
            return -1;
        }

        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT preco_quarto FROM quartos WHERE nome_quarto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeQuarto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("preco_quarto");
            } else {
                throw new SQLException("Quarto não encontrado.");
            }
        }
    }


    // Método para obter o ID do quarto selecionado
    private int pegarIdQuartoSelecionado() throws SQLException {
        String nomeQuarto = quartosComboBox.getValue();

        if (nomeQuarto == null || nomeQuarto.trim().isEmpty()) {
            throw new SQLException("Nenhum quarto selecionado.");
        }

        try (Connection conn = Conexao.getConnection()) {
            String sql = "SELECT id_quarto FROM quartos WHERE nome_quarto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomeQuarto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_quarto");
            } else {
                throw new SQLException("Quarto não encontrado.");
            }
        }
    }


    private void mostrarMensagem(String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


    // Método para finalizar o Check-in
    @FXML
    private void finalizarCheckIn() {
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false); // Transação

            // Verificar se o hóspede já existe
            String sqlVerificaHospede = "SELECT id_hospede FROM hospedes WHERE nome = ? AND nascimento = ? AND (cpf = ? OR cnpj = ?)";
            PreparedStatement stmtVerificaHospede = conn.prepareStatement(sqlVerificaHospede);
            stmtVerificaHospede.setString(1, nomeField.getText());

            if (nascimentoPicker.getValue() != null) {
                stmtVerificaHospede.setDate(2, Date.valueOf(nascimentoPicker.getValue()));
            } else {
                mostrarMensagem("Data de nascimento não foi selecionada.");
                return;
            }

            String tipoDoc = tipoDocumento.getValue();
            String cpfOuCnpj = cpfField.getText().replaceAll("[^\\d]", "");

            if ("Física".equals(tipoDoc)) {
                stmtVerificaHospede.setString(3, cpfOuCnpj);
                stmtVerificaHospede.setNull(4, Types.VARCHAR);
            } else {
                stmtVerificaHospede.setNull(3, Types.VARCHAR);
                stmtVerificaHospede.setString(4, cpfOuCnpj);
            }

            ResultSet rsHospede = stmtVerificaHospede.executeQuery();

            int idHospede;
            if (rsHospede.next()) {
                idHospede = rsHospede.getInt("id_hospede");
            } else {
                mostrarMensagem("Hóspede não encontrado no sistema. Verifique os dados.");
                return;
            }

            // Criar pagamento
            double valorTotal = calcularValorTotal();
            String sqlPagamento = "INSERT INTO pagamentos (data_pagamento, descricao_pagamento, tipo_despesa, preco_quarto, valor_pagamento, total_pagamento) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtPag = conn.prepareStatement(sqlPagamento, Statement.RETURN_GENERATED_KEYS);
            stmtPag.setDate(1, Date.valueOf(LocalDate.now()));
            stmtPag.setString(2, "Check-in de hóspede");
            stmtPag.setString(3, "Hospedagem");
            stmtPag.setDouble(4, valorTotal);
            stmtPag.setDouble(5, valorTotal);
            stmtPag.setDouble(6, valorTotal);
            stmtPag.executeUpdate();
            ResultSet rsPag = stmtPag.getGeneratedKeys();
            rsPag.next();
            int idPagamento = rsPag.getInt(1);

            // Criar reserva
            int idQuarto = pegarIdQuartoSelecionado();
            String sqlReserva = "INSERT INTO reserva_quartos (id_hospede, id_quarto, id_pagamento, inicio_data_reserva, fim_data_reserva) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmtReserva = conn.prepareStatement(sqlReserva);
            stmtReserva.setInt(1, idHospede);
            stmtReserva.setInt(2, idQuarto);
            stmtReserva.setInt(3, idPagamento);
            stmtReserva.setDate(4, Date.valueOf(checkinPicker.getValue()));
            stmtReserva.setDate(5, Date.valueOf(checkoutPicker.getValue()));
            stmtReserva.executeUpdate();

            // Atualizar status do quarto
            String sqlAtualiza = "UPDATE quartos SET status_quarto = 'ocupado' WHERE id_quarto = ?";
            PreparedStatement stmtUpdate = conn.prepareStatement(sqlAtualiza);
            stmtUpdate.setInt(1, idQuarto);
            stmtUpdate.executeUpdate();

            conn.commit();
            mostrarMensagem("Check-in realizado com sucesso!");

            limparFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao realizar o check-in. Tente novamente.");
            try (Connection conn = Conexao.getConnection()) {
                conn.rollback(); // desfaz transações em caso de erro
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void limparFormulario() {
        nomeField.clear();
        nascimentoPicker.setValue(null);
        tipoDocumento.getSelectionModel().clearSelection();
        quartosComboBox.getSelectionModel().clearSelection();
        cpfField.clear();
        telefoneField.clear();
        checkinPicker.setValue(null);
        checkoutPicker.setValue(null);
        valorLabel.setText("R$ 0,00");  // Zera o valor da estadia
    }


}

