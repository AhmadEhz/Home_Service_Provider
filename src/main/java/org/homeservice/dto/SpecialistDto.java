package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Specialist;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SpecialistDto {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Double score;
    private byte[] avatar;

    public SpecialistDto() {
    }

    public SpecialistDto(Specialist specialist) {
        firstName = specialist.getFirstName();
        lastName = specialist.getLastName();
        email = specialist.getEmail();
        username = specialist.getUsername();
        score = specialist.getScore();
        avatar = specialist.getAvatar();
    }

    public Specialist getSpecialist() {
        return new Specialist(firstName, lastName, username, null, email, avatar);
    }

    public static List<SpecialistDto> convertToDto(List<Specialist> specialists) {
        List<SpecialistDto> specialistDtoList = new ArrayList<>();
        for (Specialist s : specialists) {
            specialistDtoList.add(new SpecialistDto(s));
        }
        return specialistDtoList;
    }
}
