package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.KpopGroup;
import com.kpop.kpopbackend.repository.KpopGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KpopGroupService {

    private final KpopGroupRepository repository;

    public KpopGroupService(KpopGroupRepository repository) {
        this.repository = repository;
    }

    public List<KpopGroup> getAllGroups() {
        return repository.findAll();
    }

    public KpopGroup getGroupById(int id) {
        return repository.findById(id).orElse(null);
    }

    public KpopGroup addGroup(KpopGroup group) {
        return repository.save(group);
    }

    public KpopGroup updateGroup(int id, KpopGroup group) {
        KpopGroup existing = repository.findById(id).orElse(null);

        if (existing == null) return null;

        existing.setName(group.getName());
        existing.setCompany(group.getCompany());
        existing.setDebutDate(group.getDebutDate());
        existing.setImage(group.getImage());
        existing.setDescription(group.getDescription());

        return repository.save(existing);
    }

    public boolean deleteGroup(int id) {
        if (!repository.existsById(id)) return false;

        repository.deleteById(id);
        return true;
    }

    public List<KpopGroup> searchGroups(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
}