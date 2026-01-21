package com.voyage.backend.controller;

import com.voyage.backend.model.Voyage;
import java.util.Optional;

import com.voyage.backend.service.VoyageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.voyage.backend.repository.VoyageRepository;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@RestController
@RequestMapping("/api/voyages")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class VoyageController {

    @Autowired
    private VoyageService voyageService;
    
    @Autowired
    private VoyageRepository voyageRepository;

    @GetMapping
    public List<Voyage> getAllVoyages() {
        return voyageRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addVoyage(@Valid @RequestBody Voyage voyage) {
        try {
            Voyage savedVoyage = voyageService.addVoyage(voyage);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Voyage ajouté avec succès");
            response.put("voyage", savedVoyage);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erreur interne du serveur");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoyage(@PathVariable Long id) {
        voyageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
public ResponseEntity<Voyage> updateVoyage(@PathVariable Long id, @RequestBody Voyage updatedVoyage) {
    Voyage voyage = voyageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Voyage not found"));

    voyage.setTitle(updatedVoyage.getTitle());
    voyage.setDescription(updatedVoyage.getDescription());
    voyage.setDestination(updatedVoyage.getDestination());
    voyage.setDateDeparture(updatedVoyage.getDateDeparture());
    voyage.setDateReturn(updatedVoyage.getDateReturn());
    voyage.setPrice(updatedVoyage.getPrice());
    voyage.setMaxParticipants(updatedVoyage.getMaxParticipants());
    voyage.setCurrentParticipants(updatedVoyage.getCurrentParticipants());
    voyage.setStatus(updatedVoyage.getStatus());

    voyageRepository.save(voyage);
    return ResponseEntity.ok(voyage);
}


}