package org.homeservice;

import jakarta.persistence.EntityManager;
import org.homeservice.entity.Person;
import org.homeservice.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
    HibernateUtil.createEntityManager();


    }
}