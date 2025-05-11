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
import org.skywalkerhotel.skywalkerhotel.Model.Utils.JanelaUtil;

import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalDate;

public class CadastroHospedesController {

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
    private Button finalizarButton;

    @FXML
    private Button cancelarButton;

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
        carregarTiposDePessoa();
        configurarRestricoesDeNascimento();
        configureTipoPessoaField(tipoPessoaField, tipoPessoaComboBox);
        configurarListeners();
    }

    @FXML
    private void handleVoltarAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        JanelaUtil.trocarCenaComEstado(stage, "/org/skywalkerhotel/skywalkerhotel/Fxml/GerencHospedes.fxml");
    }

    private void configurarListeners() {
        finalizarButton.setOnAction(e->handleFinalizarAction());
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
    private void aplicarEfeitoHover(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
    }

    private void configurarRestricoesDeNascimento() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.minusYears(18);

        nascimentoPicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(dataLimite)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #d3d3d3; -fx-opacity: 0.5;");
                }
            }
        });
    }

    private void carregarTiposDePessoa() {
        tipoPessoaComboBox.getItems().clear();
        tipoPessoaComboBox.getItems().addAll("Física", "Jurídica");
    }

    public static void configureTipoPessoaField(TextField tipoPessoaField, ComboBox<String> tipoDocumento) {
        tipoDocumento.valueProperty().addListener((observable, oldValue, newValue) -> {
            tipoPessoaField.clear();
            if ("Física".equals(newValue)) {
                tipoPessoaField.setPromptText("Digite o CPF (11 dígitos)");
            } else if ("Jurídica".equals(newValue)) {
                tipoPessoaField.setPromptText("Digite o CNPJ (14 dígitos)");
            }
        });
    }


    @FXML
    private void handleFinalizarAction() {
        String nome = nomeHospedeField.getText().trim();
        LocalDate nascimento = nascimentoPicker.getValue();
        String telefone = telefoneField.getText().trim();
        String tipoPessoa = tipoPessoaComboBox.getValue();
        String identificador = tipoPessoaField.getText().trim();

        if (nome.isEmpty() || nascimento == null || telefone.isEmpty() || tipoPessoa == null || identificador.isEmpty()) {
            alerta("Erro de formato", "Formato inválido!", "Preencha todos os campos.");
            return;
        }

        String insertQuery = "INSERT INTO hospedes (nome, nascimento, telefone, cpf, cnpj) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection(); PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, nome);
            pstmt.setDate(2, Date.valueOf(nascimento));
            pstmt.setString(3, telefone);
            if ("Física".equals(tipoPessoa)) {
                pstmt.setString(4, identificador);
                pstmt.setNull(5, Types.VARCHAR);
            } else {
                pstmt.setNull(4, Types.VARCHAR);
                pstmt.setString(5, identificador);
            }
            pstmt.executeUpdate();
            successAlert("Sucesso", "Cadastro realizado com sucesso!");
            limparFormulario();
        } catch (SQLException e) {
            alerta("Erro ao cadastrar", "Erro ao inserir no banco de dados", "Não foi possível cadastrar o hóspede.");
            e.printStackTrace();
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

    private void limparFormulario() {
        nomeHospedeField.clear();
        nascimentoPicker.setValue(null);
        tipoPessoaComboBox.getSelectionModel().clearSelection();
        tipoPessoaField.clear();
        telefoneField.clear();
    }
}
