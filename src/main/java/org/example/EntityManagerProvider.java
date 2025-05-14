package org.example;

import jakarta.persistence.*;

public class EntityManagerProvider {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("anonbookPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
