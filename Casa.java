package PJBL;

public class Casa extends Imovel{
    protected int quartos;
    protected int banheiros;
    protected double area;
    protected boolean garagem;
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
        System.out.println("Quartos: " + quartos + " | Banheiros: " + banheiros + " | Área: " + area + "m² | Quintal: " + (quintal ? "Sim" : "Não") + " | Garagem: " + (garagem ? "Sim" : "Não"));
        if ("reservado".equalsIgnoreCase(status)) {
            System.out.println("Reservado por cliente: " + reservadoPorCliente + " | Corretor: " + reservadoPorCorretor
                    + " | Data/Hora: " + reservaToCsvField());
        }
    }

    @Override
    public String toCsvLine() {
        // Ordem do CSV: id,tipo,titulo,endereco,preco,quartos,banheiros,area,andar,sacada,valorCondominio,garagem,quintal,status,reservadoPorCliente,reservadoPorCorretor,dataReserva,observacoes,visitas
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",casa,");
        sb.append(quote(titulo)).append(",");
        sb.append(quote(endereco)).append(",");
        sb.append(preco).append(",");
        sb.append(quartos).append(",");
        sb.append(banheiros).append(",");
        sb.append(area).append(",");
        sb.append(","); // andar vazio
        sb.append(","); // sacada vazio
        sb.append(","); // valorCondominio vazio
        sb.append(garagem).append(",");
        sb.append(quintal).append(",");
        sb.append(status).append(",");
        sb.append(quote(reservadoPorCliente)).append(",");
        sb.append(quote(reservadoPorCorretor)).append(",");
        sb.append(quote(reservaToCsvField())).append(",");
        sb.append(quote(observacoes)).append(",");
        sb.append(visitas);
        return sb.toString();
    }

    private String quote(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
