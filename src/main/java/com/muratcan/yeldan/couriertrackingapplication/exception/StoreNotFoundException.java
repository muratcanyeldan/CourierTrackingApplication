package com.muratcan.yeldan.couriertrackingapplication.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(String name) {
        super("Store not found with name: " + name);
    }
}
