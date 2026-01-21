package com.voyage.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "voyages")
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est requis")
    @Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "La description est requise")
    @Size(min = 5, max = 1000, message = "La description doit contenir entre 5 et 1000 caractères")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "La destination est requise")
    @Size(min = 2, max = 255, message = "La destination doit contenir entre 2 et 255 caractères")
    @Column(nullable = false)
    private String destination;

    @NotNull(message = "La date de départ est requise")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_departure", nullable = false)
    private LocalDate dateDeparture;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_return")
    private LocalDate dateReturn;

    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private Double price;

    @NotNull(message = "Le nombre maximum de participants est requis")
    @Min(value = 1, message = "Le nombre maximum de participants doit être au moins 1")
    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PLANNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Méthodes de cycle de vie JPA
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (currentParticipants == null) {
            currentParticipants = 0;
        }
        if (status == null) {
            status = Status.PLANNED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructeurs
    public Voyage() {}

    public Voyage(String title, String description, String destination, LocalDate dateDeparture, 
                  Double price, Integer maxParticipants, User user) {
        this.title = title;
        this.description = description;
        this.destination = destination;
        this.dateDeparture = dateDeparture;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.user = user;
        this.currentParticipants = 0;
        this.status = Status.PLANNED;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getDateDeparture() { return dateDeparture; }
    public void setDateDeparture(LocalDate dateDeparture) { this.dateDeparture = dateDeparture; }

    public LocalDate getDateReturn() { return dateReturn; }
    public void setDateReturn(LocalDate dateReturn) { this.dateReturn = dateReturn; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }

    public Integer getCurrentParticipants() { return currentParticipants; }
    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum Status {
        @JsonProperty("PLANNED")
        PLANNED,
        @JsonProperty("ONGOING")
        ONGOING,
        @JsonProperty("COMPLETED")
        COMPLETED,
        @JsonProperty("CANCELLED")
        CANCELLED
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", destination='" + destination + '\'' +
                ", dateDeparture=" + dateDeparture +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}