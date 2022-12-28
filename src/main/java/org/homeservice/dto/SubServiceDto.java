package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubServiceDto {
    private String name;
    private String description;
    private Double basePrice;
    private String serviceName;

    public SubServiceDto() {
    }

    public SubServiceDto(SubService subService) {
        this.name = subService.getName();
        this.description = subService.getDescription();
        this.basePrice = subService.getBasePrice();
        if (subService.getService() != null)
            this.serviceName = subService.getService().getName();
    }

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
