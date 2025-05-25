package org.example.Controller;

import org.example.Model.FuncionarioMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.View.FuncionarioView;

import java.util.List;
import java.util.Scanner;

public class FuncionarioController {

    private final FuncionarioRepository repository;
    private final FuncionarioView view;
    private final Scanner scanner;

    public FuncionarioController(FuncionarioRepository repository, FuncionarioView view, Scanner scanner) {
        this.repository = repository;
        this.view = view;
        this.scanner = scanner;
    }

    public void executar() {
        int opcao;
        do {
            view.exibirMenu();
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> remover();
                case 0 -> view.mostrarMensagem("Voltando ao menu principal...");
                default -> view.mostrarMensagem("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        String nome = view.pedirNomeFuncionario();
        FuncionarioMODEL funcionario = new FuncionarioMODEL();
        funcionario.setNome(nome);

        repository.getEm().getTransaction().begin();
        repository.getEm().persist(funcionario);
        repository.getEm().getTransaction().commit();

        view.mostrarMensagem("Funcionário cadastrado com sucesso!");
    }

    private void listar() {
        List<FuncionarioMODEL> funcionarios = repository.getEm()
                .createQuery("FROM Funcionario", FuncionarioMODEL.class).getResultList();
        view.mostrarFuncionarios(funcionarios);
    }

    private void atualizar() {
        listar();
        Long id = view.pedirIdFuncionario();
        FuncionarioMODEL funcionario = repository.getEm().find(FuncionarioMODEL.class, id);

        if (funcionario != null) {
            String novoNome = view.pedirNomeFuncionario();
            repository.getEm().getTransaction().begin();
            funcionario.setNome(novoNome);
            repository.getEm().merge(funcionario);
            repository.getEm().getTransaction().commit();
            view.mostrarMensagem("Funcionário atualizado com sucesso!");
        } else {
            view.mostrarMensagem("Funcionário não encontrado.");
        }
    }

    private void remover() {
        listar();
        Long id = view.pedirIdFuncionario();
        FuncionarioMODEL funcionario = repository.getEm().find(FuncionarioMODEL.class, id);

        if (funcionario != null) {
            String confirm = view.confirmarRemocao(funcionario.getNome());
            if (confirm.equalsIgnoreCase("s")) {
                repository.getEm().getTransaction().begin();
                repository.getEm().remove(funcionario);
                repository.getEm().getTransaction().commit();
                view.mostrarMensagem("Funcionário removido com sucesso!");
            } else {
                view.mostrarMensagem("Remoção cancelada.");
            }
        } else {
            view.mostrarMensagem("Funcionário não encontrado.");
        }
    }
}
