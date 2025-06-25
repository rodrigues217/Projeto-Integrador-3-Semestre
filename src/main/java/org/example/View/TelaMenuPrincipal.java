package org.example.View;

import javax.swing.*;
import java.awt.*;

public class TelaMenuPrincipal extends JFrame {

    private final JPanel painelPrincipal;
    private final CardLayout cardLayout;

    public TelaMenuPrincipal() {
        setTitle("Sistema de Vendas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Centraliza a janela

        // === Inicializa o CardLayout e o painel principal ===
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);

        // === Criação das telas ===
        TelaVenda telaVenda = new TelaVenda();
        TelaGerenciamentoEstoque telaEstoque = new TelaGerenciamentoEstoque();
        TelaGerenciamentoCategoria telaCategoria = new TelaGerenciamentoCategoria();
        TelaGerenciamentoSetor telaSetor = new TelaGerenciamentoSetor();
        CompradorPanel compradorPanel = new CompradorPanel(e -> cardLayout.show(painelPrincipal, "telaVenda"));
        TelaGerenciamentoUsuario telaUsuario = new TelaGerenciamentoUsuario();
        TelaGerenciarFuncionario telaFuncionario = new TelaGerenciarFuncionario();
        TelaGerenciarAuditoria telaAuditoria = new TelaGerenciarAuditoria(); // Nova tela de auditorias

        painelPrincipal.add(telaVenda, "telaVenda");
        painelPrincipal.add(telaEstoque, "telaEstoque");
        painelPrincipal.add(telaCategoria, "telaCategoria");
        painelPrincipal.add(telaSetor, "telaSetores");
        painelPrincipal.add(compradorPanel, "compradorPanel");
        painelPrincipal.add(telaUsuario, "telaUsuario");
        painelPrincipal.add(telaFuncionario, "telaFuncionario");
        painelPrincipal.add(telaAuditoria, "telaAuditoria"); // Adiciona a tela de auditorias ao CardLayout

        // === Barra de menus ===
        JMenuBar menuBar = new JMenuBar();

        // === Menu Venda ===
        JMenu menuVenda = new JMenu("Venda");
        JMenuItem itemNovaVenda = new JMenuItem("Realizar Nova Venda");
        itemNovaVenda.addActionListener(e -> cardLayout.show(painelPrincipal, "telaVenda"));
        menuVenda.add(itemNovaVenda);

        // === Menu Gerenciamento ===
        JMenu menuGerenciamento = new JMenu("Gerenciamento");

        JMenuItem itemEstoque = new JMenuItem("Estoque");
        itemEstoque.addActionListener(e -> cardLayout.show(painelPrincipal, "telaEstoque"));
        menuGerenciamento.add(itemEstoque);

        JMenuItem itemCategorias = new JMenuItem("Categorias");
        itemCategorias.addActionListener(e -> cardLayout.show(painelPrincipal, "telaCategoria"));
        menuGerenciamento.add(itemCategorias);

        JMenuItem itemSetores = new JMenuItem("Setores");
        itemSetores.addActionListener(e -> cardLayout.show(painelPrincipal, "telaSetores"));
        menuGerenciamento.add(itemSetores);

        JMenuItem itemCompradores = new JMenuItem("Compradores");
        itemCompradores.addActionListener(e -> cardLayout.show(painelPrincipal, "compradorPanel"));
        menuGerenciamento.add(itemCompradores);

        JMenuItem itemUsuarios = new JMenuItem("Usuários");
        itemUsuarios.addActionListener(e -> cardLayout.show(painelPrincipal, "telaUsuario"));
        menuGerenciamento.add(itemUsuarios);

        JMenuItem itemFuncionarios = new JMenuItem("Funcionários");
        itemFuncionarios.addActionListener(e -> cardLayout.show(painelPrincipal, "telaFuncionario"));
        menuGerenciamento.add(itemFuncionarios);

        // === Menu Relatórios ===
        JMenu menuRelatorio = new JMenu("Relatórios");

        JMenuItem itemAuditoria = new JMenuItem("Auditoria de Vendas"); // Item de menu funcional para Auditorias
        itemAuditoria.addActionListener(e -> cardLayout.show(painelPrincipal, "telaAuditoria")); // Ação para exibir TelaGerenciarAuditoria
        menuRelatorio.add(itemAuditoria);

        // === Adiciona os menus à barra ===
        menuBar.add(menuVenda);
        menuBar.add(menuGerenciamento);
        menuBar.add(menuRelatorio);
        setJMenuBar(menuBar);

        // === Adiciona o painel principal ao frame ===
        add(painelPrincipal);

        // === Exibe a primeira tela ===
        cardLayout.show(painelPrincipal, "telaVenda");

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaMenuPrincipal::new);
    }
}

