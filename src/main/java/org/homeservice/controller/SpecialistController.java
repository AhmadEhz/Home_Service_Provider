package org.homeservice.controller;

import org.homeservice.dto.CreationBidDto;
import org.homeservice.entity.Specialist;
import org.homeservice.service.BidService;
import org.homeservice.service.SpecialistService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/specialist")
public class SpecialistController {
    private final SpecialistService specialistService;
    private final BidService bidService;

    public SpecialistController(SpecialistService specialistService, BidService bidService) {
        this.specialistService = specialistService;
        this.bidService = bidService;
    }

    @PostMapping("/save")
    void save(@RequestBody Specialist specialist) {
        specialistService.save(specialist);
    }

    @PutMapping("/change-password")
    void changePassword(Map<String, String> map) {
        specialistService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PutMapping("/add-avatar/{id}")
    void addAvatar(@PathVariable Long id, @RequestBody MultipartFile avatar) {
        specialistService.addAvatar(id, avatar);
    }

    @PutMapping ("/set-bid")
    void saveBid(@RequestBody CreationBidDto creationBidDTO) {
        bidService.save(creationBidDTO.getBid(), creationBidDTO.getOrderId(), creationBidDTO.getSpecialistId());
    }
}
