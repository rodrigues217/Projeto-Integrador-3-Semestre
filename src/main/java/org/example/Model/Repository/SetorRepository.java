package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.SetorMODEL;

import java.util.List;
import java.util.Scanner;

public class SetorRepository {

    private EntityManager em;

    public SetorRepository(EntityManager em) {
        this.em = em;
    }

    public void cadastrarSetor(SetorMODEL setorMODEL) {
        try {
            em.getTransaction().begin();
            em.persist(setorMODEL);
            em.getTransaction().commit();
            System.out.println("Setor cadastrado com sucesso!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

       public void atualizarSetor(SetorMODEL setorMODEL) {
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

    public void removerSetor(SetorMODEL setorMODEL) {
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

        public List<SetorMODEL> buscarTodosSetores() {
            return em.createQuery("SELECT s FROM Setor s", SetorMODEL.class)
                    .getResultList();
        }

        public List<SetorMODEL> buscarSetoresComFuncionarios() {
            return em.createQuery("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios", SetorMODEL.class)
                    .getResultList();
        }

        public SetorMODEL buscarSetoresPorId(long id) {
            return em.find(SetorMODEL.class, id);
        }
    }


