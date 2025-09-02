package com.coffeeteam.fitbyte.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users") // The index is not needed here as it's defined in your SQL script
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This now matches your SQL, allowing name to be null
    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String preference;

    // We explicitly tell JPA to use the "weightUnit" column name

    private String weightUnit;

    // We explicitly tell JPA to use the "heightUnit" column name
    private String heightUnit;

    private Integer weight;

    private Integer height;

    // We explicitly tell JPA to use the "imageUri" column name
    private String imageUri;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
