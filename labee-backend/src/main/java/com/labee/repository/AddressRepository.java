package com.labee.repository;

import com.labee.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    List<Address> findByUserUserId(String userId);

    Optional<Address> findByUserUserIdAndIsDefaultTrue(String userId);
}