package org.example.DAO;

import jakarta.persistence.*;
import org.example.Model.SetorMODEL;

import java.util.List;
import java.util.Scanner;

public class SetorDAO {

    private EntityManager em;

    public SetorDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(SetorMODEL setorMODEL) {
        try {
            em.getTransaction().begin();
            em.persist(setorMODEL);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<SetorMODEL> buscarTodos() {
        return em.createQuery("SELECT s FROM Setor s", SetorMODEL.class).getResultList();
    }

    public SetorMODEL buscarPorId(long id) {
        return em.find(SetorMODEL.class, id);
    }

    public void atualizar(SetorMODEL setorMODEL) {
        try {
            em.getTransaction().begin();
            em.merge(setorMODEL);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public void remover(SetorMODEL setorMODEL) {
        try {
            em.getTransaction().begin();
            setorMODEL = em.find(SetorMODEL.class, setorMODEL.getId());
            if (setorMODEL != null) {
                em.remove(setorMODEL);
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

        SetorMODEL setorMODEL = new SetorMODEL();
        setorMODEL.setNome(nome);

        em.getTransaction().begin();
        em.persist(setorMODEL);
        em.getTransaction().commit();

        System.out.println("Setor cadastrado com sucesso!");
    }

    public void listarSetores() {
        List<SetorMODEL> setores = em.createQuery("FROM Setor", SetorMODEL.class).getResultList();

        System.out.println("\n*** LISTA DE SETORES ***");
        for (SetorMODEL setorMODEL : setores) {
            System.out.println("ID: " + setorMODEL.getId() + " - Nome: " + setorMODEL.getNome());
        }
    }


    public List<SetorMODEL> listarSetoresComFuncionarios() {
        return em.createQuery("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios", SetorMODEL.class).getResultList();
    }
}
