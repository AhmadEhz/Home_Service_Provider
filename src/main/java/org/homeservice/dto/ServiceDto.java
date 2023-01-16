package org.homeservice.dto;


import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServiceDto {
    private Long id;
    private String name;

    public ServiceDto() {
    }

    public ServiceDto(Service service) {
        id = service.getId();
        name = service.getName();
    }

    public static List<ServiceDto> convertToDto(List<Service> services) {
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        for (Service s : services)
            serviceDtoList.add(new ServiceDto(s));
        return serviceDtoList;
    }
}
