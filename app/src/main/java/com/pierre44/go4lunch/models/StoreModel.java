package com.pierre44.go4lunch.models;

/**
 * Created by pmeignen on 23/11/2021.
 */
public class StoreModel {

    public String name, address, distance, duration;

    public StoreModel(String name, String address, String distance, String duration) {

        this.name = name;
        this.address = address;
        this.distance = distance;
        this.duration = duration;
    }

}
