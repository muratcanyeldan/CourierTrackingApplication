package com.muratcan.yeldan.couriertrackingapplication.mapper;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.StoreResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StoreMapper {

    public StoreResponseDto toDto(Store store) {
        if (store == null) {
            return null;
        }
        return new StoreResponseDto(
                store.getName(),
                store.getLat(),
                store.getLng()
        );
    }

    public Store toEntity(StoreResponseDto dto) {
        if (dto == null) {
            return null;
        }
        return Store.builder()
                .name(dto.name())
                .lat(dto.lat())
                .lng(dto.lng())
                .build();
    }

    public List<StoreResponseDto> toDtoList(List<Store> stores) {
        if (stores == null) {
            return List.of();
        }
        return stores.stream().map(this::toDto).toList();
    }

    public List<Store> toEntityList(List<StoreResponseDto> storeDtos) {
        if (storeDtos == null) {
            return List.of();
        }
        return storeDtos.stream().map(this::toEntity).toList();
    }
}
