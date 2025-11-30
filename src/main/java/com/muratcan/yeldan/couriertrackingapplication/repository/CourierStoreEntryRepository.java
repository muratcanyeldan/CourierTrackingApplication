package com.muratcan.yeldan.couriertrackingapplication.repository;

import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierStoreEntryRepository extends JpaRepository<CourierStoreEntry, Long>, JpaSpecificationExecutor<CourierStoreEntry> {
}
