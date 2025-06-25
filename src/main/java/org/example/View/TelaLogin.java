package org.example.View;

import org.example.Controller.UsuarioController;
import org.example.Model.Entity.UsuarioMODEL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

    private final JTextField campoLogin;
    private final JPasswordField campoSenha;
    private final UsuarioController usuarioController;

    public TelaLogin() {
        super("Login do Sistema");

        usuarioController = new UsuarioController();

        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(4, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelLogin = new JLabel("Login:");
        campoLogin = new JTextField();

        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField();

        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.addActionListener(new EntrarListener());

        JButton botaoSair = new JButton("Sair");
        botaoSair.addActionListener(e -> System.exit(0));

        painel.add(labelLogin);
        painel.add(campoLogin);
        painel.add(labelSenha);
        painel.add(campoSenha);
        painel.add(botaoEntrar);
        painel.add(botaoSair);

        add(painel);

        setVisible(true);
    }

    private class EntrarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String login = campoLogin.getText();
            String senha = new String(campoSenha.getPassword());

            UsuarioMODEL usuario = usuarioController.login(login, senha);

            if (usuario != null) {
                JOptionPane.showMessageDialog(null,
                        "Bem-vindo, " + usuario.getLogin() + "!",
                        "Login bem-sucedido",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Fecha a tela de login
                new TelaMenuPrincipal();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Login ou senha inválidos.",
                        "Erro de login",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
