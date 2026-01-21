package com.voyage.backend.repository;

import com.voyage.backend.model.User;
import com.voyage.backend.model.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Long> {
    
    List<Voyage> findByUser(User user);
    
    List<Voyage> findByDestinationContainingIgnoreCase(String destination);
    
    List<Voyage> findByDateDepartureBetween(LocalDate startDate, LocalDate endDate);
    
    List<Voyage> findByStatus(Voyage.Status status);
    
    List<Voyage> findByPriceLessThanEqual(Double price);
    
    @Query("SELECT v FROM Voyage v WHERE v.user.username = :username")
    List<Voyage> findByUsername(@Param("username") String username);
    
    @Query("SELECT v FROM Voyage v WHERE v.maxParticipants > v.currentParticipants")
    List<Voyage> findAvailableVoyages();
}