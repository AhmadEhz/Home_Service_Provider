package org.homeservice.entity.id;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;

import java.io.Serializable;

public class SubServiceSpecialistId implements Serializable {
    private SubService subService;
    private Specialist specialist;
}
