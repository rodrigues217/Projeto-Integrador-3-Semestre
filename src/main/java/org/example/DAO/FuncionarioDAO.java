package org.example.DAO;

import jakarta.persistence.*;
import org.example.Model.FuncionarioMODEL;

import java.util.List;
import java.util.Scanner;

public class FuncionarioDAO {

    private EntityManager em;

    public FuncionarioDAO(EntityManager em) {
        this.em = em;
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

    public void listarFuncionarios() {
        List<FuncionarioMODEL> funcionarioMODELS = em.createQuery("FROM Funcionario", FuncionarioMODEL.class).getResultList();

        System.out.println("\n*** LISTA DE FUNCIONÁRIOS ***");
        for (FuncionarioMODEL funcionarioMODEL : funcionarioMODELS) {
            System.out.println("ID: " + funcionarioMODEL.getId() + " - Nome: " + funcionarioMODEL.getNome());
        }
    }
}
