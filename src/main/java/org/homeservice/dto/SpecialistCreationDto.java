package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Specialist;

@Getter
@Setter
public class SpecialistCreationDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private byte[] avatar;

    public Specialist getSpecialist() {
        return new Specialist(firstName, lastName, username, password, email, avatar);
    }
}
