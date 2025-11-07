package PJBL;

public class Casa extends Imovel implements Alugavel, Vendavel {
    protected double precoBase;
    protected double tamTerreno;
    protected boolean possuiGaragem;
    protected boolean possuiQuintal;

    public Casa(int id, String descricao, String endereco, String complemento, double precoBase,
                boolean disponivelVenda, boolean disponivelAluguel, double tamTerreno,
                boolean possuiGaragem, boolean possuiQuintal) {
        super(id,descricao,endereco,complemento,disponivelVenda,disponivelAluguel);
        this.precoBase = precoBase;
        this.tamTerreno = tamTerreno;
        this.possuiGaragem = possuiGaragem;
        this.possuiQuintal = possuiQuintal;
    }

    @Override
    public double calcularPrecoAluguelMensal() {
        return (precoBase * 0.007);
    }

    @Override
    public void alugar() {
        if (disponivelAluguel) {
            System.out.println("Este imóvel pode ser alugado por" + calcularPrecoAluguelMensal()+ "reais");
        }
        else {
            System.out.println("Imóvel indisponível");
        }
    }

    @Override
    public double calcularPrecoVenda() {
        double valorFinal = precoBase;
        if(possuiQuintal) valorFinal *= 1.05;
        return valorFinal;
    }

    @Override
    public void vender() {
        if (disponivelVenda) {
            System.out.println("Esse imóvel pode ser vendido por" +  calcularPrecoVenda()+ "reais");
        }
        else {
            System.out.println("Imóvel indisponível");
        }
    }
}
