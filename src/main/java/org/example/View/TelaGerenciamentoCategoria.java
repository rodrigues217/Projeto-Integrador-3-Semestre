package org.example.View;

import org.example.Controller.CategoriaProdutoController;
import org.example.Controller.ProdutoController;
import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoCategoria extends JPanel {

    private final CategoriaProdutoController categoriaController = new CategoriaProdutoController();
    private final ProdutoController produtoController = new ProdutoController();

    private final DefaultListModel<CategoriaProdutoMODEL> categoriaListModel = new DefaultListModel<>();
    private final DefaultListModel<ProdutosMODEL> produtoListModel = new DefaultListModel<>();

    private final JList<CategoriaProdutoMODEL> listaCategorias = new JList<>(categoriaListModel);
    private final JList<ProdutosMODEL> listaProdutos = new JList<>(produtoListModel);

    private final JTextField campoNomeCategoria = new JTextField(20);

    public TelaGerenciamentoCategoria() {
        setLayout(new BorderLayout());

        // Painel da esquerda (categorias)
        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Categorias"));

        JScrollPane scrollCategorias = new JScrollPane(listaCategorias);
        painelEsquerdo.add(scrollCategorias, BorderLayout.CENTER);

        // Painel de botões e campo nome
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new GridLayout(0, 1, 5, 5));
        painelFormulario.add(new JLabel("Nome da Categoria:"));
        painelFormulario.add(campoNomeCategoria);

        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoExcluir = new JButton("Excluir");

        painelFormulario.add(botaoAdicionar);
        painelFormulario.add(botaoEditar);
        painelFormulario.add(botaoExcluir);

        painelEsquerdo.add(painelFormulario, BorderLayout.SOUTH);

        // Painel da direita (produtos)
        JPanel painelDireito = new JPanel(new BorderLayout());
        painelDireito.setBorder(BorderFactory.createTitledBorder("Produtos da Categoria Selecionada"));

        JScrollPane scrollProdutos = new JScrollPane(listaProdutos);
        painelDireito.add(scrollProdutos, BorderLayout.CENTER);

        // Dividir a tela entre esquerda e direita
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        // Ações dos botões
        botaoAdicionar.addActionListener(e -> adicionarCategoria());
        botaoEditar.addActionListener(e -> editarCategoria());
        botaoExcluir.addActionListener(e -> excluirCategoria());

        // Seleção de categoria
        listaCategorias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarProdutosDaCategoriaSelecionada();
                campoNomeCategoria.setText(obterCategoriaSelecionadaNome());
            }
        });

        carregarCategorias();
    }

    private void carregarCategorias() {
        categoriaListModel.clear();
        List<CategoriaProdutoMODEL> categorias = categoriaController.listarCategorias();
        categorias.forEach(categoriaListModel::addElement);
    }

    private void carregarProdutosDaCategoriaSelecionada() {
        produtoListModel.clear();
        CategoriaProdutoMODEL categoria = listaCategorias.getSelectedValue();
        if (categoria != null) {
            List<ProdutosMODEL> produtos = produtoController.listarProdutosPorCategoria(categoria.getId());
            produtos.forEach(produtoListModel::addElement);
        }
    }

    private String obterCategoriaSelecionadaNome() {
        CategoriaProdutoMODEL categoria = listaCategorias.getSelectedValue();
        return categoria != null ? categoria.getNome() : "";
    }

    private void adicionarCategoria() {
        try {
            String nome = campoNomeCategoria.getText();
            categoriaController.criarCategoria(nome);
            carregarCategorias();
            campoNomeCategoria.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCategoria() {
        try {
            CategoriaProdutoMODEL categoria = listaCategorias.getSelectedValue();
            if (categoria == null) throw new Exception("Selecione uma categoria.");
            String novoNome = campoNomeCategoria.getText();
            categoriaController.atualizarNomeCategoria(categoria.getId(), novoNome);
            carregarCategorias();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCategoria() {
        try {
            CategoriaProdutoMODEL categoria = listaCategorias.getSelectedValue();
            if (categoria == null) throw new Exception("Selecione uma categoria.");
            categoriaController.removerCategoria(categoria.getId());
            carregarCategorias();
            produtoListModel.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
