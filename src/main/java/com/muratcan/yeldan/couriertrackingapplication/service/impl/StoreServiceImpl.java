package com.muratcan.yeldan.couriertrackingapplication.service.impl;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.StoreResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.exception.StoreNotFoundException;
import com.muratcan.yeldan.couriertrackingapplication.mapper.StoreMapper;
import com.muratcan.yeldan.couriertrackingapplication.repository.StoreRepository;
import com.muratcan.yeldan.couriertrackingapplication.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    @Override
    public List<StoreResponseDto> getAllStores() {
        return storeRepository.findAll().stream()
                .map(storeMapper::toDto)
                .toList();
    }

    @Override
    public StoreResponseDto getStoreByName(String name) {
        return storeRepository.findByName(name)
                .map(storeMapper::toDto)
                .orElseThrow(() -> new StoreNotFoundException(name));
    }
}
