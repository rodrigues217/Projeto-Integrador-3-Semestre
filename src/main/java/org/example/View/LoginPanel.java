package org.example.View;

import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Refatorado de JFrame para JPanel
public class LoginPanel extends JPanel {

    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UsuarioService usuarioService;
    private ActionListener loginSuccessListener; // Listener para notificar sucesso

    /**
     * Construtor do Painel de Login.
     * @param loginSuccessListener ActionListener a ser chamado em caso de login bem-sucedido.
     */
    public LoginPanel(ActionListener loginSuccessListener) {
        this.usuarioService = new UsuarioService();
        this.loginSuccessListener = loginSuccessListener;

        // Configuração do Layout (GridBagLayout)
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Mais padding

        gbc.insets = new Insets(10, 10, 10, 10); // Mais espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Sistema de Estoque - Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.2; // Espaço acima
        add(titleLabel, gbc);

        // Label e Campo de Login
        JLabel loginLabel = new JLabel("Login:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0; // Reset weighty
        add(loginLabel, gbc);

        loginField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(loginField, gbc);

        // Label e Campo de Senha
        JLabel passwordLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(passwordField, gbc);

        loginField.setPreferredSize(new Dimension(250, 35));
        passwordField.setPreferredSize(new Dimension(250, 35));


        // Botão de Login
        loginButton = new JButton("Entrar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.2; // Espaço abaixo
        add(loginButton, gbc);

        loginButton.setPreferredSize(new Dimension(150,45));
        // --- Lógica de Autenticação ---
        loginButton.addActionListener(e -> handleLogin());

        // Permitir login com Enter no campo de senha
        passwordField.addActionListener(e -> handleLogin());
    }

    // Método para tratar o evento de clique no botão de login ou Enter
    private void handleLogin() {
        String login = loginField.getText();
        String senha = new String(passwordField.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, // Referencia o JPanel
                    "Por favor, preencha o login e a senha.",
                    "Campos Vazios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuarioMODEL usuarioAutenticado = usuarioService.autenticarViaSwing(login, senha);

        if (usuarioAutenticado != null) {
            // Login bem-sucedido: Notifica o listener (MainApplicationFrame)
            if (loginSuccessListener != null) {
                // Cria um ActionEvent com o usuário autenticado como source (ou usa setActionCommand)
                ActionEvent successEvent = new ActionEvent(usuarioAutenticado, ActionEvent.ACTION_PERFORMED, "LOGIN_SUCCESS");
                loginSuccessListener.actionPerformed(successEvent);
            }
        } else {
            // Login falhou
            JOptionPane.showMessageDialog(this, // Referencia o JPanel
                    "Login ou senha inválidos. Tente novamente.",
                    "Erro de Autenticação",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            loginField.requestFocusInWindow();
        }
    }

    // Método para limpar os campos (útil para logout)
    public void clearFields() {
        loginField.setText("");
        passwordField.setText("");
        loginField.requestFocusInWindow();
    }
}

