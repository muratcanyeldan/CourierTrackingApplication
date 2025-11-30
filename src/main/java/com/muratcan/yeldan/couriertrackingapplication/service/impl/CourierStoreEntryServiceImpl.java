package com.muratcan.yeldan.couriertrackingapplication.service.impl;

import com.muratcan.yeldan.couriertrackingapplication.dto.CourierStoreEntryFilterDto;
import com.muratcan.yeldan.couriertrackingapplication.dto.response.CourierStoreEntryResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.CourierStoreEntry;
import com.muratcan.yeldan.couriertrackingapplication.mapper.CourierStoreEntryMapper;
import com.muratcan.yeldan.couriertrackingapplication.repository.CourierStoreEntryRepository;
import com.muratcan.yeldan.couriertrackingapplication.service.CourierStoreEntryService;
import com.muratcan.yeldan.couriertrackingapplication.specification.CourierStoreEntrySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierStoreEntryServiceImpl implements CourierStoreEntryService {

    private final CourierStoreEntryRepository courierStoreEntryRepository;
    private final CourierStoreEntryMapper courierStoreEntryMapper;
    private final CourierStoreEntrySpecification courierStoreEntrySpecification;


    @Override
    public List<CourierStoreEntryResponseDto> getCourierStoreEntries(UUID courierId, String storeName) {
        CourierStoreEntryFilterDto filterDto = new CourierStoreEntryFilterDto(courierId, storeName);
        List<CourierStoreEntry> loanList = courierStoreEntryRepository.findAll(courierStoreEntrySpecification.getFilter(filterDto));
        return courierStoreEntryMapper.toDtoList(loanList);
    }

    @Override
    @Transactional
    public void save(CourierStoreEntry courierStoreEntry) {
        courierStoreEntryRepository.save(courierStoreEntry);
    }
}
