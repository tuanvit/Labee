package com.labee.service;

import com.labee.dto.request.AddressRequest;
import com.labee.dto.response.AddressResponse;
import com.labee.exception.BadRequestException;
import com.labee.exception.ResourceNotFoundException;
import com.labee.model.entity.Address;
import com.labee.model.entity.User;
import com.labee.repository.AddressRepository;
import com.labee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    public AddressResponse createAddress(String userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // If this is set as default, unset other default addresses
        if (request.getIsDefault()) {
            addressRepository.findByUser(user).stream()
                    .filter(Address::getIsDefault)
                    .forEach(address -> {
                        address.setIsDefault(false);
                        addressRepository.save(address);
                    });
        }

        // Create address (addressId will be auto-generated)
        Address address = Address.builder()
                .user(user)
                .recipientName(request.getRecipientName())
                .phoneNumber(request.getPhoneNumber())
                .detailAddress(request.getDetailAddress())
                .ward(request.getWard())
                .district(request.getDistrict())
                .province(request.getProvince())
                .isDefault(request.getIsDefault())
                .build();

        address = addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Address> addresses = addressRepository.findByUser(user);

        return addresses.stream()
                .map(this::mapToAddressResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse updateAddress(String userId, String addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Verify ownership
        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to address");
        }

        // If this is set as default, unset other default addresses
        if (request.getIsDefault() && !address.getIsDefault()) {
            addressRepository.findByUser(address.getUser()).stream()
                    .filter(Address::getIsDefault)
                    .forEach(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
        }

        address.setRecipientName(request.getRecipientName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setDetailAddress(request.getDetailAddress());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setProvince(request.getProvince());
        address.setIsDefault(request.getIsDefault());

        addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    @Transactional
    public void deleteAddress(String userId, String addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Verify ownership
        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to address");
        }

        addressRepository.delete(address);
    }

    @Transactional
    public AddressResponse setDefaultAddress(String userId, String addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        // Verify ownership
        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to address");
        }

        // Unset other default addresses
        addressRepository.findByUser(address.getUser()).stream()
                .filter(Address::getIsDefault)
                .forEach(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });

        address.setIsDefault(true);
        addressRepository.save(address);

        return mapToAddressResponse(address);
    }

    private AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .recipientName(address.getRecipientName())
                .phoneNumber(address.getPhoneNumber())
                .detailAddress(address.getDetailAddress())
                .ward(address.getWard())
                .district(address.getDistrict())
                .province(address.getProvince())
                .isDefault(address.getIsDefault())
                .build();
    }
}
