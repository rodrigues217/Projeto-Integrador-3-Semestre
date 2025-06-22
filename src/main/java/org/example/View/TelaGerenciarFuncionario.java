package org.example.View;

import org.example.Controller.FuncionarioController;
import org.example.Controller.UsuarioController;
import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Repository.SetorRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TelaGerenciarFuncionario extends JPanel {

    private final FuncionarioController funcionarioController;
    private final UsuarioController usuarioController;
    private final SetorRepository setorRepository;

    private final DefaultTableModel tableModel;
    private final JTable funcionarioTable;

    // Campos do formulário
    private final JTextField nomeField;
    private final JTextField cpfField;
    private final JTextField enderecoField;
    private final JTextField telefoneField;
    private final JComboBox<UsuarioMODEL> usuarioComboBox;
    private final JComboBox<SetorMODEL> setorComboBox;

    // Botões
    private final JButton criarButton;
    private final JButton atualizarButton;
    private final JButton deletarButton;
    private final JButton buscarCpfButton;
    private final JButton trocarUsuarioButton;
    private final JButton trocarSetorButton;
    private final JButton limparButton;

    // Campo de busca
    private final JTextField buscaCpfField;

    private Long selectedFuncionarioId = null;

    public TelaGerenciarFuncionario() {
        funcionarioController = new FuncionarioController();
        usuarioController = new UsuarioController();
        setorRepository = new SetorRepository();

        setLayout(new BorderLayout());

        // === Painel Superior - Busca ===
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.setBorder(BorderFactory.createTitledBorder("Buscar Funcionário"));

        buscaPanel.add(new JLabel("CPF:"));
        buscaCpfField = new JTextField(15);
        buscaPanel.add(buscaCpfField);

        buscarCpfButton = new JButton("Buscar por CPF");
        buscaPanel.add(buscarCpfButton);

        add(buscaPanel, BorderLayout.NORTH);

        // === Painel Central - Tabela ===
        String[] columnNames = {"ID", "Nome", "CPF", "Endereço", "Telefone", "Total Vendas", "Usuário", "Setor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        funcionarioTable = new JTable(tableModel);
        funcionarioTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(funcionarioTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));

        add(scrollPane, BorderLayout.CENTER);

        // === Painel Inferior - Formulário e Botões ===
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        formPanel.add(cpfField);

        formPanel.add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        formPanel.add(enderecoField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Usuário:"));
        usuarioComboBox = new JComboBox<>();
        formPanel.add(usuarioComboBox);

        formPanel.add(new JLabel("Setor:"));
        setorComboBox = new JComboBox<>();
        formPanel.add(setorComboBox);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        criarButton = new JButton("Criar");
        atualizarButton = new JButton("Atualizar");
        deletarButton = new JButton("Deletar");
        limparButton = new JButton("Limpar");
        trocarUsuarioButton = new JButton("Trocar Usuário");
        trocarSetorButton = new JButton("Trocar Setor");

        buttonPanel.add(criarButton);
        buttonPanel.add(atualizarButton);
        buttonPanel.add(deletarButton);
        buttonPanel.add(limparButton);
        buttonPanel.add(trocarUsuarioButton);
        buttonPanel.add(trocarSetorButton);
        buttonPanel.add(new JLabel()); // Espaço vazio
        buttonPanel.add(new JLabel()); // Espaço vazio

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Configurar Ações ===
        configurarAcoes();

        // === Carregar dados iniciais ===
        carregarComboBoxes();
        carregarFuncionarios();
    }

    private void configurarAcoes() {
        // Seleção na tabela
        funcionarioTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && funcionarioTable.getSelectedRow() != -1) {
                int selectedRow = funcionarioTable.getSelectedRow();
                selectedFuncionarioId = (Long) funcionarioTable.getValueAt(selectedRow, 0);
                preencherFormulario(selectedRow);
            }
        });

        // Botões
        criarButton.addActionListener(e -> criarFuncionario());
        atualizarButton.addActionListener(e -> atualizarFuncionario());
        deletarButton.addActionListener(e -> deletarFuncionario());
        buscarCpfButton.addActionListener(e -> buscarPorCpf());
        trocarUsuarioButton.addActionListener(e -> trocarUsuario());
        trocarSetorButton.addActionListener(e -> trocarSetor());
        limparButton.addActionListener(e -> limparCampos());
    }

    private void carregarComboBoxes() {
        // Carregar usuários
        usuarioComboBox.removeAllItems();
        List<UsuarioMODEL> usuarios = usuarioController.listarUsuarios();
        for (UsuarioMODEL usuario : usuarios) {
            usuarioComboBox.addItem(usuario);
        }

        // Carregar setores
        setorComboBox.removeAllItems();
        List<SetorMODEL> setores = setorRepository.listarTodos();
        for (SetorMODEL setor : setores) {
            setorComboBox.addItem(setor);
        }
    }

    private void carregarFuncionarios() {
        tableModel.setRowCount(0);
        List<FuncionarioMODEL> funcionarios = funcionarioController.listarFuncionarios();

        for (FuncionarioMODEL funcionario : funcionarios) {
            tableModel.addRow(new Object[]{
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getCPF(),
                    funcionario.getEndereco(),
                    funcionario.getTelefone(),
                    funcionario.getTotalVendas(),
                    funcionario.getUsuario() != null ? funcionario.getUsuario().getLogin() : "N/A",
                    funcionario.getSetor() != null ? funcionario.getSetor().getNome() : "N/A"
            });
        }
    }

    private void preencherFormulario(int row) {
        nomeField.setText((String) funcionarioTable.getValueAt(row, 1));
        cpfField.setText((String) funcionarioTable.getValueAt(row, 2));
        enderecoField.setText((String) funcionarioTable.getValueAt(row, 3));
        telefoneField.setText((String) funcionarioTable.getValueAt(row, 4));

        // Selecionar usuário no combo
        String usuarioLogin = (String) funcionarioTable.getValueAt(row, 6);
        for (int i = 0; i < usuarioComboBox.getItemCount(); i++) {
            UsuarioMODEL usuario = usuarioComboBox.getItemAt(i);
            if (usuario.getLogin().equals(usuarioLogin)) {
                usuarioComboBox.setSelectedIndex(i);
                break;
            }
        }

        // Selecionar setor no combo
        String setorNome = (String) funcionarioTable.getValueAt(row, 7);
        for (int i = 0; i < setorComboBox.getItemCount(); i++) {
            SetorMODEL setor = setorComboBox.getItemAt(i);
            if (setor.getNome().equals(setorNome)) {
                setorComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void criarFuncionario() {
        if (!validarCampos()) return;

        try {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String telefone = telefoneField.getText().trim();

            UsuarioMODEL usuarioSelecionado = (UsuarioMODEL) usuarioComboBox.getSelectedItem();
            SetorMODEL setorSelecionado = (SetorMODEL) setorComboBox.getSelectedItem();

            funcionarioController.criarFuncionario(nome, cpf, endereco, telefone,
                    usuarioSelecionado.getId(), setorSelecionado.getId());

            JOptionPane.showMessageDialog(this, "Funcionário criado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            carregarFuncionarios();
            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar funcionário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarFuncionario() {
        if (selectedFuncionarioId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para atualizar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) return;

        try {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String telefone = telefoneField.getText().trim();

            funcionarioController.atualizarFuncionario(selectedFuncionarioId, nome, cpf, endereco, telefone);

            JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            carregarFuncionarios();
            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarFuncionario() {
        if (selectedFuncionarioId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para deletar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja deletar o funcionário selecionado?",
                "Confirmar Deleção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String cpf = cpfField.getText().trim();
                funcionarioController.deletarFuncionario(cpf);

                JOptionPane.showMessageDialog(this, "Funcionário deletado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                carregarFuncionarios();
                limparCampos();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao deletar funcionário: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPorCpf() {
        String cpf = buscaCpfField.getText().trim();
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um CPF para buscar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            FuncionarioMODEL funcionario = funcionarioController.buscarFuncionarioPorCPF(cpf);

            // Limpar tabela e mostrar apenas o funcionário encontrado
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{
                    funcionario.getId(),
                    funcionario.getNome(),
                    funcionario.getCPF(),
                    funcionario.getEndereco(),
                    funcionario.getTelefone(),
                    funcionario.getTotalVendas(),
                    funcionario.getUsuario() != null ? funcionario.getUsuario().getLogin() : "N/A",
                    funcionario.getSetor() != null ? funcionario.getSetor().getNome() : "N/A"
            });

            // Selecionar a linha
            funcionarioTable.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Funcionário não encontrado", JOptionPane.WARNING_MESSAGE);
            carregarFuncionarios(); // Recarregar todos os funcionários
        }
    }

    private void trocarUsuario() {
        if (selectedFuncionarioId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para trocar o usuário.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            UsuarioMODEL usuarioSelecionado = (UsuarioMODEL) usuarioComboBox.getSelectedItem();
            funcionarioController.trocarUsuarioDeFuncionario(selectedFuncionarioId, usuarioSelecionado.getId());

            JOptionPane.showMessageDialog(this, "Usuário do funcionário alterado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            carregarFuncionarios();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao trocar usuário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void trocarSetor() {
        if (selectedFuncionarioId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para trocar o setor.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            SetorMODEL setorSelecionado = (SetorMODEL) setorComboBox.getSelectedItem();
            funcionarioController.trocarSetorDeFuncionario(selectedFuncionarioId, setorSelecionado.getId());

            JOptionPane.showMessageDialog(this, "Setor do funcionário alterado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            carregarFuncionarios();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao trocar setor: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cpfField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "CPF é obrigatório.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (telefoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Telefone é obrigatório.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usuarioComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (setorComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um setor.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void limparCampos() {
        nomeField.setText("");
        cpfField.setText("");
        enderecoField.setText("");
        telefoneField.setText("");
        buscaCpfField.setText("");

        if (usuarioComboBox.getItemCount() > 0) {
            usuarioComboBox.setSelectedIndex(0);
        }

        if (setorComboBox.getItemCount() > 0) {
            setorComboBox.setSelectedIndex(0);
        }

        selectedFuncionarioId = null;
        funcionarioTable.clearSelection();
    }
}

