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
    private final DefaultComboBoxModel<CategoriaProdutoMODEL> categoriaComboBoxModel = new DefaultComboBoxModel<>();

    private final JList<CategoriaProdutoMODEL> listaCategorias = new JList<>(categoriaListModel);
    private final JList<ProdutosMODEL> listaProdutos = new JList<>(produtoListModel);
    private final JComboBox<CategoriaProdutoMODEL> comboCategorias = new JComboBox<>(categoriaComboBoxModel);

    private final JTextField campoNomeCategoria = new JTextField(20);

    public TelaGerenciamentoCategoria() {
        setLayout(new BorderLayout());


        JPanel painelEsquerdo = new JPanel(new BorderLayout());
        painelEsquerdo.setBorder(BorderFactory.createTitledBorder("Categorias"));

        JScrollPane scrollCategorias = new JScrollPane(listaCategorias);
        painelEsquerdo.add(scrollCategorias, BorderLayout.CENTER);

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

        JPanel painelDireito = new JPanel();
        painelDireito.setLayout(new BoxLayout(painelDireito, BoxLayout.Y_AXIS));
        painelDireito.setBorder(BorderFactory.createTitledBorder("Produtos da Categoria Selecionada"));

        JScrollPane scrollProdutos = new JScrollPane(listaProdutos);
        scrollProdutos.setPreferredSize(new Dimension(400, 200));
        painelDireito.add(scrollProdutos);

        JPanel painelTroca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTroca.add(new JLabel("Nova Categoria:"));
        painelTroca.add(comboCategorias);

        JButton botaoTrocarCategoria = new JButton("Trocar Categoria do Produto");
        painelTroca.add(botaoTrocarCategoria);

        painelDireito.add(Box.createRigidArea(new Dimension(0, 10)));
        painelDireito.add(painelTroca);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        botaoAdicionar.addActionListener(e -> adicionarCategoria());
        botaoEditar.addActionListener(e -> editarCategoria());
        botaoExcluir.addActionListener(e -> excluirCategoria());
        botaoTrocarCategoria.addActionListener(e -> trocarCategoriaProduto());

        listaCategorias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarProdutosDaCategoriaSelecionada();
                campoNomeCategoria.setText(obterCategoriaSelecionadaNome());
            }
        });

        carregarCategorias();
        carregarCategoriasComboBox();
    }

    private void carregarCategorias() {
        categoriaListModel.clear();
        List<CategoriaProdutoMODEL> categorias = categoriaController.listarCategorias();
        categorias.forEach(categoriaListModel::addElement);
    }

    private void carregarCategoriasComboBox() {
        categoriaComboBoxModel.removeAllElements();
        List<CategoriaProdutoMODEL> categorias = categoriaController.listarCategorias();
        categorias.forEach(categoriaComboBoxModel::addElement);
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
            carregarCategoriasComboBox();
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
            carregarCategoriasComboBox();
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
            carregarCategoriasComboBox();
            produtoListModel.clear();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void trocarCategoriaProduto() {
        try {
            ProdutosMODEL produtoSelecionado = listaProdutos.getSelectedValue();
            CategoriaProdutoMODEL novaCategoria = (CategoriaProdutoMODEL) comboCategorias.getSelectedItem();

            if (produtoSelecionado == null) {
                throw new Exception("Selecione um produto para trocar a categoria.");
            }

            if (novaCategoria == null) {
                throw new Exception("Selecione a nova categoria.");
            }

            if (produtoSelecionado.getCategoria().getId().equals(novaCategoria.getId())) {
                throw new Exception("O produto já pertence a essa categoria.");
            }

            categoriaController.trocarCategoriaDeProduto(produtoSelecionado.getId(), novaCategoria.getId());
            carregarProdutosDaCategoriaSelecionada();

            JOptionPane.showMessageDialog(this, "Categoria do produto atualizada com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao trocar categoria", JOptionPane.ERROR_MESSAGE);
        }
    }
}
