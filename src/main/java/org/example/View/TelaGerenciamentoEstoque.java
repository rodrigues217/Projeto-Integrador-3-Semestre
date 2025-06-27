package org.example.View;

import org.example.Controller.ProdutoController;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Service.ProdutoService.ProdutoComValorTotal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoEstoque extends JPanel {

    private JTable tabelaEstoque;
    private JTextField campoQuantidade;
    private JButton botaoAdicionar, botaoRemover, botaoAtualizarABC, botaoNovoProduto, botaoSalvarProduto;

    private JTextField campoNomeProduto, campoValorProduto, campoEstoqueInicial, campoCodigoProduto;
    private JComboBox<CategoriaProdutoMODEL> comboCategoria;

    private JPanel painelCadastroProduto;
    private final ProdutoController produtoController;

    public TelaGerenciamentoEstoque() {
        this.produtoController = new ProdutoController();
        setLayout(new BorderLayout(10, 10));

        // === TABELA ===
        tabelaEstoque = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos em Estoque"));
        add(scrollPane, BorderLayout.CENTER);

        // === PAINEL INFERIOR COM 2 LINHAS ===
        JPanel painelInferior = new JPanel(new GridLayout(2, 1));

        // === PAINEL DE AÇÕES DE ESTOQUE ===
        JPanel painelEstoque = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        campoQuantidade = new JTextField(5);
        botaoAdicionar = new JButton("Adicionar Estoque");
        botaoRemover = new JButton("Remover Estoque");
        botaoAtualizarABC = new JButton("Atualizar Curva ABC");

        painelEstoque.add(new JLabel("Qtd:"));
        painelEstoque.add(campoQuantidade);
        painelEstoque.add(botaoAdicionar);
        painelEstoque.add(botaoRemover);
        painelEstoque.add(botaoAtualizarABC);

        // === PAINEL DE AÇÕES DE PRODUTO ===
        JPanel painelProduto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        botaoNovoProduto = new JButton("Novo Produto");
        painelProduto.add(botaoNovoProduto);

        // Adiciona os dois painéis
        painelInferior.add(painelEstoque);
        painelInferior.add(painelProduto);

        add(painelInferior, BorderLayout.SOUTH);

        // === PAINEL DE CADASTRO (inicialmente escondido) ===
        painelCadastroProduto = new JPanel(new GridLayout(6, 2, 5, 5));
        painelCadastroProduto.setBorder(BorderFactory.createTitledBorder("Cadastro de Novo Produto"));
        painelCadastroProduto.setVisible(false);

        campoNomeProduto = new JTextField(15);
        campoValorProduto = new JTextField(10);
        campoEstoqueInicial = new JTextField(5);
        campoCodigoProduto = new JTextField(10);
        comboCategoria = new JComboBox<>();
        botaoSalvarProduto = new JButton("Salvar");

        painelCadastroProduto.add(new JLabel("Nome:"));
        painelCadastroProduto.add(campoNomeProduto);
        painelCadastroProduto.add(new JLabel("Valor:"));
        painelCadastroProduto.add(campoValorProduto);
        painelCadastroProduto.add(new JLabel("Estoque Inicial:"));
        painelCadastroProduto.add(campoEstoqueInicial);
        painelCadastroProduto.add(new JLabel("Código:"));
        painelCadastroProduto.add(campoCodigoProduto);
        painelCadastroProduto.add(new JLabel("Categoria:"));
        painelCadastroProduto.add(comboCategoria);
        painelCadastroProduto.add(new JLabel());
        painelCadastroProduto.add(botaoSalvarProduto);

        add(painelCadastroProduto, BorderLayout.NORTH);

        // === INICIALIZAÇÕES ===
        carregarCategorias();
        carregarProdutos();

        // === EVENTOS ===
        botaoAdicionar.addActionListener(e -> atualizarEstoque(true));
        botaoRemover.addActionListener(e -> atualizarEstoque(false));
        botaoAtualizarABC.addActionListener(e -> carregarProdutosPorCurvaABC());
        botaoNovoProduto.addActionListener(e -> toggleFormulario());
        botaoSalvarProduto.addActionListener(e -> salvarProduto());
    }

    private void toggleFormulario() {
        painelCadastroProduto.setVisible(!painelCadastroProduto.isVisible());
        revalidate();
        repaint();
    }

    private void carregarProdutos() {
        List<ProdutosMODEL> produtos = produtoController.listarProdutos();
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Estoque", "Valor", "Categoria"}, 0);
        for (ProdutosMODEL p : produtos) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getEstoque(), p.getValor(), p.getCategoria().getNome()
            });
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
            campoQuantidade.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + e.getMessage());
        }
    }

    private void carregarCategorias() {
        CategoriaProdutoRepository repo = new CategoriaProdutoRepository();
        List<CategoriaProdutoMODEL> categorias = repo.listarTodos();
        comboCategoria.removeAllItems();
        for (CategoriaProdutoMODEL categoria : categorias) {
            comboCategoria.addItem(categoria);
        }
    }

    private void salvarProduto() {
        try {
            String nome = campoNomeProduto.getText().trim();
            String valorStr = campoValorProduto.getText().trim().replace(",", ".");
            String estoqueStr = campoEstoqueInicial.getText().trim();
            String codigo = campoCodigoProduto.getText().trim();
            CategoriaProdutoMODEL categoria = (CategoriaProdutoMODEL) comboCategoria.getSelectedItem();

            if (nome.isEmpty() || valorStr.isEmpty() || estoqueStr.isEmpty() || codigo.isEmpty() || categoria == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double valor = Double.parseDouble(valorStr);
            int estoque = Integer.parseInt(estoqueStr);

            produtoController.criarProduto(nome, valor, estoque, codigo, categoria.getId());

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            campoNomeProduto.setText("");
            campoValorProduto.setText("");
            campoEstoqueInicial.setText("");
            campoCodigoProduto.setText("");
            comboCategoria.setSelectedIndex(0);

            carregarProdutos();
            painelCadastroProduto.setVisible(false);
            revalidate();
            repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor ou estoque inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
