package org.homeservice.entity.id;

import org.homeservice.entity.Service;
import org.homeservice.entity.Specialist;

import java.io.Serializable;

public class ServiceSpecialistId implements Serializable {
    private Service service;
    private Specialist specialist;
}
