package org.example.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.UsuarioMODEL;

public class UsuarioDAO {

    private static EntityManager em;

    public UsuarioDAO(EntityManager em) {
        this.em = em;
    }

    public void salvar(UsuarioMODEL usuario) {
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    public UsuarioMODEL buscarPorLogin(String login) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", UsuarioMODEL.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public static UsuarioMODEL autenticar(String login, String senha) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", UsuarioMODEL.class)
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


}
