package org.example.Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.UsuarioMODEL;
import org.example.Util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.Scanner;

public class UsuarioController {

    public UsuarioMODEL fazerLogin(Scanner scanner) {
        EntityManager em = HibernateUtil.getEntityManager();
        UsuarioMODEL usuarioLogado = null;

        try {
            while (usuarioLogado == null) {
                System.out.println("*** LOGIN ***");
                System.out.print("Login: ");
                String login = scanner.next();
                System.out.print("Senha: ");
                String senha = scanner.next();

                usuarioLogado = autenticarUsuario(em, login, senha);

                if (usuarioLogado == null) {
                    System.out.println("Credenciais inválidas. Tente novamente.");
                } else {
                    System.out.println("Último login: " +
                            (usuarioLogado.getUltimoLogin() != null ? usuarioLogado.getUltimoLogin() : "Nunca"));

                    em.getTransaction().begin();
                    usuarioLogado.setUltimoLogin(LocalDateTime.now());
                    em.merge(usuarioLogado);
                    em.getTransaction().commit();

                    System.out.println("Login bem-sucedido!\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }

        return usuarioLogado;
    }

    private UsuarioMODEL autenticarUsuario(EntityManager em, String login, String senha) {
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
