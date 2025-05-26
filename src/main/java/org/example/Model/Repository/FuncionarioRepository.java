package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.FuncionarioMODEL;
import org.example.Model.ProdutosMODEL;

import java.util.List;
import java.util.Scanner;

public class FuncionarioRepository {

    private EntityManager em;

    public FuncionarioRepository(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    public void cadastrarFuncionario(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();

        FuncionarioMODEL funcionarioMODEL = new FuncionarioMODEL();
        funcionarioMODEL.setNome(nome);

        em.getTransaction().begin();
        em.persist(funcionarioMODEL);
        em.getTransaction().commit();

        System.out.println("Funcionário cadastrado com sucesso!");
    }

    public FuncionarioMODEL BuscarFuncionarioPorID(long id){

       FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);
      return funcionario;
    }


    public void listarFuncionarios() {
        List<FuncionarioMODEL> funcionarioMODELS = em.createQuery("FROM Funcionario", FuncionarioMODEL.class).getResultList();

        System.out.println("\n*** LISTA DE FUNCIONÁRIOS ***");
        for (FuncionarioMODEL funcionarioMODEL : funcionarioMODELS) {
            System.out.println("ID: " + funcionarioMODEL.getId() + " - Nome: " + funcionarioMODEL.getNome());
        }
    }

    public void removerFuncionario(Scanner scanner) {
        listarFuncionarios(); // Mostra os funcionários antes de escolher
        System.out.print("Digite o ID do funcionário que deseja remover: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Limpa o buffer

        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);

        if (funcionario != null) {
            System.out.print("Tem certeza que deseja remover o funcionário '" + funcionario.getNome() + "'? (s/n): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("s")) {
                em.getTransaction().begin();
                em.remove(funcionario);
                em.getTransaction().commit();
                System.out.println("Funcionário removido com sucesso!");
            } else {
                System.out.println("Remoção cancelada.");
            }
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }

    public void atualizarFuncionario(Scanner scanner) {
        listarFuncionarios(); // Mostra os funcionários antes de editar
        System.out.print("Digite o ID do funcionário que deseja atualizar: ");
        long id = scanner.nextLong();
        scanner.nextLine(); // Limpa o buffer

        FuncionarioMODEL funcionario = em.find(FuncionarioMODEL.class, id);

        if (funcionario != null) {
            System.out.print("Digite o novo nome para o funcionário: ");
            String novoNome = scanner.nextLine();

            em.getTransaction().begin();
            funcionario.setNome(novoNome);
            em.merge(funcionario);
            em.getTransaction().commit();

            System.out.println("Funcionário atualizado com sucesso!");
        } else {
            System.out.println("Funcionário não encontrado.");
        }
    }
}
