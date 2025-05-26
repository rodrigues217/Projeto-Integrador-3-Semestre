package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.AuditoriaVendaMODEL;
import org.example.Util.HibernateUtil;

import java.util.List;

public class AuditoriaVendaRepository {

    public void salvar(AuditoriaVendaMODEL auditoria) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(auditoria);
        em.getTransaction().commit();
        em.close();
    }

    public AuditoriaVendaMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        AuditoriaVendaMODEL auditoria = em.find(AuditoriaVendaMODEL.class, id);
        em.close();
        return auditoria;
    }

    public List<AuditoriaVendaMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<AuditoriaVendaMODEL> auditorias = em.createQuery("FROM AuditoriaVendaMODEL", AuditoriaVendaMODEL.class).getResultList();
        em.close();
        return auditorias;
    }

    public void atualizar(AuditoriaVendaMODEL auditoria) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(auditoria);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        AuditoriaVendaMODEL auditoria = em.find(AuditoriaVendaMODEL.class, id);
        if (auditoria != null) {
            em.remove(auditoria);
        }
        em.getTransaction().commit();
        em.close();
    }
}
