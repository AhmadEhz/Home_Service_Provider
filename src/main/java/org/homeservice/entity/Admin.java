package org.homeservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;

@Entity
public class Admin extends Person {
    public Admin() {
    }

    public Admin(String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
    }

    @PrePersist
    void prePersist() {
        setRole(Role.ADMIN);
    }

}
