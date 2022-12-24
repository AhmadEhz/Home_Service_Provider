package org.homeservice.controller;

import org.homeservice.service.BidService;
import org.homeservice.service.SpecialistService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialistController {
    private final SpecialistService specialistService;
    private final BidService bidService;

    public SpecialistController(SpecialistService specialistService, BidService bidService) {
        this.specialistService = specialistService;
        this.bidService = bidService;
    }
}
