package com.pierre44.go4lunch.repository;

import com.pierre44.go4lunch.models.ResultDistanceMatrix;
import com.pierre44.go4lunch.models.json2pojo.NearbySearchResult;
import com.pierre44.go4lunch.models.json2pojo.PlaceDetailsResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pmeignen on 18/11/2021.
 */
public interface PlaceApi {

    @GET("nearbysearch/json?rankby=distance&type=restaurant")
    Call<NearbySearchResult> getNearbyPlaces(
            @Query(value = "type", encoded = true) String type,
            @Query(value = "location", encoded = true) String location);

    @GET("nearbysearch/json")
    Call<NearbySearchResult> getMoreNearbyPlaces(
            @Query(value = "pagetoken", encoded = true) String nextPageToken);

    @GET("distancematrix/json")
        // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(
            @Query(value = "origins", encoded = true) String origins,
            @Query(value = "destinations", encoded = true) String destinations);

    @GET("details/json")
    Call<PlaceDetailsResult> getPlaceDetails(
            @Query(value = "place_id", encoded = true) String placeId,
            @Query(value = "language", encoded = true) String language);
}

