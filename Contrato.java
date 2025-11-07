package PJBL;

import java.time.LocalDate;

public class Contrato {
    protected int numero;
    protected LocalDate data;
    protected String tipo;
    protected Imovel imovel;
    protected Corretor corretor;
    protected Cliente cliente;
    protected double valorFinal;
    protected boolean finalizado;

    public Contrato(int numero, LocalDate data, Corretor corretor, Imovel imovel,
                    Cliente cliente, double valorFinal, boolean finalizado) {
        this.numero = numero;
        this.data = data;
        this.corretor = corretor;
        this.imovel = imovel;
        this.cliente = cliente;
        this.valorFinal = valorFinal;
        this.finalizado = finalizado;
    }
}