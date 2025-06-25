package org.example.View;

import org.example.Controller.ProdutoController;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Service.ProdutoService.ProdutoComValorTotal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoEstoque extends JPanel {

    private JTable tabelaEstoque;
    private JTextField campoQuantidade;
    private JButton botaoAdicionar;
    private JButton botaoRemover;
    private JButton botaoAtualizarABC;
    private ProdutoController produtoController;

    public TelaGerenciamentoEstoque() {
        this.produtoController = new ProdutoController();
        setLayout(new BorderLayout(10, 10));

        // === Tabela de Produtos ===
        tabelaEstoque = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos em Estoque"));
        add(scrollPane, BorderLayout.CENTER);

        // === Painel de Ações ===
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        campoQuantidade = new JTextField(5);
        botaoAdicionar = new JButton("Adicionar Estoque");
        botaoRemover = new JButton("Remover Estoque");
        botaoAtualizarABC = new JButton("Atualizar Curva ABC");

        painelAcoes.add(new JLabel("Quantidade:"));
        painelAcoes.add(campoQuantidade);
        painelAcoes.add(botaoAdicionar);
        painelAcoes.add(botaoRemover);
        painelAcoes.add(botaoAtualizarABC);

        add(painelAcoes, BorderLayout.SOUTH);

        // === Eventos ===
        botaoAdicionar.addActionListener(e -> atualizarEstoque(true));
        botaoRemover.addActionListener(e -> atualizarEstoque(false));
        botaoAtualizarABC.addActionListener(e -> carregarProdutosPorCurvaABC());

        carregarProdutos(); // Carrega inicialmente
    }

    private void carregarProdutos() {
        List<ProdutosMODEL> produtos = produtoController.listarProdutos();
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Estoque", "Valor", "Categoria"}, 0);
        for (ProdutosMODEL p : produtos) {
            modelo.addRow(new Object[]{p.getId(), p.getNome(), p.getEstoque(), p.getValor(), p.getCategoria().getNome()});
        }
        tabelaEstoque.setModel(modelo);
    }

    private void carregarProdutosPorCurvaABC() {
        List<ProdutoComValorTotal> produtosABC = produtoController.listarProdutosPorCurvaABC();
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Estoque", "Valor Total", "Classe ABC"}, 0);
        for (ProdutoComValorTotal p : produtosABC) {
            ProdutosMODEL prod = p.getProduto();
            modelo.addRow(new Object[]{prod.getId(), prod.getNome(), prod.getEstoque(), p.getValorTotal(), p.getClasseABC()});
        }
        tabelaEstoque.setModel(modelo);
    }

    private void atualizarEstoque(boolean adicionar) {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        Long idProduto = Long.parseLong(tabelaEstoque.getValueAt(linhaSelecionada, 0).toString());
        int quantidade;

        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite uma quantidade válida.");
            return;
        }

        try {
            produtoController.atualizarEstoque(idProduto, quantidade, adicionar);
            JOptionPane.showMessageDialog(this, "Estoque atualizado com sucesso!");
            carregarProdutos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + e.getMessage());
        }
    }
}
