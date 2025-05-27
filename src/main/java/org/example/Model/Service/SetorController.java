/*package org.example.Controller;

import org.example.Model.Entity.FuncionarioMODEL;
import org.example.Model.Repository.FuncionarioRepository;
import org.example.Model.Repository.SetorRepository;
import org.example.Model.Entity.SetorMODEL;

import java.util.Scanner;

public class SetorController {

    private final SetorRepository setorRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final Scanner scanner;

    public SetorController(SetorRepository setorRepository, FuncionarioRepository funcionarioRepository, Scanner scanner) {
        this.setorRepository = setorRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.scanner = scanner;
    }

    public void adicionarFuncionarioAoSetor() {
        System.out.println("---- Adicionar Funcionário ao Setor ----");

        // Listar setores
        var setores = setorRepository.buscarTodosSetores();
        if (setores.isEmpty()) {
            System.out.println("Nenhum setor encontrado.");
            return;
        }
        setores.forEach(s -> System.out.println("ID: " + s.getId() + " | Nome: " + s.getNome()));

        System.out.print("Digite o ID do setor: ");
        long setorId = scanner.nextLong();
        scanner.nextLine(); // limpar buffer

        SetorMODEL setor = setorRepository.buscarSetoresPorId(setorId);
        if (setor == null) {
            System.out.println("Setor não encontrado.");
            return;
        }

        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o endereço do funcionário: ");
        String endereco = scanner.nextLine();
        System.out.print("Digite o documento do funcionário: ");
        String documento = scanner.nextLine();

        FuncionarioMODEL funcionario = new FuncionarioMODEL();
        funcionario.setNome(nome);
        funcionario.setEndereco(endereco);
        funcionario.setDocumento(documento);
        funcionario.setSetor(setor);

        var em = funcionarioRepository.getEm();
        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();

        System.out.println("Funcionário adicionado ao setor com sucesso!");
    }

    public void removerFuncionarioDoSetor() {
        System.out.println("---- Remover Funcionário ----");

        funcionarioRepository.listarFuncionarios();

        System.out.print("Digite o ID do funcionário que deseja remover: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        FuncionarioMODEL funcionario = funcionarioRepository.getEm().find(FuncionarioMODEL.class, id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja remover o funcionário '" + funcionario.getNome() + "'? (s/n): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("s")) {
            var em = funcionarioRepository.getEm();
            em.getTransaction().begin();
            em.remove(funcionario);
            em.getTransaction().commit();
            System.out.println("Funcionário removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }
}
*/