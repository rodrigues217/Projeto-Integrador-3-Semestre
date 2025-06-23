package org.example.View;

import org.example.Controller.AuditoriaVendaController;
import org.example.Model.Entity.AuditoriaVendaMODEL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaGerenciarAuditoria extends JPanel {

    private final AuditoriaVendaController auditoriaController;

    private DefaultTableModel tableModel;
    private JTable auditoriaTable;

    // Campos de busca
    private JTextField buscaCompradorField;
    private JTextField buscaFuncionarioField;
    private JTextField buscaProdutoField;

    // Botões
    private JButton listarTodasButton;
    private JButton buscarCompradorButton;
    private JButton buscarFuncionarioButton;
    private JButton buscarProdutoButton;
    private JButton limparButton;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public TelaGerenciarAuditoria() {
        auditoriaController = new AuditoriaVendaController();

        setLayout(new BorderLayout());

        // Inicialização dos componentes
        buscaCompradorField = new JTextField();
        buscaFuncionarioField = new JTextField();
        buscaProdutoField = new JTextField();

        listarTodasButton = new JButton("Listar Todas as Auditorias");
        buscarCompradorButton = new JButton("Buscar por Comprador");
        buscarFuncionarioButton = new JButton("Buscar por Funcionário");
        buscarProdutoButton = new JButton("Buscar por Produto");
        limparButton = new JButton("Limpar Filtros");

        // Criar tabela
        String[] columnNames = {
                "ID", "Data da Venda", "Quantidade", "Produto", "Funcionário", "Comprador"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        auditoriaTable = new JTable(tableModel);
        auditoriaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        auditoriaTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // === Painel Superior - Filtros de Busca ===
        JPanel buscaPanel = createBuscaPanel();
        add(buscaPanel, BorderLayout.NORTH);

        // === Painel Central - Tabela ===
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // === Painel Inferior - Botões ===
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // === Configurar Ações ===
        configurarAcoes();

        // === Carregar dados iniciais ===
        carregarTodasAuditorias();
    }

    private JPanel createBuscaPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros de Busca"));
        panel.setPreferredSize(new Dimension(0, 120));

        // Busca por Comprador
        panel.add(new JLabel("CPF do Comprador:"));
        panel.add(buscaCompradorField);
        panel.add(buscarCompradorButton);

        // Busca por Funcionário
        panel.add(new JLabel("CPF do Funcionário:"));
        panel.add(buscaFuncionarioField);
        panel.add(buscarFuncionarioButton);

        // Busca por Produto
        panel.add(new JLabel("ID do Produto:"));
        panel.add(buscaProdutoField);
        panel.add(buscarProdutoButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(auditoriaTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Label de informações
        JLabel infoLabel = new JLabel("Total de auditorias: 0");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listarTodasButton.setPreferredSize(new Dimension(200, 30));
        limparButton.setPreferredSize(new Dimension(150, 30));

        panel.add(listarTodasButton);
        panel.add(limparButton);

        return panel;
    }

    private void configurarAcoes() {
        listarTodasButton.addActionListener(e -> carregarTodasAuditorias());
        buscarCompradorButton.addActionListener(e -> buscarPorComprador());
        buscarFuncionarioButton.addActionListener(e -> buscarPorFuncionario());
        buscarProdutoButton.addActionListener(e -> buscarPorProduto());
        limparButton.addActionListener(e -> limparFiltros());

        // Permitir busca com Enter nos campos
        buscaCompradorField.addActionListener(e -> buscarPorComprador());
        buscaFuncionarioField.addActionListener(e -> buscarPorFuncionario());
        buscaProdutoField.addActionListener(e -> buscarPorProduto());
    }

    private void carregarTodasAuditorias() {
        try {
            List<AuditoriaVendaMODEL> auditorias = auditoriaController.listarAuditorias();
            preencherTabela(auditorias);
            atualizarInfoLabel(auditorias.size());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar auditorias: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorComprador() {
        String cpf = buscaCompradorField.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Digite o CPF do comprador para buscar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<AuditoriaVendaMODEL> auditorias = auditoriaController.buscarPorComprador(cpf);
            preencherTabela(auditorias);
            atualizarInfoLabel(auditorias.size());

            JOptionPane.showMessageDialog(this,
                    "Encontradas " + auditorias.size() + " auditoria(s) para o comprador.",
                    "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Nenhum Resultado", JOptionPane.WARNING_MESSAGE);
            limparTabela();
        }
    }

    private void buscarPorFuncionario() {
        String cpf = buscaFuncionarioField.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Digite o CPF do funcionário para buscar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<AuditoriaVendaMODEL> auditorias = auditoriaController.buscarPorFuncionario(cpf);
            preencherTabela(auditorias);
            atualizarInfoLabel(auditorias.size());

            JOptionPane.showMessageDialog(this,
                    "Encontradas " + auditorias.size() + " auditoria(s) para o funcionário.",
                    "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Nenhum Resultado", JOptionPane.WARNING_MESSAGE);
            limparTabela();
        }
    }

    private void buscarPorProduto() {
        String idStr = buscaProdutoField.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Digite o ID do produto para buscar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long idProduto = Long.parseLong(idStr);
            List<AuditoriaVendaMODEL> auditorias = auditoriaController.buscarPorProduto(idProduto);
            preencherTabela(auditorias);
            atualizarInfoLabel(auditorias.size());

            JOptionPane.showMessageDialog(this,
                    "Encontradas " + auditorias.size() + " auditoria(s) para o produto.",
                    "Busca Concluída", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "ID do produto deve ser um número válido.",
                    "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Nenhum Resultado", JOptionPane.WARNING_MESSAGE);
            limparTabela();
        }
    }

    private void preencherTabela(List<AuditoriaVendaMODEL> auditorias) {
        tableModel.setRowCount(0);

        for (AuditoriaVendaMODEL auditoria : auditorias) {
            String dataFormatada = auditoria.getDataVenda() != null ?
                    auditoria.getDataVenda().format(dateFormatter) : "N/A";

            String nomeProduto = auditoria.getProduto() != null ?
                    auditoria.getProduto().getNome() : "N/A";

            String nomeFuncionario = auditoria.getFuncionario() != null ?
                    auditoria.getFuncionario().getNome() : "N/A";

            String nomeComprador = auditoria.getComprador() != null ?
                    auditoria.getComprador().getNome() : "N/A";

            tableModel.addRow(new Object[]{
                    auditoria.getId(),
                    dataFormatada,
                    auditoria.getQuantidade(),
                    nomeProduto,
                    nomeFuncionario,
                    nomeComprador
            });
        }
    }

    private void limparTabela() {
        tableModel.setRowCount(0);
        atualizarInfoLabel(0);
    }

    private void limparFiltros() {
        buscaCompradorField.setText("");
        buscaFuncionarioField.setText("");
        buscaProdutoField.setText("");
        carregarTodasAuditorias();
    }

    private void atualizarInfoLabel(int total) {
        // Encontrar o label de informações no painel da tabela
        Component[] components = ((JPanel) getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setText("Total de auditorias: " + total);
                break;
            }
        }
    }
}

