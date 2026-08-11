package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Address;
import com.kpop.kpopbackend.services.AddressService;
import com.kpop.kpopbackend.services.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@CrossOrigin("*")
public class AddressController {

    private final AddressService service;
    private final CurrentUserService currentUserService;

    public AddressController(AddressService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAddresses(@PathVariable int userId, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot access another user's addresses");
        }

        List<Address> addresses = service.getAddressesByUser(userId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddress(@PathVariable int id, Authentication authentication) {
        if (!currentUserService.canAccessAddress(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot access this address");
        }

        Address address = service.getAddressById(id);

        if (address == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody Address address, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, address.getUserId())) {
            return ResponseEntity.status(403).body("You cannot add an address for another user");
        }

        return ResponseEntity.ok(service.addAddress(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id, @RequestBody Address address,
                                           Authentication authentication) {
        if (!currentUserService.canAccessAddress(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot modify this address");
        }

        Address updated = service.updateAddress(id, address);

        if (updated == null) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok(updated);
    }

    @PutMapping("/user/{userId}/default/{addressId}")
    public ResponseEntity<?> setDefaultAddress(@PathVariable int userId, @PathVariable int addressId,
                                               Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId) ||
                !currentUserService.canAccessAddress(authentication, addressId)) {
            return ResponseEntity.status(403).body("You cannot modify this address");
        }

        Address address = service.setDefaultAddress(userId, addressId);

        if (address == null) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id, Authentication authentication) {
        if (!currentUserService.canAccessAddress(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot delete this address");
        }

        if (!service.deleteAddress(id)) return ResponseEntity.badRequest().body("Address not found");

        return ResponseEntity.ok("Address deleted successfully");
    }
}