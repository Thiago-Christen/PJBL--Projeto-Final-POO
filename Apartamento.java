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
        System.out.println("Quartos: " + quartos + " | Área: " + area + "m² | Andar: " + andar
                + " | Sacada: " + (sacada ? "Sim" : "Não") +  " | Condomínio: R$" + valorCondominio);
        if ("reservado".equalsIgnoreCase(status)) {
            System.out.println("Reservado por cliente: " + reservadoPorCliente + " | Corretor: " + reservadoPorCorretor
                    + " | Data/Hora: " + reservaToCsvField());
        }
    }

    @Override
    public String toCsvLine() {
        // Ordem do CSV: id,tipo,titulo,endereco,preco,quartos,banheiros,area,andar,sacada,valorCondominio,garagem,quintal,status,reservadoPorCliente,reservadoPorCorretor,dataReserva,observacoes,visitas
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",apartamento,");
        sb.append(quote(titulo)).append(",");
        sb.append(quote(endereco)).append(",");
        sb.append(preco).append(",");
        sb.append(quartos).append(","); // banheiros vazio
        sb.append(","); // banheiros
        sb.append(area).append(",");
        sb.append(andar).append(",");
        sb.append(sacada).append(",");
        sb.append(valorCondominio).append(",");
        sb.append(","); // garagem
        sb.append("false,"); // quintal (não aplicável)
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
