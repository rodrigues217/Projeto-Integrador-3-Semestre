package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.example.Model.AuditoriaVendaMODEL;

import java.util.List;

public class AuditoriaVendaRepository {

    private EntityManager em;

    public AuditoriaVendaRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void Criar(AuditoriaVendaMODEL auditoriaVenda) {
        if (auditoriaVenda.getId() == null) {
            em.getTransaction().begin();
            em.persist(auditoriaVenda);
            em.getTransaction().commit();
        } else {
            em.getTransaction().begin();
            em.merge(auditoriaVenda);
            em.getTransaction().commit();
        }
    }

    public AuditoriaVendaMODEL buscarPorId(Long id) {
        return em.find(AuditoriaVendaMODEL.class, id);
    }

    public List<AuditoriaVendaMODEL> buscarTodos() {
        return em.createQuery("SELECT a FROM AuditoriaVenda a", AuditoriaVendaMODEL.class)
                .getResultList();
    }

    @Transactional
    public void deletarPorId(Long id) {
        AuditoriaVendaMODEL auditoria = em.find(AuditoriaVendaMODEL.class, id);
        if (auditoria != null) {
            em.getTransaction().begin();
            em.remove(auditoria);
            em.getTransaction().commit();
        }
    }


    public List<AuditoriaVendaMODEL> buscarPorVendedorId(Long vendedorId) {
        return em.createQuery("SELECT a FROM AuditoriaVenda a WHERE a.vendedor.id = :vendedorId", AuditoriaVendaMODEL.class)
                .setParameter("vendedorId", vendedorId)
                .getResultList();
    }


    public List<AuditoriaVendaMODEL> buscarPorProdutoId(Long produtoId) {
        return em.createQuery("SELECT a FROM AuditoriaVenda a WHERE a.produto.id = :produtoId", AuditoriaVendaMODEL.class)
                .setParameter("produtoId", produtoId)
                .getResultList();
    }
}