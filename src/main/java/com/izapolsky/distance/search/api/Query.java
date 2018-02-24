package com.izapolsky.distance.search.api;

public class Query {
    public final String latitude;
    public final String longitude;
    public final Order order = Order.asc;
    public final float distanceKm;

    public Query(String latitude, String longitude, float distanceKm) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
    }

    enum Order {
        asc
    }
}
