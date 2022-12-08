package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column
    @NotNull
    private String lastName;

    @Column
    @NotNull
    @Pattern(regexp = "^{5,128}$",
            message = "Username must be between 8 and 128 characters")
    @Pattern(regexp = "^[A-Za-z0-9_.]$",
            message = "The username should only contains letters, numbers, '_' and '.'")
    private String username;

    @Column
    @NotNull
    @Pattern(regexp = "^{8,256}$",
            message = "The password must be between 8 and characters")
    @Pattern(regexp = "^[A-Za-z0-9._$%^&*#!@\\-/\\\\]$",
            message = "The password should only contain letters and numbers, '._$%^&*#!@\\' and '/'")
    private String password;

    protected Person() {
    }

    protected Person(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    protected Person(Long id, String firstName, String lastName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
