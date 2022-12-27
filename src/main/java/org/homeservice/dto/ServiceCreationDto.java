package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Service;

@Getter
@Setter
public class ServiceCreationDto {
    private String name;

    public Service getService() {
        return new Service(name);
    }
}
