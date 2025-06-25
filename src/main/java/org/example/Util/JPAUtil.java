package org.example.Util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void fechar() {
        emf.close();
    }
}
