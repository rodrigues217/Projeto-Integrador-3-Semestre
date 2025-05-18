package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.example.Controller.SistemaService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Scanner scanner = new Scanner(System.in);
        SistemaService sistemaService = new SistemaService(session);
        sistemaService.menuPrincipal(scanner, sistemaService.login(scanner));
        scanner.close();
        session.close();
        sessionFactory.close();
    }
}