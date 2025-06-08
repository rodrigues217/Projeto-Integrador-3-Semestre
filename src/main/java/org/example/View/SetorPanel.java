package org.example.View;

import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SetorPanel extends JPanel {

    private final SetorRepository setorRepository;
    private final FuncionarioRepository funcionarioRepository;

    // Componentes do formulário
    private JTextField txtId;
    private JTextField txtNome;
    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;
    private JButton btnVoltar;

    // Componentes da tabela
    private JTable tabelaSetores;
    private DefaultTableModel modeloTabela;
    private JTextField txtFiltro;
    private TableRowSorter<DefaultTableModel> sorter;

    // Controle de estado
    private SetorMODEL setorSelecionado;
    private boolean modoEdicao = false;

    public SetorPanel() {
        this.setorRepository = new SetorRepository();
        this.funcionarioRepository = new FuncionarioRepository();

        initComponents();
        setupLayout();
        setupEventListeners();
        carregarSetores();
    }

    private void initComponents() {
        // Componentes do formulário
        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(Color.LIGHT_GRAY);

        txtNome = new JTextField();

        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");
        btnVoltar = new JButton("Voltar ao Menu");

        // Componentes da tabela
        String[] colunas = {"ID", "Nome", "Qtd Funcionários"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaSetores = new JTable(modeloTabela);
        tabelaSetores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sorter = new TableRowSorter<>(modeloTabela);
        tabelaSetores.setRowSorter(sorter);

        txtFiltro = new JTextField();
        txtFiltro.setToolTipText("Digite para filtrar setores por nome");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Painel do formulário (esquerda)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Setor"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtId, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtNome, gbc);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnVoltar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(painelBotoes, gbc);

        // Painel da tabela (direita)
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Setores"));

        // Filtro
        JPanel painelFiltro = new JPanel(new BorderLayout());
        painelFiltro.add(new JLabel("Filtrar por Nome: "), BorderLayout.WEST);
        painelFiltro.add(txtFiltro, BorderLayout.CENTER);
        painelTabela.add(painelFiltro, BorderLayout.NORTH);

        // Tabela
        JScrollPane scrollPane = new JScrollPane(tabelaSetores);
        painelTabela.add(scrollPane, BorderLayout.CENTER);

        // Split pane principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelFormulario, painelTabela);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.3);

        add(splitPane, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        // Botão Novo
        btnNovo.addActionListener(e -> novoSetor());

        // Botão Salvar
        btnSalvar.addActionListener(e -> salvarSetor());

        // Botão Excluir
        btnExcluir.addActionListener(e -> excluirSetor());

        // Botão Voltar
        btnVoltar.addActionListener(e -> voltarAoMenu());

        // Duplo clique na tabela
        tabelaSetores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarSetorSelecionado();
                }
            }
        });

        // Filtro em tempo real
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarTabela();
            }
        });
    }

    private void carregarSetores() {
        try {
            List<SetorMODEL> setores = setorRepository.listarTodos();
            modeloTabela.setRowCount(0);

            for (SetorMODEL setor : setores) {
                int qtdFuncionarios = contarFuncionariosPorSetor(setor.getId());
                Object[] linha = {
                        setor.getId(),
                        setor.getNome(),
                        qtdFuncionarios
                };
                modeloTabela.addRow(linha);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar setores: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int contarFuncionariosPorSetor(Long setorId) {
        try {
            return (int) funcionarioRepository.listarTodos().stream()
                    .filter(func -> func.getSetor() != null && func.getSetor().getId().equals(setorId))
                    .count();
        } catch (Exception e) {
            return 0;
        }
    }

    private void novoSetor() {
        limparFormulario();
        modoEdicao = false;
        setorSelecionado = null;
        txtNome.requestFocus();
    }

    private void salvarSetor() {
        if (!validarFormulario()) {
            return;
        }

        try {
            String nome = txtNome.getText().trim();

            // Verificar se já existe um setor com esse nome
            if (verificarNomeDuplicado(nome)) {
                JOptionPane.showMessageDialog(this,
                        "Já existe um setor com esse nome!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (modoEdicao && setorSelecionado != null) {
                // Atualizar setor existente
                setorSelecionado.setNome(nome);
                setorRepository.atualizar(setorSelecionado);
                JOptionPane.showMessageDialog(this,
                        "Setor atualizado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Criar novo setor
                SetorMODEL novoSetor = new SetorMODEL(nome);
                setorRepository.salvar(novoSetor);
                JOptionPane.showMessageDialog(this,
                        "Setor criado com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            carregarSetores();
            limparFormulario();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar setor: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "O nome do setor é obrigatório!",
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        return true;
    }

    private boolean verificarNomeDuplicado(String nome) {
        try {
            List<SetorMODEL> setores = setorRepository.listarTodos();
            return setores.stream()
                    .anyMatch(setor -> setor.getNome().equalsIgnoreCase(nome) &&
                            (setorSelecionado == null || !setor.getId().equals(setorSelecionado.getId())));
        } catch (Exception e) {
            return false;
        }
    }

    private void excluirSetor() {
        int linhaSelecionada = tabelaSetores.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um setor na tabela para excluir!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int linhaModelo = tabelaSetores.convertRowIndexToModel(linhaSelecionada);
            Long setorId = (Long) modeloTabela.getValueAt(linhaModelo, 0);
            String nomeSetor = (String) modeloTabela.getValueAt(linhaModelo, 1);

            // Verificar se há funcionários vinculados
            int qtdFuncionarios = contarFuncionariosPorSetor(setorId);
            if (qtdFuncionarios > 0) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível excluir este setor pois existem " + qtdFuncionarios +
                                " funcionário(s) vinculado(s) a ele!",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o setor '" + nomeSetor + "'?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                setorRepository.deletar(setorId);
                JOptionPane.showMessageDialog(this,
                        "Setor excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);
                carregarSetores();
                limparFormulario();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao excluir setor: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarSetorSelecionado() {
        int linhaSelecionada = tabelaSetores.getSelectedRow();
        if (linhaSelecionada == -1) {
            return;
        }

        try {
            int linhaModelo = tabelaSetores.convertRowIndexToModel(linhaSelecionada);
            Long setorId = (Long) modeloTabela.getValueAt(linhaModelo, 0);

            setorSelecionado = setorRepository.buscarPorId(setorId);
            if (setorSelecionado != null) {
                txtId.setText(setorSelecionado.getId().toString());
                txtNome.setText(setorSelecionado.getNome());
                modoEdicao = true;
                txtNome.requestFocus();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados do setor: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarTabela() {
        String texto = txtFiltro.getText().trim();
        if (texto.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1)); // Filtrar pela coluna Nome (índice 1)
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        setorSelecionado = null;
        modoEdicao = false;
    }

    private void voltarAoMenu() {
        Container parent = getParent();
        while (parent != null && !(parent instanceof MainApplicationFrame)) {
            parent = parent.getParent();
        }

        if (parent instanceof MainApplicationFrame) {
            ((MainApplicationFrame) parent).mostrarPainelPrincipal();
        }
    }
}

