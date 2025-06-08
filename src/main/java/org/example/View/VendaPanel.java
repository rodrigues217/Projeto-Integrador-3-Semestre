package org.example.View;

import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.ProdutosMODEL;
import org.example.Model.Service.CompradorService;
import org.example.Model.Service.ProdutoService;
import org.example.Model.Service.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

public class VendaPanel extends JPanel {

    // Services
    private VendaService vendaService;
    private ProdutoService produtoService;
    private CompradorService compradorService;

    // Current seller
    private FuncionarioMODEL vendedorAtual;

    // Found entities
    private ProdutosMODEL produtoSelecionado;
    private CompradorMODEL compradorSelecionado;

    // Swing Components
    private JTextField codProdField;
    private JButton buscarProdutoButton;
    private JLabel produtoInfoLabel;
    private JLabel vendedorInfoLabel;
    private JTextField cpfCompradorField;
    private JButton buscarCompradorButton;
    private JLabel compradorInfoLabel;
    private JSpinner quantidadeSpinner;
    private JButton confirmarVendaButton;
    private JButton voltarButton;
    private JLabel statusLabel;

    // Product Table Components
    private JTable produtosTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JTextField filtroProdutoField; // Campo para filtrar a tabela

    // Listener to go back
    private ActionListener backListener;

    public VendaPanel(ActionListener backListener) {
        this.backListener = backListener;
        this.vendaService = new VendaService();
        this.produtoService = new ProdutoService();
        this.compradorService = new CompradorService();

        // --- Setup JPanel Layout ---
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Center Panel (Form) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4); // Slightly reduced insets
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int gridY = 0;

        // Status Label
        gbc.gridx = 0; gbc.gridy = gridY++; gbc.gridwidth = 3;
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(100, 20));
        formPanel.add(statusLabel, gbc);
        gbc.gridwidth = 1;

        // Vendedor Info
        gbc.gridx = 0; gbc.gridy = gridY;
        formPanel.add(new JLabel("Vendedor:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++; gbc.gridwidth = 2;
        vendedorInfoLabel = new JLabel("(Aguardando definição)");
        formPanel.add(vendedorInfoLabel, gbc);
        gbc.gridwidth = 1;

        // Produto
        gbc.gridx = 0; gbc.gridy = gridY;
        formPanel.add(new JLabel("Código Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY;
        codProdField = new JTextField(15);
        formPanel.add(codProdField, gbc);
        gbc.gridx = 2; gbc.gridy = gridY++;
        buscarProdutoButton = new JButton("Buscar");
        formPanel.add(buscarProdutoButton, gbc);

        gbc.gridx = 0; gbc.gridy = gridY++; gbc.gridwidth = 3;
        produtoInfoLabel = new JLabel("Produto: (Aguardando busca ou seleção na tabela)");
        produtoInfoLabel.setForeground(Color.BLUE);
        formPanel.add(produtoInfoLabel, gbc);
        gbc.gridwidth = 1;

        // Comprador
        gbc.gridx = 0; gbc.gridy = gridY;
        formPanel.add(new JLabel("CPF Comprador:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY;
        cpfCompradorField = new JTextField(15);
        formPanel.add(cpfCompradorField, gbc);
        gbc.gridx = 2; gbc.gridy = gridY++;
        buscarCompradorButton = new JButton("Buscar/Cadastrar");
        formPanel.add(buscarCompradorButton, gbc);

        gbc.gridx = 0; gbc.gridy = gridY++; gbc.gridwidth = 3;
        compradorInfoLabel = new JLabel("Comprador: (Opcional)");
        compradorInfoLabel.setForeground(Color.BLUE);
        formPanel.add(compradorInfoLabel, gbc);
        gbc.gridwidth = 1;

        // Quantidade
        gbc.gridx = 0; gbc.gridy = gridY;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++; gbc.gridwidth = 2;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 999, 1);
        quantidadeSpinner = new JSpinner(spinnerModel);
        quantidadeSpinner.setEnabled(false);
        formPanel.add(quantidadeSpinner, gbc);
        gbc.gridwidth = 1;

        // --- Product Table Panel (East) ---
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));

        // Filter Field
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar por Nome/Código:"));
        filtroProdutoField = new JTextField(20);
        filterPanel.add(filtroProdutoField);
        tablePanel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Código", "Nome", "Preço", "Estoque", "Categoria"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        produtosTable = new JTable(tableModel);
        produtosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        produtosTable.setAutoCreateRowSorter(true); // Enable sorting
        tableScrollPane = new JScrollPane(produtosTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 200)); // Adjust size as needed
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // --- Buttons Panel (South) ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmarVendaButton = new JButton("Confirmar Venda");
        confirmarVendaButton.setEnabled(false);
        voltarButton = new JButton("Voltar ao Menu");
        southPanel.add(confirmarVendaButton);
        southPanel.add(voltarButton);

        // --- Add Panels to Main Panel ---
        // Use a SplitPane to make table size adjustable
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setResizeWeight(0.4); // Give form panel slightly less weight initially
        add(splitPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        buscarProdutoButton.addActionListener(e -> handleBuscarProduto());
        buscarCompradorButton.addActionListener(e -> handleBuscarComprador());
        confirmarVendaButton.addActionListener(e -> handleConfirmarVenda());
        voltarButton.addActionListener(backListener);
        quantidadeSpinner.addChangeListener(e -> checkConfirmButtonEnablement());

        // Table Listener (Double-click)
        produtosTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = produtosTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get code from the model (column 0) considering sorting
                        String codProd = tableModel.getValueAt(produtosTable.convertRowIndexToModel(selectedRow), 0).toString();
                        codProdField.setText(codProd);
                        handleBuscarProduto(); // Trigger search with the selected code
                    }
                }
            }
        });

        // Filter Listener
        filtroProdutoField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
    }

    /**
     * Define o vendedor e carrega a lista de produtos na tabela.
     */
    public void setVendedor(FuncionarioMODEL vendedor) {
        if (vendedor == null || vendedor.getCPF() == null || vendedor.getCPF().isBlank()) {
            this.vendedorAtual = null;
            vendedorInfoLabel.setText("ERRO: Vendedor inválido!");
            vendedorInfoLabel.setForeground(Color.RED);
            disableForm();
            setStatusMessage("Vendedor inválido. Não é possível vender.", Color.RED);
        } else {
            this.vendedorAtual = vendedor;
            vendedorInfoLabel.setText(String.format("%s (CPF: %s)", vendedorAtual.getNome(), vendedorAtual.getCPF()));
            vendedorInfoLabel.setForeground(Color.BLACK);
            enableForm();
            resetForm();
            carregarProdutosNaTabela(); // Load products when seller is set
        }
    }

    private void carregarProdutosNaTabela() {
        setStatusMessage("Carregando produtos...", Color.BLACK);
        tableModel.setRowCount(0); // Clear existing rows
        try {
            List<ProdutosMODEL> produtos = produtoService.listarTodos();
            if (produtos.isEmpty()) {
                setStatusMessage("Nenhum produto cadastrado.", Color.ORANGE);
            } else {
                for (ProdutosMODEL p : produtos) {
                    tableModel.addRow(new Object[]{
                            p.getCodProd(),
                            p.getNome(),
                            String.format("%.2f", p.getValor()), // Format price
                            p.getEstoque(),
                            p.getCategoria() != null ? p.getCategoria().getNome() : "N/A"
                    });
                }
                setStatusMessage("Produtos carregados.", Color.GREEN);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar produtos na tabela: " + e.getMessage());
            e.printStackTrace();
            setStatusMessage("Erro ao carregar produtos.", Color.RED);
            JOptionPane.showMessageDialog(this, "Erro ao buscar lista de produtos.", "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        String text = filtroProdutoField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) produtosTable.getRowSorter();
        if (sorter == null) {
            sorter = new TableRowSorter<>(tableModel);
            produtosTable.setRowSorter(sorter);
        }
        if (text.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter by code (column 0) or name (column 1)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1));
        }
    }

    // --- Action Handlers (Minor adjustments) ---

    private void handleBuscarProduto() {
        String codProd = codProdField.getText().trim();
        if (codProd.isEmpty()) {
            setStatusMessage("Digite o código do produto ou selecione na tabela.", Color.RED);
            return;
        }
        setStatusMessage("Buscando produto...", Color.BLACK);

        Optional<ProdutosMODEL> produtoOpt = produtoService.buscarPorCodProd(codProd);

        if (produtoOpt.isPresent()) {
            produtoSelecionado = produtoOpt.get();
            produtoInfoLabel.setText(String.format("Produto: %s | Preço: R$%.2f | Estoque: %d",
                    produtoSelecionado.getNome(), produtoSelecionado.getValor(), produtoSelecionado.getEstoque()));
            int currentStock = produtoSelecionado.getEstoque();
            ((SpinnerNumberModel) quantidadeSpinner.getModel()).setMaximum(Math.max(0, currentStock));
            if (currentStock <= 0) {
                quantidadeSpinner.setEnabled(false);
                setStatusMessage("Produto selecionado, mas sem estoque.", Color.ORANGE);
            } else {
                quantidadeSpinner.setEnabled(true);
                int currentValue = (int) quantidadeSpinner.getValue();
                if (currentValue > currentStock || currentValue <= 0) {
                    quantidadeSpinner.setValue(1);
                }
                setStatusMessage("Produto selecionado.", Color.GREEN);
            }
        } else {
            produtoSelecionado = null;
            produtoInfoLabel.setText("Produto: (Não encontrado)");
            quantidadeSpinner.setValue(1);
            quantidadeSpinner.setEnabled(false);
            ((SpinnerNumberModel) quantidadeSpinner.getModel()).setMaximum(999);
            setStatusMessage("Produto não encontrado.", Color.RED);
        }
        checkConfirmButtonEnablement();
    }

    private void handleBuscarComprador() {
        String cpf = cpfCompradorField.getText().trim();
        if (cpf.isEmpty()) {
            compradorSelecionado = null;
            compradorInfoLabel.setText("Comprador: (Nenhum informado)");
            setStatusMessage("CPF do comprador removido.", Color.BLACK);
            return;
        }
        setStatusMessage("Buscando comprador...", Color.BLACK);

        Optional<CompradorMODEL> compradorOpt = compradorService.buscarPorCPFSwing(cpf);

        if (compradorOpt.isPresent()) {
            compradorSelecionado = compradorOpt.get();
            compradorInfoLabel.setText("Comprador: " + compradorSelecionado.getNome());
            setStatusMessage("Comprador encontrado.", Color.GREEN);
        } else {
            int response = JOptionPane.showConfirmDialog(this,
                    "Comprador com CPF " + cpf + " não encontrado. Deseja cadastrá-lo agora?",
                    "Cadastrar Comprador?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                String nome = JOptionPane.showInputDialog(this, "Digite o nome do novo comprador:", "Cadastrar Comprador", JOptionPane.PLAIN_MESSAGE);
                String telefone = JOptionPane.showInputDialog(this, "Digite o telefone do novo comprador:", "Cadastrar Comprador", JOptionPane.PLAIN_MESSAGE);

                if (nome != null && !nome.isBlank() && telefone != null && !telefone.isBlank()) {
                    Optional<CompradorMODEL> novoCompradorOpt = compradorService.criarCompradorSwing(nome, telefone, cpf);
                    if (novoCompradorOpt.isPresent()) {
                        compradorSelecionado = novoCompradorOpt.get();
                        compradorInfoLabel.setText("Comprador: " + compradorSelecionado.getNome() + " (Cadastrado)");
                        setStatusMessage("Comprador cadastrado e selecionado.", Color.GREEN);
                    } else {
                        compradorSelecionado = null;
                        compradorInfoLabel.setText("Comprador: (Falha ao cadastrar)");
                        setStatusMessage("Erro ao cadastrar comprador.", Color.RED);
                        JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o comprador.", "Erro Cadastro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    compradorSelecionado = null;
                    compradorInfoLabel.setText("Comprador: (Cadastro cancelado)");
                    setStatusMessage("Cadastro de comprador cancelado.", Color.ORANGE);
                }
            } else {
                compradorSelecionado = null;
                compradorInfoLabel.setText("Comprador: (Não cadastrado)");
                setStatusMessage("Comprador não selecionado.", Color.ORANGE);
                cpfCompradorField.setText("");
            }
        }
    }

    private void handleConfirmarVenda() {
        if (produtoSelecionado == null || vendedorAtual == null) {
            setStatusMessage("Erro: Produto ou vendedor inválido.", Color.RED);
            return;
        }
        int quantidade = (int) quantidadeSpinner.getValue();
        if (quantidade <= 0) {
            setStatusMessage("Erro: Quantidade deve ser maior que zero.", Color.RED);
            return;
        }

        Optional<ProdutosMODEL> currentProductOpt = produtoService.buscarPorCodProd(produtoSelecionado.getCodProd());
        if (currentProductOpt.isEmpty()) {
            setStatusMessage("Erro: Produto não encontrado no momento da confirmação.", Color.RED);
            return;
        }
        ProdutosMODEL currentProduct = currentProductOpt.get();
        if (currentProduct.getEstoque() < quantidade) {
            setStatusMessage("Erro: Estoque insuficiente (Estoque atual: " + currentProduct.getEstoque() + ").", Color.RED);
            JOptionPane.showMessageDialog(this,
                    "Estoque insuficiente para a quantidade solicitada.\nEstoque atual: " + currentProduct.getEstoque(),
                    "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
            ((SpinnerNumberModel) quantidadeSpinner.getModel()).setMaximum(Math.max(0, currentProduct.getEstoque()));
            if (currentProduct.getEstoque() <= 0) quantidadeSpinner.setEnabled(false);
            else quantidadeSpinner.setValue(Math.min(quantidade, currentProduct.getEstoque()));
            return;
        }

        setStatusMessage("Processando venda...", Color.BLACK);
        String cpfComprador = (compradorSelecionado != null) ? compradorSelecionado.getCPF() : null;

        String resultado = vendaService.realizarVenda(
                produtoSelecionado.getCodProd(),
                vendedorAtual.getCPF(),
                quantidade,
                cpfComprador
        );

        if (resultado.equals("SUCESSO")) {
            JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!", "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);
            setStatusMessage("Venda realizada com sucesso!", Color.GREEN);
            resetForm();
            carregarProdutosNaTabela(); // Reload table to show updated stock
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao realizar venda: " + resultado, "Erro de Venda", JOptionPane.ERROR_MESSAGE);
            setStatusMessage("Erro: " + resultado, Color.RED);
            handleBuscarProduto(); // Re-fetch product to update info label and spinner
            carregarProdutosNaTabela(); // Reload table to show updated stock
        }
    }

    // --- Helper Methods ---

    private void checkConfirmButtonEnablement() {
        boolean enabled = produtoSelecionado != null
                && quantidadeSpinner.isEnabled()
                && (int) quantidadeSpinner.getValue() > 0
                && vendedorAtual != null;
        confirmarVendaButton.setEnabled(enabled);
    }

    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void resetForm() {
        produtoSelecionado = null;
        compradorSelecionado = null;
        codProdField.setText("");
        cpfCompradorField.setText("");
        produtoInfoLabel.setText("Produto: (Aguardando busca ou seleção na tabela)");
        compradorInfoLabel.setText("Comprador: (Opcional)");
        quantidadeSpinner.setValue(1);
        quantidadeSpinner.setEnabled(false);
        ((SpinnerNumberModel) quantidadeSpinner.getModel()).setMaximum(999);
        confirmarVendaButton.setEnabled(false);
        setStatusMessage(" ", Color.BLACK);
        filtroProdutoField.setText(""); // Clear filter
        if (produtosTable.getRowSorter() != null) {
            ((TableRowSorter<DefaultTableModel>) produtosTable.getRowSorter()).setRowFilter(null);
        }
    }

    private void disableForm() {
        codProdField.setEnabled(false);
        buscarProdutoButton.setEnabled(false);
        cpfCompradorField.setEnabled(false);
        buscarCompradorButton.setEnabled(false);
        quantidadeSpinner.setEnabled(false);
        confirmarVendaButton.setEnabled(false);
        produtosTable.setEnabled(false);
        filtroProdutoField.setEnabled(false);
        tableModel.setRowCount(0); // Clear table if form is disabled
    }

    private void enableForm() {
        codProdField.setEnabled(true);
        buscarProdutoButton.setEnabled(true);
        cpfCompradorField.setEnabled(true);
        buscarCompradorButton.setEnabled(true);
        // quantidadeSpinner is enabled after product search
        produtosTable.setEnabled(true);
        filtroProdutoField.setEnabled(true);
    }
}

