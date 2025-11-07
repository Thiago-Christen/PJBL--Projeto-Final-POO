package PJBL;

public class Apartamento extends Imovel implements Alugavel {
    protected double precoAluguelBase;
    protected int andar;
    protected int vagasGaragem;
    protected double valorCondominio;
    protected boolean possuiElevador;
    protected boolean possuiSacada;

    public Apartamento(int id, String descricao, String endereco, String complemento, double precoAluguelBase,
                       boolean disponivelVenda, boolean disponivelAluguel, int andar, int vagasGaragem,
                       double valorCondominio, boolean possuiElevador, boolean possuiSacada) {
        super(id, descricao, endereco, complemento, disponivelVenda, disponivelAluguel);
        this.precoAluguelBase = precoAluguelBase;
        this.andar = andar;
        this.vagasGaragem = vagasGaragem;
        this.valorCondominio = valorCondominio;
        this.possuiElevador = possuiElevador;
        this.possuiSacada = possuiSacada;
    }

    @Override
    public double calcularPrecoAluguelMensal() {
        return precoAluguelBase + valorCondominio;
    }

    @Override
    public void alugar() {
        if (disponivelAluguel) {
            System.out.println("Esse imóvel pode ser alugado por"+calcularPrecoAluguelMensal()+"reais");
        }
        else {
            System.out.println("imóvel indisponível para aluguel");
        }
    }
}