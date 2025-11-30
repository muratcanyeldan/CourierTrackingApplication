package com.muratcan.yeldan.couriertrackingapplication.specification;

import com.muratcan.yeldan.couriertrackingapplication.dto.CourierStoreEntryFilterDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CourierStoreEntrySpecification extends BaseSpecification<CourierStoreEntry, CourierStoreEntryFilterDto> {

    private Specification<CourierStoreEntry> getCourierId(UUID courierId) {
        if (courierId == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("courierId").as(UUID.class), courierId));
    }

    private Specification<CourierStoreEntry> getStoreName(String storeName) {
        if (storeName == null) {
            return null;
        }
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("storeName").as(String.class), storeName));
    }

    @Override
    public Specification<CourierStoreEntry> getFilter(CourierStoreEntryFilterDto request) {
        return Specification.allOf(
                getCourierId(request.courierId()),
                getStoreName(request.storeName())
        );
    }
}
