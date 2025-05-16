package org.example.Service;

import jakarta.persistence.EntityManager;
import org.example.entities.Usuario;
import org.example.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Scanner;

public class AuthService {

    private static EntityManager em = null;
    private final UsuarioRepository usuarioRepository;

    public AuthService(EntityManager em) {
        this.em = em;
        this.usuarioRepository = new UsuarioRepository(em);
    }

    public static Usuario fazerLogin(Scanner scanner) {
        Usuario usuarioLogado = null;

        while (usuarioLogado == null) {
            System.out.println("*** LOGIN ***");
            System.out.print("Login: ");
            String login = scanner.next();
            System.out.print("Senha: ");
            String senha = scanner.next();

            usuarioLogado = UsuarioRepository.autenticar(login, senha);

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
}
