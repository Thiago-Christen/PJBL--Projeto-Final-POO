package PJBL;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class ImovelRepository {

    public static List<Imovel> carregarDoCsv(Path path) throws IOException {
        List<String> linhas = Files.readAllLines(path);
        if (linhas.isEmpty()) return Collections.emptyList();
        // remove header
        List<String> dados = linhas.subList(1, linhas.size());
        List<Imovel> lista = new ArrayList<>();
        for (String l : dados) {
            Imovel im = ImovelFactory.fromCsvLine(l);
            if (im != null) lista.add(im);
        }
        return lista;
    }

    public static void salvarNoCsv(Path path, List<Imovel> imoveis) throws IOException {
        List<String> linhas = new ArrayList<>();
        // header
        linhas.add("id,tipo,titulo,endereco,preco,quartos,banheiros,area,andar,sacada,valorCondominio,garagem,quintal,status,reservadoPorCliente,reservadoPorCorretor,dataReserva,observacoes,visitas");
        for (Imovel im : imoveis) {
            linhas.add(im.toCsvLine());
        }
        Files.write(path, linhas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // busca por id
    public static Optional<Imovel> findById(List<Imovel> imoveis, int id) {
        return imoveis.stream().filter(i -> i.id == id).findFirst();
    }

    // busca por disponibilidade
    public static List<Imovel> listarDisponiveis(List<Imovel> imoveis) {
        return imoveis.stream().filter(i -> "disponivel".equalsIgnoreCase(i.status)).collect(Collectors.toList());
    }
}
