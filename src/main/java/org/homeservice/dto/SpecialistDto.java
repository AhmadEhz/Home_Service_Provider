package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Specialist;

@Getter
@Setter
public class SpecialistDto {
    private String firstName;
    private String lastName;
    private Double score;
    private byte[] avatar;

    public SpecialistDto() {
    }

    public SpecialistDto(Specialist specialist) {
        firstName = specialist.getFirstName();
        lastName = specialist.getLastName();
        score = specialist.getScore();
        avatar = specialist.getAvatar();
    }
}
