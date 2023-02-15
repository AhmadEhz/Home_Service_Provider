package org.homeservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubServiceDto {
    private Long id;
    private String name;
    private String description;
    private Double basePrice;
    private String serviceName;

    public SubServiceDto() {
    }

    public SubServiceDto(SubService subService) {
        id = subService.getId();
        name = subService.getName();
        description = subService.getDescription();
        basePrice = subService.getBasePrice();
        if (subService.getService() != null)
            serviceName = subService.getService().getName();
    }

    @JsonIgnore
    public SubService getSubService() {
        return new SubService(name, description, basePrice, new Service(serviceName));
    }

    public static List<SubServiceDto> convertToDto(List<SubService> subServices) {
        List<SubServiceDto> subServiceDtoList = new ArrayList<>();
        for (SubService s : subServices) {
            subServiceDtoList.add(new SubServiceDto(s));
        }
        return subServiceDtoList;
    }
}
