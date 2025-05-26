package org.example.Controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.Model.UsuarioMODEL;
import org.example.Model.Repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Scanner;

public class UsuarioController {

    private static EntityManager em = null;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(EntityManager em) {
        this.em = em;
        this.usuarioRepository = new UsuarioRepository(em);
    }

    public static UsuarioMODEL fazerLogin(Scanner scanner) {
        UsuarioMODEL usuarioLogado = null;

        while (usuarioLogado == null) {
            System.out.println("*** LOGIN ***");
            System.out.print("Login: ");
            String login = scanner.next();
            System.out.print("Senha: ");
            String senha = scanner.next();

            usuarioLogado = UsuarioController.autenticarUsuario(login, senha);

            if (usuarioLogado == null) {
                System.out.println("Credenciais inválidas. Tente novamente.");
            } else {
                System.out.println("Último login: " +
                        (usuarioLogado.getUltimoLogin() != null ? usuarioLogado.getUltimoLogin() : "Nunca"));

                usuarioLogado.setUltimoLogin(LocalDateTime.now());
                em.getTransaction().begin();
                em.merge(usuarioLogado);
                em.getTransaction().commit();

                System.out.println("Login bem-sucedido!\n");
            }
        }

        return usuarioLogado;
    }
    public static UsuarioMODEL autenticarUsuario(String login, String senha) {
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
