package com.muratcan.yeldan.couriertrackingapplication.service.strategy;

import com.muratcan.yeldan.couriertrackingapplication.model.GeoPoint;

public interface DistanceStrategy {
    double calculateDistance(GeoPoint from, GeoPoint to);
}
