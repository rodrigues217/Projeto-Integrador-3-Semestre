package org.example.View;

import org.example.Controller.FuncionarioController;
import org.example.Controller.ProdutoController;
import org.example.Controller.VendaController;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaVenda extends JPanel {

    private JTable tabelaProdutos;
    private JComboBox<FuncionarioMODEL> comboFuncionario;
    private JTextField campoCpfComprador;
    private JTextField campoQuantidade;
    private JButton botaoVender;
    private VendaController vendaController;
    private ProdutoController produtoController;
    private FuncionarioController funcionarioController;

    public TelaVenda() {
        this.vendaController = new VendaController();
        this.produtoController = new ProdutoController();
        this.funcionarioController = new FuncionarioController();

        setLayout(new BorderLayout(10, 10));
        setPreferredSize(new Dimension(580, 580));

        // === Tabela de Produtos ===
        tabelaProdutos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setPreferredSize(new Dimension(580, 160));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));
        add(scrollPane, BorderLayout.NORTH);

        // === Painel de Formulário (mais acima e centralizado) ===
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Menor margem superior
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(520, 220));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados da Venda"));
        formWrapper.add(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        comboFuncionario = new JComboBox<>();
        carregarFuncionarios();
        comboFuncionario.setPreferredSize(new Dimension(250, 28));

        campoCpfComprador = new JTextField();
        campoCpfComprador.setPreferredSize(new Dimension(250, 28));
        campoQuantidade = new JTextField();
        campoQuantidade.setPreferredSize(new Dimension(250, 28));

        botaoVender = new JButton("Realizar Venda");
        botaoVender.setPreferredSize(new Dimension(180, 35));
        botaoVender.addActionListener(e -> realizarVenda());

        // Linha 1 - Funcionário
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Funcionário:"), gbc);
        gbc.gridx = 1;
        formPanel.add(comboFuncionario, gbc);

        // Linha 2 - CPF
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("CPF do Comprador (opcional):"), gbc);
        gbc.gridx = 1;
        formPanel.add(campoCpfComprador, gbc);

        // Linha 3 - Quantidade
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        formPanel.add(campoQuantidade, gbc);

        // Linha 4 - Botão
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(botaoVender, gbc);

        add(formWrapper, BorderLayout.CENTER);

        carregarProdutos();
    }

    private void carregarFuncionarios() {
        List<FuncionarioMODEL> funcionarios = funcionarioController.listarFuncionariosDisponiveis();
        for (FuncionarioMODEL f : funcionarios) {
            comboFuncionario.addItem(f);
        }
    }

    private void carregarProdutos() {
        List<ProdutosMODEL> produtos = produtoController.listarProdutos();
        String[] colunas = {"Código", "Nome", "Estoque", "Valor"};

        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Nenhuma célula será editável
            }
        };

        for (ProdutosMODEL p : produtos) {
            Object[] linha = {p.getCodProd(), p.getNome(), p.getEstoque(), p.getValor()};
            modelo.addRow(linha);
        }

        tabelaProdutos.setModel(modelo);
    }


    private void realizarVenda() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto da tabela.");
            return;
        }

        String codProduto = tabelaProdutos.getValueAt(linhaSelecionada, 0).toString();
        String cpfFuncionario = ((FuncionarioMODEL) comboFuncionario.getSelectedItem()).getCPF();
        String cpfComprador = campoCpfComprador.getText().trim();
        int quantidade;

        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            return;
        }

        try {
            vendaController.realizarVenda(codProduto, cpfFuncionario, quantidade, cpfComprador);
            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!");
            carregarProdutos(); // Atualiza estoque
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar venda: " + e.getMessage());
        }
    }
}
