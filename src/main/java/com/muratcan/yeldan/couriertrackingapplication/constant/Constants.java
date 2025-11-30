package com.muratcan.yeldan.couriertrackingapplication.constant;

public class Constants {

    public static final String STORES_GEO_KEY = "stores:geo";
    public static final String COURIER_ENTRY_KEY = "courier:entry";
    public static final double STORE_RADIUS_METERS = 100.0;
    public static final long LOG_PERIOD_BETWEEN_COURIER_ENTRY_MINUTES = 1L;

    private Constants() {
        throw new IllegalStateException("Constants class");
    }
}
