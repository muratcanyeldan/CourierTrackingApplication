package com.muratcan.yeldan.couriertrackingapplication.service.strategy;

import com.muratcan.yeldan.couriertrackingapplication.model.GeoPoint;
import org.springframework.stereotype.Component;

@Component
public class HaversineDistanceStrategy implements DistanceStrategy {

    private static final double EARTH_RADIUS_KM = 6371.0;

    @Override
    public double calculateDistance(GeoPoint from, GeoPoint to) {
        double latDistance = Math.toRadians(to.lat() - from.lat());
        double lngDistance = Math.toRadians(to.lng() - from.lng());

        double fromLat = Math.toRadians(from.lat());
        double toLat = Math.toRadians(to.lat());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(fromLat) * Math.cos(toLat)
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
