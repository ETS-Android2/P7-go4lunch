package com.pierre44.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.pierre44.go4lunch.models.json2pojo.NearbySearch;
import com.pierre44.go4lunch.models.json2pojo.NearbySearchResult;
import com.pierre44.go4lunch.models.json2pojo.PlaceDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pmeignen on 27/10/2021.
 */
public class RestaurantDataRepository {

    private static final String COLLECTION_NAME = "restaurant";
    private static volatile RestaurantDataRepository instance;
    private PlaceApi placeApi;

    public RestaurantDataRepository(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    // get nearby places
    public MutableLiveData<NearbySearchResult> getNearbyPlaces(String location, String type) {
        MutableLiveData<NearbySearch> restaurantsData = new MutableLiveData<>();
        placeApi.getNearbyPlaces(location,type).enqueue(new Callback<NearbySearch>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearch> call, @NonNull Response<NearbySearch> response) {
                if (response.isSuccessful()) {
                    restaurantsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearch> call, @NonNull Throwable t) {
                restaurantsData.setValue(null);
            }
        });
        return new MutableLiveData<>();
    }

    // get more nearby places with nextPageToken
    public MutableLiveData<NearbySearch> getMoreNearbyPlaces(String nextPageToken) {
        MutableLiveData<NearbySearch> restaurantsData = new MutableLiveData<>();
        placeApi.getMoreNearbyPlaces(nextPageToken).enqueue(new Callback<NearbySearch>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearch> call, @NonNull Response<NearbySearch> response) {
                if (response.isSuccessful()) {
                    restaurantsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearch> call, @NonNull Throwable t) {
                restaurantsData.setValue(null);
            }
        });
        return restaurantsData;
    }

    // get places details
    public MutableLiveData<PlaceDetails> getPlaceDetails(String placeId, String language) {
        MutableLiveData<PlaceDetails> placeDetails = new MutableLiveData<>();
        placeApi.getPlaceDetails(placeId, language).enqueue(new Callback<PlaceDetails>() {
            @Override
            public void onResponse(
                    @NonNull Call<PlaceDetails> call,
                    @NonNull Response<PlaceDetails> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        placeDetails.setValue(response.body().getResult());
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<PlaceDetails> call,
                    @NonNull Throwable t) {

            }
        });
        return placeDetails;
    }
}
