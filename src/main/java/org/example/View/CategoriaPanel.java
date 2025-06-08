package org.example.View;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoriaPanel extends JPanel {

    // Repository
    private final CategoriaProdutoRepository categoriaRepository;
    private final ProdutosRepository produtosRepository;

    // Categoria em edição
    private CategoriaProdutoMODEL categoriaEmEdicao;

    // Componentes do formulário
    private JTextField idField;
    private JTextField nomeField;

    // Botões
    private JButton novoButton;
    private JButton salvarButton;
    private JButton excluirButton;
    private JButton voltarButton;

    // Componentes da tabela
    private JTable categoriasTable;
    private DefaultTableModel tableModel;
    private JTextField filtroField;

    // Status
    private JLabel statusLabel;

    // Listener para voltar ao menu principal
    private ActionListener backListener;

    public CategoriaPanel(ActionListener backListener) {
        this.backListener = backListener;
        this.categoriaRepository = new CategoriaProdutoRepository();
        this.produtosRepository = new ProdutosRepository();

        // Configuração do layout principal
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Criação dos painéis
        JPanel formPanel = criarFormularioPanel();
        JPanel tablePanel = criarTabelaPanel();

        // Adiciona os painéis ao SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setResizeWeight(0.4); // 40% para o formulário, 60% para a tabela
        add(splitPane, BorderLayout.CENTER);

        // Painel de botões na parte inferior
        JPanel buttonPanel = criarBotoesPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Status label no topo
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLACK);
        add(statusLabel, BorderLayout.NORTH);

        // Inicializa o estado do formulário
        limparFormulario();
        carregarCategoriasNaTabela();
    }

    private JPanel criarFormularioPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados da Categoria"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int gridY = 0;

        // ID (não editável, apenas para referência)
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        idField = new JTextField(15);
        idField.setEditable(false);
        idField.setEnabled(false);
        panel.add(idField, gbc);

        // Nome da Categoria
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        nomeField = new JTextField(15);
        panel.add(nomeField, gbc);

        return panel;
    }

    private JPanel criarTabelaPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));

        // Painel de filtro
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar por Nome:"));
        filtroField = new JTextField(20);
        filterPanel.add(filtroField);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Tabela de categorias
        String[] colunas = {"ID", "Nome", "Qtd. Produtos"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável diretamente
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class; // Qtd. Produtos
                return String.class;
            }
        };

        categoriasTable = new JTable(tableModel);
        categoriasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriasTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(categoriasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Listener para duplo clique na tabela
        categoriasTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarCategoriaSelecionada();
                }
            }
        });

        // Listener para filtro
        filtroField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrarTabela(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrarTabela(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrarTabela(); }
        });

        return panel;
    }

    private JPanel criarBotoesPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        novoButton = new JButton("Novo");
        salvarButton = new JButton("Salvar");
        excluirButton = new JButton("Excluir");
        voltarButton = new JButton("Voltar ao Menu");

        panel.add(novoButton);
        panel.add(salvarButton);
        panel.add(excluirButton);
        panel.add(voltarButton);

        // Action Listeners
        novoButton.addActionListener(e -> limparFormulario());
        salvarButton.addActionListener(e -> salvarCategoria());
        excluirButton.addActionListener(e -> excluirCategoria());
        voltarButton.addActionListener(backListener);

        return panel;
    }

    private void carregarCategoriasNaTabela() {
        tableModel.setRowCount(0); // Limpa a tabela

        try {
            List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();
            if (categorias.isEmpty()) {
                setStatusMessage("Nenhuma categoria cadastrada.", Color.ORANGE);
            } else {
                for (CategoriaProdutoMODEL c : categorias) {
                    // Usa o método específico para contar produtos por categoria
                    int qtdProdutos = categoriaRepository.contarProdutosPorCategoria(c.getId());

                    tableModel.addRow(new Object[]{
                            c.getId(),
                            c.getNome(),
                            qtdProdutos
                    });
                }
                setStatusMessage("Categorias carregadas com sucesso: " + categorias.size() + " encontradas.", Color.GREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao carregar categorias: " + e.getMessage(), Color.RED);
        }
    }

    private void filtrarTabela() {
        String texto = filtroField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) categoriasTable.getRowSorter();

        if (sorter == null) {
            sorter = new TableRowSorter<>(tableModel);
            categoriasTable.setRowSorter(sorter);
        }

        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Filtra por nome (coluna 1)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
        }
    }

    private void carregarCategoriaSelecionada() {
        int selectedRow = categoriasTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Converte o índice da linha selecionada para o modelo (importante quando há filtro)
            int modelRow = categoriasTable.convertRowIndexToModel(selectedRow);

            // Obtém o ID da categoria selecionada
            Long id = Long.parseLong(tableModel.getValueAt(modelRow, 0).toString());

            // Busca a categoria completa no banco
            CategoriaProdutoMODEL categoria = categoriaRepository.buscarPorId(id);

            if (categoria != null) {
                categoriaEmEdicao = categoria;

                // Preenche o formulário com os dados da categoria
                idField.setText(categoria.getId().toString());
                nomeField.setText(categoria.getNome());

                excluirButton.setEnabled(true);
                setStatusMessage("Categoria carregada para edição.", Color.BLUE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar categoria. Categoria não encontrada no banco de dados.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {
        categoriaEmEdicao = null;
        idField.setText("");
        nomeField.setText("");
        excluirButton.setEnabled(false);
        setStatusMessage("Formulário limpo. Pronto para novo cadastro.", Color.BLACK);
    }

    private void salvarCategoria() {
        // Validação dos campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Verifica se é uma edição ou uma nova categoria
            if (categoriaEmEdicao == null) {
                categoriaEmEdicao = new CategoriaProdutoMODEL();
            }

            // Preenche o objeto com os dados do formulário
            categoriaEmEdicao.setNome(nomeField.getText().trim());

            // Salva a categoria
            if (categoriaEmEdicao.getId() == null) {
                categoriaRepository.salvar(categoriaEmEdicao);
                setStatusMessage("Categoria cadastrada com sucesso!", Color.GREEN);
            } else {
                categoriaRepository.atualizar(categoriaEmEdicao);
                setStatusMessage("Categoria atualizada com sucesso!", Color.GREEN);
            }

            // Atualiza a tabela e limpa o formulário
            carregarCategoriasNaTabela();
            limparFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao salvar categoria: " + e.getMessage(), Color.RED);
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar categoria: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Validação do nome
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            setStatusMessage("O nome da categoria é obrigatório.", Color.RED);
            nomeField.requestFocus();
            return false;
        }

        // Verificar se já existe uma categoria com o mesmo nome (exceto a própria categoria em edição)
        List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();
        for (CategoriaProdutoMODEL c : categorias) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                // Se for uma nova categoria ou uma categoria diferente da que está sendo editada
                if (categoriaEmEdicao == null || !c.getId().equals(categoriaEmEdicao.getId())) {
                    setStatusMessage("Já existe uma categoria com este nome.", Color.RED);
                    nomeField.requestFocus();
                    return false;
                }
            }
        }

        return true;
    }

    private void excluirCategoria() {
        if (categoriaEmEdicao == null || categoriaEmEdicao.getId() == null) {
            setStatusMessage("Nenhuma categoria selecionada para exclusão.", Color.RED);
            return;
        }

        // Verifica se existem produtos associados a esta categoria
        int qtdProdutos = categoriaRepository.contarProdutosPorCategoria(categoriaEmEdicao.getId());
        if (qtdProdutos > 0) {
            JOptionPane.showMessageDialog(this,
                    "Não é possível excluir esta categoria pois existem " + qtdProdutos + " produtos associados a ela.\n" +
                            "Remova ou altere a categoria dos produtos associados antes de excluir.",
                    "Exclusão não permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmação de exclusão
        int choice = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a categoria '" + categoriaEmEdicao.getNome() + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                categoriaRepository.deletar(categoriaEmEdicao.getId());
                setStatusMessage("Categoria excluída com sucesso!", Color.GREEN);

                // Atualiza a tabela e limpa o formulário
                carregarCategoriasNaTabela();
                limparFormulario();

            } catch (Exception e) {
                e.printStackTrace();
                setStatusMessage("Erro ao excluir categoria: " + e.getMessage(), Color.RED);
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir categoria: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public void refresh() {
        carregarCategoriasNaTabela();
        limparFormulario();
    }
}
