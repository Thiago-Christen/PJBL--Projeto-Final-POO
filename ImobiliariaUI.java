package PJBL;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface gráfica (Swing) reduzida com apenas 4 opções:
 * 1 - Cadastrar Imóvel
 * 2 - Fazer Reserva
 * 3 - Listar Imóveis
 * 4 - Sair
 *
 * Observações:
 * - Ao cadastrar ou reservar, o sistema salva automaticamente no arquivo CSV definido em `arquivoCsv`.
 * - Mantenha este arquivo no package PJBL para acessar os campos protegidos das classes modelo.
 */
public class ImobiliariaUI extends JFrame {

    private Path arquivoCsv;                  // caminho do CSV usado (definido no main ou imoveis.csv)
    private List<Imovel> listaImoveis;        // lista carregada em memória

    private ImovelTableModel tableModel;
    private JTable tabela;
    private JLabel labelArquivo;

    public ImobiliariaUI() {
        super("Sistema Imobiliária - Interface");
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Ao fechar a janela, tenta salvar automaticamente (se lista já carregada)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // nada
            }

            @Override
            public void windowClosing(WindowEvent e) {
                tentarSalvarAutomatico();
            }
        });
    }

    private void initComponents() {
        // Top: apenas 4 botões conforme solicitado
        JPanel topPainel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCadastrar = new JButton("1 - Cadastrar Imóvel");
        JButton btnReservar = new JButton("2 - Fazer Reserva");
        JButton btnListar = new JButton("3 - Listar Imóveis");
        JButton btnSair = new JButton("4 - Sair");

        labelArquivo = new JLabel("Arquivo: (nenhum)");

        topPainel.add(btnCadastrar);
        topPainel.add(btnReservar);
        topPainel.add(btnListar);
        topPainel.add(btnSair);
        topPainel.add(Box.createHorizontalStrut(10));
        topPainel.add(labelArquivo);

        // Table (mostra a lista completa)
        tableModel = new ImovelTableModel();
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tabela);

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPainel, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        // Ações
        btnCadastrar.addActionListener(e -> actionCadastrar());
        btnReservar.addActionListener(e -> actionReservar());
        btnListar.addActionListener(e -> actionListar());
        btnSair.addActionListener(e -> {
            tentarSalvarAutomatico();
            dispose();
        });

        // Duplo-clique na tabela para exibir detalhes
        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int linha = tabela.getSelectedRow();
                    if (linha >= 0) mostrarDetalhes(linha);
                }
            }
        });
    }

    /* ---------- Operações públicas que podem ser usadas externamente ---------- */

    public void carregarListaDoArquivo(Path path) {
        try {
            this.arquivoCsv = (path == null) ? null : path.normalize().toAbsolutePath();
            listaImoveis = ImovelRepository.carregarDoCsv(this.arquivoCsv == null ? Paths.get("imoveis.csv").toAbsolutePath() : this.arquivoCsv);
            tableModel.setImoveis(listaImoveis);
            labelArquivo.setText("Arquivo: " + (this.arquivoCsv == null ? Paths.get("imoveis.csv").toAbsolutePath().toString() : this.arquivoCsv.toString()));
            JOptionPane.showMessageDialog(this, "Imóveis carregados: " + (listaImoveis == null ? 0 : listaImoveis.size()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar CSV:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /* ---------- Ações do UI (reduzidas) ---------- */

    private void actionCadastrar() {
        // Igual ao fluxo anterior: pergunta tipo e campos, cria objeto, adiciona e salva automaticamente.
        String[] opcoesTipo = {"Apartamento", "Casa"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Tipo de imóvel:", "Cadastrar",
                JOptionPane.PLAIN_MESSAGE, null, opcoesTipo, opcoesTipo[0]);
        if (tipo == null) return; // cancelou

        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField tfTitulo = new JTextField();
        JTextField tfEndereco = new JTextField();
        JTextField tfPreco = new JTextField();

        painel.add(new JLabel("Título:")); painel.add(tfTitulo);
        painel.add(new JLabel("Endereço:")); painel.add(tfEndereco);
        painel.add(new JLabel("Preço:")); painel.add(tfPreco);

        if ("Apartamento".equals(tipo)) {
            JTextField tfQuartos = new JTextField();
            JTextField tfArea = new JTextField();
            JTextField tfAndar = new JTextField();
            JCheckBox cbSacada = new JCheckBox("Possui sacada");
            JTextField tfValorCond = new JTextField();

            painel.add(new JLabel("Quartos:")); painel.add(tfQuartos);
            painel.add(new JLabel("Área (m²):")); painel.add(tfArea);
            painel.add(new JLabel("Andar:")); painel.add(tfAndar);
            painel.add(new JLabel("Condomínio (R$):")); painel.add(tfValorCond);
            painel.add(new JLabel("")); painel.add(cbSacada);

            int escolha = JOptionPane.showConfirmDialog(this, painel, "Cadastrar Apartamento", JOptionPane.OK_CANCEL_OPTION);
            if (escolha != JOptionPane.OK_OPTION) return;

            try {
                String titulo = tfTitulo.getText().trim();
                String endereco = tfEndereco.getText().trim();
                double preco = Double.parseDouble(tfPreco.getText().trim());
                int quartos = Integer.parseInt(tfQuartos.getText().trim());
                double area = Double.parseDouble(tfArea.getText().trim());
                int andar = Integer.parseInt(tfAndar.getText().trim());
                boolean sacada = cbSacada.isSelected();
                double valorCond = Double.parseDouble(tfValorCond.getText().trim());

                int novoId = calcularNovoId();
                Apartamento ap = new Apartamento(novoId, titulo, endereco, preco, quartos, area, andar, sacada, valorCond);
                listaImoveis.add(ap);
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Apartamento cadastrado. ID: " + novoId);

                // salva automático após cadastro
                salvarSePossivel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro nos campos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } else { // Casa
            JTextField tfQuartos = new JTextField();
            JTextField tfBanheiros = new JTextField();
            JTextField tfArea = new JTextField();
            JCheckBox cbGaragem = new JCheckBox("Possui garagem");
            JCheckBox cbQuintal = new JCheckBox("Possui quintal");

            painel.add(new JLabel("Quartos:")); painel.add(tfQuartos);
            painel.add(new JLabel("Banheiros:")); painel.add(tfBanheiros);
            painel.add(new JLabel("Área (m²):")); painel.add(tfArea);
            painel.add(new JLabel("")); painel.add(cbGaragem);
            painel.add(new JLabel("")); painel.add(cbQuintal);

            int escolha = JOptionPane.showConfirmDialog(this, painel, "Cadastrar Casa", JOptionPane.OK_CANCEL_OPTION);
            if (escolha != JOptionPane.OK_OPTION) return;

            try {
                String titulo = tfTitulo.getText().trim();
                String endereco = tfEndereco.getText().trim();
                double preco = Double.parseDouble(tfPreco.getText().trim());
                int quartos = Integer.parseInt(tfQuartos.getText().trim());
                int banheiros = Integer.parseInt(tfBanheiros.getText().trim());
                double area = Double.parseDouble(tfArea.getText().trim());
                boolean garagem = cbGaragem.isSelected();
                boolean quintal = cbQuintal.isSelected();

                int novoId = calcularNovoId();
                Casa casa = new Casa(novoId, titulo, endereco, preco, quartos, banheiros, area, garagem, quintal);
                listaImoveis.add(casa);
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Casa cadastrada. ID: " + novoId);

                // salva automático após cadastro
                salvarSePossivel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro nos campos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actionReservar() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um imóvel na tabela primeiro.");
            return;
        }
        Imovel im = listaImoveis.get(linha);
        if (!"disponivel".equalsIgnoreCase(im.status)) {
            JOptionPane.showMessageDialog(this, "Imóvel não disponível. Status: " + im.status);
            return;
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField tfCliente = new JTextField();
        JTextField tfCorretor = new JTextField();
        JTextField tfDataHora = new JTextField("yyyy-MM-dd HH:mm");

        painel.add(new JLabel("Nome do cliente:")); painel.add(tfCliente);
        painel.add(new JLabel("Nome do corretor:")); painel.add(tfCorretor);
        painel.add(new JLabel("Data e hora (yyyy-MM-dd HH:mm):")); painel.add(tfDataHora);

        int escolha = JOptionPane.showConfirmDialog(this, painel, "Reservar Imóvel ID " + im.id, JOptionPane.OK_CANCEL_OPTION);
        if (escolha != JOptionPane.OK_OPTION) return;

        try {
            String cliente = tfCliente.getText().trim();
            String corretor = tfCorretor.getText().trim();
            LocalDateTime dt = LocalDateTime.parse(tfDataHora.getText().trim(), Imovel.DATETIME_FMT);

            boolean ok = im.reservar(cliente, corretor, dt);
            if (ok) {
                tableModel.fireTableDataChanged();
                JOptionPane.showMessageDialog(this, "Reserva efetuada.");

                // salva automático após reserva
                salvarSePossivel();
            } else {
                JOptionPane.showMessageDialog(this, "Falha ao reservar (verifique disponibilidade).");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao reservar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionListar() {
        // Mostra um resumo rápido em diálogo. A tabela já apresenta os detalhes.
        if (listaImoveis == null || listaImoveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum imóvel cadastrado.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Imovel im : listaImoveis) {
            sb.append("ID: ").append(im.id)
                    .append(" | ").append(im.getClass().getSimpleName())
                    .append(" | ").append(im.titulo)
                    .append(" | ").append(im.status)
                    .append("\n");
        }
        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, sp, "Lista de Imóveis (resumo)", JOptionPane.INFORMATION_MESSAGE);
    }

    /* ---------- utilitários / helpers ---------- */

    private int calcularNovoId() {
        return listaImoveis.stream().mapToInt(i -> i.id).max().orElse(0) + 1;
    }

    private void mostrarDetalhes(int linha) {
        Imovel im = listaImoveis.get(linha);
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(im.id).append("\n");
        sb.append("Título: ").append(im.titulo).append("\n");
        sb.append("Endereço: ").append(im.endereco).append("\n");
        sb.append("Preço: R$").append(im.preco).append("\n");
        sb.append("Status: ").append(im.status).append("\n");
        if (im.dataReserva != null) {
            sb.append("Reservado por: ").append(im.reservadoPorCliente).append("\n");
            sb.append("Corretor: ").append(im.reservadoPorCorretor).append("\n");
            sb.append("Data/Hora: ").append(im.dataReserva.format(Imovel.DATETIME_FMT)).append("\n");
        }
        if (im instanceof Apartamento) {
            Apartamento a = (Apartamento) im;
            sb.append("--- Apartamento ---\n");
            sb.append("Quartos: ").append(a.quartos).append("\n");
            sb.append("Área m²: ").append(a.area).append("\n");
            sb.append("Andar: ").append(a.andar).append("\n");
            sb.append("Sacada: ").append(a.sacada ? "Sim" : "Não").append("\n");
            sb.append("Condomínio: R$").append(a.valorCondominio).append("\n");
        } else if (im instanceof Casa) {
            Casa c = (Casa) im;
            sb.append("--- Casa ---\n");
            sb.append("Quartos: ").append(c.quartos).append("\n");
            sb.append("Banheiros: ").append(c.banheiros).append("\n");
            sb.append("Área m²: ").append(c.area).append("\n");
            sb.append("Garagem: ").append(c.garagem ? "Sim" : "Não").append("\n");
            sb.append("Quintal: ").append(c.quintal ? "Sim" : "Não").append("\n");
        }

        JTextArea ta = new JTextArea(sb.toString());
        ta.setEditable(false);
        ta.setCaretPosition(0);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, sp, "Detalhes Imóvel", JOptionPane.INFORMATION_MESSAGE);
    }

    private void salvarSePossivel() {
        if (listaImoveis == null) return;
        try {
            if (arquivoCsv == null) {
                arquivoCsv = Paths.get("imoveis.csv").toAbsolutePath();
            }
            ImovelRepository.salvarNoCsv(arquivoCsv, listaImoveis);
            labelArquivo.setText("Arquivo: " + arquivoCsv.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Falha ao salvar arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void tentarSalvarAutomatico() {
        // tenta salvar ao fechar se possível
        try {
            if (listaImoveis != null && !listaImoveis.isEmpty()) {
                salvarSePossivel();
            }
        } catch (Exception ignored) {}
    }

    /* ---------- TableModel personalizado que mostra a lista de imóveis ---------- */
    private class ImovelTableModel extends AbstractTableModel {
        private final String[] colunas = {"ID", "Tipo", "Título", "Endereço", "Preço", "Status", "Reserva"};
        private List<Imovel> dados;

        public ImovelTableModel() { this.dados = java.util.Collections.emptyList(); }

        public void setImoveis(List<Imovel> imoveis) {
            this.dados = imoveis;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() { return dados == null ? 0 : dados.size(); }

        @Override
        public int getColumnCount() { return colunas.length; }

        @Override
        public String getColumnName(int column) { return colunas[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Imovel im = dados.get(rowIndex);
            switch (columnIndex) {
                case 0: return im.id;
                case 1: return im.getClass().getSimpleName();
                case 2: return im.titulo;
                case 3: return im.endereco;
                case 4: return im.preco;
                case 5: return im.status;
                case 6:
                    if ("reservado".equalsIgnoreCase(im.status) && im.dataReserva != null) {
                        return im.reservadoPorCliente + " / " + im.dataReserva.format(Imovel.DATETIME_FMT);
                    } else {
                        return "";
                    }
                default: return "";
            }
        }
    }

    /* ---------- Main ---------- */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImobiliariaUI ui = new ImobiliariaUI();
            // tenta carregar CSV passado como argumento (opcional)
            if (args.length > 0) {
                Path p = Paths.get(args[0]).normalize().toAbsolutePath();
                ui.carregarListaDoArquivo(p);
            } else {
                // tenta carregar o CSV padrão no working dir ou o caminho que você usou antes
                try {
                    // substitua esse path pelo seu CSV se desejar carregar específico
                    Path p = Paths.get("C:\\PJBL POO\\PJBL\\src\\PJBL\\PJBL\\PJBL\\Doc\\base_de_dados_imobiliarias(in).csv").toAbsolutePath();
                    ui.carregarListaDoArquivo(p);
                } catch (Exception e) {
                    // se falhar, carrega/ cria imoveis.csv no working dir via ImovelRepository
                    ui.carregarListaDoArquivo(Paths.get("imoveis.csv").toAbsolutePath());
                }
            }
            ui.setVisible(true);
        });
    }
}
