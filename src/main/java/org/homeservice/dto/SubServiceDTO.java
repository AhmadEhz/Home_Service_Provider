package org.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;

@Getter
@Setter
public class SubServiceDTO {
    private String name;
    private String description;
    private Double basePrice;
    private String serviceName;

    public SubServiceDTO() {
    }

    public SubServiceDTO(String name, String description, Double basePrice, String serviceName) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.serviceName = serviceName;
    }

    public SubService getSubService() {
        return new SubService(name, description, basePrice, new Service(serviceName));
    }

}
