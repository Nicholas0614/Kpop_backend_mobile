package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.KpopGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KpopGroupRepository extends JpaRepository<KpopGroup, Integer> {
    List<KpopGroup> findByNameContainingIgnoreCase(String name);
}