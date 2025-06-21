package org.example.View;

import org.example.Controller.SetorController;
import org.example.Model.Entity.SetorMODEL;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaGerenciamentoSetor extends JPanel {

    private final SetorController setorController = new SetorController();
    private final DefaultListModel<SetorMODEL> setorListModel = new DefaultListModel<>();
    private final JList<SetorMODEL> listaSetores = new JList<>(setorListModel);
    private final JTextField campoNomeSetor = new JTextField(20);

    public TelaGerenciamentoSetor() {
        setLayout(new BorderLayout());

        // Painel principal da esquerda (lista de setores)
        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(BorderFactory.createTitledBorder("Setores Cadastrados"));
        JScrollPane scrollLista = new JScrollPane(listaSetores);
        scrollLista.setPreferredSize(new Dimension(300, 200));
        painelLista.add(scrollLista, BorderLayout.CENTER);

        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridLayout(0, 1, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Gerenciar Setor"));
        painelFormulario.add(new JLabel("Nome do Setor:"));
        painelFormulario.add(campoNomeSetor);

        JButton botaoAdicionar = new JButton("Adicionar");
        JButton botaoEditar = new JButton("Editar");
        JButton botaoExcluir = new JButton("Excluir");

        painelFormulario.add(botaoAdicionar);
        painelFormulario.add(botaoEditar);
        painelFormulario.add(botaoExcluir);

        // Adiciona os painéis ao layout principal
        add(painelLista, BorderLayout.WEST);
        add(painelFormulario, BorderLayout.CENTER);

        // Ações dos botões
        botaoAdicionar.addActionListener(e -> adicionarSetor());
        botaoEditar.addActionListener(e -> editarSetor());
        botaoExcluir.addActionListener(e -> excluirSetor());

        // Atualiza campo ao selecionar
        listaSetores.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SetorMODEL selecionado = listaSetores.getSelectedValue();
                campoNomeSetor.setText(selecionado != null ? selecionado.getNome() : "");
            }
        });

        carregarSetores();
    }

    private void carregarSetores() {
        setorListModel.clear();
        List<SetorMODEL> setores = setorController.listarSetores();
        setores.forEach(setorListModel::addElement);
    }

    private void adicionarSetor() {
        try {
            String nome = campoNomeSetor.getText().trim();
            setorController.criarSetor(nome);
            carregarSetores();
            campoNomeSetor.setText("");
            JOptionPane.showMessageDialog(this, "Setor criado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao adicionar setor", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarSetor() {
        try {
            SetorMODEL selecionado = listaSetores.getSelectedValue();
            if (selecionado == null) throw new Exception("Selecione um setor para editar.");
            String novoNome = campoNomeSetor.getText().trim();
            setorController.atualizarSetor(selecionado.getId(), novoNome);
            carregarSetores();
            JOptionPane.showMessageDialog(this, "Setor atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao editar setor", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirSetor() {
        try {
            SetorMODEL selecionado = listaSetores.getSelectedValue();
            if (selecionado == null) throw new Exception("Selecione um setor para excluir.");

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o setor '" + selecionado.getNome() + "'?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                setorController.deletarSetor(selecionado.getId());
                carregarSetores();
                campoNomeSetor.setText("");
                JOptionPane.showMessageDialog(this, "Setor excluído com sucesso!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao excluir setor", JOptionPane.ERROR_MESSAGE);
        }
    }
}
