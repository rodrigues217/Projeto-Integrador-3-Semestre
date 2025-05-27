package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.SetorMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.List;

public class SetorRepository {

    public void salvar(SetorMODEL setor) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(setor);
        em.getTransaction().commit();
        em.close();
    }

    public SetorMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        SetorMODEL setor = em.find(SetorMODEL.class, id);
        em.close();
        return setor;
    }

    public List<SetorMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<SetorMODEL> setores = em.createQuery("FROM Setor", SetorMODEL.class).getResultList();
        em.close();
        return setores;
    }

    public void atualizar(SetorMODEL setor) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(setor);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        SetorMODEL setor = em.find(SetorMODEL.class, id);
        if (setor != null) {
            em.remove(setor);
        }
        em.getTransaction().commit();
        em.close();
    }
}
