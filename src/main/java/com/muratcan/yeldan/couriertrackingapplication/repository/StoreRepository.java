package com.muratcan.yeldan.couriertrackingapplication.repository;

import com.muratcan.yeldan.couriertrackingapplication.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {
    Optional<Store> findByName(String name);
}
