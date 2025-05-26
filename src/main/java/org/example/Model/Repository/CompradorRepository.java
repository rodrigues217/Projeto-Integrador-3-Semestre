package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.CompradorMODEL;
import java.util.List;

public class CompradorRepository {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void salvar(CompradorMODEL comprador) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(comprador);
        em.getTransaction().commit();
        em.close();
    }

    public CompradorMODEL buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
        em.close();
        return comprador;
    }

    public List<CompradorMODEL> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<CompradorMODEL> compradores = em.createQuery("FROM Comprador", CompradorMODEL.class).getResultList();
        em.close();
        return compradores;
    }

    public void atualizar(CompradorMODEL comprador) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(comprador);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
        if (comprador != null) {
            em.remove(comprador);
        }
        em.getTransaction().commit();
        em.close();
    }
}
