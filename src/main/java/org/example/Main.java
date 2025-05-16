package org.example;

import org.example.Service.AuthService;
import org.example.Service.SistemaService;
import org.example.entities.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Scanner scanner = new Scanner(System.in);

        SistemaService sistemaService = new SistemaService(session);

        // Realiza o login pelo método da classe SistemaService
        Usuario usuarioLogado = AuthService.fazerLogin(scanner);


        // Chama o menu principal passando o admin logado
        sistemaService.menuPrincipal(scanner, usuarioLogado);

        scanner.close();
        session.close();
        sessionFactory.close();
    }
}
