package com.pierre44.go4lunch.di;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.pierre44.go4lunch.MainViewModel;
import com.pierre44.go4lunch.repository.RestaurantDataRepository;
import com.pierre44.go4lunch.repository.WorkmateDataRepository;

/**
 * Created by pmeignen on 02/11/2021.
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private RestaurantDataRepository restaurantDataRepository;
    private WorkmateDataRepository workmateDataRepository;

    public ViewModelFactory(RestaurantDataRepository restaurantDataRepository, WorkmateDataRepository workmateDataRepository) {
        this.restaurantDataRepository = restaurantDataRepository;
        this.workmateDataRepository = workmateDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(restaurantDataRepository, workmateDataRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class reference");
    }
}
