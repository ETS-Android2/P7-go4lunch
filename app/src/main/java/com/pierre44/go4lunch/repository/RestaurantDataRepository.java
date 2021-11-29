package com.pierre44.go4lunch.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.pierre44.go4lunch.models.json2pojo.NearbySearchResult;
import com.pierre44.go4lunch.models.json2pojo.NearbySearch;
import com.pierre44.go4lunch.models.json2pojo.PlaceDetails;
import com.pierre44.go4lunch.models.json2pojo.PlaceDetailsResult;

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
    public MutableLiveData<NearbySearch> getNearbyPlaces(String location, String type) {
        MutableLiveData<NearbySearchResult> restaurantsData = new MutableLiveData<>();
        placeApi.getNearbyPlaces(location,type).enqueue(new Callback<NearbySearchResult>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResult> call, @NonNull Response<NearbySearchResult> response) {
                if (response.isSuccessful()) {
                    restaurantsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResult> call, @NonNull Throwable t) {
                restaurantsData.setValue(null);
            }
        });
        return new MutableLiveData<>();
    }

    // get more nearby places with nextPageToken
    public MutableLiveData<NearbySearchResult> getMoreNearbyPlaces(String nextPageToken) {
        MutableLiveData<NearbySearchResult> restaurantsData = new MutableLiveData<>();
        placeApi.getMoreNearbyPlaces(nextPageToken).enqueue(new Callback<NearbySearchResult>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResult> call, @NonNull Response<NearbySearchResult> response) {
                if (response.isSuccessful()) {
                    restaurantsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResult> call, @NonNull Throwable t) {
                restaurantsData.setValue(null);
            }
        });
        return restaurantsData;
    }

    // get places details
    public MutableLiveData<PlaceDetails> getPlaceDetails(String placeId, String language) {
        MutableLiveData<PlaceDetails> placeDetails = new MutableLiveData<>();
        placeApi.getPlaceDetails(placeId, language).enqueue(new Callback<PlaceDetailsResult>() {
            @Override
            public void onResponse(
                    @NonNull Call<PlaceDetailsResult> call,
                    @NonNull Response<PlaceDetailsResult> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        placeDetails.setValue(response.body().getResult());
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<PlaceDetailsResult> call,
                    @NonNull Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
        return placeDetails;
    }

    public MutableLiveData<NearbySearchResult> getNearbyPlaces(String location) {
        return null;
    }
}
