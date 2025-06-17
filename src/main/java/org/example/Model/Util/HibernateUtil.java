package org.example.Model.Util;



import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Falha na criação do SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}