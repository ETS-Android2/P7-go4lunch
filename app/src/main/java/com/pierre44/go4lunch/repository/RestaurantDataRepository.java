package com.pierre44.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pierre44.go4lunch.models.Restaurant;

/**
 * Created by pmeignen on 27/10/2021.
 */
public class RestaurantDataRepository {

    private static final String COLLECTION_NAME = "restaurant";

    private static volatile RestaurantDataRepository instance;

    public RestaurantDataRepository() {

    }

    // get nearby places
    public LiveData<Restaurant> getNearbyPlaces(String location) {

        // implement request for Places

        return new MutableLiveData<>();
    }

    // get places details ...



}
