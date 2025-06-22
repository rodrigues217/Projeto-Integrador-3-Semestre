package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.CompradorMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.List;

public class CompradorRepository {

    public void salvar(CompradorMODEL comprador) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(comprador);
        em.getTransaction().commit();
        em.close();
    }

    public CompradorMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
        em.close();
        return comprador;
    }

    public List<CompradorMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<CompradorMODEL> compradores = em.createQuery("FROM CompradorMODEL", CompradorMODEL.class).getResultList();
        em.close();
        return compradores;
    }

    public void atualizar(CompradorMODEL comprador) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(comprador);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        CompradorMODEL comprador = em.find(CompradorMODEL.class, id);
        if (comprador != null) {
            em.remove(comprador);
        }
        em.getTransaction().commit();
        em.close();
    }

    public CompradorMODEL buscarPorCPF(String cpf) {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Comprador c WHERE c.CPF = :cpf", CompradorMODEL.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (Exception e) {
            return null; // Pode ser ajustado para lançar exceção ou tratar de outra forma
        } finally {
            em.close();
        }
    }
}
