package org.skywalkerhotel.skywalkerhotel.Model.Entitys;

import javafx.beans.property.*;

public class Quartos {
    private final IntegerProperty idQuarto;
    private final StringProperty nome;
    private final IntegerProperty casal;
    private final IntegerProperty solteiro;
    private final IntegerProperty maxPessoas;
    private final StringProperty status;
    private final DoubleProperty preco;

    public Quartos(int idQuarto, String nome, Integer casal, Integer solteiro, int maxPessoas, String status, double preco) {
        this.idQuarto = new SimpleIntegerProperty(idQuarto);
        this.nome = new SimpleStringProperty(nome);
        this.casal = new SimpleIntegerProperty(casal);
        this.solteiro = new SimpleIntegerProperty(solteiro);
        this.maxPessoas = new SimpleIntegerProperty(maxPessoas);
        this.status = new SimpleStringProperty(status);
        this.preco = new SimpleDoubleProperty(preco);
    }

    public int getIdQuarto() { return idQuarto.get(); }
    public String getNome() { return nome.get(); }
    public Integer isCasal() { return casal.get(); }
    public Integer isSolteiro() { return solteiro.get(); }
    public int getMaxPessoas() { return maxPessoas.get(); }
    public String getStatus() { return status.get(); }
    public double getPreco() { return preco.get(); }

    // Propriedades para vinculação (bindings)
    public IntegerProperty idQuartoProperty() { return idQuarto; }
    public StringProperty nomeProperty() { return nome; }
    public IntegerProperty casalProperty() { return casal; }
    public IntegerProperty solteiroProperty() { return solteiro; }
    public IntegerProperty maxPessoasProperty() { return maxPessoas; }
    public StringProperty statusProperty() { return status; }
    public DoubleProperty precoProperty() { return preco; }
}
