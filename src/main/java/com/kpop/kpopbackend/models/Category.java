package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
}