package org.homeservice.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class HibernateUtil {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;
    private static EntityManager entityManager;
    private static final ValidatorFactory VALIDATOR_FACTORY;

    private HibernateUtil() {
    }

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("default");
        VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    }

    public static EntityManager createEntityManager() {
        entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        return entityManager;
    }

    public static EntityManager getCurrentEntityManager() {
        return entityManager;
    }

    public static EntityManager getOrCreateEntityManager() {
        if (entityManager == null || !entityManager.isOpen())
            entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        return entityManager;
    }
    public static Validator getValidator() {
        return VALIDATOR_FACTORY.getValidator();
    }
}
