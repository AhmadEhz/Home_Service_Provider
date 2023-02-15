package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Customer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public CustomerDto () {
    }

    public CustomerDto(Customer customer) {
        id = customer.getId();
        firstName = customer.getFirstName();
        lastName  = customer.getLastName();
        username = customer.getUsername();
        email = customer.getEmail();
    }

    public static List<CustomerDto> convertToDto(List<Customer> customers) {
        List<CustomerDto> customerDtoList = new ArrayList<>(customers.size());
        for (Customer c : customers) {
            customerDtoList.add(new CustomerDto(c));
        }
        return customerDtoList;
    }

    @JsonIgnore
    public Customer getCustomer() {
        return new Customer(firstName, lastName, username, null, email);
    }
}
