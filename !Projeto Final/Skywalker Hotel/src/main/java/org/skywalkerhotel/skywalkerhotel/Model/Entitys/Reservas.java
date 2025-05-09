package org.skywalkerhotel.skywalkerhotel.Model.Entitys;

import java.time.LocalDate;

public class Reservas {
    private int idReserva;
    private String nomeHospede;   // Usando nome ao invés de ID
    private String nomeQuarto;    // Usando nome ao invés de ID
    private int idPagamento;
    private LocalDate inicioDataReserva;
    private LocalDate fimDataReserva;

    // Construtor com os parâmetros necessários
    public Reservas(int idReserva, String nomeHospede, String nomeQuarto, int idPagamento,
                    LocalDate inicioDataReserva, LocalDate fimDataReserva) {
        this.idReserva = idReserva;
        this.nomeHospede = nomeHospede;
        this.nomeQuarto = nomeQuarto;
        this.idPagamento = idPagamento;
        this.inicioDataReserva = inicioDataReserva;
        this.fimDataReserva = fimDataReserva;
    }

    // Getters e Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getNomeHospede() {
        return nomeHospede;
    }

    public void setNomeHospede(String nomeHospede) {
        this.nomeHospede = nomeHospede;
    }

    public String getNomeQuarto() {
        return nomeQuarto;
    }

    public void setNomeQuarto(String nomeQuarto) {
        this.nomeQuarto = nomeQuarto;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public LocalDate getInicioDataReserva() {
        return inicioDataReserva;
    }

    public void setInicioDataReserva(LocalDate inicioDataReserva) {
        this.inicioDataReserva = inicioDataReserva;
    }

    public LocalDate getFimDataReserva() {
        return fimDataReserva;
    }

    public void setFimDataReserva(LocalDate fimDataReserva) {
        this.fimDataReserva = fimDataReserva;
    }

    @Override
    public String toString() {
        return "Reserva [idReserva=" + idReserva + ", nomeHospede=" + nomeHospede + ", nomeQuarto=" + nomeQuarto +
                ", idPagamento=" + idPagamento + ", inicioDataReserva=" + inicioDataReserva +
                ", fimDataReserva=" + fimDataReserva + "]";
    }
}
