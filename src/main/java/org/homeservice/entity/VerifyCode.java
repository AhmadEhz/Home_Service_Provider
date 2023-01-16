package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class VerifyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotNull
    private String code;

    private boolean isVerified;

    public VerifyCode() {
    }

    public VerifyCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String verify) {
        this.code = verify;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
