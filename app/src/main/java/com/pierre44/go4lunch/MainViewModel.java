package com.pierre44.go4lunch;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewbinding.ViewBinding;

import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.models.json2pojo.NearbySearch;
import com.pierre44.go4lunch.models.json2pojo.NearbySearchResult;
import com.pierre44.go4lunch.models.json2pojo.PlaceDetails;
import com.pierre44.go4lunch.repository.RestaurantDataRepository;
import com.pierre44.go4lunch.repository.WorkmateDataRepository;

public class MainViewModel extends ViewModel implements ViewBinding {

    private RestaurantDataRepository mRestaurantDataRepository;
    private WorkmateDataRepository mWorkmateDataRepository;
    private LiveData<Workmate> createdWorkmateLiveData;

    // MainViewModel

    public MainViewModel(RestaurantDataRepository restaurantDataRepository, WorkmateDataRepository workmateDataRepository) {
        this.mRestaurantDataRepository = restaurantDataRepository;
        this.mWorkmateDataRepository = workmateDataRepository;

    }

    // RESTAURANTS

    public MutableLiveData<NearbySearchResult> getNearbyPlaces(String location, String type) {
        return mRestaurantDataRepository.getNearbyPlaces(location,type);
    }

    public LiveData<NearbySearch> getMoreNearbyPlaces(String pageToken) {
        return mRestaurantDataRepository.getMoreNearbyPlaces(pageToken);
    }

    public LiveData<PlaceDetails> getPlaceDetails(String placeId, String language) {
        return mRestaurantDataRepository.getPlaceDetails(placeId, language);
    }


    // FIREBASE

    // WORKMATES
    public void createWorkmate(Workmate workmate) {
        createdWorkmateLiveData = mWorkmateDataRepository.createWorkmate();
    }

    public void deleteWorkmate() {
        //mWorkmateDataRepository.deleteWorkmate();
    }

    public LiveData<Workmate> getCreatedWorkmateLiveData() {
        return createdWorkmateLiveData;
    }

    public LiveData<Workmate> getWorkmate(String iD) {
        return mWorkmateDataRepository.getWorkmate(iD);
    }

    @NonNull
    @Override
    public View getRoot() {
        View view = null;
        return view;
    }
}