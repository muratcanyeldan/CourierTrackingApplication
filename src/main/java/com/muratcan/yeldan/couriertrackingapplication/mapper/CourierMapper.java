package com.muratcan.yeldan.couriertrackingapplication.mapper;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.Courier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourierMapper {

    public CourierResponseDto toDto(Courier courier) {
        if (courier == null) {
            return null;
        }
        return new CourierResponseDto(
                courier.getId(),
                courier.getTotalDistance(),
                courier.getLastLat(),
                courier.getLastLng(),
                courier.getCreateDate()
        );
    }

    public Courier toEntity(CourierResponseDto dto) {
        if (dto == null) {
            return null;
        }
        return Courier.builder()
                .id(dto.id())
                .totalDistance(dto.totalDistance())
                .lastLat(dto.lastLat())
                .lastLng(dto.lastLng())
                .createDate(dto.createDate())
                .build();
    }

    public List<CourierResponseDto> toDtoList(List<Courier> couriers) {
        if (couriers == null) {
            return List.of();
        }
        return couriers.stream().map(this::toDto).toList();
    }

    public List<Courier> toEntityList(List<CourierResponseDto> courierDtos) {
        if (courierDtos == null) {
            return List.of();
        }
        return courierDtos.stream().map(this::toEntity).toList();
    }
}
