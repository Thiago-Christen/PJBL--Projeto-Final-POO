package PJBL;

import java.time.LocalDateTime;

public class ImovelFactory {

    // parse simples, tratando aspas: split por vírgula respeitando aspas
    private static String[] splitCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            s = s.substring(1, s.length()-1).replace("\"\"", "\"");
        }
        return s;
    }

    public static Imovel fromCsvLine(String line) {
        String[] cols = splitCsvLine(line);
        // Espera o header descrito anteriormente -> index por coluna:
        // 0:id,1:tipo,2:titulo,3:endereco,4:preco,5:quartos,6:banheiros,7:area,8:andar,9:sacada,10:valorCondominio,11:garagem,12:quintal,13:status,14:reservadoPorCliente,15:reservadoPorCorretor,16:dataReserva,17:observacoes,18:visitas
        try {
            int id = Integer.parseInt(cols[0]);
            String tipo = unquote(cols[1]).toLowerCase();
            String titulo = unquote(cols[2]);
            String endereco = unquote(cols[3]);
            double preco = cols[4].isEmpty() ? 0.0 : Double.parseDouble(cols[4]);
            Imovel imovel = null;
            if ("apartamento".equals(tipo)) {
                int quartos = cols[5].isEmpty() ? 0 : Integer.parseInt(cols[5]);
                double area = cols[7].isEmpty() ? 0.0 : Double.parseDouble(cols[7]);
                int andar = cols[8].isEmpty() ? 0 : Integer.parseInt(cols[8]);
                boolean sacada = cols[9].equalsIgnoreCase("true");
                double valorCondominio = cols[10].isEmpty() ? 0.0 : Double.parseDouble(cols[10]);
                imovel = new Apartamento(id, titulo, endereco, preco, quartos, area, andar, sacada, valorCondominio);
            } else { // casa
                int quartos = cols[5].isEmpty() ? 0 : Integer.parseInt(cols[5]);
                int banheiros = cols[6].isEmpty() ? 0 : Integer.parseInt(cols[6]);
                double area = cols[7].isEmpty() ? 0.0 : Double.parseDouble(cols[7]);
                boolean garagem = cols[11].equalsIgnoreCase("true");
                boolean quintal = cols[12].equalsIgnoreCase("true");
                imovel = new Casa(id, titulo, endereco, preco, quartos, banheiros, area, garagem, quintal);
            }

            // status, reservas e visitas
            imovel.status = (cols.length > 13) ? unquote(cols[13]) : "disponivel";
            imovel.reservadoPorCliente = (cols.length > 14) ? unquote(cols[14]) : "";
            imovel.reservadoPorCorretor = (cols.length > 15) ? unquote(cols[15]) : "";
            String dataReserva = (cols.length > 16) ? unquote(cols[16]) : "";
            if (!dataReserva.isEmpty()) {
                imovel.dataReserva = LocalDateTime.parse(dataReserva, Imovel.DATETIME_FMT);
            }
            imovel.observacoes = (cols.length > 17) ? unquote(cols[17]) : "";
            imovel.visitas = (cols.length > 18 && !cols[18].isEmpty()) ? Integer.parseInt(cols[18]) : 0;

            return imovel;
        } catch (Exception e) {
            System.err.println("Erro ao parsear linha CSV: " + e.getMessage());
            return null;
        }
    }
}
