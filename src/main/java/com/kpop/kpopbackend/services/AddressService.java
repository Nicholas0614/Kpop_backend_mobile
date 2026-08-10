package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Address;
import com.kpop.kpopbackend.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> getAddressesByUser(int userId) {
        return repository.findByUserId(userId);
    }

    public Address getAddressById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Address addAddress(Address address) {
        if (address.isDefaultAddress()) removeCurrentDefault(address.getUserId());

        return repository.save(address);
    }

    public Address updateAddress(int id, Address address) {
        Address existing = repository.findById(id).orElse(null);

        if (existing == null) return null;

        if (address.isDefaultAddress()) removeCurrentDefault(existing.getUserId());

        existing.setLabel(address.getLabel());
        existing.setRecipientName(address.getRecipientName());
        existing.setPhone(address.getPhone());
        existing.setAddressLine1(address.getAddressLine1());
        existing.setAddressLine2(address.getAddressLine2());
        existing.setCity(address.getCity());
        existing.setState(address.getState());
        existing.setPostcode(address.getPostcode());
        existing.setCountry(address.getCountry());
        existing.setDefaultAddress(address.isDefaultAddress());

        return repository.save(existing);
    }

    public boolean deleteAddress(int id) {
        if (!repository.existsById(id)) return false;

        repository.deleteById(id);
        return true;
    }

    public Address setDefaultAddress(int userId, int addressId) {
        Address address = repository.findById(addressId).orElse(null);

        if (address == null || address.getUserId() != userId) return null;

        removeCurrentDefault(userId);

        address.setDefaultAddress(true);

        return repository.save(address);
    }

    private void removeCurrentDefault(int userId) {
        List<Address> addresses = repository.findByUserId(userId);

        for (Address address : addresses) {
            if (address.isDefaultAddress()) {
                address.setDefaultAddress(false);
                repository.save(address);
            }
        }
    }
}