package org.example.View;

import org.example.Controller.UsuarioController;
import org.example.Model.Entity.PerfilUsuario;
import org.example.Model.Entity.UsuarioMODEL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoUsuario extends JPanel {

    private final UsuarioController usuarioController;
    private final DefaultTableModel tableModel;
    private final JTable userTable;

    private final JTextField loginField;
    private final JPasswordField senhaField;
    private final JComboBox<PerfilUsuario> perfilComboBox;
    private final JButton createButton;
    private final JButton updateButton;
    private final JButton deleteButton;

    private Long selectedUserId = null;

    public TelaGerenciamentoUsuario() {
        usuarioController = new UsuarioController();
        setLayout(new BorderLayout());

        // --- Painel de Formulário --- //
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));

        formPanel.add(new JLabel("Login:"));
        loginField = new JTextField();
        formPanel.add(loginField);

        formPanel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        formPanel.add(senhaField);

        formPanel.add(new JLabel("Perfil:"));
        perfilComboBox = new JComboBox<>(PerfilUsuario.values());
        formPanel.add(perfilComboBox);

        createButton = new JButton("Criar Usuário");
        updateButton = new JButton("Atualizar Usuário");
        deleteButton = new JButton("Deletar Usuário");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Tabela de Usuários --- //
        String[] columnNames = {"ID", "Login", "Perfil", "Último Login"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- Ações dos Botões --- //
        createButton.addActionListener(e -> criarUsuario());
        updateButton.addActionListener(e -> atualizarUsuario());
        deleteButton.addActionListener(e -> deletarUsuario());

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                int selectedRow = userTable.getSelectedRow();
                selectedUserId = (Long) userTable.getValueAt(selectedRow, 0);
                loginField.setText((String) userTable.getValueAt(selectedRow, 1));
                perfilComboBox.setSelectedItem(PerfilUsuario.valueOf((String) userTable.getValueAt(selectedRow, 2)));
                senhaField.setText(""); // Não exibe a senha por segurança
            }
        });

        carregarUsuarios();
    }

    private void carregarUsuarios() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<UsuarioMODEL> usuarios = usuarioController.listarUsuarios();
        for (UsuarioMODEL usuario : usuarios) {
            tableModel.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getLogin(),
                    usuario.getPerfil().name(),
                    usuario.getUltimoLogin() != null ? usuario.getUltimoLogin().toString() : "N/A"
            });
        }
    }

    private void criarUsuario() {
        String login = loginField.getText();
        String senha = new String(senhaField.getPassword());
        String perfil = perfilComboBox.getSelectedItem().toString();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Login e Senha não podem ser vazios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioController.criarUsuario(login, senha, perfil);
        carregarUsuarios();
        limparCampos();
    }

    private void atualizarUsuario() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String novoLogin = loginField.getText();
        String novaSenha = new String(senhaField.getPassword());
        String perfil = perfilComboBox.getSelectedItem().toString();

        if (novoLogin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Login não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se a senha estiver vazia, significa que não foi alterada
        if (novaSenha.isEmpty()) {
            // Buscar a senha atual do usuário no banco de dados (ou de outra forma segura)
            // Por simplicidade, aqui vamos assumir que a senha não será alterada se o campo estiver vazio
            // Em um sistema real, você buscaria a senha hash do banco de dados
            JOptionPane.showMessageDialog(this, "Para atualizar, a senha deve ser preenchida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        usuarioController.atualizarUsuario(selectedUserId, novoLogin, novaSenha, perfil);
        carregarUsuarios();
        limparCampos();
    }

    private void deletarUsuario() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o usuário selecionado?", "Confirmar Deleção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            usuarioController.deletarUsuario(selectedUserId);
            carregarUsuarios();
            limparCampos();
        }
    }

    private void limparCampos() {
        loginField.setText("");
        senhaField.setText("");
        perfilComboBox.setSelectedIndex(0);
        selectedUserId = null;
        userTable.clearSelection();
    }
}
