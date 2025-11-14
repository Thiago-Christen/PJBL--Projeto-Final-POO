package PJBL;

import java.time.LocalDate;

public abstract class Imovel {
    protected int id;
    protected String titulo;
    protected String endereco;
    protected double preco;
    protected String status;
    protected int visitas;
    protected String reservadoPor;
    protected LocalDate dataReserva;
    protected String observacoes;

    public Imovel(int id, String titulo, String endereco, double preco) {
        this.id = id;
        this.titulo = titulo;
        this.endereco = endereco;
        this.preco = preco;
        this.status = "disponivel";
        this.visitas = 0;
        this.reservadoPor = "";
        this.dataReserva = null;
        this.observacoes = "";
    }

    public boolean reservar(String clienteNome, LocalDate data) {
        if (!"disponivel".equalsIgnoreCase(status)) return false;
        this.status = "reservado";
        this.reservadoPor = clienteNome;
        this.dataReserva = data;
        return true;
    }
    public boolean cancelarReserva() {
        if (!"reservado".equalsIgnoreCase(status)) return false;
        this.status = "disponivel";
        this.reservadoPor = "";
        this.dataReserva = null;
        return true;
    }

    @Override
    public String toString() {
        return id + " - " + titulo + " - " + endereco + " - R$" + preco + " - " + status;
    }

    public abstract void exibirDetalhes();

    public void incrementarVisita() { visitas++; }
}