package org.skywalkerhotel.skywalkerhotel.Model.Entitys;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Pagamentos {

    private IntegerProperty idPagamento;
    private ObjectProperty<LocalDate> dataPagamento;
    private StringProperty descricaoPagamento;
    private StringProperty tipoDespesa;
    private DoubleProperty precoQuarto;
    private DoubleProperty valorPagamento;
    private DoubleProperty totalPagamento;

    public Pagamentos(int idPagamento, LocalDate dataPagamento, String descricaoPagamento, String tipoDespesa,
                     double precoQuarto, double valorPagamento, double totalPagamento) {
        this.idPagamento = new SimpleIntegerProperty(idPagamento);
        this.dataPagamento = new SimpleObjectProperty<>(dataPagamento);
        this.descricaoPagamento = new SimpleStringProperty(descricaoPagamento);
        this.tipoDespesa = new SimpleStringProperty(tipoDespesa);
        this.precoQuarto = new SimpleDoubleProperty(precoQuarto);
        this.valorPagamento = new SimpleDoubleProperty(valorPagamento);
        this.totalPagamento = new SimpleDoubleProperty(totalPagamento);
    }

    public int getIdPagamento() {
        return idPagamento.get();
    }

    public IntegerProperty idPagamentoProperty() {
        return idPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento.get();
    }

    public ObjectProperty<LocalDate> dataPagamentoProperty() {
        return dataPagamento;
    }

    public String getDescricaoPagamento() {
        return descricaoPagamento.get();
    }

    public StringProperty descricaoPagamentoProperty() {
        return descricaoPagamento;
    }

    public String getTipoDespesa() {
        return tipoDespesa.get();
    }

    public StringProperty tipoDespesaProperty() {
        return tipoDespesa;
    }

    public double getPrecoQuarto() {
        return precoQuarto.get();
    }

    public DoubleProperty precoQuartoProperty() {
        return precoQuarto;
    }

    public double getValorPagamento() {
        return valorPagamento.get();
    }

    public DoubleProperty valorPagamentoProperty() {
        return valorPagamento;
    }

    public double getTotalPagamento() {
        return totalPagamento.get();
    }

    public DoubleProperty totalPagamentoProperty() {
        return totalPagamento;
    }
}
