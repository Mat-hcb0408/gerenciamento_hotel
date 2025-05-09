package org.skywalkerhotel.skywalkerhotel.Model.Entitys;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Hospedes {
    private final IntegerProperty idHospede;
    private final StringProperty nome;
    private final ObjectProperty<LocalDate> nascimento;
    private final StringProperty telefone;
    private final StringProperty tipoPessoa;
    private final StringProperty cpf;
    private final StringProperty cnpj;

    public Hospedes(int id, String nome, LocalDate nascimento, String telefone, String tipoPessoa, String cpf, String cnpj) {
        this.idHospede = new SimpleIntegerProperty(id);
        this.nome = new SimpleStringProperty(nome);
        this.nascimento = new SimpleObjectProperty<>(nascimento);
        this.telefone = new SimpleStringProperty(telefone);
        this.tipoPessoa = new SimpleStringProperty(tipoPessoa);
        this.cpf = new SimpleStringProperty(cpf);
        this.cnpj = new SimpleStringProperty(cnpj);
    }

    // Getter amigável para "id"
    public int getId() {
        return idHospede.get();
    }

    // Outros getters
    public String getNome() {
        return nome.get();
    }

    public LocalDate getNascimento() {
        return nascimento.get();
    }

    public String getTelefone() {
        return telefone.get();
    }

    public String getTipoPessoa() {
        return tipoPessoa.get();
    }

    public String getCpf() {
        return cpf.get();
    }

    public String getCnpj() {
        return cnpj.get();
    }

    // Métodos Property
    public IntegerProperty idHospedeProperty() {
        return idHospede;
    }

    public StringProperty nomeProperty() {
        return nome;
    }

    public ObjectProperty<LocalDate> nascimentoProperty() {
        return nascimento;
    }

    public StringProperty telefoneProperty() {
        return telefone;
    }

    public StringProperty tipoPessoaProperty() {
        return tipoPessoa;
    }

    public StringProperty cpfProperty() {
        return cpf;
    }

    public StringProperty cnpjProperty() {
        return cnpj;
    }
}
