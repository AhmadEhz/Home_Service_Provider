package org.homeservice.entity;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    SPECIALIST("ROLE_SPECIALIST"),
    CUSTOMER("ROLE_CUSTOMER");
    private final String value;

    Role(String roleValue) {
        this.value = roleValue;
    }

    public String getValue() {
        return value;
    }
}
