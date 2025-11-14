package PJBL;

public class Apartamento extends Imovel {
    protected int quartos;
    protected double area;
    protected int andar;
    protected boolean sacada;
    protected double valorCondominio;

    public Apartamento(int id, String titulo, String endereco, double preco,
                       int quartos, double area, int andar, boolean sacada ,double valorCondominio) {
        super(id, titulo, endereco, preco);
        this.quartos = quartos;
        this.area = area;
        this.andar = andar;
        this.sacada = sacada;
        this.valorCondominio = valorCondominio;
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Apartamento: " + toString());
        System.out.println("Quartos: " + quartos + " | Área: " + area + "m² | Andar: " + andar + "| Sacada: " + (sacada ? "Sim" : "Não") +  "| Condomínio: R$" + valorCondominio);
    }
}