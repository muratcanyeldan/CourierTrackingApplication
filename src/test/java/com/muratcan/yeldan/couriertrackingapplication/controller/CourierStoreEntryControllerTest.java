package com.muratcan.yeldan.couriertrackingapplication.controller;

import com.muratcan.yeldan.couriertrackingapplication.service.CourierStoreEntryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourierStoreEntryController.class)
class CourierStoreEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourierStoreEntryService courierStoreEntryService;

    @Test
    void getStoreEntries_ShouldReturnOk_WhenParamsAreValid() throws Exception {
        UUID courierId = UUID.randomUUID();
        String storeName = "Migros";

        when(courierStoreEntryService.getCourierStoreEntries(courierId, storeName))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/courier-store-entry")
                        .param("courier-id", courierId.toString())
                        .param("store-name", storeName))
                .andExpect(status().isOk());
    }

    @Test
    void getStoreEntries_ShouldReturnOk_WhenParamsAreMissing() throws Exception {
        when(courierStoreEntryService.getCourierStoreEntries(any(), any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/courier-store-entry"))
                .andExpect(status().isOk());
    }
}
