package com.muratcan.yeldan.couriertrackingapplication.service.strategy;

import com.muratcan.yeldan.couriertrackingapplication.model.GeoPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HaversineDistanceStrategyTest {

    @InjectMocks
    private HaversineDistanceStrategy distanceStrategy;

    @Test
    void calculateDistance_ShouldReturnCorrectDistance() {
        GeoPoint p1 = new GeoPoint(40.9923307, 29.1244229);
        GeoPoint p2 = new GeoPoint(40.9923307, 29.1244229);

        double distance = distanceStrategy.calculateDistance(p1, p2);

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void calculateDistance_ShouldReturnApproximateDistance() {
        GeoPoint p1 = new GeoPoint(40.9923307, 29.1244229);
        GeoPoint p2 = new GeoPoint(40.986106, 29.1161293);

        double distance = distanceStrategy.calculateDistance(p1, p2);

        assertEquals(0.98, distance, 0.05);
    }
}
