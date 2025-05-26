package org.example.Model.Repository;

import jakarta.persistence.*;
import org.example.Model.UsuarioMODEL;
import java.util.List;

public class UsuarioRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public void salvar(UsuarioMODEL usuario) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
        em.close();
    }

    public UsuarioMODEL buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        UsuarioMODEL usuario = em.find(UsuarioMODEL.class, id);
        em.close();
        return usuario;
    }

    public List<UsuarioMODEL> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<UsuarioMODEL> usuarios = em.createQuery("FROM Usuario", UsuarioMODEL.class).getResultList();
        em.close();
        return usuarios;
    }

    public void atualizar(UsuarioMODEL usuario) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(usuario);
        em.getTransaction().commit();
        em.close();
    }

    public void deletar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UsuarioMODEL usuario = em.find(UsuarioMODEL.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
        em.getTransaction().commit();
        em.close();
    }
}
