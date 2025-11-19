package PJBL;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Path path = Paths.get(args.length > 0 ? args[0] : "C:\\PJBL POO\\PJBL\\src\\PJBL\\PJBL\\PJBL\\Doc\\base_de_dados_imobiliarias(in).csv").normalize().toAbsolutePath();
            System.out.println("Path do CSV: " + path.toString());

            List<Imovel> imoveis = ImovelRepository.carregarDoCsv(path);
            System.out.println("Imóveis carregados: " + imoveis.size());

            boolean running = true;
            while (running) {
                System.out.println("\n=== MENU ===");
                System.out.println("1 - Agendar / Reservar");
                System.out.println("2 - Listar Imóveis");
                System.out.println("3 - Cadastrar Imóvel");
                System.out.println("4 - Sair");
                System.out.print("Escolha uma opção (1-4): ");
                String opc = sc.nextLine().trim();

                switch (opc) {
                    case "1":
                        agendarReserva(sc, imoveis, path);
                        break;
                    case "2":
                        listarImoveis(imoveis);
                        break;
                    case "3":
                        cadastrarImovel(sc, imoveis, path);
                        break;
                    case "4":
                        running = false;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente 1, 2, 3 ou 4.");
                }
            }

            System.out.println("Saindo... Até mais.");
        } catch (Exception e) {
            System.err.println("Erro na aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarImoveis(List<Imovel> imoveis) {
        if (imoveis.isEmpty()) {
            System.out.println("Nenhum imóvel cadastrado.");
            return;
        }
        System.out.println("\n--- Lista de Imóveis ---");
        for (Imovel im : imoveis) {
            String tipo = im.getClass().getSimpleName();
            String reservaInfo = "";
            if ("reservado".equalsIgnoreCase(im.status) && im.dataReserva != null) {
                reservaInfo = " | Reserva: " + im.reservadoPorCliente + " / Corretor: " + im.reservadoPorCorretor
                        + " | " + im.dataReserva.format(Imovel.DATETIME_FMT);
            }
            System.out.println(String.format("ID: %d | %s | %s | %s%s", im.id, tipo, im.titulo, im.status, reservaInfo));
        }
    }

    private static void agendarReserva(Scanner sc, List<Imovel> imoveis, Path path) {
        try {
            if (imoveis.isEmpty()) {
                System.out.println("Não há imóveis para reservar.");
                return;
            }
            listarImoveis(imoveis);
            System.out.print("\nInforme o ID do imóvel que deseja reservar: ");
            String idStr = sc.nextLine().trim();
            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException nfe) {
                System.out.println("ID inválido.");
                return;
            }

            Optional<Imovel> maybe = ImovelRepository.findById(imoveis, id);
            if (maybe.isEmpty()) {
                System.out.println("Imóvel com ID " + id + " não encontrado.");
                return;
            }
            Imovel im = maybe.get();

            if (!"disponivel".equalsIgnoreCase(im.status)) {
                System.out.println("Imóvel não está disponível para reserva. Status atual: " + im.status);
                if (im.dataReserva != null) {
                    System.out.println("Reservado por: " + im.reservadoPorCliente + " | Corretor: " + im.reservadoPorCorretor
                            + " | Data/Hora: " + im.dataReserva.format(Imovel.DATETIME_FMT));
                }
                return;
            }

            System.out.print("Nome do cliente: ");
            String nomeCliente = sc.nextLine().trim();
            if (nomeCliente.isEmpty()) {
                System.out.println("Nome do cliente não pode ser vazio.");
                return;
            }

            System.out.print("Nome do corretor: ");
            String nomeCorretor = sc.nextLine().trim();
            if (nomeCorretor.isEmpty()) {
                System.out.println("Nome do corretor não pode ser vazio.");
                return;
            }

            System.out.print("Data e hora da reserva (formato yyyy-MM-dd HH:mm, ex: 2025-11-20 14:30): ");
            String dataStr = sc.nextLine().trim();
            LocalDateTime dataHora;
            try {
                dataHora = LocalDateTime.parse(dataStr, Imovel.DATETIME_FMT);
            } catch (Exception ex) {
                System.out.println("Formato de data/hora inválido. Use yyyy-MM-dd HH:mm");
                return;
            }

            boolean ok = im.reservar(nomeCliente, nomeCorretor, dataHora);
            if (ok) {
                ImovelRepository.salvarNoCsv(path, imoveis);
                System.out.println("Reserva efetuada com sucesso para o imóvel ID " + id + " em " + dataHora.format(Imovel.DATETIME_FMT));
            } else {
                System.out.println("Falha ao reservar — verifique disponibilidade.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar reservar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cadastrarImovel(Scanner sc, List<Imovel> imoveis, Path path) {
        try {
            System.out.println("\n--- Cadastrar Imóvel ---");
            System.out.println("1 - Apartamento");
            System.out.println("2 - Casa");
            System.out.print("Escolha o tipo (1-2): ");
            String tipo = sc.nextLine().trim();
            if (!tipo.equals("1") && !tipo.equals("2")) {
                System.out.println("Tipo inválido.");
                return;
            }

            // campos comuns
            System.out.print("Título: ");
            String titulo = sc.nextLine().trim();
            if (titulo.isEmpty()) { System.out.println("Título não pode ser vazio."); return; }

            System.out.print("Endereço: ");
            String endereco = sc.nextLine().trim();
            if (endereco.isEmpty()) { System.out.println("Endereço não pode ser vazio."); return; }

            double preco;
            System.out.print("Preço (ex: 350000.0): ");
            String precoStr = sc.nextLine().trim();
            try { preco = Double.parseDouble(precoStr); } catch (NumberFormatException n) { System.out.println("Preço inválido."); return; }

            // calcula novo id
            int novoId = imoveis.stream().mapToInt(i -> i.id).max().orElse(0) + 1;

            if (tipo.equals("1")) { // Apartamento
                int quartos = lerIntComPrompt(sc, "Quartos (ex: 2): ");
                if (quartos < 0) return;
                double area = lerDoubleComPrompt(sc, "Área m² (ex: 65.0): ");
                if (area < 0) return;
                int andar = lerIntComPrompt(sc, "Andar (ex: 3): ");
                if (andar < 0) return;
                boolean sacada = lerBooleanYesNo(sc, "Possui sacada? (s/n): ");
                double valorCondominio = lerDoubleComPrompt(sc, "Valor do condomínio (ex: 450.0) - se nenhum digite 0: ");
                if (valorCondominio < 0) return;

                Apartamento ap = new Apartamento(novoId, titulo, endereco, preco, quartos, area, andar, sacada, valorCondominio);
                imoveis.add(ap);
                ImovelRepository.salvarNoCsv(path, imoveis);
                System.out.println("Apartamento cadastrado com sucesso. ID: " + novoId);
            } else { // Casa
                int quartos = lerIntComPrompt(sc, "Quartos (ex: 3): ");
                if (quartos < 0) return;
                int banheiros = lerIntComPrompt(sc, "Banheiros (ex: 2): ");
                if (banheiros < 0) return;
                double area = lerDoubleComPrompt(sc, "Área m² (ex: 120.0): ");
                if (area < 0) return;
                boolean garagem = lerBooleanYesNo(sc, "Possui garagem? (s/n): ");
                boolean quintal = lerBooleanYesNo(sc, "Possui quintal? (s/n): ");

                Casa casa = new Casa(novoId, titulo, endereco, preco, quartos, banheiros, area, garagem, quintal);
                imoveis.add(casa);
                ImovelRepository.salvarNoCsv(path, imoveis);
                System.out.println("Casa cadastrada com sucesso. ID: " + novoId);
            }
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar imóvel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // helpers de leitura com validação simples
    private static int lerIntComPrompt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try { return Integer.parseInt(s); }
        catch (NumberFormatException nfe) { System.out.println("Valor inteiro inválido."); return -1; }
    }

    private static double lerDoubleComPrompt(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();
        try { return Double.parseDouble(s); }
        catch (NumberFormatException nfe) { System.out.println("Valor numérico inválido."); return -1; }
    }

    private static boolean lerBooleanYesNo(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine().trim().toLowerCase();
        return s.startsWith("s") || s.startsWith("y") || s.equals("true");
    }
}
