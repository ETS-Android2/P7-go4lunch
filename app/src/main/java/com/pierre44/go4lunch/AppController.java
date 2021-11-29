package com.pierre44.go4lunch;

import android.app.Application;
import android.location.Location;

import com.pierre44.go4lunch.utils.PreferenceHelper;

/**
 * Created by pmeignen on 01/12/2021.
 */
public class AppController extends Application {
    private static AppController instance;
    private Location currentLocation = null;
    private boolean settingsHaveChanged = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        PreferenceHelper.initPreferenceHelper(this);
    }

    public static AppController getInstance() {
        return instance;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    // Used when the user moves the camera on the map
    public String getLatLngString(Location currentLocation) {
        return currentLocation.getLatitude() + "," + currentLocation.getLongitude();
    }
}
