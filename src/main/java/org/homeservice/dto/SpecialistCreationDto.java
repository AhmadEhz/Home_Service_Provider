package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Specialist;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SpecialistCreationDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;

    public Specialist getSpecialist() {
        return new Specialist(firstName, lastName, username, password, email);
    }
}
