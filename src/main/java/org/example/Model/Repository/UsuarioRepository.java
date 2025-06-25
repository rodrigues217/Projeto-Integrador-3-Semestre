package org.example.Model.Repository;

import jakarta.persistence.EntityManager;
import org.example.Model.Entity.UsuarioMODEL;
import org.example.Model.Util.HibernateUtil;

import java.util.List;

public class UsuarioRepository {

    public void salvar(UsuarioMODEL usuario) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        em.close();
    }

    public UsuarioMODEL buscarPorId(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        UsuarioMODEL usuario = em.find(UsuarioMODEL.class, id);
        em.close();
        return usuario;
    }

    public List<UsuarioMODEL> listarTodos() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<UsuarioMODEL> usuarios = em.createQuery("FROM Usuario", UsuarioMODEL.class).getResultList();
        em.close();
        return usuarios;
    }

    public void atualizar(UsuarioMODEL usuario) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        em.getTransaction().begin();
        UsuarioMODEL usuario = em.find(UsuarioMODEL.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
        em.getTransaction().commit();
        em.close();
    }
}
