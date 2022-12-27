package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Customer;

@Getter
@Setter
public class CustomerCreationDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;

    public Customer getCustomer() {
        return new Customer(firstName,lastName,username,password,email);
    }
}
