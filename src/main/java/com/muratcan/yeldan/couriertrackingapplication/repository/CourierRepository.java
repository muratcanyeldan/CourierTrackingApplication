package com.muratcan.yeldan.couriertrackingapplication.repository;

import com.muratcan.yeldan.couriertrackingapplication.entity.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourierRepository extends JpaRepository<Courier, UUID> {
}
