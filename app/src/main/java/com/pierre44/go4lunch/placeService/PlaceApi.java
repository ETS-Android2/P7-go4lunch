package com.pierre44.go4lunch.placeService;

import com.pierre44.go4lunch.models.PlacesPOJO;
import com.pierre44.go4lunch.models.ResultDistanceMatrix;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pmeignen on 18/11/2021.
 */
public interface PlaceApi {

    @GET("place/nearbysearch/json?")
    Call<PlacesPOJO.Root> doPlaces(
            @Query(value = "type", encoded = true) String type,
            @Query(value = "location", encoded = true) String location,
            @Query(value = "name", encoded = true) String name,
            @Query(value = "opennow", encoded = true) boolean opennow,
            @Query(value = "rankby", encoded = true) String rankby,
            @Query(value = "key", encoded = true) String key);


    @GET("distancematrix/json")
        // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(
            @Query("key") String key,
            @Query("origins") String origins,
            @Query("destinations") String destinations);
}

