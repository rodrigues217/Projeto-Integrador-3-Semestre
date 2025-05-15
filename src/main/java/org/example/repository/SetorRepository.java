package org.example.repository;

import jakarta.persistence.*;
import org.example.entities.Setor;
import org.example.entities.Funcionario;

import java.util.List;
import java.util.Scanner;

public class SetorRepository {

    private EntityManager em;

    public SetorRepository(EntityManager em) {
        this.em = em;
    }

    public void salvar(Setor setor) {
        try {
            em.getTransaction().begin();
            em.persist(setor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Setor> buscarTodos() {
        return em.createQuery("SELECT s FROM Setor s", Setor.class).getResultList();
    }

    public Setor buscarPorId(long id) {
        return em.find(Setor.class, id);
    }

    public void atualizar(Setor setor) {
        try {
            em.getTransaction().begin();
            em.merge(setor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void remover(Setor setor) {
        try {
            em.getTransaction().begin();
            setor = em.find(Setor.class, setor.getId());
            if (setor != null) {
                em.remove(setor);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
    public void cadastrarSetor(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Digite o nome do setor: ");
        String nome = scanner.nextLine();

        Setor setor = new Setor();
        setor.setNome(nome);

        em.getTransaction().begin();
        em.persist(setor);
        em.getTransaction().commit();

        System.out.println("Setor cadastrado com sucesso!");
    }

    public void listarSetores() {
        List<Setor> setores = em.createQuery("FROM Setor", Setor.class).getResultList();

        System.out.println("\n*** LISTA DE SETORES ***");
        for (Setor setor : setores) {
            System.out.println("ID: " + setor.getId() + " - Nome: " + setor.getNome());
        }
    }


    public List<Setor> listarSetoresComFuncionarios() {
        return em.createQuery("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios", Setor.class).getResultList();
    }
}
