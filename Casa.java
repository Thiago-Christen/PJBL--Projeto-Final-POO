package PJBL;

public class Casa extends Imovel{
    protected int quartos;
    protected int banheiros;
    protected  double area;
    protected  boolean garagem;
    protected boolean quintal;


    public Casa(int id, String titulo, String endereco, double preco,
                int quartos, int banheiros, double area, boolean garagem, boolean quintal) {
        super(id, titulo, endereco, preco);
        this.quartos = quartos;
        this.banheiros = banheiros;
        this.area = area;
        this.garagem = garagem;
        this.quintal = quintal;
    }

    @Override
    public void exibirDetalhes() {
        System.out.println("Casa: " + toString());
        System.out.println("Quartos: " + quartos + " | Banheiros: " + banheiros + " | Área: " + area + "m² | Quintal: " + (quintal ? "Sim" : "Não") + "Garagem: " + (garagem ? "Sim" : "Não"));
    }
}
