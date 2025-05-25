package org.example.Controller;

import org.example.Model.Repository.SetorRepository;
import org.example.Model.SetorMODEL;
import org.example.View.SetorView;

import java.util.List;
import java.util.Scanner;

public class SetorController {

    private SetorRepository setorRepository;
    private SetorView setorView;
    private Scanner scanner;

    public SetorController(SetorRepository setorRepository, SetorView setorView, Scanner scanner) {
        this.setorRepository = setorRepository;
        this.setorView = setorView;
        this.scanner = scanner;
    }

    public void cadastrarSetor() {
        String nome = setorView.pedirNomeSetor();
        SetorMODEL setor = new SetorMODEL();
        setor.setNome(nome);
        setorRepository.cadastrarSetor(setor);
        setorView.mostrarMensagem("Setor cadastrado com sucesso!");
    }

    public void atualizarSetor() {
        Long id = setorView.pedirIdSetor();
        SetorMODEL setor = setorRepository.buscarSetoresPorId(id);
        if (setor == null) {
            setorView.mostrarMensagem("Setor não encontrado!");
            return;
        }
        String novoNome = setorView.pedirNomeSetor();
        setor.setNome(novoNome);
        setorRepository.atualizarSetor(setor);
        setorView.mostrarMensagem("Setor atualizado com sucesso!");
    }

    public void removerSetor() {
        Long id = setorView.pedirIdSetor();
        SetorMODEL setor = setorRepository.buscarSetoresPorId(id);
        if (setor == null) {
            setorView.mostrarMensagem("Setor não encontrado!");
            return;
        }
        String confirmacao = setorView.pedirConfirmacao("Tem certeza que deseja deletar o setor '" + setor.getNome() + "'? (sim/não)");
        if (confirmacao.equalsIgnoreCase("sim")) {
            setorRepository.removerSetor(setor);
            setorView.mostrarMensagem("Setor removido com sucesso!");
        } else {
            setorView.mostrarMensagem("Operação cancelada.");
        }
    }

    public void listarSetores() {
        List<SetorMODEL> setores = setorRepository.buscarTodosSetores();
        setorView.mostrarListaSetores(setores);
    }
}