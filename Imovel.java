package PJBL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Imovel {
    protected int id;
    protected String titulo;
    protected String endereco;
    protected double preco;
    protected String status;
    protected int visitas;
    protected String reservadoPorCliente;
    protected String reservadoPorCorretor;
    protected LocalDateTime dataReserva;
    protected String observacoes;

    public static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Imovel(int id, String titulo, String endereco, double preco) {
        this.id = id;
        this.titulo = titulo;
        this.endereco = endereco;
        this.preco = preco;
        this.status = "disponivel";
        this.visitas = 0;
        this.reservadoPorCliente = "";
        this.reservadoPorCorretor = "";
        this.dataReserva = null;
        this.observacoes = "";
    }

    public boolean reservar(String nomeCliente, String nomeCorretor, LocalDateTime dataHora) {
        if (!"disponivel".equalsIgnoreCase(status)) return false;
        this.status = "reservado";
        this.reservadoPorCliente = nomeCliente;
        this.reservadoPorCorretor = nomeCorretor;
        this.dataReserva = dataHora;
        return true;
    }

    public boolean cancelarReserva() {
        if (!"reservado".equalsIgnoreCase(status)) return false;
        this.status = "disponivel";
        this.reservadoPorCliente = "";
        this.reservadoPorCorretor = "";
        this.dataReserva = null;
        return true;
    }

    public void incrementarVisita() { visitas++; }

    @Override
    public String toString() {
        return id + " - " + titulo + " - " + endereco + " - R$" + preco + " - " + status;
    }

    protected String reservaToCsvField() {
        return (dataReserva == null) ? "" : dataReserva.format(DATETIME_FMT);
    }

    public abstract void exibirDetalhes();

    // método para gerar a linha CSV
    public abstract String toCsvLine();
}
