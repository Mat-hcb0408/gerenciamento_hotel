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

        String cpf = hospedes.getCpf();
        String cnpj = hospedes.getCnpj();

        nomeHospedeField.setText(hospedes.getNome());
        nascimentoPicker.setValue(LocalDate.parse(hospedes.getNascimento()));
        telefoneField.setText(hospedes.getTelefone());
        tipoPessoaField.setText((cpf != null && !cpf.isEmpty()) ? cpf : (cnpj != null ? cnpj : ""));

        String tipoDocumento = (cpf != null && !cpf.isEmpty()) ? cpf : ((cnpj != null && !cnpj.isEmpty()) ? cnpj : "");
        String tipoSelecionado = (cpf != null && !cpf.isEmpty()) ? "CPF" : "CNPJ";

        tipoPessoaField.setText(tipoDocumento);
        tipoPessoaComboBox.setValue(tipoSelecionado);    }

    @FXML
    public void initialize() {
        carregarImagemLogo();
        configurarEfeitosHover();
        configurarListeners();
        popularComboBox();
        configureCpfCnpjField(tipoPessoaField, tipoPessoaComboBox);
        configurePhoneField(telefoneField); // Chama a função para configurar o limite de telefone
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
    public static void configureCpfCnpjField(TextField cpfField, ComboBox<String> tipoDocumento) {
        tipoDocumento.valueProperty().addListener((observable, oldValue, newValue) -> {
            cpfField.clear();
            if (newValue != null) {
                if (newValue.equals("CPF")) {
                    cpfField.setPromptText("Digite o CPF (11 dígitos)");
                    configureCpfField(cpfField);
                } else if (newValue.equals("CNPJ")) {
                    cpfField.setPromptText("Digite o CNPJ (14 dígitos)");
                    configureCnpjField(cpfField);
                }
            }
        });

        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tipoDocumento.getValue() != null) {
                if (tipoDocumento.getValue().equals("CPF") && newValue.length() > 11) {
                    cpfField.setText(newValue.substring(0, 11));
                } else if (tipoDocumento.getValue().equals("CNPJ") && newValue.length() > 14) {
                    cpfField.setText(newValue.substring(0, 14));
                }
            }
        });
    }


    private static void configureCpfField(TextField cpfField) {
        cpfField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        }));
    }

    private static void configureCnpjField(TextField cpfField) {
        cpfField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        }));
    }

    private static void configurePhoneField(TextField phoneField) {
        phoneField.setTextFormatter(new TextFormatter<>(change -> {
            // Permite apenas números no campo de telefone
            change.setText(change.getText().replaceAll("[^0-9]", ""));

            // Limita o tamanho do texto a 11 caracteres (pode ser ajustado conforme necessário)
            if (change.getControlNewText().length() > 11) {
                change.setText(change.getText().substring(0, 11));
            }
            return change;
        }));
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
