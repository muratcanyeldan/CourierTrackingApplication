package com.muratcan.yeldan.couriertrackingapplication.service.impl;

import com.muratcan.yeldan.couriertrackingapplication.dto.response.StoreResponseDto;
import com.muratcan.yeldan.couriertrackingapplication.entity.Store;
import com.muratcan.yeldan.couriertrackingapplication.exception.StoreNotFoundException;
import com.muratcan.yeldan.couriertrackingapplication.mapper.StoreMapper;
import com.muratcan.yeldan.couriertrackingapplication.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @InjectMocks
    private StoreServiceImpl storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @Test
    void getStoreByName_ShouldReturnStore_WhenExists() {
        String name = "Migros";
        Store store = new Store();
        store.setName(name);

        when(storeRepository.findByName(name)).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(new StoreResponseDto(name, 40.0, 29.0));

        StoreResponseDto result = storeService.getStoreByName(name);

        assertNotNull(result);
    }

    @Test
    void getStoreByName_ShouldThrowException_WhenDoesNotExist() {
        String name = "Unknown";
        when(storeRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> storeService.getStoreByName(name));
    }
}
