package com.voyage.backend.service;

import com.voyage.backend.model.User;
import com.voyage.backend.model.Voyage;
import com.voyage.backend.repository.UserRepository;
import com.voyage.backend.repository.VoyageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class VoyageService {

    @Autowired
    private VoyageRepository voyageRepository;

    @Autowired
    private UserRepository userRepository;

    public Voyage addVoyage(Voyage voyage) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
                throw new RuntimeException("Aucun utilisateur authentifié.");
            }
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));
            voyage.setUser(user);
            // Initialisation des champs par défaut si nécessaire
            if (voyage.getCurrentParticipants() == null) {
                voyage.setCurrentParticipants(0);
            }
            if (voyage.getStatus() == null) {
                voyage.setStatus(Voyage.Status.PLANNED);
            }
            return voyageRepository.save(voyage);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du voyage: " + e.getMessage(), e);
        }
    }
}