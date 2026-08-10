package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kpop_groups")
@Data
public class KpopGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String company;

    private String debutDate;

    private String image;

    private String description;
}