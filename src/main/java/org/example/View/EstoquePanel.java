package org.example.View;

import org.example.Model.Entity.CategoriaProdutoMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Repository.CategoriaProdutoRepository;
import org.example.Model.Repository.ProdutosRepository;
import org.example.Model.Service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

public class EstoquePanel extends JPanel {

    // Services e Repositories
    private final ProdutoService produtoService;
    private final CategoriaProdutoRepository categoriaRepository;
    private final ProdutosRepository produtosRepository;

    // Produto em edição
    private ProdutosMODEL produtoEmEdicao;

    // Componentes do formulário
    private JTextField codProdField;
    private JTextField nomeField;
    private JTextField valorField;
    private JSpinner estoqueSpinner;
    private JComboBox<String> curvaAbcCombo;
    private JComboBox<CategoriaProdutoMODEL> categoriaCombo;

    // Botões
    private JButton novoButton;
    private JButton salvarButton;
    private JButton excluirButton;
    private JButton voltarButton;

    // Componentes da tabela
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JTextField filtroField;

    // Status
    private JLabel statusLabel;

    // Listener para voltar ao menu principal
    private ActionListener backListener;

    public EstoquePanel(ActionListener backListener) {
        this.backListener = backListener;
        this.produtoService = new ProdutoService();
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
        carregarCategorias();
        carregarProdutosNaTabela();
    }

    private JPanel criarFormularioPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int gridY = 0;

        // Código do Produto
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        codProdField = new JTextField(15);
        panel.add(codProdField, gbc);

        // Nome do Produto
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        nomeField = new JTextField(15);
        panel.add(nomeField, gbc);

        // Valor
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        valorField = new JTextField(15);
        panel.add(valorField, gbc);

        // Estoque
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 9999, 1);
        estoqueSpinner = new JSpinner(spinnerModel);
        panel.add(estoqueSpinner, gbc);

        // Curva ABC
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Curva ABC:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        curvaAbcCombo = new JComboBox<>(new String[]{"A", "B", "C"});
        panel.add(curvaAbcCombo, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        categoriaCombo = new JComboBox<>();
        panel.add(categoriaCombo, gbc);

        return panel;
    }

    private JPanel criarTabelaPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));

        // Painel de filtro
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar:"));
        filtroField = new JTextField(20);
        filterPanel.add(filtroField);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Tabela de produtos
        String[] colunas = {"ID", "Código", "Nome", "Valor", "Estoque", "Curva ABC", "Categoria"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável diretamente
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Double.class; // Valor
                if (columnIndex == 4) return Integer.class; // Estoque
                return String.class;
            }
        };

        produtosTable = new JTable(tableModel);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.setAutoCreateRowSorter(true);

        // Oculta a coluna ID (usada apenas para referência)
        produtosTable.getColumnModel().getColumn(0).setMinWidth(0);
        produtosTable.getColumnModel().getColumn(0).setMaxWidth(0);
        produtosTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(produtosTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Listener para duplo clique na tabela
        produtosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarProdutoSelecionado();
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
        salvarButton.addActionListener(e -> salvarProduto());
        excluirButton.addActionListener(e -> excluirProduto());
        voltarButton.addActionListener(backListener);

        return panel;
    }

    private void carregarCategorias() {
        categoriaCombo.removeAllItems();
        try {
            List<CategoriaProdutoMODEL> categorias = categoriaRepository.listarTodos();
            if (categorias.isEmpty()) {
                setStatusMessage("Nenhuma categoria encontrada. Cadastre categorias primeiro.", Color.RED);
                salvarButton.setEnabled(false);
            } else {
                for (CategoriaProdutoMODEL categoria : categorias) {
                    categoriaCombo.addItem(categoria);
                }
                salvarButton.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao carregar categorias: " + e.getMessage(), Color.RED);
        }
    }

    private void carregarProdutosNaTabela() {
        tableModel.setRowCount(0); // Limpa a tabela

        try {
            List<ProdutosMODEL> produtos = produtoService.listarTodos();
            if (produtos.isEmpty()) {
                setStatusMessage("Nenhum produto cadastrado.", Color.ORANGE);
            } else {
                for (ProdutosMODEL p : produtos) {
                    tableModel.addRow(new Object[]{
                            p.getId(),
                            p.getCodProd(),
                            p.getNome(),
                            p.getValor(),
                            p.getEstoque(),
                            p.getCurvaAbc() != null ? p.getCurvaAbc() : "N/A",
                            p.getCategoria() != null ? p.getCategoria().getNome() : "N/A"
                    });
                }
                setStatusMessage("Produtos carregados com sucesso.", Color.GREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao carregar produtos: " + e.getMessage(), Color.RED);
        }
    }

    private void filtrarTabela() {
        String texto = filtroField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) produtosTable.getRowSorter();

        if (sorter == null) {
            sorter = new TableRowSorter<>(tableModel);
            produtosTable.setRowSorter(sorter);
        }

        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Filtra por código (coluna 1) ou nome (coluna 2)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1, 2));
        }
    }

    private void carregarProdutoSelecionado() {
        int selectedRow = produtosTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Converte o índice da linha selecionada para o modelo (importante quando há filtro)
            int modelRow = produtosTable.convertRowIndexToModel(selectedRow);

            // Obtém o ID do produto selecionado
            Long id = Long.parseLong(tableModel.getValueAt(modelRow, 0).toString());

            // Busca o produto completo no banco
            Optional<ProdutosMODEL> produtoOpt = produtoService.buscarPorId(id);

            if (produtoOpt.isPresent()) {
                produtoEmEdicao = produtoOpt.get();

                // Preenche o formulário com os dados do produto
                codProdField.setText(produtoEmEdicao.getCodProd());
                nomeField.setText(produtoEmEdicao.getNome());
                valorField.setText(String.valueOf(produtoEmEdicao.getValor()));
                estoqueSpinner.setValue(produtoEmEdicao.getEstoque());

                // Seleciona a curva ABC
                String curvaAbc = produtoEmEdicao.getCurvaAbc();
                if (curvaAbc != null && !curvaAbc.isEmpty()) {
                    curvaAbcCombo.setSelectedItem(curvaAbc);
                } else {
                    curvaAbcCombo.setSelectedIndex(0);
                }

                // Seleciona a categoria
                CategoriaProdutoMODEL categoria = produtoEmEdicao.getCategoria();
                for (int i = 0; i < categoriaCombo.getItemCount(); i++) {
                    CategoriaProdutoMODEL cat = categoriaCombo.getItemAt(i);
                    if (cat.getId().equals(categoria.getId())) {
                        categoriaCombo.setSelectedIndex(i);
                        break;
                    }
                }

                excluirButton.setEnabled(true);
                setStatusMessage("Produto carregado para edição.", Color.BLUE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar produto. Produto não encontrado no banco de dados.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {
        produtoEmEdicao = null;
        codProdField.setText("");
        nomeField.setText("");
        valorField.setText("");
        estoqueSpinner.setValue(0);
        curvaAbcCombo.setSelectedIndex(0);
        if (categoriaCombo.getItemCount() > 0) {
            categoriaCombo.setSelectedIndex(0);
        }
        excluirButton.setEnabled(false);
        setStatusMessage("Formulário limpo. Pronto para novo cadastro.", Color.BLACK);
    }

    private void salvarProduto() {
        // Validação dos campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Verifica se é uma edição ou um novo produto
            if (produtoEmEdicao == null) {
                produtoEmEdicao = new ProdutosMODEL();
            }

            // Preenche o objeto com os dados do formulário
            produtoEmEdicao.setCodProd(codProdField.getText().trim());
            produtoEmEdicao.setNome(nomeField.getText().trim());
            produtoEmEdicao.setValor(Double.parseDouble(valorField.getText().replace(",", ".")));
            produtoEmEdicao.setEstoque((Integer) estoqueSpinner.getValue());
            produtoEmEdicao.setCurvaAbc(curvaAbcCombo.getSelectedItem().toString());
            produtoEmEdicao.setCategoria((CategoriaProdutoMODEL) categoriaCombo.getSelectedItem());

            // Salva o produto
            if (produtoEmEdicao.getId() == null) {
                produtosRepository.salvar(produtoEmEdicao);
                setStatusMessage("Produto cadastrado com sucesso!", Color.GREEN);
            } else {
                produtosRepository.atualizar(produtoEmEdicao);
                setStatusMessage("Produto atualizado com sucesso!", Color.GREEN);
            }

            // Atualiza a tabela e limpa o formulário
            carregarProdutosNaTabela();
            limparFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao salvar produto: " + e.getMessage(), Color.RED);
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar produto: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Validação do código
        String codProd = codProdField.getText().trim();
        if (codProd.isEmpty()) {
            setStatusMessage("O código do produto é obrigatório.", Color.RED);
            codProdField.requestFocus();
            return false;
        }

        // Verifica se o código já existe (apenas para novos produtos)
        if (produtoEmEdicao == null || produtoEmEdicao.getId() == null) {
            Optional<ProdutosMODEL> existente = produtoService.buscarPorCodProd(codProd);
            if (existente.isPresent()) {
                setStatusMessage("Já existe um produto com este código.", Color.RED);
                codProdField.requestFocus();
                return false;
            }
        }

        // Validação do nome
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            setStatusMessage("O nome do produto é obrigatório.", Color.RED);
            nomeField.requestFocus();
            return false;
        }

        // Validação do valor
        String valorStr = valorField.getText().trim().replace(",", ".");
        if (valorStr.isEmpty()) {
            setStatusMessage("O valor do produto é obrigatório.", Color.RED);
            valorField.requestFocus();
            return false;
        }

        try {
            double valor = Double.parseDouble(valorStr);
            if (valor < 0) {
                setStatusMessage("O valor não pode ser negativo.", Color.RED);
                valorField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            setStatusMessage("Valor inválido. Use apenas números e ponto/vírgula decimal.", Color.RED);
            valorField.requestFocus();
            return false;
        }

        // Validação da categoria
        if (categoriaCombo.getSelectedItem() == null) {
            setStatusMessage("Selecione uma categoria.", Color.RED);
            categoriaCombo.requestFocus();
            return false;
        }

        return true;
    }

    private void excluirProduto() {
        if (produtoEmEdicao == null || produtoEmEdicao.getId() == null) {
            setStatusMessage("Nenhum produto selecionado para exclusão.", Color.RED);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto '" + produtoEmEdicao.getNome() + "'?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                produtosRepository.deletar(produtoEmEdicao.getId());
                setStatusMessage("Produto excluído com sucesso!", Color.GREEN);
                carregarProdutosNaTabela();
                limparFormulario();
            } catch (Exception e) {
                e.printStackTrace();
                setStatusMessage("Erro ao excluir produto: " + e.getMessage(), Color.RED);
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir produto: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    // Método para atualizar o painel quando exibido
    public void refresh() {
        carregarCategorias();
        carregarProdutosNaTabela();
        limparFormulario();
    }
}
