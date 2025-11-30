package com.muratcan.yeldan.couriertrackingapplication.mapper;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierStoreEntryResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourierStoreEntryMapper {

    public CourierStoreEntryResponseDto toDto(CourierStoreEntry courierStoreEntry) {
        if (courierStoreEntry == null) {
            return null;
        }
        return new CourierStoreEntryResponseDto(
                courierStoreEntry.getCourierId(),
                courierStoreEntry.getStoreName(),
                courierStoreEntry.getTimestamp(),
                courierStoreEntry.getLat(),
                courierStoreEntry.getLng()
        );
    }

    public CourierStoreEntry toEntity(CourierStoreEntryResponseDto dto) {
        if (dto == null) {
            return null;
        }
        return CourierStoreEntry.builder()
                .courierId(dto.courierId())
                .storeName(dto.storeName())
                .timestamp(dto.timestamp())
                .lat(dto.lat())
                .lng(dto.lng())
                .build();
    }

    public List<CourierStoreEntryResponseDto> toDtoList(List<CourierStoreEntry> courierStoreEntries) {
        if (courierStoreEntries == null) {
            return List.of();
        }
        return courierStoreEntries.stream().map(this::toDto).toList();
    }

    public List<CourierStoreEntry> toEntityList(List<CourierStoreEntryResponseDto> courierStoreEntryDtos) {
        if (courierStoreEntryDtos == null) {
            return List.of();
        }
        return courierStoreEntryDtos.stream().map(this::toEntity).toList();
    }
}
