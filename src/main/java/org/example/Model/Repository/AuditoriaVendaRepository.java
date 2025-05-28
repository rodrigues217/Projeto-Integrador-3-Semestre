package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.AuditoriaVendaMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.List;

import static org.example.Model.Util.HibernateUtil.getEntityManager;

public class AuditoriaVendaRepository {

    public void salvar(AuditoriaVendaMODEL auditoria) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(auditoria);
        em.getTransaction().commit();
        em.close();
    }

    public AuditoriaVendaMODEL buscarPorId(Long id) {
        EntityManager em = getEntityManager();
        AuditoriaVendaMODEL auditoria = em.find(AuditoriaVendaMODEL.class, id);
        em.close();
        return auditoria;
    }

    public List<AuditoriaVendaMODEL> listarTodos() {
        EntityManager em = getEntityManager();
        List<AuditoriaVendaMODEL> auditorias = em.createQuery("""
            SELECT a FROM AuditoriaVenda a
            JOIN FETCH a.produto
            JOIN FETCH a.funcionario
            LEFT JOIN FETCH a.comprador
        """, AuditoriaVendaMODEL.class)
                .getResultList();
        em.close();
        return auditorias;
    }

    public void atualizar(AuditoriaVendaMODEL auditoria) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(auditoria);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        AuditoriaVendaMODEL auditoria = em.find(AuditoriaVendaMODEL.class, id);
        if (auditoria != null) {
            em.remove(auditoria);
        }
        em.getTransaction().commit();
        em.close();
    }

    public List<AuditoriaVendaMODEL> buscarPorCompradorId(Long id) {
        EntityManager em = getEntityManager();
        return em.createQuery("""
        SELECT a FROM AuditoriaVenda a
        JOIN FETCH a.produto
        JOIN FETCH a.funcionario
        LEFT JOIN FETCH a.comprador
        WHERE a.comprador.id = :id
        """, AuditoriaVendaMODEL.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<AuditoriaVendaMODEL> buscarPorFuncionarioId(Long funcionarioId) {
        EntityManager em = getEntityManager();
        List<AuditoriaVendaMODEL> auditorias = em.createQuery("""
            SELECT a FROM AuditoriaVenda a
            JOIN FETCH a.produto
            JOIN FETCH a.funcionario
            LEFT JOIN FETCH a.comprador
            WHERE a.funcionario.id = :funcionarioId
        """, AuditoriaVendaMODEL.class)
                .setParameter("funcionarioId", funcionarioId)
                .getResultList();
        em.close();
        return auditorias;
    }

    public List<AuditoriaVendaMODEL> buscarPorProdutoId(Long produtoId) {
        EntityManager em = getEntityManager();
        List<AuditoriaVendaMODEL> auditorias = em.createQuery("""
            SELECT a FROM AuditoriaVenda a
            JOIN FETCH a.produto
            JOIN FETCH a.funcionario
            LEFT JOIN FETCH a.comprador
            WHERE a.produto.id = :produtoId
        """, AuditoriaVendaMODEL.class)
                .setParameter("produtoId", produtoId)
                .getResultList();
        em.close();
        return auditorias;
    }
}
