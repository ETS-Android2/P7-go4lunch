package com.pierre44.go4lunch.models;

import com.google.gson.annotations.Expose;
import com.pierre44.go4lunch.repository.PlaceDetails;

/**
 * Created by pmeignen on 18/11/2021.
 */
public class PlaceDetailsResult {

    @Expose
    private PlaceDetails placeDetails;

    public PlaceDetails getPlaceDetails() {
        return placeDetails;
    }
}
