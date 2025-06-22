package org.example.View;

import org.example.Controller.CompradorController;
import org.example.Model.Entity.CompradorMODEL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CompradorPanel extends JPanel {

    // Controller
    private final CompradorController compradorController;

    // Comprador em edição
    private CompradorMODEL compradorEmEdicao;

    // Componentes do formulário
    private JTextField idField;
    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField cpfField;
    private JTextField emailField;
    private JTextField enderecoField;

    // Botões
    private JButton novoButton;
    private JButton salvarButton;
    private JButton excluirButton;
    private JButton buscarCpfButton;
    private JButton voltarButton;

    // Componentes da tabela
    private JTable compradoresTable;
    private DefaultTableModel tableModel;
    private JTextField filtroField;
    private JTextField buscarCpfField;

    // Status
    private JLabel statusLabel;

    // Listener para voltar ao menu principal
    private ActionListener backListener;

    public CompradorPanel(ActionListener backListener) {
        this.backListener = backListener;
        this.compradorController = new CompradorController();

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
        carregarCompradoresNaTabela();
    }

    private JPanel criarFormularioPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dados do Comprador"));

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

        // Nome do Comprador
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        nomeField = new JTextField(15);
        panel.add(nomeField, gbc);

        // Telefone
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        telefoneField = new JTextField(15);
        panel.add(telefoneField, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        cpfField = new JTextField(15);
        panel.add(cpfField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        // Endereço
        gbc.gridx = 0; gbc.gridy = gridY;
        panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridy = gridY++;
        enderecoField = new JTextField(15);
        panel.add(enderecoField, gbc);

        // Painel de busca por CPF
        gbc.gridx = 0; gbc.gridy = gridY;
        gbc.gridwidth = 2;
        JPanel buscarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buscarPanel.setBorder(BorderFactory.createTitledBorder("Buscar por CPF"));

        buscarCpfField = new JTextField(12);
        buscarCpfButton = new JButton("Buscar");
        buscarCpfButton.addActionListener(e -> buscarCompradorPorCPF());

        buscarPanel.add(buscarCpfField);
        buscarPanel.add(Box.createHorizontalStrut(5));
        buscarPanel.add(buscarCpfButton);

        panel.add(buscarPanel, gbc);

        return panel;
    }

    private JPanel criarTabelaPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Compradores Cadastrados"));

        // Painel de filtro
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar por Nome:"));
        filtroField = new JTextField(20);
        filterPanel.add(filtroField);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Tabela de compradores
        String[] colunas = {"ID", "Nome", "Telefone", "CPF", "Email", "Endereço"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável diretamente
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        compradoresTable = new JTable(tableModel);
        compradoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        compradoresTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(compradoresTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Listener para duplo clique na tabela
        compradoresTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarCompradorSelecionado();
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
        salvarButton.addActionListener(e -> salvarComprador());
        excluirButton.addActionListener(e -> excluirComprador());
        voltarButton.addActionListener(backListener);

        return panel;
    }

    private void carregarCompradoresNaTabela() {
        tableModel.setRowCount(0); // Limpa a tabela

        try {
            List<CompradorMODEL> compradores = compradorController.listarTodosCompradores();
            if (compradores.isEmpty()) {
                setStatusMessage("Nenhum comprador cadastrado.", Color.ORANGE);
            } else {
                for (CompradorMODEL c : compradores) {
                    tableModel.addRow(new Object[]{
                            c.getId(),
                            c.getNome(),
                            c.getTelefone(),
                            c.getCPF(),
                            c.getEmail(),
                            c.getEndereco()
                    });
                }
                setStatusMessage("Compradores carregados com sucesso: " + compradores.size() + " encontrados.", Color.GREEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao carregar compradores: " + e.getMessage(), Color.RED);
        }
    }

    private void filtrarTabela() {
        String texto = filtroField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) compradoresTable.getRowSorter();

        if (sorter == null) {
            sorter = new TableRowSorter<>(tableModel);
            compradoresTable.setRowSorter(sorter);
        }

        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Filtra por nome (coluna 1)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1));
        }
    }

    private void carregarCompradorSelecionado() {
        int selectedRow = compradoresTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Converte o índice da linha selecionada para o modelo (importante quando há filtro)
            int modelRow = compradoresTable.convertRowIndexToModel(selectedRow);

            // Obtém o ID do comprador selecionado
            Long id = Long.parseLong(tableModel.getValueAt(modelRow, 0).toString());

            // Busca o comprador completo no banco
            CompradorMODEL comprador = compradorController.buscarCompradorPorId(id);

            if (comprador != null) {
                compradorEmEdicao = comprador;

                // Preenche o formulário com os dados do comprador
                idField.setText(comprador.getId().toString());
                nomeField.setText(comprador.getNome());
                telefoneField.setText(comprador.getTelefone());
                cpfField.setText(comprador.getCPF());
                emailField.setText(comprador.getEmail());
                enderecoField.setText(comprador.getEndereco());

                excluirButton.setEnabled(true);
                setStatusMessage("Comprador carregado para edição.", Color.BLUE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao carregar comprador. Comprador não encontrado no banco de dados.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarCompradorPorCPF() {
        String cpf = buscarCpfField.getText().trim();
        if (cpf.isEmpty()) {
            setStatusMessage("Digite um CPF para buscar.", Color.RED);
            buscarCpfField.requestFocus();
            return;
        }

        try {
            CompradorMODEL comprador = compradorController.buscarCompradorPorCPF(cpf);
            if (comprador != null) {
                compradorEmEdicao = comprador;

                // Preenche o formulário com os dados do comprador
                idField.setText(comprador.getId().toString());
                nomeField.setText(comprador.getNome());
                telefoneField.setText(comprador.getTelefone());
                cpfField.setText(comprador.getCPF());
                emailField.setText(comprador.getEmail());
                enderecoField.setText(comprador.getEndereco());

                excluirButton.setEnabled(true);
                setStatusMessage("Comprador encontrado pelo CPF.", Color.GREEN);
                buscarCpfField.setText("");
            } else {
                setStatusMessage("Nenhum comprador encontrado com o CPF informado.", Color.ORANGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao buscar comprador por CPF: " + e.getMessage(), Color.RED);
        }
    }

    private void limparFormulario() {
        compradorEmEdicao = null;
        idField.setText("");
        nomeField.setText("");
        telefoneField.setText("");
        cpfField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        buscarCpfField.setText("");
        excluirButton.setEnabled(false);
        setStatusMessage("Formulário limpo. Pronto para novo cadastro.", Color.BLACK);
    }

    private void salvarComprador() {
        // Validação dos campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Preenche o objeto com os dados do formulário
            String nome = nomeField.getText().trim();
            String telefone = telefoneField.getText().trim();
            String cpf = cpfField.getText().trim();
            String email = emailField.getText().trim();
            String endereco = enderecoField.getText().trim();

            // Salva o comprador
            if (compradorEmEdicao == null || compradorEmEdicao.getId() == null) {
                compradorEmEdicao = compradorController.criarComprador(nome, telefone, cpf, email, endereco);
                setStatusMessage("Comprador cadastrado com sucesso!", Color.GREEN);
            } else {
                compradorEmEdicao = compradorController.atualizarComprador(compradorEmEdicao.getId(), nome, telefone, cpf, email, endereco);
                setStatusMessage("Comprador atualizado com sucesso!", Color.GREEN);
            }

            // Atualiza a tabela e limpa o formulário
            carregarCompradoresNaTabela();
            limparFormulario();

        } catch (Exception e) {
            e.printStackTrace();
            setStatusMessage("Erro ao salvar comprador: " + e.getMessage(), Color.RED);
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar comprador: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        // Validação do nome
        String nome = nomeField.getText().trim();
        if (nome.isEmpty()) {
            setStatusMessage("O nome do comprador é obrigatório.", Color.RED);
            nomeField.requestFocus();
            return false;
        }

        // Validação do telefone
        String telefone = telefoneField.getText().trim();
        if (telefone.isEmpty()) {
            setStatusMessage("O telefone do comprador é obrigatório.", Color.RED);
            telefoneField.requestFocus();
            return false;
        }

        // Validação do CPF
        String cpf = cpfField.getText().trim();
        if (cpf.isEmpty()) {
            setStatusMessage("O CPF do comprador é obrigatório.", Color.RED);
            cpfField.requestFocus();
            return false;
        }

        // Validação do Email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            setStatusMessage("O Email do comprador é obrigatório.", Color.RED);
            emailField.requestFocus();
            return false;
        }

        // Validação do Endereço
        String endereco = enderecoField.getText().trim();
        if (endereco.isEmpty()) {
            setStatusMessage("O Endereço do comprador é obrigatório.", Color.RED);
            enderecoField.requestFocus();
            return false;
        }

        // Verificar se já existe um comprador com o mesmo CPF (exceto o próprio comprador em edição)
        if (compradorEmEdicao == null || !compradorEmEdicao.getCPF().equals(cpf)) { // Apenas verifica se o CPF foi alterado
            if (compradorController.verificarCpfExistente(cpf, compradorEmEdicao != null ? compradorEmEdicao.getId() : null)) {
                setStatusMessage("Já existe um comprador com este CPF.", Color.RED);
                cpfField.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void excluirComprador() {
        if (compradorEmEdicao == null || compradorEmEdicao.getId() == null) {
            setStatusMessage("Nenhum comprador selecionado para exclusão.", Color.RED);
            return;
        }

        // Confirmação de exclusão
        int choice = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o comprador \"" + compradorEmEdicao.getNome() + "\"?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            try {
                compradorController.removerComprador(compradorEmEdicao.getId());
                setStatusMessage("Comprador excluído com sucesso!", Color.GREEN);

                // Atualiza a tabela e limpa o formulário
                carregarCompradoresNaTabela();
                limparFormulario();

            } catch (Exception e) {
                e.printStackTrace();
                setStatusMessage("Erro ao excluir comprador: " + e.getMessage(), Color.RED);
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir comprador: " + e.getMessage(),
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
        carregarCompradoresNaTabela();
        limparFormulario();
    }
}


