package com.coffeeteam.fitbyte.profileManagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    private String name;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "preference")
    @Pattern(regexp = "CARDIO|WEIGHT", message = "Preference must be CARDIO or WEIGHT")
    private String preference;

    @Column(name = "weightunit")
    @Pattern(regexp = "KG|LBS", message = "Weight unit must be KG or LBS")
    private String weightUnit;

    @Column(name = "heightunit")
    @Pattern(regexp = "CM|INCH", message = "Height unit must be CM or INCH")
    private String heightUnit;

    @Column(name = "weight")
    @Min(value = 10, message = "Weight must be at least 10")
    @Max(value = 1000, message = "Weight must not exceed 1000")
    private Integer weight;

    @Column(name = "height")
    @Min(value = 3, message = "Height must be at least 3")
    @Max(value = 250, message = "Height must not exceed 250")
    private Integer height;

    @Column(name = "imageuri")
    private String imageUri;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}