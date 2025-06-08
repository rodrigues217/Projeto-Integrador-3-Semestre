package org.example.View;

import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Entity.PerfilUsuario;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Service.FuncionarioService;
import org.example.Model.Util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainApplicationFrame extends JFrame implements ActionListener {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private JPanel mainContentPanel;
    private VendaPanel vendaPanel;
    private EstoquePanel estoquePanel;
    private CategoriaPanel categoriaPanel;
    private SetorPanel setorPanel;
    private JMenuBar menuBar;
    private Map<String, JMenuItem> menuItems;

    private UsuarioMODEL usuarioLogado;
    private FuncionarioService funcionarioService;

    // Constantes para painéis
    private static final String LOGIN_PANEL = "LoginPanel";
    private static final String MAIN_PANEL = "MainPanel";
    private static final String VENDA_PANEL = "VendaPanel";
    private static final String ESTOQUE_PANEL = "EstoquePanel";
    private static final String CATEGORIA_PANEL = "CategoriaPanel";
    private static final String SETOR_PANEL = "SetorPanel";

    // Constantes para comandos de ação
    private static final String AC_REALIZAR_VENDA = "AC_REALIZAR_VENDA";
    private static final String AC_GER_ESTOQUE = "AC_GER_ESTOQUE";
    private static final String AC_GER_CATEGORIAS = "AC_GER_CATEGORIAS";
    private static final String AC_GER_SETORES = "AC_GER_SETORES";
    private static final String AC_GER_COMPRADORES = "AC_GER_COMPRADORES";
    private static final String AC_GER_USUARIOS = "AC_GER_USUARIOS";
    private static final String AC_GER_FUNCIONARIOS = "AC_GER_FUNCIONARIOS";
    private static final String AC_REL_AUDITORIA = "AC_REL_AUDITORIA";

    public MainApplicationFrame() {
        // Inicializa Hibernate
        try {
            HibernateUtil.getEntityManager();
            System.out.println("Hibernate inicializado com sucesso pela MainApplicationFrame.");
        } catch (Throwable ex) {
            System.err.println("Falha crítica ao inicializar o Hibernate: " + ex);
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro crítico ao inicializar a conexão com o banco de dados.", "Erro de Inicialização", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        this.funcionarioService = new FuncionarioService();
        this.menuItems = new HashMap<>();

        // Configurações do JFrame
        setTitle("Sistema de Gerenciamento de Estoque");
        setSize(800, 600);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Controlamos o fechamento manualmente
        setLocationRelativeTo(null);

        // Listener para o evento de fechamento da janela (botão 'X')
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Fechar diretamente sem confirmação ao clicar no 'X'
                System.out.println("Fechando aplicação via botão 'X'...");
                performShutdown(); // Chama o método de encerramento direto
            }
        });

        // Painel com CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Criação dos painéis
        loginPanel = new LoginPanel(this);
        mainContentPanel = createMainContentPanel();
        vendaPanel = new VendaPanel(e -> cardLayout.show(cardPanel, MAIN_PANEL)); // Ação de voltar
        estoquePanel = new EstoquePanel(e -> cardLayout.show(cardPanel, MAIN_PANEL)); // Ação de voltar
        categoriaPanel = new CategoriaPanel(e -> cardLayout.show(cardPanel, MAIN_PANEL)); // Ação de voltar
        setorPanel = new SetorPanel();

        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(mainContentPanel, MAIN_PANEL);
        cardPanel.add(vendaPanel, VENDA_PANEL);
        cardPanel.add(estoquePanel, ESTOQUE_PANEL);
        cardPanel.add(categoriaPanel, CATEGORIA_PANEL);
        cardPanel.add(setorPanel, SETOR_PANEL);

        add(cardPanel, BorderLayout.CENTER);

        // Criação da Barra de Menu
        menuBar = createMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);

        // Exibe o painel de login
        cardLayout.show(cardPanel, LOGIN_PANEL);
    }

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Bem-vindo! Selecione uma opção no menu.", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        // Menu Arquivo removido conforme solicitado

        JMenu menuVendas = new JMenu("Vendas");
        menuVendas.setMnemonic(KeyEvent.VK_V);
        mb.add(menuVendas);
        addMenuItem(menuVendas, "Realizar Venda", AC_REALIZAR_VENDA, KeyEvent.VK_N, ActionEvent.CTRL_MASK);

        JMenu menuGerenciamento = new JMenu("Gerenciamento");
        menuGerenciamento.setMnemonic(KeyEvent.VK_G);
        mb.add(menuGerenciamento);
        addMenuItem(menuGerenciamento, "Estoque", AC_GER_ESTOQUE, -1, -1);
        addMenuItem(menuGerenciamento, "Categorias", AC_GER_CATEGORIAS, -1, -1);
        addMenuItem(menuGerenciamento, "Setores", AC_GER_SETORES, -1, -1);
        addMenuItem(menuGerenciamento, "Compradores", AC_GER_COMPRADORES, -1, -1);
        menuGerenciamento.addSeparator();
        addMenuItem(menuGerenciamento, "Usuários", AC_GER_USUARIOS, -1, -1);
        addMenuItem(menuGerenciamento, "Funcionários", AC_GER_FUNCIONARIOS, -1, -1);

        JMenu menuRelatorios = new JMenu("Relatórios");
        menuRelatorios.setMnemonic(KeyEvent.VK_R);
        mb.add(menuRelatorios);
        addMenuItem(menuRelatorios, "Auditoria de Vendas", AC_REL_AUDITORIA, -1, -1);

        return mb;
    }

    private void addMenuItem(JMenu menu, String text, String actionCommand, int acceleratorKey, int acceleratorModifiers) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(this);
        if (acceleratorKey > 0) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey, acceleratorModifiers));
        }
        menu.add(menuItem);
        menuItems.put(actionCommand, menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("LOGIN_SUCCESS".equals(command)) {
            handleLoginSuccess((UsuarioMODEL) e.getSource());
            return;
        }

        if (usuarioLogado == null) return;

        switch (command) {
            case AC_REALIZAR_VENDA:
                handleRealizarVenda();
                break;
            case AC_GER_ESTOQUE:
                handleGerenciarEstoque();
                break;
            case AC_GER_CATEGORIAS:
                handleGerenciarCategorias();
                break;
            case AC_GER_SETORES:
                handleGerenciarSetores();
                break;
            case AC_GER_COMPRADORES:
            case AC_GER_USUARIOS:
            case AC_GER_FUNCIONARIOS:
            case AC_REL_AUDITORIA:
                JOptionPane.showMessageDialog(this, "Funcionalidade para \"" + ((JMenuItem)e.getSource()).getText() + "\" ainda não implementada.", "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                System.err.println("Comando de ação desconhecido: " + command);
                break;
        }
    }

    private void handleLoginSuccess(UsuarioMODEL usuario) {
        this.usuarioLogado = usuario;
        System.out.println("Login realizado com sucesso por: " + usuarioLogado.getLogin());

        setTitle("Sistema de Gerenciamento de Estoque - Usuário: " + usuarioLogado.getLogin());
        updateMenuPermissions();
        menuBar.setVisible(true);
        cardLayout.show(cardPanel, MAIN_PANEL);
        ((JLabel)((BorderLayout)mainContentPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER)).setText(
                String.format("Bem-vindo, %s! Selecione uma opção no menu.", usuarioLogado.getLogin())
        );
    }

    private void updateMenuPermissions() {
        boolean isAdmin = (usuarioLogado != null && usuarioLogado.getPerfil() == PerfilUsuario.ADM);
        if (menuItems.containsKey(AC_GER_USUARIOS)) menuItems.get(AC_GER_USUARIOS).setEnabled(isAdmin);
        if (menuItems.containsKey(AC_GER_FUNCIONARIOS)) menuItems.get(AC_GER_FUNCIONARIOS).setEnabled(isAdmin);
        if (menuItems.containsKey(AC_GER_SETORES)) menuItems.get(AC_GER_SETORES).setEnabled(isAdmin);
        if (menuItems.containsKey(AC_REALIZAR_VENDA)) menuItems.get(AC_REALIZAR_VENDA).setEnabled(true);
        if (menuItems.containsKey(AC_GER_ESTOQUE)) menuItems.get(AC_GER_ESTOQUE).setEnabled(true);
        if (menuItems.containsKey(AC_GER_CATEGORIAS)) menuItems.get(AC_GER_CATEGORIAS).setEnabled(true);
        if (menuItems.containsKey(AC_GER_COMPRADORES)) menuItems.get(AC_GER_COMPRADORES).setEnabled(true);
        if (menuItems.containsKey(AC_REL_AUDITORIA)) menuItems.get(AC_REL_AUDITORIA).setEnabled(true);
    }

    private void handleRealizarVenda() {
        Optional<FuncionarioMODEL> funcionarioOpt = funcionarioService.buscarFuncionarioPorUsuario(usuarioLogado);

        if (funcionarioOpt.isPresent()) {
            mostrarPainelVenda(funcionarioOpt.get());
        } else {
            if (usuarioLogado.getPerfil() == PerfilUsuario.ADM) {
                List<FuncionarioMODEL> todosFuncionarios = funcionarioService.listarFuncionarios();
                if (todosFuncionarios.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Não há funcionários cadastrados para selecionar.", "Nenhum Funcionário", JOptionPane.WARNING_MESSAGE);
                } else {
                    String[] opcoesFuncionarios = todosFuncionarios.stream()
                            .map(f -> String.format("ID: %d | Nome: %s | CPF: %s", f.getId(), f.getNome(), f.getCPF()))
                            .toArray(String[]::new);

                    String selecionadoStr = (String) JOptionPane.showInputDialog(
                            this,
                            "Selecione o funcionário responsável pela venda:",
                            "Selecionar Vendedor (ADM)",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            opcoesFuncionarios,
                            opcoesFuncionarios[0]
                    );

                    if (selecionadoStr != null) {
                        try {
                            long selectedId = Long.parseLong(selecionadoStr.split("\\|")[0].split(":")[1].trim());
                            Optional<FuncionarioMODEL> selectedFuncOpt = todosFuncionarios.stream()
                                    .filter(f -> f.getId().equals(selectedId))
                                    .findFirst();
                            selectedFuncOpt.ifPresentOrElse(
                                    this::mostrarPainelVenda,
                                    () -> JOptionPane.showMessageDialog(this, "Erro ao encontrar o funcionário selecionado.", "Erro Interno", JOptionPane.ERROR_MESSAGE)
                            );
                        } catch (Exception ex) {
                            System.err.println("Erro ao parsear ID do funcionário selecionado: " + ex.getMessage());
                            JOptionPane.showMessageDialog(this, "Erro ao processar a seleção do funcionário.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        System.out.println("Seleção de funcionário cancelada pelo ADM.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Não foi possível iniciar a venda.\nO usuário logado (\"" + usuarioLogado.getLogin() + "\") não está associado a um funcionário cadastrado.",
                        "Venda Não Permitida",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void handleGerenciarEstoque() {
        // Atualiza o painel de estoque antes de exibi-lo
        estoquePanel.refresh();
        cardLayout.show(cardPanel, ESTOQUE_PANEL);
    }

    private void handleGerenciarCategorias() {
        // Atualiza o painel de categorias antes de exibi-lo
        categoriaPanel.refresh();
        cardLayout.show(cardPanel, CATEGORIA_PANEL);
    }

    private void handleGerenciarSetores() {
        cardLayout.show(cardPanel, SETOR_PANEL);
    }

    private void mostrarPainelVenda(FuncionarioMODEL funcionario) {
        vendaPanel.setVendedor(funcionario);
        cardLayout.show(cardPanel, VENDA_PANEL);
    }

    public void mostrarPainelPrincipal() {
        cardLayout.show(cardPanel, MAIN_PANEL);
    }

    // Método centralizado para encerrar a aplicação
    private void performShutdown() {
        System.out.println("Encerrando Hibernate...");
        HibernateUtil.closeEntityManagerFactory();
        System.out.println("EntityManagerFactory fechada.");
        System.out.println("Liberando recursos da janela...");
        dispose(); // Libera recursos da janela
        System.out.println("Encerrando JVM...");
        System.exit(0); // Termina a aplicação
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Não foi possível definir o Look and Feel do sistema.");
        }

        MainApplicationFrame mainFrame = new MainApplicationFrame();
        mainFrame.display();
    }
}
