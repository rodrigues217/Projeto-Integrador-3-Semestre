package org.example.View;

import org.example.Controller.UsuarioController;
import org.example.Controller.SistemaService;
import org.example.Model.UsuarioMODEL;
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
        UsuarioMODEL usuarioLogado = UsuarioController.fazerLogin(scanner);


        // Chama o menu principal passando o admin logado
        sistemaService.menuPrincipal(scanner, usuarioLogado);

        scanner.close();
        session.close();
        sessionFactory.close();
    }
}
