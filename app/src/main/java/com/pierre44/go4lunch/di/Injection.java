package com.pierre44.go4lunch.di;

import com.pierre44.go4lunch.repository.PlaceApi;
import com.pierre44.go4lunch.repository.PlaceService;
import com.pierre44.go4lunch.repository.RestaurantDataRepository;
import com.pierre44.go4lunch.repository.WorkmateDataRepository;

/**
 * Created by pmeignen on 02/11/2021.
 */
public class Injection {
    private Injection() {}

    private static RestaurantDataRepository provideRestaurantDataRepository() {
        PlaceApi placeApi = PlaceService.createService(PlaceApi.class);
        return new RestaurantDataRepository(placeApi);
    }

    private static WorkmateDataRepository provideWorkmateDataRepository() {
        return new WorkmateDataRepository();
    }


    public static ViewModelFactory provideViewModelFactory() {
        RestaurantDataRepository restaurantDataRepository = provideRestaurantDataRepository();
        WorkmateDataRepository workmateDataRepository = provideWorkmateDataRepository();

        return new ViewModelFactory(restaurantDataRepository,workmateDataRepository);
    }
}
