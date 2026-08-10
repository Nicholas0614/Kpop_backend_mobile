package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Address;
import com.kpop.kpopbackend.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@CrossOrigin("*")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public List<Address> getAddresses(@PathVariable int userId) {
        return service.getAddressesByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddress(@PathVariable int id) {
        Address address = service.getAddressById(id);

        if (address == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody Address address) {
        return ResponseEntity.ok(service.addAddress(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id, @RequestBody Address address) {
        Address updated = service.updateAddress(id, address);

        if (updated == null) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/user/{userId}/default/{addressId}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable int userId, @PathVariable int addressId) {
        Address address = service.setDefaultAddress(userId, addressId);

        if (address == null) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id) {
        if (!service.deleteAddress(id)) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok("Address deleted successfully");
    }
}