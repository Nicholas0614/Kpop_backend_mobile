package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.KpopGroup;
import com.kpop.kpopbackend.services.KpopGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@CrossOrigin("*")
public class KpopGroupController {

    private final KpopGroupService service;

    public KpopGroupController(KpopGroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<KpopGroup> getAllGroups() {
        return service.getAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable int id) {
        KpopGroup group = service.getGroupById(id);

        if (group == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<KpopGroup> addGroup(@RequestBody KpopGroup group) {
        return ResponseEntity.ok(service.addGroup(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable int id, @RequestBody KpopGroup group) {
        KpopGroup updated = service.updateGroup(id, group);

        if (updated == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable int id) {
        if (!service.deleteGroup(id)) return ResponseEntity.badRequest().body("Group not found");

        return ResponseEntity.ok("Group deleted successfully");
    }

    @GetMapping("/search")
    public List<KpopGroup> searchGroups(@RequestParam String keyword) {
        return service.searchGroups(keyword);
    }
}