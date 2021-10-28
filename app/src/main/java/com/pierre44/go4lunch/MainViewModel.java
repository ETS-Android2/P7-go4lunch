package com.pierre44.go4lunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pierre44.go4lunch.models.Restaurant;
import com.pierre44.go4lunch.models.Workmate;
import com.pierre44.go4lunch.repository.RestaurantDataRepository;
import com.pierre44.go4lunch.repository.WorkmateDataRepository;

public class MainViewModel extends ViewModel {

    private RestaurantDataRepository mRestaurantDataRepository;
    private WorkmateDataRepository mWorkmateDataRepository;
    private LiveData<Workmate> createdWorkmateLiveData;

    // MainViewModel

    public MainViewModel(RestaurantDataRepository restaurantDataRepository, WorkmateDataRepository workmateDataRepository) {
        mRestaurantDataRepository = restaurantDataRepository;
        mWorkmateDataRepository = workmateDataRepository;
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
        return null;
    }

}