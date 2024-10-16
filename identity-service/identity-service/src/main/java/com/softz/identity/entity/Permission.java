package com.softz.identity.entity;

import org.hibernate.mapping.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "permission")
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true) 
    private String name;
    @Column(name = "description", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci") 
    private String description;
}
