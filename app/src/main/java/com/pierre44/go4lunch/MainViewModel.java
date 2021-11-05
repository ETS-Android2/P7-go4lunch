package com.pierre44.go4lunch;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewbinding.ViewBinding;

import com.pierre44.go4lunch.models.Restaurant;
import com.pierre44.go4lunch.models.Workmate;
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

    public LiveData<Restaurant> getNearbyPlaces(String location) {
        return mRestaurantDataRepository.getNearbyPlaces(location);
    }
    // FIREBASE

    // WORKMATES
    public void createWorkmate(Workmate workmate) {
        createdWorkmateLiveData = mWorkmateDataRepository.createWorkmate();

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