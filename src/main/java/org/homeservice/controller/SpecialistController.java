package org.homeservice.controller;

import org.homeservice.service.BidService;
import org.homeservice.service.SpecialistService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialistController {
    private SpecialistService specialistService;
    private BidService bidService;
}
