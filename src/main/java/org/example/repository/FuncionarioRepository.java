package org.example.repository;

import jakarta.persistence.*;
import org.example.entities.Funcionario;
import org.example.entities.Setor;

import java.util.List;
import java.util.Scanner;

public class FuncionarioRepository {

    private EntityManager em;

    public FuncionarioRepository(EntityManager em) {
        this.em = em;
    }

    public void cadastrarFuncionario(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Digite o nome do funcionário: ");
        String nome = scanner.nextLine();

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(nome);

        em.getTransaction().begin();
        em.persist(funcionario);
        em.getTransaction().commit();

        System.out.println("Funcionário cadastrado com sucesso!");
    }

    public void listarFuncionarios() {
        List<Funcionario> funcionarios = em.createQuery("FROM Funcionario", Funcionario.class).getResultList();

        System.out.println("\n*** LISTA DE FUNCIONÁRIOS ***");
        for (Funcionario funcionario : funcionarios) {
            System.out.println("ID: " + funcionario.getId() + " - Nome: " + funcionario.getNome());
        }
    }
}
