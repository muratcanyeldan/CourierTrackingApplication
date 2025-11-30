package com.muratcan.yeldan.couriertrackingapplication.exception;

import java.util.UUID;

public class CourierNotFoundException extends RuntimeException {
    public CourierNotFoundException(UUID uuid) {
        super("Courier not found with UUID: " + uuid);
    }
}
